package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Модуль для вычисления секанса с заданной точностью.
 */
public class SecantNamed implements NamedOutputFunction {
    private final Cosine cos;
    private final String name;

    public SecantNamed(Cosine cos) {
        this.cos = cos;
        this.name = "sec";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double compute(double x) {
        double cosVal = cos.compute(x);
        if (Math.abs(cosVal) < ZERO_THRESHOLD) {
            throw new ArithmeticException("Секанс не определен для данного аргумента");
        }
        return 1.0 / cosVal;
    }
}
