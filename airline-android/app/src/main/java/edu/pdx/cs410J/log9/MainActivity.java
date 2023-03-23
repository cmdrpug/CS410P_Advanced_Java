package edu.pdx.cs410J.log9;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    public static Project6 project6 = new Project6();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        File path = getApplicationContext().getFilesDir();
        for(File file : path.listFiles()){
            if((file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("txt"))){
                String fileName = file.getName().replaceFirst("[.][^.]+$", "");
                try {
                    project6.addFromFile(new FileReader(file), fileName);
                } catch (FileNotFoundException e) {

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return true;
    }

    public void launchAirline(View view){
        startActivity(new Intent(this, AirlineActivity.class));
    }

    public void launchFlight(View view){
        startActivity(new Intent(this, FlightActivity.class));
    }

    public void launchSearch(View view){
        startActivity(new Intent(this, SearchActivity.class));
    }

    public void openREADME(MenuItem item) {
        final Dialog readme = new Dialog(MainActivity.this);
        //readme.requestWindowFeature(Window.FEATURE_NO_TITLE);
        readme.setCancelable(true);
        readme.setContentView(R.layout.readme_dialog);
        Button close = readme.findViewById(R.id.close);

        close.setOnClickListener((e) -> {
            readme.dismiss();
        });

        readme.show();
    }
}