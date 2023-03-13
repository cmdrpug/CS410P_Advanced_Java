package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.Date;

import static edu.pdx.cs410J.log9.Project5.formatDateAndTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit tests for the {@link PrettyPrinter} class.*
 */
public class PrettyPrinterTest {

    /**
     * Simple test to see if the PrettyPrinter can write a string back
     */
    @Test
    void airlineNameIsDumpedInTextFormat() {
        String airlineName = "Test Airline";
        Date depart = formatDateAndTime("1/2/2005", "1:55 AM", "depart");
        Date arrive = formatDateAndTime("12/12/2005", "11:19 PM", "arrive");
        Flight flight = new Flight("PDX", "LAX", depart, arrive, 2854);

        Airline airline = new Airline(airlineName);
        airline.addFlight(flight);

        StringWriter sw = new StringWriter();
        PrettyPrinter dumper = new PrettyPrinter(sw);
        dumper.dump(airline);

        String text = sw.toString();
        assertThat(text, containsString(airlineName));
    }

    @Test
    void airlineNameIsDumpedInTextFormatWithSorting() {
        String airlineName = "Test Airline";
        Date depart = formatDateAndTime("1/2/2005", "1:55 AM", "depart");
        Date arrive = formatDateAndTime("12/12/2005", "11:19 PM", "arrive");
        Flight flight = new Flight("PDX", "LAX", depart, arrive, 2854);

        Airline airline = new Airline(airlineName);
        airline.addFlight(flight);

        StringWriter sw = new StringWriter();
        PrettyPrinter dumper = new PrettyPrinter(sw);
        dumper.dump(airline, "LAX", "PDX");
        dumper.dump(airline, "LAX", "LAX");
        dumper.dump(airline, "PDX", "PDX");
        dumper.dump(airline, "PDX", "LAX");

        String text = sw.toString();
        assertThat(text, containsString(airlineName));
    }
}