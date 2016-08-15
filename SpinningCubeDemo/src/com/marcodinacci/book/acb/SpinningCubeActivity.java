package com.marcodinacci.book.acb;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.marcodinacci.android.commons.opengl.Cube;

public class SpinningCubeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // go fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        GLSurfaceView view = new OpenGLSurfaceView(this);
   		setContentView(view);
    }
}

class OpenGLSurfaceView extends GLSurfaceView implements Renderer {

	private Cube mCube;
	private float mXrot;
	private float mYrot;
	private float mXspeed;
	private float mYspeed;
	
	public OpenGLSurfaceView(Context context) {
		super(context);
		
		setRenderer(this);
		
		requestFocus();
		setFocusableInTouchMode(true);
		
		mCube = new Cube();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d("OPENGL", "key up: " + keyCode);
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			mYspeed -= 0.1f;
		else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
			mYspeed += 0.1f;
		else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
			mXspeed -= 0.1f;
		else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			mXspeed += 0.1f;
		
		return true;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 
		
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                          GL10.GL_NICEST);
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
		
		gl.glTranslatef(0.0f, 0.0f, -10.0f);
		
		gl.glRotatef(mXrot, 1.0f, 0.0f, 0.0f);  
        gl.glRotatef(mYrot, 0.0f, 1.0f, 0.0f);
        
		mCube.draw(gl);
		
		gl.glLoadIdentity(); 					
		
		mXrot += mXspeed;
		mYrot += mYspeed;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}