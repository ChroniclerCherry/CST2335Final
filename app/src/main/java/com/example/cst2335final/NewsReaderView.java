package com.example.cst2335final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

//TODO Java docs/comments/descrip
//todo add list view of news articles that pop up a frame NewsReaderFrame with details when selected
//This class is the main page and List View of the articles on BBC News Reader

public class NewsReaderView extends AppCompatActivity {

    //TODO check ArrayLIst settings
    private ArrayList<NewsReaderItem> articleElements = new ArrayList<NewsReaderItem>(Arrays.<NewsReaderItem>asList());

    private ArticleListAdapter articleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader_view);

        ListView articleList = findViewById(R.id.newsList);

        articleList.setAdapter(articleListAdapter = new ArticleListAdapter());

    }

        class ArticleListAdapter extends BaseAdapter {

            //returns the number of items to display in the list.
            @Override
            public int getCount() {
                return articleElements.size();
            }

            // This function should return the object that you want to
            // display at row position in the list.
            // Returns the article bject that corresponds to int passed in
            @Override
            public NewsReaderItem getItem(int position) {
                return articleElements.get(position);
            }

            //is used to return the database ID of the element
            // at the given index of position.
            @Override
            public long getItemId(int position) {
                return (long) position;
            }

            //specifies how each row looks.
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                //LayoutInflater object to load an XML layout file
                LayoutInflater articleInflater = getLayoutInflater();

                NewsReaderItem artc = this.getItem(position);

                //creates a new view or layout object from xml
                View articleView;

                articleView = articleInflater.inflate(R.layout.news_reader_view, parent, false);

                //set text for row
                // TextView articleTextView = articleViewView.findViewById(R.id.articleView);

                //articleTextView.setText(articleActivity.getArticle());

                //return to put in table
                return articleView;
            }
        }
    }

