package it.yaclp;

import java.util.List;

/**
 * Created by ziccardi on 10/01/2017.
 */
public interface IOption {
    public boolean isPresent(List<String> args);

    public void consume(List<String> args, Result res);

    public void addRequiredOption(IOption option);

    public String getShortName();
    public String getLongName();

    public void setArgument(Argument arg);

    public boolean isMandatory();
}
