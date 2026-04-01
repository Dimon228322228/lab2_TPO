package lab2.modules.derived;

import lab2.modules.core.Cosine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SineNamedTest {

    private static final double PRECISION = 1e-10;

    @Test
    void constructor() {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        assertNotNull(sin);
        assertEquals("sin", sin.getName());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, Math.PI / 6, Math.PI / 4, Math.PI / 3, Math.PI / 2,
            Math.PI, 3 * Math.PI / 2, 2 * Math.PI, -Math.PI / 4, -Math.PI})
    void computeBasicValues(double x) {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        double expected = Math.sin(x);
        double actual = sin.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("sin(%f) expected %f but got %f", x, expected, actual));
    }

    @ParameterizedTest
    @MethodSource("provideLargeAngles")
    void computeLargeAngles(double x) {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        double expected = Math.sin(x);
        double actual = sin.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("sin(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideLargeAngles() {
        return Stream.of(
                Arguments.of(100 * Math.PI),
                Arguments.of(-100 * Math.PI),
                Arguments.of(1000.0),
                Arguments.of(-1000.0)
        );
    }

    @Test
    void computeUsesCosine() {
        // Verify that sine uses cosine via identity sin(x) = cos(π/2 - x)
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        double x = 0.5;
        double expected = Math.sin(x);
        double actual = sin.compute(x);
        assertEquals(expected, actual, PRECISION);
    }

    @Test
    void getName() {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        assertEquals("sin", sin.getName());
    }
}