package it.yaclp;

import org.apache.commons.lang.StringUtils;

import java.util.List;

class MutuallyExclusiveOptions implements IOption {

    private IOption[] options;

    private boolean mandatory = false;

    public MutuallyExclusiveOptions (IOption... options) {
        this.options = options;
    }

    public void addRequiredOption(IOption option) {

    }

    private String getOptionNames(String separator) {
        String[] params = new String[options.length];
        for (int i = 0; i < params.length; ) {
            params[i] = options[i++].getLongName();
        }

        return StringUtils.join(params, separator);
    }

    public void consume(List<String> args, CommandLine res) throws ParsingException{
        // Only one must be present...
        IOption passedInOption = null;

        for (IOption opt : options) {
            if (opt.isPresent(args)) {
                if (passedInOption != null) {
                    throw new ParsingException("Incompatible options present: only one of [%s] must be specified", getOptionNames(","));
                } else {
                    passedInOption = opt;
                }
            }
        }

        // Ok, only at most one is present...
        if (passedInOption != null) {
            passedInOption.consume(args, res);
        } else {
            if (mandatory) {
                throw new ParsingException("Mandatory option missing: one of [%s] must be passed", getOptionNames(","));
            }
        }
    }

    public boolean isPresent(List<String> args) {
        boolean present = false;

        for (IOption opt : options) {
            present |= opt.isPresent(args);
        }

        return present;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public String toString() {
        String[] params = new String[options.length];
        for (int i = 0; i < params.length; i++) {
            IOption opt = options[i];
            params[i] = opt.toString();
        }

        return String.format("[%s]", StringUtils.join(params, " | "));
    }

    IOption[] getOptions() {
        return options;
    }

    public void setDescription(String description) {
    }

    public String getDescription() {
        return null;
    }

    public String getShortName() {
        return null;
    }

    public String getLongName() {
        return null;
    }

    public void setArgument(IArgument arg) {}

}
