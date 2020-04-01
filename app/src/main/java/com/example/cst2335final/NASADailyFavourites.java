package com.example.cst2335final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Displays a list of favourited NASA images and contains navigation to search and add new ones
 * or view the details of favourites images
 */
public class NASADailyFavourites extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static int REQUEST_CODE = 444;
    public static int DATABASE_CHANGED = 555;
    public static int INVALID_URL_ERROR = 666;
    public static final int REMOVE_IMAGE = 777;

    private NasaDailyFavouritesAdapter adapter;
    private FrameLayout chatFragment;
    private NASADailyImageFragment dFragment;

    private boolean isTablet;
    private ArrayList<NASAImage> imagesList = new ArrayList<NASAImage>();
    SQLiteDatabase db;


    EditText dateEntry;
    Button searchButton;

    public static final String IMAGE_DATE = "DATE";
    public static final String IMAGE_DESCRIPTION = "DESCRIPTION";
    public static final String IMAGE_TITLE = "TITLE";

    final static String URL_START = "https://api.nasa.gov/planetary/apod?api_key=";
    final static String DEFAULT_API = "DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d";
    //my API: HafOzdnVZh0xY9W9V4aec8HTJ1byVeKphccKqgRg
    private String api;
    final static String URL_END = "&date=";

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    EditText apiEntry;
    Toolbar tbar;

    /**
     * Sets up the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_daily_favourites);

        apiEntry = findViewById(R.id.NASADaily_ApiEditText);
        prefs = getSharedPreferences("NASADailyApi", Context.MODE_PRIVATE);
        api = prefs.getString("API", "");

        if (api != null || !api.isEmpty())
            apiEntry.setText(api);

        loadDataFromDatabase(); //fill the list with existing images from the database
        setupListView(); //setup the ListView

        dateEntry = findViewById(R.id.NasaDailyImageDateEditText);
        searchButton = findViewById(R.id.NasaDailyImageSearchButton);
        searchButton.setOnClickListener(click -> {
            api = apiEntry.getText().toString();
            if (api == null || api.isEmpty())
                api = DEFAULT_API;

            //create the url and pass it onto NASADailyLoading to attempt to load image data from the web
            Intent gotoLoading = new Intent(NASADailyFavourites.this, NASADailyLoading.class);
            gotoLoading.putExtra("URL",URL_START+api+URL_END+dateEntry.getText().toString());
            startActivityForResult(gotoLoading, REQUEST_CODE);
        });

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
        header.setText(R.string.yimi_info);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        edit = prefs.edit();
        edit.putString("API",apiEntry.getText().toString());
        edit.commit();
    }

    public void displayApiHelp(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.NasaDaily_apiInformation)
                .setPositiveButton(R.string.NASADaily_okay, (dialog, which) -> {
                })
                .setNegativeButton(R.string.NASADaily_goToSite, (dialog, which) -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.nasa.gov/"));
                    startActivity(browserIntent);
                });
        AlertDialog alert = builder.create();
        alert.show();

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
                Intent goHome = new Intent(NASADailyFavourites.this, MainActivity.class);
                startActivity(goHome);
            case R.id.bbc:
                Intent gotoBbc = new Intent(NASADailyFavourites.this, NewsReaderSearch.class);
                startActivity(gotoBbc);
                break;
            case R.id.guardian:
                message2 = getText(R.string.error_not_implemented).toString();
                break;
            case R.id.earth:
                Intent gotoEarth = new Intent(NASADailyFavourites.this, Nasa_Earthy_Image_Db.class);
                startActivity(gotoEarth);
                break;
            case R.id.space:
                Intent gotoSpace = new Intent(NASADailyFavourites.this, NASADailyFavourites.class);
                startActivity(gotoSpace);
                break;
            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.NASADaily_title)
                        //Message
                        .setMessage(getResources().getString(R.string.NASADaily_help))
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
                Intent gotoBbc = new Intent(NASADailyFavourites.this, NewsReaderSearch.class);
                startActivity(gotoBbc);
                break;
            case R.id.guardian:
                message2 = getText(R.string.error_not_implemented).toString();
                break;
            case R.id.earth:
                Intent gotoEarth = new Intent(NASADailyFavourites.this, Nasa_Earthy_Image_Db.class);
                startActivity(gotoEarth);
                break;
            case R.id.space:
                Intent gotoSpace = new Intent(NASADailyFavourites.this, NASADailyFavourites.class);
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
     * sets up the list view
     */
    private void setupListView(){
        ListView messagesListView = findViewById(R.id.NASADaily_listView);
        messagesListView.setAdapter(adapter = new NasaDailyFavouritesAdapter());

        chatFragment = findViewById(R.id.NasaDailyImageDataFragment);
        isTablet = chatFragment != null;

        //if an item is clicked, show a snackbar to make option to remove that item
        messagesListView.setOnItemLongClickListener((list, item, position, id) -> {
            Snackbar snackbar = Snackbar
                    .make(item, getText(R.string.NASADaily_confirmDelete)
                            + imagesList.get(position).getTitle(), Snackbar.LENGTH_LONG)
                    .setAction(R.string.NASADaily_Confirm,v->{
                                db.delete(NASADailyOpener.TABLE_NAME, NASADailyOpener.COL_ID + "= ?",
                                        new String[] {Long.toString(id)});
                                imagesList.remove(position);
                                adapter.notifyDataSetChanged();
                    }
                    );
            snackbar.show();
            return false;
        });

        messagesListView.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            NASAImage imgObj = imagesList.get(position);
            dataToPass.putString(IMAGE_TITLE,imgObj.getTitle());
            dataToPass.putString(IMAGE_DESCRIPTION,imgObj.getDescription());
            dataToPass.putString(IMAGE_DATE,imgObj.getDate());

            if(isTablet)
            {
                dFragment = new NASADailyImageFragment();
                dFragment.setTablet(isTablet);
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.NasaDailyImageDataFragment, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(NASADailyFavourites.this, NASADailyEmptyActivity.class);
                nextActivity.putExtra("data",dataToPass); //send data to next activity
                startActivityForResult(nextActivity,REQUEST_CODE); //make the transition
            }

        });

    }
    /**
     * When the loading or details page finishes, we will know if they changed the database.
     * If so, we refresh the entire listview
     *
     * If an error occurred, an error message is displayed with a toast
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == DATABASE_CHANGED) {
            //reload everything from database and update listview
            imagesList.clear();
            loadDataFromDatabase();
            adapter.notifyDataSetChanged();
        }

        if (requestCode == REQUEST_CODE && resultCode == INVALID_URL_ERROR) {
            Toast.makeText(getApplicationContext(), R.string.NASADaily_invalidUrlError, Toast.LENGTH_LONG).show();
        }

        if (requestCode == REQUEST_CODE && resultCode == REMOVE_IMAGE) {
            String d = data.getStringExtra(IMAGE_DATE);
            NASAImage img = new NASAImage(d);
            removeImage(img);
        }
    }

    /**
     * loads data from the database to populate the imagesList
     */
    private void loadDataFromDatabase()
    {

        //get a database connection:
        NASADailyOpener dbOpener = new NASADailyOpener(this);
        db = dbOpener.getWritableDatabase();

        // We want to get all of the columns. Look at NASADailyOpener.java for the definitions:
        String [] columns = {NASADailyOpener.COL_ID, NASADailyOpener.COL_TITLE, NASADailyOpener.COL_DESCRIPTION, NASADailyOpener.COL_DATE};
        //query all the results from the database:
        Cursor results = db.query(false, NASADailyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //print everything in the cursor
        dbOpener.printCursor(results);
        //Now the results object has rows of results that match the query.
        //find the column indices:
        int titleColIndex = results.getColumnIndex(NASADailyOpener.COL_TITLE);
        int descColIndex = results.getColumnIndex(NASADailyOpener.COL_DESCRIPTION);
        int dateColIndex = results.getColumnIndex(NASADailyOpener.COL_DATE);
        int idColIndex = results.getColumnIndex(NASADailyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String title = results.getString(titleColIndex);
            String des = results.getString(descColIndex);
            String date = results.getString(dateColIndex);
            long id = results.getLong(idColIndex);
            Bitmap img;
                FileInputStream fis = null;
                try {
                    fis = openFileInput("NASADaily" + date + ".png");
                    img = BitmapFactory.decodeStream(fis);
                } catch (FileNotFoundException e){
                    //image might be null as some dates don't provide an image
                    img = null;
                }

            //add the new Contact to the array list:
            imagesList.add(new NASAImage(id,title,des,date,img));
        }
        Collections.sort(imagesList);
    }

    public void loadImageFromNasa(View view) {
        Intent goToLoad = new Intent(NASADailyFavourites.this, NASADailyLoading.class);
        goToLoad.putExtra("Date",dateEntry.getText().toString());
        startActivity(goToLoad);
    }

    public void removeImage(NASAImage img) {
        db.delete(NASADailyOpener.TABLE_NAME, NASADailyOpener.COL_DATE + "= ?",
                new String[] {img.getDate()});

        imagesList.remove(Collections.binarySearch(imagesList,img));
        adapter.notifyDataSetChanged();
    }

    /**
     * A base adapter for imagesList to be used by the listView
     */
    class NasaDailyFavouritesAdapter extends BaseAdapter {

        /**
         * @return number of Images in database
         */
        @Override
        public int getCount() {
            return imagesList.size();
        }

        /**
         * Returns the NASAImage at the given position
         * @param position - the position of the item in the listview
         * @return - the NASAImage object at that position
         */
        @Override
        public NASAImage getItem(int position) {
            return imagesList.get(position);
        }

        /**
         * returns the database ID of the item at this position
         * @param position - the position of the item in the listview
         * @return - the id of the item at position
         */
        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        /**
         * returns the view for the item at the given position
         * @param position - the position of the item in the listview
         * @param convertView -
         * @param parent -
         * @return the populated view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //inflate the view to the list layout
            LayoutInflater inflater = getLayoutInflater();
            View imageView;
            imageView = inflater.inflate(R.layout.nasa_daily_list,parent,false);

            //get the item
            NASAImage img = getItem(position);

            //set all relevant fields from item
            TextView title = imageView.findViewById(R.id.NASADaily_title);
            title.setText(img.getTitle());
            TextView dateView = imageView.findViewById(R.id.NASADaily_imageDate);
            dateView.setText(img.getDate());
            ImageView imgView = imageView.findViewById(R.id.NASADaily_image);
            imgView.setImageBitmap(img.getImage());

            return imageView;
        }
    }

    /**
     * OnClick for the pick date button
     * @param view
     */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment(dateEntry);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Fragment that allosws user to pick a date and have it set to the editText
     */
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        EditText dateEntry;
        DatePickerFragment(EditText dateEntry){
            //pass in the instance of the editext we want to change the date on
            this.dateEntry = dateEntry;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        /**
         * puts the selected date into the text box in yyy-mm-dd format
         * @param view
         * @param year - the selected year
         * @param month - the selected month
         * @param day - the selected day
         */
        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateEntry.setText(String.format("%d-%d-%d",year,month+1,day));
        }
    }
    /* End of Date Picker*/

}
