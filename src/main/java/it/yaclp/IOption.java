package it.yaclp;

import java.util.List;

/**
 * Generic option interface.
 */
public interface IOption {
    /**
     * Checks if the option is present in the given command line.
     * @param args the command line
     * @return true or false
     */
    boolean isPresent(List<String> args);

    /**
     * Consumes the passed in command line by parsing the option and removing it.
     *
     * @param args the command line. It gets changed by the method.
     * @param res the result of the parsing
     * @throws ParsingException on any error parsing the command line
     */
    void consume(List<String> args, CommandLine res) throws ParsingException;

    /**
     * Sets the dependency from this option to another one.
     * @param option option to depend on
     */
    void addRequiredOption(IOption option);

    /**
     * Returns the short name for this option.
     * @return the short name for this option
     */
    String getShortName();

    /**
     * Returns the long name for this option.
     * @return Returns the long name for this option.
     */
    String getLongName();

    /**
     * Configures the arguments of this option.
     * @param arg the arguments of this option
     */
    void setArgument(IArgument arg);

    /**
     * Checks if this option is mandatory
     * @return  true or false
     */
    boolean isMandatory();

    void setDescription(String description);

    String getDescription();
}
