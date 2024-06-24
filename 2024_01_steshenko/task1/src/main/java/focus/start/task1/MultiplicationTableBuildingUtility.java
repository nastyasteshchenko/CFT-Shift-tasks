package focus.start.task1;

final class MultiplicationTableBuildingUtility {
    private MultiplicationTableBuildingUtility() {
    }

    static MultiplicationTable build(int tableSize) {
        String[] table = new String[tableSize * tableSize];

        for (int i = 0; i < tableSize; i++) {
            int lineStart = i * tableSize;
            for (int j = 0; j < tableSize; j++) {
                table[lineStart + j] = String.valueOf((i + 1) * (j + 1));
            }
        }
        return new MultiplicationTable(table, tableSize);
    }
}
