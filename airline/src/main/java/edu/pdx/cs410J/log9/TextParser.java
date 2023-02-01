package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static edu.pdx.cs410J.log9.Project2.formatDateAndTime;
import static java.lang.Integer.parseInt;

/**
 * A skeletal implementation of the <code>TextParser</code> class for Project 2.
 */
public class TextParser implements AirlineParser<Airline> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

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
              throw new ParserException("File format is incorrect");
            }
            ++argCounter;

            String src = "";
            if(args[argCounter].length() != 3){
              throw new ParserException("File format is incorrect");
            } else if(!(args[argCounter].matches("[a-zA-Z]+"))){
              throw new ParserException("File format is incorrect");
            } else{
              src = args[argCounter].toUpperCase();
            }
            ++argCounter;

            String departDate = args[argCounter];
            String departTime = args[argCounter+1];
            argCounter += 2;
            String depart = formatDateAndTime(departDate, departTime, "depart");
            if(depart == null){
              return null;
            }

            String dest = "";
            if(args[argCounter].length() != 3){
              throw new ParserException("File format is incorrect");
            } else if(!(args[argCounter].matches("[a-zA-Z]+"))){
              throw new ParserException("File format is incorrect");
            } else{
              dest = args[argCounter].toUpperCase();
            }
            ++argCounter;

            String arriveDate = args[argCounter];
            String arriveTime = args[argCounter+1];
            argCounter += 2;
            String arrive = formatDateAndTime(arriveDate, arriveTime, "arrive");
            if(arrive == null){
              throw new ParserException("File format is incorrect");
            }


            Flight flight = new Flight(src, dest, depart, arrive, flightNumber);
            airline.addFlight(flight);
            for(int i = 0; i < args.length; ++i){
              args[i] = "";
            }
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (NumberFormatException e) {
        throw new RuntimeException(e);
      }


      return airline;

    } catch (IOException e) {
      throw new ParserException("While parsing airline text", e);
    }
  }
}
