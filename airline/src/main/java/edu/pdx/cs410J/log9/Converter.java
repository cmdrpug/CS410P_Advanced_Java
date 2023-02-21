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
public class Converter {
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
        File textFile = null;
        File xmlFile = null;

        if(args.length != 2){
            System.err.println(
                    "Incorrect number of arguments supplied.\n" +
                    "usage: java edu.pdx.cs410J.log9.Converter textFile xmlFile"
            );
            return;
        }

        if(args[0] == null  || args[0].equals("txt") || !(args[0].substring(args[0].lastIndexOf(".") + 1).equals("txt"))){
            System.err.println("The first argument must be a .txt file");
            return;
        }

        if(args[1] == null  || args[1].equals("xml") || !(args[1].substring(args[1].lastIndexOf(".") + 1).equals("xml"))){
            System.err.println("The second argument must be a .xml file");
            return;
        }

        System.out.println("Good arguments");
    }
}