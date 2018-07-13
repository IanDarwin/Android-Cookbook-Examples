package com.darwinsys.roomdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    AnApplication application;
    AppDatabase mDatabase;
    List<Expense> mList = new ArrayList<>(); // Move to singleton
    ListView mListView;
    ArrayAdapter mAdapter;
    ExecutorService threadPool = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ONCREATE");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        application = (AnApplication) getApplication();
        mDatabase = application.getDatabase();
        mList = application.getList();

        mListView = findViewById(R.id.list);

        mAdapter = new ArrayAdapter<Expense>(this, R.layout.list_item, R.id.expenseTF, mList); // FROMFIELDS, TOVIEWS

        mListView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    final Expense expense = new Expense("Dinner among the Stars", 123.45);
                    new AsyncTask<Expense, Void, Expense>() {

                        @Override
                        protected Expense doInBackground(Expense... expenses) {
                            Log.d(TAG, "Inserting: " + expense);
                            mDatabase.expenseDao().insert(expense);
                            return expense;
                        }

                        @Override
                        protected void onPostExecute(Expense expense) {
                            mList.add(expense);
                            mAdapter.notifyDataSetChanged();
                        }
                    }.execute(expense);

                Snackbar.make(view, "Saving...", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        threadPool.execute(() -> {
            mList.clear();
            final List<Expense> expenses = mDatabase.expenseDao().getAll();
            Log.d(TAG, "Read " + expenses.size() + " expenses");
            mList.addAll(expenses);
            // Update the UI
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
