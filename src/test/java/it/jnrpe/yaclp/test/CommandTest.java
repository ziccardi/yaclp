package it.jnrpe.yaclp.test;

import it.jnrpe.yaclp.*;
import org.junit.Test;

public class CommandTest {

    private Parser buildParser() {
        return ParserBuilder.forCommandsBasedCli()
            .withCommands(
                CommandBuilder.forNewCommand("co", "checkout")
                    .withDescription("Switch branches or restore working tree files")
                    .withOption(
                        OptionBuilder.forOption("-b")
                        .argument(
                            ArgumentBuilder.forArgument("branch").mandatory(true).build()
                        ).build()
                    )
                    .build(),
                CommandBuilder.forNewCommand("clone")
                    .withDescription("Clone a repository into a new directory")
                    .withOption(OptionBuilder.forOption("-l").build()
                    )
                    .build()
            ).build();
    }

    @Test
    public void oneCommandHappyTest() throws ParsingException {
        buildParser().parse(new String[]{"checkout"});
    }

    @Test(expected = ParsingException.class)
    public void oneCommandFailTest() throws ParsingException {
        buildParser().parse(new String[]{"checkou1t"});
    }

    @Test
    public void commandWithOptionsTest() throws ParsingException {
        buildParser().parse(new String[]{"checkout", "-b", "mybranch"});
    }

    @Test(expected = ParsingException.class)
    public void commandConflictTest() throws ParsingException {
        buildParser().parse(new String[]{"checkout", "clone"});
    }

}
