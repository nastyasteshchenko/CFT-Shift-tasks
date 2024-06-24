package focus.start.task2.shapes;

import focus.start.task2.TestUtility;
import focus.start.task2.exception.InputFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {
    @Test
    public void testNegativeParameters() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Circle.create(-10.));

        assertEquals("It's impossible to create shape with negative parameters.", thrown.getMessage());
    }

    @Test
    public void testToStringMethod() throws InputFileException {
        Circle circle = Circle.create(5.);
        String output = String.format("Тип фигуры: Круг%nПлощадь: 78,54 кв. cм%nПериметр: 31,42 cм%n" +
                "Радиус: 5 cм%nДиаметр: 10 cм");

        assertEquals(output, circle.toString());
    }

    @ParameterizedTest
    @CsvSource(
            {"10.,314.159", "20.,1256.637", "1.,3.142", "30.,2827.433", "35.,3848.451", "1.5,7.069"}
    )
    void testAreaValue(double radius, double expected) throws InputFileException {
        Circle circle = Circle.create(radius);
        assertEquals(expected, TestUtility.roundToThousandths(circle.calculateArea()));
    }

    @ParameterizedTest
    @CsvSource(
            {"10.,62.832", "20.,125.664", "1.,6.283", "30.,188.496", "35.,219.911", "1.5,9.425"}
    )
    void testPerimeterValue(double radius, double expected) throws InputFileException {
        Circle circle = Circle.create(radius);
        assertEquals(expected, TestUtility.roundToThousandths(circle.calculatePerimeter()));
    }
}