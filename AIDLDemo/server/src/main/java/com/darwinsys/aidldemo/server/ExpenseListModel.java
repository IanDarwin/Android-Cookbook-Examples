package com.darwinsys.aidldemo.server;

import com.darwinsys.aidldemo.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListModel {

    List<Expense>  expenses = new ArrayList<>();

    int addExpense(Expense newb) {
        int id = expenses.size();
        expenses.add(newb);
        return id;
    }

    Expense getExpense(int id) {
        return expenses.get(id);
    }
}
