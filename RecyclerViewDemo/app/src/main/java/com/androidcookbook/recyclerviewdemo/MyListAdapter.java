package com.androidcookbook.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A RecyclerView.Adapter for our list view
 * @author Ian Darwin
 */
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    private static final String TAG = "CustomAdapter";

    String[] mData;

    public MyListAdapter(String[] data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final Context context = viewGroup.getContext();
        return new ViewHolder(context, LayoutInflater.from(context)
                .inflate(R.layout.row_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder(" + position + ")");
        TextView v = holder.getView();
        v.setText(mData[position]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    /**
     * RecyclerView requires use of a ViewHolder for communications between itself and the app
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView; // The View we hold

        public ViewHolder(final Context context, View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You clicked " + mData[getPosition()], Toast.LENGTH_SHORT).show();
                }
            });
            mTextView = (TextView) itemView.findViewById(R.id.textview);
        }

        public TextView getView() {
            return mTextView;
        }
    }
}
