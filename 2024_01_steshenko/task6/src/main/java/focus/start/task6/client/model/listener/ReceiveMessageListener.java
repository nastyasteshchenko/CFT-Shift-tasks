package focus.start.task6.client.model.listener;

import focus.start.task6.client.view.MessageType;

public interface ReceiveMessageListener {
    void onReceiveMessage(String message, MessageType messageType);
}
