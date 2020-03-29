package com.example.cst2335final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

    public static final String IMAGE_POSITION = "POSITION";
    public static final String IMAGE_ID = "ID";
    public static final String IMAGE_DATE = "DATE";
    public static final String IMAGE_DESCRIPTION = "DESCRIPTION";
    public static final String IMAGE_TITLE = "TITLE";
    public static final String IMAGE_BITMAP = "BITMAP";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_daily_favourites);

        dateEntry = findViewById(R.id.NasaDailyImageDateEditText);

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

        // https://api.nasa.gov/planetary/apod?api_key=HafOzdnVZh0xY9W9V4aec8HTJ1byVeKphccKqgRg&date=2020-02-01
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
