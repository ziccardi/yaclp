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

import java.util.ArrayList;
import java.util.List;

/**
 * Builds an argument parser.
 */
public final class ArgumentBuilder {

  /**
   * The name of the argument to be built.
   */
  private final String name;

  /**
   * Whether the argument is mandatory or not. Defaults to true.
   */
  private boolean mandatory = true;

  /**
   * List of the argument validator to be used to validate this argument.
   */
  private List<IArgumentValidator> validators = new ArrayList<>();

  /**
   * Minimum number of repetitions.
   */
  private int minRepetitions = 0;

  /**
   * Maximum number of repetitions.
   */
  private int maxRepetitions = 1;

  /**
   * Constructor.
   *
   * @param name the name of the argument to be built.
   */
  private ArgumentBuilder(final String name) {
    this.name = name;
  }

  /**
   * Returns an instance of the builder for an argument with the given name.
   *
   * @param name the name of the argument
   * @return the builder
   */
  public static ArgumentBuilder forArgument(final String name) {
    return new ArgumentBuilder(name);
  }

  /**
   * Whether the argument is mandatory or not.
   *
   * @param mandatory Whether the argument is mandatory or not
   * @return the builder
   */
  public ArgumentBuilder mandatory(final boolean mandatory) {
    this.mandatory = mandatory;
    return this;
  }

  /**
   * Adds a validator to the list of validators for this argument.
   * You can call this method as many times as needed to add validators: they will be
   * executed in the order they have been inserted.
   *
   * @param validator the validator to be added
   * @return the builder
   */
  public ArgumentBuilder withValidator(final IArgumentValidator validator) {
    this.validators.add(validator);
    return this;
  }

  /**
   * Sets the minimum repetitions for this argument. If it is optional, this is enforced only
   * it at least one argument is present.
   * @param minRepetitions Minimum number of repetitions
   * @return this builder
   */
  public ArgumentBuilder withMinRepetitions(final int minRepetitions) {
    this.minRepetitions = minRepetitions;
    return this;
  }

  /**
   * Sets the maximum repetitions for this argument.
   * Defaults to 1.
   * @param maxRepetitions maximum number of repetitions
   * @return this builder
   */
  public ArgumentBuilder withMaxRepetitions(final int maxRepetitions) {
    this.maxRepetitions = maxRepetitions;
    return this;
  }

  /**
   * Builds the argument with the provided options.
   *
   * @return the newly build argument
   */
  public IArgument build() {
    return new Argument(
        name, mandatory, minRepetitions, maxRepetitions,
        validators.toArray(new IArgumentValidator[validators.size()]));
  }
}
