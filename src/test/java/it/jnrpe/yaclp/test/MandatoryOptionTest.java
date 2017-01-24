package it.jnrpe.yaclp.test;

import it.jnrpe.yaclp.*;
import org.junit.Test;

public class MandatoryOptionTest {

    @Test(expected = ParsingException.class)
    public void testMissingMandatoryBeginning() throws Exception {
        ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                OptionBuilder.forOption("-m", "--mandatory").mandatory(true).build(),
                OptionBuilder.forOption("-o", "--optional").build()
            )
            .build()
            .parse(new String[]{"-o"});
    }

    @Test(expected = ParsingException.class)
    public void testMissingMandatoryEnding() throws Exception {
        ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                OptionBuilder.forOption("-o", "--optional").build(),
                OptionBuilder.forOption("-m", "--mandatory").mandatory(true).build()
            )
            .build()
            .parse(new String[]{"-o"});
    }
}
