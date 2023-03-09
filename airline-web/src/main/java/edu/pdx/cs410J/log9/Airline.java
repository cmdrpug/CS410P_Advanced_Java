package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The Airline class which extends the AbstractAirline class. The name
 * is final but the flights list is not and can be updated and read by using
 * class methods.
 */
public class Airline extends AbstractAirline<Flight> {
  private final String name;
  private Collection<Flight> flights;

  /**
   * Constructor for the Airline. Takes a name which is assigned to its
   * respective variable and creates an empty ArrayList of flights which
   * can be updated and used by class methods
   *
   * @param name the name of the Airline
   */
  public Airline(String name) {
    this.name = name;
    this.flights = new ArrayList<Flight>();
  }

  /**
   * Get method for the name variable
   * @return the name variable
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Adds a Flight object to the end of the flights variable and then sorts the list.
   *
   * @param flight the Flight object to add to the flights variable
   */
  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
    Collections.sort((ArrayList)this.flights);
  }

  /**
   * Get method for the flights variable
   * @return the ArrayList variable of flights
   */
  @Override
  public Collection<Flight> getFlights() {
    return flights;
  }
}
