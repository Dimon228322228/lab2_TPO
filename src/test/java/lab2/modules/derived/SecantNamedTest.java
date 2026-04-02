package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.stubs.SecantNamedStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SecantNamedTest {

    private static final double PRECISION = 1e-10;
    private static SecantNamed secant;
    private static SecantNamedStub secStub;

    @BeforeAll
    static void setUp() {
        Cosine cos = new Cosine(PRECISION);
        secant = new SecantNamed(cos);
        secStub = new SecantNamedStub(PRECISION);
    }

    @Test
    void constructor() {
        assertNotNull(secant);
        assertEquals("sec", secant.getName());
    }

    @ParameterizedTest
    @MethodSource("provideBasicValues")
    void computeBasicValues(double x, double expected) {
        double actual = secant.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("sec(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideBasicValues() {
        return Stream.of(
                Arguments.of(0.0, 1.0),
                Arguments.of(Math.PI / 6, 1.1547005383792515),
                Arguments.of(Math.PI / 4, 1.4142135623730951),
                Arguments.of(Math.PI / 3, 2.0),
                Arguments.of(-Math.PI / 6, 1.1547005383792515),
                Arguments.of(-Math.PI / 4, 1.4142135623730951),
                Arguments.of(-Math.PI / 3, 2.0),
                Arguments.of(Math.PI, -1.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnglesWhereCosineZero")
    void computeWhereCosineZeroThrows(double x) {
        try {
            double result = secant.compute(x);
            assertTrue(Math.abs(result) > 1e6 || Double.isInfinite(result),
                    String.format("sec(%f) should be large or infinite but got %f", x, result));
        } catch (ArithmeticException e) {
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
        double actual = secant.compute(x);
        assertTrue(Math.abs(actual) > 100.0 || Double.isInfinite(actual),
                String.format("sec(%f) should be large near singularity but got %f", x, actual));
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
        double actual = secant.compute(x);
        assertFalse(Double.isNaN(actual), "sec should not return NaN for large angle");
    }

    private static Stream<Arguments> provideLargeAngles() {
        return Stream.of(
                Arguments.of(100 * Math.PI + 0.1)
        );
    }

    @Test
    void getName() {
        assertEquals("sec", secant.getName());
    }

    @Test
    void testSecantNamedStubBasicValues() {
        assertEquals(1.0, secStub.compute(0.0), PRECISION);
        assertEquals(2.0, secStub.compute(Math.PI / 3), PRECISION);
    }

    @Test
    void testSecantNamedStubGetName() {
        assertEquals("sec", secStub.getName());
    }

    @Test
    void testSecantNamedStubVsReal() {
        assertEquals(secant.compute(0.0), secStub.compute(0.0), PRECISION);
    }

    @ParameterizedTest
    @MethodSource("provideValuesForStub")
    void compareStubAndReal(double x) {
        double realResult = secant.compute(x);
        double stubResult = secStub.compute(x);
        assertEquals(realResult, stubResult, 0.01,
                String.format("sec(%f) real=%f stub=%f", x, realResult, stubResult));
    }

    private static Stream<Arguments> provideValuesForStub() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(Math.PI / 6),
                Arguments.of(Math.PI / 4),
                Arguments.of(Math.PI / 3),
                Arguments.of(Math.PI)
        );
    }

    @Test
    void testStubWithCustomPrecision() {
        SecantNamedStub customStub = new SecantNamedStub(1e-5);
        assertEquals(1.0, customStub.compute(0.0), 1e-5);
        assertEquals(2.0, customStub.compute(Math.PI / 3), 1e-5);
    }

    @Test
    void testStubValueMapContainsExpectedValues() {
        assertEquals(1.0, secStub.compute(0.0), PRECISION);
        assertEquals(1.1547005383792515, secStub.compute(Math.PI / 6), PRECISION);
        assertEquals(1.4142135623730951, secStub.compute(Math.PI / 4), PRECISION);
        assertEquals(2.0, secStub.compute(Math.PI / 3), PRECISION);
    }

    @Test
    void testStubUsesMockitoMock() {
        assertNotNull(secStub.getMock());
    }

    @Test
    void testStubMockReturnsCorrectValues() {
        assertEquals(1.0, secStub.compute(0.0), PRECISION);
        assertEquals(-1.0, secStub.compute(Math.PI), PRECISION);
    }
}