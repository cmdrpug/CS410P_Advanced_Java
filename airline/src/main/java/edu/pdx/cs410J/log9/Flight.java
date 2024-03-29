package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AbstractFlight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Flight class which extends the AbstractFlight class. Some variables
 * are final because they shouldn't change while some are mutable since they
 * are likely to change.
 */
public class Flight extends AbstractFlight implements Comparable<Flight>{

  private final String src;
  private final String dest;
  private Date depart;
  private Date arrive;
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
  public Flight(String src, String dest, Date depart, Date arrive, int flightNumber) {
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
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    dateFormat.setLenient(false);
    String departString = dateFormat.format(this.depart);
    return departString;
  }

  /**
   * Utility function that gets the depart variable as a Java.util.Date Object
   * @return the depart variable
   */
  public Date getDepartureDate(){return this.depart;}

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
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    dateFormat.setLenient(false);
    String arriveString = dateFormat.format(this.arrive);
    return arriveString;
  }

  /**
   * Utility function that gets the arrive variable as a Java.util.Date Object
   * @return the depart variable
   */
  public Date getArrivalDate(){return this.arrive;}

  /**
   * Comparable interface which orders first by src alphabetically, and then
   * depart chronologically.
   *
   * @param o the object to be compared.
   * @return 1 if the o is less than the object, -1 if it is greater, and 0 if equal
   */
  @Override
  public int compareTo(Flight o) {
    int srcCompare = this.src.compareTo(o.src);
    if(srcCompare > 0){
      return 1;
    } else if(srcCompare < 0){
      return -1;
    }

    int departCompare = this.depart.compareTo(o.depart);
    if(departCompare > 0){
      return 1;
    } else if(departCompare < 0){
      return -1;
    }

    return 0;
  }
}
