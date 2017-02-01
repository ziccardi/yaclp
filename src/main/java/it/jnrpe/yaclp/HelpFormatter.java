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

package it.jnrpe.yaclp;

import it.jnrpe.yaclp.help.HelpModel;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for providing help info.
 */
public class HelpFormatter {

  // TODO: this class should be split in different classes to handle options and commands

  /**
   * Minimum indentation for options.
   */
  private static final int OPTION_INDENTATION = 4;
  /**
   * Distance from option and its description.
   */
  private static final int OPTION_TO_DESC_SPACES = 4;
  /**
   * Name of the app. Used to create the usage message.
   */
  private final String appName;
  /**
   * Parser to be used to create the help message.
   */
  private final Parser parser;
  /**
   * Screen width. Used for wordwrap.
   */
  private int screenWidth = 80;

  /**
   * Builds the help formatter.
   *
   * @param appName name of the app
   * @param parser  parser containing all the options
   */
  public HelpFormatter(final String appName, final Parser parser) {
    this.appName = appName;
    this.parser = parser;
  }

  /**
   * Returns a string containing the usage message.
   *
   * @return the usage message
   */
  private String formatUsage() {

    List<Command> commands = parser.getCommands();

    if (!commands.isEmpty()) {
      String[] commandNames = new String[commands.size()];

      for (int i = 0; i < commands.size(); i++) {
        commandNames[i] = commands.get(i).toString();
      }

      String commandUsage = String.format("[ %s ]", String.join(" | ", commandNames));

      return String.format("  %s %s", appName, commandUsage);
    } else {
      List<IOption> options = parser.getOptions();
      String[] opts = new String[options.size()];
      for (int i = 0; i < opts.length; i++) {
        opts[i] = options.get(i).toString() + " ";
      }

      return String.format("  %s %s", appName, String.join(" ", opts));

    }
  }

  /**
   * Prints the usage message on the provided {@link java.io.PrintWriter}.
   *
   * @param pw output print writer
   */
  public void printUsage(final PrintStream pw) {
    pw.println("Usage:");
    pw.println(formatUsage());
  }

  /**
   * Updates the model instance with all the options/command to be printed.
   *
   * @param option option to be added
   * @param hm     the model
   */
  private void updateModel(final IOption option, final HelpModel hm) {
    if (option instanceof MutuallyExclusiveOptions) {
      MutuallyExclusiveOptions mo = (MutuallyExclusiveOptions) option;
      IOption[] options = mo.getOptions();

      for (IOption nestedOpt : options) {
        updateModel(nestedOpt, hm);
      }
    } else {
      hm.addRow(option.toString(), option.getDescription());
    }
  }

  /**
   * Prints the help message for the parser passed to the constructor.
   *
   * @param ps the output printstream
   */
  public void printHelp(final PrintStream ps) {
    HelpModel hm = new HelpModel();

    List<IOption> options = parser.getOptions();
    for (IOption option : options) {
      updateModel(option, hm);
    }

    for (Command c : parser.getCommands()) {
      hm.addRow(c.toString(), c.getDescription());
    }

    int firstColumnWidth = OPTION_INDENTATION + hm.getLongestOptionLength() + OPTION_TO_DESC_SPACES;

    printModel(hm, 0, firstColumnWidth, screenWidth, ps);
  }

  /**
   * Prints the help of the specified command.
   *
   * @param command     command whose help must be printed
   * @param indentation intentation to be used to print the command help
   * @param ps          output print stream
   */
  public void printHelp(final String command, final int indentation, final PrintStream ps) {
    HelpModel hm = new HelpModel();

    Parser cmdParser = null;

    // Extract the command parser
    for (Command cmd : parser.getCommands()) {
      if (cmd.getLongName().equals(command) || cmd.getShortName().equals(command)) {
        cmdParser = cmd.getParser();
        break;
      }
    }

    if (cmdParser == null) {
      ps.printf("Unknown command <%s>%n", command);
      return;
    }

    List<IOption> options = cmdParser.getOptions();
    for (IOption option : options) {
      updateModel(option, hm);
    }

    for (Command c : cmdParser.getCommands()) {
      hm.addRow(c.toString(), c.getDescription());
    }

    int firstColumnWidth = OPTION_INDENTATION
        + indentation
        + hm.getLongestOptionLength()
        + OPTION_TO_DESC_SPACES;

    printModel(hm, OPTION_INDENTATION + indentation, firstColumnWidth, screenWidth, ps);
  }

  /**
   * Prints the model.
   *
   * @param hm             the model
   * @param indent         the indentation
   * @param firstColumnLen max length of the first column (used to align all the descriptions)
   * @param screenWidth    screen width
   * @param ps             output print stream
   */
  private void printModel(final HelpModel hm,
                          final int indent,
                          final int firstColumnLen,
                          final int screenWidth,
                          final PrintStream ps) {
    int secondColumnLen = screenWidth - firstColumnLen - indent;

    final String indentString = String.format("%1$-" + (OPTION_INDENTATION + indent) + "s", "");

    String formatString = String.format("%%1$-%ds", firstColumnLen + indent);

    for (int i = 0; i < hm.getRowCount(); i++) {
      ps.print(String.format(formatString, indentString + hm.getRow(i)[0]));

      String[] desc = wrap(hm.getRow(i)[1], secondColumnLen);
      if (desc.length == 0) {
        ps.println();
      } else {
        ps.println(desc[0]);

        for (int descLineIndex = 1; descLineIndex < desc.length; descLineIndex++) {
          ps.print(String.format(formatString, ""));
          ps.println(desc[descLineIndex]);
        }
      }
    }
  }

  /**
   * Splits and wordwrap a string.
   *
   * @param stringToWrap string to be splitted.
   * @param len maximum string length.
   * @return word-wrapped string
   */
  private String[] wrap(final String stringToWrap, final int len) {
    List<String> res = new ArrayList<>();

    String[] words = stringToWrap.split(" ");
    StringBuilder line = new StringBuilder();

    for (String word : words) {
      if (line.length() + word.length() < len) {
        line.append(word).append(' ');
      } else {
        res.add(line.toString().trim());
        line = new StringBuilder(word).append(' ');
      }
    }

    if (line.length() != 0) {
      res.add(line.toString());
    }

    return res.toArray(new String[res.size()]);
  }
}
