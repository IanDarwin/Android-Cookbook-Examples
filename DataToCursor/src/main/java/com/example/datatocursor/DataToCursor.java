package com.example.datatocursor;

import android.database.AbstractCursor;
import android.database.Cursor;

/**
 * Provide a Cursor from a fixed list of data
 * column 1 - _id
 * column 2 - filename
 * column 3 - file type
 */
public class DataToCursor extends AbstractCursor {

	private static final String[] COLUMN_NAMES = {"_id", "filename", "type"};

	private static final String[] DATA_ROWS = {
			"one.mpg",
			"two.jpg",
			"tre.dat",
			"fou.git",
	};

	@Override
	public int getCount() {
		return DATA_ROWS.length;
	}
	
	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String[] getColumnNames() {
		return COLUMN_NAMES;
	}

	@Override
	public int getType(int column) {
		switch(column) {
		case 0:
			return Cursor.FIELD_TYPE_INTEGER;
		case 1:
		case 2:
			return Cursor.FIELD_TYPE_STRING;
		default: throw new IllegalArgumentException(Integer.toString(column));
		}
	}

	/** 
	 * Return the _id value (the only integer-valued column).
	 * Conveniently, rows and array indices are both 0-based.
	 */
	@Override
	public int getInt(int column) {
		int row = getPosition();
		switch(column) {
		case 0: return row;
		default: throw new IllegalArgumentException(Integer.toString(column));
		}
	}
	
	/** SQLite _ids are actually long, so make this work as well.
	 * This direct equivalence is usually not applicable; do not blindly copy.
	 */
	@Override
	public long getLong(int column) {
		return getInt(column);
	}

	@Override
	public String getString(int column) {
		int row = getPosition();
		switch(column) {
		case 1: return DATA_ROWS[row];
		case 2: return extension(DATA_ROWS[row]);
		default: throw new IllegalArgumentException(Integer.toString(column));
		}
	}

	/** Stub version, works for sample filenames like only */
	private String extension(String path) {
		return path.substring(4);
	}

	// Remaining get*() methods call this as there are no other column types
	private char dieBadColumn() {
		throw new IllegalArgumentException("No columns of this type");
	}

	@Override
	public short getShort(int column) {
		return (short) dieBadColumn();
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
