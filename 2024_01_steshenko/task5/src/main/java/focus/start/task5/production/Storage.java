package focus.start.task5.production;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

class Storage {

    private static final Logger LOGGER = LoggerFactory.getLogger(Storage.class);

    private final Queue<Product> products = new LinkedList<>();
    private final int capacity;

    Storage(int capacity) {
        this.capacity = capacity;
    }

    synchronized void produce(Product product) throws InterruptedException {
        products.offer(product);
        LOGGER.debug("Product with id: {} has been added to the storage. Storage size: {}.",
                product.getId(), products.size());
    }

    synchronized Product consume() throws InterruptedException {
        Product product = products.poll();
        if (product != null) {
            LOGGER.debug("Product with id: {} has been get from the storage. Storage size: {}.",
                    product.getId(), products.size());
        }
        return product;
    }

    synchronized boolean isFull() {
        return products.size() >= capacity;
    }

    synchronized boolean isEmpty() {
        return products.isEmpty();
    }
}
