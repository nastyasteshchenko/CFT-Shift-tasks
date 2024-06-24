package focus.start.task5;

import focus.start.task5.production.Production;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Program started");
        Configuration configuration = getConfiguration();

        if (configuration != null) {
            LOGGER.debug("Configuration:{}{}", System.lineSeparator(), configuration);
            Production production = new Production(configuration);
            production.start();
            if (waitForExit()) {
                production.finish();
                LOGGER.info("Program ended.");
                return;
            }
        }
        LOGGER.info("Program ended with exception.");
    }

    private static Configuration getConfiguration() {
        try {
            ConfigurationPropertiesReader configurationPropertiesReader = ConfigurationPropertiesReader.create();
            return configurationPropertiesReader.readConfiguration();
        } catch (IOException e) {
            LOGGER.error("Error reading configuration. Error message: {}", e.getMessage());
            System.out.println("Something went wrong while reading configuration.");
        }
        return null;
    }

    private static boolean waitForExit() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 'exit' to exit: ");
            String input = scanner.next();
            if (input.equalsIgnoreCase("exit")) {
                return true;
            }
        }
    }
}