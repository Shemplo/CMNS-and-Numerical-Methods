package ru.shemplo.kse.course.system;


public interface Equation {

	public double evaluate (double [] input);
	
	public double [] gradient (double [] input);
	
}
