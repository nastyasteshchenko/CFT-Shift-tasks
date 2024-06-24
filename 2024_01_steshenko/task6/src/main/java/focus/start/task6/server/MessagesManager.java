package focus.start.task6.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import focus.start.task6.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

class MessagesManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesManager.class);

    private final ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    Message readMessage(String message) {
        try {
            return objectMapper.readValue(message, Message.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error reading message from json.", e);
            return null;
        }
    }

    void sendTextMessage(PrintWriter out, Message textMessage) {
        try {
            String json = objectMapper.writeValueAsString(textMessage);
            out.println(json);
            LOGGER.debug("Text message sent: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json text message.", e);
        }
    }

    void sendErrorMessage(String message, PrintWriter out) {
        try {
            Message errorMessage = new ErrorMessage(message);
            String json = objectMapper.writeValueAsString(errorMessage);
            out.println(json);
            LOGGER.debug("Error message sent: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json error message.", e);
        }
    }

    void sendEndChatMessage(PrintWriter out) {
        try {
            Message endChatMessage = new EndChatMessage();
            String json = objectMapper.writeValueAsString(endChatMessage);
            out.println(json);
            LOGGER.debug("End chat message sent: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json end chat message.", e);
        }
    }

    void sendSuccessMessage(PrintWriter out) {
        try {
            Message successMessage = new SuccessMessage();
            String json = objectMapper.writeValueAsString(successMessage);
            out.println(json);
            LOGGER.debug("Success message sent: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json success message.", e);
        }
    }

    void sendPingMessage(PrintWriter out) {
        try {
            Message pingMessage = new PingMessage();
            String json = objectMapper.writeValueAsString(pingMessage);
            out.println(json);
            LOGGER.debug("Ping message sent: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json ping message.", e);
        }
    }

    void sendParticipantsListMessage(Collection<ClientInfo> clients) {
        try {
            List<String> participantsNames = clients.stream().map(ClientInfo::getUserName).toList();
            Message participantsListMessage = new ParticipantsListMessage(participantsNames);
            String json = objectMapper.writeValueAsString(participantsListMessage);
            for (ClientInfo clientInfo : clients) {
                clientInfo.getOut().println(json);
            }
            LOGGER.debug("Participants list message sent to all clients: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json participants list message.", e);
        }
    }

    void sendParticipantJoinMessage(String newParticipantName, Collection<ClientInfo> clients) {
        try {
            Message participantStatusMessage = new ParticipantStatusMessage(ParticipantStatus.JOIN, newParticipantName);
            String json = objectMapper.writeValueAsString(participantStatusMessage);
            for (ClientInfo clientInfo : clients) {
                clientInfo.getOut().println(json);
            }
            LOGGER.debug("Participants join message sent to all clients: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json participants join message.", e);
        }
    }

    void sendParticipantLeftMessage(String newParticipantName, Collection<ClientInfo> clients) {
        try {
            Message participantStatusMessage = new ParticipantStatusMessage(ParticipantStatus.LEFT, newParticipantName);
            String json = objectMapper.writeValueAsString(participantStatusMessage);
            for (ClientInfo clientInfo : clients) {
                clientInfo.getOut().println(json);
            }
            LOGGER.debug("Participants left message sent to all clients: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json participants left message.", e);
        }
    }
}
