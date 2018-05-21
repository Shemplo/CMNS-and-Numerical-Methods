package ru.shemplo.kse.course.task;

import java.util.List;
import java.util.Map;

public interface WorkTask {

	public List <Map <String, Double>> run ();
	
	public Map <String, Double> singleStep (double T, double delta);
	
	public String [] saveKeys ();
	
}
