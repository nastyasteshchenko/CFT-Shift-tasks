package focus.start.task2.shapes;

import focus.start.task2.exception.InputFileException;

public final class Rectangle extends Shape {
    private final double length;
    private final double width;

    private Rectangle(double firstSide, double secondSide) {
        length = Math.max(firstSide, secondSide);
        width = Math.min(firstSide, secondSide);
    }

    public static Rectangle create(double firstSide, double secondSide) throws InputFileException {
        if (firstSide < 0 || secondSide < 0) {
            throw InputFileException.negativeParameterValue();
        }
        return new Rectangle(firstSide, secondSide);
    }

    @Override
    public String toString() {
        return String.format("Тип фигуры: Прямоугольник%n%sДлина: %s %s%nШирина: %s %s%nДиагональ: %s %s",
                super.toString(),
                DECIMAL_FORMAT.format(length), MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(width), MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(calculateDiagonal()), MEASUREMENT_UNIT);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Rectangle rectangle) {
            return rectangle.length == length && rectangle.width == width;
        }
        return false;
    }

    public double calculateDiagonal() {
        return Math.sqrt(length * length + width * width);
    }

    @Override
    public double calculateArea() {
        return length * width;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (length + width);
    }
}
