package com.track.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>
 * Title: Simple date converter utility file
 * </p>
 * <p>
 * Description: This class is used to generate different date formats
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: track
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class ConvertDate {
	private static SimpleDateFormat dfVisualDate = null;
	private static SimpleDateFormat dfVisualTime = null;
	private static SimpleDateFormat dfInner = null;
	private static SimpleDateFormat dfSQL = null;

	static {
		dfVisualDate = new SimpleDateFormat("MM/dd/yyyy");// new
															// SimpleDateFormat(Global.sDateFormat);
		dfVisualTime = new SimpleDateFormat(Global.sDateFormat + " "
				+ Global.sTimeFormat);
		dfInner = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		dfSQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	}

	// parses the Date String from Visual format into Date structure -
	// depricated !
	public static Date parseVisual(String sDate) throws ParseException {
		return dfVisualDate.parse(sDate + " 00:00");
	}

	// format the Date strcuture into string form showing to user
	public static String formatVisual(Date dDate) {
		if (dDate == null)
			return "";
		return dfVisualDate.format(dDate);
	}

	// format the Date strcuture into string form showing to user
	public static String formatVisual(Date dDate, boolean hasTime) {
		if (!hasTime)
			return formatVisual(dDate);
		return dfVisualTime.format(dDate);
	}

	// parses the String information for inner data flow into Date datastructure
	public static Date parseInner(String sDate) throws ParseException {
		return dfInner.parse(sDate + " 00:00");
	}

	// formats the inner date and time format into string
	public static String formatInner(Date dDate) {
		return dfInner.format(dDate);
	}

	// parses the date and time information from MS SQL DB into Date
	// datastructure
	public static Date parseSQL(String sDate) throws ParseException {
		return dfSQL.parse(sDate);
	}

	// formats the Date into string applicable for MS SQL
	public static String formatSQL(Date dDate) {
		return dfSQL.format(dDate);
	}

	public static boolean checkDate(Date dDate) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return dDate.after(cal.getTime());
	}

	public static int getInteger(float fNumber) {
		int iRes = (int) fNumber;
		if ((fNumber - iRes) < 0.5) {
			return iRes;
		} else {
			return iRes + 1;
		}
	}

	public static void main(String[] arg) throws Exception {
		Date theDate = Calendar.getInstance().getTime();
		String sString = formatInner(theDate);
		sString += " 12:30";
		theDate = parseInner(sString);

	}
}
