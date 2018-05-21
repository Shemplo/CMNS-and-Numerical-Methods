package ru.shemplo.kse.course.task;

import static ru.shemplo.kse.course.InputParams.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.shemplo.kse.course.InputParams;
import ru.shemplo.kse.course.Run;
import ru.shemplo.kse.course.system.Equation;
import ru.shemplo.kse.course.system.EquationSystem;
import ru.shemplo.kse.course.system.UniversalSystem;
import ru.shemplo.kse.course.system.solver.EquationSystemSolver;
import ru.shemplo.kse.course.system.solver.UniversalSolver;

public class TaskSolveAlGaN implements WorkTask {

	private static final String [] KEYS = {
		"G(AlCl3)", "G(GaCl)", "Vg(AlGaN)", "x"
	};
	
	@Override
	public String [] saveKeys () {
		return KEYS;
	}
	
	@Override
	public List <Map <String, Double>> run () {
		List <Map <String, Double>> maps = new ArrayList <> ();
		for (int i = 0; i < 31; i++) {
			InputParams.setPressure ("GaCl", (30 - i));
			InputParams.setPressure ("AlCl3", i);
			
			maps.add (singleStep (1100 + 273, 0.01));
		}
		
		return maps;
	}

	@Override
	public Map <String, Double> singleStep (double T, double delta) {
		String [] agents = {"HCl", "GaCl", "NH3", "AlCl3", "H2"},
				  reactios = {"AlCl3+NH3=AlN+3HCl", "GaCl+NH3=GaN+HCl+H2"};
		
		double [] coefs = new double [reactios.length],
				  press = new double [agents.length],
				  ds    = new double [agents.length];
		for (int i = 0; i < coefs.length; i++) {
			coefs [i] = getTempConstants (reactios [i], T);
		}
		for (int i = 0; i < agents.length; i++) {
			press [i] = getPressure (agents [i]);
			ds [i] = getDiffusion (agents [i], T);
		}
		
		Equation [] equations = {
			// AlCl3 + NH3 = AlN + 3 HCl
		    // Pe(AlCl3) * Pe(NH3) = K9 * x * Pe(HCl)^3
			v -> v [3] * v [2] - coefs [0] * v [5] * Math.pow (v [0], 3),
			
			// GaCl + NH3 = GaN + HCl + H2
	        // Pe(GaCl) * Pe(NH3) = K10 * (1 - x) * Pe(HCl) * Pe(H2)
			v -> v [1] * v [2] - coefs [1] * (1 - v [5]) * v [0] * v [4],
			
			// G(H) = G(HCl) + 2 * G(H2) + 3 * G(NH3) = 0
	        // D(HCl) * (Pg(HCl) - Pe(HCl)) + 2 * D(H2) * (Pg(H2) - Pe(H2)) + 3 * D(NH3) * (Pg(NH3) - Pe(NH3))
			v -> ds [0] * (press [0] - v [0]) + 2 * ds [4] * (press [4] - v [4])
					+ 3 * ds [2] * (press [2] - v [2]),
			
			// G(Cl) = 3 * G(AlCl3) + G(GaCl) + G(HCl) = 0
			// 3 * D(AlCl3) * (Pg(AlCl3) - Pe(AlCl3)) + D(GaCl) * (Pg(GaCl) - Pe(GaCl)) + D(HCl) * (Pg(HCl) - Pe(HCl)) = 0
			v -> 3 * ds [3] * (press [3] - v [3]) + ds [1] * (press [1] - v [1])
					+ ds [0] * (press [0] - v [0]),
			
			// G(Al) + G(Ga) = G(AlCl3) + G(GaCl) = G(NH3) = G(N)
			// D(AlCl3) * (Pg(AlCl3) - Pe(AlCl3)) + D(GaCl) * (Pg(GaCl) - Pe(GaCl)) = D(NH3) * (Pg(NH3) - Pe(NH3))
			v -> ds [3] * (press [3] - v [3]) + ds [1] * (press [1] - v [1])
					- ds [2] * (press [2] - v [2]),
					
			// G(AlCl3) = x * (G(AlCl3) + G(GaCl))
			// D(AlCl3) * (Pg(AlCl3) - Pe(AlCl3)) = x * (D(AlCl3) * (Pg(AlCl3) - Pe(AlCl3)) + D(GaCl) * (Pg(GaCl) - Pe(GaCl)))
			v -> ds [3] * (press [3] - v [3]) - v [5] * (ds [1] * (press [1] - v [1])
					+ ds [3] * (press [3] - v [3]))
		};
		
		EquationSystem system = new UniversalSystem (equations);
		EquationSystemSolver solver = new UniversalSolver ();
		double [] vector = null; // solution
		while (true) {
			vector = solver.solve (system);
			boolean isAccurate = true;
			for (int i = 0; i < vector.length; i++) {
				isAccurate &= vector [i] <= Run.ATMOSPHERE_PRESSURE + 1000
								&& vector [i] >= -1000;
			}
			
			isAccurate &= vector [5] >= 0 && vector [5] <= 1;
			if (isAccurate) { break; }
		}
		
		Map <String, Double> map = new HashMap <> ();
		if (Run.DEBUG) {
			System.out.println ("Pg(AlCl3) = " + getPressure ("AlCl3"));
			System.out.println ("x = " + vector [5]);
		}
		map.put ("Pg(AlCl3)", getPressure ("AlCl3"));
		map.put ("x", vector [5]);
		
		for (int i = 0; i < agents.length; i++) {
			String key = "Pe(" + agents [i] + ")";
			if (Run.DEBUG) {
				System.out.println (key + " = " + vector [i]);
			}
			
			map.put (key, vector [i]);
		}
		
		
		for (int i = 0; i < agents.length; i++) {
			double g = ds [i] * (press [i] - vector [i]) 
					   / (8314 * T * delta);
			String key = "G(" + agents [i] + ")";
			if (Run.DEBUG) {
				System.out.println (key + " = " + g);
			}
			
			map.put (key, g);
		}
		
		double V = (map.get ("G(AlCl3)") * (getDoubleParam ("Al", "mu") / getDensity ("Al")) 
					+ map.get ("G(GaCl)") * (getDoubleParam ("Ga", "mu") / getDensity ("Ga"))) 
					* Math.pow (10, 9);
		String key = "Vg(AlGaN)";
		if (Run.DEBUG) {
			System.out.println (key + " = " + V);
		}
		
		map.put (key, V);
		return map;
	}

}
