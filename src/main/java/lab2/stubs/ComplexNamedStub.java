package lab2.stubs;

import lab2.modules.derived.ComplexNamed;
import lab2.modules.interfaces.NamedOutputFunction;
import org.mockito.Mockito;

public class ComplexNamedStub implements NamedOutputFunction {
    
    private final double precision;
    private final ComplexNamed mock;
    
    public ComplexNamedStub(double precision) {
        this.precision = precision;
        
        this.mock = Mockito.mock(ComplexNamed.class);
        Mockito.when(mock.compute(Mockito.anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            if (x < 0) {
                return 1.0;
            } else if (x > 0 && x != 1.0) {
                return 1.0;
            }
            return 1.0;
        });
        Mockito.when(mock.getName()).thenReturn("complex");
    }
    
    public ComplexNamedStub() {
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
    
    public ComplexNamed getMock() {
        return mock;
    }
}