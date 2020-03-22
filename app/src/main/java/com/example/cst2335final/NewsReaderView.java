//package com.example.cst2335final;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
////TODO Java docs/comments/descrip
////todo add list view of news articles that pop up a frame NewsReaderFrame with details when selected
////This class is the main page and List View of the articles on BBC News Reader
//
//public class NewsReaderView extends AppCompatActivity {
//
//    //TODO check ArrayLIst settings
//    private ArrayList<NewsReaderItem> newsElements = new ArrayList<NewsReaderItem>(Arrays.<NewsReaderItem>asList());
//
//    private NewsListAdapter newsListAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.news_reader_view);
//
//        ListView articleList = findViewById(R.id.newsList);
//
//        articleList.setAdapter(newsListAdapter = new NewsListAdapter());
//    }
//        //inner class for list adapter
//        class NewsListAdapter extends BaseAdapter {
//
//            //returns the number of items to display in the list.
//            @Override
//            public int getCount() {
//                return newsElements.size();
//            }
//
//            // This function should return the object that you want to
//            // display at row position in the list.
//            // Returns the news object that corresponds to int passed in
//            @Override
//            public NewsReaderItem getItem(int position) {
//                return newsElements.get(position);
//            }
//
//            //is used to return the database ID of the element
//            // at the given index of position.
//            @Override
//            public long getItemId(int position) {
//                return (long) position;
//            }
//
//            //specifies how each row looks.
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//
//                //LayoutInflater object to load an XML layout file
//                LayoutInflater newsInflater = getLayoutInflater();
//
//                NewsReaderItem newsItem = this.getItem(position);
//
//                //creates a new view or layout object from xml
//                View newsView;
//
//                newsView = newsInflater.inflate(R.layout.news_reader_view, parent, false);
//
//                //set text for row
//                TextView newsTextView = newsView.findViewById(R.id.newsList);
//
//                //newsTextView.setText(newsItem.getTitle());
//
//                //return to put in table
//                return newsView;
//            }
//        }
//    }
//
