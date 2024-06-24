package focus.start.task2;

import focus.start.task2.exception.InputFileException;
import focus.start.task2.shapes.Circle;
import focus.start.task2.shapes.Rectangle;
import focus.start.task2.shapes.Shape;
import focus.start.task2.shapes.Triangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShapeCreatingUtilityTest {

    @Test
    public void testCorrectCircle() throws InputFileException {
        InputData inputData = new InputData("CIRCLE", "3");
        Shape actualShape = ShapeCreatingUtility.createShape(inputData);
        Shape expectedShape = Circle.create(3.);
        assertEquals(expectedShape, actualShape);
    }

    @Test
    public void testCorrectRectangle() throws InputFileException {
        InputData inputData = new InputData("RECTANGLE", "3 7");
        Shape actualShape = ShapeCreatingUtility.createShape(inputData);
        Shape expectedShape = Rectangle.create(3., 7.);
        assertEquals(expectedShape, actualShape);
    }

    @Test
    public void testCorrectTriangle() throws InputFileException {
        InputData inputData = new InputData("TRIANGLE", "3 7 9");
        Shape actualShape = ShapeCreatingUtility.createShape(inputData);
        Shape expectedShape = Triangle.create(3., 7., 9.);
        assertEquals(expectedShape, actualShape);
    }

    @Test
    public void testNoParametersLineTriangle() {
        InputData inputData = new InputData("TRIANGLE", null);
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals(String.format("Wrong parameters amount for shape type 'TRIANGLE'%nactual: 0, expected: 3."),
                thrown.getMessage());
    }

    @Test
    public void testNoParametersLineCircle() {
        InputData inputData = new InputData("CIRCLE", null);
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals(String.format("Wrong parameters amount for shape type 'CIRCLE'%nactual: 0, expected: 1."),
                thrown.getMessage());
    }

    @Test
    public void testNoParametersLineRectangle() {
        InputData inputData = new InputData("RECTANGLE", null);
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals(String.format("Wrong parameters amount for shape type 'RECTANGLE'%nactual: 0, expected: 2."),
                thrown.getMessage());
    }

    @Test
    public void testFewerParametersRectangle() {
        InputData inputData = new InputData("RECTANGLE", "3");
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals(String.format("Wrong parameters amount for shape type 'RECTANGLE'%nactual: 1, expected: 2."),
                thrown.getMessage());
    }

    @Test
    public void testFewerParametersTriangle() {
        InputData inputData = new InputData("TRIANGLE", "3 6.7");
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals(String.format("Wrong parameters amount for shape type 'TRIANGLE'%nactual: 2, expected: 3."),
                thrown.getMessage());
    }

    @Test
    public void testMoreParametersTriangle() {
        InputData inputData = new InputData("TRIANGLE", "3 6.7 9 8");
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals(String.format("Wrong parameters amount for shape type 'TRIANGLE'%nactual: 4, expected: 3."),
                thrown.getMessage());
    }

    @Test
    public void testMoreParametersRectangle() {
        InputData inputData = new InputData("RECTANGLE", "3 7 9.5");
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals(String.format("Wrong parameters amount for shape type 'RECTANGLE'%nactual: 3, expected: 2."),
                thrown.getMessage());
    }

    @Test
    public void testMoreParametersCircle() {
        InputData inputData = new InputData("CIRCLE", "3 7 9.5");
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals(String.format("Wrong parameters amount for shape type 'CIRCLE'%nactual: 3, expected: 1."),
                thrown.getMessage());
    }

    @Test
    public void testNonNumericParameter() {
        InputData inputData = new InputData("TRIANGLE", "3 u 9.5");
        Throwable thrown = assertThrows(InputFileException.class,
                () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals("It's impossible to create shape with a non-numeric parameter value.", thrown.getMessage());
    }

    @Test
    public void testWrongShapeType() {
        InputData inputData = new InputData("CIelfRCLE", "3");
        Throwable thrown = assertThrows(InputFileException.class, () -> ShapeCreatingUtility.createShape(inputData));

        assertEquals("There is no such shape type 'CIelfRCLE'.", thrown.getMessage());
    }
}