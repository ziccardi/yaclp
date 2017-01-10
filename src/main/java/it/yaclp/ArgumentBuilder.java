package it.yaclp;

public class ArgumentBuilder {
    private final String name;
    private boolean mandatory = true;

    private ArgumentBuilder(final String name) {
        this.name = name;
    }

    public static ArgumentBuilder forArgument(final String name) {
        return new ArgumentBuilder(name);
    }

    public ArgumentBuilder mandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public IArgument build() {
        return new Argument(name, mandatory);
    }
}
