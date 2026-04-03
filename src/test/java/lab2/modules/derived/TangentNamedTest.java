package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.stubs.TangentNamedStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TangentNamedTest {

    private static final double PRECISION = 1e-10;
    private static TangentNamed tangent;
    private static TangentNamedStub tanStub;

    @BeforeAll
    static void setUp() {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        tangent = new TangentNamed(cos, sin);
        tanStub = new TangentNamedStub(PRECISION);
    }

    @Test
    void constructor() {
        assertNotNull(tangent);
        assertEquals("tg", tangent.getName());
    }

    @ParameterizedTest
    @MethodSource("provideBasicValues")
    void computeBasicValues(double x, double expected) {
        double actual = tangent.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("tan(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideBasicValues() {
        return Stream.of(
                Arguments.of(0.0, 0.0),
                Arguments.of(Math.PI / 6, 0.5773502691896258),
                Arguments.of(Math.PI / 4, 1.0),
                Arguments.of(Math.PI / 3, 1.7320508075688774),
                Arguments.of(-Math.PI / 6, -0.5773502691896258),
                Arguments.of(-Math.PI / 4, -1.0),
                Arguments.of(-Math.PI / 3, -1.7320508075688774)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnglesWhereCosineZero")
    void computeWhereCosineZeroThrows(double x) {
        assertThrows(ArithmeticException.class, () -> tangent.compute(x),
                    String.format("tan(%f) shouldn't be exist", x));
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
        double actual = tangent.compute(x);
        assertTrue(Math.abs(actual) > 100.0 || Double.isInfinite(actual),
                String.format("tan(%f) should be large near singularity but got %f", x, actual));
    }

    private static Stream<Arguments> provideAnglesNearSingularity() {
        return Stream.of(
                Arguments.of(Math.PI / 2 - 0.001),
                Arguments.of(Math.PI / 2 + 0.001),
                Arguments.of(-Math.PI / 2 + 0.001)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLargeAngles")
    void computeLargeAngle(double x) {
        double actual = tangent.compute(x);
        assertFalse(Double.isNaN(actual), "tan should not return NaN for large angle");
    }

    private static Stream<Arguments> provideLargeAngles() {
        return Stream.of(
                Arguments.of(100 * Math.PI + 0.1)
        );
    }

    @Test
    void getName() {
        assertEquals("tg", tangent.getName());
    }

    @Test
    void testTangentNamedStubBasicValues() {
        assertEquals(0.0, tanStub.compute(0.0), PRECISION);
        assertEquals(1.0, tanStub.compute(Math.PI / 4), PRECISION);
    }

    @Test
    void testTangentNamedStubGetName() {
        assertEquals("tg", tanStub.getName());
    }

    @Test
    void testTangentNamedStubVsReal() {
        assertEquals(tangent.compute(0.0), tanStub.compute(0.0), PRECISION);
        assertEquals(tangent.compute(Math.PI / 4), tanStub.compute(Math.PI / 4), PRECISION);
    }

    @ParameterizedTest
    @MethodSource("provideValuesForStub")
    void compareStubAndReal(double x) {
        double realResult = tangent.compute(x);
        double stubResult = tanStub.compute(x);
        assertEquals(realResult, stubResult, 0.01,
                String.format("tan(%f) real=%f stub=%f", x, realResult, stubResult));
    }

    private static Stream<Arguments> provideValuesForStub() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(Math.PI / 6),
                Arguments.of(Math.PI / 4),
                Arguments.of(Math.PI / 3)
        );
    }

    @Test
    void testStubWithCustomPrecision() {
        TangentNamedStub customStub = new TangentNamedStub(1e-5);
        assertEquals(0.0, customStub.compute(0.0), 1e-5);
        assertEquals(1.0, customStub.compute(Math.PI / 4), 1e-5);
    }

    @Test
    void testStubValueMapContainsExpectedValues() {
        assertEquals(0.0, tanStub.compute(0.0), PRECISION);
        assertEquals(0.5773502691896258, tanStub.compute(Math.PI / 6), PRECISION);
        assertEquals(1.0, tanStub.compute(Math.PI / 4), PRECISION);
        assertEquals(1.7320508075688774, tanStub.compute(Math.PI / 3), PRECISION);
    }

    @Test
    void testStubFallbackToMathTan() {
        double result = tanStub.compute(123.456);
        assertFalse(Double.isNaN(result));
    }
}