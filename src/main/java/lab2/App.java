package lab2;

import lab2.manager.FunctionManager;
import lab2.manager.Range;
import lab2.modules.core.Cosine;
import lab2.modules.core.NaturalLogarithm;
import lab2.modules.derived.*;

import java.io.IOException;

/**
 * Главное приложение для генерации CSV файлов с данными функций.
 * Создаёт CSV файлы для каждого модуля в интервале, показывающем основное поведение графика.
 * Шаг выбран достаточно маленьким для построения графиков.
 */
public class App {

    private static final double PI = Math.PI;
    private static final double DEFAULT_PRECISION = 1e-10;

    /**
     * Генерирует CSV файл для модуля с заданным диапазоном.
     * @param manager менеджер функции (уже содержит модуль)
     * @param range диапазон вычислений
     * @param description описание для вывода
     */
    private static void generateCSV(FunctionManager manager, Range range, String description) {
        manager.setRange(range);
        System.out.printf("Генерация %s в диапазоне %s...%n", description, range);
        try {
            manager.writeData();
        } catch (IOException e) {
            System.err.printf("Ошибка записи CSV для %s: %s%n", description, e.getMessage());
        } catch (Exception e) {
            System.err.printf("Ошибка вычислений для %s: %s%n", description, e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Генерация CSV файлов для всех модулей ===");
        System.out.println("Цель: создать файлы с данными для построения графиков.\n");

        // 1. Cosine (ядро)
        Cosine cosine = new Cosine(DEFAULT_PRECISION);
        FunctionManager cosineManager = new FunctionManager(cosine);
        Range cosineRange = new Range(0.0, 2 * PI, 0.01);
        generateCSV(cosineManager, cosineRange, "косинуса");

        // 2. NaturalLogarithm (ядро)
        NaturalLogarithm ln = new NaturalLogarithm(DEFAULT_PRECISION);
        FunctionManager lnManager = new FunctionManager(ln);
        Range lnRange = new Range(0.01, 10.0, 0.05);
        generateCSV(lnManager, lnRange, "натурального логарифма");

        // 3. SineNamed (производный от косинуса)
        SineNamed sine = new SineNamed(cosine);
        FunctionManager sineManager = new FunctionManager(sine);
        Range sineRange = new Range(0.0, 2 * PI, 0.01);
        generateCSV(sineManager, sineRange, "синуса");

        // 4. TangentNamed (тангенс)
        TangentNamed tangent = new TangentNamed(cosine, sine);
        FunctionManager tangentManager = new FunctionManager(tangent);
        // Интервал, избегающий сингулярностей в ±π/2
        double tangentStart = -PI/2 + 0.1;
        double tangentEnd = PI/2 - 0.1;
        Range tangentRange = new Range(tangentStart, tangentEnd, 0.01);
        generateCSV(tangentManager, tangentRange, "тангенса");

        // 5. CotangentNamed (котангенс)
        CotangentNamed cotangent = new CotangentNamed(cosine, sine);
        FunctionManager cotangentManager = new FunctionManager(cotangent);
        // Интервал, избегающий сингулярностей в 0 и π
        double cotangentStart = 0.1;
        double cotangentEnd = PI - 0.1;
        Range cotangentRange = new Range(cotangentStart, cotangentEnd, 0.01);
        generateCSV(cotangentManager, cotangentRange, "котангенса");

        // 6. SecantNamed (секанс)
        SecantNamed secant = new SecantNamed(cosine);
        FunctionManager secantManager = new FunctionManager(secant);
        // Тот же интервал, что и для тангенса (избегаем нулей косинуса)
        Range secantRange = new Range(tangentStart, tangentEnd, 0.01);
        generateCSV(secantManager, secantRange, "секанса");

        // 7. CosecantNamed (косеканс)
        CosecantNamed cosecant = new CosecantNamed(sine);
        FunctionManager cosecantManager = new FunctionManager(cosecant);
        // Тот же интервал, что и для котангенса (избегаем нулей синуса)
        Range cosecantRange = new Range(cotangentStart, cotangentEnd, 0.01);
        generateCSV(cosecantManager, cosecantRange, "косеканса");

        // 8. LogarithmNamed с основанием 2
        LogarithmNamed log2 = new LogarithmNamed(2.0, ln);
        FunctionManager log2Manager = new FunctionManager(log2);
        Range log2Range = new Range(0.01, 10.0, 0.05);
        generateCSV(log2Manager, log2Range, "логарифма по основанию 2");

        // 9. LogarithmNamed с основанием 10
        LogarithmNamed log10 = new LogarithmNamed(10.0, ln);
        FunctionManager log10Manager = new FunctionManager(log10);
        Range log10Range = new Range(0.01, 10.0, 0.05);
        generateCSV(log10Manager, log10Range, "логарифма по основанию 10");

        // 10. ComplexNamed (комплексная функция)
        ComplexNamed complex = new ComplexNamed(DEFAULT_PRECISION);
        FunctionManager complexManager = new FunctionManager(complex);
        // Два интервала: отрицательные и положительные значения, избегая сингулярностей 0 и 1
        // Отрицательная сторона
        Range complexRangeNeg = new Range(-PI, -0.05, 0.05);
        generateCSV(complexManager, complexRangeNeg, "комплексной функции (отрицательные)");
        // Положительная сторона (исключая окрестность 1)
        // Разделим на два подынтервала: [0.05, 0.95] и [1.05, 10.0]
        Range complexRangePos1 = new Range(0.05, 0.95, 0.05);
        generateCSV(complexManager, complexRangePos1, "комплексной функции (положительные до 1)");
        Range complexRangePos2 = new Range(1.05, 10.0, 0.05);
        generateCSV(complexManager, complexRangePos2, "комплексной функции (положительные после 1)");

        System.out.println("\n=== Завершено ===");
        System.out.println("Все CSV файлы созданы в корневой директории проекта.");
        System.out.println("Имена файлов: csv_<имя_модуля>.csv");
    }
}
