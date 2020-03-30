package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bbc = findViewById(R.id.buttonBBC);

        //temp button set up
        bbc.setOnClickListener(click -> {
            Intent goToNewsReader = new Intent(MainActivity.this, NewsReaderSearch.class);
            startActivity(goToNewsReader);
        });
    }
}
