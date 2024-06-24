package focus.start.task1;

import java.io.OutputStream;
import java.io.PrintWriter;

class MultiplicationTablePrinter {
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String VERTICAL_SEPARATOR = "|";
    private static final String SPACE = " ";
    private final PrintWriter output;
    private final MultiplicationTable table;
    private final String tableLinesSeparator;
    private final int firstColumnWidth;
    private final int otherColumnsWidth;

    MultiplicationTablePrinter(MultiplicationTable table, OutputStream output) {
        this.output = new PrintWriter(output);
        this.table = table;

        String[] tableValues = table.tableValues();
        firstColumnWidth = tableValues[table.size() - 1].length();
        otherColumnsWidth = tableValues[tableValues.length - 1].length();

        String firstCellHorizSeparator = MINUS.repeat(firstColumnWidth);
        String otherCellHorizSeparator = PLUS + MINUS.repeat(otherColumnsWidth);
        tableLinesSeparator = firstCellHorizSeparator + otherCellHorizSeparator.repeat(table.size());
    }

    void print() {
        printFirstLine();
        printNextLines();
        output.flush();
    }

    private void printFirstLine() {
        output.print(SPACE.repeat(firstColumnWidth));

        String[] tableValues = table.tableValues();

        for (int i = 0; i < table.size(); i++) {
            output.print(VERTICAL_SEPARATOR);
            printCellValue(tableValues[i], otherColumnsWidth);
        }

        printTableLinesSeparator();
    }

    private void printNextLines() {
        String[] tableValues = table.tableValues();

        for (int i = 0; i < table.size(); i++) {
            printCellValue(tableValues[i], firstColumnWidth);

            int startIndex = i * table.size();
            for (int j = startIndex; j < table.size() + startIndex; j++) {
                output.print(VERTICAL_SEPARATOR);
                printCellValue(tableValues[j], otherColumnsWidth);
            }

            printTableLinesSeparator();
        }
    }

    private void printCellValue(String value, int columnsCellWidth) {
        output.print(SPACE.repeat(columnsCellWidth - value.length()));
        output.print(value);
    }

    private void printTableLinesSeparator() {
        output.println();
        output.println(tableLinesSeparator);
    }

}
