package focus.start.task6.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ParticipantsListMessage extends Message {

    @JsonProperty("participants")
    private List<String> participants;

    public ParticipantsListMessage() {
        setType(Message.PARTICIPANTS_LIST);
    }

    public ParticipantsListMessage(List<String> participants) {
        setType(Message.PARTICIPANTS_LIST);
        this.participants = participants;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
