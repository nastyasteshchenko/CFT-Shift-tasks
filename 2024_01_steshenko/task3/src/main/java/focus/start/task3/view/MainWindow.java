package focus.start.task3.view;

import focus.start.task3.controller.listener.DrawGameFieldListener;
import focus.start.task3.controller.listener.ChangeTimerValueListener;
import focus.start.task3.model.GameDifficultyType;
import focus.start.task3.model.listener.*;
import focus.start.task3.view.listener.MarkCellListener;
import focus.start.task3.view.listener.OpenCellListener;
import focus.start.task3.view.listener.OpenCellsAroundOpenedOneListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.BreakIterator;
import java.util.Locale;

public class MainWindow extends JFrame implements DrawOpenedBombListener, ChangeBombsAmountListener,
        DrawMarkedCellListener, DrawUnmarkedCellListener, DrawOpenedCellListener, DrawGameFieldListener,
        ChangeTimerValueListener {

    private static final int ABOUT_GAME_LINE_LENGTH = 100;

    private static final String ABOUT_GAME_DESCRIPTION =
            "Minesweeper is a logic puzzle video game genre generally played on personal computers." +
                    "The game features a grid of clickable tiles, with hidden “mines” scattered throughout the board." +
                    "The objective is to clear the board without detonating any mines, " +
                    "with help from clues about the number of neighboring mines in each field.";

    private static final String ABOUT_GAME_RULES =
            "Minesweeper rules are very simple. The board is divided into cells, with mines randomly distributed." +
                    "To win, you need to open all the cells. " +
                    "The number on a cell shows the number of mines adjacent to it." +
                    "Using this information, you can determine cells that are safe, and cells that contain mines." +
                    "Cells suspected of being mines can be marked with a flag using the right mouse button.";

    private final Container contentPane;
    private final GridBagLayout mainLayout;

    private JMenuItem newGameMenu;
    private JMenuItem highScoresMenu;
    private JMenuItem settingsMenu;
    private JMenuItem aboutMenu;
    private JMenuItem exitMenu;

    private MarkCellListener markCellListener;
    private OpenCellListener openCellListener;
    private OpenCellsAroundOpenedOneListener openCellsAroundOpenedOneListener;

    private JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel bombsCounterLabel;

    private GameType gameType = GameType.NOVICE;

    public MainWindow() {
        super("Minesweeper");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        createMenu();

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);

        addBombCounter(bombsCounterLabel = new JLabel("0"));

        contentPane.setBackground(new Color(144, 158, 184));
    }

    public void setNewGameMenuAction(ActionListener listener) {
        newGameMenu.addActionListener(listener);
    }

    public void setHighScoresMenuAction(ActionListener listener) {
        highScoresMenu.addActionListener(listener);
    }

    public void setSettingsMenuAction(ActionListener listener) {
        settingsMenu.addActionListener(listener);
    }

    public void setAboutMenuAction(ActionListener listener) {
        aboutMenu.addActionListener(listener);
    }


    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void onDrawOpenedBomb(int x, int y) {
        cellButtons[y][x].setIcon(GameImage.BOMB.getImageIcon());
    }

    @Override
    public void onBombsAmountChanged(int newAmount) {
        bombsCounterLabel.setText(String.valueOf(newAmount));
    }

    @Override
    public void onDrawMarkedCell(int x, int y) {
        cellButtons[y][x].setIcon(GameImage.MARKED.getImageIcon());
    }

    @Override
    public void onDrawUnmarkedCell(int x, int y) {
        cellButtons[y][x].setIcon(GameImage.CLOSED.getImageIcon());
    }

    @Override
    public void onChangeTimerValue(int value) {
        timerLabel.setText(String.valueOf(value));
    }

    @Override
    public void onDrawOpenedCell(int x, int y, int nearBombsAmount) {
        GameImage gameImage = GameImage.getImageByMinesAmount(nearBombsAmount);
        cellButtons[y][x].setIcon(gameImage.getImageIcon());
    }

    @Override
    public void onDrawGameField() {
        int rowsCount = 0;
        int colsCount = 0;
        switch (gameType) {
            case NOVICE -> {
                rowsCount = GameDifficultyType.NOVICE.getFieldSize().y();
                colsCount = GameDifficultyType.NOVICE.getFieldSize().x();
            }
            case MEDIUM -> {
                rowsCount = GameDifficultyType.MEDIUM.getFieldSize().y();
                colsCount = GameDifficultyType.MEDIUM.getFieldSize().x();
            }
            case EXPERT -> {
                rowsCount = GameDifficultyType.EXPERT.getFieldSize().y();
                colsCount = GameDifficultyType.EXPERT.getFieldSize().x();
            }
        }

        contentPane.removeAll();
        setPreferredSize(new Dimension(20 * colsCount + 70, 20 * rowsCount + 110));

        addButtonsPanel(createButtonsPanel(rowsCount, colsCount));
        addTimerImage();
        addTimerLabel(timerLabel = new JLabel("0"));
        addBombCounter(bombsCounterLabel = new JLabel("0"));
        addBombCounterImage();
        pack();
        setLocationRelativeTo(null);
    }

    public void setOpenCellListener(OpenCellListener openCellListener) {
        this.openCellListener = openCellListener;
    }

    public void setOpenCellsAroundOpenedOneListener(OpenCellsAroundOpenedOneListener openCellsAroundOpenedOneListener) {
        this.openCellsAroundOpenedOneListener = openCellsAroundOpenedOneListener;
    }

    public void setMarkCellListener(MarkCellListener markCellListener) {
        this.markCellListener = markCellListener;
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        gameMenu.add(newGameMenu = new JMenuItem("New Game"));
        gameMenu.addSeparator();
        gameMenu.add(highScoresMenu = new JMenuItem("High Scores"));
        gameMenu.addSeparator();
        gameMenu.add(settingsMenu = new JMenuItem("Settings"));
        gameMenu.addSeparator();
        gameMenu.add(aboutMenu = new JMenuItem("About"));
        setAboutMenuAction(e -> JOptionPane.showMessageDialog(null,
                formatLines(ABOUT_GAME_DESCRIPTION) + System.lineSeparator().repeat(2)
                        + formatLines(ABOUT_GAME_RULES), "About game", JOptionPane.PLAIN_MESSAGE));
        gameMenu.addSeparator();
        gameMenu.add(exitMenu = new JMenuItem("Exit"));
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        setExitMenuAction(e -> dispose());
    }

    private JPanel createButtonsPanel(int numberOfRows, int numberOfCols) {
        cellButtons = new JButton[numberOfRows][numberOfCols];
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(20 * numberOfCols, 20 * numberOfRows));
        buttonsPanel.setLayout(new GridLayout(numberOfRows, numberOfCols, 0, 0));

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                final int x = col;
                final int y = row;

                cellButtons[y][x] = new JButton(GameImage.CLOSED.getImageIcon());
                cellButtons[y][x].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (markCellListener == null) {
                            return;
                        }

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            openCellListener.onOpenCell(x, y);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            markCellListener.onMarkCell(x, y);
                        } else if (SwingUtilities.isMiddleMouseButton(e)) {
                            openCellsAroundOpenedOneListener.onOpenCellsAroundOpenedOne(x, y);
                        }
                    }
                });
                buttonsPanel.add(cellButtons[y][x]);
            }
        }

        return buttonsPanel;
    }

    private void addButtonsPanel(JPanel buttonsPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 20, 5, 20);
        mainLayout.setConstraints(buttonsPanel, gbc);
        contentPane.add(buttonsPanel);
    }

    private void addTimerImage() {
        JLabel label = new JLabel(GameImage.TIMER.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private void addTimerLabel(JLabel timerLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        mainLayout.setConstraints(timerLabel, gbc);
        contentPane.add(timerLabel);
    }

    private void addBombCounter(JLabel bombsCounterLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.7;
        mainLayout.setConstraints(bombsCounterLabel, gbc);
        contentPane.add(bombsCounterLabel);
    }

    private void addBombCounterImage() {
        JLabel label = new JLabel(GameImage.BOMB_ICON.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 5, 0, 20);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private String formatLines(String target) {
        StringBuilder result = new StringBuilder();
        BreakIterator boundary = BreakIterator.
                getLineInstance(Locale.US);
        boundary.setText(target);
        int start = boundary.first();
        int end = boundary.next();
        int lineLength = 0;

        while (end != BreakIterator.DONE) {
            String word = target.substring(start, end);
            lineLength = lineLength + word.length();
            if (lineLength >= ABOUT_GAME_LINE_LENGTH) {
                result.append(System.lineSeparator());
                lineLength = word.length();
            }
            result.append(word);
            start = end;
            end = boundary.next();
        }
        return result.toString();
    }

    private void setExitMenuAction(ActionListener listener) {
        exitMenu.addActionListener(listener);
    }
}
