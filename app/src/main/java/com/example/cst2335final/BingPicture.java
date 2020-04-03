/**Course Name: CST2335
 * Class Name: BingPicture
 * Date: 03/26/2020
 *
 * Class for performing query using Bing's Virtual Earth API, with the option to "favorite" an image to save to db.
 *
 * @author Karl Rezansoff 040955782
 * @version 1.0
 */

package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BingPicture extends AppCompatActivity {
    /**
     * SQLiteDatabase variable for accessing our database.
     */
    SQLiteDatabase db;
    /**
     * Bundle variable to store latitude and longitude passed from Earthy_Image_MyOpener activity in previous activity.
     */
    private Bundle dataFromActivity;
    /**
     * String to store value for latitude
     */
    private String latitude;
    /**
     * String to store value for longitude
     */
    private String longitude;
    /**
     * TextView to store value for latitude
     */
    private TextView latitudeTextView;
    /**
     * TextView to store value for longitude
     */
    private TextView longitudeTextView;
    /**
     * ImageView to store the image pulled from the api
     */
    private ImageView bingPicture;
    /**
     * Button variable to store favorites button from view
     */
    private Button favButton;
    /**
     *Progress bar variable for the progress bar in activity_nasa_earthy_image_db.xml.
     */
    private ProgressBar progressBar;
    /**
     * String value to store url of the image, consists of api link, latitude/longitude parameters, and my personal api key
     */
    private String imageURL;

    /**
     * Method will perform query to Bing's Virtual Earth api and save the image to the device. Will then initialize all variables from the view and set their values.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_picture);

        //make progress bar visible
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        //Getting data from bundle
        dataFromActivity = getIntent().getExtras();
        latitude = dataFromActivity.getString(Nasa_Earthy_Image_Db.LATITUDE);
        longitude = dataFromActivity.getString(Nasa_Earthy_Image_Db.LONGITUDE);

        //Getting widgets from activity_bing_picture.xml
        bingPicture = findViewById(R.id.bing_picture);
        favButton = findViewById(R.id.bingpicture_add_to_favorites);
        latitudeTextView = findViewById(R.id.bingpicture_latitude);
        longitudeTextView = findViewById(R.id.bingpicture_longitude);
        progressBar = findViewById(R.id.progress_bar);

        //calling imageQuery to get values from api
        String earthyImageUrl = "http://dev.virtualearth.net/REST/V1/Imagery/Map/Birdseye/" + latitude + "," + longitude + "/20?dir=180&ms=500,500&key=AiBIAQNVppJbc9JGabgjwN_qAWp59x9hhmYI5cDSxvf-IbKJkERsFka3_Gn9trNH";
        ImageQuery imageQuery = new ImageQuery();
        imageQuery.execute(earthyImageUrl);

        favButton.setOnClickListener( v -> {
            Earthy_Image_MyOpener dbOpener = new Earthy_Image_MyOpener(this);
            db = dbOpener.getWritableDatabase();
            //add a new row to the db
            ContentValues newRowValues = new ContentValues();
            //provide a value for the db columns
            newRowValues.put(Earthy_Image_MyOpener.FILE_PATH, latitude + longitude + ".png");
            newRowValues.put(Earthy_Image_MyOpener.LATITUDE, latitude);
            newRowValues.put(Earthy_Image_MyOpener.LONGITUDE, longitude);
            newRowValues.put(Earthy_Image_MyOpener.URL_PATH, imageURL);
            //insert into db
            db.insert(Earthy_Image_MyOpener.TABLE_NAME, null, newRowValues);

            Toast errorToast = Toast.makeText(BingPicture.this, getResources().getString(R.string.add_snackbar), Toast.LENGTH_SHORT);
            errorToast.show();
        });

    } //end of onCreate

    /**
     * Class extends from AsyncTask and is used for performing the image query with Google's Earthy Image API,
     * modifying the TextViews with query results, and will update a progress bar as data is retrieved.
     */
    private class ImageQuery extends AsyncTask<String, Integer, String> {
        /**
         * String for url used in performing the query.
         */
        private String imageUrl;
        /**
         * Bitmap for storing the image
         */
        private Bitmap image;
        /**
         * String to store error message while loading from api.
         */
        private String returnString = null;
        /**
         * String value used to name the image when we save to the device
         */
        private String fileName = latitude + longitude + ".png";

        /**
         * Query using Bing's Virtual Earth API done in this method, and will set values for latitude, longitude, imageUrl, filePath, and image.
         * @param args Passing a single string parameter being the API url with the latitude and longitude values from the user.
         * @return String error message
         */
        @Override
        protected String doInBackground(String... args) {
            String returnString = null;
            imageUrl = args[0];
            try {
                //If file exists we download from server
                if(!fileExistance(fileName)) {
                    image = null;
                    URL url = new URL(args[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                    publishProgress(25);
                    //And then save to local storage
                    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    publishProgress(50);
                }
                else { //else we download image from local storage
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    image = BitmapFactory.decodeStream(fis);
                }
                publishProgress(75);
            }
            catch (FileNotFoundException fnfe) {
                return returnString = "FileNotFoundException";
            } catch (MalformedURLException mfe) {
                return returnString = "MalFormed URL exception";
            } catch (IOException ioe) {
                return returnString = "IOException";
            } catch (Exception e) {
                return returnString = "Exception e error";
            }
            publishProgress(100);
            return returnString;
        }

        /**
         * Method to check if a file exists in local storage
         * @param fname String path of file
         * @return boolean value to indicate if file exists
         */
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        /**
         * Method will update the xml widgets with the values retrieved from doInBackground method (results from API query).
         * @param sentFromDoInBackground String values from sentFromBackground method.
         */
        @Override
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);

            //if no errors set new values for the view
            if(sentFromDoInBackground == null) {
                bingPicture.setImageBitmap(image);
                latitudeTextView.setText("Latitude: " + latitude);
                longitudeTextView.setText("Longitude" + longitude);
                imageURL = imageUrl; //using this variable for the bundle
                progressBar.setVisibility(View.INVISIBLE);
            }
            //A lot of coordinates do not contain images, so we show error toast message to user
            else {
                Toast errorToast = Toast.makeText(BingPicture.this, getResources().getString(R.string.error_toast) + sentFromDoInBackground, Toast.LENGTH_SHORT);
                errorToast.show();
                progressBar.setVisibility(View.INVISIBLE);
                finish(); //return to previous activity
            }
        }

        /**
         * Method only is used for updating the progress bar widget and setting it's visibility.
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
