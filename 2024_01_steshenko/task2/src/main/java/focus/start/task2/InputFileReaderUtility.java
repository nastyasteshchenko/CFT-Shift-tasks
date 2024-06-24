package focus.start.task2;

import focus.start.task2.exception.InputFileException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

final class InputFileReaderUtility {
    private InputFileReaderUtility() {
    }

    static InputData read(Path inputFile) throws IOException, InputFileException {
        List<String> lines = Files.readAllLines(inputFile, StandardCharsets.UTF_8);
        checkIfEmptyFile(lines);

        String firstLine = lines.get(0);
        if (lines.size() < 2) {
            return new InputData(firstLine, null);
        }

        String secondLine = lines.get(1);

        return new InputData(firstLine, secondLine);
    }

    private static void checkIfEmptyFile(List<String> lines) throws InputFileException {
        if (lines.isEmpty()) {
            throw InputFileException.emptyFile();
        }
    }
}
