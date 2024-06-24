package focus.start.task1;

import static focus.start.task1.Main.MAX_SIZE;
import static focus.start.task1.Main.MIN_SIZE;

final class InputErrorsUtility {
    private static final String INTEGER_OUT_OF_RANGE_MSG = "!!! You have entered an integer beyond the limits of acceptable values !!!";
    private static final String REAL_INPUT_MSG = "!!! You have entered a real value !!!";
    private static final String NOT_NUMBER_INPUT_MSG = "!!! You have entered a non-numeric value !!!";
    private static final String POSSIBLE_INTEGER_VALUES_MSG = "Possible integer values from " + MAX_SIZE + " to " + MIN_SIZE + ".";

    private InputErrorsUtility() {
    }

    static void printRealInputMsg() {
        printInputErrorMessage(REAL_INPUT_MSG);
    }

    static void printNotNumberInputMsg() {
        printInputErrorMessage(NOT_NUMBER_INPUT_MSG);
    }

    static void printIntegerOutOfRangeMsg() {
        printInputErrorMessage(INTEGER_OUT_OF_RANGE_MSG);
    }

    private static void printInputErrorMessage(String error) {
        String out = System.lineSeparator() + error + System.lineSeparator()
                + POSSIBLE_INTEGER_VALUES_MSG + System.lineSeparator();
        System.out.println(out);
    }

}
