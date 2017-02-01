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
 * Base class for all the command line parsing exceptions.
 */
public class ParsingException extends Exception {

  /**
   * Builds a parsing exception object.
   *
   * @param msg the message
   */
  public ParsingException(final String msg) {
    super(msg);
  }

  /**
   * Builds a parsing exception object.
   *
   * @param pattern the pattern of the message
   * @param values  values to be applied to the pattern to build the message. It uses
   *                {@link String#format(String, Object...)}
   */
  public ParsingException(final String pattern, final Object... values) {
    super(String.format(pattern, values));
  }
}
