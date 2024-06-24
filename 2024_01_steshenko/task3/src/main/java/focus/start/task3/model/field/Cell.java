package focus.start.task3.model.field;

import focus.start.task3.model.Point;

public class Cell {
    private static final int MAX_CELLS_AMOUNT_NEAR_OTHER_ONE = 8;

    private final Point coordinate;
    private int nearMinesAmount;
    private boolean isMarked;
    private boolean isMine;
    private boolean isOpened;

    Cell(Point coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isEmpty() {
        return nearMinesAmount == 0;
    }

    public int getNearBombsAmount() {
        return nearMinesAmount;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public boolean isBomb() {
        return isMine;
    }

    public boolean isClosed() {
        return !isOpened;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    void setMine() {
        isMine = true;
    }

    void setOpened() {
        isOpened = true;
    }

    void setMarked(boolean marked) {
        isMarked = marked;
    }

    void reset() {
        nearMinesAmount = 0;
        isMine = false;
        isOpened = false;
        isMarked = false;
    }

    void increaseNearMinesAmount() {
        if (nearMinesAmount < MAX_CELLS_AMOUNT_NEAR_OTHER_ONE) {
            nearMinesAmount++;
        }
    }
}
