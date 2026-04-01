package lab2.modules.core;

import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Реализация натурального логарифма через разложение в ряд
 */
public final class NaturalLogarithm implements NamedOutputFunction {

    /**
     * Максимальная точность - 1e-15.
     */
    private final double precision;

    public NaturalLogarithm(double precision) {
        this.precision = Math.max(precision, MAX_PRECISION);
    }

    /**
     * Вычисление натурального логарифма с заданной точностью.
     * Используется разложение ln(1+x) = x - x^2/2 + x^3/3 - x^4/4 + ... для |x| < 1
     * @param x аргумент (должен быть положительным)
     * @return значение ln(x)
     * @throws IllegalArgumentException если точность вне допустимого диапазона
     */
    @Override
    public double compute(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("Аргумент натурального логарифма должен быть положительным");
        }

        // Для значений вне диапазона (0.5, 2) используем свойства ln(a*b) = ln(a) + ln(b)
        int factor = 0;
        while (x >= 2.0) {
            x /= 2.0;
            factor++;
        }
        while (x <= 0.5) {
            x *= 2.0;
            factor--;
        }

        // Преобразуем к виду ln(1 + y), где y = x - 1
        double y = x - 1.0;

        if (Math.abs(y) >= 1.0)
            throw new IllegalArgumentException("Значение y должно быть меньше 1 для сходимости ряда");

        // Ряд Тейлора для ln(1+y) = y - y^2/2 + y^3/3 - y^4/4 + ...
        double result = y;
        double term = y;
        int n = 2;

        while (Math.abs(term) > this.precision) {
            term *= -y;
            result += term / n;
            n++;
        }

        // Добавляем поправку за масштабирование
        // Вычислим самостоятельно ln(2) через ряд
        double log2 = calculateLog2();

        return result + factor * log2;
    }

    /**
     * Вычисление ln(2) = -ln(0.5) = -ln(1-0.5)
     * ln(1-x) = -x - x^2/2 - x^3/3 - ... для |x| < 1
     * @return значение ln(2)
     */
    private double calculateLog2() {
        double result = 0.0;
        double term = 0.5;
        int n = 1;
        while (Math.abs(term) > MAX_PRECISION) {
            result += term / n;
            term *= 0.5;
            n++;
        }
        
        return result;
    }

    @Override
    public String getName() {
        return "ln";
    }
}