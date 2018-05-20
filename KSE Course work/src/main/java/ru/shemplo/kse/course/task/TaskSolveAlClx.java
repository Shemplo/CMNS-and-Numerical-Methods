package ru.shemplo.kse.course.task;

import static ru.shemplo.kse.course.InputParams.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.shemplo.kse.course.Run;
import ru.shemplo.kse.course.system.Equation;
import ru.shemplo.kse.course.system.EquationSystem;
import ru.shemplo.kse.course.system.UniversalSystem;
import ru.shemplo.kse.course.system.solver.EquationSystemSolver;
import ru.shemplo.kse.course.system.solver.UniversalSolver;

public class TaskSolveAlClx implements WorkTask {

	@Override
	public List <Map <String, Double>> run () {
		List <Map <String, Double>> maps = new ArrayList <> ();
		for (int i = 35; i < 65; i++) {
			double T = 10 * i + 273;
			maps.add (singleStep (T, 0.01));
		}
		
		return maps;
	}

	@Override
	public Map <String, Double> singleStep (double T, double delta) {
		String [] agents = {"HCl", "AlCl", "AlCl2", "AlCl3", "H2"},
				  reactios = {"2HCl+2Al=2AlCl+H2", "2HCl+Al=AlCl2+H2", 
						  	  "6HCl+2Al=2AlCl3+3H2"};
		
		double [] coefs = new double [3],
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
			// 2 HCl + 2 Al = 2 AlCl + H2
			// Pe(HCl)^2 = K1 * Pe(AlCl)^2 * Pe(H2)
			v -> v [0] * v [0] - coefs [0] * v [1] * v [1] * v [4],
			
			// 2 HCl + Al = AlCl2 + H2
			// Pe(HCl) ^ 2 = K2 * Pe(AlCl2) * Pe(H2)
			v -> v [0] * v [0] - coefs [1] * v [2] * v [4],
			
			// 6 HCl + 2 Al = 2 AlCl3 + 3 H2
			// Pe(HCl)^6 = K3 * Pe(AlCl3)^2 * Pe(H2)^3
			v -> Math.pow (v [0], 6) - coefs [2] * v [3] * v [3] * Math.pow (v [4], 3),
			
			// G(Cl) = G(HCl) + G(AlCl) + 2 * G(AlCl2) + 3 * G(AlCl3) = 0
			// D(HCl) * (Pg(HCl) - Pe(HCl)) + D(AlCl) * (Pg(AlCl) - Pe(AlCl)) + 2 * D(AlCl2) 
			// ... * (Pg(AlCl2) - Pe(AlCl2)) + 3 * D(AlCl3) * (Pg(AlCl3) - Pe(AlCl3)) = 0
			v -> ds [0] * (press [0] - v [0]) + 2 * ds [4] * (press [4] - v [4]),
			
			// G(Cl) = G(HCl) + G(AlCl) + 2 * G(AlCl2) + 3 * G(AlCl3) = 0
			// D(HCl) * (Pg(HCl) - Pe(HCl)) + D(AlCl) * (Pg(AlCl) - Pe(AlCl)) + 2 * D(AlCl2)
			// ... * (Pg(AlCl2) - Pe(AlCl2)) + 3 * D(AlCl3) * (Pg(AlCl3) - Pe(AlCl3)) = 0
			v -> {
				double result = ds [0] * (press [0] - v [0]);
				for (int i = 1; i < 4; i++) {
					result += i * ds [i] * (press [i] - v [i]);
				}
				
				return result;
			}
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
			
			if (isAccurate) { break; }
		}
		
		Map <String, Double> map = new HashMap <> ();
		System.out.println ("T = " + T);
		map.put ("T", T);
		
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
		
		double V = (map.get ("G(AlCl)") + map.get ("G(AlCl2)") + map.get ("G(AlCl3)"))
				   * (getDoubleParam ("Al", "mu") / getDensity ("Al")) * Math.pow (10, 9);
		String key = "Ve(Al)";
		if (Run.DEBUG) {
			System.out.println (key + " = " + V);
		}
		
		map.put (key, V);
		return map;
	}

}
