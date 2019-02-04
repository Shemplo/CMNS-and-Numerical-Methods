package ru.shemplo.dm.course.physics.methods;

import ru.shemplo.dm.course.physics.Model;

import java.util.function.BiFunction;

public class SomeLatyshevProcessor extends LatyshevProcessor {

    public SomeLatyshevProcessor(Model model) {
        super(model);
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

        double[][] matrixT = getMatrix(
                -lambda / (dz * dz),
                rho * C / dt + 2 * lambda / (dz * dz),
                -lambda / (dz * dz)
        );
        double[] freeT = new double[coords];
        freeT[0] = Tm;
        freeT[coords - 1] = 0;
        for (int i = 1; i < coords - 1; i++) {
            freeT[i] = -rho * Q * W.apply(solX[i], solT[i]) + rho * C / dt * solT[i];
        }
        return TridiagonalSolver.solve(matrixT, freeT);
    }

    @Override
    public double[] stepX(double[] solT, double[] solX) {
        double lambda = model.getLambda(),
                dz = 1.0E-4,
                rho = model.getRho(),
                Q = model.getQ(),
                dt = 0.01,
                C = model.getC(),
                D = model.getD();

        BiFunction<Double, Double, Double> W = model::getW;

        double[][] matrixX = getMatrix(
                -D / (dz * dz),
                1 / dt + 2 * D / (dz * dz),
                -D / (dz * dz)
        );
        double[] freeX = new double[coords];
        freeX[0] = 1;
        freeX[coords - 1] = 0;
        for (int i = 1; i < coords - 1; i++) {
            freeX[i] = W.apply(solX[i], solT[i]) + solX[i] / dt;
        }
        return TridiagonalSolver.solve(matrixX, freeX);
    }

    private double[][] getMatrix(double a, double b, double c) {
        double[][] matrix = new double[coords][];
        matrix[0] = new double[]{1, 0};
        matrix[coords - 1] = new double[]{-1, 1};
        for (int i = 1; i < coords - 1; i++) {
            matrix[i] = new double[]{a, b, c};
        }
        return matrix;
    }
}
