package it.yaclp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ziccardi on 10/01/2017.
 */
public class Result {
    private Map<String, String> parmAndValue = new HashMap<String, String>();

    public void addValue(String param, String value) {
        parmAndValue.put(param, value);
    }

    public String getValue(String param) {
        return param;
    }

    public boolean hasOption(String name) {
        return parmAndValue.containsKey(name);
    }
}
