package focus.start.task6.client.view;

import focus.start.task6.client.view.listener.SendMessageButtonListener;

import javax.swing.*;

class EnteringMessagePanel extends JPanel {

    private final JTextField messageInput;
    private final JButton sendButton;

    EnteringMessagePanel() {
        messageInput = new JTextField();
        sendButton = new JButton("Send");
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(messageInput);
        add(sendButton);
    }

    void setSendButtonListener(SendMessageButtonListener sendButtonListener) {
        sendButton.addActionListener(e -> {
            String message = messageInput.getText();
            messageInput.setText("");
            if (message != null && !message.isEmpty()) {
                sendButtonListener.onSendMessage(message);
            }
        });
    }
}
