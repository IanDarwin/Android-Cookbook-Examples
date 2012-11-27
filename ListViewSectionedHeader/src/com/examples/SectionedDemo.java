package com.examples;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SectionedDemo extends ListActivity {
    private static String[] items = { "Muscle Stiffness", "Vision Problems",
	    "Weaknesss", "Depression" };

    @Override
    public void onCreate(Bundle icicle) {
	super.onCreate(icicle);
	setContentView(R.layout.main);

	adapter.addSection("Wed Nov 3, 2010", new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, items));

	List<String> list = Arrays.asList(items);

	Collections.shuffle(list);

	adapter.addSection("Mon Nov 1, 2010", new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, list));

	list = Arrays.asList(items);

	Collections.shuffle(list);

	adapter.addSection("Tues  Oct, 31, 2010", new ArrayAdapter<String>(
		this, android.R.layout.simple_list_item_1, list));

	setListAdapter(adapter);
    }

    SectionedAdapter adapter = new SectionedAdapter() {
	@Override
	protected View getHeaderView(String caption, int index,
		View convertView, ViewGroup parent) {
	    TextView result = (TextView) convertView;

	    if (convertView == null) {
		result = (TextView) getLayoutInflater().inflate(
			R.layout.section_header, null);
	    }

	    result.setText(caption);

	    return (result);
	}
    };
}
