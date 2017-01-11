package it.jnrpe.yaclp;

import it.jnrpe.yaclp.help.OptionHelpFormatter;
import org.apache.commons.lang.StringUtils;

import java.io.PrintStream;
import java.util.List;

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
        StringBuilder usage = new StringBuilder(appName)
            .append(" ");

        List<IOption> options = parser.getOptions();
        String[] opts = new String[options.size()];
        for (int i = 0; i < opts.length; i++) {
            opts[i] = options.get(i).toString() + " ";
        }

        return String.format("  %s %s", appName, StringUtils.join(opts));
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
