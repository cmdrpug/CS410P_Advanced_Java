package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AbstractFlight;

/**
 * The Flight class which extends the AbstractFlight class. Some variables
 * are final because they shouldn't change while some are mutable since they
 * are likely to change.
 */
public class Flight extends AbstractFlight {

  private final String src;
  private final String dest;
  private String depart;
  private String arrive;
  private final int flightNumber;

  /**
   * Constructor for Flight. Simply assigns the arguments to their matching variables.
   * The variables should be parsed before being used to create a Flight.
   *
   * @param src Three-letter code of departure airport
   * @param dest Three-letter code of arrival airport
   * @param depart Departure date and time (24-hour time) which is actually two arguments
   * @param arrive Arrival date and time (24-hour time) which is actually two arguments
   * @param flightNumber Identifying number for the Flight
   */
  public Flight(String src, String dest, String depart, String arrive, int flightNumber) {
    this.src = src;
    this.dest = dest;
    this.depart = depart;
    this.arrive = arrive;
    this.flightNumber = flightNumber;
  }

  /**
   * Get method for the flightNumber variable
   * @return the flightNumber variable
   */
  @Override
  public int getNumber() {
    return this.flightNumber;
  }

  /**
   * Get method for the src variable
   * @return the src variable
   */
  @Override
  public String getSource() {
    return this.src;
  }

  /**
   * Get method for the depart variable
   * @return the depart variable
   */
  @Override
  public String getDepartureString() {
    return this.depart;
  }

  /**
   * Get method for the dest variable
   * @return the dest variable
   */
  @Override
  public String getDestination() {
    return this.dest;
  }

  /**
   * Get method for the arrive variable
   * @return the arrive variable
   */
  @Override
  public String getArrivalString() {
    return this.arrive;
  }
}
