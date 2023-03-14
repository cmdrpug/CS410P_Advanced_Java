package edu.pdx.cs410J.log9;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {
    /**
     *  Returns a Date with the date and time arguments concatenated if both arguments
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
        String[] timeNumber = time.split(" ");
        if(!(timeNumber[0].matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"))){
            System.err.println(argName + " date and time must be in the format mm/dd/yyyy hh:mm a");
            return null;
        }
        try{
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            dateFormat.setLenient(false);
            dateAndTime = dateFormat.parse(toParse);
        } catch(Exception err){
            System.err.println(argName + " date and time must be in the format mm/dd/yyyy hh:mm a");
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
                InputStream readme = Project5.class.getResourceAsStream("README.txt")
        ) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        }
    }

    /**
     * Main function for interacting with the server. Can add and search flights
     * on the server while it's running.
     *
     * @param args the arguments on the command line that will be parsed an inputted to the program
     */
    public static void main(String... args) {
        int firstNonOptionArg = 0;
        String hostName = null;
        String portString = null;
        boolean searchFlight = false;
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
                } else if (args[i].equals("-search")) {
                    searchFlight = true;
                } else if (args[i].equals("-host")) {
                    if(hostName == null){
                        if (i + 1 < args.length && args[i + 1].charAt(0) != '-') {
                            hostName = args[i + 1];
                            ++firstNonOptionArg;
                            ++i;
                        } else{
                            System.err.println("-host option was used without a value");
                            return;
                        }
                    } else {
                        System.err.println("-host option was used multiple times, only one host can be specified");
                        return;
                    }
                } else if (args[i].equals("-port")) {
                    if(portString == null) {
                        if (i + 1 < args.length && args[i + 1].charAt(0) != '-') {
                            portString = args[i + 1];
                            ++firstNonOptionArg;
                            ++i;
                        } else {
                            System.err.println("-port option was used without a value");
                            return;
                        }
                    } else {
                        System.err.println("-port option was used multiple times, only one port can be specified");
                        return;
                    }
                } else{
                    System.err.println("Invalid Option: " + args[i] + " was supplied");
                    return;
                }
                ++firstNonOptionArg;
            }
            else
                break;
        }

        if((args.length) == 0) {
            PrintStream err = System.err;
            err.println("No arguments supplied.");
            err.println("usage: java -jar target/airline-client.jar [options] <args>");
            err.println("args are (in this order):");
            err.println("  airline             The name of the airline");
            err.println("  flightNumber        The flight number");
            err.println("  src                 Three-letter code of departure airport");
            err.println("  depart              Departure date/time");
            err.println("  dest                Three-letter code of arrival airport");
            err.println("  arrive              Arrival date/time");
            err.println("options are (options may appear in any order):");
            err.println("  -host hostname      Host of web server");
            err.println("  -port port          Port number of web server");
            err.println("  -search             Search for flights");
            err.println("  -print              Prints a description of the new flight");
            err.println("  -README             Prints a README for this project and exits");
            err.println();
            err.println("This simple program posts airlines and their flights to the server.");
            err.println("If an airline and new flight arguments are supplied, the flight will be added to that airline");
            err.println("If only the airline is supplied, prints all flights from that airline");
            err.println("If -search is used, airlines can be searched by src and dest fields");
            err.println();
            return;
        }

        if(hostName == null){
            System.err.println("No -host option was used, both a host and port are needed.");
            return;
        }
        if(portString == null){
            System.err.println("No -port option was used, both a host and port are needed.");
            return;
        }

        int port;
        try {
            port = Integer.parseInt( portString );
        } catch (NumberFormatException ex) {
            System.err.println("-port value must be a number");
            return;
        }

        AirlineRestClient client = new AirlineRestClient(hostName, port);

        if(searchFlight){
            if(printFlight){
                System.err.println("-print is not supported when not adding a new flight");
                return;
            } else if(!((args.length - firstNonOptionArg) == 3 || (args.length - firstNonOptionArg) == 1)){
                PrintStream err = System.err;
                err.println("Incorrect arguments for search option");
                err.println("usage: java -jar target/airline-client.jar -host hostName -port portNumber -search <args>");
                err.println("args are (in this order):");
                err.println("  airline             The name of the airline");
                err.println("  src                 Three-letter code of departure airport (optional)");
                err.println("  dest                Three-letter code of arrival airport (optional)");
                err.println("both src and dest need to be supplied if either one is");
                return;
            }
            int argCounter = firstNonOptionArg;
            String airlineName = args[argCounter];
            String src = "";
            String dest = "";
            ++argCounter;
            if(argCounter != args.length){
                src = args[argCounter];
                ++argCounter;
                dest = args[argCounter];
                ++argCounter;
            }

            try {
                PrettyPrinter printer = new PrettyPrinter(new PrintWriter(System.out));
                if (src.equals("") || dest.equals("")) {
                    Airline airline = client.getAirline(airlineName);
                    printer.dump(airline);
                } else {
                    Airline airline = client.getAirline(airlineName, src, dest);
                    printer.dump(airline, src, dest);
                }
            } catch (ParserException e) {
                System.err.println("NEW ERROR: " + e);
                return;
            } catch (IOException e) {
                System.err.println("NEW ERROR: " + e);
                return;
            } catch (HttpRequestHelper.RestException e){
                System.err.println("NEW ERROR: " + e);
                return;
            }

        } else if((args.length - firstNonOptionArg) == 10) {
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
            String departAmPm = args[argCounter+2].toUpperCase();
            if(!(departAmPm.equals("AM") || departAmPm.equals("PM"))){
                System.err.println("depart time was entered incorrectly, must be in the form hh:mm a");
                return;
            }
            departTime = departTime + " " + departAmPm;
            argCounter += 3;
            Date depart = formatDateAndTime(departDate, departTime, "depart");
            String departString = departDate + " " + departTime;
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
            String arriveAmPm = args[argCounter+2].toUpperCase();
            if(!(arriveAmPm.equals("AM") ||arriveAmPm.equals("PM"))){
                System.err.println("arrive time was entered incorrectly, must be in the form hh:mm a");
                return;
            }
            arriveTime = arriveTime + " " + arriveAmPm;
            argCounter += 3;
            Date arrive = formatDateAndTime(arriveDate, arriveTime, "arrive");
            String arriveString = arriveDate + " " + arriveTime;
            if(arrive == null){
                return;
            }

            if(depart.after(arrive)){
                System.err.println("arrival time must be after departure time");
                return;
            }

            Flight newFlight = new Flight(src, dest, depart, arrive, flightNumber);

            try {
                client.postFlight(airlineName, src, dest, departString, arriveString, Integer.toString(flightNumber));
            } catch (IOException e) {
                System.err.println("NEW ERROR: " + e);
                return;
            } catch (HttpRequestHelper.RestException e){
                System.err.println("NEW ERROR: " + e);
                return;
            }

            if(printFlight){
                System.out.println("Newly added flight: \n" + newFlight + "\n");
            }
        } else if((args.length - firstNonOptionArg) == 1){
            if(printFlight){
                System.err.println("-print is not supported when not adding a new flight");
                return;
            }
            String airlineName = args[firstNonOptionArg];
            try{
                Airline airline = client.getAirline(airlineName);
                PrettyPrinter printer = new PrettyPrinter(new PrintWriter(System.out));
                printer.dump(airline);
            } catch (ParserException e) {
                System.err.println("NEW ERROR: " + e);
                return;
            } catch (IOException e) {
                System.err.println("NEW ERROR: " + e);
                return;
            } catch (HttpRequestHelper.RestException e){
                System.err.println("NEW ERROR: " + e.getHttpStatusCode());
                return;
            }
        } else {
            PrintStream err = System.err;
            err.println("Incorrect number of arguments supplied.");
            err.println("usage: java -jar target/airline-client.jar [options] <args>");
            err.println("args are (in this order):");
            err.println("  airline             The name of the airline");
            err.println("  flightNumber        The flight number");
            err.println("  src                 Three-letter code of departure airport");
            err.println("  depart              Departure date/time");
            err.println("  dest                Three-letter code of arrival airport");
            err.println("  arrive              Arrival date/time");
            err.println("options are (options may appear in any order):");
            err.println("  -host hostname      Host of web server");
            err.println("  -port port          Port number of web server");
            err.println("  -search             Search for flights");
            err.println("  -print              Prints a description of the new flight");
            err.println("  -README             Prints a README for this project and exits");
            err.println();
            err.println("This simple program posts airlines and their flights to the server.");
            err.println("If an airline and new flight arguments are supplied, the flight will be added to that airline");
            err.println("If only the airline is supplied, prints all flights from that airline");
            err.println("If -search is used, airlines can be searched by src and dest fields");
            err.println();
        }

        /*String message;
        try {
            if (word == null) {
                // Print all word/definition pairs
                Map<String, String> dictionary = client.getAllDictionaryEntries();
                StringWriter sw = new StringWriter();
                PrettyPrinter pretty = new PrettyPrinter(sw);
                pretty.dump(dictionary);
                message = sw.toString();

            } else if (definition == null) {
                // Print all dictionary entries
                message = PrettyPrinter.formatDictionaryEntry(word, client.getDefinition(word));

            } else {
                // Post the word/definition pair
                client.addDictionaryEntry(word, definition);
                message = Messages.definedWordAs(word, definition);
            }

        } catch (IOException | ParserException ex ) {
            error("While contacting server: " + ex.getMessage());
            return;
        }

        System.out.println(message);*/
    }
}