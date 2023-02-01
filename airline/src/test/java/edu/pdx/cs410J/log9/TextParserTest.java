package edu.pdx.cs410J.log9;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TextParser} class.*
 */
public class TextParserTest {

  /**
   * Checks that a valid file can be parsed and matches
   *
   * @throws ParserException if the file does not match the correct format
   */
  @Test
  void validTextFileCanBeParsed() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-airline.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    Airline airline = parser.parse();
    assertThat(airline.getName(), equalTo("Test Airline"));
  }

  /**
   * If a file is empty then a ParserException is thrown
   */
  @Test
  void invalidTextFileThrowsParserException() {
    InputStream resource = getClass().getResourceAsStream("empty-airline.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    assertThrows(ParserException.class, parser::parse);
  }

  /**
   * Reads a correctly formatted file with flights in it and parses correctly
   *
   * @throws ParserException ParserException if the file does not match the correct format
   */
  @Test
  void testCorrectPath() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("test.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    Airline airline = parser.parse();
    assertNotNull(airline);
  }
}
