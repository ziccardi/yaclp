package it.jnrpe.yaclp.test;

import it.jnrpe.yaclp.*;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentTest {

    @Test(expected = ParsingException.class)
    public void testMissingMandatoryArg() throws Exception {
        Parser parser = ParserBuilder.forOptionsBasedCli()
            .withOption(
                OptionBuilder.forOption("-m", "--mandatory")
                .mandatory(true)
                .repeatable(true)
                .argument(ArgumentBuilder.forArgument("myarg").build())
                .build()
        ).build();

        parser.parse(new String[]{"--mandatory"});
    }

    @Test
    public void testMissingOptionalArg() throws Exception {
        Parser parser = ParserBuilder.forOptionsBasedCli()
        .withOption(
            OptionBuilder.forOption("-m", "--mandatory")
                .mandatory(true)
                .repeatable(true)
                .argument(ArgumentBuilder.forArgument("myarg").mandatory(false).build())
                .build()
        ).build();

        parser.parse(new String[]{"--mandatory"});
    }

    @Test
    public void testDefaultValue() throws Exception {
        Parser parser = ParserBuilder.forOptionsBasedCli()
            .withOption(
                OptionBuilder.forOption("-o", "--optional")
                    .mandatory(false)
                    .argument(ArgumentBuilder.forArgument("myarg").mandatory(true).build())
                    .build()
            ).build();

        CommandLine cl = parser.parse(new String[]{});

        Assert.assertEquals("DEFAULT", cl.getValue("-o", "DEFAULT"));
    }

    @Test
    public void testMultipleValue() throws Exception {
        Parser parser = ParserBuilder.forOptionsBasedCli()
            .withOption(
                OptionBuilder.forOption("-o", "--optional")
                    .mandatory(false)
                    .argument(ArgumentBuilder.forArgument("myarg").mandatory(true)
                        .withMinRepetitions(1)
                        .withMaxRepetitions(Integer.MAX_VALUE)
                        .build())
                    .build()
            ).build();

        CommandLine cl = parser.parse(new String[]{"-o", "value1", "value2", "value3"});

        String[] values = cl.getValues("-o");
        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, values);
    }

  @Test(expected = ParsingException.class)
  public void testNotRepeatable() throws Exception {
    Parser parser = ParserBuilder.forOptionsBasedCli()
        .withOption(
            OptionBuilder.forOption("-o", "--optional")
                .mandatory(false)
                .argument(ArgumentBuilder.forArgument("myarg").mandatory(true).build())
                .build()
        ).build();

    CommandLine cl = parser.parse(new String[]{"-o", "value1", "value2", "value3"});
  }
}
