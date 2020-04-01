package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

/**
 * Displays the image details when on a phone
 */
public class NASADailyEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Intent fromFavouritesList = getIntent();
        Bundle dataToPass = fromFavouritesList.getBundleExtra("data");

        NASADailyImageFragment dFragment = new NASADailyImageFragment();
        dFragment.setTablet(false);
        dFragment.setArguments( dataToPass ); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentEmpty, dFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment.
    }
}