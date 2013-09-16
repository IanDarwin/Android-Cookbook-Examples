package com.example.dreamsdemo;

import java.util.Random;

import android.animation.TimeAnimator;
import android.content.Context;
import android.service.dreams.DreamService;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

/** 
 * Androids Do Dream of Electric Sleep, But Only While Charging
 */
public class DreamsDemo extends DreamService {
	static final String TAG = "MyDream";

	final static int YINCR = 2;
	final static int POINT_SIZE = 18;
	private static final Random RANDOM = new Random();
	String[] data;
	
	@Override
	public void onCreate() {
		Log.d(TAG, "MyDream.onCreate()");
		super.onCreate();		
		data = getResources().getStringArray(R.array.reminders);
	}

	@Override
	public void onAttachedToWindow() {
		Log.d(TAG, "MyDream.onAttachedToWindow()");
		super.onAttachedToWindow();

		// ready the Runner
		final Runner runner = new Runner(this);
		setContentView(runner);
		
		// Exit dream upon user touch
		setInteractive(false);
		// Don't hide system UI - leaves time and battery etc showing.
		setFullscreen(false);
		
	}

	
	/**
	 * A little animator.
	 */
	class Runner extends FrameLayout implements TimeAnimator.TimeListener {

		private TextView ta;
		private final TimeAnimator mAnimator;

		public Runner(Context context) {
	        this(context, null);
	    }

	    public Runner(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public Runner(Context context, AttributeSet attrs, int flags) {
	        super(context, attrs, flags);
	        mAnimator = new TimeAnimator();
	        mAnimator.setTimeListener(this);
	        ta = new TextView(context, attrs);
	        ta.setTextSize(POINT_SIZE);
	        final FrameLayout.LayoutParams params = 
	        	new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
	        addView(ta, params);
	    }

	    @Override
	    public void onAttachedToWindow() {
	    	Log.d(TAG, "MyDream.Runner.onAttachedToWindow()");
	        super.onAttachedToWindow();
	        mAnimator.start();
	    }
	    
	    @Override
	    public void onDetachedFromWindow() {
	    	Log.d(TAG, "MyDream.Runner.onDetachedFromWindow()");
	        mAnimator.cancel();
	        super.onDetachedFromWindow();
	    }

	    /** Tick! */
	    @Override
	    public void onTimeUpdate(TimeAnimator animation, long elapsed, long dt_ms) {

	        // At the top of screen, pick a new reminder
	        if (ta.getY() < YINCR) {
	        	int ix = RANDOM.nextInt(data.length);
		        ta.setText(data[ix]);
	        }
	        // move that baby along!
	        int y = (int)(ta.getY() + YINCR);
	        if (getHeight() > 0) {	        	
	        	y %= getHeight();
	        }
	        // If we've gone off the bottom, give up; next call or few will reset it
	        if (y > getHeight()) {
	        	return;
	        }
	        ta.setY(y);
		}
		
	}
}
