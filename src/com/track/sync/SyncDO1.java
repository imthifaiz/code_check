package com.track.sync;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.track.constants.IConstants;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class SyncDO1 {
	public SyncDO1() {
	}

	private static final boolean DEBUG = true;
	DateUtils dateUtils = new DateUtils();
	MLogger logger = new MLogger();
	StrUtils strUtil = new StrUtils();
	String CRAT = dateUtils.getDate(new java.text.SimpleDateFormat(
			"yyyyMMddhhmmss"));
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public boolean syncDOProcess() throws Exception {
		boolean flg = false;
		String plant = IConstants.PLANT_VAL;
		List doList = new ArrayList();
		Date dt = new Date();
		int cnt = 0;
		Connection connSRCDet = null;
		Connection connDESTDet = null;
		Connection connDolno = null;
		Connection connCloseDO = null;
		Connection connDESDetUpdate = null;
		Connection connDESDetExists = null;

		ResultSet rsSRCDet = null;
		ResultSet rsDestExist = null;
		ResultSet rsDolno = null;

		Statement stmtSrcHdr = null;
		Statement stmtSrcDet = null;
		Statement stmtDestHdr = null;
		Statement stmtDestDet = null;
		Statement stmtDestUpdate = null;
		Statement stmtDestExists = null;
		Statement stmtGetMaxPolnno = null;
		Statement stmtSrcExists = null;
		Statement stmtDolno = null;
		Statement stmtCloseDO = null;
		try {
			connSRCDet = DbBean.getODBCConnection4BPCS_MASTERS();
			connDESTDet = DbBean.getConnection();
			connDESDetUpdate = DbBean.getConnection();
			connDESDetExists = DbBean.getConnection();
			connDolno = DbBean.getConnection();
			connCloseDO = DbBean.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String selectSrcDet = "SELECT OEORDD.LINENUM, OEORDD.ITEM,OEORDD.\"DESC\", "
				+ "OEORDD.QTYORDERED, OEORDD.ORDUNIT, OEORDD.UNITPRICE, "
				+ "OEORDH.ORDNUMBER, OEORDH.CUSTOMER, OEORDH.ORDDATE, "
				+ "OEORDH.TYPE, OEORDH.BILNAME, OEORDD.PICKSEQ, OEORDH.SHPCITY,"
				+ "OEORDH.EXPDATE "
				+ "FROM OEORDD OEORDD, OEORDH OEORDH "
				+ "WHERE OEORDD.ORDUNIQ = OEORDH.ORDUNIQ AND ((OEORDH.TYPE=2))";

		// System.out.println("selectSrcDet : " + selectSrcDet);
		try {
			stmtSrcDet = connSRCDet.createStatement();
			stmtDestDet = connDESTDet.createStatement();
			stmtDestUpdate = connDESDetUpdate.createStatement();
			stmtDestExists = connDESDetExists.createStatement();
			stmtDolno = connDolno.createStatement();
			stmtCloseDO = connCloseDO.createStatement();

			rsSRCDet = stmtSrcDet.executeQuery(selectSrcDet);

			int DolnnoCnt = 0;
			String CRAT = dateUtils.getDate(new java.text.SimpleDateFormat(
					"yyyyMMddhhmmss"));

			while (rsSRCDet.next()) {

				String LINENUM = strUtil.fString(rsSRCDet.getString(1)); // DOLO
				String ITEM = strUtil.fString(rsSRCDet.getString(2)); // ITEM
				String DESC = strUtil.fString(rsSRCDet.getString(3)); // USERFLD1
				DESC = strUtil.removeQuotes(DESC).trim();
				String QTYORDERED = strUtil.fString(rsSRCDet.getString(4)); // QTYOR
				String ORDUNIT = strUtil.fString(rsSRCDet.getString(5)); // UNITMO
				String UNITPRICE = strUtil.fString(rsSRCDet.getString(6)); // USERDBL1
				String ORDNUMBER = strUtil.fString(rsSRCDet.getString(7)); // DONO
				String CUSTOMER = strUtil.fString(rsSRCDet.getString(8)); // USERFLD2
				String ORDDATE = strUtil.fString(rsSRCDet.getString(9)); // DELDATE
				String TYPE = strUtil.fString(rsSRCDet.getString(10)); // POLTYPE
				String BILNAME = strUtil.fString(rsSRCDet.getString(11)); // USERFLD3
				BILNAME = strUtil.InsertQuotes(BILNAME);
				String PICKSEQ = strUtil.fString(rsSRCDet.getString(12)); // USERFLD4
				String SHPCITY = strUtil.fString(rsSRCDet.getString(13)); // USERFLD5
				String EXPDATE = strUtil.fString(rsSRCDet.getString(14)); // USERTIME1
				float fOrdQty = new Float(QTYORDERED).floatValue();
				float fUnitPrice = new Float(UNITPRICE).floatValue();
				String selectDolno4Dodet = "select  isnull(max(cast((dolnno) as integer)),0)+1 As Cnt"
						+ "  from dodet where DONO='"
						+ ORDNUMBER
						+ "'AND COMMENT2='ODBC1'";

				rsDestExist = stmtDestExists
						.executeQuery("SELECT * FROM DODET WHERE DONO='"
								+ ORDNUMBER + "' AND COMMENT1 ='" + LINENUM
								+ "'");
				if (!(rsDestExist.next())) {
					rsDolno = stmtDolno.executeQuery(selectDolno4Dodet);
					while (rsDolno.next()) {
						DolnnoCnt = rsDolno.getInt("Cnt");
					}
					String stmtInsertDet = "INSERT INTO DODET(PLANT,DONO,DOLNNO,ITEM,USERFLD1,QTYOR,QTYIS,UNITMO,USERDBL1,USERFLD2,DELDATE,POLTYPE,USERFLD3,USERFLD4,USERFLD5,USERTIME1,LNSTAT,CRAT,CRBY,COMMENT1,COMMENT2) VALUES "
							+ "('"
							+ plant
							+ "','"
							+ ORDNUMBER
							+ "',"
							+ DolnnoCnt
							+ ",'"
							+ ITEM
							+ "','"
							+ DESC
							+ "',"
							+ fOrdQty
							+ ",0.0,'"
							+ ORDUNIT
							+ "',"
							+ fUnitPrice
							+ ",'"
							+ CUSTOMER
							+ "','"
							+ ORDDATE
							+ "','"
							+ TYPE
							+ "','"
							+ BILNAME
							+ "','"
							+ PICKSEQ
							+ "','"
							+ SHPCITY
							+ "','"
							+ EXPDATE
							+ "','N','"
							+ CRAT
							+ "','','"
							+ LINENUM + "','ODBC1')";

					stmtDestDet.execute(stmtInsertDet);

				} else {
					// String updateDet = "UPDATE DODET SET QTYOR="+new
					// Float(QTYORDERED).floatValue()+" WHERE DONO = '"+ORDNUMBER+"' AND DOLNNO = '"+LINENUM+"' AND COMMENT2 ='ODBC1' ";
					String updateDet = "UPDATE DODET SET QTYOR="
							+ new Float(QTYORDERED).floatValue()
							+ " WHERE DONO = '" + ORDNUMBER
							+ "' AND COMMENT1 = '" + LINENUM
							+ "' AND COMMENT2 ='ODBC1' ";
					MLogger.log(0, "[SQL]" + updateDet);
					stmtDestDet.execute(updateDet);
				}
				doList.add(ORDNUMBER);
				cnt = cnt + 1;
			}
			System.out.println("SyncDO1 :: " + cnt
					+ " synchronized successfully");
			if (doList.size() > 0) {
				String sql_donos = strUtil
						.getStringSeparatedByQuotsFromList(doList);
				// System.out.println("SyncDO1 :: sql_donos : "+sql_donos);
				int iCntClose = stmtCloseDO
						.executeUpdate("UPDATE DODET SET LNSTAT = 'C' WHERE DONO NOT IN ("
								+ sql_donos
								+ " ) AND COMMENT2='ODBC1' AND DONO NOT IN (SELECT DONO FROM DODET WHERE  LNSTAT ='N')");

			} else {
				int iCntClose = stmtCloseDO
						.executeUpdate("UPDATE DODET SET LNSTAT = 'C' WHERE DONO NOT IN ('') AND COMMENT2='ODBC1' AND DONO NOT IN (SELECT DONO FROM DODET WHERE  LNSTAT ='N')");

			}
			flg = true;
		} catch (Exception e) {
			flg = false;

			throw new Exception("Could not able to synchronize DO ");
		} finally {
			try {
				DbBean.closeConnection(connSRCDet, stmtSrcDet);
				DbBean.closeConnection(connDESTDet, stmtDestDet);
				DbBean.closeConnection(connDESDetUpdate, stmtDestUpdate);
				DbBean.closeConnection(connDESDetExists, stmtDestExists);
				DbBean.closeConnection(connDolno, stmtDolno);
				DbBean.closeConnection(connCloseDO, stmtCloseDO);
			} catch (Exception e) {
				System.out.println("SyncDO1 :: Exception :: " + e.toString());
				throw new Exception(
						"Could not able to synchronize Items from Server");
			}
		}
		Date dt1 = new Date();

		return flg;

	}

	public boolean syncDOProcess1() throws Exception {
		boolean flg = false;
		String plant = IConstants.PLANT_VAL;
		List doList = new ArrayList();
		Date dt = new Date();
		int cnt = 0;
		Connection connSRCDet = null;
		Connection connDESTDet = null;
		Connection connDolno = null;
		Connection connCloseDO = null;
		Connection connDESDetUpdate = null;
		Connection connDESDetExists = null;

		ResultSet rsSRCDet = null;
		ResultSet rsDestExist = null;
		ResultSet rsDolno = null;

		Statement stmtSrcHdr = null;
		Statement stmtSrcDet = null;
		Statement stmtDestHdr = null;
		Statement stmtDestDet = null;
		Statement stmtDestUpdate = null;
		Statement stmtDestExists = null;
		Statement stmtGetMaxPolnno = null;
		Statement stmtSrcExists = null;
		Statement stmtDolno = null;
		Statement stmtCloseDO = null;
		try {
			connSRCDet = DbBean.getODBCConnection4BPCS_MASTERS1();
			connDESTDet = DbBean.getConnection();
			connDESDetUpdate = DbBean.getConnection();
			connDESDetExists = DbBean.getConnection();
			connDolno = DbBean.getConnection();
			connCloseDO = DbBean.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String selectSrcDet = "SELECT OEORDD.LINENUM, OEORDD.ITEM,OEORDD.\"DESC\", "
				+ "OEORDD.QTYORDERED, OEORDD.ORDUNIT, OEORDD.UNITPRICE, "
				+ "OEORDH.ORDNUMBER, OEORDH.CUSTOMER, OEORDH.ORDDATE, "
				+ "OEORDH.TYPE, OEORDH.BILNAME, OEORDD.PICKSEQ, OEORDH.SHPCITY,"
				+ "OEORDH.EXPDATE "
				+ "FROM OEORDD OEORDD, OEORDH OEORDH "
				+ "WHERE OEORDD.ORDUNIQ = OEORDH.ORDUNIQ AND ((OEORDH.TYPE=2))";

		try {
			stmtSrcDet = connSRCDet.createStatement();
			stmtDestDet = connDESTDet.createStatement();
			stmtDestUpdate = connDESDetUpdate.createStatement();
			stmtDestExists = connDESDetExists.createStatement();
			stmtDolno = connDolno.createStatement();
			stmtCloseDO = connCloseDO.createStatement();

			rsSRCDet = stmtSrcDet.executeQuery(selectSrcDet);
			int DolnnoCnt = 0;
			String CRAT = dateUtils.getDate(new java.text.SimpleDateFormat(
					"yyyyMMddhhmmss"));

			while (rsSRCDet.next()) {
				String LINENUM = strUtil.fString(rsSRCDet.getString(1)); // DOLO
				String ITEM = strUtil.fString(rsSRCDet.getString(2)); // ITEM
				String DESC = strUtil.fString(rsSRCDet.getString(3)); // USERFLD1
				DESC = strUtil.removeQuotes(DESC).trim();
				String QTYORDERED = strUtil.fString(rsSRCDet.getString(4)); // QTYOR
				String ORDUNIT = strUtil.fString(rsSRCDet.getString(5)); // UNITMO
				String UNITPRICE = strUtil.fString(rsSRCDet.getString(6)); // USERDBL1
				String ORDNUMBER = strUtil.fString(rsSRCDet.getString(7)); // DONO
				String CUSTOMER = strUtil.fString(rsSRCDet.getString(8)); // USERFLD2
				String ORDDATE = strUtil.fString(rsSRCDet.getString(9)); // DELDATE
				String TYPE = strUtil.fString(rsSRCDet.getString(10)); // POLTYPE
				String BILNAME = strUtil.fString(rsSRCDet.getString(11)); // USERFLD3
				BILNAME = strUtil.InsertQuotes(BILNAME);
				String PICKSEQ = strUtil.fString(rsSRCDet.getString(12)); // USERFLD4
				String SHPCITY = strUtil.fString(rsSRCDet.getString(13)); // USERFLD5
				String EXPDATE = strUtil.fString(rsSRCDet.getString(14)); // USERTIME1
				float fOrdQty = new Float(QTYORDERED).floatValue();
				float fUnitPrice = new Float(UNITPRICE).floatValue();
				String selectDolno4Dodet = "select  isnull(max(cast((dolnno) as integer)),0)+1 As Cnt"
						+ "  from dodet where DONO='"
						+ ORDNUMBER
						+ "' AND COMMENT2='ODBC2'";

				rsDestExist = stmtDestExists
						.executeQuery("SELECT * FROM DODET WHERE DONO='"
								+ ORDNUMBER + "' AND COMMENT1 ='" + LINENUM
								+ "' AND COMMENT2='ODBC2'");
				if (!(rsDestExist.next())) {
					rsDolno = stmtDolno.executeQuery(selectDolno4Dodet);
					while (rsDolno.next()) {
						DolnnoCnt = rsDolno.getInt("Cnt");
					}
					String stmtInsertDet = "INSERT INTO DODET(PLANT,DONO,DOLNNO,ITEM,USERFLD1,QTYOR,QTYIS,UNITMO,USERDBL1,USERFLD2,DELDATE,POLTYPE,USERFLD3,USERFLD4,USERFLD5,USERTIME1,LNSTAT,CRAT,CRBY,COMMENT1,COMMENT2) VALUES "
							+ "('"
							+ plant
							+ "','"
							+ ORDNUMBER
							+ "',"
							+ DolnnoCnt
							+ ",'"
							+ ITEM
							+ "','"
							+ DESC
							+ "',"
							+ fOrdQty
							+ ",0.0,'"
							+ ORDUNIT
							+ "',"
							+ fUnitPrice
							+ ",'"
							+ CUSTOMER
							+ "','"
							+ ORDDATE
							+ "','"
							+ TYPE
							+ "','"
							+ BILNAME
							+ "','"
							+ PICKSEQ
							+ "','"
							+ SHPCITY
							+ "','"
							+ EXPDATE
							+ "','N','"
							+ CRAT
							+ "','','"
							+ LINENUM + "','ODBC2')";

					stmtDestDet.execute(stmtInsertDet);

				} else {

					String updateDet = "UPDATE DODET SET QTYOR="
							+ new Float(QTYORDERED).floatValue()
							+ " WHERE DONO = '" + ORDNUMBER
							+ "' AND COMMENT1 = '" + LINENUM
							+ "' AND COMMENT2='ODBC2' ";

					stmtDestDet.execute(updateDet);
				}
				doList.add(ORDNUMBER);
				cnt = cnt + 1;
			}
			System.out.println("SyncDO2 :: " + cnt
					+ " synchronized successfully");
			if (doList.size() > 0) {
				String sql_donos = strUtil
						.getStringSeparatedByQuotsFromList(doList);

				int iCntClose = stmtCloseDO
						.executeUpdate("UPDATE DODET SET LNSTAT = 'C' WHERE DONO NOT IN ("
								+ sql_donos
								+ ")AND COMMENT2='ODBC2' AND DONO NOT IN (SELECT DONO FROM DODET WHERE  LNSTAT ='N')");

			} else {
				int iCntClose = stmtCloseDO
						.executeUpdate("UPDATE DODET SET LNSTAT = 'C' WHERE DONO NOT IN ('')AND COMMENT2='ODBC2' AND DONO NOT IN (SELECT DONO FROM DODET WHERE  LNSTAT ='N')");

			}
			flg = true;
		} catch (Exception e) {
			flg = false;

			throw new Exception("Could not able to synchronize DO2 ");
		} finally {
			try {
				DbBean.closeConnection(connSRCDet, stmtSrcDet);
				DbBean.closeConnection(connDESTDet, stmtDestDet);
				DbBean.closeConnection(connDESDetUpdate, stmtDestUpdate);
				DbBean.closeConnection(connDESDetExists, stmtDestExists);
				DbBean.closeConnection(connDolno, stmtDolno);
				DbBean.closeConnection(connCloseDO, stmtCloseDO);
			} catch (Exception e) {

				throw new Exception(
						"Could not able to synchronize Items from Server");
			}
		}
		Date dt1 = new Date();

		return flg;

	}

	public int getNextPolnno(String aPlant, String aPono) throws Exception {
		Connection connDESTDet = null;
		Statement stmtNextpolnno = null;
		ResultSet rs = null;
		int nextPolnno = 0;
		try {
			connDESTDet = DbBean.getConnection();
			stmtNextpolnno = connDESTDet.createStatement();
			rs = stmtNextpolnno
					.executeQuery("select ISNULL(cast(max(polnno) as integer)+1,0) as nextPolnno from podet where plant = '"
							+ aPlant + "' AND pono= '" + aPono + "'");
			while (rs.next()) {
				nextPolnno = rs.getInt("nextPolnno");
			}
		} catch (Exception e) {
		} finally {
			connDESTDet.close();
		}
		return nextPolnno;
	}
}
