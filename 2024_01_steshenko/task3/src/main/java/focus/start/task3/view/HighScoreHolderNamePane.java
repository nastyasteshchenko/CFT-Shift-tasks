package focus.start.task3.view;

import focus.start.task3.controller.listener.GetNewHighScoreHolderNameListener;

import javax.swing.*;

public class HighScoreHolderNamePane implements GetNewHighScoreHolderNameListener {

    @Override
    public String onGetNewHighScoreHolderName() {
        String input = "";
        while (input.isEmpty()) {
            input = JOptionPane.showInputDialog(null, "Enter your name:", "New high score",
                    JOptionPane.PLAIN_MESSAGE);

            if (input == null) {
                return input;
            } else if (input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Input cannot be empty. Please try again.");
            }
        }
        return input;
    }
}
