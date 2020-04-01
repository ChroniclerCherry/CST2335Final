package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

/**
 * This activity takes a NASA image of the day url, loads and displays the information,
 * and allows users to add it to their favourites
 */
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

    SQLiteDatabase db;

    /**
     * Sets up the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_daily_loading);

        //get the url to scrape
        Intent fromFavourites = getIntent();
        fullUrl = fromFavourites.getStringExtra("URL");

        //open the database
        NASADailyOpener dbOpener = new NASADailyOpener(this);
        db = dbOpener.getWritableDatabase();

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

        favouriteCheck.setOnCheckedChangeListener((bv,isChecked) -> {
            if (isChecked){
                addNewImage();
                setResult(NASADailyFavourites.DATABASE_CHANGED);
            } else {
                removeImage();
                setResult(NASADailyFavourites.DATABASE_CHANGED);
            }
        });
    }

    /**
     * Removes the current image from the database
     */
    private void removeImage() {
        db.delete(NASADailyOpener.TABLE_NAME, NASADailyOpener.COL_DATE + "= ?",
                new String[] {date});
    }

    /**
     * Adds the current image to the database
     */
    private void addNewImage() {
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(NASADailyOpener.COL_DATE, date);
        newRowValues.put(NASADailyOpener.COL_DESCRIPTION, description);
        newRowValues.put(NASADailyOpener.COL_TITLE, title);

        long newId = db.insert(NASADailyOpener.TABLE_NAME, null, newRowValues);
    }

    /**
     * Close the database when this activity is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    /**
     * Async tasks that loads a json from the NASA api
     * publishes the results to a progress bar as it goes
     * then displays the details once done
     */
    class NASADailyQuery extends AsyncTask< String, Integer, String> {

        String mediaType;

        /**
         * Scrapes the data
         * @param args
         * @return
         */
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
                publishProgress(80);
                mediaType = imageData.getString("media_type");

                //create unique image path by date
                String imgPath = "NASADaily" + date + ".png";

                //load the images if media type is an image
                if (mediaType.equals("image")){
                    if(fileExist(imgPath)){ //if we already have the image saved, then load it locally
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(imgPath);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }  image = BitmapFactory.decodeStream(fis);
                    }
                    else { //if we don't have the image saved locally, then load it and save it
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
                //no file found means the API is incorrect or an invalid date was chosen
                if (e.getClass().equals(FileNotFoundException.class)){
                    setResult(NASADailyFavourites.INVALID_URL_ERROR);
                    cancel(true);
                    finish();
                }

                Log.e("Error", e.getMessage());
            }
            return "Done";
        }

        /**
         * After all data has been obtained, update the relevent visual elements
         * @param args
         */
        @Override
        protected void onPostExecute(String args){

            //if we got an image, hide the loading text
            if (mediaType.equals("image")){
                loadingText.setVisibility(View.GONE);
            } else { //if it's not an image, reuse the loading text view to inform the user that there is no image
                loadingText.setTextColor(Color.BLUE);
                loadingText.setText(getText(R.string.NASADaily_noImage));
            }

            //hides progress bar
            progress.setVisibility(View.GONE);

            //only show the image if the media type is image
            if (mediaType.equals("image"))
                imageView.setVisibility(View.VISIBLE);

            //unhide everything else
            imageView.setVisibility(View.VISIBLE);
            titleText.setVisibility(View.VISIBLE);
            dateText.setVisibility(View.VISIBLE);
            descriptionText.setVisibility(View.VISIBLE);
            favouriteCheck.setVisibility(View.VISIBLE);

            //set values to GUI elements
            imageView.setImageBitmap(image);
            titleText.setText(title);
            dateText.setText(date);
            descriptionText.setText(description);
        }

        /**
         * Updates the progress bar
         * @param value
         */
        @Override
        protected void onProgressUpdate(Integer ... value){
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(value[0]);
        }

        /**
         * Helper method to check if a file name exists
         * @param name - name of the file
         * @return - true if it exists, false if not
         */
        private boolean fileExist(String name){
            File file = getBaseContext().getFileStreamPath(name);
            return file.exists();
        }
    }
}
