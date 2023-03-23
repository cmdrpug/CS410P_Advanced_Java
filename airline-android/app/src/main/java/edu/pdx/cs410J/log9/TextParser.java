package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;

import static edu.pdx.cs410J.log9.Project6.formatDateAndTime;
import static java.lang.Integer.parseInt;

/**
 * The TextParser class implements AirlineParser interface.
 * It is used to read files that are formatted by the TextDumper
 * class back into memory during runtime.
 */
public class TextParser implements AirlineParser<Airline> {
  private final Reader reader;

  /**
   * Constructor for the Parser. Just assigns the FileReader to the reader variable
   *
   * @param reader The FileReader that will be read from
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * The Parse method first just checks if the file is empty. If it is
   * it throws a Parser exception. Then is goes into a while loop filling each element
   * of an array with a line from the file until it reaches 7. At that point it takes
   * the array and parses those elements. If there is a problem with an element, a
   * ParserException is thrown. If it succeeds, a flight is created and added to the airline
   * before the array is cleared and the loop continues until the end of file.
   *
   * @return The airline on successful parsing
   * @throws ParserException any time that a read in line doesn't match the format
   */
  @Override
  public Airline parse() throws ParserException {
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

      String airlineName = br.readLine();

      if (airlineName == null) {
        throw new ParserException("Missing airline name");
      }

      Airline airline = new Airline(airlineName);

      try{
        String currentLine;
        int counter = 0;
        String[] args = {"", "", "", "", "", "", ""};
        while ((currentLine = br.readLine()) != null){
          args[counter % 7] = currentLine;
          ++counter;
          if(counter % 7 == 0){
            int argCounter = 0;
            int flightNumber = 0;
            try{
              flightNumber = parseInt(args[argCounter]);
            } catch (NumberFormatException err){
              throw new ParserException("Number expected on line " + (counter + (argCounter - 5)));
            }
            ++argCounter;

            String src = "";
            if(args[argCounter].length() != 3){
              throw new ParserException("Source is not 3 letters long on line " + (counter + (argCounter - 5)));
            } else if(!(args[argCounter].matches("[a-zA-Z]+"))){
              throw new ParserException("Source can only include letters on line " + (counter + (argCounter - 5)));
            } else if(AirportNames.getName(args[argCounter]) == null) {
              throw new ParserException("Source code must be a known airport on line " + (counter + (argCounter - 5)));
            } else{
              src = args[argCounter].toUpperCase();
            }
            ++argCounter;

            String departDate = args[argCounter];
            String departTime = args[argCounter+1];
            argCounter += 2;
            Date depart = formatDateAndTime(departDate, departTime, "depart");
            if(depart == null){
              throw new ParserException("Departure is formatted incorrectly on lines " + (counter + (argCounter - 7)) + " and " + (counter + (argCounter - 6)));
            }

            String dest = "";
            if(args[argCounter].length() != 3){
              throw new ParserException("Destination is not 3 letters long on line " + (counter + (argCounter - 5)));
            } else if(!(args[argCounter].matches("[a-zA-Z]+"))){
              throw new ParserException("Destination can only include letters on line " + (counter + (argCounter - 5)));
            } else if(AirportNames.getName(args[argCounter]) == null) {
              throw new ParserException("Source code must be a known airport on line " + (counter + (argCounter - 5)));
            } else{
              dest = args[argCounter].toUpperCase();
            }
            ++argCounter;

            String arriveDate = args[argCounter];
            String arriveTime = args[argCounter+1];
            argCounter += 2;
            Date arrive = formatDateAndTime(arriveDate, arriveTime, "arrive");
            if(arrive == null){
              throw new ParserException("Arrival is formatted incorrectly on lines " + (counter + (argCounter - 7)) + " and " + (counter + (argCounter - 6)));
            }

            if(depart.after(arrive)){
              throw new ParserException("Departure time is after arrival time for the flight starting on line " + counter);
            }

            Flight flight = new Flight(src, dest, depart, arrive, flightNumber);
            airline.addFlight(flight);
            for(int i = 0; i < args.length; ++i){
              args[i] = "";
            }
          }
        }
      } catch (IOException e) {
        throw new ParserException("While parsing airline text", e);
      }

      return airline;

    } catch (IOException e) {
      throw new ParserException("While parsing airline text", e);
    }
  }
}
