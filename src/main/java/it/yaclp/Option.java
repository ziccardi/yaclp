package it.yaclp;

import java.util.ArrayList;
import java.util.List;

public class Option implements IOption {
    private List<IOption> requiredOptions = new ArrayList<IOption>();
    private Argument optionArgs = null;

    private final String shortName;
    private final String longName;

    private boolean mandatory = false;
    private boolean multiple = false;

    public Option(final String shortName, final String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public boolean isPresent(List<String> args) {
        return args.contains(shortName) || args.contains(longName);
    }

    public void consume(List<String> args, CommandLine res) throws ParsingException{
        if (!isPresent(args)) {
            if (mandatory) {
                throw new ParsingException("Mandatory option [%s] is missing", getLongName());
            }
            return;
        }

        if (res.hasOption(getShortName()) && !multiple) {
            throw new ParsingException("Option [%s] can be specified only one time", getLongName());
        }

        for (IOption requiredOption : requiredOptions) {
            if (requiredOption.isPresent(args)) {
                requiredOption.consume(args, res);
            } else {
                if (res.hasOption(requiredOption.getShortName())) {
                    continue;
                }
                throw new ParsingException("%s requires [%s]", longName, requiredOption.getLongName());
            }
        }

        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).equals(shortName) || args.get(i).equals(longName)) {
                // consume and manage it
                args.remove(i);
                res.addValue(this, null);
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

    public String getLongName() {
        return longName;
    }

    public void setArgument(Argument arg) {
        this.optionArgs = arg;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }
}
