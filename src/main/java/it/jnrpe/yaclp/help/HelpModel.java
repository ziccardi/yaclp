package it.jnrpe.yaclp.help;

import java.util.ArrayList;
import java.util.List;

public class HelpModel {
    private static class Row {
        private final String[] columns;

        private Row(String... columns) {
            this.columns = columns;
        }

        private int getColumnCount() {
            return columns.length;
        }

        private String getColumn(int index) {
            return columns[index];
        }
    }

    private List<Row> rows = new ArrayList<>();

    public void addRow(String column1, String column2) {
        rows.add(new Row(column1, column2));
    }

    public int getLongestOptionLength() {
        int maxLen = 0;

        for (Row row : rows) {
            maxLen = Math.max(maxLen, row.getColumn(0).length());
        }

        return maxLen;
    }

    public int getRowCount() {
        return rows.size();
    }

    public String[] getRow(int index) {
        Row row = rows.get(index);
        return new String[]{row.getColumn(0), row.getColumn(1)};
    }
}
