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

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for simple options.
 */
public final class SimpleOptionBuilder {

  /**
   * Option short name.
   */
  private final String shortName;

  /**
   * Option long name.
   */
  private final String longName;

  /**
   * List of required options.
   */
  private final List<AbstractOption> requiredOptions = new ArrayList<>();

  /**
   * List of incompatible options.
   */
  private final List<AbstractOption> incompatibleOptions = new ArrayList<>();

  /**
   * Whether this option can be specified more than one time or not.
   */
  private boolean repeatable = false;

  /**
   * Whether this option is mandatory or not.
   */
  private boolean mandatory = false;

  /**
   * This option description.
   */
  private String description = "";

  /**
   * The argument for this option.
   */
  private IArgument argument = null;

  /**
   * Constructor.
   *
   * @param shortName the short name
   * @param longName  the long name
   */
  SimpleOptionBuilder(final String shortName, final String longName) {
    this.shortName = shortName;
    this.longName = longName;
  }

  /**
   * If true, than this option can be specified more than one time.
   *
   * @param repeatable true or false
   * @return this builder
   */
  public SimpleOptionBuilder repeatable(final boolean repeatable) {
    this.repeatable = repeatable;
    return this;
  }

  /**
   * Whether this option is mandatory or not.
   *
   * @param mandatory true or false
   * @return this builder
   */
  public SimpleOptionBuilder mandatory(final boolean mandatory) {
    this.mandatory = mandatory;
    return this;
  }

  /**
   * Description for this option.
   *
   * @param description the description
   * @return this builder
   */
  public SimpleOptionBuilder description(final String description) {
    this.description = description;
    return this;
  }

  /**
   * Call this method many time, passing all the options required by this option.
   *
   * @param opts the options required by this option
   * @return this builder
   */
  public SimpleOptionBuilder requires(final IOption... opts) {
    for (IOption opt : opts) {
      this.requiredOptions.add((AbstractOption) opt);
    }

    return this;
  }

  /**
   * Adds a list of options that are incompatible with this option.
   *
   * @param opts list of incompatible options
   * @return this builder
   */
  public SimpleOptionBuilder incompatibleWith(final IOption... opts) {
    for (IOption opt : opts) {
      this.incompatibleOptions.add((AbstractOption) opt);
    }

    return this;
  }

  /**
   * Call this if the option accept argument(s).
   * Use the {@link ArgumentBuilder} to get an instance of {@link IArgument}
   *
   * @param arg teh argument
   * @return this builder
   */
  public SimpleOptionBuilder argument(final IArgument arg) {
    this.argument = arg;
    return this;
  }

  /**
   * Builds a new Option configured accordingly with the received parameters.
   *
   * @return the newly build option
   */
  public IOption build() {
    Option option = new Option(shortName, longName);
    option.setRepeatable(repeatable);
    option.setMandatory(mandatory);
    option.setDescription(description);
    option.setArgument((Argument) argument);

    requiredOptions.forEach(option::addRequiredOption);
    incompatibleOptions.forEach(option::addIncompatibleOption);
    return option;
  }
}
