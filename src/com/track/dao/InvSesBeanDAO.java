package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class InvSesBeanDAO extends BaseDAO {

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.INVSESBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.INVSESBEANDAO_PRINTPLANTMASTERLOG;

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

	private String tblName = "INVMST";

	/**
	 * method : insertIntoInvsmt(Hashtable ht) description : Insert the new
	 * record into Inventory master(INVMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean insertIntoInvsmt(Hashtable ht) throws Exception {
		boolean insertedInvmst = false;
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
				insertedInvmst = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertedInvmst;
	}

	/**
	 * method : deleteInvsmt(Hashtable ht) description : Delete the existing
	 * record from Inventory master(INVMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean deleteInvsmt(Hashtable ht) throws Exception {
		boolean deleteInvmst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				// sCondition = key +" = " + value + "AND ";
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String stmt = "DELETE " + tblName + " WHERE " + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteInvmst = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteInvmst;
	}

	/**
	 * method : deleteInvsmt(Hashtable ht) description : Delete the existing
	 * record from Inventory master(INVMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */

	public ArrayList queryInvsmt(ArrayList listQryFields, Hashtable ht,
			String extracond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sCondition = "";
		String sQryFields = "";
		ArrayList listQryResult = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();

			// generate the condition string
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition = sCondition + "INV." + key + " = '" + value
						+ "' AND ";

			}

			sCondition = sCondition + " INV.ITEM = ITEM.ITEM";

			for (int i = 0; i < listQryFields.size(); i++) {

				sQryFields = sQryFields + ""
						+ StrUtils.fString((String) listQryFields.get(i)) + ",";
			}
			sQryFields = sQryFields + "ITEM.itemDesc";

			String stmt = "SELECT DISTINCT " + sQryFields + " FROM  " + "["
					+ ht.get("PLANT") + "_" + "INVMST" + "] AS INV,  ["
					+ ht.get("PLANT") + "_" + "ITEMMST" + "] AS ITEM WHERE "
					+ sCondition + extracond;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList lineArrList = new ArrayList();
				for (int i = 0; i < listQryFields.size() + 1; i++) {
					int iCol = i + 1;
					String fieldName = rs.getString(iCol);
					lineArrList.add(i, fieldName);
				}
				listQryResult.add(lineArrList);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}

	/**
	 * method : updateInvsmt(Hashtable htUpdate,Hashtable htCondition)
	 * description : update the existing record from Inventory master(INVMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean updateInvsmt(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean updateInvmst = false;
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
				updateInvmst = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return updateInvmst;
	}

	/**
	 * method : getCountInvmst(Hashtable ht) description : get the count of
	 * records in invmst for the given condition
	 * 
	 * @param : Hashtable ht
	 * @return : int - count
	 * @throws Exception
	 */
	public int getCountInvmst(Hashtable ht) throws Exception {
		boolean deleteInvmst = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntInvmst = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String stmt = "SELECT COUNT(" + IConstants.ITEM + ") FROM "
					+ tblName + " WHERE " + sCondition + " AND QTY = 0";
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntInvmst = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntInvmst;
	}




	

	/**
	 * @method : getInvDetails(String aItem, String aLoc)
	 * @description : get the inventory details for the given item and location
	 * @param aItem
	 * @param aLoc
	 * @return
	 * @throws Exception
	 */
	public ArrayList getInvDetails(String aItem, String aLoc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sql = "SELECT A.ITEM,B.ITEMDESC,B.STKUOM,A.LOC,A.QTY FROM INVMST AS A, ITEMMST AS B WHERE A.ITEM = '"
					+ aItem
					+ "' AND A.LOC = '"
					+ aLoc
					+ "' AND A.ITEM = B.ITEM AND QTY > 0";
			this.mLogger.query(this.printQuery, sql);
			ps = con.prepareCall(sql);
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

	/**
	 * @method : getLocList4Item(String aItem)
	 * @description : get location list for the given item from invmst
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getLocList4Item(String aItem) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrLoc = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sql = "SELECT LOC,QTY FROM " + tblName + " WHERE "
					+ IConstants.ITEM + " = '" + aItem + "' AND QTY > 0";
			this.mLogger.query(this.printQuery, sql);
			ps = con.prepareCall(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString("LOC"))); // Loc
				arrLine.add(1, StrUtils.fString((String) rs.getString("QTY"))); // qty
				arrLoc.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrLoc;
	}

	/**
	 * @method : getLocList4Item(String aItem)
	 * @description : To check qty for the given item from invmst
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getLocList4ItemQty(String aItem) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrLoc = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sql = "SELECT LOC,QTY FROM " + tblName + " WHERE "
					+ IConstants.ITEM + " = '" + aItem + "'";
			this.mLogger.query(this.printQuery, sql);
			ps = con.prepareCall(sql);

			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString("LOC"))); // Loc
				arrLine.add(1, StrUtils.fString((String) rs.getString("QTY"))); // qty
				arrLoc.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrLoc;
	}
}