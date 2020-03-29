package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NASADailyLoading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_daily_loading);

        Intent fromFavourites = getIntent();
        String url = fromFavourites.getStringExtra("URL");
        
        Log.e("Url:",url);
        
    }
}
