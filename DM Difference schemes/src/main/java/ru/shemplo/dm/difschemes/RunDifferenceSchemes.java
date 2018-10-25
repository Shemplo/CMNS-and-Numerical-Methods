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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.reflections.Reflections;

import ru.shemplo.dm.difschemes.logic.DifferenceScheme;
import ru.shemplo.dm.difschemes.logic.DifferenceScheme.Scheme;

public class RunDifferenceSchemes extends Application {

	private static Scene scene;
	
	public static enum View {
		
		// Text fields
		U ("paramU"), K ("paramK"), dT ("paramDT"), dX ("paramDX"), 
		DOTS ("dots"),
		
		// Buttons
		SIMULATE ("simulate"), AUTO_PLAY ("autoPlay"),
		
		// Choice boxes
		PROFILES ("profiles"), METHODS ("methods"), 
		PRESETS ("presets"),
		
		// Canvases
		PROFILE ("profile"), CANVAS ("canvas");
		
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
		methodsSet = reflections.getTypesAnnotatedWith (Scheme.class);
		
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
	
	private volatile DifferenceScheme currentScheme = null;
	private SimulationProfiles selectedProfile = null;
	private String selectedMethodName = null;
	
	private int frame = 0;

	@Override
	public void start (final Stage stage) throws Exception {
		stage.setTitle ("Difference schemes");
		stage.setResizable (false);
		stage.setScene (scene);
		stage.show ();
		
		TextField dots = View.DOTS.get (); dots.setText ("100");
		dots.textProperty ().addListener ((list, prev, next) -> {
		    updateProfileCanvas ();
		});
		
		// INITIALIZATION OF GUI CHOICE BOXES //
		
		// Profiles
		ObservableList <SimulationProfiles> profiles = 
			FXCollections.observableArrayList (SimulationProfiles.values ());
		ChoiceBox <SimulationProfiles> profilesBox = View.PROFILES.get ();
		profilesBox.setItems (profiles);
		
		profilesBox.getSelectionModel ().selectedItemProperty ()
				   .addListener ((list, prev, next) -> {
			selectedProfile = next;		    
		    updateProfileCanvas ();
		    stage.sizeToScene ();
		});
		profilesBox.getSelectionModel ().select (0);
		
		// Methods
		ObservableList <String> methods = FXCollections.observableArrayList (
			methodsSet.stream ().map (t -> {
				Scheme method = t.getAnnotation (Scheme.class);
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
			currentScheme = getInstance ();
			updateMainCanvas ();
		});
		
		Button autoPlay = View.AUTO_PLAY.get ();
		autoPlay.setOnMouseClicked (me -> {
			System.out.println ("Start auto play");
		});
	}
	
	private DifferenceScheme getInstance () {
		double [] profile = selectedProfile.getProfile (getIntegerValue (View.DOTS));
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
	
	private Integer getIntegerValue (View view) {
		String value = getValueOrDefault (view, "0");
		return Integer.parseInt (value);
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
	
	private void updateProfileCanvas () {
		Canvas canvas = View.PROFILE.get ();
		double of = 25;
		
		GraphicsContext context = canvas.getGraphicsContext2D ();		
		double w = canvas.getWidth (), h = canvas.getHeight ();
		context.clearRect (0, 0, w, h);
		context.setLineWidth (1.25);
		
		int dots = getIntegerValue (View.DOTS);
		double [] dist = selectedProfile.getProfile (dots);
		
		double max = 0;
		for (double d : dist) { max = Math.max (max, d); }
		
		///////////////////////////
		if (max == 0.0) { return; }
		///////////////////////////
		
		double dx = w / dist.length, prevX = 0, 
			   prevY = of + (1 - dist [0] / max) * (h - of);
		for (int i = 1; i < dist.length; i++) {
			double y = of + (1 - dist [i] / max) * (h - of);
			context.strokeLine (prevX, prevY, dx * i, y);
			prevX = dx * i; prevY = y;
		}
	}
	
	private void updateMainCanvas () {
	    DifferenceScheme scheme = currentScheme;
	    if (scheme == null) { return; }
	    
	    Canvas canvas = View.CANVAS.get ();
	    canvas.setWidth (canvas.getParent ().getBoundsInLocal ().getWidth ());
	    double of = 25;
        
        GraphicsContext context = canvas.getGraphicsContext2D ();       
        double w = canvas.getWidth (), h = canvas.getHeight ();
        context.clearRect (0, 0, w, h);
        context.setLineWidth (1.25);
        
        double max = 0;
        double [] dist = currentScheme.getTimeLayer (frame);        
        for (double d : dist) { max = Math.max (max, d); }
        
        ///////////////////////////
        if (max == 0.0) { return; }
        ///////////////////////////
        
        double dx = w / dist.length, prevX = 0, 
               prevY = of + (1 - dist [0] / max) * (h - of);
        for (int i = 1; i < dist.length; i++) {
            double y = of + (1 - dist [i] / max) * (h - of);
            context.strokeLine (prevX, prevY, dx * i, y);
            prevX = dx * i; prevY = y;
        }
	}
	
}
