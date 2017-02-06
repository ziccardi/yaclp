/*******************************************************************************
 * Copyright (c) 2017 Massimiliano Ziccardi
 * <P/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <P/>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <P/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package it.jnrpe.yaclp;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Contains the parsed command line.
 */
public class CommandLine {
  /**
   * All the parsed options, together with its values.
   */
  private Map<String, List<String>> parmAndValue = new HashMap<>();

  /**
   * All the parsed property options.
   */
  private Map<String, Properties> propertyParams = new HashMap<>();

  /**
   * The parsed command.
   */
  private String command;

  /**
   * Adds a new value for the given option.
   *
   * @param params   map containing the param/value pairs
   * @param key      key whose value must be changed
   * @param newValue new value to add to the list of values
   */
  private void addValue(final Map<String, List<String>> params,
                        final String key,
                        final String newValue) {
    List<String> values = params.get(key);
    if (values == null) {
      values = new ArrayList<>();
      params.put(key, values);
    }

    if (newValue != null) {
      values.add(newValue);
    }
  }

  /**
   * Adds a value for the given option.
   *
   * @param option the option
   * @param value  the new value
   */
  void addValue(final IOption option, final String value) {
    addValue(parmAndValue, option.getShortName(), value);
    addValue(parmAndValue, option.getLongName(), value);
  }

  /**
   * Adds a property value to this command line object.
   *
   * @param option the property option
   * @param key    the property key
   * @param value  the property value
   */
  void addProperty(final IOption option, final String key, final String value) {
    Properties props = propertyParams.get(option.getShortName());
    if (props == null) {
      props = new Properties();
      propertyParams.put(option.getShortName(), props);
    }

    props.setProperty(key, value);
  }

  /**
   * Sets a parsed command for this command line object.
   *
   * @param command the parsed command.
   */
  void setCommand(final String command) {
    this.command = command;
  }

  /**
   * Returns the first value for the given option name.
   *
   * @param param the option name
   * @return the first value
   */
  public String getValue(final String param) {
    return getValue(param, null);
  }

  /**
   * Returns the first value for the given option name.
   * If the option is not present, defaultValue is returned.
   *
   * @param param        the option name
   * @param defaultValue the default value
   * @return the option value or the default if the option is not present.
   */
  public String getValue(final String param, final String defaultValue) {
    List<String> values = parmAndValue.get(param);
    if (values == null) {
      return defaultValue;
    }

    return values.get(0);
  }

  /**
   * Returns the value of the specified option as an object that can easily be converted
   * into the target object through the usage of the asXXX methods.
   * @param param the option param
   * @return the parsed value object
   */
  public ParsedValue getParsedValue(final String param) {
    return new ParsedValue(getValue(param));
  }

  /**
   * Returns all the values for the given option name.
   *
   * @param param the option name
   * @return the value
   */
  public String[] getValues(final String param) {
    return getValues(param, null);
  }

  /**
   * Returns the first value for the given option name.
   * If the option is not present, defaultValue is returned.
   *
   * @param param         the option name
   * @param defaultValues the default values
   * @return the option values or the defaults if the option is not present.
   */
  public String[] getValues(final String param, final String... defaultValues) {
    List<String> values = parmAndValue.get(param);
    if (values == null) {
      return defaultValues;
    }

    return values.toArray(new String[values.size()]);
  }

  /**
   * Returns the properties value for the given property option.
   *
   * @param optionName the option name
   * @return a {@link Properties} object representing the option value
   */
  public Properties getProperties(final String optionName) {
    return propertyParams.get(optionName);
  }

  /**
   * Returns if the option is present or not.
   *
   * @param name the name of the option to be checked
   * @return true or false
   */
  public boolean hasOption(final String name) {
    return parmAndValue.containsKey(name);
  }

  /**
   * Returns whether the command line has a command or not.
   *
   * @param command command to be checked
   * @return true or false
   */
  public boolean hasCommand(final String command) {
    return (this.command != null && this.command.equals(command));
  }

  /**
   * Parsed value object. Used to convert from string to the desired object.
   */
  public static class ParsedValue {

    /**
     * String representation of the value.
     */
    private final String value;

    /**
     * Constructor.
     * @param value string representation of the value
     */
    private ParsedValue(final String value) {
      this.value = value;
    }

    /**
     * Returns the value as an Integer with radix 10.
     * @return the value as an Integer with radix 10
     */
    public Integer asInteger() {
      return asInteger(null, 10);
    }

    /**
     * Returns the value as an Integer with radix 10.
     * If the option has not been specified, defaultValue is returned.
     * @param defaultValue value to be returned if the option is not specified
     * @return the value as an Integer with radix 10
     */
    public Integer asInteger(final Integer defaultValue) {
      return asInteger(defaultValue, 10);
    }

    /**
     * Returns the value as an Integer with the specified radix.
     * If the option has not been specified, defaultValue is returned.
     * @param defaultValue value to be returned if the option is not specified
     * @param radix radix to be used to parse the number
     * @return the value as an Integer with the specified radix.
     */
    public Integer asInteger(final Integer defaultValue, final int radix) {
      if (value == null) {
        return defaultValue;
      }
      return Integer.valueOf(value, radix);
    }

    /**
     * Returns the value as a Long with radix 10.
     * @return the value as a Long with radix 10
     */
    public Long asLong() {
      return asLong(null, 10);
    }

    /**
     * Returns the value as an Long with radix 10.
     * If the option has not been specified, defaultValue is returned.
     * @param defaultValue value to be returned if the option is not specified
     * @return the value as an Long with radix 10
     */
    public Long asLong(final Long defaultValue) {
      return asLong(defaultValue, 10);
    }

    /**
     * Returns the value as an Long with the specified radix.
     * If the option has not been specified, defaultValue is returned.
     * @param defaultValue value to be returned if the option is not specified
     * @param radix radix to be used to parse the number
     * @return the value as an Long with the specified radix.
     */
    public Long asLong(final Long defaultValue, final int radix) {
      if (value == null) {
        return defaultValue;
      }
      return Long.valueOf(value, radix);
    }

    /**
     * Returns the value as a Double.
     * @return the value as a Double
     */
    public Double asDouble() {
      return asDouble(null);
    }

    /**
     * Returns the value as an Double.
     * If the option has not been specified, defaultValue is returned.
     * @param defaultValue value to be returned if the option is not specified
     * @return the value as an Double
     */
    public Double asDouble(final Double defaultValue) {
      if (value == null) {
        return defaultValue;
      }
      return Double.valueOf(value);
    }

    /**
     * Returns the value as a Float.
     * @return the value as a Float
     */
    public Float asFloat() {
      return asFloat(null);
    }

    /**
     * Returns the value as an Float.
     * If the option has not been specified, defaultValue is returned.
     * @param defaultValue value to be returned if the option is not specified
     * @return the value as an Float
     */
    public Float asFloat(final Float defaultValue) {
      if (value == null) {
        return defaultValue;
      }
      return Float.valueOf(value);
    }

    /**
     * Returns the value as a File.
     * @return the value as a File
     */
    public File asFile() {
      return asFile(null);
    }

    /**
     * Returns the value as an File.
     * If the option has not been specified, defaultValue is returned.
     * @param defaultValue value to be returned if the option is not specified
     * @return the value as an File
     */
    public File asFile(final File defaultValue) {
      if (value == null) {
        return defaultValue;
      }
      return new File(value);
    }

    /**
     * Returns the value as a URL.
     * @return the value as a URL
     */
    public URL asURL() {
      return asURL(null);
    }

    /**
     * Returns the value as an URL.
     * If the option has not been specified, defaultValue is returned.
     * @param defaultValue value to be returned if the option is not specified
     * @return the value as an URL
     */
    public URL asURL(final URL defaultValue) {
      if (value == null) {
        return defaultValue;
      }

      try {
        return new URL(value);
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }
}
