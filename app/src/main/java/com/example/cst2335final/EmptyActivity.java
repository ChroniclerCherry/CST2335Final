package com.example.cst2335final;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Empty Activity = parent activity to hold fragment
 *  @Author Lia Brophy
 *  @Version 1.0
 *  @Date 2020-04-01
 */
public class EmptyActivity extends AppCompatActivity {

    /**
     * Sets up activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);

        //get the data that was passed from NewsReaderSearch
        Bundle dataToPass = getIntent().getExtras();
        NewsReaderFragment newFragment = new NewsReaderFragment();
        newFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, newFragment)
                .commit();
    }
}