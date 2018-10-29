package ru.shemplo.dm.difschemes.logic;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

public interface DifferenceScheme {
	
	@Retention (RUNTIME)
	@Target (TYPE)
	public static @interface Scheme {
		
		public String name ();
		
	}
	
	public double [] getTimeLayer (int i);
	
}
