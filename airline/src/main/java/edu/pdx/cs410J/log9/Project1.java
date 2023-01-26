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

  /**
   *  Returns a String with the date and time arguments concatenated if both arguments
   *  are valid dates and times respectively. Prints error message and returns null if
   *  either argument does not match the format.
   *
   * @param date the date portion of the command line argument
   * @param time the time portion of the command line argument
   * @param argName which argument the date and time is for, used for error messages
   * @return returns the string dateAndTime if successful, otherwise null
   */
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

  /**
   * Gets the README.txt file as a resource and prints each line to console.
   * Will throw an IOException if it fails for any reason.
   *
   * @throws IOException if the reader fails for any reason
   */
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

  /**
   * The overall purpose of main is the read and check the arguments and then create
   * the airline and flight using those arguments as parameters. It is split up into
   * three steps
   *
   * First step is the options checking. In a for loop, an argument is checked for
   * starting with the '-' character. If it does, it's respective option clause is
   * run and firstNonOptionArg is incremented to track the start of normal arguments.
   * If it isn't a valid option, the program prints a message and returns. When the
   * first argument to not start with '-' is encountered, this step ends.
   *
   * Step two is parsing arguments. If the remaining argument is not 8 after step one,
   * a message instructing how to use will be printed and the program will return.
   * Most arguments have some kind of restriction for what they can be. If the argument
   * passes its restriction, then it is assigned to a variable to be used later. If any
   * argument does not pass its restriction, a message including which argument failed
   * is printed and the program returns. After the last argument is parsed, this step
   * ends.
   *
   * Finally, step three is creating and optionally printing the objects. A new Airline
   * with the airlineName argument is created. Then a new Flight is created with the
   * remaining parsed arguments. The flight is added to the airline and then if -print
   * was included, all elements of the airlines flights field are printed.
   *
   * @param args The command line arguments are used for options and values for the flight and airline
   */
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