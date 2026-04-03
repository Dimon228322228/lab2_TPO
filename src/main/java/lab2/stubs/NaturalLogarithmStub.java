package lab2.stubs;

import lab2.modules.interfaces.NamedOutputFunction;

import java.util.HashMap;
import java.util.Map;

public class NaturalLogarithmStub implements NamedOutputFunction {
    
    private final double precision;
    private final Map<Double, Double> valueMap;
    
    public NaturalLogarithmStub(double precision) {
        this.precision = precision;
        this.valueMap = new HashMap<>();
        
        valueMap.put(0.5, -0.6931471805599453);
        valueMap.put(0.8, -0.22314355131420976);
        valueMap.put(1.0, 0.0);
        valueMap.put(1.2, 0.1823215567939546);
        valueMap.put(1.5, 0.4054651081081644);
        valueMap.put(2.0, 0.6931471805599453);
        valueMap.put(3.0, 1.0986122886681098);
        valueMap.put(5.0, 1.6094379124341003);
        valueMap.put(10.0, 2.302585092994046);
        valueMap.put(100.0, 4.605170185988092);
        valueMap.put(0.1, -2.302585092994046);
        valueMap.put(0.01, -4.605170185988092);
        valueMap.put(0.001, -6.907755278982137);
        valueMap.put(1e-6, -13.815510557964274);
        valueMap.put(Math.E, 1.0);
    }
    
    public NaturalLogarithmStub() {
        this(1e-10);
    }
    
    @Override
    public double compute(double x) {
        Double value = valueMap.get(x);
        if (value != null) {
            return value;
        }
        return Math.log(x);
    }
    
    @Override
    public String getName() {
        return "ln";
    }
}