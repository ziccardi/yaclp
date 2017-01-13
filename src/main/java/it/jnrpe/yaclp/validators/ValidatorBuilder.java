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

public class ValidatorBuilder {

    private ValidatorBuilder() {

    }

    public static IntegerValidator.Builder forInteger() {
        return new IntegerValidator.Builder();
    }

    public static StringValidator.Builder forString() {
        return new StringValidator.Builder();
    }

    public static FileValidator.Builder forFile() {
        return new FileValidator.Builder();
    }
}
