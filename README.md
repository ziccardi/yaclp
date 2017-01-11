# YACLP - Yet Another Command Line Parser

 YACLP, as the name implies, is a set of classes that implements a command line parser.
 It was born as a subproject of JNRPE to solve the need to be able to easily configure command line option inter-dependencies.
 
## Using YACLP

 To use YACLP you will have to deal mainly with 4 classes:
 
 * `OptionBuilder`: this will be used to configure all the options you will need to parse
 * `ArgumentBuilder`: use this to configure the option arguments
 * `Parser`: use this to perform the parsing of the received command line
 
 The `OptionBuilder` will allow you to create two kind of options: simple options and mutually exclusive options.
 Let's see how that work through a simple example: the JNRPE command line:
 
 
 ```
 Usage:
   test [--help (-h) | --version (-v) | --conf (-c) <path>] [--interactive (-i) | --list (-l) | --pluginHelp (-H)] 
     --help (-h)           Print this help 
     --version (-v)        Print the server version number 
     --conf (-c) <path>    Specifies the JNRPE configuration file 
     --interactive (-i)    Starts JNRPE in command line mode 
     --list (-l)           Lists all the installed plugins 
     --pluginHelp (-H)     Print help about a given plugin 
 ```
 
 In the command line depicted above:
 
  * `help` and `version` cannot be used together with any other option
  * `interactive`, `list` and `pluginHelp` cannot be used together. Moreover, each of them requires `conf`
  * `conf` is required to run JNRPE and gives the path to the configuration file.
  
 Let's configure a parser for that command line.
 
 ```java
        IOption help = OptionBuilder.forOption("-h", "--help")
            .description("Print this help")
            .build();

        IOption version = OptionBuilder.forOption("-v", "--version")
            .description("Print the server version number")
            .build();

        IOption conf = OptionBuilder.forOption("-c", "--conf")
            .description("Specifies the JNRPE configuration file")
            .argument(ArgumentBuilder.forArgument("path").mandatory(true).build())  // (1)
            .build();

        // help, version and conf are mutually exclusive
        IOption mutuallyExclusive1 = OptionBuilder.forMutuallyExclusiveOption()     // (2)
            .withOption(help)
            .withOption(version)
            .withOption(conf)
            .mandatory(true)                                                        // (3)
            .build();
        
        IOption interactive = OptionBuilder.forOption("-i", "--interactive")
            .requires(conf)                                                         // (4)
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

        // interactive, list and helpPlugin are mutually exclusive
        IOption mutuallyExclusive2 = OptionBuilder.forMutuallyExclusiveOption()
            .withOption(interactive)
            .withOption(list)
            .withOption(helpPlugin)
            .build();
```

(1) Create the `conf` option with an argument called `path`

(2) `help`, `version` and `conf` are mutually exclusive

(3) Mutually exclusive option is mandatory: **one** of the enclosed options **must** be present.

(4) `interactive` requires the `conf` option

That's all. Now that we have all the options, we can parse the command line:

```java
    Parser p = new Parser();
    p.addOption(mutuallyExclusive1);                            // (1)
    p.addOption(mutuallyExclusive2);                        

    try{
        CommandLine cl = p.parse(args);                         // (2)
    } catch (ParsingException pe) {
        System.out.println("Error - " + pe.getMessage());       // (3)

        HelpFormatter hf = new HelpFormatter("exanmple", p);    // (4)
        hf.printUsage(System.out);                              // (5)
        hf.printHelp(System.out);                               // (6)
    }
```

 (1) Pass the configured options to the parser. We don't need to pass the enclosed ones.

 (2) Parse the command line: a `CommandLine` object is returned if parsing succeeds.

 (3) If an error occurs, the received exception will contain a meaningful message,

 (4) YACLP provides an `HelpFormatter` object to help the user to create a meaningful help message.
 
 (5) `HelpFormatter` provides a mean to print the usage of the tool. In this case it will print:
 
 `example [--help (-h) | --version (-v) | --conf (-c) <path>] [--interactive (-i) | --list (-l) | --pluginHelp (-H)]`
 
 (6) The `printHelp` method will output a nice and helpful message. In this case:
 
 ```
    --help (-h)           Print this help 
    --version (-v)        Print the server version number 
    --conf (-c) <path>    Specifies the JNRPE configuration file 
    --interactive (-i)    Starts JNRPE in command line mode 
    --list (-l)           Lists all the installed plugins 
    --pluginHelp (-H)     Print help about a given plugin 
 ```
 
## Wrapping all together
 
 Below is the complete code
 
 ```java
package it.jnrpe.yaclp.example;

import it.jnrpe.yaclp.*;

public class YaclpExample {
    public static void main(String[] args) {
        IOption help = OptionBuilder.forOption("-h", "--help")
            .description("Print this help")
            .build();

        IOption version = OptionBuilder.forOption("-v", "--version")
            .description("Print the server version number")
            .build();

        IOption conf = OptionBuilder.forOption("-c", "--conf")
            .description("Specifies the JNRPE configuration file")
            .argument(ArgumentBuilder.forArgument("path").mandatory(true).build())
            .build();

        // help, version and conf are mutually exclusive
        IOption mutuallyExclusive1 = OptionBuilder.forMutuallyExclusiveOption()
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

        IOption mutuallyExclusive2 = OptionBuilder.forMutuallyExclusiveOption()
            .withOption(interactive)
            .withOption(list)
            .withOption(helpPlugin)
            .build();

        Parser p = new Parser();
        p.addOption(mutuallyExclusive1);
        p.addOption(mutuallyExclusive2);

        try{
            p.parse(args);
        } catch (ParsingException pe) {
            System.out.println("Error - " + pe.getMessage());

            HelpFormatter hf = new HelpFormatter("test", p);
            hf.printUsage(System.out);
            hf.printHelp(System.out);
        }
    }
}
```