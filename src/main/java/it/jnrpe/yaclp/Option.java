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

class Option implements IOption {
    private List<IOption> requiredOptions = new ArrayList<IOption>();
    private Argument optionArgs = null;

    private final String shortName;
    private final String longName;

    private String description = "";

    private boolean mandatory = false;
    private boolean multiple = false;

    public Option(final String shortName, final String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public boolean isPresent(List<String> args) {
        return args.contains(shortName) || args.contains(longName);
    }

    public void consume(List<String> args, CommandLine res) throws ParsingException{
        if (!isPresent(args)) {
            if (mandatory && !res.hasOption(getShortName())) {
                throw new ParsingException("Mandatory option [%s] is missing", getLongName());
            }
            return;
        }

        if (res.hasOption(getShortName()) && !multiple) {
            throw new ParsingException("Option [%s] can be specified only one time", getLongName());
        }

        for (IOption requiredOption : requiredOptions) {
            if (requiredOption.isPresent(args)) {
                requiredOption.consume(args, res);
            } else {
                if (res.hasOption(requiredOption.getShortName())) {
                    continue;
                }
                throw new ParsingException("%s requires [%s]", longName, requiredOption.getLongName());
            }
        }

        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).equals(shortName) || args.get(i).equals(longName)) {
                // consume and manage it
                args.remove(i);
                res.addValue(this, null);
                if (optionArgs != null) {
                    optionArgs.consume(this, args, i, res);
                }
                // consume again if repeated
                consume(args, res);
            }
        }
    }

    public void addRequiredOption(IOption option) {
        requiredOptions.add(option);
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setArgument(IArgument arg) {
        this.optionArgs = (Argument) arg;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        if (this.optionArgs == null) {
            return String.format("%s (%s)", longName, shortName);
        } else {
            return String.format("%s (%s) <%s>", longName, shortName, optionArgs.getName());
        }
    }
}
