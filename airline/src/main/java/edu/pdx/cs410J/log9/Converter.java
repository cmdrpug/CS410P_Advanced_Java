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
 * The Converter class for the CS410J airline Project
 */
public class Converter {
    /**
     * The converter takes a txt file and an xml. It reads the data from the txt file
     * if it exists into memory. Then it dumps it to an xml file.
     *
     * @param args The command line arguments are used for the two files .txt and .xml in that order
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

        textFile = new File(args[0]);
        xmlFile = new File(args[1]);

        Airline airline = null;
        try{
            Reader reader = new FileReader(textFile);
            TextParser textParser = new TextParser(reader);
            airline = textParser.parse();
        } catch (ParserException e){
            System.err.println("Text file formatting is incorrect: " + e.toString().substring(e.toString().lastIndexOf(":")+1));
            return;
        } catch (FileNotFoundException e) {
            System.err.println("txt file does not exist");
            return;
        }

        try {
            XmlDumper xmlDumper = new XmlDumper(new FileWriter(xmlFile));
            xmlDumper.dump(airline);
        } catch (IOException e) {
            System.err.println("xml file could not be created to write to");
            return;
        }
    }
}