package com.track.session.customer;

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
import com.track.util.MLogger;
import com.track.util.StrUtils;

@Deprecated
public class CustomerBean implements SessionBean {
	StrUtils strUtils = new StrUtils();
	SessionContext sessionContext;

	private String tblName = "CUSTMST";
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
	 * @method : insertIntoCustomer(Hashtable ht) description : Insert the new
	 *         record into CUSTMST
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean insertIntoCustomer(Hashtable ht) throws Exception {
		System.out.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean insertedCust = false;
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
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedCust;
	}

	// created for vendor
	public boolean insertIntoVendor(Hashtable ht) throws Exception {
		System.out.println(this.getClass()+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean insertedCust = false;
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
					+ "Vendmst" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedCust;
	}

	/**
	 * method : deleteCustomer(String aCustno) description : Delete the existing
	 * record from CUSTNO
	 * 
	 * @param : String aCustno
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean deleteCustomer(String aCustno) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + tblName + " WHERE "
					+ IConstants.CUSTOMER_CODE + "='" + aCustno.toUpperCase()
					+ "'";
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

	public boolean deleteCustomer(String aCustno, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_" + tblName + "]"
					+ " WHERE " + IConstants.CUSTOMER_CODE + "='"
					+ aCustno.toUpperCase() + "'";
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

	public boolean deleteVendor(String aCustno, String plant) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_" + "Vendmst" + "]"
					+ " WHERE " + IConstants.VENDOR_CODE + "='"
					+ aCustno.toUpperCase() + "'";
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
	 * @method : isExistsCustomer(String aMapItem)
	 * @param aMapItem
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsCustomer(String aCustomer) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + tblName + " WHERE "
					+ IConstants.CUSTOMER_CODE + " = '"
					+ aCustomer.toUpperCase() + "'";
			MLogger.log(0, "[SQL] : \n " + sQry);
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

	public boolean isExistsCustomer(String aCustomer, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + tblName
					+ "]" + " WHERE " + IConstants.CUSTOMER_CODE + " = '"
					+ aCustomer.toUpperCase() + "'";
			MLogger.log(0, "[SQL] : \n " + sQry);
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

	public boolean isExistsVendor(String aCustomer, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "VendMst" + "]" + " WHERE " + IConstants.VENDOR_CODE
					+ " = '" + aCustomer.toUpperCase() + "'";
			MLogger.log(0, "[SQL] : \n " + sQry);
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
	 * @method : getCustomerList4ConMent(String aMapItem)
	 * @description : get the consignment customer list
	 * @return ArrayList
	 * @throws Exception
	 */
	public ArrayList getCustomerList4ConMent() throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME FROM CUSTMST WHERE ISNULL(USERFLG1,'') = 'Y'";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrList.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}

	/**
	 * @method : getCustomerList4(String aMapItem)
	 * @description : get the consignment customer list
	 * @return ArrayList
	 * @throws Exception
	 */
	public ArrayList getCustomerList() throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,LNAME FROM CUSTMST";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrLine.add(2, strUtils.fString((String) rs.getString(3))); // lname
				arrList.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}

	/**
	 * @method : getCustomerList4(String aMapItem)
	 * @description : get the consignment customer list
	 * @return ArrayList
	 * @throws Exception
	 */
	public ArrayList getCustomerDetails(String aCustCode) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS FROM CUSTMST WHERE CUSTNO = '"
					+ aCustCode + "'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrCust.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrCust.add(2, strUtils.fString((String) rs.getString(3))); // Address1
				arrCust.add(3, strUtils.fString((String) rs.getString(4))); // Address2
				arrCust.add(4, strUtils.fString((String) rs.getString(5))); // Address3
				arrCust.add(5, strUtils.fString((String) rs.getString(6))); // country
				arrCust.add(6, strUtils.fString((String) rs.getString(7))); // zip
				arrCust.add(7, strUtils.fString((String) rs.getString(8))); // userflg1
				arrCust.add(8, strUtils.fString((String) rs.getString(9))); // LASTNAME
				arrCust.add(9, strUtils.fString((String) rs.getString(10)));// name
				arrCust.add(10, strUtils.fString((String) rs.getString(11)));// designation
				arrCust.add(11, strUtils.fString((String) rs.getString(12)));// telno
				arrCust.add(12, strUtils.fString((String) rs.getString(13)));// hpno
				arrCust.add(13, strUtils.fString((String) rs.getString(14)));// fax
				arrCust.add(14, strUtils.fString((String) rs.getString(15)));// email
				arrCust.add(15, strUtils.fString((String) rs.getString(16)));// remarks

			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public ArrayList getCustomerDetails(String aCustCode, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ " WHERE CUSTNO = '"
					+ aCustCode + "'";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrCust.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrCust.add(2, strUtils.fString((String) rs.getString(3))); // Address1
				arrCust.add(3, strUtils.fString((String) rs.getString(4))); // Address2
				arrCust.add(4, strUtils.fString((String) rs.getString(5))); // Address3
				arrCust.add(5, strUtils.fString((String) rs.getString(6))); // country
				arrCust.add(6, strUtils.fString((String) rs.getString(7))); // zip
				arrCust.add(7, strUtils.fString((String) rs.getString(8))); // userflg1
				arrCust.add(8, strUtils.fString((String) rs.getString(9))); // LASTNAME

				arrCust.add(9, strUtils.fString((String) rs.getString(10)));// name
				arrCust.add(10, strUtils.fString((String) rs.getString(11)));// designation
				arrCust.add(11, strUtils.fString((String) rs.getString(12)));// telno
				arrCust.add(12, strUtils.fString((String) rs.getString(13)));// hpno
				arrCust.add(13, strUtils.fString((String) rs.getString(14)));// fax
				arrCust.add(14, strUtils.fString((String) rs.getString(15)));// email
				arrCust.add(15, strUtils.fString((String) rs.getString(16)));// remarks
				arrCust.add(16, strUtils.fString((String) rs.getString(17)));// address4
				arrCust.add(17, strUtils.fString((String) rs.getString(18)));// address4

			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public ArrayList getVendorDetails(String aCustCode, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT VENDNO,VNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
					+ " WHERE VENDNO = '"
					+ aCustCode + "'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrCust.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrCust.add(2, strUtils.fString((String) rs.getString(3))); // Address1
				arrCust.add(3, strUtils.fString((String) rs.getString(4))); // Address2
				arrCust.add(4, strUtils.fString((String) rs.getString(5))); // Address3
				arrCust.add(5, strUtils.fString((String) rs.getString(6))); // country
				arrCust.add(6, strUtils.fString((String) rs.getString(7))); // zip
				arrCust.add(7, strUtils.fString((String) rs.getString(8))); // cons
				// arrCust.add(8,strUtils.fString((String)rs.getString(9)));
				// //contactname
				arrCust.add(8, strUtils.fString((String) rs.getString(9))); // contactname
				arrCust.add(9, strUtils.fString((String) rs.getString(10))); // desg
				arrCust.add(10, strUtils.fString((String) rs.getString(11))); // telno
				arrCust.add(11, strUtils.fString((String) rs.getString(12))); // hpno
				arrCust.add(12, strUtils.fString((String) rs.getString(13))); // email
				arrCust.add(13, strUtils.fString((String) rs.getString(14))); // fax
				arrCust.add(14, strUtils.fString((String) rs.getString(15))); // remarks
				arrCust.add(15, strUtils.fString((String) rs.getString(16)));// addr4
				arrCust.add(16, strUtils.fString((String) rs.getString(17)));// iscative
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public ArrayList getVendorByName(String custname, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT VENDNO,VNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4 FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
					+ " WHERE VNAME like '" + custname + "%'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrCust.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrCust.add(2, strUtils.fString((String) rs.getString(3))); // Address1
				arrCust.add(3, strUtils.fString((String) rs.getString(4))); // Address2
				arrCust.add(4, strUtils.fString((String) rs.getString(5))); // Address3
				arrCust.add(5, strUtils.fString((String) rs.getString(6))); // country
				arrCust.add(6, strUtils.fString((String) rs.getString(7))); // zip
				arrCust.add(7, strUtils.fString((String) rs.getString(8))); // cons
				// arrCust.add(8,strUtils.fString((String)rs.getString(9)));
				// //contactname
				arrCust.add(8, strUtils.fString((String) rs.getString(9))); // contactname
				arrCust.add(9, strUtils.fString((String) rs.getString(10))); // desg
				arrCust.add(10, strUtils.fString((String) rs.getString(11))); // telno
				arrCust.add(11, strUtils.fString((String) rs.getString(12))); // hpno
				arrCust.add(12, strUtils.fString((String) rs.getString(13))); // email
				arrCust.add(13, strUtils.fString((String) rs.getString(14))); // fax
				arrCust.add(14, strUtils.fString((String) rs.getString(15))); // remarks
				arrCust.add(15, strUtils.fString((String) rs.getString(16)));// addr4
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public boolean updateCustomer(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean update = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = strUtils
						.fString((String) enumUpdate.nextElement());
				String value = strUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			// generate the update string
			Enumeration enumCondition = htCondition.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = strUtils.fString((String) enumCondition
						.nextElement());
				String value = strUtils.fString((String) htCondition.get(key));
				System.out.println("Key" + key + "value" + value);
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ tblName + "] " + sUpdate + sCondition;
			System.out.println(stmt);
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

	public boolean updateVendor(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean update = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = strUtils
						.fString((String) enumUpdate.nextElement());
				String value = strUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			// generate the update string
			Enumeration enumCondition = htCondition.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = strUtils.fString((String) enumCondition
						.nextElement());
				String value = strUtils.fString((String) htCondition.get(key));
				System.out.println("Key" + key + "value" + value);
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ "Vendmst" + "] " + sUpdate + sCondition;
			MLogger.log(0, "[SQL] : \n " + stmt);
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

	public ArrayList getOutGoingCustomerDetails(String aPlant,
			String aCustName, String cond) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,LNAME,ISNULL(NAME,''),ISNULL(TELNO,''),ISNULL(EMAIL,''),ISNULL(ADDR1,''),ISNULL(ADDR2,''),ISNULL(ADDR3,''),ISNULL(ADDR4,''),ISNULL(COUNTRY,''),ISNULL(REMARKS,''),ISNULL(ZIP,'') FROM "
					+ "["
					+ aPlant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ "   WHERE CNAME LIKE '" + aCustName + "%'" + cond;
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrLine.add(2, strUtils.fString((String) rs.getString(3))); // cname
				arrLine.add(3, strUtils.fString((String) rs.getString(4))); // contactname
				arrLine.add(4, strUtils.fString((String) rs.getString(5))); // Telno
				arrLine.add(5, strUtils.fString((String) rs.getString(6))); // Email
				arrLine.add(6, strUtils.fString((String) rs.getString(7))); // Add1
				arrLine.add(7, strUtils.fString((String) rs.getString(8))); // Add2
				arrLine.add(8, strUtils.fString((String) rs.getString(9))); // Add3
				arrLine.add(9, strUtils.fString((String) rs.getString(10))); // Add4
				arrLine.add(10, strUtils.fString((String) rs.getString(11))); // Country
				arrLine.add(11, strUtils.fString((String) rs.getString(12))); // Remarks
				arrLine.add(12, strUtils.fString((String) rs.getString(13))); // Zip

				arrList.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getCustomerListStartsWithName(String aCustName)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,LNAME CUSTMST   WHERE CNAME LIKE '"
					+ aCustName + "%'";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrLine.add(2, strUtils.fString((String) rs.getString(3))); // cname

				arrList.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getCustomerListStartsWithName(String aCustName,
			String plant) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,LNAME,NAME,TELNO,DESGINATION,FAX,EMAIL,HPNO,ADDR1,ADDR2,ADDR3,ADDR4,COUNTRY,ZIP,REMARKS,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ " WHERE CNAME LIKE '" + aCustName + "%'";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrLine.add(2, strUtils.fString((String) rs.getString(3))); // cname
				arrLine.add(3, strUtils.fString((String) rs.getString(4))); // contact
				// name
				arrLine.add(4, strUtils.fString((String) rs.getString(5))); // telno
				arrLine.add(5, strUtils.fString((String) rs.getString(6))); // designa
				arrLine.add(6, strUtils.fString((String) rs.getString(7))); // fax
				arrLine.add(7, strUtils.fString((String) rs.getString(8))); // email
				arrLine.add(8, strUtils.fString((String) rs.getString(9))); // hpno
				arrLine.add(9, strUtils.fString((String) rs.getString(10))); // addr1
				arrLine.add(10, strUtils.fString((String) rs.getString(11))); // addr2
				arrLine.add(11, strUtils.fString((String) rs.getString(12))); // addr3
				arrLine.add(12, strUtils.fString((String) rs.getString(13))); // addr4
				arrLine.add(13, strUtils.fString((String) rs.getString(14))); // cnty
				arrLine.add(14, strUtils.fString((String) rs.getString(15))); // zip
				arrLine.add(15, strUtils.fString((String) rs.getString(16))); // remarks
				arrLine.add(16, strUtils.fString((String) rs.getString(17))); // isactive
				arrList.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getVendorListStartsWithName(String aCustName,
			String plant, String cond) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT VENDNO,VNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADD1,ISNULL(ADDR2,'') ADD2,ISNULL(ADDR3,'') ADD3,ISNULL(REMARKS,'')REMARKS,ADDR4,COUNTRY,ZIP,DESGINATION,HPNO,FAX,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
					+ " WHERE VNAME LIKE '" + aCustName + "%'" + cond;
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrLine.add(2, strUtils.fString((String) rs.getString(3))); // contactname
				arrLine.add(3, strUtils.fString((String) rs.getString(4))); // telno
				arrLine.add(4, strUtils.fString((String) rs.getString(5))); // email
				arrLine.add(5, strUtils.fString((String) rs.getString(6))); // addr1
				arrLine.add(6, strUtils.fString((String) rs.getString(7))); // addr2
				arrLine.add(7, strUtils.fString((String) rs.getString(8))); // addr3
				arrLine.add(8, strUtils.fString((String) rs.getString(9))); // remarks
				arrLine.add(9, strUtils.fString((String) rs.getString(10))); // addr4
				arrLine.add(10, strUtils.fString((String) rs.getString(11))); // country
				arrLine.add(11, strUtils.fString((String) rs.getString(12))); // zip
				arrLine.add(12, strUtils.fString((String) rs.getString(13))); // design
				arrLine.add(13, strUtils.fString((String) rs.getString(14))); // hpno
				arrLine.add(14, strUtils.fString((String) rs.getString(15))); // design
				arrLine.add(15, strUtils.fString((String) rs.getString(16))); // hpno
				arrList.add(arrLine);

				// SELECT
				// VENDNO,VNAME,NAME,TELNO,EMAIL,ADDR1,ADDR2,ADDR3,REMARKS
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public boolean isExistsLoanAssignee(String assignee, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "LOAN_ASSIGNEE_MST" + "]" + " WHERE "
					+ IConstants.LOAN_ASSIGNEE_CODE + " = '"
					+ assignee.toUpperCase() + "'";
			MLogger.log(0, "[SQL] : \n " + sQry);
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

	public boolean updateLoanAssignee(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean update = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = strUtils
						.fString((String) enumUpdate.nextElement());
				String value = strUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			// generate the update string
			Enumeration enumCondition = htCondition.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = strUtils.fString((String) enumCondition
						.nextElement());
				String value = strUtils.fString((String) htCondition.get(key));
				System.out.println("Key" + key + "value" + value);
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ "LOAN_ASSIGNEE_MST" + "] " + sUpdate + sCondition;
			System.out.println(stmt);
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

	public boolean deleteLoanAssignee(String aConsignee, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_"
					+ "LOAN_ASSIGNEE_MST" + "]" + " WHERE "
					+ IConstants.LOAN_ASSIGNEE_CODE + "='"
					+ aConsignee.toUpperCase() + "'";
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

	// created for LoanAssignee
	public boolean insertIntoLoanAssignee(Hashtable ht) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean insertedCust = false;
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
					+ "LOAN_ASSIGNEE_MST" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedCust;
	}

	public ArrayList getLoanAssigneeDetails(String aCustCode, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT LASSIGNNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "LOAN_ASSIGNEE_MST"
					+ "]"
					+ " WHERE LASSIGNNO = '" + aCustCode + "'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrCust.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrCust.add(2, strUtils.fString((String) rs.getString(3))); // Address1
				arrCust.add(3, strUtils.fString((String) rs.getString(4))); // Address2
				arrCust.add(4, strUtils.fString((String) rs.getString(5))); // Address3
				arrCust.add(5, strUtils.fString((String) rs.getString(6))); // country
				arrCust.add(6, strUtils.fString((String) rs.getString(7))); // zip
				arrCust.add(7, strUtils.fString((String) rs.getString(8))); // cons

				arrCust.add(8, strUtils.fString((String) rs.getString(9))); // contactname
				arrCust.add(9, strUtils.fString((String) rs.getString(10))); // desg
				arrCust.add(10, strUtils.fString((String) rs.getString(11))); // telno
				arrCust.add(11, strUtils.fString((String) rs.getString(12))); // hpno
				arrCust.add(12, strUtils.fString((String) rs.getString(13))); // email
				arrCust.add(13, strUtils.fString((String) rs.getString(14))); // fax
				arrCust.add(14, strUtils.fString((String) rs.getString(15))); // remarks
				arrCust.add(15, strUtils.fString((String) rs.getString(16)));// addr4
				arrCust.add(16, strUtils.fString((String) rs.getString(17)));// isactive
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public ArrayList getLoanAssigneeListStartsWithName(String aCustName,
			String aCustCode, String plant, String cond) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT LASSIGNNO,CNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADD1,ISNULL(ADDR2,'') ADD2,ISNULL(ADDR3,'') ADD3,ISNULL(REMARKS,'')REMARKS,ADDR4,COUNTRY,ZIP,DESGINATION,HPNO,ISNULL(FAX,'')FAX,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "LOAN_ASSIGNEE_MST"
					+ "]"
					+ " WHERE CNAME LIKE '"
					+ aCustName
					+ "%' and LASSIGNNO LIKE '" + aCustCode + "%'" + cond;
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrLine.add(2, strUtils.fString((String) rs.getString(3))); // contactname
				arrLine.add(3, strUtils.fString((String) rs.getString(4))); // telno
				arrLine.add(4, strUtils.fString((String) rs.getString(5))); // email
				arrLine.add(5, strUtils.fString((String) rs.getString(6))); // addr1
				arrLine.add(6, strUtils.fString((String) rs.getString(7))); // addr2
				arrLine.add(7, strUtils.fString((String) rs.getString(8))); // addr3
				arrLine.add(8, strUtils.fString((String) rs.getString(9))); // remarks
				arrLine.add(9, strUtils.fString((String) rs.getString(10))); // addr4
				arrLine.add(10, strUtils.fString((String) rs.getString(11))); // country
				arrLine.add(11, strUtils.fString((String) rs.getString(12))); // zip
				arrLine.add(12, strUtils.fString((String) rs.getString(13))); // design
				arrLine.add(13, strUtils.fString((String) rs.getString(14))); // hpno
				arrLine.add(14, strUtils.fString((String) rs.getString(15))); // fax
				arrLine.add(15, strUtils.fString((String) rs.getString(16))); // isActive
				arrList.add(arrLine);

			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getToAssingeListStartsWithName(String aCustName,
			String plant) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT ASSIGNENO,ASSIGNENAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADD1,ISNULL(ADDR2,'') ADD2,ISNULL(ADDR3,'') ADD3,ISNULL(REMARKS,'')REMARKS,ADDR4,COUNTRY,ZIP,DESGINATION,HPNO,ISNULL(FAX,'') FAX,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "TO_ASSIGNEE_MASTER"
					+ "]"
					+ " WHERE ASSIGNENAME LIKE '" + aCustName + "%'";
			System.out.println("TO_ASSIGNEE list starts" + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrLine.add(2, strUtils.fString((String) rs.getString(3))); // contactname
				arrLine.add(3, strUtils.fString((String) rs.getString(4))); // telno
				arrLine.add(4, strUtils.fString((String) rs.getString(5))); // email
				arrLine.add(5, strUtils.fString((String) rs.getString(6))); // addr1
				arrLine.add(6, strUtils.fString((String) rs.getString(7))); // addr2
				arrLine.add(7, strUtils.fString((String) rs.getString(8))); // addr3
				arrLine.add(8, strUtils.fString((String) rs.getString(9))); // remarks
				arrLine.add(9, strUtils.fString((String) rs.getString(10))); // addr4
				arrLine.add(10, strUtils.fString((String) rs.getString(11))); // country
				arrLine.add(11, strUtils.fString((String) rs.getString(12))); // zip
				arrLine.add(12, strUtils.fString((String) rs.getString(13))); // design
				arrLine.add(13, strUtils.fString((String) rs.getString(14))); // hpno
				arrLine.add(14, strUtils.fString((String) rs.getString(15))); // hpno
				arrLine.add(15, strUtils.fString((String) rs.getString(16))); // hpno
				arrList.add(arrLine);

				// SELECT
				// VENDNO,VNAME,NAME,TELNO,EMAIL,ADDR1,ADDR2,ADDR3,REMARKS
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public boolean isExistsToAssignee(String aCustomer, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "TO_ASSIGNEE_MASTER" + "]" + " WHERE ASSIGNENO" + " = '"
					+ aCustomer.toUpperCase() + "'";
			MLogger.log(0, "[SQL] : \n " + sQry);
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

	public ArrayList getOutGoingToAssigneeDetails(String aPlant,
			String aCustName, String cond) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ASSIGNENO,ASSIGNENAME,ISNULL(NAME,''),ISNULL(TELNO,''),ISNULL(EMAIL,''),ISNULL(ADDR1,''),ISNULL(ADDR2,''),ISNULL(ADDR3,''),ISNULL(ADDR4,''),ISNULL(COUNTRY,''),ISNULL(REMARKS,''),ISNULL(ZIP,'') FROM "
					+ "["
					+ aPlant
					+ "_"
					+ "TO_ASSIGNEE_MASTER"
					+ "]"
					+ "   WHERE ASSIGNENAME LIKE '" + aCustName + "%'" + cond;

			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, strUtils.fString((String) rs.getString(1))); // custno

				arrLine.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrLine.add(2, strUtils.fString((String) rs.getString(3))); // cname
				arrLine.add(3, strUtils.fString((String) rs.getString(4))); // contactname
				arrLine.add(4, strUtils.fString((String) rs.getString(5))); // Telno
				arrLine.add(5, strUtils.fString((String) rs.getString(6))); // Email
				arrLine.add(6, strUtils.fString((String) rs.getString(7))); // Add1
				arrLine.add(7, strUtils.fString((String) rs.getString(8))); // Add2
				arrLine.add(8, strUtils.fString((String) rs.getString(9))); // Add3
				arrLine.add(9, strUtils.fString((String) rs.getString(10))); // Add4
				arrLine.add(10, strUtils.fString((String) rs.getString(11))); // Country
				arrLine.add(11, strUtils.fString((String) rs.getString(12))); // Remarks

				arrList.add(arrLine);
			}

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public boolean updateToAssignee(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean update = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = strUtils
						.fString((String) enumUpdate.nextElement());
				String value = strUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			Enumeration enumCondition = htCondition.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = strUtils.fString((String) enumCondition
						.nextElement());
				String value = strUtils.fString((String) htCondition.get(key));

				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ "TO_ASSIGNEE_MASTER" + "] " + sUpdate + sCondition;
			MLogger.log(0, "[SQL] : \n " + stmt);
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

	public ArrayList getToAssigneeDetails(String aCustCode, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ASSIGNENO,ASSIGNENAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "TO_ASSIGNEE_MASTER"
					+ "]"
					+ " WHERE ASSIGNENO = '" + aCustCode + "'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, strUtils.fString((String) rs.getString(1))); // custno
				arrCust.add(1, strUtils.fString((String) rs.getString(2))); // cname
				arrCust.add(2, strUtils.fString((String) rs.getString(3))); // Address1
				arrCust.add(3, strUtils.fString((String) rs.getString(4))); // Address2
				arrCust.add(4, strUtils.fString((String) rs.getString(5))); // Address3
				arrCust.add(5, strUtils.fString((String) rs.getString(6))); // country
				arrCust.add(6, strUtils.fString((String) rs.getString(7))); // zip
				arrCust.add(7, strUtils.fString((String) rs.getString(8))); // cons

				arrCust.add(8, strUtils.fString((String) rs.getString(9))); // contactname
				arrCust.add(9, strUtils.fString((String) rs.getString(10))); // desg
				arrCust.add(10, strUtils.fString((String) rs.getString(11))); // telno
				arrCust.add(11, strUtils.fString((String) rs.getString(12))); // hpno
				arrCust.add(12, strUtils.fString((String) rs.getString(13))); // email
				arrCust.add(13, strUtils.fString((String) rs.getString(14))); // fax
				arrCust.add(14, strUtils.fString((String) rs.getString(15))); // remarks
				arrCust.add(15, strUtils.fString((String) rs.getString(16)));// addr4
				arrCust.add(16, strUtils.fString((String) rs.getString(17)));// isactive
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public boolean deleteToAssignee(String aCustno, String plant)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_"
					+ "TO_ASSIGNEE_MASTER" + "]" + " WHERE ASSIGNENO" + "='"
					+ aCustno.toUpperCase() + "'";
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

	public boolean insertIntoToAssignee(Hashtable ht) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean insertedCust = false;
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
					+ "TO_ASSIGNEE_MASTER" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			MLogger.log(0, "[SQL] : \n " + sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedCust;
	}

}