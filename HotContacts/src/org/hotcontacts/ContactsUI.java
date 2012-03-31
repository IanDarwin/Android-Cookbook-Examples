package org.hotcontacts;

import java.util.ArrayList;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class ContactsUI extends Activity {
	
    private static final String ACCOUNT_NAME = "darwinian";

	private static String LOG_TAG;
    
    private AccountManager accountManager;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG_TAG = getString(R.string.app_name);
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	accountManager = AccountManager.get(this);
    	addContact();
    }
    
	private void addContact() {
		String name = "Jon Smith";
		String homePhone = "416-555-5555";
		String workPhone = "416-555-6666";
		String email = "jon@jonsmith.domain";

		// Use new-style batch operations: Build List of ops then call applyBatch
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			AuthenticatorDescription[] types = accountManager.getAuthenticatorTypes();
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.RawContacts.CONTENT_URI).withValue(
							ContactsContract.RawContacts.ACCOUNT_TYPE, types[0].type)
							.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, ACCOUNT_NAME)
							.build());
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name)
							.build());
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI).withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
									.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
											homePhone).withValue(
													ContactsContract.CommonDataKinds.Phone.TYPE,
													ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
													.build());
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI).withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
									.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
											workPhone).withValue(
													ContactsContract.CommonDataKinds.Phone.TYPE,
													ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
													.build());
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI).withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
									.withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
									.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
											ContactsContract.CommonDataKinds.Email.TYPE_HOME)
											.build());

			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			
			Toast.makeText(this, getString(R.string.addContactSuccess),
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			
			Toast.makeText(this, getString(R.string.addContactFailure),
					Toast.LENGTH_LONG).show();
			Log.e(LOG_TAG, getString(R.string.addContactFailure), e);
		}
	}
}