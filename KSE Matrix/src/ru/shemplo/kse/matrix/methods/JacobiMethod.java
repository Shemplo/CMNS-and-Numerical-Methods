package ru.shemplo.kse.matrix.methods;

import ru.shemplo.kse.matrix.MatrixMain;

/**
 * @see <a href="https://ru.wikipedia.org/wiki/Метод_Якоби">Метод Якоби</a>
 */

public class JacobiMethod extends AbsMatrixMethod {

    @Override
    public double[] solve(double[][] A, double[] b) {
        final int SIZE = A.length;
        double[] X = new double[SIZE];     // x_i^(k)
        double[] tempX = new double[SIZE]; // x_i^(k+1)
        double norm; // max_i |X[i] - tempX[i]|

        do {
            // x_i^(k+1) = 1/A_ii (b_i - \sigma_(i \ne j) a_ij * x_j^(k)
            System.arraycopy(b, 0, tempX, 0, SIZE); // Copy b_i's
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (i != j)
                        tempX[i] -= A[i][j] * X[j]; // Subtract a_ij * x_j^(k)
                }
                tempX[i] /= A[i][i]; // Divide by A_ii
            }

            // norm = max_i |X[i] - tempX[i]|
            norm = 0;
            for (int h = 0; h < SIZE; h++) {
                norm = Math.max(norm, Math.abs(X[h] - tempX[h])); // Find maximum value for norm
            }
            System.arraycopy(tempX, 0, X, 0, SIZE); // Replace x^(k) with x^(k+1)
        } while (norm > MatrixMain.ACCURACY);

        return X;
    }

}
