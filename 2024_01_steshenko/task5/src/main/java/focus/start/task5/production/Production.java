package focus.start.task5.production;

import focus.start.task5.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Production {

    private static final Logger LOGGER = LoggerFactory.getLogger(Production.class);

    private final List<Thread> consumers = new ArrayList<>();
    private final List<Thread> producers = new ArrayList<>();

    public Production(Configuration configuration) {
        Storage storage = new Storage(configuration.storageSize());
        LOGGER.debug("Storage with capacity {} was created.", configuration.storageSize());

        for (int i = 0; i < configuration.consumerCount(); i++) {
            Consumer consumer = new Consumer(storage, configuration.consumerTime());
            consumers.add(new Thread(consumer));
        }
        LOGGER.debug("Consumers of amount: {} and consumer time: {} was created.",
                configuration.consumerCount(), configuration.consumerTime());

        for (int i = 0; i < configuration.producerCount(); i++) {
            Producer producer = new Producer(storage, configuration.producerTime());
            producers.add(new Thread(producer));
        }
        LOGGER.debug("Producers of amount: {} and producer time: {} was created.",
                configuration.producerCount(), configuration.producerTime());
    }

    public void start() {
        for (Thread consumer : consumers) {
            consumer.start();
        }
        LOGGER.info("All consumers started");

        for (Thread producer : producers) {
            producer.start();
        }
        LOGGER.info("All producers started");
    }

    public void finish() {
        for (Thread producer : producers) {
            producer.interrupt();
        }
        LOGGER.info("All producers finished");

        for (Thread consumer : consumers) {
            consumer.interrupt();
        }
        LOGGER.info("All consumers finished");
    }
}
