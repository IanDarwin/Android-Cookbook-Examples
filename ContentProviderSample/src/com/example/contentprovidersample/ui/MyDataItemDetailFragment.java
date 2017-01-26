package com.example.contentprovidersample.ui;

import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contentprovidersample.MyContentProvider;
import com.example.contentprovidersample.MyDataItem;
import com.example.contentprovidersample.R;

/**
 * A fragment representing a single MyDataitem detail screen. This fragment is
 * either contained in a {@link MyDataItemListActivity} in two-pane mode (on
 * tablets) or a {@link MyDataItemDetailActivity} on handsets.
 */
public class MyDataItemDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	
	/**
	 * The dummy content this fragment is presenting.
	 */
	private MyDataItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MyDataItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		if (arguments.containsKey(ARG_ITEM_ID)) {
			long id = arguments.getLong(ARG_ITEM_ID);
			final Cursor ret = getActivity().getContentResolver().query(ContentUris.withAppendedId(MyContentProvider.ITEMS_URI, id), null, null, null, null);
			ret.moveToFirst();
			long _id = ret.getLong(0);
			String text = ret.getString(1);
			mItem = new MyDataItem((int)_id, text);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mydataitem_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.mydataitem_detail))
					.setText(mItem.content);
		}

		return rootView;
	}
}
