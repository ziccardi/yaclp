package it.jnrpe.yaclp;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class PropertyOptionTest {

    private Parser createParser() {
        return createParser("=");
    }

    private Parser createParser(final String separator) {
        IOption option = OptionBuilder.forPropertyOption("-D").description("Properties").withValueSeparator(separator).build();

        Parser p = new Parser();
        p.addOption(option);
        return p;
    }

    @Test
    public void testOneOption() throws Exception {
        CommandLine cl = createParser().parse(new String[]{"-D", "key=value"});
        Properties props = cl.getProperties("-D");

        Assert.assertNotNull(props);
        Assert.assertEquals("value", props.getProperty("key"));
    }

    @Test
    public void testManyOptions() throws Exception {
        CommandLine cl = createParser().parse(new String[]{"-D", "key=value", "-D", "key1=value1", "-D", "key2=value2"});
        Properties props = cl.getProperties("-D");

        Assert.assertNotNull(props);
        Assert.assertEquals("value", props.getProperty("key"));
        Assert.assertEquals("value1", props.getProperty("key1"));
        Assert.assertEquals("value2", props.getProperty("key2"));
    }

    @Test
    public void testManyOptionsNoSpace() throws Exception {
        CommandLine cl = createParser().parse(new String[]{"-Dkey=value", "-Dkey1=value1", "-Dkey2=value2"});
        Properties props = cl.getProperties("-D");

        Assert.assertNotNull(props);
        Assert.assertEquals("value", props.getProperty("key"));
        Assert.assertEquals("value1", props.getProperty("key1"));
        Assert.assertEquals("value2", props.getProperty("key2"));
    }

    @Test
    public void testManyOptionsCustomSeparator() throws Exception {
        CommandLine cl = createParser("+").parse(new String[]{"-Dkey+value", "-Dkey1+value1", "-Dkey2+value2"});
        Properties props = cl.getProperties("-D");

        Assert.assertNotNull(props);
        Assert.assertEquals("value", props.getProperty("key"));
        Assert.assertEquals("value1", props.getProperty("key1"));
        Assert.assertEquals("value2", props.getProperty("key2"));
    }
}
