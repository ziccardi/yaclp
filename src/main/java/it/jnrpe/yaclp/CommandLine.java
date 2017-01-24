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

import java.util.*;

/**
 * Contains the parsed command line
 */
public class CommandLine {
    /**
     * All the parsed options, together with its values
     */
    private Map<String, List<String>> parmAndValue = new HashMap<String, List<String>>();

    private Map<String, Properties> propertyParams = new HashMap<String, Properties>();

    private String command;

    /**
     * Adds a new value for the given option
     * @param params map containing the param/value pairs
     * @param key key whose value must be changed
     * @param newValue new value to add to the list of values
     */
    private void addValue(Map<String, List<String>> params, String key, String newValue) {
        List<String> values = params.get(key);
        if (values == null) {
            values = new ArrayList<String>();
            params.put(key, values);
        }

        if (newValue != null) {
            values.add(newValue);
        }
    }

    /**
     * Adds a value for the given option
     * @param option the option
     * @param value the new value
     */
    void addValue(IOption option, String value) {
        addValue(parmAndValue, option.getShortName(), value);
        addValue(parmAndValue, option.getLongName(), value);
    }

    void addProperty(final IOption option, final String key, final String value) {
        Properties props = propertyParams.get(option.getShortName());
        if (props == null) {
            props = new Properties();
            propertyParams.put(option.getShortName(), props);
        }

        props.setProperty(key, value);
    }

    void setCommand(final String command) {
        this.command = command;
    }

    /**
     * Returns teh first value for the given option name
     * @param param the option name
     * @return the first value
     */
    public String getValue(String param) {
        List<String> values = parmAndValue.get(param);
        if (values == null) {
            return null;
        }

        return values.get(0);
    }

    /**
     * Returns all the values for the given option name
     * @param param the option name
     * @return the value
     */
    public String[] getValues(final String param) {
        List<String> values = parmAndValue.get(param);
        if (values == null) {
            return null;
        }

        return values.toArray(new String[values.size()]);
    }

    public Properties getProperties(String optionName) {
        return propertyParams.get(optionName);
    }

    /**
     * Returns if the option is present or not
     * @param name the name of the option to be checked
     * @return true or false
     */
    public boolean hasOption(String name) {
        return parmAndValue.containsKey(name);
    }

    public boolean hasCommand(final String command) {
        return (this.command != null && this.command.equals(command));
    }
}
