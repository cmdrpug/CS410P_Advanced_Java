package edu.pdx.cs410J.log9;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;
import org.checkerframework.checker.units.qual.A;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static edu.pdx.cs410J.log9.Project5.formatDateAndTime;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AirlineServlet extends HttpServlet {
  static final String AIRLINE_PARAMETER = "airline";
  static final String FLIGHT_NUMBER_PARAMETER = "flightNumber";
  static final String SRC_PARAMETER = "src";
  static final String DEPART_PARAMETER = "depart";
  static final String DEST_PARAMETER = "dest";
  static final String ARRIVE_PARAMETER = "arrive";
  private final Map<String, Airline> airlineMap = new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by writing the flights in an
   * airline to the response or the flights that match src and dest if they
   * are specified
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    response.setContentType( "file/xml" );
    String airlineName = getParameter( AIRLINE_PARAMETER, request );

    if(!(airlineMap.containsKey(airlineName))){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      String src = getParameter(SRC_PARAMETER, request);
      String dest = getParameter(DEST_PARAMETER, request);

      PrintWriter pw = response.getWriter();
      Airline airline = airlineMap.get(airlineName);
      if (src == null || dest == null) {
        XmlDumper dumper = new XmlDumper(pw);
        dumper.dump(airline);
      } else {
        //PARSE SRC AND DEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Airline filteredAirline = new Airline(airlineName);
        airline.getFlights().forEach(flight -> {
          if(src.equals(flight.getSource()) && dest.equals(flight.getDestination())) {
            filteredAirline.addFlight(flight);
          }
        });
        XmlDumper dumper = new XmlDumper(pw);
        dumper.dump(filteredAirline);
      }
      response.setStatus(HttpServletResponse.SC_OK);
    }
      /*response.setContentType( "text/plain" );

      String word = getParameter( WORD_PARAMETER, request );
      if (word != null) {
          writeDefinition(word, response);

      } else {
          writeAllDictionaryEntries(response);
      }*/
  }

  /**
   * Handles an HTTP POST request by storing the flight in an airline.
   * if the airline didn't already exist a new one is created
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    response.setContentType( "text/xml" );

    String airlineName = getParameter(AIRLINE_PARAMETER, request );
    if(airlineName == null){missingRequiredParameter(response, AIRLINE_PARAMETER); return;}
    String flightNumber = getParameter(FLIGHT_NUMBER_PARAMETER, request);
    if(flightNumber == null){missingRequiredParameter(response, FLIGHT_NUMBER_PARAMETER); return;}
    String src = getParameter(SRC_PARAMETER, request);
    if(src == null){missingRequiredParameter(response, SRC_PARAMETER); return;}
    String depart = getParameter(DEPART_PARAMETER, request);
    if(depart == null){missingRequiredParameter(response, DEPART_PARAMETER); return;}
    String dest = getParameter(DEST_PARAMETER, request);
    if(dest == null){missingRequiredParameter(response, DEST_PARAMETER); return;}
    String arrive = getParameter(ARRIVE_PARAMETER, request);
    if(arrive == null){missingRequiredParameter(response, ARRIVE_PARAMETER); return;}

    String[] departArray = depart.split(" ", 2);
    String[] arriveArray = arrive.split(" ", 2);
    if(airlineMap.containsKey(airlineName)){
      airlineMap.get(airlineName).addFlight(new Flight(src, dest, formatDateAndTime(departArray[0], departArray[1], "depart"), formatDateAndTime(arriveArray[0], arriveArray[1], "arrive"), Integer.parseInt(flightNumber)));
    } else{
      Airline airline = new Airline(airlineName);
      airline.addFlight(new Flight(src, dest, formatDateAndTime(departArray[0], departArray[1], "depart"), formatDateAndTime(arriveArray[0], arriveArray[1], "arrive"), Integer.parseInt(flightNumber)));
      airlineMap.put(airlineName, airline);
    }

    PrintWriter pw = response.getWriter();
    pw.println(Messages.definedWordAs(airlineName));
    pw.flush();

    response.setStatus( HttpServletResponse.SC_OK);

      /*response.setContentType( "text/plain" );

      String word = getParameter(WORD_PARAMETER, request );
      if (word == null) {
          missingRequiredParameter(response, WORD_PARAMETER);
          return;
      }

      String definition = getParameter(DEFINITION_PARAMETER, request );
      if ( definition == null) {
          missingRequiredParameter( response, DEFINITION_PARAMETER );
          return;
      }

      this.dictionary.put(word, definition);

      PrintWriter pw = response.getWriter();
      pw.println(Messages.definedWordAs(word, definition));
      pw.flush();

      response.setStatus( HttpServletResponse.SC_OK);*/
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName ) throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /*private void writeDefinition(String word, HttpServletResponse response) throws IOException {
    /*String definition = this.dictionary.get(word);

    if (definition == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } else {
      PrintWriter pw = response.getWriter();

      Map<String, String> wordDefinition = Map.of(word, definition);
      TextDumper dumper = new TextDumper(pw);
      dumper.dump(wordDefinition);

      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  private void writeAllDictionaryEntries(HttpServletResponse response ) throws IOException
  {
      /*PrintWriter pw = response.getWriter();
      TextDumper dumper = new TextDumper(pw);
      dumper.dump(dictionary);

      response.setStatus( HttpServletResponse.SC_OK );
  }*/
}
