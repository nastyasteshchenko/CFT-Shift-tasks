package focus.start.task6.client.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import focus.start.task6.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

class MessagesManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesManager.class);
    private static final String UTC_0 = "UTC-0";

    private final ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    Message readMessage(String message) {
        try {
            return objectMapper.readValue(message, Message.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error reading message from json.", e);
            return null;
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

    void sendLoginMessage(PrintWriter out, String userName) {
        try {
            Message loginMessage = new LoginMessage(userName);
            String json = objectMapper.writeValueAsString(loginMessage);
            out.println(json);
            LOGGER.debug("Login message sent: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json login message.", e);
        }
    }

    void sendTextMessage(PrintWriter out, String message, String senderName) {
        try {
            LocalDateTime dateTimeNow = LocalDateTime.now(ZoneId.of(UTC_0));
            Message textMessage = new TextMessage(dateTimeNow, senderName, message);
            String json = objectMapper.writeValueAsString(textMessage);
            out.println(json);
            LOGGER.debug("Text message sent: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while write to json text message.", e);
        }
    }

    String mapTextMessageToString(TextMessage textMessage) {
        ZonedDateTime dateTime = textMessage.getDateTime().atZone(ZoneId.of(UTC_0))
                .withZoneSameInstant(ZoneId.systemDefault());
        String localDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(dateTime);
        String userName = textMessage.getSender();
        String message = textMessage.getContent();
        return "[" + localDateTime + "] " + userName + ": " + message;
    }
}
