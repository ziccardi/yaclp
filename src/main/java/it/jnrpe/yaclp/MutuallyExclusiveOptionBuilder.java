package it.jnrpe.yaclp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziccardi on 11/01/2017.
 */
public class MutuallyExclusiveOptionBuilder {
    private List<IOption> options = new ArrayList<IOption>();
    private boolean mandatory = false;
    private String description = "";

    MutuallyExclusiveOptionBuilder() {
    }

    public MutuallyExclusiveOptionBuilder withOption(final IOption option) {
        this.options.add(option);
        return this;
    }

    public MutuallyExclusiveOptionBuilder mandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public MutuallyExclusiveOptionBuilder description(final String description) {
        this.description = description;
        return this;
    }

    public IOption build() {
        MutuallyExclusiveOptions opt = new MutuallyExclusiveOptions(options.toArray(new IOption[options.size()]));
        opt.setMandatory(mandatory);
        opt.setDescription(description);

        return opt;
    }
}
