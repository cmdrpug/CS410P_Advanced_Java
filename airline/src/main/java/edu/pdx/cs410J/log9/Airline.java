package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

public class Airline extends AbstractAirline<Flight> {
  private final String name;
  private Collection<Flight> flights;

  public Airline(String name) {
    this.name = name;
    this.flights = new ArrayList<Flight>();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  @Override
  public Collection<Flight> getFlights() {
    return flights;
  }
}
