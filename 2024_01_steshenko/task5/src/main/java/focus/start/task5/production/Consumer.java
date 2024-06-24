package focus.start.task5.production;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Consumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private static final long MILLISECONDS_IN_SECOND = 1000L;
    private static final ProducerConsumerMonitor PRODUCER_CONSUMER_MONITOR = ProducerConsumerMonitor.getInstance();
    private static final IdGenerator ID_GENERATOR = new IdGenerator();

    private final long id = ID_GENERATOR.getId();
    private final int consumerTime;
    private final Storage storage;

    Consumer(Storage storage, int consumerTime) {
        this.storage = storage;
        this.consumerTime = consumerTime;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        boolean isWaited = false;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                while (storage.isEmpty()) {
                    isWaited = true;
                    long elapsedTime = (System.currentTimeMillis() - startTime) / MILLISECONDS_IN_SECOND;
                    LOGGER.debug("Consumer with id: {} starts to wait while storage will not be empty. Time: {} secs.",
                            id, elapsedTime);
                    PRODUCER_CONSUMER_MONITOR.waitIsNotEmpty();
                }

                if (isWaited) {
                    long elapsedTime = (System.currentTimeMillis() - startTime) / MILLISECONDS_IN_SECOND;
                    LOGGER.debug("Consumer with id: {} resumed its work. Time: {} secs.", id, elapsedTime);
                    isWaited = false;
                }

                Product product = storage.consume();
                long elapsedTime = (System.currentTimeMillis() - startTime) / MILLISECONDS_IN_SECOND;
                LOGGER.debug("Consumer with id: {} consumed the product with id: {}. Time: {} secs.",
                        id, product.getId(), elapsedTime);

                PRODUCER_CONSUMER_MONITOR.notifyIsNotFull();

                synchronized (this) {
                    this.wait(consumerTime * MILLISECONDS_IN_SECOND);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.warn("Consumer with id: {} interrupted.", id);
        }
    }
}
