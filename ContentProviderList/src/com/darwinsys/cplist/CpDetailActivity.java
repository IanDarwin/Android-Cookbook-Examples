package com.darwinsys.cplist;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CpDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_detail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cp_detail, menu);
        return true;
    }
    
}
