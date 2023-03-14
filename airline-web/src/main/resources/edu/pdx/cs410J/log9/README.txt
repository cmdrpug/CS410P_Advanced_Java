This is a README file!

410J Project 5 written by Logan Anderson

This project takes uses a client and server to create airlines with
flights stored in them. You can create and search flights to be sent
to the server using the command line interface:

usage: java -jar target/airline-client.jar [options] <args>
args are (in this order):
  airline The name of the airline
  flightNumber The flight number
  src Three-letter code of departure airport
  depart Departure date/time
  dest Three-letter code of arrival airport
  arrive Arrival date/time
options are (options may appear in any order):
  -host hostname Host computer on which the server runs
  -port port Port on which the server is listening
  -search Search for flights
  -print Prints a description of the new flight
  -README Prints a README for this project and exits