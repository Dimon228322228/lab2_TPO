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
        Range commonTrigonometricRange = new Range(-4 * PI, 4 * PI, 0.00001);
        Range logsRange = new Range(0.01, 50.0, 0.05);

        // 1. Cosine (ядро)
        Cosine cosine = new Cosine(DEFAULT_PRECISION);
        FunctionManager cosineManager = new FunctionManager(cosine);
        generateCSV(cosineManager, commonTrigonometricRange, "косинуса");

        // 2. NaturalLogarithm (ядро)
        NaturalLogarithm ln = new NaturalLogarithm(DEFAULT_PRECISION);
        FunctionManager lnManager = new FunctionManager(ln);
        generateCSV(lnManager, logsRange, "натурального логарифма");

        // 3. SineNamed (производный от косинуса)
        SineNamed sine = new SineNamed(cosine);
        FunctionManager sineManager = new FunctionManager(sine);
        generateCSV(sineManager, commonTrigonometricRange, "синуса");

        // 4. TangentNamed (тангенс)
        TangentNamed tangent = new TangentNamed(cosine, sine);
        FunctionManager tangentManager = new FunctionManager(tangent);
        generateCSV(tangentManager, commonTrigonometricRange, "тангенса");

        // 5. CotangentNamed (котангенс)
        CotangentNamed cotangent = new CotangentNamed(cosine, sine);
        FunctionManager cotangentManager = new FunctionManager(cotangent);
        generateCSV(cotangentManager, commonTrigonometricRange, "котангенса");

        // 6. SecantNamed (секанс)
        SecantNamed secant = new SecantNamed(cosine);
        FunctionManager secantManager = new FunctionManager(secant);
        generateCSV(secantManager, commonTrigonometricRange, "секанса");

        // 7. CosecantNamed (косеканс)
        CosecantNamed cosecant = new CosecantNamed(sine);
        FunctionManager cosecantManager = new FunctionManager(cosecant);
        generateCSV(cosecantManager, commonTrigonometricRange, "косеканса");

        // 8. LogarithmNamed с основанием 2
        LogarithmNamed log2 = new LogarithmNamed(2.0, ln);
        FunctionManager log2Manager = new FunctionManager(log2);
        generateCSV(log2Manager, logsRange, "логарифма по основанию 2");

        // 9. LogarithmNamed с основанием 10
        LogarithmNamed log10 = new LogarithmNamed(10.0, ln);
        FunctionManager log10Manager = new FunctionManager(log10);
        generateCSV(log10Manager, logsRange, "логарифма по основанию 10");

        // 10. ComplexNamed (комплексная функция)
        ComplexNamed complex = new ComplexNamed(DEFAULT_PRECISION);
        FunctionManager complexManager = new FunctionManager(complex);
        generateCSV(complexManager, commonTrigonometricRange, "комплексной функции (отрицательные)");

        System.out.println("\n=== Завершено ===");
        System.out.println("Все CSV файлы созданы в корневой директории проекта.");
        System.out.println("Имена файлов: csv_<имя_модуля>.csv");
    }
}
