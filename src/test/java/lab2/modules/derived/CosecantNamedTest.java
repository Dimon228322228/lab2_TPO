package lab2.modules.derived;

import lab2.modules.core.Cosine;
import lab2.stubs.CosecantNamedStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CosecantNamedTest {

    private static final double PRECISION = 1e-10;
    private static CosecantNamed cosecant;
    private static CosecantNamedStub cscStub;

    @BeforeAll
    static void setUp() {
        Cosine cos = new Cosine(PRECISION);
        SineNamed sin = new SineNamed(cos);
        cosecant = new CosecantNamed(sin);
        cscStub = new CosecantNamedStub(PRECISION);
    }

    @Test
    void constructor() {
        assertNotNull(cosecant);
        assertEquals("csc", cosecant.getName());
    }

    @ParameterizedTest
    @MethodSource("provideBasicValues")
    void computeBasicValues(double x, double expected) {
        double actual = cosecant.compute(x);
        assertEquals(expected, actual, PRECISION,
                String.format("csc(%f) expected %f but got %f", x, expected, actual));
    }

    private static Stream<Arguments> provideBasicValues() {
        return Stream.of(
                Arguments.of(Math.PI / 6, 2.0),
                Arguments.of(Math.PI / 4, 1.4142135623730951),
                Arguments.of(Math.PI / 3, 1.1547005383792515),
                Arguments.of(Math.PI / 2, 1.0),
                Arguments.of(-Math.PI / 6, -2.0),
                Arguments.of(-Math.PI / 4, -1.4142135623730951),
                Arguments.of(-Math.PI / 3, -1.1547005383792515)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnglesWhereSineZero")
    void computeWhereSineZeroThrows(double x) {
                assertThrows(ArithmeticException.class, () -> cosecant.compute(x),
                String.format("csc(%f) shouldn't be exist", x));
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
        double actual = cosecant.compute(x);
        assertTrue(Math.abs(actual) > 100.0 || Double.isInfinite(actual),
                String.format("csc(%f) should be large near singularity but got %f", x, actual));
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
        double actual = cosecant.compute(x);
        assertFalse(Double.isNaN(actual), "csc should not return NaN for large angle");
    }

    private static Stream<Arguments> provideLargeAngles() {
        return Stream.of(
                Arguments.of(100 * Math.PI + 0.5)
        );
    }

    @Test
    void getName() {
        assertEquals("csc", cosecant.getName());
    }

    @Test
    void testCosecantNamedStubBasicValues() {
        assertEquals(2.0, cscStub.compute(Math.PI / 6), PRECISION);
        assertEquals(1.0, cscStub.compute(Math.PI / 2), PRECISION);
    }

    @Test
    void testCosecantNamedStubGetName() {
        assertEquals("csc", cscStub.getName());
    }

    @Test
    void testCosecantNamedStubVsReal() {
        assertEquals(cosecant.compute(Math.PI / 2), cscStub.compute(Math.PI / 2), PRECISION);
    }

    @Test
    void testStubWithCustomPrecision() {
        CosecantNamedStub customStub = new CosecantNamedStub(1e-5);
        assertEquals(2.0, customStub.compute(Math.PI / 6), 1e-5);
        assertEquals(1.0, customStub.compute(Math.PI / 2), 1e-5);
    }

    @Test
    void testStubValueMapContainsExpectedValues() {
        assertEquals(2.0, cscStub.compute(Math.PI / 6), PRECISION);
        assertEquals(1.4142135623730951, cscStub.compute(Math.PI / 4), PRECISION);
        assertEquals(1.1547005383792515, cscStub.compute(Math.PI / 3), PRECISION);
        assertEquals(1.0, cscStub.compute(Math.PI / 2), PRECISION);
    }

    @Test
    void testStubUsesMockitoMock() {
        assertNotNull(cscStub.getMock());
    }

    @Test
    void testStubMockReturnsCorrectValues() {
        assertEquals(1.0, cscStub.compute(Math.PI / 2), PRECISION);
        assertEquals(2.0, cscStub.compute(Math.PI / 6), PRECISION);
    }
}