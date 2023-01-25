package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project1.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
  }

    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("arg", "arg", "arg", "arg", "arg", "arg", "arg", "arg", "arg", "arg");
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }

    @Test
    void testTooFewCommandLineArguments() {
        MainMethodResult result = invokeMain("arg", "arg");
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect number of arguments supplied."));
    }
}