package com.track.gates;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;

/********************************************************************************************************
 * PURPOSE : A Class For Performing SQL operations
 *******************************************************************************************************/
public class sqlBean implements Serializable {

	Properties obpr;
	InputStream obip;
	Boolean printLog = MLoggerConstant.SQLBEAN_PRINTPLANTMASTERLOG;
	Boolean printQuery = MLoggerConstant.SQLBEAN_PRINTPLANTMASTERQUERY;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public sqlBean() throws Exception {
		obip = new FileInputStream(new File(MLoggerConstant.PROPS_FOLDER +"/OBJECTS_SQL.properties"));
		obpr = new Properties();
		obpr.load(obip);
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting a database connection from ConnectionPool
	 * thru static method of DbBean Class PARAMETER 1 : Nil RETURNS : Database
	 * Connection Object
	 *******************************************************************************************************/
	synchronized Connection getDBConnection() throws Exception {
		Connection con = DbBean.getConnection();
		return con;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for performing SQL Operation of Insert, Delete and
	 * Update Statements and returns the number of : rows affected during this
	 * Sql Operation PARAMETER 1 : Sql Statement RETURNS : Number of rows
	 * affected by the Sql statement
	 *******************************************************************************************************/
	public int insertRecords(String sqlUpdate) throws Exception {

		Connection con = getDBConnection();
		int n = 0;
		try {
			Statement stmt = con.createStatement();
			stmt.setEscapeProcessing(true);
			this.mLogger.query(printQuery, sqlUpdate);
			n = stmt.executeUpdate(sqlUpdate);
		} // End of try
		catch (Exception e) {
			DbBean.writeError("sqlBean", "insertRecords()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return n;
	}

	public int deleteRecords(String sqlUpdate) throws Exception {

		Connection con = getDBConnection();
		int n = 0;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			this.mLogger.query(printQuery, sqlUpdate);
			n = stmt.executeUpdate(sqlUpdate);
		} // End of try
		catch (Exception e) {
			DbBean.writeError("sqlBean", "insertRecords()", e);
		} finally {
			stmt.close();
			DbBean.closeConnection(con);
		}
		return n;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for performing SQL Operation of Insert, Delete and
	 * Update Statements in batch mode PARAMETER 1 : Arraylist containing Sql
	 * statements RETURNS : Returns True if all the Statements in the Arraylist
	 * are executed successfully or else false
	 *******************************************************************************************************/
	public boolean insertBatchRecords(ArrayList sqllist) throws Exception {

		Connection con = getDBConnection();
		con.setAutoCommit(false); // Auto commit turned off
		Statement stmt = con.createStatement();
		int n = 0;
		int total = 0;

		try {

			for (int i = 0; i < sqllist.size(); i++) {
				this.mLogger.query(printQuery, sqllist.get(i).toString().trim());
				n = stmt.executeUpdate(sqllist.get(i).toString().trim()); // Executing

				total++;

			}
		} catch (SQLException se) {
			System.out.println("db error");
			DbBean.writeError("sqlBean", "insertBatchRecords()", se);
			con.rollback();
			return false;
		} finally {
			if (total != sqllist.size()) { // If counter not equal to Number of
				// SQL Insert stmts - rollback
				con.rollback();
				DbBean.closeConnection(con);
				DbBean.writeError("sqlBean", "insertBatchRecords()",
						"SQL Statement Update Count Mismatch Error : Expected Count : "
								+ sqllist.size() + " Available Count : "
								+ total);
				return false;
			} else {
				con.commit(); // Else commit transaction
				DbBean.closeConnection(con);
			}
		}
		return true;
	}

	public boolean insertBatchRecords1(ArrayList sqllist) throws Exception {
		Connection con = getDBConnection();
		con.setAutoCommit(false); // Auto commit turned off
		Statement stmt = con.createStatement();

		try {

			for (int i = 0; i < sqllist.size(); i++) {
				this.mLogger
						.query(printQuery, sqllist.get(i).toString().trim());
				stmt.addBatch(sqllist.get(i).toString().trim()); // Executing

			}
		} catch (Exception se) {
			System.out.println("db error");
			DbBean.writeError("sqlBean", "insertBatchRecords()", se);
			con.rollback();
			return false;
		}
		return true;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for performing Select String ( List Box ) PARAMETER 1
	 * : Field Name for which Select string is required to form a list box in
	 * HTML RETURNS : Select String for the field
	 *******************************************************************************************************/
	public String getSelectString(String field) throws Exception {

		String htmlStr = "";
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			String sql = obpr.getProperty(field).trim();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("OPTION_CODE").trim();
					htmlStr += "<option value=" + val + ">"
							+ rs.getString("OPTION_DESC").trim() + "</option>";
				}

			}
			rs.close();
			stmt.close();

		} // End of try
		catch (Exception e) {
			DbBean.writeError("sqlBean", "getSelectString()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return htmlStr;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for forming Select String (List Box) for the SMS Code
	 * - And its Descriptiuon in the following example format : 30600 - BIRTHDAY
	 * GREETING PARAMETER 1 : Field Name for which select string is required
	 * RETURNS : Select String for the Field
	 *******************************************************************************************************/
	public String getSMSCodeSelect(String field) throws Exception {

		String htmlStr = "";

		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(obpr.getProperty(field));

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("OPTION_CODE").trim();
					htmlStr += "<option value=" + val + ">" + val + " - "
							+ rs.getString("OPTION_DESC").trim() + "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("sqlBean", "getSMSCodeSelect()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return htmlStr;
	}

	public static String formatHTML(String field) throws Exception {
       
		if (field.trim().length() == 0) {
			field = "&nbsp";
		}
		return field;
	}
	
	public static boolean istableExists (String tableName) throws Exception {
	    boolean status= false;
	    Connection con = DbBean.getConnection();
	    try{
		DatabaseMetaData md = con.getMetaData();
		ResultSet rs = md.getTables(null, null, tableName+"%", null);
		while (rs.next()) {
			status= true;
		}
	    }	
		catch (Exception e) {
			DbBean.writeError("sqlBean", "tableExits()", e);
		} finally {
			DbBean.closeConnection(con);
		}

	    return status;
	}
}
