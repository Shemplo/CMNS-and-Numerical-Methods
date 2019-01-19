package ru.shemplo.dm.course;


public class Constants {
    
    public static final double 
        // Speed of reaction
        K      = 1.6e+6, // [K] = 1 / s
        
        // Energy of activation of reaction
        E      = 8e+4,   // [E] = J / mol
        
        // Order of reaction
        ALPHA  = 1,      // [A] = _
        
        // Warm coefficient
        Q      = 7e+5,   // [Q] = J / kg
        
        // Density of medium
        RHO    = 830,    // [R] = kg / m^3
        
        // Initial temperature
        T0     = 293,    // [T] = K*
        
        // Heat capacity of the medium
        C      = 1980,   // [C] = J / (kg * K*)
        
        Tm     = T0 + Q / C, // ~ 650 K*
        
        // Thermal conductivity of the medium
        LAMBDA = 13e-4,  // [L] = J / (m * s * K*)
        
        KAPPA  = LAMBDA / (RHO * C), // [K] = ...
        
        // Coefficient of diffuse of reagents
        D      = KAPPA,              // [D] = [K]
        
        // Universal haze constant
        R      = 8.31;   // [R] = m^2 * kg / (s^2 * K* * mol)
    
}
