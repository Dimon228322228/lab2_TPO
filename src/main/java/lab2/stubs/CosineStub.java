package lab2.stubs;

import lab2.modules.interfaces.NamedOutputFunction;

import java.util.HashMap;
import java.util.Map;

public class CosineStub implements NamedOutputFunction {
    
    private final double precision;
    private final Map<Double, Double> valueMap;
    
    public CosineStub(double precision) {
        this.precision = precision;
        this.valueMap = new HashMap<>();
        
        valueMap.put(0.0, 1.0);
        valueMap.put(Math.PI / 6, 0.8660254037844386);
        valueMap.put(Math.PI / 4, 0.7071067811865476);
        valueMap.put(Math.PI / 3, 0.5);
        valueMap.put(Math.PI / 2, 0.0);
        valueMap.put(Math.PI, -1.0);
        valueMap.put(3 * Math.PI / 2, 0.0);
        valueMap.put(2 * Math.PI, 1.0);
        valueMap.put(-Math.PI / 4, 0.7071067811865476);
        valueMap.put(-Math.PI, -1.0);
        valueMap.put(100 * Math.PI, 1.0);
        valueMap.put(-100 * Math.PI, 1.0);
        valueMap.put(1000.0, 0.56);
        valueMap.put(-1000.0, 0.56);
    }
    
    public CosineStub() {
        this(1e-10);
    }
    
    @Override
    public double compute(double x) {
        Double value = valueMap.get(x);
        if (value != null) {
            return value;
        }
        for (Map.Entry<Double, Double> entry : valueMap.entrySet()) {
            if (Math.abs(entry.getKey() - x) < precision) {
                return entry.getValue();
            }
        }
        return Math.cos(x);
    }
    
    @Override
    public String getName() {
        return "cos";
    }
}