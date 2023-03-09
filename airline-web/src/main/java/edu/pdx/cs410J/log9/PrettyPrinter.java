package edu.pdx.cs410J.log9;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;

public class PrettyPrinter {
  private final Writer writer;

  @VisibleForTesting
  static String formatWordCount(int count )
  {
    return String.format( "Airline on server contains %d flights", count );
  }

  public PrettyPrinter(Writer writer) {
    this.writer = writer;
  }

  public void dump(Airline airline) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ) {

      pw.println(formatWordCount(airline.getFlights().size()));

      airline.getFlights().forEach(flight -> {
        String[] depart = flight.getDepartureString().split(" ", 2);
        String[] arrive = flight.getArrivalString().split(" ", 2);

        pw.println("Flight number " + flight.getNumber() + " will be departing from " + AirportNames.getName(flight.getSource()) + " at " + depart[1] + " on " + depart[0] + ".");
        pw.println("It will arrive in " + AirportNames.getName(flight.getDestination()) + " at " + arrive[1] + " on " + arrive[0] + ".\n");
      });

      pw.flush();
    }
  }

  public void dump(Airline airline, String src, String dest) {
    try (
            PrintWriter pw = new PrintWriter(this.writer)
    ) {

      AtomicInteger count = new AtomicInteger();

      pw.println(formatWordCount(airline.getFlights().size()));

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
        pw.println("No flights are currently going from " + src + " to " + dest);
      }

      pw.flush();
    }
  }
}
