package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Intent fromChat = getIntent();
        Bundle dataToPass = fromChat.getBundleExtra("data");

        NASADailyImageFragment dFragment = new NASADailyImageFragment(); //add a DetailFragment
        dFragment.setTablet(false);
        dFragment.setArguments( dataToPass ); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentEmpty, dFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment.
    }
}