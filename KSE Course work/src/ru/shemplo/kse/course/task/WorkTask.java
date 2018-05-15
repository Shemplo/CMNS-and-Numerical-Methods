package ru.shemplo.kse.course.task;

import java.io.PrintWriter;

public interface WorkTask {

	public void run (PrintWriter pw);
	
	public void singleStep (double T, double delta, PrintWriter pw);
	
}
