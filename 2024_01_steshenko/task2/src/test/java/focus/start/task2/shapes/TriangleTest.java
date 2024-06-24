package focus.start.task2.shapes;

import focus.start.task2.TestUtility;
import focus.start.task2.exception.InputFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TriangleTest {

    @Test
    public void testFirstNegativeParameter() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Triangle.create(-10., 10, 10.));

        assertEquals("It's impossible to create shape with negative parameters.", thrown.getMessage());
    }

    @Test
    public void testSecondNegativeParameter() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Triangle.create(10., -10., 10.));

        assertEquals("It's impossible to create shape with negative parameters.", thrown.getMessage());
    }

    @Test
    public void testThirdNegativeParameter() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Triangle.create(10., 10., -10.));

        assertEquals("It's impossible to create shape with negative parameters.", thrown.getMessage());
    }

    @Test
    public void testAllNegativeParameters() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Triangle.create(-10., -10., -10.));

        assertEquals("It's impossible to create shape with negative parameters.", thrown.getMessage());
    }

    @Test
    public void testNotExistingTriangle() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Triangle.create(10., 10., 20.));

        assertEquals("Triangle with parameters: (10, 10, 20) doesn't exist.", thrown.getMessage());
    }

    @Test
    public void testToStringMethod() throws InputFileException {
        Triangle triangle = Triangle.create(5., 7., 9.);
        String output = String.format("Тип фигуры: Треугольник%nПлощадь: 17,41 кв. cм%nПериметр: 21 cм%n" +
                "Длина первой стороны: 5 cм%nПротиволежащий угол первой стороны: 33,56°%n" +
                "Длина второй стороны: 7 cм%nПротиволежащий угол второй стороны: 50,7°%n" +
                "Длина третьей стороны: 9 cм%nПротиволежащий угол третьей стороны: 95,74°");

        assertEquals(output, triangle.toString());
    }

    @ParameterizedTest
    @CsvSource(
            {"10.,3.,10.,14.83", "20.,1.,20.5,8.766", "15.,15.,2.1,15.711", "30.,1.5,29.,16.486", "9.,2.,10.,8.182",
                    "3.5,1.2,3.,1.742"}
    )
    void testAreaValue(double firstSide, double secondsSide, double thirdSide, double expected)
            throws InputFileException {
        Triangle triangle = Triangle.create(firstSide, secondsSide, thirdSide);
        assertEquals(expected, TestUtility.roundToThousandths(triangle.calculateArea()));
    }

    @ParameterizedTest
    @CsvSource(
            {"10.,3.,10.,23.", "20.,1.,20.5,41.5", "15.,15.,2.1,32.1", "30.,1.5,29.,60.5", "9.,2.,10.,21.",
                    "3.5,1.2,3.,7.7"}
    )
    void testPerimeterValue(double firstSide, double secondsSide, double thirdSide, double expected)
            throws InputFileException {
        Triangle triangle = Triangle.create(firstSide, secondsSide, thirdSide);
        assertEquals(expected, TestUtility.roundToThousandths(triangle.calculatePerimeter()));
    }
}
