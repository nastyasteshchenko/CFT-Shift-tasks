package focus.start.task6.client.view;

import java.awt.*;

public enum MessageType {

    JOIN_PARTICIPANT(Color.ORANGE.darker()),
    LEFT_PARTICIPANT(Color.RED.darker()),
    ANOTHER_PARTICIPANT_MESSAGE(Color.BLACK),
    END_CHAT(Color.RED.darker().darker().darker()),
    CURRENT_PARTICIPANT_MESSAGE(Color.CYAN.darker());

    private final Color color;

    MessageType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
