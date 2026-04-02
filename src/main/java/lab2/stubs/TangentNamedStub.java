package lab2.stubs;

import lab2.modules.interfaces.NamedOutputFunction;

import java.util.HashMap;
import java.util.Map;

public class TangentNamedStub implements NamedOutputFunction {
    
    private final double precision;
    private final Map<Double, Double> valueMap;
    
    public TangentNamedStub(double precision) {
        this.precision = precision;
        this.valueMap = new HashMap<>();
        
        valueMap.put(0.0, 0.0);
        valueMap.put(Math.PI / 6, 0.5773502691896258);
        valueMap.put(Math.PI / 4, 1.0);
        valueMap.put(Math.PI / 3, 1.7320508075688774);
        valueMap.put(-Math.PI / 6, -0.5773502691896258);
        valueMap.put(-Math.PI / 4, -1.0);
        valueMap.put(-Math.PI / 3, -1.7320508075688774);
        valueMap.put(100 * Math.PI + 0.1, 0.1);
    }
    
    public TangentNamedStub() {
        this(1e-10);
    }
    
    @Override
    public double compute(double x) {
        Double value = valueMap.get(x);
        if (value != null) {
            return value;
        }
        return Math.tan(x);
    }
    
    @Override
    public String getName() {
        return "tg";
    }
}