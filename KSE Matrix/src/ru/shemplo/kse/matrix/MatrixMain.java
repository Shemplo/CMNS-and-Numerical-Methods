package ru.shemplo.kse.matrix;

import ru.shemplo.kse.matrix.MatrixGenerator.MatrixType;
import ru.shemplo.kse.matrix.methods.ConjugateGradientMethod;
import ru.shemplo.kse.matrix.methods.GaussMethod;
import ru.shemplo.kse.matrix.methods.JacobiMethod;
import ru.shemplo.kse.matrix.methods.MatrixMethod;
import ru.shemplo.kse.matrix.methods.SaidelMethod;
import ru.shemplo.kse.matrix.methods.SaidelRelaxedMethod;

public class MatrixMain {

	public static final double ACCURACY = 1E-12;
	
	public static final int MAX_ITERATIONS = 1000;
	public static final int ROOTS_NUMBER = 250;
	
	private static final MatrixMethod [] METHODS;
	static {
		METHODS = new MatrixMethod [] {
			new GaussMethod (),
			new JacobiMethod(),
			new SaidelMethod (),
			new SaidelRelaxedMethod (),
			new ConjugateGradientMethod ()
		};
	}
	
	public static void main (String... args) {
		System.out.println (">> Iterative methods of solving system of linear equations");
		System.out.println (">> In this session found " + METHODS.length + " methods\n");
		
		System.out.println (".. Preparations before running methods");
		
		MatrixType type = MatrixType.DIAGONAL_PRIORITY;
		System.out.println (".. Generated type of matrix: " + type.name ());
		System.out.println (".. Generating original matrix (" + ROOTS_NUMBER 
								+ "x" + ROOTS_NUMBER + ")");
		final double [][] matrix = MatrixGenerator.generate (ROOTS_NUMBER, type);
		System.out.println (".. Generating right-hand vector\n");
		final double [] value = MatrixGenerator.vector (ROOTS_NUMBER);
		
		System.out.println (".. Calculating conditionality of matrix");
		try {
			System.out.println (".. Reversing matrix");
			final double [][] reversed = MatrixUtils.reverse (matrix);
			
			final double normOriginal = MatrixUtils.normOfMatrix (matrix);
			final String strOriginal = String.format ("%12.4f", normOriginal);
			System.out.println (".. Norm of original matrix : " + strOriginal);
			
			final double normReversed = MatrixUtils.normOfMatrix (reversed);
			final String strReversed = String.format ("%12.4f", normReversed);
			System.out.println (".. Norm of reversed matrix : " + strReversed);
			
			final double conditionality = normOriginal * normReversed;
			final String strConditionality = String.format ("%12.4f", conditionality);
			System.out.println (".. Conditionality of matrix: " + strConditionality);
			System.out.println ();
		} catch (IllegalStateException ise) {
			String message = "!! Error: " + ise.getMessage ();
			System.out.println (message);
			System.out.println ();
		}
		
		for (int i = 0; i < METHODS.length; i ++) {
			MatrixMethod method = METHODS [i];
			String methodName = method.getClass ().getSimpleName ();
			System.out.println (">> Method " + (i + 1) + ": " + methodName);
			
			try {
				final long start = System.currentTimeMillis ();
				double [] result = method.solve (matrix, value);
				final long end = System.currentTimeMillis ();
				
				if (result != null) {
					double [][] resultMatrix = new double [][] {result};
					System.out.print ("~~ ROOTS: ");
					MatrixUtils.printMatrix (resultMatrix);
					
					double [][] valueMatrix = new double [][] {value};
					valueMatrix = MatrixUtils.transpose (valueMatrix);
					final boolean correctness 
						= MatrixUtils.checkCorrectness (matrix, valueMatrix, result);
					System.out.println ("~~ Correctness of methods: " 
											+ (correctness ? "ok" : "fail"));
				} else {
					System.out.println ("!! Method is not implemented");
				}
				
				System.out.println ("%% Execution time: " + (end - start) + "ms");
				System.out.println ();
			} catch (IllegalStateException ise) {
				String message = "!! Method failed: " + ise.getMessage ();
				System.out.println (message);
				System.out.println ();
			}
		}
	}
	
}
