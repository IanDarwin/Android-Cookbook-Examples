package com.example.honeycombgallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageAdapter extends BaseAdapter {
	private Context _context = null;
	private final int[] imageIds = { R.drawable.formula, R.drawable.hollywood,
			R.drawable.mode1, R.drawable.mode2, R.drawable.mother1,
			R.drawable.mother2, R.drawable.nights, R.drawable.ontwerpje1,
			R.drawable.ontwerpje2, R.drawable.relation1, R.drawable.relation2,
			R.drawable.renaissance, R.drawable.renaissance_zoom };

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
