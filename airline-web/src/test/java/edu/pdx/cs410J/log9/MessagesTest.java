package edu.pdx.cs410J.log9;

import org.junit.jupiter.api.Test;

import static edu.pdx.cs410J.log9.Messages.definedWordAs;
import static edu.pdx.cs410J.log9.Messages.missingRequiredParameter;

public class MessagesTest {
    @Test
    void canCreate(){
        Messages messages = new Messages();
        missingRequiredParameter(AirlineServlet.AIRLINE_PARAMETER);
        definedWordAs("airline");
    }
}
