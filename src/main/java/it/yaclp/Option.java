package it.yaclp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziccardi on 10/01/2017.
 */
public class Option implements IOption {
    private List<IOption> requiredOptions = new ArrayList<IOption>();

    private final String shortName;

    public Option(final String shortName) {
        this.shortName = shortName;
    }

    public boolean isPresent(List<String> args) {
        return args.contains(shortName);
    }

    public void consume(List<String> args, Result res) {
        if (!isPresent(args)) {
            return;
        }
        for (IOption requiredOption : requiredOptions) {
            if (requiredOption.isPresent(args)) {
                requiredOption.consume(args, res);
            } else {
                if (res.hasOption(requiredOption.getShortName())) {
                    continue;
                }
                throw new IllegalStateException(shortName + " requires " + requiredOption.getShortName());
            }
        }

        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).equals(shortName)) {
                // consume and manage it
                args.remove(i);
                res.addValue(shortName, "true");
                consume(args, res);
            }
        }
    }

    public void addRequiredOption(IOption option) {
        requiredOptions.add(option);
    }

    public String getShortName() {
        return shortName;
    }
}
