package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NASADailyLoading extends AppCompatActivity {

    private String fullUrl;

    private TextView loadingText;
    private ProgressBar progress;

    private ImageView imageView;
    private TextView titleText;
    private TextView dateText;
    private TextView descriptionText;
    private CheckBox favouriteCheck;

    private String title;
    private String date;
    private String description;

    private String imgUrl;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_daily_loading);

        //get the url to scrape
        Intent fromFavourites = getIntent();
        fullUrl = fromFavourites.getStringExtra("URL");
        
        Log.e("Url:", fullUrl);

        //get references to all the GUI elements
        loadingText = findViewById(R.id.NASADaily_loadingTextView);
        progress = findViewById(R.id.NASADaily_progressBar);
        imageView = findViewById(R.id.NASADaily_image);
        titleText = findViewById(R.id.NASADaily_title);
        dateText = findViewById(R.id.NASADaily_imageDate);
        descriptionText = findViewById(R.id.NASADaily_description);
        favouriteCheck = findViewById(R.id.NASADaily_favouriteButton);

        //sets text letting the user know that things are loading from the right url
        loadingText.setText(getString(R.string.NASADaily_loadingText) + fullUrl);

        //make the description scrollable
        descriptionText.setMovementMethod(new ScrollingMovementMethod());

        //hide everything but the progress bar and progress description
        imageView.setVisibility(View.INVISIBLE);
        titleText.setVisibility(View.INVISIBLE);
        dateText.setVisibility(View.INVISIBLE);
        descriptionText.setVisibility(View.INVISIBLE);
        favouriteCheck.setVisibility(View.INVISIBLE);

        //start scraping
        NASADailyQuery request = new NASADailyQuery();
        request.execute();
        
    }

    class NASADailyQuery extends AsyncTask< String, Integer, String> {

        String mediaType;

        @Override
        public String doInBackground(String ... args)
        {

            try {

                //create a URL object of what server to contact:
                URL url = new URL(fullUrl);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();


                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                // convert string to JSON:
                JSONObject imageData = new JSONObject(result);
                date = imageData.getString("date");
                publishProgress(20);
                description = imageData.getString("explanation");
                publishProgress(40);
                title = imageData.getString("title");
                publishProgress(60);
                imgUrl = imageData.getString("url");
                String imgPath = "NASADaily" + date + ".png";
                publishProgress(80);
                mediaType = imageData.getString("media_type");

                //load the images
                if (mediaType.equals("image")){
                    if(fileExist(imgPath)){
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(imgPath);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }  image = BitmapFactory.decodeStream(fis);
                    }
                    else {
                        URL imageUrl = new URL(imgUrl);
                        urlConnection = (HttpURLConnection) imageUrl.openConnection();
                        urlConnection.connect();
                        response = urlConnection.getInputStream();
                        image = BitmapFactory.decodeStream(response);

                        //save images
                        FileOutputStream fos = openFileOutput(imgPath, Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, fos);
                        fos.flush();
                        fos.close();
                        urlConnection.disconnect();
                    }
                }

                publishProgress(100);
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String args){
            //hide loading stuff and make them take no space
            if (mediaType.equals("image")){
                loadingText.setVisibility(View.GONE);
            } else {
                loadingText.setHighlightColor(Color.BLUE);
                loadingText.setText(R.string.NASADaily_noImage);
            }

            progress.setVisibility(View.GONE);

            //only show the image if the media type is a video
            if (mediaType.equals("image"))
                imageView.setVisibility(View.VISIBLE);

            //unhide everything else
            imageView.setVisibility(View.VISIBLE);
            titleText.setVisibility(View.VISIBLE);
            dateText.setVisibility(View.VISIBLE);
            descriptionText.setVisibility(View.VISIBLE);
            favouriteCheck.setVisibility(View.VISIBLE);

            //set values
            imageView.setImageBitmap(image);
            titleText.setText(title);
            dateText.setText(date);
            descriptionText.setText(description);
        }

        @Override
        protected void onProgressUpdate(Integer ... value){
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(value[0]);
        }

        public boolean fileExist(String name){
            File file = getBaseContext().getFileStreamPath(name);
            return file.exists();
        }
    }
}
