package edu.pdx.cs410J.log9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.checkerframework.checker.units.qual.A;

public class AirlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airline);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void backToMainMenu(View view){
        finish();
    }

    public void addAirline(View view){
        EditText airlineNameInput = findViewById(R.id.airline_name);
        String airlineName = airlineNameInput.getText().toString();
        if(airlineName.equals("")){
            Toast.makeText(this, "Please enter an airline name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            MainActivity.project6.addAirline(new Airline(airlineName));
        } catch (RuntimeException e) {
            Toast.makeText(this, "Airline already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Successfully created", Toast.LENGTH_SHORT).show();
    }
}