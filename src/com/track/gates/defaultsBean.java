package com.track.gates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;

/********************************************************************************************************
 * PURPOSE : Class for accessing Default Values from the Database and performing
 * few common operations like adding up to user log table etc
 *******************************************************************************************************/

public class defaultsBean {
	private Boolean printLog = MLoggerConstant.DEFAULTSBEAN_PRINTPLANTMASTERLOG;
	private Boolean printQuery = MLoggerConstant.DEFAULTSBEAN_PRINTPLANTMASTERQUERY;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	Generator gn;

	public defaultsBean() throws Exception {
		gn = new Generator();
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
	 * PURPOSE : Method that retrieves default value of any field that is
	 * specified in the default info table : For Example : The default Message
	 * type is 'TEXT' which is stored in default info table like DEF_MSG_TYPE
	 * PARAMETER 1 : The field name for which default value is required RETURNS
	 * : The default value of the field as specified in the default info table
	 *******************************************************************************************************/
	public String getDefaultValue(String field) throws Exception {

		String value = "";

		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "select FIELD_VALUE from DEFAULT_INFO where FIELD_NAME = '"
					+ field + "' and REF_TYPE = 'SYSADMIN'";
			this.mLogger.query(printQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					value = rs.getString("FIELD_VALUE").trim();
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("defaultsBean", "getDefaultValue()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return value;
	}

	/********************************************************************************************************
	 * PURPOSE : Method that retrieves default value of any field that is
	 * specified in the default info table ( for drop down LIST BOX ) : For
	 * Example : The default Message type is 'TEXT' which is stored in default
	 * info table like DEF_MSG_TYPE PARAMETER 1 : The field name for which
	 * default value is required RETURNS : The default value of the field as
	 * specified in the default info table returned as selected option in drop
	 * down list box
	 *******************************************************************************************************/
	public String getDefaultSelectedOption(String field) throws Exception {

		String value = "<option></option>";

		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "select FIELD_VALUE,FIELD_DESC from DEFAULT_INFO where FIELD_NAME = '"
					+ field + "' and REF_TYPE = 'SYSADMIN'";
			this.mLogger.query(printQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					value = "<option value=" + rs.getString(1).trim()
							+ " selected>" + rs.getString(2).trim()
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("defaultsBean", "getDefaultSelectedOption()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return value;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for gettting all the Fields of REF_TABLE for the drop
	 * down list box in the html form PARAMETER 1 : Nil RETURNS : All reference
	 * table field names as select string for the list box
	 *******************************************************************************************************/

	public String getRefTableFields() throws Exception {

		String value = "";

		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "select distinct field_name from ref_table where ref_type <> 'SYSADMIN'";
			this.mLogger.query(printQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					String tmp = rs.getString("FIELD_NAME").trim();
					value = value + "<OPTION >" + tmp + "</OPTION>";

				}

			}
		} // End of try
		catch (Exception e) {
			System.out.println(e.toString());
			DbBean.writeError("defaultsBean", "getRefTableFields()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return value;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting all the values of a field name specified by
	 * the user : Retrieves the OPTION_CODE of FIELD_NAME from the REF_TABLE :
	 * For Example : if the FIELD_NAME is SMS_LANGUAGE the values returned are E
	 * and C ( English and Big5) PARAMETER 1 : The field name for which, the
	 * values are sought RETURNS : The values
	 *******************************************************************************************************/
	public String getRefField4Combo(String fieldName) throws Exception {

		String selectStr = "";

		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT OPTION_CODE,OPTION_DESC FROM REF_TABLE WHERE FIELD_NAME = '"
					+ fieldName + "' ORDER BY SHOW_ORDER";
			this.mLogger.query(printQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			// System.out.println(sql);
			if (rs != null) {
				String code, desc;
				while (rs.next()) {
					// <option value="sdfsdfsdf">arrr</option>
					code = rs.getString("OPTION_CODE").trim();
					desc = rs.getString("OPTION_DESC").trim();
					selectStr += "<option value='" + code + "'>" + desc
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("defaultsBean", "getRefTableField()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return selectStr;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting all the values of a field name specified by
	 * the user : Retrieves the OPTION_CODE of FIELD_NAME from the REF_TABLE :
	 * For Example : if the FIELD_NAME is SMS_LANGUAGE the values returned are E
	 * and C ( English and Big5) PARAMETER 1 : The field name for which, the
	 * values are sought RETURNS : The values
	 *******************************************************************************************************/
	public String getRefField4Combo(String fieldName, String filter)
			throws Exception {

		String selectStr = "";

		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT OPTION_CODE,OPTION_DESC FROM REF_TABLE WHERE FIELD_NAME = '"
					+ fieldName + "' ORDER BY SHOW_ORDER";
			this.mLogger.query(printQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			// System.out.println(sql);
			if (rs != null) {
				String code, desc, selected;
				while (rs.next()) {
					selected = "";
					// <option value="sdfsdfsdf">arrr</option>
					code = rs.getString("OPTION_CODE").trim();
					desc = rs.getString("OPTION_DESC").trim();
					selected = (filter.equalsIgnoreCase(code)) ? "selected"
							: "";
					selectStr += "<option value='" + code + "' " + selected
							+ ">" + desc + "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("defaultsBean", "getRefTableField()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return selectStr;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting all the values of a field name specified by
	 * the user : Retrieves the OPTION_CODE of FIELD_NAME from the REF_TABLE :
	 * For Example : if the FIELD_NAME is SMS_LANGUAGE the values returned are E
	 * and C ( English and Big5) PARAMETER 1 : The field name for which, the
	 * values are sought RETURNS : The values
	 *******************************************************************************************************/
	public ArrayList getRefTableField(String fieldName) throws Exception {

		ArrayList al = new ArrayList();

		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT OPTION_CODE FROM REF_TABLE WHERE FIELD_NAME = '"
					+ fieldName + "' ORDER BY SHOW_ORDER";
			this.mLogger.query(printQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				while (rs.next()) {
					al.add(rs.getString(1).trim());
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("defaultsBean", "getRefTableField()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for Inserting records in the log file /table - With
	 * Session PARAMETER 1 : Session Object ( for getting the user name and
	 * session ID ) PARAMETER 2 : Action taken by the user ( Example : Sending
	 * Adhoc Message ) PARAMETER 3 : The result of the action ( Example : Adhoc
	 * Message added to queue successfully ) RETURNS : void
	 *******************************************************************************************************/
	public void insertToLog(HttpSession session, String action, String remarks)
			throws Exception {
		String formattedRemarks;
		StringBuffer sb = new StringBuffer();
		int i = 0;

		while (i < remarks.length()) {
			if (remarks.charAt(i) == '<') {
				while (remarks.charAt(i) != '>')
					i++;
				i++;
			} else {
				if (sb.length() < 100 && ((int) remarks.charAt(i) != 39)) // Checking
					// for
					// '
					// Character
					// and
					// length
					sb.append(remarks.charAt(i));
				i++;
			}
		}
		formattedRemarks = new String(sb); // Removing the html tags from the
		// result

		Connection con = null;
		try {
			con = getDBConnection();
			PreparedStatement ps = con
					.prepareStatement("Insert into USER_LOG values ( ?,?,?,?,?)");
			ps.setString(1, session.getId());
			ps.setString(2, session.getAttribute("LOGIN_USER").toString());
			ps.setString(3, action);
			ps.setString(4, formattedRemarks);
			ps.setString(5, gn.getDateTime());
			// ps.executeUpdate();

		} // End of try
		catch (Exception e) {
			DbBean.writeError("defaultsBean", "insertToLog()", e);
		} finally {
			DbBean.closeConnection(con);
		}
	}

	/********************************************************************************************************
	 * PURPOSE : Method for Inserting records in the log file /table - Without
	 * Session - ( This occurs when user has not logged in ) PARAMETER 1 : User
	 * ID as entered by the user in the login page PARAMETER 2 : Action taken by
	 * the user ( Example : Login Information ) PARAMETER 3 : The result of the
	 * action ( Example : Invalid User ID ) RETURNS : void
	 *******************************************************************************************************/
	public void insertLog(String userId, String action, String remarks)
			throws Exception {
		Connection con = getDBConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("Insert into USER_LOG values ( ?,?,?,?)");
			ps.setString(1, userId);
			ps.setString(2, action);
			ps.setString(3, remarks);
			ps.setString(4, gn.getDateTime());
			ps.executeUpdate();
			
		} // End of try
		catch (Exception e) {
			DbBean.writeError("defaultsBean", "insertToLog()", e);
		} finally {
			DbBean.closeConnection(con);
		}
	}

}
