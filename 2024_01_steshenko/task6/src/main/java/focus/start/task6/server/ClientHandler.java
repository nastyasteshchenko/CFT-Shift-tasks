package focus.start.task6.server;

import focus.start.task6.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

class ClientHandler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private final Collection<ClientInfo> clients;
    private final Socket clientSocket;
    private ClientInfo currentClient;
    private final MessagesManager messagesManager;

    ClientHandler(Collection<ClientInfo> clients, Socket clientSocket, MessagesManager messagesManager) {
        this.clients = clients;
        this.clientSocket = clientSocket;
        this.messagesManager = messagesManager;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            String firstClientMessage = in.readLine();
            receivedMessageLoggerDebug(firstClientMessage);
            Message message = messagesManager.readMessage(firstClientMessage);
            if (message instanceof LoginMessage lm) {
                if (clients.stream().anyMatch(c -> c.getUserName().equals(lm.getUsername()))) {
                    messagesManager.sendErrorMessage("Such username is already in use.", out);
                    closeClientSocket(in, out);
                    return;
                } else {
                    messagesManager.sendSuccessMessage(out);
                    currentClient = new ClientInfo(lm.getUsername(), clientSocket, out, in, System.currentTimeMillis());
                    clients.add(currentClient);
                    messagesManager.sendParticipantJoinMessage(lm.getUsername(), clients);
                    messagesManager.sendParticipantsListMessage(clients);
                }
                listenToClient(in);
            } else {
                closeClientSocket(in, out);
            }
        } catch (IOException e) {
            if (!clientSocket.isClosed()) {
                LOGGER.error("Failed to open client socket stream .", e);
            } else {
                clientSocketClosedLoggerDebug();
            }
        }
    }

    private void closeClientSocket(BufferedReader in, PrintWriter out) {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            LOGGER.error("Failed to close client socket.", e);
        }
    }

    private void listenToClient(BufferedReader in) {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                receivedMessageLoggerDebug(line);
                handleMessage(line);
            }
        } catch (IOException e) {
            if (!clientSocket.isClosed()) {
                LOGGER.error("Failed to read from client socket.", e);
            } else {
                clientSocketClosedLoggerDebug();
            }
        }
    }

    private void handleMessage(String line) {
        Message message = messagesManager.readMessage(line);
        if (message instanceof TextMessage textMessage) {
            if (textMessage.getSender() == null) {
                textMessage.setSender(currentClient.getUserName());
            }
            currentClient.setLastCommunication(System.currentTimeMillis());
            for (ClientInfo clientInfo : clients) {
                messagesManager.sendTextMessage(clientInfo.getOut(), textMessage);
            }
        } else if (message instanceof PingMessage) {
            currentClient.setLastCommunication(System.currentTimeMillis());
        }
    }

    private void clientSocketClosedLoggerDebug() {
        LOGGER.debug("Client socket with Port: {} Inet address: {} was closed",
                clientSocket.getPort(), clientSocket.getInetAddress().getHostAddress());
    }

    private void receivedMessageLoggerDebug(String msg) {
        LOGGER.debug("Server from client with Port: {} Inet address: {} received: {}",
                clientSocket.getPort(), clientSocket.getInetAddress().getHostAddress(), msg);
    }
}
