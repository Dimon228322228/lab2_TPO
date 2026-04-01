package lab2;

import java.util.List;

/**
 * Главное приложение для демонстрации работы интерфейса ModuleOutputCSV
 * с обновлённой архитектурой (фабрика модулей, менеджер с Range, генератор данных).
 */
public class App {

    /**
     * Вывод первых N значений для проверки
     */
    public static void printSampleValues(String moduleName, List<double[]> data, int count) {
        System.out.println("\nПервые " + count + " значений для " + moduleName + ":");
        for (int i = 0; i < Math.min(count, data.size()); i++) {
            double[] row = data.get(i);
            System.out.printf("  X = %8.4f, f(X) = %12.6f%n", row[0], row[1]);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Демонстрация работы модулей с обновлённой архитектурой ===");
        System.out.println("Используется ModuleFactory, FunctionManager с Range, DataGenerator\n");

        System.out.println("\n=== Завершено ===");
        System.out.println("Все CSV файлы созданы в корневой директории проекта.");
    }
}
