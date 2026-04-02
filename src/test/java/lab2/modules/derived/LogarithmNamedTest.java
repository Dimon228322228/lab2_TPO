package lab2.modules.derived;

import lab2.modules.core.NaturalLogarithm;
import lab2.stubs.LogarithmNamedStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LogarithmNamedTest {

    private static final double PRECISION = 1e-10;
    private static LogarithmNamedStub logStub2;
    private static LogarithmNamedStub logStub10;
    private static LogarithmNamedStub logStub05;
    private LogarithmNamed logarithm;

    @BeforeAll
    static void setUpStubs() {
        logStub2 = new LogarithmNamedStub(2.0, PRECISION);
        logStub10 = new LogarithmNamedStub(10.0, PRECISION);
        logStub05 = new LogarithmNamedStub(0.5, PRECISION);
    }

    @BeforeEach
    void setUp() {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        logarithm = new LogarithmNamed(2.0, ln);
    }

    @Test
    void constructorValidBase() {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        assertDoesNotThrow(() -> new LogarithmNamed(2.0, ln));
        assertDoesNotThrow(() -> new LogarithmNamed(0.5, ln));
        assertDoesNotThrow(() -> new LogarithmNamed(10.0, ln));
    }

    private static NaturalLogarithm createLn() {
        return new NaturalLogarithm(PRECISION);
    }

    @Test
    void constructorInvalidBaseThrows() {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        assertThrows(IllegalArgumentException.class, () -> new LogarithmNamed(0.0, ln));
        assertThrows(IllegalArgumentException.class, () -> new LogarithmNamed(-1.0, ln));
        assertThrows(IllegalArgumentException.class, () -> new LogarithmNamed(1.0, ln));
    }

    @ParameterizedTest
    @MethodSource("provideValidArguments")
    void computeValidValues(double base, double x, double expected) {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        LogarithmNamed log = new LogarithmNamed(base, ln);
        double actual = log.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("log_%f(%f) expected %f but got %f", base, x, expected, actual));
    }

    private static Stream<Arguments> provideValidArguments() {
        return Stream.of(
                Arguments.of(2.0, 1.0, 0.0),
                Arguments.of(2.0, 2.0, 1.0),
                Arguments.of(2.0, 4.0, 2.0),
                Arguments.of(2.0, 8.0, 3.0),
                Arguments.of(10.0, 1.0, 0.0),
                Arguments.of(10.0, 10.0, 1.0),
                Arguments.of(10.0, 100.0, 2.0),
                Arguments.of(Math.E, Math.E, 1.0),
                Arguments.of(0.5, 0.5, 1.0),
                Arguments.of(0.5, 0.25, 2.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRandomValues")
    void computeRandomValues(double base, double x) {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        LogarithmNamed log = new LogarithmNamed(base, ln);
        double actual = log.compute(x);
        assertFalse(Double.isNaN(actual), "log should not return NaN");
        assertTrue(actual > 0 || actual < 0, "log should return non-zero value");
    }

    private static Stream<Arguments> provideRandomValues() {
        return Stream.of(
                Arguments.of(2.0, 3.0),
                Arguments.of(3.0, 5.0),
                Arguments.of(5.0, 7.0),
                Arguments.of(0.2, 0.7),
                Arguments.of(10.0, 0.5)
        );
    }

    @Test
    void computeNonPositiveXThrows() {
        assertThrows(IllegalArgumentException.class, () -> logarithm.compute(0.0));
        assertThrows(IllegalArgumentException.class, () -> logarithm.compute(-1.0));
    }

    @Test
    void getName() {
        assertTrue(logarithm.getName().startsWith("log_"));
        assertTrue(logarithm.getName().contains("2.0"));
    }

    @Test
    void getBase() {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        LogarithmNamed log = new LogarithmNamed(3.5, ln);
        assertEquals(3.5, log.getBase(), 1e-15);
    }

    @Test
    void testLogarithmNamedStubBasicValues() {
        LogarithmNamedStub logStub = new LogarithmNamedStub(2.0, PRECISION);
        assertEquals(0.0, logStub.compute(1.0), PRECISION);
        assertEquals(1.0, logStub.compute(2.0), PRECISION);
    }

    @Test
    void testLogarithmNamedStubGetName() {
        LogarithmNamedStub logStub = new LogarithmNamedStub(2.0, PRECISION);
        assertTrue(logStub.getName().startsWith("log_"));
    }

    @Test
    void testStubWithBase2() {
        assertEquals(0.0, logStub2.compute(1.0), PRECISION);
        assertEquals(1.0, logStub2.compute(2.0), PRECISION);
        assertEquals(2.0, logStub2.compute(4.0), PRECISION);
        assertEquals(3.0, logStub2.compute(8.0), PRECISION);
    }

    @Test
    void testStubWithBase10() {
        assertEquals(0.0, logStub10.compute(1.0), PRECISION);
        assertEquals(1.0, logStub10.compute(10.0), PRECISION);
        assertEquals(2.0, logStub10.compute(100.0), PRECISION);
    }

    @Test
    void testStubWithBase05() {
        assertEquals(1.0, logStub05.compute(0.5), PRECISION);
        assertEquals(2.0, logStub05.compute(0.25), PRECISION);
    }

    @Test
    void testStubWithCustomPrecision() {
        LogarithmNamedStub customStub = new LogarithmNamedStub(2.0, 1e-5);
        assertEquals(0.0, customStub.compute(1.0), 1e-5);
        assertEquals(1.0, customStub.compute(2.0), 1e-5);
    }

    @Test
    void testStubUsesMockitoMock() {
        assertNotNull(logStub2.getMock());
    }

    @Test
    void testStubMockReturnsCorrectValues() {
        assertEquals(0.0, logStub2.compute(1.0), PRECISION);
        assertEquals(1.0, logStub2.compute(2.0), PRECISION);
    }
}