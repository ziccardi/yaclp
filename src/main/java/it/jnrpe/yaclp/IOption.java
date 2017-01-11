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

/**
 * Generic option interface.
 */
public interface IOption {
    /**
     * Checks if the option is present in the given command line.
     * @param args the command line
     * @return true or false
     */
    boolean isPresent(List<String> args);

    /**
     * Consumes the passed in command line by parsing the option and removing it.
     *
     * @param args the command line. It gets changed by the method.
     * @param res the result of the parsing
     * @throws ParsingException on any error parsing the command line
     */
    void consume(List<String> args, CommandLine res) throws ParsingException;

    /**
     * Sets the dependency from this option to another one.
     * @param option option to depend on
     */
    void addRequiredOption(IOption option);

    /**
     * Returns the short name for this option.
     * @return the short name for this option
     */
    String getShortName();

    /**
     * Returns the long name for this option.
     * @return Returns the long name for this option.
     */
    String getLongName();

    /**
     * Configures the arguments of this option.
     * @param arg the arguments of this option
     */
    void setArgument(IArgument arg);

    /**
     * Checks if this option is mandatory
     * @return  true or false
     */
    boolean isMandatory();

    void setDescription(String description);

    String getDescription();
}
