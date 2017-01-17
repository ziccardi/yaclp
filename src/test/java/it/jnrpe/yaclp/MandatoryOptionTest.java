package it.jnrpe.yaclp;

import org.junit.Test;

public class MandatoryOptionTest {

    @Test(expected = ParsingException.class)
    public void testMissingMandatoryBeginning() throws Exception {
        IOption mandatory = OptionBuilder.forOption("-m", "--mandatory").mandatory(true).build();
        IOption optional = OptionBuilder.forOption("-o", "--optional").build();

        Parser p = new Parser();
        p.addOption(mandatory);
        p.addOption(optional);

        p.parse(new String[]{"-o"});
    }

    @Test(expected = ParsingException.class)
    public void testMissingMandatoryEnding() throws Exception {
        IOption mandatory = OptionBuilder.forOption("-m", "--mandatory").mandatory(true).build();
        IOption optional = OptionBuilder.forOption("-o", "--optional").build();

        Parser p = new Parser();
        p.addOption(optional);
        p.addOption(mandatory);

        p.parse(new String[]{"-o"});
    }
}
