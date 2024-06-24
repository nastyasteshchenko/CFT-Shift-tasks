package focus.start.task6.client.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class ParticipantsListPanel extends JScrollPane {
    private final DefaultListModel<String> participantsListModel;

    ParticipantsListPanel() {
        participantsListModel = new DefaultListModel<>();
        JList<String> participantsList = new JList<>(participantsListModel);
        setViewportView(participantsList);
        setPreferredSize(new Dimension(150, 0));
    }

    void updateParticipantsList(List<String> userNames) {
        participantsListModel.clear();
        for (String userName : userNames) {
            participantsListModel.addElement(userName);
        }
    }
}
