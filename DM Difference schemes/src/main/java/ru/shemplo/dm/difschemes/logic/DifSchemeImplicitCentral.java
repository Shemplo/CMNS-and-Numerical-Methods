package ru.shemplo.dm.difschemes.logic;

import ru.shemplo.dm.difschemes.logic.DifferenceScheme.Scheme;

@Scheme (name = "Неявная центральная")
public class DifSchemeImplicitCentral extends AbsDifferenceScheme {

    public DifSchemeImplicitCentral (double [] zeroLayer, int its,
            double u, double k, double dt, double dx) {
        super (zeroLayer, its, u, k, dt, dx);
    }

    @Override
    protected void doUnexistingStep (int step, double [] profile) {
        int last = profile.length - 1;
        double [][] previous = {getTimeLayer (step - 1)},
                    equations = new double [last + 1][];
        equations [last] = new double [] {0, 0, 1, previous [0][last]};
        equations [0] = new double [] {0, 0, 1, previous [0][0]};
        for (int i = 1; i < profile.length - 1; i++) {
            equations [i] = new double [] {-R - S / 2, -R + S / 2, 1 + 2 * R, previous [0][i]};
        }
        
        run3DiagonalSolver (equations, profile);
    }
    
}
