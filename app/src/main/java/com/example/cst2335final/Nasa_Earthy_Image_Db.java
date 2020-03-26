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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
     * Represents the "give title" edit text in activity_nasa_earthy_image_db.xml.
     */
    private EditText favNameEditText;
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
     * SharedPreferences variable for storing latitude and longitude strings
     */
    private SharedPreferences prefs = null;
    /**
     * Toolbar variable for the Toolbar in activity_nasa_earthy_image_db.xml.
     */
    Toolbar tbar;

    /**
     * Inside the onCreate method we initialize all the widgets from activity_nasa_earthy_image_db.xml and adds button action listeners that will
     * call the appropriate methods.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa__earthy__image__db);

        //Show the toolbar
        tbar = findViewById(R.id.toolbar);
        setSupportActionBar(tbar);

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
        favNameEditText = findViewById(R.id.name_favorite);
        latitudeEditText = findViewById(R.id.enter_latitude);
        longitudeEditText = findViewById(R.id.enter_longitude);

        //Load shared preference
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        latitudeEditText.setText(prefs.getString("latitudeString", ""));
        longitudeEditText.setText(prefs.getString("longitudeString", ""));

        //Search button query
        String searchBtnToastMessage = getResources().getString(R.string.search_btn_toast);
        searchBtn.setOnClickListener( v -> {
            if (isEmpty(latitudeEditText)) {
                latitudeEditText.setError(getResources().getString(R.string.is_empty));;
                return;
            }

            if (isEmpty(longitudeEditText)) {
                longitudeEditText.setError(getResources().getString(R.string.is_empty));;
                return;
            }

            //make progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            //calling imageQuery to get values from api
            String earthyImageUrl = "https://api.nasa.gov/planetary/earth/imagery/?lon=" + longitudeEditText.getText().toString() + "&lat=" + latitudeEditText.getText().toString() + "&date=2014-02-01&api_key=iY2EZsiVhnakTqyq7aYKtflTqZ0wdmWjeZKbinKU";
            ImageQuery imageQuery = new ImageQuery();
            imageQuery.execute(earthyImageUrl);

            //saving input for shared preferences
            saveSharedPrefs(latitudeEditText.getText().toString(), longitudeEditText.getText().toString());
        });

        //Add to Favorites
        addToFavoritesBtn.setOnClickListener((click) -> {
            String latitude = latitudeTextView.getText().toString();
            String longitude = longitudeTextView.getText().toString();
            String date = dateTextView.getText().toString();
            String name = favNameEditText.getText().toString();

            if (!isEmpty(favNameEditText)) {
                    //Get a db connection
                    Earthy_Image_MyOpener dbOpener = new Earthy_Image_MyOpener(this);
                    db = dbOpener.getWritableDatabase();

                    //add new row to db
                    ContentValues newRowValues = new ContentValues();

                    //value for db columns
                    newRowValues.put(Earthy_Image_MyOpener.LATITUDE, latitude);
                    newRowValues.put(Earthy_Image_MyOpener.LONGITUDE, longitude);
                    newRowValues.put(Earthy_Image_MyOpener.DATE, date);
                    newRowValues.put(Earthy_Image_MyOpener.NAME, name);

                    //insert into db which returns id
                    long newId = db.insert(Earthy_Image_MyOpener.TABLE_NAME, null, newRowValues);

                    Snackbar addToDbSnackbar = Snackbar.make(addToFavoritesBtn, getResources().getString(R.string.add_snackbar), Snackbar.LENGTH_LONG);
                    addToDbSnackbar.show();
            }
            else favNameEditText.setError(getResources().getString(R.string.is_empty));
        });

        //Go to favorites
        Intent goToFavorites = new Intent(this, Favorites_List.class);
        favoritesBtn.setOnClickListener( click -> startActivity(goToFavorites));

    } //end of onCreate method

    /**
     * Method to check if an EditText is empty
     * @param etText EditText variable from XML
     * @return boolean value to represent if EditText is empty
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    /**
     * Method to create the options menu
     * @param menu Menu variable for the menu we want to inflate
     * @return boolean value to indicate menu was inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    /**
     * Method for handling what happens when an option is selected from menu.
     * @param item MenuItem object that was clicked.
     * @return boolean value to represent action is completed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId()) {
            case R.id.help_item:
                message = getResources().getString(R.string.help_menu_item);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.menu_title))
                //Message
                .setMessage(message)
                //what the Yes button does:
                .setPositiveButton(getResources().getString(R.string.ok), (click, arg) -> { })
                //Show the dialog
                .create().show();

        return true;
    }

    /**
     * Method to save the last input in the latitude and longitude edit texts
     * @param stringLatitude String for latitude
     * @param stringLongitude String for Longitude
     */
    private void saveSharedPrefs(String stringLatitude, String stringLongitude) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("latitudeString", stringLatitude);
        editor.putString("longitudeString", stringLongitude);
        editor.commit();
    }

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
         * String to store error message while loading from api.
         */
        private String returnString = null;

        /**
         * Query using Google's Earthy Image API done in this method, and will set values for date, latitude, longitude, imageUrl, and image.
         * @param args Passing a single string parameter being the API url with the latitude and longitude values from the user.
         * @return String error message
         */
        @Override
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
            // find a way to inform user error was made and not let it crash - bad params (lat 2, long 77)
            catch (FileNotFoundException fnfe) {
                returnString = "FileNotFoundException";
            }
            catch (MalformedURLException mfe) {
                returnString = "MalFormed URL exception";
            } catch (IOException ioe) {
                returnString = "IOException";
            } catch (JSONException jsone) {
                returnString = "JSON Exception";
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

            if(returnString != null) {
                imageImageView.setImageBitmap(image);
                dateTextView.setText("Date: " + date.substring(0, 10));
                latitudeTextView.setText("Latitude: " + latitudeEditText.getText().toString());
                longitudeTextView.setText("Longitude: " + longitudeEditText.getText().toString());
                progressBar.setVisibility(View.INVISIBLE);
                favNameEditText.setVisibility(View.VISIBLE);
                addToFavoritesBtn.setVisibility(View.VISIBLE);
            }
            //A lot of coordinates do not contain images, so we show error toast message to user
            else {
                Toast errorToast = Toast.makeText(Nasa_Earthy_Image_Db.this, getResources().getString(R.string.error_toast), Toast.LENGTH_SHORT);
                errorToast.show();
                progressBar.setVisibility(View.INVISIBLE);
            }
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
