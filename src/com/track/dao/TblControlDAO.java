package com.track.dao;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class TblControlDAO extends BaseDAO {
	public static String TABLE_NAME = "TBLCONTROL";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TBLCONTROLDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TBLCONTROLDAO_PRINTPLANTMASTERLOG;

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

	public String plant = "";
	StrUtils _StrUtils = null;

	public TblControlDAO() {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "TBLCONTROL" + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "INVMST" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
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

	public Map selectRowTBLCONTROL(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query);
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

	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "TBLCONTROL" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
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

	public boolean isExisit(Hashtable ht, String extCond, String aPlant)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + aPlant + "_" + "TBLCONTROL" + "]"); // "+"["+aPlant+"_"+"TBLCONTROL"+"]"
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

	public boolean isExisit(String sql) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
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

	public boolean insertTblControl(Hashtable ht, String aPlant)
			throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
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
			String query = "INSERT INTO " + "[" + aPlant + "_" + "TBLCONTROL"
					+ "]" + "(" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
					+ ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	public boolean update(String query, Hashtable htCondition, String extCond,
			String aPlant) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
					+ "TBLCONTROL" + "]");
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

	public String getBatchDate() throws Exception {
		String batchDate = "";

		Hashtable ht = new Hashtable();
		String query = " convert(varchar(20),getdate(),112) as BatchDate";
		Map m = selectRowTBLCONTROL(query, ht);
		batchDate = (String) m.get("BatchDate");
		return batchDate;
	}
	//In yymmdd format
	public String getDate() throws Exception {
		String batchDate = "";

		Hashtable ht = new Hashtable();
		String query = " convert(varchar(20),getdate(),12) as BatchDate";
		Map m = selectRowTBLCONTROL(query, ht);
		batchDate = (String) m.get("BatchDate");
		return batchDate;
	}

	public String getMinseq() throws Exception {
		String batchDate = "";
		Hashtable ht = new Hashtable();
		String query = " MINSEQ";
		Map m = selectRowTBLCONTROL(query, ht);
		batchDate = (String) m.get("MINSEQ");
		return batchDate;
	}

	public String getTableControlDetails(Hashtable ht) throws Exception {

		String retnNextSeq = "";
		String plant = (String) ht.get(IDBConstants.PLANT);
		Hashtable htnew = new Hashtable();

		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		htnew.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
		htnew.put(IDBConstants.TBL_FUNCTION, ht.get(IDBConstants.TBL_FUNCTION));
		try {
			boolean exitFlag = false;
			exitFlag = isExisit(htnew, "", plant);
			if (exitFlag == false) {
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, ht.get(IDBConstants.TBL_FUNCTION));
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, ht.get(IDBConstants.TBL_PREFIX1));
				htTblCntInsert.put("MINSEQ", ht.get(IDBConstants.TBL_MIN_SEQ));
				htTblCntInsert.put("MAXSEQ", ht.get(IDBConstants.TBL_MAX_SEQ));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, ht.get(IDBConstants.CREATED_BY));
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
				insertTblControl(htTblCntInsert, plant);
				retnNextSeq = (String) ht.get(IDBConstants.TBL_PREFIX1) + (String) IDBConstants.TBL_FIRST_NEX_SEQ;
			} else {
				Map m = selectRow(query, htnew, "");
				retnNextSeq = (String) m.get("NXTSEQ");
			}
		} catch (Exception e) {
			throw e;
		}
		return retnNextSeq;
	}

	public String getnextSeqNum(Hashtable ht) throws Exception {

		String retnNextSeq = "";
		String plant = (String) ht.get(IDBConstants.PLANT);
		Hashtable htnew = new Hashtable();

		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		htnew.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
		htnew.put(IDBConstants.TBL_FUNCTION, ht.get(IDBConstants.TBL_FUNCTION));
		try {
			boolean exitFlag = false, updateFlag = false;
			exitFlag = isExisit(htnew, "", plant);
			if (exitFlag == false) {
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, ht.get(IDBConstants.TBL_FUNCTION));
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, ht.get(IDBConstants.TBL_PREFIX1));
				htTblCntInsert.put("MINSEQ", ht.get(IDBConstants.TBL_MIN_SEQ));
				htTblCntInsert.put("MAXSEQ", ht.get(IDBConstants.TBL_MAX_SEQ));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, ht.get(IDBConstants.TBL_NEXT_SEQ));
				htTblCntInsert.put(IDBConstants.CREATED_BY, ht.get(IDBConstants.CREATED_BY));
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
				insertTblControl(htTblCntInsert, plant);
				retnNextSeq = (String) ht.get(IDBConstants.TBL_NEXT_SEQ);
			} else {
				// update
				// check for the day in month

				// 01 -- update tblctrl to nextseq = 0
				/*
				 * htnew.put(IDBConstants.TBL_PREFIX1,ht.get(IDBConstants.TBL_PREFIX1));
				 * exitFlag=isExisit(htnew,"",plant); if (exitFlag==false) { Map htUpdate =
				 * null;
				 * 
				 * Hashtable htTblCntUpdate = new Hashtable();
				 * htTblCntUpdate.put(IDBConstants.PLANT, plant);
				 * 
				 * htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
				 * ht.get(IDBConstants.TBL_FUNCTION)); StringBuffer updateQyery = new
				 * StringBuffer("set "); updateQyery.append(IDBConstants.TBL_NEXT_SEQ +
				 * " = '0' , "); updateQyery.append(IDBConstants.TBL_PREFIX1 +
				 * " = '"+ht.get(IDBConstants.TBL_PREFIX1)+"'");
				 * 
				 * updateFlag = update(updateQyery.toString(), htTblCntUpdate, "", plant);
				 * if(!updateFlag){ throw new Exception("Update of TblCtrl failed"); }
				 * 
				 * }
				 */

				Map m = selectRow(query, htnew, "");
				retnNextSeq = (String) m.get("NXTSEQ");
			}
		} catch (Exception e) {
			throw e;
		}
		return retnNextSeq;
	}

	public Boolean updateSeqNo(String aFunc, String plant) throws Exception {
		Boolean flag = false;
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = DbBean.getConnection();
			// update the next seq no
			String sQry2Update = "UPDATE " + "[" + plant + "_" + "TBLCONTROL" + "]"
					+ " SET NXTSEQ = NXTSEQ+1 WHERE FUNC = '" + aFunc + "'";
			System.out.println(sQry2Update);
			ps = con.prepareStatement(sQry2Update);
			int i = ps.executeUpdate();
			if (i <= 0) {
				flag = false;
				throw new Exception("Unable to update!");

			} else {
				flag = true;

			}

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return flag;
	}
	
	public Boolean updateMannualSeqNo(String aFunc, String plant, int seqno) throws Exception {
		Boolean flag = false;
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = DbBean.getConnection();
			// update the next seq no
			String sQry2Update = "UPDATE " + "[" + plant + "_" + "TBLCONTROL" + "]"
					+ " SET NXTSEQ = '" + seqno + "' WHERE FUNC = '" + aFunc + "'";
			ps = con.prepareStatement(sQry2Update);
			int i = ps.executeUpdate();
			if (i <= 0) {
				flag = false;
				throw new Exception("Unable to update!");

			} else {
				flag = true;

			}

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return flag;
	}

	public Boolean updateLatestSeqNo(String aFunc, String plant, String seqno) throws Exception {
		Boolean flag = false;
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = DbBean.getConnection();
			// update the next seq no
			String sQry2Update = "UPDATE " + "[" + plant + "_" + "TBLCONTROL" + "]"
					+ " SET NXTSEQ =  NXTSEQ+1 WHERE FUNC = '" + aFunc + "'";
			ps = con.prepareStatement(sQry2Update);
			int i = ps.executeUpdate();
			if (i <= 0) {
				flag = false;
				throw new Exception("Unable to update!");

			} else {
				flag = true;

			}

		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return flag;
	}

	public String getNextOrder(String Plant, String userID, String func) throws Exception {
		String nextSeqno = "";
		String prefix = "";
		String orderPrefix = "";
		try {
			Hashtable htTblCnt = new Hashtable();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);

			DateUtils _DateUtils = new DateUtils();
			// String date = _DateUtils.getGeneralDate("yyMM");
			String date = _DateUtils.getGeneralDate("MMyy");

			htTblCnt.put(IDBConstants.PLANT, Plant);
			htTblCnt.put(IDBConstants.TBL_FUNCTION, func);
			if (func.equals(IConstants.INBOUND)) {
				prefix = "P" + date;
				orderPrefix = "P";
			} 
			else if (func.equals(IConstants.MULTIPOEST)) {
				prefix = "PM" + date;
				orderPrefix = "PM";
			}else if (func.equals(IConstants.POEST)) {
				prefix = "PE" + date;
				orderPrefix = "PE";
			}else if (func.equals(IConstants.OUTBOUND)) {
				prefix = "S" + date;
				orderPrefix = "S";
			} else if (func.equals(IConstants.TRANSFER)) {
				prefix = "T" + date;
				orderPrefix = "T";
			} else if (func.equals(IConstants.CONSIGNMENT)) {
				prefix = "C" + date;
				orderPrefix = "C";
			} else if (func.equals(IConstants.LOAN)) {
				prefix = "R" + date;
				orderPrefix = "R";
			} else if (func.equals(IConstants.ESTIMATE)) {
				prefix = "SE" + date;
				orderPrefix = "SE";
			}  else if (func.equals(IConstants.PROJECT)) {
				prefix = "PR" + date;
				orderPrefix = "PR";
			} 	else if (func.equals("GIRECEIPT")) {
				prefix = "GI" + date;
				orderPrefix = "GI";
			} else if (func.equals("GRRECEIPT")) {
				prefix = "GR" + date;
				orderPrefix = "GR";
			} else if (func.equals("STOCKMOVE")) {
				prefix = "SM" + date;
				orderPrefix = "SM";
			} else if (func.equals("GRN")) {
				prefix = "GN" + date;
				orderPrefix = "GN";
			} else if (func.equals("INVOICE")) {
				prefix = "IN" + date;
				orderPrefix = "IN";
			} else if (func.equals("ITEMRETURN")) {
				prefix = "IDRN" + date;
				orderPrefix = "IDRN";
			} else if (func.equals("ITEMRECEIVE")) {
				prefix = "ICRN" + date;
				orderPrefix = "ICRN";
			} else if (func.equals("TAXINVOICE")) {
				prefix = "TI" + date;
				orderPrefix = "TI";
			} else if (func.equals("CUSTOMER_CREDIT_NOTE")) {
				prefix = "CCN" + date;
				orderPrefix = "CCN";
			}
			else if (func.equals("BILL")) {
				prefix = "BILL" + date;
				orderPrefix = "BILL";
			} else if (func.equals("SUPPLIER_CREDIT_NOTE")) {
				prefix = "SCN" + date;
				orderPrefix = "SCN";
			}else if (func.equals("PURCHASE_RETURN")) {
				prefix = "PRN" + date;
				orderPrefix = "PRN";
			}else if (func.equals("SALES_RETURN")) {
				prefix = "SRN" + date;
				orderPrefix = "SRN";
			}else if (func.equals("GINO")) {
				prefix = "GI" + date;
				orderPrefix = "GI";
			}else if (func.equals("STNO")) {
				prefix = "SK" + date;
				orderPrefix = "SK";
			}else if (func.equals("PAKING_LIST")) {
				prefix = "PL" + date;
				orderPrefix = "PL";
			}else if (func.equals("DELIVERY_NOTE")) {
				prefix = "DN" + date;
				orderPrefix = "DN";
			}else if (func.equals("PAYROLL")) {
				prefix = "PR" + date;
				orderPrefix = "PR";
			} else if (func.equals("BARCODE")) {
				prefix = "10" + date;
				orderPrefix = "10";
			} else if (func.equals(IConstants.KITTING)) {
				prefix = "KD" + date;
				orderPrefix = "KD";	
			}else if (func.equals("INVOICE_QUOTES")) {
				prefix = "INQ" + date;
				orderPrefix = "INQ";
			}
			htTblCnt.put(IDBConstants.TBL_PREFIX1, prefix);
			htTblCnt.put(IDBConstants.TBL_MIN_SEQ, "0000000");
			htTblCnt.put(IDBConstants.TBL_MAX_SEQ, "9999999");
			htTblCnt.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_INITIAL_NEX_SEQ);
			htTblCnt.put(IDBConstants.CREATED_BY, userID);
			htTblCnt.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
			nextSeqno = getnextSeqNum(htTblCnt);
			nextSeqno = String.format("%07d", Integer.parseInt(nextSeqno) + 1);
			// nextSeqno = orderPrefix + _DateUtils.getGeneralDate("yyMMdd")+nextSeqno;
			nextSeqno = orderPrefix + _DateUtils.getGeneralDate("MMyy") + nextSeqno;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return nextSeqno;
	}
	
	public String getNextOrderminvlaue(String Plant, String userID, String func) throws Exception {
		String nextSeqno = "";
		String prefix = "";
		String orderPrefix = "";
		try {
			Hashtable htTblCnt = new Hashtable();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);

			DateUtils _DateUtils = new DateUtils();
			// String date = _DateUtils.getGeneralDate("yyMM");
			String date = _DateUtils.getGeneralDate("MMyy");

			htTblCnt.put(IDBConstants.PLANT, Plant);
			htTblCnt.put(IDBConstants.TBL_FUNCTION, func);
			if (func.equals(IConstants.INBOUND)) {
				prefix = "P" + date;
				orderPrefix = "P";
			}
			else if (func.equals(IConstants.MULTIPOEST)) {
				prefix = "PM" + date;
				orderPrefix = "PM";
			}else if (func.equals(IConstants.POEST)) {
				prefix = "PE" + date;
				orderPrefix = "PE";
			}else if (func.equals(IConstants.OUTBOUND)) {
				prefix = "S" + date;
				orderPrefix = "S";
			} else if (func.equals(IConstants.TRANSFER)) {
				prefix = "T" + date;
				orderPrefix = "T";
			} 
			else if (func.equals(IConstants.CONSIGNMENT)) {
				prefix = "C" + date;
				orderPrefix = "C";
			} else if (func.equals(IConstants.LOAN)) {
				prefix = "R" + date;
				orderPrefix = "R";
			} else if (func.equals(IConstants.ESTIMATE)) {
				prefix = "SE" + date;
				orderPrefix = "SE";
			}  else if (func.equals(IConstants.PROJECT)) {
				prefix = "PR" + date;
				orderPrefix = "PR";
			} else if (func.equals("GIRECEIPT")) {
				prefix = "GI" + date;
				orderPrefix = "GI";
			} else if (func.equals("GRRECEIPT")) {
				prefix = "GR" + date;
				orderPrefix = "GR";
			} else if (func.equals("STOCKMOVE")) {
				prefix = "SM" + date;
				orderPrefix = "SM";
			} else if (func.equals("GRN")) {
				prefix = "GN" + date;
				orderPrefix = "GN";
			} else if (func.equals("INVOICE")) {
				prefix = "IN" + date;
				orderPrefix = "IN";
			} else if (func.equals("TAXINVOICE")) {
				prefix = "TI" + date;
				orderPrefix = "TI";
			} else if (func.equals("CUSTOMER_CREDIT_NOTE")) {
				prefix = "CCN" + date;
				orderPrefix = "CCN";
			}
			htTblCnt.put(IDBConstants.TBL_PREFIX1, prefix);
			htTblCnt.put(IDBConstants.TBL_MIN_SEQ, "0000");
			htTblCnt.put(IDBConstants.TBL_MAX_SEQ, "9999");
			htTblCnt.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_INITIAL_NEX_SEQ);
			htTblCnt.put(IDBConstants.CREATED_BY, userID);
			htTblCnt.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
			nextSeqno = getnextSeqNum(htTblCnt);
			nextSeqno = String.format("%04d", Integer.parseInt(nextSeqno) + 1);
			// nextSeqno = orderPrefix + _DateUtils.getGeneralDate("yyMMdd")+nextSeqno;
			nextSeqno = orderPrefix + _DateUtils.getGeneralDate("MMyy") + nextSeqno;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return nextSeqno;
	}

}