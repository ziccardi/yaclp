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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builder for mutually exclusive options.
 */
public final class MutuallyExclusiveOptionBuilder {

  /**
   * List of the mutually exclusive options.
   */
  private List<IOption> options = new ArrayList<>();

  /**
   * Whether this option is mandatory or not.
   * Being mandatory means one of the mutually exclusive options must be present.
   */
  private boolean mandatory = false;

  /**
   * Description for this option.
   */
  private String description = "";

  /**
   * Instantiates the object.
   */
  MutuallyExclusiveOptionBuilder() {
  }

  /**
   * Call this method many time to add all the options than can't be specified together.
   *
   * @param options the options to be added
   * @return the builder
   */
  public MutuallyExclusiveOptionBuilder withOptions(final IOption... options) {
    this.options.addAll(Arrays.asList(options));
    return this;
  }

  /**
   * If mandatory, one of the option MUST be present in the command line.
   *
   * @param mandatory whether the option is mandatory or not
   * @return the builder
   */
  public MutuallyExclusiveOptionBuilder mandatory(final boolean mandatory) {
    this.mandatory = mandatory;
    return this;
  }

  /**
   * Description for this mutually exlusive option group.
   *
   * @param description the description
   * @return the builder
   */
  public MutuallyExclusiveOptionBuilder description(final String description) {
    this.description = description;
    return this;
  }

  /**
   * Builds the option as configured.
   *
   * @return the newly build option
   */
  public IOption build() {
    MutuallyExclusiveOptions opt =
        new MutuallyExclusiveOptions(options.toArray(new IOption[options.size()]));
    opt.setMandatory(mandatory);
    opt.setDescription(description);

    return opt;
  }
}
