package com.track.session.itemses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.track.constants.IConstants;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
@Deprecated
public class ItemSesBean implements SessionBean {
	StrUtils strUtils = new StrUtils();
	SessionContext sessionContext;

	private String tblName = "ITEMMST";

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
		this.sessionContext = sessionContext;
	}

	/**
	 * method : insertIntoItemMst(Hashtable ht) description : Insert the new
	 * record into Itementory master(ITEMMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean insertIntoItemMst(Hashtable ht) throws Exception {
		boolean insertedItemmst = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = strUtils.fString((String) enum1.nextElement());
				String value = strUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value + "',";
			}
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ tblName + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			MLogger.log(0, "[SQL]" + sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedItemmst = true;

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertedItemmst;
	}

	/**
	 * method : deleteItemMst(Hashtable ht) description : Delete the existing
	 * record from Itementory master(ITEMMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean deleteItemMst(Hashtable ht) throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = strUtils.fString((String) enum1.nextElement());
				String value = strUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			ps = con.prepareStatement("DELETE " + "[" + ht.get("PLANT") + "_"
					+ tblName + "]" + " WHERE " + sCondition);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}

	/**
	 * method : deleteItemsmt(Hashtable ht) description : Delete the existing
	 * record from Item master(ITEMMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public List queryItemMst(List listQryFields, Hashtable ht) throws Exception {
		boolean deleteItemmst = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sCondition = "";
		String sQryFields = "";
		List listQryResult = new ArrayList();

		try {
			con = DbBean.getConnection();
			// generate the condition string
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = strUtils.fString((String) enum1.nextElement());
				String value = strUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			// get qry fields
			for (int i = 0; i < listQryFields.size(); i++) {
				sQryFields = strUtils.fString((String) listQryFields.get(i))
						+ ",";
			}
			sQryFields = (sQryFields.length() > 0) ? sQryFields.substring(0,
					sQryFields.length() - 1) : "";
			String sQry = "SELECT " + sQryFields + " FROM " + tblName
					+ " WHERE " + sCondition;
			MLogger.log(0, "[SQL]" + sQry);
			ps = con.prepareStatement(sQry);
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
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}

	/**
	 * method : updateItemMst(Hashtable htUpdate,Hashtable htCondition)
	 * description : update the existing record from Itementory master(ITEMMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean updateItemMst(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean updateItemmst = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sUpdate = "", sCondition = "";

			// generate the condition string
			Enumeration enum1Update = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = strUtils.fString((String) enum1Update
						.nextElement());
				String value = strUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			// generate the update string
			Enumeration enum1Condition = htCondition.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = strUtils.fString((String) enum1Condition
						.nextElement());
				String value = strUtils.fString((String) htCondition.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sUpdate = (sUpdate.length() > 0) ? sUpdate.substring(0, sUpdate
					.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sQry = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
					+ tblName + "]" + " SET " + sUpdate + " WHERE "
					+ sCondition;

			MLogger.log(0, "[SQL]" + sQry);

			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				updateItemmst = true;

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return updateItemmst;
	}

	/**
	 * method : getCountItemmst(Hashtable ht) description : get the count of
	 * records in Itemmst for the given condition
	 * 
	 * @param : Hashtable ht
	 * @return : int - count
	 * @throws Exception
	 */
	public int getCountItemMst(Hashtable ht) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntItemmst = 0;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = strUtils.fString((String) enum1.nextElement());
				String value = strUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sQry = "SELECT COUNT(" + IConstants.ITEM + ") FROM " + "["
					+ ht.get("PLANT") + "_" + tblName + "]" + " WHERE "
					+ sCondition;
			MLogger.log(0, "[SQL]" + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntItemmst = rs.getInt(1);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntItemmst;
	}

	/**
	 * @method : getItemDetails(String aItem)
	 * @description : get the item details for the given item
	 * @param aItem
	 * @return
	 */
	public ArrayList getItemDetails(String aItem) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			ps = con
					.prepareCall("SELECT ITEM,ITEMDESC,STKUOM,USERFLD1,STKQTY,ASSET FROM ITEMMST WHERE ITEM = '"
							+ aItem.toUpperCase() + "'");
			rs = ps.executeQuery();
			while (rs.next()) {
				arrList.add(0, strUtils.fString((String) rs.getString("ITEM")));
				arrList.add(1, strUtils.fString((String) rs.getString(
						"ITEMDESC").trim()));
				arrList.add(2, strUtils
						.fString((String) rs.getString("STKUOM")));
				arrList.add(3, strUtils.fString((String) rs
						.getString("USERFLD1")));
				arrList.add(4, strUtils
						.fString((String) rs.getString("STKQTY")));
				arrList
						.add(3, strUtils
								.fString((String) rs.getString("ASSET")));
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}

	/**
	 * @method : queryItemMst(String aItem)
	 * @description : get the record details for the given item
	 * @param aItem
	 * @return list
	 */
	public List queryItemMst(String aItem) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sCondition = strUtils.fString(aItem).length() > 0 ? sCondition = "WHERE ITEM LIKE  '"
					+ aItem.toUpperCase() + "%'"
					: "";
			String sQry = "SELECT ITEM,ITEMDESC,ARTIST,TITLE,MEDIUM,REMARK1,ITEMCONDITION,STATUS,ISACTIVE FROM ITEMMST "
					+ sCondition;
			MLogger.log(0, "[SQL]" + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, strUtils.fString((String) rs.getString("ITEM")));
				lineVec.add(1, strUtils.fString((String) rs
						.getString("ITEMDESC")));
				lineVec.add(2, strUtils
						.fString((String) rs.getString("ARTIST")));
				lineVec
						.add(3, strUtils
								.fString((String) rs.getString("TITLE")));
				lineVec.add(4, strUtils
						.fString((String) rs.getString("MEDIUM")));
				lineVec.add(5, strUtils.fString((String) rs
						.getString("REMARK1")));
				lineVec.add(6, strUtils.fString((String) rs
						.getString("ITEMCONDITION")));
				lineVec.add(7, strUtils
						.fString((String) rs.getString("STATUS")));
			    lineVec.add(8, strUtils
			                    .fString((String) rs.getString("ISACTIVE")));
				listQty.add(lineVec);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}

	public List queryItemMst(String aItem, String plant,String cond) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sCondition = "WHERE ITEM LIKE  '"+ aItem.toUpperCase() + "%'";
			String sQry = "SELECT ITEM,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE FROM "
					+ "[" + plant + "_" + "ITEMMST " + "]" + sCondition +cond;
			MLogger.log(0, "[SQL]" + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, strUtils.fString((String) rs.getString("ITEM")));
				lineVec.add(1, strUtils.fString((String) rs
						.getString("ITEMDESC")));
				lineVec.add(2, strUtils.fString((String) rs
						.getString("ITEMTYPE")));
				lineVec.add(3, strUtils
						.fString((String) rs.getString("STKUOM")));
				lineVec.add(4, strUtils.fString((String) rs
						.getString("REMARK1")));
				lineVec.add(5, strUtils.fString((String) rs
						.getString("REMARK2")));
				lineVec.add(6, strUtils.fString((String) rs
						.getString("REMARK3")));
				lineVec.add(7, strUtils.fString((String) rs
						.getString("REMARK4")));
				lineVec.add(8, strUtils
						.fString((String) rs.getString("STKQTY")));
				lineVec
						.add(9, strUtils
								.fString((String) rs.getString("ASSET")));
				lineVec.add(10, strUtils.fString((String) rs
						.getString("PRD_CLS_ID")));
			    lineVec.add(11, strUtils.fString((String) rs
			                    .getString("ISACTIVE")));
				listQty.add(lineVec);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}

	public ArrayList getStockReorderItemList() throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList listQty = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = " SELECT A.ITEM,A.ITEMDESC,A.USERFLD1,SUM(B.QTY) FROM ITEMMST AS A, INVMST AS B WHERE A.ITEM = B.ITEM AND  A.USERFLD1 >= 1 "
					+ " AND  A.USERFLD1 >= (SELECT SUM(B.QTY) FROM INVMST AS B WHERE A.ITEM = B.ITEM) GROUP BY A.ITEM,A.ITEMDESC,A.USERFLD1";

			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1)));
				arrLine.add(1, strUtils.fString((String) rs.getString(2)));
				arrLine.add(2, strUtils.fString((String) rs.getString(3)));
				arrLine.add(3, strUtils.fString((String) rs.getString(4)));
				listQty.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;

	}
}