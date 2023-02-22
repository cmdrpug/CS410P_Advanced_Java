package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import static edu.pdx.cs410J.log9.Project4.formatDateAndTime;
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
    void airlineXmlHelperTest() throws IOException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);
        airline.addFlight(new Flight("LAX", "PDX", formatDateAndTime("12/12/2005", "10:32 AM", "depart"), formatDateAndTime("12/13/2005", "11:11 PM", "arrive"), 8400));
        XmlDumper dump = new XmlDumper(new FileWriter("xmltest.xml"));
        dump.dump(airline);
    }
}