package com.darwinsys.roomdemo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Expense {
    @PrimaryKey
    long id;

    @ColumnInfo()
    String date;

    @ColumnInfo()
    String description;

    @ColumnInfo()
    double amount;

    public Expense(String date, String description, double amount) {
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
