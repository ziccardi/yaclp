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

/**
 * Validators builder entry point.
 */
public class ValidatorBuilder {

  /**
   * Constructor.
   */
  private ValidatorBuilder() {
  }

  /**
   * Returns a builder for {@link IntegerValidator}s objects.
   *
   * @return a builder for {@link IntegerValidator}s objects.
   */
  public static IntegerValidator.Builder forInteger() {
    return new IntegerValidator.Builder();
  }

  /**
   * Returns a builder for {@link StringValidator}s objects.
   *
   * @return a builder for {@link StringValidator}s objects.
   */
  public static StringValidator.Builder forString() {
    return new StringValidator.Builder();
  }

  /**
   * Returns a builder for {@link FileValidator}s objects.
   *
   * @return a builder for {@link FileValidator}s objects.
   */
  public static FileValidator.Builder forFile() {
    return new FileValidator.Builder();
  }
}
