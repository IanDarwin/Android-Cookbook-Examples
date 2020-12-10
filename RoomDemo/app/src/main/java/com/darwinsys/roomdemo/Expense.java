package com.darwinsys.roomdemo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDate;

@Entity
public class Expense {

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo()
    String date;

    @ColumnInfo()
    String description;

    @ColumnInfo()
    double amount;

    public Expense() {
        // empty
    }

    @Ignore
    public Expense(LocalDate date, String description, double amount) {
        this.date = date.toString();
        this.description = description;
        this.amount = amount;
    }

    @Ignore
    public Expense(String date, String description, double amount) {
        this.date = date;
        LocalDate nDate;
        this.description = description;
        this.amount = amount;
    }


    @Ignore
    public Expense(String description, double amount) {
        this(LocalDate.now().toString(), description, amount);
    }

    public String toString() {
        return String.format("%s %22s %.2f", date, description, amount);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate(LocalDate date) {
        this.date = date.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
