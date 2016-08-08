package com.androidcookbook.recyclerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ListActivity extends AppCompatActivity { // XXX Activity?

    private static final String TAG = ListActivity.class.getSimpleName();

    protected RecyclerView mRecyclerView;
    protected MyListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new MyListAdapter(getResources().getStringArray(R.array.foodstuffs));
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this); // XXX Move this to XML?
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
