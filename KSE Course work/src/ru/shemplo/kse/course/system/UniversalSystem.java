package ru.shemplo.kse.course.system;

import java.util.Objects;

public class UniversalSystem implements EquationSystem {

	private final Equation [] EQUATIONS;
	
	public UniversalSystem (Equation... equations) {
		if (Objects.isNull (equations) || equations.length == 0) {
			String message = "System should have at least 1 equation";
			throw new IllegalArgumentException (message);
		}
		
		this.EQUATIONS = equations;
	}
	
	@Override
	public int getSize () {
		return EQUATIONS.length;
	}

	@Override
	public Equation getEquation (int index) {
		if (index < 0 || index >= getSize ()) {
			throw new IndexOutOfBoundsException ();
		}
		
		return EQUATIONS [index];
	}

}
