package com.example.dialogdemos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends Activity {

	private static final int DLG_UNSAVED = 1;
	private static final String TAG = "DialogDemos";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @SuppressWarnings("deprecation")
	public void oldWay(View v) {
    	showDialog(DLG_UNSAVED);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
    	case DLG_UNSAVED:
    		final AlertDialog alertDialog = new AlertDialog.Builder(this)
    		.setTitle(R.string.unsaved_changes_title)
    		.setMessage(R.string.unsaved_changes_message)
    		.setPositiveButton(R.string.save_changes, new AlertDialog.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				// saveInformation(); // application-provided Save method.
    			}
    			})
    		.setNeutralButton(R.string.discard_changes, new AlertDialog.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    			        finish();
    		        }
    			})
    		.setNegativeButton(R.string.cancel_dialog, new AlertDialog.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    			        dialog.cancel();
    			}
    			})
    			.create();
    		return alertDialog;
    		default:
    			//@SuppressWarnings("deprecation")
    			return super.onCreateDialog(id);
    	}
    }
    
    public void qdWay(View v) {
    	Log.d(TAG, "Quick&Dirty Way invoked");
    	Dialog dlg = onCreateDialog(DLG_UNSAVED);
    	dlg.show();
    	
    }
    
    class MyDialog extends DialogFragment {
    	
    }
    
}
