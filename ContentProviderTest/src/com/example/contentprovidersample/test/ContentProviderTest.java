package com.example.contentprovidersample.test;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.example.contentprovidersample.MyContentProvider;

public class ContentProviderTest extends ProviderTestCase2<MyContentProvider> {

	private static final String CONTENT_EQ_TESTING = "content = 'Testing'";
	ContentValues values = new ContentValues();

	public ContentProviderTest() {
		super(MyContentProvider.class, MyContentProvider.AUTHORITY);
	}
	
	public void testGetType() {
		Uri uriItems = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, "/items");
		Uri uriItemSingle = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, "/items/5");
		MyContentProvider cp = getProvider();
		assertEquals("cp getType multi", 
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MyContentProvider.MIME_VND_TYPE,
				cp.getType(uriItems));
		assertEquals("cp getType single", 
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MyContentProvider.MIME_VND_TYPE,
				cp.getType(uriItemSingle));
	}
	
	public void testInsertAndQuery() {
		values.clear();
		values.put("content", "Testing");
		MyContentProvider cp = getProvider();
		long id = ContentUris.parseId(cp.insert(MyContentProvider.CONTENT_URI, values));
		assertTrue("created OK", id > -1);
		long id2 = ContentUris.parseId(cp.insert(MyContentProvider.CONTENT_URI, values));
		assertTrue("id's increment", id2 > id);
		String[] columns = {"content"};
		final Cursor queryResults = cp.query(MyContentProvider.CONTENT_URI, columns, CONTENT_EQ_TESTING, null, null);
		assertEquals(2, queryResults.getCount());
	}
	
	public void testDelete() {
		values.clear();
		values.put("content", "Testing");
		MyContentProvider cp = getProvider();
		long id = ContentUris.parseId(cp.insert(MyContentProvider.CONTENT_URI, values));
		assertTrue("created OK", id > -1);
		final int deleted = cp.delete(MyContentProvider.CONTENT_URI, CONTENT_EQ_TESTING, null);
		assertEquals("deleted rows", 1, deleted);
	}
}
