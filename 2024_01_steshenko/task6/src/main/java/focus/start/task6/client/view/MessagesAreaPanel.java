package focus.start.task6.client.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

class MessagesAreaPanel extends JScrollPane {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesAreaPanel.class);

    private final JTextPane messagesPane;

    MessagesAreaPanel() {
        messagesPane = new JTextPane();
        messagesPane.setEditable(false);
        this.setViewportView(messagesPane);
    }

    void addNewMessage(String message, Color color) {
        appendTextWithColor(String.format("%s%n", message), color);
    }

    private void appendTextWithColor(String text, Color color) {
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, color);

        try {
            messagesPane.getDocument().insertString(messagesPane.getDocument().getLength(), text, attr);
        } catch (BadLocationException e) {
            LOGGER.error("Error while adding text to textPane.", e);
        }
    }
}
