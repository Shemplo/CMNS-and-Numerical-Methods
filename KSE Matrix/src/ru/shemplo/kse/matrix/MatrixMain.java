package ru.shemplo.kse.matrix;

import ru.shemplo.kse.matrix.MatrixGenerator.MatrixType;

public class MatrixMain {

	private static final int ROOTS_NUMBER = 25;
	
	@SuppressWarnings ("unused")
	public static void main (String... args) {
		double [][] matrix = MatrixGenerator
								.generate (ROOTS_NUMBER, MatrixType.RANDOM);
		System.out.println (">> Original matrix:");
		//printMatrix (matrix);
		
		System.out.println (">> Clonned matrix:");
		double [][] clone = MatrixUtils.clone (matrix);
		//printMatrix (clone);
		
		System.out.println (">> Transponsed matrix:");
		double [][] transponse = MatrixUtils.transpose (matrix);
		//printMatrix (transponse);
		
		try {
			double [][] reverse = MatrixUtils.reverse (matrix);
			System.out.println (">> Reversed matrix:");
			//printMatrix (reverse);
			
			double originalNorm = MatrixUtils.normOfMatrix (matrix);
			System.out.println (">> Norm of original matrix : " 
								+ String.format ("%16.4f", originalNorm));
			
			double reversedNorm = MatrixUtils.normOfMatrix (reverse);
			System.out.println (">> Norm of reversed matrix : " 
								+ String.format ("%16.4f", reversedNorm));
			
			double cond = originalNorm * reversedNorm;
			System.out.println (">> Conditionality of matrix: " 
								+ String.format ("%16.4f", cond));
		} catch (IllegalStateException ise) {
			System.out.println ("Bad idea to reverse this matrix: " 
								+ ise.getMessage ());
		}
		
		double [] vector = MatrixGenerator.vector (ROOTS_NUMBER);
		double [][] result = new double [][] {vector};
		result = MatrixUtils.transpose (result);
		System.out.println (">> Result vector: ");
		//printMatrix (result);
		
		System.out.println (">> Gauss solution:");
		try {
			long start = System.currentTimeMillis (); // time //
			double [] rootsGauss = MatrixUtils.solveByGauss (matrix, result);
			long end = System.currentTimeMillis ();   // time //
			double [][] rootsGaussMatrix = new double [][] {rootsGauss};
			
			System.out.println (">> Roots:");
			printMatrix (rootsGaussMatrix);
			
			System.out.println (">> Roots (transposed):");
			//printMatrix (MatrixUtils.transpose (rootsGaussMatrix));
			
			System.out.println ("Execution time: " + (end - start) + "ms  for " + ROOTS_NUMBER + " equations");
			System.out.println ("Is correct: " + MatrixUtils.checkCorrectness (matrix, result, rootsGauss));
			System.out.println ();
			
			
		} catch (IllegalStateException ise) {
			System.out.println ("Bad idea to use Gauss for this matrix: " 
								+ ise.getMessage ());
		}
		
		try {
			System.out.println ("Symmetrized matrix: ");
			double [][] symmentry = MatrixUtils.symmetrize (matrix);
			//printMatrix (symmentry);
			
			System.out.println ("Diagonalized matrix: ");
			double [][] diagonalize = MatrixUtils.diagonalize (matrix);
			//printMatrix (diagonalize);
			
			double determinant = MatrixUtils.determinant (matrix);
			System.out.println ("Determinant: " + String.format ("%16.4f", determinant));
		} catch (IllegalStateException ise) {
			System.out.println ("Bad idea to diagonalize this matrix: " 
									+ ise.getMessage ());
		}
		
		System.out.println (">> Cramer solution:");
		try {
			long start = System.currentTimeMillis (); // time //
			double [] rootsCramer = MatrixUtils.solveByCramer (matrix, result);
			long end = System.currentTimeMillis ();   // time //
			double [][] rootsCramerMatrix = new double [][] {rootsCramer};
			
			System.out.println (">> Roots:");
			printMatrix (rootsCramerMatrix);
			
			System.out.println (">> Roots (transposed):");
			//printMatrix (MatrixUtils.transpose (rootsCramerMatrix));
			
			System.out.println ("Execution time: " + (end - start) + "ms  for " + ROOTS_NUMBER + " equations");
			System.out.println ("Is correct: " + MatrixUtils.checkCorrectness (matrix, result, rootsCramer));
			System.out.println ();
		} catch (IllegalStateException ise) {
			System.out.println ("Bad idea to use Cramer for this matrix: " 
								+ ise.getMessage ());
		}
	}
	
	public static void printMatrix (double [][] matrix) {
		for (int i = 0; i < matrix.length; i ++) {
			for (int j = 0; j < matrix [i].length; j ++) {
				//System.out.print (String.format ("%+16.4f", matrix [i][j]));
				System.out.print (String.format ("%+8.4f", matrix [i][j]));
				if (j < matrix [i].length - 1) {
					System.out.print (" ");
				}
			}
			
			System.out.println ();
		}
	}
	
}
