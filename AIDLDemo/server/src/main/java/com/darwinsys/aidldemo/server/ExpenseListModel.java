package com.darwinsys.aidldemo.server;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.darwinsys.aidldemo.Expense;

import java.util.ArrayList;
import java.util.List;

/**
 * An in-memory List Model for the Expense items.
 * Doesn't have to be persistent as this is a demo application.
 */
public enum ExpenseListModel {

    INSTANCE;

    private final static String TAG = ExpenseListModel.class.getSimpleName();
    public final static String ACTION_EXPENSE_ADDED = ExpenseListModel.class.getName();

    private final List<Expense>  expenses = new ArrayList<>();

    public List<Expense> getExpenses() {
        return expenses;
    }

    int addExpense(Context context, Expense nexExpense) {
        int id = expenses.size();
        expenses.add(nexExpense);
        Log.d(TAG, String.format("There are now %d items", expenses.size()));
        // Send notification from service thread to UI thread
        Intent intent = new Intent(ACTION_EXPENSE_ADDED);
        intent.putExtra("id", nexExpense.id);
        // LocalBroadcast - goes to ServiceActivity only
        if (LocalBroadcastManager.getInstance(context).sendBroadcast(intent)) {
            Log.d(TAG, "Well, we notified the Client App.");
        } else {
            Log.d(TAG, "Sending notification failed - no receivers?");
        }
        return id;
    }

    Expense getExpense(int id) {
        return expenses.get(id);
    }
}
