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
package it.jnrpe.yaclp;

import it.jnrpe.yaclp.validators.IArgumentValidator;

import java.util.regex.Pattern;

/**
 * Represent an argument in the form 'key=value'
 */
class PropertyArgument extends Argument {

    private final String keyValueSeparator;
    private final IArgumentValidator[] validators;

    PropertyArgument(final String name, String keyValueSeparator, IArgumentValidator... validators) {
        // Value must be extracted from the key=value string. We will manage validation locally.
        super(name, true, new IArgumentValidator[0]);
        this.validators = validators;
        this.keyValueSeparator = keyValueSeparator;
    }

    @Override
    protected void saveValue(CommandLine res, IOption option, String value) throws ParsingException {

        int pos = value.indexOf(keyValueSeparator);

        if (pos < 1 || pos >= (value.length() - 1)) {
            throw new ParsingException("Argument <%s> for option <%s> must be in format key%svalue", getName(), option.getLongName(), keyValueSeparator);
        }

        // Value is in the correct format
        String[] keyValuePair = value.split(Pattern.quote(keyValueSeparator), 2);

        // Validate value
        for (IArgumentValidator validator : validators) {
            validator.validate(keyValuePair[1]);
        }

        res.addProperty(option, keyValuePair[0], keyValuePair[1]);
    }
}