package com.listview;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String[] data = new String[0];	// empty list!
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_list_item_1, data);
        final ListView myList = (ListView) findViewById(R.id.myList);
        myList.setAdapter(adapter);
        myList.setFastScrollEnabled(true);
        myList.setOverScrollMode(AbsListView.OVER_SCROLL_IF_CONTENT_SCROLLS);
        myList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, 
					int pos, long id) {
				Toast.makeText(Main.this, 
						"You picked " + ((TextView)view).getText(), 
						Toast.LENGTH_LONG).show();
			}
		});
        myList.setTextFilterEnabled(true);
        Drawable d = new GradientDrawable(
        		GradientDrawable.Orientation.BOTTOM_TOP,
        		new int[]{0x00ff00, 0x008888, 0x0000ff});
        myList.setOverscrollHeader(d);
    }
}