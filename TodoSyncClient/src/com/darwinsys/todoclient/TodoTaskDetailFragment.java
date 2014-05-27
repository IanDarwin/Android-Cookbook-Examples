package com.darwinsys.todoclient;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darwinsys.todo.model.Task;
import com.darwinsys.todocontent.TaskUtils;
import com.darwinsys.todocontent.TodoContentProvider;

/**
 * A fragment representing a single Todo Task detail screen. This fragment is
 * either contained in a {@link TodoTaskListActivity} in two-pane mode (on
 * tablets) or a {@link TodoTaskDetailActivity} on handsets.
 */
public class TodoTaskDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The data content this fragment is presenting.
	 */
	private Task mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TodoTaskDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the data item; should really use a Loader
			// to load content from a content provider.
			Cursor cur = getActivity().getContentResolver().query(
				TodoContentProvider.CONTENT_URI, null, 
				"_id = ?", new String[]{getArguments().getString(ARG_ITEM_ID)}, null);
			mItem = TaskUtils.cursorToTask(cur);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_todotask_detail,
				container, false);

		// Show the Task as text in a TextView; 
		// XXX make a proper display with all fields
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.todotask_detail))
					.setText(mItem.toString());
		}

		return rootView;
	}
}
