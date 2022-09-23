package com.darwinsys.aidldemo.server;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.darwinsys.aidldemo.Expense;

public class ServiceActivity extends AppCompatActivity {
    final static String TAG = "ServiceActivity";

    ListView listView;
    ArrayAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpenseListModel expenseListModel = ExpenseListModel.INSTANCE;

        listView = findViewById(R.id.myList);
        listViewAdapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, expenseListModel.getExpenses());
        listView.setAdapter(listViewAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(updater, new IntentFilter(ExpenseListModel.ACTION_EXPENSE_ADDED));
        Log.d(TAG, "Registered Receiver " + updater);
    }

    private BroadcastReceiver updater = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("id", -1);
            Log.d(TAG, "BroadcastReceiver got new expense item " + id);
            listViewAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        // Stop receiving notifications
        super.onDestroy();
        unregisterReceiver(updater);
    }
}
