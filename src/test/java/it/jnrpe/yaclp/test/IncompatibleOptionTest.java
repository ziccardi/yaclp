package it.jnrpe.yaclp.test;

import it.jnrpe.yaclp.*;
import org.junit.Test;

public class IncompatibleOptionTest {

    private Parser buildParser() {
        // Command line that we are going to test:
        // -a --aa --ab -b --ba --bb -c --ca --cb
        // --aa and --ab requires -a
        // --ba and --bb requires -b
        // --ca and --cb requires -c
        // -a can be used together with -c, but is incompatible with -b
        // -c can be used together with -b and -a

        // Define all the options
        // -a --aa --ab -b --ba --bb -c --ca --cb
        IOption a = OptionBuilder.forOption("-a").build();
        IOption aa = OptionBuilder.forOption("--aa").build();
        IOption ab = OptionBuilder.forOption("--ab").build();
        IOption b = OptionBuilder.forOption("-b").build();
        IOption ba = OptionBuilder.forOption("--ba").build();
        IOption bb = OptionBuilder.forOption("--bb").build();
        IOption c = OptionBuilder.forOption("-c").build();
        IOption ca = OptionBuilder.forOption("--ca").build();
        IOption cb = OptionBuilder.forOption("--cb").build();

        // --aa and --ab requires -a
        aa.addRequiredOption(a);
        ab.addRequiredOption(a);

        // --ba and --bb requires -b
        ba.addRequiredOption(b);
        bb.addRequiredOption(b);

        // --ca and --cb requires -c
        ca.addRequiredOption(c);
        cb.addRequiredOption(c);

        // -a can be used together with -c, but is incompatible with -b
        a.addIncompatibleOption(b);

        // -c can be used together with -b and -a
        // nothing to do here

        return ParserBuilder
            .forOptionsBasedCli()
            .withOption(a, aa, ab, b, ba, bb, c, ca, cb).build();
    }

    // --aa requires -a
    @Test(expected = ParsingException.class)
    public void test_aa_requires_a_failure() throws Exception {
        Parser p = buildParser();
        p.parse(new String[]{"--aa"});
    }

    // --ab requires -a
    @Test(expected = ParsingException.class)
    public void test_ab_requires_a_failure() throws Exception {
        Parser p = buildParser();
        p.parse(new String[]{"--ab"});
    }

    // --aa and --ab requires -a
    @Test
    public void test_aa_ab_requires_a_happy() throws Exception {
        Parser p = buildParser();
        p.parse(new String[]{"-a", "--ab", "--aa"});
    }

    // -a can be used together with -c
    @Test
    public void test_a_and_c_happy() throws Exception {
        Parser p = buildParser();
        p.parse(new String[]{"-a", "--ab", "--aa", "-c", "--ca", "--cb"});
    }

    // -a can be used together with -c, but is incompatible with -b
    @Test(expected = ParsingException.class)
    public void test_a_and_c_and_b_fail() throws Exception {
        Parser p = buildParser();
        p.parse(new String[]{"-a", "--ab", "--aa", "-c", "--ca", "--cb", "-b", "--ba", "--bb"});
    }

    // -c can be used together with -b and -a
    @Test
    public void test_b_and_c_happy() throws Exception {
        Parser p = buildParser();
        p.parse(new String[]{"-c", "--ca", "--cb", "-b", "--ba", "--bb"});
    }

}
