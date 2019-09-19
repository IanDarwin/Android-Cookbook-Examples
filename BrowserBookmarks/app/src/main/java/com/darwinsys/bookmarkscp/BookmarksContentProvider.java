package com.darwinsys.bookmarkscp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;

/**
 * A completely mock re-implementation of the BrowserBookmarks CP
 * that died back in API 23.
 * Not useful except to keep ancient demos alive.
 */
public class BookmarksContentProvider extends ContentProvider {

    /** Private inner class just to create a list of data */
    private static class Bookmark {
        int _id;
        String title;
        String URL;

        Bookmark(int _id, String title, String URL) {
            this._id = _id;
            this.title = title;
            this.URL = URL;
        }
    }

    final static Bookmark[] data = {
            new Bookmark(1, "DarwinSys-Java", "https://darwinsys.com/java"),
            new Bookmark(2, "Facebook", "https://facebook.com"),
            new Bookmark(3, "Google Search", "https://google.com/"),
            new Bookmark(4, "Google Maps", "https://google.com/maps"),
            new Bookmark(5, "Instagram", "https://instagram.com"),
            new Bookmark(6, "Tesla", "https://ts.la/ian40191"),
            new Bookmark(7, "Twitter", "https://twitter.com"),
            new Bookmark(8, "Yahoo", "https://yahoo.com"),
    };

    private static final String[] COLUMN_NAMES = {
            Browser.BookmarkColumns._ID,
            Browser.BookmarkColumns.TITLE,
            Browser.BookmarkColumns.URL,
    };

    /** Static Cursor implementation class */
    class DataCursor extends AbstractCursor {
        int currentRow = -1;

        public boolean onMove(int oldPosition, int newPosition) {
            currentRow = newPosition;
            return true;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public String[] getColumnNames() {
            return COLUMN_NAMES;
        }

        @Override
        public String getString(int column) {
            switch(column) {
                case 0:
                    throw new IllegalArgumentException();
                case 1:
                    return data[currentRow].title;
                case 2:
                    return data[currentRow].URL;
            }
            throw new IllegalArgumentException();
        }

        @Override
        public short getShort(int column) {
            throw new IllegalArgumentException();
        }

        @Override
        public int getInt(int column) {
            if (column == 0) {
                return data[currentRow]._id;
            }
            throw new IllegalArgumentException();
        }

        @Override
        public long getLong(int column) {
            if (column == 0) {
                return data[currentRow]._id;
            }
            throw new IllegalArgumentException();
        }

        @Override
        public float getFloat(int column) {
            return 0;
        }

        @Override
        public double getDouble(int column) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isNull(int column) {
            return false;
        }
    }
    public BookmarksContentProvider() {
        // empty
    }

    @Override
    public boolean onCreate() {
        // empty
        return true;
    }


    @Override
    public String getType(Uri uri) {
        return "vnd.darwinsys.cursor.dir/bookmark";
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return new DataCursor();
    }

    // Insert, update and delete won't be implemented, as it's read-only data

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Read-only");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Read-only");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Read-Only");
    }

}
