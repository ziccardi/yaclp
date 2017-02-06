package it.jnrpe.yaclp.validators;

import it.jnrpe.yaclp.ParsingException;

/**
 * Validation errors exception
 */
public class ValidationException extends ParsingException {

  /*** Builds a validation exception object.
   *
   * @param msg the message
   */
  public ValidationException(final String msg) {
    super(msg);
  }

  /**
   * Builds a parsing exception object.
   *
   * @param pattern the pattern of the message
   * @param values  values to be applied to the pattern to build the message. It uses
   *                {@link String#format(String, Object...)}
   */
  public ValidationException(final String pattern, final Object... values) {
    super(pattern, values);
  }
}
