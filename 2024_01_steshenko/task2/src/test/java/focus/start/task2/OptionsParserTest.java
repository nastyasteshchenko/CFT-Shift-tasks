package focus.start.task2;

import focus.start.task2.exception.OptionsValuesException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    private final static String SET_INPUT_FILE_OPTION = "-i";
    private final static String SET_OUTPUT_FILE_OPTION = "-f";
    private final static String OUTPUT_FILE_NAME = "output.txt";
    private final static String INPUT_FILE_NAME = "input.txt";
    private final static CommandLineParser commandLineParser = new CommandLineParser();
    @TempDir
    private static Path inputDir;
    private static Path inputFile;

    @BeforeAll
    public static void setup() throws IOException {
        inputFile = inputDir.resolve(INPUT_FILE_NAME);
        Files.createFile(inputFile);
    }

    @Test
    public void testEmptyArgs() {
        assertThrows(ParseException.class, () -> commandLineParser.parse(new String[]{}));
    }

    @Test
    public void testNoRequiredOption() {
        assertThrows(ParseException.class,
                () -> commandLineParser.parse(new String[]{SET_OUTPUT_FILE_OPTION, OUTPUT_FILE_NAME}));
    }

    @Test
    public void testSetOutputFileOption() throws OptionsValuesException, ParseException {
        OptionsValues optionsValues = commandLineParser.parse(new String[]{SET_OUTPUT_FILE_OPTION, OUTPUT_FILE_NAME,
                SET_INPUT_FILE_OPTION, inputFile.toString()});

        assertEquals(inputFile, optionsValues.inputFile());
        assertEquals(Path.of(OUTPUT_FILE_NAME), optionsValues.outputFile());
    }

    @Test
    public void testDuplicateInputFile() {
        Throwable thrown = assertThrows(OptionsValuesException.class, () ->
                commandLineParser.parse(new String[]{SET_INPUT_FILE_OPTION, "filename",
                        SET_INPUT_FILE_OPTION, inputFile.toString()}));

        assertEquals("Double definition of input file.", thrown.getMessage());

    }

    @Test
    public void testNoArgumentForSetOutputFileOption() throws OptionsValuesException, ParseException {
        OptionsValues optionsValues =
                commandLineParser.parse(new String[]{SET_INPUT_FILE_OPTION, inputFile.toString(),
                        SET_OUTPUT_FILE_OPTION});

        assertEquals(inputFile, optionsValues.inputFile());
        assertEquals(Path.of("shape_info.out"), optionsValues.outputFile());
    }

    @Test
    public void testNoArgumentForSetInputFileOption() {
        assertThrows(ParseException.class, () -> commandLineParser.parse(new String[]{SET_INPUT_FILE_OPTION,
                SET_OUTPUT_FILE_OPTION, OUTPUT_FILE_NAME}));
    }

    @Test
    public void testNotExistingDirectory() {
        String notExistingFile = inputDir.resolve("i").toString();
        Throwable thrown = assertThrows(OptionsValuesException.class, () ->
                commandLineParser.parse(new String[]{SET_INPUT_FILE_OPTION, notExistingFile}));

        assertEquals(String.format("Cannot access '%s': No such file or directory.", notExistingFile),
                thrown.getMessage());
    }

    @Test
    public void testInputFileIsDirectory() {
        Throwable thrown = assertThrows(OptionsValuesException.class, () ->
                commandLineParser.parse(new String[]{SET_INPUT_FILE_OPTION, inputDir.toString()}));

        assertEquals(String.format("Cannot read from '%s'.", inputDir), thrown.getMessage());
    }

    @Test
    public void testNotExistingOption() {
        assertThrows(ParseException.class,
                () -> commandLineParser.parse(new String[]{SET_INPUT_FILE_OPTION, inputFile.toString(),
                        "-r", "r.txt"}));
    }
}