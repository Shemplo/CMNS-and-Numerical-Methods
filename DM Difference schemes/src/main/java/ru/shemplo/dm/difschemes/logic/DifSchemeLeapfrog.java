package ru.shemplo.dm.difschemes.logic;

import ru.shemplo.dm.difschemes.logic.DifferenceScheme.Scheme;

@Scheme (name = "Чехарда (leapfrog)")
public class DifSchemeLeapfrog extends AbsDifferenceScheme {

	public DifSchemeLeapfrog (double [] zeroLayer, int its,
			double u, double k, double dt, double dx) {
		super (zeroLayer, its, u, k, dt, dx);
	}

	@Override
	protected void doUnexistingStep (int step, double [] profile) {
	    double [] zeroLayer = getTimeLayer (0);
	    
		if (step == 1) {
			for (int i = 1; i < profile.length - 1; i++) {
				profile [i] = zeroLayer [i] * (1 - 2 * R)
							+ (R - S / 2) * zeroLayer [i + 1]
							+ (R + S / 2) * zeroLayer [i - 1];
			}
		} else if (step > 1) {			
			double [][] previous = {getTimeLayer (step - 2), getTimeLayer (step - 1)};
			
			for (int i = 1; i < profile.length - 1; i++) {
				profile [i] = previous [0][i] - previous [1][i] * 4 * R
							+ previous [1][i + 1] * (2 * R - S)
							+ previous [1][i - 1] * (2 * R + S);
			}
		}
	}
	
}
