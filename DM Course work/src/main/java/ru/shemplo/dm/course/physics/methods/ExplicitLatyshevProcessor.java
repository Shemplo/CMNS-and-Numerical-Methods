package ru.shemplo.dm.course.physics.methods;

import ru.shemplo.dm.course.physics.Model;

import java.util.function.BiFunction;

public class ExplicitLatyshevProcessor extends LatyshevProcessor {

    public ExplicitLatyshevProcessor(Model model) {
        super(model);
    }

    @Override
    public double[] stepT(double[] solT, double[] solX) {
        double lambda = model.getLambda(),
                dz = model.getStepCoord(),
                rho = model.getRho(),
                Q = model.getQ(),
                dt = model.getStepTime(),
                C = model.getC();

        BiFunction<Double, Double, Double> W = model::getW;

        double[] answer = new double[coords];
        answer[0] = model.getTm();
        for (int i = 1; i < coords - 1; i++) {
            answer[i] = (lambda * (solT[i + 1] - 2 * solT[i] + solT[i - 1]) / dz / dz - rho * Q * W.apply(solX[i], solT[i])) * dt / rho / C + solT[i];
        }
        answer[answer.length - 1] = answer[answer.length - 2];
        return answer;
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

        double[] answer = new double[coords];
        answer[0] = 1;
        for (int i = 1; i < coords - 1; i++) {
            answer[i] = (D * (solX[i + 1] - 2 * solX[i] + solX[i - 1]) / dz / dz + W.apply(solX[i], solT[i])) * dt + solX[i];
        }
        answer[answer.length - 1] = answer[answer.length - 2];
        return answer;
    }
}
