package it.yaclp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    private List<IOption> options = new ArrayList<IOption>();

    public void addOption(IOption option) {
        this.options.add(option);
    }

    public void parse(String[] args) throws ParsingException{
        List<String> argsList = new LinkedList<String>(Arrays.asList(args));

        Result res = new Result();
        for (IOption opt: options) {
            opt.consume(argsList, res);
        }

        if (!argsList.isEmpty()) {
            throw new ParsingException("Unexpected tokens: " + argsList);
        }
    }

    public static void main(String[] args) throws ParsingException {

        Option version = new Option("-v", "--version");
        Option help = new Option("-h", "--help");
        Option conf = new Option("-c", "--conf");
        conf.setArgument(new Argument(true));

        MutuallyExclusiveOptions rootOpts = new MutuallyExclusiveOptions(help, version, conf);
        rootOpts.setMandatory(true);

        Option interactive = new Option("-i", "--interactive");
        interactive.addRequiredOption(conf);

        Option list = new Option("-l", "--list");
        list.addRequiredOption(conf);

        Option helpPlugin = new Option("-H", "--pluginHelp");
        helpPlugin.addRequiredOption(conf);

        MutuallyExclusiveOptions exclOpts = new MutuallyExclusiveOptions(interactive, list, helpPlugin);

        Parser p = new Parser();
        p.addOption(rootOpts);
        p.addOption(exclOpts);

        //p.parse(new String[]{"--conf", "myconf.ini", "--pluginHelp"});
        p.parse(new String[]{"--conf", "myconf.ini", "--pluginHelp", "--list"});
        //p.parse(new String[]{});
    }
}
