package focus.start.task3.controller;

import focus.start.task3.controller.listener.*;
import focus.start.task3.model.GameDifficultyType;
import focus.start.task3.model.listener.LoseEventListener;
import focus.start.task3.model.listener.StartTimerListener;
import focus.start.task3.model.listener.WinEventListener;
import focus.start.task3.view.*;
import focus.start.task3.view.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Controller implements GameTypeListener, MarkCellListener, OpenCellListener, StartTimerListener,
        OpenCellsAroundOpenedOneListener, WinEventListener, LoseEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private static final int MILLIS_IN_SECOND = 1000;

    private StartNewGameListener startNewGameListener;
    private MarkCellListener markCellListener;
    private OpenCellListener openCellListener;
    private OpenCellsAroundOpenedOneListener openCellsAroundOpenedOneListener;
    private SwitchGameDifficultyListener switchGameDifficultyListener;
    private DrawGameFieldListener drawGameFieldListener;
    private ChangeTimerValueListener changeTimerValueListener;
    private ShowLoseWindowListener showLoseWindowListener;
    private ShowWinWindowListener showWinWindowListener;
    private GetNewHighScoreHolderNameListener getNewHighScoreHolderNameListener;

    private final Timer timer;
    private long startTime;
    private int secsLeft;
    private HighScoresManager highScoresManager;

    public Controller() {
        ActionListener timerTask = e -> {
            secsLeft = (int) (System.currentTimeMillis() - startTime) / MILLIS_IN_SECOND;
            if (changeTimerValueListener != null) {
                changeTimerValueListener.onChangeTimerValue(secsLeft);
            }
        };
        timer = new Timer(1000, timerTask);
        LOGGER.info("Timer was created.");
    }

    public void start() {
        highScoresManager = null;
        try {
            highScoresManager = HighScoresManager.create();
        } catch (IOException e) {
            LOGGER.error("Error while creating high scores manager.{}{}", System.lineSeparator(), e.getMessage());
            MessageDialogUtility.showWritingHighScoresError();
        }
        if (highScoresManager != null) {
            LOGGER.info("High scores manager was successfully created.");
        }
        startNewGame();
    }

    public void startNewGame() {
        timer.stop();
        if (drawGameFieldListener != null) {
            drawGameFieldListener.onDrawGameField();
        }
        LOGGER.info("Game field was drawn.");
        if (startNewGameListener != null) {
            startNewGameListener.onStartNewGame();
        }
        LOGGER.info("New game started.");
    }

    public void onHighScores(HighScoresWindow highScoresWindow) {
        if (highScoresManager != null) {
            GameDifficultyType[] difficulties = GameDifficultyType.values();
            for (GameDifficultyType difficultyType : difficulties) {
                Score score = highScoresManager.getScoreForDifficulty(difficultyType.name());
                if (score != null) {
                    switch (difficultyType) {
                        case NOVICE -> highScoresWindow.setNoviceHighScore(score.playerName(), score.score());
                        case MEDIUM -> highScoresWindow.setMediumHighScore(score.playerName(), score.score());
                        case EXPERT -> highScoresWindow.setExpertHighScore(score.playerName(), score.score());
                    }
                    LOGGER.debug("High score:{}difficulty: {}{}player name: {}{}score: {}{}was set to high score window.",
                            System.lineSeparator(),
                            score.difficulty(), System.lineSeparator(),
                            score.playerName(), System.lineSeparator(),
                            score.score(), System.lineSeparator());
                }
            }
            highScoresWindow.setVisible(true);
        }
    }

    @Override
    public void onGameTypeChanged(GameType gameType) {
        GameDifficultyType gameDifficultyType = switch (gameType) {
            case NOVICE -> GameDifficultyType.NOVICE;
            case MEDIUM -> GameDifficultyType.MEDIUM;
            case EXPERT -> GameDifficultyType.EXPERT;
        };
        if (switchGameDifficultyListener != null) {
            switchGameDifficultyListener.onSwitchGameDifficulty(gameDifficultyType);
        }
        startNewGame();
    }

    @Override
    public void onStartTimer() {
        startTime = System.currentTimeMillis();
        timer.start();
    }

    @Override
    public void onOpenCell(int x, int y) {
        if (openCellListener != null) {
            openCellListener.onOpenCell(x, y);
        }
    }

    @Override
    public void onMarkCell(int x, int y) {
        if (markCellListener != null) {
            markCellListener.onMarkCell(x, y);
        }
    }

    @Override
    public void onOpenCellsAroundOpenedOne(int x, int y) {
        if (openCellsAroundOpenedOneListener != null) {
            openCellsAroundOpenedOneListener.onOpenCellsAroundOpenedOne(x, y);
        }
    }

    @Override
    public void onLoseGame() {
        timer.stop();
        if (showWinWindowListener != null) {
            showLoseWindowListener.onShowLoseWindow();
        }
    }

    @Override
    public void onGameWin(GameDifficultyType gameDifficultyType) {
        timer.stop();
        if (highScoresManager != null && highScoresManager.isNewScore(gameDifficultyType.name(), secsLeft)) {
            LOGGER.debug("New high score with time = {} was detected.", secsLeft);
            if (getNewHighScoreHolderNameListener != null) {
                String playerName = getNewHighScoreHolderNameListener.onGetNewHighScoreHolderName();
                if (playerName != null) {
                    LOGGER.debug("New high score holder name was get: '{}'", playerName);
                    Score singleScore = new Score(gameDifficultyType.name(), playerName, secsLeft);
                    highScoresManager.writeHighScore(singleScore);
                }
            }
        }
        if (showWinWindowListener != null) {
            showWinWindowListener.onShowWinWindow();
        }
    }

    public void setShowLoseWindowListener(ShowLoseWindowListener showLoseWindowListener) {
        this.showLoseWindowListener = showLoseWindowListener;
    }

    public void setShowWinWindowListener(ShowWinWindowListener showWinWindowListener) {
        this.showWinWindowListener = showWinWindowListener;
    }

    public void setGetNewHighScoreHolderNameListener(
            GetNewHighScoreHolderNameListener getNewHighScoreHolderNameListener) {
        this.getNewHighScoreHolderNameListener = getNewHighScoreHolderNameListener;
    }

    public void setOnStartNewGameListener(StartNewGameListener startNewGameListener) {
        this.startNewGameListener = startNewGameListener;
    }

    public void setOnMarkCellListener(MarkCellListener markCellListener) {
        this.markCellListener = markCellListener;
    }

    public void setOnOpenCellListener(OpenCellListener openCellListener) {
        this.openCellListener = openCellListener;
    }

    public void setOnSwitchGameDifficultyListener(SwitchGameDifficultyListener switchGameDifficultyListener) {
        this.switchGameDifficultyListener = switchGameDifficultyListener;
    }

    public void setOnOpenCellsAroundOpenedOneListener(OpenCellsAroundOpenedOneListener openCellsAroundOpenedOneListener) {
        this.openCellsAroundOpenedOneListener = openCellsAroundOpenedOneListener;
    }

    public void setOnSetTimerValueListener(ChangeTimerValueListener changeTimerValueListener) {
        this.changeTimerValueListener = changeTimerValueListener;
    }

    public void setOnCreateGameFieldListener(DrawGameFieldListener drawGameFieldListener) {
        this.drawGameFieldListener = drawGameFieldListener;
    }
}
