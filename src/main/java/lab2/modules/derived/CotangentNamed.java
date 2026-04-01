package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Модуль для вычисления котангенса с заданной точностью.
 */
public class CotangentNamed implements NamedOutputFunction {
    private final Cosine cos;
    private final SineNamed sin;
    private final String name;

    public CotangentNamed(Cosine cos, SineNamed sin) {
        this.cos = cos;
        this.sin = sin;
        this.name = "cot";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double compute(double x) {
        double sinVal = sin.compute(x);
        if (Math.abs(sinVal) < ZERO_THRESHOLD) {
            throw new ArithmeticException("Котангенс не определен для данного аргумента");
        }
        return cos.compute(x) / sinVal;
    }
}
