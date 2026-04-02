package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.stubs.SineNamedStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SineNamedTest {

    private static final double PRECISION = 1e-10;
    private static SineNamed sine;
    private static SineNamedStub sineStub;

    @BeforeAll
    static void setUp() {
        Cosine cos = new Cosine(PRECISION);
        sine = new SineNamed(cos);
        sineStub = new SineNamedStub(PRECISION);
    }

    @Test
    void constructor() {
        assertNotNull(sine);
        assertEquals("sin", sine.getName());
    }

    @ParameterizedTest
    @MethodSource("provideBasicValues")
    void computeBasicValues(double x, double expected) {
        double actual = sine.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("sin(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideBasicValues() {
        return Stream.of(
                Arguments.of(0.0, 0.0),
                Arguments.of(Math.PI / 6, 0.5),
                Arguments.of(Math.PI / 4, 0.7071067811865475),
                Arguments.of(Math.PI / 3, 0.8660254037844386),
                Arguments.of(Math.PI / 2, 1.0),
                Arguments.of(Math.PI, 0.0),
                Arguments.of(3 * Math.PI / 2, -1.0),
                Arguments.of(2 * Math.PI, 0.0),
                Arguments.of(-Math.PI / 4, -0.7071067811865475),
                Arguments.of(-Math.PI, 0.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLargeAngles")
    void computeLargeAngles(double x) {
        double actual = sine.compute(x);
        assertFalse(Double.isNaN(actual), "sin should not return NaN for large angle");
        assertTrue(actual >= -1.0 && actual <= 1.0, "sin should be in range [-1, 1]");
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
        double x = 0.5;
        double expected = 0.479425538604203;
        double actual = sine.compute(x);
        assertEquals(expected, actual, PRECISION);
    }

    @Test
    void getName() {
        assertEquals("sin", sine.getName());
    }

    @Test
    void testSineNamedStubBasicValues() {
        assertEquals(0.0, sineStub.compute(0.0), PRECISION);
        assertEquals(1.0, sineStub.compute(Math.PI / 2), PRECISION);
        assertEquals(-1.0, sineStub.compute(3 * Math.PI / 2), PRECISION);
    }

    @Test
    void testSineNamedStubLargeAngles() {
        assertEquals(0.0, sineStub.compute(100 * Math.PI), 0.01);
        assertEquals(-0.8268213245981267, sineStub.compute(1000.0), 0.01);
    }

    @Test
    void testSineNamedStubGetName() {
        assertEquals("sin", sineStub.getName());
    }

    @Test
    void testSineNamedStubVsReal() {
        assertEquals(sine.compute(0.0), sineStub.compute(0.0), PRECISION);
        assertEquals(sine.compute(Math.PI / 2), sineStub.compute(Math.PI / 2), PRECISION);
    }

    @ParameterizedTest
    @MethodSource("provideValuesForStub")
    void compareStubAndReal(double x) {
        double realResult = sine.compute(x);
        double stubResult = sineStub.compute(x);
        assertEquals(realResult, stubResult, 0.01,
                String.format("sin(%f) real=%f stub=%f", x, realResult, stubResult));
    }

    private static Stream<Arguments> provideValuesForStub() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(Math.PI / 6),
                Arguments.of(Math.PI / 4),
                Arguments.of(Math.PI / 2),
                Arguments.of(Math.PI),
                Arguments.of(3 * Math.PI / 2)
        );
    }

    @Test
    void testStubWithCustomPrecision() {
        SineNamedStub customStub = new SineNamedStub(1e-5);
        assertEquals(0.0, customStub.compute(0.0), 1e-5);
        assertEquals(1.0, customStub.compute(Math.PI / 2), 1e-5);
    }

    @Test
    void testStubValueMapContainsExpectedValues() {
        assertEquals(0.0, sineStub.compute(0.0), PRECISION);
        assertEquals(0.5, sineStub.compute(Math.PI / 6), PRECISION);
        assertEquals(0.7071067811865475, sineStub.compute(Math.PI / 4), PRECISION);
        assertEquals(0.8660254037844386, sineStub.compute(Math.PI / 3), PRECISION);
        assertEquals(1.0, sineStub.compute(Math.PI / 2), PRECISION);
        assertEquals(-1.0, sineStub.compute(3 * Math.PI / 2), PRECISION);
    }

    @Test
    void testStubFallbackToMathSin() {
        double result = sineStub.compute(123.456);
        assertFalse(Double.isNaN(result));
        assertTrue(result >= -1.0 && result <= 1.0);
    }
}