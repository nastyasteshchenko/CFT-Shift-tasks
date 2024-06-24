package focus.start.task6.client.view;

import javax.swing.*;

public class ErrorMessageWindow {

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
