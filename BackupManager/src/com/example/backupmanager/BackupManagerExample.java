package com.example.backupmanager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.app.backup.BackupManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

public class BackupManagerExample extends Activity {

	static final String TAG = "BRActivity";

	static final Object[] sDataLock = new Object[0];
	static final String DATA_FILE_NAME = "saved_data";
	RadioGroup mFillingGroup;
	CheckBox mAddMayoCheckbox;
	CheckBox mAddTomatoCheckbox;
	File mDataFile;
	BackupManager mBackupManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup_restore);
		mFillingGroup = (RadioGroup) findViewById(R.id.filling_group);
		mAddMayoCheckbox = (CheckBox) findViewById(R.id.mayo);
		mAddTomatoCheckbox = (CheckBox) findViewById(R.id.tomato);
		mDataFile = new File(getFilesDir(), BackupManagerExample.DATA_FILE_NAME);
		mBackupManager = new BackupManager(this);
		populateUI();
	}

	void populateUI() {
		RandomAccessFile file;

		int whichFilling = R.id.pastrami;
		boolean addMayo = false;
		boolean addTomato = false;

		synchronized (BackupManagerExample.sDataLock) {
			boolean exists = mDataFile.exists();

			try {
				file = new RandomAccessFile(mDataFile, "rw");
				if (exists) {
					Log.v(TAG, "datafile exists");
					whichFilling = file.readInt();
					addMayo = file.readBoolean();
					addTomato = file.readBoolean();
					Log.v(TAG, " mayo=" + addMayo + " tomato=" + addTomato
							+ " filling=" + whichFilling);
				} else {
					Log.v(TAG, "creating default datafile");
					writeDataToFileLocked(file, addMayo, addTomato,
							whichFilling);
					mBackupManager.dataChanged();
				}
			} catch (IOException ioe) {
				// Do some error handling here!
			}
		}
		mFillingGroup.check(whichFilling);
		mAddMayoCheckbox.setChecked(addMayo);
		mAddTomatoCheckbox.setChecked(addTomato);
		mFillingGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						Log.v(TAG, "New radio item selected: " + checkedId);
						recordNewUIState();
					}
				});
		CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Log.v(TAG, "Checkbox toggled: " + buttonView);
				recordNewUIState();
			}
		};
		mAddMayoCheckbox.setOnCheckedChangeListener(checkListener);
		mAddTomatoCheckbox.setOnCheckedChangeListener(checkListener);
	}

	void writeDataToFileLocked(RandomAccessFile file, boolean addMayo,
			boolean addTomato, int whichFilling) throws IOException {
		file.setLength(0L);
		file.writeInt(whichFilling);
		file.writeBoolean(addMayo);
		file.writeBoolean(addTomato);
		Log.v(TAG, "NEW STATE: mayo=" + addMayo

		+ " tomato=" + addTomato + " filling=" + whichFilling);
	}

	void recordNewUIState() {
		boolean addMayo = mAddMayoCheckbox.isChecked();
		boolean addTomato = mAddTomatoCheckbox.isChecked();
		int whichFilling = mFillingGroup.getCheckedRadioButtonId();
		try {
			synchronized (BackupManagerExample.sDataLock) {
				RandomAccessFile file = new RandomAccessFile(mDataFile, "rw");
				writeDataToFileLocked(file, addMayo, addTomato, whichFilling);
			}
		} catch (IOException e) {
			Log.e(TAG, "Unable to record new UI state");
		}
		mBackupManager.dataChanged();
	}
}