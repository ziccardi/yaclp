/*******************************************************************************
 * Copyright (c) 2017 Massimiliano Ziccardi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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

    private final String appName;
    private final Parser parser;

    private static final int OPTION_INDENTATION = 4;
    private static final int OPTION_TO_DESC_SPACES = 4;

    private int screenWidth = 80;

    public HelpFormatter(final String appName, final Parser parser) {
        this.appName = appName;
        this.parser = parser;
    }

    private String formatUsage() {

        List<Command> commands = parser.getCommands();

        if (commands != null && ! commands.isEmpty()) {
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

    public void printUsage(PrintStream pw) {
        pw.println("Usage:");
        pw.println(formatUsage());
    }

    private void updateModel(IOption option, HelpModel hm) {
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

    public void printHelp(PrintStream ps) {
        HelpModel hm = new HelpModel();

        List<IOption> options = parser.getOptions();
        for (IOption option : options) {
            updateModel(option, hm);
        }

        for (Command c : parser.getCommands()) {
            hm.addRow(c.toString(), c.getDescription());
        }

        int firstColumnWidth = OPTION_INDENTATION + hm.getLongestOptionLength() + OPTION_TO_DESC_SPACES;

        printModel(hm, 0, firstColumnWidth, 80, ps);
    }

    public void printHelp(final String command, int indentation, PrintStream ps) {
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

        int firstColumnWidth = OPTION_INDENTATION + indentation + hm.getLongestOptionLength() + OPTION_TO_DESC_SPACES;

        printModel(hm, OPTION_INDENTATION + indentation, firstColumnWidth, 80, ps);
    }

    private void printModel(HelpModel hm, int indent, int firstColumnLen, int screenWidth, PrintStream ps) {
        int secondColumnLen = screenWidth - firstColumnLen - indent;

        final String indentString = String.format("%1$-"+ (OPTION_INDENTATION + indent) +"s", "");

        String formatString = String.format("%%1$-%ds", firstColumnLen + indent);

        for (int i = 0; i < hm.getRowCount(); i++) {
            ps.print (String.format(formatString, indentString + hm.getRow(i)[0]));

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

    private String[] wrap(final String s, int len) {
        List<String> res = new ArrayList<>();

        String[] words = s.split(" ");
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
