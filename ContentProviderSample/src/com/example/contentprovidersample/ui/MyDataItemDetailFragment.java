package com.example.contentprovidersample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			// mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID)); // XXX
			mItem = new MyDataItem(1, "Hello");
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
