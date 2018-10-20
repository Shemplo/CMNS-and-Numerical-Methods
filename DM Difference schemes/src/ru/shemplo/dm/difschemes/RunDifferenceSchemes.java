package ru.shemplo.dm.difschemes;

import static java.lang.ClassLoader.*;

import java.io.IOException;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import ru.shemplo.dm.difschemes.logic.DifSchemeLeapfrog;
import ru.shemplo.dm.difschemes.logic.DifferenceScheme;

public class RunDifferenceSchemes extends Application {

	private static Scene scene;
	
	public static enum View {
		
		U ("paramU"), K ("paramK"), dT ("paramDT"), dX ("paramDX"),
		SIMULATE ("simulate"), AUTO_PLAY ("autoPlay");
		
		private final String ID;
		
		private View (String id) {
			this.ID = id;
		}
		
		@SuppressWarnings ("unchecked")
		public <R extends Node> R get () {
			return (R) scene.lookup ("#" + ID);
		}
		
	}
	
	public static void main (String ... args) {
		try {
			Parent root = FXMLLoader.load (getSystemResource ("fxml/main.fxml"));
			RunDifferenceSchemes.scene = new Scene (root);
			
			URL styles = getSystemResource ("css/main.css");
			root.getStylesheets ().add (styles.toExternalForm ());
		} catch (IOException ioe) {
			System.err.println ("Failed to initialize main scene");
			System.err.println ("Reason: " + ioe);
			System.exit (1);
		}
		
		launch (args);
	}
	
	private static DifferenceScheme scheme = new DifSchemeLeapfrog ();

	@Override
	public void start (Stage stage) throws Exception {
		stage.setTitle ("Difference schemes");
		stage.setResizable (false);
		stage.setScene (scene);
		stage.show ();
		
		// INITIALIZATION OF GUI CHOICE BOXES //
		
		// ... see later ...
		
		// INITIALIZATION OF GUI HANDLERS //
		
		Button simulate = View.SIMULATE.get ();
		simulate.setOnMouseClicked (me -> {
			TextField layer = View.dT.get ();
			System.out.println ("Start new simulation for layer " + layer.getText ());
			scheme.getTimeLayer (Integer.parseInt (layer.getText ()));
		});
		
		Button autoPlay = View.AUTO_PLAY.get ();
		autoPlay.setOnMouseClicked (me -> {
			System.out.println ("Start auto play");
		});
	}
	
}
