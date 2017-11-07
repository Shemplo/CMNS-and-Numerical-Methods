package me.shemplo.kse.newton.core;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NewtonMain extends Application {
	
	private GraphicsContext context;
	
	private double cs; // Cell size
	
	public void start (Stage stage) throws IOException {
		cs = (Math.min (WIDTH, HEIGHT) - 20) /  (2 * SCALE);
		
		Parent parent = FXMLLoader.load (getClass ().getResource ("/me/shemplo/kse/newton/markup/main_window.fxml"));
		Scene scene = new Scene (parent);
		
		Canvas canvas = (Canvas) scene.lookup ("#canvas");
		canvas.setWidth (WIDTH); canvas.setHeight (HEIGHT);
		context = canvas.getGraphicsContext2D ();
		
		Slider sliderScale = (Slider) scene.lookup ("#slider_scale");
		sliderScale.setMinWidth (300);
		sliderScale.setMin (0.25);
		sliderScale.setMax (10);
		sliderScale.setValue (1.5);
		sliderScale.setShowTickLabels (true);
		sliderScale.setShowTickMarks (true);
		sliderScale.setMajorTickUnit (1);
		sliderScale.setMinorTickCount (4);
		sliderScale.setBlockIncrement (0.25);
		sliderScale.valueProperty ().addListener ((ov, old, nev) -> {
			context.clearRect (0, 0, WIDTH, HEIGHT);
			SCALE = nev.doubleValue ();
			
			cs = (Math.min (WIDTH, HEIGHT) - 20) /  (2 * SCALE);
			
			scan ();
			drawScreen ();
		});
		
		Slider sliderParts = (Slider) scene.lookup ("#slider_parts");
		sliderParts.setMinWidth (300);
		sliderParts.setMin (100);
		sliderParts.setMax (1000);
		sliderParts.setValue (750);
		sliderParts.setShowTickLabels (true);
		sliderParts.setShowTickMarks (true);
		sliderParts.setMajorTickUnit (50);
		sliderParts.setMinorTickCount (10);
		sliderParts.setBlockIncrement (50);
		sliderParts.valueProperty ().addListener ((ov, old, nev) -> {
			context.clearRect (0, 0, WIDTH, HEIGHT);
			PARTS = nev.doubleValue ();
			
			scan ();
			drawScreen ();
		});
		
		stage.setTitle ("Newton field");
		stage.setScene (scene);
		stage.show ();
		
		scan ();
		drawScreen ();
	}
	
	public void drawScreen () {
		//context.clearRect (0, 0, WIDTH, HEIGHT);
		context.setStroke (Color.BLACK);
		
		context.strokeLine (WIDTH / 2 - 10, 0, WIDTH / 2 - 10, HEIGHT - 20);
		context.strokeLine (0, HEIGHT / 2 - 10, WIDTH - 20, HEIGHT / 2 - 10);
		context.strokeOval (WIDTH / 2 - 10 - cs, HEIGHT / 2 - 10 - cs, cs * 2, cs * 2);
	}
	
	public void drawComplex (Color color, Complex number) {
		context.setStroke (color);
		context.setFill (color);
		
		double sa = number.a * cs + WIDTH / 2 - 10;
		double sb = -number.b * cs + HEIGHT / 2 - 10;
		
		//context.strokeLine (sa, sb, sa, sb);
		
		//context.fillRect (sa - 1, sb - 1, 3, 3);
		context.fillRect (sa, sb, 1, 1);
	}
	
	public void drawComplexTrace (Color color, Complex a, Complex b) {
		context.setStroke (color);
		context.setFill (color);
		
		double saa = a.a * cs + WIDTH / 2 - 10;
		double sab = -a.b * cs + HEIGHT / 2 - 10;
		double sba = b.a * cs + WIDTH / 2 - 10;
		double sbb = -b.b * cs + HEIGHT / 2 - 10;
		
		context.strokeLine (saa, sab, sba, sbb);
	}
	
	public void scan () {
		Complex root1 = new Complex (1, 0);
		Complex root2 = new Complex (-1 / sqrt (2), 1 / sqrt (2));
		Complex root3 = new Complex (-1 / sqrt (2), -1 / sqrt (2));
		
		double part = PARTS;
		Complex base = new Complex (-SCALE, -SCALE),
				tmp  = new Complex (0, 0), 
				rend = new Complex (0, 0);
		List <Complex> trace;
		Color color;
		
		double dr1, dr2, dr3, minDist;
		
		for (int i = 0; i <= part; i ++) {
			tmp.a = base.a + 2 * SCALE * i / part;
			for (int j = 0; j <= part; j ++) {
				tmp.b = base.b + 2 * SCALE * j / part;
				trace = newton (tmp);
				
				rend = trace.get (trace.size () - 1);
				dr1 = sqDist (root1, rend);
				dr2 = sqDist (root2, rend);
				dr3 = sqDist (root3, rend);
				
				minDist = min (dr1, min (dr2, dr3));
				
				if (minDist == dr1) {
					color = Color.RED;
				} else if (minDist == dr2) {
					color = Color.YELLOW;
				} else {
					color = Color.GREEN;
				}
				
				rend = trace.get (0);
				drawComplex (color, rend);
			}
		}
	}
	
	public List <Complex> newton (Complex number) {
		List <Complex> nearness = new ArrayList <> ();
		nearness.add (number);
		
		for (int i = 1; i < 10; i ++) {
			Complex prev = nearness.get (i - 1);
			
			Complex z3 = z3 (prev);
			Complex z2 = z2 (prev);
			
			z3 = Complex.multiply (z3, 2);
			z3 = Complex.summate (z3, 1);
			
			Complex nz2 = new Complex (z2.a, -z2.b);
			z3 = Complex.multiply (z3, nz2);
			
			z2 = Complex.multiply (z2, 3);
			z2 = Complex.multiply (z2, nz2);
			
			nearness.add (Complex.multiply (z3, 1 / z2.a));
		}
		
		return nearness;
	}
	
	public Complex z2 (Complex number) {
		double a = number.a,
				b = number.b;
		return new Complex (a * a - b * b, 
							2 * a * b);
	}
	
	public Complex z3 (Complex number) {
		double a = number.a,
				b = number.b;
		return new Complex (a * a * a - 3 * a * b * b,
							3 * a * a * b - b * b * b);
	}
	
	public double sqDist (Complex a, Complex b) {
		return (a.a - b.a) * (a.a - b.a) + (a.b - b.b) * (a.b - b.b);
	}
	
	public static double WIDTH = 600,
							HEIGHT = 600;
	public static double SCALE = 1.5;
	public static double PARTS = 750;
	
	public static void main (String... args) {
		launch (args);
	}
	
	public static class Complex {
		
		double a, b;
		
		public Complex (double a, double b) {
			this.a = a; this.b = b;
		}
		
		public String toString () {
			return "(" + a + "; " + b + ")";
		}
		
		public static Complex summate (Complex a, double l) {
			return new Complex (a.a + l, a.b);
		}
		
		public static Complex multiply (Complex a, Complex b) {
			double ax = a.a;
			double ay = a.b;
			return new Complex (ax * b.a - ay * b.b, 
								ay * b.a + ax * b.b);
		}
		
		public static Complex multiply (Complex a, double l) {
			return new Complex (a.a * l, a.b * l);
		}
		
	}
	
}
