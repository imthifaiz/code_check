package com.track.session.invses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.track.constants.IConstants;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@Deprecated
public class InvSesBean implements SessionBean {
	StrUtils _strUtils = new StrUtils();
	DateUtils _dateUtil = new DateUtils();
	SessionContext sessionContext;

	private String tblName = "INVMST";
	Connection con = null;

	public void ejbCreate() {
	}

	public void ejbRemove() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void setSessionContext(SessionContext sessionContext) {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		this.sessionContext = sessionContext;
	}

	/**
	 * method : insertIntoInvsmt(Hashtable ht) description : Insert the new
	 * record into Inventory master(INVMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean insertIntoInvsmt(Hashtable ht) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean insertedInvmst = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			
			for (int i = 0; i < ht.size(); i++) {
				String key = _strUtils.fString((String) enum1.nextElement());
				String value = _strUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String stmt = "INSERT INTO " + tblName + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedInvmst = true;

		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteInvmst = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _strUtils.fString((String) enum1.nextElement());
				String value = _strUtils.fString((String) ht.get(key));
				// sCondition = key +" = " + value + "AND ";
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String stmt = "DELETE " + tblName + " WHERE " + sCondition;
			MLogger.log(0, "[SQL] : \n " + stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteInvmst = true;

		} catch (Exception e) {
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

	/*---Update on 20100521.............................................................#############################*/
	public ArrayList queryInvsmt(ArrayList listQryFields, Hashtable ht,
			String extracond) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sCondition = "";
		String sQryFields = "";
		ArrayList listQryResult = new ArrayList();

		try {
			con = DbBean.getConnection();

			// generate the condition string
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _strUtils.fString((String) enum1.nextElement());
				String value = _strUtils.fString((String) ht.get(key));
				sCondition = sCondition + "INV." + key + " = '" + value
						+ "' AND ";

			}

			sCondition = sCondition + " INV.ITEM = ITEM.ITEM";

			for (int i = 0; i < listQryFields.size(); i++) {

				sQryFields = sQryFields + ""
						+ _strUtils.fString((String) listQryFields.get(i))
						+ ",";
			}
			sQryFields = sQryFields + "ITEM.itemDesc";

			String stmt = "SELECT DISTINCT " + sQryFields + " FROM  " + "["
					+ ht.get("PLANT") + "_" + "INVMST" + "] AS INV,  ["
					+ ht.get("PLANT") + "_" + "ITEMMST" + "] AS ITEM WHERE "
					+ sCondition + extracond;
			MLogger.log(0, "[SQL] : \n " + stmt);
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean updateInvmst = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sUpdate = "", sCondition = "";

			// generate the condition string
			Enumeration enum1Update = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = _strUtils.fString((String) enum1Update
						.nextElement());
				String value = _strUtils.fString((String) htUpdate.get(key));
				sUpdate = key + " = '" + value + "',";
			}

			// generate the update string
			Enumeration enum1Condition = htUpdate.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = _strUtils.fString((String) enum1Condition
						.nextElement());
				String value = _strUtils.fString((String) htCondition.get(key));
				sCondition = key + " = " + value + "AND ";
			}

			sUpdate = (sUpdate.length() > 0) ? sUpdate.substring(0, sUpdate
					.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String stmt = "UPDATE " + tblName + " " + sUpdate + " WHERE "
					+ sCondition;
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				updateInvmst = true;

		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteInvmst = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntInvmst = 0;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _strUtils.fString((String) enum1.nextElement());
				String value = _strUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String stmt = "SELECT COUNT(" + IConstants.ITEM + ") FROM "
					+ tblName + " WHERE " + sCondition + " AND QTY = 0";
			MLogger.log(0, "[SQL] : \n " + stmt);
			ps = con.prepareStatement(stmt);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntInvmst = rs.getInt(1);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntInvmst;
	}

	/**
	 * method : isExistsInvMst(String aItem,String aLoc) description :
	 * 
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsInvMst(String aItem, String aLoc) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean exists = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(" + IConstants.ITEM + ") FROM "
					+ tblName + " WHERE " + IConstants.ITEM + "='" + aItem
					+ "' AND " + IConstants.LOC + "='" + aLoc + "'";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) >= 1)
					exists = true;
			}
		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean update = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String process = (aAddQty) ? "+" : "-"; //
			String sQry = "UPDATE INVMST SET QTY=ISNULL(QTY,0)" + process
					+ Integer.parseInt(aQty) + ",UPBY='" + aUserId + "',UPAT='"
					+ _dateUtil.getDateTime() + "' WHERE ITEM = '" + aItem
					+ "' AND LOC = '" + aLoc + "'";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			int i = ps.executeUpdate();
			if (i > 0)
				update = true;
		} catch (Exception e) {
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
	public boolean insertIntoInvsmt(String aItem, String aLoc, String aUserId)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean insert = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String sQry = "INSERT INTO INVMST (PLANT,ITEM,LOC,CRAT,CRBY,STATUS) VALUES ('"
					+ IConstants.PLANT_VAL
					+ "','"
					+ aItem
					+ "','"
					+ aLoc
					+ "','"
					+ _dateUtil.getDateTime()
					+ "','"
					+ aUserId
					+ "','N')";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			int i = ps.executeUpdate();
			
			if (i > 0)
				insert = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return insert;
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			ps = con
					.prepareCall("SELECT A.ITEM,B.ITEMDESC,B.STKUOM,A.LOC,A.QTY FROM INVMST AS A, ITEMMST AS B WHERE A.ITEM = '"
							+ aItem
							+ "' AND A.LOC = '"
							+ aLoc
							+ "' AND A.ITEM = B.ITEM AND QTY > 0");
			rs = ps.executeQuery();
			while (rs.next()) {
				arrList.add(0, _strUtils.fString((String) rs.getString(1))); // item
				arrList.add(1, _strUtils.fString((String) rs.getString(2))); // descripton
				arrList.add(2, _strUtils.fString((String) rs.getString(3))); // uom
				arrList.add(3, _strUtils.fString((String) rs.getString(4))); // location
				arrList.add(4, _strUtils.fString((String) rs.getString(5))); // qty
				break;
			}
		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrLoc = new ArrayList();
		try {
			con = DbBean.getConnection();
			ps = con.prepareCall("SELECT LOC,QTY FROM " + tblName + " WHERE "
					+ IConstants.ITEM + " = '" + aItem + "' AND QTY > 0");
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, _strUtils.fString((String) rs.getString("LOC"))); // Loc
				arrLine.add(1, _strUtils.fString((String) rs.getString("QTY"))); // qty
				arrLoc.add(arrLine);
			}
		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrLoc = new ArrayList();
		try {
			con = DbBean.getConnection();
			ps = con.prepareCall("SELECT LOC,QTY FROM " + tblName + " WHERE "
					+ IConstants.ITEM + " = '" + aItem + "'");
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, _strUtils.fString((String) rs.getString("LOC"))); // Loc
				arrLine.add(1, _strUtils.fString((String) rs.getString("QTY"))); // qty
				arrLoc.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrLoc;
	}
}