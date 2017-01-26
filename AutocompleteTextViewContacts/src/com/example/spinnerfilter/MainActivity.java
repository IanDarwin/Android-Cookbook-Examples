package com.example.spinnerfilter;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {
	
	AutoCompleteTextView emailText;
	ContentResolver cr;
	SimpleCursorAdapter emailAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cr = getContentResolver();
		emailText = (AutoCompleteTextView) findViewById(R.id.emailAddress);
		String[] fromCols = {
				ContactsContract.Contacts.DISPLAY_NAME,
	            ContactsContract.CommonDataKinds.Email.DATA,
	    };
		int[] toViewIds = { R.id.list_name, R.id.list_email };
		emailAdapter = new SimpleCursorAdapter(this, R.layout.email_and_name, getNamesAndEmails(null), fromCols, toViewIds);
		
		// Important 1: You have to provide a way of making the chosen choice look presentable.
		// emailAdapter.setStringConversionColumn(1); // 1=DISPLAY_NAME, 2=Email
		emailAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
			@Override
			public CharSequence convertToString(Cursor cursor) {
				return String.format("%s <%s>", cursor.getString(1).trim(), cursor.getString(2).trim());
			}
		});

		// Important 2: You have to provide a query containing the values on demand
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
	
	final static String[] PROJECTION = new String[] { 
		ContactsContract.RawContacts._ID, 
		ContactsContract.Contacts.DISPLAY_NAME,
		ContactsContract.CommonDataKinds.Email.DATA, 
	};
	
	/** Get the contacts that have email addresses matching "partialName".
	 * @author Modified from code obtained from
	 * http://stackoverflow.com/questions/5205999/android-get-a-cursor-only-with-contacts-that-have-an-email-listed-android-2-0
	 * @return
	 */
	Cursor getNamesAndEmails(String partialName) {
		// Look for partialName either in display name (person name) or in email
		final String filter = 
				ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%" + partialName + "%'" +
				" OR " +
				ContactsContract.CommonDataKinds.Email.DATA + " LIKE '%" + partialName + "%'";
		// If display name contains "@" (maybe it's null so Contacts provides email here),
		// order by email, else order by display name.
		final String order = "CASE WHEN " 
				+ ContactsContract.Contacts.DISPLAY_NAME 
				+ " NOT LIKE '%@%' THEN 1 ELSE 2 END, " 
				+ ContactsContract.Contacts.DISPLAY_NAME 
				+ ", " 
				+ ContactsContract.CommonDataKinds.Email.DATA
				+ " COLLATE NOCASE";
		// Now make a Cursor containing the contacts that now match partialName as per "filter".
		return cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
	}

}
