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
package it.jnrpe.yaclp.help;

import it.jnrpe.yaclp.IOption;

public class OptionHelpFormatter {
    private final int indent;
    private final int screenWidth;
    private final int longestOptionLength;
    private final int spaceBetweenOptionAndDesc;

    public OptionHelpFormatter(final int indent, final int longestOptionLength, final int spaceBetweenOptionAndDesc, final int screenWidth) {
        this.indent = indent;
        this.longestOptionLength = longestOptionLength;
        this.screenWidth = screenWidth;
        this.spaceBetweenOptionAndDesc = spaceBetweenOptionAndDesc;
    }

    public String format(IOption option) {
        String optionString = format(option.toString(), indent, longestOptionLength);

        // Formatting description
        String[] words = option.getDescription().split(" ");

        String filler = getFiller(' ', optionString.length());

        StringBuilder formattedDesc = new StringBuilder();

        StringBuilder currentTrunk = new StringBuilder(optionString);

        for (String s : words) {
            if (currentTrunk.length() + s.length() + 1 <= screenWidth) {
                currentTrunk.append(s).append(' ');
            } else {
                if (formattedDesc.length() != 0) {
                    formattedDesc.append('\n');
                }
                formattedDesc.append(currentTrunk);
                currentTrunk = new StringBuilder(filler).append(s).append(' ');
            }
        }

        if (formattedDesc.length() != 0) {
            formattedDesc.append('\n');
        }
        formattedDesc.append(currentTrunk);

        // TODO: add requires...
        return formattedDesc.toString();
    }

    private String getFiller(char fillerChar, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(fillerChar);
        }

        return sb.toString();
    }

    private String format(String s, int indent, int optionLength) {
        StringBuilder sb = new StringBuilder(s);

        while(sb.length() < optionLength) {
            sb.append(' ');
        }

        for (int i = 0; i < indent; i++) {
            sb.insert(0, ' ');
        }

        for (int i = 0; i < spaceBetweenOptionAndDesc; i++) {
            sb.append(' ');
        }

        return sb.toString();
    }
}
