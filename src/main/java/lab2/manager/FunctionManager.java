package lab2.manager;

import lab2.modules.interfaces.NamedOutputFunction;
import lab2.csv.CSVWriter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Менеджер для управления диапазоном, шагом и записью значений функции в CSV файл.
 * Использует ModuleOutputCSV для получения имени модуля и вычисления значений.
 * Теперь использует immutable Range для представления диапазона.
 */
public class FunctionManager {
    @Setter @Getter
    private NamedOutputFunction module;
    @Setter @Getter @NonNull
    private Range range;
    @Setter @Getter
    private Writer writer;

    /**
     * Конструктор с заданной точностью.
     * @param module модуль, реализующий ModuleOutputCSV
     */
    public FunctionManager(NamedOutputFunction module) {
        if (module == null)
            throw new IllegalArgumentException("Module не может быть null");
        this.module = module;
        this.writer = new CSVWriter();
    }

    /**
     * Генерирует данные функции для текущего диапазона.
     * @return список пар [x, f(x)]
     */
    private List<double[]> generateData() {

        List<double[]> data = new ArrayList<>();
        double x = range.start();
        double end = range.end();
        double step = range.step();

        while (x <= end + step * 0.000001) {
            try {
                double y = module.compute(x);
                data.add(new double[]{x, y});
            } catch (IllegalArgumentException e) {
                System.err.printf("Предупреждение: функция %s не определена в x=%.4f: %s%n",
                        module.getName(), x, e.getMessage());
            }
            x += step;
        }

        return data;
    }

    /**
     * Записывает данные в CSV файл с указанным разделителем.
     * @throws IOException если произошла ошибка записи
     * @throws IllegalStateException если диапазон не задан
     */
    public void writeData() throws IOException {
        List<double[]> data = generateData();
        String filename = "csv_" + module.getName() + ".csv";
        writer.write(data, filename);
        System.out.printf("Данные записаны в файл: %s (точек: %d)%n", filename, data.size());
    }
}