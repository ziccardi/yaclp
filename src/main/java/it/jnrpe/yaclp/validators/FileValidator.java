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

import java.io.File;

/**
 * Validator for file arguments.
 * Can check:
 * <ul>
 * <li>that passed path exists</li>
 * <li>that passed path does not exists</li>
 * <li>that passed path is writable</li>
 * <li>that passed path is readable</li>
 * <li>that passed path is a file</li>
 * <li>that passed path is a directorye</li>
 * </ul>
 */
public class FileValidator implements IArgumentValidator {

  /**
   * Flag to enable the 'must exists' check.
   */
  private static final int MUST_EXISTS = 1;

  /**
   * Flag to enable the 'must not exists' check.
   */
  private static final int MUST_NOT_EXISTS = 1 << 2;

  /**
   * Flag to enable the 'must be writable' check.
   */
  private static final int MUST_BE_WRITABLE = 1 << 3;

  /**
   * Flag to enable the 'must be a file' check.
   */
  private static final int MUST_BE_FILE = 1 << 4;

  /**
   * Flag to enable the 'must be a drectory' check.
   */
  private static final int MUST_BE_DIRECTORY = 1 << 5;

  /**
   * Flag to enable the 'must be a readable' check.
   */
  private static final int MUST_BE_READABLE = 1 << 6;

  /**
   * Enabled check flags.
   */
  private final int enabledChecks;

  /**
   * Constructor.
   *
   * @param checks enabled checks flags.
   */
  private FileValidator(final int checks) {
    this.enabledChecks = checks;
  }

  /**
   * Checks if the passed in flag is enabled.
   *
   * @param flag flag to be checked
   * @return true or false
   */
  private boolean mustCheck(final int flag) {
    return (this.enabledChecks & flag) != 0;
  }

  @Override
  public void validate(final IOption option, final IArgument argument, final String value)
      throws ValidationException {
    File pathToCheck = new File(value);
    if (mustCheck(MUST_BE_FILE) && !pathToCheck.isFile()) {
      throw new ValidationException("Specified path (<%s>) is not a file", value);
    }

    if (mustCheck(MUST_BE_DIRECTORY) && !pathToCheck.isDirectory()) {
      throw new ValidationException("Specified path (<%s>) is not a directory", value);
    }

    if (mustCheck(MUST_EXISTS) && !pathToCheck.exists()) {
      throw new ValidationException("Specified file (<%s>) does not exists", value);
    }

    if (mustCheck(MUST_NOT_EXISTS) && pathToCheck.exists()) {
      throw new ValidationException("Specified file (<%s>) exists: it shouldn't", value);
    }

    if (mustCheck(MUST_BE_WRITABLE) && !pathToCheck.canWrite()) {
      throw new ValidationException("Specified file (<%s>) can't be written", value);
    }

    if (mustCheck(MUST_BE_READABLE) && !pathToCheck.canRead()) {
      throw new ValidationException("Specified file (<%s>) can't be read", value);
    }
  }

  /**
   * Builder for {@link FileValidator} objects.
   */
  public static final class Builder {

    /**
     * Enabled checks flags.
     */
    private int enabledChecks = 0;

    /**
     * Constructor.
     */
    Builder() {
    }

    /**
     * Argument must point to a file.
     *
     * @return this builder
     */
    public Builder isFile() {
      enabledChecks |= MUST_BE_FILE;
      enabledChecks &= ~MUST_BE_DIRECTORY;
      return this;
    }

    /**
     * Argument must point to a directory.
     *
     * @return this builder
     */
    public Builder isDirectory() {
      enabledChecks |= MUST_BE_DIRECTORY;
      enabledChecks &= ~MUST_BE_FILE;
      return this;
    }

    /**
     * Argument must point to an existing path.
     *
     * @return this builder
     */
    public Builder exists() {
      enabledChecks |= MUST_EXISTS;
      enabledChecks &= ~MUST_NOT_EXISTS;
      return this;
    }

    /**
     * Argument must point to a non existing path.
     *
     * @return this builder
     */
    public Builder notExists() {
      enabledChecks |= MUST_NOT_EXISTS;
      enabledChecks &= ~MUST_EXISTS;
      return this;
    }

    /**
     * Argument must point to writable path.
     *
     * @return this builder
     */
    public Builder isWritable() {
      enabledChecks |= MUST_BE_WRITABLE;
      return this;
    }

    /**
     * Argument must point to readable path.
     *
     * @return this builder
     */
    public Builder isReadable() {
      enabledChecks |= MUST_BE_READABLE;
      return this;
    }

    /**
     * Builds the validator.
     *
     * @return a {@link FileValidator} instance
     */
    public FileValidator build() {
      return new FileValidator(enabledChecks);
    }
  }
}
