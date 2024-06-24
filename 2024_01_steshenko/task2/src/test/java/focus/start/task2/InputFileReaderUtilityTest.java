package focus.start.task2;

import focus.start.task2.exception.InputFileException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class InputFileReaderUtilityTest {
    private final static String INPUT_FILE_NAME = "input.txt";
    @TempDir
    private static Path inputDir;
    private static Path inputFile;

    @BeforeAll
    public static void setup() throws IOException {
        inputFile = inputDir.resolve(INPUT_FILE_NAME);
        Files.createFile(inputFile);
    }

    @Test
    public void testEmptyInputFile() throws IOException {
        String triangleFileData = "";
        Files.writeString(inputFile, triangleFileData, StandardCharsets.UTF_8);
        Throwable thrown = assertThrows(InputFileException.class, () -> InputFileReaderUtility.read(inputFile));

        assertEquals("Input file is empty.", thrown.getMessage());
    }

    @Test
    public void testEmptySecondLine() throws IOException, InputFileException {
        String triangleFileData = "CIRCLE";
        Files.writeString(inputFile, triangleFileData, StandardCharsets.UTF_8);
        InputData inputData = InputFileReaderUtility.read(inputFile);

        assertEquals(new InputData("CIRCLE", null), inputData);
    }

    @Test
    public void testBothLinesNotEmpty() throws IOException, InputFileException {
        String triangleFileData = """
                CIRCLE
                3 7 8
                """;
        Files.writeString(inputFile, triangleFileData, StandardCharsets.UTF_8);
        InputData inputData = InputFileReaderUtility.read(inputFile);

        assertEquals(new InputData("CIRCLE", "3 7 8"), inputData);
    }
}