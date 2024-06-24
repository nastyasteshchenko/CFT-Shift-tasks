package focus.start.task2.shapes;

import java.text.DecimalFormat;

public sealed abstract class Shape permits Rectangle, Circle, Triangle {
    protected final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    protected final static String MEASUREMENT_UNIT = "cм";
    protected final static String AREA_MEASUREMENT_UNIT = "кв. " + MEASUREMENT_UNIT;

    abstract double calculateArea();

    abstract double calculatePerimeter();

    @Override
    public String toString() {
        return String.format("Площадь: %s %s%nПериметр: %s %s%n",
                DECIMAL_FORMAT.format(calculateArea()), AREA_MEASUREMENT_UNIT,
                DECIMAL_FORMAT.format(calculatePerimeter()), MEASUREMENT_UNIT);
    }
}
