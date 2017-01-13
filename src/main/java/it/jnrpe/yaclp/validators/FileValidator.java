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
package it.jnrpe.yaclp.validators;

import it.jnrpe.yaclp.ParsingException;

import java.io.File;

public class FileValidator implements IArgumentValidator {

    private static final int MUST_EXISTS = 1;
    private static final int MUST_NOT_EXISTS = 1<<2;
    private static final int MUST_BE_WRITABLE = 1<<3;
    private static final int MUST_BE_FILE = 1<<4;
    private static final int MUST_BE_DIRECTORY = 1<<5;
    private static final int MUST_BE_READABLE = 1<<6;

    private final int enabledChecks;

    private FileValidator(final int checks) {
        this.enabledChecks = checks;
    }

    private boolean mustCheck(final int check) {
        return (this.enabledChecks & check) != 0;
    }

    public void validate(String value) throws ParsingException {
        File f = new File(value);
        if (mustCheck(MUST_BE_FILE) && !f.isFile()) {
            throw new ParsingException("Specified path (<%s>) is not a file", value);
        }

        if (mustCheck(MUST_BE_DIRECTORY) && !f.isDirectory()) {
            throw new ParsingException("Specified path (<%s>) is not a directory", value);
        }

        if (mustCheck(MUST_EXISTS) && !f.exists()) {
            throw new ParsingException("Specified file (<%s>) does not exists", value);
        }

        if (mustCheck(MUST_NOT_EXISTS) && f.exists()) {
            throw new ParsingException("Specified file (<%s>) exists: it shouldn't", value);
        }

        if (mustCheck(MUST_BE_WRITABLE) && !f.canWrite()) {
            throw new ParsingException("Specified file (<%s>) can't be written", value);
        }

        if (mustCheck(MUST_BE_READABLE) && !f.canRead()) {
            throw new ParsingException("Specified file (<%s>) can't be read", value);
        }
    }

    public static class Builder {
        Builder() {
        }

        private int enabledChecks = 0;

        public Builder isFile() {
            enabledChecks |= MUST_BE_FILE;
            enabledChecks &= ~MUST_BE_DIRECTORY;
            return this;
        }

        public Builder isDirectory() {
            enabledChecks |= MUST_BE_DIRECTORY;
            enabledChecks &= ~MUST_BE_FILE;
            return this;
        }

        public Builder exists() {
            enabledChecks |= MUST_EXISTS;
            enabledChecks &= ~MUST_NOT_EXISTS;
            return this;
        }

        public Builder notExists() {
            enabledChecks |= MUST_NOT_EXISTS;
            enabledChecks &= ~MUST_EXISTS;
            return this;
        }

        public Builder isWritable() {
            enabledChecks |= MUST_BE_WRITABLE;
            return this;
        }

        public Builder isReadable() {
            enabledChecks |= MUST_BE_READABLE;
            return this;
        }

        public FileValidator build() {
            return new FileValidator(enabledChecks);
        }
    }
}
