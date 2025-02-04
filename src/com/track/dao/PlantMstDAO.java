package com.track.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PlantMstDAO extends BaseDAO {

	
	private String TABLE_NAME = "PLNTMST";
//	private DateUtils dateutils = new DateUtils();
        
	private MLogger mLogger = new MLogger();
        
	private boolean printQuery = MLoggerConstant.PLANTMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PLANTMSTDAO_PRINTPLANTMASTERLOG;
    private boolean printInfo = MLoggerConstant.PlantMstDAO_PRINTPLANTMASTERINFO;
  

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

	/*-------------------Modification History------------------------------------------
	  Bruhan,Oct 13 2014, Include LabelSetting
	
	
	*/
	public boolean insertPlnMst(Hashtable ht, String user) throws Exception {

		boolean inserted = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
//			String key1 = "USERFLD1";
			String remark = "";
			remark = StrUtils.fString((String) ht.get("USERFLD1"));
			Enumeration enum1 = ht.keys();

			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));

				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + TABLE_NAME + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			inserted = insertData(con, query);
			String plant = (String) ht.get("PLANT");
			
			plant = plant.toUpperCase();
			//String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);
			MovHisDAO mdao = new MovHisDAO();
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", "track");
			htm.put("DIRTYPE", TransactionConstants.CRAT_COMPANY);
			htm.put("RECID", "");
			htm.put("REMARKS", plant + "," + remark);
			htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
			htm.put("CRBY", (String) ht.get("CRBY"));
			htm.put("CRAT", DateUtils.getDateTime());

			if (inserted) {
                        
				CallableStatement colStmt = null;
//				int iCnt = 0;
				int index=0;
				String Plant = StrUtils.fString((String) ht.get("PLANT"));
				String Companyname=StrUtils.fString((String) ht.get("PLNTDESC"));
				String sp = "";
				try {
					
		         inserted = mdao.insertIntodefaultMovHis(htm);
                                        
				} catch (Exception e) {
					inserted = false;
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				}

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			inserted = false;

		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return inserted;
	}

	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");
			String conditon = formCondition(htCondition);
			sql.append(conditon);
			if (extCond.length() != 0) {
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			flag = updateData(con, sql.toString());

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
	
	public boolean updateContactHdr(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " +htCondition.get("PLANT")+"_CONTACTHDR ");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");
			String conditon = formCondition(htCondition);
			sql.append(conditon);
			if (extCond.length() != 0) {
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			flag = updateData(con, sql.toString());
			
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

	public String formCondition(Hashtable ht) {
		String sCondition = "";
		try {
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sCondition;
	}

	

	public boolean deletePlntMst(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + TABLE_NAME);
			sql.append(" WHERE " + formCondition(ht));

			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateData(con, sql.toString());
			CallableStatement colStmt = null;
//			int iCnt = 0;
			String Plant = StrUtils.fString((String) ht.get("PLANT"));
			String sp = "";
			sp = "ESAB_MULTI_COMP_DROP_TABLES '" + Plant + "'";
			colStmt = con.prepareCall(sp);
			colStmt.executeUpdate();//iCnt = colStmt.executeUpdate();
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

	public ArrayList getPlantMstDetails(String plant) throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			if ("".equals(plant)) {
				sQry = "SELECT  PLANT,PLNTDESC,AUTHSTAT, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(COMP_INDUSTRY,'') COMP_INDUSTRY,ISNULL(TAXBY,'') TAXBY,ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,ISNULL(ENABLE_POS,0) AS ENABLE_POS,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(APPPATH,'') APPPATH,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(PRODUCT_SHOWBY_CATAGERY,0) PRODUCT_SHOWBY_CATAGERY ,ISNULL(ISASSIGN_USERLOC,0) ISASSIGN_USERLOC ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER , ISNULL(NUMBER_OF_EBIQI,'') NUMBER_OF_EBIQI , ISNULL(NUMBER_OF_PAYMENT,'') NUMBER_OF_PAYMENT ,ISNULL(NUMBER_OF_JOURNAL,'') NUMBER_OF_JOURNAL ,ISNULL(NUMBER_OF_CONTRA,'') NUMBER_OF_CONTRA, shopify, lazada, shopee, amazon,ISNULL(ISAUTO_CONVERT_ESTPO,'') ISAUTO_CONVERT_ESTPO,ISNULL(ISAUTO_CONVERT_SOINVOICE,'') ISAUTO_CONVERT_SOINVOICE,ISNULL(ISACCESSOWNERAPP,0) ISACCESSOWNERAPP,ISNULL(ISACCESSMANGERAPP,0) ISACCESSMANGERAPP,ISNULL(ISRIDERRAPP,0) ISRIDERRAPP,ISNULL(ISACCESS_STOREAPP,0) ISACCESS_STOREAPP,ISNULL(ISACCESS_CUSTOMERAPP,0) ISACCESS_CUSTOMERAPP,ISNULL(STATUS,'') STATUS,ISNULL(REMARKS,'') REMARKS,ISNULL(USERNAME,'') USERNAME,ISNULL(PARTNER,'') PARTNER,ISNULL(ISAUTO_CONVERT_RECEIPTBILL,'0') ISAUTO_CONVERT_RECEIPTBILL,ISNULL(SHOW_STOCKQTY_ONAPP,'0') SHOW_STOCKQTY_ONAPP,ISNULL(ISMANAGEWORKFLOW,'0') ISMANAGEWORKFLOW,ISNULL(ISPRODUCTMAXQTY,'0') ISPRODUCTMAXQTY,ISNULL(ISPOSTAXINCLUSIVE,'0') ISPOSTAXINCLUSIVE,ISNULL(ISPOSRETURN_TRAN,'0') ISPOSRETURN_TRAN,ISNULL(ISPOSVOID_TRAN,'0') ISPOSVOID_TRAN,ISPEPPOL,ISNULL(PEPPOL_ID,'') PEPPOL_ID,ISNULL(SHOW_POS_SUMMARY,'0') SHOW_POS_SUMMARY,ISNULL(ISPOSSALESMAN_BYBILLPRODUCT,'0') ISPOSSALESMAN_BYBILLPRODUCT,ISNULL(ALLOWPRODUCT_TO_OTHERCOMPANY,'0') ALLOWPRODUCT_TO_OTHERCOMPANY,PEPPOL_UKEY,ISNULL(ISSHOWPOSPRICEBYOUTLET,'0') ISSHOWPOSPRICEBYOUTLET,ISNULL(ISSALESTOPURCHASE,'0') ISSALESTOPURCHASE,ISNULL(ALLOWPOSTRAN_LESSTHAN_AVBQTY,'0') ALLOWPOSTRAN_LESSTHAN_AVBQTY,ISNULL(ISSALESAPP_TAXINCLUSIVE,'0') ISSALESAPP_TAXINCLUSIVE,ISNULL(NUMBER_OF_USER,'') NUMBER_OF_USER,ISNULL(SHOWITEM_AVGCOST,'0')SHOWITEM_AVGCOST,ISNULL(ISAUTO_MINMAX_MULTIESTPO,'0')ISAUTO_MINMAX_MULTIESTPO,ISNULL(ENABLE_RESERVEQTY,'0'),ISNULL(IsDefautDrawerAmount,'0') IsDefautDrawerAmount,ISNULL(IsExpJournalEdit ,'0') AS IsExpJournalEdit,ISNULL(ALLOWCATCH_ADVANCE_SEARCH,'1') AS ALLOWCATCH_ADVANCE_SEARCH,ISNULL(SETCURRENTDATE_ADVANCE_SEARCH,'0') AS SETCURRENTDATE_ADVANCE_SEARCH,ISNULL(ISREFERENCEINVOICE,'0') AS ISREFERENCEINVOICE,ISNULL(ISSUPPLIERMANDATORY,'0') ISSUPPLIERMANDATORY,ISNULL(SETCURRENTDATE_GOODSRECEIPT,'0')SETCURRENTDATE_GOODSRECEIPT,ISNULL(SETCURRENTDATE_PICKANDISSUE,'0')SETCURRENTDATE_PICKANDISSUE,ISNULL(ISPRICE_UPDATEONLY_IN_OWNOUTLET,'0')ISPRICE_UPDATEONLY_IN_OWNOUTLET,ISNULL(ISEMPLOYEEVALIDATEPO,'0')ISEMPLOYEEVALIDATEPO,ISNULL(ISEMPLOYEEVALIDATESO,'0')ISEMPLOYEEVALIDATESO,ISNULL(ISAUTO_CONVERT_ISSUETOINVOICE,'0')ISAUTO_CONVERT_ISSUETOINVOICE,ISNULL(ISSTOCKTAKE_BYAVGCOST,'0')ISSTOCKTAKE_BYAVGCOST,ISNULL(AGING,'0')AGING   FROM PLNTMST ";
			}else {
				sQry = "SELECT  PLANT,PLNTDESC,AUTHSTAT, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(COMP_INDUSTRY,'') COMP_INDUSTRY,ISNULL(TAXBY,'') TAXBY, ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ISNULL(CRBY,'') CRBY, ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,ISNULL(ENABLE_POS,0) AS ENABLE_POS,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(APPPATH,'') APPPATH,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(PRODUCT_SHOWBY_CATAGERY,0) PRODUCT_SHOWBY_CATAGERY ,ISNULL(ISASSIGN_USERLOC,0) ISASSIGN_USERLOC ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER , ISNULL(NUMBER_OF_EBIQI,'') NUMBER_OF_EBIQI , ISNULL(NUMBER_OF_PAYMENT,'') NUMBER_OF_PAYMENT ,ISNULL(NUMBER_OF_JOURNAL,'') NUMBER_OF_JOURNAL ,ISNULL(NUMBER_OF_CONTRA,'') NUMBER_OF_CONTRA, shopify, lazada, shopee, amazon,ISNULL(ISAUTO_CONVERT_ESTPO,'') ISAUTO_CONVERT_ESTPO,ISNULL(ISAUTO_CONVERT_SOINVOICE,'') ISAUTO_CONVERT_SOINVOICE,ISNULL(ISACCESSOWNERAPP,0) ISACCESSOWNERAPP,ISNULL(ISACCESSMANGERAPP,0) ISACCESSMANGERAPP,ISNULL(ISRIDERRAPP,0) ISRIDERRAPP,ISNULL(ISACCESS_STOREAPP,0) ISACCESS_STOREAPP,ISNULL(ISACCESS_CUSTOMERAPP,0) ISACCESS_CUSTOMERAPP,ISNULL(STATUS,'') STATUS,ISNULL(REMARKS,'') REMARKS,ISNULL(USERNAME,'') USERNAME,ISNULL(PARTNER,'') PARTNER,ISNULL(ISAUTO_CONVERT_RECEIPTBILL,'0') ISAUTO_CONVERT_RECEIPTBILL,ISNULL(SHOW_STOCKQTY_ONAPP,'0') SHOW_STOCKQTY_ONAPP,ISNULL(ISMANAGEWORKFLOW,'0') ISMANAGEWORKFLOW,ISNULL(ISPRODUCTMAXQTY,'0') ISPRODUCTMAXQTY,ISNULL(ISPOSTAXINCLUSIVE,'0') ISPOSTAXINCLUSIVE,ISNULL(ISPOSRETURN_TRAN,'0') ISPOSRETURN_TRAN,ISNULL(ISPOSVOID_TRAN,'0') ISPOSVOID_TRAN,ISPEPPOL,ISNULL(PEPPOL_ID,'') PEPPOL_ID,ISNULL(SHOW_POS_SUMMARY,'0') SHOW_POS_SUMMARY,ISNULL(ISPOSSALESMAN_BYBILLPRODUCT,'0') ISPOSSALESMAN_BYBILLPRODUCT,ISNULL(ALLOWPRODUCT_TO_OTHERCOMPANY,'0') ALLOWPRODUCT_TO_OTHERCOMPANY,PEPPOL_UKEY,ISNULL(ISSHOWPOSPRICEBYOUTLET,'0') ISSHOWPOSPRICEBYOUTLET,ISNULL(ISSALESTOPURCHASE,'0') ISSALESTOPURCHASE,ISNULL(ALLOWPOSTRAN_LESSTHAN_AVBQTY,'0') ALLOWPOSTRAN_LESSTHAN_AVBQTY,ISNULL(ISSALESAPP_TAXINCLUSIVE,'0') ISSALESAPP_TAXINCLUSIVE,ISNULL(NUMBER_OF_USER,'') NUMBER_OF_USER,ISNULL(SHOWITEM_AVGCOST,'0')SHOWITEM_AVGCOST,ISNULL(ISAUTO_MINMAX_MULTIESTPO,'0')ISAUTO_MINMAX_MULTIESTPO,ISNULL(ENABLE_RESERVEQTY,'0') ENABLE_RESERVEQTY,ISNULL(IsDefautDrawerAmount,'0') IsDefautDrawerAmount,ISNULL(IsExpJournalEdit ,'0') AS IsExpJournalEdit,ISNULL(ALLOWCATCH_ADVANCE_SEARCH,'1') AS ALLOWCATCH_ADVANCE_SEARCH,ISNULL(SETCURRENTDATE_ADVANCE_SEARCH,'0') AS SETCURRENTDATE_ADVANCE_SEARCH,ISNULL(ISREFERENCEINVOICE,'0') AS ISREFERENCEINVOICE,ISNULL(ISSUPPLIERMANDATORY,'0') ISSUPPLIERMANDATORY,ISNULL(SETCURRENTDATE_GOODSRECEIPT,'0')SETCURRENTDATE_GOODSRECEIPT,ISNULL(SETCURRENTDATE_PICKANDISSUE,'0')SETCURRENTDATE_PICKANDISSUE,ISNULL(ISPRICE_UPDATEONLY_IN_OWNOUTLET,'0')ISPRICE_UPDATEONLY_IN_OWNOUTLET,ISNULL(ISEMPLOYEEVALIDATEPO,'0')ISEMPLOYEEVALIDATEPO,ISNULL(ISEMPLOYEEVALIDATESO,'0')ISEMPLOYEEVALIDATESO,ISNULL(ISAUTO_CONVERT_ISSUETOINVOICE,'0')ISAUTO_CONVERT_ISSUETOINVOICE,ISNULL(ISSTOCKTAKE_BYAVGCOST,'0')ISSTOCKTAKE_BYAVGCOST,ISNULL(AGING,'0')AGING  FROM PLNTMST WHERE  PLANT = '"
						+ plant + "'";
			}
			
			
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
	
	public ArrayList getPlantMstDetailForCrm(String plant) throws Exception {
		
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			if ("".equals(plant)) {
				sQry = "SELECT  PLANT,PLNTDESC,AUTHSTAT, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(COMP_INDUSTRY,'') COMP_INDUSTRY,ISNULL(TAXBY,'') TAXBY,ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,ISNULL(ENABLE_POS,0) AS ENABLE_POS,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(APPPATH,'') APPPATH,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(PRODUCT_SHOWBY_CATAGERY,0) PRODUCT_SHOWBY_CATAGERY ,ISNULL(ISASSIGN_USERLOC,0) ISASSIGN_USERLOC ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER , ISNULL(NUMBER_OF_EBIQI,'') NUMBER_OF_EBIQI , ISNULL(NUMBER_OF_PAYMENT,'') NUMBER_OF_PAYMENT ,ISNULL(NUMBER_OF_JOURNAL,'') NUMBER_OF_JOURNAL ,ISNULL(NUMBER_OF_CONTRA,'') NUMBER_OF_CONTRA, shopify, lazada, shopee, amazon,ISNULL(ISAUTO_CONVERT_ESTPO,'') ISAUTO_CONVERT_ESTPO,ISNULL(ISAUTO_CONVERT_SOINVOICE,'') ISAUTO_CONVERT_SOINVOICE,ISNULL(ISACCESSOWNERAPP,0) ISACCESSOWNERAPP,ISNULL(ISACCESSMANGERAPP,0) ISACCESSMANGERAPP,ISNULL(ISRIDERRAPP,0) ISRIDERRAPP,ISNULL(ISACCESS_STOREAPP,0) ISACCESS_STOREAPP,ISNULL(ISACCESS_CUSTOMERAPP,0) ISACCESS_CUSTOMERAPP,ISNULL(STATUS,'') STATUS,ISNULL(REMARKS,'') REMARKS,ISNULL(USERNAME,'') USERNAME,ISNULL(PARTNER,'') PARTNER FROM PLNTMST WHERE PLANT!='C7282381867S2T' and PLANT!='track'";
			}else {
				sQry = "SELECT  PLANT,PLNTDESC,AUTHSTAT, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(COMP_INDUSTRY,'') COMP_INDUSTRY,ISNULL(TAXBY,'') TAXBY, ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ISNULL(CRBY,'') CRBY, ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,ISNULL(ENABLE_POS,0) AS ENABLE_POS,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(APPPATH,'') APPPATH,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(PRODUCT_SHOWBY_CATAGERY,0) PRODUCT_SHOWBY_CATAGERY ,ISNULL(ISASSIGN_USERLOC,0) ISASSIGN_USERLOC ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER , ISNULL(NUMBER_OF_EBIQI,'') NUMBER_OF_EBIQI , ISNULL(NUMBER_OF_PAYMENT,'') NUMBER_OF_PAYMENT ,ISNULL(NUMBER_OF_JOURNAL,'') NUMBER_OF_JOURNAL ,ISNULL(NUMBER_OF_CONTRA,'') NUMBER_OF_CONTRA, shopify, lazada, shopee, amazon,ISNULL(ISAUTO_CONVERT_ESTPO,'') ISAUTO_CONVERT_ESTPO,ISNULL(ISAUTO_CONVERT_SOINVOICE,'') ISAUTO_CONVERT_SOINVOICE,ISNULL(ISACCESSOWNERAPP,0) ISACCESSOWNERAPP,ISNULL(ISACCESSMANGERAPP,0) ISACCESSMANGERAPP,ISNULL(ISRIDERRAPP,0) ISRIDERRAPP,ISNULL(ISACCESS_STOREAPP,0) ISACCESS_STOREAPP,ISNULL(ISACCESS_CUSTOMERAPP,0) ISACCESS_CUSTOMERAPP,ISNULL(STATUS,'') STATUS,ISNULL(REMARKS,'') REMARK,ISNULL(USERNAME,'') USERNAME,ISNULL(PARTNER,'') PARTNER FROM PLNTMST WHERE  PLANT = '"
						+ plant + "'";
			}
			
			
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
	
	public ArrayList getContactHdr(String plant) throws Exception {
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			if ("".equals(plant)) {
				sQry = "SELECT  PLANT,NAME,EMAIL,COMPANYCONTACT,JOB,PHONENUMBER,LEADSTATUS,ID,ISNULL(COUNTRY,'') COUNTRY,ISNULL(INDUSTRY,'') INDUSTRY,ISNULL(MOBILENO,'') MOBILENO,ISNULL(SALESPROBABILITY,'') SALESPROBABILITY,ISNULL((SELECT TOP 1 LIFECYCLESTAGE FROM "+plant+"_CONTACTDET D WHERE D.CONTACTHDRID=H.ID ORDER BY ID DESC),'') LIFECYCLESTAGE,ISNULL((SELECT TOP 1 NOTE FROM "+plant+"_CONTACTDET D WHERE D.CONTACTHDRID=H.ID ORDER BY ID DESC),'') NOTE FROM "+plant+"_CONTACTHDR H ";
			}else {
				sQry = "SELECT  PLANT,NAME,EMAIL,COMPANYCONTACT,JOB,PHONENUMBER,LEADSTATUS,ID,ISNULL(COUNTRY,'') COUNTRY,ISNULL(INDUSTRY,'') INDUSTRY,ISNULL(MOBILENO,'') MOBILENO,ISNULL(SALESPROBABILITY,'') SALESPROBABILITY,ISNULL((SELECT TOP 1 LIFECYCLESTAGE FROM "+plant+"_CONTACTDET D WHERE D.CONTACTHDRID=H.ID ORDER BY ID DESC),'') LIFECYCLESTAGE,ISNULL((SELECT TOP 1 NOTE FROM "+plant+"_CONTACTDET D WHERE D.CONTACTHDRID=H.ID ORDER BY ID DESC),'') NOTE FROM "+plant+"_CONTACTHDR H WHERE  PLANT = '" + plant + "'";
			}
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
	
	public ArrayList getsalesorderdeliverysummary(String plant,String FROM_DATE,String TO_DATE,String dono,String invoice,String item,String status,String shipped,String deliverystatus) throws Exception {
		String sCondition = "",dtCondStr="";
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			dtCondStr ="and ISNULL(inhdr.INVOICE_DATE,'')<>'' AND CAST((SUBSTRING(inhdr.INVOICE_DATE, 7, 4) + '-' + SUBSTRING(inhdr.INVOICE_DATE, 4, 2) + '-' + SUBSTRING(inhdr.INVOICE_DATE, 1, 2)) AS date)";
			   if (FROM_DATE.length() > 0) {
	              	sCondition = sCondition + dtCondStr + "  >= '" 
	  						+ FROM_DATE
	  						+ "'  ";
	  				if (TO_DATE.length() > 0) {
	  					sCondition = sCondition +dtCondStr+ " <= '" 
	  					+ TO_DATE
	  					+ "'  ";
	  				}
	  			  } else {
	  				if (TO_DATE.length() > 0) {
	  					sCondition = sCondition +dtCondStr+ " <= '" 
	  					+ TO_DATE
	  					+ "'  ";
	  				}
	  		     	}   
			if (dono != null && dono!="") {
				sCondition = sCondition +" AND dohdr.DONO  like '%"+dono+"%'";
						
			}
			if (invoice != null && invoice!="") {
				sCondition = sCondition +" AND inhdr.INVOICE  like '%"+invoice+"%'";
				
			}
			if (item != null && item!="") {
				sCondition = sCondition +" AND indet.ITEM  like '%"+item+"%'";
				
			}
			if (status != null && status!="") {
				sCondition = sCondition +" AND dohdr.STATUS  like '%"+status+"%'";
				
			}
			if (shipped != null && shipped!="") {
				sCondition = sCondition +" AND dohdr.PickStaus  like '%"+shipped+"%'";
				
			}
			if (deliverystatus != null && deliverystatus!="") {
				sCondition = sCondition +" AND inhdr.BILL_STATUS  like '%"+deliverystatus+"%'";
				
			}
			String sQry;
//				sQry = "SELECT dohdr.DONO,inhdr.INVOICE,dodet.ITEM,dodet.QtyPick,dohdr.STATUS,dohdr.PickStaus,inhdr.BILL_STATUS from "+plant+"_DOHDR dohdr inner join "+plant+"_FININVOICEHDR inhdr on dohdr.DONO = inhdr.DONO left join "+plant+"_FININVOICEDET indet on inhdr.ID = indet.INVOICEHDRID left join "+plant+"_DODET dodet on dohdr.DONO = dodet.DONO   ";
				sQry = "SELECT dohdr.DONO,inhdr.INVOICE,indet.ITEM,indet.Qty,dohdr.STATUS,dohdr.ORDER_STATUS,dohdr.PickStaus,inhdr.BILL_STATUS,ISNULL(DELIVERY_PERSON,'') DELIVERY_PERSON,ISNULL(DELIVERY_DATE,'') DELIVERY_DATE,"
					+ "ISNULL((SELECT TOP 1 ISNULL(SIGNATURENAME,'') FROM "+plant+"_SHIPHIS S WHERE S.DONO=dohdr.DONO AND S.INVOICENO=inhdr.GINO AND S.ITEM=indet.ITEM AND S.DOLNO=indet.LNNO),'') AS REV_PERSON,dohdr.CustName "
					+ " from "+plant+"_FININVOICEDET indet join "+plant+"_FININVOICEHDR inhdr on inhdr.ID = indet.INVOICEHDRID join "+plant+"_DOHDR dohdr on dohdr.DONO = inhdr.DONO   where dohdr.STATUS <>'O' "+ sCondition;
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
	
	public ArrayList getsalesorderdeliveryShippingsummary(String plant,String FROM_DATE,String TO_DATE,String dono,String invoice,String item,String status,String shipped,String intransit,String deliverystatus) throws Exception {
		String sCondition = "",dtCondStr="";
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			dtCondStr ="and ISNULL(ship.ISSUEDATE,'')<>'' AND CAST((SUBSTRING(ship.ISSUEDATE, 7, 4) + '-' + SUBSTRING(ship.ISSUEDATE, 4, 2) + '-' + SUBSTRING(ship.ISSUEDATE, 1, 2)) AS date)";
			if (FROM_DATE.length() > 0) {
				sCondition = sCondition + dtCondStr + "  >= '" 
						+ FROM_DATE
						+ "'  ";
				if (TO_DATE.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" 
							+ TO_DATE
							+ "'  ";
				}
			} else {
				if (TO_DATE.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" 
							+ TO_DATE
							+ "'  ";
				}
			}   
			if (dono != null && dono!="") {
				sCondition = sCondition +" AND dohdr.DONO  like '%"+dono+"%'";
				
			}
			if (invoice != null && invoice!="") {
				sCondition = sCondition +" AND inv.INVOICE in(ISNULL((SELECT TOP 1 ISNULL(DN.INVOICE,'') FROM "+plant+"_FININVOICEHDR DN join "+plant+"_SHIPHIS DT on DN.DONO=DT.DONO where DN.DONO= ship.DONO and dn.INVOICE='"+invoice+"'),''))";
//				sCondition = sCondition +" AND ship.INVOICENO  like '%"+invoice+"%'";
				
			}
			if (item != null && item!="") {
				sCondition = sCondition +" AND ship.ITEM  like '%"+item+"%'";
				
			}
			if (status != null && status!="") {
				sCondition = sCondition +" AND dohdr.STATUS  like '%"+status+"%'";
				
			}
			if (shipped != null && shipped!="") {
				sCondition = sCondition +" AND dohdr.PickStaus  like '%"+shipped+"%'";
				
			}
			if (deliverystatus != null && deliverystatus!="") {
				sCondition = sCondition +"AND dohdr.DONO in(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= dohdr.DONO AND DN.GINO=GINO and DT.ITEM = ship.ITEM and DT.LNNO = ship.DOLNO and d.DELIVERY_STATUS='"+deliverystatus+"'),'') ) ";
//				sCondition = sCondition +" AND inhdr.BILL_STATUS  like '%"+deliverystatus+"%'";
				
			}
			if (intransit != null && intransit!="") {
				sCondition = sCondition +"AND dohdr.DONO in(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= dohdr.DONO AND DN.GINO=GINO and DT.ITEM = ship.ITEM and DT.LNNO = ship.DOLNO and d.INTRANSIT_STATUS='"+intransit+"'),'') ) ";
				 
			}
			String sQry;
			sQry = "SELECT dohdr.DONO,ship.ITEM,ship.PICKQTY as Qty,dohdr.STATUS,dohdr.ORDER_STATUS,dohdr.PickStaus,ship.ISSUEDATE,"
					+ "ISNULL((SELECT TOP 1 ISNULL(D.SHIPPING_STATUS,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = d.DNPLID where DN.DONO=dohdr.DONO AND DN.GINO=ship.INVOICENO and DT.ITEM = ship.ITEM and DT.LNNO = ship.DOLNO),'')as SHIPPING_STATUS, "
					+ "ISNULL((SELECT TOP 1 ISNULL(D.SHIPPING_DATE,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = d.DNPLID where DN.DONO=dohdr.DONO AND DN.GINO=ship.INVOICENO and DT.ITEM = ship.ITEM and DT.LNNO = ship.DOLNO),'')as SHIPPING_DATE, "
					+ "ISNULL((SELECT TOP 1 ISNULL(D.INTRANSIT_STATUS,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = d.DNPLID where DN.DONO=dohdr.DONO AND DN.GINO=ship.INVOICENO and DT.ITEM = ship.ITEM and DT.LNNO = ship.DOLNO),'')as INTRANSIT_STATUS, "
					+ "ISNULL((SELECT TOP 1 ISNULL(D.INTRANSIT_DATE,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = d.DNPLID where DN.DONO=dohdr.DONO AND DN.GINO=ship.INVOICENO and DT.ITEM = ship.ITEM and DT.LNNO = ship.DOLNO),'')as INTRANSIT_DATE, "
					+ "ISNULL((SELECT TOP 1 ISNULL(D.DELIVERY_STATUS,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = d.DNPLID where DN.DONO=dohdr.DONO AND DN.GINO=ship.INVOICENO and DT.ITEM = ship.ITEM and DT.LNNO = ship.DOLNO),'')as DELIVERY_STATUS, "
					+ "ISNULL((SELECT TOP 1 ISNULL(D.DELIVERY_DATE,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = d.DNPLID where DN.DONO=dohdr.DONO AND DN.GINO=ship.INVOICENO and DT.ITEM = ship.ITEM and DT.LNNO = ship.DOLNO),'')as DELIVERY_DATE, "
					+ "ISNULL((SELECT TOP 1 ISNULL(DELIVERY_PERSON,'') FROM "+plant+"_FININVOICEHDR hdr WHERE hdr.DONO=dohdr.DONO AND hdr.GINO=ship.INVOICENO ),'') AS DELIVERY_PERSON,  "
					+ "ISNULL((SELECT TOP 1 ISNULL(INVOICE,'') FROM "+plant+"_FININVOICEHDR hdr WHERE hdr.DONO=dohdr.DONO AND hdr.GINO=ship.INVOICENO ),'') AS INVOICE, "
					+ "ISNULL(ship.SIGNATURENAME,'') as REV_PERSON,dohdr.CustName"
					+ " from "+plant+"_SHIPHIS ship join "+plant+"_DOHDR dohdr on dohdr.DONO = ship.DONO left join "+plant+"_FININVOICEHDR inv on inv.GINO=ship.INVOICENO where dohdr.STATUS <>'O' "+ sCondition;
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
	
	public ArrayList getContactDet(String plant) throws Exception {
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			if ("".equals(plant)) {
				sQry = "SELECT  PLANT,LEADDATE,LIFECYCLESTAGE,LEADSTATUS,NOTE,ID FROM "+plant+"_CONTACTDET ";
			}else {
				sQry = "SELECT  PLANT,LEADDATE,LIFECYCLESTAGE,LEADSTATUS,NOTE,ID FROM "+plant+"_CONTACTDET WHERE  PLANT = '" + plant + "'";
			}
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
	
	public ArrayList getContactHdr(String plant,String ID) throws Exception {
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			if ("".equals(plant)) {
				sQry = "SELECT  PLANT,NAME,EMAIL,COMPANYCONTACT,JOB,PHONENUMBER,LEADSTATUS,ID,ISNULL(MOBILENO,'')MOBILENO,ISNULL(HOWWEKNOW,'')HOWWEKNOW,ISNULL(USER_NAME,'')USER_NAME,ISNULL(COUNTRY,'')COUNTRY,ISNULL(INDUSTRY,'')INDUSTRY,ISNULL(WEBSITE,'')WEBSITE,ISNULL(FACEBOOK,'')FACEBOOK,ISNULL(LINKED,'')LINKED,ISNULL(SALESPROBABILITY,'')SALESPROBABILITY FROM "+plant+"_CONTACTHDR ";
			}else {
				sQry = "SELECT  PLANT,NAME,EMAIL,COMPANYCONTACT,JOB,PHONENUMBER,LEADSTATUS,ID,ISNULL(MOBILENO,'')MOBILENO,ISNULL(HOWWEKNOW,'')HOWWEKNOW,ISNULL(USER_NAME,'')USER_NAME,ISNULL(COUNTRY,'')COUNTRY,ISNULL(INDUSTRY,'')INDUSTRY,ISNULL(WEBSITE,'')WEBSITE,ISNULL(FACEBOOK,'')FACEBOOK,ISNULL(LINKED,'')LINKED,ISNULL(SALESPROBABILITY,'')SALESPROBABILITY FROM "+plant+"_CONTACTHDR WHERE  PLANT = '" + plant + "'AND ID = '" + ID + "'";
			}
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
	
	public ArrayList getContactDet(String plant,String ID) throws Exception {
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			if ("".equals(plant)) {
				sQry = "SELECT  PLANT,LEADDATE,LIFECYCLESTAGE,LEADSTATUS,NOTE,ID FROM "+plant+"_CONTACTDET ";
			}else {
				sQry = "SELECT  PLANT,LEADDATE,LIFECYCLESTAGE,LEADSTATUS,NOTE,ID FROM "+plant+"_CONTACTDET WHERE  PLANT = '" + plant + "' AND CONTACTHDRID = '" + ID + "'";
			}
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
	
	public boolean isExistsContactName(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM  " + ht.get("PLANT")+"_CONTACTHDR");
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
	
	public boolean insertIntoContactHdr(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO  " +ht.get("PLANT")+"_CONTACTHDR" + "("
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
	
	public int addContactHdr(Hashtable ht, String plant)throws Exception {
		boolean flag = false;
		int contacthdrID = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "";
		try {
			 /*Instantiate the list*/
		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}
			query = "INSERT INTO ["+plant+"_CONTACTHDR] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			contacthdrID = execute_NonSelectQueryGetLastInsert(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return contacthdrID;
	}
	
	public boolean insertIntoContactDet(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " +ht.get("PLANT")+"_CONTACTDET" + "("
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
	
	public ArrayList getPlantMstDetailsForMaintenanceExpDate(String plant) throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT  PLANT,PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,WEBSITE,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY FROM PLNTMST WHERE  PLANT = '"
					+ plant + "'";
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

	public ArrayList selectForReport(String query, Hashtable ht)
			throws Exception {
//		boolean flag = false;
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
        
        
    public ArrayList getCompanyDetails(String plant) throws Exception {

            Connection con = null;
            ArrayList al = new ArrayList();
            try {
                    con = DbBean.getConnection();

                    String sQry = "SELECT  PLANT,PLNTDESC, SALES_CHARGE_BY  , CASE WHEN SALES_CHARGE_BY ='FLATRATE' THEN CAST(SALES_FR_DOLLARS AS VARCHAR) +'.'+CAST(SALES_FR_CENTS AS VARCHAR) " + 
                    "  ELSE  CAST(SALES_PERCENT AS VARCHAR) END AS SALESRATE ,CAST( ENQUIRY_FR_DOLLARS AS VARCHAR)+'.'+ CAST(ENQUIRY_FR_CENTS AS VARCHAR)  AS  EFLATRATE" +
                    //"  FROM PLNTMST WHERE  PLANT like '" + plant + "%' and PLANT<>'track' ";
                    "  FROM PLNTMST WHERE  PLANT = '" + plant + "' and PLANT<>'track' ";
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
    
    public Map selectRow(String query, Hashtable ht) throws Exception {
            Map map = new HashMap();

            java.sql.Connection con = null;
            try {

                    con = com.track.gates.DbBean.getConnection();
                    StringBuffer sql = new StringBuffer(" SELECT " + query + " from PLNTMST ");                    
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
public boolean insertSignatureCapture(Hashtable ht) throws Exception {
		boolean insertRecvHis = false;
		java.sql.Connection con = null;
		try {

			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value.toUpperCase() + "',";
			}
			String sql = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "SIGNCAPTURE" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, sql);
			insertRecvHis = insertData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertRecvHis;
	}
public int getSignatureCount(Hashtable ht, String extCond) throws Exception {

//	boolean flag = false;
	java.sql.Connection con = null;int count=0;
	try {
		String TABLE_NAME="SIGNCAPTURE";
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append("COUNT(*) ");
		sql.append(" ");
		sql.append(" FROM " + "["+ht.get(IDBConstants.PLANT)+"_"+TABLE_NAME+"]");
		sql.append(" WHERE  " + formCondition(ht));;
		if (extCond.length() > 0)
			sql.append(" and " + extCond);

		this.mLogger.query(this.printLog, sql.toString());

		count = countRows(con, sql.toString());
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}

	return count;

}

public boolean isInventoryModuleEnabled(String plant) throws Exception{
//	boolean inventoryModuleEnabled = false;
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer("select enable_inventory " );
	sql.append(" from [plntmst] WHERE plant = '" + plant + "'");

	try {
		con = com.track.gates.DbBean.getConnection();
		DbBean.writeError("PlantMstDAO", "isInventoryModuleEnabled()",  sql.toString());
		this.mLogger.query(this.printLog, sql.toString());
		ArrayList alData = selectData(con, sql.toString());
		return Integer.parseInt(((Map)alData.get(0)).get("enable_inventory").toString()) == 1;
	} catch (Exception e) {
		DbBean.writeError("PlantMstDAO", "isInventoryModuleEnabled()", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
}

public boolean isAccountingModuleEnabled(String plant) throws Exception{
//	boolean inventoryModuleEnabled = false;
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer("select enable_accounting " );
	sql.append(" from [plntmst] WHERE plant = '" + plant + "'");

	try {
		con = com.track.gates.DbBean.getConnection();
		DbBean.writeError("PlantMstDAO", "isAccountingModuleEnabled()",  sql.toString());
		this.mLogger.query(this.printLog, sql.toString());
		ArrayList alData = selectData(con, sql.toString());
		return Integer.parseInt(((Map)alData.get(0)).get("enable_accounting").toString()) == 1;
	} catch (Exception e) {
		DbBean.writeError("PlantMstDAO", "isAccountingModuleEnabled()", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
}

public boolean isPayrollModuleEnabled(String plant) throws Exception{
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer("select enable_payroll " );
	sql.append(" from [plntmst] WHERE plant = '" + plant + "'");
	
	try {
		con = com.track.gates.DbBean.getConnection();
		DbBean.writeError("PlantMstDAO", "isPayrollModuleEnabled()",  sql.toString());
		this.mLogger.query(this.printLog, sql.toString());
		ArrayList alData = selectData(con, sql.toString());
		return Integer.parseInt(((Map)alData.get(0)).get("enable_payroll").toString()) == 1;
	} catch (Exception e) {
		DbBean.writeError("PlantMstDAO", "isPayrollModuleEnabled()", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
}

public ArrayList validateUser(String userid,String pwd)
		throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	PreparedStatement ps = null;
	List<String> args = null;
	StringBuffer sql = new StringBuffer("select distinct(upper(a.dept)) as plant,upper(b.plntdesc) plantname " );
	sql.append(" from [user_info] a,[plntmst] b where a.dept=b.plant and a.[user_id]= ? and a.PASSWORD = ? and b.authstat=1");
	
	

	try {
		args = new ArrayList<String>();
		con = com.track.gates.DbBean.getConnection();
		DbBean.writeError("PlantMstDAO", "validateUser()",  sql.toString());
		System.out.println(sql.toString());
		
		ps = con.prepareStatement(sql.toString());  
		args.add(userid);
		args.add(pwd);
		
		alData = (ArrayList) selectData(ps, args);
	} catch (Exception e) {
		DbBean.writeError("PlantMstDAO", "validateUser()", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	
	return alData;
}

public ArrayList validateUser(String userid)
		throws Exception {
//	boolean flag = false;
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer("select distinct(upper(a.dept)) as plant,upper(b.plntdesc) plantname " );
	sql.append(" from [user_info] a,[plntmst] b where a.dept=b.plant and a.[user_id]='"+userid+"'");
//	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();
		DbBean.writeError("PlantMstDAO", "validateUser()",  sql.toString());
		System.out.println(sql.toString());
		alData = selectData(con, sql.toString());

	} catch (Exception e) {
		DbBean.writeError("PlantMstDAO", "validateUser()", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	
	return alData;
}

public ArrayList<Map<String, String>> getTaxByList(String Plant) throws Exception {

    java.sql.Connection con = null;
    ArrayList<Map<String, String>> al = new ArrayList<>();
    try {
            con = com.track.gates.DbBean.getConnection();
           
            StringBuffer sQry = new StringBuffer("SELECT ISNULL(TAXBY,'')TAXBY from "
                            + "PLNTMST WHERE PLANT='" + Plant +"'");
          
            this.mLogger.query(this.printQuery, sQry.toString());
            System.out.println(sQry);
            al =  selectData(con, sQry.toString());

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

public ArrayList<Map<String, String>> getTaxByLabelList(String Plant) throws Exception {

    java.sql.Connection con = null;
    ArrayList<Map<String, String>> al = new ArrayList<>();
    try {
            con = com.track.gates.DbBean.getConnection();
           
            StringBuffer sQry = new StringBuffer("SELECT ISNULL(TAXBYLABEL,'')TAXBYLABEL from "
                            + "PLNTMST WHERE PLANT='" + Plant +"'");
          
            this.mLogger.query(this.printQuery, sQry.toString());
            System.out.println(sQry);
            al =  selectData(con, sQry.toString());

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
public ArrayList<Map<String, String>> getTaxByLabeOrderList(String Plant) throws Exception {

    java.sql.Connection con = null;
    ArrayList<Map<String, String>> al = new ArrayList();
    try {
            con = com.track.gates.DbBean.getConnection();
           
            StringBuffer sQry = new StringBuffer("SELECT ISNULL(TAXBYLABELORDERMANAGEMENT ,'')TAXBYLABELORDERMANAGEMENT  from "
                            + "PLNTMST WHERE PLANT='" + Plant +"'");
          
            this.mLogger.query(this.printQuery, sQry.toString());
            System.out.println(sQry);
            al =  selectData(con, sQry.toString());

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
public String getTaxBy(String plant) throws Exception {
    java.sql.Connection con = null;
    String taxby = "";
    try {
            
    con = DbBean.getConnection();
    StringBuffer SqlQuery = new StringBuffer(" select isnull(taxby,'') taxby from [plntmst] where plant='"+plant+"'") ;
     
    System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());

       taxby = (String) m.get("taxby");

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return taxby;
}

public String getTaxByLabel(String plant) throws Exception {
    java.sql.Connection con = null;
    String taxbylabel = "";
    try {
            
    con = DbBean.getConnection();
    StringBuffer SqlQuery = new StringBuffer(" select isnull(taxbylabel,'') taxbylabel from [plntmst] where plant='"+plant+"'") ;
     
    System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());

       taxbylabel = (String) m.get("taxbylabel");

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return taxbylabel;
}

	public ArrayList getUserPassword(String userid) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("select a.PASSWORD,a.DEPT ");
		sql.append(" from [user_info] a where a.[user_id]='" + userid
				+ "'");
//		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
			DbBean.writeError("PlantMstDAO", "getUserPassword()", sql.toString());
			System.out.println(sql.toString());
			alData = selectData(con, sql.toString());

		} catch (Exception e) {
			DbBean.writeError("PlantMstDAO", "getUserPassword()", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return alData;
	}
	public String getBaseCurrency(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String basecurrency = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(base_currency,'') basecurrency from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        basecurrency = (String) m.get("basecurrency");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return basecurrency;
	}
	public String getNumberOfDecimal(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String numberOfDecimal = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(NUMBEROFDECIMAL,'') NUMBEROFDECIMAL from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        numberOfDecimal = (String) m.get("NUMBEROFDECIMAL");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return numberOfDecimal;
	}
	
	public String getispayroll (String plant) throws Exception {
	    java.sql.Connection con = null;
	    String payroll = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ENABLE_PAYROLL,'0')ENABLE_PAYROLL  from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        payroll = (String) m.get("ENABLE_PAYROLL");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return payroll;
	}
	public String getispos (String plant) throws Exception {
	    java.sql.Connection con = null;
	    String pos = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ENABLE_POS,'0')ENABLE_POS  from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        pos = (String) m.get("ENABLE_POS");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return pos;
	}
	public String getisPeppol (String plant) throws Exception {
	    java.sql.Connection con = null;
	    String pos = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISPEPPOL,'0')ISPEPPOL  from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        pos = (String) m.get("ISPEPPOL");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return pos;
	}
	public String getshopify (String plant) throws Exception {
		java.sql.Connection con = null;
		String pos = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(Shopify,'0')Shopify  from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			pos = (String) m.get("Shopify");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return pos;
	}
	public String getshopee(String plant) throws Exception {
		java.sql.Connection con = null;
		String pos = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(Shopee,'0')Shopee  from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			pos = (String) m.get("Shopee");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return pos;
	}
	public String getistaxregistred (String plant) throws Exception {
		java.sql.Connection con = null;
		String pos = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(ISTAXREGISTRED,'0')ISTAXREGISTRED  from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			pos = (String) m.get("ISTAXREGISTRED");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return pos;
	}
	public String getSETCURRENTDATE_GOODSRECEIPT (String plant) throws Exception {
		java.sql.Connection con = null;
		String pos = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(SETCURRENTDATE_GOODSRECEIPT,'0')SETCURRENTDATE_GOODSRECEIPT  from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			pos = (String) m.get("SETCURRENTDATE_GOODSRECEIPT");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return pos;
	}
	public String getSETCURRENTDATE_PICKANDISSUE (String plant) throws Exception {
		java.sql.Connection con = null;
		String pos = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(SETCURRENTDATE_PICKANDISSUE,'0')SETCURRENTDATE_PICKANDISSUE  from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			pos = (String) m.get("SETCURRENTDATE_PICKANDISSUE");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return pos;
	}
	
	public String getFiscalYear(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String fiscalyear = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(FISCAL_YEAR,'') FISCALYEAR from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        fiscalyear = (String) m.get("FISCALYEAR");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return fiscalyear;
	}
	public List getCompAttachByHrdId(String plant,String Id) throws Exception {

		Connection con = null;
		List<Map<String, String>> al = new ArrayList<>();
		try {
			con = DbBean.getConnection();
			String sQry;
			String sContion="";
				sQry = "SELECT * FROM PLANTMST_ATTACHMENTS WHERE PLANT = '"+plant+"'" ;
			
				if(!Id.equalsIgnoreCase(""))
					sContion= "AND ID = '"+ Id + "'";
			
			this.mLogger.query(this.printQuery, (sQry+sContion));

			al = selectData(con, sQry+sContion);

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

	public boolean deleteCompAttachByPrimId(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM PLANTMST_ATTACHMENTS");
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
	
	public String getworkmandsys(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String mandays = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        mandays = (String) m.get("EMPLOYEEWORKINGMANDAYSBY");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return mandays;
	}
	
	public int getCompanyCount(String userid) throws Exception {
//		boolean flag = false;
		java.sql.Connection con = null;int count=0;
		try {
//			String TABLE_NAME="SIGNCAPTURE";
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer("select count(*) " );
			sql.append(" from [user_info] a,[plntmst] b where a.dept=b.plant and a.[user_id]= '"+userid+"' and b.authstat=1");
			this.mLogger.query(this.printLog, sql.toString());
			count = countRows(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return count;
	}
	
	public ArrayList getPlantMstlistdropdownParentChild(String plant,String query) throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;			
			sQry = "WITH A1 AS (select TOP 1 PARENT_PLANT as PLANT from PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"' or CHILD_PLANT='"+plant+"'"
					+ " UNION ALL "
					+ "select CHILD_PLANT as PLANT from PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT in ("
					+ "select PARENT_PLANT from PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"' or CHILD_PLANT='"+plant+"') ) SELECT C.*,isnull(PLNTDESC,'') PLNTDESC FROM A1 C JOIN PLNTMST P ON C.PLANT=P.PLANT WHERE (P.PLANT LIKE '%"+query+"%' OR PLNTDESC LIKE '%"+query+"%')";
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
	public ArrayList getPlantMstlistdropdownParent(String plant) throws Exception {
		
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			sQry = "SELECT PLANT FROM PLNTMST WHERE PLANT NOT IN (SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET GROUP BY CHILD_PLANT) AND PLANT LIKE '%"+plant+"%'";
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
	
	public ArrayList getPlantMstlistdropdownChild(String plant) throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			sQry = "SELECT PLANT FROM PLNTMST WHERE PLANT NOT IN (SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET GROUP BY PARENT_PLANT) AND PLANT LIKE '%"+plant+"%'";
	
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
	
	public String getcmpyname(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String cpyname = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(PLNTDESC,'') PLNTDESC from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        cpyname = (String) m.get("PLNTDESC");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return cpyname;
	}
	
	public String getbasecurrency(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String cpyname = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(BASE_CURRENCY,'') BASE_CURRENCY from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        cpyname = (String) m.get("BASE_CURRENCY");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return cpyname;
	}
	
	public String getCOMP_INDUSTRY(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String COMP_INDUSTRY = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(COMP_INDUSTRY,'General') COMP_INDUSTRY from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());
	        COMP_INDUSTRY = (String) m.get("COMP_INDUSTRY");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return COMP_INDUSTRY;
	}
	
	public String getProducttoOtherCompany(String plant) throws Exception {
		java.sql.Connection con = null;
		String allowproduct_to_othercompany = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(ALLOWPRODUCT_TO_OTHERCOMPANY,'0') ALLOWPRODUCT_TO_OTHERCOMPANY from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			allowproduct_to_othercompany = (String) m.get("ALLOWPRODUCT_TO_OTHERCOMPANY");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return allowproduct_to_othercompany;
	}
	
	public String getMANAGEWORKFLOW1(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String ISMANAGEWORKFLOW = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISMANAGEWORKFLOW,'0') ISMANAGEWORKFLOW from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        ISMANAGEWORKFLOW = (String) m.get("ISMANAGEWORKFLOW");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return ISMANAGEWORKFLOW;
	}
	public String getACCESSOWNERAPP(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String ISACCESSOWNERAPP = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISACCESSOWNERAPP,'0') ISACCESSOWNERAPP from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        ISACCESSOWNERAPP = (String) m.get("ISACCESSOWNERAPP");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return ISACCESSOWNERAPP;
	}
	public String getACCESSMANGERAPP(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String ISACCESSMANGERAPP = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISACCESSMANGERAPP,'0') ISACCESSMANGERAPP from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        ISACCESSMANGERAPP = (String) m.get("ISACCESSMANGERAPP");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return ISACCESSMANGERAPP;
	}
	public String getACCESSSTOREAPP(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String ISACCESS_STOREAPP = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISACCESS_STOREAPP,'0') ISACCESS_STOREAPP from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        ISACCESS_STOREAPP = (String) m.get("ISACCESS_STOREAPP");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return ISACCESS_STOREAPP;
	}
	public String getRIDERRAPP(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String ISRIDERRAPP = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISRIDERRAPP,'0') ISRIDERRAPP from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        ISRIDERRAPP = (String) m.get("ISRIDERRAPP");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return ISRIDERRAPP;
	}
	
	public String getPEST_PO(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String ISAUTO_CONVERT_ESTPO = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISAUTO_CONVERT_ESTPO,'0') ISAUTO_CONVERT_ESTPO from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        ISAUTO_CONVERT_ESTPO = (String) m.get("ISAUTO_CONVERT_ESTPO");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return ISAUTO_CONVERT_ESTPO;
	}
	
	public String getISPRICE_UPDATEONLY_IN_OWNOUTLET(String plant) throws Exception {
		java.sql.Connection con = null;
		String ISPRICE_UPDATEONLY_IN_OWNOUTLET = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(ISPRICE_UPDATEONLY_IN_OWNOUTLET,'0') ISPRICE_UPDATEONLY_IN_OWNOUTLET from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			ISPRICE_UPDATEONLY_IN_OWNOUTLET = (String) m.get("ISPRICE_UPDATEONLY_IN_OWNOUTLET");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return ISPRICE_UPDATEONLY_IN_OWNOUTLET;
	}
	
	public String getcompindustry(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String cpyname = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(COMP_INDUSTRY,'') INDUSTRY from [plntmst] where plant='"+plant+"'") ;
		
		 System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());
			
			  cpyname = (String) m.get("INDUSTRY");
			  
			   } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
		   return cpyname;
	}
	
	public String getconvertRECEIPTBILL(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String ISAUTO_CONVERT_RECEIPTBILL = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISAUTO_CONVERT_RECEIPTBILL,'0') ISAUTO_CONVERT_RECEIPTBILL from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        ISAUTO_CONVERT_RECEIPTBILL = (String) m.get("ISAUTO_CONVERT_RECEIPTBILL");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return ISAUTO_CONVERT_RECEIPTBILL;
	}
	
	public String getAutoSOINVOICE(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String ISAUTO_CONVERT_SOINVOICE = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISAUTO_CONVERT_SOINVOICE,'0') ISAUTO_CONVERT_SOINVOICE from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        ISAUTO_CONVERT_SOINVOICE = (String) m.get("ISAUTO_CONVERT_SOINVOICE");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return ISAUTO_CONVERT_SOINVOICE;
	}
	
	public String getStockTakebyAvgCost(String plant) throws Exception {
		java.sql.Connection con = null;
		String ISAUTO_CONVERT_SOINVOICE = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(ISSTOCKTAKE_BYAVGCOST,'0') ISSTOCKTAKE_BYAVGCOST from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			ISAUTO_CONVERT_SOINVOICE = (String) m.get("ISSTOCKTAKE_BYAVGCOST");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return ISAUTO_CONVERT_SOINVOICE;
	}
	
	public String getapiinvloc(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String loc = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(LOC,'') LOC from ["+ plant +"_API_INVENTORY_LOC] where PLANT='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        loc = (String) m.get("LOC");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return loc;
	}
	
	public boolean getisshopify(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String spy = "";
	    boolean isshopify = false;
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(Shopify,'0') Shopify from [PLNTMST] where PLANT='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        spy = (String) m.get("Shopify");
	        
	        if(spy.equalsIgnoreCase("1")) {
	        	isshopify = true;
	        }

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return isshopify;
	}
	
	public boolean getisuserloc(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String spy = "";
	    boolean isuserloc = false;
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISASSIGN_USERLOC,'0') ISASSIGN_USERLOC from [PLNTMST] where PLANT='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        spy = (String) m.get("ISASSIGN_USERLOC");
	        
	        if(spy.equalsIgnoreCase("1")) {
	        	isuserloc = true;
	        }

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return isuserloc;
	}
	
	public ArrayList getPlantMstlistdropdownAll(String plant) throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			sQry = "SELECT PLANT,ISNULL(PLNTDESC,'') PLNTDESC FROM PLNTMST WHERE PLANT LIKE '%"+plant+"%'";
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
	
	public String getCOUNTRY_CODE(String plant) throws Exception {//Get Country Code Date -Azees 10.22
		java.sql.Connection con = null;
		String COUNTY = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE from [plntmst] p where p.plant='"+plant+"'");
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			COUNTY = (String) m.get("COUNTRY_CODE");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return COUNTY;
	}
	
	public String getCOUNTRYNAME(String ccode) throws Exception {
		java.sql.Connection con = null;
		String COUNTY = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer("SELECT ISNULL(COUNTRYNAME,'') AS COUNTRYNAME FROM COUNTRYMASTER WHERE COUNTRY_CODE='"+ccode+"'");
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			COUNTY = (String) m.get("COUNTRYNAME");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return COUNTY;
	}
	
	public String getCOUNTRYNAME(String plant,String country) throws Exception {
		java.sql.Connection con = null;
		String COUNTY = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer("SELECT ISNULL(COUNTRYNAME,'') AS COUNTRYNAME FROM "+plant+"_COUNTRYMASTER WHERE COUNTRYNAME='"+country+"'");
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			COUNTY = (String) m.get("COUNTRYNAME");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return COUNTY;
	}

	public String getCOUNTRY_TIMEZONE(String plant) throws Exception {//Get Country Based Date -Azees 06.22
		java.sql.Connection con = null;
		String COUNTY = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(c.TIME_ZONE_NAME,'Asia/Singapore') TIME_ZONE_NAME from [plntmst] p join [COUNTRYMASTER] c on p.COUNTY=c.COUNTRYNAME where p.plant='"+plant+"'");
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			COUNTY = (String) m.get("TIME_ZONE_NAME");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return COUNTY;
	}
	
	
	public String getIsSalesToPurchase(String plant) throws Exception {
		java.sql.Connection con = null;
		String issalestopurchase = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(ISSALESTOPURCHASE,'0') ISSALESTOPURCHASE from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			issalestopurchase = (String) m.get("ISSALESTOPURCHASE");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return issalestopurchase;
	}
	public String getIsSalesPickandIssueToInvoice(String plant) throws Exception {
		java.sql.Connection con = null;
		String issalestopurchase = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(ISAUTO_CONVERT_ISSUETOINVOICE,'0') ISAUTO_CONVERT_ISSUETOINVOICE from [plntmst] where plant='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			issalestopurchase = (String) m.get("ISAUTO_CONVERT_ISSUETOINVOICE");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return issalestopurchase;
	}
	
	public String CheckPlantDesc(String cust) throws Exception {
		java.sql.Connection con = null;
		String plant = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" SELECT PLANT FROM [PLNTMST] WHERE PLNTDESC='"+cust+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			plant = (String) m.get("PLANT");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return plant;
	}
	
	public String getcmpyEmail(String plant) throws Exception {
	    java.sql.Connection con = null;
	    String cpyemail = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(EMAIL,'') EMAIL from [plntmst] where plant='"+plant+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        cpyemail = (String) m.get("EMAIL");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return cpyemail;
	}
	
}
