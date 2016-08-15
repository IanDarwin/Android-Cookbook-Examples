package com.marcodinacci.android.commons;

import java.io.File;

public class DataFile {

	private byte[] mData;
	
	private File mFile;

	public DataFile(byte[] data, File file) {
		mData = data;
		mFile = file;
	}
	
	public DataFile() {
		this(null,null);
	}

	public byte[] getData() { return mData; }
	public File getFile() { return mFile; }
	
	public void setData(byte[] data) { mData = data;}
	public void setFile(File file) {mFile = file;}
	
}
