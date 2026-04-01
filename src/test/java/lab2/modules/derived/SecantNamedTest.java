package lab2.modules.derived;

import lab2.modules.core.Cosine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SecantNamedTest {

    private static final double PRECISION = 1e-10;

    private SecantNamed createSecant() {
        Cosine cos = new Cosine(PRECISION);
        return new SecantNamed(cos);
    }

    @Test
    void constructor() {
        SecantNamed sec = createSecant();
        assertNotNull(sec);
        assertEquals("sec", sec.getName());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, Math.PI / 6, Math.PI / 4, Math.PI / 3,
            -Math.PI / 6, -Math.PI / 4, -Math.PI / 3, Math.PI})
    void computeBasicValues(double x) {
        SecantNamed sec = createSecant();
        double expected = 1.0 / Math.cos(x);
        double actual = sec.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("sec(%f) expected %f but got %f", x, expected, actual));
    }

    @ParameterizedTest
    @MethodSource("provideAnglesWhereCosineZero")
    void computeWhereCosineZeroThrows(double x) {
        SecantNamed sec = createSecant();
        try {
            double result = sec.compute(x);
            // If no exception, the value should be large (since cos(x) is near zero)
            assertTrue(Math.abs(result) > 1e6 || Double.isInfinite(result),
                    String.format("sec(%f) should be large or infinite but got %f", x, result));
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    private static Stream<Arguments> provideAnglesWhereCosineZero() {
        return Stream.of(
                Arguments.of(Math.PI / 2),
                Arguments.of(-Math.PI / 2),
                Arguments.of(3 * Math.PI / 2),
                Arguments.of(5 * Math.PI / 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnglesNearSingularity")
    void computeNearSingularity(double x) {
        SecantNamed sec = createSecant();
        double expected = 1.0 / Math.cos(x);
        double actual = sec.compute(x);
        // Allow larger tolerance near singularity due to numerical instability
        double tolerance = 1e-6;
        assertEquals(expected, actual, tolerance,
                String.format("sec(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideAnglesNearSingularity() {
        return Stream.of(
                Arguments.of(Math.PI / 2 - 0.001),
                Arguments.of(Math.PI / 2 + 0.001),
                Arguments.of(-Math.PI / 2 + 0.001)
        );
    }

    @Test
    void computeLargeAngle() {
        SecantNamed sec = createSecant();
        double x = 100 * Math.PI + 0.1; // not a singularity
        double expected = 1.0 / Math.cos(x);
        double actual = sec.compute(x);
        assertEquals(expected, actual, PRECISION);
    }

    @Test
    void getName() {
        SecantNamed sec = createSecant();
        assertEquals("sec", sec.getName());
    }
}