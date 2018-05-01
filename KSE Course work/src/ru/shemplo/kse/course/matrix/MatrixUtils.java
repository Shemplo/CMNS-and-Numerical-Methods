package ru.shemplo.kse.course.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.shemplo.kse.course.matrix.MatrixGenerator.MatrixType;

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
	
	public static double [][] transpose (double [] vector) {
		return transpose (vector2Matrix (vector));
	}
	
	public static double [][] vector2Matrix (double [] vector) {
		return new double [][] {vector};
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
		int errors = 0;
		
		for (int i = 0; i < matrix.length; i++) {
			double summ = 0;
			for (int j = 0; j < matrix [i].length; j++) {
				summ += matrix [i][j] * roots [j];
			}
      
			if (Math.abs (result [i][0] - summ) > 0.00005) {
				if (errors < 5) {
					System.out.println ("?? In line " + i + " result: " + summ
											+ " (expected: " + result [i][0] + ")");
				}
				
				wasError = true;
				errors ++;
			}
		}
		
		if (errors > 5) {
			System.out.println ("?? And " + (errors - 5) 
									+ " more mistakes...");
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

	public static boolean checkDominant(double [][] matrix) {
		int n = matrix.length;

		for (int i = 0; i < n; i++) {
			double sum = 0;
			for (int j = 0; j < n; j++) {
				if (i == j) continue;;
				sum += Math.abs(matrix[i][j]);
			}

			if (Math.abs(matrix [i][i]) < sum) {
				System.out.println("In row " + i + ": " + matrix [i][i] + " < " + sum);
				return false;
			}
		}

		return true;
	}

	public static List <double [][]> makeDominant(double [][] matrix, double [][] result) {
		boolean [] visited = new boolean [matrix.length];
		int [] rows = new int [matrix.length];
		Arrays.fill(visited, false);

		return transformToDominant(matrix, result, 0, visited, rows);
	}

	private static List <double [][]> transformToDominant 
		(double [][] M, double [][] res, int r, boolean [] V, int [] R) {
		int n = M.length;
		if (r == n) {
			double [][] TM = new double [n][n];
			double [][] TRes = new double [n][1];

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++)
					TM [i][j] = M [R [i]][j];
				TRes [i][0] = res [R [i]][0];
			}

			List <double [][]> matrixes = new ArrayList<>();
			matrixes.add (TM); matrixes.add (TRes);

			return matrixes;
		}

		for (int i = 0; i < n; i++) {
			if (V[i]) continue;

			double sum = 0;
			for (int j = 0; j < n; j++)
				sum += Math.abs(M[i][j]);

			if (2 * Math.abs(M[i][r]) > sum) {
				V[i] = true;
				R[r] = i;

				List <double [][]> t = transformToDominant(M, res, r + 1, V, R);

				V[i] = false;
				return t;
			}
		}

		return null;
	}

}
