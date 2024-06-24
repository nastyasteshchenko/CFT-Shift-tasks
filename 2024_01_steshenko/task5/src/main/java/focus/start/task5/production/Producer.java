package focus.start.task5.production;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Producer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    private static final long MILLISECONDS_IN_SECOND = 1000L;
    private static final ProducerConsumerMonitor PRODUCER_CONSUMER_MONITOR = ProducerConsumerMonitor.getInstance();
    private static final IdGenerator ID_GENERATOR = new IdGenerator();

    private final long id = ID_GENERATOR.getId();
    private final int producerTime;
    private final Storage storage;

    Producer(Storage storage, int producerTime) {
        this.storage = storage;
        this.producerTime = producerTime;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        boolean isWaited = false;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                while (storage.isFull()) {
                    isWaited = true;
                    long elapsedTime = (System.currentTimeMillis() - startTime) / MILLISECONDS_IN_SECOND;
                    LOGGER.debug("Producer with id: {} starts to wait while storage will not be full. Time: {} secs.",
                            id, elapsedTime);
                    PRODUCER_CONSUMER_MONITOR.waitIsNotFull();
                }

                if (isWaited) {
                    long elapsedTime = (System.currentTimeMillis() - startTime) / MILLISECONDS_IN_SECOND;
                    LOGGER.debug("Producer with id: {} resumed its work. Time: {} secs.", id, elapsedTime);
                    isWaited = false;
                }

                Product product = new Product();
                storage.produce(product);
                long elapsedTime = (System.currentTimeMillis() - startTime) / MILLISECONDS_IN_SECOND;
                LOGGER.debug("Producer with id: {} produced the product with id: {}. Time: {} secs.",
                        id, product.getId(), elapsedTime);

                PRODUCER_CONSUMER_MONITOR.notifyIsNotEmpty();

                synchronized (this) {
                    this.wait(producerTime * MILLISECONDS_IN_SECOND);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.warn("Producer with id: {} interrupted.", id);
        }
    }
}
