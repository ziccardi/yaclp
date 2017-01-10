package it.yaclp;

import java.util.List;

/**
 * Created by ziccardi on 10/01/2017.
 */
public class Argument {
    private final boolean mandatory;

    public Argument(final boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void consume(IOption option, List<String> args, int pos, Result res) throws ParsingException {
        if (pos < args.size()) {
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
