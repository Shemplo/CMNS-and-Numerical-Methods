package ru.shemplo.kse.matrix.methods;

public interface MatrixMethod {

	/**
	 * This methods finds the solution for system of linear equations.
	 * 
	 * @param matrix Left-hand equation values (linear equations)
	 * @param value Right-hand values of equation (free members)
	 * @return Array of roots of current system
	 * 
	 */
	public double [] solve (double [][] matrix, double [] value);
	
	/**
	 * This methods finds the solution for system of linear equations.
	 * 
	 * @param matrix Left-hand equation values (linear equations)
	 * @param value Right-hand values of equation in matrix representation (free members)
	 * @return Array of roots of current system
	 * 
	 */
	public double [] solve (double [][] matrix, double [][] value);
	
}
