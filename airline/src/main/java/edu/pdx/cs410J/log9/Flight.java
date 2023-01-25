package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {

  private final String src;
  private final String dest;
  private String depart;
  private String arrive;
  private final int flightNumber;

  public Flight(String src, String dest, String depart, String arrive, int flightNumber) {
    this.src = src;
    this.dest = dest;
    this.depart = depart;
    this.arrive = arrive;
    this.flightNumber = flightNumber;
  }

  @Override
  public int getNumber() {
    return this.flightNumber;
  }

  @Override
  public String getSource() {
    return this.src;
  }

  @Override
  public String getDepartureString() {
    return this.depart;
  }

  @Override
  public String getDestination() {
    return this.dest;
  }

  @Override
  public String getArrivalString() {
    return this.arrive;
  }
}
