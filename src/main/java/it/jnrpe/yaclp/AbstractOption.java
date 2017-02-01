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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class <b>must</b> ne extended by all the option classes.
 */
abstract class AbstractOption implements IOption {

  /**
   * Instructs if this option is mandatory or not.
   */
  private boolean mandatory = false;

  /**
   * Instructs if this option can be repeated or not.
   */
  private boolean repeatable = false;

  /**
   * Contains the list of options required by this option.
   */
  private Set<AbstractOption> requiredOptions = new LinkedHashSet<>();

  /**
   * Contains the list of options that cannot be specified together with this option.
   */
  private Set<AbstractOption> incompatibleOptions = new LinkedHashSet<>();

  /**
   * The argument of this option.
   */
  private Argument optionArgs = null;

  /**
   * Consumes the passed in command line by parsing the option and removing it.
   * Before consuming all sanity checks are performed (checks that required options are present and
   * incompatible options are not present).
   *
   * @param args the command line. It gets changed by the method.
   * @param res  the result of the parsing
   * @throws ParsingException on any error parsing the command line
   */
  final void consume(final List<String> args, final CommandLine res) throws ParsingException {
    sanityCheck(args, res);
    doConsume(args, res);
  }

  /**
   * Consume the option.
   *
   * @param args the command line. The class <b>must remove</b> the consumed option from this
   *             object.
   * @param res  the results
   * @throws ParsingException on any error parsing the command line
   */
  protected abstract void doConsume(final List<String> args, final CommandLine res)
      throws ParsingException;

  /**
   * Adds an option to the list of required options.
   *
   * @param option option to be added
   */
  public void addRequiredOption(final IOption option) {
    requiredOptions.add((AbstractOption) option);
  }

  /**
   * Adds an option to the list of incompatible options.
   *
   * @param option option to be added
   */
  public void addIncompatibleOption(final IOption option) {
    incompatibleOptions.add((AbstractOption) option);
    ((AbstractOption) option).incompatibleOptions.add(this);
  }

  /**
   * Returns the list of required options.
   *
   * @return the list of required options
   */
  Set<AbstractOption> getRequiredOptions() {
    return requiredOptions;
  }

  /**
   * Returns the argument for this option.
   *
   * @return the argument for this option (null if this option does not accepts arguments).
   */
  Argument getArgument() {
    return optionArgs;
  }

  /**
   * Configures the arguments of this option.
   *
   * @param arg the arguments of this option
   */
  void setArgument(final Argument arg) {
    this.optionArgs = arg;
  }

  /**
   * Return whether this option is repeatable or not.
   *
   * @return true or false
   */
  public boolean isRepeatable() {
    return repeatable;
  }

  /**
   * Sets if this option is repeatable or not.
   *
   * @param repeatable true or false
   */
  void setRepeatable(final boolean repeatable) {
    this.repeatable = repeatable;
  }

  /**
   * Return whether this option is mandatory or not.
   *
   * @return true or false
   */
  public boolean isMandatory() {
    return mandatory;
  }

  /**
   * Sets whether this option is mandatory or not.
   *
   * @param mandatory true or false
   */
  void setMandatory(final boolean mandatory) {
    this.mandatory = mandatory;
  }

  /**
   * Return whether this option is present or not.
   *
   * @param args command line to be checked
   * @return true or false
   */
  public abstract boolean isPresent(final List<String> args);

  /**
   * Performs all the sanity check to safely consume this option.
   *
   * @param args the command line
   * @param res  the results
   * @throws ParsingException if sanity checks fails
   */
  private void sanityCheck(final List<String> args, final CommandLine res) throws ParsingException {
    // Check if the mandatory option is present
    if (!isPresent(args)) {
      if (isMandatory() && !res.hasOption(getShortName())) {
        throw new ParsingException("Mandatory option [%s] is missing", getLongName());
      }
      return;
    }

    // Check if a non repeatable option is repeated
    if (res.hasOption(getShortName()) && !isRepeatable()) {
      throw new ParsingException("Option [%s] can be specified only one time", getLongName());
    }

    // Check if incompatible options are presents
    for (AbstractOption option : incompatibleOptions) {
      if (res.hasOption(option.getShortName()) || option.isPresent(args)) {
        throw new ParsingException(
            "Option [%s] can not be specified together with option [%s]",
            getLongName(),
            option.getLongName());
      }
    }

    // Check required option. If they are found, consume them
    for (AbstractOption requiredOption : getRequiredOptions()) {
      if (requiredOption.isPresent(args)) {
        requiredOption.consume(args, res);
      } else {
        if (res.hasOption(requiredOption.getShortName())) {
          continue;
        }
        throw new ParsingException("%s requires [%s]", getLongName(), requiredOption.getLongName());
      }
    }
  }
}
