package org.panel;

import org.panel.Panel.OnPanelListener;

import easing.interpolator.BackInterpolator;
import easing.interpolator.BounceInterpolator;
import easing.interpolator.ElasticInterpolator;
import easing.interpolator.ExpoInterpolator;
import easing.interpolator.EasingType.Type;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class Test extends Activity implements OnPanelListener {

	private Panel bottomPanel;
	private Panel topPanel;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Panel panel;
        
        topPanel = panel = (Panel) findViewById(R.id.topPanel);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BounceInterpolator(Type.OUT));
        
        panel = (Panel) findViewById(R.id.leftPanel1);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BackInterpolator(Type.OUT, 2));

        panel = (Panel) findViewById(R.id.leftPanel2);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BackInterpolator(Type.OUT, 2));

        panel = (Panel) findViewById(R.id.rightPanel);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new ExpoInterpolator(Type.OUT));

        bottomPanel = panel = (Panel) findViewById(R.id.bottomPanel);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new ElasticInterpolator(Type.OUT, 1.0f, 0.3f));
        
        findViewById(R.id.smoothButton1).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				bottomPanel.setOpen(!bottomPanel.isOpen(), true);
			}
        });
        findViewById(R.id.smoothButton2).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				topPanel.setOpen(!topPanel.isOpen(), false);
			}
        });
    }

	public void onPanelClosed(Panel panel) {
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("Test", "Panel [" + panelName + "] closed");
	}
	public void onPanelOpened(Panel panel) {
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("Test", "Panel [" + panelName + "] opened");
	}
}
