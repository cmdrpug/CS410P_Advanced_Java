package edu.pdx.cs410J.log9;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static edu.pdx.cs410J.log9.Project5.formatDateAndTime;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

  /**
   * Test all comparison outcomes for flights
   */
  @Test
  void flightComparisons() {
    Flight testFlight = testFlightConstructor();
    Date depart = formatDateAndTime("1/2/2005", "1:55 AM", "depart");
    Date departS = formatDateAndTime("1/2/2005", "1:54 AM", "depart");
    Date departL = formatDateAndTime("1/2/2005", "1:56 AM", "depart");
    Date arrive = formatDateAndTime("12/12/2005", "11:19 PM", "arrive");
    Flight smallerSrc = new Flight("ABQ", "LAX", depart, arrive, 2854);
    Flight largerSrc = new Flight("ZAL", "LAX", depart, arrive, 2854);
    Flight smallerDepart = new Flight("PDX", "LAX", departS, arrive, 2854);
    Flight largerDepart = new Flight("PDX", "LAX", departL, arrive, 2854);

    assertThat(testFlight.compareTo(smallerSrc), is(1));
    assertThat(testFlight.compareTo(largerSrc), is(-1));
    assertThat(testFlight.compareTo(smallerDepart), is(1));
    assertThat(testFlight.compareTo(largerDepart), is(-1));
    assertThat(testFlight.compareTo(testFlight), is(0));
  }
  
}
