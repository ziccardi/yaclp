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
 * Generic option interface.
 */
public interface IOption {
  /**
   * Returns the short name for this option.
   *
   * @return the short name for this option
   */
  String getShortName();

  /**
   * Returns the long name for this option.
   *
   * @return Returns the long name for this option.
   */
  String getLongName();

  /**
   * Checks if this option is mandatory.
   *
   * @return true or false
   */
  boolean isMandatory();

  /**
   * Returns the option description.
   *
   * @return the option description
   */
  String getDescription();

  /**
   * Adds an option to the list of required options.
   *
   * @param option option to be added
   */
  void addRequiredOption(IOption option);

  /**
   * Adds an option to the list of incompatible options.
   *
   * @param option option to be added
   */
  void addIncompatibleOption(IOption option);
}
