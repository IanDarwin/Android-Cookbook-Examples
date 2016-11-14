package com.pfizer.android;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Demonstrate using Rome API to parse an RSS feed
 * @author Wagied Davies, original version
 * @author Ian Darwin, code reorganized to use an AsyncTask instead of
 * throwing NetworkOnMainThreadException as the original did.
 */
public class AndroidRss extends Activity {
	private EditText text;
	private ListView listView;
	private Button goButton;
	private Button clearButton;
	private ArrayAdapter<String> adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		text = (EditText) this.findViewById(R.id.rssURL);
		goButton = (Button) this.findViewById(R.id.goButton);
		goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String rss = text.getText().toString().trim();
				new RssGetter().execute(rss);
			}
		});

		clearButton = (Button) this.findViewById(R.id.clearButton);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.clear();
				adapter.notifyDataSetChanged();
			}
		});

		listView = (ListView) this.findViewById(R.id.ListView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long duration) {
				Toast.makeText(AndroidRss.this,
					"Selected " + adapter.getItem(position) + " @ "
								+ position, Toast.LENGTH_SHORT).show();
			}
		});

		adapter = new ArrayAdapter<String>(this, R.layout.dataview,
				R.id.ListItemView);
		listView.setAdapter(adapter);
	}

	/**
	 * The AsyncTask to do the network IO on a background thread
	 * and the UI updating on, well, the UI thread.
	 */
	private class RssGetter extends AsyncTask<String, Void, List<SyndEntry>> {
	
		@Override
		public List<SyndEntry> doInBackground(String... rss) {

			URL feedUrl;
			try {
				Log.d("DEBUG", "Entered:" + rss);
				feedUrl = new URL(rss[0]);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid URL, try again");
			}

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed;
			try {
				feed = input.build(new XmlReader(feedUrl));
				@SuppressWarnings("unchecked")
				List<SyndEntry> entries = feed.getEntries();
				Toast.makeText(AndroidRss.this, "#Feeds retrieved: " + entries.size(),
						Toast.LENGTH_SHORT).show();
				return entries;
			} catch (Exception e) {
				throw new RuntimeException("Feeding failed: " + e);
			}
		}

		@Override
		public void onPostExecute(List<SyndEntry> entries) {
			for (SyndEntry ent : entries) {
				String title = ent.getTitle();
				adapter.add(title);
			}
			adapter.notifyDataSetChanged();
		}
	};
}
