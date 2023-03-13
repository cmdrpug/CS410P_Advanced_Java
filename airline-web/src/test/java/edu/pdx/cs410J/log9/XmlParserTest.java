package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static edu.pdx.cs410J.log9.Project5.formatDateAndTime;

/**
 * Unit tests for the {@link XmlParser} class.*
 */
public class XmlParserTest {

    /**
     * creates an airline, dumps it to xml, reads it back from xml and dumps it in a different file
     * @throws IOException if the date is malformed
     * @throws ParserException if fails to parse the file
     */
    @Test
    void airlineXmlParserTest() throws IOException, ParserException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);
        airline.addFlight(new Flight("LAX", "PDX", formatDateAndTime("12/12/2005", "10:32 AM", "depart"), formatDateAndTime("12/13/2005", "11:11 PM", "arrive"), 8400));
        XmlDumper dump = new XmlDumper(new FileWriter("xmltest.xml"));
        dump.dump(airline);

        XmlParser xmlParser = new XmlParser(new File("xmltest.xml"));
        Airline airline2 = xmlParser.parse();
        XmlDumper dump2 = new XmlDumper(new FileWriter("xmltest2.xml"));
        dump2.dump(airline2);
    }
}
