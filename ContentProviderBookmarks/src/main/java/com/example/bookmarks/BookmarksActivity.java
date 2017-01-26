package com.example.bookmarks;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class BookmarksActivity extends ListActivity {

    private static String TAG = BookmarksActivity.class.getSimpleName();
    
    // Herewith the standard "highly structured" parallel array anti-pattern enforced by Android
    String[] from = { 
    		Browser.BookmarkColumns.TITLE, 
    		Browser.BookmarkColumns.URL,
    };
    int[] to = { 
    		R.id.bmark_title, 
    		R.id.bmark_url,
    };
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
		Cursor qr = getContentResolver().query(Browser.BOOKMARKS_URI, null, null, null, null);
		Log.d(TAG, "CURSOR rows = " + qr.getCount());
		CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, qr, from, to);
		setListAdapter(adapter);
		final ListView myListView = getListView();
		myListView.setLongClickable(true);
		myListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(TAG, "BookmarksActivity.getListView().onLongClick");
				final int deleted = getContentResolver().delete(Browser.BOOKMARKS_URI, "_id = ?", new String[]{Long.toString(id)});
				
				Toast.makeText(BookmarksActivity.this, 
					deleted == 1 ? "Deleted!" : "Failed to delete",
					Toast.LENGTH_SHORT).show();
				
				return false;
			}
		});
    }

}

