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
    private ParserBuilder() {
    }

    public static OptionsBasedParserBuilder forOptionsBasedCli() {
        return new OptionsBasedParserBuilder();
    }

    public static CommandBasedParserBuilder forCommandsBasedCli() {
        return new CommandBasedParserBuilder();
    }

    public static class CommandBasedParserBuilder {

        private List<Command> commands = new ArrayList<>();

        private CommandBasedParserBuilder() {
        }

        private void addCommands(final Command... commands) {
            if (commands.length == 1) {
                this.commands.add(commands[0]);
            } else {
                this.commands.addAll(Arrays.asList(commands));
            }
        }

        public CommandBasedParserBuilder withCommands(final Command... commands) {
            addCommands(commands);
            return this;
        }

        public Parser build() {
            Parser p = new Parser();
            commands.forEach(p::addCommand);
            return p;
        }
    }

    public static class OptionsBasedParserBuilder {

        private List<IOption> options = new ArrayList<>();

        private OptionsBasedParserBuilder() {
        }

        private void addOptions(IOption... options) {
            if (options.length == 1) {
                this.options.add(options[0]);
            } else {
                this.options.addAll(Arrays.asList(options));
            }
        }

        public OptionsBasedParserBuilder withOption(final IOption... options) {
            addOptions(options);
            return this;
        }

        public Parser build() {
            Parser p = new Parser();
            options.forEach(p::addOption);
            return p;
        }
    }
}
