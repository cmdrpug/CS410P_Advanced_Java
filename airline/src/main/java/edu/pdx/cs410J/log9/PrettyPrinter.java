package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

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
     * The Airline is written nicely at the top and then each
     * flight is printed in two line, one for departure and one for arrival.
     * The full names of the locations are used instead of airport codes.
     *
     * @param airline the airline to print
     */
    @Override
    public void dump(Airline airline) {
        try (
                PrintWriter pw = new PrintWriter(this.writer)
        ) {
            pw.println("Flights belonging to " + airline.getName() + ":\n");

            airline.getFlights().forEach(flight -> {
                String[] depart = flight.getDepartureString().split(" ", 2);
                String[] arrive = flight.getArrivalString().split(" ", 2);

                pw.println("Flight number " + flight.getNumber() + " will be departing from " + AirportNames.getName(flight.getSource()) + " at " + depart[1] + " on " + depart[0] + ".");
                pw.println("It will arrive in " + AirportNames.getName(flight.getDestination()) + " at " + arrive[1] + " on " + arrive[0] + ".\n");
            });

            pw.flush();
        }
    }
}
