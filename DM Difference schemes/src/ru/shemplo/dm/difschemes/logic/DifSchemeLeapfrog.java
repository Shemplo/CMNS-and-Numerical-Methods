package ru.shemplo.dm.difschemes.logic;


public class DifSchemeLeapfrog extends AbsDifferenceScheme {

	@Override
	protected double [] doUnexistingStep (int step) {
		return null;
	}
	
}
