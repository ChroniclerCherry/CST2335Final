package com.example.cst2335final;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.util.ArrayList;

public class NewsReaderSearch extends AppCompatActivity {

    private ArrayList<NewsReaderItem> newsTitles = new ArrayList<>();
    private NewsListAdapter newsListAdapter;
    private Button faveListBtn;

    public static final String TITLE = "TITLE";
    public static final String DESC = "DESC";
    public static final String DATE = "DATE";
    public static final String LINK = "LINK";
    //public static final String DB = "DB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader_view);

        //ref to listview in xml
        ListView newsList = findViewById(R.id.newsList);

        //set adapter on listview to populate with objects
        newsList.setAdapter(newsListAdapter);

        newsList.setAdapter(newsListAdapter = new NewsListAdapter());

       //website w data to grab
       NewsQuery req = new NewsQuery();
       req.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

       //on Item Click listener for rows to get news item details
       newsList.setOnItemClickListener((parent, view, position, id) -> {
           Bundle dataToPass = new Bundle();
           dataToPass.putString(TITLE, newsTitles.get(position).getTitle());
           dataToPass.putString(DESC, newsTitles.get(position).getDesc());
           dataToPass.putString(DATE, newsTitles.get(position).getDate());
           dataToPass.putString(LINK, newsTitles.get(position).getLink());

           Intent nextActivity = new Intent(NewsReaderSearch.this, EmptyActivity.class);
           nextActivity.putExtras(dataToPass); //send data to next activity
           startActivity(nextActivity); //make the transition

        });

        faveListBtn = findViewById(R.id.goToFaveList);
        faveListBtn.setOnClickListener(click -> {
           Intent goToFaves = new Intent(NewsReaderSearch.this, NewsReaderFaves.class);
            startActivity(goToFaves);
        });
    }

    //Adapter to inflate view
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

            View newsView;
            //inflate the view to show the list of news item titles
            newsView = newsInflater.inflate(R.layout.news_reader_titles, parent, false);
            TextView newsListTitle = newsView.findViewById(R.id.newsListRow);
            newsListTitle.setText(newsItem.getTitle());

            return newsView;
        }
    }

    //connects to website, searches tags in xml for strings required
    public class NewsQuery extends AsyncTask<String, Integer, String> {
        private String title; //article title
        private String description; //description of article
        private String date; //date of article
        private String link;
        private ProgressBar progbar;

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
                            //Thread.sleep(10);
                            if(xpp.getName().equalsIgnoreCase("item")){
                                 newsItem = new NewsReaderItem();
                            }
                                if(xpp.getName().equalsIgnoreCase("title")){
                                xpp.next();
                                title = xpp.getText();
                                    //set title of object
                                    newsItem.setTitle(title);
                                   publishProgress(25);
                                }
                                else if(xpp.getName().equalsIgnoreCase("description")){
                                xpp.next();
                                description = xpp.getText();
                                    //set description of object
                                    newsItem.setDesc(description);
                                    publishProgress(50);
                                }
                                else if(xpp.getName().equalsIgnoreCase("link")){
                                xpp.next();
                                link = xpp.getText();
                                    //set link of object
                                    newsItem.setLink(link);
                                    publishProgress(75);
                                }
                                else if(xpp.getName().equalsIgnoreCase("pubDate")){
                                xpp.next();
                                date = xpp.getText();
                                    //set date of object
                                    newsItem.setDate(date);

                                    //add item to list
                                    newsTitles.add(newsItem);
                                    publishProgress(100);
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
            }
            return "done";
        }

        public void onProgressUpdate(Integer... args) {
            progbar = findViewById(R.id.progressBar);
           progbar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String fromDoInBackground) {
            newsListAdapter.notifyDataSetChanged();
            progbar.setVisibility(View.INVISIBLE);
        }
    }
}
