package focus.start.task5;

public record Configuration(int producerCount, int consumerCount, int producerTime, int consumerTime, int storageSize) {

    @Override
    public String toString() {
        return String.format("Producer count = %d%nConsumer count = %d%n" +
                        "Producer time = %d%nConsumer time = %d%nStorage size = %d",
                producerCount, consumerCount, producerTime, consumerTime, storageSize);
    }
}
