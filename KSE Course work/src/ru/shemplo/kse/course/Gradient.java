package ru.shemplo.kse.course;

import ru.shemplo.kse.course.matrix.method.MatrixMethod;
import ru.shemplo.kse.course.system.Equation;
import ru.shemplo.kse.course.system.EquationSystem;

public class Gradient {

	public static double findDescend (Equation eq, double p, 
						 double initStep, final double PRECISION) {
		double [] argument = {p};
		double derivate = eq.derivative (0, argument),
				step = initStep, x = p, 
				value = eq.evaluate (argument);
		while (step > PRECISION) {
			double dx = x - step;
			if (derivate < 0) {
				dx = x + step;
			}
			
			argument [0] = dx;
			double val = eq.evaluate (argument);
			if (val < value) {
				value = val;
				x = dx;
				
				derivate = eq.derivative (0, argument);
			} else {
				step /= 2;
			}
		}
		
		return x;
	}
	
	public static double [] findGradient (EquationSystem system, 
							double [] input, MatrixMethod method) {
		int size = system.getSize ();
		double [] result = new double [size];
		double [][] matrix = new double [size][];
		
		for (int i = 0; i < size; i++) {
			Equation equation = system.getEquation (i);
			result [i] = -equation.evaluate (input);
			matrix [i] = equation.gradient (input);
		}
		
		return method.solve (matrix, result);
	}
	
}
