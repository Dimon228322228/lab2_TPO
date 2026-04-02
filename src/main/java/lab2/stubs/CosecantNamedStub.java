package lab2.stubs;

import lab2.modules.derived.CosecantNamed;
import lab2.modules.interfaces.NamedOutputFunction;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class CosecantNamedStub implements NamedOutputFunction {
    
    private final double precision;
    private final CosecantNamed mock;
    private final Map<Double, Double> valueMap;
    
    public CosecantNamedStub(double precision) {
        this.precision = precision;
        this.valueMap = new HashMap<>();
        
        valueMap.put(Math.PI / 6, 2.0);
        valueMap.put(Math.PI / 4, 1.4142135623730951);
        valueMap.put(Math.PI / 3, 1.1547005383792515);
        valueMap.put(Math.PI / 2, 1.0);
        valueMap.put(-Math.PI / 6, -2.0);
        valueMap.put(-Math.PI / 4, -1.4142135623730951);
        valueMap.put(-Math.PI / 3, -1.1547005383792515);
        valueMap.put(100 * Math.PI + 0.5, 0.0);
        
        this.mock = Mockito.mock(CosecantNamed.class);
        Mockito.when(mock.compute(Mockito.anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            Double result = valueMap.get(x);
            if (result != null) {
                return result;
            }
            return 1.0 / Math.sin(x);
        });
        Mockito.when(mock.getName()).thenReturn("csc");
    }
    
    public CosecantNamedStub() {
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
    
    public CosecantNamed getMock() {
        return mock;
    }
}