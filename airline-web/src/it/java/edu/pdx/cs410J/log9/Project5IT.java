package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * An integration test for {@link Project5} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project5.class, args );
    }

    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test
    void testNoCommandLineArguments() {
        MainMethodResult result = invokeMain();
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("No arguments supplied."));
    }

    /**
     * Tests the -README argument prints the readme
     */
    @Test
    void testReadmeOption() {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("This is a README file!"));
    }

    /**
     * test invalid option
     */
    @Test
    void testInvalidOption() {
        MainMethodResult result = invokeMain("-invalid");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid Option: -invalid was supplied"));
    }

    /**
     * Tests for no -host option
     */
    @Test
    void testNoHostOption() {
        MainMethodResult result = invokeMain("hi");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("No -host option was used, both a host and port are needed."));
    }

    /**
     * Tests for no -host option value
     */
    @Test
    void testNoHostOptionValue() {
        MainMethodResult result = invokeMain("-host", "-port");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("-host option was used without a value"));
    }

    /**
     * Tests for no -host option value
     */
    @Test
    void testMultipleHostOption() {
        MainMethodResult result = invokeMain("-host", "localHost", "-host", "localHost");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("-host option was used multiple times, only one host can be specified"));
    }

    /**
     * Tests for no -Port option
     */
    @Test
    void testNoPortOption() {
        MainMethodResult result = invokeMain("-host", "localhost");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("No -port option was used, both a host and port are needed."));
    }

    /**
     * Tests for no -Port option value
     */
    @Test
    void testNoPortOptionValue() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "-print");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("-port option was used without a value"));
    }

    /**
     * Tests for no -port option value
     */
    @Test
    void testMultiplePortOption() {
        MainMethodResult result = invokeMain("-port", "12", "-port", "12");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("-port option was used multiple times, only one port can be specified"));
    }

    /**
     * Tests for no -port option value is not a number
     */
    @Test
    void testPortNotNumber() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "notNum");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("-port value must be a number"));
    }

    /**
     * Tests for wring number of args
     */
    @Test
    void testWrongArgNum() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "hi", "whats", "up");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Incorrect number of arguments supplied."));
    }

    /**
     * Tests for -print and -search being incompatible
     */
    @Test
    void testNoPrintAndSearch() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "-search", "-print");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("-print is not supported when not adding a new flight"));
    }

    /**
     * Tests for  -search wrong number of args
     */
    @Test
    void testSearchWrongArgNum() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "-search", "airline", "src");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Incorrect arguments for search option"));
    }

    /**
     * Tests for -print and airline being incompatible
     */
    @Test
    void testNoPrintAndAirline() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "-print", "airline");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("-print is not supported when not adding a new flight"));
    }

    /**
     * Tests for new flight with flightNum not a number
     */
    @Test
    void testNewFlightBadNum() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "NotNum", "PDX", "1/2/2005", "1:55", "AM", "LAX", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("flightNumber must be a number"));
    }

    /**
     * Tests for new flight with bad src length
     */
    @Test
    void testNewFlightBadSrcLen() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PD", "1/2/2005", "1:55", "AM", "LAX", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("src must be a 3 letters long"));
    }

    /**
     * Tests for new flight with bad src that contains numbers
     */
    @Test
    void testNewFlightBadSrcWithNumbers() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "P0X", "1/2/2005", "1:55", "AM", "LAX", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("src must contain only letters"));
    }

    /**
     * Tests for new flight with bad src that isn't a known airport code
     */
    @Test
    void testNewFlightBadSrcNotKnownAirport() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PPP", "1/2/2005", "1:55", "AM", "LAX", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("src must be a known airport code"));
    }

    /**
     * Tests for new flight with bad depart am/pm
     */
    @Test
    void testNewFlightBadDepartTimeAmPm() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PDX", "1/2/2005", "1:55", "A", "LAX", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("depart time was entered incorrectly, must be in the form hh:mm a"));
    }

    /**
     * Tests for new flight with bad depart
     */
    @Test
    void testNewFlightBadDepartTime() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PDX", "1/22005", "1:55", "AM", "LAX", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("depart date and time must be in the format mm/dd/yyyy hh:mm a"));
    }

    /**
     * Tests for new flight with bad dest length
     */
    @Test
    void testNewFlightBadDestLen() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PDX", "1/2/2005", "1:55", "AM", "LX", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("dest must be a 3 letters long"));
    }

    /**
     * Tests for new flight with bad dest that contains numbers
     */
    @Test
    void testNewFlightBadDestWithNumbers() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PDX", "1/2/2005", "1:55", "AM", "L0X", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("dest must contain only letters"));
    }

    /**
     * Tests for new flight with bad dest that isn't a known airport code
     */
    @Test
    void testNewFlightBadDestNotKnownAirport() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PDX", "1/2/2005", "1:55", "AM", "LLL", "12/12/2005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("dest must be a known airport code"));
    }

    /**
     * Tests for new flight with bad arrive am/pm
     */
    @Test
    void testNewFlightBadArriveTimeAmPm() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PDX", "1/2/2005", "1:55", "AM", "LAX", "12/12/2005", "11:19", "P");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("arrive time was entered incorrectly, must be in the form hh:mm a"));
    }

    /**
     * Tests for new flight with bad arrive
     */
    @Test
    void testNewFlightBadArriveTime() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "PDX", "1/2/2005", "1:55", "AM", "LAX", "12/122005", "11:19", "PM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("arrive date and time must be in the format mm/dd/yyyy hh:mm a"));
    }

    /**
     * Tests for new flight with bad arrive
     */
    @Test
    void testNewFlightArriveBeforeDepart() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline", "8932", "LAX", "12/12/2005", "11:19", "PM", "PDX", "1/2/2005", "1:55", "AM");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("arrival time must be after departure time"));
    }

    /**
     * Tests for searching without src and dest args
     */
    @Test
    void testGoodSearchNoSrcAndDest() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "-search", "Airline");
        //assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("arrival time must be after departure time"));
    }

    /**
     * Tests for searching with src and dest args
     */
    @Test
    void testGoodSearch() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "-search", "Airline", "PDX", "LAX");
        //assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("arrival time must be after departure time"));
    }

    /**
     * Tests for new flight with good arguments and -print
     */
    @Test
    void testGoodAdd() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "-print", "Airline", "8932", "PDX", "1/2/2005", "1:55", "AM", "LAX", "12/12/2005", "11:19", "PM");
        //assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("arrival time must be after departure time"));
    }

    /**
     * Tests for searching by airline without the -search flag
     */
    @Test
    void testGoodAirline() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "1267", "Airline");
        //assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("arrival time must be after departure time"));
    }
}