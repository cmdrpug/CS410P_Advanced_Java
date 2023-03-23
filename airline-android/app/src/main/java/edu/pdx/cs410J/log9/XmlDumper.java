package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.AirlineDumper;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * The XmlDumper class implements AirlineDumper interface.
 * It is used to create files in a format that can be read by the
 * XmlParser on future runs of the program
 */
public class XmlDumper implements AirlineDumper<Airline> {
    private final Writer writer;

    /**
     * Constructor for the dumper. Just assigns the FileWriter to the writer variable
     *
     */
    public XmlDumper(Writer writer) {
        this.writer = writer;
    }

    /**
     * First the airline tag is created and its name is added as a child. Then
     * for every flight, it goes into a loop creating every child needed and its
     * attributes before moving on to the next.
     *
     * @param airline the Airline object to be dumped into a file
     */
    @Override
    public void dump(Airline airline) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder =
                    factory.newDocumentBuilder();
            DOMImplementation dom =
                    builder.getDOMImplementation();
            DocumentType docType =
                    dom.createDocumentType("airline", AirlineXmlHelper.PUBLIC_ID, AirlineXmlHelper.SYSTEM_ID);
            doc = dom.createDocument(null, "airline",
                    docType);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Parser failed to configure");
        } catch (DOMException e) {
            throw new RuntimeException("DOM failed to create");
        }

        try {
            Element root = doc.getDocumentElement();
            Element airlineName = doc.createElement("name");
            airlineName.appendChild(doc.createTextNode(airline.getName()));
            root.appendChild(airlineName);

            Document finalDoc = doc;
            airline.getFlights().forEach(flight -> {
                Date departureDate = flight.getDepartureDate();
                Date arrivalDate = flight.getArrivalDate();
                LocalDateTime departLocalDate = departureDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime arriveLocalDate = arrivalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                Element xmlFlight = finalDoc.createElement("flight");
                root.appendChild(xmlFlight);

                Element number = finalDoc.createElement("number");
                number.appendChild(finalDoc.createTextNode(String.valueOf(flight.getNumber())));
                xmlFlight.appendChild(number);

                Element src = finalDoc.createElement("src");
                src.appendChild(finalDoc.createTextNode(flight.getSource()));
                xmlFlight.appendChild(src);

                Element depart = finalDoc.createElement("depart");
                Element departDate = finalDoc.createElement("date");
                departDate.setAttribute("day", String.valueOf(departLocalDate.getDayOfMonth()));
                departDate.setAttribute("month", String.valueOf(departLocalDate.getMonthValue()));
                departDate.setAttribute("year", String.valueOf(departLocalDate.getYear()));
                Element departTime = finalDoc.createElement("time");
                departTime.setAttribute("hour", String.valueOf(departLocalDate.getHour()));
                departTime.setAttribute("minute", String.valueOf(departLocalDate.getMinute()));
                depart.appendChild(departDate);
                depart.appendChild(departTime);
                xmlFlight.appendChild(depart);

                Element dest = finalDoc.createElement("dest");
                dest.appendChild(finalDoc.createTextNode(flight.getDestination()));
                xmlFlight.appendChild(dest);

                Element arrive = finalDoc.createElement("arrive");
                Element arriveDate = finalDoc.createElement("date");
                arriveDate.setAttribute("day", String.valueOf(arriveLocalDate.getDayOfMonth()));
                arriveDate.setAttribute("month", String.valueOf(arriveLocalDate.getMonthValue()));
                arriveDate.setAttribute("year", String.valueOf(arriveLocalDate.getYear()));
                Element arriveTime = finalDoc.createElement("time");
                arriveTime.setAttribute("hour", String.valueOf(arriveLocalDate.getHour()));
                arriveTime.setAttribute("minute", String.valueOf(arriveLocalDate.getMinute()));
                arrive.appendChild(arriveDate);
                arrive.appendChild(arriveTime);
                xmlFlight.appendChild(arrive);
            });
        } catch (DOMException e) {
            throw new RuntimeException("There was an issue while creating the DOM tree from the airline");
        }

        try {
            Source src = new DOMSource(doc);
            Result res = new StreamResult(this.writer);
            TransformerFactory xFactory =
                    TransformerFactory.newInstance();
            Transformer xform = xFactory.newTransformer();
            xform.setOutputProperty(OutputKeys.INDENT,
                    "yes");
            xform.setOutputProperty(
                    OutputKeys.DOCTYPE_SYSTEM, AirlineXmlHelper.SYSTEM_ID);
            xform.transform(src, res);
        } catch (TransformerException e) {
            throw new RuntimeException("Failed to convert DOM tree to an XML file");
        }
    }
}
