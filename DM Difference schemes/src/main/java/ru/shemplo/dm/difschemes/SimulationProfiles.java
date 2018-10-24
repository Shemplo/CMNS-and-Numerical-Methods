package ru.shemplo.dm.difschemes;


public enum SimulationProfiles {
	
	STEP   ("Ступенька"),
	SPLASH ("Единичный импульс");
	
	public final String NAME;
	
	private SimulationProfiles (String name, double... profile) {
		this.NAME = name;
	}
	
	@Override
	public String toString () {
		return this.NAME;
	}
	
	public double [] getProfile (int points) {
		return new double [points];
	}
	
}
