package com.track.gates;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;
import com.track.util.StrUtils;
/********************************************************************************************************
 * PURPOSE : Class used in forming the strings of Select Item ( List Box ) of
 * the HTML
 *******************************************************************************************************/
public class selectBean {
	Properties obpr;
	InputStream obip;
	private Boolean printLog = MLoggerConstant.SELECTBEAN_PRINTPLANTMASTERLOG;
	private Boolean printQuery = MLoggerConstant.SELECTBEAN_PRINTPLANTMASTERQUERY;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public selectBean() throws Exception {

		obip = new FileInputStream(new File(MLoggerConstant.PROPS_FOLDER + "/OBJECTS_SQL.properties"));
		obpr = new Properties();
		obpr.load(obip);
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
	 * PURPOSE : Method for forming the Select String ( List Box ) for all the
	 * items specified in the Array List PARAMETER 1 : Arraylist with field
	 * names for which select strings have to be built RETURNS : Arraylist with
	 * select strings for each field in the incoming Arraylist
	 *******************************************************************************************************/
	public ArrayList getSelectString(ArrayList al) throws Exception {

		ArrayList ral = new ArrayList();
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			for (int i = 0; i < al.size(); i++) {
				String field = al.get(i).toString().trim();
				String sql = "select distinct OPTION_CODE,OPTION_DESC,SHOW_ORDER from REF_TABLE where FIELD_NAME = '"
						+ field + "' ORDER BY SHOW_ORDER";
				this.mLogger.query(printQuery, sql);
				ResultSet rs = stmt.executeQuery(sql);
				String selectStr = "";
				if (rs != null) {
					String val;
					while (rs.next()) {
						val = rs.getString("OPTION_CODE").trim();
						selectStr += "<option value=" + val + ">"
								+ rs.getString("OPTION_DESC").trim()
								+ "</option>";
					}

				}
				ral.add(selectStr);
			}

		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getSelectString()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return ral;
	}

	// To retrieve userlevels
	public String getdisnctUserLevels(String unAuthorisedlevels)
			throws Exception {
		ArrayList ral = new ArrayList();
		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q;
			if (unAuthorisedlevels.equalsIgnoreCase("1"))
				q = "select distinct LEVEL_NAME from USER_LEVEL";
			else
				q = "select distinct LEVEL_NAME from USER_LEVEL where (AUTHORISE_BY <>'null' and AUTHORISE_BY <>'0')";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("LEVEL_NAME").trim();
					selectStr += val + ",";
				}
			}

		} catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for getting the SMS Code - And its Descriptiuon in the
	 * following example format ( for List Box ) : 30600 - BIRTHDAY GREETING
	 * PARAMETER 1 : Nil RETURNS : Select string for SMS Code drop down list
	 *******************************************************************************************************/
	public String getSMSCodeSelect() throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();

			String q = "select distinct OPTION_CODE,OPTION_DESC,SHOW_ORDER from REF_TABLE where FIELD_NAME = 'SMS_CODE' ORDER BY SHOW_ORDER";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("OPTION_CODE").trim();
					selectStr += "<option value=" + val + ">" + val + " - "
							+ rs.getString("OPTION_DESC").trim() + "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getSMSCodeSelect()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

	/********************************************************************************************************
	 * PURPOSE : A Method for getting all the user levels both authorised and
	 * un-authorised PARAMETER 1 : String to specify ( authorised - 1,
	 * un-authorised - anyvalue other than 1 ) RETURNS : Select String for User
	 * levels
	 *******************************************************************************************************/
	public String getUserLevels(String unAuthorisedlevels) throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q;
			if (unAuthorisedlevels.equalsIgnoreCase("1"))
				q = "select distinct LEVEL_NAME from USER_LEVEL where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME";
			else
				q = "select distinct LEVEL_NAME from USER_LEVEL where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME ";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("LEVEL_NAME").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

	public String getUserLevels(String unAuthorisedlevels, String plant)
			throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q;
			if (plant.equalsIgnoreCase("track")) {
				plant = "";
			} else {
				plant = plant + "_";
			}
			if (unAuthorisedlevels.equalsIgnoreCase("1")) {
				q = "select distinct LEVEL_NAME from " + "[" + plant
						+ "USER_LEVEL" + "] where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME";
			} else {
				q = "select distinct LEVEL_NAME from "
						+ "["
						+ plant
						+ "USER_LEVEL] where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME ";
			}
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("LEVEL_NAME").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}
	public String getMutiUserLevels(String unAuthorisedlevels,String SystemNow) throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q="";
			//if (unAuthorisedlevels.equalsIgnoreCase("1"))
			//{
				if(SystemNow.equalsIgnoreCase("PAYROLL"))
					q = "select distinct LEVEL_NAME from USER_LEVEL_PAYROLL where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME";
				else if(SystemNow.equalsIgnoreCase("ACCOUNTING"))
					q = "select distinct LEVEL_NAME from USER_LEVEL_ACCOUNTING where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME";
				else
					q = "select distinct LEVEL_NAME from USER_LEVEL where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME";
			//}
			//else
				//q = "select distinct LEVEL_NAME from USER_LEVEL where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME ";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("LEVEL_NAME").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

	public String getMutiUserLevels(String unAuthorisedlevels, String plant,String SystemNow)
			throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q="";
			if (plant.equalsIgnoreCase("track")) {
				plant = "";
			} else {
				plant = plant + "_";
			}
			if (unAuthorisedlevels.equalsIgnoreCase("1")) {
				if(SystemNow.equalsIgnoreCase("PAYROLL")) {
				q = "select distinct LEVEL_NAME from " + "[" + plant
						+ "USER_LEVEL_PAYROLL" + "] where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME";
				} else if(SystemNow.equalsIgnoreCase("ACCOUNTING")) {
					q = "select distinct LEVEL_NAME from " + "[" + plant
					+ "USER_LEVEL_ACCOUNTING" + "] where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME";
				} else {
					q = "select distinct LEVEL_NAME from " + "[" + plant
					+ "USER_LEVEL" + "] where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME";
				}
			} else {
				if(SystemNow.equalsIgnoreCase("PAYROLL")) {
				q = "select distinct LEVEL_NAME from "
						+ "["
						+ plant
						+ "USER_LEVEL_PAYROLL] where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME ";
				} else if(SystemNow.equalsIgnoreCase("ACCOUNTING")) {
					q = "select distinct LEVEL_NAME from "
							+ "["
							+ plant
							+ "USER_LEVEL_ACCOUNTING] where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME ";
				} else {
					q = "select distinct LEVEL_NAME from "
							+ "["
							+ plant
							+ "USER_LEVEL] where LEVEL_NAME<>'MaintenanceGroup' ORDER BY LEVEL_NAME ";
				}
			}
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("LEVEL_NAME").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getMutiUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

			//Get GSt
	public String getGST(String key, String plant)
			throws Exception {
		
		String selectStr = "";
		Connection con = getDBConnection();
		try {
		
			Statement stmt = con.createStatement();
			String q;
			String scondn=" where  a.plant=b.plant and a.gsttype is not null and a.gsttype='"+key+"'";
				q = "select  distinct a.plant,a.value,a.gsttype,a.gstdesc," 
						+" case b.taxby when 'BYORDER' then a.gstpercentage else 0 end gstpercentage"
						+" from " + "[" + plant	+ "_company_config] a,plntmst b ";
				if(key!=null)
					q=q+scondn;
			
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
		
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("gstpercentage").trim();
					selectStr=val;
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
		}
		public String getNOfCatalogs( String plant)
			throws Exception {
		
		String selectStr = "";
		Connection con = getDBConnection();
		try {		
			Statement stmt = con.createStatement();
			String q;
			String scondn=" where PLANT is not null and PLANT='"+plant+"'";
				q = "select  PLANT,PLNTDESC,isnull(NUMBER_OF_CATALOGS,0) NUMBER_OF_CATALOGS,RCBNO from " + "PLNTMST"+scondn;
				
					this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
		
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("NUMBER_OF_CATALOGS").trim();
					selectStr=val;
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getNOfCatalogs()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
		}

	public String getUserLevels(String unAuthorisedlevels, String plant,
			String levelname) throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q;

			if (unAuthorisedlevels.equalsIgnoreCase("1")) {
				q = "select distinct LEVEL_NAME from " + "[" + plant
						+ "USER_LEVEL" + "]" + "where  level_name not in('"
						+ levelname + "') ORDER BY LEVEL_NAME ";
			} else {
				q = "select distinct LEVEL_NAME from "
						+ "["
						+ plant
						+ "USER_LEVEL] where (AUTHORISE_BY <>'null' and AUTHORISE_BY <>'0') and level_name<>'"
						+ levelname
						+ "' ORDER BY LEVEL_NAME ";
			}
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("LEVEL_NAME").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}
	
	public String getUserLevelspayroll(String unAuthorisedlevels, String plant,
			String levelname) throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q;

			if (unAuthorisedlevels.equalsIgnoreCase("1")) {
				q = "select distinct LEVEL_NAME from " + "[" + plant
						+ "USER_LEVEL" + "]" + "where  level_name not in('"
						+ levelname + "') ORDER BY LEVEL_NAME ";
			} else {
				q = "select distinct LEVEL_NAME from "
						+ "["
						+ plant
						+ "USER_LEVEL_PAYROLL] where (AUTHORISE_BY <>'null' and AUTHORISE_BY <>'0') and level_name<>'"
						+ levelname
						+ "' ORDER BY LEVEL_NAME ";
			}
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("LEVEL_NAME").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

	public String getUserLevelsAccounting(String unAuthorisedlevels, String plant,
			String levelname) throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q;

			if (unAuthorisedlevels.equalsIgnoreCase("1")) {
				q = "select distinct LEVEL_NAME from " + "[" + plant
						+ "USER_LEVEL" + "]" + "where  level_name not in('"
						+ levelname + "') ORDER BY LEVEL_NAME ";
			} else {
				q = "select distinct LEVEL_NAME from "
						+ "["
						+ plant
						+ "USER_LEVEL_ACCOUNTING] where (AUTHORISE_BY <>'null' and AUTHORISE_BY <>'0') and level_name<>'"
						+ levelname
						+ "' ORDER BY LEVEL_NAME ";
			}
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("LEVEL_NAME").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}
	
	public String getPlantNames(String plant) throws Exception {
		
		String selectStr = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			String q;
			q = "select distinct PLANT from " + "PLNTMST where plant='" + plant +"'" ;
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("PLANT").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserLevels()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for getting all the User IDs in a List Box PARAMETER 1
	 * : Nil RETURNS : Select String for User ID's
	 *******************************************************************************************************/

	public String getUserIDs() throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			String q = "select distinct USER_ID from USER_INFO";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("USER_ID").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserIDs()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

	public String getUserIDs(String plant) throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q = "select distinct USER_ID from USER_INFO where dept='"
					+ plant + "'";
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("USER_ID").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}

			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getUserIDs()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}
public String getNOfSignatures( String plant)
			throws Exception {
		
		String selectStr = "";
		Connection con = getDBConnection();
		try {		
			Statement stmt = con.createStatement();
			String q;
			String scondn=" where PLANT is not null and PLANT='"+plant+"'";
				q = "select  isnull(NUMBER_OF_SIGNATURES,0) NUMBER_OF_SIGNATURES from " + "PLNTMST"+scondn;
				
					this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
		
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("NUMBER_OF_SIGNATURES").trim();
					selectStr=val;
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("selectBean", "getNOfSignatures()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
		}
}
