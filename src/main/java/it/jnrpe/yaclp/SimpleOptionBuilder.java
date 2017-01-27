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

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for simple options.
 */
public class SimpleOptionBuilder {
    private final String shortName;
    private final String longName;

    private final List<AbstractOption> requiredOptions = new ArrayList<>();

    private boolean multiplicity = false;
    private boolean mandatory = false;
    private String description = "";

    private IArgument argument = null;

    SimpleOptionBuilder(final String shortName, final String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    /**
     * If true, than this option can be specified more than one time.
     * @param multiplicity true or false
     * @return this builder
     */
    public SimpleOptionBuilder repeatable(boolean multiplicity) {
        this.multiplicity = multiplicity;
        return this;
    }

    /**
     * Whether this option is mandatory or not
     * @param mandatory true or false
     * @return this builder
     */
    public SimpleOptionBuilder mandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    /**
     * Description for this option
     * @param description the description
     * @return this builder
     */
    public SimpleOptionBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Call this method many time, passing all the options required by this option
     * @param opt the option required by this option
     * @return this builder
     */
    public SimpleOptionBuilder requires(IOption opt) {
        this.requiredOptions.add((AbstractOption) opt);
        return this;
    }

    /**
     * Call this if the option accept argument(s).
     * Use the {@link ArgumentBuilder} to get an instance of {@link IArgument}
     * @param arg teh argument
     * @return this builder
     */
    public SimpleOptionBuilder argument(IArgument arg) {
        this.argument = arg;
        return this;
    }

    /**
     * Builds a new Option configured accordingly with the received parameters.
     * @return the newly build option
     */
    public IOption build() {
        Option option = new Option(shortName, longName);
        option.setRepeatable(multiplicity);
        option.setMandatory(mandatory);
        option.setDescription(description);
        option.setArgument((Argument)argument);

        requiredOptions.forEach(option::addRequiredOption);
        return option;
    }
}
