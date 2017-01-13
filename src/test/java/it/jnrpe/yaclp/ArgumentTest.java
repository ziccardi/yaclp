package it.jnrpe.yaclp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ziccardi on 13/01/2017.
 */
public class ArgumentTest {

    @Test(expected = ParsingException.class)
    public void testMissingMandatoryArg() throws Exception {
        IOption option = OptionBuilder.forOption("-m", "--mandatory")
            .mandatory(true)
            .multiplicity(true)
            .argument(ArgumentBuilder.forArgument("myarg").build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--mandatory"));
        option.consume(args, new CommandLine());
    }

    @Test
    public void testMissingOptionalArg() throws Exception {
        IOption option = OptionBuilder.forOption("-m", "--mandatory")
            .mandatory(true)
            .multiplicity(true)
            .argument(ArgumentBuilder.forArgument("myarg").mandatory(false).build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--mandatory"));
        option.consume(args, new CommandLine());
    }
}
