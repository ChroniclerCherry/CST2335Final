//package com.example.cst2335final;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Xml;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NewsReaderView  extends AppCompatActivity {
//    //    private String title;
////    private String description;
////    private String link;
////    private String date;
//    private static final String ns = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.news_reader_view);
//
//        NewsQuery req = new NewsQuery();
//        req.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
//}
//
//    public static class Article {
//        public final String title;
//        public final String link;
//        public final String description;
//        public final String date;
//
//
//        private Article(String title, String description, String link, String date) {
//            this.title = title;
//            this.description = description;
//            this.link = link;
//            this.date = date;
//        }
//    }
//
//    //inner class
//    public class NewsQuery extends AsyncTask<String, Integer, String> {
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
//        } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return "Done";
//        }
//
//        private List readFeed(XmlPullParser xpp) throws XmlPullParserException, IOException {
//            List articles = new ArrayList();
//
//            xpp.require(XmlPullParser.START_TAG, ns, "feed");
//            while (xpp.next() != XmlPullParser.END_TAG) {
//                if (xpp.getEventType() != XmlPullParser.START_TAG) {
//                    continue;
//                }
//                String name = xpp.getName();
//                // Starts by looking for the entry tag
//                if (name.equals("item")) {
//                    articles.add(readEntry(xpp));
//                } else {
//                    skip(xpp);
//                }
//            }
//            return articles;
//        }
//
//        public List articles(InputStream in) throws XmlPullParserException, IOException {
//            try {
//                XmlPullParser parser = Xml.newPullParser();
//                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//                parser.setInput(in, null);
//                parser.nextTag();
//                return readFeed(parser);
//            } finally {
//                in.close();
//            }
//        }
//
//        private Article readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
//            parser.require(XmlPullParser.START_TAG, ns, "item");
//            String title = null;
//            String description = null;
//            String link = null;
//            String date = null;
//            while (parser.next() != XmlPullParser.END_TAG) {
//                if (parser.getEventType() != XmlPullParser.START_TAG) {
//                    continue;
//                }
//                String tag = parser.getName();
//                if (tag.equals("title")) {
//                    title = readTitle(parser);
//                } else if (tag.equals("description")) {
//                    description = readDescription(parser);
//                } else if (tag.equals("link")) {
//                    link = readLink(parser);
//                } else if (tag.equals("link")) {
//                    date = readDate(parser);
//                } else {
//                    skip(parser);
//                }
//            }
//            return new Article(title, link, description, date);
//        }
//
//        // Processes title tags in the feed.
//        private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
//            parser.require(XmlPullParser.START_TAG, ns, "title");
//            String title = readTitle(parser);
//            parser.require(XmlPullParser.END_TAG, ns, "title");
//            return title;
//        }
//
//        // Processes desc tags in the feed.
//        private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
//            parser.require(XmlPullParser.START_TAG, ns, "description");
//            String description = readDescription(parser);
//            parser.require(XmlPullParser.END_TAG, ns, "description");
//            return description;
//        }
//
//        // Processes link tags in the feed.
//        private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
//            parser.require(XmlPullParser.START_TAG, ns, "link");
//            String link = readDescription(parser);
//            parser.require(XmlPullParser.END_TAG, ns, "link");
//            return link;
//        }
//
//        // Processes desc tags in the feed.
//        private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
//            parser.require(XmlPullParser.START_TAG, ns, "date");
//            String date = readDescription(parser);
//            parser.require(XmlPullParser.END_TAG, ns, "date");
//            return date;
//        }
//
//        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
//            if (parser.getEventType() != XmlPullParser.START_TAG) {
//                throw new IllegalStateException();
//            }
//            int depth = 1;
//            while (depth != 0) {
//                switch (parser.next()) {
//                    case XmlPullParser.END_TAG:
//                        depth--;
//                        break;
//                    case XmlPullParser.START_TAG:
//                        depth++;
//                        break;
//                }
//            }
//        }
//    }
//}
