package ru.shemplo.kse.course.system.solver;

import ru.shemplo.kse.course.system.EquationSystem;

public interface EquationSystemSolver {

	public double [] solve (EquationSystem system) throws IllegalArgumentException;
	
	default
	public double majorValue (double [] vector) {
		double value = 0;
		for (int i = 0; i < vector.length; i++) {
			value = Math.max (value, vector [i]);
		}
		
		return value;
	}
	
}
