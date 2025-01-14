package com.track.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class POBeanDAO extends BaseDAO {

	private String tblName = "PODET";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POBEANDAO_PRINTPLANTMASTERLOG;

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

	/**
	 * @method : getCountOpenPO4Item(String aItem) description : Find the count
	 *         of new / open PO nos of given item.
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public int getCountOpenPO4Item(String aItem) throws Exception {
		int iCnt = 0;
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(PONO) FROM "
					+ tblName
					+ " WHERE ITEM = '"
					+ aItem
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCnt = rs.getInt(1);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCnt;
	}

	public int getCountOpenPO4Vendor(String aVendNo) throws Exception {
		int iCnt = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(PONO) FROM "
					+ tblName
					+ " WHERE USERFLD2 = '"
					+ aVendNo
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCnt = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCnt;
	}

	/**
	 * @method : getPONO4Vendor(String aVendNo)
	 * @description : get PONO for the given vendor number
	 * @param aVendNo
	 * @return
	 * @throws Exception
	 */
	public String getPONO4Vendor(String aVendNo) throws Exception {
		String sPONO = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT TOP 1 PONO FROM "
					+ tblName
					+ " WHERE USERFLD2 = '"
					+ aVendNo
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				sPONO = StrUtils.fString(rs.getString(1));
			}
		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return sPONO;

	}

	/**
	 * @method : getItemDetails4PONO(String aPONO)
	 * @description :
	 * @param aPONO
	 * @return
	 * @throws Exception
	 */
	public ArrayList getItemDetails4PONO(String aPONO) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT PONO,USERFLD2,DELDATE FROM "
					+ tblName
					+ " WHERE PONO = '"
					+ aPONO
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // PONO
				arrLine.add(1, StrUtils.fString((String) rs.getString(2))); // VENDNO
				arrLine.add(2, StrUtils.fString((String) rs.getString(3))); // DATE
				arrList.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}

	/**
	 * @method : isValidOpenPO(String aPONO)
	 * @description : check the given po is valid and it's open to receive
	 * @param aPONO
	 * @return
	 * @throws Exception
	 */
	public int getCountOpenItems4PO(String aPONO) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCount = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM PODET WHERE PONO = '"
					+ aPONO
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCount;

	}

	public int getCountOpenItems4Vendor(String aVendNo) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCount = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "";
			ps = con
					.prepareStatement("SELECT COUNT(*) FROM PODET WHERE VENDNO = '"
							+ aVendNo
							+ "' AND (ISNULL(POSTAT,'') = 'N' OR ISNULL(POSTAT,'') = 'O')");
			this.mLogger.query(this.printQuery, sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCount;
	}

	public ArrayList getOpenItemDetails4PO(String aPONO) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ITEM,USERFLD1 FROM PODET WHERE PONO = '"
					+ aPONO
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			this.mLogger.query(this.printQuery, sQry);

			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // Item
				arrLine.add(1, StrUtils.fString((String) rs.getString(2))); // Item
				// Desc
				arrList.add(arrLine);
			}
			System.out.println("getOpenItemDetails4PO :: arrList.size "
					+ arrList.size());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList queryPODET_Col(List listQryFields, Hashtable ht,
			String aExtraCondition) throws Exception {
		boolean deleteItemmst = false;
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
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			sCondition = sCondition + " " + aExtraCondition;

			// get qry fields
			for (int i = 0; i < listQryFields.size(); i++) {
				sQryFields += StrUtils.fString((String) listQryFields.get(i))
						+ ",";
			}
			sQryFields = (sQryFields.length() > 0) ? sQryFields.substring(0,
					sQryFields.length() - 1) : "";
			String sQry = "SELECT " + sQryFields + " FROM " + tblName
					+ " WHERE " + sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				for (int iQryFiled = 1; iQryFiled <= listQryFields.size(); iQryFiled++) {
					String fieldValue = StrUtils.fString(rs
							.getString(iQryFiled));
					arrLine.add(fieldValue);
				}
				listQryResult.add(arrLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}

	public boolean updatePodet(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = "", sCondition = "";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate = key + " = '" + value + "',";
			}

			// generate the update string
			Enumeration enumCondition = htUpdate.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = StrUtils.fString((String) enumCondition
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
				update = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}

	/**
	 * @method : updatePODET4Recving(String aPono,String aItem, String
	 *         aLnstatus, float aRecvQty)
	 * @description : update the qty and status in
	 * @param aPono
	 * @param aItem
	 * @param aLnstatus
	 * @param aRecvQty
	 * @return
	 * @throws Exception
	 */
	public boolean updatePODET4Recving(String aPono, String aItem,
			String aLnstatus, float aRecvQty, String aUserid) throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String stmt = "UPDATE " + tblName + " SET QTYRC=ISNULL(QTYRC,0)+"
					+ aRecvQty + ",LNSTAT = '" + aLnstatus + "',CRBY='"
					+ aUserid + "' WHERE PONO = '" + aPono + "' AND ITEM = '"
					+ aItem + "'";
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return update;
	}

	public ArrayList getPOList() throws Exception {
		CallableStatement cs = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			cs = con.prepareCall("proc_view_postatus");
			rs = cs.executeQuery();

			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // PONO
				arrLine.add(1, StrUtils.fString((String) rs.getString(2))); // UNAME
				arrLine.add(2, StrUtils.fString((String) rs.getString(3))); // VENAME
				arrLine.add(3, StrUtils.fString((String) rs.getString(4))); // STATEMENT
				arrLine.add(4, StrUtils.fString((String) rs.getString(5))); // COMMENTS
				arrList.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, cs);
		}
		return arrList;

	}
}