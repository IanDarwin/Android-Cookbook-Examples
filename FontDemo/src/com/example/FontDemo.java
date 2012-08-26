package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class FontDemo extends Activity {
	private static final int DLG_FONTFAILED = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView v = (TextView) findViewById(R.id.FontView);	// 1
		Typeface t = null;
		try {
			t = Typeface.createFromAsset(getAssets(),			// 2
					"fonts/fontdemo.ttf");
		} catch (RuntimeException e) {
			showDialog(DLG_FONTFAILED);		}
		v.setTypeface(t, Typeface.BOLD_ITALIC);					// 3
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		
		switch (id) {
		case DLG_FONTFAILED:
			return new AlertDialog.Builder(this)
			.setCancelable(false)
			.setTitle(R.string.font_fail_title)
			.setMessage(R.string.font_fail_text)
			.setNeutralButton("OK",
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							System.exit(1);
						}
					}).create();
		default:
			return super.onCreateDialog(id, args);
		}
	}
}
