package lab2.csv;

import lab2.manager.Writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CSVWriter implements Writer {

    private final String DEFAULT_SEPARATOR = ",";

    @Override
    public void write(List<double[]> data, String filename) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filename),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.append("X").append(DEFAULT_SEPARATOR).append("f(X)").append("\n");

            for (double[] row : data) {
                writer.append(String.valueOf(row[0]))
                        .append(DEFAULT_SEPARATOR)
                        .append(String.valueOf(row[1]))
                        .append("\n");
            }
        }
    }
}