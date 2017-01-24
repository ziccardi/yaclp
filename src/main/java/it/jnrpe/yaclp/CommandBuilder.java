package it.jnrpe.yaclp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBuilder {
    private final String shortName;
    private final String longName;
    private String description;

    private List<IOption> options = new ArrayList<>();

    private CommandBuilder(final String shortName, final String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public static CommandBuilder forNewCommand(String shortName, String longName) {
        return new CommandBuilder(shortName, longName);
    }

    public static CommandBuilder forNewCommand(String name) {
        return new CommandBuilder(name, name);
    }

    /**
     * Adds options to the command configuration
     * @param options the options to be added
     * @return this builder
     */
    public CommandBuilder withOption(final IOption... options) {
        if (options.length == 1) {
            this.options.add(options[0]);
        } else {
            this.options.addAll(Arrays.asList(options));
        }
        return this;
    }

    public CommandBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Builds a new Command instance
     * @return the newly build command instance
     */
    public Command build() {
        Parser p = new Parser();
        options.forEach(p::addOption);

        return new Command(shortName, longName, description, p);
    }

}
