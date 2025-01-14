package com.track.session.itemmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.track.constants.IConstants;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@Deprecated
public class ItemMapBean implements SessionBean {
	StrUtils strUtils = new StrUtils();
	SessionContext sessionContext;

	private String tblName = "ITEMMAP";
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
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
		this.sessionContext = sessionContext;
	}

	/**
	 * @method : insertIntoItemMapMst(Hashtable ht) description : Insert the new
	 *         record into ITEMMAP
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean insertIntoItemMap(Hashtable ht) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
		boolean insertedItemMst = false;
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
			MLogger.log(0, "[SQL]" + sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedItemMst = true;

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedItemMst;
	}

	/**
	 * method : deleteItemMap(Hashtable ht) description : Delete the existing
	 * record from ITEMMAP
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean deleteItemMap(Hashtable ht) throws Exception {
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
			String sQry = "DELETE " + tblName + " WHERE " + sCondition;
			ps = con.prepareStatement(sQry);
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
	 * @method : isExistsMapItem(String aMapItem)
	 * @param aMapItem
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsMapItem(String aMapItem) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + tblName + " WHERE "
					+ IConstants.MAP_ITEM + " = '" + aMapItem.toUpperCase()
					+ "'";
			MLogger.log(0, "[SQL]" + sQry);

			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}

	/**
	 * @method : getMapItems4Key(String aKeyItem)
	 * @description : get the list of map items for the given key item
	 * @param aKeyItem
	 * @return
	 * @throws Exception
	 */
	public List getMapItems4Key(String aKeyItem) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listMapItems = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT " + IConstants.MAP_ITEM + " FROM " + tblName
					+ " WHERE " + IConstants.KEY_ITEM + " = '" + aKeyItem + "'";
			MLogger.log(0, "[SQL]" + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				listMapItems.add(strUtils.fString((String) rs.getString(1)));
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listMapItems;
	}

	/**
	 * @method : getKeyItem4MapItem(String aMapItem)
	 * @description : get the key item for the given map item
	 * @param aMapItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getKeyItem4MapItem(String aMapItem) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT A.KEYITEM,B.ITEMDESC FROM ITEMMAP AS A,ITEMMST AS B WHERE A.KEYITEM = B.ITEM AND MAPITEM = '"
					+ aMapItem + "'";
			MLogger.log(0, "[SQL]" + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrList.add(0, strUtils.fString((String) rs.getString(1)));
				arrList.add(1, strUtils.fString((String) rs.getString(2)));
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}
}