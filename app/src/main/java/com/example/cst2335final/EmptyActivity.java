package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

/**
 * The activity launched when on a phone and the user chooses to view an image's details
 *
 */
public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Intent fromFavouritesList = getIntent();
        Bundle dataToPass = fromFavouritesList.getBundleExtra("data");

        NASADailyImageFragment dFragment = new NASADailyImageFragment(); //add a DetailFragment
        dFragment.setTablet(false);
        dFragment.setArguments( dataToPass ); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentEmpty, dFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment.
    }
}