package ru.shemplo.kse.course;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ru.shemplo.kse.course.task.TaskSolveAlClx;
import ru.shemplo.kse.course.task.TaskSolveAlGaN;
import ru.shemplo.kse.course.task.TaskSolveGaCl;
import ru.shemplo.kse.course.task.WorkTask;

public class Run {
	
	public static final double ATMOSPHERE_PRESSURE;
	public static final double PRECISION;
	public static final Random RANDOM;
	public static final double R;
	
	public static final Locale L = new Locale ("EN");
	public static final boolean DEBUG = !true;
	
	private static final WorkTask [] PREPARE_TASKS = {
		new TaskSolveAlClx (), new TaskSolveGaCl ()
	};
	
	static { // Static constructor
		ATMOSPHERE_PRESSURE = 100000;
		RANDOM = new Random ();
		PRECISION = 1e-10;
		R = 8.314; // Gaze constant
	}
	
	public static void main (String... args) throws IOException {
		InputParams.loadParams ();
		
		int index = 1;
		for (WorkTask task : PREPARE_TASKS) {
			String name = task.getClass ().getSimpleName ();
			System.out.println ("Running task " + index + ": " + name);
			long start = System.currentTimeMillis ();
			
			System.out.println (" Solving equation system");
			List <Map <String, Double>> maps = task.run ();
			if (maps.size () == 0) {
				System.err.println (" No success tasks in run: " + name);
				continue;
			}
			
			saveMap (name, maps, task.saveKeys ());
			long end = System.currentTimeMillis ();
			System.out.println (String.format ("Done by %d ms", end - start));
			
			index += 1;
		}
		
		InputParams.setPressure ("N2", 98470d);
		InputParams.setPressure ("HCl", 0);
		InputParams.setPressure ("H2", 0d);
		System.out.println ("Running task 3: N2 only (pure)");
		long start = System.currentTimeMillis ();
		WorkTask task = new TaskSolveAlGaN ();
		System.out.println (" Solving equation system");
		List <Map <String, Double>> maps = task.run ();
		
		saveMap (task.getClass ().getSimpleName () + ".pureN2", maps, task.saveKeys ());
		long end = System.currentTimeMillis ();
		System.out.println (String.format ("Done by %d ms", end - start));
		
		InputParams.setPressure ("N2", 88623d);
		InputParams.setPressure ("H2", 9847d);
		System.out.println ("Running task 4: N2 + H2 (mixed)");
		start = System.currentTimeMillis ();
		System.out.println (" Solving equation system");
		maps = task.run ();
		
		saveMap (task.getClass ().getSimpleName () + ".mixedN2H2", maps, task.saveKeys ());
		end = System.currentTimeMillis ();
		System.out.println (String.format ("Done by %d ms", end - start));
	}
	
	private static void saveMap (String task, List <Map <String, Double>> maps,
									String [] keys) throws IOException {
		System.out.println (" Saving data to files");
		File dir = new File (task);
		if (!dir.mkdir () && !dir.isDirectory ()) {
			System.err.println (" Failed to save results: " + task);
			return;
		}
		
		for (String key : keys) {
			File file = new File (dir, key +".xlsx");
			if (file.exists ()) {
				file.delete ();
			}
			
			try (
				OutputStream os = new FileOutputStream (file);
				XSSFWorkbook wb = new XSSFWorkbook ();
			) {
				XSSFSheet sheet = wb.createSheet (key);
				for (int i = 0; i < maps.size (); i++) {
					Map <String, Double> map = maps.get (i);
					Row row = sheet.createRow (i);
					
					if (map.containsKey ("T")) {
						double V = Math.log (Math.abs (map.get (key)));
						double T = map.get ("T");
						
						row.createCell (0).setCellValue (1 / T);
						row.createCell (1).setCellValue (V);
					} else {
						double P = map.get ("Pg(AlCl3)");
						double V = map.get (key);
						
						row.createCell (0).setCellValue (P / 30);
						row.createCell (1).setCellValue (V);
					}
				}
				
				wb.write (os);
			}
		}
	}
	
}
