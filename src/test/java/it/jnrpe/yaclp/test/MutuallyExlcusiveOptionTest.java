package it.jnrpe.yaclp.test;

import it.jnrpe.yaclp.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class MutuallyExlcusiveOptionTest {

    private Parser buildMutuallyExclusiveOption() {
        return ParserBuilder.forOptionsBasedCli().withOption(
            OptionBuilder.forMutuallyExclusiveOption()
            .withOptions(
                OptionBuilder.forOption("-e", "--exists")
                    .description("Checks that a file exists")
                    .build())
            .withOptions(
                OptionBuilder.forOption("-c", "--contains")
                    .description("Check that a file contains a label")
                    .argument(ArgumentBuilder.forArgument("label").build())
                    .build())
            .build()
        ).build();
    }

    @Test
    public void testSuccess() throws Exception {
        Parser p = buildMutuallyExclusiveOption();

        p.parse(new String[]{"--exists"});
        p.parse(new String[]{"--contains", "mylabel"});
    }

    @Test(expected = ParsingException.class)
    public void testFail() throws Exception {
        Parser p = buildMutuallyExclusiveOption();

        p.parse(new String[]{"--exists", "--contains", "label"});
    }
}
