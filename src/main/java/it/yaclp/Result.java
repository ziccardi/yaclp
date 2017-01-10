package it.yaclp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ziccardi on 10/01/2017.
 */
public class Result {
    private Map<String, String> parmAndValue = new HashMap<String, String>();

    public void addValue(IOption option, String value) {
        parmAndValue.put(option.getShortName(), value);
        parmAndValue.put(option.getLongName(), value);
    }

    public String getValue(String param) {
        return parmAndValue.get(param);
    }

    public boolean hasOption(String name) {
        return parmAndValue.containsKey(name);
    }
}
