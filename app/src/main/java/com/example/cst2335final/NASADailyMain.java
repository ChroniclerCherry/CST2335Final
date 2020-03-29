package com.example.cst2335final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class NASADailyMain extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    EditText apiEntry;

    String userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_daily_main);

        apiEntry = findViewById(R.id.NASADaily_ApiEditText);

        prefs = getSharedPreferences("NASADailyApi",Context.MODE_PRIVATE);

        userApi = prefs.getString("API", "");

        if (userApi != null){
            apiEntry.setText(userApi);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        edit = prefs.edit();
        edit.putString("API",apiEntry.getText().toString());
        edit.commit();
    }

    public void displayApiHelp(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.NasaDaily_apiInformation)
                .setPositiveButton(R.string.NASADaily_okay, (dialog, which) -> {
                })
        .setNegativeButton(R.string.NASADaily_goToSite, (dialog, which) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.nasa.gov/"));
            startActivity(browserIntent);
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void goToFavourites(View view) {
        Intent goToFavourites = new Intent(NASADailyMain.this, NASADailyFavourites.class);
        userApi = apiEntry.getText().toString();
        goToFavourites.putExtra("API",userApi);
        startActivity(goToFavourites);
    }
}
