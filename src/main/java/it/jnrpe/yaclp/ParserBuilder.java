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
import java.util.Arrays;
import java.util.List;

/**
 * Builder for command line parser.
 */
public final class ParserBuilder {

  /**
   * Constructor.
   */
  private ParserBuilder() {
  }

  /**
   * Returns a builder for an option based parser. An option based parser can't have 'commands'.
   *
   * @return a builder for an option based parser. An option based parser can't have 'commands'.
   */
  public static OptionsBasedParserBuilder forOptionsBasedCli() {
    return new OptionsBasedParserBuilder();
  }

  /**
   * Returns a builder for an command based parser.
   *
   * @return a builder for an command based parser.
   */
  public static CommandBasedParserBuilder forCommandsBasedCli() {
    return new CommandBasedParserBuilder();
  }

  /**
   * Command based parser builder.
   */
  public static final class CommandBasedParserBuilder {

    /**
     * List of supported commands.
     */
    private List<Command> commands = new ArrayList<>();

    /**
     * Constructor.
     */
    private CommandBasedParserBuilder() {
    }

    /**
     * Adds a list of supported commands.
     *
     * @param commands commands to be added
     */
    private void addCommands(final Command... commands) {
      if (commands.length == 1) {
        this.commands.add(commands[0]);
      } else {
        this.commands.addAll(Arrays.asList(commands));
      }
    }

    /**
     * Adds a list of supported commands.
     *
     * @param commands commands to be added
     * @return this builder
     */
    public CommandBasedParserBuilder withCommands(final Command... commands) {
      addCommands(commands);
      return this;
    }

    /**
     * Builds the parser.
     *
     * @return return the newly built parser
     */
    public Parser build() {
      Parser parser = new Parser();
      commands.forEach(parser::addCommand);
      return parser;
    }
  }

  /**
   * Option based parser builder.
   */
  public static final class OptionsBasedParserBuilder {

    /**
     * List of supported options.
     */
    private List<IOption> options = new ArrayList<>();

    /**
     * Constructor.
     */
    private OptionsBasedParserBuilder() {
    }

    /**
     * Adds a list of options to the list of supported options.
     *
     * @param options options to be added
     */
    private void addOptions(final IOption... options) {
      if (options.length == 1) {
        this.options.add(options[0]);
      } else {
        this.options.addAll(Arrays.asList(options));
      }
    }

    /**
     * Adds a list of options to the list of supported options.
     *
     * @param options options to be added
     * @return this builder
     */
    public OptionsBasedParserBuilder withOption(final IOption... options) {
      addOptions(options);
      return this;
    }

    /**
     * Builds the parser.
     *
     * @return the newly built parser
     */
    public Parser build() {
      Parser parser = new Parser();
      options.forEach(parser::addOption);
      return parser;
    }
  }
}
