package focus.start.task2.shapes;

public enum ShapeType {
    CIRCLE(1),
    RECTANGLE(2),
    TRIANGLE(3);

    private final int parametersAmount;

    ShapeType(int parametersAmount) {
        this.parametersAmount = parametersAmount;
    }

    public int getParametersAmount() {
        return parametersAmount;
    }
}
