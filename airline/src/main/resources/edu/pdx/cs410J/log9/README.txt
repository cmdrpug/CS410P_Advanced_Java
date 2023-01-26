This is a README file!

This project takes command line arguments to create Flight and Airline objects.
The flight is then added to the airline and optionally printed with the -print option.

Usage: java -jar target/airline-2023.0.0.jar [options] <args>

args are (in this order):
airline         The name of the airline
flightNumber    The flight number
src             Three-letter code of departure airport
depart          Departure date and time (24-hour time)
dest            Three-letter code of arrival airport
arrive          Arrival date and time (24-hour time)

options are (options may appear in any order):
-print          Prints a description of the new flight
-README         Prints a README for this project and exits

Date and time should be in the format: mm/dd/yyyy hh:mm