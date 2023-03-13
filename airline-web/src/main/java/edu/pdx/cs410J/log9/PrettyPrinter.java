package edu.pdx.cs410J.log9;

import com.google.common.annotations.VisibleForTesting;
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
   * Formats a string to print first when calling dump
   *
   * @param count the number of flights in the airline
   * @param airlineName the name of the airline
   * @return a formatted string displaying basic info about the airline
   */
  @VisibleForTesting
  static String formatWordCount(int count, String airlineName)
  {
    return String.format( "The airline, " + airlineName + ", on server contains %d flights", count );
  }

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

      pw.println(formatWordCount(airline.getFlights().size(), airline.getName()));

      airline.getFlights().forEach(flight -> {
        String[] depart = flight.getDepartureString().split(" ", 2);
        String[] arrive = flight.getArrivalString().split(" ", 2);

        pw.println("Flight number " + flight.getNumber() + " will be departing from " + AirportNames.getName(flight.getSource()) + " at " + depart[1] + " on " + depart[0] + ".");
        pw.println("It will arrive in " + AirportNames.getName(flight.getDestination()) + " at " + arrive[1] + " on " + arrive[0] + ".\n");
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

      AtomicInteger count = new AtomicInteger();

      pw.println(formatWordCount(airline.getFlights().size(), airline.getName()));

      airline.getFlights().forEach(flight -> {
        String[] depart = flight.getDepartureString().split(" ", 2);
        String[] arrive = flight.getArrivalString().split(" ", 2);

        if(src.equals(flight.getSource()) && dest.equals(flight.getDestination())) {
          pw.println("Flight number " + flight.getNumber() + " will be departing from " + AirportNames.getName(flight.getSource()) + " at " + depart[1] + " on " + depart[0] + ".");
          pw.println("It will arrive in " + AirportNames.getName(flight.getDestination()) + " at " + arrive[1] + " on " + arrive[0] + ".\n");
          count.incrementAndGet();
        }
      });

      if (count.get() == 0){
        pw.println("No " + airline.getName() + " flights are currently going from " + src + " to " + dest);
      }

      pw.flush();
    }
  }
}
