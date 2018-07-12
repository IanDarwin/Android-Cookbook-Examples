package com.darwinsys.roomdemo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expense")
    List<Expense> getAll();

    @Query("SELECT * FROM expense WHERE id IN (:expenseIds)")
    List<Expense> findByIds(int[] expenseIds);

    @Query("SELECT * FROM expense WHERE description LIKE :descr Limit 1")
    Expense findByDescription(String descr);

    @Insert
    void insert(Expense... expenses);

    @Delete
    void delete(Expense expense);
}
