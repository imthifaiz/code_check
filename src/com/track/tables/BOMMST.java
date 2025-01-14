package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class BOMMST {
	private boolean printQuery = MLoggerConstant.BOMMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.BOMMST_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sPARITEM = "";
	String sCLASS = "";
	String sATTRIB = "";
	float fPARQTY;
	String sCLDITEM = "";
	String sCLDCLASS = "";
	String sCLDATTRIB = "";
	float fTOTQTY;
	String sCRAT = "";
	String sCRBY = "";
	String sUPAT = "";
	String sUPBY = "";
	String sRECSTAT = "";
	String sUSERFLD1 = "";
	String sUSERFLD2 = "";
	String sUSERFLD3 = "";
	String sUSERFLD4 = "";
	String sUSERFLD5 = "";
	String sUSERFLD6 = "";
	String sUSERFLG1 = "";
	String sUSERFLG2 = "";
	String sUSERFLG3 = "";
	String sUSERFLG4 = "";
	String sUSERFLG5 = "";
	String sUSERFLG6 = "";
	String sUSERTIME1 = "";
	String sUSERTIME2 = "";
	String sUSERTIME3 = "";
	float fUSERDBL1;
	float fUSERDBL2;
	float fUSERDBL3;
	float fUSERDBL4;
	float fUSERDBL5;
	float fUSERDBL6;

	public String getPLANT() {
		return sPLANT;
	}

	public String getPARITEM() {
		return sPARITEM;
	}

	public String getCLASS() {
		return sCLASS;
	}

	public String getATTRIB() {
		return sATTRIB;
	}

	public float getPARQTY() {
		return fPARQTY;
	}

	public String getCLDITEM() {
		return sCLDITEM;
	}

	public String getCLDCLASS() {
		return sCLDCLASS;
	}

	public String getCLDATTRIB() {
		return sCLDATTRIB;
	}

	public float getTOTQTY() {
		return fTOTQTY;
	}

	public String getCRAT() {
		return sCRAT;
	}

	public String getCRBY() {
		return sCRBY;
	}

	public String getUPAT() {
		return sUPAT;
	}

	public String getUPBY() {
		return sUPBY;
	}

	public String getRECSTAT() {
		return sRECSTAT;
	}

	public String getUSERFLD1() {
		return sUSERFLD1;
	}

	public String getUSERFLD2() {
		return sUSERFLD2;
	}

	public String getUSERFLD3() {
		return sUSERFLD3;
	}

	public String getUSERFLD4() {
		return sUSERFLD4;
	}

	public String getUSERFLD5() {
		return sUSERFLD5;
	}

	public String getUSERFLD6() {
		return sUSERFLD6;
	}

	public String getUSERFLG1() {
		return sUSERFLG1;
	}

	public String getUSERFLG2() {
		return sUSERFLG2;
	}

	public String getUSERFLG3() {
		return sUSERFLG3;
	}

	public String getUSERFLG4() {
		return sUSERFLG4;
	}

	public String getUSERFLG5() {
		return sUSERFLG5;
	}

	public String getUSERFLG6() {
		return sUSERFLG6;
	}

	public String getUSERTIME1() {
		return sUSERTIME1;
	}

	public String getUSERTIME2() {
		return sUSERTIME2;
	}

	public String getUSERTIME3() {
		return sUSERTIME3;
	}

	public float getUSERDBL1() {
		return fUSERDBL1;
	}

	public float getUSERDBL2() {
		return fUSERDBL2;
	}

	public float getUSERDBL3() {
		return fUSERDBL3;
	}

	public float getUSERDBL4() {
		return fUSERDBL4;
	}

	public float getUSERDBL5() {
		return fUSERDBL5;
	}

	public float getUSERDBL6() {
		return fUSERDBL6;
	}

	public void setPLANT(String aPLANT) {
		sPLANT = aPLANT;
	}

	public void setPARITEM(String aPARITEM) {
		sPARITEM = aPARITEM;
	}

	public void setCLASS(String aCLASS) {
		sCLASS = aCLASS;
	}

	public void setATTRIB(String aATTRIB) {
		sATTRIB = aATTRIB;
	}

	public void setPARQTY(float aPARQTY) {
		fPARQTY = aPARQTY;
	}

	public void setCLDITEM(String aCLDITEM) {
		sCLDITEM = aCLDITEM;
	}

	public void setCLDCLASS(String aCLDCLASS) {
		sCLDCLASS = aCLDCLASS;
	}

	public void setCLDATTRIB(String aCLDATTRIB) {
		sCLDATTRIB = aCLDATTRIB;
	}

	public void setTOTQTY(float aTOTQTY) {
		fTOTQTY = aTOTQTY;
	}

	public void setCRAT(String aCRAT) {
		sCRAT = aCRAT;
	}

	public void setCRBY(String aCRBY) {
		sCRBY = aCRBY;
	}

	public void setUPAT(String aUPAT) {
		sUPAT = aUPAT;
	}

	public void setUPBY(String aUPBY) {
		sUPBY = aUPBY;
	}

	public void setRECSTAT(String aRECSTAT) {
		sRECSTAT = aRECSTAT;
	}

	public void setUSERFLD1(String aUSERFLD1) {
		sUSERFLD1 = aUSERFLD1;
	}

	public void setUSERFLD2(String aUSERFLD2) {
		sUSERFLD2 = aUSERFLD2;
	}

	public void setUSERFLD3(String aUSERFLD3) {
		sUSERFLD3 = aUSERFLD3;
	}

	public void setUSERFLD4(String aUSERFLD4) {
		sUSERFLD4 = aUSERFLD4;
	}

	public void setUSERFLD5(String aUSERFLD5) {
		sUSERFLD5 = aUSERFLD5;
	}

	public void setUSERFLD6(String aUSERFLD6) {
		sUSERFLD6 = aUSERFLD6;
	}

	public void setUSERFLG1(String aUSERFLG1) {
		sUSERFLG1 = aUSERFLG1;
	}

	public void setUSERFLG2(String aUSERFLG2) {
		sUSERFLG2 = aUSERFLG2;
	}

	public void setUSERFLG3(String aUSERFLG3) {
		sUSERFLG3 = aUSERFLG3;
	}

	public void setUSERFLG4(String aUSERFLG4) {
		sUSERFLG4 = aUSERFLG4;
	}

	public void setUSERFLG5(String aUSERFLG5) {
		sUSERFLG5 = aUSERFLG5;
	}

	public void setUSERFLG6(String aUSERFLG6) {
		sUSERFLG6 = aUSERFLG6;
	}

	public void setUSERTIME1(String aUSERTIME1) {
		sUSERTIME1 = aUSERTIME1;
	}

	public void setUSERTIME2(String aUSERTIME2) {
		sUSERTIME2 = aUSERTIME2;
	}

	public void setUSERTIME3(String aUSERTIME3) {
		sUSERTIME3 = aUSERTIME3;
	}

	public void setUSERDBL1(float aUSERDBL1) {
		fUSERDBL1 = aUSERDBL1;
	}

	public void setUSERDBL2(float aUSERDBL2) {
		fUSERDBL2 = aUSERDBL2;
	}

	public void setUSERDBL3(float aUSERDBL3) {
		fUSERDBL3 = aUSERDBL3;
	}

	public void setUSERDBL4(float aUSERDBL4) {
		fUSERDBL4 = aUSERDBL4;
	}

	public void setUSERDBL5(float aUSERDBL5) {
		fUSERDBL5 = aUSERDBL5;
	}

	public void setUSERDBL6(float aUSERDBL6) {
		fUSERDBL6 = aUSERDBL6;
	}

	/**
	 * constructor method BOMMST sb - SQL Bean gn - Utilites
	 */
	public BOMMST() throws Exception {
		sb = new sqlBean();
		gn = new Generator();
	}

	/**
	 * DBConnection() get connection pool from the database
	 * 
	 */
	public Connection getDBConnection() throws Exception {
		Connection con = DbBean.getConnection();
		return con;
	}

	/**
	 * Insert Command - BOMMST
	 * 
	 * 
	 */
	public int insertBOMMST() throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "Insert into BOMMST values ('" + sPLANT + "','" + sPARITEM
					+ "','" + sCLASS + "','" + sATTRIB + "','" + sCLDITEM
					+ "','" + sCLDCLASS + "','" + sCLDATTRIB + "'," + fPARQTY
					+ "," + fTOTQTY + ",'" + sCRAT + "','" + sCRBY + "','"
					+ sUPAT + "','" + sUPBY + "','" + sRECSTAT + "','"
					+ sUSERFLD1 + "','" + sUSERFLD2 + "','" + sUSERFLD3 + "','"
					+ sUSERFLD4 + "','" + sUSERFLD5 + "','" + sUSERFLD6 + "','"
					+ sUSERFLG1 + "','" + sUSERFLG2 + "','" + sUSERFLG3 + "','"
					+ sUSERFLG4 + "','" + sUSERFLG5 + "','" + sUSERFLG6 + "','"
					+ sUSERTIME1 + "','" + sUSERTIME2 + "','" + sUSERTIME3
					+ "'," + fUSERDBL1 + "," + fUSERDBL2 + "," + fUSERDBL3
					+ "," + fUSERDBL4 + "," + fUSERDBL5 + "," + fUSERDBL6 + ")";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/**
	 * Update Function - BOMMST
	 * 
	 * 
	 */
	public int updateBOMMST() throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = " Update BOMMST set USERFLG1 = '" + sUSERFLG1
					+ "' , TOTQTY = " + fTOTQTY + " where PLANT= '" + sPLANT
					+ "' and PARITEM= '" + sPARITEM + "' and CLASS= '" + sCLASS
					+ "' and ATTRIB= '" + sATTRIB + "' and CLDITEM= '"
					+ sCLDITEM + "' and CLDCLASS='" + sCLDCLASS
					+ "' and CLDATTRIB='" + sCLDATTRIB + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/**
	 * Select FunctionBOMMST
	 * 
	 * 
	 */
	public void selectBOMMST() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from BOMMST where PLANT= '" + sPLANT
					+ "' and PARITEM= '" + sPARITEM + "' and CLASS= '" + sCLASS
					+ "' and ATTRIB= '" + sATTRIB + "' and CLDITEM= '"
					+ sCLDITEM + "' and CLDCLASS= '" + sCLDCLASS
					+ "' and CLDATTRIB= '" + sCLDATTRIB + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				fPARQTY = rs.getFloat("PARQTY");
				fTOTQTY = rs.getFloat("TOTQTY");
				sCRAT = rs.getString("CRAT");
				sCRBY = rs.getString("CRBY");
				sUPAT = rs.getString("UPAT");
				sUPBY = rs.getString("UPBY");
				sRECSTAT = rs.getString("RECSTAT");
				sUSERFLD1 = rs.getString("USERFLD1");
				sUSERFLD2 = rs.getString("USERFLD2");
				sUSERFLD3 = rs.getString("USERFLD3");
				sUSERFLD4 = rs.getString("USERFLD4");
				sUSERFLD5 = rs.getString("USERFLD5");
				sUSERFLD6 = rs.getString("USERFLD6");
				sUSERFLG1 = rs.getString("USERFLG1");
				sUSERFLG2 = rs.getString("USERFLG2");
				sUSERFLG3 = rs.getString("USERFLG3");
				sUSERFLG4 = rs.getString("USERFLG4");
				sUSERFLG5 = rs.getString("USERFLG5");
				sUSERFLG6 = rs.getString("USERFLG6");
				sUSERTIME1 = rs.getString("USERTIME1");
				sUSERTIME2 = rs.getString("USERTIME2");
				sUSERTIME3 = rs.getString("USERTIME3");
				fUSERDBL1 = rs.getFloat("USERDBL1");
				fUSERDBL2 = rs.getFloat("USERDBL2");
				fUSERDBL3 = rs.getFloat("USERDBL3");
				fUSERDBL4 = rs.getFloat("USERDBL4");
				fUSERDBL5 = rs.getFloat("USERDBL5");
				fUSERDBL6 = rs.getFloat("USERDBL6");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
	}

	/**
	 * Delete Function -- BOMMST
	 * 
	 * 
	 */
	public int deleteBOMMST() throws Exception {
		String sql = "";
		// Connection con = getDBConnection();
		// ResultSet rs = null;
		int returnCode = 1;
		// if (con == null)
		// returnCode = 100; // Could not get Database Connection..Please check
		// the DSN, UserName and Password in the db_param file
		try {
			sql = "delete from BOMMST where PLANT= '" + sPLANT
					+ "' and PARITEM= '" + sPARITEM + "' and CLASS= '" + sCLASS
					+ "' and ATTRIB= '" + sATTRIB + "' and CLDITEM= '"
					+ sCLDITEM + "' and CLDCLASS= '" + sCLDCLASS
					+ "' and CLDATTRIB= '" + sCLDATTRIB + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return returnCode;
	}

	public int deleteBOMMST(String item) throws Exception {
		String sql = "";
		// Connection con = getDBConnection();
		// ResultSet rs = null;
		int returnCode = 1;
		// if (con == null)
		// returnCode = 100; // Could not get Database Connection..Please check
		// the DSN, UserName and Password in the db_param file
		try {
			sql = "delete from BOMMST where PLANT= '" + sPLANT
					+ "' and PARITEM= '" + item + "' ";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		// finally {
		// DbBean.closeConnection(con);
		// }
		// System.out.println("Hello i am here in deleteBOMMST()");
		return returnCode;
	}

	public String listBOMMST(String aITEM, String aCLASS, String aATTRIB)
			throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		String link = "";
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			q = "select clditem,cldclass,cldattrib,totqty from bommst where paritem = '"
					+ aITEM
					+ "' and CLASS= '"
					+ aCLASS
					+ "' and ATTRIB= '"
					+ aATTRIB + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				/*
				 * recId = recId.replace(ordtype.charAt(0), '0'); StringBuffer s
				 * = new StringBuffer(recId); s.deleteCharAt(0);
				 * //System.out.println(s); n = Integer.parseInt(recId) + 1;
				 * //System.out.println("1" + n); NumberFormat formatter = new
				 * DecimalFormat("0000"); res = ordtype + formatter.format(n);
				 * //System.out.println("2" + res);
				 */
				String item = rs.getString("CLDITEM").trim();
				String cldclass = rs.getString("CLDCLASS").trim();
				String cldattrib = rs.getString("CLDATTRIB").trim();
				ITEMMST im = new ITEMMST();
				String desc = im.getItemDesc(item.trim());
				// NumberFormat formatter = new DecimalFormat("000.0000");
				String qty = rs.getString("TOTQTY").trim();
				// String qty = formatter.format(qtyb4);
				link = "<a href=\"" + "bomModiItem.jsp?PARITEM=" + aITEM
						+ "&CLASS=" + aCLASS + "&ATTRIB=" + aATTRIB
						+ "&CLDITEM=" + item + "&CLDCLASS=" + cldclass
						+ "&CLDATTRIB=" + cldattrib + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + item + "</FONT></a>"
						+ "</td>" + "<td width=\"30%\">" + sb.formatHTML(desc)
						+ "</td>" + "<td width=\"20%\">"
						+ sb.formatHTML(cldclass) + "</td>"
						+ "<td width=\"20%\">" + sb.formatHTML(cldattrib)
						+ "</td>" + "<td width=\"10%\">" + sb.formatHTML(qty)
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public float getProductionQty(String aItemCode, String aClass,
			String aAttrib) throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from BOMMST where PLANT= 'PLNT' and PARITEM= '"
					+ aItemCode + "' and CLASS ='" + aClass
					+ "' and ATTRIB = '" + aAttrib + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				fPARQTY = Float.parseFloat(rs.getString("PARQTY"));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return fPARQTY;
	}

	public float getParentQty(String aItemCode, String aClass, String aAttrib)
			throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from BOMMST where PLANT= 'PLNT' and PARITEM= '"
					+ aItemCode + "' and CLASS ='" + aClass
					+ "' and ATTRIB = '" + aAttrib + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				fPARQTY = Float.parseFloat(rs.getString("USERFLD1"));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return fPARQTY;
	}

	public boolean isAvaliable(String aItem, String aClass, String aAttrib)
			throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from BOMMST where PLANT= 'PLNT' and PARITEM= '"
					+ aItem
					+ "' and CLASS ='"
					+ aClass
					+ "' and ATTRIB = '"
					+ aAttrib + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				int m = rs.getInt(1);
				if (m > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

}