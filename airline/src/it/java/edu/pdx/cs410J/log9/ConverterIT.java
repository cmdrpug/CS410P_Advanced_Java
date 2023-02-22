package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import static edu.pdx.cs410J.log9.Project4.formatDateAndTime;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * An integration test for the {@link Converter} main class.
 */
class ConverterIT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Converter} with the given arguments.
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

    /**
     * Tests that invoking the main method with too few arguments issues an error
     */
    @Test
    void testTooFewCommandLineArguments() {
        MainMethodResult result = invokeMain("converterTest.txt");
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }

    /**
     * Tests that invoking the main method with too many arguments issues an error
     */
    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("converterTest.txt", "converterTest.xml", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }

    /**
     * if first argument isn't a txt file, error
     */
    @Test
    void firstArgNotTxt() {
        MainMethodResult result = invokeMain("converterTest", "converterTest.xml");
        assertThat(result.getTextWrittenToStandardError(), containsString("The first argument must be a .txt file"));
    }

    /**
     * if second argument isn't a .xml file, error
     */
    @Test
    void SecondArgNotXml() {
        MainMethodResult result = invokeMain("converterTest.txt", "converterTest");
        assertThat(result.getTextWrittenToStandardError(), containsString("The second argument must be a .xml file"));
    }

    /**
     * both arguments are correct
     * @throws IOException if the dates in the test are wrong
     */
    @Test
    void mainPathTest() throws IOException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);
        airline.addFlight(new Flight("LAX", "PDX", formatDateAndTime("12/12/2005", "10:32 AM", "depart"), formatDateAndTime("12/13/2005", "11:11 PM", "arrive"), 8400));
        TextDumper textDumper = new TextDumper(new FileWriter("converterTest.txt"));
        textDumper.dump(airline);
        MainMethodResult result = invokeMain("converterTest.txt", "converterTest.xml");
    }
}