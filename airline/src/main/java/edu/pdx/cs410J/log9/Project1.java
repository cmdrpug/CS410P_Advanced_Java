package edu.pdx.cs410J.log9;

import com.google.common.annotations.VisibleForTesting;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static java.lang.Integer.parseInt;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  @VisibleForTesting
  static String formatDateAndTime(String date, String time, String argName) {
    String dateAndTime = "";
    try{
      DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
      dateFormat.parse(date);
      dateFormat.setLenient(false);
      dateAndTime = date;
    } catch(Exception err){
      System.err.println(argName + " date must be in the format mm/dd/yyyy");
      return null;
    }
    if(time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")){
      dateAndTime = dateAndTime + " " + time;
    } else{
      System.err.println(argName + " time must be a valid time in the format hh:mm");
      return null;
    }
    return dateAndTime;
  }

  static void printREADME() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line;
      while((line = reader.readLine()) != null){
        System.out.println(line);
      }
    }
  }

  public static void main(String[] args){
    int firstNonOptionArg = 0;
    boolean printFlight = false;
    for(int i = 0; i < args.length; ++i){
      if (args[i].charAt(0) == '-'){
        if(args[i].equals("-README")){
          try{
            printREADME();
          } catch (IOException err){
            System.err.println("Failed to read README.txt");
            return;
          }
          return;
        } else if (args[i].equals("-print")) {
          printFlight = true;
        }
        else{
          System.err.println("Invalid Option: " + args[i] + " was supplied.");
          return;
        }
        ++firstNonOptionArg;
      }
      else
        break;
    }

    if(args.length - firstNonOptionArg != 8){
      System.err.println(
        "Incorrect number of arguments supplied.\n" +
        "Usage: java -jar target/airline-2023.0.0.jar [options] <args>"
      );
      System.out.println(
        "args are (in this order):\n" +
        "airline \t\t The name of the airline\n" +
        "flightNumber \t The flight number\n" +
        "src  \t\t\t Three-letter code of departure airport\n" +
        "depart \t\t Departure date and time (24-hour time)\n" +
        "dest \t\t\t Three-letter code of arrival airport\n" +
        "arrive \t\t Arrival date and time (24-hour time)\n\n" +
        "options are (options may appear in any order):\n" +
        "-print \t\t\t Prints a description of the new flight\n" +
        "-README \t\t Prints a README for this project and exits\n" +
        "Date and time should be in the format: mm/dd/yyyy hh:mm"
      );
      return;
    }

    int argCounter = firstNonOptionArg;
    String airlineName = args[argCounter];
    ++argCounter;

    int flightNumber = 0;
    try{
      flightNumber = parseInt(args[argCounter]);
    } catch (NumberFormatException err){
      System.err.println("flightNumber must be a number");
      return;
    }
    ++argCounter;

    String src = "";
    if(args[argCounter].length() != 3 || !(args[argCounter].matches("[a-zA-Z]+"))){
      System.err.println("src must be a 3 letter code");
      return;
    } else{
      src = args[argCounter].toUpperCase();
    }
    ++argCounter;

    String departDate = args[argCounter];
    String departTime = args[argCounter+1];
    argCounter += 2;
    String depart = formatDateAndTime(departDate, departTime, "depart");
    if(depart == null){
      return;
    }

    String dest = "";
    if(args[argCounter].length() != 3 || !(args[argCounter].matches("[a-zA-Z]+"))){
      System.err.println("dest must be a 3 letter code");
      return;
    } else{
      dest = args[argCounter].toUpperCase();
    }
    ++argCounter;

    String arriveDate = args[argCounter];
    String arriveTime = args[argCounter+1];
    argCounter += 2;
    String arrive = formatDateAndTime(arriveDate, arriveTime, "arrive");
    if(arrive == null){
      return;
    }

    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(src, dest, depart, arrive, flightNumber);
    airline.addFlight(flight);

    if(printFlight){
      for(Flight currentFlight: airline.getFlights()) {
        System.out.println(currentFlight.toString());
      }
    }
  }
}