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

package it.jnrpe.yaclp.validators;

import it.jnrpe.yaclp.ParsingException;

/**
 * Generic interface for argument validators.
 */
public interface IArgumentValidator {
  /**
   * Validate the argument value.
   *
   * @param value the argument value
   * @throws ParsingException if the value can't be validated
   */
  void validate(final String value) throws ParsingException;
}
