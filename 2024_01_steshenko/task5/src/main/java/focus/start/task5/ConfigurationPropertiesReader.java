package focus.start.task5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class ConfigurationPropertiesReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationPropertiesReader.class);

    private static final String CONFIG_PROPERTIES_FILE = "config.properties";
    private final Properties properties;

    private ConfigurationPropertiesReader(Properties properties) {
        this.properties = properties;
    }

    static ConfigurationPropertiesReader create() throws IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE);
        Properties properties = new Properties();
        properties.load(inputStream);
        return new ConfigurationPropertiesReader(properties);
    }

    Configuration readConfiguration() {

        try {
            String producerCountProp = properties.getProperty("producerCount");
            int producerCount = Integer.parseInt(producerCountProp);
            String consumerCountProp = properties.getProperty("consumerCount");
            int consumerCount = Integer.parseInt(consumerCountProp);
            String producerTimeProp = properties.getProperty("producerTime");
            int producerTime = Integer.parseInt(producerTimeProp);
            String consumerTimeProp = properties.getProperty("consumerTime");
            int consumerTime = Integer.parseInt(consumerTimeProp);
            String storageSizeProp = properties.getProperty("storageSize");
            int storageSize = Integer.parseInt(storageSizeProp);
            return new Configuration(producerCount, consumerCount, producerTime, consumerTime, storageSize);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid configuration properties file: {}.", CONFIG_PROPERTIES_FILE);
            System.out.println("Invalid configuration properties file: " + CONFIG_PROPERTIES_FILE);
        }

        return null;
    }
}
