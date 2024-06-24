package focus.start.task6.client.model;

import focus.start.task6.client.controller.listener.ConnectServerListener;
import focus.start.task6.client.controller.listener.ExitListener;
import focus.start.task6.client.controller.listener.SendMessageListener;
import focus.start.task6.client.model.listener.CloseViewListener;
import focus.start.task6.client.model.listener.ErrorMessageListener;
import focus.start.task6.client.model.listener.ReceiveMessageListener;
import focus.start.task6.client.model.listener.UpdateParticipantsListListener;
import focus.start.task6.client.view.MessageType;
import focus.start.task6.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientModel implements ConnectServerListener, SendMessageListener, ExitListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientModel.class);

    private String currentClientName;
    private PrintWriter out;
    private BufferedReader in;
    private Socket serverSocket;

    private final MessagesManager messagesManager = new MessagesManager();

    private ReceiveMessageListener receiveMessageListener;
    private ErrorMessageListener errorMessageListener;
    private UpdateParticipantsListListener updateParticipantsListListener;
    private CloseViewListener closeViewListener;

    @Override
    public void onConnectServer(String serverAddress, String port, String userName) {

        closeServerSocket();

        new Thread(() -> {
            Integer serverPort = getServerPort(port);
            if (serverPort == null) {
                return;
            }
            try {
                serverSocket = new Socket(serverAddress, serverPort);
                LOGGER.debug("Connected to {}:{}", serverAddress, serverPort);
            } catch (IOException e) {
                LOGGER.error("Failed to connect to server.", e);
                errorMessageListener.onErrorMessage("Failed to connect to server. " +
                        "Please check entered connection parameter.");
                return;
            }
            try {
                out = new PrintWriter(serverSocket.getOutputStream(), true, StandardCharsets.UTF_8);
                in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                LOGGER.error("Failed to get server socket stream.", e);
                errorMessageListener.onErrorMessage("Something went wrong with connection to server");
                return;
            }
            messagesManager.sendLoginMessage(out, userName);
            String serverAnswer;
            try {
                serverAnswer = in.readLine();
            } catch (IOException e) {
                LOGGER.error("Failed to read server answer.", e);
                errorMessageListener.onErrorMessage("Something went wrong with reading server answer.");
                return;
            }
            Message response = messagesManager.readMessage(serverAnswer);
            if (response instanceof ErrorMessage errorMessage) {
                LOGGER.error("Error join chat: {}", errorMessage);
                if (errorMessageListener != null) {
                    errorMessageListener.onErrorMessage(errorMessage.getMessage());
                }
                closeServerSocket();
                return;
            }
            LOGGER.info("Chat successfully was joined.");
            currentClientName = userName;
            listenToServer(in, serverSocket);
        }).start();
    }

    @Override
    public void onExit() {
        closeServerSocket();
        closeViewListener.onCloseView();
    }

    @Override
    public synchronized void onSendMessage(String message) {
        messagesManager.sendTextMessage(out, message, currentClientName);
    }

    public void setCloseViewListener(CloseViewListener closeViewListener) {
        this.closeViewListener = closeViewListener;
    }

    public void setErrorMessageListener(ErrorMessageListener errorMessageListener) {
        this.errorMessageListener = errorMessageListener;
    }

    public void setUpdateParticipantsListListener(UpdateParticipantsListListener updateParticipantsListListener) {
        this.updateParticipantsListListener = updateParticipantsListListener;
    }

    public void setReceiveMessageListener(ReceiveMessageListener receiveMessageListener) {
        this.receiveMessageListener = receiveMessageListener;
    }

    private void closeServerSocket() {
        if (serverSocket != null) {
            try {
                serverSocket.getInputStream().close();
                serverSocket.getOutputStream().close();
            } catch (IOException e) {
                LOGGER.error("Error while closing server input stream.", e);
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOGGER.error("Error while closing server socket.", e);
            }
        }
    }

    private void listenToServer(BufferedReader in, Socket serverSocket) {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                receivedMessageLoggerDebug(line);
                if (handleMessage(line)) {
                    return;
                }
            }
        } catch (IOException e) {
            if (!serverSocket.isClosed()) {
                LOGGER.error("Error reading from server socket", e);
            } else {
                if (receiveMessageListener != null) {
                    receiveMessageListener.onReceiveMessage("Чат завершен.", MessageType.END_CHAT);
                }
                serverSocketClosedLoggerDebug(serverSocket);
            }
        }
    }

    private synchronized boolean handleMessage(String line) {
        Message message = messagesManager.readMessage(line);
        if (message instanceof TextMessage textMessage) {
            handleTextMessage(textMessage);
        } else if (message instanceof ErrorMessage errorMessage) {
            if (errorMessageListener != null) {
                errorMessageListener.onErrorMessage(errorMessage.getMessage());
            }
        } else if (message instanceof PingMessage) {
            messagesManager.sendPingMessage(out);
        } else if (message instanceof ParticipantsListMessage participantsListMessage) {
            if (updateParticipantsListListener != null) {
                updateParticipantsListListener.onUpdateParticipantsList(participantsListMessage.getParticipants());
            }
        } else if (message instanceof ParticipantStatusMessage participantStatusMessage) {
            handleParticipantStatusMessage(participantStatusMessage);
        } else if (message instanceof EndChatMessage) {
            if (receiveMessageListener != null) {
                receiveMessageListener.onReceiveMessage("Чат завершен.", MessageType.END_CHAT);
            }
            return true;
        }
        return false;
    }

    private void receivedMessageLoggerDebug(String msg) {
        LOGGER.debug("Client from server received: {}", msg);
    }

    private void serverSocketClosedLoggerDebug(Socket serverSocket) {
        LOGGER.debug("Server socket with Port: {} Inet address: {} was closed",
                serverSocket.getPort(), serverSocket.getInetAddress().getHostAddress());
    }

    private void handleParticipantStatusMessage(ParticipantStatusMessage participantStatusMessage) {
        if (receiveMessageListener != null) {
            switch (participantStatusMessage.getStatus()) {
                case JOIN -> {
                    String msg = String.format("Пользователь '%s' присоединился.",
                            participantStatusMessage.getParticipantName());
                    receiveMessageListener.onReceiveMessage(msg, MessageType.JOIN_PARTICIPANT);
                }
                case LEFT -> {
                    String msg = String.format("Пользователь '%s' покинул чат.",
                            participantStatusMessage.getParticipantName());
                    receiveMessageListener.onReceiveMessage(msg, MessageType.LEFT_PARTICIPANT);
                }
            }
        }
    }

    private void handleTextMessage(TextMessage textMessage) {
        if (receiveMessageListener != null) {
            String messageToView = messagesManager.mapTextMessageToString(textMessage);
            if (textMessage.getSender().equals(currentClientName)) {
                receiveMessageListener.onReceiveMessage(messageToView, MessageType.CURRENT_PARTICIPANT_MESSAGE);
            } else {
                receiveMessageListener.onReceiveMessage(messageToView, MessageType.ANOTHER_PARTICIPANT_MESSAGE);
            }
        }
    }

    private Integer getServerPort(String port) {
        int serverPort;
        try {
            serverPort = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            if (errorMessageListener != null) {
                errorMessageListener.onErrorMessage("Invalid port number.");
            }
            return null;
        }
        return serverPort;
    }
}
