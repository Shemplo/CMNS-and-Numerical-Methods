package ru.shemplo.kse.matrix.methods;

import ru.shemplo.kse.matrix.MatrixUtils;

public class CramerMethod extends AbsMatrixMethod {

	@Override
	public double [] solve (double [][] matrix, double [] value) {
		double [][] valueMatrix = new double [][] {value};
		valueMatrix = MatrixUtils.transpose (valueMatrix);
		return this.solve (matrix, valueMatrix);
	}
	
	@Override
	public double [] solve (double [] [] matrix, double [][] value) {
		double determinant = MatrixUtils.determinant (matrix);
		System.out.println ("~~ DETERMINANT: " + determinant);
		if (determinant == 0) {
			String message = "Determinant of given matrix is 0";
			throw new IllegalStateException (message);
		} else if (Double.isInfinite (determinant)) {
			String message = "Determinant of given matrix is INFINITY";
			throw new IllegalStateException (message);
		}

		int columns = 0;
		for (int i = 0; i < matrix.length; i++) {
			columns = Math.max (columns, matrix [i].length);
		}

		double [] roots = new double [columns];
		for (int i = 0; i < columns; i++) {
			double [][] clone = MatrixUtils.clone (matrix);
			for (int j = 0; j < matrix.length; j ++) {
				clone [j][i] = value [j][0];
			}

			double outrageDeterminant = MatrixUtils.determinant (clone);
			roots [i] = outrageDeterminant / determinant;
		}

		return roots;
	}

}
