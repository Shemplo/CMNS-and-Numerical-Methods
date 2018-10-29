package ru.shemplo.dm.difschemes.logic;

import ru.shemplo.dm.difschemes.logic.DifferenceScheme.Scheme;

@Scheme (name = "Явная против потока")
public class DifSchemeExplicitCounterFlow extends AbsDifferenceScheme {

    public DifSchemeExplicitCounterFlow (double [] zeroLayer, 
            double u, double k, double dt, double dx) {
        super (zeroLayer, u, k, dt, dx);
    }

    @Override
    protected double [] doUnexistingStep (int step, double [] profile) {
        double [][] previous = {getTimeLayer (step - 1)};
        for (int i = 1; i < profile.length - 1; i++) {
            profile [i] = previous [0][i] * (1 - S - 2 * R)
                        + previous [0][i - 1] * (R + S)
                        + previous [0][i + 1] * R;
        }
        
        return profile;
    }
    
}
