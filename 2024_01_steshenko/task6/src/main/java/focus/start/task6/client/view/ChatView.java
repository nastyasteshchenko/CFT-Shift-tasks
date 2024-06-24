package focus.start.task6.client.view;

import focus.start.task6.client.model.listener.ErrorMessageListener;
import focus.start.task6.client.model.listener.CloseViewListener;
import focus.start.task6.client.model.listener.ReceiveMessageListener;
import focus.start.task6.client.model.listener.UpdateParticipantsListListener;
import focus.start.task6.client.view.listener.ConnectServerParamsListener;
import focus.start.task6.client.view.listener.ExitButtonListener;
import focus.start.task6.client.view.listener.SendMessageButtonListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChatView extends JFrame implements ReceiveMessageListener, ErrorMessageListener,
        UpdateParticipantsListListener, CloseViewListener {

    private final ConnectingParamsPanel connectingParamsPanel;
    private final EnteringMessagePanel enteringMessagePanel;
    private final MessagesAreaPanel messagesAreaPanel;
    private final ParticipantsListPanel participantsListPanel;
    private final ErrorMessageWindow errorMessageWindow;

    public ChatView() {
        setTitle("Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setMinimumSize(new Dimension(800, 500));
        setLayout(new BorderLayout());

        connectingParamsPanel = new ConnectingParamsPanel();
        enteringMessagePanel = new EnteringMessagePanel();
        participantsListPanel = new ParticipantsListPanel();
        messagesAreaPanel = new MessagesAreaPanel();
        errorMessageWindow = new ErrorMessageWindow();

        add(messagesAreaPanel, BorderLayout.CENTER);
        add(participantsListPanel, BorderLayout.EAST);
        add(connectingParamsPanel, BorderLayout.NORTH);
        add(enteringMessagePanel, BorderLayout.SOUTH);
    }

    @Override
    public void onErrorMessage(String message) {
        errorMessageWindow.showErrorMessage(message);
    }

    @Override
    public void onUpdateParticipantsList(List<String> participants) {
        participantsListPanel.updateParticipantsList(participants);
    }

    @Override
    public void onCloseView() {
        this.dispose();
    }

    @Override
    public void onReceiveMessage(String message, MessageType messageType) {
        messagesAreaPanel.addNewMessage(message, messageType.getColor());
    }

    public void setSendMessageButtonListener(SendMessageButtonListener sendMessageButtonListener) {
        enteringMessagePanel.setSendButtonListener(sendMessageButtonListener);
    }

    public void setConnectServerButtonListener(ConnectServerParamsListener connectServerParamsListener) {
        connectingParamsPanel.setOnConnectButtonListener(connectServerParamsListener);
    }

    public void setExitButtonListener(ExitButtonListener exitButtonListener) {
        connectingParamsPanel.setOnExitButtonListener(exitButtonListener);
    }
}