package lab2.modules.interfaces;

public interface Computable {
    public final double ZERO_THRESHOLD = 1e-10;
    public final double MAX_PRECISION = 1e-15;

    /**
     * Вычисление значения функции в точке x с заданной точностью.
     * @param x аргумент функции
     * @return значение функции
     */
    double compute(double x);
}
