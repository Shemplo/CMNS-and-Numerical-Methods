package ru.shemplo.kse.course.matrix.method;

import ru.shemplo.kse.course.matrix.Matrix;

public class GaussMethod extends AbsMatrixMethod {

	@Override
	public double [] solve (double [][] matrix, double [] value) {
		Matrix m = new Matrix (matrix);
		return m.gaussMethod (value);
		/*
		double [][] valueMatrix = new double [][] {value};
		valueMatrix = MatrixUtils.transpose (valueMatrix);
		return this.solve (matrix, valueMatrix);
		*/
	}
	
	@Override
	public double [] solve (double [][] matrix, double [][] value) {
		return null;
		/*
		List <double [][]> gauss = MatrixUtils.runGauss (matrix, value, true);
		double [][] resultVector = gauss.get (1);
		double [][] diagonal = gauss.get (0);

		double [] roots = new double [matrix.length];
		for (int i = matrix.length - 1; i >= 0; i--) {
			double summOfPrev = 0;
			for (int j = i + 1; j < roots.length; j ++) {
				summOfPrev += diagonal [i][j] * roots [j];
			}

			roots [i] = resultVector [i][0] / diagonal [i][i] - summOfPrev;
		}
		
		return roots;
		*/
	}

}
