package ru.shemplo.dm.course.gfx;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Анастасия on 31.01.2019.
 */
public class ChartObserver implements Observer {
    LineChart<Number, Number> chart;

    public ChartObserver(LineChart chart) {
        this.chart = chart;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
