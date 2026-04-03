package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.stubs.CotangentNamedStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CotangentNamedTest {

    private static final double PRECISION = 1e-10;
    private static CotangentNamed cotangent;
    private static CotangentNamedStub cotStub;

    @BeforeAll
    static void setUp() {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        cotangent = new CotangentNamed(cos, sin);
        cotStub = new CotangentNamedStub(PRECISION);
    }

    @Test
    void constructor() {
        assertNotNull(cotangent);
        assertEquals("cot", cotangent.getName());
    }

    @ParameterizedTest
    @MethodSource("provideBasicValues")
    void computeBasicValues(double x, double expected) {
        double actual = cotangent.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("cot(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideBasicValues() {
        return Stream.of(
                Arguments.of(Math.PI / 6, 1.7320508075688774),
                Arguments.of(Math.PI / 4, 1.0),
                Arguments.of(Math.PI / 3, 0.5773502691896258),
                Arguments.of(Math.PI / 2, 0.0),
                Arguments.of(-Math.PI / 6, -1.7320508075688774),
                Arguments.of(-Math.PI / 4, -1.0),
                Arguments.of(-Math.PI / 3, -0.5773502691896258)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnglesWhereSineZero")
    void computeWhereSineZeroThrows(double x) {
        assertThrows(ArithmeticException.class, () -> cotangent.compute(x), 
                String.format("cot(%f) shouldn't be exist", x));
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
        double actual = cotangent.compute(x);
        assertTrue(Math.abs(actual) > 100.0 || Double.isInfinite(actual),
                String.format("cot(%f) should be large near singularity but got %f", x, actual));
    }

    private static Stream<Arguments> provideAnglesNearSingularity() {
        return Stream.of(
                Arguments.of(0.001),
                Arguments.of(-0.001),
                Arguments.of(Math.PI - 0.001),
                Arguments.of(Math.PI + 0.001)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLargeAngles")
    void computeLargeAngle(double x) {
        double actual = cotangent.compute(x);
        assertFalse(Double.isNaN(actual), "cot should not return NaN for large angle");
    }

    private static Stream<Arguments> provideLargeAngles() {
        return Stream.of(
                Arguments.of(100 * Math.PI + 0.5)
        );
    }

    @Test
    void getName() {
        assertEquals("cot", cotangent.getName());
    }

    @Test
    void testCotangentNamedStubBasicValues() {
        assertEquals(1.0, cotStub.compute(Math.PI / 4), PRECISION);
        assertEquals(0.0, cotStub.compute(Math.PI / 2), PRECISION);
    }

    @Test
    void testCotangentNamedStubGetName() {
        assertEquals("cot", cotStub.getName());
    }

    @Test
    void testCotangentNamedStubVsReal() {
        assertEquals(cotangent.compute(Math.PI / 4), cotStub.compute(Math.PI / 4), PRECISION);
    }

    @Test
    void testStubWithCustomPrecision() {
        CotangentNamedStub customStub = new CotangentNamedStub(1e-5);
        assertEquals(1.0, customStub.compute(Math.PI / 4), 1e-5);
        assertEquals(0.0, customStub.compute(Math.PI / 2), 1e-5);
    }

    @Test
    void testStubValueMapContainsExpectedValues() {
        assertEquals(1.7320508075688774, cotStub.compute(Math.PI / 6), PRECISION);
        assertEquals(1.0, cotStub.compute(Math.PI / 4), PRECISION);
        assertEquals(0.5773502691896258, cotStub.compute(Math.PI / 3), PRECISION);
        assertEquals(0.0, cotStub.compute(Math.PI / 2), PRECISION);
    }

    @Test
    void testStubUsesMockitoMock() {
        assertNotNull(cotStub.getMock());
    }

    @Test
    void testStubMockReturnsCorrectValues() {
        assertEquals(1.0, cotStub.compute(Math.PI / 4), PRECISION);
        assertEquals(0.0, cotStub.compute(Math.PI / 2), PRECISION);
    }
}