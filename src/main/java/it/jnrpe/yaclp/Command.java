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
 * This object represent a command (for example: "checkout" in "git checkout").
 */
public final class Command {

  /**
   * Short name for the command.
   */
  private final String shortName;

  /**
   * Long name for the command.
   */
  private final String longName;

  /**
   * Description of the command.
   */
  private final String description;

  /**
   * Sub-parser used to parse the command 'command line'.
   */
  private final Parser parser;

  /**
   * Builds a new command object.
   *
   * @param shortName   the command short name
   * @param longName    the command long name
   * @param description the command description
   * @param parser      the sub-parser to be used to validate the command options
   */
  Command(final String shortName,
          final String longName,
          final String description,
          final Parser parser) {
    this.shortName = shortName;
    this.longName = longName;
    this.description = description;
    this.parser = parser;
  }

  /**
   * Returns the description for the command.
   *
   * @return the description for the command
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the long name for the command.
   *
   * @return the long name for the command
   */
  public String getLongName() {
    return longName;
  }

  /**
   * Returns the short name for the command.
   *
   * @return the short name for the command
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Return whether the command is present or not.
   *
   * @param args the received command line
   * @return true or false
   */
  boolean isPresent(final List<String> args) {
    if (args.isEmpty()) {
      return false;
    }

    String arg = args.get(0);
    return arg.equals(shortName) || arg.equals(longName);
  }

  /**
   * Consumes the command. When this method is executed, the command and its command line are
   * removed from args.
   *
   * @param args the command line to be parsed
   * @param res  the results
   * @throws ParsingException on any error parsing or validating the command line
   */
  void consume(final List<String> args, final CommandLine res) throws ParsingException {
    if (isPresent(args)) {
      res.setCommand(args.remove(0));
      parser.parse(args.toArray(new String[args.size()]), res);
      args.clear();
    }
  }

  /**
   * Returns the command sub parser.
   *
   * @return the command sub parser
   */
  Parser getParser() {
    return parser;
  }

  /**
   * Returns a string representation fo the command.
   *
   * @return a string representation fo the command
   */
  @Override
  public String toString() {
    if (shortName.equals(longName)) {
      return shortName;
    } else {
      return String.format("%s (%s)", longName, shortName);
    }
  }
}
