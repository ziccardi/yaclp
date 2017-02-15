# YACLP - Yet Another Command Line Parser

 YACLP, as the name implies, is a set of classes that implements a command line parser.
 It was born as a subproject of JNRPE to solve the need to be able to easily configure command line option inter-dependencies.

## Where can I get the latest release?

The easiest way is to pull it from the central maven repositories:

```xml
<dependency>
    <groupId>it.jnrpe.yaclp</groupId>
    <artifactId>cli-parser</artifactId>
    <version>1.0.2</version>
</dependency>
```

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
// We need to save the reference to the conf option to be able to pass it
// as required option to the other options
IOption conf = OptionBuilder.forOption("-c", "--conf")                          // (1)
    .description("Specifies the JNRPE configuration file")
    .argument(ArgumentBuilder.forArgument("path").mandatory(true).build())
    .build();

Parser p = ParserBuilder
    .forOptionsBasedCli()                                                       // (2)
    .withOption(
        OptionBuilder
            .forMutuallyExclusiveOption() // help, version and conf are mutually exclusive
            .withOptions(                                                       // (3)
                OptionBuilder.forOption("-h", "--help")
                    .description("Print this help")
                    .build(),
                OptionBuilder.forOption("-v", "--version")
                    .description("Print the server version number")
                    .build(),
                conf
            )
            .mandatory(true)                                                    // (4)
            .build(),
        OptionBuilder
            .forMutuallyExclusiveOption() // interactive, list and helpPlugin are mutually exclusive
            .withOptions(
                OptionBuilder.forOption("-i", "--interactive")
                    .requires(conf)                                             // (5)
                    .description("Starts JNRPE in command line mode")
                    .build(),
                OptionBuilder.forOption("-l", "--list")
                    .requires(conf)
                    .description("Lists all the installed plugins")
                    .build(),
                OptionBuilder.forOption("-H", "--pluginHelp")
                    .requires(conf)
                    .description("Print help about a given plugin")
                    .build()
            ).build()
    ).build();
```

(1) Create the `conf` option with an argument called `path`. We need to save its reference so we will be able to 
configure other options that depends on it

(2) Instantiate a parser builder and start passing all the options. We are going to create a parser for option based
cli (YACLP supports command based cli as well)

(3) `help`, `version` and `conf` are mutually exclusive. We can pass them all together as in the example or we can 
invoke `withOptions` multiple times.

(4) Mutually exclusive option is mandatory: **one** of the enclosed options **must** be present.

(5) `interactive`, `list` and `pluginConf` requires the `conf` option

That's all. Now that we have a parser, we can process the command line:

```java
    try{
        CommandLine cl = p.parse(args);                         // (1)
    } catch (ParsingException pe) {
        System.out.println("Error - " + pe.getMessage());       // (2)

        HelpFormatter hf = new HelpFormatter("exanmple", p);    // (3)
        hf.printUsage(System.out);                              // (4)
        hf.printHelp(System.out);                               // (5)
    }
```

 (1) Parse the command line: a `CommandLine` object is returned if parsing succeeds.

 (2) If an error occurs, the received exception will contain a meaningful message,

 (3) YACLP provides an `HelpFormatter` object to help the user to create a meaningful help message.
 
 (4) `HelpFormatter` provides a mean to print the usage of the tool. In this case it will print:
 
 `example [--help (-h) | --version (-v) | --conf (-c) <path>] [--interactive (-i) | --list (-l) | --pluginHelp (-H)]`
 
 (5) The `printHelp` method will output a nice and helpful message. In this case:
 
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

        // We need to save the reference to the conf option to be able to pass it
        // as required option to the other options
        IOption conf = OptionBuilder.forOption("-c", "--conf")
            .description("Specifies the JNRPE configuration file")
            .argument(ArgumentBuilder.forArgument("path").mandatory(true).build())
            .build();

        Parser p = ParserBuilder
            .forOptionsBasedCli()
            .withOption(
                OptionBuilder
                    .forMutuallyExclusiveOption() // help, version and conf are mutually exclusive
                    .withOptions(
                        OptionBuilder.forOption("-h", "--help")
                            .description("Print this help")
                            .build(),
                        OptionBuilder.forOption("-v", "--version")
                            .description("Print the server version number")
                            .build(),
                        conf
                    )
                    .mandatory(true)
                    .build(),
                OptionBuilder
                    .forMutuallyExclusiveOption() // interactive, list and helpPlugin are mutually exclusive
                    .withOptions(
                        OptionBuilder.forOption("-i", "--interactive")
                            .requires(conf)
                            .description("Starts JNRPE in command line mode")
                            .build(),
                        OptionBuilder.forOption("-l", "--list")
                            .requires(conf)
                            .description("Lists all the installed plugins")
                            .build(),
                        OptionBuilder.forOption("-H", "--pluginHelp")
                            .requires(conf)
                            .description("Print help about a given plugin")
                            .build()
                    ).build()
            ).build();


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
