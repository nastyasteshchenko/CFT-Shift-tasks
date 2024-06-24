package focus.start.task6.client.view;

import focus.start.task6.client.view.listener.ConnectServerParamsListener;
import focus.start.task6.client.view.listener.ExitButtonListener;

import javax.swing.*;

class ConnectingParamsPanel extends JPanel {

    private final JTextField serverAddressField;
    private final JTextField usernameField;
    private final JTextField portField;
    private final JButton connectButton;
    private final JButton exitButton;

    private String lastServerAddress = "";
    private String lastPort = "";
    private String lastUsername = "";

    ConnectingParamsPanel() {

        exitButton = new JButton("Exit");
        JLabel serverAddressLabel = new JLabel("Server Address:");
        serverAddressField = new JTextField(10);
        JLabel portLabel = new JLabel("Port:");
        portField = new JTextField(10);
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        connectButton = new JButton("Connect");

        add(exitButton);
        add(serverAddressLabel);
        add(serverAddressField);
        add(portLabel);
        add(portField);
        add(usernameLabel);
        add(usernameField);
        add(connectButton);
    }

    void setOnExitButtonListener(ExitButtonListener exitButtonListener) {
        exitButton.addActionListener(e -> exitButtonListener.onExitButtonClicked());
    }

    void setOnConnectButtonListener(ConnectServerParamsListener connectButtonListener) {
        connectButton.addActionListener(e -> {
            String serverAddress = serverAddressField.getText();
            String port = portField.getText();
            String username = usernameField.getText();
            if (!username.isEmpty() && !(lastServerAddress.equals(serverAddress) && lastPort.equals(port)
                    && lastUsername.equals(username))) {
                lastServerAddress = serverAddress;
                lastPort = port;
                lastUsername = username;
                connectButtonListener.onConnectServer(serverAddress, port, username);
            }
        });
    }
}
