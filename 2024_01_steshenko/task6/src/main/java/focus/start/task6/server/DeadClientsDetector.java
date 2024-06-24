package focus.start.task6.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class DeadClientsDetector implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeadClientsDetector.class);
    private static final Double PART_OF_TIMEOUT_TO_SEND_PING_MSG = 0.75;

    private final Collection<ClientInfo> clients;
    private final long timeout;
    private final long toSendPingMessageTimeout;
    private final MessagesManager messagesManager;

    DeadClientsDetector(Collection<ClientInfo> clients, long timeout, MessagesManager messagesManager) {
        this.timeout = timeout;
        this.toSendPingMessageTimeout = (long) (timeout * PART_OF_TIMEOUT_TO_SEND_PING_MSG);
        this.clients = clients;
        this.messagesManager = messagesManager;
    }

    @Override
    public void run() {
        List<ClientInfo> deadClients = new ArrayList<>();

        long currentTime = System.currentTimeMillis();
        for (ClientInfo client : clients) {
            long timeSpend = currentTime - client.getLastCommunication();
            if (timeSpend > timeout) {
                deadClients.add(client);
                LOGGER.debug("Detected dead client: Port: {} Inet address: {} Username: {}." +
                                " Time since last communication: {} ms.",
                        client.getClientSocket().getPort(),
                        client.getClientSocket().getInetAddress().getHostAddress(),
                        client.getUserName(), timeSpend);
                messagesManager.sendParticipantLeftMessage(client.getUserName(), clients);
            } else if (timeSpend > toSendPingMessageTimeout) {
                LOGGER.debug("Detected client needs to be sent ping message: " +
                                "Port: {} Inet address: {} Username: {}" +
                                " Time since last communication: {} ms.",
                        client.getClientSocket().getPort(),
                        client.getClientSocket().getInetAddress().getHostAddress(),
                        client.getUserName(), timeSpend);
                messagesManager.sendPingMessage(client.getOut());
            }
        }
        clients.removeAll(deadClients);
        if (!deadClients.isEmpty()) {
            messagesManager.sendParticipantsListMessage(clients);
        }
    }

}
