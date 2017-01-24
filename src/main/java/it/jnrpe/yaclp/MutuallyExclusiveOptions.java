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

import java.util.Arrays;
import java.util.List;

class MutuallyExclusiveOptions extends AbstractOption {

    private AbstractOption[] options;

    MutuallyExclusiveOptions (IOption... options) {
        this.options = Arrays.copyOf(options, options.length, AbstractOption[].class);
    }

    private String getOptionNames(String separator) {
        String[] params = new String[options.length];
        for (int i = 0; i < params.length; ) {
            params[i] = options[i++].getLongName();
        }

        return String.join(separator, params);
    }

    public void consume(List<String> args, CommandLine res) throws ParsingException{
        // Only one must be present...
        AbstractOption passedInOption = null;

        for (AbstractOption opt : options) {
            if (opt.isPresent(args)) {
                if (passedInOption != null) {
                    throw new ParsingException("Incompatible options present: only one of [%s] must be specified", getOptionNames(","));
                } else {
                    passedInOption = opt;
                }
            }
        }

        // Ok, only at most one is present...
        if (passedInOption != null) {
            passedInOption.consume(args, res);
        } else {
            if (isMandatory()) {
                throw new ParsingException("Mandatory option missing: one of [%s] must be passed", getOptionNames(","));
            }
        }
    }

    public boolean isPresent(List<String> args) {
        boolean present = false;

        for (AbstractOption opt : options) {
            present |= opt.isPresent(args);
        }

        return present;
    }

    @Override
    public String toString() {
        String[] params = new String[options.length];
        for (int i = 0; i < params.length; i++) {
            IOption opt = options[i];
            params[i] = opt.toString();
        }

        return String.format("[%s]", String.join(" | ", params));
    }

    IOption[] getOptions() {
        return options;
    }

    public void setDescription(String description) {
    }

    public String getDescription() {
        return null;
    }

    public String getShortName() {
        return null;
    }

    public String getLongName() {
        return null;
    }
}
