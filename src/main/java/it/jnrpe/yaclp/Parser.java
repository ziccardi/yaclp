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
        List<String> argsList = new LinkedList<String>(Arrays.asList(args));

        CommandLine res = new CommandLine();
        for (IOption opt: options) {
            opt.consume(argsList, res);
        }

        if (!argsList.isEmpty()) {
            throw new ParsingException("Unexpected tokens: " + argsList);
        }

        return res;
    }
}
