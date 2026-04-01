package lab2.modules.derived;

import lab2.modules.core.Cosine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CosecantNamedTest {

    private static final double PRECISION = 1e-10;

    private CosecantNamed createCosecant() {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        return new CosecantNamed(sin);
    }

    @Test
    void constructor() {
        CosecantNamed csc = createCosecant();
        assertNotNull(csc);
        assertEquals("csc", csc.getName());
    }

    @ParameterizedTest
    @ValueSource(doubles = {Math.PI / 6, Math.PI / 4, Math.PI / 3, Math.PI / 2,
            -Math.PI / 6, -Math.PI / 4, -Math.PI / 3})
    void computeBasicValues(double x) {
        CosecantNamed csc = createCosecant();
        double expected = 1.0 / Math.sin(x);
        double actual = csc.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("csc(%f) expected %f but got %f", x, expected, actual));
    }

    @ParameterizedTest
    @MethodSource("provideAnglesWhereSineZero")
    void computeWhereSineZeroThrows(double x) {
        CosecantNamed csc = createCosecant();
        try {
            double result = csc.compute(x);
            // If no exception, the value should be large (since sin(x) is near zero)
            assertTrue(Math.abs(result) > 1e6 || Double.isInfinite(result),
                    String.format("csc(%f) should be large or infinite but got %f", x, result));
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    private static Stream<Arguments> provideAnglesWhereSineZero() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(Math.PI),
                Arguments.of(-Math.PI),
                Arguments.of(2 * Math.PI)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnglesNearSingularity")
    void computeNearSingularity(double x) {
        CosecantNamed csc = createCosecant();
        double expected = 1.0 / Math.sin(x);
        double actual = csc.compute(x);
        // Allow larger tolerance near singularity due to numerical instability
        double tolerance = 1e-6;
        assertEquals(expected, actual, tolerance,
                String.format("csc(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideAnglesNearSingularity() {
        return Stream.of(
                Arguments.of(0.001),
                Arguments.of(-0.001),
                Arguments.of(Math.PI - 0.001),
                Arguments.of(Math.PI + 0.001)
        );
    }

    @Test
    void computeLargeAngle() {
        CosecantNamed csc = createCosecant();
        double x = 100 * Math.PI + 0.5; // not a singularity
        double expected = 1.0 / Math.sin(x);
        double actual = csc.compute(x);
        assertEquals(expected, actual, PRECISION);
    }

    @Test
    void getName() {
        CosecantNamed csc = createCosecant();
        assertEquals("csc", csc.getName());
    }
}