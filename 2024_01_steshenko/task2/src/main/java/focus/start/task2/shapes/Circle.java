package focus.start.task2.shapes;

import focus.start.task2.exception.InputFileException;

public final class Circle extends Shape {
    private final double radius;

    private Circle(double radius) {
        this.radius = radius;
    }

    public static Circle create(double radius) throws InputFileException {
        if (radius < 0) {
            throw InputFileException.negativeParameterValue();
        }
        return new Circle(radius);
    }

    @Override
    public String toString() {
        return String.format("Тип фигуры: Круг%n%sРадиус: %s %s%nДиаметр: %s %s",
                super.toString(),
                DECIMAL_FORMAT.format(radius), MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(calculateDiameter()), MEASUREMENT_UNIT);
    }

    public double calculateDiameter() {
        return radius * 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Circle circle) {
            return circle.radius == radius;
        }
        return false;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}
