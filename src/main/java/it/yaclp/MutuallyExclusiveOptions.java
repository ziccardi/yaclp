package it.yaclp;

import java.util.List;

/**
 * Created by ziccardi on 10/01/2017.
 */
public class MutuallyExclusiveOptions implements IOption {

    private IOption[] options;

    public MutuallyExclusiveOptions (IOption... options) {
        this.options = options;
    }

    public void addRequiredOption(IOption option) {

    }

    public void consume(List<String> args, Result res) {
        // Only one must be present...
        IOption passedInOption = null;

        for (IOption opt : options) {
            if (opt.isPresent(args)) {
                if (passedInOption != null) {
                    throw new IllegalStateException("Incompatible options present");
                } else {
                    passedInOption = opt;
                }
            }
        }

        // Ok, only at most one is present...
        if (passedInOption != null) {
            passedInOption.consume(args, res);
        }
    }

    public boolean isPresent(List<String> args) {
        boolean present = false;

        for (IOption opt : options) {
            present |= opt.isPresent(args);
        }

        return present;
    }

    public String getShortName() {
        return null;
    }
}
