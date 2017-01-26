package org.panel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

public class SmoothButton extends Button {
	
	private static final long DELAY = 25;

	private LevelListDrawable transitionDrawable;
	private int transitionDrawableLength;
	private int level;

	private int[] colors;

	public SmoothButton(Context context, AttributeSet attrs) {
		super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmoothButton);
        transitionDrawable = (LevelListDrawable) a.getDrawable(R.styleable.SmoothButton_transitionDrawable);
        transitionDrawableLength = a.getInt(R.styleable.SmoothButton_transitionDrawableLength, 0);
        int useTextColors = 0;
        int c0 = 0;
        if (a.hasValue(R.styleable.SmoothButton_transitionTextColorUp)) {
        	c0 = a.getColor(R.styleable.SmoothButton_transitionTextColorUp, 0);
        	useTextColors++;
        }
        int c1 = 0;
        if (useTextColors == 1 && a.hasValue(R.styleable.SmoothButton_transitionTextColorDown)) {
        	c1 = a.getColor(R.styleable.SmoothButton_transitionTextColorDown, 0);
        	useTextColors++;
        }
        a.recycle();

        if (transitionDrawable == null) {
        	throw new RuntimeException("transitionDrawable must be defined in XML (with valid xmlns)");
        }
        if (transitionDrawableLength == 0) {
        	throw new RuntimeException("transitionDrawableLength must be defined in XML (with valid xmlns)");
        }
        if (useTextColors == 2) {
        	setTextColor(c0);
        	int a0 = Color.alpha(c0);
        	int r0 = Color.red(c0);
        	int g0 = Color.green(c0);
        	int b0 = Color.blue(c0);
        	int a1 = Color.alpha(c1);
        	int r1 = Color.red(c1);
        	int g1 = Color.green(c1);
        	int b1 = Color.blue(c1);
        	colors = new int[transitionDrawableLength];
        	for (int i=0; i<transitionDrawableLength; i++) {
	        	int ai = a0 + i * (a1 - a0) / transitionDrawableLength;
	        	int ri = r0 + i * (r1 - r0) / transitionDrawableLength;
	        	int gi = g0 + i * (g1 - g0) / transitionDrawableLength;
	        	int bi = b0 + i * (b1 - b0) / transitionDrawableLength;
	        	colors[i] = Color.argb(ai, ri, gi, bi);
        	}
        }
        level = 0;
		transitionDrawable.setLevel(level);
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();
		setBackgroundDrawable(transitionDrawable);
		setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		int delta = isPressed()? 1 : -1;
		handler.removeMessages(-delta);
		handler.sendEmptyMessage(delta);
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;

			level += what;
			if (level >= 0 && level < transitionDrawableLength) {
				transitionDrawable.setLevel(level);
		        if (colors != null) {
		        	setTextColor(colors[level]);
		        }
				handler.sendEmptyMessageDelayed(what, DELAY);
			} else {
				level = Math.max(0, level);
				level = Math.min(transitionDrawableLength-1, level);
			}
		}
	};
	
	public void setTransitionDrawable(Drawable drawable, int length) {
		transitionDrawable = (LevelListDrawable) drawable;
		transitionDrawableLength = length;
		level = 0;
		invalidate();
	}
}
