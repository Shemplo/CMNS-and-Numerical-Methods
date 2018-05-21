package ru.shemplo.kse.course;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ru.shemplo.kse.course.task.TaskSolveAlClx;
import ru.shemplo.kse.course.task.TaskSolveGaCl;
import ru.shemplo.kse.course.task.WorkTask;

public class Run {
	
	public static final double ATMOSPHERE_PRESSURE;
	public static final double PRECISION;
	public static final Random RANDOM;
	public static final double R;
	
	public static final boolean DEBUG = !true;
	
	private static final WorkTask [] PREPARE_TASKS = {
		new TaskSolveAlClx (), new TaskSolveGaCl ()
	};
	
	private static final Set <String> FOLLOWING_KEYS;
	
	static { // Static constructor
		ATMOSPHERE_PRESSURE = 100000;
		RANDOM = new Random ();
		PRECISION = 1e-10;
		R = 8.314; // Gaze constant
		
		FOLLOWING_KEYS = new HashSet <> ();
		FOLLOWING_KEYS.add ("G(AlCl)");
		FOLLOWING_KEYS.add ("G(AlCl2)");
		FOLLOWING_KEYS.add ("G(AlCl3)");
		FOLLOWING_KEYS.add ("G(GaCl)");
		FOLLOWING_KEYS.add ("G(GaCl2)");
		FOLLOWING_KEYS.add ("G(GaCl3)");
		
		FOLLOWING_KEYS.add ("Ve(Al)");
		FOLLOWING_KEYS.add ("Ve(Ga)");
	}
	
	public static void main (String... args) throws IOException {
		InputParams.loadParams ();
		Locale l = new Locale ("EN");
		
		int index = 1;
		for (WorkTask task : PREPARE_TASKS) {
			String name = task.getClass ().getSimpleName ();
			System.out.println ("Running task " + index + ": " + name);
			long start = System.currentTimeMillis ();
			
			System.out.println (" Solving equation system");
			List <Map <String, Double>> maps = task.run ();
			if (maps.size () == 0) {
				System.err.println ("No success tasks in run: " + name);
				continue;
			}
			
			System.out.println (" Saving data to files");
			File dir = new File (name);
			if (!dir.mkdir () && !dir.isDirectory ()) {
				System.err.println ("Failed to save results: " + name);
				continue;
			}
			
			Set <String> keys = maps.get (0).keySet ();
			for (String key : keys) {
				if (!FOLLOWING_KEYS.contains (key)) {
					continue;
				}
				
				File file = new File (dir, key +".seq");
				try (
					PrintWriter pw = new PrintWriter (file);
				) {
					for (int i = 0; i < maps.size (); i++) {
						Map <String, Double> map = maps.get (i);
						double V = Math.log (Math.abs (map.get (key)));
						double T = map.get ("T");
						
						pw.println (String.format (l, "%f, %f", 1 / T, V));
					}
				}
			}
			
			long end = System.currentTimeMillis ();
			System.out.println (String.format ("Done by %d ms", end - start));
			
			index += 1;
		}
	}
	
}
