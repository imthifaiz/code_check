package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class AgeingDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.AgeingDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.AgeingDAO_PRINTPLANTMASTERLOG;
	
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
	
	public boolean isExisittemp(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "FIN_TEMP_ORDER_PAYMENT_DET" + "]");
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
	
	public boolean deletetemporderpayment(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "FIN_TEMP_ORDER_PAYMENT_DET"
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
	
	public String formCondition(Hashtable<String, String> ht) throws Exception {
		String sCondition = "";
		try {
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			throw e;
		}
		return sCondition;
	}
	
	public boolean updateData(Connection conn, String sql) throws Exception {		
		boolean flag = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int updateCount = 0;
		try {
			stmt = conn.prepareStatement(sql);
			updateCount = stmt.executeUpdate();
			if (updateCount > 0) {
				flag = true;
			}
			else
				throw new Exception("Unable to update!");			
		} catch (Exception e) {
			MLogger.log(0, "Exception" + e.getMessage());
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return flag;
	}
	
	public boolean insertinvoicetemporderpayment(Hashtable ht,String cond) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
					
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_FIN_TEMP_ORDER_PAYMENT_DET] "
					+ "(PLANT,ROWID,ORDNO,ORDERNAME,PAYMENT_REFNO,AMOUNT_PAID,PAYMENT_DT,PAYMENT_MODE,ORDERTYPE,CustCode)" + 
					"  SELECT a.PLANT," + 
					"  ROW_NUMBER() OVER(ORDER BY CNAME,CAST(SUBSTRING(A.PAYMENT_DT, 7, 4) +'-'+ " + 
					"  SUBSTRING(A.PAYMENT_DT, 4,2) +'-'+ SUBSTRING(A.PAYMENT_DT, 1,2) AS date)) AS ID,"
					+ "a.DONO,a.INVOICE,a.REFERENCE,a.AMOUNT,a.PAYMENT_DT,a.PAYMENT_MODE,a.ORDERTYPE,a.CNAME FROM (" + 
					" SELECT  INVOICE_DATE as PAYMENT_DT,DONO,INVOICE, '' as REFERENCE," + 
					"  (SELECT CNAME FROM " + "[" + ht.get("PLANT") + "_CUSTMST] WHERE CUSTNO = A.CUSTNO) AS CNAME,"
					+ "TOTAL_AMOUNT AS AMOUNT, ''  AS PAYMENT_MODE,PLANT, '"+ht.get("ORDERNAME")+"' AS ORDERTYPE" + 
					" FROM [" + ht.get("PLANT") + "_FININVOICEHDR] A" + 
					" UNION ALL" + 
					" SELECT (SUBSTRING(B.CRAT, 7, 2) + '/' + SUBSTRING(B.CRAT, 5, 2) + '/' + SUBSTRING(B.CRAT, 1, 4)) AS PAYMENT_DT," + 
					"   ISNULL((SELECT DONO FROM [" + ht.get("PLANT") + "_FININVOICEHDR] WHERE ID = B.INVOICEHDRID),'') AS DONO,"+
					" ((SELECT INVOICE FROM " + "[" + ht.get("PLANT") + "_FININVOICEHDR] WHERE ID = B.INVOICEHDRID)) AS INVOICE, A.REFERENCE," + 
					"  (SELECT CNAME FROM "+"["+ ht.get("PLANT")+"_CUSTMST] WHERE CUSTNO = A.CUSTNO) AS CNAME," + 
					" B.AMOUNT AS AMOUNT, A.RECEIVE_MODE AS PAYMENT_MODE,A.PLANT, '"+ht.get("ORDERNAME")+"' AS ORDERTYPE "
					+ "FROM [" + ht.get("PLANT") + "_FINRECEIVEHDR] A JOIN " + "[" + ht.get("PLANT") + "_FINRECEIVEDET] B "
					+ "ON A.ID = B.RECEIVEHDRID WHERE B.TYPE='REGULAR') a where PLANT='"+ht.get("PLANT")+"' "+cond+"ORDER BY ID";
					
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
	
	public boolean inserttemporderpayment(Hashtable ht,String cond) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
					
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_" +
					"FIN_TEMP_ORDER_PAYMENT_DET" + "]" + "(" +
					"PLANT,ROWID,ORDNO,ORDERNAME,PAYMENT_REFNO,AMOUNT_PAID,PAYMENT_DT,PAYMENT_MODE,ORDERTYPE,CustCode)" +
					" SELECT a.PLANT," + 
					"  ROW_NUMBER() OVER(ORDER BY VENDORNAME,CAST(SUBSTRING(A.PAYMENT_DT, 7, 4) +'-'+ " + 
					" SUBSTRING(A.PAYMENT_DT, 4,2) +'-'+ SUBSTRING(A.PAYMENT_DT, 1,2) AS date)) AS ID," +
					" a.PONO,a.BILL,a.REFERENCE,a.AMOUNT,a.PAYMENT_DT,a.PAYMENT_MODE,a.ORDERTYPE,a.VENDORNAME FROM (" + 
					"  SELECT  BILL_DATE as PAYMENT_DT,PONO,BILL, '' as REFERENCE," + 
					"  (SELECT VNAME FROM [" + ht.get("PLANT") +"_VENDMST] WHERE VENDNO = A.VENDNO) AS VENDORNAME,"
					+ "TOTAL_AMOUNT AS AMOUNT, ''  AS PAYMENT_MODE,PLANT, '"+ht.get("ORDERNAME")+"' AS ORDERTYPE" + 
					" FROM [" + ht.get("PLANT") +"_FINBILLHDR] A " + 
					" UNION ALL " + 
					" SELECT (SUBSTRING(B.CRAT, 7, 2) + '/' + SUBSTRING(B.CRAT, 5, 2) + '/' + SUBSTRING(B.CRAT, 1, 4)) AS PAYMENT_DT," + 
					" ((SELECT PONO FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] WHERE ID = B.BILLHDRID)) AS PONO," + 
					" ((SELECT BILL FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] WHERE ID = B.BILLHDRID)) AS BILL, A.REFERENCE," + 
					" (SELECT VNAME FROM "+"["+ ht.get("PLANT")+"_VENDMST] WHERE VENDNO = A.VENDNO) AS VENDORNAME," + 
					" B.AMOUNT AS AMOUNT, A.PAID_THROUGH AS PAYMENT_MODE,A.PLANT, '"+ht.get("ORDERNAME")+"' AS ORDERTYPE "
					+ "FROM [" + ht.get("PLANT") +"_FINPAYMENTHDR] A JOIN [" + ht.get("PLANT") +"_FINPAYMENTDET] B "
					+ "ON A.ID = B.PAYHDRID WHERE B.TYPE='REGULAR') a where PLANT='"+ht.get("PLANT")+"' "+cond+" ORDER BY ID";
					
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
	
	public Map getAgingSummaryForReport(Hashtable ht) throws Exception{
		Map map = new HashMap();
        Connection con = null;
        String custno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	custno = (String)ht.get("CUSTNO");
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(custno.length()>0) {
        		condition1+=" AND A.CUSTNO='"+custno+"' ";
        		condition2+=" AND A.CUSTNO='"+custno+"' ";
        	}
        	
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT ISNULL(SUM(DAY0),0) AS DAY0,ISNULL(SUM(DAY1),0) AS DAY1,ISNULL(SUM(DAY2),0) AS DAY2,ISNULL(SUM(DAY3),0) AS DAY3,ISNULL(SUM(DAY4),0) AS DAY4," + 
            		"ISNULL((SUM(DAY0)+SUM(DAY1)+SUM(DAY2)+SUM(DAY3)+SUM(DAY4)),0) as TOTAL_DUE FROM (" + 
            		"SELECT " + 
            		"CASE WHEN (OVER_DUE = 0) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY0," + 
            		"CASE WHEN (OVER_DUE >0 AND OVER_DUE <= 30) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY1," + 
            		"CASE WHEN (OVER_DUE >30 AND OVER_DUE <= 60) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY2," + 
            		"CASE WHEN (OVER_DUE >60 AND OVER_DUE <= 90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY3," + 
            		"CASE WHEN (OVER_DUE >90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY4" + 
            		" FROM (" + 
            		"SELECT INVOICE_DATE,INVOICE," + 
            		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE INVOICE_DATE END AS DUE_DATE," + 
            		"ROUND(TOTAL_AMOUNT,6) AS INVOICE_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
            		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
            		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
            		" ELSE 0 END AS OVER_DUE," + 
            		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING  " + 
            		"FROM "+"["+ ht.get("PLANT")+"_FININVOICEHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.INVOICEHDRID WHERE A.PLANT = '"+ht.get("PLANT")+"'" + condition1 +
            		" GROUP BY INVOICE_DATE,INVOICE,DUE_DATE,TOTAL_AMOUNT" + 
            		" UNION ALL " + 
            		"SELECT RECEIVE_DATE AS INVOICE_DATE,REFERENCE AS INVOICE,RECEIVE_DATE AS DUE_DATE,ROUND(BALANCE,6) AS INVOICE_AMOUNT, 0 AS RECEIVED," + 
            		"DATEDIFF(day, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date)," + 
            		"CAST( GETDATE() AS Date )) AS OVER_DUE," + 
            		"ROUND(-BALANCE,6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINRECEIVEHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.RECEIVEHDRID " + 
            		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 " + condition2 +
            		") A WHERE INVOICE <> '' GROUP BY OVER_DUE " + 
            		") A");  
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
	
	public Map getAgingSummaryForReportBycurrecy(Hashtable ht,String currency) throws Exception{
		Map map = new HashMap();
        Connection con = null;
        String custno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	custno = (String)ht.get("CUSTNO");
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(custno.length()>0) {
        		condition1+=" AND A.CUSTNO='"+custno+"' AND A.CURRENCYID='"+currency+"'";
        		condition2+=" AND A.CUSTNO='"+custno+"' ";
        	}else {
        		condition1+=" AND A.CURRENCYID='"+currency+"'";
        	}
        	
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT ISNULL(SUM(DAY0),0) AS DAY0,ISNULL(SUM(DAY1),0) AS DAY1,ISNULL(SUM(DAY2),0) AS DAY2,ISNULL(SUM(DAY3),0) AS DAY3,ISNULL(SUM(DAY4),0) AS DAY4," + 
            		"ISNULL((SUM(DAY0)+SUM(DAY1)+SUM(DAY2)+SUM(DAY3)+SUM(DAY4)),0) as TOTAL_DUE FROM (" + 
            		"SELECT " + 
            		"CASE WHEN (OVER_DUE = 0) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY0," + 
            		"CASE WHEN (OVER_DUE >0 AND OVER_DUE <= 30) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY1," + 
            		"CASE WHEN (OVER_DUE >30 AND OVER_DUE <= 60) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY2," + 
            		"CASE WHEN (OVER_DUE >60 AND OVER_DUE <= 90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY3," + 
            		"CASE WHEN (OVER_DUE >90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY4" + 
            		" FROM (" + 
            		"SELECT INVOICE_DATE,INVOICE,A.CURRENCYID AS CURRENCY," + 
            		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE INVOICE_DATE END AS DUE_DATE," + 
            		" (TOTAL_AMOUNT*A.CURRENCYUSEQT) AS INVOICE_AMOUNT, ROUND(SUM(ISNULL((AMOUNT*A.CURRENCYUSEQT),0)),6) AS RECEIVED," + 
            		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
            		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
            		" ELSE 0 END AS OVER_DUE," + 
            		" ROUND((TOTAL_AMOUNT*A.CURRENCYUSEQT)-SUM(ISNULL((AMOUNT*A.CURRENCYUSEQT),0)),6) AS OUTSTANDING  " + 
            		"FROM "+"["+ ht.get("PLANT")+"_FININVOICEHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.INVOICEHDRID WHERE A.PLANT = '"+ht.get("PLANT")+"'" + condition1 +
            		" GROUP BY INVOICE_DATE,INVOICE,DUE_DATE,TOTAL_AMOUNT,A.CURRENCYUSEQT,A.CURRENCYID" + 
            		" UNION ALL " + 
            		"SELECT RECEIVE_DATE AS INVOICE_DATE,REFERENCE AS INVOICE,A.CURRENCYID AS CURRENCY,RECEIVE_DATE AS DUE_DATE, (BALANCE*B.CURRENCYUSEQT) AS INVOICE_AMOUNT, 0 AS RECEIVED," + 
            		"DATEDIFF(day, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date)," + 
            		"CAST( GETDATE() AS Date )) AS OVER_DUE," + 
            		"ROUND(-(BALANCE*B.CURRENCYUSEQT),6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINRECEIVEHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.RECEIVEHDRID " + 
            		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 " + condition2 +
            		") A WHERE INVOICE <> '' GROUP BY OVER_DUE " + 
            		") A");  
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
	
	public ArrayList getAgingDetailForReport(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
        Connection con = null;
        String custno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	custno = (String)ht.get("CUSTNO");
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(custno.length()>0) {
        		condition1+=" AND A.CUSTNO='"+custno+"' ";
        		condition2+=" AND A.CUSTNO='"+custno+"' ";
        	}
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT INVOICE_DATE, INVOICE, DUE_DATE, INVOICE_AMOUNT, RECEIVED, OVER_DUE, OUTSTANDING,CURRENCY,CONV_INVOICE_AMOUNT,CONV_RECEIVED,CONV_OUTSTANDING," + 
        		"SUM(OUTSTANDING) OVER (ORDER BY CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date), INVOICE) AS CUMM_BAL " + 
        		" FROM (" + 
        		"SELECT INVOICE_DATE,INVOICE,A.CURRENCYID AS CURRENCY,(TOTAL_AMOUNT*A.CURRENCYUSEQT) AS CONV_INVOICE_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)*ISNULL(B.CURRENCYUSEQT,1)),6) AS CONV_RECEIVED,ROUND((TOTAL_AMOUNT*A.CURRENCYUSEQT)-SUM(ISNULL(AMOUNT,0)*ISNULL(B.CURRENCYUSEQT,1)),6) AS CONV_OUTSTANDING ," + 
        		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE INVOICE_DATE END AS DUE_DATE," + 
        		"ROUND(TOTAL_AMOUNT,6) AS INVOICE_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
        		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
        		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
        		" ELSE 0 END AS OVER_DUE," + 
        		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING " + 
        		"FROM "+"["+ ht.get("PLANT")+"_FININVOICEHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.INVOICEHDRID WHERE A.PLANT='"+ht.get("PLANT")+"' AND A.BILL_STATUS!='Draft' " + condition1 +
        		"GROUP BY INVOICE_DATE,INVOICE,DUE_DATE,TOTAL_AMOUNT,A.CURRENCYUSEQT,A.CURRENCYID" + 
        		" UNION ALL " + 
        		"SELECT RECEIVE_DATE AS INVOICE_DATE,REFERENCE AS INVOICE,A.CURRENCYID AS CURRENCY,(BALANCE*B.CURRENCYUSEQT) AS CONV_INVOICE_AMOUNT,0 AS CONV_RECEIVED,ROUND(-(BALANCE*B.CURRENCYUSEQT),6) AS CONV_OUTSTANDING,RECEIVE_DATE AS DUE_DATE,ROUND(BALANCE,6) AS INVOICE_AMOUNT," + 
        		" 0 AS RECEIVED," + 
        		"DATEDIFF(day, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) , CAST( GETDATE() AS Date )) AS OVER_DUE," + 
        		"ROUND(-BALANCE,6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINRECEIVEHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.RECEIVEHDRID " + 
        		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 AND A.PLANT ='"+ht.get("PLANT")+"' " + condition2 +
        		") A WHERE INVOICE <> '' " + 
        		"ORDER BY CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date), INVOICE");  
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
	
	public ArrayList getAgingDetailForReportByCurrency(Hashtable ht,String currency) throws Exception{
		ArrayList al = new ArrayList();
        Connection con = null;
        String custno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	custno = (String)ht.get("CUSTNO");
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(custno.length()>0) {
        		condition1+=" AND A.CUSTNO='"+custno+"' AND A.CURRENCYID='"+currency+"'";
        		condition2+=" AND A.CUSTNO='"+custno+"' ";
        	}else {
        		condition1+=" AND A.CURRENCYID='"+currency+"'";
        	}
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT INVOICE_DATE, INVOICE, DUE_DATE, INVOICE_AMOUNT, RECEIVED, OVER_DUE, OUTSTANDING,CURRENCY,CONV_INVOICE_AMOUNT,CONV_RECEIVED,CONV_OUTSTANDING," + 
        		"SUM(CONV_OUTSTANDING) OVER (ORDER BY CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date), INVOICE) AS CUMM_BAL " + 
        		" FROM (" + 
        		"SELECT INVOICE_DATE,INVOICE,A.CURRENCYID AS CURRENCY,(TOTAL_AMOUNT*A.CURRENCYUSEQT) AS CONV_INVOICE_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)*ISNULL(B.CURRENCYUSEQT,1)),6) AS CONV_RECEIVED,ROUND((TOTAL_AMOUNT*A.CURRENCYUSEQT)-SUM(ISNULL(AMOUNT,0)*ISNULL(B.CURRENCYUSEQT,1)),6) AS CONV_OUTSTANDING ," + 
        		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE INVOICE_DATE END AS DUE_DATE," + 
        		"ROUND(TOTAL_AMOUNT,6) AS INVOICE_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
        		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
        		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
        		" ELSE 0 END AS OVER_DUE," + 
        		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING " + 
        		"FROM "+"["+ ht.get("PLANT")+"_FININVOICEHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.INVOICEHDRID WHERE A.PLANT='"+ht.get("PLANT")+"' " + condition1 +
        		"GROUP BY INVOICE_DATE,INVOICE,DUE_DATE,TOTAL_AMOUNT,A.CURRENCYUSEQT,A.CURRENCYID" + 
        		" UNION ALL " + 
        		"SELECT RECEIVE_DATE AS INVOICE_DATE,REFERENCE AS INVOICE,A.CURRENCYID AS CURRENCY,(BALANCE*B.CURRENCYUSEQT) AS CONV_INVOICE_AMOUNT,0 AS CONV_RECEIVED,ROUND(-(BALANCE*B.CURRENCYUSEQT),6) AS CONV_OUTSTANDING,RECEIVE_DATE AS DUE_DATE,ROUND(BALANCE,6) AS INVOICE_AMOUNT," + 
        		" 0 AS RECEIVED," + 
        		"DATEDIFF(day, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) , CAST( GETDATE() AS Date )) AS OVER_DUE," + 
        		"ROUND(-BALANCE,6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINRECEIVEHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.RECEIVEHDRID " + 
        		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 AND A.PLANT ='"+ht.get("PLANT")+"' " + condition2 +
        		") A WHERE INVOICE <> '' " + 
        		"ORDER BY CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date), INVOICE");  
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
	
	public Map getSupplierAgingSummaryForReport(Hashtable ht) throws Exception{
		Map map = new HashMap();
        Connection con = null;
        String vendno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	vendno = (String)ht.get("VENDNO");
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(vendno.length()>0) {
        		condition1+=" AND A.VENDNO='"+vendno+"' ";
        		condition2+=" AND A.VENDNO='"+vendno+"' ";
        	}
        	
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT ISNULL(SUM(DAY0),0) AS DAY0,ISNULL(SUM(DAY1),0) AS DAY1,ISNULL(SUM(DAY2),0) AS DAY2,ISNULL(SUM(DAY3),0) AS DAY3,ISNULL(SUM(DAY4),0) AS DAY4," + 
            		"ISNULL((SUM(DAY0)+SUM(DAY1)+SUM(DAY2)+SUM(DAY3)+SUM(DAY4)),0) as TOTAL_DUE FROM (" + 
            		"SELECT " + 
            		"CASE WHEN (OVER_DUE = 0) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY0," + 
            		"CASE WHEN (OVER_DUE >0 AND OVER_DUE <= 30) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY1," + 
            		"CASE WHEN (OVER_DUE >30 AND OVER_DUE <= 60) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY2," + 
            		"CASE WHEN (OVER_DUE >60 AND OVER_DUE <= 90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY3," + 
            		"CASE WHEN (OVER_DUE >90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY4" + 
            		" FROM (" + 
            		"SELECT BILL_DATE,BILL," + 
            		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE BILL_DATE END AS DUE_DATE," + 
            		"ROUND(TOTAL_AMOUNT,6) AS BILL_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
            		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
            		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
            		" ELSE 0 END AS OVER_DUE," + 
            		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING  " + 
            		"FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.BILLHDRID WHERE A.PLANT = '"+ht.get("PLANT")+"'" + condition1 +
            		" GROUP BY BILL_DATE,BILL,DUE_DATE,TOTAL_AMOUNT" + 
            		" UNION ALL " + 
            		"SELECT PAYMENT_DATE AS BILL_DATE,REFERENCE AS BILL,PAYMENT_DATE AS DUE_DATE,ROUND(BALANCE,6) AS BILL_AMOUNT, 0 AS RECEIVED," + 
            		"DATEDIFF(day, CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date)," + 
            		"CAST( GETDATE() AS Date )) AS OVER_DUE," + 
            		"ROUND(-BALANCE,6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINPAYMENTHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.PAYHDRID " + 
            		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 " + condition2 +
            		") A WHERE BILL <> '' GROUP BY OVER_DUE " + 
            		") A");  
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
	
	
	public Map getSupplierAgingSummaryForReportByCurrency(Hashtable ht,String currency) throws Exception{
		Map map = new HashMap();
        Connection con = null;
        String vendno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	vendno = (String)ht.get("VENDNO");
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(vendno.length()>0) {
        		condition1+=" AND A.VENDNO='"+vendno+"' AND A.CURRENCYID='"+currency+"'";
        		condition2+=" AND A.VENDNO='"+vendno+"' ";
        	}else {
        		condition1+=" AND A.CURRENCYID='"+currency+"'";
        	}
        	
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT ISNULL(SUM(DAY0),0) AS DAY0,ISNULL(SUM(DAY1),0) AS DAY1,ISNULL(SUM(DAY2),0) AS DAY2,ISNULL(SUM(DAY3),0) AS DAY3,ISNULL(SUM(DAY4),0) AS DAY4," + 
            		"ISNULL((SUM(DAY0)+SUM(DAY1)+SUM(DAY2)+SUM(DAY3)+SUM(DAY4)),0) as TOTAL_DUE FROM (" + 
            		"SELECT " + 
            		"CASE WHEN (OVER_DUE = 0) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY0," + 
            		"CASE WHEN (OVER_DUE >0 AND OVER_DUE <= 30) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY1," + 
            		"CASE WHEN (OVER_DUE >30 AND OVER_DUE <= 60) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY2," + 
            		"CASE WHEN (OVER_DUE >60 AND OVER_DUE <= 90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY3," + 
            		"CASE WHEN (OVER_DUE >90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY4" + 
            		" FROM (" + 
            		"SELECT BILL_DATE,BILL," + 
            		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE BILL_DATE END AS DUE_DATE," + 
            		"ROUND(TOTAL_AMOUNT*(ISNULL(A.CURRENCYUSEQT,1)),6) AS BILL_AMOUNT,ROUND(SUM(ISNULL(AMOUNT*(ISNULL(A.CURRENCYUSEQT,1)),0)),6) AS RECEIVED," + 
            		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
            		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
            		" ELSE 0 END AS OVER_DUE," + 
            		" ROUND((TOTAL_AMOUNT*(ISNULL(A.CURRENCYUSEQT,1)))-SUM(ISNULL(AMOUNT*(ISNULL(A.CURRENCYUSEQT,1)),0)),6) AS OUTSTANDING  " + 
            		"FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.BILLHDRID WHERE A.PLANT = '"+ht.get("PLANT")+"'" + condition1 +
            		" GROUP BY BILL_DATE,BILL,DUE_DATE,TOTAL_AMOUNT,A.CURRENCYUSEQT" + 
            		" UNION ALL " + 
            		"SELECT PAYMENT_DATE AS BILL_DATE,REFERENCE AS BILL,PAYMENT_DATE AS DUE_DATE,ROUND(BALANCE*(ISNULL(B.CURRENCYUSEQT,1)),6) AS BILL_AMOUNT, 0 AS RECEIVED," + 
            		"DATEDIFF(day, CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date)," + 
            		"CAST( GETDATE() AS Date )) AS OVER_DUE," + 
            		"ROUND(-(BALANCE*(ISNULL(B.CURRENCYUSEQT,1))),6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINPAYMENTHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.PAYHDRID " + 
            		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 " + condition2 +
            		") A WHERE BILL <> '' GROUP BY OVER_DUE " + 
            		") A");  
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
	
	public ArrayList getSupplierAgingDetailForReport(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
        Connection con = null;
        String vendno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	vendno = (String)ht.get("VENDNO");
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(vendno.length()>0) {
        		condition1+=" AND A.VENDNO='"+vendno+"' ";
        		condition2+=" AND A.VENDNO='"+vendno+"' ";
        	}
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT BILL_DATE, BILL, DUE_DATE, BILL_AMOUNT, RECEIVED, OVER_DUE, OUTSTANDING," + 
        		"SUM(OUTSTANDING) OVER (ORDER BY CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date), BILL) AS CUMM_BAL " + 
        		" FROM (" + 
        		"SELECT BILL_DATE,BILL," + 
        		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE BILL_DATE END AS DUE_DATE," + 
        		"ROUND(TOTAL_AMOUNT,6) AS BILL_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
        		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
        		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
        		" ELSE 0 END AS OVER_DUE," + 
        		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING " + 
        		"FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.BILLHDRID WHERE A.PLANT='"+ht.get("PLANT")+"' " + condition1 +
        		"GROUP BY BILL_DATE,BILL,DUE_DATE,TOTAL_AMOUNT" + 
        		" UNION ALL " + 
        		"SELECT PAYMENT_DATE AS BILL_DATE,REFERENCE AS BILL,PAYMENT_DATE AS DUE_DATE,ROUND(BALANCE,6) AS BILL_AMOUNT," + 
        		" 0 AS RECEIVED," + 
        		"DATEDIFF(day, CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) , CAST( GETDATE() AS Date )) AS OVER_DUE," + 
        		"ROUND(-BALANCE,6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINPAYMENTHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.PAYHDRID " + 
        		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 AND A.PLANT ='"+ht.get("PLANT")+"' " + condition2 +
        		") A WHERE BILL <> '' " + 
        		"ORDER BY CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date), BILL");  
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
	
	public ArrayList getSupplierAgingDetailForReportBycurrency(Hashtable ht,String currency) throws Exception{
		ArrayList al = new ArrayList();
        Connection con = null;
        String vendno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	vendno = (String)ht.get("VENDNO");
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(vendno.length()>0) {
        		condition1+=" AND A.VENDNO='"+vendno+"' AND A.CURRENCYID='"+currency+"' ";
        		condition2+=" AND A.VENDNO='"+vendno+"' ";
        	}else {
        		condition1+=" AND  A.CURRENCYID='"+currency+"' ";
        	}
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT BILL_DATE, BILL,REFNUMBER, DUE_DATE, BILL_AMOUNT, RECEIVED, OVER_DUE, OUTSTANDING,CURRENCY," + 
        		"SUM(OUTSTANDING) OVER (ORDER BY CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date), BILL) AS CUMM_BAL " + 
        		" FROM (" + 
        		"SELECT BILL_DATE,BILL,ISNULL(A.REFERENCE_NUMBER,'') AS REFNUMBER," + 
        		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE BILL_DATE END AS DUE_DATE,A.CURRENCYID AS CURRENCY," + 
        		"ROUND(TOTAL_AMOUNT*(ISNULL(A.CURRENCYUSEQT,1)),6) AS BILL_AMOUNT,ROUND(SUM(ISNULL(AMOUNT*(ISNULL(A.CURRENCYUSEQT,1)),0)),6) AS RECEIVED," + 
        		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
        		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
        		" ELSE 0 END AS OVER_DUE," + 
        		" ROUND((TOTAL_AMOUNT*(ISNULL(A.CURRENCYUSEQT,1)))-SUM(ISNULL(AMOUNT*(ISNULL(A.CURRENCYUSEQT,1)),0)),6) AS OUTSTANDING " + 
        		"FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.BILLHDRID WHERE A.PLANT='"+ht.get("PLANT")+"' " + condition1 +
        		"GROUP BY BILL_DATE,BILL,A.REFERENCE_NUMBER,DUE_DATE,TOTAL_AMOUNT,A.CURRENCYID,A.CURRENCYUSEQT" + 
        		" UNION ALL " + 
        		"SELECT PAYMENT_DATE AS BILL_DATE,REFERENCE AS BILL,NOTE AS REFERENCE_NUMBER,PAYMENT_DATE AS DUE_DATE,A.CURRENCYID AS CURRENCY,ROUND(BALANCE*(ISNULL(B.CURRENCYUSEQT,1)),6) AS BILL_AMOUNT," + 
        		" 0 AS RECEIVED," + 
        		"DATEDIFF(day, CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) , CAST( GETDATE() AS Date )) AS OVER_DUE," + 
        		"ROUND(-(BALANCE*(ISNULL(B.CURRENCYUSEQT,1))),6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINPAYMENTHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.PAYHDRID " + 
        		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 AND A.PLANT ='"+ht.get("PLANT")+"' " + condition2 +
        		") A WHERE BILL <> '' " + 
        		"ORDER BY CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date), BILL");  
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
	
	public ArrayList getAgingSummaryForDashboard(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
        Connection con = null;
        String fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT TOP 5 ISNULL((SELECT CNAME FROM "+ ht.get("PLANT")+"_CUSTMST WHERE CUSTNO = A.CUSTNO),'') AS NAME,ISNULL(SUM(DAY0),0) AS DAY0,ISNULL(SUM(DAY1),0) AS DAY1,ISNULL(SUM(DAY2),0) AS DAY2,ISNULL(SUM(DAY3),0) AS DAY3,ISNULL(SUM(DAY4),0) AS DAY4," + 
            		"ISNULL((SUM(DAY0)+SUM(DAY1)+SUM(DAY2)+SUM(DAY3)+SUM(DAY4)),0) as TOTAL_DUE FROM (" + 
            		"SELECT CUSTNO," + 
            		"CASE WHEN (OVER_DUE = 0) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY0," + 
            		"CASE WHEN (OVER_DUE >0 AND OVER_DUE <= 30) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY1," + 
            		"CASE WHEN (OVER_DUE >30 AND OVER_DUE <= 60) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY2," + 
            		"CASE WHEN (OVER_DUE >60 AND OVER_DUE <= 90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY3," + 
            		"CASE WHEN (OVER_DUE >90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY4" + 
            		" FROM (" + 
            		"SELECT A.CUSTNO,INVOICE_DATE,INVOICE," + 
            		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE INVOICE_DATE END AS DUE_DATE," + 
            		"ROUND(TOTAL_AMOUNT,6) AS INVOICE_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
            		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
            		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
            		" ELSE 0 END AS OVER_DUE," + 
            		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING  " + 
            		"FROM "+"["+ ht.get("PLANT")+"_FININVOICEHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.INVOICEHDRID WHERE A.PLANT = '"+ht.get("PLANT")+"'" + condition1 +
            		" GROUP BY CUSTNO,INVOICE_DATE,INVOICE,DUE_DATE,TOTAL_AMOUNT" + 
            		" UNION ALL " + 
            		"SELECT A.CUSTNO,RECEIVE_DATE AS INVOICE_DATE,REFERENCE AS INVOICE,RECEIVE_DATE AS DUE_DATE,ROUND(BALANCE,6) AS INVOICE_AMOUNT, 0 AS RECEIVED," + 
            		"DATEDIFF(day, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date)," + 
            		"CAST( GETDATE() AS Date )) AS OVER_DUE," + 
            		"ROUND(-BALANCE,6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINRECEIVEHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.RECEIVEHDRID " + 
            		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 " + condition2 +
            		") A WHERE INVOICE <> '' GROUP BY CUSTNO,OVER_DUE " + 
            		") A GROUP BY CUSTNO ORDER BY TOTAL_DUE DESC");  
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
	
	public ArrayList getSupplierAgingSummaryForDashboard(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
        Connection con = null;
        String vendno="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT TOP 5 ISNULL((SELECT VNAME FROM "+"["+ ht.get("PLANT")+"_VENDMST] WHERE VENDNO = A.VENDNO),'') NAME, ISNULL(SUM(DAY0),0) AS DAY0,ISNULL(SUM(DAY1),0) AS DAY1,ISNULL(SUM(DAY2),0) AS DAY2,ISNULL(SUM(DAY3),0) AS DAY3,ISNULL(SUM(DAY4),0) AS DAY4," + 
            		"ISNULL((SUM(DAY0)+SUM(DAY1)+SUM(DAY2)+SUM(DAY3)+SUM(DAY4)),0) as TOTAL_DUE FROM (" + 
            		"SELECT VENDNO," + 
            		"CASE WHEN (OVER_DUE = 0) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY0," + 
            		"CASE WHEN (OVER_DUE >0 AND OVER_DUE <= 30) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY1," + 
            		"CASE WHEN (OVER_DUE >30 AND OVER_DUE <= 60) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY2," + 
            		"CASE WHEN (OVER_DUE >60 AND OVER_DUE <= 90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY3," + 
            		"CASE WHEN (OVER_DUE >90) THEN SUM(OUTSTANDING) ELSE 0 END AS DAY4" + 
            		" FROM (" + 
            		"SELECT VENDNO,BILL_DATE,BILL," + 
            		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE BILL_DATE END AS DUE_DATE," + 
            		"ROUND(TOTAL_AMOUNT,6) AS BILL_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
            		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
            		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
            		" ELSE 0 END AS OVER_DUE," + 
            		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING  " + 
            		"FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.BILLHDRID WHERE A.PLANT = '"+ht.get("PLANT")+"'" + condition1 +
            		" GROUP BY VENDNO,BILL_DATE,BILL,DUE_DATE,TOTAL_AMOUNT" + 
            		" UNION ALL " + 
            		"SELECT VENDNO,PAYMENT_DATE AS BILL_DATE,REFERENCE AS BILL,PAYMENT_DATE AS DUE_DATE,ROUND(BALANCE,6) AS BILL_AMOUNT, 0 AS RECEIVED," + 
            		"DATEDIFF(day, CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date)," + 
            		"CAST( GETDATE() AS Date )) AS OVER_DUE," + 
            		"ROUND(-BALANCE,6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINPAYMENTHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.PAYHDRID " + 
            		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 " + condition2 +
            		") A WHERE BILL <> '' GROUP BY VENDNO,OVER_DUE " + 
            		") A GROUP BY VENDNO ORDER BY TOTAL_DUE DESC");  
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
	
	public ArrayList getAccountPayableForDashboard(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
        Connection con = null;
        String vendname="", fromDate="", toDate="", condition1="", condition2 = "";
        try {
        	con = DbBean.getConnection();
        	
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	vendname = (String)ht.get("VENDNAME");
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT NAME, ISNULL(SUM(UNDER_DUE),0) AS UNDER_DUE,ISNULL(SUM(OVER_DUE),0) AS OVER_DUE," + 
            		"ISNULL((SUM(UNDER_DUE)+SUM(OVER_DUE)),0) as TOTAL_DUE FROM (" + 
            		"SELECT ISNULL((SELECT VNAME FROM "+"["+ ht.get("PLANT")+"_VENDMST] WHERE VENDNO = A.VENDNO),'') NAME," + 
            		"CASE WHEN (OVER_DUE <= 0) THEN SUM(OUTSTANDING) ELSE 0 END AS UNDER_DUE," + 
            		"CASE WHEN (OVER_DUE > 0) THEN SUM(OUTSTANDING) ELSE 0 END AS OVER_DUE" + 
            		" FROM (" + 
            		"SELECT VENDNO,BILL_DATE,BILL," + 
            		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE BILL_DATE END AS DUE_DATE," + 
            		"ROUND(TOTAL_AMOUNT,6) AS BILL_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
            		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
            		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
            		" ELSE 0 END AS OVER_DUE," + 
            		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING  " + 
            		"FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.BILLHDRID WHERE A.PLANT = '"+ht.get("PLANT")+"' AND A.BILL_STATUS!='Draft' " + condition1 +
            		" GROUP BY VENDNO,BILL_DATE,BILL,DUE_DATE,TOTAL_AMOUNT" + 
            		" UNION ALL " + 
            		"SELECT VENDNO,PAYMENT_DATE AS BILL_DATE,REFERENCE AS BILL,PAYMENT_DATE AS DUE_DATE,ROUND(BALANCE,6) AS BILL_AMOUNT, 0 AS RECEIVED," + 
            		"DATEDIFF(day, CAST((SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2)) AS date)," + 
            		"CAST( GETDATE() AS Date )) AS OVER_DUE," + 
            		"ROUND(-BALANCE,6) AS OUTSTANDING FROM "+"["+ ht.get("PLANT")+"_FINPAYMENTHDR] A JOIN "+"["+ ht.get("PLANT")+"_FINPAYMENTDET] B ON A.ID=B.PAYHDRID " + 
            		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 " + condition2 +
            		") A WHERE BILL <> '' GROUP BY VENDNO,OVER_DUE " + 
            		") A WHERE NAME LIKE '%"+vendname+"%' GROUP BY NAME ORDER BY TOTAL_DUE DESC");  
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
	
	public ArrayList getAccountReceivableForDashboard(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
        Connection con = null;
        String fromDate="", toDate="", condition1="", condition2 = "", custname = "";
        try {
        	con = DbBean.getConnection();
        	
        	fromDate = (String)ht.get("FROMDATE");
        	toDate = (String)ht.get("TODATE");
        	custname = (String)ht.get("CNAME");
        	if(fromDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) >= '"+fromDate+"' ";
        	}        	
        	if(toDate.length()>0) {
        		condition1+=" AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        		condition2+=" AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '"+toDate+"' ";
        	}
            
            StringBuffer sql = new StringBuffer("SELECT NAME, ISNULL(SUM(UNDER_DUE),0) AS UNDER_DUE,ISNULL(SUM(OVER_DUE),0) AS OVER_DUE," + 
            		"ISNULL((SUM(UNDER_DUE)+SUM(OVER_DUE)),0) as TOTAL_DUE FROM (" + 
            		"SELECT ISNULL((SELECT CNAME FROM ["+ ht.get("PLANT") + "_CUSTMST] WHERE CUSTNO = A.CUSTNO),'') AS NAME," + 
            		"CASE WHEN (OVER_DUE <= 0) THEN SUM(OUTSTANDING) ELSE 0 END AS UNDER_DUE," + 
            		"CASE WHEN (OVER_DUE > 0) THEN SUM(OUTSTANDING) ELSE 0 END AS OVER_DUE" + 
            		" FROM (" + 
            		"SELECT A.CUSTNO,INVOICE_DATE,INVOICE," + 
            		"CASE WHEN (LEN(DUE_DATE)>0) THEN DUE_DATE ELSE INVOICE_DATE END AS DUE_DATE," + 
            		"ROUND(TOTAL_AMOUNT,6) AS INVOICE_AMOUNT,ROUND(SUM(ISNULL(AMOUNT,0)),6) AS RECEIVED," + 
            		"CASE WHEN (TOTAL_AMOUNT != SUM(ISNULL(AMOUNT,0))) THEN" + 
            		" DATEDIFF(day,CASE WHEN (LEN(DUE_DATE)>0) THEN CAST((SUBSTRING(DUE_DATE, 7, 4) + '-' + SUBSTRING(DUE_DATE, 4, 2) + '-' + SUBSTRING(DUE_DATE, 1, 2)) AS date) ELSE CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date) END,CAST( GETDATE() AS Date ))" + 
            		" ELSE 0 END AS OVER_DUE," + 
            		" ROUND(TOTAL_AMOUNT-SUM(ISNULL(AMOUNT,0)),6) AS OUTSTANDING  " + 
            		"FROM "+"["+ ht.get("PLANT")+"_FININVOICEHDR] A LEFT JOIN "+"["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.INVOICEHDRID WHERE A.PLANT = '"+ht.get("PLANT")+"' AND A.BILL_STATUS!='Draft' " + condition1 +
            		" GROUP BY CUSTNO,INVOICE_DATE,INVOICE,DUE_DATE,TOTAL_AMOUNT" + 
            		" UNION ALL " + 
            		"SELECT A.CUSTNO,RECEIVE_DATE AS INVOICE_DATE,REFERENCE AS INVOICE,RECEIVE_DATE AS DUE_DATE,ROUND(BALANCE,6) AS INVOICE_AMOUNT, 0 AS RECEIVED," + 
            		"DATEDIFF(day, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date)," + 
            		"CAST( GETDATE() AS Date )) AS OVER_DUE," + 
            		"ROUND(-BALANCE,6) AS OUTSTANDING FROM ["+ ht.get("PLANT")+"_FINRECEIVEHDR] A JOIN ["+ ht.get("PLANT")+"_FINRECEIVEDET] B ON A.ID=B.RECEIVEHDRID " + 
            		"WHERE  TYPE='ADVANCE' AND BALANCE > 0 " + condition2 +
            		") A WHERE INVOICE <> '' GROUP BY CUSTNO,OVER_DUE " + 
            		") A WHERE NAME LIKE '%"+custname+"%' GROUP BY NAME ORDER BY TOTAL_DUE DESC");  
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
}
