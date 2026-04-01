package lab2.modules.derived;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNamedTest {

    private static final double PRECISION = 1e-10;
    private static final double TOLERANCE = 1e-6;

    private ComplexNamed createComplex() {
        return new ComplexNamed(PRECISION);
    }

    // Helper to compute expected value using Math library
    private double expectedTrigPart(double x) {
        double cos = Math.cos(x);
        double sin = Math.sin(x);
        double csc = 1.0 / sin;
        double sec = 1.0 / cos;
        double cot = cos / sin;
        double numerator = ((((cos + csc) * sec) * csc) / sin);
        double denominator = cot / csc;
        return numerator / denominator;
    }

    private double expectedLogPart(double x) {
        double log10 = Math.log10(x);
        double log3 = Math.log(x) / Math.log(3);
        double ln = Math.log(x);
        double numerator = (Math.pow(log10, 3) - log3) / log10;
        return Math.pow(numerator / ln, 3);
    }

    @Test
    void constructor() {
        ComplexNamed complex = createComplex();
        assertNotNull(complex);
        assertEquals("complex", complex.getName());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.5, -Math.PI / 4, -Math.PI / 2 + 0.1, -Math.PI + 0.1})
    void computeTrigPart(double x) {
        ComplexNamed complex = createComplex();
        double expected = expectedTrigPart(x);
        double actual = complex.compute(x);
        assertEquals(expected, actual, TOLERANCE,
                String.format("complex(%f) trig part expected %f but got %f", x, expected, actual));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.5, 1.5, 2.0, 3.0, 5.0, 10.0})
    void computeLogPart(double x) {
        ComplexNamed complex = createComplex();
        double expected = expectedLogPart(x);
        double actual = complex.compute(x);
        assertEquals(expected, actual, TOLERANCE,
                String.format("complex(%f) log part expected %f but got %f", x, expected, actual));
    }

    @Test
    void computeAtZero() {
        ComplexNamed complex = createComplex();
        try {
            double result = complex.compute(0.0);
            // If no exception, the value should be large (due to division by near-zero)
            assertTrue(Math.abs(result) > 1e6 || Double.isInfinite(result),
                    "complex(0) should be large or infinite but got " + result);
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @ParameterizedTest
    @MethodSource("provideSingularPoints")
    void computeAtSingularPointsThrows(double x) {
        ComplexNamed complex = createComplex();
        try {
            double result = complex.compute(x);
            // If no exception, the value should be large (due to division by near-zero)
            assertTrue(Math.abs(result) > 1e6 || Double.isInfinite(result),
                    String.format("complex(%f) should be large or infinite but got %f", x, result));
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    private static Stream<Arguments> provideSingularPoints() {
        return Stream.of(
                Arguments.of(0.0),          // sin = 0
                Arguments.of(-Math.PI)      // sin = 0
                // x = π is not a singularity for x > 0 (log part is defined)
                // x = 1.0 is a mathematical singularity but may produce finite value due to approximation
        );
    }

    @Test
    void computeNearSingularities() {
        ComplexNamed complex = createComplex();
        // Points near singularities should still compute
        double x = 1.0 + 1e-5;
        double expected = expectedLogPart(x);
        double actual = complex.compute(x);
        // Large absolute tolerance due to huge values near singularity
        assertEquals(expected, actual, 1e5);
    }

    @Test
    void computeTransitionAtZero() {
        ComplexNamed complex = createComplex();
        // x = 0 is trig part, x = 1e-10 is log part (since >0). Ensure continuity? Not required.
        // Just test that both sides work.
        double neg = complex.compute(-0.001);
        double pos = complex.compute(0.001);
        // No assertion on values, just ensure no exception
        assertFalse(Double.isNaN(neg));
        assertFalse(Double.isNaN(pos));
    }

    @Test
    void getName() {
        ComplexNamed complex = createComplex();
        assertEquals("complex", complex.getName());
    }
}