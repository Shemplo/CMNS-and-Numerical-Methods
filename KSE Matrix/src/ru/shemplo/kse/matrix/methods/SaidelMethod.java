package ru.shemplo.kse.matrix.methods;


import ru.shemplo.kse.matrix.MatrixMain;
import ru.shemplo.kse.matrix.MatrixUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaidelMethod extends AbsMatrixMethod {
	@Override
	public double [] solve (double [][] matrix, double [][] value) {
		if (!MatrixUtils.checkDominant(matrix)) {
			List<double [][]> dominated = MatrixUtils.makeDominant(matrix, value);
			if (dominated != null) {
				System.out.println("Matrix isn't diagonally dominant, but was successfully converted.");
				matrix = dominated.get(0);
				value = dominated.get(1);
			} else {
				throw new IllegalStateException("Matrix isn't diagonally dominant");
			}
		}

		final int n = matrix.length;
		int iteration = 0;
		double [] roots = new double [n];
		Arrays.fill(roots, 0);

		boolean converge = false;
		while (!converge && iteration++ < MatrixMain.MAX_ITERATIONS) {
			double [] currentRoots = roots.clone();

			for (int i = 0; i < n; i++) {
				double s1 = 0, s2 = 0;
				for (int j = 0; j < i; j++) s1 += matrix [i][j] * currentRoots [j];
				for (int j = i + 1; j < n; j++) s2 += matrix [i][j] * roots [j];

				currentRoots [i] = (value [i][0] - s1 - s2) / matrix [i][i];
			}

			double s = 0;

			//for (int i = 0; i < n; i++) s += (currentRoots [i] - roots [i]) * (currentRoots [i] - roots [i]);

			for (int i = 0; i < n; i++) {
				double part = 0;
				for (int j = 0; j < n; j++) {
					part += matrix[i][j] * currentRoots[j];
				}
				part -= value[i][0];
				s += part * part;
			}

			converge = Math.sqrt(s) < MatrixMain.ACCURACY;
			roots = currentRoots;
		}

		iteration--;
		System.out.println("~~ ITERATIONS: " + iteration);
		return roots;
	}

	public double [] solve (double [][] matrix, double [] value) {
		return solve(matrix, MatrixUtils.transpose(new double [][] {value}));
	}
}
