package it.jnrpe.yaclp;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractOption implements IOption {
    private boolean mandatory = false;
    private boolean repeatable = false;
    private List<AbstractOption> requiredOptions = new ArrayList<>();

    private Argument optionArgs = null;

    /**
     * Consumes the passed in command line by parsing the option and removing it.
     *
     * @param args the command line. It gets changed by the method.
     * @param res the result of the parsing
     * @throws ParsingException on any error parsing the command line
     */
    abstract void consume(List<String> args, CommandLine res) throws ParsingException;

    void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    void addRequiredOption(AbstractOption option) {
        requiredOptions.add(option);
    }

    List<AbstractOption> getRequiredOptions() {
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
}
