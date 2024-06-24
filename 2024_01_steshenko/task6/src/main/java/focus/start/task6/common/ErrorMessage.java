package focus.start.task6.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage extends Message {

    @JsonProperty("message")
    private String message;

    public ErrorMessage() {
        setType(Message.ERROR);
    }

    public ErrorMessage(String message) {
        setType(Message.ERROR);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        setType(Message.ERROR);
        this.message = message;
    }
}
