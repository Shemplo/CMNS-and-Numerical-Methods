package ru.shemplo.dm.course.physics;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 * Reaction model
 */
public class Model {

    /**
     * Current time
     * Текущее значение времени
     */
    private final DoubleProperty time = new SimpleDoubleProperty(0);

    /**
     * Maximum time
     * Максимальное значение времени
     */
    private final IntegerProperty maxTime = new SimpleIntegerProperty(10);

    /**
     * Time step size
     * Шаг по времени
     */
    private final DoubleProperty stepTime = new SimpleDoubleProperty(0.01);

    /**
     * Coordinate step size
     * Шаг по координате
     */
    private final DoubleProperty stepZ = new SimpleDoubleProperty(0.0001);

    /**
     * Speed of reaction, 1 / sec
     * Константа скорости реакции
     */
    private final DoubleProperty k = new SimpleDoubleProperty(1.6e+6);

    /**
     * Activation energy of a chemical reaction, J / mol
     * Энергия активации реакции
     */
    private final DoubleProperty e = new SimpleDoubleProperty(8e+4);

    /**
     * Order of reaction, 0.5 - 3
     * Порядок реакции
     */
    private final DoubleProperty alpha = new SimpleDoubleProperty(1);

    /**
     * Heat coefficient, J / kg
     * Удельный на единицу массы тепловой эффект реакции
     */
    private final DoubleProperty q = new SimpleDoubleProperty(7e+5);

    /**
     * Initial temperature, K
     * Начальная температура
     */
    private final DoubleProperty t0 = new SimpleDoubleProperty(293);

    /**
     * Mass density, kg / m^3
     * Плотность среды
     */
    private final DoubleProperty rho = new SimpleDoubleProperty(830);

    /**
     * Heat capacity of the environment, J / (kg * K)
     * Удельная на единицу массы теплоемкость среды
     */
    private final DoubleProperty c = new SimpleDoubleProperty(1990);

    /**
     * Thermal conductivity of the environment, J / (sec * m * K)
     * Теплопроводность среды
     */
    private final DoubleProperty lambda = new SimpleDoubleProperty(0.13);

    /**
     * Diffusion coefficient, m^2 / sec
     * Коэффициент диффузии реагента
     */
    private final DoubleProperty d = new SimpleDoubleProperty(8e-12);

    /**
     * Universal gas constant, J / (mol * K)
     * Универсальная газовая постоянная
     */
    private final ReadOnlyDoubleProperty r = new SimpleDoubleProperty(8.31446);

    /**
     * Temperature increase, K
     * Повышение температуры среды за счёт теплового эффекта реакции в адиабатических условиях
     */
    private final ReadOnlyDoubleWrapper dt = new ReadOnlyDoubleWrapper();

    /**
     * Temperature of reaction, K
     * Температура адиабатического прохождения реакции
     */
    private final ReadOnlyDoubleWrapper tm = new ReadOnlyDoubleWrapper();

    /**
     * Thermal diffusivity, m^2 / sec
     * Коэффициент температуропроводности
     */
    private final ReadOnlyDoubleWrapper kappa = new ReadOnlyDoubleWrapper();

    /**
     * Число Зельдовича–Франк-Каменецкого (ЗФК): бета
     */
    private final ReadOnlyDoubleWrapper beta = new ReadOnlyDoubleWrapper();

    /**
     * Число Зельдовича–Франк-Каменецкого (ЗФК): гамма
     */
    private final ReadOnlyDoubleWrapper gamma = new ReadOnlyDoubleWrapper();

    /**
     * Активированность реакции
     */
    private final ReadOnlyBooleanWrapper activated = new ReadOnlyBooleanWrapper();

    /**
     * Данные для графика X для всех возможных значений времени
     */
    private ListProperty<XYChart.Series<Number, Number>> dataX
            = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Данные для графика T для всех возможных значений времени
     */
    private ListProperty<XYChart.Series<Number, Number>> dataT
            = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Данные для графика W для всех возможных значений времени
     */
    private ListProperty<XYChart.Series<Number, Number>> dataW
            = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Model() {
        dt.bind(q.divide(c));
        tm.bind(t0.add(dt));
        kappa.bind(lambda.divide(rho).divide(c));
        beta.bind(r.multiply(tm).divide(e));
        gamma.bind(r.multiply(tm).multiply(tm).divide(e).divide(dt));
        activated.bind(beta.lessThan(1).and(gamma.lessThan(1)));

        time.addListener((observable, oldValue, newValue) -> {
            double value = Math.min(Math.max(time.doubleValue(), 0), maxTime.doubleValue());
            double diff = value % stepTime.doubleValue();
            if (stepTime.doubleValue() - diff < 1e-10) {
                diff = 0;
            }
            time.setValue(value - diff);
        });

        maxTime.addListener((observable, oldValue, newValue) ->
                time.setValue(Math.min(time.doubleValue(), maxTime.doubleValue())));

        stepTime.addListener((observable, oldValue, newValue) -> {
            double value = time.doubleValue();
            time.setValue(value - value % stepTime.doubleValue());
        });
    }

    /**
     * Speed of reaction, W(X, T)
     * Скорость реакции как функция концентрации и температуры
     */
    public double getW(double x, double t) {
        return -getK() * Math.pow(x, getAlpha()) * Math.exp(-getE() / (getR() * t));
    }

    /**
     * Unknown code :)
     */
    public double getdWdA(double a, double t) {
        return -getAlpha() * getK() * Math.pow(a, getAlpha() - 1) * Math.exp(-getE() / (getR() * t));
    }

    /**
     * Unknown code :)
     */
    public double getdWdT(double a, double t) {
        return -getE() * getK() * Math.pow(a, getAlpha()) * Math.exp(-getE() / (getR() * t)) / (getR() * Math.pow(t, 2));
    }

    /* GENERATED METHODS */

    public ObservableList<XYChart.Series<Number, Number>> getDataX() {
        return dataX.get();
    }

    public void setDataX(ObservableList<XYChart.Series<Number, Number>> dataX) {
        this.dataX.set(dataX);
    }

    public ListProperty<XYChart.Series<Number, Number>> dataXProperty() {
        return dataX;
    }

    public ObservableList<XYChart.Series<Number, Number>> getDataT() {
        return dataT.get();
    }

    public void setDataT(ObservableList<XYChart.Series<Number, Number>> dataT) {
        this.dataT.set(dataT);
    }

    public ListProperty<XYChart.Series<Number, Number>> dataTProperty() {
        return dataT;
    }

    public ObservableList<XYChart.Series<Number, Number>> getDataW() {
        return dataW.get();
    }

    public void setDataW(ObservableList<XYChart.Series<Number, Number>> dataW) {
        this.dataW.set(dataW);
    }

    public ListProperty<XYChart.Series<Number, Number>> dataWProperty() {
        return dataW;
    }

    public double getTime() {
        return time.get();
    }

    public void setTime(double time) {
        this.time.set(time);
    }

    public DoubleProperty timeProperty() {
        return time;
    }

    public int getMaxTime() {
        return maxTime.get();
    }

    public void setMaxTime(int maxTime) {
        this.maxTime.set(maxTime);
    }

    public IntegerProperty maxTimeProperty() {
        return maxTime;
    }

    public double getStepTime() {
        return stepTime.get();
    }

    public void setStepTime(double stepTime) {
        this.stepTime.set(stepTime);
    }

    public DoubleProperty stepTimeProperty() {
        return stepTime;
    }

    public double getStepZ() {
        return stepZ.get();
    }

    public void setStepZ(double stepZ) {
        this.stepZ.set(stepZ);
    }

    public DoubleProperty stepZProperty() {
        return stepZ;
    }

    public double getK() {
        return k.get();
    }

    public void setK(double k) {
        this.k.set(k);
    }

    public DoubleProperty kProperty() {
        return k;
    }

    public double getE() {
        return e.get();
    }

    public void setE(double e) {
        this.e.set(e);
    }

    public DoubleProperty eProperty() {
        return e;
    }

    public double getAlpha() {
        return alpha.get();
    }

    public void setAlpha(double alpha) {
        this.alpha.set(alpha);
    }

    public DoubleProperty alphaProperty() {
        return alpha;
    }

    public double getQ() {
        return q.get();
    }

    public void setQ(double q) {
        this.q.set(q);
    }

    public DoubleProperty qProperty() {
        return q;
    }

    public double getT0() {
        return t0.get();
    }

    public void setT0(double t0) {
        this.t0.set(t0);
    }

    public DoubleProperty t0Property() {
        return t0;
    }

    public double getRho() {
        return rho.get();
    }

    public void setRho(double rho) {
        this.rho.set(rho);
    }

    public DoubleProperty rhoProperty() {
        return rho;
    }

    public double getC() {
        return c.get();
    }

    public void setC(double c) {
        this.c.set(c);
    }

    public DoubleProperty cProperty() {
        return c;
    }

    public double getLambda() {
        return lambda.get();
    }

    public void setLambda(double lambda) {
        this.lambda.set(lambda);
    }

    public DoubleProperty lambdaProperty() {
        return lambda;
    }

    public double getD() {
        return d.get();
    }

    public void setD(double d) {
        this.d.set(d);
    }

    public DoubleProperty dProperty() {
        return d;
    }

    public double getR() {
        return r.get();
    }

    public ReadOnlyDoubleProperty rProperty() {
        return r;
    }

    public double getDt() {
        return dt.get();
    }

    public ReadOnlyDoubleProperty dtProperty() {
        return dt.getReadOnlyProperty();
    }

    public double getTm() {
        return tm.get();
    }

    public ReadOnlyDoubleProperty tmProperty() {
        return tm.getReadOnlyProperty();
    }

    public double getKappa() {
        return kappa.get();
    }

    public ReadOnlyDoubleProperty kappaProperty() {
        return kappa.getReadOnlyProperty();
    }

    public double getBeta() {
        return beta.get();
    }

    public ReadOnlyDoubleProperty betaProperty() {
        return beta.getReadOnlyProperty();
    }

    public double getGamma() {
        return gamma.get();
    }

    public ReadOnlyDoubleProperty gammaProperty() {
        return gamma.getReadOnlyProperty();
    }

    public boolean isActivated() {
        return activated.get();
    }

    public ReadOnlyBooleanProperty activatedProperty() {
        return activated.getReadOnlyProperty();
    }
}
