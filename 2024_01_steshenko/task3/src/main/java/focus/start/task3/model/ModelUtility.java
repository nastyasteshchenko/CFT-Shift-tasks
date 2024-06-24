package focus.start.task3.model;

public final class ModelUtility {

    private ModelUtility() {
    }

    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value < max;
    }
}
