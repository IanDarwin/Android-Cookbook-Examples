package com.darwinsys.aidldemo;

parcelable Expense;

interface AIDLDemo {

    /** Verification: what is the server's PID? */
	int getPid();

    /** Verification: what is the server's Thread ID? */
	long getTid();

	/** Verification: what is the server's UID? */
	int getUid();

    /** If those all look OK, we can submit our Expense item */
	int submitExpense(in Expense expense);

    /** We can look up expense items by ID */
	Expense getExpense(int id);
}
