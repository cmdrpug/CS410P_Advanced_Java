package edu.pdx.cs410J.log9;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static edu.pdx.cs410J.log9.Project4.formatDateAndTime;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Flight} class.*
 */
public class FlightTest {
  /**
   * Utility function to create a Flight object
   *
   * @return a Flight object with correctly parsed values
   */
  Flight testFlightConstructor() {
    Date depart = formatDateAndTime("1/2/2005", "1:55 AM", "depart");
    Date arrive = formatDateAndTime("12/12/2005", "11:19 PM", "arrive");
    return new Flight("PDX", "LAX", depart, arrive, 2854);
  }

  /**
   * Creates a flight object and calls toString() to check if it matches what is expected
   */
  @Test
  void testFlightToString() {
    Flight testFlight = testFlightConstructor();
    assertThat(testFlight.toString(), is("Flight 2854 departs PDX at 01/02/2005 01:55 AM arrives LAX at 12/12/2005 11:19 PM"));
  }

  /**
   * Creates a flight object and calls getNumber() to check if it matches what is expected
   */
  @Test
  void testGetNumber() {
    Flight testFlight = testFlightConstructor();
    assertThat(testFlight.getNumber(), is(2854));
  }

  /**
   * Creates a flight object and calls getSource() to check if it matches what is expected
   */
  @Test
  void testGetSource() {
    Flight testFlight = testFlightConstructor();
    assertThat(testFlight.getSource(), is("PDX"));
  }

  /**
   * Creates a flight object and calls getDepartureString() to check if it matches what is expected
   */
  @Test
  void testGetDepartureString() {
    Flight testFlight = testFlightConstructor();
    assertThat(testFlight.getDepartureString(), is("01/02/2005 01:55 AM"));
  }

  /**
   * Creates a flight object and calls getDestination() to check if it matches what is expected
   */
  @Test
  void testGetDestination() {
    Flight testFlight = testFlightConstructor();
    assertThat(testFlight.getDestination(), is("LAX"));
  }

  /**
   * Creates a flight object and calls getArrivalString() to check if it matches what is expected
   */
  @Test
  void testGetArrivalString() {
    Flight testFlight = testFlightConstructor();
    assertThat(testFlight.getArrivalString(), is("12/12/2005 11:19 PM"));
  }
  
}
