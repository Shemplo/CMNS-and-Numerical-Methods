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
			double min = 1;
			for (int j = 0; j < size; j++) {
				result [i] += min * dx [i];
			}
			
			// ...
		}
		
		return result;
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
