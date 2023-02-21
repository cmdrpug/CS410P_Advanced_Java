package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * An integration test for the {@link Project4} main class.
 */
class ConverterIT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project4} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Converter.class, args );
    }

    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test
    void testNoCommandLineArguments() {
        MainMethodResult result = invokeMain();
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }

    @Test
    void testTooFewCommandLineArguments() {
        MainMethodResult result = invokeMain("converterTest.txt");
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }

    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("converterTest.txt", "converterTest.xml", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }

    @Test
    void correctPath(){
        MainMethodResult result = invokeMain("converterTest.txt", "converterTest.xml");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Good arguments"));
    }
}