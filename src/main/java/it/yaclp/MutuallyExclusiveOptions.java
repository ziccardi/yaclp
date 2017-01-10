package it.yaclp;

import java.util.List;

/**
 * Created by ziccardi on 10/01/2017.
 */
public class MutuallyExclusiveOptions implements IOption {

    private IOption[] options;

    private boolean mandatory = false;

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
        } else {
            if (mandatory) {
                throw new IllegalStateException("Mandatory option missing");
            }
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

    public String getLongName() {
        return null;
    }

    public void setArgument(Argument arg) {

    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
}
