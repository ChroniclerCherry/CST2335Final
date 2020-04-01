/**Course Name: CST2335
 * Class Name: Favorites_List
 * Date: 03/16/2020
 *
 * Activity class for the view activity_favorites_list.xml. This activity will show all the queries "favorited"
 * or saved by the user and pulled from the SQLite database.
 *
 * @author Karl Rezansoff 040955782
 * @version 1.0
 */
package com.example.cst2335final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Favorites_List extends AppCompatActivity {
    /**
     * Adapter used for updating the ListView
     */
    private MyListAdapter myAdapter;
    /**
     * ArrayList to store EarthyImage class objects.
     */
    private ArrayList<EarthyImage> elements = new ArrayList<>();
    /**
     * SQLiteDatabase variable for accessing our database.
     */
    SQLiteDatabase db;
    Toolbar tbar;
    public static final String DATE = "DATE";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String ID = "ID";
    public static final String URL_PATH = "URL_PATH";

    /**
     * Method will initialize widget variables from xml and call loadDataFromDatabase() to populate the ListView with information from the saved queries.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites__list);

        //Show the toolbar
        tbar = findViewById(R.id.toolbar);
        setSupportActionBar(tbar);

        //Adapter for favorites ListView
        ListView myList = findViewById(R.id.fav_images_listview);
        myList.setAdapter(myAdapter = new MyListAdapter());

        //load favorites from db
        loadDataFromDatabase();

        //Action when longclicking a list object
        myList.setOnItemLongClickListener( (p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title))

                    //Message
                    .setMessage(getResources().getString(R.string.alert_message))

                    //what the Yes button does:
                    .setPositiveButton(getResources().getString(R.string.yes), (click, arg) -> {
                        deleteFavorite(elements.get(pos));
                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton(getResources().getString(R.string.no), (click, arg) -> { })

                    //Show the dialog
                    .create().show();
            return true;
        });

        //Action when clicking a list object
        myList.setOnItemClickListener((list, item, position, id) -> {
            //Creating bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(LATITUDE, elements.get(position).getLatitude());
            dataToPass.putString(LONGITUDE, elements.get(position).getLongitude());
            dataToPass.putString(ID, Long.toString(elements.get(position).getId()));
            dataToPass.putString(URL_PATH, elements.get(position).getUrlPath());

            DetailsFragment dFragment = new DetailsFragment(); //Creating the fragment
            dFragment.setArguments(dataToPass); //Passing the bundle of information
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                    .commit(); //Load the fragment
        });

    } //end of onCreate

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
     * Method for handling what happens when an option is selected from menu.
     * @param item MenuItem object that was clicked.
     * @return boolean value to represent action is completed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId()) {
            case R.id.help:
                message = getResources().getString(R.string.help_menu_favs);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.menu_title_favs))
                //Message
                .setMessage(message)
                //what the Yes button does:
                .setPositiveButton(getResources().getString(R.string.ok), (click, arg) -> { })
                //Show the dialog
                .create().show();

        return true;
    }

    /**
     * Class extends BaseAdapter and provides methods for retrieving count, item, id, or the view for the ListView.
     */
    private class MyListAdapter extends BaseAdapter {
        /**
         * Returns how many elements are contained in the elements ArrayList.
         * @return int size of elements ArrayList
         */
        public int getCount() { return elements.size(); }

        /**
         * Returns EarthyImage from elements ArrayList.
         * @param position int index value
         * @return EarthyImage object
         */
        public EarthyImage getItem(int position) { return elements.get(position); }

        /**
         * Returns database id
         * @param position int index value
         * @return long database id value
         */
        public long getItemId(int position) { return elements.get(position).getId(); }

        /**
         * Method to create a new row in the ListView and set the appropriate value.
         * @param position int index value for retrieving EarthyImage from elements ArrayList
         * @param old View object, will take old view and add a new row
         * @param parent ViewGroup parent for the view
         * @return View with new ListView values
         */
        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();

            //make a new row
            newView = inflater.inflate(R.layout.earthy_image_listview, parent, false);

            //set values for the row
            TextView listViewFavName = newView.findViewById(R.id.listView_favName);
            listViewFavName.setText(elements.get(position).getName());

            //getting image from local storage
            FileInputStream fis = null;
            try {
                fis = openFileInput(elements.get(position).getLatitude() + elements.get(position).getLongitude() + ".png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap image = BitmapFactory.decodeStream(fis);

            ImageView listViewImage = newView.findViewById(R.id.listView_image);
            listViewImage.setImageBitmap(image);

            return newView;
        }
    }

    /**
     * Method to retrieve all rows from the SQLite database. Each row represents an EarthyImage object and all will be added to elements ArrayList.
     */
    private void loadDataFromDatabase() {
        //Get db connection
        Earthy_Image_MyOpener dbOpener = new Earthy_Image_MyOpener(this);
        db = dbOpener.getWritableDatabase();

        //Array to store column names
        String[] columns = {Earthy_Image_MyOpener.COL_ID, Earthy_Image_MyOpener.NAME, Earthy_Image_MyOpener.DATE, Earthy_Image_MyOpener.LATITUDE, Earthy_Image_MyOpener.LONGITUDE, Earthy_Image_MyOpener.URL_PATH};
        //Query for all results
        Cursor results = db.query(false,Earthy_Image_MyOpener.TABLE_NAME,columns,null,null,null,null,null,null);

        //Getting column indices
        int nameIndex = results.getColumnIndex(Earthy_Image_MyOpener.NAME);
        int latitudeIndex = results.getColumnIndex(Earthy_Image_MyOpener.LATITUDE);
        int longitudeIndex = results.getColumnIndex(Earthy_Image_MyOpener.LONGITUDE);
        int idColIndex = results.getColumnIndex(Earthy_Image_MyOpener.COL_ID);
        int urlPathIndex = results.getColumnIndex(Earthy_Image_MyOpener.URL_PATH);

        //Iterate over results, return true if next item
        while (results.moveToNext()) {
            String name = results.getString(nameIndex);
            String latitude = results.getString(latitudeIndex);
            String longitude = results.getString(longitudeIndex);
            long id = results.getLong(idColIndex);
            String urlPath = results.getString(urlPathIndex);

            //add to elements ArrayList
            elements.add(new EarthyImage(name, latitude, longitude, id, urlPath));
        }
    }

    /**
     * Method to delete an entry from the database, done with raw query.
     * @param earthyImage object, will use database id attribute in the query.
     */
    private void deleteFavorite(EarthyImage earthyImage) {
        db.delete(Earthy_Image_MyOpener.TABLE_NAME, Earthy_Image_MyOpener.COL_ID + "= ?", new String[] {Long.toString(earthyImage.getId())});
    }

    /**
     * Class to represent a saved or "favorited" query and contains relevant data on the query.
     */
    private class EarthyImage {
        /**
         * String name given by the user
         */
        private String name;
        /**
         * String value for latitude of the image.
         */
        private String latitude;
        /**
         * String value for the longitude of the image.
         */
        private String longitude;
        /**
         * Long value for database id.
         */
        private long Id;
        /**
         * String value for image url
         */
        private String urlPath;

        /**
         * Single class constructor
         * @param latitude String value for latitude
         * @param longitude String value for longitude
         * @param Id long value for database Id
         */
        public EarthyImage(String name, String latitude, String longitude, long Id, String urlPath) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.Id = Id;
            this.urlPath = urlPath;
        }

        /**
         * Returns latitude value for the image.
         * @return String value for latitude
         */
        public String getLatitude() { return latitude; }

        /**
         * Returns longitude value for the image.
         * @return String value for longitude
         */
        public String getLongitude() { return longitude; }

        /**
         * Returns database id.
         * @return long id.
         */
        public long getId() { return Id; }

        /**
         * Returns name of image
         * @return String name
         */
        public String getName() { return name; }

        /**
         * Returns url path of image
         * @return String url path
         */
        public String getUrlPath() { return urlPath; }
    }
}
