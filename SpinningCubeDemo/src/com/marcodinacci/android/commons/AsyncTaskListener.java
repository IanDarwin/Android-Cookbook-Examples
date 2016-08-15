package com.marcodinacci.android.commons;

public interface AsyncTaskListener<T,S> {
	public void onProgressUpdate(T... values);
	public void onPostExecute(S...result);
}
