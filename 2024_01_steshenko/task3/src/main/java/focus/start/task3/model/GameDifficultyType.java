package focus.start.task3.model;

public enum GameDifficultyType {
    NOVICE(10, 9, 9),
    MEDIUM(40, 16, 16),
    EXPERT(99, 30, 16);

    private final int bombsAmount;
    private final Point fieldSize;

    GameDifficultyType(int bombsAmount, int columnsAmount, int rowsAmount) {
        this.bombsAmount = bombsAmount;
        this.fieldSize = new Point(columnsAmount, rowsAmount);
    }

    public int getBombsAmount() {
        return bombsAmount;
    }

    public Point getFieldSize() {
        return fieldSize;
    }
}
