package it.jnrpe.yaclp;

import it.jnrpe.yaclp.validators.ValidatorBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntegerValidatorTest {

    @Test
    public void testValidValue() throws Exception {
        IOption option = OptionBuilder.forOption("-i", "--integer")
            .multiplicity(true)
            .argument(ArgumentBuilder
                .forArgument("myarg")
                .withValidator(
                    ValidatorBuilder.forInteger().build())
                .build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--integer", "1"));
        args.addAll(Arrays.asList("--integer", "100"));
        args.addAll(Arrays.asList("--integer", "1000"));
        args.addAll(Arrays.asList("--integer", "0"));
        option.consume(args, new CommandLine());
    }

    @Test(expected = ParsingException.class)
    public void testInvalidValue() throws Exception {
        IOption option = OptionBuilder.forOption("-i", "--integer")
            .multiplicity(true)
            .argument(ArgumentBuilder
                .forArgument("myarg")
                .withValidator(
                    ValidatorBuilder.forInteger().build())
                .build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--integer", "Hello"));
        option.consume(args, new CommandLine());
    }

    @Test(expected = ParsingException.class)
    public void testValueTooSmall() throws Exception {
        IOption option = OptionBuilder.forOption("-i", "--integer")
            .multiplicity(true)
            .argument(ArgumentBuilder
                .forArgument("myarg")
                .withValidator(
                    ValidatorBuilder.forInteger()
                        .min(30)
                        .build())
                .build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--integer", "10"));
        option.consume(args, new CommandLine());
    }

    @Test(expected = ParsingException.class)
    public void testValueTooBig() throws Exception {
        IOption option = OptionBuilder.forOption("-i", "--integer")
            .multiplicity(true)
            .argument(ArgumentBuilder
                .forArgument("myarg")
                .withValidator(
                    ValidatorBuilder.forInteger()
                        .max(30)
                        .build())
                .build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--integer", "31"));
        option.consume(args, new CommandLine());
    }

}
