package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.modules.core.NaturalLogarithm;
import lab2.modules.interfaces.NamedOutputFunction;

/**
 * Модуль для вычисления заданной сложной функции
 * x <= 0 : (((((cos(x) + csc(x)) * sec(x)) * csc(x)) / sin(x)) / (cot(x) / csc(x)))
 * x > 0 : (((((log_10(x) ^ 3) - log_3(x)) / log_10(x)) / ln(x)) ^ 3)
 */
public class ComplexNamed implements NamedOutputFunction {
    private final String name;
    private final Cosine cos;
    private final SineNamed sin;
    private final CosecantNamed csc;
    private final SecantNamed sec;
    private final CotangentNamed ctg;
    private final NaturalLogarithm ln;
    private final LogarithmNamed log3;
    private final LogarithmNamed log10;

    /**
     * @param precision - точность для вычислений косинуса, смотри {@link Cosine}
     */
    public ComplexNamed(double precision) {
        this.cos = new Cosine(precision);
        this.sin = new SineNamed(this.cos);
        this.csc = new CosecantNamed(this.sin);
        this.sec = new SecantNamed(this.cos);
        this.ctg = new CotangentNamed(this.cos, this.sin);
        this.ln = new NaturalLogarithm(precision);
        this.log3 = new LogarithmNamed(3, ln);
        this.log10 = new LogarithmNamed(10, ln);
        this.name = "complex";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double compute(double x) {
        if (x <= 0) {
            // Для x <= 0 используем тригонометрическую часть с заданной точностью
            double cosX = cos.compute(x);
            double sinX = sin.compute(x);
            double cscX = csc.compute(x);
            double secX = sec.compute(x);
            double cotX = ctg.compute(x);
            
            // Вычисляем выражение по формуле
            double numerator = ((((cosX + cscX) * secX) * cscX) / sinX);
            double denominator = cotX / cscX;
            
            return numerator / denominator;
        } else {
            // Для x > 0 используем логарифмическую часть с заданной точностью
            double log10X = log10.compute(x);
            double log3X = log3.compute(x);
            double lnX = ln.compute(x);
            
            // Вычисляем выражение по формуле
            double numerator = (Math.pow(log10X, 3) - log3X) / log10X;
            
            return Math.pow(numerator / lnX, 3);
        }
    }
}
