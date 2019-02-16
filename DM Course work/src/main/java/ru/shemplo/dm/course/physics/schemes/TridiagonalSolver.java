package ru.shemplo.dm.course.physics.schemes;

/**
 * Created by Aleksei Latyshev on 03.11.2016.
 */

public class TridiagonalSolver {

    private static void IF_DEBUG(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Solves system.
     *
     * @param matrix -- not zero coefficients of matrix
     *               matrix[0].length and matrix[matrix.length - 1].length == 2
     *               others.length = 3
     * @param free   -- free column
     * @return solution of this system
     */
    public static double[] solve(double[][] matrix, double[] free) {
        IF_DEBUG(matrix[0].length == 2, "matrix[0].length = 2");
        IF_DEBUG(matrix[matrix.length - 1].length == 2, "matrix[last].length = 2");
        for (int i = 1; i < matrix.length - 1; i++) {
            IF_DEBUG(matrix[i].length == 3, "matrix[i].length = 3");
            for (int j = 0; j < 3; j++) {
                IF_DEBUG(!Double.isNaN(matrix[i][j]), "matrix[i][j] != NaN");
            }
        }
        IF_DEBUG(matrix.length == free.length, "matrix.length = free.length");
        for (int i = 0; i < free.length; i++) {
            IF_DEBUG(!Double.isNaN(free[i]), "free[i] != NaN");
        }
        double[] A = new double[matrix.length];
        A[0] = -1; // Shouldn't use this
        for (int i = 1; i < matrix.length; i++) {
            A[i] = matrix[i][0];
        }
        double[] C = new double[matrix.length];
        C[0] = matrix[0][0];
        for (int i = 1; i < matrix.length; i++) {
            C[i] = matrix[i][1];
        }
        double[] B = new double[matrix.length];
        B[0] = matrix[0][1];
        B[matrix.length - 1] = -1; // Shouldn't use this
        for (int i = 1; i < matrix.length - 1; i++) {
            B[i] = matrix[i][2];
        }

        // First step of algorithm: calculating C', free'
        double[] CSucc = new double[matrix.length];
        CSucc[0] = C[0];
        for (int i = 1; i < matrix.length; i++) {
            CSucc[i] = C[i] - A[i] / CSucc[i - 1] * B[i - 1];
        }
        double[] freeSucc = new double[matrix.length];
        freeSucc[0] = free[0];
        for (int i = 1; i < matrix.length; i++) {
            freeSucc[i] = free[i] - A[i] / CSucc[i - 1] * freeSucc[i - 1];
        }

        // Second step of algorithm: calculating x
        double[] x = new double[matrix.length];
        x[matrix.length - 1] = freeSucc[matrix.length - 1] / CSucc[matrix.length - 1];
        for (int i = matrix.length - 2; i >= 0; i--) {
            x[i] = (freeSucc[i] - B[i] * x[i + 1]) / CSucc[i];
        }
        return x;
    }
}
