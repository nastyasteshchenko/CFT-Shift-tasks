package focus.start.task2;

import focus.start.task2.exception.OptionsValuesException;
import org.apache.commons.cli.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

class CommandLineParser {
    private final static String YES = "Y";
    private final static String DEFAULT_OUTPUT_FILE = "shape_info.out";
    private final static int NUMBER_OF_ARGS = 1;
    private final static String OPTIONS_ARG_NAME = "path";
    private final static String SET_INPUT_FILE_OPTION_SHORT_NAME = "i";
    private final static String SET_OUTPUT_FILE_OPTION_SHORT_NAME = "f";
    private final static String SET_OUTPUT_FILE_OPTION_DESCRIPTION = "set output file for shape info";
    private final static String SET_INPUT_FILE_OPTION_DESCRIPTION = "set input file";
    private final Options options;

    CommandLineParser() {
        Option inputFile =
                createOption(SET_INPUT_FILE_OPTION_SHORT_NAME, SET_INPUT_FILE_OPTION_DESCRIPTION,
                        true, false);
        Option outputFile =
                createOption(SET_OUTPUT_FILE_OPTION_SHORT_NAME, SET_OUTPUT_FILE_OPTION_DESCRIPTION,
                        false, true);

        options = new Options();
        options.addOption(inputFile)
                .addOption(outputFile);
    }

    OptionsValues parse(String[] args) throws ParseException, OptionsValuesException {
        CommandLine commandLine = new DefaultParser().parse(options, args);

        String[] inputPathValues = commandLine.getOptionValues(SET_INPUT_FILE_OPTION_SHORT_NAME);
        if (inputPathValues.length > 1) {
            throw OptionsValuesException.doubleDefinitionOfInputFile();
        }
        String inputPath = inputPathValues[0];

        Path inputFile = Path.of(inputPath);
        checkIfFileExists(inputPath, inputFile);
        if (!Files.isRegularFile(inputFile.toAbsolutePath())) {
            throw OptionsValuesException.wrongInputFile(inputPath);
        }

        boolean hasOutputOption = commandLine.hasOption(SET_OUTPUT_FILE_OPTION_SHORT_NAME);
        Path path = null;
        if (hasOutputOption) {
            String outputPath = commandLine.getOptionValue(SET_OUTPUT_FILE_OPTION_SHORT_NAME);
            if (outputPath == null) {
                outputPath = DEFAULT_OUTPUT_FILE;
            }
            path = Path.of(outputPath);
            checkIfAllowToOverwriteFile(path, outputPath);
        }
        return new OptionsValues(inputFile, path);
    }

    private Option createOption(String shortName, String description, boolean required, boolean optionalArg) {
        return Option.builder(shortName)
                .argName(OPTIONS_ARG_NAME)
                .desc(description)
                .hasArg()
                .numberOfArgs(NUMBER_OF_ARGS)
                .required(required)
                .optionalArg(optionalArg)
                .build();
    }

    private static void checkIfAllowToOverwriteFile(Path path, String outputPath) throws OptionsValuesException {
        if (!Files.notExists(path.toAbsolutePath())) {
            Scanner in = new Scanner(System.in);
            System.out.println("The file '" + outputPath + "' is already exists. Do you really want to continue? " +
                    "The file data will be overwritten. Y or N? : ");
            String userDecision = in.next();
            if (!YES.equalsIgnoreCase(userDecision)) {
                throw OptionsValuesException.forbiddenOverwrittenFile(outputPath);
            }
        }
    }

    private void checkIfFileExists(String inputPath, Path file) throws OptionsValuesException {
        if (Files.notExists(file.toAbsolutePath())) {
            throw OptionsValuesException.wrongFile(inputPath);
        }
    }
}
