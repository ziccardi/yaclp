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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator implements IArgumentValidator {

    private final Integer minLen;
    private final Integer maxLen;
    private final Pattern regexp;

    private StringValidator(Integer minLen, Integer maxLen, String regexp) {
        this.minLen = minLen;
        this.maxLen = maxLen;

        if (regexp != null) {
            this.regexp = Pattern.compile(regexp);
        } else {
            this.regexp = null;
        }
    }

    public void validate(String value) throws ParsingException {
        if (minLen != null && value.length() < minLen) {
            throw new ParsingException("Value is too short (minimum length: %d)", minLen);
        }

        if (maxLen != null && value.length() > maxLen) {
            throw new ParsingException("Value is too long (maximum length: %d)", minLen);
        }

        if (regexp != null) {
            Matcher matcher = regexp.matcher(value);
            if (!matcher.matches()) {
                throw new ParsingException("Value is not valid according to specified rules");
            }
        }
    }

    public static class Builder {
        private Integer minLen = 0;
        private Integer maxLen = null;
        private String regexp = null;

        Builder() {

        }

        public Builder minLen(Integer minLen) {
            this.minLen = minLen;
            return this;
        }

        public Builder maxLen(Integer maxLen) {
            this.maxLen = maxLen;
            return this;
        }

        public Builder regexp(String regexp) {
            this.regexp = regexp;
            return this;
        }

        public StringValidator build() {
            return new StringValidator(minLen, maxLen, regexp);
        }
    }
}
