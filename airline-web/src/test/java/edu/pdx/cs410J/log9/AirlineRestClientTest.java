package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * A unit test for the REST client that demonstrates using mocks and
 * dependency injection
 */
public class AirlineRestClientTest {

  @Test
  void canCreate() throws IOException {
    Map<String, String> dictionary = Map.of("One", "1", "Two", "2");
    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of()))).thenReturn(dictionaryAsText(dictionary));

    AirlineRestClient client = new AirlineRestClient(http);
    assertThrows(NullPointerException.class, () -> client.postFlight("airline", "PDX", "LAX", "1/1/2008 10:00 AM", "1/1/2008 10:00 PM", "1992"));
    assertThrows(NullPointerException.class, () -> client.getAirline("airline"));
    assertThrows(NullPointerException.class, () -> client.getAirline("airline", "PDX", "LAX"));
  }

  /*@Disabled
  @Test
  void getAllDictionaryEntriesPerformsHttpGetWithNoParameters() throws ParserException, IOException {
    Map<String, String> dictionary = Map.of("One", "1", "Two", "2");

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of()))).thenReturn(dictionaryAsText(dictionary));
    
    AirlineRestClient client = new AirlineRestClient(http);

    //assertThat(client.getAllDictionaryEntries(), equalTo(dictionary));
  }*/

  private HttpRequestHelper.Response dictionaryAsText(Map<String, String> dictionary) {
    StringWriter writer = new StringWriter();
    new TextDumper(writer).dump(dictionary);

    return new HttpRequestHelper.Response(writer.toString());
  }
}
