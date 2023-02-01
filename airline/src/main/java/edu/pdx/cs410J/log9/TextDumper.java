package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * The TextDumper class implements AirlineDumper interface.
 * It is used to create files in a format that can be read by the
 * TextParser on future runs of the program
 */
public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;

  /**
   * Constructor for the dumper. Just assigns the FileWriter to the writer variable
   *
   * @param writer the FileWriter that will be used to write the Airline
   */
  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * First the airline name is printed on the first line of the file. Then
   * for each flight in the airline, the depart and arrive strings are split
   * and then all fields of the flight are written to the file.
   *
   * @param airline the Airline object to be dumped into a file
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
