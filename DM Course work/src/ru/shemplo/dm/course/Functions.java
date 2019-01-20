package ru.shemplo.dm.course;

import static java.lang.Math.*;
import static ru.shemplo.dm.course.Constants.*;
import static ru.shemplo.dm.course.Constants.E;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Functions {
    
    public static final Function <Double, Double>
        EXP = T -> exp (-E / (R * T));
    
    public static final BiFunction <Double, Double, Double> 
        W = (a, T) -> -K * pow (a, ALPHA) * EXP.apply (T);
    
    public static final BiFunction <Double, Double, Double>
        dW_dA = (a, T) -> -ALPHA * K * pow (a, ALPHA - 1) * EXP.apply (T);
        
    public static final BiFunction <Double, Double, Double>
        dW_dT = (a, T) -> -E * K * pow (a, ALPHA) * EXP.apply (T) 
                        / (R * pow (T, 2));
        
}
