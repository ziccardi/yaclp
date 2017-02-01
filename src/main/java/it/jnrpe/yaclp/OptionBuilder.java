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

/**
 * Entry point for option builders.
 */
public final class OptionBuilder {

  /**
   * Constructor.
   */
  private OptionBuilder() {
  }

  /**
   * Returns an instance of {@link SimpleOptionBuilder}.
   *
   * @param shortName option short name
   * @param longName  option long name
   * @return an instance of {@link SimpleOptionBuilder}.
   */
  public static SimpleOptionBuilder forOption(final String shortName, final String longName) {
    return new SimpleOptionBuilder(shortName, longName);
  }

  /**
   * Returns an instance of {@link SimpleOptionBuilder}.
   * This method should be used when an option has only one name (short or long).
   *
   * @param optionName option short name
   * @return an instance of {@link SimpleOptionBuilder}.
   */
  public static SimpleOptionBuilder forOption(final String optionName) {
    return new SimpleOptionBuilder(optionName, optionName);
  }

  /**
   * Creates a {@link PropertyOptionBuilder} object, used to create options like -Dname=value.
   *
   * @param shortName property options has only short name
   * @return an instance of {@link PropertyOptionBuilder}
   */
  public static PropertyOptionBuilder forPropertyOption(final String shortName) {
    return new PropertyOptionBuilder(shortName);
  }

  /**
   * Create a new instance of a {@link MutuallyExclusiveOptionBuilder}.
   *
   * @return a new instance of a {@link MutuallyExclusiveOptionBuilder}.
   */
  public static MutuallyExclusiveOptionBuilder forMutuallyExclusiveOption() {
    return new MutuallyExclusiveOptionBuilder();
  }
}
