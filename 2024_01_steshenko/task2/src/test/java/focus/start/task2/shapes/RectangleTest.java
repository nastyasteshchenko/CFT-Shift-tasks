package focus.start.task2.shapes;

import focus.start.task2.TestUtility;
import focus.start.task2.exception.InputFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RectangleTest {

    @Test
    public void testFirstNegativeParameter() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Rectangle.create(-10., 10));

        assertEquals("It's impossible to create shape with negative parameters.", thrown.getMessage());
    }

    @Test
    public void testSecondNegativeParameter() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Rectangle.create(10., -10));

        assertEquals("It's impossible to create shape with negative parameters.", thrown.getMessage());
    }

    @Test
    public void testBothNegativeParameters() {
        Throwable thrown = assertThrows(InputFileException.class, () ->
                Rectangle.create(-10., -10));

        assertEquals("It's impossible to create shape with negative parameters.", thrown.getMessage());
    }

    @Test
    public void testToStringMethod() throws InputFileException {
        Rectangle rectangle = Rectangle.create(5., 7.);
        String output = String.format("Тип фигуры: Прямоугольник%nПлощадь: 35 кв. cм%nПериметр: 24 cм%n" +
                "Длина: 7 cм%nШирина: 5 cм%nДиагональ: 8,6 cм");

        assertEquals(output, rectangle.toString());
    }

    @ParameterizedTest
    @CsvSource(
            {"10.,3.,30.", "20.,1.,20.", "15.,15.,225.", "30.,1.5,45.", "35.,2.,70."}
    )
    void testAreaValue(double base, double side, double expected) throws InputFileException {
        Rectangle rectangle = Rectangle.create(base, side);
        assertEquals(expected, TestUtility.roundToThousandths(rectangle.calculateArea()));
    }

    @ParameterizedTest
    @CsvSource(
            {"10.,3.,26.", "20.,1.,42.", "15.,15.,60.", "30.,1.5,63.", "35.,2.,74.", "3.5,1.2,9.4"}
    )
    void testPerimeterValue(double base, double side, double expected) throws InputFileException {
        Rectangle rectangle = Rectangle.create(base, side);
        assertEquals(expected, TestUtility.roundToThousandths(rectangle.calculatePerimeter()));
    }
}
