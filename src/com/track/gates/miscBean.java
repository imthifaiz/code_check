package com.track.gates;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;

/********************************************************************************************************
 * PURPOSE : Class for other miscellaneous activities like ...Converting to Big5
 * String, performing House Keeping action
 *******************************************************************************************************/

public class miscBean {
	private Boolean printLog = MLoggerConstant.MISCBEAN_PRINTPLANTMASTERLOG;
	private Boolean printQuery = MLoggerConstant.MISCBEAN_PRINTPLANTMASTERQUERY;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting a database connection from ConnectionPool
	 * thru static method of DbBean Class PARAMETER 1 : Nil RETURNS : Database
	 * Connection Object
	 *******************************************************************************************************/
	public Connection getDBConnection() throws Exception {

		Connection con = DbBean.getConnection();
		return con;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for converting a normal string ( Cp1252 Encoding) to
	 * Big5 String PARAMETER 1 : Normal string RETURNS : Big5 String
	 *******************************************************************************************************/
	public String toBig5String(String genStr) throws Exception {

		String B5Str;

		byte[] b1252Bytes = genStr.getBytes("Cp1252");
		B5Str = new String(b1252Bytes, "Big5");
		return B5Str;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for checking if the SMS Msg Format could be inserted in
	 * the MSG_TEXT table based on : 4 parameters SMS_CODE, SMS_LANGUAGE,
	 * EFFECTIVE FROM, EFFECTIVE TO. : The constraint here is .... Only one SMS
	 * Format should be active within a time period ..this method checks this
	 * constraint PARAMETER 1 : SMS Code PARAMETER 2 : SMS Language PARAMETER 3
	 * : Effective From Date PARAMETER 4 : Effective To Date RETURNS : True if
	 * insertable else false
	 *******************************************************************************************************/
	public boolean isTextInsertable(String sms_code, String sms_lng,
			String eff_from, String eff_to) throws Exception {

		if (!isValid(eff_from, eff_to)) {
			return false;
		}

		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();

			String sql = "select EFFECTIVE_FROM, EFFECTIVE_TO from MSG_TEXT where SMS_CODE = '"
					+ sms_code + "' and SMS_LANGUAGE = '" + sms_lng + "'";
			this.mLogger.query(printQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				while (rs.next()) {
					String tb_eff_from = rs.getString("EFFECTIVE_FROM").trim();
					String tb_eff_to = rs.getString("EFFECTIVE_TO").trim();
					if (isBetween(eff_from, tb_eff_from, tb_eff_to)) {
						return false;
					}
					if (isBetween(eff_to, tb_eff_from, tb_eff_to)) {
						return false;
					}
				}
			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("miscBean", "isTextInsertable(4)", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return true;
	}

	/********************************************************************************************************
	 * // Overridden Method PURPOSE : Method for updating SMS Msg Format and
	 * checking if the SMS Msg Format could be inserted in the MSG_TEXT table
	 * based on : 4 parameters SMS_CODE, SMS_LANGUAGE, EFFECTIVE FROM, EFFECTIVE
	 * TO. : The constraint here is .... Only one SMS Format should be active
	 * within a time period ..this method checks this constraint PARAMETER 1 :
	 * SMS Code PARAMETER 2 : SMS Language PARAMETER 3 : Effective From Date
	 * PARAMETER 4 : Effective To Date PARAMETER 5 : The Message ID which is
	 * used to update the previous records RETURNS : True if insertable else
	 * false
	 *******************************************************************************************************/

	public boolean isTextInsertable(String sms_code, String sms_lng,
			String eff_from, String eff_to, String msg_id) throws Exception {

		if (!isValid(eff_from, eff_to)) {
			return false;
		}

		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();

			String sql = "select EFFECTIVE_FROM, EFFECTIVE_TO from MSG_TEXT where SMS_CODE = '"
					+ sms_code
					+ "' and SMS_LANGUAGE = '"
					+ sms_lng
					+ "' and msg_id <> " + msg_id;
			this.mLogger.query(printQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				while (rs.next()) {

					String tb_eff_from = rs.getString("EFFECTIVE_FROM").trim();
					String tb_eff_to = rs.getString("EFFECTIVE_TO").trim();
					if (isBetween(eff_from, tb_eff_from, tb_eff_to)) {
						return false;
					}
					if (isBetween(eff_to, tb_eff_from, tb_eff_to)) {
						return false;
					}

				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("miscBean", " isTextInsertable(5)", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return true;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for checking if the Effective From and Effective To
	 * dates are valid PARAMETER 1 : Effective From Date PARAMETER 2 : Effective
	 * To Date RETURNS : True if valid else false
	 *******************************************************************************************************/
	public boolean isValid(String eff_from, String eff_to)
			throws NumberFormatException {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = new GregorianCalendar();

		// Getting the current date
		java.util.Date dt = new java.util.Date();
		calendar.setTime(dt);
		String curDate = formatter.format(dt).toString(); // Generating current
		// date

		int icurDate = Integer.parseInt(curDate);
		int ieff_from = Integer.parseInt(eff_from);
		int ieff_to = 0;
		if ((eff_to != null) && (eff_to != "0")) {
			ieff_to = Integer.parseInt(eff_to);

		}
		if ((ieff_from >= icurDate)
				&& ((ieff_to > ieff_from) || (ieff_to == 0))) {
			return true;
		}
		return false;
	}

	public boolean iSExistStartndEND(String eff_from, String eff_to)
			throws NumberFormatException {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = new GregorianCalendar();

		// Getting the current date
		java.util.Date dt = new java.util.Date();
		calendar.setTime(dt);
		String curDate = formatter.format(dt).toString(); // Generating current
		// date

		int icurDate = Integer.parseInt(curDate);
		int ieff_from = Integer.parseInt(eff_from);
		int ieff_to = Integer.parseInt(eff_to);

		if ((icurDate >= ieff_from) && (icurDate <= ieff_to)) {
			return true;
		}
		return false;
	}

	/********************************************************************************************************
	 * PURPOSE : Checking for the validity of the digits in the date
	 * string..that is .dd < 32 and mm < 13 etc PARAMETER 1 : Date String
	 * RETURNS : True if valid else false
	 *******************************************************************************************************/

	public boolean isValidDateDigit(String date) {
		// Date in Format("dd/mm/yyyy");
		if (date.length() < 8) {
			return false;
		}

		String[] dateStr = new String[3];
		String dd, mm, yy;
		int imm, idd, iyy, maxdays, i = 0;

		StringTokenizer st = new StringTokenizer(date, "/");
		while (st.hasMoreTokens()) {
			dateStr[i++] = st.nextToken();

		}
		dd = dateStr[0];
		mm = dateStr[1];
		yy = dateStr[2];

		try {
			idd = Integer.parseInt(dd);
			imm = Integer.parseInt(mm);
			iyy = Integer.parseInt(yy);
		} catch (NumberFormatException nfe) {
			return false;
		}

		maxdays = 31;
		if (imm == 2) {
			maxdays = 29;
		} else if (imm == 4 || imm == 6 || imm == 9 || imm == 11) {
			maxdays = 30;

		}
		if ((idd > 0 && idd <= maxdays) && (imm > 0 && imm <= 12)
				&& (iyy >= 2000 && iyy <= 3000)) {
			return true;
		} else {
			return false;
		}

	}

	/********************************************************************************************************
	 * PURPOSE : Checking for the validity of the time digit ( by parsing it as
	 * an Integer ) PARAMETER 1 : Time String to be parsed RETURNS : True if
	 * valid else false
	 *******************************************************************************************************/

	public boolean isValidTimeDigit(String time) {
		// Time in Format :hh/mm/ss

		if (time.length() < 6) {
			return false;
		}
		int test;

		try {
			test = Integer.parseInt(time);
		} catch (NumberFormatException nfe) {
			return false;
		}

		if (test >= 0 && test < 240000) {
			return true;
		} else {
			return false;
		}

	}

	/********************************************************************************************************
	 * PURPOSE : Method for checking if a date is between 2 other dates (
	 * Prevents overlap of dates ) PARAMETER 1 : Date to be checked for
	 * PARAMETER 2 : Date's minimum limit PARAMETER 3 : Dates's maximum limit
	 * RETURNS : True if it is between the dates else false
	 *******************************************************************************************************/

	public boolean isBetween(String datetf, String tb_eff_from, String tb_eff_to)
			throws NumberFormatException {

		int idatetf = Integer.parseInt(datetf);
		int itb_eff_from = Integer.parseInt(tb_eff_from);
		int itb_eff_to = Integer.parseInt(tb_eff_to);

		if ((idatetf >= itb_eff_from) && (idatetf <= itb_eff_to)) {
			return true;
		}
		return false;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for checking if the Number String is valid or not
	 * PARAMETER 1 : Number String to be parsed RETURNS : True if valid else
	 * false
	 *******************************************************************************************************/
	public boolean isValidNum(String num) {
		if (num.length() < 3) {
			return false;
		}

		try {
			long n = Long.parseLong(num);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for house keeping based on To Date passed as Parameter
	 * PARAMETER 1 : The date upto which the house keeping action has to be done
	 * PARAMETER 2 : if type ==2 , house-keep all tables except adhoc history
	 * table - SMS_MSG table -- This will be useful for viewing history of
	 * messages RETURNS : Number of records deleted in each table
	 *******************************************************************************************************/

	public long[] houseKeep(String dateTo, int type) throws Exception {

		Connection con = getDBConnection();
		long[] deletedRecords = new long[6];
		int toExecute = 6;

		if (type == 2) {
			toExecute = 5;

		}
		try {
			Statement stmt = con.createStatement();

			String[] sql = new String[6];
			sql[0] = "DELETE FROM SMS_REC WHERE INSERT_DTS < '" + dateTo + "' ";
			sql[1] = "DELETE FROM SMS_REC1 WHERE INSERT_DTS < '" + dateTo
					+ "' ";
			sql[2] = "DELETE FROM MSG_LOG WHERE DS_DTS < '" + dateTo + "' ";
			sql[3] = "DELETE FROM LOADING_LOG WHERE LOADING_DTS < '" + dateTo
					+ "' ";
			sql[4] = "DELETE FROM USER_LOG WHERE ACTION_TIME < '" + dateTo
					+ "' ";
			sql[5] = "DELETE FROM SMS_MSG WHERE CREATED_ON < '" + dateTo + "' ";

			for (int i = 0; i < toExecute; i++) {
				this.mLogger.query(printQuery, sql[i]);
				deletedRecords[i] = stmt.executeUpdate(sql[i]); // Executing SQL
				// Statements
				// one by one
			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("miscBean", "houseKeep()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return deletedRecords;
	}

}