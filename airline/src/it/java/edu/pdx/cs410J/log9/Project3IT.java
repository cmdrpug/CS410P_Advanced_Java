package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * An integration test for the {@link Project3} main class.
 */
class Project3IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project3.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
  }

    /**
     * Tests that invoking the main method with more than 8 arguments besides options issues an error
     */
    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("arg", "arg", "arg", "arg", "arg", "arg", "arg", "arg", "arg", "arg");
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }

    /**
     * Tests that invoking the main method with less than 8 arguments besides options issues an error
     */
    @Test
    void testTooFewCommandLineArguments() {
        MainMethodResult result = invokeMain("arg", "arg");
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }

    /**
     * Tests that invoking the main method with 8 valid arguments and -print writes the correct toString() output
     */
    @Test
    void testCorrectArguments() {
        MainMethodResult result = invokeMain("-print", "airline", "8932", "PDX", "1/2/2005", "1:55", "LAX", "12/12/2005", "11:19");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 8932 departs PDX at 01/02/2005 01:55 arrives LAX at 12/12/2005 11:19"));
    }

    /**
     * Tests that invoking the main method with invalid arguments issues an error
     */
    @Test
    void testInvalidOption() {
        MainMethodResult result = invokeMain("-corn", "airline", "8932", "PDX", "12/12/2005", "1:55", "LAX", "1/2/2005", "11:19");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Option: -corn was supplied"));
    }

    /**
     * Tests that invoking the main method with and invalid flightNumber issues an error
     */
    @Test
    void testFlightNumberIsNotANumber() {
        MainMethodResult result = invokeMain("-print", "airline", "89D32", "PDX", "12/12/2005", "1:55", "LAX", "1/2/2005", "11:19");
        assertThat(result.getTextWrittenToStandardError(), containsString("flightNumber must be a number"));
    }

    /**
     * Tests that invoking the main method with and invalid src issues an error
     */
    @Test
    void testSrcIsNotAThreeLetterCode() {
        MainMethodResult result = invokeMain("-print", "airline", "8932", "Goo goo", "12/12/2005", "1:55", "LAX", "1/2/2005", "11:19");
        assertThat(result.getTextWrittenToStandardError(), containsString("src must be a 3 letters long"));
    }

    /**
     * Tests that invoking the main method with and invalid dest issues an error
     */
    @Test
    void testDestIsNotAThreeLetterCode() {
        MainMethodResult result = invokeMain("-print", "airline", "8932", "PDX", "12/12/2005", "1:55", "Ga ga", "1/2/2005", "11:19");
        assertThat(result.getTextWrittenToStandardError(), containsString("dest must be a 3 letters long"));
    }

    /**
     * Tests the -README argument prints the readme
     */
    @Test
    void testReadmeOption() {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is a README file!"));
    }

    /**
     * Tests that using the -textFile option multiple times issues an error
     */
    @Test
    void multipleTextFileOptions() {
        MainMethodResult result = invokeMain("-print", "-textFile", "text.txt", "-textFile", "text2.txt", "airline", "8932", "PDX", "12/12/2005", "1:55", "Ga ga", "1/2/2005", "11:19");
        assertThat(result.getTextWrittenToStandardError(), containsString("Multiple .txt files cannot be used"));
    }

    /**
     * Tests that the text file missing ".txt" extension issues an error
     */
    @Test
    void textFileOptionMissionExtension() {
        MainMethodResult result = invokeMain("-print", "-textFile", "text", "airline", "8932", "PDX", "12/12/2005", "1:55", "Ga ga", "1/2/2005", "11:19");
        assertThat(result.getTextWrittenToStandardError(), containsString("The specified file must be a .txt file"));
    }

    /**
     * Tests that invoking the main method with a .txt file that doesn't exist still works
     */
    @Test
    void textFileNotFound(){
        MainMethodResult result = invokeMain("-print", "-textFile", "text.txt", "airline", "8932", "PDX", "1/2/2005", "1:55", "LAX", "12/12/2005", "11:19");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 8932 departs PDX at 01/02/2005 01:55 arrives LAX at 12/12/2005 11:19"));
    }

    @Test
    void prettyPrintToStandardOut(){
        MainMethodResult result = invokeMain("-pretty", "airline", "8932", "PDX", "1/2/2005", "1:55", "LAX", "12/12/2005", "11:19");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flights belonging to airline:"));
    }

    @Test
    void multiplePrettyCallsFails(){
        MainMethodResult result = invokeMain("-pretty", "pretty.txt", "-pretty", "pretty.txt", "airline", "8932", "PDX", "1/2/2005", "1:55", "LAX", "12/12/2005", "11:19");
        assertThat(result.getTextWrittenToStandardError(), containsString("Multiple .txt files cannot be used"));
    }

    @Test
    void prettyWithFile(){
        MainMethodResult result = invokeMain("-pretty", "pretty.txt", "airline", "8932", "PDX", "1/2/2005", "1:55", "LAX", "12/12/2005", "11:19");
        assertThat(result.getTextWrittenToStandardError(), containsString(""));
    }
}