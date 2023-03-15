package edu.pdx.cs410J.log9;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
class AirlineServletTest {

  @Test
  void canCreate(){
    AirlineServlet servlet = new AirlineServlet();
  }


  @Test
  void initiallyServletContainsNoDictionaryEntries() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    // Nothing is written to the response's PrintWriter
    verify(pw, never()).println(anyString());
    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test
  void addOneWordToDictionary() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airline = "airline";
    String flightNumber = "234";
    String src = "PDX";
    String depart = "1/1/2008 10:00 AM";
    String dest = "LAX";
    String arrive = "1/1/2008 11:00 AM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);
    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);
    when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn(airline);
    servlet.doPost(request, response);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    servlet.doPost(request, response);
    when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(src);
    servlet.doPost(request, response);
    when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(depart);
    servlet.doPost(request, response);
    when(request.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn(dest);
    servlet.doPost(request, response);
    when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(arrive);
    servlet.doPost(request, response);

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
    servlet.doGet(request, response);
  }

}
