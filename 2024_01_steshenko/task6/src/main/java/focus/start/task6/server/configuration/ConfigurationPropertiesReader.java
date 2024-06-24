package focus.start.task6.server.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigurationPropertiesReader {

    private static final String CONFIG_PROPERTIES_FILE = Path.of("server", "config.properties").toString();
    private final Properties properties;

    private ConfigurationPropertiesReader(Properties properties) {
        this.properties = properties;
    }

    public static ConfigurationPropertiesReader create() throws IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE);
        Properties properties = new Properties();
        properties.load(inputStream);
        return new ConfigurationPropertiesReader(properties);
    }

    public Configuration readConfiguration() {

        try {
            String portProp = properties.getProperty("port");
            int port = Integer.parseInt(portProp);
            String timeoutProp = properties.getProperty("timeout");
            long timeout = Long.parseLong(timeoutProp);
            return new Configuration(port, timeout);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid configuration properties file: " + CONFIG_PROPERTIES_FILE);
        }

        return null;
    }
}
