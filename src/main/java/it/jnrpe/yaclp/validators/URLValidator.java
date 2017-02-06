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
package it.jnrpe.yaclp.validators;

import it.jnrpe.yaclp.IArgument;
import it.jnrpe.yaclp.IOption;
import it.jnrpe.yaclp.ParsingException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Checks that the argument value is a valid URL.
 * If a list of accepted protocols is provided, checks if the URL matches any of the given
 * protocols.
 */
public class URLValidator implements IArgumentValidator {

  /**
   * List of protocols to be verified.
   */
  private Set<String> requestedProtocols = new HashSet<>();

  /**
   * Constructor.
   */
  private URLValidator() {

  }

  @Override
  public void validate(final IOption option, final IArgument argument, final String value) throws
      ParsingException {
    try {
      URL url = new URL(value);
      if (!requestedProtocols.isEmpty()
          && requestedProtocols.contains(url.getProtocol().toLowerCase())) {
        throw new ParsingException(
            "Protocol cam be only one of [%s]. Received : '%s'",
            String.join(",", requestedProtocols), url.getProtocol());
      }
    } catch (MalformedURLException e) {
      throw new ParsingException("Value '%s' is not a valid URL", value);
    }
  }

  /**
   * Adds a protocol to the list of required protocols.
   * @param requestedProtocol the new protocol to be added
   */
  private void addRequestedProtocol(final String requestedProtocol) {
    this.requestedProtocols.add(requestedProtocol);
  }

  /**
   * Builder for {@link URLValidator} objects.
   */
  public static final class Builder {

    /**
     * List of accepted protocols.
     */
    private Set<String> requestedProtocols = new HashSet<>();

    /**
     * Constructor.
     */
    Builder() {
    }

    /**
     * Adds a list of accepted protocols to the current list.
     * @param protocols the list of accepted protocols to be added
     * @return this builder.
     */
    public Builder withRequestedProtocols(final String... protocols) {
      this.requestedProtocols.addAll(Arrays.asList(protocols));
      return this;
    }

    /**
     * Returns a new instance of {@link URLValidator}.
     * @return a new instance of {@link URLValidator}
     */
    public URLValidator build() {
      URLValidator validator = new URLValidator();
      requestedProtocols.forEach(validator::addRequestedProtocol);

      return validator;
    }
  }
}
