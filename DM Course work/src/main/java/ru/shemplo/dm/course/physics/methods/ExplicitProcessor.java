package ru.shemplo.dm.course.physics.methods;

import ru.shemplo.dm.course.physics.Model;

import java.util.function.BiFunction;

public class ExplicitProcessor extends Processor {

    public ExplicitProcessor(Model model) {
        super(model);
    }

    @Override
    public double[] getStepT(double[] oldX, double[] oldT) {
        double dz = model.getStepCoord(), dt = model.getStepTime();
        double Q = model.getQ(), kappa = model.getKappa(), C = model.getC();

        BiFunction<Double, Double, Double> W = model::getW;

        double[] answer = new double[coords];
        answer[0] = model.getTm();
        for (int i = 1; i < coords - 1; i++) {
            answer[i] = oldT[i] + dt * (kappa * (oldT[i + 1] - 2 * oldT[i] + oldT[i - 1]) / dz / dz - Q / C * W.apply(oldX[i], oldT[i]));
            if (Math.abs(answer[i]) < 1E-40) {
                answer[i] = 0;
            }
        }
        answer[answer.length - 1] = answer[answer.length - 2];
        return answer;
    }

    @Override
    public double[] getStepX(double[] oldX, double[] oldT) {
        double dz = model.getStepCoord(), dt = model.getStepTime();
        double D = model.getD();

        BiFunction<Double, Double, Double> W = model::getW;

        double[] answer = new double[coords];
        answer[0] = 0;
        for (int i = 1; i < coords - 1; i++) {
            answer[i] = oldX[i] + dt * (D * (oldX[i + 1] - 2 * oldX[i] + oldX[i - 1]) / dz / dz + W.apply(oldX[i], oldT[i]));
            if (Math.abs(answer[i]) < 1E-40) {
                answer[i] = 0;
            }
        }
        answer[answer.length - 1] = answer[answer.length - 2];
        return answer;
    }
}
