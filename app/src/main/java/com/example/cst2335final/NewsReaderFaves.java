package com.example.cst2335final;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NewsReaderFaves extends AppCompatActivity {

    private ArrayList<Favourite> faveList = new ArrayList<Favourite>();
    //private FaveListAdapter faveListAdapter;
    private Button delete;
    private Button back;
    SQLiteDatabase db;
    Cursor results;
    private FaveListAdapter faveListAdapter;
    //Favourite fave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader_fave_view);
        //ref to listview in xml
        ListView favesList = findViewById(R.id.newsList);
        //set adapter on listview to populate with objects
        //favesList.setAdapter(faveListAdapter);

        favesList.setAdapter(faveListAdapter = new FaveListAdapter());

        loadDataFromDatabase(); //get articles from db

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        favesList.setOnItemClickListener( (parent, view, position, id) -> {
            alertDialogBuilder.setTitle("Do you want to delete this?");
          //  alertDialogBuilder.setMessage(R.string.alertSetMsg + position + "\n"+ R.string.alertSetMsg2 + id);
            alertDialogBuilder.setNegativeButton("Cancel", null);
            alertDialogBuilder.setPositiveButton("Confirm", ((click, arg) -> {

                deleteMsg(faveList.get(position)); //remove the msg from database
                faveList.remove(position);
                faveListAdapter.notifyDataSetChanged();
            }));
            alertDialogBuilder.create().show();
        });

        back = findViewById(R.id.goBack);
        back.setOnClickListener(click -> {
            //go back
            this.finish();
        });
    }

    //    //load news items objects from db
    private void loadDataFromDatabase()
    {
        //get a database connection:
        NewsReaderOpener dbOpener = new NewsReaderOpener(this);
        db = dbOpener.getWritableDatabase();// We want to get all of the columns.
        String [] columns = {NewsReaderOpener.COL_ID, NewsReaderOpener.TITLE, NewsReaderOpener.DESC, NewsReaderOpener.DATE, NewsReaderOpener.LINK};

        //query all the results from the database:
        results = db.query(false, NewsReaderOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int titleColIndex = results.getColumnIndex(NewsReaderOpener.TITLE);
        int descColIndex = results.getColumnIndex(NewsReaderOpener.DESC);
        int dateColIndex = results.getColumnIndex(NewsReaderOpener.DATE);
        int linkColIndex = results.getColumnIndex(NewsReaderOpener.LINK);
        int idColIndex = results.getColumnIndex(NewsReaderOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            Favourite fave = new Favourite();

            String title = results.getString(titleColIndex);
            String desc = results.getString(descColIndex);
            String date = results.getString(dateColIndex);
            String link = results.getString(linkColIndex);
            long id = results.getLong(idColIndex);

            fave.setTitle(title);
            fave.setDate(date);
            fave.setDesc(desc);
            fave.setLink(link);
            fave.setID(id);
            //add the new item to the array list:
            faveList.add(fave);
        }
    }

    public class Favourite extends Object {
        private String title;
        private String date;
        private String link;
        private String desc;
        private long id;

        public Favourite() {}

        public Favourite(String title, String desc, String date, String link, long id) {
            this.title = title;
            this.desc = desc;
            this.date = date;
            this.link = link;
            this.id = id;
        }
            public void setTitle(String title) { this.title = title; }

            public String getTitle() {
                return title;
            }
            public void setDesc(String desc) {
                this.desc = desc;
            }
            public String getDesc() {
                return desc;
            }
            public void setDate(String date) {
                this.date = date;
            }
            public String getDate() {
                return date;
            }
            public void setLink(String link) {
                this.link = link;
            }
            public String getLink() {
                return link;
            }
            public void setID(long id) {
            this.id = id;
            }
            public long getID() {
            return id;
            }
          }

    //Adapter to inflate view
    class FaveListAdapter extends BaseAdapter {
        //returns the number of items to display in the list.
        @Override
        public int getCount() {
            return faveList.size();
        }
        //This function should return the object that you want to
        // display at row position in the list.
        @Override
        public Favourite getItem(int position) {
            return faveList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return (long) position;
        }
        @Override
        public View getView(int position, View newView, ViewGroup parent) {
            //LayoutInflater object to load an XML layout file
            LayoutInflater faveInflater = getLayoutInflater();

            Favourite fave = this.getItem(position);

            View faveView;
            //inflate the view to show the list of news item titles
            faveView = faveInflater.inflate(R.layout.news_reader_faves, parent, false);
            TextView faveListView = faveView.findViewById(R.id.faveListRow);
            faveListView.setText(fave.getTitle());

            return faveListView;
        }
    }

    protected void deleteMsg(Favourite f)
    {
        db.delete(NewsReaderOpener.TABLE_NAME, NewsReaderOpener.COL_ID + "= ?", new String[] {Long.toString(f.getID())});
    }

//    public void printCursor( Cursor c,  int version) {
//        Log.e("DB Version: ",  "db_version: " + version);
//        Log.e("Number of Columns: ", "num_of_cols: " + c.getColumnCount());;
//        c.moveToFirst();
//        for(int i = 0; i < c.getColumnCount(); i++) {
//            Log.e("Name of Columns", "column_names: " + c.getColumnName(i));
//            c.moveToNext();
//        }
//        Log.e("Number of Results","number_of_results: " + c.getCount());
//        c.moveToFirst();
//        while(!c.isAfterLast()){
//            Log.e("Results", "message: " + c.getString(c.getColumnIndex(NewsReaderOpener.TITLE)));
//            c.moveToNext();
//        }
//    }
}
