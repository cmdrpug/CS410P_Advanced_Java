package edu.pdx.cs410J.log9;

import org.junit.jupiter.api.Test;

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
    return new Flight("PDX", "LAX", "12/12/2005 1:55", "1/2/2005 11:19", 2854);
  }

  /**
   * Creates a flight object and calls toString() to check if it matches what is expected
   */
  @Test
  void testFlightToString() {
    Flight testFlight = testFlightConstructor();
    assertThat(testFlight.toString(), is("Flight 2854 departs PDX at 12/12/2005 1:55 arrives LAX at 1/2/2005 11:19"));
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
    assertThat(testFlight.getDepartureString(), is("12/12/2005 1:55"));
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
    assertThat(testFlight.getArrivalString(), is("1/2/2005 11:19"));
  }
  
}
