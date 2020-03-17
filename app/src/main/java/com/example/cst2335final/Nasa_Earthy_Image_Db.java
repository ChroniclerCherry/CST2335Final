/**Course Name: CST2335
 * Class Name: Nasa_Earthy_Image_Db
 * Date: 03/16/2020
 *
 * Activity class for the view activity_nasa_earthy_image_db.xml. This activity will take user input for
 * latitude and longitude and output an image with details using Google's Earthy Image API. The user can
 * then choose to save the image into a favorites list, which will store the query details in the SQLite
 * database and the user can go to the Favorites_List Activity class to view the favorites list.
 *
 * @author Karl Rezansoff 040955782
 * @version 1.0
 */
package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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
import java.util.ArrayList;

import static org.xmlpull.v1.XmlPullParser.START_TAG;

public class Nasa_Earthy_Image_Db extends AppCompatActivity {
    /**
     * Represents the "search" button in activity_nasa_earthy_image_db.xml.
     */
    private Button searchBtn;
    /**
     * Represents the "view favorites" button in activity_nasa_earthy_image_db.xml.
     */
     private Button favoritesBtn;
    /**
     * Represents the "add to favorites" button in activity_nasa_earthy_image_db.xml.
     */
    private Button addToFavoritesBtn;
    /**
     * Represents the latitude input box in activity_nasa_earthy_image_db.xml.
     */
    private EditText latitudeEditText;
    /**
     * Represents the longitude input box in activity_nasa_earthy_image_db.xml.
     */
    private EditText longitudeEditText;
    /**
     * Represents the image placeholder in activity_nasa_earthy_image_db.xml.
     */
    ImageView imageImageView;
    /**
     * TextView variable for latitude that will show after search button is pressed and query is executed.
     */
    TextView latitudeTextView;
    /**
     *TextView variable for longitude that will show after the search button is pressed and query is executed.
     */
    TextView longitudeTextView;
    /**
     * TextView variable for date that will show after the.
     */
    TextView dateTextView;
    /**
     * SQLiteDatabase variable for accessing our database.
     */
    SQLiteDatabase db;
    /**
     * {@value} Constant to represent the database date column name.
     */
    private final static String DATE = "date";
    /**
     * {@value} Constant to represent the database latitude column name.
     */
    private final static String LATITUDE = "latitude";
    /**
     * {@value} Constant to represent the database longitude column name.
     */
    private final static String LONGITUDE = "longitude";
    /**
     *Progress bar variable for the progress bar in activity_nasa_earthy_image_db.xml.
     */
    private ProgressBar progressBar;

    /**
     * Inside the onCreate method we initialize all the widgets from activity_nasa_earthy_image_db.xml and adds button action listeners that will
     * call the appropriate methods.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa__earthy__image__db);

        //Widgets in top portion
        addToFavoritesBtn = findViewById(R.id.add_to_favorites_btn);
        imageImageView = findViewById(R.id.earthy_image);
        latitudeTextView = findViewById(R.id.latitude_textView);
        longitudeTextView = findViewById(R.id.longitude_textView);
        dateTextView = findViewById(R.id.date_textView);
        progressBar = findViewById(R.id.progress_bar);

        //Widgets in input section
        searchBtn = findViewById(R.id.search_btn);
        favoritesBtn = findViewById(R.id.view_saved_img_btn);
        latitudeEditText = findViewById(R.id.enter_latitude);
        longitudeEditText = findViewById(R.id.enter_longitude);

        //Search button toast message and query
        String searchBtnToastMessage = getResources().getString(R.string.search_btn_toast);
        searchBtn.setOnClickListener( v -> {
            Toast.makeText(this, searchBtnToastMessage , Toast.LENGTH_LONG).show();

            //make progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            //calling imageQuery to get values from api
            String earthyImageUrl = "https://api.nasa.gov/planetary/earth/imagery/?lon=" + longitudeEditText.getText().toString() + "&lat=" + latitudeEditText.getText().toString() + "&date=2014-02-01&api_key=iY2EZsiVhnakTqyq7aYKtflTqZ0wdmWjeZKbinKU";
            ImageQuery imageQuery = new ImageQuery();
            imageQuery.execute(earthyImageUrl);
            //setting text after query
            latitudeTextView.setText("Latitude: " + latitudeEditText.getText().toString());
            longitudeTextView.setText("Longitude: " + longitudeEditText.getText().toString());
            latitudeEditText.setText("");
            longitudeEditText.setText("");
        });

        //Add to Favorites
        addToFavoritesBtn.setOnClickListener((click) -> {
            String latitude = latitudeTextView.getText().toString();
            String longitude = longitudeTextView.getText().toString();
            String date = dateTextView.getText().toString();

            if (!latitude.equals("") && !longitude.equals("")) {
                //Get a db connection
                Earthy_Image_MyOpener dbOpener = new Earthy_Image_MyOpener(this);
                db = dbOpener.getWritableDatabase();

                //add new row to db
                ContentValues newRowValues = new ContentValues();

                //value for db columns
                newRowValues.put(Earthy_Image_MyOpener.LATITUDE, latitude);
                newRowValues.put(Earthy_Image_MyOpener.LONGITUDE, longitude);
                newRowValues.put(Earthy_Image_MyOpener.DATE, date);

                //insert into db which returns id
                long newId = db.insert(Earthy_Image_MyOpener.TABLE_NAME, null, newRowValues);

                Snackbar addToDbSnackbar = Snackbar.make(addToFavoritesBtn, getResources().getString(R.string.add_snackbar), Snackbar.LENGTH_LONG);
                addToDbSnackbar.show();
            }
        });

        //Go to favorites
        Intent goToFavorites = new Intent(this, Favorites_List.class);
        favoritesBtn.setOnClickListener( click -> startActivity(goToFavorites));

    } //end of onCreate method

    /**
     * Class extends from AsyncTask and is used for performing the image query with Google's Earthy Image API,
     * modifying the TextViews with query results, and will update a progress bar as data is retrieved.
     */
    private class ImageQuery extends AsyncTask<String, Integer, String> {
        /**
         * String for date.
         */
        private String date;
        /**
         * String for latitude.
         */
        private String latitude;
        /**
         * String for longitude.
         */
        private String longitude;
        /**
         * String for imageUrl. API will return a seperate url to retrieve the image.
         */
        private String imageUrl;
        /**
         * Bitmap object to store the image from the API.
         */
        private Bitmap image;

        /**
         * Query using Google's Earthy Image API done in this method, and will set values for date, latitude, longitude, imageUrl, and image.
         * @param args Passing a single string parameter being the API url with the latitude and longitude values from the user.
         * @return String error message
         */
        @SuppressLint("WrongThread")
        protected String doInBackground(String... args) {
            String returnString = null;

            try {
                //url object of server to contact
                URL imageURL = new URL(args[0]);
                //open connection
                HttpURLConnection urlConnection = (HttpURLConnection) imageURL.openConnection();
                //wait for data
                InputStream response = urlConnection.getInputStream();

                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject earthyImageJson = new JSONObject(result);

                //get the double associated with "value"
                date = earthyImageJson.getString("date");
                publishProgress(25);
                imageUrl = earthyImageJson.getString("url");
                publishProgress(50);

                //getting image from a seperate url
                image = null;
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                }   publishProgress(75);

            }
            catch (FileNotFoundException e) {
                return getResources().getString(R.string.query_error_toast);
                // find a way to inform user error was made and not let it exit out - bad params (lat 2, long 77)
            }

            catch (Exception e) {
                returnString = "error";
            }
            publishProgress(100);
            return returnString;
        }

        /**
         * Method will update the xml widgets with the values retrieved from doInBackground method (results from API query).
         * @param sentFromDoInBackground String values from sentFromBackground method.
         */
        @Override
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);

            imageImageView.setImageBitmap(image);
            dateTextView.setText("Date: " + date.substring(0,10));
            progressBar.setVisibility(View.INVISIBLE);
            addToFavoritesBtn.setVisibility(View.VISIBLE);
        }

        /**
         * Method only is used for updating the progress bar widget and setting it's visability.
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
    }
    }
