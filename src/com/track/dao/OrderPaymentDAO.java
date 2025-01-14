package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class OrderPaymentDAO extends BaseDAO {
	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.OrderPaymentDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.OrderPaymentDAO_PRINTPLANTMASTERLOG;

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

	public OrderPaymentDAO() {
		_StrUtils = new StrUtils();
	}
	
	public boolean insertIntoOrderPaymentDet(Hashtable<String,String> ht) throws Exception {
		boolean insertRecord = false;
		java.sql.Connection con = null;
		try {

			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration<String> enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString(enum1.nextElement());
				String value = StrUtils.fString(ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value.toUpperCase() + "',";
			}
			String sql = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "ORDER_PAYMENT_DET" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, sql);
			insertRecord = insertData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertRecord;
	}
	

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
	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "ORDER_PAYMENT_DET" + "]");
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
	
	public boolean insertpaymentMst(Hashtable ht) throws Exception {
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
					+ "PAYMENT_MST" + "]" + "("
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

	public boolean isExistpaymentMst(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PAYMENT_MST"
					+ "]");
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

	public boolean updatepaymentMst(Hashtable htUpdate, Hashtable htCondition)
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
					+ "PAYMENT_MST" + "]" + sUpdate + sCondition;
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

	public boolean deletepaymentId(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PAYMENT_MST"
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

public ArrayList getPaymentIdDetails(String paymentid, String plant, String cond)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct payment_id,payment_desc,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "PAYMENT_MST] where payment_id like '"
					+ paymentid
					+ "%' " + cond
					+ " ORDER BY payment_id ";
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

public String gettotalpaidamt(String plant, String orderno) {
	boolean insertFlag = false;
	Connection conn = null;
	try {
		conn = DbBean.getConnection();

	
		String query = " SELECT SUM(AMOUNT_PAID) as Totpaid  FROM [" + plant + "_"
		+ "ORDER_PAYMENT_DET] WHERE PLANT='" + plant
		+ "' AND ORDNO = '" + orderno + "'"; 

		System.out.println(query);
		this.mLogger.query(this.printQuery, query);
		Map m = getRowOfData(conn, query);

		return (String) m.get("Totpaid");
	} catch (Exception e) {
		
		return null;
	} finally {
		if (conn != null) {
			DbBean.closeConnection(conn);
		}
	}

}


public boolean updatePaymentDetails(String query, Hashtable htCondition, String extCond)
		throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" UPDATE " + "["
				+ htCondition.get("PLANT") + "_" + "ORDER_PAYMENT_DET" + "]");
		sql.append(" ");
		sql.append(query);
		sql.append(" WHERE ");
		String conditon = formCondition(htCondition);
		sql.append(conditon);

		if (extCond.length() != 0) {
			sql.append(extCond);
		}

		flag = updateData(con, sql.toString());
		this.mLogger.query(this.printQuery, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con);
	}
	return flag;
}

public boolean deletepaymentdetails(java.util.Hashtable ht) throws Exception {
	boolean delete = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" DELETE ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_ORDER_PAYMENT_DET]");
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

public Map getPaymentReciptHeaderDetails(String plant) throws Exception {

    MLogger.log(1, this.getClass() + " getPaymentReciptHeaderDetails()");
    Map m = new HashMap();
    java.sql.Connection con = null;
    String scondtn ="";
    try {
            
            con = DbBean.getConnection();
            
            StringBuffer sql = new StringBuffer("  SELECT  PMTDATE,PAYMENTMODE,REFERENCENO,INVOICENUMBER,INVOICEDATE,ISNULL(DUEDATE,'')AS DUEDATE,ORIGINALAMOUNT,BALANCE,PAYMENT,AMOUNTCREDITED ,TOTAL ,MEMO ,SIGNATURE ,");
            sql.append("HEADER AS HDR,ISNULL(RECEIVEDFROM,'') AS RECEIVEDFROM ,ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ");
            sql.append(" ");
            sql.append(" FROM " + "[" + plant + "_"
                            + "PAYMENT_RECIPT_HDR] where plant='"+plant+"'");
     
            
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

public boolean updatePaymentReciptHeader(String query, Hashtable htCondition, String extCond)
		throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
			                + htCondition.get("PLANT") + "_PAYMENT_RECIPT_HDR]");
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

public boolean deletetemporderpayment(java.util.Hashtable ht) throws Exception {
	boolean delete = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" DELETE ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TEMP_ORDER_PAYMENT_DET"
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
public boolean inserttemporderpayment(Hashtable ht,String cond) throws Exception {
	boolean insertedInv = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
				
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_" +
				"TEMP_ORDER_PAYMENT_DET" + "]" + "(" +
				"PLANT,ROWID,ORDNO,ORDERNAME,PAYMENT_REFNO,AMOUNT_PAID,PAYMENT_DT,PAYMENT_MODE,ORDERTYPE,CustCode)" +
				" SELECT a.PLANT," + 
				"  ROW_NUMBER() OVER(ORDER BY VENDNORNAME,CAST(SUBSTRING(A.PAYMENT_DT, 7, 4) +'-'+ " + 
				" SUBSTRING(A.PAYMENT_DT, 4,2) +'-'+ SUBSTRING(A.PAYMENT_DT, 1,2) AS date)) AS ID," +
				" a.PONO,a.BILL,a.REFERENCE,a.AMOUNT,a.PAYMENT_DT,a.PAYMENT_MODE,a.ORDERTYPE,a.VENDNORNAME FROM (" + 
				"  SELECT  BILL_DATE as PAYMENT_DT,PONO,BILL, '' as REFERENCE," + 
				"  (SELECT VNAME FROM [" + ht.get("PLANT") +"_VENDMST] WHERE VENDNO = A.VENDNO) AS VENDNORNAME,"
				+ "TOTAL_AMOUNT AS AMOUNT, ''  AS PAYMENT_MODE,PLANT, '"+ht.get("ORDERNAME")+"' AS ORDERTYPE" + 
				" FROM [" + ht.get("PLANT") +"_FINBILLHDR] A " + 
				" UNION ALL " + 
				" SELECT (SUBSTRING(B.CRAT, 7, 2) + '/' + SUBSTRING(B.CRAT, 5, 2) + '/' + SUBSTRING(B.CRAT, 1, 4)) AS PAYMENT_DT," + 
				" ((SELECT PONO FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] WHERE ID = B.BILLHDRID)) AS PONO," + 
				" ((SELECT BILL FROM "+"["+ ht.get("PLANT")+"_FINBILLHDR] WHERE ID = B.BILLHDRID)) AS BILL, A.REFERENCE," + 
				" (SELECT VNAME FROM "+"["+ ht.get("PLANT")+"_VENDMST] WHERE VENDNO = A.VENDNO) AS VENDNORNAME," + 
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
public boolean isExisittemp(Hashtable ht, String extCond) throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append("COUNT(*) ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TEMP_ORDER_PAYMENT_DET" + "]");
		sql.append(" WHERE  " + formCondition(ht));

		/*if (extCond.length() > 0)
			sql.append(" and " + extCond);*/

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

public boolean insertinvoicetemporderpayment(Hashtable ht,String cond) throws Exception {
	boolean insertedInv = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
				
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_TEMP_ORDER_PAYMENT_DET] "
				+ "(PLANT,ROWID,ORDNO,ORDERNAME,PAYMENT_REFNO,AMOUNT_PAID,PAYMENT_DT,PAYMENT_MODE,ORDERTYPE,CustCode)" + 
				"  SELECT a.PLANT," + 
				"  ROW_NUMBER() OVER(ORDER BY CNAME,CAST(SUBSTRING(A.PAYMENT_DT, 7, 4) +'-'+ " + 
				"  SUBSTRING(A.PAYMENT_DT, 4,2) +'-'+ SUBSTRING(A.PAYMENT_DT, 1,2) AS date)) AS ID,"
				+ "a.DONO,a.INVOICE,a.REFERENCE,a.AMOUNT,a.PAYMENT_DT,a.PAYMENT_MODE,a.ORDERTYPE,a.CNAME FROM (" + 
				" SELECT  INVOICE_DATE as PAYMENT_DT,DONO,INVOICE, '' as REFERENCE," + 
				"  (SELECT CNAME FROM " + "[" + ht.get("PLANT") + "_CUSTMST] WHERE CUSTNO = A.CUSTNO) AS CNAME,"
				+ "TOTAL_AMOUNT AS AMOUNT, ''  AS PAYMENT_MODE,PLANT, '"+ht.get("ORDERNAME")+"' AS ORDERTYPE" + 
				" FROM [" + ht.get("PLANT") +"_FININVOICEHDR] A" + 
				" UNION ALL" + 
				" SELECT (SUBSTRING(B.CRAT, 7, 2) + '/' + SUBSTRING(B.CRAT, 5, 2) + '/' + SUBSTRING(B.CRAT, 1, 4)) AS PAYMENT_DT," + 
				"   ((SELECT DONO FROM [" + ht.get("PLANT") +"_FININVOICEHDR] WHERE ID = B.INVOICEHDRID)) AS DONO,"+
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
}
