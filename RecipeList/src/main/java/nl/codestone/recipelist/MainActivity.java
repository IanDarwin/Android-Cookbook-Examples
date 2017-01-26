package nl.codestone.recipelist;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener {

    String url = "https://androidcookbook.com/seam/resource/rest/recipe/list";

    protected static final int DIALOG_KEY = 0;
    ListView mListView;
    Button mClear;
    Button mRefresh1;
    Button mRefresh2;
    ProgressDialog mProgressDialog;
    WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminateVisibility(false);

        setContentView(R.layout.main);
        mListView = (ListView) findViewById(R.id.listView1);
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener(this);

        mRefresh1 = (Button) findViewById(R.id.button1);
        mRefresh1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LoadRecipesTask1 mLoadRecipesTask = new LoadRecipesTask1();
                mLoadRecipesTask.execute(url);
            }
        });

        mRefresh2 = (Button) findViewById(R.id.button2);
        mRefresh2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LoadRecipesTask2 mLoadRecipesTask = new LoadRecipesTask2();
                showDialog(DIALOG_KEY);                                                     // 1
                mLoadRecipesTask.execute(url, url, url, url, url);                          // 2
            }
        });

        mClear = (Button) findViewById(R.id.button3);
        mClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setAdapter(null);
            }
        });

    }



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_KEY:                                                               // 1
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         // 2
            mProgressDialog.setMessage("Retrieving recipes...");                       // 3
            mProgressDialog.setCancelable(false);                                      // 4
            return mProgressDialog;
        }
        return null;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Datum datum = (Datum) mListView.getItemAtPosition(position);
        Uri uri = Uri.parse("http://androidcookbook.com/Recipe.seam?recipeId=" + datum.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

    /** Parse an XML input URL */
    public static ArrayList<Datum> parse(String url) throws IOException, XmlPullParserException {
        URL input = new URL(url);
        final InputStream inputStream = input.openStream();
        return parse(inputStream);
    }

    public static ArrayList<Datum> parse(InputStream inputStream) throws IOException, XmlPullParserException {
        final ArrayList<Datum> results = new ArrayList<Datum>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(inputStream, null);
        int eventType = xpp.getEventType();
        String currentTag = null;
        Integer id = null;
        String title = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                currentTag = xpp.getName();
            } else if (eventType == XmlPullParser.TEXT) {
                if ("id".equals(currentTag)) {
                    id = Integer.valueOf(xpp.getText());
                }
                if ("title".equals(currentTag)) {
                    title = xpp.getText();
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if ("recipe".equals(xpp.getName())) {
                    results.add(new Datum(id, title));
                }
            }
            eventType = xpp.next();
        }
        return results;
    }
    
    /** Parse an XML input URL */
    public static ArrayList<Datum> parse2(String url) throws IOException, XmlPullParserException {
        URL input = new URL(url);
        final InputStream inputStream = input.openStream();
        return parse2(inputStream);
    }

    public static ArrayList<Datum> parse2(InputStream inputStream) throws IOException, XmlPullParserException {
        final ArrayList<Datum> results = new ArrayList<Datum>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(inputStream, null);
        xpp.nextTag();
        xpp.require(XmlPullParser.START_TAG, null, "recipes");
        while (xpp.nextTag() == XmlPullParser.START_TAG) {
            xpp.require(XmlPullParser.START_TAG, null, "recipe");

            xpp.nextTag();
            xpp.require(XmlPullParser.START_TAG, null, "id");
            Integer id = Integer.valueOf(xpp.nextText());
            xpp.require(XmlPullParser.END_TAG, null, "id");

            xpp.nextTag();
            xpp.require(XmlPullParser.START_TAG, null, "title");
            String title = xpp.nextText();
            xpp.require(XmlPullParser.END_TAG, null, "title");

            xpp.nextTag();
            xpp.require(XmlPullParser.END_TAG, null, "recipe");

            results.add(new Datum(id, title));
        }
        xpp.require(XmlPullParser.END_TAG, null, "recipes");

        return results;
    }

    /**
     * Fetch the list of Recipes using an AsyncTask and the deprecated
     * setProgressBarIndeterminateVisibility method
     */
    protected class LoadRecipesTask1 extends AsyncTask<String, Void, ArrayList<Datum>> {

        @Override
        protected void onPreExecute() {
            MainActivity.this.setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected ArrayList<Datum> doInBackground(String... urls) {
            ArrayList<Datum> datumList = new ArrayList<Datum>();
            try {
                datumList = parse(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return datumList;
        }

        @Override
        protected void onPostExecute(ArrayList<Datum> result) {
            mListView.setAdapter(new ArrayAdapter<Datum>(MainActivity.this, R.layout.list_item, result));
            MainActivity.this.setProgressBarIndeterminateVisibility(false);
        }
    }

    /**
     * Fetch the list of Recipes using an AsyncTask and a ProgressDialog
     */
    protected class LoadRecipesTask2 extends AsyncTask<String, Integer, ArrayList<Datum>> {
        
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();                                                          // 1
        }
        
        @Override
        protected ArrayList<Datum> doInBackground(String... urls) {
            ArrayList<Datum> datumList = new ArrayList<Datum>();
            for (int i = 0; i < urls.length; i++) {                                          // 2
                try {
                    datumList = parse(urls[i]);
                    publishProgress((int) (((i+1) / (float) urls.length) * 100));            // 3
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            return datumList;
        }
        
        @Override
        protected void onProgressUpdate(Integer... values) {                                 // 4
            mProgressDialog.setProgress(values[0]);                                          // 5
        }
        
        @Override
        protected void onPostExecute(ArrayList<Datum> result) {
            mListView.setAdapter(new ArrayAdapter<Datum>(MainActivity.this, R.layout.list_item, result));
            mProgressDialog.dismiss();                                                       // 6
        }
    }
}