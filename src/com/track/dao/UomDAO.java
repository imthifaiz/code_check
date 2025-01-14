package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class UomDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.RSNMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.RSNMST_PRINTPLANTMASTERLOG;

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

	public static final String TABLE_NAME = "UOM";

	public UomDAO() {
	}

	public boolean isExists(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
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

	public boolean insertIntoUom(Hashtable ht) throws Exception {
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

	public ArrayList getUomDetails(String uom, String plant,
			String cond) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select distinct uom,isnull(uomdesc,'') uomdesc,isnull(Display,'') Display,isnull(QPUOM,'0') QPUOM,isnull(ISCONVERTIBLE,'0') ISCONVERTIBLE,isnull(CUOM,'') CUOM,ISACTIVE from "
					+ "[" + plant + "_" + "uom] where uom like '"
					+ uom + "%' " + cond
					+ " ORDER BY uom ";
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
	
	public boolean getisExistUom(String uom, String plant) throws Exception {

		java.sql.Connection con = null;
		boolean al = false;
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select distinct uom,isnull(uomdesc,'') uomdesc,isnull(Display,'') Display,isnull(QPUOM,'0') QPUOM,ISACTIVE from "
					+ "[" + plant + "_" + "uom] where uom ='"+uom+"'";
			this.mLogger.query(this.printQuery, sQry);

			al = isExists(con, sQry);

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

	public boolean updateUom(Hashtable htUpdate, Hashtable htCondition)
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
					+ TABLE_NAME + "]" + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}

	public boolean deleteUom(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
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
		public ArrayList getUOMList(String Plant) throws Exception {

        java.sql.Connection con = null;
        ArrayList al = new ArrayList();
        try {
                con = com.track.gates.DbBean.getConnection();
               
                StringBuffer sQry = new StringBuffer("SELECT UOM , UOMDESC  from "
                		+"[" + Plant + "_" + TABLE_NAME + "]");                
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

	public ArrayList getUom(String plant) throws Exception {

		MLogger.log(1, this.getClass() + " getUom()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("uom,");
			sql.append("uomdesc");
			
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "uom] WHERE ISACTIVE='Y'");
			sql.append(" ORDER BY rsnCode");
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
	public ArrayList getAllUOMByOrder(String Plant,String user) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
				con = DbBean.getConnection();			
			String query = "select UOM as uom,ISNULL(QPUOM,1) as uomqty"				
				+ " from "
				+ " " +Plant+"_UOM where ISACTIVE='Y' AND PLANT <> ''  "; 
			    

			this.setmLogger(mLogger);
			this.mLogger.query(this.printQuery, query.toString());
			al = selectData(con, query.toString());   		

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
	
	public int getCountUOM(Hashtable ht) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntItemmst = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sQry = "SELECT COUNT(UOM) FROM " + "["
					+ ht.get("PLANT") + "_UOM]" + " WHERE "
					+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntItemmst = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntItemmst;
	}

public boolean isExistUom(String uom, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_UOM"
				+ "]" + " WHERE " + IConstants.UOM + " = '"
				+ uom.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}

public boolean isExistHscode(String hscode, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_HSCODEMST"
				+ "]" + " WHERE " + IConstants.HSCODE + " = '"
				+ hscode.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}

public boolean isExistCoo(String coo, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_COOMST"
				+ "]" + " WHERE " + IConstants.COO + " = '"
				+ coo.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}

public String getUomByUomcodeInPeppoluomMaster(String ucode) throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	String uomdesc = "";
	try {
		con = DbBean.getConnection();
		String query = "SELECT UOM_DESCRIPTION FROM " + "[PEPPOL_UOM_CODE] "
		+ "WHERE UOM_CODE=?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1,ucode);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()){
			uomdesc = rs.getString("UOM_DESCRIPTION");
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}finally {
		if (con != null) {
		DbBean.closeConnection(con);
		}
	}
	return uomdesc;
}


//imti added 
public ArrayList getUomDetail(String uom, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	ArrayList arrCust = new ArrayList();
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT UOM,UOMDESC,Display,QPUOM,ISACTIVE FROM "
				+ "["
				+ plant
				+ "_"
				+ "UOM"
				+ "]"
				+ " WHERE UOM = '"
				+ uom + "' OR UOMDESC = '"
				+ uom + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			arrCust.add(0, StrUtils.fString(rs.getString(1))); // uom
			arrCust.add(1, StrUtils.fString(rs.getString(2))); // uomdesc
			arrCust.add(2, StrUtils.fString(rs.getString(3))); // display
			arrCust.add(3, StrUtils.fString(rs.getString(4))); // quom
			arrCust.add(4, StrUtils.fString(rs.getString(5))); // isactive
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return arrCust;
}


public double getQPUOM(String plant,String uom) throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	String QPUOM = "";
	double qpuomint = 1;
	try {
		con = DbBean.getConnection();
		String query = "SELECT isnull(QPUOM,'1') QPUOM FROM [" + plant + "_" + TABLE_NAME + "] WHERE UOM=?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1,uom);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()){
			QPUOM = rs.getString("QPUOM");
		}
		qpuomint = Double.valueOf(QPUOM);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}finally {
		if (con != null) {
		DbBean.closeConnection(con);
		}
	}
	return qpuomint;
}

}