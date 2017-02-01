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

import java.util.List;

/**
 * Consumes the command line by removing the arguments of the current option.
 */
class Argument implements IArgument {

  /**
   * Whether this argument is mandatory or not.
   */
  private final boolean mandatory;

  /**
   * The name of this argument.
   */
  private final String name;

  /**
   * The list of validatory for this argument value.
   */
  private final IArgumentValidator[] validators;

  /**
   * Create a new argument object.
   *
   * @param name       the name of the argument
   * @param mandatory  <code>true</code> if the argument is mandatory
   * @param validators the list of validators to be used to validate this argument value
   */
  Argument(final String name, final boolean mandatory, final IArgumentValidator... validators) {
    this.mandatory = mandatory;
    this.name = name;
    this.validators = validators;
  }

  /**
   * Consumes the argument. After the execution of the method, the argument won't be inside args
   * anymore.
   *
   * @param option Owner option for this argument
   * @param args   command line to be parsed
   * @param pos    position where this argument value should be found in the command line
   * @param res    result
   * @throws ParsingException on error parsing the argument or ig the argument is not present
   */
  public final void consume(final IOption option, final List<String> args,
                            final int pos, final CommandLine res) throws ParsingException {
    if (pos < args.size()) {
      // An argument must not start with '-'
      if (!args.get(pos).startsWith("-")) {
        String value = args.remove(pos);

        for (IArgumentValidator validator : validators) {
          validator.validate(value);
        }

        saveValue(res, option, value);
      } else {
        if (mandatory) {
          throw new ParsingException(
              "Mandatory argument <%s> for option <%s> is not present",
              getName(),
              option.getLongName());
        }
      }
    } else {
      if (mandatory) {
        throw new ParsingException(
            "Mandatory argument <%s> for option <%s> is not present",
            getName(),
            option.getLongName());
      }
    }
  }

  /**
   * Saves the value of the argument inside res.
   *
   * @param res    the command line parsing result
   * @param option option owning the argument
   * @param value  value to be saved
   * @throws ParsingException on error saving the argument value
   */
  protected void saveValue(final CommandLine res,
                           final IOption option,
                           final String value) throws ParsingException {
    res.addValue(option, value);
  }

  /**
   * Returns the name of the argument.
   *
   * @return the name of the argument
   */
  public String getName() {
    return name;
  }
}
