package it.jnrpe.yaclp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    private List<IOption> options = new ArrayList<IOption>();

    public void addOption(IOption option) {
        this.options.add(option);
    }

    List<IOption> getOptions() {
        return options;
    }

    public CommandLine parse(String[] args) throws ParsingException{
        List<String> argsList = new LinkedList<String>(Arrays.asList(args));

        CommandLine res = new CommandLine();
        for (IOption opt: options) {
            opt.consume(argsList, res);
        }

        if (!argsList.isEmpty()) {
            throw new ParsingException("Unexpected tokens: " + argsList);
        }

        return res;
    }

    public static void main(String[] args) {

        IOption version = OptionBuilder.forOption("-v", "--version")
            .description("Print the server version number")
            .build();

        IOption help = OptionBuilder.forOption("-h", "--help")
            .description("Print this help")
            .build();

        IOption conf = OptionBuilder.forOption("-c", "--conf")
            .description("Specifies the JNRPE configuration file")
            .argument(ArgumentBuilder.forArgument("path").mandatory(true).build())
            .build();

        IOption rootOpts = OptionBuilder.forMutuallyExclusiveOption()
            .withOption(help)
            .withOption(version)
            .withOption(conf)
            .mandatory(true)
            .build();

        IOption interactive = OptionBuilder.forOption("-i", "--interactive")
            .requires(conf)
            .description("Starts JNRPE in command line mode")
            .build();

        IOption list = OptionBuilder.forOption("-l", "--list")
            .requires(conf)
            .description("Lists all the installed plugins")
            .build();

        IOption helpPlugin = OptionBuilder.forOption("-H", "--pluginHelp")
            .requires(conf)
            .description("Print help about a given plugin")
            .build();

        IOption exclOpts = OptionBuilder.forMutuallyExclusiveOption()
            .withOption(interactive)
            .withOption(list)
            .withOption(helpPlugin)
            .build();

        Parser p = new Parser();
        p.addOption(rootOpts);
        p.addOption(exclOpts);







        try {
            //CommandLine cl = p.parse(new String[]{"--conf", "myconf.ini", "--pluginHelp"});
            CommandLine cl = p.parse(new String[]{"--conf"});
        } catch (ParsingException pe) {
            System.out.println("Error - " + pe.getMessage());

            HelpFormatter hf = new HelpFormatter("test", p);
            hf.printUsage(System.out);
            hf.printHelp(System.out);

        }

        //p.parse(new String[]{"--conf", "myconf.ini", "--pluginHelp", "--list"});
        //p.parse(new String[]{});

//        System.out.println (cl.getValues("--conf")[0]);
//        System.out.println (cl.getValue("-c"));

    }
}
