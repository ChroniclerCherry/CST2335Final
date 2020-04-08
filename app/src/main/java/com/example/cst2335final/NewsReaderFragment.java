package com.example.cst2335final;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Fragment that displays the article details
 * @Author Lia Brophy
 * @Version 1.0
 * @Date 2020-04-01
 */
public class NewsReaderFragment extends Fragment {

    private Bundle dataFromActivity;
    private SQLiteDatabase db;
    private String title;
    private String desc;
    private String date;
    private String link;
    private String note;
    private ContentValues newRow = new ContentValues();
    private AppCompatActivity parentActivity;

    /**
     * Sets up and displays article details and allows user to save to favourites
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.bing_fragment_details, container, false);
        dataFromActivity = getArguments();
        title = dataFromActivity.getString("Title");
        desc = dataFromActivity.getString("Desc");
        date = dataFromActivity.getString("Date");
        link = dataFromActivity.getString("Link");

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.news_reader_fragment, container, false);
        //get date
        TextView dateView = result.findViewById(R.id.textViewDate);
        //save date
        String date = dataFromActivity.getString(NewsReaderSearch.DATE);
        //show date
        dateView.setText(date);
        //get title
        TextView titleView = result.findViewById(R.id.textViewTitle);
        //save title
        String title = dataFromActivity.getString(NewsReaderSearch.TITLE);
        //show title
        titleView.setText(title);
        //get description
        TextView descView = result.findViewById(R.id.textViewDesc);
        //save desc
        String desc = dataFromActivity.getString(NewsReaderSearch.DESC);
        //show desc
        descView.setText(desc);
        //get link
        TextView linkView = result.findViewById(R.id.textViewLink);
        //save link
        String link = dataFromActivity.getString(NewsReaderSearch.LINK);
        linkView.setText(link);

        EditText noteBox = result.findViewById(R.id.noteText);
        noteBox.setText(note);

        // get the fave button, and add a click listener:
        Button faveBtn = result.findViewById(R.id.buttonFave);
        faveBtn.setOnClickListener(clk -> {

            NewsReaderOpener dbOpener = new NewsReaderOpener(this.parentActivity);
            db = dbOpener.getWritableDatabase();
            //create newsItem
            NewsReaderItem fave = new NewsReaderItem(title, date, desc, link, note);
            newRow = new ContentValues();
            //add to db
            newRow.put(NewsReaderOpener.TITLE, title);
            newRow.put(NewsReaderOpener.DESC, desc);
            newRow.put(NewsReaderOpener.DATE, date);
            newRow.put(NewsReaderOpener.LINK, link);
            if (noteBox != null) {
                newRow.put(NewsReaderOpener.NOTE, String.valueOf(noteBox.getText()));
            }
            else {
                newRow.put(NewsReaderOpener.NOTE, "");
            }

            long newId = db.insert(NewsReaderOpener.TABLE_NAME, null, newRow);
            //Show the item was inserted
            Snackbar.make(faveBtn, R.string.add_to_favorites, Snackbar.LENGTH_SHORT).show();
        });

        // get the back button, and add a click listener to finish and go back to the list of titles
        Button backBtn = result.findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(clk -> {
            //Tell the parent activity to remove
            parentActivity.finish();
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    /**
     * called once the fragment is associated with its activity
     * @param context
     */
        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            parentActivity = (AppCompatActivity) context;
        }
    }

