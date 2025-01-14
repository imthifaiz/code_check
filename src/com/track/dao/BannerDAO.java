package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class BannerDAO extends BaseDAO {
	public static String TABLE_NAME = "BANNERMST";
	public String plant = "";

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.INVMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.INVMSTDAO_PRINTPLANTMASTERLOG;

	public boolean isPrintQuery() {
		return printQuery;
	}

	public void setPrintQuery(boolean printQuery) {
		this.printQuery = printQuery;
	}

	public boolean isPrintLog() {
		return printLog;
	}

	public void setPrintLog(boolean printLog) {
		this.printLog = printLog;
	}

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	 public boolean saveMst(Hashtable ht) throws Exception {

			boolean insertFlag = false;
			java.sql.Connection conn = null;
			try {
				conn = DbBean.getConnection();
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = StrUtils.fString((String) enumeration
							.nextElement());
					String value = StrUtils.fString((String) ht.get(key));
					FIELDS += key + ",";
					VALUES += "'" + value + "',";
				}
				
				String query = "INSERT INTO " + "["+ht.get("PLANT")+"_"+TABLE_NAME+"]" + "("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";

				this.mLogger.query(this.printQuery, query);
				insertFlag = insertData(conn, query);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (conn != null) {
					DbBean.closeConnection(conn);
				}
			}

			return insertFlag;
		}


	 
	 public boolean updateMst(Hashtable htUpdate, Hashtable htCondition)
		throws Exception {
			boolean update = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sUpdate = " ", sCondition = " ";
			
				// generate the condition string
				Enumeration enumUpdate = htUpdate.keys();
				for (int i = 0; i < htUpdate.size(); i++) {
					String key = StrUtils
							.fString((String) enumUpdate.nextElement());
					String value = StrUtils.fString((String) htUpdate.get(key));
					sUpdate += key.toUpperCase() + " = '" + value + "',";
				}
			
				// generate the update string
				Enumeration enumCondition = htCondition.keys();
				for (int i = 0; i < htCondition.size(); i++) {
					String key = StrUtils.fString((String) enumCondition
							.nextElement());
					String value = StrUtils.fString((String) htCondition.get(key));
			
					sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
							+ "' AND ";
				}
				sUpdate = (sUpdate.length() > 0) ? " SET "
						+ sUpdate.substring(0, sUpdate.length() - 1) : "";
				sCondition = (sCondition.length() > 0) ? " WHERE  "
						+ sCondition.substring(0, sCondition.length() - 4) : "";
				String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
						+ TABLE_NAME + "] " + sUpdate + sCondition;
				this.mLogger.query(this.printQuery, stmt);
				ps = con.prepareStatement(stmt);
				int iCnt = ps.executeUpdate();
				if (iCnt > 0)
					update = true;
			
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			
			return update;
		}
	 /**
		 * Author Bruhan. 12 july 2012 method :
		 * delMst(Hashtable htcondtn) description : Deletes the Calatog from the catalog master)
		 * 
		 * @param : Hashtable htcondtn
		 * @return : Boolean 
		 * @throws Exception
		 */
	public boolean delMst(Hashtable<String, String> ht) throws Exception {

		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + ht.get(IDBConstants.PLANT) + "_"
					+ TABLE_NAME + " ] WHERE " + formCondition(ht);
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			MLogger.log(0, "[ERROR] : " + e);
			return Boolean.valueOf(false);
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	}

	 
		
		public ArrayList getAdditionalImgForbanner(String plant 
				) throws Exception {
			
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				boolean flag = false;
				String sQry = "select ISNULL(BANNERPATH,'') BANNERPATH,ISNULL(BANNERLNNO,'1') BANNERLNNO from" + "["
						+ plant + "_" + "BANNERMST" + "]"
						+ " where PLANT='" + plant + "'  order by BANNERLNNO" ;
				
				this.mLogger.query(this.printQuery, sQry);
				al = selectData(con, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return al;
		}


}
