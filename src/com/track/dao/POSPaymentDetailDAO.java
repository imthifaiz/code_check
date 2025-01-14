package com.track.dao;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class POSPaymentDetailDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POSPaymentDetailDAO_PRINTPLANTMASTERQUERY;
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
	private boolean printLog = MLoggerConstant.POSPaymentDetailDAO_PRINTPLANTMASTERLOG;
	public static final String TABLE_NAME = "POS_PAYMENT_DETAILS";
	public boolean insertPOSPaymentDetails(Hashtable ht) throws Exception {
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
					+ TABLE_NAME + "]" + "("
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
	
	public ArrayList getPaymentDetails(String plant,String receiptNo,String condtn) throws Exception {

		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("RECEIPTNO,PAYMENTMODE,AMOUNT");
			sql.append(" FROM " + "[" + plant + "_"
					+ TABLE_NAME+"] where plant='"+plant+"'"+" and RECEIPTNO='"+receiptNo+"'"+scondtn);
			
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
