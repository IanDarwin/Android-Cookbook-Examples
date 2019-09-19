package com.darwinsys.browserbookmarks;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.darwinsys.bookmarkscp.Browser;

public class BookmarksActivity extends Activity {

    public static final String TAG = "BookmarksList";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String[] projection = {
                Browser.BookmarkColumns._ID,
                Browser.BookmarkColumns.TITLE,
                Browser.BookmarkColumns.URL
        };

        String[] fromCols = {
                Browser.BookmarkColumns.TITLE,
                Browser.BookmarkColumns.URL
        };
        int[] toViews = { R.id.text1, R.id.text2 };

        final Cursor c = getContentResolver().query(Browser.BOOKMARKS_URI,
                projection,
                null, null,
                "title asc");

        // IRL: Use a CursorLoader! This is a simple demo
        mListView.setAdapter(new SimpleCursorAdapter(this,
                R.layout.two_item_list,
                c,
                fromCols,
                toViews));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Position = " + position);
                // We still have the Cursor, so re-use it.
                c.moveToPosition(position);
                String url = c.getString(2);
                Log.d(TAG, "Opening URL " + url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}
