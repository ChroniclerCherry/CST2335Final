package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bbc.findViewById(R.id.buttonBBC);

//        //temp button set up
//        bbc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View click) {
//
//                Intent goToNewsReader = new Intent(MainActivity.this, NewsReaderView.class);
//                // goToProfile.putExtra("EMAIL",email.getText().toString());
//                MainActivity.this.startActivity(goToNewsReader);
//            }
//        });
    }
}
