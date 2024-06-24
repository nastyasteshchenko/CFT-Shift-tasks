package focus.start.task2;

import focus.start.task2.shapes.Shape;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

class ShapeInfoWritingUtility {
    private static final String MESSAGE_FOR_USER_FRAME = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

    static void write(Shape shape, OptionsValues options) throws IOException {
        if (options.outputFile() == null) {
            System.out.println(MESSAGE_FOR_USER_FRAME);
            System.out.println(shape.toString());
            System.out.println(MESSAGE_FOR_USER_FRAME);
            return;
        }

        Files.writeString(options.outputFile(), shape.toString(), Charset.defaultCharset());
    }
}
