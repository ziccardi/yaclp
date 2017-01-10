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

    public void consume(IOption option, List<String> args, int pos, Result res) {
        if (pos < args.size()) {
            if (!args.get(pos).startsWith("-")) {
                res.addValue(option.getShortName(), args.remove(pos));
            } else {
                if (mandatory) {
                    throw new IllegalStateException("Mandatory arg for " + option.getShortName() + " not present");
                }
            }
        } else {
            if (mandatory) {
                throw new IllegalStateException("Mandatory arg for " + option.getShortName() + " not present");
            }
        }
    }
}
