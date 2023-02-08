package edu.pdx.cs410J.log9;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * The main class for the CS410J airline Project
 */
public class Project3 {

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
  static Date formatDateAndTime(String date, String time, String argName) {
    String toParse = date + " " + time;
    Date dateAndTime = null;
    if(!(time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"))){
      System.err.println(argName + " date and time must be in the format mm/dd/yyyy hh:mm");
      return null;
    }
    try{
      DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
      dateFormat.setLenient(false);
      dateAndTime = dateFormat.parse(toParse);
    } catch(Exception err){
      System.err.println(argName + " date and time must be in the format mm/dd/yyyy hh:mm");
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
      InputStream readme = Project3.class.getResourceAsStream("README.txt")
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
   * four steps
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
   * Step three is creating the airline and there are two main paths. If the textFile option
   * was not included, a new Airline is created with the airlineName argument and the new Flight
   * is added. If the textFile option was included and is a .txt file, then the reading/writing
   * branch is entered. If the file was not found, then a new Airline is created exactly like the
   * other path. If it was found and is formatted correctly, it is parsed into a new Airline from
   * the contents of the file, and then the new Flight from the command line is added after. After
   * the airline exists, it is written back to the same file or a new one if it didn't exist.
   *
   * Finally, step four is optionally printing the objects. The new flight created from
   * the command line is printed to System.out.
   *
   * @param args The command line arguments are used for options and values for the flight and airline
   */
  public static void main(String[] args){
    int firstNonOptionArg = 0;
    File textFile = null;
    File prettyFile = null;
    boolean prettyPrint = false;
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
        } else if (args[i].equals("-textFile")) {
          if(textFile != null){
            System.err.println("Multiple .txt files cannot be used");
            return;
          } else {
            if(args[i + 1] == null  || args[i + 1].equals("txt") || !(args[i + 1].substring(args[i + 1].lastIndexOf(".") + 1).equals("txt"))){
              System.err.println("The specified file must be a .txt file");
              return;
            }
            textFile = new File(args[i + 1]);
            ++firstNonOptionArg;
            ++i;
          }
        } else if(args[i].equals("-pretty")){
          if(prettyFile != null){
            System.err.println("Multiple .txt files cannot be used");
            return;
          } else if ((args[i + 1].substring(args[i + 1].lastIndexOf(".") + 1).equals("txt"))) {
            prettyFile = new File(args[i + 1]);
            prettyPrint = true;
            ++firstNonOptionArg;
            ++i;
          } else {
            prettyPrint = true;
          }
        }
        else{
          System.err.println("Invalid Option: " + args[i] + " was supplied");
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
        "Usage: java -jar target/airline-2023.0.0.jar [options] <args>" +
        "args are (in this order):\n" +
        "airline \t\t The name of the airline\n" +
        "flightNumber \t The flight number\n" +
        "src  \t\t\t Three-letter code of departure airport\n" +
        "depart \t\t Departure date and time (24-hour time)\n" +
        "dest \t\t\t Three-letter code of arrival airport\n" +
        "arrive \t\t Arrival date and time (24-hour time)\n\n" +
        "options are (options may appear in any order):\n" +
        "-pretty file \t Pretty print the airline’s flights to\n" +
        "\t\t\t\t a text file or standard out (file -)\n" +
        "-TextFile file \t Where to read/write the airline info\n" +
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
    if(args[argCounter].length() != 3){
      System.err.println("src must be a 3 letters long");
      return;
    } else if(!(args[argCounter].matches("[a-zA-Z]+"))){
      System.err.println("src must contain only letters");
      return;
    } else if(AirportNames.getName(args[argCounter]) == null) {
      System.err.println("src must be a known airport code");
      return;
    } else {
      src = args[argCounter].toUpperCase();
    }
    ++argCounter;

    String departDate = args[argCounter];
    String departTime = args[argCounter+1];
    argCounter += 2;
    Date depart = formatDateAndTime(departDate, departTime, "depart");
    if(depart == null){
      return;
    }

    String dest = "";
    if(args[argCounter].length() != 3){
      System.err.println("dest must be a 3 letters long");
      return;
    } else if(!(args[argCounter].matches("[a-zA-Z]+"))){
      System.err.println("dest must contain only letters");
      return;
    } else if(AirportNames.getName(args[argCounter]) == null) {
      System.err.println("dest must be a known airport code");
      return;
    } else{
      dest = args[argCounter].toUpperCase();
    }
    ++argCounter;

    String arriveDate = args[argCounter];
    String arriveTime = args[argCounter+1];
    argCounter += 2;
    Date arrive = formatDateAndTime(arriveDate, arriveTime, "arrive");
    if(arrive == null){
      return;
    }

    if(depart.after(arrive)){
      System.err.println("arrival time must be after departure time");
      return;
    }


    Flight newFlight = new Flight(src, dest, depart, arrive, flightNumber);
    Airline airline = null;
    if(textFile != null){
      try{
        Reader reader = new FileReader(textFile);
        TextParser textParser = new TextParser(reader);
        airline = textParser.parse();
        if(!(airline.getName().equals(airlineName))){
          System.err.println("The airline name provided on the command line does not match the one on file");
          return;
        }
        airline.addFlight(newFlight);
      } catch (ParserException e){
        System.err.println("File formatting is incorrect: " + e.toString().substring(e.toString().lastIndexOf(":")+1));
        return;
      } catch (FileNotFoundException e) {
        airline = new Airline(airlineName);
        airline.addFlight(newFlight);
      }

      try{
        TextDumper textDumper = new TextDumper(new FileWriter(textFile));
        textDumper.dump(airline);
      } catch (IOException e) {
        System.err.println("File could not be created to write to");
        return;
      }
    } else{
      airline = new Airline(airlineName);
      airline.addFlight(newFlight);
    }


    if(printFlight){
      System.out.println(newFlight);
    }
  }
}