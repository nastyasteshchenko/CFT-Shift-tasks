package focus.start.task2.exception;

public class OptionsValuesException extends Exception {
    private OptionsValuesException(String message) {
        super(message);
    }

    public static OptionsValuesException doubleDefinitionOfInputFile() {
        return new OptionsValuesException("Double definition of input file.");
    }

    public static OptionsValuesException wrongInputFile(String fileName) {
        return new OptionsValuesException(String.format("Cannot read from '%s'.", fileName));
    }

    public static OptionsValuesException forbiddenOverwrittenFile(String filePath) {
        return new OptionsValuesException(String.format("It is forbidden to overwrite the file '%s'.", filePath));
    }

    public static OptionsValuesException wrongFile(String filePath) {
        return new OptionsValuesException(String.format("Cannot access '%s': No such file or directory.", filePath));
    }
}
