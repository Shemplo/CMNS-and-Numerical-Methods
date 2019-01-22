package ru.shemplo.dm.course;


public class Constants {

    /**
     * Speed of reaction, 1 / sec
     *
     * Константа скорости реакции
     */
    public static final double K = 1.6e+6;

    /**
     * Activation energy of a chemical reaction, J / mol
     *
     * Энергия активации реакции
     */
    public static final double E = 8e+4;

    /**
     * Order of reaction, 0.5 - 3
     *
     * Порядок реакции
     */
    public static final double ALPHA = 1;

    /**
     * Heat coefficient, J / kg
     *
     * Удельный на единицу массы тепловой эффект реакции
     */
    public static final double Q = 7e+5;

    /**
     * Initial temperature, K
     *
     * Начальная температура
     */
    public static final double T0 = 293;

    /**
     * Mass density, kg / m^3
     *
     * Плотность среды
     */
    public static final double RHO = 830;

    /**
     * Heat capacity of the environment, J / (kg * K)
     *
     * Удельная на единицу массы теплоемкость
     */
    public static final double C = 1990;

    /**
     * Thermal conductivity of the environment, J / (sec * m * K)
     *
     * Теплопроводность среды
     */
    public static final double LAMBDA = 0.13;

    /**
     * Diffusion coefficient, m^2 / sec
     *
     * Коэффициент диффузии реагента
     */
    public static final double D = 8e-12;

    /**
     * Temperature increase, K
     *
     * Повышение температуры среды за счёт теплового эффекта реакции в адиабатических условиях
     */
    public static final double DT = Q / C;

    /**
     * Temperature of reaction, K
     *
     * Температура адиабатического прохождения реакции
     */
    public static final double Tm = T0 + DT;

    /**
     * Thermal diffusivity, m^2 / sec
     *
     * Коэффициент температуропроводности
     */
    public static final double KAPPA = LAMBDA / (RHO * C);

    /**
     * Universal gas constant, J / (mol * K)
     *
     * Универсальная газовая постоянная
     */
    public static final double R = 8.31;

    /**
     * Числа Зельдовича–Франк-Каменецкого (ЗФК)
     */
    public static final double BETA = R * Tm / E;
    public static final double GAMMA = R * Tm * Tm / (E * DT);

    // public static final boolean ACTIVATED = BETA << 1 && GAMMA << 1;
}
