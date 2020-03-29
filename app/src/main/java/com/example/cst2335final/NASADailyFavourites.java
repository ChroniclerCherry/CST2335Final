package com.example.cst2335final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Calendar;

public class NASADailyFavourites extends AppCompatActivity {

    private MyListAdapter adapter;
    private FrameLayout chatFragment;
    private boolean isTablet;
    private ArrayList<NASAImage> imagesList = new ArrayList<NASAImage>();
    SQLiteDatabase db;
    private NASADailyImageFragment dFragment;

    EditText dateEntry;
    Button searchButton;

    public static final String IMAGE_POSITION = "POSITION";
    public static final String IMAGE_ID = "ID";
    public static final String IMAGE_DATE = "DATE";
    public static final String IMAGE_DESCRIPTION = "DESCRIPTION";
    public static final String IMAGE_TITLE = "TITLE";
    public static final String IMAGE_BITMAP = "BITMAP";

    final static String URL_START = "https://api.nasa.gov/planetary/apod?api_key=";
    final static String DEFAULT_API = "DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d";
    //my API: HafOzdnVZh0xY9W9V4aec8HTJ1byVeKphccKqgRg
    private String userApi;
    final static String URL_END = "&date=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_daily_favourites);

        Intent fromMain = getIntent();
        userApi = fromMain.getStringExtra("API");

        loadDataFromDatabase();
        setupListView();

        dateEntry = findViewById(R.id.NasaDailyImageDateEditText);

        searchButton = findViewById(R.id.NasaDailyImageSearchButton);
        searchButton.setOnClickListener(click -> {
            Intent gotoLoading = new Intent(NASADailyFavourites.this, NASADailyLoading.class);
            String api = userApi;
            if (userApi == null || userApi.isEmpty())
                api = DEFAULT_API;

            gotoLoading.putExtra("URL",URL_START+api+URL_END+dateEntry.getText().toString());
            startActivityForResult(gotoLoading,0);
        });
    }

    private void setupListView(){
        ListView messagesListView = findViewById(R.id.NASADaily_listView);
        messagesListView.setAdapter(adapter = new MyListAdapter());

        chatFragment = findViewById(R.id.NasaDailyImageDataFragment);
        isTablet = chatFragment != null;

        messagesListView.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putInt(IMAGE_POSITION, position);
            dataToPass.putLong(IMAGE_ID, id);
            dataToPass.putString(IMAGE_DATE, imagesList.get(position).getDate());
            dataToPass.putString(IMAGE_TITLE, imagesList.get(position).getTitle());
            dataToPass.putString(IMAGE_DESCRIPTION, imagesList.get(position).getDescription());
            dataToPass.putParcelable(IMAGE_BITMAP,imagesList.get(position).getImage());

            if(isTablet)
            {
                dFragment = new NASADailyImageFragment(); //add a DetailFragment
                dFragment.setTablet(isTablet);
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.NasaDailyImageDataFragment, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(NASADailyFavourites.this, EmptyActivity.class);
                nextActivity.putExtra("data",dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });
    }

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

                FileInputStream fis = null;
                try {
                    fis = openFileInput(date + ".png");
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }

                Bitmap img = BitmapFactory.decodeStream(fis);

            //add the new Contact to the array list:
            imagesList.add(new NASAImage(id,title,des,date,img));
        }

        //At this point, the messagesList array has loaded every row from the cursor.
    }

    public void loadImageFromNasa(View view) {
        Intent goToLoad = new Intent(NASADailyFavourites.this, NASADailyLoading.class);
        goToLoad.putExtra("Date",dateEntry.getText().toString());
        startActivity(goToLoad);
    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imagesList.size();
        }

        @Override
        public NASAImage getItem(int position) {
            return imagesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View imageView;
            NASAImage img = getItem(position);
            imageView = inflater.inflate(R.layout.nasa_daily_list,parent,false);

            TextView title = findViewById(R.id.NASADaily_title);
            title.setText(img.getTitle());

            TextView dateView = findViewById(R.id.NASADaily_imageDate);
            dateView.setText(img.getDate());

            ImageView imgView = findViewById(R.id.NASADaily_image);
            imgView.setImageBitmap(img.getImage());

            return imageView;
        }
    }

    /* Date Picker code */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment(dateEntry);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

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

        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateEntry.setText(String.format("%d-%d-%d",year,month+1,day));
        }
    }
    /* End of Date Picker*/

}
