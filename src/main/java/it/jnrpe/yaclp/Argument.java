package it.jnrpe.yaclp;

import java.util.List;

/**
 * Consumes the command line by removing the arguments of the current option/
 */
class Argument implements IArgument {
    private final boolean mandatory;
    private final String name;

    /**
     * Create a new argument object
     * @param mandatory <code>true</code> if the argument is mandatory
     */
    public Argument(final String name, final boolean mandatory) {
        this.mandatory = mandatory;
        this.name = name;
    }

    public void consume(IOption option, List<String> args, int pos, CommandLine res) throws ParsingException {
        if (pos < args.size()) {
            // An argument must not start with '-'
            if (!args.get(pos).startsWith("-")) {
                res.addValue(option, args.remove(pos));
            } else {
                if (mandatory) {
                    throw new ParsingException("Mandatory argument <%s> for option <%s> is not present", getName(), option.getLongName());
                }
            }
        } else {
            if (mandatory) {
                throw new ParsingException("Mandatory argument <%s> for option <%s> is not present", getName(), option.getLongName());
            }
        }
    }

    public String getName() {
        return name;
    }
}
