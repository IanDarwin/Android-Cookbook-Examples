package com.tabhostdemo;

import android.app.Dialog;
import android.content.Context;
import android.widget.TabHost;

public class CustomDialog extends Dialog {
     public CustomDialog(final Context context) {
        super(context);
    
        setTitle("My Custom Tabbed Dialog");
        setContentView(R.layout.custom_dialog_layout);

        TabHost tabHost = (TabHost)findViewById(R.id.TabHost01);
        tabHost.setup();
        
        TabHost.TabSpec spec1 = tabHost.newTabSpec("tab1");
        spec1.setIndicator("Part 1",
            context.getResources().getDrawable(android.R.drawable.ic_media_rew));
        spec1.setContent(R.id.TextView01);
        tabHost.addTab(spec1);
        
        TabHost.TabSpec spec2 = tabHost.newTabSpec("tab2");
        spec2.setIndicator("Part Deux",
            context.getResources().getDrawable(android.R.drawable.ic_media_play));
        spec2.setContent(R.id.TextView02);
        tabHost.addTab(spec2);
    }
}
