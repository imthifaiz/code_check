package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.BillHdr;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.HrClaim;
import com.track.db.object.ItemCogs;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class BillDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.BillDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.BillDAO_PRINTPLANTMASTERLOG;

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
	
	public int addBillHdr(Hashtable ht, String plant)throws Exception {
		boolean flag = false;
		int billHdrId = 0;
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
			query = "INSERT INTO ["+ plant +"_FINBILLHDR] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			billHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return billHdrId;
	}
	
	public boolean addMultipleBillDet(List<Hashtable<String, String>> billDetInfoList, String plant) 
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
			
			for (Hashtable<String, String> billDetInfo : billDetInfoList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = billDetInfo.keys();
				for (int i = 0; i < billDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) billDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINBILLDET]  ("
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
	
	public boolean addBillAttachments(List<Hashtable<String, String>>  billAttachmentList, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> billDetInfo : billAttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = billDetInfo.keys();
				for (int i = 0; i < billDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) billDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINBILLATTACHMENTS]  ("
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
	
	public List getBillHdrById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, VENDNO, BILL, ISNULL(PONO, '') PONO,ISNULL(BILL_TYPE, '') BILL_TYPE, ISNULL(SHIPMENT_CODE,'') SHIPMENT_CODE, ISNULL(ISSHIPPINGTAXABLE, '0') ISSHIPPINGTAXABLE,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP, BILL_DATE, ISNULL(DUE_DATE, '') DUE_DATE,"
					+ " (ISNULL(ADJUSTMENT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINBILLDET] WHERE BILLHDRID = A.ID ),1))  ADJUSTMENT,"
					+ " ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS, ITEM_RATES,ISNULL(A.PROJECTID,0) PROJECTID,ISNULL(A.TRANSPORTID,0) TRANSPORTID,ISNULL(A.ORDERTYPE,'') ORDERTYPE,ISNULL(A.EMPNO,0) EMPNO,ISNULL((SELECT FNAME FROM " +ht.get("PLANT")  +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME,ISNULL(A.SHIPPINGID,'') SHIPPINGID,"
					+ "(case when DISCOUNT_TYPE='%' then ISNULL(DISCOUNT, 0) else (ISNULL(DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINBILLDET] WHERE BILLHDRID = A.ID ),1)) end) DISCOUNT,"
					+ "(case when ORDERDISCOUNTTYPE='%' then ISNULL(ORDER_DISCOUNT, 0) else (ISNULL(ORDER_DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINBILLDET] WHERE BILLHDRID = A.ID ),1)) end) ORDER_DISCOUNT,"
					+ " (ISNULL(TAXAMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINBILLDET] WHERE BILLHDRID = A.ID ),1)) TAXAMOUNT, ISNULL(TAX_STATUS, '') TAX_STATUS,"
					+ " (SELECT VNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME, ISNULL(INBOUND_GST, '0') INBOUND_GST,"
//					+ " ISNULL((SELECT ISNULL(VNAME,'') VNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] WHERE VENDNO = A.SHIPPINGID), '') AS SHIPPINGCUSTOMER,"
					+ " ISNULL(SHIPPINGCUSTOMER,'') AS SHIPPINGCUSTOMER,"
					+ " ISNULL(DISCOUNT_TYPE, '') DISCOUNT_TYPE, ISNULL(DISCOUNT_ACCOUNT, '') DISCOUNT_ACCOUNT,ISNULL(ORDERDISCOUNTTYPE, '') ORDERDISCOUNTTYPE,ISNULL(TAXID, '0') TAXID,ISNULL(ISDISCOUNTTAX, '0') ISDISCOUNTTAX,ISNULL(ISORDERDISCOUNTTAX, '0') ISORDERDISCOUNTTAX,"
					+ " (ISNULL(SHIPPINGCOST, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINBILLDET] WHERE BILLHDRID = A.ID ),1)) SHIPPINGCOST, "
					+ " (ISNULL(SUB_TOTAL, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINBILLDET] WHERE BILLHDRID = A.ID ),1)) SUB_TOTAL, "
					+ " (ISNULL(TOTAL_AMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINBILLDET] WHERE BILLHDRID = A.ID ),1)) TOTAL_AMOUNT, TOTAL_AMOUNT as CONV_TOTAL_AMOUNT,BILL_STATUS,"
					+ " NOTE, ISNULL(GRNO, '') GRNO, ISNULL(SHIPMENT_CODE, '') SHIPMENT_CODE,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(ORIGIN,0) as ORIGIN,ISNULL(DEDUCT_INV,0) as DEDUCT_INV,"
					+ " ISNULL(CURRENCYID, '') CURRENCYID,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINBILLDET] WHERE BILLHDRID = A.ID ),1) CURRENCYUSEQT, "
					+ " ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,"
					+ " ISNULL(ADJUSTMENT_LABEL, '') ADJUSTMENT_LABEL, ISNULL(REFERENCE_NUMBER, '') REFERENCE_NUMBER,ISNULL(REVERSECHARGE, 0) REVERSECHARGE,ISNULL(GOODSIMPORT, 0) GOODSIMPORT,"
					+ " ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION, ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE A.PURCHASE_LOCATION = C.STATE),'') as STATE_PREFIX,"
					+ " ISNULL(NOTE, '') NOTE, CRAT, CRBY, UPAT, UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FINBILLHDR] A WHERE ID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			billHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billHdrList;
	}
	
	
	public ArrayList selectBillHdr(String query, Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "FINBILLHDR" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				MLogger.log(0, "condition preisent stage 3");
				sql.append(" WHERE ");

				conditon = formCondition(ht);

				sql.append(conditon);

			}
			this.mLogger.query(this.printQuery, sql.toString());
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
	
	public List getBillHdrByVendNo(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, VENDNO, BILL, ISNULL(PONO, '') PONO, BILL_DATE, ISNULL(DUE_DATE, '') DUE_DATE,"
					+ " ISNULL(ADJUSTMENT, 0.0) ADJUSTMENT, ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS, ISNULL(EMPNO, '') EMPNO ,ITEM_RATES, ISNULL(DISCOUNT, 0.0) DISCOUNT,"
					+ " (SELECT VNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME, "
					+ " ISNULL(DISCOUNT_TYPE, '') DISCOUNT_TYPE, ISNULL(DISCOUNT_ACCOUNT, '') DISCOUNT_ACCOUNT,"
					+ " ISNULL(SHIPPINGCOST, 0.0) SHIPPINGCOST, SUB_TOTAL, SUB_TOTAL, TOTAL_AMOUNT, BILL_STATUS,"
					+ " NOTE, ISNULL(GRNO, '') GRNO, ISNULL(SHIPMENT_CODE, '') SHIPMENT_CODE,"
					+ " ISNULL(ADJUSTMENT_LABEL, '') ADJUSTMENT_LABEL, ISNULL(REFERENCE_NUMBER, '') REFERENCE_NUMBER,"
					+ " ISNULL(NOTE, '') NOTE, CRAT, CRBY, UPAT, UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FINBILLHDR] A WHERE VENDNO = ? AND PLANT =? AND BILL_STATUS=?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("VENDNO"));
			args.add((String) ht.get("PLANT"));
			args.add((String) ht.get("BILL_STATUS"));
			
			this.mLogger.query(this.printQuery, query);
			
			billHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billHdrList;
	}
	
	public List getBillHdrByVendNoopen(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, VENDNO, BILL, ISNULL(PONO, '') PONO, BILL_DATE, ISNULL(DUE_DATE, '') DUE_DATE,ISNULL(CURRENCYID,'') CURRENCYID," 
					+ " ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINBILLDET B WHERE B.BILLHDRID=A.ID),1) CURRENCYUSEQT,"
					+ " ISNULL(ADJUSTMENT, 0.0) ADJUSTMENT, ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ISNULL(EMPNO, '') EMPNO, ITEM_RATES, ISNULL(DISCOUNT, 0.0) DISCOUNT,"
					+ " (SELECT VNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME, "
					+ " ISNULL((SELECT JobNum FROM " + "[" + ht.get("PLANT") + "_POHDR] WHERE PONO = A.PONO ),'') AS REFERENCE_NUMBER, "
					+ " ISNULL(DISCOUNT_TYPE, '') DISCOUNT_TYPE, ISNULL(DISCOUNT_ACCOUNT, '') DISCOUNT_ACCOUNT,"
					+ " ISNULL(SHIPPINGCOST, 0.0) SHIPPINGCOST, SUB_TOTAL, SUB_TOTAL, TOTAL_AMOUNT, BILL_STATUS,"
					+ " NOTE, ISNULL(GRNO, '') GRNO, ISNULL(SHIPMENT_CODE, '') SHIPMENT_CODE,"
					+ " ISNULL(ADJUSTMENT_LABEL, '') ADJUSTMENT_LABEL,"
					+ " ISNULL(NOTE, '') NOTE, CRAT, CRBY, UPAT, UPBY  FROM "
			//+ "[" + ht.get("PLANT") + "_FINBILLHDR] A WHERE VENDNO = ? AND PLANT =? AND BILL_STATUS !='PAID' AND BILL_STATUS !='DRAFT' AND BILL_STATUS!='CANCELLED' ORDER BY CONVERT(date, BILL_DATE, 103) desc";
			+ "[" + ht.get("PLANT") + "_FINBILLHDR] A WHERE VENDNO = ? AND PLANT =? AND BILL_STATUS !='PAID' AND BILL_STATUS !='DRAFT' AND BILL_STATUS!='CANCELLED' ORDER BY CONVERT(date, BILL_DATE, 103) asc";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("VENDNO"));
			args.add((String) ht.get("PLANT"));
			
			
			this.mLogger.query(this.printQuery, query);
			
			billHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billHdrList;
	}
	
	public List getBillDetByHdrId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, LNNO, BILLHDRID, ITEM,ISNULL((SELECT C.ITEMDESC FROM [" + ht.get("PLANT") + "_ITEMMST] C WHERE A.ITEM = C.ITEM),'') ITEMDESC, ACCOUNT_NAME,ISNULL((SELECT C.COST FROM [" + ht.get("PLANT") + "_ITEMMST] C WHERE A.ITEM = C.ITEM),'') BASECOST, QTY,ISNULL(DEBITNOTE_QTY,'0') AS DEBITNOTE_QTY,"
					+"(SELECT ISNULL(SUM(RETURN_QTY),0) FROM [" + ht.get("PLANT") + "_FINPORETURN] C JOIN [" + ht.get("PLANT") + "_FINBILLHDR] D ON C.BILL=D.BILL WHERE D.ID=A.BILLHDRID AND C.LNNO=A.LNNO) RETURN_QTY,"
					+ "(COST*ISNULL(CURRENCYUSEQT,1)) COST,ISNULL(CURRENCYUSEQT,1) CURRENCYUSEQT,"
					+ "(case when DISCOUNT_TYPE='%' then DISCOUNT else (DISCOUNT*ISNULL(CURRENCYUSEQT,1)) end) DISCOUNT,"
					+ "TAX_TYPE, ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE, ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE, ISNULL((SELECT C.CATLOGPATH FROM [" + ht.get("PLANT") + "_ITEMMST] C WHERE A.ITEM = C.ITEM),'') CATLOGPATH, "
					+ "(ISNULL((SELECT D.DISCOUNT FROM [" + ht.get("PLANT") + "_FINBILLHDR] D WHERE A.BILLHDRID = D.ID),'')*ISNULL(CURRENCYUSEQT,1)) ALLDISCOUNT,"
					+ "ISNULL((SELECT D.ORDER_DISCOUNT FROM [" + ht.get("PLANT") + "_FINBILLHDR] D WHERE A.BILLHDRID = D.ID),'') ORDER_DISCOUNT,"
					+ "ISNULL((SELECT D.BILL_STATUS FROM [" + ht.get("PLANT") + "_FINBILLHDR] D WHERE A.BILLHDRID = D.ID),'') BILL_STATUS,"
					+ "(ISNULL(LANDED_COST,0)*ISNULL(CURRENCYUSEQT,1)) LANDED_COST,ISNULL(UOM,'') UOM,ISNULL(LOC,'') LOC,ISNULL(BATCH, '') BATCH, "
					+ "(ISNULL((SELECT ISNULL(UNITCOST_AOD,UNITCOST) AS UNITCOST_AOD FROM "+ "[" + ht.get("PLANT") + "_PODET] D JOIN "
					+ "[" + ht.get("PLANT") + "_FINBILLHDR] E ON D.PONO = E.PONO WHERE A.ITEM = D.ITEM AND A.LNNO = D.POLNNO AND A.BILLHDRID = E.ID),COST)) UNITCOST_AOD, "
					+ "(AMOUNT*ISNULL(CURRENCYUSEQT,1)) AMOUNT, CRAT, CRBY, UPAT, UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FINBILLDET] A LEFT JOIN "
			+ "[" + ht.get("PLANT") + "_COMPANY_CONFIG] B ON A.TAX_TYPE = B.GSTTYPE"
					+ " WHERE BILLHDRID = ? AND A.PLANT =? ORDER BY A.LNNO";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("BILLHDRID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			billDetList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billDetList;
	}
	
	public boolean updateGrntoBill(String query, Hashtable htCondition, String extCond,
			String aPlant) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
					+ "FINGRNOTOBILL" + "]");
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
			DbBean.closeConnection(con);
		}
		return flag;

	}
	
	public boolean updateBillHdr(String query, Hashtable htCondition, String extCond)
		     throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		     con = com.track.gates.DbBean.getConnection();
		     StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                     + htCondition.get("PLANT") + "_FINBILLHDR]");
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
	
	public boolean updateBillDet(String query, Hashtable htCondition, String extCond)
		     throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		     con = com.track.gates.DbBean.getConnection();
		     StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                     + htCondition.get("PLANT") + "_FINBILLDET]");
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
	
	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "FINBILLHDR" + "]");
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
			sql.append(" FROM [" + ht.get("A.PLANT") + "_FINBILLDET] A JOIN [" + ht.get("A.PLANT")+"_FINBILLHDR] B "
					+ "ON A.BILLHDRID = B.ID ");
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
	
	public List getBillAttachByHrdId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billAttachList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT * FROM"
					+ "[" + ht.get("PLANT") + "_FINBILLATTACHMENTS] WHERE BILLHDRID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));

			// this.mLogger.query(this.printQuery, query);

			billAttachList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billAttachList;
	}
	public List getBillAttachByPrimId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billAttachList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT * FROM"
					+ "[" + ht.get("PLANT") + "_FINBILLATTACHMENTS] WHERE ID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));

			// this.mLogger.query(this.printQuery, query);

			billAttachList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billAttachList;
	}
 public int deleteBillAttachByPrimId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		Map<String, String> map = null;
		String query="";
		int count=0;
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="DELETE FROM " + "[" + ht.get("PLANT") + "_FINBILLATTACHMENTS] "
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
 public boolean deleteBillDet(String plant,String TranId)
	        throws Exception {
			boolean deleteprdForTranId = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant + "_FINBILLDET" + "]"
			                        + " WHERE BILLHDRID ='"+TranId+"'";
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
 
 public List getBillHdrByVendNoforcreditnotes(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT PLANT,ID,VENDNO,BILL,PONO,GRNO,BILL_DATE,DUE_DATE,PAYMENT_TERMS,ISNULL(EMPNO, '') EMPNO,ITEM_RATES,DISCOUNT,DISCOUNT_TYPE,ISNULL(CURRENCYID,'') CURRENCYID," + 
					"ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINBILLDET B WHERE B.BILLHDRID=A.ID),1) CURRENCYUSEQT," +
					"DISCOUNT_ACCOUNT,SHIPPINGCOST,ADJUSTMENT,SUB_TOTAL,TOTAL_AMOUNT,BILL_STATUS,NOTE,CRAT,CRBY,UPAT," + 
					"UPBY,ADJUSTMENT_LABEL,SHIPMENT_CODE,REFERENCE_NUMBER,ADVANCEFROM,PURCHASE_LOCATION,TAXTREATMENT," + 
					"REVERSECHARGE,GOODSIMPORT,CREDITNOTESSTATUS,TAXAMOUNT,TAX_STATUS,'BILLHDR' AS BILLFROM"
					+ "  FROM [" + ht.get("PLANT") + "_FINBILLHDR] A WHERE VENDNO = ? AND PLANT =? AND BILL_STATUS!='Draft' AND BILL_STATUS!='CANCELLED' AND CREDITNOTESSTATUS = 0 AND BILL_TYPE = 'NON INVENTORY' order by A.ID desc";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("VENDNO"));
			args.add((String) ht.get("PLANT"));
			//args.add((String) ht.get("BILL_STATUS"));
			
			this.mLogger.query(this.printQuery, query);
			
			billHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billHdrList;
	}
 
 public boolean isShipmentcode(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" FROM [" + ht.get("PLANT")+"_FINBILLHDR]");
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
 
 
 	public boolean deleteBill(String plant,String TranId)
	        throws Exception {
			boolean deleteprdForTranId = false;
			PreparedStatement ps = null;
			PreparedStatement pshdr = null;
			PreparedStatement psatt = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant + "_FINBILLHDR" + "]"
			                        + " WHERE ID ='"+TranId+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0)
			                deleteprdForTranId = true;
			        if(deleteprdForTranId) {
			        sQry = "DELETE FROM " + "[" + plant + "_FINBILLDET" + "]"
	                        + " WHERE BILLHDRID ='"+TranId+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        pshdr = con.prepareStatement(sQry);
			        int iCnthdr = pshdr.executeUpdate();
			       
			     
			         sQry = "DELETE FROM " + "[" + plant + "_FINBILLATTACHMENTS" + "]"
		                    + " WHERE BILLHDRID ='"+TranId+"'";
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
 	
 	public String  getTotalReceivableForDashboard(String plant, String fromDate, String toDate, 
			String numberOfDecimal) throws Exception {
	      java.sql.Connection con = null;
	      String amountReceivable = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT CAST(ISNULL(SUM(AMOUNTRECEIVED),0) AS DECIMAL(18,"+numberOfDecimal+")) AS AMOUNT_RECEIVED FROM ["+plant+"_FINRECEIVEHDR] WHERE  " 
					+ " CUSTNO IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=3 AND ACCOUNTDETAILTYPE=7) "
					+ " AND DEPOSIT_TO NOT IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=3 AND ACCOUNTDETAILTYPE=8) "
					+ " AND CONVERT(DATETIME, RECEIVE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' ";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       amountReceivable = (String) m.get("AMOUNT_RECEIVED");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return amountReceivable;
	  }
 	
 	public List getPayableAmountBySupplierForDashboard(String plant, String fromDate, String toDate, 
			String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> InvoiceDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			
			query = "SELECT ISNULL((SELECT VNAME FROM ["+plant+"_VENDMST] V WHERE V.VENDNO = A.VENDNO),'') SUPPLIER,SUM(AMOUNT_PAYABLE) AS AMOUNT_PAYABLE,SUM(PDC_AMOUNT) AS PDC_AMOUNT,"
					+" (SUM(AMOUNT_PAYABLE) + SUM(PDC_AMOUNT)) TOTAL_PAYABLE FROM ("
					+" SELECT VENDNO,CAST(SUM(AMOUNTPAID) AS DECIMAL(18,2)) AS AMOUNT_PAYABLE, 0 AS PDC_AMOUNT FROM ["+plant+"_FINPAYMENTHDR] WHERE" 
					+" VENDNO IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=6 AND ACCOUNTDETAILTYPE=18)"  
					+" AND PAID_THROUGH NOT IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=3 AND ACCOUNTDETAILTYPE=8)"  
					+" AND CONVERT(DATETIME, PAYMENT_DATE, 103) BETWEEN ? AND ? GROUP BY VENDNO"
					+" UNION"
					+" SELECT VENDNO, 0 AS AMOUNT_PAYABLE, CAST(ISNULL(SUM(CHEQUE_AMOUNT),0) AS DECIMAL(18,2)) PDC_AMOUNT FROM ["+plant+"_FINPAYMENTPDC]  WHERE" 
					+" STATUS = 'NOT PROCESSED'  AND CONVERT(DATETIME, CHEQUE_DATE, 103) BETWEEN ? AND ?"  
					+" OR CONVERT(DATETIME, CHEQUE_REVERSAL_DATE, 103) BETWEEN ? AND ? GROUP BY VENDNO"
					+" ) A GROUP BY VENDNO";
			

			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add(fromDate);
			args.add(toDate);
			args.add(fromDate);
			args.add(toDate);
			args.add(fromDate);
			args.add(toDate);
			
			this.mLogger.query(this.printQuery, query);
			
			InvoiceDetList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return InvoiceDetList;
	}
 	
 	 public Map getBillHeaderDetails(String plant) throws Exception {
         MLogger.log(1, this.getClass() + " getBillHeaderDetails()");
         Map m = new HashMap();
         java.sql.Connection con = null;
         String scondtn ="";
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(BILLHEADER,'') AS BILLHEADER,ISNULL(TOHEADER,'') AS TOHEADER ,ISNULL(FROMHEADER,'') AS FROMHEADER,ISNULL(ORDERNO,'') AS ORDERNO,ISNULL(DATE,'') AS ORDERDATE,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(PAYMENTTERMS,'') AS PAYMENTTERMS ,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM ,ISNULL(PRINTDETAILDESCRIPTION,'0') AS PRINTDETAILDESCRIPTION,ISNULL(PRINTWITHSHIPINGADD,'0') AS PRINTWITHSHIPINGADD,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(PRINTWITHSUPPLIERUENNO,'0') AS PRINTWITHSUPPLIERUENNO,ISNULL(PRINTPURCHASELOCATION,'0') AS PRINTPURCHASELOCATION, ");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(BILLQTY,'') AS BILLQTY,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(GRNDATE,'') GRNDATE,ISNULL(DISPLAYBYORDERTYPE,'') AS DISPLAYBYORDERTYPE,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL(RATE,'') AS RATE ,ISNULL(ITEMDISCOUNT,'') AS ITEMDISCOUNT,ISNULL(ITEMAMOUNT,'') AS ITEMAMOUNT, ");
                 sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TAX,'') AS TAX,ISNULL(TOTAL,'') AS TOTAL,ISNULL(UOM,'') AS UOM,ISNULL(PROJECT,'') AS PROJECT,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT, ISNULL(EMPLOYEE,'') AS EMPLOYEE,ISNULL(PRINTEMPLOYEE,'0') AS PRINTEMPLOYEE,ISNULL(TRANSPORT_MODE,'') AS TRANSPORT_MODE,ISNULL(PRINTWITHTRANSPORT_MODE,'0') AS PRINTWITHTRANSPORT_MODE,");
                 sql.append("ISNULL(FOOTER1,'') AS FOOTER1,ISNULL(FOOTER2,'') AS FOOTER2 ,ISNULL(FOOTER3,'') AS FOOTER3,ISNULL(FOOTER4,'') AS FOOTER4,ISNULL(FOOTER5,'') AS FOOTER5,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(PRINTSUPTERMS,'0') AS PRINTSUPTERMS,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE, ");
                 sql.append("ISNULL(FOOTER6,'') AS FOOTER6,ISNULL(FOOTER7,'') AS FOOTER7,ISNULL(FOOTER8,'') AS FOOTER8,ISNULL(FOOTER9,'') AS FOOTER9,ISNULL(BILLNO,'') AS BILLNO,ISNULL(BILLDATE,'') AS BILLDATE, ");
   				 sql.append("ISNULL(NOTES,'') NOTES,ISNULL(DUEDATE,'') DUEDATE,ISNULL(SHIPTO,'')SHIPTO,ISNULL(PURCHASELOCATION,'') PURCHASELOCATION,ISNULL(BALANCEDUE,'') BALANCEDUE, "); 
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(DISCOUNT,'') DISCOUNT,ISNULL(ADJUSTMENT,'') ADJUSTMENT, ");
                 sql.append("ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCHARGE,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM, ISNULL(SHIPTO, '') AS SHIPTO,ISNULL(RCBNO, '') AS RCBNO,ISNULL(PRINTINCOTERM,'0') PRINTINCOTERM,ISNULL(SUPPLIERRCBNO,'') SUPPLIERRCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(SUPPLIERUENNO,'') AS SUPPLIERUENNO,ISNULL(GRNO,'') GRNO, ");
                 sql.append("ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(AUTHSIGNATURE,'') AS AUTHSIGNATURE,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(DATE,'') AS DATE,ISNULL(PAYMENTMADE,'') AS PAYMENTMADE ");
                 sql.append(" FROM " + "[" + plant + "_"+ "INBOUND_RECIPT_BILL_HDR] where plant='"+plant+"'");
          
                 this.mLogger.query(this.printQuery, sql.toString());
                 m = getRowOfData(con, sql.toString());

         } catch (Exception e) {

                 this.mLogger.exception(this.printLog, "", e);
                 throw e;
         } finally {
                 if (con != null) {
                         DbBean.closeConnection(con);
                 }
         }
         return m;

 }
 	public boolean updateBillHeader(String query, Hashtable htCondition, String extCond)
 			throws Exception {
 			boolean flag = false;
 			java.sql.Connection con = null;
 			try {
 			con = com.track.gates.DbBean.getConnection();
 			StringBuffer sql = new StringBuffer(" UPDATE " + "["
 			                + htCondition.get("PLANT") + "_INBOUND_RECIPT_BILL_HDR]");
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
 	
 	public int updateLandedCost(CostofgoodsLanded reqObj,String plant) {
 		int cnt=0;
 		PreparedStatement ps = null;
		Connection con = null;
 		try {
 			con = DbBean.getConnection();
	        
	        String sQry = "UPDATE [" + plant + "_FINBILLDET] SET LANDED_COST='"+(reqObj.getAvg_rate()-reqObj.getUnit_cost())+"'  WHERE BILLHDRID ='"+reqObj.getBillhdrid()+"' AND ITEM='"+reqObj.getProd_id()+"'";
	        ps = con.prepareStatement(sQry);
	        cnt = ps.executeUpdate();
 			
 		}catch (Exception e) {
			e.printStackTrace();
		} finally {
	        DbBean.closeConnection(con, ps);
	}
 		return cnt;
 	}
 	
 	public int addItemCogs(ItemCogs reqObj, String plant)throws Exception {
		int itemCogsCnt = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			if(reqObj.getItem()!=null) {
				connection = DbBean.getConnection();	
				boolean isChk=itemCogs(reqObj,plant);
				if(isChk) {
					query="INSERT INTO ["+ plant +"_FINITEMCOGSMAIN] (PLANT,ITEM,COGS_DATE,DESCRIPTION,OPS_QTY,OPS_AMOUNT,OPS_AVG,MOS_QTY,MOS_AMOUNT,MOS_AVG,COS_QTY,COS_AMOUNT,COS_AVG) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
					ps = connection.prepareStatement(query);
					ps.setString(1, reqObj.getPlant());
					ps.setString(2, reqObj.getItem());
					ps.setString(3, reqObj.getCOGS_DATE());
					ps.setString(4, reqObj.getDescription());
					ps.setDouble(5, reqObj.getOps_qty());
					ps.setDouble(6, reqObj.getOps_amount());
					ps.setDouble(7, reqObj.getOps_avg());
					ps.setDouble(8, reqObj.getMos_qty());
					ps.setDouble(9, reqObj.getMos_amount());
					ps.setDouble(10, reqObj.getMos_avg());
					ps.setDouble(11, reqObj.getCos_qty());
					ps.setDouble(12, reqObj.getCos_amount());
					ps.setDouble(13, reqObj.getCos_avg());
					this.mLogger.query(this.printQuery, query);
					itemCogsCnt=ps.executeUpdate();
				}else {
					query="UPDATE ["+ plant +"_FINITEMCOGSMAIN] SET COGS_DATE=?,DESCRIPTION=?,OPS_QTY=?,OPS_AMOUNT=?,OPS_AVG=?,MOS_QTY=?,MOS_AMOUNT=?,MOS_AVG=?,COS_QTY=?,COS_AMOUNT=?,COS_AVG=? WHERE ITEM=?";
					ps = connection.prepareStatement(query);
					ps.setString(1, reqObj.getCOGS_DATE());
					ps.setString(2, reqObj.getDescription());
					ps.setDouble(3, reqObj.getOps_qty());
					ps.setDouble(4, reqObj.getOps_amount());
					ps.setDouble(5, reqObj.getOps_avg());
					ps.setDouble(6, reqObj.getMos_qty());
					ps.setDouble(7, reqObj.getMos_amount());
					ps.setDouble(8, reqObj.getMos_avg());
					ps.setDouble(9, reqObj.getCos_qty());
					ps.setDouble(10, reqObj.getCos_amount());
					ps.setDouble(11, reqObj.getCos_avg());
					ps.setString(12, reqObj.getItem());
					this.mLogger.query(this.printQuery, query);
					itemCogsCnt=ps.executeUpdate();
				}
				if(itemCogsCnt>0) {
					int itemCogshistyCnt=insertToItemCogsHistory(reqObj,plant);
					System.out.println("ITEMCOGSMAIN_HISTORY inserted successfully "+itemCogshistyCnt);
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
		return itemCogsCnt;
	}
 
 	public boolean itemCogs(ItemCogs reqObj, String plant) throws Exception{
 		boolean isChk=false;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			connection = DbBean.getConnection();	
			String slcQuery="SELECT ITEM FROM ["+ plant +"_FINITEMCOGSMAIN] WHERE ITEM=?";
			ps = connection.prepareStatement(slcQuery);
			ps.setString(1, reqObj.getItem());
			rs=ps.executeQuery();
			if(!rs.next()) { // no data
				isChk=true;
			}else {
				isChk=false;
			}
			
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return isChk;
 	}
 	
  public ItemCogs getItemCogs(String reqObj, String plant) throws Exception{
 		ItemCogs itemCogs=null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			connection = DbBean.getConnection();	
			String slcQuery="SELECT ITEM,OPS_QTY,OPS_AMOUNT,OPS_AVG,MOS_QTY,MOS_AMOUNT,MOS_AVG,COS_QTY,COS_AMOUNT,COS_AVG FROM ["+ plant +"_FINITEMCOGSMAIN] WHERE ITEM=?";
			ps = connection.prepareStatement(slcQuery);
			ps.setString(1, reqObj);
			rs=ps.executeQuery();
			if(rs.next()) { 
				itemCogs=new ItemCogs();
				itemCogs.setOps_qty(rs.getDouble("OPS_QTY"));
				itemCogs.setOps_amount(rs.getDouble("OPS_AMOUNT"));
				itemCogs.setOps_avg(rs.getDouble("OPS_AVG"));
				itemCogs.setMos_qty(rs.getDouble("MOS_QTY"));
				itemCogs.setMos_amount(rs.getDouble("MOS_AMOUNT"));
				itemCogs.setMos_avg(rs.getDouble("MOS_AVG"));
				itemCogs.setCos_qty(rs.getDouble("COS_QTY"));
				itemCogs.setCos_amount(rs.getDouble("COS_AMOUNT"));
				itemCogs.setCos_avg(rs.getDouble("COS_AVG"));
				itemCogs.setItem(rs.getString("ITEM"));
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return itemCogs;
 	}
 	
 public int insertToItemCogsHistory(ItemCogs reqObj,String plant) throws Exception{
		int itemCnt=0;
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DbBean.getConnection();	
			String insrToHistory="INSERT INTO ["+ plant +"_FINITEMCOGSMAIN_HISTORY] (PLANT,ITEM,COGS_DATE,DESCRIPTION,OPS_QTY,OPS_AMOUNT,OPS_AVG,MOS_QTY,MOS_AMOUNT,MOS_AVG,COS_QTY,COS_AMOUNT,COS_AVG)\r\n" + 
					"	SELECT PLANT,ITEM,COGS_DATE,DESCRIPTION,OPS_QTY,OPS_AMOUNT,OPS_AVG,MOS_QTY,MOS_AMOUNT,MOS_AVG,COS_QTY,COS_AMOUNT,COS_AVG  FROM ["+ plant +"_FINITEMCOGSMAIN]  where item=? ";
			ps = connection.prepareStatement(insrToHistory);
			ps.setString(1, reqObj.getItem());
			itemCnt=ps.executeUpdate();
			
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return itemCnt;
 }
 
	public Map getBillDetailsByBill(String bill, String plant) throws Exception {
	     MLogger.log(1, this.getClass() + " getBillHeaderDetails()");
	     Map m = new HashMap();
	     java.sql.Connection con = null;
	     String scondtn ="";
	     try {
	         con = DbBean.getConnection();
	         StringBuffer sql = new StringBuffer("SELECT * FROM ["+plant+"_FINBILLHDR] WHERE BILL='"+bill+"' AND PLANT='"+plant+"'");
	         this.mLogger.query(this.printQuery, sql.toString());
	         m = getRowOfData(con, sql.toString());
	     } catch (Exception e) {
	         this.mLogger.exception(this.printLog, "", e);
	         throw e;
	     } finally {
	         if (con != null) {
	             DbBean.closeConnection(con);
	         }
	     }
	     return m;
	}
	
	 public boolean isBillNoexisit(String plant, String bill) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" FROM [" + plant+"_FINBILLHDR]");
				sql.append(" WHERE BILL='"+bill+"' AND PLANT='"+plant+"'");

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
	 
	 public boolean isGRNOexisit(String plant, String grno) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" FROM [" + plant+"_FINBILLHDR]");
				sql.append(" WHERE GRNO='"+grno+"' AND PLANT='"+plant+"'");

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
	 
	 public boolean isGRNOPaid(String plant, String grno) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" FROM [" + plant+"_FINBILLHDR]");
				sql.append(" WHERE GRNO='"+grno+"' AND PLANT='"+plant+"' AND (BILL_STATUS='Draft' or BILL_STATUS='Open')");

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
	 

	 
	 public List getBillHdrByDono(String plant,String pono) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> billHdrList = new ArrayList<>();
			Map<String, String> map = null;
			String query="";
			List<String> args = null;
			try {			
				/*Instantiate the list*/
			    args = new ArrayList<String>();
			    
				con = com.track.gates.DbBean.getConnection();
				query="SELECT * FROM ["+ plant +"_FINBILLHDR] WHERE PLANT=? AND PONO =?";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				args.add((String) plant);
				args.add((String) pono);

				
				this.mLogger.query(this.printQuery, query);
				
				billHdrList = selectData(ps, args);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return billHdrList;
		}
}
