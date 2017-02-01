package it.jnrpe.yaclp;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

abstract class AbstractOption implements IOption {
    private boolean mandatory = false;
    private boolean repeatable = false;
    private Set<AbstractOption> requiredOptions = new LinkedHashSet<>();
    private Set<AbstractOption> incompatibleOptions = new LinkedHashSet<>();

    private Argument optionArgs = null;

    /**
     * Consumes the passed in command line by parsing the option and removing it.
     *
     * @param args the command line. It gets changed by the method.
     * @param res the result of the parsing
     * @throws ParsingException on any error parsing the command line
     */
    final void consume(List<String> args, CommandLine res) throws ParsingException {
        sanityCheck(args, res);
        doConsume(args, res);
    }

    protected abstract void doConsume(List<String> args, CommandLine res) throws ParsingException;

    void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void addRequiredOption(IOption option) {
        requiredOptions.add((AbstractOption) option);
    }

    public void addIncompatibleOption(IOption option) {
        incompatibleOptions.add((AbstractOption)option);
        ((AbstractOption) option).incompatibleOptions.add(this);
    }

    Set<AbstractOption> getRequiredOptions() {
        return requiredOptions;
    }

    /**
     * Configures the arguments of this option.
     * @param arg the arguments of this option
     */
    void setArgument(Argument arg) {
        this.optionArgs = arg;
    }

    Argument getArgument() {
        return optionArgs;
    }

    void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public abstract boolean isPresent(List<String> args);

    private void sanityCheck(List<String> args, CommandLine res) throws ParsingException {
        // Check if the mandatory option is present
        if (!isPresent(args)) {
            if (isMandatory() && !res.hasOption(getShortName())) {
                throw new ParsingException("Mandatory option [%s] is missing", getLongName());
            }
            return;
        }

        // Check if a non repeatable option is repeated
        if (res.hasOption(getShortName()) && !isRepeatable()) {
            throw new ParsingException("Option [%s] can be specified only one time", getLongName());
        }

        // Check if incompatible options are presents
        for (AbstractOption option : incompatibleOptions) {
            if (res.hasOption(option.getShortName()) || option.isPresent(args)) {
                throw new ParsingException("Option [%s] can not be specified together with option [%s]", getLongName(), option.getLongName());
            }
        }

        // Check required option. If they are found, consume them
        for (AbstractOption requiredOption : getRequiredOptions()) {
            if (requiredOption.isPresent(args)) {
                requiredOption.consume(args, res);
            } else {
                if (res.hasOption(requiredOption.getShortName())) {
                    continue;
                }
                throw new ParsingException("%s requires [%s]", getLongName(), requiredOption.getLongName());
            }
        }
    }
}
