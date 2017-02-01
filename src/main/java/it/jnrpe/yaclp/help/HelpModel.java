/*******************************************************************************
 * Copyright (c) 2017 Massimiliano Ziccardi
 * <P/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <P/>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <P/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package it.jnrpe.yaclp.help;

import java.util.ArrayList;
import java.util.List;

/**
 * Object used to contain the columns to be visualised in the help message.
 */
public final class HelpModel {

  /**
   * Rows composing the model.
   */
  private List<Row> rows = new ArrayList<>();

  /**
   * Adds a row to the model.
   *
   * @param column1 first column
   * @param column2 second column
   */
  public void addRow(final String column1, final String column2) {
    rows.add(new Row(column1, column2));
  }

  /**
   * Returns the longest option length.
   *
   * @return the longest option length.
   */
  public int getLongestOptionLength() {
    int maxLen = 0;

    for (Row row : rows) {
      maxLen = Math.max(maxLen, row.getColumn(0).length());
    }

    return maxLen;
  }

  /**
   * Returns the number of rows of the model.
   *
   * @return the number of rows of the model
   */
  public int getRowCount() {
    return rows.size();
  }

  /**
   * Returns the row at the given index.
   *
   * @param index the row index
   * @return the columns composing the row
   */
  public String[] getRow(final int index) {
    Row row = rows.get(index);
    return new String[]{row.getColumn(0), row.getColumn(1)};
  }

  /**
   * A model row object.
   */
  private static class Row {

    /**
     * Columns composing the row.
     */
    private final String[] columns;

    /**
     * Contructor.
     *
     * @param columns the columns composing the row
     */
    private Row(final String... columns) {
      this.columns = columns;
    }

    /**
     * Returns the value of the column at position index.
     *
     * @param index the column index
     * @return the value
     */
    private String getColumn(final int index) {
      return columns[index];
    }
  }
}
