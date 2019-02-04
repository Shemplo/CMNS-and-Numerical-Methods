package ru.shemplo.dm.course.physics.methods;

import ru.shemplo.dm.course.physics.Model;
import ru.shemplo.dm.course.physics.ProcessorResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomProcessor extends Processor {

    private Random random = new Random();

    public RandomProcessor(Model model) {
        super(model);
    }

    @Override
    protected ProcessorResult call() {
        updateProgress(0, ticks * 3);

        List<double[]> dataX = new ArrayList<>();
        for (int t = 0; t < ticks; t++) {
            double[] series = new double[coords];
            for (int z = 0; z < coords; z++) {
                series[z] = random.nextDouble();
            }
            dataX.add(series);
            updateProgress(t, ticks * 3);
        }

        List<double[]> dataT = new ArrayList<>();
        for (int t = 0; t < ticks; t++) {
            double[] series = new double[coords];
            for (int z = 0; z < coords; z++) {
                series[z] = random.nextDouble();
            }
            dataT.add(series);
            updateProgress(ticks + t, ticks * 3);
        }

        List<double[]> dataW = new ArrayList<>();
        for (int t = 0; t < ticks; t++) {
            double[] series = new double[coords];
            for (int z = 0; z < coords; z++) {
                series[z] = random.nextDouble();
            }
            dataW.add(series);
            updateProgress(2 * ticks + t, ticks * 3);
        }

        return new ProcessorResult(dataX, dataT, dataW);
    }
}
