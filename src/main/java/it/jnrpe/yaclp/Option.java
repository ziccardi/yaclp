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

import java.util.List;

/**
 * Concrete option class.
 */
class Option extends AbstractOption {

  /**
   * The option short name.
   */
  private final String shortName;

  /**
   * The option long nale.
   */
  private final String longName;

  /**
   * The option description.
   */
  private String description = "";

  /**
   * Builds a new option object.
   *
   * @param shortName the option short name
   * @param longName  the option long name
   */
  Option(final String shortName, final String longName) {
    this.shortName = shortName;
    this.longName = longName;
  }

  /**
   * Checks if the option is present in the command line.
   *
   * @param args command line to be checked
   * @return true or false
   */
  public boolean isPresent(final List<String> args) {
    return args.contains(shortName) || args.contains(longName);
  }

  /**
   * Consumes the option. After the method executes, the option won't be present in args anymore.
   *
   * @param args the command line. The class <b>must remove</b> the consumed option from this
   *             object.
   * @param res  the results
   * @throws ParsingException on any error parsing the command line
   */
  protected final void doConsume(final List<String> args, final CommandLine res)
      throws ParsingException {
    for (int i = 0; i < args.size(); i++) {
      if (args.get(i).equals(shortName) || args.get(i).equals(longName)) {
        // consume and manage it
        args.remove(i);
        res.addValue(this, null);
        if (getArgument() != null) {
          getArgument().consume(this, args, i, res);
        }
        // consume again if repeated
        consume(args, res);
      }
    }
  }

  /**
   * Returns the option short name.
   *
   * @return the option short name.
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Returns the option long name.
   *
   * @return the option long name.
   */
  public String getLongName() {
    return longName;
  }

  /**
   * Returns the option description.
   *
   * @return the optiond escription
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the option description.
   *
   * @param description the new description
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    if (!longName.equals(shortName)) {
      if (getArgument() == null) {
        return String.format("%s (%s)", longName, shortName);
      } else {
        return String.format("%s (%s) <%s>", longName, shortName, getArgument().getName());
      }
    } else {
      if (getArgument() == null) {
        return String.format("%s", shortName);
      } else {
        return String.format("%s <%s>", shortName, getArgument().getName());
      }
    }
  }
}
