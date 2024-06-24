package focus.start.task6.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TextMessage extends Message {

    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    @JsonProperty("sender")
    private String sender;
    @JsonProperty("text")
    private String content;

    public TextMessage() {
        setType(Message.TEXT_MESSAGE);
    }

    public TextMessage(LocalDateTime dateTime, String sender, String content) {
        setType(Message.TEXT_MESSAGE);
        this.sender = sender;
        this.content = content;
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
