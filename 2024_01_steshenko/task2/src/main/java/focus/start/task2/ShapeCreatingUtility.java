package focus.start.task2;

import focus.start.task2.exception.InputFileException;
import focus.start.task2.shapes.*;

final class ShapeCreatingUtility {
    private ShapeCreatingUtility() {
    }

    static Shape createShape(InputData inputData) throws InputFileException {
        ShapeType shapeType;
        try {
            shapeType = ShapeType.valueOf(inputData.firstLine());
        } catch (IllegalArgumentException e) {
            throw InputFileException.noSuchShapeType(inputData.firstLine());
        }

        if (inputData.secondLine() == null) {
            throw InputFileException.wrongParametersAmountForShapeType(shapeType.name(), 0,
                    shapeType.getParametersAmount());
        }

        String[] parameters = inputData.secondLine().split(" ");
        int parametersAmount = parameters.length;
        checkIfWrongParametersAmount(shapeType, parametersAmount);

        return createShape(shapeType, parameters);
    }

    private static void checkIfWrongParametersAmount(ShapeType shapeType, int inputParamsAmount)
            throws InputFileException {
        if (inputParamsAmount != shapeType.getParametersAmount()) {
            throw InputFileException.wrongParametersAmountForShapeType(shapeType.name(), inputParamsAmount,
                    shapeType.getParametersAmount());
        }
    }

    private static Shape createShape(ShapeType shapeType, String[] parameters) throws InputFileException {
        try {
            switch (shapeType) {
                case CIRCLE -> {
                    double radius = Double.parseDouble(parameters[0]);
                    return Circle.create(radius);
                }
                case TRIANGLE -> {
                    double firstSide = Double.parseDouble(parameters[0]);
                    double secondSide = Double.parseDouble(parameters[1]);
                    double thirdSide = Double.parseDouble(parameters[2]);
                    return Triangle.create(firstSide, secondSide, thirdSide);
                }
                case RECTANGLE -> {
                    double firstSide = Double.parseDouble(parameters[0]);
                    double secondSide = Double.parseDouble(parameters[1]);
                    return Rectangle.create(firstSide, secondSide);
                }
                default -> {
                    return null;
                }
            }
        } catch (NumberFormatException e) {
            throw InputFileException.nonNumericParameterValue();
        }
    }
}
