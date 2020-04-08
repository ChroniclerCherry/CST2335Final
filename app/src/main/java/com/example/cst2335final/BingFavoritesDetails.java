/**Course Name: CST2335
 * Class Name: BingFavoritesDetails
 * Date: 03/26/2020
 *
 * Class for displaying details of a favorited image after clicking on an item from the listview.
 *
 * @author Karl Rezansoff 040955782
 * @version 1.0
 */

package com.example.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BingFavoritesDetails extends AppCompatActivity {
    /**
     * Method will initialize all variables from the ListView, and use values from the Bundle to set new values for the widgets.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_favorites_details);

        //Getting widgets from activity_bing_favorites_details
        ImageView imageView = findViewById(R.id.favDetails_image);
        TextView latitudeTextView = findViewById(R.id.favDetails_latitude);
        TextView longitudeTextView = findViewById(R.id.favDetails_longitude);
        TextView urlTextView = findViewById(R.id.favDetails_imageurl);
        TextView filePathTextView = findViewById(R.id.favDetails_filePath);

        //Getting data from bundle
        Bundle dataFromActivity = getIntent().getExtras();
        String latitude = dataFromActivity.getString(BingFavoritesList.LATITUDE);
        String longitude = dataFromActivity.getString(BingFavoritesList.LONGITUDE);
        String url = dataFromActivity.getString(BingFavoritesList.URL_PATH);
        String filePath = dataFromActivity.getString(BingFavoritesList.FILE_PATH);

        //Changing widget values
        latitudeTextView.setText(getResources().getString(R.string.latitude) + " " + latitude);
        longitudeTextView.setText(getResources().getString(R.string.longitude) + " " + longitude);
        urlTextView.setText(getResources().getString(R.string.view_online) + " " + dataFromActivity.getString(BingFavoritesList.URL_PATH));
        filePathTextView.setText(getResources().getString(R.string.saved_on_device) + " " + filePath);

        //getting image from local storage
        FileInputStream fis = null;
        try {
            fis = openFileInput(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap image = BitmapFactory.decodeStream(fis);
        imageView.setImageBitmap(image);
    }
}
