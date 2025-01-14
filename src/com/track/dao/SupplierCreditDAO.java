package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class SupplierCreditDAO  extends BaseDAO {

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.SupplierCreditDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.SupplierCreditDAO_PRINTPLANTMASTERLOG;

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
	
	public int addSupplierCreditHdr(Hashtable ht, String plant)throws Exception {
		boolean flag = false;
		int HdrId = 0;
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
			query = "INSERT INTO ["+ plant +"_FINVENDORCREDITNOTEHDR] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			HdrId = execute_NonSelectQueryGetLastInsert(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return HdrId;
	}

	public boolean addMultipleCreditDet(List<Hashtable<String, String>> htDetInfoList, String plant) 
			throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> htDetInfo : htDetInfoList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = htDetInfo.keys();
				for (int i = 0; i < htDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) htDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINVENDORCREDITNOTEDET]  ("
				+ FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
				
				if(connection != null){
					/*Create  PreparedStatement object*/
					ps = connection.prepareStatement(query);
					this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if(flag){
						args.clear();
					}
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return flag;		
	}
	
	public boolean addAttachments(List<Hashtable<String, String>>  htAttachmentList, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> htDetInfo : htAttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = htDetInfo.keys();
				for (int i = 0; i < htDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) htDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINVENDORCREDITNOTEATTACHMENTS]  ("
				+ FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
				
				if(connection != null){
					/*Create  PreparedStatement object*/
					ps = connection.prepareStatement(query);
					this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if(flag){
						args.clear();
					}
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return flag;
	}


	public ArrayList selectForReport(String query, Hashtable ht, String extCond)
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
	
	public List getHdrById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> CNHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, VENDNO, CREDITNOTE, ISNULL(PORETURN, '') PORETURN,ISNULL(PONO, '') PONO, CREDIT_DATE, ISNULL(A.ORDER_DISCOUNT, '') ORDER_DISCOUNT, ISNULL(A.DISCOUNT_TYPE, '') DISCOUNT_TYPE,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,"
					+ " ISNULL(ADJUSTMENT, 0.0) ADJUSTMENT, ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS, ITEM_RATE, ISNULL(DISCOUNT, 0.0) DISCOUNT,TAXAMOUNT,"
					+ " (SELECT VNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME, ISNULL(INBOUND_GST, '0') INBOUND_GST, "
					+ " ISNULL(DISCOUNT_TYPE, '') DISCOUNT_TYPE, ISNULL(DISCOUNT_ACCOUNT, '') DISCOUNT_ACCOUNT,ISNULL(ORDERDISCOUNTTYPE, '') ORDERDISCOUNTTYPE,ISNULL(TAXID, '0') TAXID,ISNULL(ISDISCOUNTTAX, '0') ISDISCOUNTTAX,ISNULL(ISORDERDISCOUNTTAX, '0') ISORDERDISCOUNTTAX,"
					+ "(case when ORDERDISCOUNTTYPE='%' then ISNULL(ORDER_DISCOUNT, 0) else (ISNULL(ORDER_DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) end) ORDER_DISCOUNT,"
					+ " ISNULL(SHIPPINGCOST, 0.0) SHIPPINGCOST, SUB_TOTAL, SUB_TOTAL, TOTAL_AMOUNT, CREDIT_STATUS,"					
					+ " ISNULL(ADJUSTMENT_LABEL, '') ADJUSTMENT_LABEL,ISNULL(TAXTREATMENT,'') TAXTREATMENT,"
					+ " ISNULL(A.PROJECTID,0) PROJECTID,ISNULL(A.SHIPPINGID,'') SHIPPINGID,ISNULL(A.SHIPPINGCUSTOMER,'') SHIPPINGCUSTOMER,"
					+ " ISNULL(CURRENCYID, '') CURRENCYID,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1) CURRENCYUSEQT, "
					+ " ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,"
					+  "ISNULL(A.EMPNO,0) EMPNO,ISNULL((SELECT FNAME FROM " +ht.get("PLANT")  +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME,"
					+ " ISNULL(BILL, '') BILL,  ISNULL(REVERSECHARGE, 0) REVERSECHARGE,ISNULL(GOODSIMPORT, 0) GOODSIMPORT,"
					+ " ISNULL(GRNO, '') GRNO,"
					+ " ISNULL(NOTE, '') NOTE, CRAT, CRBY, UPAT, UPBY,ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION  FROM "
			+ "[" + ht.get("PLANT") + "_FINVENDORCREDITNOTEHDR] A WHERE ID = ? AND PLANT =? ";
			
		/*	query="SELECT ID, VENDNO, CREDITNOTE, ISNULL(PORETURN, '') PORETURN,ISNULL(PONO, '') PONO, CREDIT_DATE,"
					+ " (ISNULL(ADJUSTMENT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1))  ADJUSTMENT,"
					+ " ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS, ITEM_RATE,"
					+ "(case when DISCOUNT_TYPE='%' then ISNULL(DISCOUNT, 0) else (ISNULL(DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) end) DISCOUNT,"
					+ "(case when ORDERDISCOUNTTYPE='%' then ISNULL(ORDER_DISCOUNT, 0) else (ISNULL(ORDER_DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) end) ORDER_DISCOUNT,"
					+ " (ISNULL(TAXAMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) TAXAMOUNT,"
					+ " (SELECT VNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME, ISNULL(INBOUND_GST, '0') INBOUND_GST,"
					+ " ISNULL(DISCOUNT_TYPE, '') DISCOUNT_TYPE, ISNULL(DISCOUNT_ACCOUNT, '') DISCOUNT_ACCOUNT,ISNULL(ORDERDISCOUNTTYPE, '') ORDERDISCOUNTTYPE,ISNULL(TAXID, '0') TAXID,ISNULL(ISDISCOUNTTAX, '0') ISDISCOUNTTAX,ISNULL(ISORDERDISCOUNTTAX, '0') ISORDERDISCOUNTTAX,"
					+ " (ISNULL(SHIPPINGCOST, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) SHIPPINGCOST, "
					+ " (ISNULL(SUB_TOTAL, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) SUB_TOTAL, "
					+ " (ISNULL(TOTAL_AMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) TOTAL_AMOUNT, TOTAL_AMOUNT as CONV_TOTAL_AMOUNT,BILL_STATUS,"
					+ " NOTE, ISNULL(GRNO, '') GRNO, ISNULL(SHIPMENT_CODE, '') SHIPMENT_CODE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,"
					+ " ISNULL(CURRENCYID, '') CURRENCYID,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1) CURRENCYUSEQT, "
					+ " ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,"
					+ " ISNULL(ADJUSTMENT_LABEL, '') ADJUSTMENT_LABEL, ISNULL(REFERENCE_NUMBER, '') REFERENCE_NUMBER,ISNULL(REVERSECHARGE, 0) REVERSECHARGE,ISNULL(GOODSIMPORT, 0) GOODSIMPORT,"
					+ " ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION, ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE A.PURCHASE_LOCATION = C.STATE),'') as STATE_PREFIX,"
					+ " ISNULL(NOTE, '') NOTE, CRAT, CRBY, UPAT, UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FINVENDORCREDITNOTEHDR] A WHERE ID = ? AND PLANT =? ";*/
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			CNHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return CNHdrList;
	}
	
	public List getDetByHdrId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> CNDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, LNNO, HDRID, ITEM, ACCOUNT_NAME, QTY, COST, DISCOUNT, TAX_TYPE, ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE, ISNULL(A.DISCOUNT_TYPE,'') DISCOUNT_TYPE,"
			+" ISNULL((SELECT C.COST FROM [" + ht.get("PLANT") + "_ITEMMST] C WHERE A.ITEM = C.ITEM),'') BASECOST,ISNULL(A.CURRENCYUSEQT,1) CURRENCYUSEQT,"
			+ "ISNULL((SELECT ITEMDESC FROM "+ ht.get("PLANT") +"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as ITEMDESC,"
			+" ISNULL((SELECT CATLOGPATH FROM "+ ht.get("PLANT") +"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as CATLOGPATH, ISNULL((SELECT COUNT(*) FROM " + ht.get("PLANT") +"_FINVENDORCREDITNOTEATTACHMENTS N where A.HDRID=N.HDRID),0) as ATTACHNOTE_COUNT, "
			+ " AMOUNT, CRAT, CRBY, UPAT, UPBY,ISNULL((SELECT NOTE FROM "+ ht.get("PLANT") +"_FINVENDORCREDITNOTEHDR C WHERE A.HDRID = C.ID),'') as NOTE  FROM "
			+ "[" + ht.get("PLANT") + "_FINVENDORCREDITNOTEDET] A LEFT JOIN "
			+ "[" + ht.get("PLANT") + "_COMPANY_CONFIG] B ON A.TAX_TYPE = B.GSTTYPE"
					+ " WHERE HDRID = ? AND A.PLANT =? ORDER BY A.LNNO";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("HDRID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			CNDetList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return CNDetList;
	}
	
	public List getConvHdrById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> CNHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, VENDNO, CREDITNOTE, ISNULL(PORETURN, '') PORETURN,ISNULL(PONO, '') PONO, CREDIT_DATE, ISNULL(A.ORDERDISCOUNTTYPE, '') ORDERDISCOUNTTYPE, ISNULL(A.DISCOUNT_TYPE, '') DISCOUNT_TYPE,"
					+ " (ISNULL(ADJUSTMENT, 0.0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) ADJUSTMENT, "
					+ " ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS, ITEM_RATE, ISNULL(A.TAXID, '0') TAXID, ISNULL(A.ISORDERDISCOUNTTAX, '0') ISORDERDISCOUNTTAX, ISNULL(A.ISDISCOUNTTAX, '0') ISDISCOUNTTAX,"
					+ " (case when DISCOUNT_TYPE='%' then ISNULL(DISCOUNT, 0) else (ISNULL(DISCOUNT, 0.0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) end) DISCOUNT,"
					+ " (case when ORDERDISCOUNTTYPE='%' then ISNULL(A.ORDER_DISCOUNT, 0) else (ISNULL(A.ORDER_DISCOUNT, 0.0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) end) ORDER_DISCOUNT,"
					+ " (ISNULL(TAXAMOUNT,0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) TAXAMOUNT,"
					+ " (SELECT VNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME,ISNULL(A.PROJECTID, '0') PROJECTID, "
					+ " ISNULL(DISCOUNT_TYPE, '') DISCOUNT_TYPE, ISNULL(DISCOUNT_ACCOUNT, '') DISCOUNT_ACCOUNT,CREDIT_STATUS,"
					+ " (ISNULL(SHIPPINGCOST, 0.0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) SHIPPINGCOST, "
					+ " (SUB_TOTAL * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) SUB_TOTAL, "
					+ " (TOTAL_AMOUNT * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1)) TOTAL_AMOUNT, "					
					+ " ISNULL(ADJUSTMENT_LABEL, '') ADJUSTMENT_LABEL,ISNULL(TAXTREATMENT,'') TAXTREATMENT,"
					+ " ISNULL(CURRENCYID, '') CURRENCYID,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINVENDORCREDITNOTEDET] WHERE HDRID = A.ID ),1) CURRENCYUSEQT, "
					+ " ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,"
					+ " ISNULL(BILL, '') BILL,ISNULL(REVERSECHARGE, 0) REVERSECHARGE,ISNULL(GOODSIMPORT, 0) GOODSIMPORT,"
					+ " ISNULL(GRNO, '') GRNO,"
					+ " ISNULL(NOTE, '') NOTE, CRAT, CRBY, UPAT, UPBY,ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION  FROM "
			+ "[" + ht.get("PLANT") + "_FINVENDORCREDITNOTEHDR] A WHERE ID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			CNHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return CNHdrList;
	}
	
	public List getConvDetByHdrId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> CNDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, LNNO, HDRID, ITEM,ISNULL((SELECT C.ITEMDESC FROM ["+ht.get("PLANT") +"_ITEMMST] C WHERE A.ITEM = C.ITEM),'') ITEMDESC, ACCOUNT_NAME, QTY,"
					+ " (COST * ISNULL(CURRENCYUSEQT,1)) COST, (case when DISCOUNT_TYPE='%' then DISCOUNT else (DISCOUNT*ISNULL(CURRENCYUSEQT,1)) end) DISCOUNT, "
					+ " TAX_TYPE, ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE, ISNULL(A.DISCOUNT_TYPE,'') DISCOUNT_TYPE,"
					+ " ISNULL((SELECT CATLOGPATH FROM "+ ht.get("PLANT") +"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as CATLOGPATH, ISNULL((SELECT COUNT(*) FROM " + ht.get("PLANT") +"_FINVENDORCREDITNOTEATTACHMENTS N where A.HDRID=N.HDRID),0) as ATTACHNOTE_COUNT, "
					+ " (AMOUNT * ISNULL(CURRENCYUSEQT,1)) AMOUNT,"
					+ " CRAT, CRBY, UPAT, UPBY,ISNULL((SELECT NOTE FROM "+ ht.get("PLANT") +"_FINVENDORCREDITNOTEHDR C WHERE A.HDRID = C.ID),'') as NOTE  FROM "
			+ "[" + ht.get("PLANT") + "_FINVENDORCREDITNOTEDET] A LEFT JOIN "
			+ "[" + ht.get("PLANT") + "_COMPANY_CONFIG] B ON A.TAX_TYPE = B.GSTTYPE"
					+ " WHERE HDRID = ? AND A.PLANT =? ORDER BY A.LNNO";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("HDRID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			CNDetList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return CNDetList;
	}
	
	public boolean updateHdr(String query, Hashtable htCondition, String extCond)
		     throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		     con = com.track.gates.DbBean.getConnection();
		     StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                     + htCondition.get("PLANT") + "_FINVENDORCREDITNOTEHDR]");
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

	public boolean deleteDet(String plant,String TranId)
	        throws Exception {
			boolean deleteprdForTranId = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant + "_FINVENDORCREDITNOTEDET" + "]"
			                        + " WHERE HDRID ='"+TranId+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0)
			                deleteprdForTranId = true;
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deleteprdForTranId;
	}
	 public List getSupplierCrdnoteAttachByHrdId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> crdnoteAttachList = new ArrayList<>();
			Map<String, String> map = null;
			String query = "";
			List<String> args = null;
			try {
				/* Instantiate the list */
				args = new ArrayList<String>();

				con = com.track.gates.DbBean.getConnection();
				query = "SELECT * FROM"
						+ "[" + ht.get("PLANT") + "_FINVENDORCREDITNOTEATTACHMENTS] WHERE HDRID = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);

				/* Storing all the query param argument in list squentially */
				args.add((String) ht.get("ID"));
				args.add((String) ht.get("PLANT"));

				// this.mLogger.query(this.printQuery, query);

				crdnoteAttachList = selectData(ps, args);

			} catch (Exception e) {
				// this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return crdnoteAttachList;
		}
	 public List getSupplierCrdnoteAttachByPrimId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> crdnoteAttachList = new ArrayList<>();
			Map<String, String> map = null;
			String query = "";
			List<String> args = null;
			try {
				/* Instantiate the list */
				args = new ArrayList<String>();

				con = com.track.gates.DbBean.getConnection();
				query = "SELECT * FROM"
						+ "[" + ht.get("PLANT") + "_FINVENDORCREDITNOTEATTACHMENTS] WHERE ID = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);

				/* Storing all the query param argument in list squentially */
				args.add((String) ht.get("ID"));
				args.add((String) ht.get("PLANT"));

				// this.mLogger.query(this.printQuery, query);

				crdnoteAttachList = selectData(ps, args);

			} catch (Exception e) {
				// this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return crdnoteAttachList;
		}
	 public int deleteCreditAttachByPrimId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			Map<String, String> map = null;
			String query="";
			int count=0;
			try {			
			    
				con = com.track.gates.DbBean.getConnection();
				query="DELETE FROM " + "[" + ht.get("PLANT") + "_FINVENDORCREDITNOTEATTACHMENTS] "
						+ "WHERE ID = ? AND PLANT = ?";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				ps.setString(1, (String) ht.get("ID"));
				ps.setString(2, (String) ht.get("PLANT"));	
				
				//this.mLogger.query(this.printQuery, query);	
				count=ps.executeUpdate();
				
			} catch (Exception e) {
				//this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return count;
		}
	 
	 public boolean isExisit(Hashtable ht) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "FINVENDORCREDITNOTEHDR" + "]");
				sql.append(" WHERE  " + formCondition(ht));

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
	 public boolean isExisit(Hashtable ht, String extCond) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM [" + ht.get("A.PLANT") + "_FINVENDORCREDITNOTEHDR] A ");
				sql.append(" WHERE  " + formCondition(ht));

				if (extCond.length() > 0)
					sql.append("  " + extCond);

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
	 
	 public String getPrefixByState(String state) throws Exception
	 {
		 java.sql.Connection con = null;
	      String prefix = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT PREFIX FROM  [FINSALESLOCATION] b"
	        		+ " WHERE b.STATE='"+state+"'";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       prefix = (String) m.get("PREFIX");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return prefix;
	 }
	 
	 public List getSupplierCrdnotebycreditnote(String plant, String cnote) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> crdnoteAttachList = new ArrayList<>();
			Map<String, String> map = null;
			String query = "";
			List<String> args = null;
			try {
				/* Instantiate the list */
				args = new ArrayList<String>();

				con = com.track.gates.DbBean.getConnection();
				query = "SELECT ISNULL(PONO,'') PONO, ISNULL(BILL,'') BILL FROM"
						+ "[" + plant+ "_FINVENDORCREDITNOTEHDR] WHERE CREDITNOTE = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);

				/* Storing all the query param argument in list squentially */
				args.add(cnote);
				args.add(plant);

				// this.mLogger.query(this.printQuery, query);

				crdnoteAttachList = selectData(ps, args);

			} catch (Exception e) {
				// this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return crdnoteAttachList;
		}

	 
	 public boolean deleteSupplierCrdnote(String plant,String TranId)
		        throws Exception {
				boolean deleteprdForTranId = false;
				PreparedStatement ps = null;
				PreparedStatement pshdr = null;
				PreparedStatement psatt = null;
				Connection con = null;
				try {
				        con = DbBean.getConnection();
				        
				        
				        String sQry = "DELETE FROM " + "[" + plant + "_FINVENDORCREDITNOTEHDR" + "]"
				                        + " WHERE ID ='"+TranId+"'";
				        this.mLogger.query(this.printQuery, sQry);
				        ps = con.prepareStatement(sQry);
				        int iCnt = ps.executeUpdate();
				        if (iCnt > 0)
				                deleteprdForTranId = true;
				        if(deleteprdForTranId) {
				        sQry = "DELETE FROM " + "[" + plant + "_FINVENDORCREDITNOTEDET" + "]"
		                        + " WHERE HDRID ='"+TranId+"'";
				        this.mLogger.query(this.printQuery, sQry);
				        pshdr = con.prepareStatement(sQry);
				        int iCnthdr = pshdr.executeUpdate();
				       
				     
				         sQry = "DELETE FROM " + "[" + plant + "_FINVENDORCREDITNOTEATTACHMENTS" + "]"
			                    + " WHERE HDRID ='"+TranId+"'";
					    this.mLogger.query(this.printQuery, sQry);
					    psatt = con.prepareStatement(sQry);
					    int iCntatt = psatt.executeUpdate();
					    
				        }
				        
				} catch (Exception e) {
				        this.mLogger.exception(this.printLog, "", e);
				} finally {
				        DbBean.closeConnection(con, ps);
				}
				
				return deleteprdForTranId;
	 	}
}
