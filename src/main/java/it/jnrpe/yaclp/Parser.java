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
import java.util.LinkedList;
import java.util.List;

/**
 * Parse a command line.
 */
public class Parser {
    private List<IOption> options = new ArrayList<IOption>();

    /**
     * Ad an option to the list of supported options.
     * It can be a simple option ({@link Option}) or a mutually exclusive option ({@link MutuallyExclusiveOptions})
     * @param option the option to be added to the parser
     */
    public void addOption(IOption option) {
        this.options.add(option);
    }

    List<IOption> getOptions() {
        return options;
    }

    /**
     * Parses the command line and returns a {@link CommandLine} object if the parsing succeeds.
     * @param args the command line to be parsed
     * @return the parsed command line
     * @throws ParsingException if the command line is not valid according to the parser configuration
     */
    public CommandLine parse(String[] args) throws ParsingException{
        List<String> argsList = preparse(args);

        CommandLine res = new CommandLine();
        for (IOption opt: options) {
            opt.consume(argsList, res);
        }

        if (!argsList.isEmpty()) {
            throw new ParsingException("Unexpected tokens: " + argsList);
        }

        return res;
    }

    /**
     * Separates options from arguments when they are attached (for example -Dkey=value becomes -D key=value), so that
     * they can be managed as all the other options
     * @param args the command line
     * @return a List contained the command line (options and arguments)
     */
    private List<String> preparse(String[] args) {
        List<String> res = new LinkedList<String>();

        for (String arg: args) {

            if (arg.length() == 1 || arg.startsWith("--") || (arg.startsWith("-") && arg.length() == 2) || !arg.startsWith("-") ) {
                res.add(arg);
                continue;
            }

            res.add("-" + arg.substring(1, 2));
            res.add(arg.substring(2));
        }

        return res;
    }
}
