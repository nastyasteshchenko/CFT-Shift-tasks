package focus.start.task6.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = Message.LOGIN, value = LoginMessage.class),
        @JsonSubTypes.Type(name = Message.ERROR, value = ErrorMessage.class),
        @JsonSubTypes.Type(name = Message.SUCCESS, value = SuccessMessage.class),
        @JsonSubTypes.Type(name = Message.TEXT_MESSAGE, value = TextMessage.class),
        @JsonSubTypes.Type(name = Message.PING, value = PingMessage.class),
        @JsonSubTypes.Type(name = Message.PARTICIPANTS_LIST, value = ParticipantsListMessage.class),
        @JsonSubTypes.Type(name = Message.PARTICIPANT_STATUS, value = ParticipantStatusMessage.class),
        @JsonSubTypes.Type(name = Message.PARTICIPANTS_LIST, value = ParticipantsListMessage.class),
        @JsonSubTypes.Type(name = Message.END_CHAT, value = EndChatMessage.class),
})
public abstract class Message {
    protected static final String LOGIN = "login";
    protected static final String ERROR = "error";
    protected static final String SUCCESS = "success";
    protected static final String TEXT_MESSAGE = "text_message";
    protected static final String PING = "ping";
    protected static final String PARTICIPANTS_LIST = "participants_list";
    protected static final String PARTICIPANT_STATUS = "participant_status";
    protected static final String END_CHAT = "end_chat";

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
