package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BaseDAO;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class CycleCountBeanDAO extends BaseDAO {

	private boolean printQuery = MLoggerConstant.CYCLECOUNTBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CYCLECOUNTBEANDAO_PRINTPLANTMASTERLOG;
	private String tblName = "CYCLECOUNT";
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

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

	/**
	 * method : insertIntoCycleCount(Hashtable ht) description : Insert the new
	 * record into Inventory master(CYCLECOUNT)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean insertIntoCycleCount(Hashtable ht) throws Exception {
		boolean insertedCycleCount = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String stmt = "INSERT INTO " + tblName + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, stmt);

			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCycleCount = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertedCycleCount;
	}

	/**
	 * method : deleteCycleCount(Hashtable ht) description : Delete the existing
	 * record from Inventory master(CYCLECOUNT)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean deleteCycleCount(Hashtable ht) throws Exception {
		boolean deleteCycleCount = false;
		PreparedStatement ps = null;

		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition = key + " = " + value + "AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String stmt = "DELETE " + tblName + " WHERE " + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteCycleCount = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteCycleCount;
	}

	/**
	 * method : queryCycleCount(Hashtable ht) description : Delete the existing
	 * record from Inventory master(CYCLECOUNT)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public List queryCycleCount(List listQryFields, Hashtable ht)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sCondition = "";
		String sQryFields = "";
		List listQryResult = new ArrayList();

		Connection con = null;
		try {
			con = DbBean.getConnection();

			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition = key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			// get qry fields
			for (int i = 0; i < listQryFields.size(); i++) {
				sQryFields = StrUtils.fString((String) listQryFields.get(i))
						+ ",";
			}
			sQryFields = (sQryFields.length() > 0) ? sQryFields.substring(0,
					sQryFields.length() - 1) : "";
			String stmt = "SELECT " + sQryFields + " FROM " + tblName
					+ " WHERE " + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector vecLine = new Vector();
				for (int i = 0; i < listQryFields.size(); i++) {
					String fieldName = (String) listQryFields.get(i);
					fieldName = rs.getString(fieldName);
					vecLine.add(i, fieldName);
				}
				listQryResult.add(vecLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}

	/**
	 * method : updateCycleCount(Hashtable htUpdate,Hashtable htCondition)
	 * description : update the existing record from Inventory
	 * master(CYCLECOUNT)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean updateCycleCount(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean updateCycleCount = false;
		PreparedStatement ps = null;

		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = "", sCondition = "";

			// generate the condition string
			Enumeration enum1Update = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils.fString((String) enum1Update
						.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate = key + " = '" + value + "',";
			}

			// generate the update string
			Enumeration enum1Condition = htUpdate.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = StrUtils.fString((String) enum1Condition
						.nextElement());
				String value = StrUtils.fString((String) htCondition.get(key));
				sCondition = key + " = " + value + "AND ";
			}

			sUpdate = (sUpdate.length() > 0) ? sUpdate.substring(0, sUpdate
					.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String stmt = "UPDATE " + tblName + " " + sUpdate + " WHERE "
					+ sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				updateCycleCount = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return updateCycleCount;
	}

	/**
	 * method : getCountCycleCount(Hashtable ht) description : get the count of
	 * records in CycleCount for the given condition
	 * 
	 * @param : Hashtable ht
	 * @return : int - count
	 * @throws Exception
	 */
	public int getCountCycleCount(Hashtable ht) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntCycleCount = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition = key + " = " + value + "AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String stmt = "SELECT COUNT(" + IConstants.ITEM + ") FROM "
					+ tblName + " WHERE " + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntCycleCount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntCycleCount;
	}

	/**
	 * method : isExistsCycleCount(String aItem,String aLoc) description :
	 * 
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsCycleCount(String aItem, String aLoc)
			throws Exception {
		boolean exists = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(" + IConstants.ITEM + ") FROM "
					+ tblName + " WHERE " + IConstants.ITEM + "='" + aItem
					+ "' AND " + IConstants.LOC + "='" + aLoc + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) >= 1)
					exists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}

	/**
	 * @method : updateQty4PK(String aItem,String aLoc,String aQty)
	 * @description : update the qty for the given item and loc
	 * @param aItem
	 * @param aLoc
	 * @param aQty
	 * @return
	 * @throws Exception
	 */
	public boolean updateQty4PK(String aItem, String aLoc, String aQty,
			String aUserId, boolean aAddQty) throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String process = (aAddQty) ? "+" : "-"; //
			String sQry = "UPDATE " + tblName + " SET QTY=ISNULL(QTY,0)"
					+ process + Float.parseFloat(aQty) + ",UPBY='" + aUserId
					+ "',UPAT='" + DateUtils.getDateTime() + "' WHERE ITEM = '"
					+ aItem + "' AND LOC = '" + aLoc + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int i = ps.executeUpdate();
			if (i > 0)
				update = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return update;
	}

	/**
	 * 
	 * @param aItem
	 * @param aLoc
	 * @return
	 * @throws Exception
	 */
	public boolean insertIntoCycleCount(String aItem, String aLoc,
			String aUserId) throws Exception {
		boolean insert = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "INSERT INTO " + tblName
					+ " (PLANT,ITEM,LOC,CRAT,CRBY,STATUS) VALUES ('"
					+ IConstants.PLANT_VAL + "','" + aItem + "','" + aLoc
					+ "','" + DateUtils.getDateTime() + "','" + aUserId
					+ "','N')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int i = ps.executeUpdate();
			if (i > 0)
				insert = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return insert;
	}

	/**
	 * @method : getCycleCountDetails(String aItem, String aLoc)
	 * @description : get the inventory details for the given item and location
	 * @param aItem
	 * @param aLoc
	 * @return
	 * @throws Exception
	 */
	public ArrayList getCycleCountDetails(String aItem, String aLoc)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT A.ITEM,B.ITEMDESC,B.STKUOM,A.LOC,A.QTY FROM CYCLECOUNT AS A, ITEMMST AS B WHERE A.ITEM = '"
					+ aItem
					+ "' AND A.LOC = '"
					+ aLoc
					+ "' AND A.ITEM = B.ITEM AND QTY > 0";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareCall(sQry);

			rs = ps.executeQuery();
			while (rs.next()) {
				arrList.add(0, StrUtils.fString((String) rs.getString(1))); // item
				arrList.add(1, StrUtils.fString((String) rs.getString(2))); // descripton
				arrList.add(2, StrUtils.fString((String) rs.getString(3))); // uom
				arrList.add(3, StrUtils.fString((String) rs.getString(4))); // location
				arrList.add(4, StrUtils.fString((String) rs.getString(5))); // qty
				break;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}

	public ArrayList getLocList4Item(String aItem) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrLoc = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT LOC,QTY FROM " + tblName + " WHERE "
					+ IConstants.ITEM + " = '" + aItem + "' AND QTY > 0";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareCall(sQry);

			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString("LOC"))); // Loc
				arrLine.add(1, StrUtils.fString((String) rs.getString("QTY"))); // Loc
				arrLoc.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrLoc;
	}

	public float getAvailableQty(String aItem, String aLoc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		float fQty = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT QTY FROM CYCLECOUNT ITEM = '" + aItem
					+ "' AND A.LOC = '" + aLoc + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareCall(sQry);

			rs = ps.executeQuery();
			while (rs.next()) {
				fQty = rs.getFloat(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return fQty;

	}

	public ArrayList getCCItemList() throws Exception {
		ResultSet rs = null;

		Statement stmt = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			stmt = con.createStatement();
			String sQry = "SELECT C.ITEM,I.ITEMDESC,C.CYCLESDATE,C.CYCLEEDATE,ISNULL(C.USRFLG1,''),ISNULL(C.USRFLG2,'') FROM CYCLECNT AS C, ITEMMST AS I WHERE C.ITEM = I.ITEM";
			this.mLogger.query(this.printQuery, sQry);
			rs = stmt.executeQuery(sQry);
			int i = 0;
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString(1)));
				arrLine.add(1, StrUtils.fString((String) rs.getString(2)));
				arrLine.add(2, StrUtils.fString(rs.getString(3)));
				arrLine.add(3, StrUtils.fString((String) rs.getString(4)));
				arrList.add(arrLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, stmt);
		}
		return arrList;
	}

	/**
	 * 
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean insertIntoCCItem(Hashtable ht) throws Exception {
		boolean insertedCycleCount = false;
		PreparedStatement ps = null;

		Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String stmt = "INSERT INTO CYCLECNT ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCycleCount = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertedCycleCount;
	}

	/**
	 * 
	 * @param htUpdate
	 * @param htCondition
	 * @return
	 * @throws Exception
	 */
	public boolean updateCCItem(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean updateCycleCount = false;
		PreparedStatement ps = null;

		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = "", sCondition = "";

			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

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

			String stmt = "UPDATE " + " CYCLECNT " + sUpdate + sCondition;

			this.mLogger.query(this.printQuery, stmt);

			ps = con.prepareStatement(stmt);

			int iCnt = ps.executeUpdate();

			if (iCnt > 0)
				updateCycleCount = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return updateCycleCount;
	}

	/**
	 * method : isExistInCycleCntItem(String aItem,String aLoc) description :
	 * 
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean isExistInCycleCntItem(String aItem) throws Exception {
		boolean exists = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(" + IConstants.ITEM
					+ ") FROM CYCLECNT WHERE " + IConstants.ITEM + "='" + aItem
					+ "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) >= 1)
					exists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}// 
		return exists;
	}

	public boolean deleteCycleCountItem(String aItem) throws Exception {
		boolean deleted = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM CYCLECNT WHERE " + IConstants.ITEM
					+ "='" + aItem + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int i = ps.executeUpdate();
			if (i > 0)
				deleted = true;

		} catch (Exception e) {
		}
		return deleted;

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList getCCItemList4PA() throws Exception {
		ResultSet rs = null;

		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			Date dt = new Date();
			SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
			String today = dfVisualDate.format(dt);
			String sQry = "SELECT C.ITEM,I.ITEMDESC FROM CYCLECNT AS C, ITEMMST AS I WHERE C.ITEM = I.ITEM AND '"
					+ today + "' BETWEEN CYCLESDATE AND  CYCLEEDATE";
			this.mLogger.query(this.printQuery, sQry);
			rs = stmt.executeQuery(sQry);
			int i = 0;
			while (rs.next()) {
				ArrayList arrListLine = new ArrayList();
				arrListLine.add(0, StrUtils.fString((String) rs.getString(1)));
				arrListLine.add(1, StrUtils.fString((String) rs.getString(2)));
				arrList.add(arrListLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;

	}
}