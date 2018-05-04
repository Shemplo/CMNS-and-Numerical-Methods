package ru.shemplo.kse.course.system;

import ru.shemplo.kse.course.Run;

public interface Equation {

	public double evaluate (double [] input);
	
	default
	public double [] gradient (double [] input) {
		double [] resuls = new double [input.length];
		final double EPS = Run.PRECISION * 1e4;
		double value = evaluate (input);
		
		for (int i = 0; i < input.length; i++) {
			// Outraging vector
			input [i] += EPS;
			resuls [i] = (evaluate (input) - value) 
						 / EPS;
			// Turn back to calm
			input [i] -= EPS;
		}
		
		return resuls;
	}
	
}
