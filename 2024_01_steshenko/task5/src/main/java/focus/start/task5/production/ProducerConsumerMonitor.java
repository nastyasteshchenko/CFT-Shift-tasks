package focus.start.task5.production;

class ProducerConsumerMonitor {

    private final Object isNotEmptyMonitor = new Object();
    private final Object isNotFullMonitor = new Object();

    private static final ProducerConsumerMonitor INSTANCE = new ProducerConsumerMonitor();

    private ProducerConsumerMonitor() {
    }

    static ProducerConsumerMonitor getInstance() {
        return INSTANCE;
    }

    void waitIsNotFull() throws InterruptedException {
        synchronized (isNotFullMonitor) {
            isNotFullMonitor.wait();
        }
    }

    void notifyIsNotFull() {
        synchronized (isNotFullMonitor) {
            isNotFullMonitor.notify();
        }
    }

    void waitIsNotEmpty() throws InterruptedException {
        synchronized (isNotEmptyMonitor) {
            isNotEmptyMonitor.wait();
        }
    }

    void notifyIsNotEmpty() {
        synchronized (isNotEmptyMonitor) {
            isNotEmptyMonitor.notify();
        }
    }
}
