package com.example.spinnerfilter;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {
	
	AutoCompleteTextView emailText;
	Cursor cursor;
	SimpleCursorAdapter emailAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		emailText = (AutoCompleteTextView) findViewById(R.id.emailAddress);
		String[] fromCols = {
				ContactsContract.Contacts.DISPLAY_NAME,
	            ContactsContract.CommonDataKinds.Email.DATA,
	    };
		int[] toViewIds = { R.id.list_name, R.id.list_email };
		emailAdapter = new SimpleCursorAdapter(this, R.layout.email_and_name, cursor = getNamesAndEmails(null), fromCols, toViewIds);
		
		// important difference 1
		emailAdapter.setStringConversionColumn(2);
		// Or use setCursorToStringConverter(SimpleCursorAdapter.CursorToStringConverter)?

		// important difference 2
		emailAdapter.setFilterQueryProvider(new FilterQueryProvider() {

		        public Cursor runQuery(CharSequence constraint) {
		            String partialItemName = null;
		            if (constraint != null) {
		                partialItemName = constraint.toString();
		            }
		            return getNamesAndEmails(partialItemName);
		        }
		    });

		emailText.setAdapter(emailAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Get the contacts that have emails.
	 * @author http://stackoverflow.com/questions/5205999/android-get-a-cursor-only-with-contacts-that-have-an-email-listed-android-2-0
	 * @return
	 */
	Cursor getNamesAndEmails(String partialName) {
		   ContentResolver cr = getContentResolver();
		    String[] PROJECTION = new String[] { 
		    		ContactsContract.RawContacts._ID, 
		            ContactsContract.Contacts.DISPLAY_NAME,
		            ContactsContract.CommonDataKinds.Email.DATA, 
		    };
		    String order = "CASE WHEN " 
		            + ContactsContract.Contacts.DISPLAY_NAME 
		            + " NOT LIKE '%@%' THEN 1 ELSE 2 END, " 
		            + ContactsContract.Contacts.DISPLAY_NAME 
		            + ", " 
		            + ContactsContract.CommonDataKinds.Email.DATA
		            + " COLLATE NOCASE";
		    String filter = ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%" + partialName + "%'" +
		            " OR " +
		    		ContactsContract.CommonDataKinds.Email.DATA + " LIKE '%" + partialName + "%'";
		    Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
		    return cur;
	}

}
