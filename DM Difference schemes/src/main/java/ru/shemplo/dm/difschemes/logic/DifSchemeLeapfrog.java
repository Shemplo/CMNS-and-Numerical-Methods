package ru.shemplo.dm.difschemes.logic;

import ru.shemplo.dm.difschemes.annot.Method;

@Method (name = "Чехрда (leapfrog)")
public class DifSchemeLeapfrog extends AbsDifferenceScheme {

	public DifSchemeLeapfrog (double [] zeroLayer) {
		super (zeroLayer);
	}

	@Override
	protected double [] doUnexistingStep (int step) {
		double [][] previous = {getTimeLayer (step - 2), getTimeLayer (step - 1)};
		
		double [] profile = new double [previous.length];
		for (int i = 1; i < profile.length - 1; i++) {
			profile [i] = previous [0][i] 
						- 0.5 * (previous [1][i + 1] - previous [1][i - 1]) 
						+ 2 * 0;
		}
		
		return profile;
	}
	
}
