package focus.start.task6.server;

import focus.start.task6.server.configuration.Configuration;
import focus.start.task6.server.configuration.ConfigurationPropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        LOGGER.info("Starting server.");
        Configuration configuration = getConfiguration();

        if (configuration != null) {
            LOGGER.debug("Configuration was read: {}", configuration);
            Server server = createServer(configuration);
            if (server != null) {
                LOGGER.info("Server started.");
                Thread serverThread = new Thread(server);
                serverThread.start();

                waitForStop();

                server.stop();
                LOGGER.info("Stopping server is started.");
            }
        }
        LOGGER.info("Server stopped.");
    }

    private static void waitForStop() {
        System.out.println("Enter 'stop' or 'exit' to stop the server.");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        while (!input.equalsIgnoreCase("exit") && !input.equalsIgnoreCase("stop")) {
            input = in.nextLine();
        }
    }

    private static Configuration getConfiguration() {
        try {
            ConfigurationPropertiesReader configurationPropertiesReader = ConfigurationPropertiesReader.create();
            return configurationPropertiesReader.readConfiguration();
        } catch (IOException e) {
            LOGGER.error("Could not read configuration.", e);
        }
        return null;
    }

    private static Server createServer(Configuration configuration) {
        try {
            return Server.create(configuration);
        } catch (IOException e) {
            LOGGER.error("Could not create server.", e);
        }
        return null;
    }
}