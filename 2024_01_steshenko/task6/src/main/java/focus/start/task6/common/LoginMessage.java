package focus.start.task6.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginMessage extends Message {
    @JsonProperty("username")
    private String username;

    public LoginMessage() {
        setType(Message.LOGIN);
    }

    public LoginMessage(String username) {
        setType(Message.LOGIN);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
