package ru.shemplo.dm.course.physics;

import java.util.List;

public class ProcessorResult {

    private final List<double[]> dataX;

    private final List<double[]> dataT;

    private final List<double[]> dataW;

    public ProcessorResult(List<double[]> dataX,
                           List<double[]> dataT,
                           List<double[]> dataW) {
        this.dataX = dataX;
        this.dataT = dataT;
        this.dataW = dataW;
    }

    public List<double[]> getDataX() {
        return dataX;
    }

    public List<double[]> getDataT() {
        return dataT;
    }

    public List<double[]> getDataW() {
        return dataW;
    }
}
