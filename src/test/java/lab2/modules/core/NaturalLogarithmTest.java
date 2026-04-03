package lab2.modules.core;

import lab2.stubs.NaturalLogarithmStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NaturalLogarithmTest {

    private static final double PRECISION = 1e-10;
    private static NaturalLogarithm naturalLogarithm;
    private static NaturalLogarithmStub lnStub;

    @BeforeAll
    static void setUp() {
        naturalLogarithm = new NaturalLogarithm(PRECISION);
        lnStub = new NaturalLogarithmStub(PRECISION);
    }

    @Test
    void constructorRespectsMaxPrecision() {
        NaturalLogarithm ln = new NaturalLogarithm(1e-20);
        assertDoesNotThrow(() -> ln.compute(1.5));
    }

    @ParameterizedTest
    @MethodSource("providePositiveValues")
    void computePositiveValues(double x, double expected) {
        double actual = naturalLogarithm.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("ln(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> providePositiveValues() {
        return Stream.of(
                Arguments.of(0.5, -0.6931471805599453),
                Arguments.of(0.8, -0.22314355131420976),
                Arguments.of(1.0, 0.0),
                Arguments.of(1.2, 0.1823215567939546),
                Arguments.of(1.5, 0.4054651081081644),
                Arguments.of(2.0, 0.6931471805599453),
                Arguments.of(3.0, 1.0986122886681098),
                Arguments.of(5.0, 1.6094379124341003),
                Arguments.of(10.0, 2.302585092994046),
                Arguments.of(100.0, 4.605170185988092)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSmallPositiveValues")
    void computeSmallPositiveValues(double x, double expected) {
        double actual = naturalLogarithm.compute(x);
        assertEquals(expected, actual, PRECISION * 10,
                String.format("ln(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideSmallPositiveValues() {
        return Stream.of(
                Arguments.of(0.1, -2.302585092994046),
                Arguments.of(0.01, -4.605170185988092),
                Arguments.of(0.001, -6.907755278982137),
                Arguments.of(1e-6, -13.815510557964274)
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -5.0})
    void computeNonPositiveThrows(double x) {
        assertThrows(IllegalArgumentException.class, () -> naturalLogarithm.compute(x),
                String.format("ln(%f) should throw IllegalArgumentException", x));
    }

    @ParameterizedTest
    @MethodSource("provideEdgeCases")
    void computeEdgeCases(double x, double expected) {
        double actual = naturalLogarithm.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("ln(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideEdgeCases() {
        return Stream.of(
                Arguments.of(Math.E, 1.0),
                Arguments.of(1.0, 0.0),
                Arguments.of(2.0, 0.6931471805599453)
        );
    }

    @Test
    void computeWithHighPrecision() {
        NaturalLogarithm ln = new NaturalLogarithm(1e-15);
        double actual = ln.compute(1.2345);
        assertFalse(Double.isNaN(actual), "ln should not return NaN");
        assertTrue(actual > 0, "ln(1.2345) should be positive");
    }

    @Test
    void getName() {
        assertEquals("ln", naturalLogarithm.getName());
    }

    @Test
    void testNaturalLogarithmStubBasicValues() {
        assertEquals(0.0, lnStub.compute(1.0), PRECISION);
        assertEquals(1.0, lnStub.compute(Math.E), PRECISION);
        assertEquals(0.6931471805599453, lnStub.compute(2.0), PRECISION);
    }

    @Test
    void testNaturalLogarithmStubSmallValues() {
        assertEquals(-2.302585092994046, lnStub.compute(0.1), PRECISION);
        assertEquals(-4.605170185988092, lnStub.compute(0.01), PRECISION);
        assertEquals(-6.907755278982137, lnStub.compute(0.001), PRECISION);
    }

    @Test
    void testNaturalLogarithmStubGetName() {
        assertEquals("ln", lnStub.getName());
    }

    @Test
    void testNaturalLogarithmStubVsReal() {
        assertEquals(naturalLogarithm.compute(1.0), lnStub.compute(1.0), PRECISION);
        assertEquals(naturalLogarithm.compute(Math.E), lnStub.compute(Math.E), PRECISION);
        assertEquals(naturalLogarithm.compute(2.0), lnStub.compute(2.0), PRECISION);
    }

    @ParameterizedTest
    @MethodSource("provideValuesForStub")
    void compareStubAndReal(double x) {
        double realResult = naturalLogarithm.compute(x);
        double stubResult = lnStub.compute(x);
        assertEquals(realResult, stubResult, 0.01,
                String.format("ln(%f) real=%f stub=%f", x, realResult, stubResult));
    }

    private static Stream<Arguments> provideValuesForStub() {
        return Stream.of(
                Arguments.of(0.5),
                Arguments.of(1.0),
                Arguments.of(1.5),
                Arguments.of(2.0),
                Arguments.of(3.0),
                Arguments.of(5.0),
                Arguments.of(10.0),
                Arguments.of(100.0)
        );
    }

    @Test
    void testStubWithCustomPrecision() {
        NaturalLogarithmStub customStub = new NaturalLogarithmStub(1e-5);
        assertEquals(0.0, customStub.compute(1.0), 1e-5);
        assertEquals(1.0, customStub.compute(Math.E), 1e-5);
    }

    @Test
    void testStubValueMapContainsExpectedValues() {
        assertEquals(-0.6931471805599453, lnStub.compute(0.5), PRECISION);
        assertEquals(0.0, lnStub.compute(1.0), PRECISION);
        assertEquals(0.1823215567939546, lnStub.compute(1.2), PRECISION);
        assertEquals(0.4054651081081644, lnStub.compute(1.5), PRECISION);
        assertEquals(0.6931471805599453, lnStub.compute(2.0), PRECISION);
        assertEquals(1.0986122886681098, lnStub.compute(3.0), PRECISION);
        assertEquals(2.302585092994046, lnStub.compute(10.0), PRECISION);
    }

    @Test
    void testStubFallbackToMathLog() {
        double result = lnStub.compute(7.89);
        assertFalse(Double.isNaN(result));
        assertTrue(result > 2.0);
    }
}