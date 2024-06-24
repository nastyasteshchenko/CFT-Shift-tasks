package focus.start.task6.common;

public class ParticipantStatusMessage extends Message {

    private ParticipantStatus status;
    private String participantName;

    public ParticipantStatusMessage() {
        setType(Message.PARTICIPANT_STATUS);
    }

    public ParticipantStatusMessage(ParticipantStatus status, String participantName) {
        setType(Message.PARTICIPANT_STATUS);
        this.participantName = participantName;
        this.status = status;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
}
