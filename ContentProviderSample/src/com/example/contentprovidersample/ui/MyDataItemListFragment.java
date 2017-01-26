package com.example.contentprovidersample.ui;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import com.example.contentprovidersample.MyContentProvider;
import com.example.contentprovidersample.R;

/**
 * A list fragment representing a list of Data Items. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link MyDataItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MyDataItemListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private static final int URL_LOADER = 0;

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private Uri mDataUrl = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, "/items");
	
	public String[] mFromColumns = {
			MyContentProvider.COLUMNS[1]
	};
	public int[] mToFields = {
			android.R.id.text1
	};
	
	SimpleCursorAdapter mAdapter;
	
	
	// Get the List View
	ListView mListView;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(long id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(long id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MyDataItemListFragment() {
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mListView = (ListView) getActivity().findViewById(R.id.mydataitem_list);
		
		mAdapter = 
			    new SimpleCursorAdapter(
			            getActivity(),                // Current context
			            android.R.layout.simple_list_item_activated_1,  // Layout for a single row
			            null,                // No Cursor yet
			            mFromColumns,        // Cursor columns to use
			            mToFields,           // Layout fields to use
			            0                    // No flags
			    );
		
		/*
         * Initializes the CursorLoader. The URL_LOADER value is eventually passed
         * to onCreateLoader().
         */
        getLoaderManager().initLoader(URL_LOADER, (Bundle)null, (LoaderCallbacks<Cursor>) this);

		setListAdapter(mAdapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		
		mCallbacks.onItemSelected(id);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	// Methods for CursorLoader
	
	/**
	 * Callback that's invoked when the system has initialized the Loader and
	 * is ready to start the query. This usually happens when initLoader() is
	 * called. The loaderID argument contains the ID value passed to the
	 * initLoader() call.
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		/*
	     * Takes action based on the ID of the Loader that's being created
	     */
	    switch (loaderId) {
	        case URL_LOADER:
	            // Returns a new CursorLoader
	            return new CursorLoader(
	                        getActivity(),   // Parent activity context
	                        mDataUrl,        // What to query
	                        MyContentProvider.COLUMNS,	// Projection to return
	                        null,			// No selection clause
	                        null,			// No selection arguments
	                        "_id asc"		// Sort order
	            		);
	        default:
	            // An invalid LoaderId id was passed in
	            return null;
	    }
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		/*
	     * Moves the query results into the adapter, causing the
	     * ListView fronting this adapter to re-display
	     */
	    mAdapter.changeCursor(cursor);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/*
	     * Clears out the adapter's reference to the Cursor.
	     * This prevents memory leaks.
	     */
	    mAdapter.changeCursor(null);
	}


}
