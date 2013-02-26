package com.example.checkboxradiobutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SelectExample extends Activity {
	private CheckBox checkBox;
	private TextView txtCheckBox, txtRadio;
	private RadioButton rb1, rb2, rb3;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_example);
		checkBox = (CheckBox) findViewById(R.id.cbxBox1);
		txtCheckBox = (TextView) findViewById(R.id.txtCheckBox);
		txtRadio = (TextView) findViewById(R.id.txtRadio);
		rb1 = (RadioButton) findViewById(R.id.RB1);
		rb2 = (RadioButton) findViewById(R.id.RB2);
		rb3 = (RadioButton) findViewById(R.id.RB3);
		
		// React to events from the CheckBox
		checkBox.setOnClickListener(new CheckBox.OnClickListener() {
			public void onClick(View v) {
				if (checkBox.isChecked()) {
					txtCheckBox.setText("CheckBox: Box is checked");
				} else {
					txtCheckBox.setText("CheckBox: Not checked");
				}
			}
		});
		
		// React to events from the RadioGroup
		rb1.setOnClickListener(new RadioGroup.OnClickListener() {
			public void onClick(View v) {
				txtRadio.setText("Radio: Button 1 picked");
			}
		});
		rb2.setOnClickListener(new RadioGroup.OnClickListener() {
			public void onClick(View v) {
				txtRadio.setText("Radio: Button 2 picked");
			}
		});
		rb3.setOnClickListener(new RadioGroup.OnClickListener() {
			public void onClick(View v) {
				txtRadio.setText("Radio: Button 3 picked");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_example, menu);
		return true;
	}

}
