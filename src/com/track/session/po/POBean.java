package com.track.session.po;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.track.gates.DbBean;
import com.track.util.StrUtils;

@Deprecated
public class POBean implements SessionBean {
	StrUtils _strUtils = new StrUtils();
	SessionContext sessionContext;

	private String tblName = "PODET";
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
	 * @method : getCountOpenPO4Item(String aItem) description : Find the count
	 *         of new / open PO nos of given item.
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public int getCountOpenPO4Item(String aItem) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		int iCnt = 0;
		boolean insertedCust = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(PONO) FROM "
					+ tblName
					+ " WHERE ITEM = '"
					+ aItem
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCnt = rs.getInt(1);
			}
			// System.out.println(" getCountOpenPO4Item : " + sQry + " - " +
			// iCnt);
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCnt;
	}

	public int getCountOpenPO4Vendor(String aVendNo) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		int iCnt = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(PONO) FROM "
					+ tblName
					+ " WHERE USERFLD2 = '"
					+ aVendNo
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCnt = rs.getInt(1);
			}
			// System.out.println(" getCountOpenPO4Vendor : " + sQry+" - " +
			// iCnt);
		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		String sPONO = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT TOP 1 PONO FROM "
					+ tblName
					+ " WHERE USERFLD2 = '"
					+ aVendNo
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				sPONO = _strUtils.fString(rs.getString(1));
			}
			// System.out.println(" getPONO4Vendor : " + sQry+" - " + sPONO);
		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT PONO,USERFLD2,DELDATE FROM "
					+ tblName
					+ " WHERE PONO = '"
					+ aPONO
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, _strUtils.fString((String) rs.getString(1))); // PONO
				arrLine.add(1, _strUtils.fString((String) rs.getString(2))); // VENDNO
				arrLine.add(2, _strUtils.fString((String) rs.getString(3))); // DATE
				arrList.add(arrLine);
			}
		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCount = 0;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM PODET WHERE PONO = '"
					+ aPONO
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			// System.out.println("getCountOpenItems4PO : " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCount = rs.getInt(1);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCount;

	}

	public int getCountOpenItems4Vendor(String aVendNo) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCount = 0;
		try {
			con = DbBean.getConnection();
			String sQry = "";
			ps = con
					.prepareStatement("SELECT COUNT(*) FROM PODET WHERE VENDNO = '"
							+ aVendNo
							+ "' AND (ISNULL(POSTAT,'') = 'N' OR ISNULL(POSTAT,'') = 'O')");
			rs = ps.executeQuery();
			while (rs.next()) {
				iCount = rs.getInt(1);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCount;
	}

	public ArrayList getOpenItemDetails4PO(String aPONO) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ITEM,USERFLD1 FROM PODET WHERE PONO = '"
					+ aPONO
					+ "' AND (ISNULL(LNSTAT,'') = 'N' OR ISNULL(LNSTAT,'') = 'O')";
			// System.out.println(sQry);

			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, _strUtils.fString((String) rs.getString(1))); // Item
				arrLine.add(1, _strUtils.fString((String) rs.getString(2))); // Item
				// Desc
				arrList.add(arrLine);
			}
			System.out.println("getOpenItemDetails4PO :: arrList.size "
					+ arrList.size());
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList queryPODET_Col(List listQryFields, Hashtable ht,
			String aExtraCondition) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteItemmst = false;
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
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			sCondition = sCondition + " " + aExtraCondition;

			// get qry fields
			for (int i = 0; i < listQryFields.size(); i++) {
				sQryFields += _strUtils.fString((String) listQryFields.get(i))
						+ ",";
			}
			sQryFields = (sQryFields.length() > 0) ? sQryFields.substring(0,
					sQryFields.length() - 1) : "";
			String sQry = "SELECT " + sQryFields + " FROM " + tblName
					+ " WHERE " + sCondition;
			// System.out.println("queryPODET : " +sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				for (int iQryFiled = 1; iQryFiled <= listQryFields.size(); iQryFiled++) {
					String fieldValue = _strUtils.fString(rs
							.getString(iQryFiled));
					arrLine.add(fieldValue);
				}
				listQryResult.add(arrLine);
			}

		} catch (Exception e) {
			System.out.println("Exception : " + e.toString());
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}

	public boolean updatePodet(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean update = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = "", sCondition = "";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = _strUtils.fString((String) enumUpdate
						.nextElement());
				String value = _strUtils.fString((String) htUpdate.get(key));
				sUpdate = key + " = '" + value + "',";
			}

			// generate the update string
			Enumeration enumCondition = htUpdate.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = _strUtils.fString((String) enumCondition
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
				update = true;

		} catch (Exception e) {
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean update = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String stmt = "UPDATE " + tblName + " SET QTYRC=ISNULL(QTYRC,0)+"
					+ aRecvQty + ",LNSTAT = '" + aLnstatus + "',CRBY='"
					+ aUserid + "' WHERE PONO = '" + aPono + "' AND ITEM = '"
					+ aItem + "'";
			// System.out.println(stmt);
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
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		CallableStatement cs = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			cs = con.prepareCall("proc_view_postatus");
			rs = cs.executeQuery();

			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, _strUtils.fString((String) rs.getString(1))); // PONO
				arrLine.add(1, _strUtils.fString((String) rs.getString(2))); // UNAME
				arrLine.add(2, _strUtils.fString((String) rs.getString(3))); // VENAME
				arrLine.add(3, _strUtils.fString((String) rs.getString(4))); // STATEMENT
				arrLine.add(4, _strUtils.fString((String) rs.getString(5))); // COMMENTS
				arrList.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, cs);
		}
		return arrList;

	}
}