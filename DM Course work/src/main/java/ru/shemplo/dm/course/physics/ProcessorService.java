package ru.shemplo.dm.course.physics;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProcessorService extends Service<ProcessorResult> {

    private Random random = new Random();

    private final Model model;

    public ProcessorService(Model model) {
        this.model = model;
    }

    @Override
    protected Task<ProcessorResult> createTask() {

        // TODO: Replace with new Processor(model)
        return new Task<ProcessorResult>() {

            @Override
            protected ProcessorResult call() {

                int ticks = model.getTicks();

                updateProgress(0, ticks * 3);

                List<XYChart.Series<Number, Number>> dataX = new ArrayList<>();
                for (int t = 0; t < ticks; t++) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    for (int z = 0; z < 20; z++) {
                        series.getData().add(new XYChart.Data<>(random.nextInt(10), random.nextInt(10)));
                    }
                    dataX.add(series);
                    updateProgress(t, ticks * 3);
                }

                List<XYChart.Series<Number, Number>> dataT = new ArrayList<>();
                for (int t = 0; t < ticks; t++) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    for (int z = 0; z < 20; z++) {
                        series.getData().add(new XYChart.Data<>(random.nextInt(10), random.nextInt(10)));
                    }
                    dataT.add(series);
                    updateProgress(ticks + t, ticks * 3);
                }

                List<XYChart.Series<Number, Number>> dataW = new ArrayList<>();
                for (int t = 0; t < ticks; t++) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    for (int z = 0; z < 20; z++) {
                        series.getData().add(new XYChart.Data<>(random.nextDouble() * 10, random.nextInt(10)));
                    }
                    dataW.add(series);
                    updateProgress(2 * ticks + t, ticks * 3);
                }

                return new ProcessorResult(dataX, dataT, dataW);
            }
        };
    }
}
