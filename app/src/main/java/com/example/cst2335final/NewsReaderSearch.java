package com.example.cst2335final;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

public class NewsReaderSearch extends AppCompatActivity {
    private ArrayList<NewsReaderItem> newsTitles = new ArrayList<>();
    ListView newsList;
    NewsListAdapter newsAdapter = new NewsListAdapter();
    SQLiteDatabase db;
    Cursor results;

    public  NewsReaderFragment newsFragment = new NewsReaderFragment();
    public static final String TITLE = "TITLE";
    public static final String DESC = "DESC";
    public static final String DATE = "DATE";
    public static final String LINK = "LINK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.news_reader_view);
       newsList = findViewById(R.id.newsList);

       loadDataFromDatabase(); //get articles from db

        //set adapter on listview to populate with objects
       newsList.setAdapter(newsAdapter);

       NewsQuery req = new NewsQuery();
       req.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

       newsList.setOnItemClickListener((parent, view, position, id) -> {
           Bundle dataToPass = new Bundle();
           dataToPass.putString("Title", newsTitles.get(position).getTitle());
           dataToPass.putString("Desc", String.valueOf(newsTitles.get(position).getDesc()));
           dataToPass.putString("Date", String.valueOf(newsTitles.get(position).getDate()));
           dataToPass.putString("Link", String.valueOf(newsTitles.get(position).getLink()));

           Intent nextActivity = new Intent(NewsReaderSearch.this, EmptyActivity.class);
           nextActivity.putExtras(dataToPass); //send data to next activity
           startActivity(nextActivity); //make the transition

        });
    }

    //load news items objects from db
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
            String title = results.getString(titleColIndex);
            String desc = results.getString(descColIndex);
            String date = results.getString(dateColIndex);
            String link = results.getString(linkColIndex);
            long id = results.getLong(idColIndex);

            //add the new item to the array list:
            //newsTitles.add();
        }
        //At this point, the contactsList array has loaded every row from the cursor.
        printCursor(results, db.getVersion());
    }

    public void printCursor( Cursor c,  int version) {
        Log.e("DB Version: ",  "db_version: " + version);
        Log.e("Number of Columns: ", "num_of_cols: " + c.getColumnCount());;
        c.moveToFirst();
        for(int i = 0; i < c.getColumnCount(); i++) {
            Log.e("Name of Columns", "column_names: " + c.getColumnName(i));
            c.moveToNext();
        }
        Log.e("Number of Results","number_of_results: " + c.getCount());
        c.moveToFirst();
        while(!c.isAfterLast()){
            Log.e("Results", "Title: " + c.getString(c.getColumnIndex(NewsReaderOpener.TITLE)));
            Log.e("Results", "Desc: " + c.getString(c.getColumnIndex(NewsReaderOpener.DESC)));
            Log.e("Results", "Date: " + c.getString(c.getColumnIndex(NewsReaderOpener.DATE)));
            Log.e("Results", "Link: " + c.getString(c.getColumnIndex(NewsReaderOpener.LINK)));
            Log.e("Results", "id: " + c.getString(c.getColumnIndex(NewsReaderOpener.COL_ID)));

            c.moveToNext();
        }
    }

    //inner class
    class NewsListAdapter extends BaseAdapter {
        //returns the number of items to display in the list.
        @Override
        public int getCount() {
            return newsTitles.size();
        }
        //This function should return the object that you want to
        // display at row position in the list.
        @Override
        public NewsReaderItem getItem(int position) {
            return newsTitles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View newView, ViewGroup parent) {
            //LayoutInflater object to load an XML layout file
            LayoutInflater newsInflater = getLayoutInflater();

            NewsReaderItem newsItem = this.getItem(position);

            //inflate the view to show the list of news item titles
            newView = newsInflater.inflate(R.layout.news_reader_view, parent, false);
            TextView newsListTitle = newView.findViewById(R.id.newsListRow);
            newsListTitle.setText(newsItem.getTitle());

            return newView;
        }
    }

    //inner class
    public class NewsQuery extends AsyncTask<String, Integer, String> {
        private String title; //article title
        private String description; //description of article
        private String date; //date of article
        private String link;
        //private ProgressBar progbar;

        @Override
        protected String doInBackground(String... strings) {
            try {
                //create URL object of server to contact
                URL url = new URL(strings[0]);
                //open connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data
                InputStream response = urlConnection.getInputStream();
                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, null);

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                //create newsReader object
                NewsReaderItem newsItem = new NewsReaderItem();
                //https://stackoverflow.com/questions/17434135/how-to-parse-an-rss-feed-with-xmlpullparser
                //https://stackoverflow.com/questions/25360955/xmlpullparser-getname-returns-null
                while (eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            Thread.sleep(10);
                            if(xpp.getName().equalsIgnoreCase("item")){
                            }
                                if(xpp.getName().equalsIgnoreCase("title")){
                                xpp.next();
                                title = xpp.getText();
                                    //set title of object
                                    newsItem.setTitle(title);
                                    //publishProgress(25);
                                }
                                else if(xpp.getName().equalsIgnoreCase("description")){
                                xpp.next();
                                description = xpp.getText();
                                    //set description of object
                                    newsItem.setDesc(description);
                                    //publishProgress(50);
                                }
                                else if(xpp.getName().equalsIgnoreCase("link")){
                                xpp.next();
                                link = xpp.getText();
                                    //set link of object
                                    newsItem.setLink(link);
                                    //publishProgress(75);
                                }
                                else if(xpp.getName().equalsIgnoreCase("pubDate")){
                                xpp.next();
                                date = xpp.getText();
                                    //set date of object
                                    newsItem.setDate(date);

                                    newsTitles.add(newsItem);

                                    ContentValues newRow = new ContentValues();
                                    newRow.put(NewsReaderOpener.TITLE, newsItem.getTitle());
                                    newRow.put(NewsReaderOpener.DESC, newsItem.getDesc());
                                    newRow.put(NewsReaderOpener.DATE, newsItem.getDate());
                                    newRow.put(NewsReaderOpener.LINK, newsItem.getLink());
                                    //publishProgress(100);
                                }
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                    }
                    eventType = xpp.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "done";
        }

        public void onProgressUpdate(Integer... args) {
            //progbar = findViewById(R.id.progressBar);
            //progbar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String fromDoInBackground) {
            newsAdapter.notifyDataSetChanged();
            //progbar.setVisibility(View.INVISIBLE);
        }
    }
}
