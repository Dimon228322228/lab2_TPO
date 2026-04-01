package lab2.modules.derived;

import lab2.modules.core.NaturalLogarithm;
import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Модуль функции логарифма по произвольному основанию, реализующий интерфейс ModuleOutputCSV.
 * Делегирует вычисления калькулятору LogarithmCalculator.
 */
public final class LogarithmNamed implements NamedOutputFunction {
    private final NaturalLogarithm calculator;
    private final double base;
    private final String name;

    /**
     * @param base - основание логарифма
     */
    public LogarithmNamed(double base, NaturalLogarithm ln) {
        this.calculator = ln;
        if (base <= 0 || Math.abs(base - 1.0) < 1e-15) {
            throw new IllegalArgumentException("Основание логарифма должно быть положительным и не равным 1");
        }
        this.base = base;
        this.name = "log_" + base;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double compute(double x) {
        double lnX = calculator.compute(x);
        double lnBase = calculator.compute(base);
        return lnX / lnBase;
    }

    /**
     * Возвращает основание логарифма.
     */
    public double getBase() {
        return base;
    }
}