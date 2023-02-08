package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

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
        Airline airline = new Airline(airlineName);

        StringWriter sw = new StringWriter();
        PrettyPrinter dumper = new PrettyPrinter(sw);
        dumper.dump(airline);

        String text = sw.toString();
        assertThat(text, containsString(airlineName));
    }

    /**
     * Tests if an airline can be written to a temporary file and retrieved by the {@link TextParser} class
     * Typically the text parser should not read these type of files but for this specific testing case it works
     *
     * @param tempDir The File for the Airline to be written to
     * @throws IOException if the file is not found
     * @throws ParserException if the written file does not match the correct format
     */
    @Test
    void canWriteToFile(@TempDir File tempDir) throws IOException, ParserException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);

        File textFile = new File(tempDir, "airline.txt");
        PrettyPrinter prettyPrinter = new PrettyPrinter(new FileWriter(textFile));
        prettyPrinter.dump(airline);

        TextParser parser = new TextParser(new FileReader(textFile));
        Airline read = parser.parse();
        assertThat(read.getName(), equalTo("Flights belonging to " + airlineName + ":"));
    }
}