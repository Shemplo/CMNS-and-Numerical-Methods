package ru.shemplo.dm.difschemes;

import static java.lang.Math.*;
import java.util.function.Function;

public enum SimulationProfiles {
	
	STEP   ("Ступенька",         p -> (p >= 0.35 && p <= 0.65) ? 1.0 : 0.0),
	SPLASH ("Единичный импульс", p -> (10 * p >= 3 * PI / 2 && 10 * p <= 5 * PI / 2) ? cos (10 * p) : 0);
	
	private final Function <Double, Double> DITRIBUTION;
	public final String NAME;
	
	private SimulationProfiles (String name, Function <Double, Double> distribution) {
		this.DITRIBUTION = distribution;
		this.NAME = name;
	}
	
	@Override
	public String toString () {
		return this.NAME;
	}
	
	public double [] getProfile (int points) {
	    points = Math.min (1000, points);
	    
		double [] dist = new double [points];
		for (double i = 0; i < points; i++) {
			dist [(int) i] = DITRIBUTION.apply (i / points);
		}
		
		return dist;
	}
	
}
