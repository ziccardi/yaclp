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

import java.util.List;

class Option extends AbstractOption {

    private final String shortName;
    private final String longName;

    private String description = "";



    public Option(final String shortName, final String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public boolean isPresent(List<String> args) {
        return args.contains(shortName) || args.contains(longName);
    }

    protected final void doConsume(List<String> args, CommandLine res) throws ParsingException{
        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).equals(shortName) || args.get(i).equals(longName)) {
                // consume and manage it
                args.remove(i);
                res.addValue(this, null);
                if (getArgument() != null) {
                    getArgument().consume(this, args, i, res);
                }
                // consume again if repeated
                consume(args, res);
            }
        }
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        if (!longName.equals(shortName)) {
            if (getArgument() == null) {
                return String.format("%s (%s)", longName, shortName);
            } else {
                return String.format("%s (%s) <%s>", longName, shortName, getArgument().getName());
            }
        } else {
            if (getArgument() == null) {
                return String.format("%s", shortName);
            } else {
                return String.format("%s <%s>", shortName, getArgument().getName());
            }
        }
    }
}
