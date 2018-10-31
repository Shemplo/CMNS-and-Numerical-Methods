package ru.shemplo.dm.difschemes;

import static java.lang.Math.*;
import java.util.function.Function;

public enum SimulationProfiles {
	
    STEP     ("Ступенька",           p -> (p >= 0.35 && p <= 0.65) ? 1.0 : 0.0),
	PEEK     ("Пик",                 p -> (p >= 0.475 && p <= 0.5) 
	                                    ? (p - 0.475) / 0.025 
	                                    : (p > 0.5 && p <= 0.525 ? (0.525 - p) / 0.025 : 0)),
	SINUSOID ("Синусоида",           p -> (p >= 0.25 && p <= 0.75) ? sin ((2 * PI) * ((p - 0.25) / 0.5)) : 0),
	HEARTBIT ("Единичное колебание", p -> (p >= 0.45 && p <= 0.55) 
	                                    ? (p < 0.5 
	                                            ? abs (abs ((p - 0.475) / 0.025) - 1) 
	                                            : abs ((p - 0.525) / 0.025) - 1) 
	                                    : 0d),
	LINE     ("Полюсы",              p -> p - 0.5),
	TRAPEZE  ("Трапеция",            p -> (p >= 0.25 && p <= 0.75) 
	                                    ? (p >= 0.25 && p <= 0.4 
	                                        ? abs ((p - 0.25) / 0.15) 
	                                        : (p >= 0.6 && p <= 0.75 ? abs (1 - (p - 0.6) / 0.15) : 1)) 
	                                    : 0d),
	ASCIATION ("Асциляция",          p -> 5 * sin (10 * p) * cos (15 * p)),
	SPLASH   ("Единичный импульс",   p -> (10 * p >= 3 * PI / 2 && 10 * p <= 5 * PI / 2) ? cos (10 * p) : 0);
	
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
