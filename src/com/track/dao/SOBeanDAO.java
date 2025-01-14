package com.track.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class SOBeanDAO extends BaseDAO {

	private boolean printQuery = MLoggerConstant.SOBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.SOBEANDAO_PRINTPLANTMASTERLOG;
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

	private String tblName = "DODET";

	/**
	 * @method : getOpenSO_Col() description : get the list of open so
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public ArrayList getOpenSO_Col(String aUserId) throws Exception {
		ArrayList arrListCol = new ArrayList();
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT DISTINCT USERTIME1,DONO,USERFLD3 FROM "
					+ tblName
					+ " WHERE (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O') AND (ISNULL(USERFLG2,'') <> 'B' OR (ISNULL(USERFLG2,'') = 'B' AND ISNULL(USERTIME3,'') ='"
					+ aUserId + "'))";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrListLine = new ArrayList();
				arrListLine.add(0, StrUtils.fString((String) rs.getString(1)));
				arrListLine.add(1, StrUtils.fString((String) rs.getString(2)));
				arrListLine.add(2, StrUtils.fString((String) rs.getString(3)));
				arrListCol.add(arrListLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrListCol;
	}

	/**
	 * @method : getItemDet4SO_Col(String aSono)
	 * @description : get the item and description for the given so no
	 * @param aSono
	 * @return
	 * @throws Exception
	 */
	public ArrayList getItemDet4SO_Col(String aSono) throws Exception {
		ArrayList arrListCol = new ArrayList();
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ITEM,USERFLD1,DOLNNO,USERTIME2 FROM  "
					+ tblName + " WHERE DONO = '" + aSono + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrListLine = new ArrayList();
				arrListLine.add(0, StrUtils.fString((String) rs.getString(1)));
				arrListLine.add(1, StrUtils.fString((String) rs.getString(2)));
				arrListLine.add(2, StrUtils.fString((String) rs.getString(3)));
				arrListLine.add(3, StrUtils.fString((String) rs.getString(4)));
				arrListCol.add(arrListLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrListCol;
	}

	/**
	 * @method : getItemDetails4Item(String aSono, String aItem)
	 * @description : get the details for the given sono and item code
	 * @param aSono
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getItemDetails4Item(String aSono, String aDolno)
			throws Exception {
		ArrayList arrList = new ArrayList();
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ITEM, USERFLD1,USERFLD5,USERFLD3,UNITMO,USERDBL1,ISNULL(QTYOR,0),ISNULL(QTYIS,0),LNSTAT FROM  "
					+ tblName
					+ " WHERE DONO = '"
					+ aSono
					+ "' AND DOLNNO = '"
					+ aDolno + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrList.add(0, StrUtils.fString((String) rs.getString(1))); // item
				arrList.add(1, StrUtils.fString((String) rs.getString(2))); // item
				// desc
				arrList.add(2, StrUtils.fString((String) rs.getString(3))); // area
				arrList.add(3, StrUtils.fString((String) rs.getString(4))); // CUSTOMER
				arrList.add(4, StrUtils.fString((String) rs.getString(5))); // ORDUNIT
				arrList.add(5, StrUtils.fString((String) rs.getString(6))); // UNITPRICE
				arrList.add(6, StrUtils.fString((String) rs.getString(7))); // ORD
				arrList.add(7, StrUtils.fString((String) rs.getString(8))); // ISS
				arrList.add(8, StrUtils.fString((String) rs.getString(9))); // STATUS
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	/**
	 * @method : getItemDetails4NoStckItem(String aSono,String aDolno, String
	 *         aItem)
	 * @description : get the details for the given sono and item code
	 * @param aSono
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getItemDetails4NoStckItem(String aSono, String aDolno)
			throws Exception {
		ArrayList arrList = new ArrayList();
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ITEM, USERFLD1 FROM  " + tblName
					+ " WHERE DONO = '" + aSono + "' AND DOLNNO = '" + aDolno
					+ "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrList.add(0, StrUtils.fString((String) rs.getString(1))); // item
				arrList.add(1, StrUtils.fString((String) rs.getString(2))); // item

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	/**
	 * 
	 * @param aSono
	 * @param aItem
	 * @param aPickQty
	 * @return
	 * @throws Exception
	 */
	public boolean updateQtyIssue(String aSono, String aItem, String aDolno,
			String aLoc, String aPickQty, String aStatus, String aRemarks,
			String aUsrId) throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "UPDATE DODET SET QTYIS = ISNULL(QTYIS,0) +"
					+ aPickQty + ",LNSTAT = '" + aStatus + "',LOC = '" + aLoc
					+ "',USERTIME2='" + aRemarks + "',CRBY='" + aUsrId
					+ "'  WHERE DONO = '" + aSono + "' AND ITEM = '" + aItem
					+ "' AND DOLNNO='" + aDolno + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);

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

	public boolean isExistItem4SO(String aSono, String aItem) throws Exception {
		boolean exists = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(ITEM) FROM DODET WHERE DONO = '"
					+ aSono + "' AND ITEM = '" + aItem
					+ "' AND (LNSTAT = 'N' OR LNSTAT = 'O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					exists = true;
			}

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return exists;

	}

	public boolean isAllItemsPicked4SO(String aSONO) throws Exception {
		boolean allPicked = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM DODET WHERE DONO = '"
					+ aSONO
					+ "' AND  (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					allPicked = false;
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return allPicked;

	}

	public boolean updateSO(Hashtable htUpdate, Hashtable htCondition,
			String aExtraCondition) throws Exception {
		boolean update = false;
		PreparedStatement ps = null;

		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "',";
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

			String stmt = "UPDATE " + tblName + sUpdate + sCondition;
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

	public String loadOpenItems4SO(String aSONO) throws Exception {
		String items = "";
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ITEM FROM  " + tblName + " WHERE DONO = '"
					+ aSONO + "' AND LNSTAT in ('N','O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				items += StrUtils.fString(rs.getString(1)) + ",";
			}
			items = items.substring(0, items.lastIndexOf(","));
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return items;

	}

	public ArrayList getSOSummary() throws Exception {
		CallableStatement cs = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();

			cs = con.prepareCall("PROC_VIEW_SOSTATUS");
			rs = cs.executeQuery();

			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // PONO
				arrLine.add(1, StrUtils.fString((String) rs.getString(2))); // UNAME
				arrLine.add(2, StrUtils.fString((String) rs.getString(3))); // CENAME
				arrLine.add(3, StrUtils.fString((String) rs.getString(4))); // STATEMENT
				arrLine.add(4, StrUtils.fString((String) rs.getString(5))); // CARTON
				arrLine.add(5, StrUtils.fString((String) rs.getString(6))); // COMMENTS
				arrLine.add(6, StrUtils.fString((String) rs.getString(7))); // USERFLG2
				arrList.add(arrLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, cs);
		}
		return arrList;
	}

	public ArrayList queryDODET_Col(List listQryFields, Hashtable ht,
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

	/*
	 * Not Used anywhere
	 */
	public ArrayList getOpenItemDet4SO_Col(String aSono) throws Exception {
		ArrayList arrListCol = new ArrayList();
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ITEM, USERFLD1 FROM  " + tblName
					+ " WHERE DONO = '" + aSono + "' AND LNSTAT IN ('N','O')";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrListLine = new ArrayList();
				arrListLine.add(0, StrUtils.fString((String) rs.getString(1)));
				arrListLine.add(1, StrUtils.fString((String) rs.getString(2)));
				arrListCol.add(arrListLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrListCol;

	}

	// Added by samatha
	/**
	 * @method : CheckAvailableQty(String aItem,String aLoc,String aQty)
	 * @param aItem
	 *            ,aLoc,aQty
	 * @return
	 * @throws Exception
	 */
	public float getAvailableQty(String aItem, String aLoc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		float avlQty = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT sum(qty) FROM INVMST WHERE "
					+ IConstants.ITEM + " = '" + aItem + "' AND "
					+ IConstants.LOC + " = '" + aLoc + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				avlQty = rs.getFloat(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return avlQty;
	}

	// Sort Item By Location

	public ArrayList getOpenItemDet4SOSortByLoc_Col(String aSono)
			throws Exception {
		ArrayList arrListCol = new ArrayList();
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT I.ITEM,D.USERFLD1,D.DOLNNO FROM ITEMLOCMST AS I,DODET AS D WHERE I.ITEM IN (SELECT ITEM FROM DODET WHERE DONO = '"
					+ aSono
					+ "' ) AND D.DONO = '"
					+ aSono
					+ "' AND I.ITEM = D.ITEM AND D.LNSTAT='N' ORDER BY I.LOC ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			ArrayList distinctItemList = new ArrayList();
			while (rs.next()) {
				ArrayList arrListLine = new ArrayList();
				String item = (String) rs.getString(1);
				String lnno = (String) rs.getString(3);
				// if (!distinctItemList.contains(item)){
				if (!distinctItemList.contains(lnno)) {
					arrListLine.add(0, StrUtils.fString(item));
					arrListLine.add(1, StrUtils.fString((String) rs
							.getString(2)));
					arrListLine.add(2, StrUtils.fString((String) rs
							.getString(3))); // description
					arrListCol.add(arrListLine);
					distinctItemList.add(lnno);
				}
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrListCol;

	}

	public boolean updateQtyIssue4Reverse(String aSono, String aItem,
			String aLoc, String aPickQty, String aStatus, String aRemarks,
			String aUsrId) throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "UPDATE DODET SET QTYIS = " + aPickQty
					+ ",LNSTAT = '" + aStatus + "',LOC = '" + aLoc
					+ "',USERTIME2='" + aRemarks + "',CRBY='" + aUsrId
					+ "'  WHERE DONO = '" + aSono + "' AND ITEM = '" + aItem
					+ "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);

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
	 * not in use
	 * 
	 * @method : updateBlkOrCloseStatus(String aSono,String aStatus)
	 * @description : update the Block/Close Status and Userid for the given
	 *              SONO
	 * @param aSono
	 * @return
	 * @throws Exception
	 */
	public boolean updateBlkOrCloseStatus(String aSono, String aStatus,
			String aUserid) throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "UPDATE DODET SET USERFLG2='" + aStatus + "',UPBY='"
					+ aUserid + "'  WHERE DONO = '" + aSono + "' ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int i = ps.executeUpdate();
			if (i > 0)
				update = true;
		} catch (Exception e) {
		}
		return update;
	}

	public String CheckUserAlreadyPicking(String aSONO) throws Exception {
		String sUser = "";
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT  DISTINCT UPBY FROM " + tblName
					+ " WHERE DONO='" + aSONO + "' AND USERFLG2='B' ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				sUser += StrUtils.fString(rs.getString(1));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return sUser;

	}

	public String loadDolnnoForItem(String aSONO, String aItem)
			throws Exception {
		String sDolno = "";
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT  TOP 1 DOLNNO FROM " + tblName
					+ " WHERE DONO='" + aSONO + "' AND ITEM ='" + aItem
					+ "' AND LNSTAT='N' ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				sDolno += StrUtils.fString(rs.getString(1));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return sDolno;

	}

	public String loadItemForDolnno(String aSONO, String aDolno)
			throws Exception {
		String sItem = "";
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT  ITEM FROM " + tblName + " WHERE DONO='"
					+ aSONO + "' AND DOLNNO ='" + aDolno + "' AND LNSTAT='N' ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				sItem += StrUtils.fString(rs.getString(1));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return sItem;

	}

	/**
	 * @method : getAllItemDetails_Col_PDA() description : get the list of open
	 *         so
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public ArrayList getAllItemDetails_Col_PDA(String sSONO) throws Exception {
		ArrayList arrListCol = new ArrayList();
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT DOLNNO,ITEM, USERFLD1,USERFLD5,USERFLD3,UNITMO,USERDBL1,ISNULL(QTYOR,0),ISNULL(QTYIS,0),USERTIME1 FROM  "
					+ tblName + " WHERE DONO = '" + sSONO + "' ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrListLine = new ArrayList();
				arrListLine.add(0, StrUtils.fString((String) rs.getString(1)));
				arrListLine.add(1, StrUtils.fString((String) rs.getString(2)));
				arrListLine.add(2, StrUtils.fString((String) rs.getString(3)));
				arrListLine.add(3, StrUtils.fString((String) rs.getString(4)));
				arrListLine.add(4, StrUtils.fString((String) rs.getString(5)));
				arrListLine.add(5, StrUtils.fString((String) rs.getString(6)));
				arrListLine.add(6, StrUtils.fString((String) rs.getString(7)));
				arrListLine.add(7, StrUtils.fString((String) rs.getString(8)));
				arrListLine.add(8, StrUtils.fString((String) rs.getString(9)));
				arrListLine.add(9, StrUtils.fString((String) rs.getString(10)));
				arrListCol.add(arrListLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrListCol;
	}
}