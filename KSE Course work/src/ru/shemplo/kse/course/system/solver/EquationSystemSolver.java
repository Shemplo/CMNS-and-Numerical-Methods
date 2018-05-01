package ru.shemplo.kse.course.system.solver;

import ru.shemplo.kse.course.system.EquationSystem;

public interface EquationSystemSolver {

	public double [] solve (EquationSystem system) throws IllegalArgumentException;
	
}
