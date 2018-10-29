package ru.shemplo.dm.difschemes.logic;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsDifferenceScheme implements DifferenceScheme {
	
	private final Map <Integer, double []> LAYERS = new HashMap <> ();
	
	private final double [] TIME_BOUNDS = {0.0, 100.0};
	
	protected final double U, K, dT, dX;
	protected final double S, R;
	
	protected AbsDifferenceScheme (double [] zeroLayer, 
			double u, double k, double dt, double dx) {
		this.U = u; this.K = k; this.dT = dt; this.dX = dx;
		this.S = U * dT / dX; this.R = K * dT / (dX * dX);
		this.LAYERS.put (0, zeroLayer);
	}
	
	private final int findPrevious (int layer) {
		while (!LAYERS.containsKey (layer) && layer >= 0) { layer --; }
		return layer;
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
			    
				LAYERS.put (i, doUnexistingStep (i, profile));
			}
		}
		
		return LAYERS.get (layer);
	}
	
	@Override
	public double [] getTimeBounds () {
		return TIME_BOUNDS;
	}
	
	/**
	 * (guaranteed that previous layers are calculated)
	 * 
	 * @param step
	 * 
	 * @return
	 * 
	 */
	protected abstract double [] doUnexistingStep (int step, double [] profile);
	
}
