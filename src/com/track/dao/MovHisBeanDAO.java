package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class MovHisBeanDAO extends BaseDAO {

	private boolean printQuery = MLoggerConstant.MOVHISBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.MOVHISBEANDAO_PRINTPLANTMASTERLOG;
	private String tblName = "MOVHIS";
	private MLogger mLogger = new MLogger();

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

	public MovHisBeanDAO() {
	}

	/**
	 * method : insertIntoInvsmt(Hashtable ht) description : Insert the new
	 * record into Inventory master(INVMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean insertIntoMovHis(Hashtable ht) throws Exception {
		boolean insertRecvHis = false;
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
				VALUES += "'" + value.toUpperCase() + "',";
			}
			String stmt = "INSERT INTO " + tblName + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertRecvHis = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertRecvHis;
	}

	public List qryMovHis(String aDirType, String aMovtType) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listMovHis = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT RECID,PONO AS GRNNO,ITEM,LOC,QTY FROM MOVHIS WHERE DIRTYPE = '"
					+ aDirType + "' AND MOVTID = '" + aMovtType + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString(rs.getString("RECID")));
				arrLine.add(1, StrUtils.fString(rs.getString("GRNNO")));
				arrLine.add(2, StrUtils.fString(rs.getString("ITEM")));
				arrLine.add(3, StrUtils.fString(rs.getString("LOC")));
				arrLine.add(4, StrUtils.fString(rs.getString("QTY")));
				listMovHis.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listMovHis;

	}

	public ArrayList queryMovhis(ArrayList listQryFields, Hashtable ht,
			String afrmDate, String atoDate) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sCondition = "";
		String sQryFields = "";
		ArrayList listQryResult = new ArrayList();

		Connection con = null;
		try {
			con = DbBean.getConnection();
			// generate the condition string
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}

			if (afrmDate.length() > 0) {
				sCondition = sCondition + " CRAT  >= '" + afrmDate + "' AND ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " CRAT  <= '" + atoDate
							+ "' AND ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " CRAT  <= '" + atoDate
							+ "' AND ";
				}
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";

			for (int i = 0; i < listQryFields.size(); i++) {
				sQryFields += StrUtils.fString((String) listQryFields.get(i))
						+ ",";
			}
			sQryFields = (sQryFields.length() > 0) ? sQryFields.substring(0,
					sQryFields.length() - 1) : "";
			String sQry = "SELECT " + sQryFields + " FROM " + tblName
					+ " WHERE " + sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();

			while (rs.next()) {
				ArrayList lineArrList = new ArrayList();
				for (int i = 0; i < listQryFields.size(); i++) {
					int iCol = i + 1;
					String fieldName = rs.getString(iCol);
					lineArrList.add(i, fieldName);
				}

				listQryResult.add(lineArrList);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}

}