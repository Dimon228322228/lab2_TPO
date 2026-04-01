package lab2.modules.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NaturalLogarithmTest {

    private static final double PRECISION = 1e-10;

    @Test
    void constructorRespectsMaxPrecision() {
        NaturalLogarithm ln = new NaturalLogarithm(1e-20);
        // Should not throw
        assertDoesNotThrow(() -> ln.compute(1.5));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.5, 0.8, 1.0, 1.2, 1.5, 2.0, 3.0, 5.0, 10.0, 100.0})
    void computePositiveValues(double x) {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        double expected = Math.log(x);
        double actual = ln.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("ln(%f) expected %f but got %f", x, expected, actual));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.01, 0.001, 1e-6})
    void computeSmallPositiveValues(double x) {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        double expected = Math.log(x);
        double actual = ln.compute(x);
        assertEquals(expected, actual, PRECISION * 10, // allow larger relative error for very small values
                String.format("ln(%f) expected %f but got %f", x, expected, actual));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -5.0})
    void computeNonPositiveThrows(double x) {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        assertThrows(IllegalArgumentException.class, () -> ln.compute(x),
                String.format("ln(%f) should throw IllegalArgumentException", x));
    }

    @ParameterizedTest
    @MethodSource("provideEdgeCases")
    void computeEdgeCases(double x, double expected) {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        double actual = ln.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("ln(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideEdgeCases() {
        return Stream.of(
                Arguments.of(Math.E, 1.0),
                Arguments.of(1.0, 0.0),
                Arguments.of(2.0, Math.log(2.0))
        );
    }

    @Test
    void computeWithHighPrecision() {
        NaturalLogarithm ln = new NaturalLogarithm(1e-15);
        double x = 1.2345;
        double expected = Math.log(x);
        double actual = ln.compute(x);
        assertEquals(expected, actual, 1e-14);
    }

    @Test
    void getName() {
        NaturalLogarithm ln = new NaturalLogarithm(PRECISION);
        assertEquals("ln", ln.getName());
    }
}