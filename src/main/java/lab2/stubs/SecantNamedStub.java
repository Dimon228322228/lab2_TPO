package lab2.stubs;

import lab2.modules.derived.SecantNamed;
import lab2.modules.interfaces.NamedOutputFunction;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class SecantNamedStub implements NamedOutputFunction {
    
    private final double precision;
    private final SecantNamed mock;
    private final Map<Double, Double> valueMap;
    
    public SecantNamedStub(double precision) {
        this.precision = precision;
        this.valueMap = new HashMap<>();
        
        valueMap.put(0.0, 1.0);
        valueMap.put(Math.PI / 6, 1.1547005383792515);
        valueMap.put(Math.PI / 4, 1.4142135623730951);
        valueMap.put(Math.PI / 3, 2.0);
        valueMap.put(-Math.PI / 6, 1.1547005383792515);
        valueMap.put(-Math.PI / 4, 1.4142135623730951);
        valueMap.put(-Math.PI / 3, 2.0);
        valueMap.put(Math.PI, -1.0);
        valueMap.put(100 * Math.PI + 0.1, 1.0);
        
        this.mock = Mockito.mock(SecantNamed.class);
        Mockito.when(mock.compute(Mockito.anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            Double result = valueMap.get(x);
            if (result != null) {
                return result;
            }
            return 1.0 / Math.cos(x);
        });
        Mockito.when(mock.getName()).thenReturn("sec");
    }
    
    public SecantNamedStub() {
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
    
    public SecantNamed getMock() {
        return mock;
    }
}