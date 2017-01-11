package it.jnrpe.yaclp;

public class OptionBuilder {

    public static SimpleOptionBuilder forOption(final String shortName, final String longName){
        return new SimpleOptionBuilder(shortName, longName);
    }

    public static MutuallyExclusiveOptionBuilder forMutuallyExclusiveOption(){
        return new MutuallyExclusiveOptionBuilder();
    }
}
