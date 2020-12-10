package com.androidcookbook.nativefunctfromjs;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchNativeContactsApp(View v) {
        String packageName = "com.android.contacts";
        String className = ".DialtactsContactsEntryActivity";
        String action = "android.intent.action.MAIN";
        String category1 = "android.intent.category.LAUNCHER";
        String category2 = "android.intent.category.DEFAULT";
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, packageName + className));
        intent.setAction(action);
        intent.addCategory(category1);
        intent.addCategory(category2);
        startActivity(intent);
    }
}
