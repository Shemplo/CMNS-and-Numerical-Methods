package ru.shemplo.kse.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import ru.shemplo.kse.course.task.TaskSolveAlClx;
import ru.shemplo.kse.course.task.WorkTask;

public class Run {
	
	public static final double ATMOSPHERE_PRESSURE;
	public static final double PRECISION;
	public static final Random RANDOM;
	public static final double R;
	
	static { // Static constructor
		ATMOSPHERE_PRESSURE = 100000;
		RANDOM = new Random ();
		PRECISION = 1e-10;
		R = 8.314; // Gaze constant
	}
	
	public static void main (String... args) throws IOException {
		InputParams.loadParams ();
		
		WorkTask task = new TaskSolveAlClx ();
		task.run (new PrintWriter ("out.txt"));
	}
	
}
