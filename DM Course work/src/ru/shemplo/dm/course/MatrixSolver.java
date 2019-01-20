package ru.shemplo.dm.course;

import static java.lang.Math.*;

public class MatrixSolver {
    
    public static double [] solve (double [][] matrix, double [] y) {
        double [][] copyM = new double [matrix.length][];
        double [] copyY = new double [y.length];
        final int n = matrix.length;
        
        System.arraycopy (y, 0, copyY, 0, y.length);
        
        for (int i = 0; i < matrix.length; i++) {
            copyM [i] = new double [matrix [i].length];
            System.arraycopy (matrix [i], 0, copyM [i], 0, 
                              matrix [i].length);
        }
        
        for (int i = 0; i < matrix.length - 1; i++) {
            double divider = copyM [i][i];
            
            for (int j = i; j < min (n, i + 3); j++) {
                copyM [i][j] /= divider;
            }
            copyY [i] /= divider;
            
            for (int j = i + 1; j < min (n, i + 3); j++) {
                double multiplier = copyM [j][i];
                
                for (int k = i; k < min (n, i + 3); k++) {
                    copyM [j][k] -= copyM [i][k] * multiplier;
                }
                copyY [j] -= copyY [i] * multiplier;
            }
        }
        
        copyY [n - 1] /= copyM [n - 1][n - 1];
        copyM [n - 1][n - 1] = 1;
        
        double [] result = new double [y.length];
        result [n - 1] = copyY [n - 1];
        
        for (int i = n - 1; i >= 0; i--) {
            result [i] = copyY [i];
            
            for (int j = i + 1; j < min (n, i + 3); j++) {
                result [i] -= result [j] * copyM [i][j];
            }
        }
        
        return result;
    }
    
}
