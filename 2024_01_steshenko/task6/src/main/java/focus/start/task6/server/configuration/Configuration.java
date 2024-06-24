package focus.start.task6.server.configuration;

public record Configuration(int port, long timeout) {

    @Override
    public String toString() {
        return "Port: " + port + ", Timeout: " + timeout;
    }
}
