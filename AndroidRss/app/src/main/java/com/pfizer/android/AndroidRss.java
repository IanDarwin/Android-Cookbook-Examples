package com.pfizer.android;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
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

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

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
				getRSS(rss);
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
				Toast.makeText(
						getApplicationContext(),
						"Selected " + adapter.getItem(position) + " @ "
								+ position, Toast.LENGTH_SHORT).show();
			}
		});

		adapter = new ArrayAdapter<String>(this, R.layout.dataview,
				R.id.ListItemView);
		listView.setAdapter(adapter);

	}

	private void getRSS(String rss) {

		URL feedUrl;
		try {
			Log.d("DEBUG", "Entered:" + rss);
			feedUrl = new URL(rss);

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			List<SyndEntry> entries = feed.getEntries();
			Toast.makeText(this, "#Feeds retrieved: " + entries.size(),
					Toast.LENGTH_SHORT).show();

			Iterator<SyndEntry> iterator = entries.listIterator();
			while (iterator.hasNext()) {
				SyndEntry ent = iterator.next();
				String title = ent.getTitle();
				adapter.add(title);
			}
			adapter.notifyDataSetChanged();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void clearTextFields() {
		Log.d("DEBUG", "clearTextFields()");
		this.text.setText("");
	}
}
