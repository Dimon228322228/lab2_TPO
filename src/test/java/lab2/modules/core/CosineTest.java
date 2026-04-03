package lab2.modules.core;

import lab2.stubs.CosineStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CosineTest {

    private static final double PRECISION = 1e-10;
    private static Cosine cosine;
    private static CosineStub cosineStub;

    @BeforeAll
    static void setUp() {
        cosine = new Cosine(PRECISION);
        cosineStub = new CosineStub(PRECISION);
    }

    @Test
    void constructorRespectsMaxPrecision() {
        Cosine cosTest = new Cosine(1e-20);
        assertDoesNotThrow(() -> cosTest.compute(0.0));
    }

    @ParameterizedTest
    @MethodSource("provideBasicValues")
    void computeBasicValues(double x, double expected) {
        double actual = cosine.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("cos(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideBasicValues() {
        return Stream.of(
                Arguments.of(0.0, 1.0),
                Arguments.of(Math.PI / 6, 0.8660254037844386),
                Arguments.of(Math.PI / 4, 0.7071067811865476),
                Arguments.of(Math.PI / 3, 0.5),
                Arguments.of(Math.PI / 2, 0.0),
                Arguments.of(Math.PI, -1.0),
                Arguments.of(3 * Math.PI / 2, 0.0),
                Arguments.of(2 * Math.PI, 1.0),
                Arguments.of(-Math.PI / 4, 0.7071067811865476),
                Arguments.of(-Math.PI, -1.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLargeAngles")
    void computeLargeAngles(double x) {
        double actual = cosine.compute(x);
        assertFalse(Double.isNaN(actual), "cos should not return NaN for large angle");
        assertTrue(actual >= -1.0 && actual <= 1.0, "cos should be in range [-1, 1]");
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
        Cosine highPrecCos = new Cosine(1e-15);
        double actual = highPrecCos.compute(1.2345);
        assertFalse(Double.isNaN(actual), "cos should not return NaN");
        assertTrue(actual >= -1.0 && actual <= 1.0, "cos should be in range [-1, 1]");
    }

    @Test
    void getName() {
        assertEquals("cos", cosine.getName());
    }

    @Test
    void testCosineStubBasicValues() {
        assertEquals(1.0, cosineStub.compute(0.0), PRECISION);
        assertEquals(0.0, cosineStub.compute(Math.PI / 2), PRECISION);
        assertEquals(-1.0, cosineStub.compute(Math.PI), PRECISION);
        assertEquals(1.0, cosineStub.compute(2 * Math.PI), PRECISION);
    }

    @Test
    void testCosineStubLargeAngles() {
        assertEquals(1.0, cosineStub.compute(100 * Math.PI), 0.01);
        assertEquals(0.56, cosineStub.compute(1000.0), 0.01);
        assertEquals(0.56, cosineStub.compute(-1000.0), 0.01);
    }

    @Test
    void testCosineStubGetName() {
        assertEquals("cos", cosineStub.getName());
    }

    @Test
    void testCosineStubVsReal() {
        assertEquals(cosine.compute(0.0), cosineStub.compute(0.0), PRECISION);
        assertEquals(cosine.compute(Math.PI / 4), cosineStub.compute(Math.PI / 4), PRECISION);
        assertEquals(cosine.compute(Math.PI), cosineStub.compute(Math.PI), PRECISION);
    }

    @ParameterizedTest
    @MethodSource("provideValuesForStub")
    void compareStubAndReal(double x) {
        double realResult = cosine.compute(x);
        double stubResult = cosineStub.compute(x);
        assertEquals(realResult, stubResult, 0.01,
                String.format("cos(%f) real=%f stub=%f", x, realResult, stubResult));
    }

    private static Stream<Arguments> provideValuesForStub() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(Math.PI / 6),
                Arguments.of(Math.PI / 4),
                Arguments.of(Math.PI / 3),
                Arguments.of(Math.PI / 2),
                Arguments.of(Math.PI),
                Arguments.of(3 * Math.PI / 2),
                Arguments.of(2 * Math.PI)
        );
    }

    @Test
    void testStubWithCustomPrecision() {
        CosineStub customStub = new CosineStub(1e-5);
        assertEquals(1.0, customStub.compute(0.0), 1e-5);
        assertEquals(0.0, customStub.compute(Math.PI / 2), 1e-5);
    }

    @Test
    void testStubValueMapContainsExpectedValues() {
        assertEquals(1.0, cosineStub.compute(0.0), PRECISION);
        assertEquals(0.8660254037844386, cosineStub.compute(Math.PI / 6), PRECISION);
        assertEquals(0.7071067811865476, cosineStub.compute(Math.PI / 4), PRECISION);
        assertEquals(0.5, cosineStub.compute(Math.PI / 3), PRECISION);
        assertEquals(0.0, cosineStub.compute(Math.PI / 2), PRECISION);
        assertEquals(-1.0, cosineStub.compute(Math.PI), PRECISION);
    }

    @Test
    void testStubFallbackToMathCos() {
        double result = cosineStub.compute(123.456);
        assertFalse(Double.isNaN(result));
        assertTrue(result >= -1.0 && result <= 1.0);
    }
}