package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Модуль для вычисления тангенса с заданной точностью.
 */
public class TangentNamed implements NamedOutputFunction {
    private final Cosine cos;
    private final SineNamed sin;
    private final String name;

    public TangentNamed(Cosine cos, SineNamed sin) {
        this.cos = cos;
        this.sin = sin;
        this.name = "tg";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double compute(double x) {
        double cosVal = cos.compute(x);
        if (Math.abs(cosVal) < ZERO_THRESHOLD) {
            throw new ArithmeticException("Тангенс не определен для данного аргумента");
        }
        return sin.compute(x) / cosVal;
    }
}
