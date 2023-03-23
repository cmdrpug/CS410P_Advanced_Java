package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AirportNames;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PrettyPrinter for printing flights to console during searches
 */
public class PrettyPrinter {
  private final Writer writer;

  /**
   * Just sets the writing to a variable so the printer knows where to write
   *
   * @param writer where to write to
   */
  public PrettyPrinter(Writer writer) {
    this.writer = writer;
  }

  /**
   * Pretty prints every flight in the airline to the writer
   *
   * @param airline the airline to print
   */
  public void dump(Airline airline) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ) {

      AtomicInteger count = new AtomicInteger(1);

      pw.println("The airline, " + airline.getName() + ", contains " + airline.getFlights().size() + " flights matching your search");

      airline.getFlights().forEach(flight -> {
        String[] depart = flight.getDepartureString().split(" ", 2);
        String[] arrive = flight.getArrivalString().split(" ", 2);

        pw.println(count + ".");
        pw.println("Flight number: " + flight.getNumber());
        pw.println("Departing airport: " + AirportNames.getName(flight.getSource()));
        pw.println("Departing date & time: " + depart[1] + " on " + depart[0] + ".");
        pw.println("Arriving airport: " + AirportNames.getName(flight.getDestination()));
        pw.println("Arriving date & time: " + arrive[1] + " on " + arrive[0] + ".\n");
        count.incrementAndGet();
      });

      pw.flush();
    }
  }

  /**
   * Pretty prints every flight in the airline to the writer that matches
   * src and dest. If no flights match src and dest, a string saying so will
   * be printed instead.
   *
   * @param airline the airline to print
   * @param src the src to search by
   * @param dest the destination to search by
   */
  public void dump(Airline airline, String src, String dest) {
    try (
            PrintWriter pw = new PrintWriter(this.writer)
    ) {

      AtomicInteger count = new AtomicInteger(1);

      pw.println("The airline, " + airline.getName() + ", on server contains " + airline.getFlights().size() + " flights that match your search");
      pw.println("----------------------------------");

      airline.getFlights().forEach(flight -> {
        String[] depart = flight.getDepartureString().split(" ", 2);
        String[] arrive = flight.getArrivalString().split(" ", 2);

        if(src.equals(flight.getSource()) && dest.equals(flight.getDestination())) {
          pw.println(count + ".");
          pw.println("Flight number: " + flight.getNumber());
          pw.println("Departing airport: " + AirportNames.getName(flight.getSource()));
          pw.println("Departing date & time: " + depart[1] + " on " + depart[0] + ".");
          pw.println("Arriving airport: " + AirportNames.getName(flight.getDestination()));
          pw.println("Arriving date & time: " + arrive[1] + " on " + arrive[0] + ".\n");
          count.incrementAndGet();
        }
      });

      pw.flush();
    }
  }
}
