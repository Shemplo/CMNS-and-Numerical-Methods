package ru.shemplo.kse.course.system;

import ru.shemplo.kse.course.Run;

public interface Equation {

	public final double EPS = Run.PRECISION;
	
	public double evaluate (double... input);
	
	default
	public double derivative (int index, double [] input) {
		double value = evaluate (input);
		// Outraging vector
		input [index] += EPS;
		double result = (evaluate (input) - value) 
						/ EPS;
		// Turn back to calm
		input [index] -= EPS;
		
		return result;
	}
	
	default
	public double [] gradient (double [] input) {
		double [] resuls = new double [input.length];
		for (int i = 0; i < input.length; i++) {
			resuls [i] = derivative (i, input);
		}
		
		return resuls;
	}
	
}
