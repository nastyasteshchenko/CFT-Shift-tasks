package focus.start.task2;

import focus.start.task2.exception.OptionsValuesException;
import focus.start.task2.shapes.Shape;
import focus.start.task2.exception.InputFileException;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    private static final String PROGRAM_ENDING_WITH_ERROR_MSG = "Program ended with error.";
    private static final String AVAILABLE_OPTIONS = """
            Available options:

            -i <path>\t\tset the input file
            -f <path>\t\tset the output file for shape info""";

    private static final String MESSAGE_FOR_USER_FRAME = "----------------------------------";
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.info("Program started.");
        OptionsValues optionsValues = parseCommandLine(args);
        if (optionsValues == null) {
            LOGGER.info(PROGRAM_ENDING_WITH_ERROR_MSG);
            return;
        }

        InputData inputData = readInputFile(optionsValues);
        if (inputData == null) {
            LOGGER.info(PROGRAM_ENDING_WITH_ERROR_MSG);
            return;
        }

        Shape shape = createShape(inputData);
        if (shape == null) {
            LOGGER.info(PROGRAM_ENDING_WITH_ERROR_MSG);
            return;
        }

        if (!writeShapeInfo(shape, optionsValues)) {
            LOGGER.info(PROGRAM_ENDING_WITH_ERROR_MSG);
        }

        LOGGER.info("Program successfully ended.");
    }

    private static OptionsValues parseCommandLine(String[] args) {
        LOGGER.info("Command line parsing started");
        LOGGER.debug(String.format("Args: %s", Arrays.toString(args)));
        CommandLineParser commandLineParser = new CommandLineParser();
        try {
            OptionsValues optionsValues = commandLineParser.parse(args);
            LOGGER.info("Command line parsing successfully completed.");
            return optionsValues;
        } catch (ParseException | OptionsValuesException e) {
            LOGGER.error(e.getMessage());
            printErrorMessage(e.getMessage(), true);
            return null;
        }
    }

    private static InputData readInputFile(OptionsValues optionsValues) {
        LOGGER.info(String.format("Reading data from input file '%s' started.", optionsValues.inputFile()));
        try {
            InputData inputData = InputFileReaderUtility.read(optionsValues.inputFile());
            LOGGER.debug(String.format("First line: '%s'; second line: '%s'.",
                    inputData.firstLine(), inputData.secondLine()));
            LOGGER.info(String.format("Reading data from input file '%s' successfully completed.",
                    optionsValues.inputFile()));
            return inputData;
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage());
            System.out.println("Something went wrong while reading shape parameters.");
        } catch (InputFileException e2) {
            LOGGER.error(e2.getMessage());
            printErrorMessage(e2.getMessage(), false);
        }
        return null;
    }

    private static Shape createShape(InputData inputData) {
        LOGGER.info("Creating shape from input data started.");
        try {
            Shape shape = ShapeCreatingUtility.createShape(inputData);
            LOGGER.info("Creating shape from input data successfully completed.");
            return shape;
        } catch (InputFileException e1) {
            LOGGER.error(e1.getMessage());
            printErrorMessage(e1.getMessage(), false);
            return null;
        }
    }

    private static boolean writeShapeInfo(Shape shape, OptionsValues optionsValues) {
        boolean isSuccess = true;
        if (optionsValues.outputFile() == null) {
            LOGGER.info("Writing information about the shape to console started.");
        } else {
            LOGGER.info(String.format("Writing information about the shape to file '%s' started.",
                    optionsValues.outputFile()));
        }

        try {
            ShapeInfoWritingUtility.write(shape, optionsValues);
            LOGGER.info("Writing information about the shape successfully completed.");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            System.out.println("Something went wrong while writing shape info.");
            isSuccess = false;
        }
        return isSuccess;
    }

    private static void printErrorMessage(String e, boolean needPrintAvailableOptions) {
        System.out.println(MESSAGE_FOR_USER_FRAME);
        System.out.println(e);
        if (needPrintAvailableOptions) {
            System.out.println();
            System.out.println(AVAILABLE_OPTIONS);
        }
        System.out.println(MESSAGE_FOR_USER_FRAME);
    }
}