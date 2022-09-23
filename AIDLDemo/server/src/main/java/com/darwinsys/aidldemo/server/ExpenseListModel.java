package com.darwinsys.aidldemo.server;

import android.util.Log;

import com.darwinsys.aidldemo.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListModel {

    private static List<Expense>  expenses = new ArrayList<>();

    private final static String TAG = ExpenseListModel.class.getSimpleName();

    static int addExpense(Expense newb) {
        int id = expenses.size();
        expenses.add(newb);
        Log.d(TAG, String.format("There are now %d items", expenses.size()));
        return id;
    }

    static Expense getExpense(int id) {
        return expenses.get(id);
    }
}
