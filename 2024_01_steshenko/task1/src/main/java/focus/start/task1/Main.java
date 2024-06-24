package focus.start.task1;

import java.util.Scanner;

import static focus.start.task1.InputErrorsUtility.*;

public class Main {
    static final int MAX_SIZE = 32;
    static final int MIN_SIZE = 1;
    private static final int STOP_PROGRAM_RETURN_VALUE = -1;
    private static final String STOP_PROGRAM_INPUT = "exit";

    public static void main(String[] args) {
        int size = getTableSize();
        if (size != STOP_PROGRAM_RETURN_VALUE) {
            MultiplicationTable table = buildTable(size);
            printTable(table);
        }
    }

    private static void printTable(MultiplicationTable table) {
        MultiplicationTablePrinter printer = new MultiplicationTablePrinter(table, System.out);
        printer.print();
    }

    private static MultiplicationTable buildTable(int size) {
        return MultiplicationTableBuildingUtility.build(size);
    }

    private static int getTableSize() {
        Scanner in = new Scanner(System.in);
        System.out.println("To end the program enter '" + STOP_PROGRAM_INPUT + "'");
        while (true) {
            System.out.print("Enter the size of the multiplication table: ");
            String inputStr = in.next();
            if (STOP_PROGRAM_INPUT.equalsIgnoreCase(inputStr)) {
                return STOP_PROGRAM_RETURN_VALUE;
            }
            try {
                int tableSize = Integer.parseInt(inputStr);
                if (tableSize > MAX_SIZE || tableSize < MIN_SIZE) {
                    printIntegerOutOfRangeMsg();
                    continue;
                }
                return tableSize;
            } catch (NumberFormatException e1) {
                checkDataType(inputStr);
            }
        }
    }

    private static void checkDataType(String inputStr) {
        try {
            Double.parseDouble(inputStr);
            printRealInputMsg();
        } catch (NumberFormatException e2) {
            printNotNumberInputMsg();
        }
    }

}