package com.track.gates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/********************************************************************************************************
 * PURPOSE : A class for controlling the User Activitites
 *******************************************************************************************************/

public class userBean {
	String Quo = "\"";
	sqlBean sb;
	Generator gn;
	StrUtils strUtils = null;

	Boolean flgPrintLog = MLoggerConstant.USERBEAN_PRINTPLANTMASTERLOG;
	Boolean flgPrintQuery = MLoggerConstant.USERBEAN_PRINTPLANTMASTERQUERY;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public userBean() throws Exception {

		sb = new sqlBean();
		gn = new Generator();
		strUtils = new StrUtils();

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
	 * PURPOSE : A method for Validating the User name for the given User Id and
	 * Password PARAMETER 1 : User ID PARAMETER 2 : Password RETURNS : An
	 * integer identifying the User Status
	 *******************************************************************************************************/

	public int isValidUser(String userid, String pwd) throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		try {
			sb.setmLogger(mLogger);
			Statement stmt = con.createStatement();
			sql = "select USER_NAME,ACCESS_COUNTER,EFFECTIVE_DATE,AUTHORISE_BY,PASSWORD,dept from USER_INFO where USER_ID = '"
					+ userid + "' and PASSWORD = '" + pwd + "'";

			this.mLogger.query(flgPrintQuery, sql);

			rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					String pwd_counter = rs.getString("ACCESS_COUNTER").trim();
					int counter = 0;
					boolean updateFlag = true;
					try {
						counter = Integer.parseInt(pwd_counter);
						if (counter > 3) {
							returnCode = 103; // Password Blocked
						}
					} catch (NumberFormatException nfe) {
						returnCode = 104; // Password New - To be changed since
						// it is 'C'
						updateFlag = false;
					}

					String authoriser = rs.getString("AUTHORISE_BY");
					if (authoriser == null
							|| authoriser.trim().equalsIgnoreCase("0")) {
						returnCode = 102; // User Not Authorised

					}
					String password = rs.getString("PASSWORD").trim();
					if ((!password.equalsIgnoreCase(pwd))
							&& (updateFlag == true)) {
						returnCode = 105; // Wrong Password
						if (counter == 9) {
							counter = 8; // Preventing SQL insert error as the
							// field length is 1 in database

						}
						String q = "Update USER_INFO Set ACCESS_COUNTER = '"
								+ (++counter) + "' where USER_ID = '" + userid
								+ "'";
						
						int n = sb.insertRecords(q);
						if (n != 1) {
							returnCode = 106; // Error in updating the access
							// counter
						}
					} else if (!password.equalsIgnoreCase(pwd)) {
						returnCode = 105; // Wrong Password

					}
				} else {
					returnCode = 101; // Invalid User ID
				}
			}

			if (returnCode == 1) {
				int m = sb
						.insertRecords("Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '"
								+ userid + "'");
				if (m != 1) {
					returnCode = 107; // Error in updating the access counter
				}
			}

		} // End of try
		catch (Exception e) {
			this.mLogger.log(0, "" + e.getMessage());
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}

		return returnCode;
	}

	public int isValidUser(String userid, String pwd, String Company)
			throws Exception {
		String sql;
		PreparedStatement ps = null;
		List<String> args = null;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		try {
			args = new ArrayList<String>();
			sb.setmLogger(mLogger);
			sql = "select a.USER_NAME,a.ACCESS_COUNTER,a.EFFECTIVE_DATE,a.AUTHORISE_BY,a.PASSWORD,a.dept from USER_INFO a where "
					+ "USER_ID = ? and PASSWORD = ? and dept= ?";

			HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();

			loggerDetailsHasMap.put(MLogger.COMPANY_CODE, Company);
			loggerDetailsHasMap.put(MLogger.USER_CODE, userid);

			mLogger.setLoggerConstans(loggerDetailsHasMap);

			this.mLogger.query(flgPrintQuery, sql);
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, pwd);
			ps.setString(3, Company);
			rs = ps.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					String pwd_counter = rs.getString("ACCESS_COUNTER").trim();
					int counter = 0;
					boolean updateFlag = true;
					try {
						counter = Integer.parseInt(pwd_counter);

						if (counter > 3) {
							returnCode = 103; // Password Blocked
						}
					} catch (NumberFormatException nfe) {
						returnCode = 104; // Password New - To be changed since
						updateFlag = false;
					}

					String authoriser = rs.getString("AUTHORISE_BY");
					if (authoriser == null
							|| authoriser.trim().equalsIgnoreCase("0")) {
						returnCode = 102; // User Not Authorised

					}
					String password = rs.getString("PASSWORD").trim();
					if ((!password.equalsIgnoreCase(pwd))
							&& (updateFlag == true)) {
						returnCode = 105; // Wrong Password
						if (counter == 9) {
							counter = 8; // Preventing SQL insert error as the
							// field length is 1 in database

						}
						String q = "Update USER_INFO Set ACCESS_COUNTER = '"
								+ (++counter) + "' where USER_ID = '" + userid
								+ "'" + "and dept='" + Company + "'";

						int n = sb.insertRecords(q);
						if (n != 1) {
							returnCode = 106; // Error in updating the access
							// counter
						}
					} else if (!password.equalsIgnoreCase(pwd)) {
						returnCode = 105; // Wrong Password

					}
				} else {
					returnCode = 101; // Invalid User ID
				}
			}

			if (returnCode == 1) {
				//System.out.println("Inside Access counter");
				int m = sb
						.insertRecords("Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '"
								+ userid + "'" + "and dept='" + Company + "'");
				if (m != 1) {
					returnCode = 107; // Error in updating the access counter
				}
			}

		} // End of try
		catch (Exception e) {
			this.mLogger.log(0, "" + e.getMessage());

		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}

		return returnCode;
	}
	
	public int isValidMobileUser(String userid, String pwd,String Company) throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		try {
			sb.setmLogger(mLogger);
			Statement stmt = con.createStatement();
//			if(plant.length()>0)
//			TABLENAME = "["+plant+"_"+TABLENAME+"]";
			sql = "select EMAIL,USER_ID,PASSWORD,PLANT,ISMEMBER from "+"["+Company+"_"+"CUSTMST] a where USER_ID = '"
				+ userid
				+ "' and PASSWORD = '"
				+ pwd
				
				+ "' and ISMEMBER = '"
				+ "Y"
				+ "'";
			
			this.mLogger.query(flgPrintQuery, sql);
			
			rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					
					int counter = 0;
					boolean updateFlag = true;
					
					
				String password = rs.getString("PASSWORD").trim();
					if ((password.equalsIgnoreCase(pwd))
							) {
						returnCode = 104; // Correct Password
						
						
					} else if (!password.equalsIgnoreCase(pwd)) {
						returnCode = 105; // Wrong Password

					}
				} else {
					returnCode = 101; // Invalid User ID
				}
			}
			else {
				returnCode = 101; // Invalid User ID
			}
		

		} // End of try
		catch (Exception e) {
			this.mLogger.log(0, "" + e.getMessage());
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}

		return returnCode;
	}
	public int isValidUser(String userid, String pwd, String Company,
			String expirydate) throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		try {
			sb.setmLogger(mLogger);
			Statement stmt = con.createStatement();
			sql = "select a.USER_NAME,a.ACCESS_COUNTER,a.EFFECTIVE_DATE,a.AUTHORISE_BY,a.PASSWORD,a.dept,b.expirydate from USER_INFO a,plntmst b where a.USER_ID = '"
					+ userid
					+ "' and a.PASSWORD = '"
					+ pwd
					+ "'"
					+ "and a.dept='"
					+ Company
					+ "'"
					+ "and b.expirydate<='"
					+ expirydate + "'";

			this.mLogger.query(flgPrintQuery, sql);
			rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					String pwd_counter = rs.getString("ACCESS_COUNTER").trim();
					int counter = 0;
					boolean updateFlag = true;
					try {
						counter = Integer.parseInt(pwd_counter);
						if (counter > 3) {
							returnCode = 103; // Password Blocked
						}
					} catch (NumberFormatException nfe) {
						returnCode = 104; // Password New - To be changed since
						updateFlag = false;
					}

					String authoriser = rs.getString("AUTHORISE_BY");
					if (authoriser == null
							|| authoriser.trim().equalsIgnoreCase("0")) {
						returnCode = 102; // User Not Authorised

					}
					String password = rs.getString("PASSWORD").trim();
					if ((!password.equalsIgnoreCase(pwd))
							&& (updateFlag == true)) {
						returnCode = 105; // Wrong Password
						if (counter == 9) {
							counter = 8; // Preventing SQL insert error as the
							// field length is 1 in database
						}
						String q = "Update USER_INFO Set ACCESS_COUNTER = '"
								+ (++counter) + "' where USER_ID = '" + userid
								+ "'" + "and dept='" + Company + "'";

						int n = sb.insertRecords(q);
						if (n != 1) {
							returnCode = 106; // Error in updating the access
						}
					} else if (!password.equalsIgnoreCase(pwd)) {
						returnCode = 105; // Wrong Password

					}
				} else {
					returnCode = 101; // Invalid User ID
				}
			}

			if (returnCode == 1) {
				//System.out.println("Inside Access counter");
				int m = sb
						.insertRecords("Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '"
								+ userid + "'" + "and dept='" + Company + "'");
				if (m != 1) {
					returnCode = 107; // Error in updating the access counter
				}
			}

		} // End of try
		catch (Exception e) {

			this.mLogger.log(0, "" + e.getMessage());
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}

		return returnCode;
	}
	/**
	 * Used when the request comes from accounting module
	 * @param userid
	 * @param Company
	 * @param expirydate
	 * @return
	 * @throws Exception
	 */
	public int validateUser(String userid, String Company) throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		try {
			sb.setmLogger(mLogger);
			Statement stmt = con.createStatement();
			sql = "select a.USER_NAME,a.ACCESS_COUNTER,a.EFFECTIVE_DATE,a.AUTHORISE_BY,a.PASSWORD,a.dept,b.expirydate from USER_INFO a,plntmst b where a.USER_ID = '"
					+ userid
					+ "'"
					+ "and a.dept='"
					+ Company
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);
			rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					String pwd_counter = rs.getString("ACCESS_COUNTER").trim();
					int counter = 0;
					boolean updateFlag = true;
					try {
						counter = Integer.parseInt(pwd_counter);
						if (counter > 3) {
							returnCode = 103; // Password Blocked
						}
					} catch (NumberFormatException nfe) {
						returnCode = 104; // Password New - To be changed since
						updateFlag = false;
					}

					String authoriser = rs.getString("AUTHORISE_BY");
					if (authoriser == null
							|| authoriser.trim().equalsIgnoreCase("0")) {
						returnCode = 102; // User Not Authorised

					}
				} else {
					returnCode = 101; // Invalid User ID
				}
			}

			if (returnCode == 1) {
				//System.out.println("Inside Access counter");
				int m = sb
						.insertRecords("Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '"
								+ userid + "'" + "and dept='" + Company + "'");
				if (m != 1) {
					returnCode = 107; // Error in updating the access counter
				}
			}

		} // End of try
		catch (Exception e) {

			this.mLogger.log(0, "" + e.getMessage());
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}

		return returnCode;
	}

	/********************************************************************************************************
	 * PURPOSE : A Method for getting the User Level for the given User ID
	 * PARAMETER 1 : The User ID RETURNS : The User level
	 *******************************************************************************************************/

	public String getUserLevel(String userid) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();

			sql = "select USER_NAME,USER_LEVEL from USER_INFO where USER_ID = '"
					+ userid + "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("USER_LEVEL").trim();
				}
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevel()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}

	public String getUserLevel(String userid, String company) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		try {

			sql = "select USER_NAME,USER_LEVEL from USER_INFO where USER_ID = ? and dept = ? ";

			this.mLogger.query(flgPrintQuery, sql);
			
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, company);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("USER_LEVEL").trim();
				}
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevel()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
	
	public String getUserLevelAcct(String userid, String company) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		try {

			sql = "select USER_NAME,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING from USER_INFO where USER_ID = ? and dept = ? ";

			this.mLogger.query(flgPrintQuery, sql);
			
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, company);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("USER_LEVEL_ACCOUNTING").trim();
				}
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevelAcct()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
	
	public String getUserLevelPay(String userid, String company) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		try {

			sql = "select USER_NAME,ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL from USER_INFO where USER_ID = ? and dept = ? ";

			this.mLogger.query(flgPrintQuery, sql);
			
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, company);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("USER_LEVEL_PAYROLL").trim();
				}
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevelPay()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}

	// TO get Department
	public String getDepartment(String userid) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select DEPT from USER_INFO where USER_ID = '" + userid + "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("DEPT").trim();
				}
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDepartment()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}

	public String getExpiryDate(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select EXPIRYDATE from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("EXPIRYDATE").trim();
				}
			}
                        //Hard coded for comapny DKP
                       // name = "2012-02-25";
			if (name == null || name.equals("")) {
				throw new Exception("Expiry date is null in DB");
			}
                        

		} catch (Exception e) {
			DbBean.writeError("userBean", "getDepartment()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}

	public String getAuthstat(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();

			sql = "select AUTHSTAT from PLNTMST where PLANT = '" + plant + "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {

				if (rs.next()) {
					name = rs.getString("AUTHSTAT").trim();
				}

			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getAuthStat()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}

	public String getStartDate(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select STARTDATE from PLNTMST where PLANT = '" + plant + "'";
			this.mLogger.query(flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("STARTDATE").trim();
				}
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDepartment()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for getting all the URLs associated with a user level
	 * PARAMETER 1 : The User level name RETURNS : Arraylist with URLs
	 *******************************************************************************************************/

	public ArrayList getUserLevelLinks(String level_name) throws Exception {

		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();

		try {
			Statement stmt = con.createStatement();
			String sql = "select ul.URL_NAME,ul.REMARKS,ul.AUTHORISE_BY,ul.AUTHORISE_ON from USER_LEVEL ul ,USER_MENU um where ul.LEVEL_NAME = '"
					+ level_name
					+ "' AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1";
			this.mLogger.query(flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			if (rs != null) {
				String remark = "", authorise_by = "", authorise_on = "";
				String element;
				while (rs.next()) {
					element = "";
					element = rs.getString(1);
					if (element == null) {
						element = "";
					} else {
						element = element.trim();
					}
					boolean b = tempList.add(element);
					remark = rs.getString(2);
					authorise_by = rs.getString(3);
					authorise_on = rs.getString(4);
				}
				if (remark == null) {
					remark = "";
				} else {
					remark = remark.trim();
				}
				if (authorise_by == null) {
					authorise_by = "";
				} else {
					authorise_by = authorise_by.trim();
				}
				if (authorise_on == null) {
					authorise_on = "";
				} else {
					authorise_on = gn.getDB2UserDateTime(authorise_on);
				}
				tempList.add(remark);
				tempList.add(authorise_by);
				tempList.add(authorise_on);
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevelLinks()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	public ArrayList getUserLevelLinks(String level_name, String plant)
			throws Exception {
		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		try {
			Statement stmt = con.createStatement();
			String sql = "select ul.URL_NAME,ul.REMARKS,ul.AUTHORISE_BY,ul.AUTHORISE_ON from "
					+ "["
					+ plant
					+ "USER_LEVEL] ul ,"
					+ "["
					+ plant
					+ "USER_MENU] um where ul.LEVEL_NAME = '"
					+ level_name
					+ "' AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1";

			ResultSet rs = stmt.executeQuery(sql);
			this.mLogger.query(flgPrintQuery, sql);
			if (rs != null) {
				String remark = "", authorise_by = "", authorise_on = "";
				String element;
				while (rs.next()) {
					element = "";
					element = rs.getString(1);
					if (element == null) {
						element = "";
					} else {
						element = element.trim();
					}
					boolean b = tempList.add(element);
					remark = rs.getString(2);
					authorise_by = rs.getString(3);
					authorise_on = rs.getString(4);
				}
				if (remark == null) {
					remark = "";
				} else {
					remark = remark.trim();
				}
				if (authorise_by == null) {
					authorise_by = "";
				} else {
					authorise_by = authorise_by.trim();
				}
				if (authorise_on == null) {
					authorise_on = "";
				} else {
					authorise_on = gn.getDB2UserDateTime(authorise_on);
				}
				tempList.add(remark);
				tempList.add(authorise_by);
				tempList.add(authorise_on);
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevelLinks()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}
	
	public ArrayList getUserLevelLinkspayroll(String level_name, String plant)
			throws Exception {
		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		try {
			Statement stmt = con.createStatement();
			String sql = "select ul.URL_NAME,ul.REMARKS,ul.AUTHORISE_BY,ul.AUTHORISE_ON from "
					+ "["
					+ plant
					+ "USER_LEVEL_PAYROLL] ul ,"
					+ "["
					+ plant
					+ "USER_MENU_PAYROLL] um where ul.LEVEL_NAME = '"
					+ level_name
					+ "' AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1";

			ResultSet rs = stmt.executeQuery(sql);
			this.mLogger.query(flgPrintQuery, sql);
			if (rs != null) {
				String remark = "", authorise_by = "", authorise_on = "";
				String element;
				while (rs.next()) {
					element = "";
					element = rs.getString(1);
					if (element == null) {
						element = "";
					} else {
						element = element.trim();
					}
					boolean b = tempList.add(element);
					remark = rs.getString(2);
					authorise_by = rs.getString(3);
					authorise_on = rs.getString(4);
				}
				if (remark == null) {
					remark = "";
				} else {
					remark = remark.trim();
				}
				if (authorise_by == null) {
					authorise_by = "";
				} else {
					authorise_by = authorise_by.trim();
				}
				if (authorise_on == null) {
					authorise_on = "";
				} else {
					authorise_on = gn.getDB2UserDateTime(authorise_on);
				}
				tempList.add(remark);
				tempList.add(authorise_by);
				tempList.add(authorise_on);
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevelLinks()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}
	
	public ArrayList getUserLevelLinksAccounting(String level_name, String plant)
			throws Exception {
		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		try {
			Statement stmt = con.createStatement();
			String sql = "select ul.URL_NAME,ul.REMARKS,ul.AUTHORISE_BY,ul.AUTHORISE_ON from "
					+ "["
					+ plant
					+ "USER_LEVEL_ACCOUNTING] ul ,"
					+ "["
					+ plant
					+ "USER_MENU_ACCOUNTING] um where ul.LEVEL_NAME = '"
					+ level_name
					+ "' AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1";

			ResultSet rs = stmt.executeQuery(sql);
			this.mLogger.query(flgPrintQuery, sql);
			if (rs != null) {
				String remark = "", authorise_by = "", authorise_on = "";
				String element;
				while (rs.next()) {
					element = "";
					element = rs.getString(1);
					if (element == null) {
						element = "";
					} else {
						element = element.trim();
					}
					boolean b = tempList.add(element);
					remark = rs.getString(2);
					authorise_by = rs.getString(3);
					authorise_on = rs.getString(4);
				}
				if (remark == null) {
					remark = "";
				} else {
					remark = remark.trim();
				}
				if (authorise_by == null) {
					authorise_by = "";
				} else {
					authorise_by = authorise_by.trim();
				}
				if (authorise_on == null) {
					authorise_on = "";
				} else {
					authorise_on = gn.getDB2UserDateTime(authorise_on);
				}
				tempList.add(remark);
				tempList.add(authorise_by);
				tempList.add(authorise_on);
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevelLinks()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	public ArrayList getGroupListSummary(Hashtable ht, String aPlant,
			String aGroup) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		sCondition = " ORDER BY LEVEL_NAME ";
		try {

			String sql = "";
			if (aPlant.equalsIgnoreCase("track")) {

				sql = "select distinct ul.LEVEL_NAME from USER_LEVEL ul  where ul.LEVEL_NAME like '"
						+ aGroup + "%'  and ul.PLANT='" + aPlant + "'";

			} else {
				sql = "select distinct ul.LEVEL_NAME from " + "[" + aPlant
						+ "_" + "USER_LEVEL] ul  where ul.LEVEL_NAME like '"
						+ aGroup + "%' AND   ul.LEVEL_NAME<>'MaintenanceGroup' AND ul.PLANT='" + aPlant + "'";

			}
			this.mLogger.query(flgPrintQuery, sql);
			arrList = selectForReport(sql, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getGroupListSummary:"
					+ e.toString());
		}
		return arrList;
	}
	
	public ArrayList getGroupListSummarypayroll(Hashtable ht, String aPlant,
			String aGroup) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		sCondition = " ORDER BY LEVEL_NAME ";
		try {

			String sql = "";
			if (aPlant.equalsIgnoreCase("track")) {

				sql = "select distinct ul.LEVEL_NAME from USER_LEVEL ul  where ul.LEVEL_NAME like '"
						+ aGroup + "%'  and ul.PLANT='" + aPlant + "'";

			} else {
				sql = "select distinct ul.LEVEL_NAME from " + "[" + aPlant
						+ "_" + "USER_LEVEL_PAYROLL] ul  where ul.LEVEL_NAME like '"
						+ aGroup + "%' AND   ul.LEVEL_NAME<>'MaintenanceGroup' AND ul.PLANT='" + aPlant + "'";

			}
			this.mLogger.query(flgPrintQuery, sql);
			arrList = selectForReport(sql, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getGroupListSummary:"
					+ e.toString());
		}
		return arrList;
	}
	
	
	public ArrayList getGroupListSummaryAccounting(Hashtable ht, String aPlant,
			String aGroup) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		sCondition = " ORDER BY LEVEL_NAME ";
		try {

			String sql = "";
			if (aPlant.equalsIgnoreCase("track")) {

				sql = "select distinct ul.LEVEL_NAME from USER_LEVEL ul  where ul.LEVEL_NAME like '"
						+ aGroup + "%'  and ul.PLANT='" + aPlant + "'";

			} else {
				sql = "select distinct ul.LEVEL_NAME from " + "[" + aPlant
						+ "_" + "USER_LEVEL_ACCOUNTING] ul  where ul.LEVEL_NAME like '"
						+ aGroup + "%' AND   ul.LEVEL_NAME<>'MaintenanceGroup' AND ul.PLANT='" + aPlant + "'";

			}
			this.mLogger.query(flgPrintQuery, sql);
			arrList = selectForReport(sql, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getGroupListSummary:"
					+ e.toString());
		}
		return arrList;
	}
	
	public boolean updateuserpassword(String ID,String password) {
		
		Connection con = null;
		boolean insertedItemImage = false;
		try {
			con = DbBean.getConnection();
			String query = "update USER_INFO set PASSWORD ='"+password+"' where UI_PKEY ='"+ID+"'";
			
			this.mLogger.query(this.flgPrintQuery, query);
			PreparedStatement ps = con.prepareStatement(query);
			int iCnt = ps.executeUpdate();
			
			if (iCnt > 0)
				insertedItemImage = true;
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}	
		}
		
		return insertedItemImage;
	}
	
	public String getidbyuserid(String name,String extCond) throws Exception {
		Connection con = null;
		String uid = "";
		try {
			Hashtable ht = new Hashtable();
			ht.put("USER_ID", name);
			
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT UI_PKEY from [USER_INFO]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.flgPrintQuery, sql.toString());
		
			Map m = getRowOfData(con, sql.toString());
			uid = (String) m.get("UI_PKEY");
			if (uid == "" || uid == null)
				uid = "";
		}catch (Exception e) {
			this.mLogger.exception(this.flgPrintLog, "", e);
			
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}	
		}
		return uid;
	}	

	
	public Map<String, String> getRowOfData(Connection conn, String query) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> map = new HashMap<>();

		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				map = new HashMap<>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));

				}

			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return map;
	}


	public boolean checkuserpassword(String user_id) throws Exception {

		Connection con = getDBConnection();
		
		boolean b = false;
		String q;
		try {
			Statement stmt = con.createStatement();
			q ="select USER_NAME from USER_INFO where USER_ID = '"+user_id+"'";
			//AND password = '"+ pwd+"'";
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			b = rs.next();
		} catch (Exception e) {
			DbBean.writeError("userBean", "checkuserpassword", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}

	public ArrayList getUserListSummary(Hashtable ht, String aPlant,
			String aCompany, String aUserId) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			String sql = "";
			if (aPlant.equalsIgnoreCase("track")) {

				sql = "select  USER_ID,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ACCESS_COUNTER,USER_STATUS,ENROLLED_BY,"
						+ "ENROLLED_ON,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,"
						+ "ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING,"
						+ "ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL,ISNULL(WEB_ACCESS,0) WEB_ACCESS,ISNULL(ISACCESSOWNERAPP,0) ISACCESSOWNERAPP,"
						+ "ISNULL(MANAGER_APP_ACCESS,0) MANAGER_APP_ACCESS,ISNULL(RIDER_APP_ACCESS,0) RIDER_APP_ACCESS,ISNULL(ISACCESS_STOREAPP,0) ISACCESS_STOREAPP,"
						+ "ISNULL(ISADMIN,0) AS ISADMIN,ISNULL(ISPURCHASEAPPROVAL,0) AS ISPURCHASEAPPROVAL,ISNULL(ISSALESAPPROVAL,0) AS ISSALESAPPROVAL,"
						+ "ISNULL(ISPURCHASERETAPPROVAL,0) AS ISPURCHASERETAPPROVAL,ISNULL(ISSALESRETAPPROVAL,0) AS ISSALESRETAPPROVAL,ISNULL(IMAGEPATH,'') IMAGEPATH from USER_INFO   where DEPT  like  '"
						+ aCompany
						+ "%' and USER_ID like '"
						+ aUserId
						+ "%' ";
			} else {
				sql = "select  USER_ID,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ACCESS_COUNTER,USER_STATUS,ENROLLED_BY,"
						+ "ENROLLED_ON,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,"
						+ "ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING,"
						+ "ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL,ISNULL(WEB_ACCESS,0) WEB_ACCESS,ISNULL(ISACCESSOWNERAPP,0) ISACCESSOWNERAPP,"
						+ "ISNULL(MANAGER_APP_ACCESS,0) MANAGER_APP_ACCESS,ISNULL(RIDER_APP_ACCESS,0) RIDER_APP_ACCESS,ISNULL(ISACCESS_STOREAPP,0) ISACCESS_STOREAPP,"
						+ "ISNULL(ISADMIN,0) AS ISADMIN,ISNULL(ISPURCHASEAPPROVAL,0) AS ISPURCHASEAPPROVAL,ISNULL(ISSALESAPPROVAL,0) AS ISSALESAPPROVAL,"
						+ "ISNULL(ISPURCHASERETAPPROVAL,0) AS ISPURCHASERETAPPROVAL,ISNULL(ISSALESRETAPPROVAL,0) AS ISSALESRETAPPROVAL,ISNULL(IMAGEPATH,'') IMAGEPATH from USER_INFO   where DEPT = '"
						+ aCompany
						+ "' and USER_ID like '"
						+ aUserId
						+ "%' and User_ID not in('218202180','20212022','trackmaint')" + "and dept!='track'";
			}

			this.mLogger.query(flgPrintQuery, sql);
			arrList = selectForReport(sql, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getUserListSummary:"
					+ e.toString());
		}
		return arrList;
	}
	
	

	public ArrayList getUserListforCompany(Hashtable ht, String aPlant,
			String aCompany, String aUserId) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			String sql = "";
			if (aPlant.equalsIgnoreCase("track")) {

				sql = "select  USER_ID,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(IMAGEPATH,'') AS IMAGEPATH from USER_INFO   where DEPT  like  '"
						+ aCompany
						+ "%' and USER_ID like '"
						+ aUserId
						//+ "%' and User_ID != '218202180' ";
						+ "%'  ";
			} else {
				sql = "select  USER_ID,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(IMAGEPATH,'') AS IMAGEPATH from USER_INFO   where DEPT = '"
						+ aPlant
						+ "' and USER_ID like '"
						+ aUserId
						+ "%' and User_ID not in('218202180','20212022','trackmaint')" + "and dept!='track'";
			}

			this.mLogger.query(flgPrintQuery, sql);
			arrList = selectForReport(sql, ht, " order by dept,user_id ");

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getUserListSummary:"
					+ e.toString());
		}
		return arrList;
	}

	public ArrayList getUserListforCompany(Hashtable ht, String aPlant,
			String aCompany, String aUserId, String userlevel) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			String q = "";
			if (aPlant.equalsIgnoreCase("track")) {

				q = "select  USER_ID,UI_PKEY,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING,ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL from USER_INFO   where DEPT  like  '"
						+ aCompany
						+ "%' and USER_ID like '"
						+ aUserId
						+ "%'"
						+ "and USER_LEVEL like '"
						+ userlevel
						+ "%'";
						//+ "and User_ID != '218202180' ";
						
			} else {
				q = "select  USER_ID,UI_PKEY,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING,ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL from USER_INFO   where DEPT = '"
						+ aPlant
						+ "' and USER_ID like '"
						+ aUserId
						+ "%'"
						+ "and USER_LEVEL like '"
						+ userlevel
						+ "%'"
						+ " and User_ID not in('218202180','20212022','trackmaint','brt@track.com')" + " and dept!='track'";
			}

			this.mLogger.query(flgPrintQuery, q);
			arrList = selectForReport(q, ht, " order by dept,user_id ");

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getUserListSummary:"
					+ e.toString());
		}
		return arrList;
	}
	
	
	public ArrayList getUserListforCompanyAcc(Hashtable ht, String aPlant,
			String aCompany, String aUserId, String userlevel) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			String q = "";
			if (aPlant.equalsIgnoreCase("track")) {

				q = "select  USER_ID,UI_PKEY,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING,ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL from USER_INFO   where DEPT  like  '"
						+ aCompany
						+ "%' and USER_ID like '"
						+ aUserId
						+ "%'"
						+ "and USER_LEVEL_ACCOUNTING like '"
						+ userlevel
						+ "%'";
						//+ "and User_ID != '218202180' ";
						
			} else {
				q = "select  USER_ID,UI_PKEY,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING,ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL from USER_INFO   where DEPT = '"
						+ aPlant
						+ "' and USER_ID like '"
						+ aUserId
						+ "%'"
						+ "and USER_LEVEL_ACCOUNTING like '"
						+ userlevel
						+ "%'"
						+ " and User_ID not in('218202180','20212022','trackmaint')" + " and dept!='track'";
			}

			this.mLogger.query(flgPrintQuery, q);
			arrList = selectForReport(q, ht, " order by dept,user_id ");

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getUserListSummary:"
					+ e.toString());
		}
		return arrList;
	}
	
	
	
	public ArrayList getUserListforCompanyPay(Hashtable ht, String aPlant,
			String aCompany, String aUserId, String userlevel) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			String q = "";
			if (aPlant.equalsIgnoreCase("track")) {

				q = "select  USER_ID,UI_PKEY,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING,ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL from USER_INFO   where DEPT  like  '"
						+ aCompany
						+ "%' and USER_ID like '"
						+ aUserId
						+ "%'"
						+ "and USER_LEVEL_PAYROLL like '"
						+ userlevel
						+ "%'";
						//+ "and User_ID != '218202180' ";
						
			} else {
				q = "select  USER_ID,UI_PKEY,USER_NAME,USER_LEVEL,DEPT,PASSWORD,USER_NAME,ISNULL(REMARKS,'')REMARKS,ISNULL(RANK,'')RANK,ISNULL(DESGINATION,'')DESGINATION,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,EFFECTIVE_DATE,USER_LEVEL,ISNULL(USER_LEVEL_ACCOUNTING,'DefaultGroup') USER_LEVEL_ACCOUNTING,ISNULL(USER_LEVEL_PAYROLL,'DefaultGroup') USER_LEVEL_PAYROLL from USER_INFO   where DEPT = '"
						+ aPlant
						+ "' and USER_ID like '"
						+ aUserId
						+ "%'"
						+ "and USER_LEVEL_PAYROLL like '"
						+ userlevel
						+ "%'"
						+ " and User_ID not in('218202180','20212022','trackmaint')" + " and dept!='track'";
			}

			this.mLogger.query(flgPrintQuery, q);
			arrList = selectForReport(q, ht, " order by dept,user_id ");

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getUserListSummary:"
					+ e.toString());
		}
		return arrList;
	}
	
	

	public ArrayList getUserListCompany(Hashtable ht, String aPlant,
			String aCompany) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			String q = "";
			if (aPlant.equalsIgnoreCase("track")) {
				q = "select distinct DEPT from USER_INFO   where DEPT like '"
						+ aCompany + "%'";
			} else {
				q = "select distinct DEPT from USER_INFO   where DEPT = '"
						+ aPlant + "'" + "and user_id not in('218202180','20212022')";
			}
			this.mLogger.query(flgPrintQuery, q);
			arrList = selectForReport(q, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean:  getUserListCompany:"
					+ e.toString());
		}
		return arrList;
	}

	public ArrayList getUserListUserId(Hashtable ht, String aPlant,
			String aCompany, String aUserId, String USER_LEVEL) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		sCondition = " ORDER BY DEPT, USER_ID ";
		try {

			String q = "";
			String a="";
			if (!USER_LEVEL.equalsIgnoreCase("")){
			a=" and USER_LEVEL='"+USER_LEVEL+"'";
			}
			if (aPlant.equalsIgnoreCase("track")) {
				q = "select distinct DEPT,USER_ID from USER_INFO   where DEPT like '"
						+ aCompany + "' and USER_ID  like '" + aUserId + "%'";
			} else {
				q = "select distinct DEPT,USER_ID from USER_INFO   where DEPT = '"
						+ aPlant
						+ "' and USER_ID  like '"
						+ aUserId
						+ "%'"
						+ "and user_id not in('218202180','20212022','trackmaint')"
						+a;
			}

			this.mLogger.query(flgPrintQuery, q);
			arrList = selectForReport(q, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean:  getUserListUserId:"
					+ e.toString());
		}
		return arrList;
	}

	public ArrayList getUserListDetails(Hashtable ht, String aPlant,
			String aCompany) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			String q = "";
			q = "select  DEPT from USER_INFO   where DEPT like '" + aCompany
					+ "%'";
			this.mLogger.query(flgPrintQuery, q);
			arrList = selectForReport(q, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean:  getUserListCompany:"
					+ e.toString());
		}
		return arrList;
	}

	public ArrayList selectForReport(String query, Hashtable ht, String extCond)
			throws Exception {

		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				conditon = formCondition(ht);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}

			this.mLogger.query(flgPrintQuery, sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
	}

	public String formCondition(Hashtable ht) {
		String sCondition = "";
		try {
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			System.out.println("Exception userBean:" + e.getMessage());
		}
		return sCondition;
	}

	public ArrayList selectData(Connection conn, String query) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		Map map = null;
		ArrayList arrayList = new ArrayList();
		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				map = new HashMap();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));
				}
				arrayList.add(map);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return arrayList;
	}
	
	public int getUserMenuPK(String userMenu) throws Exception{
		int result=0;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
		String q="select UM_PKEY from USER_MENU where URL_NAME='"+userMenu+"'";
		ps=con.prepareStatement(q);
		rs=ps.executeQuery();
		while(rs.next()) {
			result=rs.getInt("UM_PKEY");
		}
		rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DbBean.closeConnection(con,ps);
		}
		return result;
		
	}
	
	public int getUserMenuACCPK(String userMenu) throws Exception{
		int result=0;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
		String q="select UM_PKEY from USER_MENU_ACCOUNTING where URL_NAME='"+userMenu+"'";
		ps=con.prepareStatement(q);
		rs=ps.executeQuery();
		while(rs.next()) {
			result=rs.getInt("UM_PKEY");
		}
		rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DbBean.closeConnection(con,ps);
		}
		return result;
		
	}
	
	public int getUserMenuPKpayroll(String userMenu, String plant) throws Exception{
		int result=0;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
		String q="select UM_PKEY from "+plant+"_USER_MENU_PAYROLL where URL_NAME='"+userMenu+"'";
		ps=con.prepareStatement(q);
		rs=ps.executeQuery();
		while(rs.next()) {
			result=rs.getInt("UM_PKEY");
		}
		rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DbBean.closeConnection(con,ps);
		}
		return result;
		
	}

	/********************************************************************************************************
	 * PURPOSE : A method for getting the User Menu Drop Downs PARAMETER 1 :
	 * Level Name RETURNS : Arraylist containing
	 *******************************************************************************************************/

	public ArrayList getDropDownMenu(String level_name) throws Exception {

		Connection con = getDBConnection();
		Statement stmt = con.createStatement();
		ArrayList al = new ArrayList();
		try {

			for (int i = 1; i <= 13; i++) {
				String q = "select TEXT,URL from USER_MENU where URL_NAME in (select URL_NAME from USER_LEVEL where LEVEL_NAME = '"
						+ level_name
						+ "' and AUTHORISE_BY <> '0' and URL_NAME not like 'pda%') and (COL="
						+ i + ") AND STATUS=1 order by COL,ROW";

				//this.mLogger.query(flgPrintQuery, q);

				Hashtable menu = new Hashtable(12);
				ResultSet rs = stmt.executeQuery(q);
				if (rs != null) {
					String url_text, url;
					int m = 0;
					while (rs.next()) {
						Hashtable subHash = new Hashtable();

						url_text = rs.getString("TEXT").trim();
						url = rs.getString("URL").trim();

						subHash.put(url_text, url);
						menu.put(new Integer(m++), subHash);
					}
				}
				rs.close();
				al.add(menu);

			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDropDownMenu()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
	}
	
	public ArrayList getDropDownMenuWithRowColSequence(String level_name, String company)
			throws Exception {

		Connection con = getDBConnection();
		Statement stmt = con.createStatement();
		ArrayList al = new ArrayList();
		try {

			for (int i = 1; i <= 77; i++) {
				String q = "select COL,TEXT,URL from "
						+ "["
						+ (company.equalsIgnoreCase("track") ? "" : company
								+ "_")
						+ "USER_MENU] where URL_NAME in (select URL_NAME from "
						+ "["
						+ (company.equalsIgnoreCase("track") ? "" : company
						+ "_")
						+ "USER_LEVEL] where LEVEL_NAME = '"
						+ level_name
						+ "' and AUTHORISE_BY <> '0' and URL_NAME not like 'pda%') and (ROW="
						+ i + ") AND STATUS=1 AND SEQUENCE IS NOT NULL order by COL,SEQUENCE";
				//this.mLogger.query(flgPrintQuery, q);
				Hashtable menu = new Hashtable(12);
				ResultSet rs = stmt.executeQuery(q);
				if (rs != null) {
					String url_text, url;
					int col;
					int m = 0;//	Column number
					Map subHash = new LinkedHashMap();
					while (rs.next()) {
						col = rs.getInt("COL");
						if(m == 0) {
							subHash = new LinkedHashMap();
						}else if (m != col) {
							menu.put(m, subHash);
							subHash = new LinkedHashMap();
						}
						m = col;
						url_text = rs.getString("TEXT").trim();
						url = rs.getString("URL").trim();
						subHash.put(url_text, url);
					}
					if (m > 0) {
						menu.put(m, subHash);
					}
				}
				rs.close();
				al.add(menu);

			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDropDownMenu()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
		}
	
	public ArrayList getDropDownMenuWithRowColSequencePayroll(String level_name, String company)
			throws Exception {

		Connection con = getDBConnection();
		Statement stmt = con.createStatement();
		ArrayList al = new ArrayList();
		try {

			for (int i = 1; i <= 77; i++) {
				String q = "select COL,TEXT,URL from "
						+ "["
						+ (company.equalsIgnoreCase("track") ? "" : company
								+ "_")
						+ "USER_MENU_PAYROLL] where URL_NAME in (select URL_NAME from "
						+ "["
						+ (company.equalsIgnoreCase("track") ? "" : company
						+ "_")
						+ "USER_LEVEL_PAYROLL] where LEVEL_NAME = '"
						+ level_name
						+ "' and AUTHORISE_BY <> '0' and URL_NAME not like 'pda%') and (ROW="
						+ i + ") AND STATUS=1 AND SEQUENCE IS NOT NULL order by COL,SEQUENCE";
				//this.mLogger.query(flgPrintQuery, q);
				Hashtable menu = new Hashtable(12);
				ResultSet rs = stmt.executeQuery(q);
				if (rs != null) {
					String url_text, url;
					int col;
					int m = 0;//	Column number
					Map subHash = new LinkedHashMap();
					while (rs.next()) {
						col = rs.getInt("COL");
						if(m == 0) {
							subHash = new LinkedHashMap();
						}else if (m != col) {
							menu.put(m, subHash);
							subHash = new LinkedHashMap();
						}
						m = col;
						url_text = rs.getString("TEXT").trim();
						url = rs.getString("URL").trim();
						subHash.put(url_text, url);
					}
					if (m > 0) {
						menu.put(m, subHash);
					}
				}
				rs.close();
				al.add(menu);

			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDropDownMenu()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
		}
	
	
	public ArrayList getDropDownMenuWithRowColSequenceAccounting(String level_name, String company)
			throws Exception {

		Connection con = getDBConnection();
		Statement stmt = con.createStatement();
		ArrayList al = new ArrayList();
		try {

			for (int i = 1; i <= 77; i++) {
				String q = "select COL,TEXT,URL from "
						+ "["
						+ (company.equalsIgnoreCase("track") ? "" : company
								+ "_")
						+ "USER_MENU_ACCOUNTING] where URL_NAME in (select URL_NAME from "
						+ "["
						+ (company.equalsIgnoreCase("track") ? "" : company
						+ "_")
						+ "USER_LEVEL_ACCOUNTING] where LEVEL_NAME = '"
						+ level_name
						+ "' and AUTHORISE_BY <> '0' and URL_NAME not like 'pda%') and (ROW="
						+ i + ") AND STATUS=1 AND SEQUENCE IS NOT NULL order by COL,SEQUENCE";
				//this.mLogger.query(flgPrintQuery, q);
				Hashtable menu = new Hashtable(12);
				ResultSet rs = stmt.executeQuery(q);
				if (rs != null) {
					String url_text, url;
					int col;
					int m = 0;//	Column number
					Map subHash = new LinkedHashMap();
					while (rs.next()) {
						col = rs.getInt("COL");
						if(m == 0) {
							subHash = new LinkedHashMap();
						}else if (m != col) {
							menu.put(m, subHash);
							subHash = new LinkedHashMap();
						}
						m = col;
						url_text = rs.getString("TEXT").trim();
						url = rs.getString("URL").trim();
						subHash.put(url_text, url);
					}
					if (m > 0) {
						menu.put(m, subHash);
					}
				}
				rs.close();
				al.add(menu);

			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDropDownMenu()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
		}

	public ArrayList getDropDownMenu(String level_name, String company)
	throws Exception {

Connection con = getDBConnection();
Statement stmt = con.createStatement();
ArrayList al = new ArrayList();
try {

	for (int i = 1; i <= 13; i++) {
		String q = "select TEXT,URL from "
				+ "["
				+ company
				+ "_"
				+ "USER_MENU] where URL_NAME in (select URL_NAME from "
				+ "["
				+ company
				+ "_"
				+ "USER_LEVEL] where LEVEL_NAME = '"
				+ level_name
				+ "' and AUTHORISE_BY <> '0' and URL_NAME not like 'pda%') and (COL="
				+ i + ") AND STATUS=1 order by COL,ROW";
		//this.mLogger.query(flgPrintQuery, q);
		Hashtable menu = new Hashtable(12);
		ResultSet rs = stmt.executeQuery(q);
		if (rs != null) {
			String url_text, url;
			int m = 0;
			while (rs.next()) {
				Hashtable subHash = new Hashtable();

				url_text = rs.getString("TEXT").trim();
				url = rs.getString("URL").trim();

				subHash.put(url_text, url);
				menu.put(new Integer(m++), subHash);
			}
		}
		rs.close();
		al.add(menu);

	}
} catch (Exception e) {
	DbBean.writeError("userBean", "getDropDownMenu()", e);
} finally {
	DbBean.closeConnection(con);
}

return al;
}
	
	public ArrayList getDropDownMenuPayroll(String level_name, String company)
			throws Exception {

		Connection con = getDBConnection();
		Statement stmt = con.createStatement();
		ArrayList al = new ArrayList();
		try {

			for (int i = 1; i <= 13; i++) {
				String q = "select TEXT,URL from "
						+ "["
						+ company
						+ "_"
						+ "USER_MENU_PAYROLL] where URL_NAME in (select URL_NAME from "
						+ "["
						+ company
						+ "_"
						+ "USER_LEVEL_PAYROLL] where LEVEL_NAME = '"
						+ level_name
						+ "' and AUTHORISE_BY <> '0' and URL_NAME not like 'pda%') and (COL="
						+ i + ") AND STATUS=1 order by COL,ROW";
				//this.mLogger.query(flgPrintQuery, q);
				Hashtable menu = new Hashtable(12);
				ResultSet rs = stmt.executeQuery(q);
				if (rs != null) {
					String url_text, url;
					int m = 0;
					while (rs.next()) {
						Hashtable subHash = new Hashtable();

						url_text = rs.getString("TEXT").trim();
						url = rs.getString("URL").trim();

						subHash.put(url_text, url);
						menu.put(new Integer(m++), subHash);
					}
				}
				rs.close();
				al.add(menu);

			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDropDownMenu()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
		}

	
	public ArrayList getDropDownMenuAccounting(String level_name, String company)
			throws Exception {

		Connection con = getDBConnection();
		Statement stmt = con.createStatement();
		ArrayList al = new ArrayList();
		try {

			for (int i = 1; i <= 13; i++) {
				String q = "select TEXT,URL from "
						+ "["
						+ company
						+ "_"
						+ "USER_MENU_ACCOUNTING] where URL_NAME in (select URL_NAME from "
						+ "["
						+ company
						+ "_"
						+ "USER_LEVEL_ACCOUNTING] where LEVEL_NAME = '"
						+ level_name
						+ "' and AUTHORISE_BY <> '0' and URL_NAME not like 'pda%') and (COL="
						+ i + ") AND STATUS=1 order by COL,ROW";
				//this.mLogger.query(flgPrintQuery, q);
				Hashtable menu = new Hashtable(12);
				ResultSet rs = stmt.executeQuery(q);
				if (rs != null) {
					String url_text, url;
					int m = 0;
					while (rs.next()) {
						Hashtable subHash = new Hashtable();

						url_text = rs.getString("TEXT").trim();
						url = rs.getString("URL").trim();

						subHash.put(url_text, url);
						menu.put(new Integer(m++), subHash);
					}
				}
				rs.close();
				al.add(menu);

			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDropDownMenu()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
		}
	
	
	public String getPdaMenu(String level_name) throws Exception {
		Connection con = getDBConnection();
		String result = "";
		String link = "";
		Statement stmt = con.createStatement();
		try {

			String q = "select TEXT,URL from USER_MENU where URL_NAME in (select URL_NAME from USER_LEVEL where LEVEL_NAME = '"
					+ level_name
					+ "' and AUTHORISE_BY <> '0' and URL_NAME like 'pda%' ) order by COL,ROW";

			this.mLogger.query(flgPrintQuery, q);

			ResultSet rs = stmt.executeQuery(q);

			while (rs.next()) {
				String text = rs.getString("TEXT").trim();
				String url = rs.getString("URL").trim();
				link = "<input class=\"pdabutton\" type=" + Quo + "button"
						+ Quo + " value=" + Quo + text + Quo + "onClick=" + Quo
						+ "window.location.href='" + url + "'" + Quo + ">";
				result += "<tr>" + "<td align=\"center\">" + link
						+ "</td></tr>";
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getPdaMenu(String level_name)", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String getPdaMenu(String level_name, String company)
			throws Exception {
		Connection con = getDBConnection();
		String result = "";
		String link = "";
		Statement stmt = con.createStatement();
		try {

			String q = "select TEXT,URL from "
					+ "["
					+ company
					+ "_"
					+ "USER_MENU] where URL_NAME in (select URL_NAME from "
					+ "["
					+ company
					+ "_"
					+ "USER_LEVEL] where LEVEL_NAME = '"
					+ level_name
					+ "' and AUTHORISE_BY <> '0' and URL_NAME like 'pda%' ) order by COL,ROW";

			this.mLogger.query(flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			while (rs.next()) {
				String text = rs.getString("TEXT").trim();
				String url = rs.getString("URL").trim();
				link = "<input class=\"pdabutton\" type=" + Quo + "button"
						+ Quo + " value=" + Quo + text + Quo + "onClick=" + Quo
						+ "window.location.href='" + url + "'" + Quo + ">";
				result += "<tr>" + "<td align=\"center\">" + link
						+ "</td></tr>";
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getPdaMenu(String level_name)", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	/********************************************************************************************************
	 * PURPOSE : A Method for checking if the User Level or User Name is already
	 * available PARAMETER 1 : User Level or User Name PARAMETER 2 : 0 - User
	 * ID, Other than 0 for Level Name RETURNS : True if already
	 * userName/userLevel is available else false
	 *******************************************************************************************************/
	public boolean isAlreadyAvail(String name, int n) throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; // Switch sql statement based on integer parameter 'n'
			if (n == 0) {
				sql = "select USER_NAME  from USER_INFO where USER_ID='" + name
						+ "'";
			} else {
				sql = "select LEVEL_NAME from USER_LEVEL where LEVEL_NAME='"
						+ name + "'";

			}
			this.mLogger.query(this.flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			b = rs.next();
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}

	public boolean isAlreadyAvail(String name, int n, String plant)
			throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; // Switch sql statement based on integer parameter 'n'
			if (n == 0) {
				sql = "select USER_NAME  from USER_INFO where USER_ID='" + name
						+ "'" + "and dept ='" + plant + "'";
			} else {
				if (plant.equalsIgnoreCase("track"))
					sql = "select LEVEL_NAME from " + "["
							+ "USER_LEVEL] where LEVEL_NAME='" + name + "'"
							+ "and plant ='" + plant + "'";
				else
					sql = "select LEVEL_NAME from " + "[" + plant + "_"
							+ "USER_LEVEL] where LEVEL_NAME='" + name + "'"
							+ "and plant ='" + plant + "'";
			}
			this.mLogger.query(flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);

			b = rs.next();
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}
	
	public boolean isAlreadyAvailpayroll(String name, int n, String plant)
			throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; // Switch sql statement based on integer parameter 'n'
			if (n == 0) {
				sql = "select USER_NAME  from USER_INFO where USER_ID='" + name
						+ "'" + "and dept ='" + plant + "'";
			} else {
				if (plant.equalsIgnoreCase("track"))
					sql = "select LEVEL_NAME from " + "["
							+ "USER_LEVEL] where LEVEL_NAME='" + name + "'"
							+ "and plant ='" + plant + "'";
				else
					sql = "select LEVEL_NAME from " + "[" + plant + "_"
							+ "USER_LEVEL_PAYROLL] where LEVEL_NAME='" + name + "'"
							+ "and plant ='" + plant + "'";
			}
			this.mLogger.query(flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);

			b = rs.next();
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}
	
	public boolean isAlreadyAvailaccounting(String name, int n, String plant)
			throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; // Switch sql statement based on integer parameter 'n'
			if (n == 0) {
				sql = "select USER_NAME  from USER_INFO where USER_ID='" + name
						+ "'" + "and dept ='" + plant + "'";
			} else {
				if (plant.equalsIgnoreCase("track"))
					sql = "select LEVEL_NAME from " + "["
							+ "USER_LEVEL] where LEVEL_NAME='" + name + "'"
							+ "and plant ='" + plant + "'";
				else
					sql = "select LEVEL_NAME from " + "[" + plant + "_"
							+ "USER_LEVEL_ACCOUNTING] where LEVEL_NAME='" + name + "'"
							+ "and plant ='" + plant + "'";
			}
			this.mLogger.query(flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);

			b = rs.next();
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}

	/********************************************************************************************************
	 * PURPOSE : A Method for deleting the user levels PARAMETER 1 : Level Name
	 * to be deleted PARAMETER 2 : LevelUser is set to 1 if any user using the
	 * level name need to be checked RETURNS : Number of records deleted in that
	 * User Level
	 *******************************************************************************************************/
	public int deletePreviousRecords(String level_name, String levelUser)
			throws Exception {

		if (levelUser.equalsIgnoreCase("1")) {

			Connection con = getDBConnection();
			boolean b = false;
			try {
				Statement stmt = con.createStatement();

				String sql = "select user_id  from USER_INFO where USER_LEVEL='"
						+ level_name + "'";
				this.mLogger.query(this.flgPrintQuery, sql);
				ResultSet rs = stmt.executeQuery(sql);
				b = rs.next();

			} // End of try
			catch (Exception e) {
				DbBean.writeError("userBean", "deletePreviousRecords()", e);
			} finally {
				DbBean.closeConnection(con);
			}
			if (b) {
				return -2;
			}

		}

		String q = "delete from USER_LEVEL where LEVEL_NAME = '" + level_name
				+ "'";
		this.mLogger.query(this.flgPrintQuery, q);
		int n = sb.insertRecords(q);

		return n;
	}

	public int deletePreviousRecords(String level_name, String levelUser,
			String plant) throws Exception {

		if (levelUser.equalsIgnoreCase("1")) {

			Connection con = getDBConnection();
			boolean b = false;
			String sql = "";
			try {
				Statement stmt = con.createStatement();

				sql = "select user_id  from USER_INFO where USER_LEVEL='"
						+ level_name + "'" + "and dept='" + plant + "'";
				this.mLogger.query(this.flgPrintQuery, sql);
				ResultSet rs = stmt.executeQuery(sql);
				b = rs.next();

			} // End of try
			catch (Exception e) {
				DbBean.writeError("userBean", "deletePreviousRecords()", e);
			} finally {
				DbBean.closeConnection(con);
			}
			if (b) {
				return -2;
			}

		}
		if (plant.equalsIgnoreCase("track")) {
			plant = "";
		} else {
			plant = plant + "_";
		}

		String q = "delete from " + "[" + plant
				+ "USER_LEVEL] where LEVEL_NAME = '" + level_name + "'";
		this.mLogger.query(this.flgPrintQuery, q);
		int n = sb.insertRecords(q);

		return n;
	}
	
	public int deletePreviousRecordspayroll(String level_name, String levelUser,
			String plant) throws Exception {

		if (levelUser.equalsIgnoreCase("1")) {

			Connection con = getDBConnection();
			boolean b = false;
			String sql = "";
			try {
				Statement stmt = con.createStatement();

				sql = "select user_id  from USER_INFO where USER_LEVEL='"
						+ level_name + "'" + "and dept='" + plant + "'";
				this.mLogger.query(this.flgPrintQuery, sql);
				ResultSet rs = stmt.executeQuery(sql);
				b = rs.next();

			} // End of try
			catch (Exception e) {
				DbBean.writeError("userBean", "deletePreviousRecords()", e);
			} finally {
				DbBean.closeConnection(con);
			}
			if (b) {
				return -2;
			}

		}
		if (plant.equalsIgnoreCase("track")) {
			plant = "";
		} else {
			plant = plant + "_";
		}

		String q = "delete from " + "[" + plant
				+ "USER_LEVEL_PAYROLL] where LEVEL_NAME = '" + level_name + "'";
		this.mLogger.query(this.flgPrintQuery, q);
		int n = sb.insertRecords(q);

		return n;
	}
	
	public int deletePreviousRecordsaccounting(String level_name, String levelUser,
			String plant) throws Exception {

		if (levelUser.equalsIgnoreCase("1")) {

			Connection con = getDBConnection();
			boolean b = false;
			String sql = "";
			try {
				Statement stmt = con.createStatement();

				sql = "select user_id  from USER_INFO where USER_LEVEL='"
						+ level_name + "'" + "and dept='" + plant + "'";
				this.mLogger.query(this.flgPrintQuery, sql);
				ResultSet rs = stmt.executeQuery(sql);
				b = rs.next();

			} // End of try
			catch (Exception e) {
				DbBean.writeError("userBean", "deletePreviousRecords()", e);
			} finally {
				DbBean.closeConnection(con);
			}
			if (b) {
				return -2;
			}

		}
		if (plant.equalsIgnoreCase("track")) {
			plant = "";
		} else {
			plant = plant + "_";
		}

		String q = "delete from " + "[" + plant
				+ "USER_LEVEL_ACCOUNTING] where LEVEL_NAME = '" + level_name + "'";
		this.mLogger.query(this.flgPrintQuery, q);
		int n = sb.insertRecords(q);

		return n;
	}


	/********************************************************************************************************
	 * PURPOSE : A method for building an sql string to delete user PARAMETER 1
	 * : User ID to be deleted RETURNS : Sql String for deleting the User ID
	 *******************************************************************************************************/
	public String getDeleteUserString(String user_id) throws Exception {

		String q = "delete from USER_INFO where USER_ID = '" + user_id + "'";
		return q;
	}

	public String getDeleteUserString(String user_id, String plant)
			throws Exception {

		String q = "delete from USER_INFO where USER_ID = '" + user_id + "'"
				+ "and dept='" + plant + "'";
		return q;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for getting the user details based on User ID or User
	 * Name PARAMETER 1 : User Name or User ID is passed PARAMETER 2 : Option
	 * specifying whether it is Name or ID ( 1 for Name, Other than 1 for ID )
	 * RETURNS : User Details in a Arraylist
	 *******************************************************************************************************/
	public ArrayList getUserDetails(String user_ref, int opt) throws Exception {

		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		String q = "";
		try {

			Statement stmt = con.createStatement();
			if (opt == 1) {
				q = "select USER_ID,PASSWORD,USER_NAME,RANK,REMARKS,EFFECTIVE_DATE,USER_LEVEL,AUTHORISE_BY,AUTHORISE_ON from USER_INFO where USER_NAME = '"
						+ user_ref + "'";
			} else {
				q = "select USER_ID,PASSWORD,USER_NAME,RANK,REMARKS,EFFECTIVE_DATE,USER_LEVEL,AUTHORISE_BY,AUTHORISE_ON from USER_INFO where USER_ID = '"
						+ user_ref + "'";

			}
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int maxlen = rs.getMetaData().getColumnCount();
			int n = 1;
			if (rs != null) {
				String element;
				if (rs.next()) {
					while (n <= maxlen) {
						element = "";
						element = rs.getString(n++);
						if (element == null) {
							element = "";
						} else {
							element = element.trim();
						}
						boolean b = tempList.add(element);
					}
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("userBean", "getUserDetails()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	public ArrayList getUserDetails(String user_ref, int opt, String dept)
			throws Exception {
		Connection con = getDBConnection();
		ArrayList tempList = new ArrayList();
		String q = "";
		try {

			Statement stmt = con.createStatement();
			if (opt == 1) {
				q = "select USER_ID,PASSWORD,USER_NAME,RANK,REMARKS,EFFECTIVE_DATE,USER_LEVEL,AUTHORISE_BY,AUTHORISE_ON from USER_INFO where USER_NAME = '"
						+ user_ref + "'" + "and dept='" + dept + "'";
			} else {
				q = "select USER_ID,PASSWORD,USER_NAME,RANK,REMARKS,EFFECTIVE_DATE,USER_LEVEL,AUTHORISE_BY,AUTHORISE_ON from USER_INFO where USER_ID = '"
						+ user_ref + "'" + "and dept='" + dept + "'";

			}
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int maxlen = rs.getMetaData().getColumnCount();
			int n = 1;
			if (rs != null) {
				String element;
				if (rs.next()) {
					while (n <= maxlen) {
						element = "";
						element = rs.getString(n++);
						if (element == null) {
							element = "";
						} else {
							element = element.trim();
						}
						boolean b = tempList.add(element);
					}
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("userBean", "getUserDetails()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return tempList;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for changing the user password PARAMETER 1 : The User
	 * Id PARAMETER 2 : New Password PARAMETER 3 : Old Password RETURNS : 1 if
	 * password is changed Successfully else - 2
	 *******************************************************************************************************/

	public int changePassword(String user_id, String newPwd, String oldPwd)
			throws Exception {

		Connection con = getDBConnection();
		int n;
		boolean b = false;
		String q;

		try {
			Statement stmt = con.createStatement();
			q = "select USER_NAME from USER_INFO where PASSWORD = '" + oldPwd
					+ "' and USER_ID = '" + user_id + "'";
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			b = rs.next();

		} // End of try
		catch (Exception e) {
			DbBean.writeError("userBean", "changePassword()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		if (b) {
			q = "update USER_INFO set PASSWORD = '" + newPwd
					+ "',ACCESS_COUNTER = '0' where USER_ID  = '" + user_id
					+ "'";
			n = sb.insertRecords(q);
			return n;
		} else {
			return -2;
		}
	}

	/********************************************************************************************************
	 * PURPOSE : A method for changing the user password PARAMETER 1 : The User
	 * Id PARAMETER 2 : New Password PARAMETER 3 : Old Password RETURNS : 1 if
	 * password is changed Successfully else - 2
	 *******************************************************************************************************/

	public int changePassword(String user_id, String newPwd, String oldPwd,
			String plant) throws Exception {

		Connection con = getDBConnection();
		int n;
		boolean b = false;
		String q;
		try {
			Statement stmt = con.createStatement();
			q = "select USER_NAME from USER_INFO where PASSWORD = '" + oldPwd
					+ "' and USER_ID = '" + user_id + "'" + "and dept ='"
					+ plant + "'";
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			b = rs.next();
		} catch (Exception e) {
			DbBean.writeError("userBean", "changePassword()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		if (b) {
			q = "update USER_INFO set PASSWORD = '" + newPwd
					+ "',ACCESS_COUNTER = '0' where USER_ID  = '" + user_id
					+ "'" + "and dept ='" + plant + "'";
			this.mLogger.query(this.flgPrintQuery, q);
			n = sb.insertRecords(q);
			return n;
		} else {
			return -2;
		}
	}
	
	public int changePasswordUponLogin(String user_id, String newPwd,
			String plant) throws Exception {

		Connection con = getDBConnection();
		int n;
		boolean b = false;
		String q;
		/*try {
			Statement stmt = con.createStatement();
			q = "select USER_NAME from USER_INFO where PASSWORD = '" + oldPwd
					+ "' and USER_ID = '" + user_id + "'" + "and dept ='"
					+ plant + "'";
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			b = rs.next();
		} catch (Exception e) {
			DbBean.writeError("userBean", "changePassword()", e);
		} finally {
			DbBean.closeConnection(con);
		}*/

		//if (b) {
			q = "update USER_INFO set PASSWORD = '" + newPwd
					+ "',ACCESS_COUNTER = '0' where USER_ID  = '" + user_id
					+ "'" + "and dept ='" + plant + "'";
			this.mLogger.query(this.flgPrintQuery, q);
			n = sb.insertRecords(q);
			return n;
		//} else {
		//	return -2;
		//}
	}

	/********************************************************************************************************
	 * PURPOSE : A method for getting all the unauthorised Users for
	 * authorisation PARAMETER 1 : User Name RETURNS : Returns String containing
	 * result of all un-authorised Users ( except those created by this user )
	 *******************************************************************************************************/
	public String getUsersToAuthorise(String user) throws Exception {

		Connection con = getDBConnection();
		String userStr = "";
		try {
			Statement stmt = con.createStatement();

			String q = "select USER_ID,USER_NAME,USER_LEVEL,DEPT,EFFECTIVE_DATE,UPDATED_BY from USER_INFO where UPDATED_BY <> '"
					+ user
					+ "' and (AUTHORISE_BY ='null' or AUTHORISE_BY ='0') order by UPDATED_ON";
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			if (rs != null) {
				while (rs.next()) {
					String user_id = rs.getString("USER_ID").trim();
					String uname = rs.getString("USER_NAME").trim();
					String ulev = rs.getString("USER_LEVEL").trim();
					String udept = rs.getString("DEPT").trim();
					String eff_dt = rs.getString("EFFECTIVE_DATE").trim();
					String upby = rs.getString("UPDATED_BY").trim();
					eff_dt = gn.getDB2UserDate(eff_dt); // Converting to user
					// readable format
					userStr += "<tr valign=\"middle\"><td align=\"center\" width=\"7%\">"
							+ n++
							+ ". </td>"
							+ "<td width=\"3%\" align=\"center\"><input type=\"checkbox\" name=\"USER_ID\" value=\""
							+ user_id
							+ "\"></td>"
							+ "<td width=\"25%\"><b><a href=maintUser.jsp?view=&LEVEL_NAME="
							+ user_id.replace(' ', '+')
							+ ">"
							+ user_id
							+ "</a></td>"
							+ "<td width=\"25%\"><b>"
							+ uname
							+ "</td>"
							+ "<td width=\"20%\"><b>"
							+ ulev
							+ "</td>"
							+ "<td width=\"20%\"><b>"
							+ udept
							+ "</td>"
							+ "<td width=\"15%\"><b>"
							+ eff_dt
							+ "</td>"
							+ "<td width=\"15%\"><b>"
							+ upby
							+ "</td></tr>";

				}

			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUsersToAuthorise()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return userStr;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for getting all the unauthorised User Levels for
	 * authorisation PARAMETER 1 : The User Name RETURNS : Returns String
	 * containing result of all un-authorised levels ( except those created by
	 * this user )
	 *******************************************************************************************************/
	public String getUserLevelsToAuthorise(String user) throws Exception {

		Connection con = getDBConnection();
		String userStr = "";
		try {
			Statement stmt = con.createStatement();

			String q = "select distinct LEVEL_NAME,UPDATED_BY,UPDATED_ON from USER_LEVEL where UPDATED_BY <> '"
					+ user
					+ "' and (AUTHORISE_BY ='null' or AUTHORISE_BY ='0') order by UPDATED_ON";

			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			if (rs != null) {
				while (rs.next()) {
					String levelname = rs.getString("LEVEL_NAME");
					String updated_by = rs.getString("UPDATED_BY");
					String updated_on = rs.getString("UPDATED_ON");
					userStr += "<tr valign=\"middle\"><td align=\"center\" width=\"9%\">"
							+ n++
							+ ". </td>"
							+ "<td width=\"1%\" align=\"center\"><input type=\"checkbox\" name=\"LEVEL_NAME\" value=\""
							+ levelname
							+ "\"></td>"
							+ "<td width=\"40%\"><b><a href=maintUserLevel.jsp?view=&LEVEL_NAME="
							+ levelname.replace(' ', '+')
							+ ">"
							+ levelname
							+ "</a></td>"
							+ "<td width=\"25%\"><b>"
							+ updated_by
							+ "</td>"
							+ "<td width=\"25%\"><b>"
							+ updated_on + "</td></tr>";
				}
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevelsToAuthorise()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return userStr;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for getting the record count from the USER_LEVEL table
	 * for the given level name Usage : Used in deleting or updation of user
	 * level -- That is if the count = 0 then the level can be deleted else it
	 * is assumed that some user is using this level and hence can not be
	 * deleted PARAMETER 1 : The Level Name RETURNS : Number of User levels
	 * using this level name
	 *******************************************************************************************************/

	public int getRecordCount(String level_name) throws Exception {

		Connection con = getDBConnection();
		int count = 0;
		try {
			Statement stmt = con.createStatement();

			String q = "select count(*)  from USER_LEVEL where LEVEL_NAME = '"
					+ level_name + "'";
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				if (rs.next()) {
					count = rs.getInt(1);

				}
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getRecordCount()", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return count;
	}

	public List getPdaUser_Level(String level_name, String level1, String level2)
			throws Exception {
		Connection con = null;
		String result = "";
		String link = "";
		Statement stmt = null;
		List menuList = new ArrayList();
		try {
			con = getDBConnection();
			stmt = con.createStatement();
			String q = "SELECT URL FROM USER_MENU WHERE URL_NAME IN "
					+ "(SELECT URL_NAME FROM USER_LEVEL WHERE LEVEL_NAME IN ("
					+ "SELECT USER_LEVEL FROM USER_INFO WHERE USER_ID = '"
					+ level_name + "')" + "AND URL_NAME LIKE 'PDA%' AND COL ='"
					+ level1 + "' AND ROW = '" + level2 + "')";
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String url = strUtils.fString(rs.getString("URL"));
				menuList.add(new String(url));
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getPdaMenu(String level_name)", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return menuList;
	}
	
	public ArrayList get_pda_user_menu_access(String plant,String userid) 
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		String q = "SELECT URL,STATUS FROM " + "[" + plant
				+ "_USER_MENU] WHERE URL_NAME IN "
				+ "(SELECT URL_NAME FROM " + "[" + plant
				+ "_USER_LEVEL] WHERE LEVEL_NAME IN ("
				+ "SELECT USER_LEVEL FROM USER_INFO WHERE DEPT='"+plant+"' and  USER_ID = '"
				+ userid + "')" + "AND URL_NAME IN ('pdainboundReceipt','pdaMiscReceipt','pdaMiscIssue','pdaOutBoundIssue','pdaInventoryQry','pdaLocTransfer','pdaStockTake','pdaSalesPick','pdaSalesIssue','pdaPickIssue','pdaSalesOrderCheck') AND COL ='1')";
		
		
		try {
			con = com.track.gates.DbBean.getConnection();
			DbBean.writeError("PlantMstDAO", "get_pda_user_menu_access",  q);
			System.out.println(q.toString());
			alData = selectData(con, q);
		} catch (Exception e) {
			DbBean.writeError("PlantMstDAO", "get_pda_user_menu_access", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		
		return alData;
	}

	/*public List get_pda_user_menu_access(String plant,String userid) throws Exception {

		Connection con = null;
		String result = "";
		String link = "";
		Statement stmt = null;
		List menuList = new ArrayList();
		try {
			con = getDBConnection();
			stmt = con.createStatement();
			String q = "SELECT URL,STATUS FROM " + "[" + plant
					+ "_USER_MENU] WHERE URL_NAME IN "
					+ "(SELECT URL_NAME FROM " + "[" + plant
					+ "_USER_LEVEL] WHERE LEVEL_NAME IN ("
					+ "SELECT USER_LEVEL FROM USER_INFO WHERE DEPT='"+plant+"' and  USER_ID = '"
					+ userid + "')" + "AND URL_NAME IN ('pdainboundReceipt','pdaMiscIssue','pdaMiscIssue','pdaOutBoundIssue','pdaInventoryQry','pdaLocTransfer','pdaStockTake') AND COL ='1')";
			this.mLogger.query(this.flgPrintQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String url = strUtils.fString(rs.getString("URL"));
				menuList.add(new String(url));
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getPdaMenu(String level_name)", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return menuList;
	}*/
        
    public boolean isValidUserID(String userid,  String Company)
                    throws Exception {
            String sql;

            Connection con = getDBConnection();
            ResultSet rs = null;
           boolean isValid=false;
            try {
                    sb.setmLogger(mLogger);
                    Statement stmt = con.createStatement();
                    sql = "select a.USER_NAME,a.dept from USER_INFO a where USER_ID = '"
                                    + userid
                                    + "'"
                                    + "and dept='"
                                    + Company + "'";

                    HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();

                    loggerDetailsHasMap.put(MLogger.COMPANY_CODE, Company);
                    loggerDetailsHasMap.put(MLogger.USER_CODE, userid);

                    mLogger.setLoggerConstans(loggerDetailsHasMap);

                    this.mLogger.query(flgPrintQuery, sql);
                    rs = stmt.executeQuery(sql);

                    if (rs != null) {
                            if (rs.next()) {
                                   
                        isValid =true;           
                                    
                    }
                    }
                   

            } // End of try
            catch (Exception e) {
                    this.mLogger.log(0, "" + e.getMessage());

            } finally {
                    rs.close();
                    DbBean.closeConnection(con);
            }

            return isValid;
    }
    
    public boolean isValidUserUIPKEYID(String userid,  String Company)
    		throws Exception {
    	String sql;
    	
    	Connection con = getDBConnection();
    	ResultSet rs = null;
    	boolean isValid=false;
    	try {
    		sb.setmLogger(mLogger);
    		Statement stmt = con.createStatement();
    		sql = "select a.USER_NAME,a.dept from USER_INFO a where UI_PKEY = '"
    				+ userid
    				+ "'"
    				+ "and dept='"
    				+ Company + "'";
    		
    		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
    		
    		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, Company);
    		loggerDetailsHasMap.put(MLogger.USER_CODE, userid);
    		
    		mLogger.setLoggerConstans(loggerDetailsHasMap);
    		
    		this.mLogger.query(flgPrintQuery, sql);
    		rs = stmt.executeQuery(sql);
    		
    		if (rs != null) {
    			if (rs.next()) {
    				
    				isValid =true;           
    				
    			}
    		}
    		
    		
    	} // End of try
    	catch (Exception e) {
    		this.mLogger.log(0, "" + e.getMessage());
    		
    	} finally {
    		rs.close();
    		DbBean.closeConnection(con);
    	}
    	
    	return isValid;
    }
    
    /**
	 * Author Bruhan. 15 Aug 2012
	 * method : getBaseCurrency(String plant) description : gets the base currency of the given plant.)
	 * 
	 * @param : String plant
	 * @return : String
	 * @throws Exception
	 */
    
    public String getBaseCurrency(String plant) throws Exception {

		String baseCurrency = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select BASE_CURRENCY from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					baseCurrency = rs.getString("BASE_CURRENCY").trim();
				}
			}
			if (baseCurrency == null || baseCurrency.equals("")) {
				throw new Exception("Base Currency is not exists for the company");
			}
                        

		} catch (Exception e) {
			DbBean.writeError("userBean", "getBaseCurrency()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return baseCurrency;
	}
    
    public String getTaxByLable(String plant) throws Exception {

		String taxbylabel = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(TAXBYLABEL,'') TAXBYLABEL from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					taxbylabel = rs.getString("TAXBYLABEL").trim();
				}
			}
			if (taxbylabel == null || taxbylabel.equals("")) {
				throw new Exception("Tax By Label is not exists for the company");
			}
                        

		} catch (Exception e) {
			DbBean.writeError("userBean", "getTaxByLable()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return taxbylabel;
	}
        public String getTaxByLableOrderManagement(String plant) throws Exception {

  		String taxbylabelordermanagement = "";
  		String sql;
  		Connection con = getDBConnection();

  		try {
  			Statement stmt = con.createStatement();
  			sql = "select ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT from PLNTMST where PLANT = '" + plant
  					+ "'";

  			this.mLogger.query(flgPrintQuery, sql);

  			ResultSet rs = stmt.executeQuery(sql);

  			if (rs != null) {
  				if (rs.next()) {
  					taxbylabelordermanagement = rs.getString("TAXBYLABELORDERMANAGEMENT").trim();
  				}
  			}
  			if (taxbylabelordermanagement == null || taxbylabelordermanagement.equals("")) {
  				throw new Exception("Tax By Label is not exists for the company");
  			}
                          

  		} catch (Exception e) {
  			DbBean.writeError("userBean", "getTaxByLableOrderManagement()", e);
  			throw e;
  		} finally {
  			DbBean.closeConnection(con);
  		}
  		return taxbylabelordermanagement;
  	}
   // added by Bruhan for maintenance expiry check 
    public String getMaintenanceExpiryDate(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(MNTEXPIRYDATE,'')MNTEXPIRYDATE from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("MNTEXPIRYDATE").trim();
				}
			}
          
		} catch (Exception e) {
			DbBean.writeError("userBean", "getDepartment()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
    // added by azees for country region 
    public String getRegion(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(REGION ,'') REGION from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("REGION").trim();
				}
			}
          
		} catch (Exception e) {
			DbBean.writeError("userBean", "getRegion()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 public int daysBetween(Date d1, Date d2){
        return (int)( (d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
} 

//added by azees for No.of Supplier 
 public String getNUMBEROFSUPPLIER(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_SUPPLIER ,'') NUMBER_OF_SUPPLIER from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_SUPPLIER").trim();
				}
			}
       
		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFSUPPLIER()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 
 public String getNUMBEROFCUSTOMER(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_CUSTOMER ,'') NUMBER_OF_CUSTOMER from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_CUSTOMER").trim();
				}
			}
    
		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFCUSTOMER()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}

 public String getNUMBEROFEMPLOYEE(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_EMPLOYEE ,'') NUMBER_OF_EMPLOYEE from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_EMPLOYEE").trim();
				}
			}
    
		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFEMPLOYEE()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 
 public String getNUMBEROFINVENTORY(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_INVENTORY ,'') NUMBER_OF_INVENTORY from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_INVENTORY").trim();
				}
			}
    
		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFINVENTORY()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 public String getNUMBEROFLOCATION(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_LOCATION ,'') NUMBER_OF_LOCATION from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_LOCATION").trim();
				}
			}
 
		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFLOCATION()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 
 public String getNUMBEROFUSER(String plant) throws Exception {
	 
	 String name = "";
	 String sql;
	 Connection con = getDBConnection();
	 
	 try {
		 Statement stmt = con.createStatement();
		 sql = "select ISNULL(NUMBER_OF_USER ,'') NUMBER_OF_USER from PLNTMST where PLANT = '" + plant
				 + "'";
		 
		 this.mLogger.query(flgPrintQuery, sql);
		 
		 ResultSet rs = stmt.executeQuery(sql);
		 
		 if (rs != null) {
			 if (rs.next()) {
				 name = rs.getString("NUMBER_OF_USER").trim();
			 }
		 }
		 
	 } catch (Exception e) {
		 DbBean.writeError("userBean", "getNUMBEROFUSER()", e);
		 throw e;
	 } finally {
		 DbBean.closeConnection(con);
	 }
	 return name;
 }

 public String getALLOWCATCH_ADVANCE_SEARCH(String plant) throws Exception {
	 
	 String name = "";
	 String sql;
	 Connection con = getDBConnection();
	 
	 try {
		 Statement stmt = con.createStatement();
		 sql = "select ISNULL(ALLOWCATCH_ADVANCE_SEARCH ,1) ALLOWCATCH_ADVANCE_SEARCH from PLNTMST where PLANT = '" + plant
				 + "'";
		 
		 this.mLogger.query(flgPrintQuery, sql);
		 
		 ResultSet rs = stmt.executeQuery(sql);
		 
		 if (rs != null) {
			 if (rs.next()) {
				 name = rs.getString("ALLOWCATCH_ADVANCE_SEARCH").trim();
			 }
		 }
		 
	 } catch (Exception e) {
		 DbBean.writeError("userBean", "getALLOWCATCH_ADVANCE_SEARCH()", e);
		 throw e;
	 } finally {
		 DbBean.closeConnection(con);
	 }
	 return name;
 }
 
 public String getSETCURRENTDATE_ADVANCE_SEARCH(String plant) throws Exception {
	 
	 String name = "";
	 String sql;
	 Connection con = getDBConnection();
	 
	 try {
		 Statement stmt = con.createStatement();
		 sql = "select ISNULL(SETCURRENTDATE_ADVANCE_SEARCH ,'0') SETCURRENTDATE_ADVANCE_SEARCH from PLNTMST where PLANT = '" + plant
				 + "'";
		 
		 this.mLogger.query(flgPrintQuery, sql);
		 
		 ResultSet rs = stmt.executeQuery(sql);
		 
		 if (rs != null) {
			 if (rs.next()) {
				 name = rs.getString("SETCURRENTDATE_ADVANCE_SEARCH").trim();
			 }
		 }
		 
	 } catch (Exception e) {
		 DbBean.writeError("userBean", "getSETCURRENTDATE_ADVANCE_SEARCH()", e);
		 throw e;
	 } finally {
		 DbBean.closeConnection(con);
	 }
	 return name;
 }
 
 public String getNUMBEROFORDER(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_ORDER ,'') NUMBER_OF_ORDER from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_ORDER").trim();
				}
			}
 
		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFORDER()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 
// RESVI START
 
 public String getNUMBEROFPAYMENT(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_PAYMENT ,'') NUMBER_OF_PAYMENT from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_PAYMENT").trim();
				}
			}

		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFPAYMENT()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 
 public String getNUMBEROFJOURNAL(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_JOURNAL ,'') NUMBER_OF_JOURNAL from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_JOURNAL").trim();
				}
			}

		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFJOURNAL()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 
 public String getNUMBEROFCONTRA(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(NUMBER_OF_CONTRA ,'') NUMBER_OF_CONTRA from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NUMBER_OF_CONTRA").trim();
				}
			}

		} catch (Exception e) {
			DbBean.writeError("userBean", "getNUMBEROFCONTRA()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
// ENDS
 
 
 public boolean isconvertob(String levelname, String userid,String plant) throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; 
			
			sql = "select COUNT(*) from USER_INFO a,"+plant+"_user_level b where USER_ID ='"+userid+"' and dept ='"+plant+"' and URL_NAME='convertOB' and b.LEVEL_NAME='"+levelname+"' and a.USER_LEVEL=b.LEVEL_NAME";
			this.mLogger.query(this.flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					b = true;
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}
 public String getCompanyName(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(PLNTDESC,'') COMPANYNAME from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("COMPANYNAME").trim();
				}
			}
                     //Hard coded for comapny DKP
                    // name = "2012-02-25";
			if (name == null || name.equals("")) {
				throw new Exception("Company Name is not exists");
			}
                     

		} catch (Exception e) {
			DbBean.writeError("userBean", "getCompanyName", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 public boolean deleteMessage(java.util.Hashtable ht) throws Exception {
		
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			String messageID = (String) ht.get("MESSAGEID");
			String sqlQuery = "UPDATE TBLMESSAGE SET ISACTIVE='N' WHERE MESSAGEID="+messageID;

			
			delete = updateData(con, sqlQuery);
		} catch (Exception e) {
			
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return delete;
	}
 
 public boolean Updatepasswordmessage(java.util.Hashtable ht) throws Exception {
		
		boolean update = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			
			String messageID = (String) ht.get("MESSAGEID");
			String message = (String) ht.get("MESSAGE");
			String logopath = (String) ht.get("LOGOPATH");
			String sqlQuery = "UPDATE TBLMESSAGE SET LOGOPATH='"+logopath+"',MESSAGE='"+message+"' WHERE MESSAGEID="+messageID;

			
			update = updateData(con, sqlQuery);
		} catch (Exception e) {
			
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return update;
	}
 public boolean updateData(Connection conn, String sql) throws Exception {
		boolean flag = false;
		Statement stmt = null;
		int updateCount = 0;
		try {
			stmt = conn.createStatement();
			updateCount = stmt.executeUpdate(sql);
			
			if (updateCount <= 0) {
				flag = false;
				throw new Exception("Unable to update!");
				
				
			} else {
				flag = true;
				
			}
		} catch (Exception e) {

			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return flag;
	}
 public boolean RemovePasswordLogo(java.util.Hashtable ht) throws Exception {
		
		boolean update = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			
			String messageID = (String) ht.get("MESSAGEID");
			//String message = (String) ht.get("MESSAGE");
			String logopath = (String) ht.get("LOGOPATH");
			String sqlQuery = "UPDATE TBLMESSAGE SET LOGOPATH='"+logopath+"'  WHERE MESSAGEID="+messageID;

			
			update = updateData(con, sqlQuery);
		} catch (Exception e) {
			
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return update;
	}
		

 
 public int getUserCount(String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCount = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "select count(*) from user_info where dept ='"+plant+"' and user_id not in('218202180','20212022','trackmaint')";
		   	this.mLogger.query(this.flgPrintQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCount = rs.getInt(1);
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserCount()", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCount;

	}
 public boolean isCheckVal(String name, String plant, String userid) throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; 
			sql = "if not exists(select * from [USER_INFO] ui where ui.USER_LEVEL in ('DefaultGroup','MaintenanceGroup') AND ui.USER_ID='"+userid+"' and ui.DEPT='"+plant+"')";
				sql =sql+ " select ul.URL_NAME from ["+plant+"_USER_LEVEL] ul ,["+plant+"_USER_MENU] um,[USER_INFO] ui where ui.DEPT=ul.PLANT AND ul.LEVEL_NAME = ui.USER_LEVEL AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1 AND ui.USER_ID='"+userid+"' and ul.PLANT='"+plant+"' AND ul.URL_NAME='" + name
						+ "'  else  select '" + name+ "' as URL_NAME";
			
			this.mLogger.query(this.flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			b = rs.next();
			
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}
 
 public boolean isCheckValinv(String name, String plant, String userid) throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; 
				sql =" select ul.URL_NAME from ["+plant+"_USER_LEVEL] ul ,["+plant+"_USER_MENU] um,[USER_INFO] ui where ui.DEPT=ul.PLANT AND ul.LEVEL_NAME = ui.USER_LEVEL AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1 AND ui.USER_ID='"+userid+"' and ul.PLANT='"+plant+"' AND ul.URL_NAME='" + name+"'";

			this.mLogger.query(this.flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			b = rs.next();
			
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}
 
 public boolean isCheckValPay(String name, String plant, String userid) throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; 
				sql =" select ul.URL_NAME from ["+plant+"_USER_LEVEL_PAYROLL] ul ,["+plant+"_USER_MENU_PAYROLL] um,[USER_INFO] ui where ui.DEPT=ul.PLANT AND ul.LEVEL_NAME = ISNULL(ui.USER_LEVEL_PAYROLL,'DefaultGroup') AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1 AND ui.USER_ID='"+userid+"' and ul.PLANT='"+plant+"' AND ul.URL_NAME='" + name+"'";

			this.mLogger.query(this.flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			b = rs.next();
			
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}
 
 public boolean isCheckValAcc(String name, String plant, String userid) throws Exception {

		Connection con = getDBConnection();
		boolean b = false;
		try {
			Statement stmt = con.createStatement();
			String sql; 
				sql =" select ul.URL_NAME from ["+plant+"_USER_LEVEL_ACCOUNTING] ul ,["+plant+"_USER_MENU_ACCOUNTING] um,[USER_INFO] ui where ui.DEPT=ul.PLANT AND ul.LEVEL_NAME = ISNULL(ui.USER_LEVEL_ACCOUNTING,'DefaultGroup') AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1 AND ui.USER_ID='"+userid+"' and ul.PLANT='"+plant+"' AND ul.URL_NAME='" + name+"'";

			this.mLogger.query(this.flgPrintQuery, sql);
			ResultSet rs = stmt.executeQuery(sql);
			b = rs.next();
			
		} catch (Exception e) {
			DbBean.writeError("userBean", "isAlreadyAvail()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return b;
	}
//added by azees for country 
 public String getCountry(String plant) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "select ISNULL(COUNTY ,'') COUNTRY from PLNTMST where PLANT = '" + plant
					+ "'";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("COUNTRY").trim();
				}
			}
       
		} catch (Exception e) {
			DbBean.writeError("userBean", "getCountry()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 
 	public List getMenuListForSuggestion(String plant, String levelname, String text) throws Exception {
		Connection con = null;
		String result = "";
		String link = "";
		Statement stmt = null;
		List menuList = new ArrayList();
		try {
			con = getDBConnection();
			stmt = con.createStatement();
			String q = "SELECT TEXT, URL FROM ["+plant+"_USER_MENU] A JOIN ["+plant+"_USER_LEVEL] B "
					+ "ON A.URL_NAME = B.URL_NAME "
					+ "WHERE (URL LIKE '%Summary%' or TEXT like '%Summary%') "
					+ "AND STATUS=1 AND B.LEVEL_NAME='"+ levelname +"' AND TEXT LIKE '%"+text+"%'ORDER BY col, row";
			this.mLogger.query(this.flgPrintQuery, q);
			menuList = selectData(con, q);
		} catch (Exception e) {
			DbBean.writeError("userBean", "getPdaMenu(String level_name)", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return menuList;
	}
 	
 	public ArrayList getNotificationList(String plant, String userid) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		Hashtable ht = new Hashtable();
		try {
			String sql = " SELECT CONVERT(nvarchar, CAST((SUBSTRING(CRAT,1,4) + '-' + SUBSTRING(CRAT,5,2) + '-' + SUBSTRING(CRAT,7,2) +' '+SUBSTRING(CRAT,9,2)+':'+SUBSTRING(CRAT,11,2)+':'+SUBSTRING(CRAT,13,2)) AS datetime), 106) AS CRAT,Title,Description FROM [tblmessage], [USER_INFO] "
					+ "WHERE USER_ID='"+userid+"' AND DEPT='"+plant+"' ORDER BY ID DESC";

			this.mLogger.query(flgPrintQuery, sql);
			arrList = selectForReport(sql, ht, sCondition);
		} catch (Exception e) {
			this.mLogger.log(0, "Exception :userBean: getUserListSummary:"
					+ e.toString());
		}
		return arrList;
	}
 	
 	public String getNotificationCount(String plant, String userid) throws Exception {

		String name = "";
		String sql;
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			sql = "SELECT COUNT(*) AS NOTIFICATION_COUNT FROM [tblmessage] A, [USER_INFO] B  "
					+ "WHERE USER_ID='"+userid+"' AND DEPT='"+plant+"' AND "
					+ "CAST((SUBSTRING(CRAT,1,4) + '-' + SUBSTRING(CRAT,5,2) + '-' + SUBSTRING(CRAT,7,2) +' '+SUBSTRING(CRAT,9,2)+':'+SUBSTRING(CRAT,11,2)+':'+SUBSTRING(CRAT,13,2)) AS datetime) > "
					+ "CAST((SUBSTRING(ISNULL(LAST_NOTFICATION_CHK,'20000101000000'),1,4) +"
					+ " '-' + SUBSTRING(ISNULL(LAST_NOTFICATION_CHK,'20000101000000'),5,2) + "
					+ " '-' + SUBSTRING(ISNULL(LAST_NOTFICATION_CHK,'20000101000000'),7,2) +"
					+ "' '+SUBSTRING(ISNULL(LAST_NOTFICATION_CHK,'20000101000000'),9,2)+':'+SUBSTRING(ISNULL(LAST_NOTFICATION_CHK,'20000101000000'),11,2)+':'+SUBSTRING(ISNULL(LAST_NOTFICATION_CHK,'20000101000000'),13,2)) AS datetime)";

			this.mLogger.query(flgPrintQuery, sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs != null) {
				if (rs.next()) {
					name = rs.getString("NOTIFICATION_COUNT").trim();
				}
			}
       
		} catch (Exception e) {
			DbBean.writeError("userBean", "getCountry()", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return name;
	}
 	
 	
 	public boolean getUserIspurchaseApproval(String userid, String company) throws Exception {
		String value = "";
		boolean val=false;
		String sql;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		try {

			sql = "select ISNULL(ISPURCHASEAPPROVAL,0) AS ISPURCHASEAPPROVAL from USER_INFO where USER_ID = ? and dept = ? ";

			this.mLogger.query(flgPrintQuery, sql);
			
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, company);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					value = rs.getString("ISPURCHASEAPPROVAL").trim();
				}
			}
			if(value.equalsIgnoreCase("1")) {
				val=true;
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevel()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return val;
	}
 	
 	public boolean getUserIspurchaseReturnApproval(String userid, String company) throws Exception {
		String value = "";
		boolean val=false;
		String sql;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		try {

			sql = "select ISNULL(ISPURCHASERETAPPROVAL,0) AS ISPURCHASERETAPPROVAL from USER_INFO where USER_ID = ? and dept = ? ";

			this.mLogger.query(flgPrintQuery, sql);
			
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, company);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					value = rs.getString("ISPURCHASERETAPPROVAL").trim();
				}
			}
			if(value.equalsIgnoreCase("1")) {
				val=true;
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevel()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return val;
	}
 	
 	public boolean getUserIsSalesApproval(String userid, String company) throws Exception {
		String value = "";
		boolean val=false;
		String sql;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		try {

			sql = "select ISNULL(ISSALESAPPROVAL,0) AS ISSALESAPPROVAL from USER_INFO where USER_ID = ? and dept = ? ";

			this.mLogger.query(flgPrintQuery, sql);
			
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, company);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					value = rs.getString("ISSALESAPPROVAL").trim();
				}
			}
			if(value.equalsIgnoreCase("1")) {
				val=true;
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevel()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return val;
	}
 	
 	public boolean getUserIsSalesReturnApproval(String userid, String company) throws Exception {
		String value = "";
		boolean val=false;
		String sql;
		Connection con = getDBConnection();
		PreparedStatement ps = null;
		try {

			sql = "select ISNULL(ISSALESRETAPPROVAL,0) AS ISSALESRETAPPROVAL from USER_INFO where USER_ID = ? and dept = ? ";

			this.mLogger.query(flgPrintQuery, sql);
			
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, company);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					value = rs.getString("ISSALESRETAPPROVAL").trim();
				}
			}
			if(value.equalsIgnoreCase("1")) {
				val=true;
			}
		} catch (Exception e) {
			DbBean.writeError("userBean", "getUserLevel()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return val;
	}

}
