package it.jnrpe.yaclp.test;

import it.jnrpe.yaclp.*;
import it.jnrpe.yaclp.validators.ValidatorBuilder;
import org.junit.Test;

public class IntegerValidatorTest {

    @Test
    public void testValidValue() throws Exception {
        ParserBuilder
            .forNewParser()
            .withOption(
                OptionBuilder.forOption("-i", "--integer")
                .repeatable(true)
                .argument(ArgumentBuilder
                    .forArgument("myarg")
                    .withValidator(
                        ValidatorBuilder.forInteger().build())
                    .build())
                .build()
            )
        .build()
        .parse(new String[]{"--integer", "1", "--integer", "100", "--integer", "1000", "--integer", "0"});
    }

    @Test(expected = ParsingException.class)
    public void testInvalidValue() throws Exception {
        ParserBuilder
            .forNewParser()
            .withOption(
                OptionBuilder.forOption("-i", "--integer")
                .repeatable(true)
                .argument(ArgumentBuilder
                    .forArgument("myarg")
                    .withValidator(
                        ValidatorBuilder.forInteger().build())
                    .build())
                .build()
            )
            .build()
            .parse(new String[]{"--integer", "Hello"});
    }

    @Test(expected = ParsingException.class)
    public void testValueTooSmall() throws Exception {
        ParserBuilder
            .forNewParser()
            .withOption(
                OptionBuilder.forOption("-i", "--integer")
                    .repeatable(true)
                    .argument(ArgumentBuilder
                        .forArgument("myarg")
                        .withValidator(
                            ValidatorBuilder.forInteger()
                                .min(30)
                                .build())
                        .build())
                    .build()
            )
            .build()
            .parse(new String[]{"--integer", "10"});
    }

    @Test(expected = ParsingException.class)
    public void testValueTooBig() throws Exception {
        ParserBuilder
            .forNewParser()
            .withOption(
                OptionBuilder.forOption("-i", "--integer")
                    .repeatable(true)
                    .argument(ArgumentBuilder
                        .forArgument("myarg")
                        .withValidator(
                            ValidatorBuilder.forInteger()
                                .max(30)
                                .build())
                        .build())
                    .build()
            )
            .build()
            .parse(new String[]{"--integer", "31"});
    }

}
