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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * This class validates that the argument value falls inside a specified list of values.
 */
public class EnumValidator implements IArgumentValidator {

  /**
   * Collection of accepted values.
   */
  private final Collection<String> acceptedValues;

  /**
   * Whether to perform a case sensitive or insensitive check.
   */
  private final boolean caseSensitive;

  /**
   * Constructor.
   *
   * @param acceptedValues list of accepted values
   * @param caseSensitive Whether to perform a case sensitive or insensitive check
   */
  private EnumValidator(final Collection<String> acceptedValues, final boolean caseSensitive) {
    this.caseSensitive = caseSensitive;

    if (caseSensitive) {
      this.acceptedValues = acceptedValues;
    } else {
      this.acceptedValues = new HashSet<>();

      acceptedValues.forEach(value -> this.acceptedValues.add(value.toLowerCase()));
    }
  }

  @Override
  public void validate(
      final IOption option,
      final IArgument argument,
      final String value) throws ParsingException {
      if (!acceptedValues.contains(caseSensitive ? value : value.toLowerCase())) {
        throw new ParsingException(
            "Value for argument <%s> of option <%s> must be one of [%s]",
            argument.getName(),
            option.getLongName(),
            String.join(",", acceptedValues));
    }
  }

  /**
   * Bulder for {@link EnumValidator} objects.
   */
  final static class Builder {

    /**
     * List of accepted values.
     */
    private Collection<String> acceptedValues = new HashSet<>();

    /**
     * Whether to perform case sensitive or insensitive checks.
     * Defaults to case insensitive.
     */
    private boolean caseSensitive = false;

    /**
     * Constructor.
     */
    Builder() {
    }

    /**
     * Adds a list of values to the collection of accepted values.
     * @param values values to be added
     * @return this builder
     */
    public Builder withValues(final String ... values) {
      acceptedValues.addAll(Arrays.asList(values));
      return this;
    }

    /**
     * Configure the validator to be built as case sensitive or insensitive.
     * @param caseSensitive true for case sensitive, false otherwise.
     * @return this builder
     */
    public Builder caseSensitive(final boolean caseSensitive) {
      this.caseSensitive = caseSensitive;
      return this;
    }

    /**
     * Returns a new instance of {@link EnumValidator}.
     * @return a new instance of {@link EnumValidator}.
     */
    public EnumValidator build() {
      if (acceptedValues.isEmpty()) {
        throw new IllegalArgumentException("List of accepted values for enum validator can't be " +
            "empty");
      }

      return new EnumValidator(acceptedValues, caseSensitive);
    }
  }
}
