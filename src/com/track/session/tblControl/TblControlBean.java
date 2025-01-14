package com.track.session.tblControl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.StrUtils;

@Deprecated
public class TblControlBean implements SessionBean {
	public TblControlBean() {
	}

	StrUtils _strUtils = new StrUtils();
	DateUtils _dateUtil = new DateUtils();
	SessionContext sessionContext;
	private String _tblName = "TBLCONTROL";
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
	public boolean insertIntoTblControl(Hashtable ht) throws Exception {
		boolean insertTblControl = false;
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
			String stmt = "INSERT INTO " + _tblName + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertTblControl = true;
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertTblControl;
	}

	/**
	 * @method : getNextSeqNo(String aFunc)
	 * @description : get next sequence number for the given function name
	 * @param aFunc
	 * @return
	 * @throws Exception
	 */
	public String getNextSeqNo(String aFunc) throws Exception {
		String nextSeqno = "";
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		Connection con1 = null;
		Connection con2 = null;
		ResultSet rs = null;
		try {
			con1 = DbBean.getConnection();
			con2 = DbBean.getConnection();
			// update the next seq no
			String sQry2Update = "UPDATE " + _tblName
					+ " SET NXTSEQ = NXTSEQ+1 WHERE FUNC = '" + aFunc + "'";
			ps1 = con1.prepareStatement(sQry2Update);
			int i = ps1.executeUpdate();
			if (i <= 0)
				return "";

			// reterieve the seq no
			String sQry2 = "SELECT NXTSEQ FROM " + _tblName + " WHERE FUNC = '"
					+ aFunc + "'";
			ps2 = con2.prepareStatement(sQry2);
			rs = ps2.executeQuery();
			while (rs.next()) {
				nextSeqno = _strUtils.fString((String) rs.getString(1));
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con1, ps1);
			DbBean.closeConnection(con2, ps2);
		}
		return nextSeqno;
	}

	public ArrayList getTblControlDetails(String aFunc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT FUNC,DESCRIPTION,PREFIX,MINSEQ,MAXSEQ,NXTSEQ FROM TBLCONTROL WHERE FUNC = '"
					+ aFunc.toUpperCase() + "'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, _strUtils.fString((String) rs.getString(1))); // Function
				// code
				arrCust.add(1, _strUtils.fString((String) rs.getString(2))); // Function
				// Description
				arrCust.add(2, _strUtils.fString((String) rs.getString(3))); // Prefix
				arrCust.add(3, _strUtils.fString((String) rs.getString(4))); // minimum
				// sequence
				// number
				arrCust.add(4, _strUtils.fString((String) rs.getString(5))); // maximum
				// sequence
				// number
				arrCust.add(5, _strUtils.fString((String) rs.getString(6))); // next
				// sequence
				// number
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;

	}

	/**
	 * @method :
	 * @param aDesc
	 * @return
	 * @throws Exception
	 */
	public ArrayList getTblControlList(String aFunc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT FUNC,DESCRIPTION FROM TBLCONTROL WHERE FUNC LIKE '%"
					+ aFunc.toUpperCase() + "'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, _strUtils.fString((String) rs.getString(1))); // Funcution
				// Code
				arrLine.add(1, _strUtils.fString((String) rs.getString(2))); // Description
				arrList.add(arrLine);
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}

	/**
	 * 
	 * @param htUpdate
	 * @param htCondition
	 * @return
	 * @throws Exception
	 */
	public boolean updateTblControl(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {

		boolean update = false;
		PreparedStatement ps = null;

		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = _strUtils.fString((String) enumUpdate
						.nextElement());
				String value = _strUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "',";
			}

			// generate the update string
			Enumeration enumCondition = htUpdate.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = _strUtils.fString((String) enumCondition
						.nextElement());
				String value = _strUtils.fString((String) htCondition.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";
			String stmt = "UPDATE " + _tblName + sUpdate + sCondition;

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

	/**
	 * 
	 * @param aFunc
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTblControl(String aFunc) throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + _tblName + " WHERE FUNC ="
					+ aFunc.toUpperCase() + "'";
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return deleteItemMst;
	}

	public boolean isExistInTblControl(String aFunc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM TBLCONTROL WHERE FUNC = '"
					+ aFunc + "'";
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					exists = true;
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return exists;

	}
}