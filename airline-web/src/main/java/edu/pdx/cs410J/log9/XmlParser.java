package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.pdx.cs410J.log9.Project5.formatDateAndTime;
import static java.lang.Integer.parseInt;

/**
 * The XmlParser class implements AirlineParser interface.
 * It is used to read files that are formatted by the XmlDumper
 * class back into memory during runtime.
 */
public class XmlParser implements AirlineParser<Airline> {
    private final File xmlFile;
    /**
     * Constructor for the Parser. Just assigns the File to the xmlFile variable
     *
     * @param xmlFile The FileReader that will be read from
     */
    public XmlParser(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    /**
     * The Parse method first just checks if the file is empty. If it is
     * it throws a DTD exception. Then is goes into a for loop which will go through every
     * node in the tree adding it to an airline to return at the end.
     *
     * @return The airline on successful parsing
     * @throws ParserException any time that a read in line doesn't match the format
     */
    @Override
    public Airline parse() throws ParserException {

        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new AirlineXmlHelper());
            doc = builder.parse(this.xmlFile);
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException("Parser configuration failed");
        } catch (SAXException ex) {
            throw new RuntimeException("File does not conform to the DTD");
        } catch (IOException ex) {
            throw new RuntimeException("Could not read file " + ex);
        }

        Element root = doc.getDocumentElement();
        NodeList flights = root.getChildNodes();
        Airline airline = new Airline(flights.item(1).getTextContent());

        for(int i = 2; i < flights.getLength(); ++i){
            Node cur = flights.item(i);
            if(cur.getNodeName().equals("flight")) {
                String src = "";
                String dest = "";
                String departDate = "";
                String departTime = "";
                String arriveDate = "";
                String arriveTime = "";
                String flightNumber = "";

                NodeList curList = cur.getChildNodes();
                for(int j = 0; j < curList.getLength(); ++j){
                    Node node = curList.item(j);
                    if ((node instanceof Element)) {
                        Element entry = (Element) node;
                        switch (entry.getNodeName()) {
                            case "number":
                                flightNumber = entry.getTextContent();
                                break;
                            case "src":
                                src = entry.getTextContent();
                                break;
                            case "depart":
                                NodeList departList = entry.getChildNodes();
                                for(int k = 0; k < departList.getLength(); ++k) {
                                    Node departNode = departList.item(k);
                                    if ((departNode instanceof Element)) {
                                        Element departEntry = (Element) departNode;
                                        switch (departEntry.getNodeName()) {
                                            case "date":
                                                String day = departEntry.getAttribute("day");
                                                String month = departEntry.getAttribute("month");
                                                String year = departEntry.getAttribute("year");
                                                departDate = month + "/" + day + "/" + year;
                                                break;
                                            case "time":
                                                String hour = departEntry.getAttribute("hour");
                                                String minute = departEntry.getAttribute("minute");
                                                departTime = hour + ":" + minute;
                                                try{
                                                    final SimpleDateFormat df24hour = new SimpleDateFormat("HH:mm");
                                                    final SimpleDateFormat df12hour = new SimpleDateFormat("hh:mm a");
                                                    Date tempDate = df24hour.parse(departTime);
                                                    departTime = df12hour.format(tempDate);
                                                } catch (ParseException e) {
                                                    throw new ParserException("Xml file had bad time data");
                                                }
                                                break;
                                        }
                                    }
                                }
                                break;
                            case "dest":
                                dest = entry.getTextContent();
                                break;
                            case "arrive":
                                NodeList arriveList = entry.getChildNodes();
                                for(int k = 0; k < arriveList.getLength(); ++k) {
                                    Node arriveNode = arriveList.item(k);
                                    if ((arriveNode instanceof Element)) {
                                        Element arriveEntry = (Element) arriveNode;
                                        switch (arriveEntry.getNodeName()) {
                                            case "date":
                                                String day = arriveEntry.getAttribute("day");
                                                String month = arriveEntry.getAttribute("month");
                                                String year = arriveEntry.getAttribute("year");
                                                arriveDate = month + "/" + day + "/" + year;
                                                break;
                                            case "time":
                                                String hour = arriveEntry.getAttribute("hour");
                                                String minute = arriveEntry.getAttribute("minute");
                                                arriveTime = hour + ":" + minute;
                                                try{
                                                    final SimpleDateFormat df24hour = new SimpleDateFormat("HH:mm");
                                                    final SimpleDateFormat df12hour = new SimpleDateFormat("hh:mm a");
                                                    Date tempDate = df24hour.parse(arriveTime);
                                                    arriveTime = df12hour.format(tempDate);
                                                } catch (ParseException e) {
                                                    throw new ParserException("Xml file had bad time data");
                                                }
                                                break;
                                        }
                                    }
                                }
                                break;
                        }
                    }

                }
                if(src.length() != 3){
                    throw new ParserException("Xml file had bad src data");
                } else if(!(src.matches("[a-zA-Z]+"))){
                    throw new ParserException("Xml file had bad src data");
                } else if(AirportNames.getName(src) == null) {
                    throw new ParserException("Xml file had bad src data");
                } else{
                    src = src.toUpperCase();
                }

                if(dest.length() != 3){
                    throw new ParserException("Xml file had bad dest data");
                } else if(!(dest.matches("[a-zA-Z]+"))){
                    throw new ParserException("Xml file had bad dest data");
                } else if(AirportNames.getName(dest) == null) {
                    throw new ParserException("Xml file had bad dest data");
                } else{
                    dest = dest.toUpperCase();
                }

                airline.addFlight(new Flight(src, dest, formatDateAndTime(departDate,departTime,"depart"), formatDateAndTime(arriveDate,arriveTime,"arrive"), parseInt(flightNumber)));
            }
        }

        return airline;
    }
}
