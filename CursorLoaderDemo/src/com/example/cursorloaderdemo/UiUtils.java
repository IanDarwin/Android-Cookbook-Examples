package com.example.cursorloaderdemo;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;

/** Random assortment of utility methods for the UI
 * @author Ian Darwin
 */
public class UiUtils {
	
	// MISC UI HELPERS

	/**
	 * Extract a number from a TextField.
	 * @param activity
	 * @param viewId
	 * @return
	 */
	public static int getNumberFromView(Activity activity, int viewId) {
		final EditText editText = (EditText) activity.findViewById(viewId);
		if (editText == null) {
			throw new IllegalArgumentException(String.format("Can not find view for 0x%x", viewId));
		}
		return getNumberFromView(activity, editText);
	}
	
	public static int getNumberFromView(Activity activity, EditText editText) {		
		String numberString = editText.getText().toString();
		if (numberString.isEmpty()) {
			return -1;
		}
		try {
			return Integer.parseInt(numberString);
		} catch (NumberFormatException e) {
			Toast.makeText(activity, activity.getString(R.string.not_a_number) + numberString, Toast.LENGTH_SHORT).show();
			return -1;
		}
	}
	
}
