package lab2.modules.derived;

import lab2.modules.core.Cosine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TangentNamedTest {

    private static final double PRECISION = 1e-10;

    private TangentNamed createTangent() {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        return new TangentNamed(cos, sin);
    }

    @Test
    void constructor() {
        TangentNamed tan = createTangent();
        assertNotNull(tan);
        assertEquals("tg", tan.getName());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, Math.PI / 6, Math.PI / 4, Math.PI / 3,
            -Math.PI / 6, -Math.PI / 4, -Math.PI / 3})
    void computeBasicValues(double x) {
        TangentNamed tan = createTangent();
        double expected = Math.tan(x);
        double actual = tan.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("tan(%f) expected %f but got %f", x, expected, actual));
    }

    @ParameterizedTest
    @MethodSource("provideAnglesWhereCosineZero")
    void computeWhereCosineZeroThrows(double x) {
        TangentNamed tan = createTangent();
        try {
            double result = tan.compute(x);
            // If no exception, the value should be large (since cos(x) is near zero)
            assertTrue(Math.abs(result) > 1e6 || Double.isInfinite(result),
                    String.format("tan(%f) should be large or infinite but got %f", x, result));
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
        TangentNamed tan = createTangent();
        double expected = Math.tan(x);
        double actual = tan.compute(x);
        // Allow larger tolerance near singularity due to numerical instability
        double tolerance = 1e-6;
        assertEquals(expected, actual, tolerance,
                String.format("tan(%f) expected %f but got %f", x, expected, actual));
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
        TangentNamed tan = createTangent();
        double x = 100 * Math.PI + 0.1; // not a singularity
        double expected = Math.tan(x);
        double actual = tan.compute(x);
        assertEquals(expected, actual, PRECISION);
    }

    @Test
    void getName() {
        TangentNamed tan = createTangent();
        assertEquals("tg", tan.getName());
    }
}