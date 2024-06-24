package focus.start.task2;

public final class TestUtility {
    private TestUtility() {
    }

    public static double roundToThousandths(double value) {
        return (double) Math.round(value * 1000) / 1000;
    }
}
