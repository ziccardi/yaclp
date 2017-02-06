/*******************************************************************************
 * Copyright (c) 2017 Massimiliano Ziccardi
 * <P/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <P/>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <P/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package it.jnrpe.yaclp.validators;

import it.jnrpe.yaclp.IArgument;
import it.jnrpe.yaclp.IOption;
import it.jnrpe.yaclp.ParsingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for a generic string argument.
 */
public class StringValidator implements IArgumentValidator {

  /**
   * Minimum length of the string (null if no min must be checked).
   */
  private final Integer minLen;

  /**
   * Maximum length of the string (null if no max must be checked).
   */
  private final Integer maxLen;

  /**
   * Regexp to be used to validate the argument value or null.
   */
  private final Pattern regexp;

  /**
   * Constructor.
   *
   * @param minLen minimum string length or null.
   * @param maxLen maximum string length or null.
   * @param regexp regexp to be used to validate the argument or null.
   */
  private StringValidator(final Integer minLen, final Integer maxLen, final String regexp) {
    this.minLen = minLen;
    this.maxLen = maxLen;

    if (regexp != null) {
      this.regexp = Pattern.compile(regexp);
    } else {
      this.regexp = null;
    }
  }

  /**
   * Validates the string accorging to the passed in configuration.
   *
   * @param value the argument value
   * @throws ParsingException on error validating the string
   */
  public void validate(
      final IOption option,
      final IArgument argument,
      final String value) throws ParsingException {
    if (minLen != null && value.length() < minLen) {
      throw new ParsingException("Value is too short (minimum length: %d)", minLen);
    }

    if (maxLen != null && value.length() > maxLen) {
      throw new ParsingException("Value is too long (maximum length: %d)", minLen);
    }

    if (regexp != null) {
      Matcher matcher = regexp.matcher(value);
      if (!matcher.matches()) {
        throw new ParsingException("Value is not valid according to specified rules");
      }
    }
  }

  /**
   * Builder for {@link StringValidator} objects.
   */
  public static final class Builder {

    /**
     * Minimum string length (defaults to 0).
     */
    private Integer minLen = 0;

    /**
     * Maximum string length (defaults to undefined).
     */
    private Integer maxLen = null;

    /**
     * Regular expression to be used to validate teh argument.
     * Defaults to null.
     */
    private String regexp = null;

    /**
     * Constructor.
     */
    Builder() {
    }

    /**
     * Sets the minimum length of the string.
     *
     * @param minLen the minimum length.
     * @return this builder.
     */
    public Builder minLen(final Integer minLen) {
      this.minLen = minLen;
      return this;
    }

    /**
     * Sets the maximum length of the string.
     *
     * @param maxLen the maximum length.
     * @return this builder.
     */
    public Builder maxLen(final Integer maxLen) {
      this.maxLen = maxLen;
      return this;
    }

    /**
     * Sets the regexp to be used to validate the string argument.
     *
     * @param regexp the regular expression length.
     * @return this builder.
     */
    public Builder regexp(final String regexp) {
      this.regexp = regexp;
      return this;
    }

    public StringValidator build() {
      return new StringValidator(minLen, maxLen, regexp);
    }
  }
}
