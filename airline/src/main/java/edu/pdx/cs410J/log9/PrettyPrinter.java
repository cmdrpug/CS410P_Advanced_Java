package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * The PrettyPrinter class implements AirlineDumper interface.
 *
 */
public class PrettyPrinter implements AirlineDumper<Airline> {
    private final Writer writer;

    /**
     * Constructor for the printer. Just assigns the FileWriter to the writer variable
     *
     * @param writer the FileWriter that will be used to write the Airline
     */
    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }

    /**
     *
     *
     * @param airline
     */
    @Override
    public void dump(Airline airline) {
        try (
                PrintWriter pw = new PrintWriter(this.writer)
        ) {
            pw.println(airline.getName());

            airline.getFlights().forEach(flight -> {
                String[] depart = flight.getDepartureString().split(" ", 2);
                String[] arrive = flight.getArrivalString().split(" ", 2);

                pw.println(flight.getNumber());
                pw.println(flight.getSource());
                pw.println(depart[0]);
                pw.println(depart[1]);
                pw.println(flight.getDestination());
                pw.println(arrive[0]);
                pw.println(arrive[1]);
            });

            pw.flush();
        }
    }
}
