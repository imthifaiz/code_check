package com.track.gates;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;

/********************************************************************************************************
 * PURPOSE : Class Used in general Searching Operation and Getting values from
 * database
 *******************************************************************************************************/
public class searchBean {

	Generator gn;
	defaultsBean df;
	miscBean misc;
	sqlBean sb;
	private Boolean printLog = MLoggerConstant.SEARCHBEAN_PRINTPLANTMASTERLOG;
	private Boolean printQuery = MLoggerConstant.SEARCHBEAN_PRINTPLANTMASTERQUERY;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public searchBean() throws Exception {

		gn = new Generator();
		df = new defaultsBean();
		misc = new miscBean();
		sb = new sqlBean();
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
	 * PURPOSE : Checking if a record is already available with the given SMS
	 * Code in the MSG_DEFAULT table PARAMETER 1 : The SMS Code to be checked
	 * RETURNS : True if available else
	 *******************************************************************************************************/
	public boolean isSMSCodeAvail(String code) throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();

			String q = "select * from msg_default where SMS_CODE='" + code
					+ "'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			b = rs.next();

		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "isSMSCodeAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}

	/********************************************************************************************************
	 * PURPOSE : Searhing for Template for the given SMS Code and SMS Language
	 * PARAMETER 1 : SMS Code PARAMETER 2 : SMS Code PARAMETER 3 : URL of the
	 * next jsp to be used as hyperlink in the html RETURNS : String with result
	 * ( templates matching SMS Code and Language )
	 *******************************************************************************************************/

	public String getTemplates(String code, String lng, String url)
			throws Exception {

		Connection con = getDBConnection();
		String templateStr = "";
		try {
			Statement stmt = con.createStatement();
			String q = "select * from msg_text where SMS_CODE='" + code
					+ "' and SMS_LANGUAGE='" + lng + "'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			if (rs != null) {
				while (rs.next()) {
					String eff_from = rs.getString("EFFECTIVE_FROM").trim();
					eff_from = gn.getDB2UserDate(eff_from); // Converting date
					// to User readable
					// format
					String eff_to = rs.getString("EFFECTIVE_TO").trim();
					eff_to = gn.getDB2UserDate(eff_to); // Converting date to
					// User readable format

					templateStr += "<tr valign=\"middle\"><td align=\"center\" width=\"8%\">"
							+ n++
							+ ".</td><td width=\"62%\"><a href="
							+ url
							+ "?msg_id="
							+ rs.getString("MSG_ID").trim()
							+ ">"
							+ rs.getString("SMS_FORMAT").trim()
							+ "</a></td><td align=\"center\" width=\"15%\">"
							+ eff_from
							+ "</td><td align=\"center\" width=\"15%\">"
							+ eff_to + "</td></tr>";
				}

			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getTemplates()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return templateStr;
	}

	/********************************************************************************************************
	 * PURPOSE : Retrieves the parameters of Msg Template from the MSG_TEXT
	 * table based on the Msg_ID PARAMETER 1 : The message id of the template
	 * for which Values are required RETURNS : Arraylist containing the values
	 * of each of the field in MSG_TEXT table
	 *******************************************************************************************************/
	public ArrayList getTemplateValues(String msg_id) throws Exception {

		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		try {

			Statement stmt = con.createStatement();
			String q = "select * from msg_text where MSG_ID=" + msg_id;
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int maxlen = rs.getMetaData().getColumnCount();
			int n = 1;
			if (rs != null) {
				String element;
				if (rs.next()) {
					while (n < maxlen) {
						element = "";
						element = rs.getString(n++);
						if (element == null)
							element = "";
						else
							element = element.trim();
						boolean b = tempList.add(element);
					}
				}

			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getTemplateValues()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	/********************************************************************************************************
	 * PURPOSE : This method retrieves all parameters required to build the SMS
	 * Message from the SMS_REC table ( Used in Simulator ) PARAMETER 1 : The
	 * Msg_ref ( in SMS_REC ) table PARAMETER 2 : The Schduled Date ( For
	 * selecting the message format applicable to that date ) RETURNS :
	 * ArrayList with parameter required to build the SMS Message
	 *******************************************************************************************************/
	public ArrayList getSMSFormatParameters(String msg_ref, String sch_date)
			throws Exception {

		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		try {
			Statement stmt = con.createStatement();

			String q = "select P1,P2,P3,P4,P5,P6,P7,P8,P9,P10 from sms_rec where MSG_REF='"
					+ msg_ref + "'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int maxlen = rs.getMetaData().getColumnCount();
			int n = 1;
			if (rs != null) {
				String element;
				if (rs.next()) {
					while (n <= maxlen) {
						element = "";
						element = rs.getString(n++);
						if (element == null)
							element = "";
						else
							element = element.trim();
						boolean b = tempList.add(element);
					}
				}

			}
			rs.close();

			/*
			 * Selecting the SMS Message Format applicable from MSG_TEXT table
			 * based on the Msg in SMS_REC
			 */
			String q1 = "select SMS_FORMAT,FOOTER,EFFECTIVE_FROM,EFFECTIVE_TO from MSG_TEXT where ( SMS_CODE,SMS_LANGUAGE,MSG_SREF ) in "
					+ "( select SMS_CODE,SMS_LANGUAGE,MSG_SREF from SMS_REC where MSG_REF='"
					+ msg_ref + "')";
			this.mLogger.query(printQuery, q1);
			ResultSet rs1 = stmt.executeQuery(q1);
			if (rs1 != null) {
				String from, to, sms_format, footer;
				while (rs1.next()) {
					from = rs.getString("EFFECTIVE_FROM").trim();
					to = rs.getString("EFFECTIVE_TO").trim();

					if (misc.isBetween(sch_date, from, to)) // Check if the
					// Scheduled date
					// from JSP is
					// between effective
					// from and
					// effective to
					{
						sms_format = rs.getString("SMS_FORMAT").trim();
						footer = rs.getString("FOOTER");
						if (footer != null)
							footer = footer.trim();
						else
							footer = "";

						tempList.add(sms_format);
						tempList.add(footer);
					}
				}
			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getSMSFormatParameters()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	/********************************************************************************************************
	 * PURPOSE : Getting the description/full detasils of the SMS Message based
	 * on the Message Reference PARAMETER 1 : Message Reference PARAMETER 2 :
	 * Counter ( to detect the exact message ..as the message reference is a
	 * duplicate field in MSG_LOG table ) RETURNS : Arraylist with all
	 * information pertaining to the message reference in SMS_REC as well as
	 * MSG_LOG table
	 *******************************************************************************************************/
	public ArrayList getMessageDescription(String msg_ref, int inCounter)
			throws Exception {

		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		try {
			Statement stmt = con.createStatement();

			String q = "select SMS_CODE,SMS_LANGUAGE,MSG_SREF,MOBILE_NO,PRIORITY_CODE,MSG_TYPE,PROCESS_STATUS,"
					+ "SCHEDULE_TIME,EXPIRY_TIME from sms_rec where MSG_REF='"
					+ msg_ref + "'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int maxlen = rs.getMetaData().getColumnCount();
			int n = 1;
			if (rs != null) {
				String element;
				if (rs.next()) {
					while (n <= maxlen) {
						element = "";
						element = rs.getString(n++);
						if (element == null)
							element = "";
						else
							element = element.trim();
						boolean b = tempList.add(element);
					}
				}

			}
			rs.close();

			// If Message is processed then details are further collected from
			// the MSG_LOG table
			String q1 = "select DS_DTS,DELIVERY_STATUS,RS_DTS,RECEIVER_STATUS,SMS_TEXT,SERIAL_NO from MSG_LOG where MSG_REF='"
					+ msg_ref + "' order by ds_dts,rs_dts";
			this.mLogger.query(printQuery, q1);
			ResultSet rs1 = stmt.executeQuery(q1);
			int maxlen1 = rs1.getMetaData().getColumnCount();

			if (rs1 != null) {
				boolean more = false;
				int counter = 0;
				while (counter <= inCounter) // Moving to the exact result set
				// based on the counter value
				// from search query
				{
					more = rs1.next();
					counter++;
				}

				String[] element = new String[maxlen1];
				if (more)// Values stored in String array
				{
					n = 1;
					while (n <= maxlen1) {
						element[n - 1] = rs1.getString(n);
						n++;
					}
				}

				// Values checked for null and trimmed
				if (element[0] != null) // Checking if any value is available
				{
					for (int i = 0; i < maxlen1; i++) {
						if (element[i] == null)
							element[i] = "";
						else
							element[i] = element[i].trim();
						tempList.add(element[i]);
					}
				}

			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getMessageDescription()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	/********************************************************************************************************
	 * PURPOSE : Getting the description/full detasils of the Loading Log based
	 * on Batch Code PARAMETER 1 : Batch Code RETURNS : Arraylist with info
	 * pertaining to Batch Code from LOADING_LOG table
	 *******************************************************************************************************/
	public ArrayList getLoadingLogDescription(String batch_code, String read,
			String loaded, String status, String ldTime) throws Exception {

		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		try {
			Statement stmt = con.createStatement();

			String q = "select FILE_NAME,RECORD_LOADED,RECORD_READ,LOADING_STATUS,LOADING_DTS,ERROR_FILE from LOADING_LOG WHERE "
					+ "BATCH_CODE='"
					+ batch_code
					+ "' and RECORD_LOADED="
					+ loaded
					+ " and RECORD_READ="
					+ read
					+ " and LOADING_STATUS ='"
					+ status
					+ "' and LOADING_DTS= '" + ldTime + "'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int maxlen = rs.getMetaData().getColumnCount();
			int n = 1;
			if (rs != null) {
				String element;
				if (rs.next()) {
					while (n <= maxlen) {
						element = "";
						element = rs.getString(n++);
						if (element == null)
							element = "";
						else
							element = element.trim();
						boolean b = tempList.add(element);
					}
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getLoadingLogDescription()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting all the Unauthorised templates to authorise
	 * ( Templates not created by this user ) PARAMETER 1 : User login name
	 * RETURNS : Unauthorised Templates not created by this user
	 *******************************************************************************************************/
	public String getTemplatesToAuthorise(String user) throws Exception {

		Connection con = getDBConnection();
		String templateStr = "";
		try {
			Statement stmt = con.createStatement();

			String q = "select * from msg_text where UPDATED_BY <> '"
					+ user
					+ "' and (AUTHORISE_BY ='null' or AUTHORISE_BY ='0') order by UPDATED_ON";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			if (rs != null) {
				while (rs.next()) {
					String msg_id = rs.getString("MSG_ID").trim();
					String eff_from = rs.getString("EFFECTIVE_FROM").trim();
					eff_from = gn.getDB2UserDate(eff_from); // Converting to
					// user readable
					// date format
					String eff_to = rs.getString("EFFECTIVE_TO").trim();
					eff_to = gn.getDB2UserDate(eff_to); // Converting to user
					// readable date format
					String sms_format = rs.getString("SMS_FORMAT").trim();

					templateStr += "<tr valign=\"middle\"><td align=\"center\" width=\"7%\">"
							+ n++
							+ ". </td><td width=\"3%\" align=\"center\"><input type=\"checkbox\" name=\"MSG_ID\" value=\""
							+ msg_id
							+ "\"></td>"
							+ "<td align=\"center\" width=\"13%\" nowrap><b>"
							+ eff_from
							+ "</td><td align=\"center\" width=\"13%\" nowrap><b>"
							+ eff_to
							+ "</td><td width=\"50%\"><b><a href=editTemp.jsp?view="
							+ msg_id
							+ "&msg_id="
							+ msg_id
							+ ">"
							+ sms_format
							+ "</a></td><td width=\"14%\">"
							+ rs.getString("UPDATED_BY").trim() + "</td></tr>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getTemplatesToAuthorise()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return templateStr;
	}

	/********************************************************************************************************
	 * PURPOSE : Method to delete the unauthorised templates PARAMETER 1 :
	 * Arraylist containing template ID 's to be deleted RETURNS : True if all
	 * the templates mentioned in the arraylist were deleted else false
	 *******************************************************************************************************/
	public boolean deleteUnauthorisedTemplates(ArrayList al) throws Exception {
		ArrayList sqllist = new ArrayList();
		for (int i = 0; i < al.size(); i++) {
			String str = "delete from MSG_TEXT where MSG_ID = "
					+ al.get(i).toString();
			sqllist.add(str);
		}

		boolean b = sb.insertBatchRecords(sqllist); // return boolean value
		return b;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting all the unauthorised adhoc messages to
	 * authorise based on the user PARAMETER 1 : User login name RETURNS :
	 * String with all unauthorised adhoc messages ( not created by this user )
	 *******************************************************************************************************/
	public String getAdhocMsgToAuthorise(String user) throws Exception {

		Connection con = getDBConnection();
		String messages = "";
		try {
			Statement stmt = con.createStatement();

			String q = "select * from SMS_MSG where UPDATED_BY <> '"
					+ user
					+ "' and (AUTHORISE_BY ='null' or AUTHORISE_BY ='0') order by UPDATED_ON";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			if (rs != null) {
				while (rs.next()) {
					String ins_dts = rs.getString("UPDATED_ON").trim();
					ins_dts = gn.getDB2UserDateTime(ins_dts); // Converting to
					// user readable
					// format

					String exp_dts = rs.getString("EXPIRY_TIME");
					if (exp_dts.length() < 4 || exp_dts == null)
						exp_dts = gn.addDayAt(ins_dts, Integer.parseInt(df
								.getDefaultValue("DEF_EXPIRY_TIME"))); // if
					// message
					// expiry
					// time
					// is
					// not
					// specified
					// get
					// the
					// DEFAULT
					// EXPIRY
					// TIME
					// from
					// database
					// and
					// add
					// to
					// scheduled
					// time

					else
						exp_dts = gn.getDB2UserDateTime(exp_dts.trim()); // Converting
					// to
					// user
					// readable
					// format
					messages += "<tr valign=\"middle\"><td width=\"7%\"> <a href=previewAdhocMsg.jsp?msg_bref="
							+ rs.getString("MSG_BREF").trim()
							+ ">"
							+ n++
							+ ". </a></td><td width=\"3%\" align=\"center\"><input type=\"checkbox\" name=\"MSG_BREF\" value=\""
							+ rs.getString("MSG_BREF").trim()
							+ "\"></td>"
							+ "<td align=\"center\" width=\"13%\">"
							+ ins_dts
							+ "</td><td align=\"center\" width=\"13%\">"
							+ exp_dts
							+ "</td><td width=\"50%\">"
							+ rs.getString("MSG_TEXT").trim()
							+ "</td><td width=\"14%\">"
							+ rs.getString("UPDATED_BY").trim() + "</td></tr>";
				}

			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getAdhocMsgToAuthorise()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return messages;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting the previously selected value in message
	 * template based on sms_code during updation process PARAMETER 1 : The SMS
	 * Code PARAMETER 2 : Arraylist containing the field names for which values
	 * have to be retrieved RETURNS : Arraylist with values for each field in
	 * the incoming arraylist
	 *******************************************************************************************************/
	public ArrayList getSelectedValue(String code, ArrayList al)
			throws Exception {

		Connection con = getDBConnection();
		ArrayList ral = new ArrayList();
		try {
			Statement stmt = con.createStatement();
			String propStr;

			for (int i = 0; i < al.size(); i++) {
				String prop = al.get(i).toString();

				String q = "select " + prop
						+ " from MSG_DEFAULT where SMS_CODE='" + code + "'";
				this.mLogger.query(printQuery, q);
				try {
					propStr = df.getDefaultValue("DEF_" + prop.trim());
				} catch (Exception e) {
					propStr = "";
				}

				ResultSet rs = stmt.executeQuery(q);
				if (rs != null) {
					if (rs.next()) {
						propStr = rs.getString(prop).trim();
					}

				}

				ral.add(propStr);
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getSelectedValue()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return ral;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting the Description from the REF_TABLE for each
	 * of the field stored in the MSG_DEFAULT table : based on SMS Code --- This
	 * is used in Template Editor - 1 web screen PARAMETER 1 : SMS Code
	 * PARAMETER 2 : Arraylist with all fields that require description RETURNS
	 * : Arraylist with values for fields mentioned in incoming arraylist
	 *******************************************************************************************************/
	public ArrayList getSMSSelectProperty(String code, ArrayList al)
			throws Exception {

		Connection con = getDBConnection();
		ArrayList ral = new ArrayList();
		try {
			Statement stmt = con.createStatement();
			for (int i = 0; i < al.size(); i++) {
				String prop = al.get(i).toString();

				String q = "select MD." + prop + ",RT.OPTION_DESC "
						+ "from MSG_DEFAULT as MD, REF_TABLE as RT "
						+ "where MD." + prop
						+ "=RT.OPTION_CODE and MD.SMS_CODE='" + code
						+ "' and RT.FIELD_NAME = '" + prop
						+ "' and RT.REF_TYPE <> 'SYSADMIN'";
				this.mLogger.query(printQuery, q);
				String propStr = "<option selected value=\"\"> <--Choose--> </option>";
				ResultSet rs = stmt.executeQuery(q);

				if (rs != null) {
					if (rs.next()) {
						propStr = "<option selected value="
								+ rs.getString(prop).trim() + ">"
								+ rs.getString("OPTION_DESC").trim()
								+ "</option>";
					}
				}

				ral.add(propStr);
			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getSMSSelectProperty()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return ral;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting the SMS Code - And its Descriptiuon in the
	 * following example format ( for List Box ) : Example -- 30600 - BIRTHDAY
	 * GREETING PARAMETER 1 : The SMS Code RETURNS : Descriptive String
	 *******************************************************************************************************/
	public String getSMSSelectedDescription(String code) throws Exception {

		Connection con = getDBConnection();
		String smsDesc = "";
		try {
			Statement stmt = con.createStatement();

			String q = "select RT.OPTION_DESC "
					+ "from REF_TABLE as RT "
					+ "where RT.OPTION_CODE = '"
					+ code
					+ "' and RT.FIELD_NAME = 'SMS_CODE' and RT.REF_TYPE <> 'SYSADMIN'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				if (rs.next()) {
					smsDesc += "<option selected value=" + code + ">" + code
							+ " - " + rs.getString("OPTION_DESC").trim()
							+ "</option>";
				}
			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getSMSSelectedDescription()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return smsDesc;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting the Description from the REF_TABLE
	 * specifying field name and its value ( for List Box ) : Example : Field
	 * Name : PRIORITY_CODE code = 1 gives ----------- 1 - HIGH PARAMETER 1 :
	 * Code value PARAMETER 2 : Field Name RETURNS : Descriptive String for drop
	 * down list box
	 *******************************************************************************************************/
	public String getPropertySelectedDescription(String code, String fieldname)
			throws Exception {

		Connection con = getDBConnection();
		String propDesc = "";
		try {
			Statement stmt = con.createStatement();

			String q = "select RT.OPTION_DESC " + "from REF_TABLE as RT "
					+ "where RT.OPTION_CODE = '" + code
					+ "' and RT.FIELD_NAME = '" + fieldname
					+ "' and RT.REF_TYPE <> 'SYSADMIN'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				if (rs.next()) {
					propDesc += "<option selected value=" + code + ">"
							+ rs.getString("OPTION_DESC").trim() + "</option>";
				}
			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getPropertySelectedDescription()",
					e);
		} finally {
			DbBean.closeConnection(con);
		}

		return propDesc;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting the Description from the REF_TABLE
	 * specifying field name and its value ( for Text Box ) : Example : Field
	 * Name : PROCESS_STATUS code = F gives ----------- FAILED PARAMETER 1 :
	 * Code value PARAMETER 2 : Field Name RETURNS : Descriptive String for text
	 * box
	 *******************************************************************************************************/
	public String getPropertyDescription(String code, String fieldname)
			throws Exception {

		String propDesc = "&nbsp;";
		if (code != null || code.length() > 0) {
			Connection con = getDBConnection();
			try {
				Statement stmt = con.createStatement();

				String q = "select RT.OPTION_DESC " + "from REF_TABLE as RT "
						+ "where RT.OPTION_CODE = '" + code
						+ "' and RT.FIELD_NAME = '" + fieldname
						+ "' and RT.REF_TYPE <> 'SYSADMIN'";
				this.mLogger.query(printQuery, q);
				ResultSet rs = stmt.executeQuery(q);

				if (rs != null) {
					if (rs.next()) {
						propDesc = rs.getString("OPTION_DESC").trim();
					}
				}

			} // End of try
			catch (Exception e) {
				DbBean.writeError("searchBean", "getPropertyDescription(2)", e);
			} finally {
				DbBean.closeConnection(con);
			}
		}
		return propDesc;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting the Description from the REF_TABLE
	 * specifying field name , value and the User ( for List Box ) PARAMETER 1 :
	 * Code value PARAMETER 2 : Field Name PARAMETER 3 : User login name RETURNS
	 * : Descriptive String
	 *******************************************************************************************************/
	public String getPropertyDescription(String code, String fieldname,
			String user) throws Exception {

		String propDesc = "&nbsp;";
		if (code != null || code.length() > 0) {
			Connection con = getDBConnection();
			try {
				Statement stmt = con.createStatement();

				String q = "select RT.OPTION_DESC " + "from REF_TABLE as RT "
						+ "where RT.OPTION_CODE = '" + code
						+ "' and RT.FIELD_NAME = '" + fieldname
						+ "' and RT.REF_TYPE = 'SYSADMIN'";
				this.mLogger.query(printQuery, q);
				ResultSet rs = stmt.executeQuery(q);

				if (rs != null) {
					if (rs.next()) {
						propDesc = rs.getString("OPTION_DESC").trim();
					}
				}

			} // End of try
			catch (Exception e) {
				DbBean.writeError("searchBean", "getPropertyDescription(3)", e);
			} finally {
				DbBean.closeConnection(con);
			}
		}
		return propDesc;
	}

	/********************************************************************************************************
	 * PURPOSE : Getting all the values and description specific to the
	 * field_name and storing it in Hash tables --- Used in frequent access
	 * PARAMETER 1 : Field Name ( Example - PROCESS_STATUS ) RETURNS : Hashtable
	 * with errorCode and error description for each code
	 *******************************************************************************************************/
	public Hashtable getPropertiesDescription(String fieldname)
			throws Exception {

		Connection con = getDBConnection();
		Hashtable ht = new Hashtable();
		try {
			Statement stmt = con.createStatement();

			String q = "select RT.OPTION_CODE,RT.OPTION_DESC "
					+ "from REF_TABLE as RT where RT.FIELD_NAME = '"
					+ fieldname + "'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				while (rs.next()) {
					ht.put(rs.getString("OPTION_CODE").trim(), rs.getString(
							"OPTION_DESC").trim());
				}
			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getPropertiesDescription()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return ht;
	}

	/********************************************************************************************************
	 * PURPOSE : Getting all the parameters of the adhoc message from the
	 * SMS_MSG table, based on the Message Batch Reference PARAMETER 1 : Batch
	 * Reference of the Message RETURNS : Arraylist with all parameters
	 * associated with the Batch Reference
	 *******************************************************************************************************/
	public ArrayList getAdhocMessage(String msg_bref) throws Exception {

		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		try {
			Statement stmt = con.createStatement();

			String q = "select * from SMS_MSG where MSG_BREF = '" + msg_bref
					+ "'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int maxlen = rs.getMetaData().getColumnCount();
			int n = 1;
			if (rs != null) {
				String element;
				if (rs.next()) {
					while (n < maxlen) {
						element = "";
						element = rs.getString(n++);
						if (element == null)
							element = "";
						else
							element = element.trim();
						boolean b = tempList.add(element);
					}
				}

			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getAdhocMessage()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting the Description from the REF_TABLE
	 * specifying field name : Used in viewing the details of the fields in
	 * REF_TABLE ( Reference Table - View ) PARAMETER 1 : Field Name for which
	 * the description is required RETURNS : Descriptive string of the field
	 * name
	 *******************************************************************************************************/
	public String getRefTableFieldDescription(String field) throws Exception {

		Connection con = getDBConnection();
		String fieldDesc = "";
		try {
			Statement stmt = con.createStatement();

			String q = "select SHOW_ORDER,OPTION_CODE,OPTION_DESC from REF_TABLE where REF_TYPE <> 'SYSADMIN' and FIELD_NAME='"
					+ field + "' order by SHOW_ORDER";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				while (rs.next()) {
					fieldDesc += "<tr valign=\"middle\"><td align=\"center\" width=\"15%\">"
							+ rs.getInt("SHOW_ORDER")
							+ "</td>"
							+ "<td width=\"20%\">"
							+ rs.getString("OPTION_CODE").trim()
							+ "</td>"
							+ "<td width=\"65%\">"
							+ rs.getString("OPTION_DESC").trim() + "</td></tr>";
				}

			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getRefTableFieldDescription()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return fieldDesc;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for editing the Description of the REF_TABLE fields
	 * specifying field name : Used in editing the details of the fields in
	 * REF_TABLE ( Reference Table - Modify ) : Comes with a hyperlink for
	 * Editing PARAMETER 1 : Field to Edit RETURNS : String decribing the field
	 * name
	 *******************************************************************************************************/

	public String getRefTableFieldToEdit(String field) throws Exception {

		Connection con = getDBConnection();
		StringBuffer fieldDesc = new StringBuffer(1000);
		try {
			Statement stmt = con.createStatement();

			String q = "select SHOW_ORDER,OPTION_CODE,OPTION_DESC from REF_TABLE where REF_TYPE <> 'SYSADMIN' and FIELD_NAME='"
					+ field + "' order by SHOW_ORDER";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				while (rs.next()) {
					String ord = rs.getString("SHOW_ORDER").trim();
					String ss = rs.getString("OPTION_CODE").trim();
					String desc = rs.getString("OPTION_DESC").trim();
					fieldDesc
							.append("<tr valign=\"middle\"><td align=\"center\" width=\"15%\">"
									+ ord
									+ "</td>"
									+ "<td width=\"20%\"><a href=editRefTable.jsp?code="
									+ ss
									+ "&field="
									+ field
									+ ">"
									+ ss
									+ "</a></td>"
									+ "<td width=\"65%\">"
									+ desc
									+ "</td></tr>");
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getRefTableFieldDescription()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return fieldDesc.toString();
	}

	/********************************************************************************************************
	 * PURPOSE : Method for editing the Description of the REF_TABLE fields
	 * specifying field name and field code : Used in editing and modifying the
	 * details of the fields in REF_TABLE ( Reference Table - Modify ) PARAMETER
	 * 1 : The Field Name PARAMETER 2 : The Code value RETURNS : Arraylist with
	 * description of field
	 *******************************************************************************************************/
	public ArrayList getRefTableFieldDescToEdit(String field, String code)
			throws Exception {

		Connection con = getDBConnection();
		ArrayList fieldDesc = new ArrayList();
		try {
			Statement stmt = con.createStatement();

			String q = "select SHOW_ORDER,OPTION_CODE,OPTION_DESC from REF_TABLE where OPTION_CODE = '"
					+ code + "' and FIELD_NAME='" + field + "'";
			this.mLogger.query(printQuery, q);

			ResultSet rs = stmt.executeQuery(q);
			if (rs.next()) {
				fieldDesc.add(rs.getInt("SHOW_ORDER") + "");
				fieldDesc.add(rs.getString("OPTION_CODE"));
				fieldDesc.add(rs.getString("OPTION_DESC"));
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "getRefTableFieldDescription()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return fieldDesc;
	}

	public String getRefTableDesc(String code, String fieldname)
			throws Exception {
		String propDesc = "&nbsp;";
		if (code != null || code.length() > 0) {
			Connection con = getDBConnection();
			try {
				Statement stmt = con.createStatement();

				String q = "select OPTION_DESC from ref_table where option_code = '"
						+ code + "' and field_name = '" + fieldname + "'";
				this.mLogger.query(printQuery, q);
				ResultSet rs = stmt.executeQuery(q);

				if (rs != null) {
					if (rs.next()) {
						propDesc = rs.getString("OPTION_DESC").trim();
					}
				}

			} // End of try
			catch (Exception e) {
				DbBean.writeError("searchBean", "getRefTableDesc", e);
			} finally {
				DbBean.closeConnection(con);
			}
		}
		return propDesc;
	}

	public String getRefTableCode(String desc, String fieldname)
			throws Exception {
		String propDesc = "&nbsp;";
		if (desc != null || desc.length() > 0) {
			Connection con = getDBConnection();
			try {
				Statement stmt = con.createStatement();

				String q = "select OPTION_CODE from ref_table where option_desc = '"
						+ desc + "' and field_name = '" + fieldname + "'";
				this.mLogger.query(printQuery, q);
				ResultSet rs = stmt.executeQuery(q);

				if (rs != null) {
					if (rs.next()) {
						propDesc = rs.getString("OPTION_DESC").trim();
					}
				}

			} // End of try
			catch (Exception e) {
				DbBean.writeError("searchBean", "getRefTableDesc", e);
			} finally {
				DbBean.closeConnection(con);
			}
		}
		return propDesc;
	}

	public String populateListBox(String option) throws Exception {
		Connection con = getDBConnection();
		String fieldDesc = "";

		try {
			Statement stmt = con.createStatement();

			String q = "select OPTION_CODE,OPTION_DESC from REF_TABLE where REF_TYPE <> 'SYSADMIN' and FIELD_NAME='"
					+ option + "' order by SHOW_ORDER";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				while (rs.next()) {
					String ss = rs.getString("OPTION_CODE").trim();
					String desc = rs.getString("OPTION_DESC").trim();
					fieldDesc += "<option value='" + ss + "'>" + desc
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("searchBean", "populateListBox()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return fieldDesc;
	}

}
