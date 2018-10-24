package ru.shemplo.dm.difschemes;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import ru.shemplo.dm.difschemes.RunDifferenceSchemes.View;

public enum SimulationPreset {
	
	J4F ("just for fun", () -> {
		View.U.<TextField> get ().setText ("0.2");
		View.PROFILES.<ChoiceBox <?>> get ().getSelectionModel ()
			.select (SimulationProfiles.SPLASH.ordinal ());
	});
	
	private final Runnable RENEW;
	private final String NAME;
	
	private SimulationPreset (String name, Runnable renewer) {
		this.RENEW = renewer;
		this.NAME = name;
	}
	
	@Override
	public String toString () {
		return this.NAME;
	}
	
	public void renewGUI () {
		this.RENEW.run ();
	}
	
}