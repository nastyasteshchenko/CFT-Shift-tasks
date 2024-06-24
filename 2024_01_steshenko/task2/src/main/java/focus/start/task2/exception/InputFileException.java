package focus.start.task2.exception;

import java.text.DecimalFormat;

public class InputFileException extends Exception {
    private InputFileException(String message) {
        super(message);
    }

    public static InputFileException notExistingTriangle(double firstSide, double secondSide, double thirdSide) {
        DecimalFormat df = new DecimalFormat("#.##");
        return new InputFileException(String.format("Triangle with parameters: (%s, %s, %s) doesn't exist.",
                df.format(firstSide), df.format(secondSide), df.format(thirdSide)));
    }

    public static InputFileException negativeParameterValue() {
        return new InputFileException("It's impossible to create shape with negative parameters.");
    }

    public static InputFileException nonNumericParameterValue() {
        return new InputFileException("It's impossible to create shape with a non-numeric parameter value.");
    }

    public static InputFileException noSuchShapeType(String inputShapeType) {
        return new InputFileException(String.format("There is no such shape type '%s'.", inputShapeType));
    }

    public static InputFileException emptyFile() {
        return new InputFileException("Input file is empty.");
    }

    public static InputFileException wrongParametersAmountForShapeType(String shapeType,
                                                                       int actualParametersAmount,
                                                                       int expectedParametersAmount) {
        return new InputFileException(
                String.format("Wrong parameters amount for shape type '%s'%nactual: %d, expected: %d.",
                        shapeType, actualParametersAmount, expectedParametersAmount));
    }

}
