/**Course Name: CST2335
 * Class Name: Nasa_Earthy_Image_Db
 * Date: 03/16/2020
 *
 * Note: Classes are named NASA, but had to switch to Bing api midproject, so the class names still reflect NASA
 *
 * Activity class for the view activity_nasa_earthy_image_db.xml. This activity will take user input for
 * latitude and longitude and output an image with details using Bing's Virtual Earth API. The user can
 * then choose to save the image into a favorites list, which will store the query details in the SQLite
 * database and the user can go to the Favorites_List Activity class to view the favorites list.
 *
 * @author Karl Rezansoff 040955782
 * @version 1.0
 */
package com.example.cst2335final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

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

public class Nasa_Earthy_Image_Db extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    /**
     * Represents the "search" button in activity_nasa_earthy_image_db.xml.
     */
    private Button searchBtn;
    /**
     * Represents the "Go to favorites" button in activity_nasa_earthy_image_db.xml
     */
    private Button favoritesBtn;
    /**
     * Represents the latitude input box in activity_nasa_earthy_image_db.xml.
     */
    private EditText latitudeEditText;
    /**
     * Represents the longitude input box in activity_nasa_earthy_image_db.xml.
     */
    private EditText longitudeEditText;
    /**
     * String key for latitude for passing data in the bundle
     */
    public static final String LATITUDE = "LATITUDE";
    /**
     * String key for longitude used for passing data in the bundle
     */
    public static final String LONGITUDE = "LONGITUDE";
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

        //NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        TextView header = navigationView.getHeaderView(0).findViewById(R.id.header_info);
        header.setText("Bing Virtual Earth Imagery\nAuthor: Karl Rezansoff\nVersion 1.0");
        navigationView.setNavigationItemSelectedListener(this);

        //Widgets
        searchBtn = findViewById(R.id.search_btn);
        favoritesBtn = findViewById(R.id.view_saved_img_btn);
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

            //saving input for shared preferences
            saveSharedPrefs(latitudeEditText.getText().toString(), longitudeEditText.getText().toString());

            //Creating bundle to pass data
            Bundle dataToPass = new Bundle();
            dataToPass.putString(LONGITUDE, longitudeEditText.getText().toString());
            dataToPass.putString(LATITUDE, latitudeEditText.getText().toString());

            //load new activity
                Intent nextActivity = new Intent(this, BingPicture.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
        });

        //Go to favorites
        Intent goToFavorites = new Intent(this, Favorites_List.class);
        favoritesBtn.setOnClickListener( click -> startActivity(goToFavorites));

    } //end of onCreate method

    /**
     * Method to check if an EditText is empty
     * @param etText EditText variable from XML
     * @return boolean value to represent if the EditText is empty
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
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handles when menu bar items are selected
     * @param item - the selected item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message2 = null;
        switch(item.getItemId())
        {
            case R.id.home:
                Intent goHome = new Intent(Nasa_Earthy_Image_Db.this, MainActivity.class);
                startActivity(goHome);
                break;
            case R.id.bbc:
                Intent gotoBbc = new Intent(Nasa_Earthy_Image_Db.this, NewsReaderSearch.class);
                startActivity(gotoBbc);
                break;
            case R.id.guardian:
                message2 = getText(R.string.error_not_implemented).toString();
                break;
            case R.id.earth:
                Intent gotoEarth = new Intent(Nasa_Earthy_Image_Db.this, Nasa_Earthy_Image_Db.class);
                startActivity(gotoEarth);
                break;
            case R.id.space:
                Intent gotoSpace = new Intent(Nasa_Earthy_Image_Db.this, NASADailyFavourites.class);
                startActivity(gotoSpace);
                break;
            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.menu_title)
                        //Message
                        .setMessage(getResources().getString(R.string.help_menu_item))
                        //what the Yes button does:
                        .setPositiveButton(getResources().getString(R.string.ok), (click, arg) -> { })
                        //Show the dialog
                        .create().show();
                break;
        }

        if (message2 != null)
            Toast.makeText(this, message2, Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * Handles navigation menu when an item is selected
     * @param item - the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String message2 = null;
        switch(item.getItemId())
        {
            case R.id.bbc:
                Intent gotoBbc = new Intent(Nasa_Earthy_Image_Db.this, NewsReaderSearch.class);
                startActivity(gotoBbc);
                break;
            case R.id.guardian:
                message2 = getText(R.string.error_not_implemented).toString();
                break;
            case R.id.earth:
                Intent gotoEarth = new Intent(Nasa_Earthy_Image_Db.this, Nasa_Earthy_Image_Db.class);
                startActivity(gotoEarth);
                break;
            case R.id.space:
                Intent gotoSpace = new Intent(Nasa_Earthy_Image_Db.this, NASADailyFavourites.class);
                startActivity(gotoSpace);
                break;
        }

        if (message2 != null)
            Toast.makeText(this, message2, Toast.LENGTH_LONG).show();
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
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
    }
