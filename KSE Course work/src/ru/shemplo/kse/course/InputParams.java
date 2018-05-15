package ru.shemplo.kse.course;

import static ru.shemplo.kse.course.Run.*;
import static java.lang.Math.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.function.Function;

public class InputParams {

	private static final Map <String, Function <Double, Double>> 
		TEMPERATURE_CONSTANTS;
	private static final Map <String, List <String>> DATA;
	private static final Map <String, Double> DENSITIES, 
											  PRESSURES;
	private static final List <String> PARAMETRS;
	static {
		TEMPERATURE_CONSTANTS = new HashMap <> ();
		TEMPERATURE_CONSTANTS.put ("2HCl+2Al=2AlCl+H2", T -> {
			double c1 = getGibbson ("HCl", T), c2 = getGibbson ("Al", T),
				   c3 = getGibbson ("AlCl", T), c4 = getGibbson ("H2", T);
			return exp (-2 * (c1 + c2 - c3 - c4 / 2) / (R * T)) 
				   / ATMOSPHERE_PRESSURE;
		});
		TEMPERATURE_CONSTANTS.put ("2HCl+Al=AlCl2+H2", T -> {
			double c1 = getGibbson ("HCl", T), c2 = getGibbson ("Al", T),
				   c3 = getGibbson ("AlCl2", T), c4 = getGibbson ("H2", T);
			return exp (-(2 * c1 + c2 - c3 - c4) / (R * T));
		});
		TEMPERATURE_CONSTANTS.put ("6HCl+2Al=2AlCl3+3H2", T -> {
			double c1 = getGibbson ("HCl", T), c2 = getGibbson ("Al", T),
				   c3 = getGibbson ("AlCl3", T), c4 = getGibbson ("H2", T);
			return exp (-2 * (3 * c1 + c2 - c3 - 3 * c4 / 2) / (R * T)) 
				   / ATMOSPHERE_PRESSURE;
		});
		
		PARAMETRS = new ArrayList<> ();
		DATA = new HashMap <> ();
		
		DENSITIES = new HashMap <> ();
		DENSITIES.put ("Al",  2690.0);
		DENSITIES.put ("Ga",  5900.0);
		DENSITIES.put ("AlN", 3200.0);
		DENSITIES.put ("GaN", 6150.0);
		
		PRESSURES = new HashMap <> ();
		PRESSURES.put ("HCl",   10000.0);
		PRESSURES.put ("N2",    90000.0);
		PRESSURES.put ("AlCl",  0.0);
		PRESSURES.put ("AlCl2", 0.0);
		PRESSURES.put ("AlCl3", 0.0);
		PRESSURES.put ("GaCl",  0.0);
		PRESSURES.put ("GaCl2", 0.0);
		PRESSURES.put ("GaCl3", 0.0);
		PRESSURES.put ("H2",    0.0);
	}
	
	public static void loadParams () throws IOException {
		loadParams ("Bank_TD_Fragment.dat");
	}
	
	public static void loadParams (String filename) throws IOException {
		File file = new File (filename);
		try (
			InputStream is = new FileInputStream (file);
			Reader r = new InputStreamReader (is, StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader (r);
		) {
			String line;
			while ((line = br.readLine ()) != null) {
				if (line.length () == 0 
					|| (line.charAt (0) != '\''
					&& line.charAt (0) != 'I')) {
					continue;
				}
				
				StringTokenizer st = new StringTokenizer (line);
				Queue <String> data = new LinkedList <> ();
				if (line.charAt (0) == 'I') {
					while (st.hasMoreTokens ()) {
						String token = st.nextToken ();
						data.add (token);
					}
					
					data.poll ();
					PARAMETRS.addAll (data);
					continue;
				}
				
				
				while (st.hasMoreTokens ()) {
					String token = st.nextToken ()
									 .replace ("'", "");
					data.add (token);
				}
				
				String agent = data.poll ();
				DATA.put (agent, new ArrayList <> (data));
			}
		}	
	}
	
	public static double getDoubleParam (String agent, String param) {
		return Double.parseDouble (getParam (agent, param));
	}
	
	private static double _getD (String agent, String param) {
		return getDoubleParam (agent, param);
	}
	
	public static String getParam (String agent, String param) {
		if (!DATA.containsKey (agent)) { return ""; }
		
		int index = PARAMETRS.indexOf (param);
		if (index == -1) { return ""; }
		
		return DATA.get (agent).get (index);
	}
	
	public static double getDiffusion (String agent, double T) {
		double sigmaI  = _getD (agent, "sigma"),
			   epsilI  = _getD (agent, "epsil"),
			   sigmaN2 = _getD ("N2", "sigma"),
			   epsilN2 = _getD ("N2", "epsil"),
			   muI     = _getD (agent, "mu"),
			   muN2    = _getD ("N2", "mu");
		double epsilon = sqrt (epsilI * epsilN2);
		double sigma   = (sigmaI + sigmaN2) / 2;
		double omega   = 1.074 * pow (T / epsilon, -0.1604);
		double mu      = 2 * muI * muN2 / (muI + muN2);
		
		double press = ATMOSPHERE_PRESSURE;
		return 0.02628 * pow (T, 1.5) 
			   / (press * sigma * omega * sqrt (mu));
	}
	
	public static double getGibbson (String agent, double T) {
		double x = T / 10000;
		
		double [] fs = new double [7];
		for (int i = 0; i < fs.length; i++) {
			String param = "f" + (i + 1);
			fs [i] = _getD (agent, param);
		}
		
		double h298 = _getD (agent, "H(298)");
		double s1 = fs [0],
			   s2 = fs [1] * log (x),
			   s3 = fs [2] / (x * x),
			   s4 = fs [3] / x,
			   s5 = fs [4] * x,
			   s6 = fs [5] * x * x,
			   s7 = fs [6] * x * x * x;
		
		return h298 - T * (s1 + s2 + s3 + s4 + s5 + s6 + s7);
	}
	
	public static double getTempConstants (String reaction, double T) {
		if (TEMPERATURE_CONSTANTS.containsKey (reaction)) {
			return TEMPERATURE_CONSTANTS.get (reaction).apply (T);
		}
		
		System.err.println ("Reactoin `" + reaction + "` is not found");
		return 0;
	}
	
	public static double getPressure (String agent) {
		if (PRESSURES.containsKey (agent)) {
			return PRESSURES.get (agent);
		}
		
		System.err.println ("Pressure `" + agent + "` is not found");
		return 0;
	}
 	
}
