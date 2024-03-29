package edu.pdx.cs410J.log9;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String definedWordAs(String airlineName)
    {
        return String.format( "Flight was successfully added to %s", airlineName);
    }

}
