package com.simpleflashlightdemo;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
	private final static String TAG = "SimpleTorch";
	Camera cam;
	ToggleButton mTorch;
	Parameters camParams;
	private Context context;
	AlertDialog.Builder builder;
	AlertDialog alertDialog;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private final int FLASH_NOT_SUPPORTED = 0;
	private final int FLASH_TORCH_NOT_SUPPORTED = 1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = MainActivity.this;
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
			mTorch = (ToggleButton) findViewById(R.id.toggleButton1);
			mTorch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Log.d(TAG, "onCheckedChanged");
					try{
						if (cam == null){
							cam = Camera.open();
						}
						camParams = cam.getParameters();
						List<String> flashModes = camParams.getSupportedFlashModes();
						if (isChecked){
							if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
								camParams.setFlashMode(Parameters.FLASH_MODE_TORCH);
							}else{
								showDialog(MainActivity.this, FLASH_TORCH_NOT_SUPPORTED);
							}
						} else {
							camParams.setFlashMode(Parameters.FLASH_MODE_OFF);
						}
						cam.setParameters(camParams);
						cam.startPreview();
					}catch (Exception e) {
						Log.d(TAG, "Caught " + e);
						Toast.makeText(MainActivity.this, 
							"Camera/Torch failure: " + e, Toast.LENGTH_SHORT).show();
						e.printStackTrace();
						if (cam != null) {
							cam.stopPreview();
							cam.release();
						}
					}
				}
			});
			surfaceView = (SurfaceView) this.findViewById(R.id.hiddenSurfaceView);
			surfaceHolder = surfaceView.getHolder();
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			surfaceHolder.addCallback(this);
		} else {
			showDialog(MainActivity.this, FLASH_NOT_SUPPORTED);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(cam == null){
			cam = Camera.open();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		cam.release();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(cam != null){
			cam.release();
		}
	}

	public void showDialog (Context context, int dialogId) {
		switch(dialogId){
		case FLASH_NOT_SUPPORTED:
			builder = new AlertDialog.Builder(context);
			builder.setMessage("Sorry, Your phone does not support Camera Flash")
			.setCancelable(false)
			.setNeutralButton("Close", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alertDialog = builder.create();
			alertDialog.show();
			break;
		case FLASH_TORCH_NOT_SUPPORTED:
			builder = new AlertDialog.Builder(context);
			builder.setMessage("Sorry, Your camera flash does not support torch feature")
			.setCancelable(false)
			.setNeutralButton("Close", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alertDialog = builder.create();
			alertDialog.show();
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Empty - required by interface
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			cam.setPreviewDisplay(holder);
		} catch (IOException e) {
			Log.e(TAG, "Unexpected IO Exception in setPreviewDisplay()", e);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Empty - required by interface.
	}
	
	
}