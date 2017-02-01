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
 * Builder for commands.
 */
public final class CommandBuilder {

  /**
   * The new command short name.
   */
  private final String shortName;

  /**
   * The new command long name.
   */
  private final String longName;

  /**
   * The new command description.
   */
  private String description;

  /**
   * The list of options accepted by this command.
   */
  private List<IOption> options = new ArrayList<>();

  /**
   * Instantiate this command builder.
   *
   * @param shortName the new command short name
   * @param longName  the new command long name
   */
  private CommandBuilder(final String shortName, final String longName) {
    this.shortName = shortName;
    this.longName = longName;
  }

  /**
   * Returns an instance of this builder.
   *
   * @param shortName the new command short name
   * @param longName  the new command long name
   * @return an instance of this builder
   */
  public static CommandBuilder forNewCommand(final String shortName, final String longName) {
    return new CommandBuilder(shortName, longName);
  }

  /**
   * Returns an instance of this builder.
   *
   * @param name the new command name
   * @return an instance of this builder
   */
  public static CommandBuilder forNewCommand(final String name) {
    return new CommandBuilder(name, name);
  }

  /**
   * Adds options to the command configuration.
   *
   * @param options the options to be added
   * @return this builder
   */
  public CommandBuilder withOption(final IOption... options) {
    if (options.length == 1) {
      this.options.add(options[0]);
    } else {
      this.options.addAll(Arrays.asList(options));
    }
    return this;
  }

  /**
   * Sets the description for the new command.
   *
   * @param description the new command description
   * @return this
   */
  public CommandBuilder withDescription(final String description) {
    this.description = description;
    return this;
  }

  /**
   * Builds a new Command instance.
   *
   * @return the newly build command instance
   */
  public Command build() {
    Parser parser = new Parser();
    options.forEach(parser::addOption);

    return new Command(shortName, longName, description, parser);
  }
}
