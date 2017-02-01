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

import java.util.Arrays;
import java.util.List;

/**
 * A mutually exclusive option object manages a list of options that can'b be present together.
 */
class MutuallyExclusiveOptions extends AbstractOption {

  /**
   * The list of mutually exclusive options.
   */
  private AbstractOption[] options;

  /**
   * Builds a mutually exclusive options object.
   *
   * @param options the list of mutually exclusive options
   */
  MutuallyExclusiveOptions(final IOption... options) {
    this.options = Arrays.copyOf(options, options.length, AbstractOption[].class);
  }

  /**
   * Returns a string containing all the mutually exclusive options (long names) separed by a
   * <code>separator</code>.
   *
   * @param separator the separator
   * @return a string containing all the mutually exclusive options (long names) separed by a
   * <code>separator</code>
   */
  private String getOptionNames(final String separator) {
    String[] params = new String[options.length];
    for (int i = 0; i < params.length; ) {
      params[i] = options[i++].getLongName();
    }

    return String.join(separator, params);
  }

  /**
   * Checks wich (if any) of the embedded option is present and consumes it.
   * If more then one of the embedded option is present, an exception is raised.
   *
   * @param args the command line. The class <b>must remove</b> the consumed option from this
   *             object.
   * @param res  the results
   * @throws ParsingException on error parsing the command line
   */
  protected void doConsume(final List<String> args, final CommandLine res) throws ParsingException {
    // Only one must be present...
    AbstractOption passedInOption = null;

    for (AbstractOption opt : options) {
      if (opt.isPresent(args)) {
        if (passedInOption != null) {
          throw new ParsingException(
              "Incompatible options present: only one of [%s] must be specified",
              getOptionNames(","));
        } else {
          passedInOption = opt;
        }
      }
    }

    // Ok, only at most one is present...
    if (passedInOption != null) {
      passedInOption.consume(args, res);
    } else {
      if (isMandatory()) {
        throw new ParsingException(
            "Mandatory option missing: one of [%s] must be passed",
            getOptionNames(","));
      }
    }
  }

  /**
   * Returns whether at least one of the embedded options is present.
   *
   * @param args received command line
   * @return true or false
   */
  public boolean isPresent(final List<String> args) {
    boolean present = false;

    for (AbstractOption opt : options) {
      present |= opt.isPresent(args);
    }

    return present;
  }

  @Override
  public String toString() {
    String[] params = new String[options.length];
    for (int i = 0; i < params.length; i++) {
      IOption opt = options[i];
      params[i] = opt.toString();
    }

    return String.format("[%s]", String.join(" | ", params));
  }

  /**
   * Returns the list of embedded options.
   *
   * @return the list of embedded options.
   */
  IOption[] getOptions() {
    return options;
  }

  /**
   * Returns the mutually exclusive options group desciption.
   *
   * @return the mutually exclusive options group desciption.
   */
  public String getDescription() {
    return null;
  }

  /**
   * Sets the mutually exclusive options group desciption.
   *
   * @param description the description
   */
  public void setDescription(final String description) {
  }

  /**
   * Not implemented.
   *
   * @return <code>null</code>
   */
  public String getShortName() {
    return null;
  }

  /**
   * Not implemented.
   *
   * @return <code>null</code>
   */
  public String getLongName() {
    return null;
  }
}
