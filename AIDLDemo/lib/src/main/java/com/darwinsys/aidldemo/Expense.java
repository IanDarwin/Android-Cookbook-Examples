package com.darwinsys.aidldemo;

import android.os.Parcel;
import android.os.Parcelable;

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
        out.writeDouble(amount);
    }

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        description = in.readString();
        date = in.readString();
        amount = in.readDouble();
    }

    // Not an FD so just return 0
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Expense{id %d '%s' on %s for %s %.2f", id, description, date, currency, amount);
    }
}
