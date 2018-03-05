package ru.shemplo.kse.matrix.methods;

import static ru.shemplo.kse.matrix.MatrixMain.*;

import java.util.Arrays;

import ru.shemplo.kse.matrix.MatrixUtils;

public class ConjugateGradientMethod extends AbsMatrixMethod {
	
	@Override
	public double [] solve (double [][] matrix, double [] value) {
		if (!MatrixUtils.checkPositiveDefiniteness (matrix)) {
			String message = "Matrix is not positive definet";
			throw new IllegalStateException (message);
		}
		
		final double norm = MatrixUtils.scalar (value, value);
		double [] vector = new double [matrix.length];
		Arrays.fill (vector, 0.25);
		
		matrix = MatrixUtils.symmetrize (matrix);
		double [] sz = MatrixUtils.multiply (matrix, vector);
		double [] rk = new double [value.length],
					zk = new double [value.length];
		for (int i = 0; i < value.length; i++) {
			zk [i] = rk [i] = value [i] - sz [i];
		}
		
		final double acc = ACCURACY * ACCURACY;
		int iteration = 0;
		double spr1 = 0;
		
		do {
			iteration ++;
			
			double spz = 0, spr = 0;
			for (int i = 0; i < value.length; i++) {
				sz [i] = 0;
				for (int j = 0; j < value.length; j++) {
					sz [i] += matrix [i][j] * zk [j];
				}
				spz += sz [i] * zk [i];
				spr += rk [i] * rk [i];
			}
			
			double alpha = spr / spz;
			spr1 = 0;
			
			for (int i = 0; i < value.length; i++) {
				vector [i] += alpha * zk [i];
				rk [i] -= alpha * sz [i];
				spr1 += rk [i] * rk [i];
			}
			double beta = spr1 / spr;
			
			for (int i = 0; i < value.length; i++) {
				zk [i] = rk [i] + beta * zk [i];
			}
			
			System.out.println (spr1 / norm + " " + acc);
		} while (spr1 / norm > acc && iteration < MAX_ITERATIONS);
		
		System.out.println ("~~ ITERATIONS: " + iteration);
		return vector;
	}

}
