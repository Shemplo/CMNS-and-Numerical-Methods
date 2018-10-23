package ru.shemplo.dm.difschemes;

import static java.lang.ClassLoader.*;

import java.util.Set;
import java.util.stream.Collectors;

import java.io.IOException;

import java.net.URL;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.reflections.Reflections;

import ru.shemplo.dm.difschemes.annot.Method;
import ru.shemplo.dm.difschemes.logic.DifSchemeLeapfrog;
import ru.shemplo.dm.difschemes.logic.DifferenceScheme;

public class RunDifferenceSchemes extends Application {

	private static Scene scene;
	
	public static enum View {
		
		// Text fields
		U ("paramU"), K ("paramK"), dT ("paramDT"), dX ("paramDX"),
		
		// Buttons
		SIMULATE ("simulate"), AUTO_PLAY ("autoPlay"),
		
		// Choice boxes
		PROFILES ("profiles"), METHODS ("methods");
		
		private final String ID;
		
		private View (String id) {
			this.ID = id;
		}
		
		@SuppressWarnings ("unchecked")
		public <R extends Node> R get () {
			return (R) scene.lookup ("#" + ID);
		}
		
	}
	
	private static Set <Class <?>> methodsSet;
	
	public static void main (String ... args) {
		// Searching for all classes of methods
		Package path = DifferenceScheme.class.getPackage ();
		Reflections reflections = new Reflections (path);
		methodsSet = reflections.getTypesAnnotatedWith (Method.class);
		
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
	
	private static DifferenceScheme scheme = new DifSchemeLeapfrog (new double [] {
		0.1, 0.2, 0.4, 0.8, 1, 1, 1, 0.7, 0.5, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0
	});

	@Override
	public void start (final Stage stage) throws Exception {
		stage.setTitle ("Difference schemes");
		stage.setResizable (false);
		stage.setScene (scene);
		stage.show ();
		
		// INITIALIZATION OF GUI CHOICE BOXES //
		
		// Profiles
		ObservableList <SimulationProfiles> profiles = 
			FXCollections.observableArrayList (SimulationProfiles.values ());
		ChoiceBox <SimulationProfiles> profilesBox = View.PROFILES.get ();
		profilesBox.setItems (profiles);
		
		profilesBox.getSelectionModel ()
				   .selectedItemProperty ()
				   .addListener ((list, prev, next) -> {
			System.out.println (next + " (" + next.ordinal () + ")");
		    stage.sizeToScene ();
		});
		
		// Methods
		ObservableList <String> methods = FXCollections.observableArrayList (
			methodsSet.stream ().map (t -> {
				Method method = t.getAnnotation (Method.class);
				return method.name ();
			}).collect (Collectors.toList ())
		);
		
		ChoiceBox <String> methodsBox = View.METHODS.get ();
		methodsBox.setItems (methods);
		
		methodsBox.getSelectionModel ()
        		  .selectedItemProperty ()
        		  .addListener ((list, prev, next) -> {
        	System.out.println (next);
        	stage.sizeToScene ();
        });
		
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
