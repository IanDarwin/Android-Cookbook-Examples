package com.darwinsys.todo.model;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.persistence.Embeddable;

/*
 * Simple Date for Tasks: only has Year, Month, and Day.
 * Month is number the human way, starting at one.
 */
@Embeddable
public class Date implements Serializable {

	private static final long serialVersionUID = -5944728253397921658L;
	
	int year, month, day;

	/** Construct a Date for today */
	public Date() {
		this(new java.util.Date());
	}
	

	
	/** Construct a Date for the given y-m-d */
	public Date(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		validate();
	}

	/** Construct a Date from a java.util.Date */
	public Date(java.util.Date date) {
		this(1900 + date.getYear(), date.getMonth() + 1, date.getDay());
		validate();
	}

	/** Construct a date from a YYYY-mm-DD String */
	public Date(String dateString) {
		populate(dateString);
	}
	
	private void populate(String dateString) {
		if (dateString == null) {
			throw new NullPointerException("dateString may not be null");
		}
		Scanner scan = new Scanner(dateString.replace('-', ' '));
		try {
			year = scan.nextInt();
			month = scan.nextInt();
			day = scan.nextInt();
		} catch (InputMismatchException e) {
			throw new IllegalArgumentException("Not YYYY-MM-DD: " + dateString);
		} finally {
			scan.close();
		}
		validate();
	}

	private void validate() {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Invalid month: " + month);
		}
		if (day < 1 || day > 31) {
			throw new IllegalArgumentException("Invalid day: " + day);
		}
	}

	/** Convert a Date to a java.util.Date() */
	public java.util.Date asJULDate() {
		return new java.util.Date(year, month - 1, day);
	}

	public String toString() {
		return String.format("%04d-%02d-%02d", year, month, day);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + month;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Date other = (Date) obj;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
}
