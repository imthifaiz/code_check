package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class CustomerBeanDAO extends BaseDAO {
	private boolean printQuery = MLoggerConstant.CUSTOMERBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CUSTOMERBEANDAO_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	StrUtils strUtils = new StrUtils();

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

	private String tblName = "CUSTMST";

	/**
	 * @method : insertIntoCustomer(Hashtable ht) description : Insert the new
	 *         record into CUSTMST
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean insertIntoCustomer(Hashtable ht) throws Exception {
		boolean insertedCust = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value + "',";
			}
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ tblName + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedCust;
	}

	// created for vendor
	public boolean insertIntoVendor(Hashtable ht) throws Exception {
		boolean insertedCust = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value + "',";
			}
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "Vendmst" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
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
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + tblName + " WHERE "
					+ IConstants.CUSTOMER_CODE + "='" + aCustno.toUpperCase()
					+ "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}

	public boolean deleteCustomer(String aCustno, String plant)
			throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_" + tblName + "]"
					+ " WHERE " + IConstants.CUSTOMER_CODE + "='"
					+ aCustno.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}

	public boolean deleteVendor(String aCustno, String plant) throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_" + "Vendmst" + "]"
					+ " WHERE " + IConstants.VENDOR_CODE + "='"
					+ aCustno.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + tblName + " WHERE "
					+ IConstants.CUSTOMER_CODE + " = '"
					+ aCustomer.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}

	public boolean isExistsCustomerCode(String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + tblName
					+ "]" + " WHERE " + IConstants.CUSTOMER_CODE + " = '"
					+ aCustomer.toUpperCase() + "'" ;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
	public boolean isExistsCustomer(String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + tblName
					+ "]" + " WHERE " + IConstants.CUSTOMER_CODE + " = '"
					+ aCustomer.toUpperCase() + "'" + " OR " +IConstants.CUSTOMER_NAME+ " = '"+ aCustomer.toUpperCase() + "'" ;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
	
	public boolean isExistsAppCustomer(String aCustomer, String item,String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_APPORDERQTYCONFIG" 
					+ "]" + " WHERE " + IConstants.CUSTOMER_CODE + " = '"
					+ aCustomer.toUpperCase() + "'" + " AND " +IConstants.ITEM+ " = '"+ item.toUpperCase() + "'" ;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
	public boolean isExistsCustomerLoan(String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;

		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "LOAN_ASSIGNEE_MST"
					+ "]" + " WHERE " + IConstants.LOAN_ASSIGNEE_CODE + " = '"
					+ aCustomer.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
    public boolean isExistsCustomerName(String aCustomerName, String plant)
                    throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean isExists = false;
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + tblName
                                    + "]" + " WHERE " + IConstants.CUSTOMER_NAME + " = '"
                                    + strUtils.InsertQuotes(aCustomerName).toUpperCase() + "'";
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            if (rs.getInt(1) > 0)
                                    isExists = true;
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return isExists;
    }
	public boolean isExistsVendor(String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "VendMst" + "]" + " WHERE " + IConstants.VENDOR_CODE
					+ " = '" + aCustomer.toUpperCase() + "'" + " OR " +IConstants.VENDOR_NAME+ " = '"+ aCustomer.toUpperCase() + "'" ;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
	
    public boolean isExistsVendorName(String avendName, String plant)
                    throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean isExists = false;
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
                                    + "VendMst" + "]" + " WHERE " + IConstants.VENDOR_NAME
                                    + " = '" + avendName.toUpperCase() + "'";
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            if (rs.getInt(1) > 0)
                                    isExists = true;
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME FROM CUSTMST WHERE ISNULL(USERFLG1,'') = 'Y'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrLine.add(1, StrUtils.fString(rs.getString(2))); // cname
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
	 * @method : getCustomerList4(String aMapItem)
	 * @description : get the consignment customer list
	 * @return ArrayList
	 * @throws Exception
	 */
	public ArrayList getCustomerList() throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,LNAME FROM CUSTMST";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrLine.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrLine.add(2, StrUtils.fString(rs.getString(3))); // lname
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
	 * @method : getCustomerList4(String aMapItem)
	 * @description : get the consignment customer list
	 * @return ArrayList
	 * @throws Exception
	 */
	public ArrayList getCustomerDetails(String aCustCode) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,CURRENCY_ID FROM CUSTMST WHERE CUSTNO = '"
					+ aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
				arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
				arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
				arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
				arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
				arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
				arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
				arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// currency

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}
	public ArrayList getUserCustDetails(String selectList, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;

		Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + tblName + "]");

		try {
			con = DbBean.getConnection();

			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0) {
				sql.append(" ");
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}
	
	public boolean deleteUserCustomer(String aUser, String plant) throws Exception {

		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_USERCUSTOMER]"
					+ " WHERE USERID ='"+aUser.toUpperCase()+"'";
			ps = con.prepareStatement(sQry);
			this.mLogger.query(this.printQuery, sQry);
			deleteItemMst = DeleteRow(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return deleteItemMst;
	}
	
	public boolean insertIntoUserCustMst(Hashtable ht) throws Exception {
		boolean insertedCycleCount = false;

		Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_USERCUSTOMER]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query.toString());

			insertedCycleCount = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(conn);
		}

		return insertedCycleCount;
	}
	
	public boolean isAlreadtUserCustExists(String aUser,String aLoc,String plant) throws Exception {
		Connection con = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_USERCUSTOMER] WHERE "
                                        + "  USERID ='"+aUser.toUpperCase()+"' AND " + "CUSTNO"+ "='" + aLoc.toUpperCase()
                                        + "'";
			this.mLogger.query(this.printQuery, sQry);
			isExists = isExists(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return isExists;
	}

	public ArrayList getCustomerDetails(String aCustCode, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE,ISNULL(PAY_TERMS,'')  as PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(CUSTOMER_TYPE_ID,'') CUSTOMER_TYPE_ID, "
					+ " ISNULL(CUSTOMER_STATUS_ID,'') CUSTOMER_STATUS_ID,ISNULL(STATE,'') STATE,ISNULL(RCBNO,'') RCBNO,ISNULL(CREDITLIMIT,0) as CREDITLIMIT,ISNULL(ISCREDITLIMIT,'') as ISCREDITLIMIT,ISNULL(CUSTOMEREMAIL,'')CUSTOMEREMAIL,ISNULL(WEBSITE,'')WEBSITE,ISNULL(FACEBOOKID,'')FACEBOOK,ISNULL(TWITTERID,'')TWITTER,ISNULL(LINKEDINID,'')LINKEDIN,ISNULL(SKYPEID,'')SKYPE,ISNULL(OPENINGBALANCE,'')OPENINGBALANCE,ISNULL(WORKPHONE,'')WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(CREDIT_LIMIT_BY,'') CREDIT_LIMIT_BY,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER ,'') COMPANYREGNUMBER ,ISNULL(CURRENCY_ID ,'') CURRENCY_ID ,ISNULL(ISSHOWBYPRODUCT ,0) ISSHOWBYPRODUCT,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY_CODE,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,ISNULL(ISSHOWAPPCATEGORYIMAGE,0) ISSHOWAPPCATEGORYIMAGE,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=SHIPCOUNTRY_CODE),'') SHIPCOUNTRY_CODE,ISNULL(PAYMENT_TERMS,'') as PAYMENTTERM,ISNULL(ISSAMEASCONTACTADD ,0) as ISSAMEASCONTACTADD,ISNULL(DATEOFBIRTH,'') as DATEOFBIRTH,ISNULL(NATIONALITY,'') as NATIONALITY,ISNULL(ISPEPPOL,'0') as ISPEPPOL,ISNULL(PEPPOL_ID,'') as PEPPOL_ID,ISNULL(SHOWSALESBYPURCHASECOST ,0)as SHOWSALESBYPURCHASECOST,ISNULL(CUST_ADDONCOST ,0)as CUST_ADDONCOST,ISNULL(ADDONCOSTTYPE ,0)as ADDONCOSTTYPE FROM"
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ " WHERE CUSTNO = '"
					+ aCustCode + "' OR CNAME = '"
					+ aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
		
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
				arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
				arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
				arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
				arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
				arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
				arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
				arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
				arrCust.add(17, StrUtils.fString(rs.getString(18)));// isActive
				arrCust.add(18, StrUtils.fString(rs.getString(19)));// Payment type
				arrCust.add(19, StrUtils.fString(rs.getString(20)));// Payment in Days
				arrCust.add(20, StrUtils.fString(rs.getString(21)));// Customr_Type_Id
				arrCust.add(21, StrUtils.fString(rs.getString(22)));// Customr_Status_Id
				arrCust.add(22, StrUtils.fString(rs.getString(23)));// State
				arrCust.add(23, StrUtils.fString(rs.getString(24)));// RCBNO
				arrCust.add(24, StrUtils.fString(rs.getString(25)));// CREDIT LIMIT
				arrCust.add(25, StrUtils.fString(rs.getString(26)));//ISCREDIT LIMIT
				arrCust.add(26, StrUtils.fString(rs.getString(27)));//customr email
				arrCust.add(27, StrUtils.fString(rs.getString(28)));//website
				arrCust.add(28, StrUtils.fString(rs.getString(29)));//facebook
				arrCust.add(29, StrUtils.fString(rs.getString(30)));//twitter
				arrCust.add(30, StrUtils.fString(rs.getString(31)));//linkedin
				arrCust.add(31, StrUtils.fString(rs.getString(32)));//skype
				arrCust.add(32, StrUtils.fString(rs.getString(33)));//opening balance
				arrCust.add(33, StrUtils.fString(rs.getString(34)));//workphone
				arrCust.add(34, StrUtils.fString(rs.getString(35)));//TAXTREATMENT
				arrCust.add(35, StrUtils.fString(rs.getString(36)));//CREDIT_LIMIT_BY
				arrCust.add(36, StrUtils.fString(rs.getString(37)));//COUNTRY_CODE
				arrCust.add(37, StrUtils.fString(rs.getString(38)));//BANKNAME
				arrCust.add(38, StrUtils.fString(rs.getString(39)));//BRANCH
				arrCust.add(39, StrUtils.fString(rs.getString(40)));//IBAN
				arrCust.add(40, StrUtils.fString(rs.getString(41)));//BANKROUTINGCODE
				arrCust.add(41, StrUtils.fString(rs.getString(42)));//companyuen
				arrCust.add(42, StrUtils.fString(rs.getString(43)));//currency
				arrCust.add(43, StrUtils.fString(rs.getString(44)));//show_product
				
			
				arrCust.add(44, StrUtils.fString(rs.getString(45)));//SHIP_CONTACTNAME
				arrCust.add(45, StrUtils.fString(rs.getString(46)));//SHIP_DESGINATION
				arrCust.add(46, StrUtils.fString(rs.getString(47)));//SHIP_WORKPHONE
				arrCust.add(47, StrUtils.fString(rs.getString(48)));//SHIP_HPNO
				arrCust.add(48, StrUtils.fString(rs.getString(49)));//SHIP_EMAIL
				arrCust.add(49, StrUtils.fString(rs.getString(50)));//SHIP_COUNTRY
				arrCust.add(50, StrUtils.fString(rs.getString(51)));//SHIP_ADDR1
				arrCust.add(51, StrUtils.fString(rs.getString(52)));//SHIP_ADDR2
				arrCust.add(52, StrUtils.fString(rs.getString(53)));//SHIP_ADDR3
				arrCust.add(53, StrUtils.fString(rs.getString(54)));//SHIP_ADDR4
				arrCust.add(54, StrUtils.fString(rs.getString(55)));//SHIP_STATE
				arrCust.add(55, StrUtils.fString(rs.getString(56)));//SHIP_ZIP
				arrCust.add(56, StrUtils.fString(rs.getString(57)));//ISSHOWAPPCATEGORYIMAGE
				arrCust.add(57, StrUtils.fString(rs.getString(58)));//TRANSPORT
				arrCust.add(58, StrUtils.fString(rs.getString(59)));//SHIPCOUNTRY_CODE
				arrCust.add(59, StrUtils.fString(rs.getString(60)));// Payment terms
				arrCust.add(60, StrUtils.fString(rs.getString(61)));// SameAsContactAddress
				arrCust.add(61, StrUtils.fString(rs.getString(62)));// DOB
				arrCust.add(62, StrUtils.fString(rs.getString(63)));// NATIONALITY
				arrCust.add(63, StrUtils.fString(rs.getString(64)));// ISPEPPOL
				arrCust.add(64, StrUtils.fString(rs.getString(65)));// PEPPOL_ID
				arrCust.add(65, StrUtils.fString(rs.getString(66)));//SHOWSALESBYPURCHASECOST 
				arrCust.add(66, StrUtils.fString(rs.getString(67)));//CUST_ADDONCOST 
				arrCust.add(67, StrUtils.fString(rs.getString(68)));//ADDONCOSTTYPE

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public ArrayList getCustomerDetailsLoan(String aCustCode, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT LASSIGNNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE, "
					+ "ISNULL(STATE,'') STATE FROM"
					+ "["
					+ plant
					+ "_"
					+ "LOAN_ASSIGNEE_MST"
					+ "]"
					+ " WHERE LASSIGNNO = '"
					+ aCustCode + "' OR CNAME = '"
					+ aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
		
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
				arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
				arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
				arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
				arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
				arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
				arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
				arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
				arrCust.add(17, StrUtils.fString(rs.getString(18)));// address4
				/*arrCust.add(18, StrUtils.fString(rs.getString(19)));// Payment Terms
				arrCust.add(19, StrUtils.fString(rs.getString(20)));// Payment in Days
				arrCust.add(20, StrUtils.fString(rs.getString(21)));// Customr_Type_Id
				arrCust.add(21, StrUtils.fString(rs.getString(22)));// Customr_Status_Id
*/				arrCust.add(18, StrUtils.fString(rs.getString(19)));// State
				//arrCust.add(23, StrUtils.fString(rs.getString(24)));// RCBNO
				

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	
	public ArrayList getCustomerDetailsByIDORHP(String aCustCode, String plant)
	throws Exception {
PreparedStatement ps = null;
ResultSet rs = null;
ArrayList arrCust = new ArrayList();
Connection con = null;
try {
	con = DbBean.getConnection();
	String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE FROM "
			+ "["
			+ plant
			+ "_"
			+ "CUSTMST"
			+ "]"
			+ " WHERE CUSTNO = '"
			+ aCustCode + "'" +" OR "+" HPNO='"+aCustCode+"'";
	this.mLogger.query(this.printQuery, sQry);

	ps = con.prepareStatement(sQry);
	rs = ps.executeQuery();
	while (rs.next()) {
		arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
		arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
		arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
		arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
		arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
		arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
		arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
		arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
		arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
		arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
		arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
		arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
		arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
		arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
		arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
		arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
		arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
		arrCust.add(17, StrUtils.fString(rs.getString(18)));// address4

	}
} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
} finally {
	DbBean.closeConnection(con, ps);
}
return arrCust;
}
	public ArrayList getVendorDetails(String aCustCode, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT VENDNO,VNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE,ISNULL(PAY_TERMS,'') as PAY_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(STATE,'') STATE,ISNULL(RCBNO,'') RCBNO,ISNULL(SUPPLIER_TYPE_ID,'') SUPPLIER_TYPE_ID, ISNULL(CUSTOMEREMAIL,'')CUSTOMEREMAIL,ISNULL(WEBSITE,'')WEBSITE,ISNULL(FACEBOOKID,'')FACEBOOK,ISNULL(TWITTERID,'')TWITTER,ISNULL(LINKEDINID,'')LINKEDIN,ISNULL(SKYPEID,'')SKYPE,ISNULL(OPENINGBALANCE,'')OPENINGBALANCE,ISNULL(WORKPHONE,'')WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER , ISNULL(CURRENCY_ID,'') CURRENCY_ID, ISNULL(TRANSPORTID,'0')TRANSPORTID,ISNULL(PAYMENT_TERMS,'') PAYMENT_TERMS,ISNULL(ISPEPPOL,'0') as ISPEPPOL,ISNULL(PEPPOL_ID,'') as PEPPOL_ID FROM  "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
					+ " WHERE VENDNO = '"
					+ aCustCode + "' OR VNAME = '"
					+ aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons
				arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
				arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
				arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
				arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
				arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
				arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
				arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// iscative
				arrCust.add(17, StrUtils.fString(rs.getString(18)));// PaymentType
				arrCust.add(18, StrUtils.fString(rs.getString(19)));// Payment in Days
				arrCust.add(19, StrUtils.fString(rs.getString(20)));// State
				arrCust.add(20, StrUtils.fString(rs.getString(21)));//RCBNO
				arrCust.add(21, StrUtils.fString(rs.getString(22)));//SUPPLIER TYPE 
				arrCust.add(22, StrUtils.fString(rs.getString(23)));//CUSTOMER EMAIL
				arrCust.add(23, StrUtils.fString(rs.getString(24)));//WEBSITE
				arrCust.add(24, StrUtils.fString(rs.getString(25)));//FACEBOOK
				arrCust.add(25, StrUtils.fString(rs.getString(26)));//TWITTER
				arrCust.add(26, StrUtils.fString(rs.getString(27)));//LINKEDIN
				arrCust.add(27, StrUtils.fString(rs.getString(28)));//SKYPE
				arrCust.add(28, StrUtils.fString(rs.getString(29)));//OPENING BALANCE
				arrCust.add(29, StrUtils.fString(rs.getString(30)));//WORKPHONE
				arrCust.add(30, StrUtils.fString(rs.getString(31)));//TAXTREATMENT
				arrCust.add(31, StrUtils.fString(rs.getString(32)));//COUNTRY_CODE
				arrCust.add(32, StrUtils.fString(rs.getString(33)));//BANKNAME
				arrCust.add(33, StrUtils.fString(rs.getString(34)));//BRANCH
				arrCust.add(34, StrUtils.fString(rs.getString(35)));//IBAN
				arrCust.add(35, StrUtils.fString(rs.getString(36)));//BANKROUTINGCODE
				arrCust.add(36, StrUtils.fString(rs.getString(37)));//companyreg
				arrCust.add(37, StrUtils.fString(rs.getString(38)));//CURRENCY
				arrCust.add(38, StrUtils.fString(rs.getString(39)));//TRANSPORT
				arrCust.add(39, StrUtils.fString(rs.getString(40)));// PaymentTerms
				arrCust.add(40, StrUtils.fString(rs.getString(41)));// peppol
				arrCust.add(41, StrUtils.fString(rs.getString(42)));// peppolid
        
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public ArrayList getVendorDetailsbyName(String aCustCode, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT VENDNO,VNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE,ISNULL(PAY_TERMS,'') as PAY_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(STATE,'') STATE,ISNULL(RCBNO,'') RCBNO,ISNULL(SUPPLIER_TYPE_ID,'') SUPPLIER_TYPE_ID, ISNULL(CUSTOMEREMAIL,'')CUSTOMEREMAIL,ISNULL(WEBSITE,'')WEBSITE,ISNULL(FACEBOOKID,'')FACEBOOK,ISNULL(TWITTERID,'')TWITTER,ISNULL(LINKEDINID,'')LINKEDIN,ISNULL(SKYPEID,'')SKYPE,ISNULL(OPENINGBALANCE,'')OPENINGBALANCE,ISNULL(WORKPHONE,'')WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER , ISNULL(CURRENCY_ID,'') CURRENCY_ID, ISNULL(TRANSPORTID,'0')TRANSPORTID,ISNULL(PAYMENT_TERMS,'') PAYMENT_TERMS,ISNULL(ISPEPPOL,'0') as ISPEPPOL,ISNULL(PEPPOL_ID,'') as PEPPOL_ID FROM  "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
					+ " WHERE VNAME = '"
					+ aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons
				arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
				arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
				arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
				arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
				arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
				arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
				arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// iscative
				arrCust.add(17, StrUtils.fString(rs.getString(18)));// PaymentType
				arrCust.add(18, StrUtils.fString(rs.getString(19)));// Payment in Days
				arrCust.add(19, StrUtils.fString(rs.getString(20)));// State
				arrCust.add(20, StrUtils.fString(rs.getString(21)));//RCBNO
				arrCust.add(21, StrUtils.fString(rs.getString(22)));//SUPPLIER TYPE 
				arrCust.add(22, StrUtils.fString(rs.getString(23)));//CUSTOMER EMAIL
				arrCust.add(23, StrUtils.fString(rs.getString(24)));//WEBSITE
				arrCust.add(24, StrUtils.fString(rs.getString(25)));//FACEBOOK
				arrCust.add(25, StrUtils.fString(rs.getString(26)));//TWITTER
				arrCust.add(26, StrUtils.fString(rs.getString(27)));//LINKEDIN
				arrCust.add(27, StrUtils.fString(rs.getString(28)));//SKYPE
				arrCust.add(28, StrUtils.fString(rs.getString(29)));//OPENING BALANCE
				arrCust.add(29, StrUtils.fString(rs.getString(30)));//WORKPHONE
				arrCust.add(30, StrUtils.fString(rs.getString(31)));//TAXTREATMENT
				arrCust.add(31, StrUtils.fString(rs.getString(32)));//COUNTRY_CODE
				arrCust.add(32, StrUtils.fString(rs.getString(33)));//BANKNAME
				arrCust.add(33, StrUtils.fString(rs.getString(34)));//BRANCH
				arrCust.add(34, StrUtils.fString(rs.getString(35)));//IBAN
				arrCust.add(35, StrUtils.fString(rs.getString(36)));//BANKROUTINGCODE
				arrCust.add(36, StrUtils.fString(rs.getString(37)));//companyreg
				arrCust.add(37, StrUtils.fString(rs.getString(38)));//CURRENCY
				arrCust.add(38, StrUtils.fString(rs.getString(39)));//TRANSPORT
				arrCust.add(39, StrUtils.fString(rs.getString(40)));// PaymentTerms
				arrCust.add(40, StrUtils.fString(rs.getString(41)));// peppol
				arrCust.add(41, StrUtils.fString(rs.getString(42)));// peppolid
				
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}
        
    public ArrayList getVendorDetailsForPO( String plant, String aPono)
                    throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList arrCust = new ArrayList();
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "SELECT V.VENDNO,VNAME,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3," +
                    		"ISNULL(V.COUNTRY,'') COUNTRY,ISNULL(ZIP,'') ZIP,ISNULL(V.USERFLG1,'') USERFLG1,ISNULL(NAME,'') NAME," +
                    		"ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,'')," +
                    		"ISNULL(REMARKS,'') REMARKS,ISNULL(ADDR4,'')ADDR4,ISACTIVE ,ISNULL(V.COMMENT1,'') COMMENT1," +
                    		"ISNULL(P.REMARK1,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTSUPTERMS," +
                    		"ISNULL(P.REMARK3,'') as REMARK3,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS,ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(V.RCBNO,'') RCBNO,ISNULL(V.COMPANYREGNUMBER,'') UENNO, " +
                    		"ISNULL(P.SHIPCONTACTNAME,'') SHIPCONTACTNAME," +
                    		"ISNULL(P.SHIPDESGINATION,'') SHIPDESGINATION," +
                    		"ISNULL(P.SHIPWORKPHONE,'') SHIPWORKPHONE," +
                    		"ISNULL(P.SHIPHPNO,'') SHIPHPNO," +
                    		"ISNULL(P.SHIPEMAIL,'') SHIPEMAIL," +
                    		"ISNULL(P.SHIPADDR1,'') SHIPUNTINO," +
                    		"ISNULL(P.SHIPADDR2,'') SHIPBUILDING," +
                    		"ISNULL(P.SHIPADDR3,'') SHIPSTREET," +
                    		"ISNULL(P.SHIPADDR4,'') SHIPCITY," +
                    		"ISNULL(P.SHIPSTATE,'') SHIPSTATE," +
                    		"ISNULL(P.SHIPCOUNTRY,'') SHIPCOUNTRY," +
                    		"ISNULL(P.SHIPZIP,'') SHIPZIP, " +
                    		"ISNULL(V.PAYMENT_TERMS,'') PAYMENT_TERMS "
                    + "FROM  [" + plant+ "_" + "VENDMST"+ "] V , "+"[" + plant+ "_" + "POHDR"+ "] P"
                     + " WHERE V.PLANT=P.PLANT and  V.VENDNO=P.custCode  and  P.PONO ='"+aPono+"'" ;
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                            arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                            arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                            arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                            arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                            arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                            arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                            arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons
                            arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
                            arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
                            arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
                            arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
                            arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
                            arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
                            arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
                            arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
                            arrCust.add(16, StrUtils.fString(rs.getString(17)));// iscative
                            arrCust.add(17, StrUtils.fString(rs.getString(18)));// comment1  
                            arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                            arrCust.add(19, StrUtils.fString(rs.getString(20)));// Payment terms
                            arrCust.add(20, StrUtils.fString(rs.getString(21)));// Remark3
                            arrCust.add(21, StrUtils.fString(rs.getString(22)));// Payment Days
                            arrCust.add(22, StrUtils.fString(rs.getString(23)));// State
                            arrCust.add(23, StrUtils.fString(rs.getString(24)));// Shipping ID
                            arrCust.add(24, StrUtils.fString(rs.getString(25)));// Supplier RCBno
                            arrCust.add(25, StrUtils.fString(rs.getString(26)));// Supplier UENNO
                            arrCust.add(26, StrUtils.fString(rs.getString(27))); // shipcontactname
    	                    arrCust.add(27, StrUtils.fString(rs.getString(28))); // shiptelephone
    	                    arrCust.add(28, StrUtils.fString(rs.getString(29))); // shiphandphone
    	                    arrCust.add(29, StrUtils.fString(rs.getString(30))); // shipfax
    	                    arrCust.add(30, StrUtils.fString(rs.getString(31))); // shipemail
    	                    arrCust.add(31, StrUtils.fString(rs.getString(32))); // shipunitno
    	                    arrCust.add(32, StrUtils.fString(rs.getString(33)));// shipbuilding
    	                    arrCust.add(33, StrUtils.fString(rs.getString(34)));// shipstreet
    	                    arrCust.add(34, StrUtils.fString(rs.getString(35)));// shipcity
    	                    arrCust.add(35, StrUtils.fString(rs.getString(36)));// shipstate
    	                    arrCust.add(36, StrUtils.fString(rs.getString(37)));//shipcountry
    	                    arrCust.add(37, StrUtils.fString(rs.getString(38)));// shippostalcode
    	                    arrCust.add(38, StrUtils.fString(rs.getString(39)));// PAYMENTTERMS
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return arrCust;
    }
    
    
    public ArrayList getVendorDetailsForIdrno( String plant, String idrno)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
            String sQry = "SELECT V.VENDNO,V.VNAME,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3," +
            		"ISNULL(V.COUNTRY,'') COUNTRY,ISNULL(ZIP,'') ZIP,ISNULL(V.USERFLG1,'') USERFLG1,ISNULL(NAME,'') NAME," +
            		"ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,'')," +
            		"ISNULL(REMARKS,'') REMARKS,ISNULL(ADDR4,'')ADDR4,ISACTIVE ,ISNULL(V.COMMENT1,'') COMMENT1," +
            		"ISNULL('','') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTSUPTERMS," +
            		"ISNULL('','') as REMARK3,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS,ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(V.RCBNO,'') RCBNO,ISNULL(V.COMPANYREGNUMBER,'') UENNO, " +
            		"ISNULL(P.SHIPCONTACTNAME,'') SHIPCONTACTNAME," +
            		"ISNULL(P.SHIPDESGINATION,'') SHIPDESGINATION," +
            		"ISNULL(P.SHIPWORKPHONE,'') SHIPWORKPHONE," +
            		"ISNULL(P.SHIPHPNO,'') SHIPHPNO," +
            		"ISNULL(P.SHIPEMAIL,'') SHIPEMAIL," +
            		"ISNULL(P.SHIPADDR1,'') SHIPUNTINO," +
            		"ISNULL(P.SHIPADDR2,'') SHIPBUILDING," +
            		"ISNULL(P.SHIPADDR3,'') SHIPSTREET," +
            		"ISNULL(P.SHIPADDR4,'') SHIPCITY," +
            		"ISNULL(P.SHIPSTATE,'') SHIPSTATE," +
            		"ISNULL(P.SHIPCOUNTRY,'') SHIPCOUNTRY," +
            		"ISNULL(P.SHIPZIP,'') SHIPZIP, " +
            		"ISNULL(V.PAYMENT_TERMS,'') PAYMENT_TERMS "
            + "FROM  [" + plant+ "_" + "VENDMST"+ "] V , "+"[" + plant+ "_" + "PRODUCTRETURNHDR"+ "] P"
             + " WHERE V.PLANT=P.PLANT and  V.VENDNO=P.VENDNO  and  P.IDRNO ='"+idrno+"'" ;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons
                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
                    arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
                    arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
                    arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
                    arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
                    arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// iscative
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// comment1  
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                    arrCust.add(19, StrUtils.fString(rs.getString(20)));// Payment terms
                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// Remark3
                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// Payment Days
                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// State
                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// Shipping ID
                    arrCust.add(24, StrUtils.fString(rs.getString(25)));// Supplier RCBno
                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// Supplier UENNO
                    arrCust.add(26, StrUtils.fString(rs.getString(27))); // shipcontactname
                    arrCust.add(27, StrUtils.fString(rs.getString(28))); // shiptelephone
                    arrCust.add(28, StrUtils.fString(rs.getString(29))); // shiphandphone
                    arrCust.add(29, StrUtils.fString(rs.getString(30))); // shipfax
                    arrCust.add(30, StrUtils.fString(rs.getString(31))); // shipemail
                    arrCust.add(31, StrUtils.fString(rs.getString(32))); // shipunitno
                    arrCust.add(32, StrUtils.fString(rs.getString(33)));// shipbuilding
                    arrCust.add(33, StrUtils.fString(rs.getString(34)));// shipstreet
                    arrCust.add(34, StrUtils.fString(rs.getString(35)));// shipcity
                    arrCust.add(35, StrUtils.fString(rs.getString(36)));// shipstate
                    arrCust.add(36, StrUtils.fString(rs.getString(37)));//shipcountry
                    arrCust.add(37, StrUtils.fString(rs.getString(38)));// shippostalcode
                    arrCust.add(38, StrUtils.fString(rs.getString(39)));// PAYMENTTERMS
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
}
    
    
    public ArrayList getVendorDetailsForIcrno( String plant, String icrno)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
            String sQry = "SELECT V.VENDNO,V.VNAME,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3," +
            		"ISNULL(V.COUNTRY,'') COUNTRY,ISNULL(ZIP,'') ZIP,ISNULL(V.USERFLG1,'') USERFLG1,ISNULL(NAME,'') NAME," +
            		"ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,'')," +
            		"ISNULL(REMARKS,'') REMARKS,ISNULL(ADDR4,'')ADDR4,ISACTIVE ,ISNULL(V.COMMENT1,'') COMMENT1," +
            		"ISNULL('','') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTSUPTERMS," +
            		"ISNULL('','') as REMARK3,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS,ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(V.RCBNO,'') RCBNO,ISNULL(V.COMPANYREGNUMBER,'') UENNO, " +
            		"ISNULL(P.SHIPCONTACTNAME,'') SHIPCONTACTNAME," +
            		"ISNULL(P.SHIPDESGINATION,'') SHIPDESGINATION," +
            		"ISNULL(P.SHIPWORKPHONE,'') SHIPWORKPHONE," +
            		"ISNULL(P.SHIPHPNO,'') SHIPHPNO," +
            		"ISNULL(P.SHIPEMAIL,'') SHIPEMAIL," +
            		"ISNULL(P.SHIPADDR1,'') SHIPUNTINO," +
            		"ISNULL(P.SHIPADDR2,'') SHIPBUILDING," +
            		"ISNULL(P.SHIPADDR3,'') SHIPSTREET," +
            		"ISNULL(P.SHIPADDR4,'') SHIPCITY," +
            		"ISNULL(P.SHIPSTATE,'') SHIPSTATE," +
            		"ISNULL(P.SHIPCOUNTRY,'') SHIPCOUNTRY," +
            		"ISNULL(P.SHIPZIP,'') SHIPZIP, " +
            		"ISNULL(V.PAYMENT_TERMS,'') PAYMENT_TERMS "
            + "FROM  [" + plant+ "_" + "VENDMST"+ "] V , "+"[" + plant+ "_" + "PRODUCTRECEIVEHDR"+ "] P"
             + " WHERE V.PLANT=P.PLANT and  V.VENDNO=P.VENDNO  and  P.ICRNO ='"+icrno+"'" ;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons
                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
                    arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
                    arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
                    arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
                    arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
                    arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// iscative
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// comment1  
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                    arrCust.add(19, StrUtils.fString(rs.getString(20)));// Payment terms
                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// Remark3
                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// Payment Days
                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// State
                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// Shipping ID
                    arrCust.add(24, StrUtils.fString(rs.getString(25)));// Supplier RCBno
                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// Supplier UENNO
                    arrCust.add(26, StrUtils.fString(rs.getString(27))); // shipcontactname
                    arrCust.add(27, StrUtils.fString(rs.getString(28))); // shiptelephone
                    arrCust.add(28, StrUtils.fString(rs.getString(29))); // shiphandphone
                    arrCust.add(29, StrUtils.fString(rs.getString(30))); // shipfax
                    arrCust.add(30, StrUtils.fString(rs.getString(31))); // shipemail
                    arrCust.add(31, StrUtils.fString(rs.getString(32))); // shipunitno
                    arrCust.add(32, StrUtils.fString(rs.getString(33)));// shipbuilding
                    arrCust.add(33, StrUtils.fString(rs.getString(34)));// shipstreet
                    arrCust.add(34, StrUtils.fString(rs.getString(35)));// shipcity
                    arrCust.add(35, StrUtils.fString(rs.getString(36)));// shipstate
                    arrCust.add(36, StrUtils.fString(rs.getString(37)));//shipcountry
                    arrCust.add(37, StrUtils.fString(rs.getString(38)));// shippostalcode
                    arrCust.add(38, StrUtils.fString(rs.getString(39)));// PAYMENTTERMS
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
}
    
    
    
//    Resvi Include This code Purchaseestimate Supplier Details
    public ArrayList getVendorDetailsForPOEST( String plant, String aPOESTNO)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
            String sQry = "SELECT V.VENDNO,VNAME,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3," +
            		"ISNULL(V.COUNTRY,'') COUNTRY,ISNULL(ZIP,'') ZIP,ISNULL(V.USERFLG1,'') USERFLG1,ISNULL(NAME,'') NAME," +
            		"ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,'')," +
            		"ISNULL(REMARKS,'') REMARKS,ISNULL(ADDR4,'')ADDR4,ISACTIVE ,ISNULL(V.COMMENT1,'') COMMENT1," +
            		"ISNULL(P.REMARK1,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTSUPTERMS," +
            		"ISNULL(P.REMARK3,'') as REMARK3,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS,ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(V.RCBNO,'') RCBNO,ISNULL(V.COMPANYREGNUMBER,'') UENNO FROM  "
            + "[" + plant+ "_" + "VENDMST"+ "] V , "+"[" + plant+ "_" + "POESTHDR"+ "] P"
             + " WHERE V.PLANT=P.PLANT and  V.VENDNO=P.custCode  and  P.POESTNO ='"+aPOESTNO+"'" ;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons
                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
                    arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
                    arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
                    arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
                    arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
                    arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// iscative
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// comment1  
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                    arrCust.add(19, StrUtils.fString(rs.getString(20)));// Payment terms
                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// Remark3
                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// Payment Days
                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// State
                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// Shipping ID
                    arrCust.add(24, StrUtils.fString(rs.getString(25)));// Supplier RCBno
                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// Supplier UENNO
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
}
    
//    Ends
	/*
	 * //Resvi public ArrayList getVendorDetailsForPOMULTIEST( String plant, String
	 * aPOMULTIESTNO) throws Exception { PreparedStatement ps = null; ResultSet rs =
	 * null; ArrayList arrCust = new ArrayList(); Connection con = null; try { con =
	 * DbBean.getConnection(); String sQry =
	 * "SELECT V.VENDNO,VNAME,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,"
	 * +
	 * "ISNULL(V.COUNTRY,'') COUNTRY,ISNULL(ZIP,'') ZIP,ISNULL(V.USERFLG1,'') USERFLG1,ISNULL(NAME,'') NAME,"
	 * +
	 * "ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),"
	 * +
	 * "ISNULL(REMARKS,'') REMARKS,ISNULL(ADDR4,'')ADDR4,ISACTIVE ,ISNULL(V.COMMENT1,'') COMMENT1,"
	 * + "ISNULL(P.REMARK1,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTSUPTERMS,"
	 * +
	 * "ISNULL(P.REMARK3,'') as REMARK3,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS,ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(V.RCBNO,'') RCBNO,ISNULL(V.COMPANYREGNUMBER,'') UENNO FROM  "
	 * + "[" + plant+ "_" + "VENDMST"+ "] V , "+"[" + plant+ "_" +
	 * "PO_MULTI_ESTHDR"+ "] P" +
	 * " WHERE V.PLANT=P.PLANT and   P.POMULTIESTNO ='"+aPOMULTIESTNO+"'" ;
	 * this.mLogger.query(this.printQuery, sQry); ps = con.prepareStatement(sQry);
	 * rs = ps.executeQuery(); while (rs.next()) { arrCust.add(0,
	 * StrUtils.fString(rs.getString(1))); // custno arrCust.add(1,
	 * StrUtils.fString(rs.getString(2))); // cname arrCust.add(2,
	 * StrUtils.fString(rs.getString(3))); // Address1 arrCust.add(3,
	 * StrUtils.fString(rs.getString(4))); // Address2 arrCust.add(4,
	 * StrUtils.fString(rs.getString(5))); // Address3 arrCust.add(5,
	 * StrUtils.fString(rs.getString(6))); // country arrCust.add(6,
	 * StrUtils.fString(rs.getString(7))); // zip arrCust.add(7,
	 * StrUtils.fString(rs.getString(8))); // cons arrCust.add(8,
	 * StrUtils.fString(rs.getString(9))); // contactname arrCust.add(9,
	 * StrUtils.fString(rs.getString(10))); // desg arrCust.add(10,
	 * StrUtils.fString(rs.getString(11))); // telno arrCust.add(11,
	 * StrUtils.fString(rs.getString(12))); // hpno arrCust.add(12,
	 * StrUtils.fString(rs.getString(13))); // email arrCust.add(13,
	 * StrUtils.fString(rs.getString(14))); // fax arrCust.add(14,
	 * StrUtils.fString(rs.getString(15))); // remarks arrCust.add(15,
	 * StrUtils.fString(rs.getString(16)));// addr4 arrCust.add(16,
	 * StrUtils.fString(rs.getString(17)));// iscative arrCust.add(17,
	 * StrUtils.fString(rs.getString(18)));// comment1 arrCust.add(18,
	 * StrUtils.fString(rs.getString(19)));// order Remarks arrCust.add(19,
	 * StrUtils.fString(rs.getString(20)));// Payment terms arrCust.add(20,
	 * StrUtils.fString(rs.getString(21)));// Remark3 arrCust.add(21,
	 * StrUtils.fString(rs.getString(22)));// Payment Days arrCust.add(22,
	 * StrUtils.fString(rs.getString(23)));// State arrCust.add(23,
	 * StrUtils.fString(rs.getString(24)));// Shipping ID arrCust.add(24,
	 * StrUtils.fString(rs.getString(25)));// Supplier RCBno arrCust.add(25,
	 * StrUtils.fString(rs.getString(26)));// Supplier UENNO } } catch (Exception e)
	 * { this.mLogger.exception(this.printLog, "", e); } finally {
	 * DbBean.closeConnection(con, ps); } return arrCust; }
	 * 
	 * //Ends
	 */    
    
    public ArrayList getVendorDetailsForBILL( String plant, String bill)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
            String sQry = "SELECT V.VENDNO,VNAME,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3," +
            		"ISNULL(V.COUNTRY,'') COUNTRY,ISNULL(ZIP,'') ZIP,ISNULL(V.USERFLG1,'') USERFLG1,ISNULL(NAME,'') NAME," +
            		"ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,'')," +
            		"ISNULL(REMARKS,'') REMARKS,ISNULL(ADDR4,'')ADDR4,ISACTIVE ,ISNULL(V.COMMENT1,'') COMMENT1," +
            		"ISNULL(P.NOTE,'') as NOTE,ISNULL(PAY_TERMS,'')   as PRINTSUPTERMS," +
            		"ISNULL(P.TAX_STATUS,'') as TAXSTATUS,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS,ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(V.RCBNO,'') RCBNO,ISNULL(V.COMPANYREGNUMBER,'') UENNO,ISNULL(P.BILL_STATUS,'') as BILLSTATUS FROM  "
            + "[" + plant+ "_" + "VENDMST"+ "] V , "+"[" + plant+ "_" + "FINBILLHDR"+ "] P"
             + " WHERE V.PLANT=P.PLANT and  V.VENDNO=P.VENDNO  and  P.BILL ='"+bill+"'" ;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons
                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
                    arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
                    arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
                    arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
                    arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
                    arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// iscative
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// comment1  
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// note
                    arrCust.add(19, StrUtils.fString(rs.getString(20)));// Payment terms
                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// tax status
                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// Payment Days
                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// State
                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// Shipping ID
                    arrCust.add(24, StrUtils.fString(rs.getString(25)));// Supplier RCBno
                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// Supplier UENNO
                    arrCust.add(26, StrUtils.fString(rs.getString(27)));// bill status
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
}

	public ArrayList getVendorByName(String custname, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT VENDNO,VNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4 FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
					+ " WHERE VNAME like '" + custname + "%'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons
				arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
				arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
				arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
				arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
				arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
				arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
				arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// currency
				
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public boolean updateCustomer(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			// generate the update string
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
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ tblName + "] " + sUpdate + sCondition;
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

	public boolean updateVendor(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			// generate the update string
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
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ "Vendmst" + "] " + sUpdate + sCondition;
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

	public ArrayList getOutGoingCustomerDetails(String aPlant,
			String aCustName, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
				String sQry = "SELECT CUSTNO,CNAME,LNAME,ISNULL(NAME,''),ISNULL(TELNO,''),ISNULL(EMAIL,''),ISNULL(ADDR1,''),ISNULL(ADDR2,''),ISNULL(ADDR3,''),ISNULL(ADDR4,''),ISNULL(COUNTRY,''),ISNULL(REMARKS,''),ISNULL(ZIP,''), "
					+ "ISNULL(HPNO,'') , ISNULL(CUSTOMER_STATUS_ID,'')CUSTOMERSTATUSID,ISNULL(CUSTOMER_TYPE_ID,'')CUSTOMERTYPEID,ISNULL(PAY_TERMS,'') PAYMENT_TERMS,ISNULL(TAXTREATMENT,'') TAXTREATMENT FROM"
		
					+ "["
					+ aPlant
					+ "_"
					+ "CUSTMST"
					+ "]"
					//+ "   WHERE CNAME LIKE '" + aCustName + "%'" + cond
					+ "   WHERE (CNAME LIKE '" + aCustName + "%' or CUSTNO LIKE '%" + aCustName + "%')" + cond
					+ " ORDER BY CUSTNO,CNAME ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrLine.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrLine.add(2, StrUtils.fString(rs.getString(3))); // cname
				arrLine.add(3, StrUtils.fString(rs.getString(4))); // contactname
				arrLine.add(4, StrUtils.fString(rs.getString(5))); // Telno
				arrLine.add(5, StrUtils.fString(rs.getString(6))); // Email
				arrLine.add(6, StrUtils.fString(rs.getString(7))); // Add1
				arrLine.add(7, StrUtils.fString(rs.getString(8))); // Add2
				arrLine.add(8, StrUtils.fString(rs.getString(9))); // Add3
				arrLine.add(9, StrUtils.fString(rs.getString(10))); // Add4
				arrLine.add(10, StrUtils.fString(rs.getString(11))); // Country
				arrLine.add(11, StrUtils.fString(rs.getString(12))); // Remarks
				arrLine.add(12, StrUtils.fString(rs.getString(13))); // Zip
				arrLine.add(13, StrUtils.fString(rs.getString(14))); // HPNO
				arrLine.add(14, StrUtils.fString(rs.getString(15))); // CustomerStatusId
				arrLine.add(15, StrUtils.fString(rs.getString(16))); // CustomerTypeId
				arrLine.add(16, StrUtils.fString(rs.getString(17))); // PaymentType
				arrLine.add(17, StrUtils.fString(rs.getString(18))); // Tax Treatment

				arrList.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getCustomerListStartsWithName(String aCustName)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,LNAME CUSTMST   WHERE CNAME LIKE '"
					+ aCustName + "%' ORDER BY CNAME ASC";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrLine.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrLine.add(2, StrUtils.fString(rs.getString(3))); // cname

				arrList.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getCustomerListStartsWithName(String aCustName,
			String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		try {
			con = DbBean.getConnection();
		/*	String sQry = "SELECT CUSTNO,CNAME,LNAME,NAME,TELNO,DESGINATION,FAX,EMAIL,HPNO,ADDR1,ADDR2,ADDR3,ADDR4,COUNTRY,ZIP,REMARKS,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ " WHERE CNAME LIKE '" + aCustName + "%' ORDER BY CNAME ASC ";*/
			
			String sQry = "SELECT CUSTNO,ISNULL(BANKNAME,'') BANKNAME,CNAME,ISNULL(LNAME,'')LNAME,ISNULL(NAME,'') NAME,ISNULL(STATE,'') AS STATE,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,"
					+ "ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(ISPEPPOL,0) ISPEPPOL,"
					+ "ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(SHOWSALESBYPURCHASECOST,0) SHOWSALESBYPURCHASECOST,"
					+ "ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAY_TERMS,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(CUSTOMER_TYPE_ID,'') CUSTOMER_TYPE_ID ,"
					+ "ISNULL(SHIPCONTACTNAME,'')SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'')SHIPDESGINATION,ISNULL(SHIPADDR1,'')SHIPADDR1,ISNULL(PAY_TERMS,'')PAY_TERMS,"
					+ "ISNULL(SHIPADDR2,'')SHIPADDR2,ISNULL(SHIPADDR3,'')SHIPADDR3,ISNULL(SHIPADDR4,'')SHIPADDR4,ISNULL(TRANSPORTID,0)TRANSPORTID,ISNULL(T.TRANSPORT_MODE,'') AS TRANSPORT_MODE,"
					+ "ISNULL(SHIPHPNO,'')SHIPHPNO,ISNULL(SHIPEMAIL,'')SHIPEMAIL,ISNULL((SELECT TOP 1 COUNTRY_CODE FROM COUNTRYMASTER as C WHERE C.COUNTRYNAME = SHIPCOUNTRY_CODE),'') AS SHIPCOUNTRY_CODE,ISNULL(SHIPCOUNTRY_CODE,'')AS SHIPCOUNTRY,"
					+ "ISNULL(SHIPSTATE,'')SHIPSTATE,ISNULL(SHIPZIP,'')SHIPZIP,ISNULL(SHIPWORKPHONE,'')SHIPWORKPHONE,ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(PAYMENT_TERMS,'') PAYMENT_TERMS,ISNULL(DATEOFBIRTH,'') as DATEOFBIRTH,ISNULL(NATIONALITY,'') as NATIONALITY FROM "
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]  left join "+plant+"_TRANSPORT_MODE_MST as T on T.ID = TRANSPORTID "
					+ " WHERE CNAME LIKE '" + aCustName + "%' or CUSTNO LIKE '%" + aCustName + "%' ORDER BY CNAME ASC ";
			
			this.mLogger.query(this.printQuery, sQry);
			arrList = selectForReport(sQry, ht, "");
		/*	ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrLine.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrLine.add(2, StrUtils.fString(rs.getString(3))); // cname
				arrLine.add(3, StrUtils.fString(rs.getString(4))); // contact
				// name
				arrLine.add(4, StrUtils.fString(rs.getString(5))); // telno
				arrLine.add(5, StrUtils.fString(rs.getString(6))); // designa
				arrLine.add(6, StrUtils.fString(rs.getString(7))); // fax
				arrLine.add(7, StrUtils.fString(rs.getString(8))); // email
				arrLine.add(8, StrUtils.fString(rs.getString(9))); // hpno
				arrLine.add(9, StrUtils.fString(rs.getString(10))); // addr1
				arrLine.add(10, StrUtils.fString(rs.getString(11))); // addr2
				arrLine.add(11, StrUtils.fString(rs.getString(12))); // addr3
				arrLine.add(12, StrUtils.fString(rs.getString(13))); // addr4
				arrLine.add(13, StrUtils.fString(rs.getString(14))); // cnty
				arrLine.add(14, StrUtils.fString(rs.getString(15))); // zip
				arrLine.add(15, StrUtils.fString(rs.getString(16))); // remarks
				arrLine.add(16, StrUtils.fString(rs.getString(17))); // isactive
				arrList.add(arrLine);
			}*/
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
	public ArrayList getCustomerListStartsWithNameActive(String aCustName,
			String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		try {
			con = DbBean.getConnection();
		/*	String sQry = "SELECT CUSTNO,CNAME,LNAME,NAME,TELNO,DESGINATION,FAX,EMAIL,HPNO,ADDR1,ADDR2,ADDR3,ADDR4,COUNTRY,ZIP,REMARKS,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ " WHERE CNAME LIKE '" + aCustName + "%' ORDER BY CNAME ASC ";*/
			
			String sQry = "SELECT CUSTNO,ISNULL(BANKNAME,'') BANKNAME,CNAME,ISNULL(LNAME,'')LNAME,ISNULL(NAME,'') NAME,ISNULL(STATE,'') AS STATE,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,"
					+ "ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(ISPEPPOL,0) ISPEPPOL,"
					+ "ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(SHOWSALESBYPURCHASECOST,0) SHOWSALESBYPURCHASECOST,"
					+ "ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAY_TERMS,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(CUSTOMER_TYPE_ID,'') CUSTOMER_TYPE_ID ,"
					+ "ISNULL(SHIPCONTACTNAME,'')SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'')SHIPDESGINATION,ISNULL(SHIPADDR1,'')SHIPADDR1,ISNULL(PAY_TERMS,'')PAY_TERMS,"
					+ "ISNULL(SHIPADDR2,'')SHIPADDR2,ISNULL(SHIPADDR3,'')SHIPADDR3,ISNULL(SHIPADDR4,'')SHIPADDR4,ISNULL(TRANSPORTID,0)TRANSPORTID,ISNULL(T.TRANSPORT_MODE,'') AS TRANSPORT_MODE,"
					+ "ISNULL(SHIPHPNO,'')SHIPHPNO,ISNULL(SHIPEMAIL,'')SHIPEMAIL,ISNULL((SELECT TOP 1 COUNTRY_CODE FROM COUNTRYMASTER as C WHERE C.COUNTRYNAME = SHIPCOUNTRY_CODE),'') AS SHIPCOUNTRY_CODE,ISNULL(SHIPCOUNTRY_CODE,'')AS SHIPCOUNTRY,"
					+ "ISNULL(SHIPSTATE,'')SHIPSTATE,ISNULL(SHIPZIP,'')SHIPZIP,ISNULL(SHIPWORKPHONE,'')SHIPWORKPHONE,ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(PAYMENT_TERMS,'') PAYMENT_TERMS,ISNULL(DATEOFBIRTH,'') as DATEOFBIRTH,ISNULL(NATIONALITY,'') as NATIONALITY,ISNULL(CUST_ADDONCOST ,0)as CUST_ADDONCOST,ISNULL(ADDONCOSTTYPE ,0)as ADDONCOSTTYPE   FROM "
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]  left join "+plant+"_TRANSPORT_MODE_MST as T on T.ID = TRANSPORTID "
					+ " WHERE IsActive='Y' AND (CNAME LIKE '" + aCustName + "%' or CUSTNO LIKE '%" + aCustName + "%') ORDER BY CNAME ASC ";
			
			this.mLogger.query(this.printQuery, sQry);
			arrList = selectForReport(sQry, ht, "");
		/*	ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrLine.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrLine.add(2, StrUtils.fString(rs.getString(3))); // cname
				arrLine.add(3, StrUtils.fString(rs.getString(4))); // contact
				// name
				arrLine.add(4, StrUtils.fString(rs.getString(5))); // telno
				arrLine.add(5, StrUtils.fString(rs.getString(6))); // designa
				arrLine.add(6, StrUtils.fString(rs.getString(7))); // fax
				arrLine.add(7, StrUtils.fString(rs.getString(8))); // email
				arrLine.add(8, StrUtils.fString(rs.getString(9))); // hpno
				arrLine.add(9, StrUtils.fString(rs.getString(10))); // addr1
				arrLine.add(10, StrUtils.fString(rs.getString(11))); // addr2
				arrLine.add(11, StrUtils.fString(rs.getString(12))); // addr3
				arrLine.add(12, StrUtils.fString(rs.getString(13))); // addr4
				arrLine.add(13, StrUtils.fString(rs.getString(14))); // cnty
				arrLine.add(14, StrUtils.fString(rs.getString(15))); // zip
				arrLine.add(15, StrUtils.fString(rs.getString(16))); // remarks
				arrLine.add(16, StrUtils.fString(rs.getString(17))); // isactive
				arrList.add(arrLine);
			}*/
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
	public ArrayList getCustomerListStartsWithNamePeppol(String aCustName,
			String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		try {
			con = DbBean.getConnection();
		/*	String sQry = "SELECT CUSTNO,CNAME,LNAME,NAME,TELNO,DESGINATION,FAX,EMAIL,HPNO,ADDR1,ADDR2,ADDR3,ADDR4,COUNTRY,ZIP,REMARKS,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ " WHERE CNAME LIKE '" + aCustName + "%' ORDER BY CNAME ASC ";*/
			
			String sQry = "SELECT CUSTNO,ISNULL(BANKNAME,'') BANKNAME,CNAME,ISNULL(LNAME,'')LNAME,ISNULL(NAME,'') NAME,ISNULL(STATE,'') AS STATE,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,"
					+ "ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(ISPEPPOL,0) ISPEPPOL,"
					+ "ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,"
					+ "ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAY_TERMS,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(CUSTOMER_TYPE_ID,'') CUSTOMER_TYPE_ID ,"
					+ "ISNULL(SHIPCONTACTNAME,'')SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'')SHIPDESGINATION,ISNULL(SHIPADDR1,'')SHIPADDR1,ISNULL(PAY_TERMS,'')PAY_TERMS,"
					+ "ISNULL(SHIPADDR2,'')SHIPADDR2,ISNULL(SHIPADDR3,'')SHIPADDR3,ISNULL(SHIPADDR4,'')SHIPADDR4,ISNULL(TRANSPORTID,0)TRANSPORTID,ISNULL(T.TRANSPORT_MODE,'') AS TRANSPORT_MODE,"
					+ "ISNULL(SHIPHPNO,'')SHIPHPNO,ISNULL(SHIPEMAIL,'')SHIPEMAIL,ISNULL((SELECT TOP 1 COUNTRY_CODE FROM COUNTRYMASTER as C WHERE C.COUNTRYNAME = SHIPCOUNTRY_CODE),'') AS SHIPCOUNTRY_CODE,ISNULL(SHIPCOUNTRY_CODE,'')AS SHIPCOUNTRY,"
					+ "ISNULL(SHIPSTATE,'')SHIPSTATE,ISNULL(SHIPZIP,'')SHIPZIP,ISNULL(SHIPWORKPHONE,'')SHIPWORKPHONE,ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(PAYMENT_TERMS,'') PAYMENT_TERMS,ISNULL(DATEOFBIRTH,'') as DATEOFBIRTH,ISNULL(NATIONALITY,'') as NATIONALITY FROM "
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]  left join "+plant+"_TRANSPORT_MODE_MST as T on T.ID = TRANSPORTID "
					+ " WHERE ISNULL(ISPEPPOL,0) = 1 AND (CNAME LIKE '" + aCustName + "%' or CUSTNO LIKE '%" + aCustName + "%') ORDER BY CNAME ASC ";
			
			this.mLogger.query(this.printQuery, sQry);
			arrList = selectForReport(sQry, ht, "");
		/*	ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrLine.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrLine.add(2, StrUtils.fString(rs.getString(3))); // cname
				arrLine.add(3, StrUtils.fString(rs.getString(4))); // contact
				// name
				arrLine.add(4, StrUtils.fString(rs.getString(5))); // telno
				arrLine.add(5, StrUtils.fString(rs.getString(6))); // designa
				arrLine.add(6, StrUtils.fString(rs.getString(7))); // fax
				arrLine.add(7, StrUtils.fString(rs.getString(8))); // email
				arrLine.add(8, StrUtils.fString(rs.getString(9))); // hpno
				arrLine.add(9, StrUtils.fString(rs.getString(10))); // addr1
				arrLine.add(10, StrUtils.fString(rs.getString(11))); // addr2
				arrLine.add(11, StrUtils.fString(rs.getString(12))); // addr3
				arrLine.add(12, StrUtils.fString(rs.getString(13))); // addr4
				arrLine.add(13, StrUtils.fString(rs.getString(14))); // cnty
				arrLine.add(14, StrUtils.fString(rs.getString(15))); // zip
				arrLine.add(15, StrUtils.fString(rs.getString(16))); // remarks
				arrLine.add(16, StrUtils.fString(rs.getString(17))); // isactive
				arrList.add(arrLine);
			}*/
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

		public ArrayList getVendorListWithName(String aCustName,
			String plant, String aCustType) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		String extCond="";
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT VENDNO,VNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(STATE,'')STATE,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(RCBNO,'') AS RCBNO,ISNULL(SUPPLIER_TYPE_ID,'')  as SUPPLIER_TYPE_ID,ISNULL(TRANSPORTID,'0')  as TRANSPORTID FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
				//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
		//	+ " WHERE VNAME LIKE '" + aCustName + "%' "//or VENDNO LIKE '%" + aCustName + "%'" ; + cond;
			+ " WHERE VNAME LIKE '" + aCustName + "%' ";
			
			this.mLogger.query(this.printQuery, sQry);
			if(aCustType.length()>0)
			{
				extCond=" AND SUPPLIER_TYPE_ID = '" + aCustType + "' ORDER BY VNAME ASC ";
			}
			else
			{
				extCond=" ORDER BY VNAME ASC ";
			}
			arrList = selectForReport(sQry, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
		public ArrayList getVendorListssWithNameActive(String aCustName,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;		
			try {
				con = DbBean.getConnection();

						String sQry = "SELECT VENDNO,VNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(STATE,'')STATE,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(RCBNO,'') AS RCBNO,ISNULL(SUPPLIER_TYPE_ID,'') AS SUPPLIER_TYPE_ID,ISNULL(TRANSPORTID,'0') AS TRANSPORTID ,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(FACEBOOKID,'') AS FACEBOOKID,ISNULL(TWITTERID,'') AS TWITTERID,ISNULL(LINKEDINID,'') AS LINKEDINID,ISNULL(SKYPEID,'') AS SKYPEID,ISNULL(OPENINGBALANCE,'') AS OPENINGBALANCE,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER , ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(PAYMENT_TERMS,'') PAYMENTTERMS FROM "
						+ "["
						+ plant
						+ "_"
						+ "VENDMST"
						+ "]"
					//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
				+ " WHERE IsActive ='Y' and (VNAME LIKE '" + aCustName + "%' or VENDNO LIKE '%" + aCustName + "%')"  + cond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;

		}

		public ArrayList getVendorTypeListStartsWithName(String aCustName,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;		
			try {
				con = DbBean.getConnection();

						String sQry = "SELECT SUPPLIER_TYPE_ID,SUPPLIER_TYPE_DESC FROM "
						+ "["
						+ plant
						+ "_"
						+ "SUPPLIER_TYPE_MST"
						+ "]"
					//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
				+ " WHERE SUPPLIER_TYPE_DESC LIKE '" + aCustName + "%' or SUPPLIER_TYPE_ID LIKE '%" + aCustName + "%'"  + cond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;

		}
	public ArrayList getVendorListStartsWithName(String aCustName,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;	
		String ext="";
		if(cond.length()>0)
		{
		ext ="AND SUPPLIER_TYPE_ID = '" + cond + "' ";
		}
		else {
		ext="ORDER BY VNAME ASC";
		}
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT VENDNO,VNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(STATE,'')STATE,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(RCBNO,'') AS RCBNO,ISNULL(SUPPLIER_TYPE_ID,'') AS SUPPLIER_TYPE_ID,ISNULL(TRANSPORTID,'0') AS TRANSPORTID ,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(FACEBOOKID,'') AS FACEBOOKID,ISNULL(TWITTERID,'') AS TWITTERID,ISNULL(LINKEDINID,'') AS LINKEDINID,ISNULL(SKYPEID,'') AS SKYPEID,ISNULL(OPENINGBALANCE,'') AS OPENINGBALANCE,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER ,ISNULL(CURRENCY_ID,'') CURRENCY_ID, ISNULL(TRANSPORTID,'0')TRANSPORTID  FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
				//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
//			+ " WHERE (VNAME LIKE '" + aCustName + "%' or VENDNO LIKE '%" + aCustName + "%')"  + ext;
					+ " WHERE VNAME LIKE '" + aCustName + "%' or VENDNO LIKE '%" + aCustName + "%' ORDER BY VNAME ASC ";
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry); 
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
	public ArrayList getVendorListssWithName(String aCustName,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT VENDNO,VNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(STATE,'')STATE,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(RCBNO,'') AS RCBNO,ISNULL(SUPPLIER_TYPE_ID,'') AS SUPPLIER_TYPE_ID,ISNULL(TRANSPORTID,'0') AS TRANSPORTID ,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(FACEBOOKID,'') AS FACEBOOKID,ISNULL(TWITTERID,'') AS TWITTERID,ISNULL(LINKEDINID,'') AS LINKEDINID,ISNULL(SKYPEID,'') AS SKYPEID,ISNULL(OPENINGBALANCE,'') AS OPENINGBALANCE,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER , ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(PAYMENT_TERMS,'') PAYMENTTERMS FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
				//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
			+ " WHERE (VNAME LIKE '" + aCustName + "%' or VENDNO LIKE '%" + aCustName + "%')"  + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
	public ArrayList getVendorListssWithNamePeppol(String aCustName,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT VENDNO,VNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(STATE,'')STATE,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(RCBNO,'') AS RCBNO,ISNULL(SUPPLIER_TYPE_ID,'') AS SUPPLIER_TYPE_ID,ISNULL(TRANSPORTID,'0') AS TRANSPORTID ,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(FACEBOOKID,'') AS FACEBOOKID,ISNULL(TWITTERID,'') AS TWITTERID,ISNULL(LINKEDINID,'') AS LINKEDINID,ISNULL(SKYPEID,'') AS SKYPEID,ISNULL(OPENINGBALANCE,'') AS OPENINGBALANCE,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER , ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(PAYMENT_TERMS,'') PAYMENTTERMS FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
				//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
			+ " WHERE ISNULL(ISPEPPOL,0) = 1 AND (VNAME LIKE '" + aCustName + "%' or VENDNO LIKE '%" + aCustName + "%')"  + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getPeppolVendorListssWithName(String aCustName,
			String plant,String SUPPLIERTYPE, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();
			
			String sQry = "SELECT VENDNO,VNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(STATE,'')STATE,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(PAY_TERMS,'') PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(RCBNO,'') AS RCBNO,ISNULL(SUPPLIER_TYPE_ID,'') AS SUPPLIER_TYPE_ID,ISNULL(TRANSPORTID,'0') AS TRANSPORTID ,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(FACEBOOKID,'') AS FACEBOOKID,ISNULL(TWITTERID,'') AS TWITTERID,ISNULL(LINKEDINID,'') AS LINKEDINID,ISNULL(SKYPEID,'') AS SKYPEID,ISNULL(OPENINGBALANCE,'') AS OPENINGBALANCE,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER , ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(PAYMENT_TERMS,'') PAYMENTTERMS,ISNULL(PEPPOL_ID,'') as PEPPOL_ID FROM "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST"
					+ "]"
					+ " WHERE ISPEPPOL=1 AND (VNAME LIKE '" + aCustName + "%' or VENDNO LIKE '%" + aCustName + "%')"  + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
		
	}

	public boolean isExistsLoanAssignee(String assignee, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "LOAN_ASSIGNEE_MST" + "]" + " WHERE "
					+ IConstants.LOAN_ASSIGNEE_CODE + " = '"
					+ assignee.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
        
    public boolean isExistsLoanAssigneeName(String loanassigneeName, String plant)
                    throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean isExists = false;
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
                                    + "LOAN_ASSIGNEE_MST" + "]" + " WHERE CNAME "
                                    + " = '"
                                    + loanassigneeName.toUpperCase() + "'";
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            if (rs.getInt(1) > 0)
                                    isExists = true;
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return isExists;
    }


	public boolean updateLoanAssignee(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			// generate the update string
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
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ "LOAN_ASSIGNEE_MST" + "] " + sUpdate + sCondition;
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

	public boolean deleteLoanAssignee(String aConsignee, String plant)
			throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_"
					+ "LOAN_ASSIGNEE_MST" + "]" + " WHERE "
					+ IConstants.LOAN_ASSIGNEE_CODE + "='"
					+ aConsignee.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}

	// created for LoanAssignee
	public boolean insertIntoLoanAssignee(Hashtable ht) throws Exception {
		boolean insertedCust = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value + "',";
			}
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "LOAN_ASSIGNEE_MST" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedCust;
	}

	public ArrayList getLoanAssigneeDetails(String aCustCode, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT LASSIGNNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE,ISNULL(STATE,'') STATE FROM "
					+ "["
					+ plant
					+ "_"
					+ "LOAN_ASSIGNEE_MST"
					+ "]"
					+ " WHERE LASSIGNNO = '" + aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons

				arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
				arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
				arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
				arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
				arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
				arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
				arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// isactive
				arrCust.add(17, StrUtils.fString(rs.getString(18)));// State
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public ArrayList getLoanAssigneeListStartsWithName(String aCustName,
			String aCustCode, String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		try {
			con = DbBean.getConnection();

			
			String sQry = "SELECT LASSIGNNO,CNAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(STATE,'')AS STATE,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "LOAN_ASSIGNEE_MST"
					+ "]"
					//+ " WHERE CNAME LIKE '" + aCustName + "%' ORDER BY LASSIGNNO,CNAME ";
					+ " WHERE CNAME LIKE '" + aCustName + "%' or LASSIGNNO LIKE '%" + aCustName + "%' ORDER BY LASSIGNNO,CNAME ";
				
			arrList = selectForReport(sQry, ht, "");
			
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getToAssingeListStartsWithName(String aCustName,
			String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			
			String sQry = "SELECT ASSIGNENO,ASSIGNENAME,ISNULL(NAME,'') NAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(STATE,'') AS STATE,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "TO_ASSIGNEE_MASTER"
					+ "]"
					+ " WHERE ASSIGNENAME LIKE '" + aCustName + "%' ORDER BY ASSIGNENO,ASSIGNENAME ";
			arrList = selectForReport(sQry, ht, "");
			
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public boolean isExistsToAssignee(String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "TO_ASSIGNEE_MASTER" + "]" + " WHERE ASSIGNENO" + " = '"
					+ aCustomer.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}

    public boolean isExistsToAssigneeName(String atoAssigneeName, String plant)
                    throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean isExists = false;
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
                                    + "TO_ASSIGNEE_MASTER" + "]" + " WHERE ASSIGNENAME" + " = '"
                                    + atoAssigneeName.toUpperCase() + "'";
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            if (rs.getInt(1) > 0)
                                    isExists = true;
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return isExists;
    }

	public ArrayList getOutGoingToAssigneeDetails(String aPlant,
			String aCustName, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ASSIGNENO,ASSIGNENAME,ISNULL(NAME,''),ISNULL(TELNO,''),ISNULL(EMAIL,''),ISNULL(ADDR1,''),ISNULL(ADDR2,''),ISNULL(ADDR3,''),ISNULL(ADDR4,''),ISNULL(COUNTRY,''),ISNULL(REMARKS,''),ISNULL(ZIP,'') FROM "
					+ "["
					+ aPlant
					+ "_"
					+ "TO_ASSIGNEE_MASTER"
					+ "]"
					//+ "   WHERE ASSIGNENAME LIKE '" + aCustName + "%'" + cond
					+ "   WHERE (ASSIGNENAME LIKE '" + aCustName + "%' OR ASSIGNENO LIKE '%" + aCustName + "%' ) " + cond
					+ " ORDER BY ASSIGNENO,ASSIGNENAME ";

			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString(1))); // custno

				arrLine.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrLine.add(2, StrUtils.fString(rs.getString(3))); // cname
				arrLine.add(3, StrUtils.fString(rs.getString(4))); // contactname
				arrLine.add(4, StrUtils.fString(rs.getString(5))); // Telno
				arrLine.add(5, StrUtils.fString(rs.getString(6))); // Email
				arrLine.add(6, StrUtils.fString(rs.getString(7))); // Add1
				arrLine.add(7, StrUtils.fString(rs.getString(8))); // Add2
				arrLine.add(8, StrUtils.fString(rs.getString(9))); // Add3
				arrLine.add(9, StrUtils.fString(rs.getString(10))); // Add4
				arrLine.add(10, StrUtils.fString(rs.getString(11))); // Country
				arrLine.add(11, StrUtils.fString(rs.getString(12))); // Remarks

				arrList.add(arrLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public boolean updateToAssignee(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
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
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ "TO_ASSIGNEE_MASTER" + "] " + sUpdate + sCondition;
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

	public ArrayList getToAssigneeDetails(String aCustCode, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ASSIGNENO,ASSIGNENAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE,ISNULL(STATE,'') STATE FROM "
					+ "["
					+ plant
					+ "_"
					+ "TO_ASSIGNEE_MASTER"
					+ "]"
					+ " WHERE ASSIGNENO = '" + aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons

				arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
				arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
				arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
				arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
				arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
				arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
				arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// isactive
				arrCust.add(17, StrUtils.fString(rs.getString(18)));// State
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}

	public boolean deleteToAssignee(String aCustno, String plant)
			throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_"
					+ "TO_ASSIGNEE_MASTER" + "]" + " WHERE ASSIGNENO" + "='"
					+ aCustno.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}

	public boolean insertIntoToAssignee(Hashtable ht) throws Exception {
		boolean insertedCust = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value + "',";
			}
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "TO_ASSIGNEE_MASTER" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedCust;
	}
        
    public ArrayList getCustomerDetailsForDO(String aDono, String plant)
                    throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList arrCust = new ArrayList();
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                      String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,C.USERFLG1,LNAME,NAME,DESGINATION," +
                      		"TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE," +
                            "ISNULL(D.REMARK1,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTCUSTERMS,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS," +
                            "ISNULL(D.REMARK3,'') as REMARK3,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPNO,'') EMPNO, ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(RCBNO,'') RCBNO,ISNULL(COMPANYREGNUMBER,'') UENNO,ISNULL(C.PAYMENT_TERMS,'') PAYMENT_TERMS  FROM"
                    		   		+ "[" + plant+ "_" + "CUSTMST"+ "] C , "+"[" + plant+ "_" + "DOHDR"+ "] D"
                                    + " WHERE C.PLANT=D.PLANT and  C.CUSTNO=D.custCode  and  D.DONO ='"+aDono+"'" ;
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                            arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                            arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                            arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                            arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                            arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                            arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                            arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
                            arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
                            arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
                            arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
                            arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
                            arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
                            arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
                            arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
                            arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
                            arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
                            arrCust.add(17, StrUtils.fString(rs.getString(18)));// isactive
                            arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                            arrCust.add(19, StrUtils.fString(rs.getString(20)));// Customer Payment Terms
                            arrCust.add(20, StrUtils.fString(rs.getString(21)));// payment days
							arrCust.add(21, StrUtils.fString(rs.getString(22)));// Remark3
                            arrCust.add(22, StrUtils.fString(rs.getString(23)));// DeliveryDate
                            arrCust.add(23, StrUtils.fString(rs.getString(24)));// Employeeono
                            arrCust.add(24, StrUtils.fString(rs.getString(25)));// State
                            arrCust.add(25, StrUtils.fString(rs.getString(26)));// Shipping ID
                            arrCust.add(26, StrUtils.fString(rs.getString(27)));// RCB
                            arrCust.add(27, StrUtils.fString(rs.getString(28)));// UEN
                            arrCust.add(28, StrUtils.fString(rs.getString(29)));// UEN
                            
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return arrCust;
    }
    
    public ArrayList getCustomerDetailsForInvoice(String aINVOICE, String plant)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
              String sQry = "SELECT D.CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,C.USERFLG1,LNAME,NAME,DESGINATION," +
              		"TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE," +
                    "ISNULL(D.NOTE,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTCUSTERMS,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS," +
    				"ISNULL(D.TERMSCONDITIONS,'') as REMARK3,ISNULL(DUE_DATE,'') DELIVERYDATE,ISNULL(EMPNO,'') EMPNO, ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(RCBNO,'') RCBNO,"
    				+ "ISNULL(COMPANYREGNUMBER,'') UENNO,"
    				+ "ISNULL(D.SHIPCONTACTNAME,C.NAME) SHIPCONTACTNAME,ISNULL(D.SHIPDESGINATION,C.DESGINATION) SHIPDESGINATION,ISNULL(D.SHIPWORKPHONE,C.TELNO) SHIPWORKPHONE,ISNULL(D.SHIPHPNO,C.HPNO) SHIPHPNO,"
    				+ "ISNULL(D.SHIPEMAIL,C.EMAIL) SHIPEMAIL,ISNULL(D.SHIPADDR1,C.ADDR1) SHIPUNTINO,ISNULL(D.SHIPADDR2,C.ADDR2) SHIPBUILDING,ISNULL(D.SHIPADDR3,C.ADDR3) SHIPSTREET,ISNULL(D.SHIPADDR4,C.ADDR4) SHIPCITY,"
    				+ "ISNULL(D.SHIPSTATE,C.STATE) SHIPSTATE,ISNULL(D.SHIPCOUNTRY,C.COUNTRY) SHIPCOUNTRY,ISNULL(D.SHIPZIP,C.ZIP) SHIPZIP,ISNULL(C.PAYMENT_TERMS,'') PAYMENT_TERMS  FROM"
            		   		+ "[" + plant+ "_" + "CUSTMST"+ "] C , "+"[" + plant+ "_" + "FININVOICEHDR"+ "] D"
                            + " WHERE C.PLANT=D.PLANT and  C.CUSTNO=D.CUSTNO  and  D.INVOICE ='"+aINVOICE+"'" ;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
                    arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// isactive
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                    arrCust.add(19, StrUtils.fString(rs.getString(20)));// Customer Payment Terms
                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// payment days
					arrCust.add(21, StrUtils.fString(rs.getString(22)));// Remark3
                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// DeliveryDate
                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// Employeeono
                    arrCust.add(24, StrUtils.fString(rs.getString(25)));// State
                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// Shipping ID
                    arrCust.add(26, StrUtils.fString(rs.getString(27)));// RCB
                    arrCust.add(27, StrUtils.fString(rs.getString(28)));// UEN
                    
                    arrCust.add(28, StrUtils.fString(rs.getString(29)));// shipname
                    arrCust.add(29, StrUtils.fString(rs.getString(30)));// shipdes
                    arrCust.add(30, StrUtils.fString(rs.getString(31)));// shipwkph
                    arrCust.add(31, StrUtils.fString(rs.getString(32)));// shiptel
                    arrCust.add(32, StrUtils.fString(rs.getString(33)));// shipemail
                    arrCust.add(33, StrUtils.fString(rs.getString(34)));// shipaddr1 unit
                    arrCust.add(34, StrUtils.fString(rs.getString(35)));// shipaddr2 buil 
                    arrCust.add(35, StrUtils.fString(rs.getString(36)));// shipaddr3 stret 
                    arrCust.add(36, StrUtils.fString(rs.getString(37)));// shipaddr4 city  
                    arrCust.add(37, StrUtils.fString(rs.getString(38)));// shipstate
                    arrCust.add(38, StrUtils.fString(rs.getString(39)));// shipcountry
                    arrCust.add(39, StrUtils.fString(rs.getString(40)));// shipzip
                    arrCust.add(40, StrUtils.fString(rs.getString(41)));// Payment terms

                    
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
}
    
    public ArrayList getCustomerDetailsForBill(String plant, String aBill)
    		throws Exception {
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	ArrayList arrCust = new ArrayList();
    	Connection con = null;
    	try {
    		con = DbBean.getConnection();
    		String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,C.USERFLG1,LNAME,NAME,DESGINATION," +
    				"TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE," +
    				"ISNULL(D.NOTE,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTCUSTERMS,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS," +
    				" '' as REMARK3,ISNULL(DUE_DATE,'') DELIVERYDATE,ISNULL(EMPNO,'') EMPNO, ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(RCBNO,'') RCBNO,"
    				+ "ISNULL(COMPANYREGNUMBER,'') UENNO,"
    				+ "ISNULL(D.SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(D.SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(D.SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(D.SHIPHPNO,'') SHIPHPNO,"
    				+ "ISNULL(D.SHIPEMAIL,'') SHIPEMAIL,ISNULL(D.SHIPADDR1,'') SHIPUNTINO,ISNULL(D.SHIPADDR2,'') SHIPBUILDING,ISNULL(D.SHIPADDR3,'') SHIPSTREET,ISNULL(D.SHIPADDR4,'') SHIPCITY,"
    				+ "ISNULL(D.SHIPSTATE,'') SHIPSTATE,ISNULL(D.SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(D.SHIPZIP,'') SHIPZIP  FROM"
    				+ "[" + plant+ "_" + "CUSTMST"+ "] C , "+"[" + plant+ "_" + "FINBILLHDR"+ "] D"
    				+ " WHERE C.PLANT=D.PLANT and  C.CUSTNO=D.SHIPPINGID  and  D.BILL ='"+aBill+"'" ;
    		this.mLogger.query(this.printQuery, sQry);
    		ps = con.prepareStatement(sQry);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
    			arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
    			arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
    			arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
    			arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
    			arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
    			arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
    			arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
    			arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
    			arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
    			arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
    			arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
    			arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
    			arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
    			arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
    			arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
    			arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
    			arrCust.add(17, StrUtils.fString(rs.getString(18)));// isactive
    			arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
    			arrCust.add(19, StrUtils.fString(rs.getString(20)));// Customer Payment Terms
    			arrCust.add(20, StrUtils.fString(rs.getString(21)));// payment days
    			arrCust.add(21, StrUtils.fString(rs.getString(22)));// Remark3
    			arrCust.add(22, StrUtils.fString(rs.getString(23)));// DeliveryDate
    			arrCust.add(23, StrUtils.fString(rs.getString(24)));// Employeeono
    			arrCust.add(24, StrUtils.fString(rs.getString(25)));// State
    			arrCust.add(25, StrUtils.fString(rs.getString(26)));// Shipping ID
    			arrCust.add(26, StrUtils.fString(rs.getString(27)));// RCB
    			arrCust.add(27, StrUtils.fString(rs.getString(28)));// UEN
    			
    			arrCust.add(28, StrUtils.fString(rs.getString(29)));// shipname
    			arrCust.add(29, StrUtils.fString(rs.getString(30)));// shipdes
    			arrCust.add(30, StrUtils.fString(rs.getString(31)));// shipwkph
    			arrCust.add(31, StrUtils.fString(rs.getString(32)));// shiptel
    			arrCust.add(32, StrUtils.fString(rs.getString(33)));// shipemail
    			arrCust.add(33, StrUtils.fString(rs.getString(34)));// shipaddr1 unit
    			arrCust.add(34, StrUtils.fString(rs.getString(35)));// shipaddr2 buil 
    			arrCust.add(35, StrUtils.fString(rs.getString(36)));// shipaddr3 stret 
    			arrCust.add(36, StrUtils.fString(rs.getString(37)));// shipaddr4 city  
    			arrCust.add(37, StrUtils.fString(rs.getString(38)));// shipstate
    			arrCust.add(38, StrUtils.fString(rs.getString(39)));// shipcountry
    			arrCust.add(39, StrUtils.fString(rs.getString(40)));// shipzip
    			
    			
    		}
    	} catch (Exception e) {
    		this.mLogger.exception(this.printLog, "", e);
    	} finally {
    		DbBean.closeConnection(con, ps);
    	}
    	return arrCust;
    }
    
    public ArrayList getCustomerDetailsForBillVendmst(String plant, String aBill)
    		throws Exception {
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	ArrayList arrCust = new ArrayList();
    	Connection con = null;
    	try {
    		con = DbBean.getConnection();
    		String sQry = "SELECT D.VENDNO,VNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,C.USERFLG1,C.IBAN,NAME,DESGINATION," +
    				"TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE," +
    				"ISNULL(D.NOTE,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTCUSTERMS,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS," +
    				" '' as REMARK3,ISNULL(DUE_DATE,'') DELIVERYDATE,ISNULL(EMPNO,'') EMPNO, ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(RCBNO,'') RCBNO,"
    				+ "ISNULL(COMPANYREGNUMBER,'') UENNO,"
    				+ "ISNULL(D.SHIPCONTACTNAME,C.NAME) SHIPCONTACTNAME,ISNULL(D.SHIPDESGINATION,C.DESGINATION) SHIPDESGINATION,ISNULL(D.SHIPWORKPHONE,C.TELNO) SHIPWORKPHONE,ISNULL(D.SHIPHPNO,C.HPNO) SHIPHPNO,"
    				+ "ISNULL(D.SHIPEMAIL,C.EMAIL) SHIPEMAIL,ISNULL(D.SHIPADDR1,C.ADDR1) SHIPUNTINO,ISNULL(D.SHIPADDR2,C.ADDR2) SHIPBUILDING,ISNULL(D.SHIPADDR3,C.ADDR3) SHIPSTREET,ISNULL(D.SHIPADDR4,C.ADDR4) SHIPCITY,"
    				+ "ISNULL(D.SHIPSTATE,C.STATE) SHIPSTATE,ISNULL(D.SHIPCOUNTRY,C.COUNTRY) SHIPCOUNTRY,ISNULL(D.SHIPZIP,C.ZIP) SHIPZIP  FROM"
    				+ "[" + plant+ "_" + "VENDMST"+ "] C , "+"[" + plant+ "_" + "FINBILLHDR"+ "] D"
    				+ " WHERE C.PLANT=D.PLANT and  C.VENDNO=D.SHIPPINGID  and  D.BILL ='"+aBill+"'" ;
    		this.mLogger.query(this.printQuery, sQry);
    		ps = con.prepareStatement(sQry);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
    			arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
    			arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
    			arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
    			arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
    			arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
    			arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
    			arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
    			arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
    			arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
    			arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
    			arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
    			arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
    			arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
    			arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
    			arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
    			arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
    			arrCust.add(17, StrUtils.fString(rs.getString(18)));// isactive
    			arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
    			arrCust.add(19, StrUtils.fString(rs.getString(20)));// Customer Payment Terms
    			arrCust.add(20, StrUtils.fString(rs.getString(21)));// payment days
    			arrCust.add(21, StrUtils.fString(rs.getString(22)));// Remark3
    			arrCust.add(22, StrUtils.fString(rs.getString(23)));// DeliveryDate
    			arrCust.add(23, StrUtils.fString(rs.getString(24)));// Employeeono
    			arrCust.add(24, StrUtils.fString(rs.getString(25)));// State
    			arrCust.add(25, StrUtils.fString(rs.getString(26)));// Shipping ID
    			arrCust.add(26, StrUtils.fString(rs.getString(27)));// RCB
    			arrCust.add(27, StrUtils.fString(rs.getString(28)));// UEN
    			
    			arrCust.add(28, StrUtils.fString(rs.getString(29)));// shipname
    			arrCust.add(29, StrUtils.fString(rs.getString(30)));// shipdes
    			arrCust.add(30, StrUtils.fString(rs.getString(31)));// shipwkph
    			arrCust.add(31, StrUtils.fString(rs.getString(32)));// shiptel
    			arrCust.add(32, StrUtils.fString(rs.getString(33)));// shipemail
    			arrCust.add(33, StrUtils.fString(rs.getString(34)));// shipaddr1 unit
    			arrCust.add(34, StrUtils.fString(rs.getString(35)));// shipaddr2 buil 
    			arrCust.add(35, StrUtils.fString(rs.getString(36)));// shipaddr3 stret 
    			arrCust.add(36, StrUtils.fString(rs.getString(37)));// shipaddr4 city  
    			arrCust.add(37, StrUtils.fString(rs.getString(38)));// shipstate
    			arrCust.add(38, StrUtils.fString(rs.getString(39)));// shipcountry
    			arrCust.add(39, StrUtils.fString(rs.getString(40)));// shipzip
    			
    			
    		}
    	} catch (Exception e) {
    		this.mLogger.exception(this.printLog, "", e);
    	} finally {
    		DbBean.closeConnection(con, ps);
    	}
    	return arrCust;
    }
    
    //imtinavas detail jsp page
    public ArrayList getCustomerDetailsForTO(String aTono, String plant)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
              String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,C.USERFLG1,LNAME,NAME,DESGINATION," +
              		"TELNO,HPNO,FAX,EMAIL,ISNULL(REMARKS,'') REMARKS,ADDR4,ISACTIVE," +
                    "ISNULL(D.REMARK1,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTCUSTERMS,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS," +
                    "ISNULL(D.REMARK3,'') as REMARK3,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPNO,'') EMPNO, ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(RCBNO,'') RCBNO,ISNULL(COMPANYREGNUMBER,'') UENNO   FROM"
            		   		+ "[" + plant+ "_" + "CUSTMST"+ "] C , "+"[" + plant+ "_" + "TOHDR"+ "] D"
                            + " WHERE C.PLANT=D.PLANT and  C.CUSTNO=D.custCode  and  D.TONO ='"+aTono+"'" ;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
                    arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// isactive
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                    arrCust.add(19, StrUtils.fString(rs.getString(20)));// Customer Payment Terms
                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// payment days
					arrCust.add(21, StrUtils.fString(rs.getString(22)));// Remark3
                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// DeliveryDate
                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// Employeeono
                    arrCust.add(24, StrUtils.fString(rs.getString(25)));// State
                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// Shipping ID
                    arrCust.add(26, StrUtils.fString(rs.getString(27)));// Shipping ID
                    arrCust.add(27, StrUtils.fString(rs.getString(28)));// CUSTOMERUEN
                    
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
}
    
	    public ArrayList getCustomerDetailsForRegister(String acustcode, String plant)
	    throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	ArrayList arrCust = new ArrayList();
	Connection con = null;
	try {
	    con = DbBean.getConnection();
	    String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE FROM "
	                    + "["
	                    + plant
	                    + "_"
	                    + "CUSTMST"
	                    + "]"
	                    + " WHERE CUSTNO ='"+acustcode+"'" ;
	    this.mLogger.query(this.printQuery, sQry);
	    ps = con.prepareStatement(sQry);
	    rs = ps.executeQuery();
	    while (rs.next()) {
	            arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
	            arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
	            arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
	            arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
	            arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
	            arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
	            arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
	            arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
	            arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
	            arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
	            arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
	            arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
	            arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
	            arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
	            arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
	            arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
	            arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
	            arrCust.add(17, StrUtils.fString(rs.getString(18)));// address4
	
	    }
	} catch (Exception e) {
	    this.mLogger.exception(this.printLog, "", e);
	} finally {
	    DbBean.closeConnection(con, ps);
	}
	return arrCust;
	}
    
    public ArrayList getAssigneeDetailsForTO(String aTono, String plant)
                    throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList arrCust = new ArrayList();
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "SELECT ASSIGNENO,ASSIGNENAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,A.USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE,ISNULL(T.SCUST_NAME,'') as SCUST_NAME,ISNULL(T.SCNAME,'') as SCNAME,ISNULL(T.SADDR1,'') as SADDR1,ISNULL(T.SADDR2,'') as SADDR2,ISNULL(T.SCITY,'') as SCITY,ISNULL(T.SCOUNTRY,'') as SCOUNTRY ,ISNULL(T.SZIP,'') as SZIP,ISNULL(T.STELNO,'') as STELNO,ISNULL(T.REMARK1,'') as REMARK1 FROM "
                                    + "["
                                    + plant
                                    + "_"
                                    + "TO_ASSIGNEE_MASTER"
                                    + "] A ,"
                                    + "["
                                    + plant
                                    + "_"
                                    + "TOHDR"
                                    + "]T WHERE A.PLANT=T.PLANT AND  A.ASSIGNENO=T.CUSTCODE AND T.TONO ='"+aTono+"'";
               
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                            arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                            arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                            arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                            arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                            arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                            arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                            arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons

                            arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
                            arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
                            arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
                            arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
                            arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
                            arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
                            arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
                            arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
                            arrCust.add(16, StrUtils.fString(rs.getString(17)));// isactive
                            
                            arrCust.add(17, StrUtils.fString(rs.getString(18)));// scustname
                            arrCust.add(18, StrUtils.fString(rs.getString(19)));// scname
                            arrCust.add(19, StrUtils.fString(rs.getString(20)));// saddr1
                            arrCust.add(20, StrUtils.fString(rs.getString(21)));// saddr2
                            arrCust.add(21, StrUtils.fString(rs.getString(22)));// scity
                            arrCust.add(22, StrUtils.fString(rs.getString(23)));// scountry
                            arrCust.add(23, StrUtils.fString(rs.getString(24)));// szip
                            arrCust.add(24, StrUtils.fString(rs.getString(25)));// scountry
                            arrCust.add(25, StrUtils.fString(rs.getString(26)));// order Remarks
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return arrCust;
    }
    
    public ArrayList getAssigneeDetailsForTONEW(String aTono, String plant)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
            String sQry = "SELECT CUSTNO AS ASSIGNNO, CustName AS ASSIGNENAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,A.USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE,ISNULL(T.SCUST_NAME,'') as SCUST_NAME,ISNULL(T.SCNAME,'') as SCNAME,ISNULL(T.SADDR1,'') as SADDR1,ISNULL(T.SADDR2,'') as SADDR2,ISNULL(T.SCITY,'') as SCITY,ISNULL(T.SCOUNTRY,'') as SCOUNTRY ,ISNULL(T.SZIP,'') as SZIP,ISNULL(T.STELNO,'') as STELNO,ISNULL(T.REMARK1,'') as REMARK1 FROM "
                            + "["
                            + plant
                            + "_"
                            + "CUSTMST"
                            + "] A ,"
                            + "["
                            + plant
                            + "_"
                            + "TOHDR"
                            + "]T WHERE A.PLANT=T.PLANT AND  A.CUSTNO=T.CUSTCODE AND T.TONO ='"+aTono+"'";
       
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons

                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
                    arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
                    arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
                    arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
                    arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
                    arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// isactive
                    
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// scustname
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// scname
                    arrCust.add(19, StrUtils.fString(rs.getString(20)));// saddr1
                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// saddr2
                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// scity
                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// scountry
                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// szip
                    arrCust.add(24, StrUtils.fString(rs.getString(25)));// scountry
                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// order Remarks
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
}
    
    public ArrayList getAssigneeDetailsforLoan(String aLoanNo, String plant)
                    throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList arrCust = new ArrayList();
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "SELECT LASSIGNNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,LM.USERFLG1,ISNULL(NAME,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(EMAIL,''),ISNULL(FAX,''),ISNULL(REMARKS,''),ADDR4,ISACTIVE,ISNULL(REMARK1,'')Remark1,ISNULL(LH.ORDERTYPE,'')ORDTYPE,ISNULL(LH.CRBY,'')CRBY,ISNULL(LH.PAYMENTTYPE,'')PAYMENTTYPE FROM "
                                    + "["
                                    + plant
                                    + "_"
                                    + "LOAN_ASSIGNEE_MST"
                                    + "]LM,["+plant+"_LOANHDR]LH "
                                    +" WHERE  LM.PLANT=LH.PLANT and LM.LASSIGNNO = LH.CustCode and LH.ORDNO ='"+aLoanNo+"'";
                                   /* + " WHERE LASSIGNNO in  (SELECT CUSTCODE FROM "
                                    + "["
                                    + plant
                                    + "_"
                                    + "LOANHDR"
                                    + "] WHERE ORDNO ='"+aLoanNo+"')";*/
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                            arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                            arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                            arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                            arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                            arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                            arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                            arrCust.add(7, StrUtils.fString(rs.getString(8))); // cons

                            arrCust.add(8, StrUtils.fString(rs.getString(9))); // contactname
                            arrCust.add(9, StrUtils.fString(rs.getString(10))); // desg
                            arrCust.add(10, StrUtils.fString(rs.getString(11))); // telno
                            arrCust.add(11, StrUtils.fString(rs.getString(12))); // hpno
                            arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
                            arrCust.add(13, StrUtils.fString(rs.getString(14))); // fax
                            arrCust.add(14, StrUtils.fString(rs.getString(15))); // remarks
                            arrCust.add(15, StrUtils.fString(rs.getString(16)));// addr4
                            arrCust.add(16, StrUtils.fString(rs.getString(17)));// isactive
                            arrCust.add(17, StrUtils.fString(rs.getString(18)));// Order Remarks
							arrCust.add(18, StrUtils.fString(rs.getString(19)));// Order Type
							arrCust.add(19, StrUtils.fString(rs.getString(20)));// User
							arrCust.add(20, StrUtils.fString(rs.getString(21)));// Payment Type
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return arrCust;
    }

    public String getEmailofAllCustomer(String plant)  throws Exception {
            PreparedStatement ps = null;
            ResultSet rs = null;
          String customerList ="";
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "SELECT EMAIL FROM "
                                    + "["
                                    + plant
                                    + "_"
                                    + "CUSTMST"
                                    + "]"
                                    + " WHERE ISNULL(EMAIL,'')<>'' and EMAIL like  ('%@%')";
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            customerList= customerList+StrUtils.fString(rs.getString(1))+","; // email
                    }
                    if(customerList.length()>0){
                             customerList =  customerList.substring(0,customerList.lastIndexOf(","));
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return customerList;
    }
    

  //---Added by Bruhan on March 18 2014, Description: For Work Order Print Work Order report --ManufacturingModuelsChange 
    public ArrayList getCustomerDetailsForWO(String aWono, String plant)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
            String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,C.USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE,ISNULL(D.REMARK1,'') as REMARK1 FROM "
                            + "[" + plant+ "_" + "CUSTMST"+ "] C , "+"[" + plant+ "_" + "WOHDR"+ "] D"
                            + " WHERE C.PLANT=D.PLANT and  C.CUSTNO=D.custCode  and  D.WONO ='"+aWono+"'" ;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
                    arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// address4
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
  }

 //---Added by Bruhan on March 18 2014 End

  public ArrayList selectForReport(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" AND ");
				conditon = formCondition(ht);
				sql.append(" " + conditon);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());

			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
    //---Added by Bruhan on March 18 2014 End
 public ArrayList selectmobileordercustomerdetails(String Plant, String Custname,String SearchBy,String cond)
	throws Exception {
		
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		String sCondition1="";
		String sql ="";
		
		if(SearchBy.equalsIgnoreCase("BYLIKE"))
		{
		sql = "SELECT CUSTNO,CNAME,LNAME,ISNULL(NAME,'')NAME,ISNULL(TELNO,'')TELNO,ISNULL(EMAIL,'')EMAIL,ISNULL(ADDR1,'')ADDR1,ISNULL(ADDR2,'')ADDR2,ISNULL(ADDR3,'')ADDR3,ISNULL(ADDR4,'')ADDR4,ISNULL(COUNTRY,'')COUNTRY,ISNULL(REMARKS,'')REMARKS,ISNULL(ZIP,'')ZIP, "
			+ " ISNULL(HPNO,'')HPNO,ISNULL(CUSTOMER_STATUS_ID,'') customerstatusid FROM "
	    	+ "["
			+ Plant
			+ "_"
			+ "CUSTMST"
			+ "]"
			+ "   WHERE CNAME LIKE '" + Custname + "%'" + cond
			+ " ORDER BY CUSTNO,CNAME ";
		}
		else if(SearchBy.equalsIgnoreCase("BYEQUAL"))
		{
			sql = "SELECT CUSTNO,CNAME,LNAME,ISNULL(NAME,'')NAME,ISNULL(TELNO,'')TELNO,ISNULL(EMAIL,'')EMAIL,ISNULL(ADDR1,'')ADDR1,ISNULL(ADDR2,'')ADDR2,ISNULL(ADDR3,'')ADDR3,ISNULL(ADDR4,'')ADDR4,ISNULL(COUNTRY,'')COUNTRY,ISNULL(REMARKS,'')REMARKS,ISNULL(ZIP,'')ZIP, "
					+ " ISNULL(HPNO,'')HPNO,ISNULL(CUSTOMER_STATUS_ID,'') customerstatusid FROM "
			    	+ "["
					+ Plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ "   WHERE CNAME= '" + Custname + "'" + cond
					+ " ORDER BY CUSTNO,CNAME ";
		}
		
		try {
			this.mLogger.query(this.printQuery, sql.toString());
			con = com.track.gates.DbBean.getConnection();
			alData = selectData(con, sql.toString());
		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
			return alData;
	}
   // start added by Bruhan to get customer details for estimate order printout on 02 july 2015 
    public ArrayList getCustomerDetailsForEST(String aEstno, String plant)
            throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList arrCust = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
            String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,C.USERFLG1,LNAME,NAME," +
            		       "DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE," +
            		       "ISNULL(D.REMARK1,'') as REMARK1," +
            		       "ISNULL(PAY_TERMS,'')   as PRINTCUSTERMS,ISNULL(D.REMARK3,'') REMARK3,ISNULL(EMPNO,'') EMPNO, ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(RCBNO, '') RCBNO,ISNULL(COMPANYREGNUMBER, '') UENNO   FROM "
                            + "[" + plant+ "_" + "CUSTMST"+ "] C , "+"[" + plant+ "_" + "ESTHDR"+ "] D"
                            + " WHERE C.PLANT=D.PLANT and  C.CUSTNO=D.custCode  and  D.ESTNO ='"+aEstno+"'" ;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
                    arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
                    arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
                    arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
                    arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
                    arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
                    arrCust.add(17, StrUtils.fString(rs.getString(18)));// isactive
                    arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                    arrCust.add(19, StrUtils.fString(rs.getString(20)));// Customer Payment Terms
                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// order Remarks3
                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// empno
                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// state
                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// shipping id
                    arrCust.add(24, StrUtils.fString(rs.getString(25)));// CUSTOMER RCBNO
                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// CUSTOMER UENNO
                    //arrCust.add(24, StrUtils.fString(rs.getString(25)));
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return arrCust;
}
    // end added by Bruhan to get customer details for estimate order printout on 02 july 2015     

public boolean isExistsCustomerType(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "CUSTOMER_TYPE_MST"
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			System.out.println(sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}
    
public boolean isExistsSupplierType(Hashtable ht) throws Exception {

	boolean flag = false;
	java.sql.Connection con = null;

	try {
		con = com.track.gates.DbBean.getConnection();

		// query
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" COUNT(*) ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "SUPPLIER_TYPE_MST"
				+ "]");
		sql.append(" WHERE  " + formCondition(ht));
		this.mLogger.query(this.printQuery, sql.toString());
		System.out.println(sql.toString());
		flag = isExists(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return flag;
}

public boolean isExistSupplierType(String supplier_type_id,String supplier_type_desc, String plant) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "SUPPLIER_TYPE_MST" + "]" + " WHERE "
				+ IConstants.SUPPLIERTYPEID + " = '"
				+ supplier_type_desc.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}

public boolean isExistCustomerType(String customer_type_id,String customer_type_desc, String plant) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "CUSTOMER_TYPE_MST" + "]" + " WHERE "
				+ IConstants.CUSTOMERTYPEID + " = '"
				+ customer_type_desc.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}
   
   public boolean isExistsCustomerType(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
            String TABLE_NAME="CUSTMST";
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
   
public boolean isExistsSupplierType(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
            String TABLE_NAME="Vendmst";
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}
   
 public boolean isExistsProductCustomerType(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
            String TABLE_NAME="CUSTOMER_TYPE_MST";
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
	
	public boolean insertIntoSupplierTypeMst(Hashtable ht) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "SUPPLIER_TYPE_MST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			insertedInv = insertData(con, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertedInv;
	}
 
	public boolean insertIntoCustomerTypeMst(Hashtable ht) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "CUSTOMER_TYPE_MST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			insertedInv = insertData(con, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertedInv;
	}
	
		public boolean deleteSupplierTypeId(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deleteCustomerTypeId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "SUPPLIER_TYPE_MST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));

			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return delete;
	}
	
	
	public boolean deleteCustomerTypeId(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deleteCustomerTypeId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "CUSTOMER_TYPE_MST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));

			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return delete;
	}
	
	public boolean updateSupplierTypeMst(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

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

			String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
					+ "SUPPLIER_TYPE_MST" + "]" + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}
    
	public boolean updateCustomerTypeMst(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

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

			String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
					+ "CUSTOMER_TYPE_MST" + "]" + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}
	
	public ArrayList getSupplierTypeSummaryDetails(String  custtypeid, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct SUPPLIER_TYPE_ID,SUPPLIER_TYPE_DESC,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "SUPPLIER_TYPE_MST] where SUPPLIER_TYPE_ID like '"
					+ custtypeid + "%' OR SUPPLIER_TYPE_DESC LIKE '" + custtypeid + "%'"+ extraCon
					+ " ORDER BY SUPPLIER_TYPE_ID ";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}

	

	public ArrayList getSupplierTypeDetails(String  custtypeid, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct SUPPLIER_TYPE_ID,SUPPLIER_TYPE_DESC,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "SUPPLIER_TYPE_MST] where SUPPLIER_TYPE_ID like '"
					+ custtypeid + "%' OR SUPPLIER_TYPE_DESC LIKE '" + custtypeid + "%'"+ extraCon
					+ " ORDER BY SUPPLIER_TYPE_ID ";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}

	
	public ArrayList getCustomerTypeDetails(String  custtypeid, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct CUSTOMER_TYPE_ID,CUSTOMER_TYPE_DESC,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "CUSTOMER_TYPE_MST] where CUSTOMER_TYPE_ID like '"
					+ custtypeid + "%' OR CUSTOMER_TYPE_DESC LIKE '" + custtypeid + "%'"+ extraCon
					+ " ORDER BY CUSTOMER_TYPE_ID ";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
 public String getCustomerTypeId(String aPlant, String CustomerName) throws Exception {
	      java.sql.Connection con = null;
	      String customertypeid = "";
	      try {
	              
	       con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer(" select isnull(Customer_Type_Id,'') CustomerTypeId  FROM ["+aPlant+"_CUSTMST]  WHERE CNAME='"+CustomerName+"'");
          System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery.toString());
          customertypeid = (String) m.get("CustomerTypeId");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return customertypeid;
	  }

public String getSupplierTypeId(String aPlant, String CustomerName) throws Exception {
	      java.sql.Connection con = null;
	      String suppliertypeid = "";
	      try {
	              
	       con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer(" select isnull(Supplier_Type_Id,'') SupplierTypeId  FROM ["+aPlant+"_VENDMST]  WHERE VNAME='"+CustomerName+"'");
        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery.toString());
        suppliertypeid = (String) m.get("SupplierTypeId");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return suppliertypeid;
	  }

public String getSupplierTypeById(String plant, String id)throws Exception {
	java.sql.Connection connection = null;
    String query = "";
    String transfername="";
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT SUPPLIER_TYPE_DESC FROM ["+ plant +"_"+"SUPPLIER_TYPE_MST"+"] WHERE SUPPLIER_TYPE_ID ='"+id+"'";

		if(connection != null){
			Map m = this.getRowOfData(connection, query.toString());

			transfername = (String) m.get("SUPPLIER_TYPE_DESC");
		this.mLogger.query(this.printQuery, query);
		
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return transfername;
}

public String getCustomerTypeById(String plant, String id)throws Exception {
	java.sql.Connection connection = null;
    String query = "";
    String transfername="";
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT CUSTOMER_TYPE_DESC FROM ["+ plant +"_"+"CUSTOMER_TYPE_MST"+"] WHERE CUSTOMER_TYPE_ID='"+id+"'";

		if(connection != null){
			Map m = this.getRowOfData(connection, query.toString());

			transfername = (String) m.get("CUSTOMER_TYPE_DESC");
		this.mLogger.query(this.printQuery, query);
		
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return transfername;
}
	
		  
	
	public ArrayList getCustomerListWithType(String aCustName,
			String plant,String aCustType) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		String extCond="";
		try {
			con = DbBean.getConnection();
				String sQry = "SELECT CUSTNO,CNAME,ISNULL(LNAME,'')LNAME,ISNULL(NAME,'') NAME,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY_CODE,'') SHIPCOUNTRY_CODE,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,"
						+ "ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(STATE,'')AS STATE,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(CUSTOMER_TYPE_ID,'')  as CUSTOMER_TYPE_ID, "
					+ " ISNULL(CUSTOMER_STATUS_ID,'')  as CUSTOMER_STATUS_ID,ISNULL(PAY_TERMS,'')  as PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(RCBNO,'') AS RCBNO,ISNULL(CREDITLIMIT,'') AS CREDITLIMIT,ISNULL(CREDIT_LIMIT_BY,'NOLIMIT') AS CREDIT_LIMIT_BY,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(FACEBOOKID,'') AS FACEBOOKID,ISNULL(TWITTERID,'') AS TWITTERID,ISNULL(LINKEDINID,'') AS LINKEDINID,ISNULL(SKYPEID,'') AS SKYPEID,ISNULL(OPENINGBALANCE,'') AS OPENINGBALANCE,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER , ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(TRANSPORTID,'') TRANSPORTID,ISNULL(DATEOFBIRTH,'') as DATEOFBIRTH,ISNULL(NATIONALITY,'') as NATIONALITY  FROM "
		
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ " WHERE CNAME LIKE '" + aCustName + "%' ";
			
			this.mLogger.query(this.printQuery, sQry);
			if(aCustType.length()>0)
			{
				extCond=" AND CUSTOMER_TYPE_ID = '" + aCustType + "' ORDER BY CNAME ASC ";
			}
			else
			{
				extCond=" ORDER BY CNAME ASC ";
			}
			arrList = selectForReport(sQry, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getPeppolCustomerListWithType(String aCustName,
			String plant,String aCustType) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		String extCond="";
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT CUSTNO,CNAME,ISNULL(LNAME,'')LNAME,ISNULL(NAME,'') NAME,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY_CODE,'') SHIPCOUNTRY_CODE,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,"
					+ "ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(REMARKS,'')REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(STATE,'')AS STATE,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(DESGINATION,'')DESGINATION,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(ISACTIVE,'')ISACTIVE,ISNULL(CUSTOMER_TYPE_ID,'')  as CUSTOMER_TYPE_ID,ISNULL(PEPPOL_ID,'') as PEPPOL_ID,"
					+ " ISNULL(CUSTOMER_STATUS_ID,'')  as CUSTOMER_STATUS_ID,ISNULL(PAY_TERMS,'')  as PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(RCBNO,'') AS RCBNO,ISNULL(CREDITLIMIT,'') AS CREDITLIMIT,ISNULL(CREDIT_LIMIT_BY,'NOLIMIT') AS CREDIT_LIMIT_BY,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(FACEBOOKID,'') AS FACEBOOKID,ISNULL(TWITTERID,'') AS TWITTERID,ISNULL(LINKEDINID,'') AS LINKEDINID,ISNULL(SKYPEID,'') AS SKYPEID,ISNULL(OPENINGBALANCE,'') AS OPENINGBALANCE,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE,ISNULL(COMPANYREGNUMBER,'') COMPANYREGNUMBER , ISNULL(CURRENCY_ID,'') CURRENCY_ID,ISNULL(TRANSPORTID,'') TRANSPORTID,ISNULL(DATEOFBIRTH,'') as DATEOFBIRTH,ISNULL(NATIONALITY,'') as NATIONALITY  FROM "
					
					+ "["
					+ plant
					+ "_"
					+ "CUSTMST"
					+ "]"
					+ " WHERE ISPEPPOL=1 AND CNAME LIKE '" + aCustName + "%' ";
			
			this.mLogger.query(this.printQuery, sQry);
			if(aCustType.length()>0)
			{
				extCond=" AND CUSTOMER_TYPE_ID = '" + aCustType + "' ORDER BY CNAME ASC ";
			}
			else
			{
				extCond=" ORDER BY CNAME ASC ";
			}
			arrList = selectForReport(sQry, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
		
	}
    
/*public ArrayList getMultiPriceCustTypeList(String plant, String customertype,
			String itemDesc,String aLoc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
			if (itemDesc.length() > 0) {
				itemDesc = " AND altitem.ITEM in (SELECT ITEM FROM " + plant
						+ "_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+new StrUtils().InsertQuotes(itemDesc.replaceAll(" ",""))+"%')" ;
				
			}
                        if (aLoc.length() > 0) {
                            aLoc = "   and loc = '"+ aLoc+"'";
                        }
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct(invmst.item) item ,(select itemdesc from "+plant+"_ITEMMST where ITEM =invmst.ITEM) as itemDesc,(select stkuom from "+plant+"_ITEMMST where ITEM =invmst.ITEM) as uom from " + "["
					+ plant + "_" + "invmst" + "] invmst, [" + plant + "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem  "
					+ " where invmst.plant='" + plant + "'  "+ aLoc
					+ " and Qty > 0  and  altitem.ALTERNATE_ITEM_NAME like '"
					+ itemno + "%' and altitem.ITEM = invmst.ITEM " + itemDesc
					+ " ORDER BY invmst.item ";
			this.mLogger.query(this.printQuery, sQry);
			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}*/
    
	public boolean insertIntoCustomerStatus(Hashtable ht) throws Exception {
		boolean insertedCycleCount = false;

		Connection conn = null;
		String TABLE_NAME="CUSTOMER_STATUS_MST";
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ TABLE_NAME + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query.toString());

			insertedCycleCount = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(conn);
		}

		return insertedCycleCount;
	}
	
	public ArrayList getCustomerStatusDetails(String selectList, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;
        String TABLE_NAME="CUSTOMER_STATUS_MST";
		Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");
		try {
			con = DbBean.getConnection();

			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0) {
				sql.append(" ");
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}
	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		String TABLE_NAME="CUSTOMER_STATUS_MST";
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
	
	public boolean isExisitCustomer(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "CUSTMST" + "]");
			sql.append(" WHERE  " + formCondition(ht));
			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
 
	public boolean updateCustomerStatus(String query, Hashtable htCondition,
			String extCondition) throws Exception {
		boolean flag = false;
		PreparedStatement ps = null;
		Connection con = null;
		String TABLE_NAME="CUSTOMER_STATUS_MST";
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + TABLE_NAME + "]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE  " + formCondition(htCondition));

			if (extCondition.length() != 0) {
				sql.append(extCondition);
			}

			this.mLogger.query(this.printQuery, sql.toString());

			flag = updateData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return flag;
	}

	public boolean deleteCustomerStatus(String aCustStatusId,String plant) throws Exception {

		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		String TABLE_NAME="CUSTOMER_STATUS_MST";
		try {
			con = DbBean.getConnection();
	      String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
					+ "where " + IConstants.CUSTOMERSTATUSID + "='" + aCustStatusId.toUpperCase() + "'";
			ps = con.prepareStatement(sQry);
			this.mLogger.query(this.printQuery, sQry);
			deleteItemMst = DeleteRow(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}

	public ArrayList getCustomerStatusDetails(String  custstatusid, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct CUSTOMER_STATUS_ID,ISNULL(CUSTOMER_STATUS_DESC,'') CUSTOMER_STATUS_DESC,ISNULL(ISACTIVE,'') ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "CUSTOMER_STATUS_MST] where CUSTOMER_STATUS_ID like '"
					+ custstatusid + "%' OR CUSTOMER_STATUS_DESC LIKE '" + custstatusid + "%'"+ extraCon
					+ " ORDER BY CUSTOMER_STATUS_ID ";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	 public String getCustomerStatusId(String aPlant, String CustomerName) throws Exception {
	      java.sql.Connection con = null;
	      String customerstatusid = "";
	      try {
	              
	       con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer(" select isnull(Customer_Status_Id,'') CustomerStatusId  FROM ["+aPlant+"_CUSTMST]  WHERE CNAME='"+CustomerName+"'");
           System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery.toString());
           customerstatusid = (String) m.get("CustomerStatusId");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return customerstatusid;
	  }
	  public String getCustomerStatusDesc(String aPlant, String CustomerStatusId) throws Exception {
	      java.sql.Connection con = null;
	      String customerstatusdesc = "";
	      try {
	              
	       con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer(" select isnull(Customer_Status_Desc,'') CustomerStatusDesc  FROM ["+aPlant+"_CUSTOMER_STATUS_MST]  WHERE CUSTOMER_STATUS_ID='"+CustomerStatusId+"'");
           System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery.toString());
          customerstatusdesc = (String) m.get("CustomerStatusDesc");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return customerstatusdesc;
	  }
	  
	  public String getCustomerTypeDesc(String aPlant, String CustomerTypeId) throws Exception {
	      java.sql.Connection con = null;
	      String customertypedesc = "";
	      try {
	              
	       con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer(" select isnull(Customer_Type_Desc,'') CustomerTypeDesc  FROM ["+aPlant+"_CUSTOMER_TYPE_MST]  WHERE CUSTOMER_TYPE_ID='"+CustomerTypeId+"'");
           System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery.toString());
          customertypedesc = (String) m.get("CustomerTypeDesc");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return customertypedesc;
	  }
    
  public String getSupplierTypeDesc(String aPlant, String CustomerTypeId) throws Exception {
	      java.sql.Connection con = null;
	      String suppliertypedesc = "";
	      try {
	              
	       con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer(" select isnull(Supplier_Type_Desc,'') SupplierTypeDesc  FROM ["+aPlant+"_SUPPLIER_TYPE_MST]  WHERE SUPPLIER_TYPE_ID='"+CustomerTypeId+"'");
           System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery.toString());
          suppliertypedesc = (String) m.get("SupplierTypeDesc");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return suppliertypedesc;
	  }
    
 
 public String getCustomerCode(String aPlant, String CustomerName) throws Exception {
	      java.sql.Connection con = null;
	      String customercode = "";
	      try {
	              
	       con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer(" select isnull(CUSTNO,'') customercode  FROM ["+aPlant+"_CUSTMST]  WHERE CNAME='"+CustomerName+"'");
           System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery.toString());
	       customercode = (String) m.get("customercode");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return customercode == null ? "" : customercode.toString();
	  }
 public String getVendorCode(String aPlant, String VendorName) throws Exception {
	      java.sql.Connection con = null;
	      String vendorcode = "";
	      try {
	              
	       con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer(" select isnull(VENDNO,'') customercode  FROM ["+aPlant+"_VENDMST]  WHERE VNAME='"+VendorName+"'");
           System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery.toString());
	       vendorcode = (String) m.get("customercode");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return vendorcode;
	  }  
 public ArrayList getSupplierDetails(String  supplier, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct ISNULL(VENDNO,'') VENDNO,ISNULL(VNAME,'') VNAME,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "VENDMST] where VENDNO like '"
					+ supplier + "%' OR VNAME LIKE '" + supplier + "%'"+ extraCon
					+ " ORDER BY VENDNO ";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
 public boolean isExistsSupplier(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
         String TABLE_NAME="VENDMST";
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
	 public ArrayList getCustomerDetailsForTaxInv(String aDono, String plant)
         throws Exception {
 PreparedStatement ps = null;
 ResultSet rs = null;
 ArrayList arrCust = new ArrayList();
 Connection con = null;
 try {
         con = DbBean.getConnection();
           String sQry = "SELECT ISNULL(CUSTNO,'') as CUSTNO,ISNULL(CNAME,'') as CNAME,ISNULL(ADDR1,'') as ADDR1,ISNULL(ADDR2,'') as ADDR2,ISNULL(ADDR3,'') as ADDR3,ISNULL(COUNTRY,'') as COUNTRY,ISNULL(ZIP,'') as ZIP,ISNULL(C.USERFLG1,'') as USERFLG1,ISNULL(LNAME,'') as LNAME,ISNULL(NAME,'') as NAME,ISNULL(DESGINATION,'') as DESGINATION," +
           		"ISNULL(TELNO,'') as TELNO,ISNULL(HPNO,'') as HPNO,ISNULL(FAX,'') as FAX,ISNULL(EMAIL,'') as EMAIL,ISNULL(C.REMARKS,'') as REMARKS,ISNULL(ADDR4,'') as ADDR4,ISNULL(ISACTIVE,'') as ISACTIVE," +
                 "ISNULL(D.REMARK1,'') as REMARK1,ISNULL(PAY_TERMS,'')   as PRINTCUSTERMS,ISNULL(PAY_IN_DAYS,'')   as PMTDAYS," +
                 "ISNULL(D.REMARK3,'') as REMARK3,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPNO,'') EMPNO, ISNULL(STATE,'') STATE, ISNULL(SHIPPINGID, -1) SHIPPINGID,ISNULL(RCBNO,'') RCBNO,ISNULL(CashCust,'') CashCust FROM"
         		   		+ "[" + plant+ "_" + "POSHDR"+ "] D LEFT JOIN "+"[" + plant+ "_" + "CUSTMST"+ "] C on C.PLANT=D.PLANT and  C.CUSTNO=D.custCode"
                         + " WHERE  D.POSTRANID ='"+aDono+"'" ;
         this.mLogger.query(this.printQuery, sQry);
         ps = con.prepareStatement(sQry);
         rs = ps.executeQuery();
         while (rs.next()) {
                 arrCust.add(0, StrUtils.fString(rs.getString(1))); // custno
                 arrCust.add(1, StrUtils.fString(rs.getString(2))); // cname
                 arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
                 arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
                 arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
                 arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
                 arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
                 arrCust.add(7, StrUtils.fString(rs.getString(8))); // userflg1
                 arrCust.add(8, StrUtils.fString(rs.getString(9))); // LASTNAME
                 arrCust.add(9, StrUtils.fString(rs.getString(10)));// name
                 arrCust.add(10, StrUtils.fString(rs.getString(11)));// designation
                 arrCust.add(11, StrUtils.fString(rs.getString(12)));// telno
                 arrCust.add(12, StrUtils.fString(rs.getString(13)));// hpno
                 arrCust.add(13, StrUtils.fString(rs.getString(14)));// fax
                 arrCust.add(14, StrUtils.fString(rs.getString(15)));// email
                 arrCust.add(15, StrUtils.fString(rs.getString(16)));// remarks
                 arrCust.add(16, StrUtils.fString(rs.getString(17)));// address4
                 arrCust.add(17, StrUtils.fString(rs.getString(18)));// isactive
                 arrCust.add(18, StrUtils.fString(rs.getString(19)));// order Remarks
                 arrCust.add(19, StrUtils.fString(rs.getString(20)));// Customer Payment Terms
                 arrCust.add(20, StrUtils.fString(rs.getString(21)));// payment days
					arrCust.add(21, StrUtils.fString(rs.getString(22)));// Remark3
                 arrCust.add(22, StrUtils.fString(rs.getString(23)));// DeliveryDate
                 arrCust.add(23, StrUtils.fString(rs.getString(24)));// Employeeono
                 arrCust.add(24, StrUtils.fString(rs.getString(25)));// State
                 arrCust.add(25, StrUtils.fString(rs.getString(26)));// Shipping ID
                 arrCust.add(26, StrUtils.fString(rs.getString(27)));// RCBNO
                 arrCust.add(27, StrUtils.fString(rs.getString(28)));// CashCust
                 
         }
 } catch (Exception e) {
         this.mLogger.exception(this.printLog, "", e);
 } finally {
         DbBean.closeConnection(con, ps);
 }
 return arrCust;
}
	 public ArrayList getCustomerTypeListStartsWithName(String aCustName,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;		
			try {
				con = DbBean.getConnection();

						String sQry = "SELECT CUSTOMER_TYPE_ID,CUSTOMER_TYPE_DESC FROM "
						+ "["
						+ plant
						+ "_"
						+ "CUSTOMER_TYPE_MST"
						+ "]"
					//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
				+ " WHERE CUSTOMER_TYPE_DESC LIKE '" + aCustName + "%' or CUSTOMER_TYPE_ID LIKE '%" + aCustName + "%'"  + cond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;

		}
		public ArrayList getCustomerListStartsWithName(String aCustName,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;		
			try {
				con = DbBean.getConnection();

						String sQry = "SELECT CUSTNO,CNAME FROM "
						+ "["
						+ plant
						+ "_"
						+ "CUSTMST"
						+ "]"
					//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
				+ " WHERE CNAME LIKE '" + aCustName + "%' or CUSTNO LIKE '%" + aCustName + "%'"  + cond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;

		}
		public ArrayList getEmployeeListStartsWithName(String aCustName,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;		
			try {
				con = DbBean.getConnection();

						String sQry = "SELECT EMPNO,FNAME FROM "
						+ "["
						+ plant
						+ "_"
						+ "EMP_MST"
						+ "]"
					//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
				+ " WHERE FNAME LIKE '" + aCustName + "%' or EMPNO LIKE '%" + aCustName + "%'"  + cond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;

		}
		public ArrayList getEmployeeListNameForPOSsales(String aCustName,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;		
			try {
				con = DbBean.getConnection();
						String sQry = "SELECT EMPNO,FNAME,ISSALESMAN FROM "
						+ "["
						+ plant
						+ "_"
						+ "EMP_MST"
						+ "]"
				+ " WHERE FNAME LIKE '" + aCustName + "%' and ISSALESMAN = 1 "  + cond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;
		}
		public ArrayList getEmployeeListNameForPOSCashier(String aCustName,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;		
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT EMPNO,FNAME,ISCASHIER FROM "
						+ "["
						+ plant
						+ "_"
						+ "EMP_MST"
						+ "]"
						+ " WHERE FNAME LIKE '" + aCustName + "%' and ISCASHIER = 1 "  + cond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;
		}
		public int Vendorcount(String plant)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int Vendorcount = 0;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
						+ "VendMst" + "]" + " WHERE " + IConstants.PLANT
						+ " = '" + plant.toUpperCase() + "'";
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					Vendorcount = rs.getInt(1);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return Vendorcount;
		}
		
		public int Ordercount(String plant, String afrmDate, String atoDate)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int Ordercount = 0;
			String sCondition = "";
			String dtCondStr =    " AND (SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2))";
			if (afrmDate.length() > 0) {
	          	sCondition = " " + dtCondStr +"  >= '" + afrmDate
	          	+ "'  ";
	          	if (atoDate.length() > 0) {
	          		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
	          		+ "'  ";
	          	}
	          }
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
						+ "FINBILLHDR" + "]" + " WHERE " + IConstants.PLANT
						+ " = '" + plant.toUpperCase() + "'"+ sCondition;
				System.out.println(sQry);
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					Ordercount = rs.getInt(1);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return Ordercount;
		}
		
		public Map getCustomerDetail(Hashtable ht, String plant) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Map custDetail = new HashMap();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT CUSTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE,ISNULL(PAY_TERMS,'')  as PAYMENT_TERMS,ISNULL(PAY_IN_DAYS ,'') AS DAYS,ISNULL(CUSTOMER_TYPE_ID,'') CUSTOMER_TYPE_ID, "
						+ " ISNULL(CUSTOMER_STATUS_ID,'') CUSTOMER_STATUS_ID,ISNULL(STATE,'') STATE,ISNULL(RCBNO,'') RCBNO,ISNULL(CREDITLIMIT,0) as CREDITLIMIT,ISNULL(ISCREDITLIMIT,'') as ISCREDITLIMIT,ISNULL(CUSTOMEREMAIL,'')CUSTOMEREMAIL,ISNULL(WEBSITE,'')WEBSITE,ISNULL(FACEBOOKID,'')FACEBOOK,ISNULL(TWITTERID,'')TWITTER,ISNULL(LINKEDINID,'')LINKEDIN,ISNULL(SKYPEID,'')SKYPE,ISNULL(OPENINGBALANCE,'')OPENINGBALANCE,ISNULL(WORKPHONE,'')WORKPHONE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(CREDIT_LIMIT_BY,'') CREDIT_LIMIT_BY,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE,ISNULL(BANKNAME,'') BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'') IBAN,ISNULL(BANKROUTINGCODE,'') BANKROUTINGCODE FROM"
						+ "["+ plant+ "_CUSTMST] ";
				String conditon = formCondition(ht);
                if(conditon!="") {
                	sQry+=" WHERE ";
                	sQry+=conditon;
                }
				this.mLogger.query(this.printQuery, sQry);
				custDetail = getRowOfData(con, sQry);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return custDetail;
		}
		
		public Map getSupplierDetail(Hashtable ht, String plant) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Map vendorDetail = new HashMap();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT * FROM"
						+ "["+ plant+ "_VENDMST] ";
				String conditon = formCondition(ht);
                if(conditon!="") {
                	sQry+=" WHERE ";
                	sQry+=conditon;
                }
				this.mLogger.query(this.printQuery, sQry);
				vendorDetail = getRowOfData(con, sQry);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return vendorDetail;
		}
		
		public Map selectRow(String query, Hashtable ht,String plant) throws Exception {
            Map map = new HashMap();

            java.sql.Connection con = null;
            try {

                    con = com.track.gates.DbBean.getConnection();
                    StringBuffer sql = new StringBuffer(" SELECT " + query + " from ["+ plant+ "_CUSTMST]  ");                    
                    String conditon = formCondition(ht);
                    if(conditon!="")
                    	sql.append(" WHERE ");
                    sql.append(conditon);
                    this.mLogger.query(this.printQuery, sql.toString());

                    map = getRowOfData(con, sql.toString());

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            } finally {
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return map;
    }
		
		public String getPeppolId(String custno,String plant) throws Exception {
	        PlantMstDAO plantmst = new PlantMstDAO();
	        plantmst.setmLogger(mLogger);
	        String peppolid = "";
	        Hashtable ht = new Hashtable();
	        ht.put("CUSTNO", custno);
	        String query = " ISNULL(PEPPOL_ID,'')PEPPOL_ID ";
	        Map m = selectRow(query, ht,plant);
	        peppolid = (String) m.get("PEPPOL_ID");
	        return peppolid;
	    }
		
		public ArrayList getCustTypeDetails(String aCustCode, String plant)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrCust = new ArrayList();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT PLANT,CUSTOMER_TYPE_ID,CUSTOMER_TYPE_DESC,COMMENTS1,COMMENTS2,CRAT,CRBY,IsActive FROM  "
						+ "["
						+ plant
						+ "_"
						+ "CUSTOMER_TYPE_MST"
						+ "]"
						+ " WHERE CUSTOMER_TYPE_ID = '"
						+ aCustCode + "' OR CUSTOMER_TYPE_DESC = '"
						+ aCustCode + "'";
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					arrCust.add(0, StrUtils.fString(rs.getString(1))); 
					arrCust.add(1, StrUtils.fString(rs.getString(2))); 
					arrCust.add(2, StrUtils.fString(rs.getString(3))); 
					arrCust.add(3, StrUtils.fString(rs.getString(4))); 
					arrCust.add(4, StrUtils.fString(rs.getString(5))); 
					arrCust.add(5, StrUtils.fString(rs.getString(6))); 
					arrCust.add(6, StrUtils.fString(rs.getString(7))); 
					arrCust.add(7, StrUtils.fString(rs.getString(8))); 
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrCust;
		}
		
		public ArrayList getSupplierTypeDetails(String aSupCode, String plant)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrSup = new ArrayList();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT PLANT,SUPPLIER_TYPE_ID,SUPPLIER_TYPE_DESC,CRAT,CRBY,IsActive FROM  "
						+ "["
						+ plant
						+ "_"
						+ "SUPPLIER_TYPE_MST"
						+ "]"
						+ " WHERE SUPPLIER_TYPE_ID = '"
						+ aSupCode + "' OR SUPPLIER_TYPE_DESC = '"
						+ aSupCode + "'";
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					arrSup.add(0, StrUtils.fString(rs.getString(1))); 
					arrSup.add(1, StrUtils.fString(rs.getString(2))); 
					arrSup.add(2, StrUtils.fString(rs.getString(3))); 
					arrSup.add(3, StrUtils.fString(rs.getString(6))); 
					arrSup.add(4, StrUtils.fString(rs.getString(7))); 
					arrSup.add(5, StrUtils.fString(rs.getString(8))); 
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrSup;
		}
		
		 public boolean isValidCustno(String custno,  String plant)
		    		throws Exception {
		    	String sql;
		    	
		    	Connection con = null;
		    	ResultSet rs = null;
		    	boolean isValid=false;
		    	try {
		    		con = DbBean.getConnection();
		    		Statement stmt = con.createStatement();
		    		sql = "select a.CUSTNO,a.CNAME from "+ plant+"_CUSTMST a where CUSTNO = '"
		    				+ custno
		    				+ "'"
		    				+ "and PLANT='"
		    				+ plant + "'";
		    		
		    		
		    		this.mLogger.query(printLog, sql);
		    		rs = stmt.executeQuery(sql);
		    		
		    		if (rs != null) {
		    			if (rs.next()) {
		    				
		    				isValid =true;           
		    				
		    			}
		    		}
		    		
		    		
		    	} // End of try
		    	catch (Exception e) {
		    		this.mLogger.log(0, "" + e.getMessage());
		    		
		    	} finally {
		    		rs.close();
		    		DbBean.closeConnection(con);
		    	}
		    	
		    	return isValid;
		    }
}