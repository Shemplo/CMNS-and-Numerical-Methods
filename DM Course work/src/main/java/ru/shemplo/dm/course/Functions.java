package ru.shemplo.dm.course;

import java.util.function.BiFunction;

import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static ru.shemplo.dm.course.Constants.*;

public class Functions {

    /**
     * Speed of reaction
     *
     * Скорость реакции как функция концентрации и температуры
     */
    public static final BiFunction<Double, Double, Double>
            W = (X, T) -> -K * pow(X, ALPHA) * exp(-E / (R * T));

    public static final BiFunction<Double, Double, Double>
            dW_dA = (a, T) -> -ALPHA * K * pow(a, ALPHA - 1) * exp(-E / (R * T));

    public static final BiFunction<Double, Double, Double>
            dW_dT = (a, T) -> -E * K * pow(a, ALPHA) * exp(-E / (R * T)) / (R * pow(T, 2));

}
