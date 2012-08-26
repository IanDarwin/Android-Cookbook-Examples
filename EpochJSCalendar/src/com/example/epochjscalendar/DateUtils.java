package com.example.epochjscalendar;

import java.util.Date;

/** THIS IS NOT THE REAL DATEUTILS, JUST A QUICK HACK TO LET THINGS COMPILE */
public class DateUtils {

	public static CharSequence convertDateToSectionHeaderFormat(long time) {
		return new Date(time).toString();
	}

}
