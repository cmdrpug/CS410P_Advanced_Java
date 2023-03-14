package edu.pdx.cs410J.log9;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
import static edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class AirlineRestClient
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final HttpRequestHelper http;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient( String hostName, int port )
    {
        this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
    }

    /**
     * Constructor for AirlineRestClient, just sets up the http
     *
     * @param http
     */
    @VisibleForTesting
    AirlineRestClient(HttpRequestHelper http) {
      this.http = http;
    }

    /**
     * get function for the client. takes the airline name and request all flights
     * from the server with it
     *
     * @param airlineName the name of the airline to request
     * @return the airline on success
     * @throws IOException if the server response fails
     * @throws ParserException if there is an issue parsing the returned xml from the server
     */
    public Airline getAirline(String airlineName) throws IOException, ParserException{
        Response response = http.get(Map.of(AirlineServlet.AIRLINE_PARAMETER, airlineName));
        throwExceptionIfNotOkayHttpStatus(response);

        File file = new File("temp.xml");
        FileWriter fw = new FileWriter("temp.xml");
        fw.write(response.getContent());
        fw.close();

        XmlParser parser = new XmlParser(file);
        return parser.parse();
    }

    /**
     * get function for the client. takes the airline name and request all flights
     * from the server that match src and dest
     *
     * @param airlineName the name of the airline to request
     * @param src the src airport to check against
     * @param dest the dest airport to check against
     * @return the airline on success
     * @throws IOException if the server response fails
     * @throws ParserException if there is an issue parsing the returned xml from the server
     */
    public Airline getAirline(String airlineName, String src, String dest) throws IOException, ParserException{
        Response response = http.get(Map.of(AirlineServlet.AIRLINE_PARAMETER, airlineName, AirlineServlet.SRC_PARAMETER, src, AirlineServlet.DEST_PARAMETER, dest));
        throwExceptionIfNotOkayHttpStatus(response);

        File file = new File("temp.xml");
        FileWriter fw = new FileWriter("temp.xml");
        fw.write(response.getContent());
        fw.close();

        XmlParser parser = new XmlParser(file);
        return parser.parse();
    }

    /**
     * posts a new flight to an airline one the server if it exists,
     * otherwise it will create a new airline
     *
     * @param airlineName the airline to add too
     * @param src src arg
     * @param dest dest arg
     * @param depart depart arg
     * @param arrive arrive arg
     * @param flightNumber flightNumber arg
     * @throws IOException throws if server has any issues
     */
    public void postFlight(String airlineName, String src, String dest, String depart, String arrive, String flightNumber) throws IOException{
        Response response = http.post(Map.of(
                AirlineServlet.AIRLINE_PARAMETER, airlineName,
                AirlineServlet.FLIGHT_NUMBER_PARAMETER, flightNumber,
                AirlineServlet.SRC_PARAMETER, src,
                AirlineServlet.DEPART_PARAMETER, depart,
                AirlineServlet.DEST_PARAMETER, dest,
                AirlineServlet.ARRIVE_PARAMETER, arrive
        ));
        throwExceptionIfNotOkayHttpStatus(response);
    }

    /**
     * throws the rest exception based on what happened
     *
     * @param response
     */
    private void throwExceptionIfNotOkayHttpStatus(Response response) {
        int code = response.getHttpStatusCode();
        if (code != HTTP_OK) {
            String message = response.getContent();
            throw new RestException(code, message);
        }
    }

  /*public Map<String, String> getAllDictionaryEntries() throws IOException, ParserException {
    Response response = http.get(Map.of());
    throwExceptionIfNotOkayHttpStatus(response);

    TextParser parser = new TextParser(new StringReader(response.getContent()));
    return parser.parse();
  }

  public String getDefinition(String word) throws IOException, ParserException {
    Response response = http.get(Map.of(AirlineServlet.WORD_PARAMETER, word));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse().get(word);
  }

  public void addDictionaryEntry(String word, String definition) throws IOException {
    Response response = http.post(Map.of(AirlineServlet.WORD_PARAMETER, word, AirlineServlet.DEFINITION_PARAMETER, definition));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  public void removeAllDictionaryEntries() throws IOException {
    Response response = http.delete(Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }*/



}
