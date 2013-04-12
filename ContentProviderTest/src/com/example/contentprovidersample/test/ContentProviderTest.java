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
	private static final String[] contentColumns = {"content"};
	final static ContentValues values = new ContentValues();

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
		long id = ContentUris.parseId(cp.insert(MyContentProvider.ITEMS_URI, values));
		assertTrue("created OK", id > -1);
		long id2 = ContentUris.parseId(cp.insert(MyContentProvider.ITEMS_URI, values));
		assertTrue("id's increment", id2 > id);
		final Cursor queryResults = cp.query(MyContentProvider.ITEMS_URI, contentColumns, CONTENT_EQ_TESTING, null, null);
		assertEquals(2, queryResults.getCount());
	}
	
	public void testUpdateSingleViaUri() {
		values.clear();
		values.put("content", "Testing");
		MyContentProvider cp = getProvider();
		long id = ContentUris.parseId(cp.insert(MyContentProvider.ITEMS_URI, values));
		assertTrue("row for update test created OK", id > -1);
		values.put("content", "Not testing!");
		Uri updateUri = ContentUris.withAppendedId(MyContentProvider.ITEMS_URI, id);
		int rc = cp.update(updateUri, values, null, null);
		assertEquals("update count", 1, rc);		
	}
	
	public void testUpdateMultipleWithWhere() {
		values.clear();
		values.put("content", "Testing");
		MyContentProvider cp = getProvider();
		long id = ContentUris.parseId(cp.insert(MyContentProvider.ITEMS_URI, values));
		assertTrue("row for update test created OK", id > -1);
		values.put("content", "Not testing!");
		Uri updateUri = MyContentProvider.ITEMS_URI;
		int rc = cp.update(updateUri, values, CONTENT_EQ_TESTING, null);
		assertEquals("update count", 1, rc);		
	}
	
	/** This test has a dependency on the Sample Content Provider's createDB logic,
	 * "knowing" that it pre-creates three rows.
	 */
	public void testUpdateMultipleWithoutWhere() {
		values.clear();
		MyContentProvider cp = getProvider();
		values.put("content", "Not testing!");
		Uri updateUri = MyContentProvider.ITEMS_URI;
		int rc = cp.update(updateUri, values, null, null);
		assertEquals("update count", 3, rc);
		Cursor c = cp.query(MyContentProvider.ITEMS_URI, contentColumns, "content = 'Not testing!'", null, null);
		assertEquals("Query the updated rows", 3, c.getCount());
	}
	
	public void testDeleteById() {
		values.clear();
		values.put("content", "Testing");
		MyContentProvider cp = getProvider();
		long id = ContentUris.parseId(cp.insert(MyContentProvider.ITEMS_URI, values));
		assertTrue("created OK", id > -1);
		final int deleted = cp.delete(ContentUris.withAppendedId(MyContentProvider.ITEMS_URI, id), null, null);
		assertEquals("deleted rows", 1, deleted);
	}
	
	public void testDeleteByWhere() {
		values.clear();
		values.put("content", "Testing");
		MyContentProvider cp = getProvider();
		long id = ContentUris.parseId(cp.insert(MyContentProvider.ITEMS_URI, values));
		assertTrue("created OK", id > -1);
		final int deleted = cp.delete(MyContentProvider.ITEMS_URI, CONTENT_EQ_TESTING, null);
		assertEquals("deleted rows", 1, deleted);
	}
}
