package ru.shemplo.dm.difschemes;

import static java.lang.ClassLoader.*;
import static java.lang.Math.*;
import static javafx.animation.Animation.Status.*;
import static org.reflections.util.ClasspathHelper.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import java.io.IOException;

import java.lang.reflect.Constructor;
import java.net.URL;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

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
		RESET ("reset"),
		
		// Choice boxes
		PROFILES ("profiles"), METHODS ("methods"), 
		PRESETS ("presets"),
		
		// Canvases
		PROFILE ("profile"), CANVAS ("canvas"),
		
		// Sliders
		FRAME ("frame"),
		
		// CheckBoxes
		FIXED_SCALE ("fixedScale");
		
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
		Set <URL> urls = forPackage (path.getName (), getSystemClassLoader ());
		Reflections reflections = new Reflections (urls);
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
	
	private volatile int frame = 0;
	private Timeline animation;

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
		
		animation = new Timeline (new KeyFrame (Duration.ZERO, this::animateFrame),
		                          new KeyFrame (Duration.millis (65 - 5)));
		animation.setCycleCount (Timeline.INDEFINITE);
		animation.setAutoReverse (false);
		
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
		presetsBox.getSelectionModel ().select (0);
		
		// INITIALIZATION OF GUI HANDLERS //
		
		Button simulate = View.SIMULATE.get ();
		simulate.setOnMouseClicked (me -> {
		    View missed = checkInputFields ();
		    if (missed == null) {
		        currentScheme = getInstance ();
		        currentScheme.getTimeLayer (1000); // pre-calculation
		        animation.stop (); frame = 0;
		        scaleMax = scaleMin = 0;
		        
		        CheckBox fixed = View.FIXED_SCALE.get ();
		        isFixedScale = fixed.isSelected ();
		        Slider slider = View.FRAME.get ();
	            slider.setValue (frame);
		        updateMainCanvas ();
		    } else {
		        System.out.println (missed);
		    }
		});
		
		Button autoPlay = View.AUTO_PLAY.get ();
		autoPlay.setOnMouseClicked (me -> {
		    if (!animation.getStatus ().equals (RUNNING)) {
		        animation.playFromStart ();
		    } else { animation.stop (); }
		});
		
		Button reset = View.RESET.get ();
		reset.setOnMouseClicked (me -> { 
		    frame = 0; 
		    
		    Slider slider = View.FRAME.get ();
		    slider.setValue (frame);
		    updateMainCanvas ();
		});
		
		Slider slider = View.FRAME.get ();
		slider.setMin (0); slider.setMax (1000);
		slider.setBlockIncrement (1.0);
		//slider.setShowTickMarks (true);
		slider.valueProperty ()
		      .addListener ((value, prev, next) -> {
		    frame = next.intValue ();
		    updateMainCanvas ();
		});
	}
	
	public void animateFrame (ActionEvent ae) {
	    Slider slider = View.FRAME.get ();
	    
	    if (frame >= slider.getMax ()) {
	        animation.stop ();
	        frame = 0;
	    } else { frame += 1; }
	    
	    slider.setValue (frame);
	    updateMainCanvas ();
	}
	
	public View checkInputFields () {
	    for (View view : View.values ()) {
	        if (view.get () instanceof TextField) {
	            TextField field = view.get ();
	            String text = field.getText ();
	            if (text == null || text.length () == 0) {
	                return view;
	            }
	        }
	    }
	    
	    return null;
	}
	
	private DifferenceScheme getInstance () {
		double [] profile = selectedProfile.getProfile (getIntegerValue (View.DOTS));
		double dt = getDoubleValue (View.dT), dx = getDoubleValue (View.dX),
			   u = getDoubleValue (View.U), k = getDoubleValue (View.K);
		int iterations = (int) View.FRAME.<Slider> get ().getMax ();
		
		try {
			Class <?> methodToken = methodsMap.get (selectedMethodName);
			
			@SuppressWarnings ("unchecked")
			Constructor <DifferenceScheme> constructor = (Constructor <DifferenceScheme>) 
				methodToken.getConstructor (double [].class, int.class, 
				        double.class, double.class, double.class, double.class);
			return constructor.newInstance (profile, iterations, u, k, dt, dx);
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
	
	private double scaleMax = 0, scaleMin = 0;
	private boolean isFixedScale = false;
	
	private void updateMainCanvas () {
	    DifferenceScheme scheme = currentScheme;
	    if (scheme == null) { return; }
	    
	    Canvas canvas = View.CANVAS.get ();
	    canvas.setWidth (canvas.getParent ().getBoundsInLocal ().getWidth ());
	    double of = 25;
        
        GraphicsContext context = canvas.getGraphicsContext2D ();       
        double w = canvas.getWidth (), h = canvas.getHeight ();
        context.clearRect (0, 0, w, h);
        
        context.setStroke (Color.BLACK);
        context.setLineWidth (1.25);
        
        double max = 0.0, min = 0.0, line = 0.0;
        double [] dist = currentScheme.getTimeLayer (frame);
        for (double d : dist) { 
            max = max (max, d); min = min (min, d);
        }
        
        if (isFixedScale && scaleMax == 0 && scaleMin == 0) {
            scaleMax = max; scaleMin = min;
        }
        
        double rmax = max, rmin = min;
        if (isFixedScale) {
            max = scaleMax; min = scaleMin;
        }
        line = max - min;
        
        double bottom = abs (min) / line * (h - of),
               top = max / line *  (h - of);
        
        ////////////////////////////////////////////
        if (top == 0.0 && bottom == 0.0) { return; }
        ////////////////////////////////////////////
        
        context.setStroke (Color.GRAY);
        context.setLineWidth (0.5);
        context.strokeLine (0, of + top, w, of + top);
        
        context.setStroke (Color.BLACK);
        context.setLineWidth (1.25);
        double dx = w / dist.length, prevX = 0, part = 0, prevY = 0;
        for (int i = 0; i < dist.length; i++) {
            part = abs (dist [i] / line);
            
            double y = of + top - (dist [i] > 0 ? top : -bottom) * part;
            context.strokeLine (prevX, (i == 0 ? y : prevY), dx * i, y);
            prevX = dx * i; prevY = y;
        }
        
        Font font = context.getFont ();
        context.setFont (Font.font ("Consolas", FontWeight.BOLD, 14));
        context.setFill (Color.RED);
        
        context.fillText (String.format (Locale.ENGLISH, "Max: %.12f", rmax), 0, 10);
        context.fillText (String.format (Locale.ENGLISH, "Min: %.12f", rmin), 0, 30);
        
        context.setFont (font);
	}
	
}
