package com.darwinsys.bookmarkscp;

import android.net.Uri;

public class Browser {
    public final static Uri BOOKMARKS_URI =
            Uri.parse("content://com.darwinsys.bookmarks/bookmarks");

    public interface BookmarkColumns {
        public final String _ID = "_id";
        public final String TITLE = "title";
        public final String URL = "url";
    }
}
