package ru.shemplo.kse.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import ru.shemplo.kse.course.task.TaskSolveAlClx;
import ru.shemplo.kse.course.task.TaskSolveGaCl;
import ru.shemplo.kse.course.task.WorkTask;

public class Run {
	
	public static final double ATMOSPHERE_PRESSURE;
	public static final double PRECISION;
	public static final Random RANDOM;
	public static final double R;
	
	public static final boolean DEBUG = true;
	
	static { // Static constructor
		ATMOSPHERE_PRESSURE = 100000;
		RANDOM = new Random ();
		PRECISION = 1e-10;
		R = 8.314; // Gaze constant
	}
	
	public static void main (String... args) throws IOException {
		InputParams.loadParams ();
		Locale l = new Locale ("EN");
		
		WorkTask task = new TaskSolveAlClx ();
		List <Map <String, Double>> maps = task.run ();
		PrintWriter pw = new PrintWriter ("plot.txt");
		for (int i = 0; i < maps.size (); i++) {
			Map <String, Double> map = maps.get (i);
			double V = Math.log (Math.abs (map.get ("G(AlCl)")));
			double T = map.get ("T");
			
			pw.println (String.format (l, "%f, %f", 1 / T, V));
		}
		
		task = new TaskSolveGaCl ();
		maps = task.run ();
		
		pw.close ();
	}
	
}
