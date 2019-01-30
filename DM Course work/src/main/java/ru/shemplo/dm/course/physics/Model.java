package ru.shemplo.dm.course.physics;

/**
 * Reaction model
 */
public class Model {

    /**
     * Maximum time
     *
     * Максимальное значение времени
     */
    private double maxTime = 1000;

    /**
     * Time step size
     *
     * Шаг по времени
     */
    private double stepTime = 0.01;

    /**
     * Coordinate step size
     *
     * Шаг по координате
     */
    private double stepZ = 0.0001;
    
    /**
     * Speed of reaction, 1 / sec
     *
     * Константа скорости реакции
     */
    private double k = 1.6e+6;

    /**
     * Activation energy of a chemical reaction, J / mol
     *
     * Энергия активации реакции
     */
    private double e = 8e+4;

    /**
     * Order of reaction, 0.5 - 3
     *
     * Порядок реакции
     */
    private double alpha = 1;

    /**
     * Heat coefficient, J / kg
     *
     * Удельный на единицу массы тепловой эффект реакции
     */
    private double q = 7e+5;

    /**
     * Initial temperature, K
     *
     * Начальная температура
     */
    private double t0 = 293;

    /**
     * Mass density, kg / m^3
     *
     * Плотность среды
     */
    private double rho = 830;

    /**
     * Heat capacity of the environment, J / (kg * K)
     *
     * Удельная на единицу массы теплоемкость среды
     */
    private double c = 1990;

    /**
     * Thermal conductivity of the environment, J / (sec * m * K)
     *
     * Теплопроводность среды
     */
    private double lambda = 0.13;

    /**
     * Diffusion coefficient, m^2 / sec
     *
     * Коэффициент диффузии реагента
     */
    private double d = 8e-12;

    /**
     * Universal gas constant, J / (mol * K)
     *
     * Универсальная газовая постоянная
     */
    private final double r = 8.31446;

    /**
     * Temperature increase, K
     *
     * Повышение температуры среды за счёт теплового эффекта реакции в адиабатических условиях
     */
    public double getDt() {
        return q / c;
    }

    /**
     * Temperature of reaction, K
     *
     * Температура адиабатического прохождения реакции
     */
    public double getTm() {
        return t0 + getDt();
    }

    /**
     * Thermal diffusivity, m^2 / sec
     *
     * Коэффициент температуропроводности
     */
    public double getKappa() {
        return lambda / (rho * c);
    }

    /**
     * Число Зельдовича–Франк-Каменецкого (ЗФК): бета
     */
    public double getBeta() {
        return r * getTm() / e;
    }

    /**
     * Число Зельдовича–Франк-Каменецкого (ЗФК): гамма
     */
    public double getGamma() {
        return r * getTm() * getTm() / (e * getDt());
    }

    /**
     * Активированность реакции
     */
    public boolean isActivated() {
        return getBeta() < 1 && getGamma() < 1;
    }

    /**
     * Speed of reaction, W(X, T)
     *
     * Скорость реакции как функция концентрации и температуры
     */
    public double getW(double x, double t) {
        return -k * Math.pow(x, alpha) * Math.exp(-e / (r * t));
    }

    public double getdWdA(double a, double t) {
        return -alpha * k * Math.pow(a, alpha - 1) * Math.exp(-e / (r * t));
    }

    public double getdWdT(double a, double t) {
        return -e * k * Math.pow(a, alpha) * Math.exp(-e / (r * t)) / (r * Math.pow(t, 2));
    }

    public double getMaxTime() {
        return maxTime;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public double getT0() {
        return t0;
    }

    public void setT0(double t0) {
        this.t0 = t0;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getR() {
        return r;
    }

    public double getStepTime() {
        return stepTime;
    }

    public void setStepTime(double stepTime) {
        this.stepTime = stepTime;
    }

    public double getStepZ() {
        return stepZ;
    }

    public void setStepZ(double stepZ) {
        this.stepZ = stepZ;
    }
}
