package com.track.session.movhis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.StrUtils;
@Deprecated
public class MovHisBean implements SessionBean {
	public MovHisBean() {
	}

	StrUtils _strUtils = new StrUtils();
	DateUtils _dateUtil = new DateUtils();
	SessionContext sessionContext;
	private String tblName = "MOVHIS";
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
		this.sessionContext = sessionContext;
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
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _strUtils.fString((String) enum1.nextElement());
				String value = _strUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value.toUpperCase() + "',";
			}
			String stmt = "INSERT INTO " + tblName + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertRecvHis = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertRecvHis;
	}

	public List qryMovHis(String aDirType, String aMovtType) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listMovHis = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT RECID,PONO AS GRNNO,ITEM,LOC,QTY FROM MOVHIS WHERE DIRTYPE = '"
					+ aDirType + "' AND MOVTID = '" + aMovtType + "'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, _strUtils.fString(rs.getString("RECID")));
				arrLine.add(1, _strUtils.fString(rs.getString("GRNNO")));
				arrLine.add(2, _strUtils.fString(rs.getString("ITEM")));
				arrLine.add(3, _strUtils.fString(rs.getString("LOC")));
				arrLine.add(4, _strUtils.fString(rs.getString("QTY")));
				listMovHis.add(arrLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
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

		try {
			con = DbBean.getConnection();
			// generate the condition string
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _strUtils.fString((String) enum1.nextElement());
				String value = _strUtils.fString((String) ht.get(key));
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

			// get qry fields
			// get qry fields

			for (int i = 0; i < listQryFields.size(); i++) {
				sQryFields += _strUtils.fString((String) listQryFields.get(i))
						+ ",";
			}
			sQryFields = (sQryFields.length() > 0) ? sQryFields.substring(0,
					sQryFields.length() - 1) : "";
			String sQry = "SELECT " + sQryFields + " FROM " + tblName
					+ " WHERE " + sCondition;

			
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
			System.out.println(e.toString());
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}

}