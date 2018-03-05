package ru.shemplo.kse.matrix;

import java.util.Objects;
import java.util.Random;

public class MatrixGenerator {

	public static enum MatrixType {
		DIAGONAL_PRIORITY, RANDOM, GILBERT,
		IDENITY
	}
	
	//private static final int MULT = 4000000;
	private static final int MULT = 4000;
	
	public static double [][] generate (int size, MatrixType type) {
		Objects.requireNonNull (type, "Matrix type must be non NULL");
		if (type.equals (MatrixType.GILBERT)) {
			return generateGilbert (size);
		} else if (type.equals (MatrixType.IDENITY)) {
			return generateIdentity (size);
		}
		
		double [][] matrix = generateRandom (size);
		if (type.equals (MatrixType.DIAGONAL_PRIORITY)) {
			for (int i = 0; i < size; i++) {
				double summ = 0;
				for (int j = 0; j < size; j ++) {
					if (i == j) { continue; }
					summ += Math.abs (matrix [i][j]);
				}
				
				if (matrix [i][i] < summ) {
					double sign = Math.signum (matrix [i][i]);
					matrix [i][i] = sign == 0 ? summ : summ * sign;
				}
			}
		}
		
		return matrix;
	}
	
	private static double [][] generateIdentity (int size) {
		double [][] matrix = new double [size][size];
		for (int i = 0; i < size; i++) {
			matrix [i][i] = +1.0;
		}
		
		return matrix;
	}
	
	private static double [][] generateGilbert (int size) {
		double [][] matrix = new double [size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j ++) {
				matrix [i][j] = 1 / ((double) (1 + i + j));
			}
		}
		
		return matrix;
	}
	
	private static double [][] generateRandom (int size) {
		double [][] matrix = new double [size][];
		
		for (int i = 0; i < size; i++) {
			matrix [i] = vector (size);
		}
		
		return matrix;
	}
	
	public static double [] vector (int size) {
		double [] vector = new double [size];
		Random random = new Random ();
		
		for (int i = 0; i < size; i ++) {
			int base = random.nextInt (Integer.MAX_VALUE / (size * MULT));
			double sign = random.nextBoolean () ? 1 : -1;
			double mod = random.nextDouble ();
			
			vector [i] = sign * mod * ((double) base);
		}
		
		return vector;
	}
	
}
