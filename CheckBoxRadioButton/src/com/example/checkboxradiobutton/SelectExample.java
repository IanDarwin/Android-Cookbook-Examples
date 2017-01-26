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
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_example);
		checkBox = (CheckBox) findViewById(R.id.cbxBox1);
		txtCheckBox = (TextView) findViewById(R.id.txtCheckBox);
		
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
		
		final RadioGroup rg = (RadioGroup) findViewById(R.id.rgGroup1);
		// React to events from the RadioGroup
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
				txtRadio.setText(rb.getText() + " picked");
			}
		});
		txtRadio = (TextView) findViewById(R.id.txtRadio);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_example, menu);
		return true;
	}

}
