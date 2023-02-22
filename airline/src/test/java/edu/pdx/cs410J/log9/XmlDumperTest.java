package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static edu.pdx.cs410J.log9.Project4.formatDateAndTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit tests for the {@link XmlDumper} class.*
 */
public class XmlDumperTest {
    /**
     * Tests if an airline can be written to a temporary file and retrieved by the {@link TextParser} class
     *
     * @throws IOException if the file is not found
     * @throws ParserException if the written file does not match the correct format
     */
    @Test
    void airlineXmlHelperTest() throws IOException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);
        airline.addFlight(new Flight("LAX", "PDX", formatDateAndTime("12/12/2005", "10:32 AM", "depart"), formatDateAndTime("12/13/2005", "11:11 PM", "arrive"), 8400));
        XmlDumper dump = new XmlDumper(new FileWriter("xmltest.xml"));
        dump.dump(airline);
    }
}
