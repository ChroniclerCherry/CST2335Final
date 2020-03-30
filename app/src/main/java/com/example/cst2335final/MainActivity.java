package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Go to NASA Earthy Image Db activity
        Button earthyImageBtn = findViewById(R.id.karls_activity_button);
        Intent goToEarthyImageDb = new Intent(this, Nasa_Earthy_Image_Db.class);
        earthyImageBtn.setOnClickListener( click -> startActivity(goToEarthyImageDb));
    }
}
