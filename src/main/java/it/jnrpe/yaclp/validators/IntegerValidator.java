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

import it.jnrpe.yaclp.IArgument;
import it.jnrpe.yaclp.IOption;
import it.jnrpe.yaclp.ParsingException;

/**
 * Validator for integer arguments.
 * It can validate:
 * <ul>
 * <li>Minimum value</li>
 * <li>Maximum value</li>
 * </ul>
 */
public class IntegerValidator implements IArgumentValidator {

  /**
   * Minimum value or null.
   */
  private final Integer min;

  /**
   * Maximum value or null.
   */
  private final Integer max;

  /**
   * Radix to be used to interpret the number string.
   */
  private final int radix;

  /**
   * Constructor.
   *
   * @param min   minimum value or null
   * @param max   maximum value or null
   * @param radix radix used to parse the number string
   */
  private IntegerValidator(final Integer min, final Integer max, final int radix) {
    this.min = min;
    this.max = max;
    this.radix = radix;
  }

  /**
   * Validates the argument value according to the configured constraints.
   *
   * @param value the argument value
   * @throws ParsingException if validation fails
   */
  public void validate(final IOption option, final IArgument argument, final String value) throws
      ParsingException {
    try {
      Integer val = Integer.parseInt(value, radix);

      if (min != null && val < min) {
        throw new ParsingException("Value must be greater than %d (current value: %s)", min, value);
      }

      if (max != null && val > max) {
        throw new ParsingException("Value must be smaller than %d (current value: %s)", max, value);
      }
    } catch (NumberFormatException nfe) {
      throw new ParsingException(
          "Value [%s] is not a correct number with radix [%d]", value, radix);
    }
  }

  /**
   * Builder for {@link IntegerValidator} objects.
   */
  public static class Builder {

    /**
     * Minimum value. Default: null.
     */
    private Integer min = null;

    /**
     * Maximum value. Default: null.
     */
    private Integer max = null;

    /**
     * Radix. Default: 10.
     */
    private int radix = 10;

    /**
     * Constructor.
     */
    Builder() {
    }

    /**
     * Sets the minimum value (inclusive).
     *
     * @param min minimum value
     * @return this builder
     */
    public Builder min(final Integer min) {
      this.min = min;
      return this;
    }

    /**
     * Sets the maximum value (inclusive).
     *
     * @param max minimum value
     * @return this builder
     */
    public Builder max(final Integer max) {
      this.max = max;
      return this;
    }

    /**
     * Sets the radix used to parse the number string.
     *
     * @param radix the radix
     * @return this builder
     */
    public Builder radix(final int radix) {
      this.radix = radix;
      return this;
    }

    /**
     * Builds the {@link IntegerValidator} object.
     *
     * @return an {@link IntegerValidator} instance configured with the given parameters.
     */
    public IntegerValidator build() {
      return new IntegerValidator(min, max, radix);
    }
  }
}
