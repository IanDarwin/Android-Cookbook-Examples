package com.darwinsys.aidldemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/** The official Android docco at
 * https://developer.android.com/guide/components/aidl#PassingObjects
 * claims you can create this just by putting the fields into
 * "parcelable Expense { ... }" in an AIDL file, but this
 * DOES NOT WORK (says "';' expected" on the "{").
 * So, pragmatically, we do it the hard way.
 */
public class Expense implements Parcelable {
    public int id;
    /** A one-line description of the expense event */
    public String description;
    /** The date is in standard (ISO) order, yyyy-mm-dd, i.e., LocalDate.now().toString(); */
    public String date;
    /** The currencies we can use */
    enum Currency { USD, UKP, CAD, INR, YEN }
    public Currency currency = Currency.CAD;
    /** The amount, in the stated currency */
    public double amount;

    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<>() {
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    @SuppressWarnings("unused")
    public Expense() {
        // Somebody might need this
    }

    private Expense(Parcel in) {
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(description);
        out.writeString(date);
        out.writeInt(currency.ordinal());
        out.writeDouble(amount);
    }

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        description = in.readString();
        date = in.readString();
        currency = Currency.values()[in.readInt()];
        amount = in.readDouble();
    }

    // An Expense is not a file descriptor so we just return 0
    public int describeContents() {
        return 0;
    }

    @Override
    // Hand-rolled toString()
    public String toString() {
        return String.format(Locale.getDefault(),
                "Expense{id %d '%s' on %s for %s %.2f", id, description, date, currency, amount);
    }

    @Override
    // Generated equals()
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        if (id != expense.id) return false;
        if (Double.compare(expense.amount, amount) != 0) return false;
        if (!description.equals(expense.description)) return false;
        if (!date.equals(expense.date)) return false;
        return currency == expense.currency;
    }

    @Override
    // Generated hashCode()
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + description.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + currency.hashCode();
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
