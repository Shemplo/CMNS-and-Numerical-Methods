package ru.shemplo.dm.course.physics;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProcessorService extends Service<ProcessorResult> {

    private final IntegerProperty ticks = new SimpleIntegerProperty();
    private final ObjectProperty<Model> model = new SimpleObjectProperty<>();
    private Random random = new Random();

    public int getTicks() {
        return ticks.get();
    }

    public void setTicks(int ticks) {
        this.ticks.set(ticks);
    }

    public IntegerProperty ticksProperty() {
        return ticks;
    }

    public Model getModel() {
        return model.get();
    }

    public void setModel(Model model) {
        this.model.set(model);
    }

    public ObjectProperty<Model> modelProperty() {
        return model;
    }

    @Override
    protected Task<ProcessorResult> createTask() {

        return new Task<ProcessorResult>() {
            @Override
            protected ProcessorResult call() throws Exception {

                Thread.sleep(10000);

                // TODO: Fix real computations below
                /*Processor p = new Processor(getModel(), 50, 1001, 1);
                p.computeWithListener(new ComputationProgressListener());

                List<XYChart.Series<Number, Number>> dataX = new ArrayList<>(),
                        dataT = new ArrayList<>(),
                        dataW = new ArrayList<>();

                for (int tick = 0; tick < getTicks(); tick++) {
                    XYChart.Series<Number, Number> seriesW = new XYChart.Series<>(),
                            seriesT = new XYChart.Series<>(),
                            seriesX = new XYChart.Series<>();

                    double z = 0;
                    Double[] ws = p.getWs().get(tick),
                            ts = p.getTs().get(tick),
                            xs = p.getXs().get(tick);

                    for (int i = 0; i < ws.length; i++, z += getModel().getStepZ()) {
                        //series.getData().add(new XYChart.Data<>(current - time, Math.cos(.25 * current)));
                        seriesW.getData().add(new XYChart.Data<>(z, ws[i]));
                        seriesT.getData().add(new XYChart.Data<>(z, ts[i]));
                        seriesX.getData().add(new XYChart.Data<>(z, xs[i]));
                    }

                    dataX.add(seriesX);
                    dataT.add(seriesT);
                    dataW.add(seriesW);
                }*/


                List<XYChart.Series<Number, Number>> dataX = new ArrayList<>();
                for (int i = 0; i < getTicks(); i++) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    for (int z = 0; z < 20; z++) {
                        series.getData().add(new XYChart.Data<>(random.nextInt(10), random.nextInt(10)));
                    }
                    dataX.add(series);
                }

                List<XYChart.Series<Number, Number>> dataT = new ArrayList<>();
                for (int i = 0; i < getTicks(); i++) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    for (int z = 0; z < 20; z++) {
                        series.getData().add(new XYChart.Data<>(random.nextInt(10), random.nextInt(10)));
                    }
                    dataT.add(series);
                }

                List<XYChart.Series<Number, Number>> dataW = new ArrayList<>();
                for (int i = 0; i < getTicks(); i++) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    for (int z = 0; z < 20; z++) {
                        series.getData().add(new XYChart.Data<>(random.nextDouble() * 10, random.nextInt(10)));
                    }
                    dataW.add(series);
                }

                return new ProcessorResult(dataX, dataT, dataW);
            }
        };
    }
}
