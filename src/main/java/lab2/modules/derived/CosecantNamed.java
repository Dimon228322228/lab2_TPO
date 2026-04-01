package lab2.modules.derived;

import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Модуль для вычисления косеканса с заданной точностью.
 */
public class CosecantNamed implements NamedOutputFunction {
    private final SineNamed sin;
    private final String name;

    public CosecantNamed(SineNamed sin) {
        this.sin = sin;
        this.name = "csc";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double compute(double x) {
        double sinVal = sin.compute(x);
        if (Math.abs(sinVal) < ZERO_THRESHOLD) {
            throw new ArithmeticException("Косеканс не определен для данного аргумента");
        }
        return 1.0 / sinVal;
    }
}
