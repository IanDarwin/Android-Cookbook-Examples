package com.marcodinacci.android.commons.io;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.marcodinacci.android.commons.AsyncTaskListener;

/**
 * Write to a stream asynchronously.
 * 
 * @author Marco Dinacci <marco.dinacci@gmail.com>
 */
@SuppressWarnings("rawtypes")
public class DataWriter {

	private static final String TAG = "DataWriter";

	@SuppressWarnings({ "unchecked" })
	// FIXME fix the listener parameter
	public void writeAsync(AsyncTaskListener<Integer, Long> listener, 
			DataSink ds, boolean flushOnWrite, boolean closeOnWrite) {
		
		List<AsyncTaskListener> listeners = new ArrayList<AsyncTaskListener>(1);
		listeners.add(listener);
		
		Log.d(TAG, "Writing asynchronously to " + ds.getSink().toString());
		new StreamDataTask((AsyncTaskListener<Integer, Long>[]) 
				listeners.toArray(),flushOnWrite, closeOnWrite).execute(ds);
	}
	
	public void writeAsync(DataSink ds) {
		writeAsync(null, ds, true, true);
	}
}
