package focus.start.task4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String MESSAGE_FOR_USER_FRAME = "----------------------------------";
    private static final int STOP_PROGRAM_RETURN_VALUE = -1;
    private static final String STOP_PROGRAM_INPUT = "exit";

    public static void main(String[] args) {
        LOGGER.info("Program started.");
        long seriesLength = getSeriesLength();
        LOGGER.debug("Series length was successfully read from console. Length: {}.", seriesLength);

        Double sum = calculateSum(seriesLength);
        if (sum != null) {
            System.out.println(MESSAGE_FOR_USER_FRAME);
            System.out.println("Calculated sum: " + sum);
            System.out.println(MESSAGE_FOR_USER_FRAME);
            LOGGER.info("Program successfully ended.");
            return;
        }
        LOGGER.info("Program ended with exception.");
    }

    private static Double calculateSum(long seriesLength) {
        try {
            SeriesSumCalculator seriesSumCalculator = new SeriesSumCalculator();
            return seriesSumCalculator.calculateSum(seriesLength, CalculatingSeriesSumAlgorithm::new);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to calculate series sum with length {}.{}{}", seriesLength,
                    System.lineSeparator(), e.getMessage());
            System.out.println("Something went wrong while trying to calculate series sum with length "
                    + seriesLength + ".");
            return null;
        }
    }

    private static long getSeriesLength() {
        Scanner in = new Scanner(System.in);
        System.out.println("To end the program enter '" + STOP_PROGRAM_INPUT + "'");
        while (true) {
            System.out.print("Enter series length: ");
            String inputStr = in.next();
            LOGGER.debug("Input string: '{}'.", inputStr);
            if (STOP_PROGRAM_INPUT.equalsIgnoreCase(inputStr)) {
                return STOP_PROGRAM_RETURN_VALUE;
            }
            try {
                return Long.parseLong(inputStr);
            } catch (NumberFormatException e1) {
                LOGGER.warn("Not a valid number was entered.");
                System.out.println("!!! Please enter an integer. !!!");
            }
        }
    }
}