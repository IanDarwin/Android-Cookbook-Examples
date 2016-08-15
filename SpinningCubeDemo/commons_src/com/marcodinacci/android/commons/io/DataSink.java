package com.marcodinacci.android.commons.io;

public class DataSink<T> {

	private byte[] mData;
	private T mSink;
	
	public DataSink(byte[] data, T sink) {
		mData = data;
		mSink = sink;
	}
	
	public byte[] getData() { return mData; }
	public T getSink() { return mSink; }
	
	public void setData(byte[] data) { mData = data;}
	public void setSink(T sink) {mSink = sink;}
	
}
