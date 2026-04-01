package lab2.modules.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CosineTest {

    private static final double PRECISION = 1e-10;

    @Test
    void constructorRespectsMaxPrecision() {
        Cosine cos = new Cosine(1e-20);
        // precision should be at least MAX_PRECISION (1e-15)
        // we can't directly access private field, but we can test that compute works
        assertDoesNotThrow(() -> cos.compute(0.0));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, Math.PI / 6, Math.PI / 4, Math.PI / 3, Math.PI / 2,
            Math.PI, 3 * Math.PI / 2, 2 * Math.PI, -Math.PI / 4, -Math.PI})
    void computeBasicValues(double x) {
        Cosine cos = new Cosine(PRECISION);
        double expected = Math.cos(x);
        double actual = cos.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("cos(%f) expected %f but got %f", x, expected, actual));
    }

    @ParameterizedTest
    @MethodSource("provideLargeAngles")
    void computeLargeAngles(double x) {
        Cosine cos = new Cosine(PRECISION);
        double expected = Math.cos(x);
        double actual = cos.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("cos(%f) expected %f but got %f", x, expected, actual));
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
    void computeWithHighPrecision() {
        Cosine cos = new Cosine(1e-15);
        double x = 1.2345;
        double expected = Math.cos(x);
        double actual = cos.compute(x);
        assertEquals(expected, actual, 1e-14); // allow slightly larger error due to series truncation
    }

    @Test
    void getName() {
        Cosine cos = new Cosine(PRECISION);
        assertEquals("cos", cos.getName());
    }
}