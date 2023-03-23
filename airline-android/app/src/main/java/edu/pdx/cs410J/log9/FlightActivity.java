package edu.pdx.cs410J.log9;

import static java.lang.Integer.parseInt;

import static edu.pdx.cs410J.log9.Project6.formatDateAndTime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

public class FlightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void backToMainMenu(View view){
        finish();
    }

    public void addFlight(View view) {
        EditText airlineNameInput = findViewById(R.id.airline_name);
        EditText flightNumberInput = findViewById(R.id.flight_number);
        EditText srcInput = findViewById(R.id.src);
        EditText arriveDateInput = findViewById(R.id.arriveDate);
        EditText arriveTimeInput = findViewById(R.id.arriveTime);
        EditText destInput = findViewById(R.id.dest);
        EditText departDateInput = findViewById(R.id.departDate);
        EditText departTimeInput = findViewById(R.id.departTime);

        String airlineName = airlineNameInput.getText().toString();
        String flightNumberString = flightNumberInput.getText().toString();
        String src = srcInput.getText().toString();
        String arriveDate = arriveDateInput.getText().toString();
        String arriveTime = arriveTimeInput.getText().toString();
        String dest = destInput.getText().toString();
        String departDate = departDateInput.getText().toString();
        String departTime = departTimeInput.getText().toString();

        if (airlineName.equals("")) {
            Toast.makeText(this, "Please enter an airline name", Toast.LENGTH_SHORT).show();
            return;
        }

        int flightNumber = 0;
        if (flightNumberString.equals("")) {
            Toast.makeText(this, "Please enter a flight number", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                flightNumber = parseInt(flightNumberString);
            } catch (NumberFormatException err) {
                Toast.makeText(this, "Flight number must be a number", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (src.equals("")) {
            Toast.makeText(this, "Please enter a Source airport", Toast.LENGTH_SHORT).show();
            return;
        } else if (src.length() != 3) {
            Toast.makeText(this, "source must be a 3 letters long", Toast.LENGTH_SHORT).show();
            return;
        } else if (!(src.matches("[a-zA-Z]+"))) {
            Toast.makeText(this, "source must contain only letters", Toast.LENGTH_SHORT).show();
            return;
        } else if (AirportNames.getName(src.toUpperCase()) == null) {
            Toast.makeText(this, "source must be a known airport code", Toast.LENGTH_SHORT).show();
            return;
        } else {
            src = src.toUpperCase();
        }

        Date depart = null;
        try{
            depart = formatDateAndTime(departDate, departTime, "depart");
        } catch (ParserException e) {
            Toast.makeText(this, "departure date and time must be in the format mm/dd/yyyy hh:mm a", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dest.equals("")){
            Toast.makeText(this, "Please enter a Destination airport", Toast.LENGTH_SHORT).show();
            return;
        } else if(dest.length() != 3){
            Toast.makeText(this, "destination must be a 3 letters long", Toast.LENGTH_SHORT).show();
            return;
        } else if(!(dest.matches("[a-zA-Z]+"))){
            Toast.makeText(this, "destination must contain only letters", Toast.LENGTH_SHORT).show();
            return;
        } else if(AirportNames.getName(dest.toUpperCase()) == null) {
            Toast.makeText(this, "destination must be a known airport code", Toast.LENGTH_SHORT).show();
            return;
        } else{
            dest = dest.toUpperCase();
        }

        Date arrive = null;
        try{
            arrive = formatDateAndTime(arriveDate, arriveTime, "arrive");
        } catch (ParserException e) {
            Toast.makeText(this, "arrival date and time must be in the format mm/dd/yyyy hh:mm a", Toast.LENGTH_SHORT).show();
            return;
        }

        if(depart.after(arrive)){
            Toast.makeText(this,"arrival time must be after departure time", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            MainActivity.project6.addFlight(airlineName, new Flight(src, dest, depart, arrive, flightNumber));
        } catch (Resources.NotFoundException e) {
            Toast.makeText(this, "The airline you tried to add to does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        StringWriter sw = new StringWriter();
        TextDumper dumper = new TextDumper(sw);
        dumper.dump(MainActivity.project6.searchAirline(airlineName));

        String fileName = MainActivity.project6.searchAirline(airlineName).getName() + ".txt";
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, fileName));
            writer.write(sw.toString().getBytes());
            writer.close();
        } catch (IOException e) {
            Toast.makeText(this, "Airline could not be saved", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show();
    }
}