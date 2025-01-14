package com.track.dao;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class TimeSlotDAO extends BaseDAO {
	public static String TABLE_NAME = "TIME_SLOT";
	public String plant = "";
/* Test for Bruhan */
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TIMESLOTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TIMESLOTDAO_PRINTPLANTMASTERLOG;
	
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
	 public ArrayList selectMst(String query, Hashtable ht, String extCondi)
		throws Exception {

	boolean flag = false;
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
			+"["+ht.get("PLANT")+"_"+ TABLE_NAME+"]");
	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();

		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = formCondition(ht);
			sql.append(conditon);
		}
		if (extCondi.length() > 0)
			sql.append(" and " + extCondi);
		this.mLogger.query(this.printQuery, sql.toString());
		alData = selectData(con, sql.toString());
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}

	return alData;
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
	 
	 public boolean deleteTimeSlots(String Plant,String aTimeslots) throws Exception {

			boolean deleteTimeSlots = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "DELETE FROM [" + Plant  + "_" + TABLE_NAME + "] WHERE "
						+ IConstants.TIMESLOTS + "='" + aTimeslots  + "'";
				ps = con.prepareStatement(sQry);
				this.mLogger.query(this.printQuery, sQry);
				deleteTimeSlots = DeleteRow(con, sQry);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(con, ps);
			}

			return deleteTimeSlots;
		}

	 public boolean isExisit(Hashtable ht, String extCond) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
						+ "]");
				sql.append(" WHERE  " + formCondition(ht));

				if (extCond.length() > 0)
					sql.append(" and " + extCond);

				this.mLogger.query(this.printQuery, sql.toString());
				flag = isExists(con, sql.toString());

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return flag;

		}
	 
	/* public List queryTimeSlot(String atimeslot, String plant, String cond) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			List listQty = new ArrayList();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sCondition = "WHERE TIMESLOTS LIKE  '" + atimeslot.toUpperCase()
						+ "%'";
							
				String sQry = "SELECT SUBSTRING(TIMESLOTS,0,(PATINDEX('%:%',TIMESLOTS))) AS FROMTIME, SUBSTRING(TIMESLOTS,(PATINDEX('%:%',TIMESLOTS)),LEN(TimeSlots)-4) AS TOTIME,SUBSTRING(TIMESLOTS,LEN(TimeSlots)-1,(PATINDEX('%:%',TIMESLOTS))) AS MERIDIEN,ISNULL(QTY,0)QTY,ISNULL(REMARKS,'') REMARKS FROM "
				+ "[" + plant + "_" + "TIME_SLOT" + "]" + sCondition + cond
				+ " ORDER BY TIMESLOTS ";
				
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					Vector lineVec = new Vector();
					lineVec.add(0, StrUtils.fString((String) rs.getString("FROMTIME")));
					lineVec.add(1, StrUtils.fString((String) rs.getString("TOTIME")));
					lineVec.add(2, StrUtils.fString((String) rs.getString("MERIDIEN")));
					lineVec.add(3, StrUtils.fString((String) rs.getString("QTY")));
					lineVec.add(4, StrUtils.fString((String) rs.getString("REMARKS")));
					listQty.add(lineVec);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return listQty;
		}*/
	 
	 public List queryTimeSlot(String atimeslot, String plant, String cond) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			List listQty = new ArrayList();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sCondition = "WHERE TIMESLOTS LIKE  '" + atimeslot.toUpperCase()
						+ "%'";
							
				String sQry = "SELECT TIMESLOTS AS TIMESLOTS, ISNULL(QTY,0)QTY,ISNULL(REMARKS,'') REMARKS FROM "
				+ "[" + plant + "_" + "TIME_SLOT" + "]" + sCondition + cond
				+ " ORDER BY TIMESLOTS ";
				
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					Vector lineVec = new Vector();
					lineVec.add(0, StrUtils.fString((String) rs.getString("TIMESLOTS")));
					lineVec.add(1, StrUtils.fString((String) rs.getString("QTY")));
					lineVec.add(2, StrUtils.fString((String) rs.getString("REMARKS")));
					listQty.add(lineVec);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return listQty;
		}
	 
	 
	 public List queryEditTimeSlot(String atimeslot, String plant, String cond) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			List listQty = new ArrayList();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sCondition = "WHERE PLANT =  '" + plant.toUpperCase()
						+ " '";
							
				String sQry = "SELECT TIMESLOTS AS TIMESLOTS, ISNULL(QTY,0)QTY,ISNULL(REMARKS,'') REMARKS FROM "
				+ "[" + plant + "_" + "TIME_SLOT" + "]" + sCondition + cond
				+ " ORDER BY TIMESLOTS ";
				
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					Vector lineVec = new Vector();
					lineVec.add(0, StrUtils.fString((String) rs.getString("TIMESLOTS")));
					lineVec.add(1, StrUtils.fString((String) rs.getString("QTY")));
					lineVec.add(2, StrUtils.fString((String) rs.getString("REMARKS")));
					listQty.add(lineVec);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return listQty;
		}
	 
	

	 public ArrayList getTimeSlotList(String plant, String cond) throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();

				boolean flag = false;
				String sQry = "select distinct timeslots as timeslots  from " + "[" + plant
						+ "_" + "time_slot] where isnull(timeslots,'')<>'' order by timeslots" + cond;
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

	 public ArrayList getDAOTimeSlotsDetails(String selectList, Hashtable ht, String timeslot)
		throws Exception {
		 boolean flag = false;

		 Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
			+ "[" + ht.get("PLANT") + "_" + TABLE_NAME
			+ "] where timeslots like'" + timeslot + "%" + "'");

			try {
				con = DbBean.getConnection();
		
				String conditon = "";
				if (ht.size() > 0) {
					sql.append(" and ");
					conditon = formCondition(ht);
					sql.append(conditon);
				}
		
				this.mLogger.query(this.printQuery, sql.toString());
				alResult = selectData(con, sql.toString());
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(con);
			}
		
			return alResult;
		
		}
}
