package ru.shemplo.dm.difschemes.logic;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsDifferenceScheme implements DifferenceScheme {
	
	private final Map <Integer, double []> LAYERS = new HashMap <> ();
	
	private final double [] TIME_BOUNDS = {0.0, 100.0};
	
	protected AbsDifferenceScheme (double [] zeroLayer) {
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
			
			for (int i = from + 1; i <= layer; i++) {
				LAYERS.put (i, doUnexistingStep (i));
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
	protected abstract double [] doUnexistingStep (int step);
	
}
