package lab2.manager;

import java.io.IOException;
import java.util.List;

public interface Writer {
    public void write(List<double[]> data, String filename) throws IOException;
}
