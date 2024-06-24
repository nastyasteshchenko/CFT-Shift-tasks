package focus.start.task6.server;

import focus.start.task6.server.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.*;

class Server implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final double PART_OF_TIMEOUT_TO_CHECK_DEAD_CLIENTS = 0.5;

    private final long delayForCheckingDeadClients;
    private final ExecutorService executor;
    private final ServerSocket serverSocket;
    private boolean isRunning;
    private final Configuration configuration;

    private Server(ServerSocket serverSocket, Configuration configuration) {
        executor = Executors.newCachedThreadPool();
        delayForCheckingDeadClients = (long) (configuration.timeout() * PART_OF_TIMEOUT_TO_CHECK_DEAD_CLIENTS);
        this.configuration = configuration;
        this.serverSocket = serverSocket;
    }

    static Server create(Configuration configuration) throws IOException {
        ServerSocket serverSocket = new ServerSocket(configuration.port());
        return new Server(serverSocket, configuration);
    }

    @Override
    public void run() {
        MessagesManager messagesManager = new MessagesManager();
        Collection<ClientInfo> clients = new ConcurrentLinkedQueue<>();
        ScheduledExecutorService scheduledExecutorService = startDeadClientsDetector(clients, messagesManager);
        isRunning = true;
        LOGGER.debug("Server started on port {}", serverSocket.getLocalPort());
        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                LOGGER.debug("Accepted connection from Port: {} Inet address: {}",
                        clientSocket.getPort(), clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clients, clientSocket, messagesManager);
                executor.execute(clientHandler);
                LOGGER.info("Handling client from Port: {} Inet address: {} was started.",
                        clientSocket.getPort(), clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    LOGGER.error("Error while accepting client connection.", e);
                } else {
                    LOGGER.info("Server socket was closed.");
                }
            }
        }
        scheduledExecutorService.shutdown();
        LOGGER.info("Dead clients detector stopped.");
        for (ClientInfo clientInfo : clients) {
            messagesManager.sendEndChatMessage(clientInfo.getOut());
        }
        closeAllSockets(clients);
    }

    void stop() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.error("Error while closing server socket.", e);
        }
    }

    private static void closeAllSockets(Collection<ClientInfo> clients) {
        for (ClientInfo clientInfo : clients) {
            clientInfo.getOut().close();
            try {
                clientInfo.getIn().close();
            } catch (IOException e) {
                LOGGER.error("Error while closing client input stream.", e);
            }
            try {
                clientInfo.getClientSocket().close();
            } catch (IOException e) {
                LOGGER.error("Error while closing client socket.", e);
            }
        }
    }

    private ScheduledExecutorService startDeadClientsDetector(Collection<ClientInfo> clients,
                                                              MessagesManager messagesManager) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable deadClientsDetector =
                new DeadClientsDetector(clients, configuration.timeout(), messagesManager);
        scheduledExecutorService.scheduleAtFixedRate(deadClientsDetector, delayForCheckingDeadClients,
                delayForCheckingDeadClients, TimeUnit.MICROSECONDS);
        Thread deadClientsDetectorThread = new Thread(deadClientsDetector);
        deadClientsDetectorThread.start();
        LOGGER.info("Dead clients detector started.");
        return scheduledExecutorService;
    }
}
