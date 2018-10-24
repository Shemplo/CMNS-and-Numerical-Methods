package ru.shemplo.dm.difschemes;

import static java.lang.ClassLoader.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import java.io.IOException;

import java.lang.reflect.Constructor;
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
import ru.shemplo.dm.difschemes.logic.DifferenceScheme;

public class RunDifferenceSchemes extends Application {

	private static Scene scene;
	
	public static enum View {
		
		// Text fields
		U ("paramU"), K ("paramK"), dT ("paramDT"), dX ("paramDX"),
		
		// Buttons
		SIMULATE ("simulate"), AUTO_PLAY ("autoPlay"),
		
		// Choice boxes
		PROFILES ("profiles"), METHODS ("methods"), PRESETS ("presets");
		
		private final String ID;
		
		private View (String id) {
			this.ID = id;
		}
		
		@SuppressWarnings ("unchecked")
		public <R extends Node> R get () {
			return (R) scene.lookup ("#" + ID);
		}
		
	}
	
	private static Map <String, Class <?>> methodsMap = new HashMap <> ();
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
	
	private SimulationProfiles selectedProfile = null;
	private String selectedMethodName = null;

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
		
		profilesBox.getSelectionModel ().selectedItemProperty ()
				   .addListener ((list, prev, next) -> {
			selectedProfile = next;
		    stage.sizeToScene ();
		});
		profilesBox.getSelectionModel ().select (0);
		
		// Methods
		ObservableList <String> methods = FXCollections.observableArrayList (
			methodsSet.stream ().map (t -> {
				Method method = t.getAnnotation (Method.class);
				methodsMap.put (method.name (), t);
				return method.name ();
			}).collect (Collectors.toList ())
		);
		
		ChoiceBox <String> methodsBox = View.METHODS.get ();
		methodsBox.setItems (methods);
		
		methodsBox.getSelectionModel ().selectedItemProperty ()
        		  .addListener ((list, prev, next) -> {
        	selectedMethodName = next;
        	stage.sizeToScene ();
        });
		methodsBox.getSelectionModel ().select (0);
		
		// Presets
		ObservableList <SimulationPreset> presets = 
			FXCollections.observableArrayList (SimulationPreset.values ());
		ChoiceBox <SimulationPreset> presetsBox = View.PRESETS.get ();
		presetsBox.setItems (presets);
		
		presetsBox.getSelectionModel ().selectedItemProperty ()
        		  .addListener ((list, prev, next) -> {
        	next.renewGUI (); stage.sizeToScene ();
        });
		
		// INITIALIZATION OF GUI HANDLERS //
		
		Button simulate = View.SIMULATE.get ();
		simulate.setOnMouseClicked (me -> {
			DifferenceScheme scheme = getInstance ();
			System.out.println (scheme.getClass ().getName ());
		});
		
		Button autoPlay = View.AUTO_PLAY.get ();
		autoPlay.setOnMouseClicked (me -> {
			System.out.println ("Start auto play");
		});
	}
	
	private DifferenceScheme getInstance () {
		double [] profile = selectedProfile.getProfile (1000);
		double dt = getDoubleValue (View.dT), dx = getDoubleValue (View.dX),
			   u = getDoubleValue (View.U), k = getDoubleValue (View.K);
		
		try {
			Class <?> methodToken = methodsMap.get (selectedMethodName);
			
			@SuppressWarnings ("unchecked")
			Constructor <DifferenceScheme> constructor = (Constructor <DifferenceScheme>) 
				methodToken.getConstructor (double [].class, double.class, 
						double.class, double.class, double.class);
			return constructor.newInstance (profile, u, k, dt, dx);
		} catch (Exception e) { System.err.println (e); }
		
		return null;
	}
	
	private Double getDoubleValue (View view) {
		String value = getValueOrDefault (view, "0.0");
		return Double.parseDouble (value);
	}
	
	private String getValueOrDefault (View view, String def) {
		String value = view.<TextField> get ().getText ();
		if (value == null || value.length () == 0) {
			return def;
		}
		
		return value;
	}
	
}
