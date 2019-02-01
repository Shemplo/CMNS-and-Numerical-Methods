package ru.shemplo.dm.course.physics;

import javafx.scene.chart.XYChart;

import java.util.List;

public class ProcessorResult {

    private final List<XYChart.Series<Number, Number>> dataX;

    private final List<XYChart.Series<Number, Number>> dataT;

    private final List<XYChart.Series<Number, Number>> dataW;

    public ProcessorResult(List<XYChart.Series<Number, Number>> dataX,
                           List<XYChart.Series<Number, Number>> dataT,
                           List<XYChart.Series<Number, Number>> dataW) {
        this.dataX = dataX;
        this.dataT = dataT;
        this.dataW = dataW;
    }

    public List<XYChart.Series<Number, Number>> getDataX() {
        return dataX;
    }

    public List<XYChart.Series<Number, Number>> getDataT() {
        return dataT;
    }

    public List<XYChart.Series<Number, Number>> getDataW() {
        return dataW;
    }
}
