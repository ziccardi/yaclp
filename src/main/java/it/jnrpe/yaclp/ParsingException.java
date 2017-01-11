package it.jnrpe.yaclp;

public class ParsingException extends Exception {
    public ParsingException(String msg) {
        super(msg);
    }

    public ParsingException(String pattern, String... values) {
        super(String.format(pattern, values));
    }
}
