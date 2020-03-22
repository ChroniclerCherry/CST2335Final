//package com.example.cst2335final;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import java.io.File;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class NewsReaderSearch extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //load news_article
//        //setContentView(R.layout.news_reader_view);
//        NewsQuery req = new NewsQuery();
//        req.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
//    }
//
//    //inner class
//    public class NewsQuery extends AsyncTask<String, Integer, String> {
//
//        private String title; //article title
//        private String description; //description of article
//        private String date; //date of article
//        private String link;
//        private ProgressBar progbar;
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                //create URL object of server to contact
//                URL url = new URL(strings[0]);
//                //open connection
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                //wait for data
//                InputStream response = urlConnection.getInputStream();
//
//                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                factory.setNamespaceAware(false);
//                XmlPullParser xpp = factory.newPullParser();
//                xpp.setInput(response, "UTF-8");
//
//                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT
//
//                while (eventType != XmlPullParser.END_DOCUMENT) //while not at end do this
//                {
//                    if (eventType == XmlPullParser.START_TAG) //if at start tag of doc
//                    {
//
//                        if (xpp.getName().equals("item")) //if tag name item
//                        {
//                            if (xpp.getName().equals("title")) //get article title
//                            {
//                                title = xpp.getAttributeValue(null, null);
//                                publishProgress(25);
//                            }
//                            if (xpp.getName().equals("description")) {
//                                description = xpp.getAttributeValue(null, null);
//                                publishProgress(50);
//                            }
//                            if (xpp.getName().equals("pubDate")) {
//                                date = xpp.getAttributeValue(null, null);
//                                publishProgress(75);
//                            }
//                            if (xpp.getName().equals("link")) {
//                                link = xpp.getAttributeValue(null, null);
//                                publishProgress(100);
//                            }
//                        }
//                        eventType = xpp.next(); //move to the next xml event and store it in a variable
//                    }
//                }
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//            }
//            return "Done";
//        }
//        public void onProgressUpdate(Integer... args) {
//            progbar = findViewById(R.id.progressBar);
//            progbar.setVisibility(View.VISIBLE);
//        }
//
//        public void onPostExecute(String fromDoInBackground) {
//            ListView newsList = findViewById(R.id.newsList);
//
//            progbar.setVisibility(View.INVISIBLE);
//        }
//    }
//}