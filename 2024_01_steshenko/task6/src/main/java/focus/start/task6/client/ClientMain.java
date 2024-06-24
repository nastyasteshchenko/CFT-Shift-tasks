package focus.start.task6.client;

import focus.start.task6.client.controller.ClientController;
import focus.start.task6.client.model.ClientModel;
import focus.start.task6.client.view.ChatView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) {

        LOGGER.info("Starting client.");
        ClientController clientController = new ClientController();
        ClientModel clientModel = new ClientModel();
        ChatView chatView = new ChatView();

        clientModel.setReceiveMessageListener(chatView);
        clientModel.setErrorMessageListener(chatView);
        clientModel.setUpdateParticipantsListListener(chatView);
        clientModel.setCloseViewListener(chatView);
        LOGGER.info("Client model was configured.");

        chatView.setConnectServerButtonListener(clientController);
        chatView.setSendMessageButtonListener(clientController);
        chatView.setExitButtonListener(clientController);
        LOGGER.info("Chat view was configured.");

        clientController.setConnectServerListener(clientModel);
        clientController.setSendMessageListener(clientModel);
        clientController.setExitListener(clientModel);
        LOGGER.info("Client controller was configured.");

        LOGGER.info("Chat view was set visible.");
        chatView.setVisible(true);
    }

}