package com.example.myaccountmechanism;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;

import android.accounts.Account;

public class RestUtils {
	
	/**
	 * Check the username and password; the password may be a 4-12 character password or it may be an MD5 hash
	 * @param username - the username
	 * @param password - the password
	 * @return an Auth Token indicating that the user is signed in (present in place of password in future!),
	 * or null if something went wrong.
	 */
	public static String checkAuth(String username, String password) {
		return null;
	}
	
	/**
	 * Run one synchronization operation.
	 */
	public static List<Object> syncContacts(
            Account account, String authtoken, long serverSyncState, List<Object> dirtyContacts)
            throws JSONException, ParseException, IOException, AuthenticationException {
		return null;
	}

}
