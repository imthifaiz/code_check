package com.track.session.itemloc;

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
public class ItemLocBean implements SessionBean {
	StrUtils strUtils = new StrUtils();
	SessionContext sessionContext;

	private String tblName = "ITEMLOCMST";
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
	 * @method : insertIntoItemMapMst(Hashtable ht) description : Insert the new
	 *         record into ITEMMAP
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean insertIntoItemLoc(Hashtable ht) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
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
				VALUES += "'" + value.toUpperCase() + "',";
			}
			String sQry = "INSERT INTO " + tblName + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			MLogger.log(0, "[SQL] : \n " + sQry);
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
	 * method : deleteItemMap(Hashtable ht) description : Delete the existing
	 * record from ITEMMAP
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean deleteItemLoc(Hashtable ht) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
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

			ps = con.prepareStatement("DELETE " + tblName + " WHERE "
					+ sCondition);
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
	 * @method : queryItemMst(String aItem)
	 * @description : get the record details for the given item
	 * @param aItem
	 * @return list
	 */
	public List queryItemLoc(String aItem) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sCondition = strUtils.fString(aItem).length() > 0 ? sCondition = "WHERE ITEM LIKE  '"
					+ aItem + "%'"
					: "";
			
			ps = con.prepareStatement("SELECT ITEM,LOC FROM ITEMLOCMST "
					+ sCondition);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, strUtils.fString((String) rs.getString("ITEM")));
				lineVec.add(1, strUtils.fString((String) rs.getString("LOC")));
				listQty.add(lineVec);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}

	/**
	 * @method : queryItemMst(String aItem)
	 * @description : get the record details for the given item
	 * @param aItem
	 * @return list
	 */
	public List getLocations4Item(String aItem) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listLoc = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT " + IConstants.LOC + " FROM " + tblName
					+ " WHERE " + IConstants.ITEM + " = '" + aItem + "'";
			
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				listLoc.add(strUtils.fString((String) rs.getString(1)));
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listLoc;
	}

	public boolean isExistItemAndLoc(String aItem, String aLoc)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT COUNT(" + IConstants.ITEM + ") FROM "
					+ tblName + " WHERE " + IConstants.ITEM + " = '"
					+ aItem.toUpperCase() + "' AND " + IConstants.LOC + " = '"
					+ aLoc.toUpperCase() + "'";
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

	public ArrayList getItemLocationList(String aItem, String aLoc)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrItemLoc = new ArrayList();
		try {
			con = DbBean.getConnection();
			String condition = "";
			condition += (aItem.length() > 0) ? " AND A.ITEM = '" + aItem + "'"
					: "";
			condition += (aLoc.length() > 0) ? " AND A.LOC = '" + aLoc + "'"
					: "";
			String sQry = "select A.ITEM,B.ITEMDESC,A.LOC from itemlocmst AS A, ITEMMST AS B WHERE A.ITEM = B.ITEM "
					+ condition;
			
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList lineArr = new ArrayList();
				lineArr.add(0, strUtils.fString(rs.getString(1)));
				lineArr.add(1, strUtils.fString(rs.getString(2)));
				lineArr.add(2, strUtils.fString(rs.getString(3)));
				arrItemLoc.add(lineArr);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrItemLoc;

	}

}