package com.example.cst2335final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsReaderSearch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<NewsReaderItem> newsTitles = new ArrayList<>();
    private NewsListAdapter newsListAdapter;
    private Button faveListBtn;
    private Button backBtn;

    private int lastSelectedArticle;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    public static final String TITLE = "TITLE";
    public static final String DESC = "DESC";
    public static final String DATE = "DATE";
    public static final String LINK = "LINK";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

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
           lastSelectedArticle = position;

           Bundle dataToPass = new Bundle();
           dataToPass.putString(TITLE, newsTitles.get(position).getTitle());
           dataToPass.putString(DESC, newsTitles.get(position).getDesc());
           dataToPass.putString(DATE, newsTitles.get(position).getDate());
           dataToPass.putString(LINK, newsTitles.get(position).getLink());
           Intent nextActivity = new Intent(NewsReaderSearch.this, EmptyActivity.class);
           nextActivity.putExtras(dataToPass); //send data to next activity
           startActivity(nextActivity); //make the transition
        });

        backBtn = findViewById(R.id.backToMain);
        backBtn.setOnClickListener(click -> finish());

        faveListBtn = findViewById(R.id.goToFaveList);
        faveListBtn.setOnClickListener(click -> {
           Intent goToFaves = new Intent(NewsReaderSearch.this, NewsReaderFaves.class);
            startActivity(goToFaves);
        });

        prefs = getSharedPreferences("NewsReaderLastArticle", Context.MODE_PRIVATE);
        lastSelectedArticle = prefs.getInt("LastViewed",0);

        //button to go to last article viewed
        Button viewLastButton = findViewById(R.id.viewLastArticle);
        viewLastButton.setOnClickListener(clk -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(TITLE, newsTitles.get(lastSelectedArticle).getTitle());
            dataToPass.putString(DESC, newsTitles.get(lastSelectedArticle).getDesc());
            dataToPass.putString(DATE, newsTitles.get(lastSelectedArticle).getDate());
            dataToPass.putString(LINK, newsTitles.get(lastSelectedArticle).getLink());
            Intent nextActivity = new Intent(NewsReaderSearch.this, EmptyActivity.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition
        });

        //toolbar
        Toolbar tbar = findViewById(R.id.toolbar);
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
        header.setText(R.string.lia_info);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        edit = prefs.edit();
        edit.putInt("LastViewed",lastSelectedArticle);
        edit.apply();
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
                Intent goHome = new Intent(NewsReaderSearch.this, MainActivity.class);
                startActivity(goHome);
                break;
            case R.id.bbc:
                Intent gotoBbc = new Intent(NewsReaderSearch.this, NewsReaderSearch.class);
                startActivity(gotoBbc);
                break;
            case R.id.guardian:
                message2 = getText(R.string.error_not_implemented).toString();
                break;
            case R.id.earth:
                Intent gotoEarth = new Intent(NewsReaderSearch.this, Nasa_Earthy_Image_Db.class);
                startActivity(gotoEarth);
                break;
            case R.id.space:
                Intent gotoSpace = new Intent(NewsReaderSearch.this, NASADailyFavourites.class);
                startActivity(gotoSpace);
                break;
            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.title_bbc)
                        //Message
                        .setMessage("this is an example message for instructions") //TODO: ADD INSTRUCTIONS FOR YOUR ACTIVITY
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
                Intent gotoBbc = new Intent(NewsReaderSearch.this, NewsReaderSearch.class);
                startActivity(gotoBbc);
                break;
            case R.id.guardian:
                message2 = getText(R.string.error_not_implemented).toString();
                break;
            case R.id.earth:
                Intent gotoEarth = new Intent(NewsReaderSearch.this, Nasa_Earthy_Image_Db.class);
                startActivity(gotoEarth);
                break;
            case R.id.space:
                Intent gotoSpace = new Intent(NewsReaderSearch.this, NASADailyFavourites.class);
                startActivity(gotoSpace);
                break;
        }

        if (message2 != null)
            Toast.makeText(this, message2, Toast.LENGTH_LONG).show();
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
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
