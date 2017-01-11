package it.jnrpe.yaclp;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class MutuallyExlcusiveOptionTest {

    private IOption buildMutuallyExclusiveOption() {
        return OptionBuilder.forMutuallyExclusiveOption()
            .withOption(
                OptionBuilder.forOption("-e", "--exists")
                    .description("Checks that a file exists")
                    .build())
            .withOption(
                OptionBuilder.forOption("-c", "--contains")
                    .description("Check that a file contains a label")
                    .argument(ArgumentBuilder.forArgument("label").build())
                    .build())
            .build();
    }

    @Test
    public void testSuccess() throws Exception {
        IOption option = buildMutuallyExclusiveOption();

        option.consume(new LinkedList<String>(Arrays.asList("--exists")), new CommandLine());
        option.consume(new LinkedList<String>(Arrays.asList("--contains", "mylabel")), new CommandLine());
    }

    @Test(expected = ParsingException.class)
    public void testFail() throws Exception {
        IOption option = buildMutuallyExclusiveOption();

        option.consume(new LinkedList<String>(Arrays.asList("--exists", "--contains", "label")), new CommandLine());
    }
}
