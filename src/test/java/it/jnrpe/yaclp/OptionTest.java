package it.jnrpe.yaclp;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionalOption() throws Exception {
        IOption option = OptionBuilder.forOption("-o", "--optional")
            .build();

        List<String> args = new ArrayList<String>();
        option.consume(args, new CommandLine());
    }

    @Test(expected = ParsingException.class)
    public void testMandatoryOption() throws Exception {
        IOption option = OptionBuilder.forOption("-o", "--optional")
            .mandatory(true)
            .build();

        List<String> args = new ArrayList<String>();
        option.consume(args, new CommandLine());
    }

    @Test
    public void testSingleOption() throws Exception {
        IOption option = OptionBuilder.forOption("-m", "--mandatory")
            .mandatory(true)
            .argument(ArgumentBuilder.forArgument("myarg").build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--mandatory", "test"));
        CommandLine cl = new CommandLine();
        option.consume(args, cl);
        Assert.assertTrue(cl.hasOption(option.getShortName()));
        Assert.assertTrue(cl.hasOption(option.getLongName()));

        Assert.assertEquals("test", cl.getValue(option.getShortName()));
        Assert.assertEquals("test", cl.getValue(option.getLongName()));

        Assert.assertTrue(cl.getValues(option.getShortName()).length == 1);
    }

    @Test(expected = ParsingException.class)
    public void testUnrepetibleOption() throws Exception {
        IOption option = OptionBuilder.forOption("-m", "--mandatory")
            .mandatory(true)
            .argument(ArgumentBuilder.forArgument("myarg").build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--mandatory", "test", "--mandatory", "test"));
        option.consume(args, new CommandLine());
    }

    @Test
    public void testRepetibleOption() throws Exception {
        IOption option = OptionBuilder.forOption("-m", "--mandatory")
            .mandatory(true)
            .multiplicity(true)
            .argument(ArgumentBuilder.forArgument("myarg").build())
            .build();

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("--mandatory", "test", "--mandatory", "test1"));
        CommandLine cl = new CommandLine();
        option.consume(args, cl);

        Assert.assertTrue(cl.hasOption(option.getShortName()));
        Assert.assertTrue(cl.hasOption(option.getLongName()));

        Assert.assertEquals("test", cl.getValue(option.getShortName()));
        Assert.assertEquals("test", cl.getValue(option.getLongName()));

        Assert.assertEquals("test", cl.getValues(option.getShortName())[0]);
        Assert.assertEquals("test", cl.getValues(option.getLongName())[0]);
        Assert.assertEquals("test1", cl.getValues(option.getShortName())[1]);
        Assert.assertEquals("test1", cl.getValues(option.getLongName())[1]);


        Assert.assertTrue(cl.getValues(option.getShortName()).length == 2);
    }
}
