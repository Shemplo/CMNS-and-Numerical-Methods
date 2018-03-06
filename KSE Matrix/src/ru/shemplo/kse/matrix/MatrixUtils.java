package ru.shemplo.kse.matrix;

import java.util.ArrayList;
import java.util.List;

import ru.shemplo.kse.matrix.MatrixGenerator.MatrixType;

public class MatrixUtils {

	public static void printMatrix (double [][] matrix) {
		for (int i = 0; i < matrix.length; i ++) {
			for (int j = 0; j < matrix [i].length; j ++) {
				System.out.print (String.format ("%+8.4f", matrix [i][j]));
				if (j < matrix [i].length - 1) {
					System.out.print (" ");
				}
			}

			System.out.println ();
		}
	}

	public static double [][] clone (double [][] matrix) {
		double [][] clone = new double [matrix.length][];
		for (int i = 0; i < clone.length; i++) {
			clone [i] = new double [matrix [i].length];
			System.arraycopy (matrix [i], 0, clone [i], 0, matrix [i].length);
		}

		return clone;
	}

	public static double [][] transpose (double [][] matrix) {
		int [] bounds = new int [] {matrix.length, 0};
		for (int i = 0; i < matrix.length; i++) {
			bounds [1] = Math.max (bounds [1], matrix [i].length);
		}

		double [][] clone = new double [bounds [1]][bounds [0]];
		for (int i = 0; i < clone.length; i ++) {
			for (int j = 0; j < clone [i].length; j ++) {
				if (j < matrix.length && i < matrix [j].length) {
					clone [i][j] = matrix [j][i];
				}
			}
		}

		return clone;
	}

	public static double [][] reverse (double [][] matrix) {
		int size = matrix.length;
		double [][] identity = MatrixGenerator.generate (size, MatrixType.IDENTITY);
		List <double [][]> matrixes = runGauss (matrix, identity, true);
		return matrixes.get (1);
	}

	public static List <double [][]> runGauss (double [][] oMatrix, double [][] oDocked, 
												boolean normalize) {
		double [][] matrix = clone (oMatrix), docked = clone (oDocked);
		for (int i = 0; i < matrix.length; i++) {
			if (matrix [i][i] == 0) {
				throw new IllegalStateException ("Zero value on diagonal");
			}
			double mul = matrix [i][i];

			if (normalize) {
				for (int j = 0; j < matrix [i].length; j++) {
					matrix [i][j] /= mul;
				}
				for (int j = 0; j < docked [i].length; j++) {
					docked [i][j] /= mul;
				}
			}

			for (int j = i + 1; j < matrix.length; j++) {
				if (matrix [j][i] == 0) { continue; }
				mul = matrix [j][i] / matrix [i][i];

				for (int k = 0; k < matrix [i].length; k++) {
					matrix [j][k] -= mul * matrix [i][k];
				}
				for (int k = 0; k < docked [i].length; k++) {
					docked [j][k] -= mul * docked [i][k];
				}
			}
		}

		List <double [][]> matrixes = new ArrayList <> ();
		matrixes.add (matrix); matrixes.add (docked);
		return matrixes;
	}

	public static double normOfMatrix (double [][] matrix) {
		double [] abses = new double [matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix [i].length; j++) {
				abses [i] += Math.abs (matrix [i][j]);
			}
		}

		double max = 0;
		for (int i = 0; i < abses.length; i++) {
			max = Math.max (max, abses [i]);
		}

		return max;
	}

	public static boolean checkCorrectness (double [][] matrix, double [][] result,
											double [] roots) {
		boolean wasError = false;
		for (int i = 0; i < matrix.length; i++) {
			double summ = 0;
			for (int j = 0; j < matrix [i].length; j++) {
				summ += matrix [i][j] * roots [j];
			}
      
			if (Math.abs (result [i][0] - summ) > 0.000001) {
				System.out.println ("In line " + i + " result: " + summ
									+ " (expected: " + result [i][0] + ")");
				wasError = true;
			}
		}

		return !wasError;
	}

	public static double [][] symmetrize (double [][] matrix) {
		double [][] transpose = transpose (matrix);
		double [][] symmetry = clone (matrix);

		for (int i = 0; i < symmetry.length; i++) {
			for (int j = 0; j < symmetry [i].length; j++) {
				symmetry [i][j] = (symmetry [i][j] + transpose [i][j]) / 2;
			}
		}

		return symmetry;
	}

	public static double [][] diagonalize (double [][] matrix) {
		int size = matrix.length;
		double [][] identity = MatrixGenerator.generate (size, MatrixType.IDENTITY);
		List <double [][]> matrixes = runGauss (matrix, identity, false);
		return matrixes.get (0);
	}

	public static double determinant (double [][] matrix) {
		double [][] diagonalize = diagonalize (matrix);
		double determinant = 1;

		for (int i = 0; i < matrix.length; i ++) {
			determinant *= diagonalize [i][i];
		}

		return determinant;
	}

	@Deprecated
	public static double [] solveByCramer (double [][] matrix, double [][] result) {
		double determinant = determinant (matrix);
		if (determinant == 0) {
			String message = "Determinant of given matrix is 0";
			throw new IllegalStateException (message);
		}

		int columns = 0;
		for (int i = 0; i < matrix.length; i++) {
			columns = Math.max (columns, matrix [i].length);
		}

		double [] roots = new double [columns];
		for (int i = 0; i < columns; i++) {
			double [][] clone = clone (matrix);
			for (int j = 0; j < matrix.length; j ++) {
				clone [j][i] = result [j][0];
			}

			double outrageDeterminant = determinant (clone);
			roots [i] = outrageDeterminant / determinant;
		}

		return roots;
	}
	
	public static double scalar (double [] a, double [] b) {
		int size = Math.min (a.length, b.length);
		double scalar = 0;
		
		for (int i = 0; i < size; i++) {
			scalar += a [i] * b [i];
		}
		
		return scalar;
	}
	
	public static double [] multiply (double [][] matrix, double [] vector) {
		final double [] result = new double [vector.length];
		for (int i = 0; i < vector.length; i++) {
			for (int j = 0; j < matrix [i].length; j++) {
				result [i] += matrix [i][j] * vector [j];
			}
		}
		
		return result;
	}
	
	public static double [][] multiply (double [][] a, double [][] b) {
		int boundsA = 0;
		for (int i = 0; i < a.length; i++) {
			boundsA = Math.max (boundsA, a [i].length);
		}
		
		double [][] result = new double [boundsA][b.length];
		for (int i = 0; i < a.length; i++) {
			for (int k = 0; k < b [i].length; k ++) {
				double summ = 0;
				for (int j = 0; j < a [i].length; j++) {
					summ += a [i][j] * b [j][k];
				}
				
				result [i][k] = summ;
			}
		}
		
		return result;
	}
	
	public static boolean checkPositiveDefiniteness (double [][] matrix) {
		final double [][] diagonalize = diagonalize (matrix);
		for (int i = 0; i < diagonalize.length; i ++) {
			if (diagonalize [i][i] < 0) { return false; }
		}
		
		return true;
	}

}
