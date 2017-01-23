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

import it.jnrpe.yaclp.help.OptionHelpFormatter;

import java.io.PrintStream;
import java.util.List;

/**
 * Utility class for providing help info.
 */
public class HelpFormatter {

    private final String appName;
    private final Parser parser;

    private static final int OPTION_INDENTATION = 4;
    private static final int OPTION_TO_DESC_SPACES = 4;

    private int longestOptionLength = 0;
    private int screenWidth = 80;

    private OptionHelpFormatter optionHelpFormatter;

    public HelpFormatter(final String appName, final Parser parser) {
        this.appName = appName;
        this.parser = parser;

        init();
    }

    private void init() {
        List<IOption> options = parser.getOptions();
        for (IOption opt : options) {
            longestOptionLength = Math.max(getOptionLength(opt), longestOptionLength);
        }

        this.optionHelpFormatter = new OptionHelpFormatter(OPTION_INDENTATION, longestOptionLength, OPTION_TO_DESC_SPACES, screenWidth);
    }

    private int getOptionLength(IOption opt) {
        if (opt instanceof MutuallyExclusiveOptions) {
            MutuallyExclusiveOptions option = (MutuallyExclusiveOptions) opt;
            int maxLen = 0;
            for (IOption nestedOpt : option.getOptions()) {
                maxLen = Math.max(getOptionLength(nestedOpt), maxLen);
            }

            return maxLen;
        } else {
            return opt.toString().length();
        }

    }

    private String formatUsage() {
        List<IOption> options = parser.getOptions();
        String[] opts = new String[options.size()];
        for (int i = 0; i < opts.length; i++) {
            opts[i] = options.get(i).toString() + " ";
        }

        return String.format("  %s %s", appName, String.join(" ", opts));
    }

    public void printUsage(PrintStream pw) {
        pw.println("Usage:");
        pw.println(formatUsage());
    }

    private String formatHelp(IOption opt) {
        if (opt instanceof MutuallyExclusiveOptions) {
            MutuallyExclusiveOptions mo = (MutuallyExclusiveOptions) opt;
            IOption[] options = mo.getOptions();

            StringBuilder ret = new StringBuilder();

            for (IOption option : options) {
                ret.append("\n").append(formatHelp(option));
            }
            return ret.toString().substring(1);
        } else {
            return optionHelpFormatter.format(opt);
        }
    }

    public void printHelp(PrintStream ps) {
        List<IOption> options = parser.getOptions();
        for (IOption option : options) {
            ps.println(formatHelp(option));
        }
    }
}
