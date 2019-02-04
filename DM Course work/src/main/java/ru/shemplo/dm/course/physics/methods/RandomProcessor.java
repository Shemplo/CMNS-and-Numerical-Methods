package ru.shemplo.dm.course.physics.methods;

import javafx.scene.chart.XYChart;
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
    protected ProcessorResult call() throws Exception {
        int ticks = model.getTicks();
        int coords = model.getCoords();
        double maxCoord = model.getMaxCoord();
        double maxTime = model.getMaxTime();

        System.out.println(ticks + " x " + coords);

        updateProgress(0, ticks * 3);

        List<XYChart.Series<Number, Number>> dataX = new ArrayList<>();
        for (int t = 0; t < ticks; t++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int z = 0; z < coords; z++) {
                series.getData().add(new XYChart.Data<>(z * model.getStepCoord(), random.nextInt(10)));
            }
            dataX.add(series);
            updateProgress(t, ticks * 3);
        }

        List<XYChart.Series<Number, Number>> dataT = new ArrayList<>();
        for (int t = 0; t < ticks; t++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int z = 0; z < 20; z++) {
                series.getData().add(new XYChart.Data<>(z * model.getStepCoord(), random.nextInt(10)));
            }
            dataT.add(series);
            updateProgress(ticks + t, ticks * 3);
        }

        List<XYChart.Series<Number, Number>> dataW = new ArrayList<>();
        for (int t = 0; t < ticks; t++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int z = 0; z < 20; z++) {
                series.getData().add(new XYChart.Data<>(z * model.getStepCoord(), random.nextInt(10)));
            }
            dataW.add(series);
            updateProgress(2 * ticks + t, ticks * 3);
        }

        return new ProcessorResult(dataX, dataT, dataW);
    }
}
