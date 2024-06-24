package focus.start.task3.model.field;

import focus.start.task3.model.GameDifficultyType;
import focus.start.task3.model.ModelUtility;
import focus.start.task3.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {

    private final Random random = new Random();
    private int difficultyBombsAmount;
    private int leftBombsAmount;
    private int openCellsAmount = 0;
    private boolean isGenerated = false;
    private Point fieldSize = GameDifficultyType.NOVICE.getFieldSize();
    private Cell[][] cells;

    private final List<Cell> bombs = new ArrayList<>();

    public Field() {
        switchField(GameDifficultyType.NOVICE);
    }

    public void switchField(GameDifficultyType gameDifficultyType) {
        fieldSize = gameDifficultyType.getFieldSize();
        cells = new Cell[fieldSize.x()][fieldSize.y()];
        difficultyBombsAmount = gameDifficultyType.getBombsAmount();
        leftBombsAmount = difficultyBombsAmount;
        initField();
    }

    public void generateField(int clickedX, int clickedY) {
        int setMines = 0;
        while (setMines < difficultyBombsAmount) {
            int x = random.nextInt(fieldSize.x());
            int y = random.nextInt(fieldSize.y());
            if (x == clickedX && y == clickedY) {
                continue;
            }
            Cell cell = cells[x][y];
            if (!cell.isBomb()) {
                cell.setMine();
                bombs.add(cell);
                for (int nearX = x - 1; nearX <= x + 1; nearX++) {
                    if (!ModelUtility.isInRange(nearX, 0, getFieldSize().x())) {
                        continue;
                    }
                    for (int nearY = y - 1; nearY <= y + 1; nearY++) {
                        if ((nearX != x || nearY != y)
                                && ModelUtility.isInRange(nearY, 0, getFieldSize().y())) {
                            cells[nearX][nearY].increaseNearMinesAmount();
                        }
                    }
                }
                setMines++;
            }
        }
        isGenerated = true;
    }

    public void openCell(int x, int y) {
        cells[x][y].setOpened();
        openCellsAmount++;
    }

    public void markCell(int x, int y) {
        if (!cells[x][y].isMarked()) {
            leftBombsAmount--;
        }
        cells[x][y].setMarked(true);
    }

    public void unmarkCell(int x, int y) {
        if (cells[x][y].isMarked()) {
            leftBombsAmount++;
        }
        cells[x][y].setMarked(false);
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public boolean isMarkedCellsAmountEqualsNearBombsAmount(int x, int y) {
        int markedCellsAroundAmount = 0;
        for (int nearX = x - 1; nearX <= x + 1; nearX++) {
            for (int nearY = y - 1; nearY <= y + 1; nearY++) {
                if (!ModelUtility.isInRange(nearX, 0, getFieldSize().x())) {
                    continue;
                }
                if ((nearX != x || nearY != y) && ModelUtility.isInRange(nearY, 0, getFieldSize().y())
                        && cells[nearX][nearY].isMarked()) {
                    markedCellsAroundAmount++;
                }
            }
        }
        return markedCellsAroundAmount == cells[x][y].getNearBombsAmount();
    }

    public void resetField() {
        isGenerated = false;
        bombs.clear();
        leftBombsAmount = difficultyBombsAmount;
        openCellsAmount = 0;
        for (int i = 0; i < fieldSize.x(); i++) {
            for (int j = 0; j < fieldSize.y(); j++) {
                cells[i][j].reset();
            }
        }
    }

    public Point getFieldSize() {
        return fieldSize;
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public int getOpenCellsAmount() {
        return openCellsAmount;
    }

    public int getCellsAmount() {
        return fieldSize.x() * fieldSize.y();
    }

    public List<Cell> getBombs() {
        return bombs;
    }

    public int getLeftBombsAmount() {
        return leftBombsAmount;
    }

    private void initField() {
        for (int i = 0; i < fieldSize.x(); i++) {
            for (int j = 0; j < fieldSize.y(); j++) {
                cells[i][j] = new Cell(new Point(i, j));
            }
        }
    }
}
