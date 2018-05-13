package ru.shemplo.kse.course;

import java.io.IOException;
import java.util.Random;

public class Run {
	
	public static final double ATMOSPHERE_PRESSURE;
	public static final double PRECISION;
	public static final Random RANDOM;
	
	static { // Static constructor
		ATMOSPHERE_PRESSURE = 100000;
		RANDOM = new Random ();
		PRECISION = 1e-10;
	}
	
	public static void main (String... args) throws IOException {
		InputParams.loadParams ();
	}
	
}
