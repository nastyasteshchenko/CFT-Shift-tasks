package focus.start.task3.controller;

import javax.swing.*;

final class MessageDialogUtility {

    private MessageDialogUtility() {
    }

    static void showWritingHighScoresError() {
        JOptionPane.showMessageDialog(null, "Something went wrong with high scores.");
    }
}
