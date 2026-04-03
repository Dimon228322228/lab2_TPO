package lab2.stubs;

import lab2.modules.derived.LogarithmNamed;
import lab2.modules.interfaces.NamedOutputFunction;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class LogarithmNamedStub implements NamedOutputFunction {
    
    private final double precision;
    private final LogarithmNamed mock;
    private final double base;
    private final Map<Double, Double> valueMap;
    
    public LogarithmNamedStub(double base, double precision) {
        this.precision = precision;
        this.base = base;
        this.valueMap = new HashMap<>();
        
        if (base == 2.0) {
            valueMap.put(1.0, 0.0);
            valueMap.put(2.0, 1.0);
            valueMap.put(4.0, 2.0);
            valueMap.put(8.0, 3.0);
        } else if (base == 10.0) {
            valueMap.put(1.0, 0.0);
            valueMap.put(10.0, 1.0);
            valueMap.put(100.0, 2.0);
        } else if (base == 0.5) {
            valueMap.put(0.5, 1.0);
            valueMap.put(0.25, 2.0);
        } else if (base == Math.E) {
            valueMap.put(Math.E, 1.0);
        }
        
        this.mock = Mockito.mock(LogarithmNamed.class);
        Mockito.when(mock.compute(Mockito.anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            Double result = valueMap.get(x);
            if (result != null) {
                return result;
            }
            return Math.log(x) / Math.log(base);
        });
        Mockito.when(mock.getName()).thenReturn("log_" + base);
    }
    
    public LogarithmNamedStub() {
        this(2.0, 1e-10);
    }
    
    @Override
    public double compute(double x) {
        return mock.compute(x);
    }
    
    @Override
    public String getName() {
        return mock.getName();
    }
    
    public LogarithmNamed getMock() {
        return mock;
    }
    
    public double getBase() {
        return base;
    }
}