package lab2.modules.core;

import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Реализация функции косинуса через разложение в степенной ряд Тейлора.
 */
public final class Cosine implements NamedOutputFunction {

    private static final double TWO_PI = 2.0 * Math.PI;
    /**
     * Максимальная точность - 1e-15.
     */
    private final double precision;

    public Cosine(double precision) {
        this.precision = Math.max(precision, MAX_PRECISION);
    }

    /**
     * Вычисление косинуса с заданной точностью
     * @param x аргумент в радианах
     * @return значение cos(x)
     */
    @Override
    public double compute(double x) {
        x = reduceAngle(x);

        // Ряд Тейлора для cos(x) = 1 - x^2/2! + x^4/4! - x^6/6! + ...
        double result = 1.0;
        double term = 1.0;
        int n = 1;

        while (Math.abs(term) > this.precision) {
            term *= -x * x / ((2 * n - 1) * (2 * n));
            result += term;
            n++;
        }

        return result;
    }

    /**
     * Приведение угла к диапазону [0, 2π]
     */
    private double reduceAngle(double x) {
        x = x % TWO_PI;
        if (x < 0) x += TWO_PI;
        return x;
    }

    @Override
    public String getName() {
        return "cos";
    }
}