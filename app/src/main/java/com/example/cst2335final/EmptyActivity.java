package com.example.cst2335final;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cst2335final.R;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java lines 47-54
        NewsReaderFragment newFragment = new NewsReaderFragment();
        newFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, newFragment)
                .commit();
    }
}