package ru.shemplo.kse.matrix.methods;

import ru.shemplo.kse.matrix.MatrixMain;
import ru.shemplo.kse.matrix.MatrixUtils;

import java.util.Arrays;
import java.util.List;

public class SaidelRelaxedMethod extends SaidelMethod {
    private static final double OMEGA = 1.d;//1.713d

    @Override
    public double[] solve(double[][] matrix, double[][] value) {
        if (!MatrixUtils.checkDominant(matrix)) {
            List<double[][]> dominated = MatrixUtils.makeDominant(matrix, value);
            if (dominated != null) {
                System.out.println("Matrix isn't diagonally dominant, but was successfully converted.");
                matrix = dominated.get(0);
                value = dominated.get(1);
            } else {
                //throw new IllegalStateException("Matrix isn't diagonally dominant");
            	System.out.println ("?? Matrix is not diagonally dominant, but we tried...");
            }
        }

        final int n = matrix.length;
        int iteration = 0;
        double[] roots = new double[n];
        Arrays.fill(roots, 0);

        boolean converge = false;
        while (!converge && iteration++ < MatrixMain.MAX_ITERATIONS) {
            double[] currentRoots = roots.clone();

            for (int i = 0; i < n; i++) {
                double s1 = 0, s2 = 0;
                for (int j = 0; j < i; j++) s1 += matrix[i][j] * currentRoots[j];
                for (int j = i + 1; j < n; j++) s2 += matrix[i][j] * roots[j];

                currentRoots[i] = (value[i][0] - s1 - s2) / matrix[i][i];
            }

            double s = 0;

            for (int i = 0; i < n; i++) {
                double part = 0;
                for (int j = 0; j < n; j++) {
                    part += matrix[i][j] * currentRoots[j];
                }
                part -= value[i][0];
                s += part * part;
            }

            converge = Math.sqrt(s) < MatrixMain.ACCURACY;

            for (int i = 0; i < n; i++) {
                currentRoots[i] = roots[i] + OMEGA * (currentRoots[i] - roots[i]);
            }
            roots = currentRoots;
        }
        iteration--;
        System.out.println("~~ ITERATIONS: " + iteration);
        return roots;
    }
}
