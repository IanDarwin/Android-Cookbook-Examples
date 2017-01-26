package com.marcodinacci.demos.alv;

/**
 * This file is part of AdvancedListViewDemo.
 * You should have downloaded this file from www.intransitione.com, if not, 
 * please inform me by writing an e-mail at the address below:
 *
 * Copyright [2011] [Marco Dinacci <marco.dinacci@gmail.com>]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.

 * The license text is available online and in the LICENSE file accompanying the distribution
 * of this program.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAndTextAdapter extends ArrayAdapter<String> {

	private LayoutInflater mInflater;
	
	private String[] mStrings;
	private TypedArray mIcons;
	
	private int mViewResourceId;
	
	public ImageAndTextAdapter(Context ctx, int viewResourceId,
			String[] strings, TypedArray icons) {
		super(ctx, viewResourceId, strings);
		
		mInflater = (LayoutInflater)ctx.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		
		mStrings = strings;
		mIcons = icons;
		
		mViewResourceId = viewResourceId;
	}

	@Override
	public int getCount() {
		return mStrings.length;
	}

	@Override
	public String getItem(int position) {
		return mStrings[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(mViewResourceId, null);
		
		ImageView iv = (ImageView)convertView.findViewById(R.id.option_icon);
		iv.setImageDrawable(mIcons.getDrawable(position));

		TextView tv = (TextView)convertView.findViewById(R.id.option_text);
		tv.setText(mStrings[position]);
		
		return convertView;
	}
}
