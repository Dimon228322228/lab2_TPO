package lab2.stubs;

import lab2.modules.derived.CotangentNamed;
import lab2.modules.interfaces.NamedOutputFunction;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class CotangentNamedStub implements NamedOutputFunction {
    
    private final double precision;
    private final CotangentNamed mock;
    private final Map<Double, Double> valueMap;
    
    public CotangentNamedStub(double precision) {
        this.precision = precision;
        this.valueMap = new HashMap<>();
        
        valueMap.put(Math.PI / 6, 1.7320508075688774);
        valueMap.put(Math.PI / 4, 1.0);
        valueMap.put(Math.PI / 3, 0.5773502691896258);
        valueMap.put(Math.PI / 2, 0.0);
        valueMap.put(-Math.PI / 6, -1.7320508075688774);
        valueMap.put(-Math.PI / 4, -1.0);
        valueMap.put(-Math.PI / 3, -0.5773502691896258);
        valueMap.put(100 * Math.PI + 0.5, 0.0);
        
        this.mock = Mockito.mock(CotangentNamed.class);
        Mockito.when(mock.compute(Mockito.anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            Double result = valueMap.get(x);
            if (result != null) {
                return result;
            }
            return 1.0 / Math.tan(x);
        });
        Mockito.when(mock.getName()).thenReturn("cot");
    }
    
    public CotangentNamedStub() {
        this(1e-10);
    }
    
    @Override
    public double compute(double x) {
        return mock.compute(x);
    }
    
    @Override
    public String getName() {
        return mock.getName();
    }
    
    public CotangentNamed getMock() {
        return mock;
    }
}