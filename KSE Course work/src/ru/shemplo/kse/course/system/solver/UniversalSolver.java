package ru.shemplo.kse.course.system.solver;

import java.util.Objects;

import ru.shemplo.kse.course.Run;
import ru.shemplo.kse.course.matrix.method.GaussMethod;
import ru.shemplo.kse.course.matrix.method.MatrixMethod;
import ru.shemplo.kse.course.system.Equation;
import ru.shemplo.kse.course.system.EquationSystem;

public class UniversalSolver implements EquationSystemSolver {

	private final MatrixMethod MATRIX_METHOD = new GaussMethod ();
	
	public double [] solve (EquationSystem system) throws IllegalArgumentException {
		if (Objects.isNull (system) || system.getSize () == 0) {
			String mes = "Given equation system is Null or empty";
			throw new IllegalArgumentException (mes);
		}
		
		int size = system.getSize ();
		double [] result = new double [size];
		for (int i = 0; i < size; i++) {
			result [i] = Run.RANDOM.nextDouble ();
		}
		
		for (int i = 0; i < size; i++) {
			double [] dx = findGradient (system, result);
			double min = localMin (system, dx, result);
			for (int j = 0; j < size; j++) {
				result [i] += min * dx [i];
			}
			
			if (majorValue (dx) < Run.PRECISION) {
				break; // Req. precision reached
			}
		}
		
		return result;
	}
	
	private double vectorMismatch (EquationSystem system, double [] vector) {
		double mis = 0;
		for (int i = 0; i < system.getSize (); i++) {
			double value = system.getEquation (i)
								 .evaluate (vector);
			mis += value * value;
		}
		
		return mis;
	}
	
	private double vectorMismatch (EquationSystem system, double [] vector, 
									double [] gradient, double coft) {
		double [] tmp = new double [system.getSize ()];
		for (int i = 0; i < system.getSize (); i++) {
			tmp [i] = vector [i] + gradient [i] * coft;
		}
		
		return vectorMismatch (system, tmp);
	}
	
	private double localMin (EquationSystem system, double [] input, double [] gradient) {
		double mis = vectorMismatch (system, input);
		mis = Math.min (mis, vectorMismatch (system, input, gradient, 1));
		
		double rad = 1, dr;
		do {
			rad *= 2;
			dr = vectorMismatch (system, input, gradient, rad);
			mis = Math.min (mis, dr);
		} while (dr <= mis);
		
		// TODO: ... 
		
		return 0;
	}
	
	private double [] findGradient (EquationSystem system, double [] input) {
		int size = system.getSize ();
		double [] result = new double [size];
		double [][] matrix = new double [size][];
		
		for (int i = 0; i < size; i++) {
			Equation equation = system.getEquation (i);
			result [i] = -equation.evaluate (input);
			matrix [i] = equation.gradient (input);
		}
		
		return MATRIX_METHOD.solve (matrix, result);
	}
	
}
