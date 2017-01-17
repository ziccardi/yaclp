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

import java.util.List;

/**
 * Consumes the command line by removing the arguments of the current option/
 */
class Argument implements IArgument {
    private final boolean mandatory;
    private final String name;

    private final IArgumentValidator[] validators;

    /**
     * Create a new argument object
     * @param mandatory <code>true</code> if the argument is mandatory
     */
    Argument(final String name, final boolean mandatory, IArgumentValidator... validators) {
        this.mandatory = mandatory;
        this.name = name;
        this.validators = validators;
    }

    public final void consume(IOption option, List<String> args, int pos, CommandLine res) throws ParsingException {
        if (pos < args.size()) {
            // An argument must not start with '-'
            if (!args.get(pos).startsWith("-")) {
                String value = args.remove(pos);

                for (IArgumentValidator validator : validators) {
                    validator.validate(value);
                }

                saveValue(res, option, value);
            } else {
                if (mandatory) {
                    throw new ParsingException("Mandatory argument <%s> for option <%s> is not present", getName(), option.getLongName());
                }
            }
        } else {
            if (mandatory) {
                throw new ParsingException("Mandatory argument <%s> for option <%s> is not present", getName(), option.getLongName());
            }
        }
    }

    protected void saveValue(final CommandLine res, IOption option, final String value) throws ParsingException {
        res.addValue(option, value);
    }

    public String getName() {
        return name;
    }
}
