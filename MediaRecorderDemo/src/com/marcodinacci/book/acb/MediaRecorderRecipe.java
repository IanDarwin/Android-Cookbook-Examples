package com.marcodinacci.book.acb;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class MediaRecorderRecipe extends Activity implements SurfaceHolder.Callback {
	private static final String VIDEO_PATH_NAME = "/Pictures/test.3gp";
	private MediaRecorder mMediaRecorder;
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	private View mToggleButton;
	private boolean mInitSuccesful;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_recorder_recipe);
        
        // we shall take the video in landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mToggleButton = (ToggleButton) findViewById(R.id.toggleRecordingButton);
		mToggleButton.setOnClickListener(new OnClickListener() {
			@Override
			// toggle video recording
			public void onClick(View v) {
				if (((ToggleButton)v).isChecked())
					mMediaRecorder.start();
				else {
					mMediaRecorder.stop();
					mMediaRecorder.reset();
					try {
						initRecorder(mHolder.getSurface());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
    }

    /* Init the MediaRecorder, the order the methods are called is vital to
     * its correct functioning */
	private void initRecorder(Surface surface) throws IOException {
		// It is very important to unlock the camera before doing setCamera
		// or it will results in a black preview
		if(mCamera == null) {
			mCamera = Camera.open();
			mCamera.unlock();
		}

		if(mMediaRecorder == null)
			mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setPreviewDisplay(surface);
		mMediaRecorder.setCamera(mCamera);
		
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		File file = new File(Environment.getExternalStorageDirectory(),VIDEO_PATH_NAME);
		// "touch" the file
		if(!file.exists()) {
			File parent = file.getParentFile();
			if(parent != null) 
				if(!parent.exists())
					if(!parent.mkdirs())
						throw new IOException("Cannot create " +
								"parent directories for file: " + file);
			
				file.createNewFile();
		}
		
		mMediaRecorder.setOutputFile(file.getAbsolutePath());
		
		// No limit. Check the space on disk!
		mMediaRecorder.setMaxDuration(-1);
		mMediaRecorder.setVideoFrameRate(15);
		
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
		
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			// This is thrown if the previous calls are not called with the 
			// proper order
			e.printStackTrace();
		}
		
		mInitSuccesful = true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if(!mInitSuccesful)
				initRecorder(mHolder.getSurface());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		shutdown();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}

	
	private void shutdown() {
		// Release MediaRecorder and especially the Camera as it's a shared
		// object that can be used by other applications
		mMediaRecorder.reset();
		mMediaRecorder.release();
		mCamera.release();
		
		// once the objects have been released they can't be reused
		mMediaRecorder = null;
		mCamera = null;
	}
}