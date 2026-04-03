package lab2.modules.derived;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lab2.stubs.ComplexNamedStub;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNamedTest {

    private static final double PRECISION = 1e-10;
    private static ComplexNamed complex;
    private static ComplexNamedStub complexStub;

    @BeforeAll
    static void setUp() {
        complex = new ComplexNamed(PRECISION);
        complexStub = new ComplexNamedStub(PRECISION);
    }

    @Test
    void constructor() {
        assertNotNull(complex);
        assertEquals("complex", complex.getName());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.5, -Math.PI / 4, -Math.PI / 2 + 0.1, -Math.PI + 0.1})
    void computeTrigPart(double x) {
        double actual = complex.compute(x);
        assertFalse(Double.isNaN(actual), "complex should not return NaN for trig part");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.5, 1.5, 2.0, 3.0, 5.0, 10.0})
    void computeLogPart(double x) {
        double actual = complex.compute(x);
        assertFalse(Double.isNaN(actual), "complex should not return NaN for log part");
    }

    @ParameterizedTest
    @MethodSource("provideSingularPoints")
    void computeAtSingularPointsThrows(double x) {
        assertThrows(ArithmeticException.class, () -> complex.compute(x),
                String.format("ln(%f) should throw ArithmeticException", x));
    }

    private static Stream<Arguments> provideSingularPoints() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(-Math.PI)
        );
    }

    @Test
    void computeNearSingularities() {
        double x = 1.0 + 1e-5;
        double actual = complex.compute(x);
        assertFalse(Double.isNaN(actual), "complex should not return NaN near singularity");
    }

    @Test
    void computeTransitionAtZero() {
        double neg = complex.compute(-0.001);
        double pos = complex.compute(0.001);
        assertFalse(Double.isNaN(neg));
        assertFalse(Double.isNaN(pos));
    }

    @Test
    void getName() {
        assertEquals("complex", complex.getName());
    }

    @Test
    void testComplexNamedStubBasicValues() {
        assertFalse(Double.isNaN(complexStub.compute(1.0)));
        assertFalse(Double.isNaN(complexStub.compute(-1.0)));
    }

    @Test
    void testComplexNamedStubGetName() {
        assertEquals("complex", complexStub.getName());
    }

    @Test
    void testComplexNamedStubVsReal() {
        assertFalse(Double.isNaN(complexStub.compute(1.0)), "stub result should not be NaN");
        assertFalse(Double.isNaN(complexStub.compute(-1.0)), "stub result for negative should not be NaN");
        assertFalse(Double.isNaN(complexStub.compute(0.5)), "stub result for positive should not be NaN");
    }

    @Test
    void testStubWithCustomPrecision() {
        ComplexNamedStub customStub = new ComplexNamedStub(1e-5);
        assertFalse(Double.isNaN(customStub.compute(1.0)));
        assertFalse(Double.isNaN(customStub.compute(-1.0)));
    }

    @Test
    void testStubReturnsConsistentValues() {
        double result1 = complexStub.compute(1.0);
        double result2 = complexStub.compute(1.0);
        assertEquals(result1, result2, PRECISION);
    }

    @Test
    void testStubUsesMockitoMock() {
        assertNotNull(complexStub.getMock());
    }

    @Test
    void testStubMockReturns1ForVariousInputs() {
        assertEquals(1.0, complexStub.compute(1.0), PRECISION);
        assertEquals(1.0, complexStub.compute(-1.0), PRECISION);
        assertEquals(1.0, complexStub.compute(0.5), PRECISION);
    }
}