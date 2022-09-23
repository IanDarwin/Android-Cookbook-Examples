package com.darwinsys.aidldemo.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.darwinsys.aidldemo.AIDLDemo;
import com.darwinsys.aidldemo.Expense;

import java.time.LocalDate;

public class ExpenseService extends Service {

    private static final String TAG = ExpenseService.class.getSimpleName();

    public ExpenseService() {
        Log.d(TAG, "ExpenseService::Init");
    }

    private final AIDLDemo.Stub mBinder = new AIDLDemo.Stub() {

        @Override
        public int getPid() {
            return Process.myPid();
        }

        @Override
        public long getTid() {
            return Thread.currentThread().getId();
        }

        @Override
        public int getUid() {
            return Process.myUid();
        }

        @Override
        public int submitExpense(Expense expense) {
            Log.d(TAG, "Received Expense item " + expense);
            return ExpenseListModel.addExpense(expense);
        }

        @Override
        public Expense getExpense(int id) {
            return ExpenseListModel.getExpense(id);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}