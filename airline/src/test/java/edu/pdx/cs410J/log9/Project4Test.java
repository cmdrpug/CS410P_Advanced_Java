package edu.pdx.cs410J.log9;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import static edu.pdx.cs410J.log9.Project4.formatDateAndTime;
import static edu.pdx.cs410J.log9.Project4.printREADME;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A unit test for code in the <code>Project4</code> class.  This is different
 * from <code>Project4IT</code> which is an integration test (and can capture data)
 * written to {@link System#out} and the like.
 */
class Project4Test {

  /**
   * Test to see if the README.txt file is readable as a resource
   * and matches the correct text
   *
   * @throws IOException if anything goes wrong it will throw IO
   */
  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project4.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("This is a README file!"));
    }
  }

  /**
   * Similar to readmeCanBeReadAsResource(), this tests the same functionality
   * but from within the code itself.
   *
   * @throws IOException if anything goes wrong it will throw IO
   */
  @Test
  void readmeFunctionCanBeReadAsResource() throws IOException {
    printREADME();
  }

  /**
   * Creates a bunch of String variations of valid and invalid
   * dates and times and tests both combinations that should work
   * and combinations that shouldn't. formatDataAndTime returns null
   * if the format is incorrect so assertNotNull and assertNull are used
   * to test if input was accepted.
   */
  @Test
  void testDateAndTimeFormats(){
    String doubleDoubleDate = "12/12/2001";
    String doubleSingleDate = "12/1/2001";
    String singleDoubleDate = "2/12/2001";
    String singleSingleDate = "2/2/2001";
    String noneSingleDate = "/12/2003";
    String doubleDoubleTime = "12:12 AM";
    String doubleSingleTime = "12:1 PM";
    String singleDoubleTime = "1:12 PM";
    String singleSingleTime = "1:2 AM";

    Date testDate = formatDateAndTime(doubleDoubleDate, doubleDoubleTime, "test");
    assertNotNull(testDate);
    testDate = formatDateAndTime(singleDoubleDate, singleDoubleTime, "test");
    assertNotNull(testDate);
    testDate = formatDateAndTime(doubleSingleDate, singleDoubleTime, "test");
    assertNotNull(testDate);
    testDate = formatDateAndTime(singleSingleDate, singleDoubleTime, "test");
    assertNotNull(testDate);

    testDate = formatDateAndTime(doubleDoubleDate, doubleSingleTime, "test");
    assertNull(testDate);
    testDate = formatDateAndTime(doubleDoubleDate, singleSingleTime, "test");
    assertNull(testDate);
    testDate = formatDateAndTime(noneSingleDate, doubleDoubleTime, "test");
    assertNull(testDate);
  }
}
