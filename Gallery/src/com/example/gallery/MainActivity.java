package com.example.gallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	private static final String tag = "Main";
	private Gallery _gallery;
	private ImageAdapter _imageAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Android Photo Gallery Example");
		_gallery = (Gallery) this.findViewById(R.id.gallery1);
		_imageAdapter = new ImageAdapter(this);
		_gallery.setAdapter(_imageAdapter);
		_gallery.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long duration) {
		int resourcId = (Integer) _imageAdapter.getItem(position);
		// Drawable drawable = getResources().getDrawable(resourcId);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourcId);
		Toast.makeText(this,
				"Selected Image: " + getResources().getText(resourcId) + "\n"
						+ "Height: " + bitmap.getHeight() + "\n" + "Width: "
						+ bitmap.getWidth(), Toast.LENGTH_SHORT).show();
	}
}
