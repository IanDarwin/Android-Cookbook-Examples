package com.example.dragdropdemo;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.Button;
import android.widget.Toast;

public class DragDropActivity extends Activity {
	private static final String TAG = DragDropActivity.class.getSimpleName();
	private View target;
	private final static int TARGET_NORMAL = Color.WHITE,
			TARGET_DRAGGING = Color.YELLOW,
			TARGET_ALERT = Color.RED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_drop);
		
		// Register the long-click listener to START the drag
		Button b = (Button) findViewById(R.id.button);
		b.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Uri contentUri = Uri.parse("http://oracle.com/java/");
				ClipData cd = ClipData.newUri(getContentResolver(), "Dragging", contentUri); 
				v.startDrag(cd, new DragShadowBuilder(v), null, 0);
				return true;
			}
		});
		
		target = findViewById(R.id.drop_target);
		target.setBackgroundColor(TARGET_NORMAL);
		target.setOnDragListener(new MyDrag());
	}
	
	public class MyDrag implements View.OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent e) {
			switch (e.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				target.setBackgroundColor(TARGET_DRAGGING);
				return true;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d(TAG, "onDrag: ENTERED e=" + e);
				target.setBackgroundColor(TARGET_ALERT);
				return true;
			case DragEvent.ACTION_DRAG_LOCATION:
				// Nothing to do but MUST consume the event
				return true;
			case DragEvent.ACTION_DROP:
				Log.d(TAG, "onDrag: DROP e=" + e);
				final ClipData clipItem = e.getClipData();
				Toast.makeText(DragDropActivity.this, 
					"DROPPED: " + clipItem.getItemAt(0).getUri(),
					Toast.LENGTH_LONG).show();
				return true;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d(TAG, "onDrag: EXITED e=" + e);
				target.setBackgroundColor(TARGET_NORMAL);
				return true;
			case DragEvent.ACTION_DRAG_ENDED:
				target.setBackgroundColor(TARGET_NORMAL);
				return true;
			default:			// Un-handled event type
				return false;
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drag_drop, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
