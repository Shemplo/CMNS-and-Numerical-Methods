package ru.shemplo.dm.course.physics.methods;

import ru.shemplo.dm.course.physics.Model;

import java.util.function.BiFunction;

public class StupidLatyshevProcessor extends LatyshevProcessor {

    public StupidLatyshevProcessor(Model model) {
        super(model);
    }

    @Override
    public double[] stepX(double[] solT, double[] solX) {
        double lambda = model.getLambda(),
                dz = 1.0E-4,
                rho = model.getRho(),
                Q = model.getQ(),
                dt = 0.01,
                C = model.getC(),
                D = model.getD(),
                Tm = model.getTm();

        BiFunction<Double, Double, Double> W = model::getW;

        double[][] matrixX = new double[coords][];
        matrixX[0] = new double[]{1, 0};
        matrixX[coords - 1] = new double[]{-1, 1};

        for (int time = 1; time < coords - 1; time++) {
            matrixX[time] = new double[]{
                    -D / (dz * dz),
                    1 / dt + 2 * D / (dz * dz) - W.apply(solX[time], solT[time]), // FIXME: MAGIC
                    -D / (dz * dz)
            };
        }

        double[] freeX = new double[coords];
        freeX[0] = 1;
        freeX[coords - 1] = 0;
        for (int time = 1; time < coords - 1; time++) {
            freeX[time] = solX[time] / dt;
        }
        return TridiagonalSolver.solve(matrixX, freeX);
    }

    @Override
    public double[] stepT(double[] solT, double[] solX) {
        double lambda = model.getLambda(),
                dz = 1.0E-4,
                rho = model.getRho(),
                Q = model.getQ(),
                dt = 0.01,
                C = model.getC(),
                D = model.getD(),
                Tm = model.getTm();

        BiFunction<Double, Double, Double> W = model::getW;

        double[][] matrixT = new double[coords][];
        matrixT[0] = new double[]{1, 0};
        matrixT[coords - 1] = new double[]{-1, 1};
        for (int time = 1; time < coords - 1; time++) {
            matrixT[time] = new double[]{
                    -lambda / (dz * dz),
                    rho * C / dt + 2 * lambda / (dz * dz),
                    -lambda / (dz * dz)
            };
        }

        double[] freeT = new double[coords];
        freeT[0] = Tm;
        freeT[coords - 1] = 0;
        for (int time = 1; time < coords - 1; time++) {
            freeT[time] = rho * C / dt * solT[time] - rho * Q * W.apply(solX[time], solT[time]);
        }

        return TridiagonalSolver.solve(matrixT, freeT);
    }
}
