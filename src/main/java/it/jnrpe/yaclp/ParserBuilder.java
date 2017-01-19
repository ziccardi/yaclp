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
import java.util.Arrays;
import java.util.List;

/**
 * Builder for command line parser
 */
public class ParserBuilder {
    private List<IOption> options = new ArrayList<IOption>();

    private ParserBuilder() {
    }

    /**
     * Creates a new builder
     * @return the new builder
     */
    public static ParserBuilder forNewParser() {
        return new ParserBuilder();
    }

    /**
     * Adds options to the parser configuration
     * @param options the options to be added
     * @return this builder
     */
    public ParserBuilder withOption(final IOption... options) {
        if (options.length == 1) {
            this.options.add(options[0]);
        } else {
            this.options.addAll(Arrays.asList(options));
        }
        return this;
    }

    /**
     * Builds a new Parser instance
     * @return the newly build parser instance
     */
    public Parser build() {
        Parser p = new Parser();
        options.forEach(p::addOption);
        return p;
    }
}
