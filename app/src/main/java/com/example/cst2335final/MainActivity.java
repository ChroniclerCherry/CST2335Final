package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    Button bbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //click sends to replace with lambda - temp button set up
        bbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click) {

                Intent goToNewsReader = new Intent(MainActivity.this, NewsReaderView.class);
                // goToProfile.putExtra("EMAIL",email.getText().toString());
                MainActivity.this.startActivity(goToNewsReader);
            }
        });
    }
}
