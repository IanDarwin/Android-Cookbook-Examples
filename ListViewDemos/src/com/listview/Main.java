package com.listview;

import android.app.ListActivity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends ListActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String[] data = getResources().getStringArray(R.array.foodstuffs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
        final ListView myList = getListView();
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
        
        View orderZone = findViewById(R.id.orderZone);
        orderZone.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(Main.this, 
						"Your order will be ready soon", 
						Toast.LENGTH_LONG).show();
			}
		});
        OnLongClickListener longClickListener = new OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				PopupMenu p = new PopupMenu(Main.this, view);
				p.getMenuInflater().inflate(R.menu.main_popup_menu, p.getMenu());
				p.show();
				return true;
			}        	
        };
		myList.setOnLongClickListener(longClickListener);
		orderZone.setOnLongClickListener(longClickListener);
    }
}