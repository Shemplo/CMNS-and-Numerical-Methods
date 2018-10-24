package ru.shemplo.dm.difschemes.logic;

import ru.shemplo.dm.difschemes.logic.DifferenceScheme.Scheme;

@Scheme (name = "Чехарда (leapfrog)")
public class DifSchemeLeapfrog extends AbsDifferenceScheme {

	public DifSchemeLeapfrog (double [] zeroLayer, 
			double u, double k, double dt, double dx) {
		super (zeroLayer, u, k, dt, dx);
	}

	@Override
	protected double [] doUnexistingStep (int step) {
		double [] zeroLayaer = getTimeLayer (0), profile = new double [zeroLayaer.length];
		double left = zeroLayaer [0], right = zeroLayaer [zeroLayaer.length - 1];
		profile [0] = left; profile [profile.length - 1] = right;
		
		if (step == 1) {
			for (int i = 0; i < profile.length - 1; i++) {
				profile [i] = zeroLayaer [i] * (1 - 2 * R)
							+ (R - S / 2) * zeroLayaer [i + 1]
							+ (R + S / 2) * zeroLayaer [i - 1];
			}
		} else if (step > 1) {			
			double [][] previous = {getTimeLayer (step - 2), getTimeLayer (step - 1)};
			
			for (int i = 1; i < profile.length - 1; i++) {
				profile [i] = previous [0][i] - previous [1][i] * 4 * R
							+ previous [1][i + 1] * (2 * R - S)
							+ previous [1][i - 1] * (2 * R + S);
			}
		}
		
		return profile;
	}
	
}
