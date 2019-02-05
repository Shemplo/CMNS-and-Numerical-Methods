package ru.shemplo.dm.course.physics;

import java.util.List;

public class ProcessorResult {

    private final List<double[]> dataX;

    private final List<double[]> dataT;

    private final List<double[]> dataW;

    private final Bounds boundsX;

    private final Bounds boundsT;

    private final Bounds boundsW;

    public ProcessorResult(List<double[]> dataX,
                           List<double[]> dataT,
                           List<double[]> dataW) {
        this.dataX = dataX;
        this.dataT = dataT;
        this.dataW = dataW;
        this.boundsX = getBounds(dataX);
        this.boundsT = getBounds(dataT);
        this.boundsW = getBounds(dataW);
    }

    private Bounds getBounds(List<double[]> list) {
        double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY;
        for (int i = 5; i < list.size() - 5; i++) {
            double[] array = list.get(i);
            for (int j = 5; j < array.length - 5; j++) {
                max = Math.max(max, array[j]);
                min = Math.min(min, array[j]);
            }
        }
        return new Bounds(min, max);
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

    public Bounds getBoundsX() {
        return boundsX;
    }

    public Bounds getBoundsT() {
        return boundsT;
    }

    public Bounds getBoundsW() {
        return boundsW;
    }

    public static class Bounds {

        private final double min;

        private final double max;

        public Bounds(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }
    }
}
