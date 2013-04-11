package com.example.contentprovidersample.test;

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
	
	public void testInsertAndQuery() {
		values.clear();
		values.put("content", "Testing");
		MyContentProvider cp = getProvider();
		Uri ret = cp.insert(MyContentProvider.CONTENT_URI, values);
		int id = Integer.parseInt(ret.getLastPathSegment());
		assertTrue("created OK", id > -1);
		int id2 = Integer.parseInt(cp.insert(MyContentProvider.CONTENT_URI, values).getLastPathSegment());
		assertTrue("id's increment", id2 > id);
		String[] columns = {"content"};
		final Cursor queryResults = cp.query(MyContentProvider.CONTENT_URI, columns, CONTENT_EQ_TESTING, null, null);
		assertEquals(2, queryResults.getCount());
	}
	
	public void testDelete() {
		values.clear();
		values.put("content", "Testing");
		MyContentProvider cp = getProvider();
		Uri ret = cp.insert(MyContentProvider.CONTENT_URI, values);
		int id = Integer.parseInt(ret.getLastPathSegment());
		assertTrue("created OK", id > -1);
		final int deleted = cp.delete(MyContentProvider.CONTENT_URI, CONTENT_EQ_TESTING, null);
		assertEquals("deleted rows", 1, deleted);
	}
}
