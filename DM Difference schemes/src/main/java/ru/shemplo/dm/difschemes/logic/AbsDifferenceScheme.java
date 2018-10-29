package ru.shemplo.dm.difschemes.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsDifferenceScheme implements DifferenceScheme {
	
	protected final Map <Integer, double []> LAYERS = new HashMap <> ();
	
	protected final double U, K, dT, dX;
	protected final double S, R;
	protected final int ITERATIONS;
	
	protected AbsDifferenceScheme (double [] zeroLayer, int its, 
	        double u, double k, double dt, double dx) {
		this.U = u; this.K = k; this.dT = dt; this.dX = dx;
		this.S = U * dT / dX; this.R = K * dT / (dX * dX);
		this.LAYERS.put (0, zeroLayer);
		this.ITERATIONS = its;
	}
	
	private final int findPrevious (int layer) {
		while (!LAYERS.containsKey (layer) && layer >= 0) { layer --; }
		return layer;
	}
	
	protected final void run3DiagonalSolver (double [][] matrix, double [] answer) {
	    List <Double> as = new ArrayList <> (), bs = new ArrayList <> ();
	    double a = -matrix [0][1] / matrix [0][2],
	           b = matrix [0][3] / matrix [0][2];
	    as.add (a); bs.add (b);
	    for (int i = 1; i < matrix.length - 1; i++) {
	        double pA = a, pB = b, denom = (pA * matrix [i][0] + matrix [i][2]);
	        
	        b = (matrix [i][3] - pB * matrix [i][0]) / denom;
	        a = -matrix [i][1] / denom;
	        as.add (a); bs.add (b);
	    }
	    
	    int last = answer.length - 1;
	    answer [answer.length - 1] = (matrix [last][3] - b * matrix [last][0])
	                     / (matrix [last][2] + a * matrix [last][0]);
	    for (int i = last - 1; i >= 0; i--) {
	        answer [i] = as.get (i) * answer [i + 1] + bs.get (i);
	    }
	}
	
	@Override
	public final double [] getTimeLayer (int layer) {
		if (!LAYERS.containsKey (layer)) {
			int from = findPrevious (layer);
			
			double [] zeroLayaer = getTimeLayer (0);
	        double left = zeroLayaer [0], right = zeroLayaer [zeroLayaer.length - 1];
			for (int i = from + 1; i <= layer; i++) {
			    double [] profile = new double [zeroLayaer.length];
			    profile [profile.length - 1] = right;
			    profile [0] = left;
			    
			    doUnexistingStep (i, profile);
			    LAYERS.put (i, profile);
			}
		}
		
		return LAYERS.get (layer);
	}
	
	/**
	 * (guaranteed that previous layers are calculated)
	 * 
	 * @param step
	 * 
	 * @return
	 * 
	 */
	protected abstract void doUnexistingStep (int step, double [] profile);
	
}
