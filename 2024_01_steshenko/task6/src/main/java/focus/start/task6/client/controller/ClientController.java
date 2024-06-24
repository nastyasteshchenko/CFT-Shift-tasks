package focus.start.task6.client.controller;

import focus.start.task6.client.controller.listener.ConnectServerListener;
import focus.start.task6.client.controller.listener.ExitListener;
import focus.start.task6.client.controller.listener.SendMessageListener;
import focus.start.task6.client.view.listener.ConnectServerParamsListener;
import focus.start.task6.client.view.listener.ExitButtonListener;
import focus.start.task6.client.view.listener.SendMessageButtonListener;

public class ClientController implements SendMessageButtonListener, ConnectServerParamsListener, ExitButtonListener {

    private ConnectServerListener connectServerListener;
    private SendMessageListener sendMessageListener;
    private ExitListener exitListener;

    @Override
    public void onSendMessage(String message) {
        if (sendMessageListener != null) {
            sendMessageListener.onSendMessage(message);
        }
    }

    @Override
    public void onConnectServer(String serverAddress, String port, String userName) {
        if (connectServerListener != null) {
            connectServerListener.onConnectServer(serverAddress, port, userName);
        }
    }

    @Override
    public void onExitButtonClicked() {
        if (exitListener != null) {
            exitListener.onExit();
        }
    }

    public void setSendMessageListener(SendMessageListener sendMessageListener) {
        this.sendMessageListener = sendMessageListener;
    }

    public void setConnectServerListener(ConnectServerListener connectServerListener) {
        this.connectServerListener = connectServerListener;
    }

    public void setExitListener(ExitListener exitListener) {
        this.exitListener = exitListener;
    }
}
