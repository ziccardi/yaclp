/*******************************************************************************
 * Copyright (c) 2017 Massimiliano Ziccardi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package it.jnrpe.yaclp.validators;

import it.jnrpe.yaclp.ParsingException;

public class IntegerValidator implements IArgumentValidator {
    private final Integer min;
    private final Integer max;
    private final int radix;

    private IntegerValidator(Integer min, Integer max, final int radix) {
        this.min = min;
        this.max = max;
        this.radix = radix;
    }

    public void validate(final String value) throws ParsingException {
        try {
            Integer val = Integer.parseInt(value, radix);

            if (min != null && val < min) {
                    throw new ParsingException("Value must be greater than %d (current value: %s)", min, value);
            }

            if (max != null && val > max) {
                throw new ParsingException("Value must be smaller than %d (current value: %s)", max, value);
            }
        } catch (NumberFormatException nfe) {
            throw new ParsingException("Value [%s] is not a correct number with radix [%d]", value, radix);
        }
    }

    public static class Builder {
        private Integer min = null;
        private Integer max = null;
        private int radix = 10;

        Builder() {
        }

        public Builder min(Integer min) {
            this.min = min;
            return this;
        }

        public Builder max(Integer max) {
            this.max = max;
            return this;
        }

        public Builder radix(int radix) {
            this.radix = radix;
            return this;
        }

        public IntegerValidator build() {
            return new IntegerValidator(min, max, radix);
        }
    }
}
