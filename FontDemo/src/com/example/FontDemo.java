package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class FontDemo extends Activity {
	private static final int DLG_FONTFAILED = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView vR = (TextView) findViewById(R.id.FontViewR);	// 1
		TextView vBI = (TextView) findViewById(R.id.FontViewBI);
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(getAssets(),	// 2
					"fonts/Montserrat-Regular.ttf");
		} catch (RuntimeException e) {
			showDialog(DLG_FONTFAILED);
		}
		vR.setText("Montserrat Regular");
		vR.setTypeface(tf, Typeface.NORMAL);	
		vBI.setText("Montserrat Bold Italic");
		vBI.setTypeface(tf, Typeface.BOLD_ITALIC);			// 3
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
