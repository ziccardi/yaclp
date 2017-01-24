package it.jnrpe.yaclp;

import java.util.List;

public class Command {

    private final String shortName;
    private final String longName;
    private final String description;

    private final Parser parser;

    Command(final String shortName, final String longName, final String description, Parser parser) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.parser = parser;
    }

    public String getDescription() {
        return description;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    boolean isPresent(List<String> args) {
        if (args.isEmpty()) {
            return false;
        }

        String arg = args.get(0);
        return arg.equals(shortName) || arg.equals(longName);
    }

    void consume(List<String> args, CommandLine res) throws ParsingException {
        if (isPresent(args)) {
            res.setCommand(args.remove(0));
            parser.parse(args.toArray(new String[args.size()]), res);
            args.clear();
        }
    }

    Parser getParser() {
        return parser;
    }

    @Override
    public String toString() {
        if (shortName.equals(longName)) {
            return shortName;
        } else {
            return String.format("%s (%s)", longName, shortName);
        }
    }
}
