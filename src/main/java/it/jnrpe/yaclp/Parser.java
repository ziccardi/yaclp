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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Parse a command line.
 */
public class Parser {

  /**
   * List of commands managed by this parser.
   */
  private final List<Command> commands = new ArrayList<>();
  /**
   * List of options managed by this parser.
   */
  private List<IOption> options = new ArrayList<>();

  /**
   * Constructor.
   */
  Parser() {
  }

  /**
   * Adds an option to the list of supported options.
   * It can be a simple option ({@link Option}) or a mutually exclusive option
   * ({@link MutuallyExclusiveOptions})
   *
   * @param option the option to be added to the parser
   */
  void addOption(final IOption option) {
    this.options.add(option);
  }

  /**
   * Adds a new command to this parser.
   *
   * @param command the command to be added
   */
  void addCommand(final Command command) {
    this.commands.add(command);
  }

  /**
   * Returns the list of options managed by this parser.
   *
   * @return the list of options managed by this parser.
   */
  List<IOption> getOptions() {
    return Collections.unmodifiableList(options);
  }

  /**
   * Returns the list of commands managed by this parser.
   *
   * @return the list of commands managed by this parser.
   */
  List<Command> getCommands() {
    return Collections.unmodifiableList(commands);
  }

  /**
   * Parses the command line and returns a {@link CommandLine} object if the parsing succeeds.
   *
   * @param args the command line to be parsed
   * @return the parsed command line
   * @throws ParsingException if the command line is not valid according to the parser configuration
   */
  public CommandLine parse(final String[] args) throws ParsingException {
    CommandLine res = new CommandLine();
    return parse(args, res);
  }

  /**
   * Parses the arguments and store the value of the options inside the passed it
   * {@link CommandLine} object.
   *
   * @param args the command line
   * @param cl   the result object
   * @return the result object
   * @throws ParsingException on any error parsing the command line
   */
  CommandLine parse(final String[] args, final CommandLine cl) throws ParsingException {
    List<String> argsList = normalize(args);

    if (commands != null && !commands.isEmpty()) {
      boolean commandFound = false;

      for (Command command : commands) {
        if (command.isPresent(argsList)) {
          commandFound = true;
          command.consume(argsList, cl);
        }
      }

      if (!commandFound) {
        // FIXME: improve error message
        List<String> commandNames = new ArrayList<>(commands.size());
        commands.forEach(command -> commandNames.add(command.getLongName()));

        throw new ParsingException(
            "At least one of [%s] must be specified", String.join(",", commandNames));
      }
    }

    for (IOption opt : options) {
      ((AbstractOption) opt).consume(argsList, cl);
    }

    if (!argsList.isEmpty()) {
      throw new ParsingException("Unexpected tokens: " + argsList);
    }

    return cl;
  }

  /**
   * Separates options from arguments when they are attached (for example
   * -Dkey=value becomes -D key=value), so that
   * they can be managed as all the other options.
   *
   * @param args the command line
   * @return a List contained the command line (options and arguments)
   */
  private List<String> normalize(final String[] args) {
    List<String> res = new LinkedList<>();

    for (String arg : args) {

      if (arg.length() == 1
          || arg.startsWith("--")
          || (arg.startsWith("-") && arg.length() == 2) || !arg.startsWith("-")) {
        res.add(arg);
        continue;
      }

      res.add("-" + arg.substring(1, 2));
      res.add(arg.substring(2));
    }

    return res;
  }
}
