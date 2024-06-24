package focus.start.task2.shapes;

import focus.start.task2.exception.InputFileException;

public final class Triangle extends Shape {
    private final static double FULL_ANGLE_RADIANS = Math.PI;
    private final static String ANGLE_MEASUREMENT_UNIT = "°";
    private final double firstSide;
    private final double secondSide;
    private final double thirdSide;
    private final double oppositeFirstSideAngleDegree;
    private final double oppositeSecondSideAngleDegree;
    private final double oppositeThirdSideAngleDegree;

    private Triangle(double firstSide, double secondSide, double thirdSide) {
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.thirdSide = thirdSide;

        double firstSideSquare = firstSide * firstSide;
        double secondSideSquare = secondSide * secondSide;
        double thirdSideSquare = thirdSide * thirdSide;

        double oppositeFirstSideAngleRadians =
                Math.acos((secondSideSquare + thirdSideSquare - firstSideSquare) / (2 * secondSide * thirdSide));
        oppositeFirstSideAngleDegree = Math.toDegrees(oppositeFirstSideAngleRadians);

        double oppositeSecondSideAngleRadians =
                Math.acos((firstSideSquare + thirdSideSquare - secondSideSquare) / (2 * firstSide * thirdSide));
        oppositeSecondSideAngleDegree = Math.toDegrees(oppositeSecondSideAngleRadians);

        oppositeThirdSideAngleDegree =
                Math.toDegrees(FULL_ANGLE_RADIANS - oppositeFirstSideAngleRadians - oppositeSecondSideAngleRadians);
    }

    public static Triangle create(double firstSide, double secondSide, double thirdSide) throws InputFileException {
        if (firstSide < 0 || secondSide < 0 || thirdSide < 0) {
            throw InputFileException.negativeParameterValue();
        }

        if (firstSide + secondSide > thirdSide && firstSide + thirdSide > secondSide
                && firstSide + thirdSide > secondSide) {
            return new Triangle(firstSide, secondSide, thirdSide);
        }

        throw InputFileException.notExistingTriangle(firstSide, secondSide, thirdSide);
    }

    @Override
    public String toString() {
        return String.format("Тип фигуры: Треугольник%n%s" +
                        "Длина первой стороны: %s %s%nПротиволежащий угол первой стороны: %s%s%n" +
                        "Длина второй стороны: %s %s%nПротиволежащий угол второй стороны: %s%s%n" +
                        "Длина третьей стороны: %s %s%nПротиволежащий угол третьей стороны: %s%s",
                super.toString(),
                DECIMAL_FORMAT.format(firstSide), MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(oppositeFirstSideAngleDegree), ANGLE_MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(secondSide), MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(oppositeSecondSideAngleDegree), ANGLE_MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(thirdSide), MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(oppositeThirdSideAngleDegree), ANGLE_MEASUREMENT_UNIT);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Triangle triangle) {
            return triangle.firstSide == firstSide && triangle.secondSide == secondSide
                    && triangle.thirdSide == thirdSide;
        }
        return false;
    }

    @Override
    public double calculateArea() {
        return 0.5 * firstSide * secondSide * Math.sin(Math.toRadians(oppositeThirdSideAngleDegree));
    }

    @Override
    public double calculatePerimeter() {
        return firstSide + secondSide + thirdSide;
    }
}
