package com.marcodinacci.android.commons;

import android.util.Log;


/**
 * Generic class for (simple) image processing. 
 * It doesn't subclass Bitmap in android.graphics as it's declared final.
 * 
 * TODO refactor: create various classes, one for each PixelFormat and implement
 * the abstract operations defined here instead of having an ugly switch case
 * 
 */
public abstract class AbstractAndroidImage implements AndroidImage {
	
	private final String TAG = "AbstractAndroidImage"; //this.getClass().getName();
	protected byte[] mData;
	protected Size<Integer, Integer> mSize;

	public AbstractAndroidImage(byte[] data, Size<Integer, Integer> size) {
		mData = data;
		mSize = size;
	}

	// TODO throw exception and log every problem encountered
	protected boolean assertImage(AndroidImage other) {
		boolean result = true;
		
		byte[] otherData = other.get();
		
		if(mData.length != otherData.length) {
			Log.e(TAG, "Data length between images to compare is different");
			// data length must be the same
			result = false;
		}
		/*
		if(other.getClass() == this.getClass()) {
			Log.e(TAG, "Cannot compare two different implementations: " + 
					getClass().getName() + " and " + other.getClass().getName());
			result = false;
		}*/
		
		otherData = null;
		
		Log.d(TAG, "Images are compatible: " + result);
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.marcodinacci.android.commons.AndroidImage#toGrayScale()
	 */
	@Override
	public abstract AndroidImage toGrayscale();

	/* (non-Javadoc)
	 * @see com.marcodinacci.android.commons.AndroidImage#erode(int)
	 * TODO implement. Use a kernel instead of an int or use the int as a 
	 * manhattan distance
	 */
	@Override
	public AndroidImage erode(int erosionLevel) {
		return this;
	}

	/* (non-Javadoc)
	 * @see com.marcodinacci.android.commons.AndroidImage#morph(byte[], int)
	 */
	@Override
	public AndroidImage morph(AndroidImage other, int value) {
		Log.v(TAG, "Beginning morph operation with value: " + value);
		
		byte[] otherData = other.get();

		assert value <= 100 && value >= 0;
		
		if(value == 0) return this;
		
		float thisValue = (100 - value) / 100; 
		float otherValue = value / 100;
		
		for (int i = 0; i < mData.length; i++) {
			mData[i] = (byte) Math.round((mData[i] * thisValue) + (otherData[i] * otherValue));
		}
		
		return this;
	}
	
	@Override
	public byte[] get() {
		return mData;
	}
}
