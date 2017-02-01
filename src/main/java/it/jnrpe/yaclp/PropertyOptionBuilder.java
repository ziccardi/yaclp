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

package it.jnrpe.yaclp;

import it.jnrpe.yaclp.validators.IArgumentValidator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builder for property option (options that has a MANDATORY argument in the form key=value).
 * Example: -Dname=value
 */
public final class PropertyOptionBuilder {

  /**
   * A property option is a simple option with a {@link PropertyArgument}.
   * This builder is use to create a properly configured simple option.
   */
  private final SimpleOptionBuilder simpleOptionBuilder;

  /**
   * Separator for key and value.
   */
  private String keyValueSeparator = "=";

  /**
   * List of validators to be used to validate this option value.
   */
  private List<IArgumentValidator> argValidators = new ArrayList<>();

  /**
   * Builds a new builder for property options.
   *
   * @param shortName a property option has only a short name (one letter).
   */
  PropertyOptionBuilder(final String shortName) {
    // We want only short name for property options
    this.simpleOptionBuilder = new SimpleOptionBuilder(shortName, shortName)
        .repeatable(true);
  }

  /**
   * Whether this option is mandatory or not.
   *
   * @param mandatory true or false
   * @return this builder
   */
  public PropertyOptionBuilder mandatory(final boolean mandatory) {
    simpleOptionBuilder.mandatory(mandatory);
    return this;
  }

  /**
   * Description for this option.
   *
   * @param description the description
   * @return this builder
   */
  public PropertyOptionBuilder description(final String description) {
    simpleOptionBuilder.description(description);
    return this;
  }

  /**
   * Call this method many time, passing all the options required by this option.
   *
   * @param opt the option required by this option
   * @return this builder
   */
  public PropertyOptionBuilder requires(final IOption opt) {
    simpleOptionBuilder.requires(opt);
    return this;
  }

  /**
   * Adds a validator to the list of validators used to check the value of this argument.
   *
   * @param argumentValidator the new validator
   * @return this builder
   */
  public PropertyOptionBuilder withArgumentValidator(final IArgumentValidator argumentValidator) {
    this.argValidators.add(argumentValidator);
    return this;
  }

  /**
   * Adds a list of validators to the list of validators used to check the value of this argument.
   *
   * @param argumentValidators the new validators
   * @return this builder
   */
  public PropertyOptionBuilder withArgumentValidator(
      final IArgumentValidator[] argumentValidators) {
    this.argValidators.addAll(Arrays.asList(argumentValidators));
    return this;
  }

  /**
   * Sets the value separator for this option.
   *
   * @param separator the new separator.
   * @return this builder.
   */
  public PropertyOptionBuilder withValueSeparator(final String separator) {
    this.keyValueSeparator = separator;
    return this;
  }

  /**
   * Builds a new Option configured accordingly with the received parameters.
   *
   * @return the newly build option
   */
  public IOption build() {
    final String argName = MessageFormat.format("<property>{0}<value>", keyValueSeparator);

    return simpleOptionBuilder
        .argument(new PropertyArgument(argName, keyValueSeparator))
        .build();
  }
}
