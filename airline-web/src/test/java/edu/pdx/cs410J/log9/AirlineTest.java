package edu.pdx.cs410J.log9;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static edu.pdx.cs410J.log9.Project5.formatDateAndTime;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Flight} class.*
 */
public class AirlineTest {
    /**
     * Utility function for creating an Airline instance
     *
     * @return and Airline with name = "Swag Air"
     */
    Airline testAirlineConstructor() {
        return new Airline("Swag Air");
    }

    /**
     * Test if getName() returns the correct String
     */
    @Test
    void testAirlineGetName() {
        Airline testAirline = testAirlineConstructor();
        assertThat(testAirline.getName(), is("Swag Air"));
    }

    /**
     * Creates a flight object and adds it to testAirline's flight variable
     * Then calls getFlights with an iterator to get the first element and
     * compare it to the original flight object
     */
    @Test
    void testAirlineAddAndGetFlights() {
        Airline testAirline = testAirlineConstructor();
        Date depart = formatDateAndTime("12/12/2005", "1:55 AM", "depart");
        Date arrive = formatDateAndTime("1/2/2005", "12:19 PM", "arrive");
        Flight flight = new Flight("PDX", "LAX", depart, arrive, 2854);
        testAirline.addFlight(flight);
        assertThat(testAirline.getFlights().iterator().next(), is(flight));
    }
}
