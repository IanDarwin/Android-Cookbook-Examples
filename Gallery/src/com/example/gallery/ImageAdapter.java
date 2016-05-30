package com.example.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageAdapter extends BaseAdapter {
	private Context _context = null;
	private final int[] imageIds = { 
			R.drawable.earthmover1,
			R.drawable.grain,
			R.drawable.palm_tree_backlit,
			R.drawable.red_green_macaw,
			R.drawable.september_sunset,
			R.drawable.vancouver_narrows_bridge,
		};

	public ImageAdapter(Context context) {
		this._context = context;
	}

	@Override
	public int getCount() {
		return imageIds.length;
	}

	@Override
	public Object getItem(int index) {
		return imageIds[index];
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int postion, View view, ViewGroup group) {
		ImageView imageView = new ImageView(_context);
		imageView.setImageResource(imageIds[postion]);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setLayoutParams(new Gallery.LayoutParams(400, 400));
		return imageView;
	}
}
