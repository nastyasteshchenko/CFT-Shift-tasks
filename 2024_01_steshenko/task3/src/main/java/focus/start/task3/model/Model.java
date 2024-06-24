package focus.start.task3.model;

import focus.start.task3.controller.listener.*;
import focus.start.task3.model.field.Cell;
import focus.start.task3.model.field.Field;
import focus.start.task3.model.listener.*;
import focus.start.task3.view.listener.MarkCellListener;
import focus.start.task3.view.listener.OpenCellListener;
import focus.start.task3.view.listener.OpenCellsAroundOpenedOneListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Model implements StartNewGameListener, MarkCellListener, OpenCellListener, SwitchGameDifficultyListener,
        OpenCellsAroundOpenedOneListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Model.class);

    private LoseEventListener loseEventListener;
    private WinEventListener winEventListener;
    private ChangeBombsAmountListener changeBombsAmountListener;
    private DrawOpenedCellListener drawOpenedCellListener;
    private DrawMarkedCellListener drawMarkedCellListener;
    private DrawUnmarkedCellListener drawUnmarkedCellListener;
    private DrawOpenedBombListener drawOpenedBombListener;
    private StartTimerListener startTimerListener;

    private boolean isGameInProcess = false;
    private GameDifficultyType gameDifficultyType = GameDifficultyType.NOVICE;
    private final Field field = new Field();

    @Override
    public void onStartNewGame() {
        field.resetField();
        if (changeBombsAmountListener != null) {
            changeBombsAmountListener.onBombsAmountChanged(field.getLeftBombsAmount());
        }
    }

    @Override
    public void onMarkCell(int x, int y) {
        Cell cell = field.getCell(x, y);
        if (cell.isClosed() && field.getLeftBombsAmount() > 0) {
            if (cell.isMarked()) {
                field.unmarkCell(x, y);
                LOGGER.debug("Cell with coordinate ({}, {}) was unmarked.", x, y);
                if (drawUnmarkedCellListener != null) {
                    drawUnmarkedCellListener.onDrawUnmarkedCell(x, y);
                }
            } else {
                field.markCell(x, y);
                LOGGER.debug("Cell with coordinate ({}, {}) was marked.", x, y);
                if (drawMarkedCellListener != null) {
                    drawMarkedCellListener.onDrawMarkedCell(x, y);
                }
            }
            if (changeBombsAmountListener != null) {
                changeBombsAmountListener.onBombsAmountChanged(field.getLeftBombsAmount());
            }
        }
    }

    @Override
    public void onOpenCell(int x, int y) {
        if (!field.isGenerated()) {
            field.generateField(x, y);
            LOGGER.info("Field was generated");
            isGameInProcess = true;
            if (startTimerListener != null) {
                startTimerListener.onStartTimer();
            }
        }
        Cell cell = field.getCell(x, y);
        if (!cell.isMarked() && cell.isClosed()) {
            field.openCell(x, y);
            if (cell.isBomb()) {
                LOGGER.debug("Bomb with coordinates ({}, {}) was opened.", x, y);
                handleGameLoss();
                return;
            }
            LOGGER.debug("Cell with coordinates ({}, {}) and {} near bombs was opened.",
                    x, y, cell.getNearBombsAmount());
            if (drawOpenedCellListener != null) {
                drawOpenedCellListener.onDrawOpenedCell(x, y, cell.getNearBombsAmount());
            }
            if (cell.isEmpty()) {
                openCellsAround(x, y);
            }
            if (isGameWin()) {
                handleGameWin();
            }
        }
    }

    @Override
    public void onSwitchGameDifficulty(GameDifficultyType gameDifficultyType) {
        this.gameDifficultyType = gameDifficultyType;
        field.switchField(gameDifficultyType);
        LOGGER.info("Game difficulty was switched to {}.", gameDifficultyType.name());
    }


    @Override
    public void onOpenCellsAroundOpenedOne(int x, int y) {
        Cell cell = field.getCell(x, y);
        if (!cell.isClosed() && field.isMarkedCellsAmountEqualsNearBombsAmount(x, y)) {
            openCellsAround(x, y);
        }
    }

    public void setWinEventListener(WinEventListener winEventListener) {
        this.winEventListener = winEventListener;
    }

    public void setLoseEventListener(LoseEventListener loseEventListener) {
        this.loseEventListener = loseEventListener;
    }

    public void setBombsAmountListener(ChangeBombsAmountListener changeBombsAmountListener) {
        this.changeBombsAmountListener = changeBombsAmountListener;
    }

    public void setOpenCellListener(DrawOpenedCellListener drawOpenedCellListener) {
        this.drawOpenedCellListener = drawOpenedCellListener;
    }

    public void setMarkCellListener(DrawMarkedCellListener drawMarkedCellListener) {
        this.drawMarkedCellListener = drawMarkedCellListener;
    }

    public void setUnmarkCellListener(DrawUnmarkedCellListener drawUnmarkedCellListener) {
        this.drawUnmarkedCellListener = drawUnmarkedCellListener;
    }

    public void setOpenBombListener(DrawOpenedBombListener drawOpenedBombListener) {
        this.drawOpenedBombListener = drawOpenedBombListener;
    }


    public void setStartTimerListener(StartTimerListener startTimerListener) {
        this.startTimerListener = startTimerListener;
    }

    private boolean isGameWin() {
        return field.getOpenCellsAmount() == field.getCellsAmount() - gameDifficultyType.getBombsAmount();
    }

    private void openCellsAround(int x, int y) {
        for (int nearX = x - 1; nearX <= x + 1; nearX++) {
            if (!ModelUtility.isInRange(nearX, 0, field.getFieldSize().x())) {
                continue;
            }
            for (int nearY = y - 1; nearY <= y + 1; nearY++) {
                if ((nearX != x || nearY != y)
                        && ModelUtility.isInRange(nearY, 0, field.getFieldSize().y())) {
                    onOpenCell(nearX, nearY);
                    if (!isGameInProcess) {
                        return;
                    }
                }
            }
        }
    }

    private void handleGameLoss() {
        if (drawOpenedBombListener != null) {
            List<Cell> bombs = field.getBombs();
            if (drawOpenedBombListener != null) {
                for (Cell c : bombs) {
                    drawOpenedBombListener.onDrawOpenedBomb(c.getCoordinate().x(), c.getCoordinate().y());
                }
            }
        }
        isGameInProcess = false;
        LOGGER.info("Game lose.");
        if (loseEventListener != null) {
            loseEventListener.onLoseGame();
        }
    }

    private void handleGameWin() {
        isGameInProcess = false;
        LOGGER.info("Game win.");
        if (winEventListener != null) {
            winEventListener.onGameWin(gameDifficultyType);
        }
    }
}
