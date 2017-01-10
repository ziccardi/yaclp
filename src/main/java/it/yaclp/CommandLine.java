package it.yaclp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the parsed command line
 */
public class CommandLine {
    /**
     * All the parsed options, together with its values
     */
    private Map<String, List<String>> parmAndValue = new HashMap<String, List<String>>();

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

    /**
     * Returns if the option is present or not
     * @param name the name of the option to be checked
     * @return true or false
     */
    public boolean hasOption(String name) {
        return parmAndValue.containsKey(name);
    }
}
