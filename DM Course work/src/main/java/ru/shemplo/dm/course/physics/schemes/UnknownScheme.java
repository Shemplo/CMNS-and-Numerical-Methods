package ru.shemplo.dm.course.physics.schemes;

import ru.shemplo.dm.course.physics.Model;
import ru.shemplo.dm.course.physics.ProcessorResult;

import java.util.ArrayList;
import java.util.List;

public class UnknownScheme extends Scheme {

    private final List<double[]>
            Ts = new ArrayList<>(),
            Xs = new ArrayList<>(),
            Ws = new ArrayList<>();

    private final double dt, dx, dx2, ti;

    public UnknownScheme(Model model) {
        super(model);
        this.dt = model.getStepTime();
        this.dx = model.getStepCoord();
        this.ti = 1000; // FIXME: Это что за покемон?
        this.dx2 = dx * dx;

    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected ProcessorResult call() {

        double[] X0s = getInitialStepX();
        Xs.add(X0s);

        double[] T0s = getInitialStepT();
        Ts.add(T0s);

        double[] W0s = new double[coords];
        for (int i = 0; i < coords; i++) {
            W0s[i] = model.getW(X0s[i], T0s[i]);
        }
        Ws.add(W0s);

        for (int i = Ws.size(); i < ticks; i = Ws.size()) {
            double[][] equations = new double[2 * coords][2 * coords];
            double[] y = new double[2 * coords];

            double[] previousA = Xs.get(i - 1), previousT = Ts.get(i - 1);
            double fW, fdW_dA, fdW_dT, fdW_dAa, fdW_dTt;

            // Left-side border condition
            fW = model.getW(previousA[0], previousT[0]);
            fdW_dA = model.getdWdX(previousA[0], previousT[0]);
            fdW_dT = model.getdWdT(previousA[0], previousT[0]);
            fdW_dAa = fdW_dA * previousA[0];
            fdW_dTt = fdW_dT * previousT[0];

            y[0] = previousA[0] / dt + fW - fdW_dAa - fdW_dTt;
            equations[0][0] = 1 / dt + 2 * model.getD() / dx2 - fdW_dA;
            equations[0][1] = -fdW_dT;
            equations[0][2] = -model.getD() / dx2;

            y[1] = ti * model.getKappa() / dx2 + previousT[0] / dt
                    + model.getDt() * (-fW + fdW_dAa + fdW_dTt);
            equations[1][0] = model.getDt() * fdW_dA;
            equations[1][1] = 1 / dt + 2 * model.getKappa() / dx2 + model.getDt() * fdW_dT;
            equations[1][3] = -model.getKappa() / dx2;

            // Inner cells
            for (int x = 1; x < coords - 1; x++) {
                fW = model.getW(previousA[x], previousT[x]);
                fdW_dA = model.getdWdX(previousA[x], previousT[x]);
                fdW_dT = model.getdWdT(previousA[x], previousT[x]);
                fdW_dAa = fdW_dA * previousA[x];
                fdW_dTt = fdW_dT * previousT[x];

                y[2 * x + 0] = previousA[x] / dt + fW - fdW_dAa - fdW_dTt;
                equations[2 * x + 0][2 * x - 2] = -model.getD() / dx2;
                equations[2 * x + 0][2 * x + 0] = 1 / dt + 2 * model.getD() / dx2 - fdW_dA;
                equations[2 * x + 0][2 * x + 1] = -fdW_dT;
                equations[2 * x + 0][2 * x + 2] = -model.getD() / dx2;

                y[2 * x + 1] = previousT[x] / dt + model.getDt() * (-fW + fdW_dAa + fdW_dTt);
                equations[2 * x + 1][2 * x - 1] = -model.getKappa() / dx2;
                equations[2 * x + 1][2 * x + 0] = model.getDt() * fdW_dA;
                equations[2 * x + 1][2 * x + 1] = 1 / dt + 2 * model.getKappa() / dx2 + model.getDt() * fdW_dT;
                equations[2 * x + 1][2 * x + 3] = -model.getKappa() / dx2;
            }

            // Right-side border condition
            fW = model.getW(previousA[coords - 1], previousT[coords - 1]);
            fdW_dA = model.getdWdX(previousA[coords - 1], previousT[coords - 1]);
            fdW_dT = model.getdWdT(previousA[coords - 1], previousT[coords - 1]);
            fdW_dAa = fdW_dA * previousA[coords - 1];
            fdW_dTt = fdW_dT * previousT[coords - 1];

            y[2 * coords - 2] = previousA[coords - 1] / dt
                    + fW - fdW_dAa - fdW_dTt;
            equations[2 * coords - 2][2 * coords - 4] = -model.getD() / dx2;
            equations[2 * coords - 2][2 * coords - 2] = 1 / dt + model.getD() / dx2 - fdW_dA;
            equations[2 * coords - 2][2 * coords - 1] = -fdW_dT;

            y[2 * coords - 1] = model.getKappa() * model.getT0() / dx2 + previousT[coords - 1] / dt
                    + model.getDt() * (-fW + fdW_dAa + fdW_dTt);
            equations[2 * coords - 1][2 * coords - 3] = -model.getKappa() / dx2;
            equations[2 * coords - 1][2 * coords - 2] = model.getDt() * fdW_dA;
            equations[2 * coords - 1][2 * coords - 1] = 1 / dt + 2 * model.getKappa() / dx2 + model.getDt() * fdW_dT;

            double[] rX = new double[coords],
                    rT = new double[coords],
                    rW = new double[coords];

            double[] result = MatrixSolver.solve(equations, y);

            for (int x = 0; x < coords; x++) {
                rX[x] = result[2 * x + 0];
                rT[x] = result[2 * x + 1];
                rW[x] = model.getW(rX[x], rT[x]);
            }
            Xs.add(rX);
            Ts.add(rT);
            Ws.add(rW);

            updateProgress(i, ticks);
        }

        return new ProcessorResult(Xs, Ts, Ws, new double[coords]);
    }

    @Override
    public double[] getStepX(double[] oldX, double[] oldT) {
        return new double[0];
    }

    @Override
    public double[] getStepT(double[] oldX, double[] oldT) {
        return new double[0];
    }
}
