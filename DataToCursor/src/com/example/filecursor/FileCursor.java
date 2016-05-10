package com.example.filecursor;

import android.database.AbstractCursor;

/**
 * Provide a Cursor from a fixed list of files
 * column 1 - filename
 * column 2 - file type
 */
public class FileCursor extends AbstractCursor {

	String[] files = {
			"one.mpg",
			"two.jpg"
	};

	@Override
	public int getCount() {
		return files.length;
	}

	@Override
	public String[] getColumnNames() {
		return new String[]{"filename", "type"};
	}

	@Override
	public String getString(int column) {
		switch(column) {
		case 0: return files[column];
		case 1: return files[column].substring(4);
		default: throw new IllegalArgumentException(Integer.toString(column));
		}
	}

	@Override
	public short getShort(int column) {
		return (short) dieBadColumn();
	}

	private char dieBadColumn() {
		throw new IllegalArgumentException("No columns of this type");
	}

	@Override
	public int getInt(int column) {
		return dieBadColumn();
	}

	@Override
	public long getLong(int column) {
		return dieBadColumn();
	}

	@Override
	public float getFloat(int column) {
		return dieBadColumn();
	}

	@Override
	public double getDouble(int column) {
		return dieBadColumn();
	}

	@Override
	public boolean isNull(int column) {
		return false;
	}

}
