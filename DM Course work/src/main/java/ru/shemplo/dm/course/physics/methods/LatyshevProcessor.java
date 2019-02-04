package ru.shemplo.dm.course.physics.methods;

import ru.shemplo.dm.course.physics.Model;
import ru.shemplo.dm.course.physics.ProcessorResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class LatyshevProcessor extends Processor {

    public LatyshevProcessor(Model model) {
        super(model);
    }

    @Override
    protected ProcessorResult call() {

        List<double[]>
                dataX = new ArrayList<>(),
                dataT = new ArrayList<>(),
                dataW = new ArrayList<>();

        double[] stepForX = getInitialStepX();
        double[] stepForT = getInitialStepT();

        for (int time = 0; time < ticks; time++) {

            double[] stepForW = new double[coords];
            for (int i = 0; i < coords; i++) {
                stepForW[i] = model.getW(stepForX[i], stepForT[i]);
            }

            dataX.add(stepForX);
            dataT.add(stepForT);
            dataW.add(stepForW);

            double[] oldStepT = stepForT;
            stepForT = stepT(oldStepT, stepForX);
            stepForX = stepX(oldStepT, stepForX);

            updateProgress(time, ticks);
        }

        return new ProcessorResult(dataX, dataT, dataW);
    }

    public abstract double[] stepT(double[] solT, double[] solX);

    public abstract double[] stepX(double[] solT, double[] solX);

    private double[] getInitialStepT() {
        double[] solT = new double[coords];
        Arrays.fill(solT, model.getT0());
        solT[0] = model.getTm();

        return solT;
    }

    private double[] getInitialStepX() {
        double[] solX = new double[coords];
        Arrays.fill(solX, 1.0D);
        solX[0] = 0;

        return solX;
    }
}
