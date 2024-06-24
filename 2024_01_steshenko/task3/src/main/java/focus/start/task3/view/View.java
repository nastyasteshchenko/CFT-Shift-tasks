package focus.start.task3.view;

public class View {

    private final MainWindow mainWindow = new MainWindow();
    private final HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
    private final SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
    private final LoseWindow loseWindow = new LoseWindow(mainWindow);
    private final WinWindow winWindow = new WinWindow(mainWindow);
    private final HighScoreHolderNamePane highScoreHolderNamePane = new HighScoreHolderNamePane();

    public View(){
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public HighScoresWindow getHighScoresWindow() {
        return highScoresWindow;
    }

    public LoseWindow getLoseWindow() {
        return loseWindow;
    }

    public HighScoreHolderNamePane getHighScoreHolderNameWindow() {
        return highScoreHolderNamePane;
    }

    public SettingsWindow getSettingsWindow() {
        return settingsWindow;
    }

    public WinWindow getWinWindow() {
        return winWindow;
    }
}
