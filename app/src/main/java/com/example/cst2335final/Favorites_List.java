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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

    /**
     * Method will initialize widget variables from xml and call loadDataFromDatabase() to populate the ListView with information from the saved queries.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites__list);

        //Adapter for favorites ListView
        ListView myList = findViewById(R.id.fav_images_listview);
        myList.setAdapter(myAdapter = new MyListAdapter());

        //load favorites from db
        loadDataFromDatabase();

        //Action when longclicking a list object
        myList.setOnItemLongClickListener( (p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this")

                    //Message
                    .setMessage("Just click Yes to delete")

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteFavorite(elements.get(pos));
                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> { })

                    //Show the dialog
                    .create().show();
            return true;
        });

    } //end of onCreate

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
            TextView listViewLatitude = newView.findViewById(R.id.listView_latitude);
            listViewLatitude.setText(elements.get(position).getLatitude());

            TextView listViewLongitude = newView.findViewById(R.id.listview_longitude);
            listViewLongitude.setText(elements.get(position).getLongitude());

            TextView listViewDate = newView.findViewById(R.id.listView_date);
            listViewDate.setText(elements.get(position).getDate());

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
        String[] columns = {Earthy_Image_MyOpener.COL_ID, Earthy_Image_MyOpener.DATE, Earthy_Image_MyOpener.LATITUDE, Earthy_Image_MyOpener.LONGITUDE};
        //Query for all results
        Cursor results = db.query(false,Earthy_Image_MyOpener.TABLE_NAME,columns,null,null,null,null,null,null);

        //Getting column indices
        int dateIndex = results.getColumnIndex(Earthy_Image_MyOpener.DATE);
        int latitudeIndex = results.getColumnIndex(Earthy_Image_MyOpener.LATITUDE);
        int longitudeIndex = results.getColumnIndex(Earthy_Image_MyOpener.LONGITUDE);
        int idColIndex = results.getColumnIndex(Earthy_Image_MyOpener.COL_ID);

        //Iterate over results, return true if next item
        while (results.moveToNext()) {
            String date = results.getString(dateIndex);
            String latitude = results.getString(latitudeIndex);
            String longitude = results.getString(longitudeIndex);
            long id = results.getLong(idColIndex);

            //add to elements ArrayList
            elements.add(new EarthyImage(date, latitude, longitude, id));
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
         * String value for the date the image was taken.
         */
        private String date;
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
         * Single class constructor
         * @param date String value for date
         * @param latitude String value for latitude
         * @param longitude String value for longitude
         * @param Id long value for database Id
         */
        public EarthyImage(String date, String latitude, String longitude, long Id) {
            this.date = date;
            this.latitude = latitude;
            this.longitude = longitude;
            this.Id = Id;
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
         * Returns date value for when the image was taken.
         * @return String date
         */
        public String getDate() { return date; }

        /**
         * Returns database id.
         * @return long id.
         */
        public long getId() { return Id; }
    }
}
