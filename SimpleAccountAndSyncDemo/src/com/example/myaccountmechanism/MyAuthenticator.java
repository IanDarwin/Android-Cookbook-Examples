/*
 * Boilerplate portions copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.myaccountmechanism;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyAuthenticator extends AbstractAccountAuthenticator {
	
	private static final String TAG = MainActivity.TAG;
	private final Context mContext;

	public MyAuthenticator(Context context) {
		super(context);
		this.mContext = context;
		Log.d(TAG, "MyAuthenticator.MyAuthenticator()");
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		Log.d(TAG, "MyAuthenticator.addAccount()");
		final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		Log.d(TAG, "MyAuthenticator.confirmCredentials()");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		Log.d(TAG, "MyAuthenticator.getAuthToken()");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		Log.d(TAG, "MyAuthenticator.getAuthTokenLabel()");
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		Log.d(TAG, "MyAuthenticator.hasFeatures()");
		// No, actually, we don't have that feature." We ain't got no feechures...
		final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);  
        return result;
	}
	
	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		Log.d(TAG, "MyAuthenticator.editProperties()");
		// Should never get here
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		Log.d(TAG, "MyAuthenticator.updateCredentials()");
		return null;
	}
}
