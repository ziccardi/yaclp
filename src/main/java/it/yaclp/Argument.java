package it.yaclp;

import java.util.List;

/**
 * Consumes the command line by removing the arguments of the current option/
 */
public class Argument {
    private final boolean mandatory;

    /**
     * Create a new argument object
     * @param mandatory <code>true</code> if the argument is mandatory
     */
    public Argument(final boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void consume(IOption option, List<String> args, int pos, CommandLine res) throws ParsingException {
        if (pos < args.size()) {
            // An argument must not start with '-'
            if (!args.get(pos).startsWith("-")) {
                res.addValue(option, args.remove(pos));
            } else {
                if (mandatory) {
                    throw new ParsingException("Mandatory argument for option [%s] is not present", option.getLongName());
                }
            }
        } else {
            if (mandatory) {
                throw new ParsingException("Mandatory argument for option [%s] is not present", option.getLongName());
            }
        }
    }
}
