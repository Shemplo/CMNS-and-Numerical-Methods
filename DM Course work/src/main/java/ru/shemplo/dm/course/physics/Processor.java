package ru.shemplo.dm.course.physics;

import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Processor extends Task<ProcessorResult> {

    private final List<Double[]>
            Ts = new ArrayList<>(),
            Xs = new ArrayList<>(),
            Ws = new ArrayList<>();

    @SuppressWarnings("unused")
    private final double dt, dx, dx2, ti;
    private final int nodes, iterations;
    private final Model model;

    public Processor(Model model) {
        this.model = model;
        this.nodes = model.getCoords();
        this.iterations = model.getTicks();
        this.dt = model.getStepTime();
        this.dx = model.getStepZ();
        this.ti = 1; // FIXME: Это что за покемон?
        this.dx2 = dx * dx;

        Double[] T0s = new Double[nodes];
        Arrays.fill(T0s, model.getT0());
        Ts.add(T0s);

        Double[] A0s = new Double[nodes];
        Arrays.fill(A0s, 1.0D);
        Xs.add(A0s);

        Double[] W0s = new Double[nodes];
        for (int i = 0; i < nodes; i++) {
            W0s[i] = Math.abs(model.getW(A0s[i], T0s[i]));
        }
        Ws.add(W0s);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected ProcessorResult call() throws Exception {

        for (int i = Ws.size(); i < iterations; i = Ws.size()) {
            double[][] equations = new double[2 * nodes][2 * nodes];
            double[] y = new double[2 * nodes];

            Double[] previousA = Xs.get(i - 1), previousT = Ts.get(i - 1);
            double fW, fdW_dA, fdW_dT, fdW_dAa, fdW_dTt;

            // Left-side border condition
            fW = model.getW(previousA[0], previousT[0]);
            fdW_dA = model.getdWdA(previousA[0], previousT[0]);
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
            for (int x = 1; x < nodes - 1; x++) {
                fW = model.getW(previousA[x], previousT[x]);
                fdW_dA = model.getdWdA(previousA[x], previousT[x]);
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
            fW = model.getW(previousA[nodes - 1], previousT[nodes - 1]);
            fdW_dA = model.getdWdA(previousA[nodes - 1], previousT[nodes - 1]);
            fdW_dT = model.getdWdT(previousA[nodes - 1], previousT[nodes - 1]);
            fdW_dAa = fdW_dA * previousA[nodes - 1];
            fdW_dTt = fdW_dT * previousT[nodes - 1];

            y[2 * nodes - 2] = previousA[nodes - 1] / dt
                    + fW - fdW_dAa - fdW_dTt;
            equations[2 * nodes - 2][2 * nodes - 4] = -model.getD() / dx2;
            equations[2 * nodes - 2][2 * nodes - 2] = 1 / dt + model.getD() / dx2 - fdW_dA;
            equations[2 * nodes - 2][2 * nodes - 1] = -fdW_dT;

            y[2 * nodes - 1] = model.getKappa() * model.getT0() / dx2 + previousT[nodes - 1] / dt
                    + model.getDt() * (-fW + fdW_dAa + fdW_dTt);
            equations[2 * nodes - 1][2 * nodes - 3] = -model.getKappa() / dx2;
            equations[2 * nodes - 1][2 * nodes - 2] = model.getDt() * fdW_dA;
            equations[2 * nodes - 1][2 * nodes - 1] = 1 / dt + 2 * model.getKappa() / dx2 + model.getDt() * fdW_dT;

            Double[] rA = new Double[nodes], rT = new Double[nodes],
                    rW = new Double[nodes];
            double[] result = MatrixSolver.solve(equations, y);
            for (int x = 0; x < nodes; x++) {
                rA[x] = result[2 * x + 0];
                rT[x] = result[2 * x + 1];

                rW[x] = Math.abs(model.getW(rA[x], rT[x]));
            }
            Xs.add(rA);
            Ts.add(rT);
            Ws.add(rW);

        }


        List<XYChart.Series<Number, Number>> dataX = new ArrayList<>(),
                dataT = new ArrayList<>(),
                dataW = new ArrayList<>();

        for (int tick = 0; tick < model.getTicks(); tick++) {
            XYChart.Series<Number, Number> seriesW = new XYChart.Series<>(),
                    seriesT = new XYChart.Series<>(),
                    seriesX = new XYChart.Series<>();

            double z = 0;
            Double[] ws = Ws.get(tick),
                    ts = Ts.get(tick),
                    xs = Xs.get(tick);

            for (int i = 0; i < ws.length; i++, z += model.getStepZ()) {
                //series.getData().add(new XYChart.Data<>(current - time, Math.cos(.25 * current)));
                seriesW.getData().add(new XYChart.Data<>(z, ws[i]));
                seriesT.getData().add(new XYChart.Data<>(z, ts[i]));
                seriesX.getData().add(new XYChart.Data<>(z, xs[i]));
            }

            dataX.add(seriesX);
            dataT.add(seriesT);
            dataW.add(seriesW);

        }

        return new ProcessorResult(dataX, dataT, dataW);
    }

    public int computedSteps() {
        return Ws.size();
    }

    public List<Double[]> getWs() {
        return Ws;
    }

    public List<Double[]> getTs() {
        return Ts;
    }

    public List<Double[]> getXs() {
        return Xs;
    }
}
