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
 * Builder for mutually exclusive options
 */
public class MutuallyExclusiveOptionBuilder {
    private List<IOption> options = new ArrayList<IOption>();
    private boolean mandatory = false;
    private String description = "";

    MutuallyExclusiveOptionBuilder() {
    }

    /**
     * Call this method many time to add all the options than can't be specified together.
     * @param option the option
     * @return the builder
     */
    public MutuallyExclusiveOptionBuilder withOption(final IOption option) {
        this.options.add(option);
        return this;
    }

    /**
     * If mandatory, one of the option MUST be present in the command line
     * @param mandatory whether the option is mandatory or not
     * @return the builder
     */
    public MutuallyExclusiveOptionBuilder mandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    /**
     * Description for this mutually exlusive option group.
     * @param description the description
     * @return the builder
     */
    public MutuallyExclusiveOptionBuilder description(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Builds the option as configured.
     * @return the newly build option
     */
    public IOption build() {
        MutuallyExclusiveOptions opt = new MutuallyExclusiveOptions(options.toArray(new IOption[options.size()]));
        opt.setMandatory(mandatory);
        opt.setDescription(description);

        return opt;
    }
}
