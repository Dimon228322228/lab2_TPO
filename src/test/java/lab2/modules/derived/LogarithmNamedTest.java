package lab2.modules.derived;

import lab2.modules.core.NaturalLogarithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LogarithmNamedTest {

    private static final double PRECISION = 1e-10;

    private LogarithmNamed createLogarithm(double base) {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        return new LogarithmNamed(base, ln);
    }

    @Test
    void constructorValidBase() {
        assertDoesNotThrow(() -> createLogarithm(2.0));
        assertDoesNotThrow(() -> createLogarithm(0.5));
        assertDoesNotThrow(() -> createLogarithm(10.0));
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
        LogarithmNamed log = createLogarithm(base);
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
        LogarithmNamed log = createLogarithm(base);
        double expected = Math.log(x) / Math.log(base);
        double actual = log.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("log_%f(%f) expected %f but got %f", base, x, expected, actual));
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
        LogarithmNamed log = createLogarithm(2.0);
        assertThrows(IllegalArgumentException.class, () -> log.compute(0.0));
        assertThrows(IllegalArgumentException.class, () -> log.compute(-1.0));
    }

    @Test
    void getName() {
        LogarithmNamed log = createLogarithm(2.0);
        assertTrue(log.getName().startsWith("log_"));
        // name should contain base
        assertTrue(log.getName().contains("2.0"));
    }

    @Test
    void getBase() {
        LogarithmNamed log = createLogarithm(3.5);
        assertEquals(3.5, log.getBase(), 1e-15);
    }
}