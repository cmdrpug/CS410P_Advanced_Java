package edu.pdx.cs410J.log9;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.StringWriter;

import edu.pdx.cs410J.AirportNames;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void backToMainMenu(View view){
        finish();
    }

    public void search(View view){
        EditText airlineNameInput = findViewById(R.id.airline_name);
        EditText srcInput = findViewById(R.id.src);
        EditText destInput = findViewById(R.id.dest);

        String airlineName = airlineNameInput.getText().toString();
        String src = srcInput.getText().toString();
        String dest = destInput.getText().toString();
        boolean optional = false;

        if(airlineName.equals("")){
            Toast.makeText(this, "Please enter an airline name", Toast.LENGTH_SHORT).show();
            return;
        }

        if((src.equals("") && (!dest.equals(""))) || ((!src.equals("")) && dest.equals(""))){
            Toast.makeText(this, "Both optional parameters must be used together", Toast.LENGTH_SHORT).show();
            return;
        } else if((!src.equals("")) && (!dest.equals(""))){
            optional = true;
            if(src.length() != 3){
                Toast.makeText(this, "source must be a 3 letters long", Toast.LENGTH_SHORT).show();
                return;
            } else if(!(src.matches("[a-zA-Z]+"))){
                Toast.makeText(this, "source must contain only letters", Toast.LENGTH_SHORT).show();
                return;
            } else if(AirportNames.getName(src.toUpperCase()) == null) {
                Toast.makeText(this, "source must be a known airport code", Toast.LENGTH_SHORT).show();
                return;
            } else {
                src = src.toUpperCase();
            }

            if(dest.length() != 3){
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
        }

        Airline airline = null;
        if(optional){
            try{
                airline = MainActivity.project6.searchAirline(airlineName, src, dest);
            } catch (Resources.NotFoundException e) {
                Toast.makeText(this, "Airline does not exist", Toast.LENGTH_SHORT).show();
                return;
            }
        } else{
            try {
                airline = MainActivity.project6.searchAirline(airlineName);
            } catch (Resources.NotFoundException e) {
                Toast.makeText(this, "Airline does not exist", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        final Dialog searchResult = new Dialog(SearchActivity.this);
        searchResult.setCancelable(true);
        searchResult.setContentView(R.layout.search_dialog);
        Button close = searchResult.findViewById(R.id.close);

        close.setOnClickListener((e) -> {
            searchResult.dismiss();
        });

        StringWriter sw = new StringWriter();
        PrettyPrinter prettyPrinter = new PrettyPrinter(sw);
        prettyPrinter.dump(airline);

        TextView result = searchResult.findViewById(R.id.result);
        result.setMovementMethod(new ScrollingMovementMethod());
        result.setText(sw.toString());

        searchResult.show();
    }
}