package it.jnrpe.yaclp.test;

import it.jnrpe.yaclp.*;
import org.junit.Assert;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionalOption() throws Exception {
        ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                OptionBuilder.forOption("-o", "--optional").build()
            )
            .build()
            .parse(new String[0]);
    }

    @Test(expected = ParsingException.class)
    public void testMandatoryOption() throws Exception {
        ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                OptionBuilder.forOption("-o", "--optional").mandatory(true).build()
            )
            .build()
            .parse(new String[0]);
    }

    @Test
    public void testSingleOption() throws Exception {
        CommandLine cl =
            ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                OptionBuilder
                    .forOption("-m", "--mandatory")
                    .mandatory(true)
                    .argument(ArgumentBuilder.forArgument("myarg").build())
                    .build()
            )
            .build()
            .parse(new String[]{"--mandatory", "test"});

        Assert.assertTrue(cl.hasOption("-m"));
        Assert.assertTrue(cl.hasOption("--mandatory"));

        Assert.assertEquals("test", cl.getValue("-m"));
        Assert.assertEquals("test", cl.getValue("--mandatory"));

        Assert.assertTrue(cl.getValues("-m").length == 1);
    }

    @Test(expected = ParsingException.class)
    public void testUnrepetibleOption() throws Exception {
        ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                OptionBuilder
                    .forOption("-m", "--mandatory")
                    .mandatory(true)
                    .argument(ArgumentBuilder.forArgument("myarg").build())
                    .build()
            )
            .build()
            .parse(new String[]{"--mandatory", "test", "--mandatory", "test"});
    }

    @Test
    public void testRepetibleOption() throws Exception {
        CommandLine cl = ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                OptionBuilder
                    .forOption("-m", "--mandatory")
                    .mandatory(true)
                    .repeatable(true)
                    .argument(ArgumentBuilder.forArgument("myarg").build())
                    .build()
            )
            .build()
            .parse(new String[]{"--mandatory", "test", "--mandatory", "test1"});

        Assert.assertTrue(cl.hasOption("-m"));
        Assert.assertTrue(cl.hasOption("--mandatory"));

        Assert.assertEquals("test", cl.getValue("-m"));
        Assert.assertEquals("test", cl.getValue("--mandatory"));

        Assert.assertEquals("test", cl.getValues("-m")[0]);
        Assert.assertEquals("test", cl.getValues("--mandatory")[0]);
        Assert.assertEquals("test1", cl.getValues("-m")[1]);
        Assert.assertEquals("test1", cl.getValues("--mandatory")[1]);


        Assert.assertTrue(cl.getValues("-m").length == 2);
    }

    @Test(expected = ParsingException.class)
    public void testOptionDependency() throws Exception {
        IOption primary = OptionBuilder
            .forOption("-p", "--primary")
            .argument(ArgumentBuilder.forArgument("myarg").build()).build();

        Parser p = ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                primary,
                OptionBuilder
                    .forOption("-s", "--secondary")
                    .requires(primary)
                .build()
            )
            .build();
        try {
            p.parse(new String[]{"-p", "primaryarg"});
        } catch (Exception e) {
            Assert.fail("Primary option can be passed alone");
        }

        try {
            p.parse(new String[]{"-p", "primaryarg", "-s"});
        } catch (Exception e) {
            Assert.fail("Both primary and secondary is correct");
        }

        p.parse(new String[]{"-s"});
        Assert.fail("-s depends on -p. This should fail");
    }

    @Test
    public void testMutualRequiredOptions() throws Exception {
        IOption option1 = OptionBuilder.forOption("-o", "--option1").build();
        IOption option2 = OptionBuilder.forOption("-O", "--option2").build();

        option1.addRequiredOption(option2);
        option2.addRequiredOption(option1);

        ParserBuilder
            .forOptionsBasedCli()
            .withOption(option1, option2)
            .build()
            .parse(new String[]{"-o", "-O"});

    }
}
