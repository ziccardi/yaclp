package it.yaclp;

import java.util.ArrayList;
import java.util.List;

public class Option implements IOption {
    private List<IOption> requiredOptions = new ArrayList<IOption>();
    private Argument optionArgs = null;

    private final String shortName;

    private boolean hasArgs = false;

    public Option(final String shortName) {
        this.shortName = shortName;
    }

    public boolean isPresent(List<String> args) {
        return args.contains(shortName);
    }

    public void consume(List<String> args, Result res) {
        if (!isPresent(args)) {
            return;
        }
        for (IOption requiredOption : requiredOptions) {
            if (requiredOption.isPresent(args)) {
                requiredOption.consume(args, res);
            } else {
                if (res.hasOption(requiredOption.getShortName())) {
                    continue;
                }
                throw new IllegalStateException(shortName + " requires " + requiredOption.getShortName());
            }
        }

        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).equals(shortName)) {
                // consume and manage it
                args.remove(i);
                res.addValue(shortName, "true");
                if (optionArgs != null) {
                    optionArgs.consume(this, args, i, res);
                }
                // consume again if repeated
                consume(args, res);
            }
        }
    }

    public void addRequiredOption(IOption option) {
        requiredOptions.add(option);
    }

    public String getShortName() {
        return shortName;
    }

    public void setArgument(Argument arg) {
        this.optionArgs = arg;
    }
}
