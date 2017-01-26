package com.example.contentprovidersample.test;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.example.contentprovidersample.MyContentProvider;

public class MyContentProviderTest extends ProviderTestCase2<MyContentProvider> {

	private static final String TEST_VALUE_COMMENT = "Testing";
	private static final String CONTENT_EQ_TESTING = "content = '" + TEST_VALUE_COMMENT + "'";
	private static final String[] contentColumns = {"content"};
	final static ContentValues values = new ContentValues();

	public MyContentProviderTest() {
		super(MyContentProvider.class, MyContentProvider.AUTHORITY);
	}
	
	public void testGetTypeSingle() {
		Uri uriItemSingle = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, "items/5");
		ContentProvider cp = getProvider();
		assertEquals("cp getType single", 
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MyContentProvider.MIME_VND_TYPE,
				cp.getType(uriItemSingle));
	}
	
	public void testGetTypeMulti() {
		Uri uriItems = MyContentProvider.ITEMS_URI;
		ContentProvider cp = getProvider();
		assertEquals("cp getType multi", 
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MyContentProvider.MIME_VND_TYPE,
				cp.getType(uriItems));
	}
	
	public void testGetTypeInvalid() {
		Uri einval = Uri.parse("geo:49.0000,49.000");
		ContentProvider cp = getProvider();
		try {
			cp.getType(einval);
			fail("Did not get expected RuntimeException");
		} catch (RuntimeException expected) {
			// empty
		}
	}
	
	public void testInsertAndQuery() {
		values.clear();
		values.put("content", "Testing");
		ContentProvider cp = getProvider();
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
		ContentProvider cp = getProvider();
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
		ContentProvider cp = getProvider();
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
		ContentProvider cp = getProvider();
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
		ContentProvider cp = getProvider();
		long id = ContentUris.parseId(cp.insert(MyContentProvider.ITEMS_URI, values));
		assertTrue("created OK", id > -1);
		final int deleted = cp.delete(ContentUris.withAppendedId(MyContentProvider.ITEMS_URI, id), null, null);
		assertEquals("deleted rows", 1, deleted);
	}
	
	public void testDeleteByWhere() {
		values.clear();
		values.put("content", "Testing");
		ContentProvider cp = getProvider();
		long id = ContentUris.parseId(cp.insert(MyContentProvider.ITEMS_URI, values));
		assertTrue("created OK", id > -1);
		final int deleted = cp.delete(MyContentProvider.ITEMS_URI, CONTENT_EQ_TESTING, null);
		assertEquals("deleted rows", 1, deleted);
	}
}
