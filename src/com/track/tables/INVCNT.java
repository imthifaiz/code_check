package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.searchBean;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class INVCNT {
	private boolean printQuery = MLoggerConstant.INVCNT_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.INVCNT_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	searchBean sr;
	String sPLANT = "SIS";
	String sITEM = "";
	String sCLASS = "";
	String sATTRIB = "";
	String sATTDESC = "";
	String sBATCH = "";
	String sBINNO = "";
	String sPALLET = "";
	String sSTATUS = "";
	String sSLED = "";
	String sQED = "";
	String sSTKTYPE = "";
	float fQTY;
	float fQTYALLOC;
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

	public String getITEM() {
		return sITEM;
	}

	public String getCLASS() {
		return sCLASS;
	}

	public String getATTRIB() {
		return sATTRIB;
	}

	public String getATTDESC() {
		return sATTDESC;
	}

	public String getBATCH() {
		return sBATCH;
	}

	public String getBINNO() {
		return sBINNO;
	}

	public String getPALLET() {
		return sPALLET;
	}

	public String getSTATUS() {
		return sSTATUS;
	}

	public String getSLED() {
		return sSLED;
	}

	public String getQED() {
		return sQED;
	}

	public String getSTKTYPE() {
		return sSTKTYPE;
	}

	public float getQTY() {
		return fQTY;
	}

	public float getQTYALLOC() {
		return fQTYALLOC;
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

	public void setITEM(String aITEM) {
		sITEM = aITEM;
	}

	public void setBATCH(String aBATCH) {
		sBATCH = aBATCH;
	}

	public void setBINNO(String aBINNO) {
		sBINNO = aBINNO;
	}

	public void setPALLET(String aPALLET) {
		sPALLET = aPALLET;
	}

	public void setSTATUS(String aSTATUS) {
		sSTATUS = aSTATUS;
	}

	public void setCLASS(String aCLASS) {
		sCLASS = aCLASS;
	}

	public void setATTRIB(String aATTRIB) {
		sATTRIB = aATTRIB;
	}

	public void setATTDESC(String aATTDESC) {
		sATTDESC = aATTDESC;
	}

	public void setSLED(String aSLED) {
		sSLED = aSLED;
	}

	public void setQED(String aQED) {
		sQED = aQED;
	}

	public void setSTKTYPE(String aSTKTYPE) {
		sSTKTYPE = aSTKTYPE;
	}

	public void setQTY(float aQTY) {
		fQTY = aQTY;
	}

	public void setQTYALLOC(float aQTYALLOC) {
		fQTYALLOC = aQTYALLOC;
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
	 * constructor method INVCNT sb - SQL Bean gn - Utilites
	 */
	public INVCNT() throws Exception {
		sb = new sqlBean();
		gn = new Generator();
		sr = new searchBean();
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
	 * Insert Command - INVCNT
	 * 
	 * 
	 */
	public int insertINVCNT() throws Exception {
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
			sql = "Insert into INVCNT values ('" + sPLANT + "','" + sITEM
					+ "','" + sBATCH + "','" + sBINNO + "','" + sPALLET + "','"
					+ sSTATUS + "','" + sCLASS + "','" + sATTRIB + "','"
					+ sATTDESC + "','" + sSLED + "','" + sQED + "','"
					+ sSTKTYPE + "'," + fQTY + "," + fQTYALLOC + ",'" + sCRAT
					+ "','" + sCRBY + "','" + sUPAT + "','" + sUPBY + "','"
					+ sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2 + "','"
					+ sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5 + "','"
					+ sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2 + "','"
					+ sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5 + "','"
					+ sUSERFLG6 + "','" + sUSERTIME1 + "','" + sUSERTIME2
					+ "','" + sUSERTIME3 + "'," + fUSERDBL1 + "," + fUSERDBL2
					+ "," + fUSERDBL3 + "," + fUSERDBL4 + "," + fUSERDBL5 + ","
					+ fUSERDBL6 + ")";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);

		} catch (Exception e) {
			DbBean.writeError("INVCNT", "insertINVCNT()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getInsertSQL() throws Exception {
		String sql = "";
		int returnCode = 1;
		try {
			sql = "Insert into INVCNT values ('" + sPLANT + "','" + sITEM
					+ "','" + sBATCH + "','" + sBINNO + "','" + sPALLET + "','"
					+ sSTATUS + "','" + sCLASS + "','" + sATTRIB + "','"
					+ sATTDESC + "','" + sSLED + "','" + sQED + "','"
					+ sSTKTYPE + "'," + fQTY + "," + fQTYALLOC + ",'" + sCRAT
					+ "','" + sCRBY + "','" + sUPAT + "','" + sUPBY + "','"
					+ sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2 + "','"
					+ sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5 + "','"
					+ sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2 + "','"
					+ sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5 + "','"
					+ sUSERFLG6 + "','" + sUSERTIME1 + "','" + sUSERTIME2
					+ "','" + sUSERTIME3 + "'," + fUSERDBL1 + "," + fUSERDBL2
					+ "," + fUSERDBL3 + "," + fUSERDBL4 + "," + fUSERDBL5 + ","
					+ fUSERDBL6 + ")";

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sql;
	}

	/**
	 * UpString Function - INVCNT
	 * 
	 * 
	 */
	public int updateINVCNT() throws Exception {
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
			sql = "Update INVCNT set SLED= '" + sSLED + "',QED= '" + sQED
					+ "',STKTYPE= '" + sSTKTYPE + "',QTY = " + fQTY
					+ ",QTYALLOC = " + fQTYALLOC + ",CRAT = '" + sCRAT
					+ "',CRBY= '" + sCRBY + "',UPAT = '" + sUPAT + "',UPBY= '"
					+ sUPBY + "',RECSTAT= '" + sRECSTAT + "',USERFLD1= '"
					+ sUSERFLD1 + "',USERFLD2= '" + sUSERFLD2 + "',USERFLD3= '"
					+ sUSERFLD3 + "',USERFLD4= '" + sUSERFLD4 + "',USERFLD5= '"
					+ sUSERFLD5 + "',USERFLD6= '" + sUSERFLD6 + "',USERFLG1= '"
					+ sUSERFLG1 + "',USERFLG2= '" + sUSERFLG2 + "',USERFLG3= '"
					+ sUSERFLG3 + "',USERFLG4= '" + sUSERFLG4 + "',USERFLG5= '"
					+ sUSERFLG5 + "',USERFLG6= '" + sUSERFLG6
					+ "',USERTIME1 = '" + sUSERTIME1 + "',USERTIME2 = '"
					+ sUSERTIME2 + "',USERTIME3 = '" + sUSERTIME3
					+ "',USERDBL1 = " + fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2
					+ ",USERDBL3 = " + fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4
					+ ",USERDBL5 = " + fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and ITEM= '" + sITEM
					+ "' and BATCH= '" + sBATCH + "' and BINNO= '" + sBINNO
					+ "' and PALLET= '" + sPALLET + "' and STATUS= '" + sSTATUS
					+ "'";
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
	 * Select FunctionINVCNT
	 * 
	 * 
	 */
	public void selectINVCNT() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from INVCNT where PLANT= '" + sPLANT
					+ "' and ITEM= '" + sITEM + "' and BATCH= '" + sBATCH
					+ "' and BINNO= '" + sBINNO + "' and STATUS= '" + sSTATUS
					+ "' and CLASS= '" + sCLASS + "' and ATTRIB= '" + sATTRIB
					+ "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sCLASS = rs.getString("CLASS");
				sATTRIB = rs.getString("ATTRIB");
				sSLED = rs.getString("SLED");
				sQED = rs.getString("QED");
				sSTKTYPE = rs.getString("STKTYPE");
				fQTY = rs.getFloat("QTY");
				fQTYALLOC = rs.getFloat("QTYALLOC");
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
	 * Delete Function -- INVCNT
	 * 
	 * 
	 */
	public int deleteINVCNT() throws Exception {
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
			sql = "delete from INVCNT where PLANT= '" + sPLANT
					+ "' and ITEM= '" + sITEM + "' and BATCH= '" + sBATCH
					+ "' and BINNO= '" + sBINNO + "' and PALLET= '" + sPALLET
					+ "' and STATUS= '" + sSTATUS + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String listINVCNT(String aITEM, String aCLASS, String aATTRIB,
			String aBATCH, String aBINNO, String aSTATUS) throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aITEM.length() > 0)) {
				vecWhr.addElement(" ITEM = '" + aITEM + "'");
			}
			if ((aCLASS.length() > 0)) {
				vecWhr.addElement(" CLASS = '" + aCLASS + "'");
			}
			if ((aATTRIB.length() > 0)) {
				vecWhr.addElement(" ATTRIB = '" + aATTRIB + "'");
			}
			if ((aBATCH.length() > 0)) {
				vecWhr.addElement(" BATCH = '" + aBATCH + "'");
			}
			if ((aBINNO.length() > 0)) {
				vecWhr.addElement(" BINNO = '" + aBINNO + "'");
			}
			if ((aSTATUS.length() > 0)) {
				vecWhr.addElement(" STATUS = '" + aSTATUS + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where Qty > 0 and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			// System.out.println(where);
			q = "select item,class,attrib,batch,binno,pallet,status,sled,qed,stktype,qty,qtyalloc from INVCNT "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"14%\">"
						+ sb.formatHTML(rs.getString("ITEM").trim())
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("CLASS").trim())
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("ATTRIB").trim())
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(rs.getString("BATCH").trim())
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(rs.getString("BINNO").trim())
						+ "</td>"
						+ "<td width=\"6%\">"
						+ sb.formatHTML(rs.getString("STATUS").trim())
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("SLED").trim())
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getFloat("QTY") + "") + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String listINVCNT4PutAway(String aITEM) throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		String link = "";
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			q = "select item,class,attrib,batch,binno,status,pallet,qty from INVCNT where ITEM = '"
					+ aITEM + "' and QTY > 0";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String batch = rs.getString("BATCH").trim();
				String binno = rs.getString("BINNO").trim();
				String status = rs.getString("STATUS").trim();
				String pallet = rs.getString("PALLET").trim();
				link = "<a href=\"" + "putAway.jsp?ITEM=" + item + "&BATCH="
						+ batch + "&BINNO=" + binno + "&CLASS=" + clas
						+ "&ATTRIB=" + attrib + "&STATUS=" + status + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">"
						+ link
						+ "<FONT COLOR=\"blue\">"
						+ item
						+ "</font></a>"
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(clas)
						+ "</td>"
						+ "<td width=\"20%\">"
						+ sb.formatHTML(attrib)
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(batch)
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(sr.getRefTableDesc(status, "STATUS"))
						+ "</td>"
						+ "<td width=\"20%\">"
						+ sb.formatHTML(binno)
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getFloat("QTY") + "") + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String listINVCNT(String aITEM) throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		String link = "";
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			q = "select item,batch,binno,status,pallet,qty from INVCNT where ITEM = '"
					+ aITEM + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String batch = rs.getString("BATCH").trim();
				String binno = rs.getString("BINNO").trim();
				String status = rs.getString("STATUS").trim();
				String pallet = rs.getString("PALLET").trim();
				link = "<a href=\"" + "putAway.jsp?ITEM=" + item + "&BATCH="
						+ batch + "&BINNO=" + binno + "&STATUS=" + status
						+ "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + item + "</font></a>"
						+ "</td>" + "<td width=\"20%\">" + sb.formatHTML(batch)
						+ "</td>" + "<td width=\"10%\">"
						+ sb.formatHTML(sr.getRefTableDesc(status, "STATUS"))
						+ "</td>" + "<td width=\"30%\">" + sb.formatHTML(binno)
						+ "</td>" + "<td width=\"10%\">"
						+ sb.formatHTML(rs.getFloat("QTY") + "") + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String pdalistINVCNT(String aITEM) throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		String link = "";
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			q = "select item,batch,binno,status,pallet,qty from INVCNT where ITEM = '"
					+ aITEM + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String batch = rs.getString("BATCH").trim();
				String binno = rs.getString("BINNO").trim();
				String status = rs.getString("STATUS").trim();
				String pallet = rs.getString("PALLET").trim();
				link = "<a href=\"" + "pdaputAway.jsp?ITEM=" + item + "&BATCH="
						+ batch + "&BINNO=" + binno + "&STATUS=" + status
						+ "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"40%\">" + link
						+ "<FONT COLOR=\"blue\">" + item + "</font></a>"
						+ "</td>" + "<td width=\"40%\">" + sb.formatHTML(batch)
						+ "</td>" + "<td width=\"60%\">" + sb.formatHTML(binno)
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public boolean processPutAway(String sbin, String dbin, float qty)
			throws Exception {
		String sql;
		ArrayList al = new ArrayList();
		boolean retBool = false;
		int ret1 = 1;
		int ret2 = 1;
		ResultSet rs = null;
		try {
			if (isAvaliable(sITEM, sCLASS, sATTRIB, sBATCH, sSTATUS, dbin)) {
				sql = "update INVCNT set qty = qty + " + qty
						+ " where PLANT= 'PLNT' and ITEM= '" + sITEM
						+ "' and CLASS= '" + sCLASS + "' and ATTRIB= '"
						+ sATTRIB + "' and BATCH= '" + sBATCH
						+ "' and BINNO= '" + dbin + "' and STATUS= '" + sSTATUS
						+ "'";
				al.add(sql);
			} else {
				sBINNO = sbin;
				selectINVCNT();
				sBINNO = dbin;
				fQTY = qty;
				sql = getInsertSQL();
				al.add(sql);
			}
			sql = "update INVCNT set qty = qty - " + qty
					+ " where PLANT= 'PLNT' and ITEM= '" + sITEM
					+ "' and CLASS= '" + sCLASS + "' and ATTRIB= '" + sATTRIB
					+ "' and BATCH= '" + sBATCH + "' and BINNO= '" + sbin
					+ "' and STATUS= '" + sSTATUS + "'";
			al.add(sql);
			sb.setmLogger(mLogger);
			retBool = sb.insertBatchRecords(al);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return retBool;
	}

	public boolean processPutAway4PDA(String sbin, String dbin, float qty)
			throws Exception {
		String sql;
		ArrayList al = new ArrayList();
		boolean retBool = false;
		int ret1 = 1;
		int ret2 = 1;
		ResultSet rs = null;
		try {
			if (isAvaliable(sITEM, sCLASS, sATTRIB, sBATCH, sSTATUS, dbin)) {
				sql = "update INVCNT set qty = qty + " + qty
						+ " where PLANT= 'PLNT' and ITEM= '" + sITEM
						+ "' and CLASS= '" + sCLASS + "' and ATTRIB= '"
						+ sATTRIB + "' and BATCH= '" + sBATCH
						+ "' and BINNO= '" + dbin + "' and STATUS= '" + sSTATUS
						+ "'";
				al.add(sql);
			} else {
				sBINNO = sbin;
				selectINVCNT();
				sBINNO = dbin;
				fQTY = qty;
				sql = getInsertSQL();
				al.add(sql);
			}
			sql = "update INVCNT set qty = qty - " + qty
					+ " where PLANT= 'PLNT' and ITEM= '" + sITEM
					+ "' and CLASS= '" + sCLASS + "' and ATTRIB= '" + sATTRIB
					+ "' and BATCH= '" + sBATCH + "' and BINNO= '" + sbin
					+ "' and STATUS= '" + sSTATUS + "'";
			al.add(sql);
			sb.setmLogger(mLogger);
			retBool = sb.insertBatchRecords(al);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return retBool;
	}

	public boolean processHoldRelease(String clas, String attrib,
			String statOld, String statNew, String bin, float qty, String sled)
			throws Exception {
		String sql;
		ArrayList al = new ArrayList();
		boolean retBool = false;
		int ret1 = 1;
		int ret2 = 1;
		ResultSet rs = null;
		sCLASS = clas;
		sATTRIB = attrib;
		try {
			if (isAvaliable(sITEM, sCLASS, sATTRIB, sBATCH, statNew, bin)) {
				// String sled1 = getSLED(sITEM,sCLASS,sATTRIB,sBATCH);
				sql = "update INVCNT set qty = qty + " + qty + " ,sled = '"
						+ sled + "' " + " where PLANT= 'PLNT' and ITEM= '"
						+ sITEM + "' and CLASS= '" + sCLASS + "' and ATTRIB= '"
						+ sATTRIB + "' and BATCH= '" + sBATCH
						+ "' and BINNO= '" + bin + "' and STATUS= '" + statNew
						+ "'";
				al.add(sql);
			} else {
				sBINNO = bin;
				fQTY = qty;
				sSLED = sled;
				// sSTATUS = statOld;
				sSTATUS = statNew;
				sql = getInsertSQL();
				al.add(sql);
			}
			sql = "update INVCNT set qty = qty - " + qty
					+ " where PLANT= 'PLNT' and ITEM= '" + sITEM
					+ "' and CLASS= '" + sCLASS + "' and ATTRIB= '" + sATTRIB
					+ "' and BATCH= '" + sBATCH + "' and BINNO= '" + bin
					+ "' and STATUS= '" + statOld + "'";
			al.add(sql);
			sb.setmLogger(mLogger);
			retBool = sb.insertBatchRecords(al);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return retBool;
	}

	public boolean processHoldRelease(String clas, String attrib,
			String statOld, String statNew, String bin, float qty)
			throws Exception {
		String sql;
		ArrayList al = new ArrayList();
		boolean retBool = false;
		int ret1 = 1;
		int ret2 = 1;
		ResultSet rs = null;
		sCLASS = clas;
		sATTRIB = attrib;
		try {
			if (isAvaliable(sITEM, sCLASS, sATTRIB, sBATCH, statNew, bin)) {
				String sled1 = getSLED(sITEM, sCLASS, sATTRIB, sBATCH);
				sql = "update INVCNT set qty = qty + " + qty + " ,sled = '"
						+ sled1 + "' " + " where PLANT= 'PLNT' and ITEM= '"
						+ sITEM + "' and CLASS= '" + sCLASS + "' and ATTRIB= '"
						+ sATTRIB + "' and BATCH= '" + sBATCH
						+ "' and BINNO= '" + bin + "' and STATUS= '" + statNew
						+ "'";
				al.add(sql);
			} else {
				sBINNO = bin;
				fQTY = qty;
				sSTATUS = statOld;
				sSTATUS = statNew;
				sql = getInsertSQL();
				al.add(sql);
			}
			sql = "update INVCNT set qty = qty - " + qty
					+ " where PLANT= 'PLNT' and ITEM= '" + sITEM
					+ "' and CLASS= '" + sCLASS + "' and ATTRIB= '" + sATTRIB
					+ "' and BATCH= '" + sBATCH + "' and BINNO= '" + bin
					+ "' and STATUS= '" + statOld + "'";
			al.add(sql);
			sb.setmLogger(mLogger);
			retBool = sb.insertBatchRecords(al);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return retBool;
	}

	public boolean isAvaliable(String item, String clas, String attrib,
			String batch, String status, String dbin) throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from INVCNT where PLANT= 'PLNT' ";
			q = (item.equalsIgnoreCase("")) ? q : q + " and ITEM= '" + item
					+ "'";
			q = (clas.equalsIgnoreCase("")) ? q : q + " and CLASS= '" + clas
					+ "' ";
			q = (attrib.equalsIgnoreCase("")) ? q : q + "and ATTRIB= '"
					+ attrib + "'";
			q = (batch.equalsIgnoreCase("")) ? q : q + " and BATCH= '" + batch
					+ "'";
			q = (dbin.equalsIgnoreCase("")) ? q : q + " and BINNO= '" + dbin
					+ "'";
			q = (status.equalsIgnoreCase("")) ? q : q + " and STATUS= '"
					+ status + "'";

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
		// System.out.println(result);
		return result;
	}

	public boolean isAvaliable(String item, String clas, String attrib,
			String batch, String status, String dbin, String sled)
			throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from INVCNT where PLANT= 'PLNT' "
					+ " and ITEM= '" + item + "'" + " and CLASS= '" + clas
					+ "' " + " and ATTRIB= '" + attrib + "'" + " and BATCH= '"
					+ batch + "'" + " and BINNO= '" + dbin + "'"
					+ " and (sled= '' or sled='000000')" + " and STATUS= '"
					+ status + "'";
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
		// System.out.println(result);
		return result;
	}

	// to check - is stock available for item when creating sales order

	public boolean isQtyAvaliable(String item, String clas, String attrib)
			throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select qty from INVCNT where PLANT= 'PLNT' ";
			q = (item.equalsIgnoreCase("")) ? q : q + " and ITEM= '" + item
					+ "'";
			q = (clas.equalsIgnoreCase("")) ? q : q + " and CLASS= '" + clas
					+ "' ";
			q = (attrib.equalsIgnoreCase("")) ? q : q + "and ATTRIB= '"
					+ attrib + "'";
			q += " and status in ('A','P') and qty > 0";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				int m = rs.getInt(1);
				if (m > 0) {
					result = true;
					// System.out.println("qty lessthan zero");
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public boolean isAvaliable(String item, String batch, String status,
			String dbin) throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from INVCNT where PLANT= 'PLNT' and ITEM= '"
					+ item + "' and BATCH= '" + batch + "' and BINNO= '" + dbin
					+ "' and STATUS= '" + status + "'";
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

	// insertBatchRecords(ArrayList sqllist)

	public int decreaseQty(String item, String clas, String attrib,
			String batch, String status, String sbin, float sqty)
			throws Exception {
		boolean result = false;
		String q = "";
		int returnCode = 1;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "update INVCNT set qty = qty - " + sqty
					+ " where PLANT= 'PLNT' and ITEM= '" + item
					+ "' and CLASS= '" + clas + "' and ATTRIB= '" + attrib
					+ "' and BATCH= '" + batch + "' and BINNO= '" + sbin
					+ "' and STATUS= '" + status + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(q);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int increaseQty(String item, String clas, String attrib,
			String batch, String status, String sbin, float sqty)
			throws Exception {
		String q = "";
		int returnCode = 1;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "update INVCNT set qty = qty + " + sqty
					+ " where PLANT= 'PLNT' and ITEM= '" + item
					+ "' and CLASS= '" + clas + "' and ATTRIB= '" + attrib
					+ "' and BATCH= '" + batch + "' and BINNO= '" + sbin
					+ "' and STATUS= '" + status + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(q);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int insertItem(String item, String clas, String attrib,
			String batch, String status, String sbin, String dbin, float qty)
			throws Exception {
		String q = "";
		int returnCode = 1;
		try {
			sITEM = item;
			sCLASS = clas;
			sATTRIB = attrib;
			sBATCH = batch;
			sSTATUS = status;
			sBINNO = dbin;
			fQTY = qty;
			returnCode = insertINVCNT();
		} catch (Exception e) {
			DbBean.writeError("INVCNT", "updateINVCNT()", e);
		} finally {
		}
		return returnCode;
	}

	public Vector getAllocatedItem(String aITEM, String clas, String attrib,
			float aQTY) throws Exception {
		Vector resVec = new Vector();
		String q = "";
		String result = "";
		String where = "";
		String link = "";
		Connection con = getDBConnection();
		float resQty = 0;
		boolean flg = true;

		try {
			Statement stmt = con.createStatement();
			q = "select item,batch,binno,qty from INVCNT where status = 'A' and item = '"
					+ aITEM
					+ "' and CLASS= '"
					+ clas
					+ "' and ATTRIB= '"
					+ attrib + "' and qty > 0 order by sled ";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				if (flg) {
					Vector datVec = new Vector();
					String item = rs.getString("ITEM").trim();
					datVec.add(item);
					String batch = rs.getString("BATCH").trim();
					datVec.add(batch);
					String bin = rs.getString("BINNO").trim();
					datVec.add(bin);
					String qty = rs.getString("QTY").trim();
					resQty = resQty + Float.parseFloat(qty);
					if (aQTY < resQty) {
						float qt = resQty - aQTY;
						qt = Float.parseFloat(qty) - qt;
						datVec.add(qt + "");
						flg = false;
					} else {
						datVec.add(qty);
					}
					resVec.add(datVec);
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return resVec;
	}

	public float getAvailQty(String aITEM, String clas, String attrib)
			throws Exception {
		String q = "";
		float resFlt = 0;
		String link = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select sum(qty) from INVCNT where status = 'A' and item = '"
					+ aITEM + "' and CLASS= '" + clas + "' and ATTRIB= '"
					+ attrib + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				resFlt = rs.getFloat(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return resFlt;
	}

	/*
	 * public int increaseQty(String item, String batch, String status, String
	 * sbin, float sqty) throws Exception{ String q = ""; int returnCode = 1;
	 * Connection con = getDBConnection(); try { Statement stmt =
	 * con.createStatement(); q = "update INVCNT set qty = qty + " + sqty +
	 * " where PLANT= 'PLNT' and ITEM= '"
	 * +item+"' and BATCH= '"+batch+"' and BINNO= '"
	 * +sbin+"' and STATUS= '"+status+"'"; returnCode = sb.insertRecords(q); }
	 * catch (Exception e) { DbBean.writeError("INVCNT", "updateINVCNT()", e); }
	 * finally { DbBean.closeConnection(con); } return returnCode; }
	 */

	public float getAvailQty(String item, String clas, String attrib,
			String batch, String status, String dbin) throws Exception {
		boolean result = false;
		float m = 0;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select QTY from INVCNT where PLANT= 'PLNT' and ITEM= '" + item
					+ "' and CLASS= '" + clas + "' and ATTRIB= '" + attrib
					+ "' and BATCH= '" + batch + "' and BINNO= '" + dbin
					+ "' and STATUS= '" + status + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				m = rs.getFloat(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return m;
	}

	public float getAvailQty4PDA(String item, String clas, String attrib,
			String batch, String status, String sbin) throws Exception {
		boolean result = false;
		float m = 0;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select QTY from INVCNT where PLANT= 'PLNT' ";
			q = (item.equalsIgnoreCase("")) ? q : q + " and ITEM= '" + item
					+ "'";
			q = (clas.equalsIgnoreCase("")) ? q : q + " and CLASS= '" + clas
					+ "' ";
			q = (attrib.equalsIgnoreCase("")) ? q : q + "and ATTRIB= '"
					+ attrib + "'";
			q = (batch.equalsIgnoreCase("")) ? q : q + " and BATCH= '" + batch
					+ "'";
			q = (batch.equalsIgnoreCase("")) ? q : q + " and BINNO= '" + sbin
					+ "'";

			q += " and STATUS= '" + status + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				m = rs.getFloat(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return m;
	}

	public String getUpdateSql(String item, String clas, String attrib,
			String batch, String status, String sbin, float sqty)
			throws Exception {
		String q = "";
		try {
			q = "update INVCNT set qty = qty + " + sqty
					+ " where PLANT= 'PLNT' and ITEM= '" + item
					+ "' and CLASS= '" + clas + "' and ATTRIB= '" + attrib
					+ "' and BATCH= '" + batch + "' and BINNO= '" + sbin
					+ "' and STATUS= '" + status + "'";
		} catch (Exception e) {
			DbBean.writeError("INVCNT", "updateINVCNT()", e);
		}
		return q;
	}

	public String listINVCNT4RPT(String aITEM, String aCLASS, String aATTRIB,
			String aBATCH, String aBINNO, String aITEMTYPE, String aSTATUS)
			throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aITEM.length() > 0)) {
				vecWhr.addElement(" a.ITEM = '" + aITEM + "'");
			}
			if ((aCLASS.length() > 0)) {
				vecWhr.addElement(" b.CLASS = '" + aCLASS + "'");
			}
			if ((aATTRIB.length() > 0)) {
				vecWhr.addElement(" b.ATTRIB = '" + aATTRIB + "'");
			}
			if ((aBATCH.length() > 0)) {
				vecWhr.addElement(" b.BATCH = '" + aBATCH + "'");
			}
			if ((aBINNO.length() > 0)) {
				vecWhr.addElement(" b.BINNO = '" + aBINNO + "'");
			}

			if ((aITEMTYPE.length() > 0)) {
				vecWhr.addElement(" a.ITEMTYPE = '" + aITEMTYPE + "'");
			}

			if ((aSTATUS.length() > 0)) {
				vecWhr.addElement(" b.STATUS = '" + aSTATUS + "'");
			}

			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where b.item = a.item and b.Qty > 0 and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}

			q = "select a.item,a.itemdesc,a.itemtype,b.class,b.attrib,b.batch,b.binno,b.status,b.sled,b.qty from itemmst a, INVCNT b "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				NumberFormat formatter = new DecimalFormat("#,##0.00#");
				// result =
				// formatter.format(Double.parseDouble(rs.getString(1)));
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"15%\">"
						+ sb.formatHTML(rs.getString("ITEM").trim())
						+ "</td>"
						+ "<td width=\"25%\">"
						+ sb.formatHTML(rs.getString("ITEMDESC").trim())
						+ "</td>"
						+ "<td width=\"5%\">"
						+ sb.formatHTML(rs.getString("ITEMTYPE").trim())
						+ "</td>"
						+ "<td width=\"5%\">"
						+ sb.formatHTML(rs.getString("CLASS").trim())
						+ "</td>"
						+ "<td width=\"5%\">"
						+ sb.formatHTML(rs.getString("ATTRIB").trim())
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("BATCH").trim())
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("BINNO").trim())
						+ "</td>"
						+ "<td width=\"5%\">"
						+ sb.formatHTML(rs.getString("STATUS").trim())
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("SLED").trim())
						+ "</td>"
						+ "<td align=\"right\" width=\"10%\">"
						+ sb.formatHTML(formatter.format(rs.getFloat("QTY"))
								+ "") + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String getQtyINVCNT4RPT(String aITEM, String aCLASS, String aATTRIB,
			String aBATCH, String aBINNO, String aITEMTYPE, String aSTATUS)
			throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aITEM.length() > 0)) {
				vecWhr.addElement(" a.ITEM = '" + aITEM + "'");
			}
			if ((aCLASS.length() > 0)) {
				vecWhr.addElement(" b.CLASS = '" + aCLASS + "'");
			}
			if ((aATTRIB.length() > 0)) {
				vecWhr.addElement(" b.ATTRIB = '" + aATTRIB + "'");
			}
			if ((aBATCH.length() > 0)) {
				vecWhr.addElement(" b.BATCH = '" + aBATCH + "'");
			}
			if ((aBINNO.length() > 0)) {
				vecWhr.addElement(" b.BINNO = '" + aBINNO + "'");
			}

			if ((aITEMTYPE.length() > 0)) {
				vecWhr.addElement(" a.ITEMTYPE = '" + aITEMTYPE + "'");
			}

			if ((aSTATUS.length() > 0)) {
				vecWhr.addElement(" b.STATUS = '" + aSTATUS + "'");
			}

			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where b.item = a.item and b.Qty > 0 and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			q = "select sum(b.qty) from itemmst a, INVCNT b " + where; // a.item
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				// result = rs.getString(1);
				NumberFormat formatter = new DecimalFormat("#,##0.00#");
				result = formatter.format(Double.parseDouble(rs.getString(1)));

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String updateQtySQL(String item, String clas, String attrib,
			float qty) {
		String updateSQL;
		updateSQL = "UPDATE INVCNT SET QTYALLOC = QTYALLOC-" + qty
				+ " where  ITEM ='" + item + "' AND CLASS ='" + clas
				+ "' AND ATTRIB = '" + attrib + "'";
		return updateSQL;
	}

	public boolean checkStatus(String itemCode) throws Exception {
		boolean itemStatus = false;
		String q = "";
		Connection con = getDBConnection();
		int count = 0;
		try {
			Statement stmt = con.createStatement();
			q = "select ITEM from ITEMMST WHERE ITEM='" + itemCode + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				count++;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		if (count > 0)
			itemStatus = true;
		return itemStatus;
	}

	public String populateBins(String item, String clas, String attrib,
			String batch, String status) throws Exception {
		String options = "";
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select BINNO from INVCNT where PLANT= 'PLNT' and ITEM= '"
					+ item + "' and CLASS= '" + clas + "' and ATTRIB= '"
					+ attrib + "' and BATCH= '" + batch + "' and  STATUS= '"
					+ status + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			String bno = "";
			while (rs.next()) {
				bno = rs.getString(1).trim();
				options += "<option value=" + bno + ">" + bno + "</option>";
			}
		} catch (Exception e) {
			DbBean.writeError("INVCNT", "populateBins()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return options;
	}

	public String listItems4QA(String item) throws Exception {
		String recStr = "";
		String link = "";
		String itm = "", clas = "", attrib = "", batch = "", bin = "", status = "", sled = "";
		float qty = 0;
		String q = "select item,class,attrib,batch,binno,qty,sled,status from INVCNT where status in ('H','A') and qty >0 and item like '%"
				+ item.trim() + "%'";
		this.mLogger.query(this.printQuery, q);
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			ITEMMST im = new ITEMMST();
			while (rs.next()) {
				itm = rs.getString("item");
				clas = rs.getString("class");
				attrib = rs.getString("attrib");
				batch = rs.getString("batch");
				bin = rs.getString("binno");
				qty = rs.getFloat("qty");
				sled = rs.getString("sled");
				status = rs.getString("status");
				link = "<a href=\"javascript:window.opener.form.ITEM.value="
						+ "'"
						+ itm
						+ "';"
						+ "window.opener.form.ITEMDESC.value="
						+ "'"
						+ im.getItemDesc(itm.trim())
						+ "';"
						+ "window.opener.form.CLASS.value="
						+ "'"
						+ clas
						+ "';"
						+ "window.opener.form.ATTRIB.value="
						+ "'"
						+ attrib
						+ "';"
						+ "window.opener.form.BATCH.value="
						+ "'"
						+ batch
						+ "';"
						+ "window.opener.form.BIN.value="
						+ "'"
						+ bin
						+ "';"
						+ "window.opener.form.QTY.value="
						+ "'"
						+ qty
						+ "';"
						+ "window.opener.form.SLED.value="
						+ "'"
						+ sled
						+ "';"
						+ "window.opener.form.STATUS.value="
						+ "'" + status + "';" + "window.close();\">";
				recStr += "<tr bgcolor=\"#eeeeee\"><td>" + link + itm
						+ "</a></td><td>" + clas + "</td><td>" + attrib
						+ "</td><td>" + batch + "</td><td>" + bin + "</td><td>"
						+ qty + "</td><td>" + status + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return recStr;
	}

	public String getSLED(String item, String clas, String attrib, String batch)
			throws Exception {
		String sled = "";
		String q = "select TOP 1 SLED from INVCNT where item = '%"
				+ item.trim() + "%' and attrib='" + attrib + "' and class='"
				+ clas + "' and batch='" + batch + "' and sled<> ''";
		this.mLogger.query(this.printQuery, q);
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			ITEMMST im = new ITEMMST();
			while (rs.next()) {
				sled = rs.getString("sled");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return sled;
	}private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}