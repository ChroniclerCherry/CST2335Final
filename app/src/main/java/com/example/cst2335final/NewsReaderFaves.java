package com.example.cst2335final;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

/**
 * List of articles that were saved to the database as favourites.
 *  @Author Lia Brophy
 *  @Version 1.0
 *  @Date 2020-04-01
 */
public class NewsReaderFaves extends AppCompatActivity {
    private ArrayList<Favourite> faveList = new ArrayList<Favourite>();
    private Button delete;
    private Button back;
    SQLiteDatabase db;
    Cursor results;
    private FaveListAdapter faveListAdapter;
    //Favourite fave;

    /**
     * Sets up activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader_fave_view);
        //ref to listview in xml
        ListView favesList = findViewById(R.id.newsList);
        //set adapter on listview to populate with objects
        favesList.setAdapter(faveListAdapter = new FaveListAdapter());
        //get articles from db
        loadDataFromDatabase();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //click listner to remove from favourites list
        favesList.setOnItemClickListener( (parent, view, position, id) -> {
            alertDialogBuilder.setTitle(R.string.alert_message);
            alertDialogBuilder.setNegativeButton(R.string.no, null);
            alertDialogBuilder.setPositiveButton(R.string.ok, ((click, arg) -> {

                deleteMsg(faveList.get(position)); //remove the item from database
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

    /**
     *  Method to load items from the database
     */
    private void loadDataFromDatabase()
    {
        //get a database connection:
        NewsReaderOpener dbOpener = new NewsReaderOpener(this);
        db = dbOpener.getWritableDatabase();// We want to get all of the columns.
        String [] columns = {NewsReaderOpener.COL_ID, NewsReaderOpener.TITLE, NewsReaderOpener.DESC, NewsReaderOpener.DATE, NewsReaderOpener.LINK, NewsReaderOpener.NOTE};

        //query all the results from the database:
        results = db.query(false, NewsReaderOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int titleColIndex = results.getColumnIndex(NewsReaderOpener.TITLE);
        int descColIndex = results.getColumnIndex(NewsReaderOpener.DESC);
        int dateColIndex = results.getColumnIndex(NewsReaderOpener.DATE);
        int linkColIndex = results.getColumnIndex(NewsReaderOpener.LINK);
        int noteColIndex = results.getColumnIndex(NewsReaderOpener.NOTE);
        int idColIndex = results.getColumnIndex(NewsReaderOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            Favourite fave = new Favourite();

            String title = results.getString(titleColIndex);
            String desc = results.getString(descColIndex);
            String date = results.getString(dateColIndex);
            String link = results.getString(linkColIndex);
            String note = results.getString(noteColIndex);
            long id = results.getLong(idColIndex);

            fave.setTitle(title);
            fave.setDate(date);
            fave.setDesc(desc);
            fave.setLink(link);
            fave.setID(id);
            fave.setNote(note);
            //add the new item to the array list:
            faveList.add(fave);
        }
    }

    /**
     * inner class to create a favourite object
     */
    public class Favourite extends Object {
        private String title;
        private String date;
        private String link;
        private String desc;
        private String note;
        private long id;

        /**
         * Default constructor
         */
        public Favourite() {}

        /**
         * Parameterized constructor
         * @param title
         * @param desc
         * @param date
         * @param link
         * @param note
         * @param id
         */
        public Favourite(String title, String desc, String date, String link, String note, long id) {
            this.title = title;
            this.desc = desc;
            this.date = date;
            this.link = link;
            this.note = note;
            this.id = id;
        }

        /**
         * Access to set object title
         * @param title
         */
            public void setTitle(String title) { this.title = title; }

        /**
         * Access to retrieve object title
         * @return
         */
            public String getTitle() {
                return title;
            }

        /**
         * Access to set object desc
         * @param desc
         */
            public void setDesc(String desc) {
                this.desc = desc;
            }

        /**
         * Access to retrieve object desc
         * @return
         */
            public String getDesc() {
                return desc;
            }

        /**
         * Access to set object date
         * @param date
         */
            public void setDate(String date) {
                this.date = date;
            }

        /**
         * Access to get object date
         * @return
         */
            public String getDate() {
                return date;
            }

        /**
         * Access to set object link
         * @param link
         */
            public void setLink(String link) {
                this.link = link;
            }

        /**
         * Access to retrieve object link
         * @return
         */
            public String getLink() {
                return link;
            }

        /**
         * Access to set object note
         * @param note
         */
            public void setNote(String note) {
            this.note = note;
        }

        /**
         * Access to retrieve object note
         * @return
         */
            public String getNote() {
            return note;
        }

        /**
         * Access to set object id
         * @param id
         */
            public void setID(long id) {
            this.id = id;
            }

        /**
         * Access to get object id
         * @return
         */
            public long getID() {
            return id;
            }
          }

    /**
     * Adapter for favourites list
     */
    class FaveListAdapter extends BaseAdapter {
        //returns the number of items to display in the list.

        /**
         * count of objects
         * @return
         */
        @Override
        public int getCount() {
            return faveList.size();
        }

        /**
         * Return object to display at position in list
         * @param position
         * @return
         */
        @Override
        public Favourite getItem(int position) {
            return faveList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        /**
         * Gets view
         * @param position
         * @param newView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View newView, ViewGroup parent) {
            //LayoutInflater object to load an XML layout file
            LayoutInflater faveInflater = getLayoutInflater();

            Favourite fave = this.getItem(position);

            View faveView;
            //inflate the view to show the list of news item titles
            faveView = faveInflater.inflate(R.layout.news_reader_faves, parent, false);
            TextView faveListView = faveView.findViewById(R.id.faveListRow);
            faveListView.setText(" Date:  " + fave.getDate() + " Title:  " + fave.getTitle()+ " " + fave.getDesc()+ " Link:  " + fave.getLink() + " Note: " + fave.getNote());

            return faveListView;
        }
    }

    /**
     * Memthod to remove an object from the database
     * @param f
     */
    protected void deleteMsg(Favourite f)
    {
        db.delete(NewsReaderOpener.TABLE_NAME, NewsReaderOpener.COL_ID + "= ?", new String[] {Long.toString(f.getID())});
    }
}
