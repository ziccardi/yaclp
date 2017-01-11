package it.jnrpe.yaclp;

import java.util.ArrayList;
import java.util.List;

public class SimpleOptionBuilder {
    private final String shortName;
    private final String longName;

    private final List<IOption> requiredOptions = new ArrayList<IOption>();

    private boolean multiplicity = false;
    private boolean mandatory = false;
    private String description = "";

    private IArgument argument = null;

    SimpleOptionBuilder(final String shortName, final String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public SimpleOptionBuilder multiplicity(boolean multiplicity) {
        this.multiplicity = multiplicity;
        return this;
    }

    public SimpleOptionBuilder mandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public SimpleOptionBuilder description(String description) {
        this.description = description;
        return this;
    }

    public SimpleOptionBuilder requires(IOption opt) {
        this.requiredOptions.add(opt);
        return this;
    }

    public SimpleOptionBuilder argument(IArgument arg) {
        this.argument = arg;
        return this;
    }

    public IOption build() {
        Option option = new Option(shortName, longName);
        option.setMultiple(multiplicity);
        option.setMandatory(mandatory);
        option.setDescription(description);
        option.setArgument(argument);

        return option;
    }
}
