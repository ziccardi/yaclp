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

import java.util.ArrayList;
import java.util.List;

/**
 * Builds an argument parser.
 */
public class ArgumentBuilder {
    private final String name;
    private boolean mandatory = true;
    private List<IArgumentValidator> validators = new ArrayList<IArgumentValidator>();

    private ArgumentBuilder(final String name) {
        this.name = name;
    }

    /**
     * Returns an instance of the builder for an argument with the given name.
     * @param name the name of the argument
     * @return the builder
     */
    public static ArgumentBuilder forArgument(final String name) {
        return new ArgumentBuilder(name);
    }

    /**
     * Whether the argument is mandatory or not
     * @param mandatory Whether the argument is mandatory or not
     * @return the builder
     */
    public ArgumentBuilder mandatory(final boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    /**
     * Adds a validator to the list of validators for this argument.
     * You can call this method as many times as needed to add validators: they will be
     * executed in the order they have been inserted.
     * @param validator the validator to be added
     * @return the builder
     */
    public ArgumentBuilder withValidator(IArgumentValidator validator) {
        this.validators.add(validator);
        return this;
    }

    /**
     * Builds the argument with the provided options.
     * @return the newly build argument
     */
    public IArgument build() {
        return new Argument(name, mandatory, validators.toArray(new IArgumentValidator[validators.size()]));
    }
}
