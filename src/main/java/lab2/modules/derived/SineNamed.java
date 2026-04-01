package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Модуль для вычисления синуса с заданной точностью.
 */
public class SineNamed implements NamedOutputFunction {
    private final Cosine cos;
    private final String name;

    public SineNamed(Cosine cos) {
        this.cos = cos;
        this.name = "sin";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double compute(double x) {
        return cos.compute(Math.PI / 2.0 - x);
    }
}