package com.track.sync;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.track.constants.IConstants;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;

public class SyncDO {
	public SyncDO() {
	}

	private static final boolean DEBUG = true;
	DateUtils dateUtils = new DateUtils();
	MLogger logger = new MLogger();
	String CRAT = dateUtils.getDate(new java.text.SimpleDateFormat(
			"yyyyMMddhhmmss"));
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public boolean syncDOProcess(String sDono) throws Exception {
		boolean flg = false;
		String plant = IConstants.PLANT_VAL;
		Date dt = new Date();

		// Connection connSRCHdr = null;
		Connection connSRCDet = null;
		// Connection connDESTHdr = null;
		Connection connDESTDet = null;
		Connection connDESDetUpdate = null;
		Connection connDESDetExists = null;
		// Connection connDESSrcExists = null;

		// ResultSet rsSRCHdr = null;
		ResultSet rsSRCDet = null;
		ResultSet rsDestExist = null;
		// ResultSet rsSrcExist = null;

		Statement stmtSrcHdr = null;
		Statement stmtSrcDet = null;
		Statement stmtDestHdr = null;
		Statement stmtDestDet = null;
		Statement stmtDestUpdate = null;
		Statement stmtDestExists = null;
		Statement stmtGetMaxPolnno = null;
		Statement stmtSrcExists = null;
		try {
			// connSRCHdr = DbBean.getODBCConnection4BPCS_MASTERS();
			connSRCDet = DbBean.getODBCConnection4BPCS_MASTERS();
			// connDESTHdr = DbBean.getConnection();
			connDESTDet = DbBean.getConnection();
			connDESDetUpdate = DbBean.getConnection();
			connDESDetExists = DbBean.getConnection();
			// connDESSrcExists = DbBean.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String selectSrcDet = "SELECT OEORDD.LINENUM, OEORDD.ITEM, OEORDD.\"DESC\","
				+ "OEORDD.QTYORDERED, OEORDD.ORDUNIT, OEORDD.UNITPRICE, "
				+ "OEORDH.ORDNUMBER, OEORDH.CUSTOMER, OEORDH.ORDDATE, "
				+ "OEORDH.TYPE, OEORDH.BILNAME, OEORDD.PICKSEQ, OEORDH.SHPCITY,"
				+ "OEORDH.EXPDATE "
				+ "FROM OEORDD OEORDD, OEORDH OEORDH"
				+ "WHERE OEORDD.ORDUNIQ = OEORDH.ORDUNIQ AND ((OEORDH.TYPE=2))";
		try {
			stmtSrcDet = connSRCDet.createStatement();
			stmtDestDet = connDESTDet.createStatement();
			stmtDestUpdate = connDESDetUpdate.createStatement();
			stmtDestExists = connDESDetExists.createStatement();

			rsSRCDet = stmtSrcDet.executeQuery(selectSrcDet);
			int cnt = 0;
			String CRAT = dateUtils.getDate(new java.text.SimpleDateFormat(
					"yyyyMMddhhmmss"));
			while (rsSRCDet.next()) {

				String LINENUM = rsSRCDet.getString(1); // DOLO
				String ITEM = rsSRCDet.getString(2); // ITEM
				String DESC = rsSRCDet.getString(3); // USERFLD1
				String QTYORDERED = rsSRCDet.getString(4); // QTYOR
				String ORDUNIT = rsSRCDet.getString(5); // UNITMO
				String UNITPRICE = rsSRCDet.getString(6); // USERDBL1
				String ORDNUMBER = rsSRCDet.getString(7); // DONO
				String CUSTOMER = rsSRCDet.getString(8); // USERFLD2
				String ORDDATE = rsSRCDet.getString(9); // DELDATE
				String TYPE = rsSRCDet.getString(10); // POLTYPE
				String BILNAME = rsSRCDet.getString(11); // USERFLD3
				String PICKSEQ = rsSRCDet.getString(12); // USERFLD4
				String SHPCITY = rsSRCDet.getString(13); // USERFLD5
				String EXPDATE = rsSRCDet.getString(14); // USERTIME1

				rsDestExist = stmtDestExists
						.executeQuery("SELECT * FROM DODET WHERE DONO='"
								+ ORDNUMBER + "' AND DOLNNO ='" + LINENUM + "'");
				if (!(rsDestExist.next())) {
					
					String stmtInsertDet = "INSERT INTO DODET(PLANT,DONO,DOLNNO,ITEM,USERFLD1,QTYOR,QTYIS,UNITMO,USERDBL1,USERFLD2,DELDATE,POLTYPE,USERFLD3,USERFLD4,USERFLD5,USERTIME1,LNSTAT,CRAT,CRBY) VALUES "
							+ "('"
							+ plant
							+ "','"
							+ ORDNUMBER
							+ "',"
							+ LINENUM
							+ ",'"
							+ ITEM
							+ "','"
							+ DESC
							+ "'"
							+ new Float(QTYORDERED).floatValue()
							+ ",0.0,'"
							+ ORDUNIT
							+ "',new Float(UNITPRICE).floatValue(),'"
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
							+ "','N','" + CRAT + "','')";
					
					stmtDestDet.execute(stmtInsertDet);
				} else {
					String updateDet = "UPDATE DODET SET QTYOR="
							+ new Float(QTYORDERED).floatValue()
							+ " WHERE DONO = '" + ORDNUMBER
							+ "' AND DOLNNO = '" + LINENUM + "'";
					
					stmtDestDet.execute(updateDet);
				}
				cnt = cnt + 1;
			}
			flg = true;
		} catch (Exception e) {
			flg = false;
			System.out.println("SyncDO :: Exception : " + e.toString());
			throw new Exception("Could not able to synchronize DO ");
		} finally {
			try {
				// connSRCHdr.close();
				connSRCDet.close();
				// connDESTHdr.close();
				connDESTDet.close();
				connDESDetUpdate.close();
				connDESDetExists.close();
				// connDESSrcExists.close();

			} catch (Exception e) {
				System.out.println("error in OUTER loop : " + e.toString());
				throw new Exception(
						"Could not able to synchronize Items from Server");
			}
		}
		Date dt1 = new Date();
		logger.log("SyncBPCSMasters::syncItems(" + plant
				+ ") Ended...@@@@@   at " + formatter.format(dt1), DEBUG);
		return flg;

	}

	public boolean syncDOProcessOld(String sDono) throws Exception {
		boolean flg = false;
		String plant = IConstants.PLANT_VAL;
		Date dt = new Date();
		logger.log("Sync::syncDO(" + plant + ") Starting @@@@@ .... at"
				+ formatter.format(dt), DEBUG);
		Connection connSRCHdr = null;
		Connection connSRCDet = null;
		Connection connDESTHdr = null;
		Connection connDESTDet = null;
		Connection connDESDetUpdate = null;
		Connection connDESDetExists = null;
		Connection connDESSrcExists = null;

		ResultSet rsSRCHdr = null;
		ResultSet rsSRCDet = null;
		ResultSet rsDestExist = null;
		ResultSet rsSrcExist = null;

		Statement stmtSrcHdr = null;
		Statement stmtSrcDet = null;
		Statement stmtDestHdr = null;
		Statement stmtDestDet = null;
		Statement stmtDestUpdate = null;
		Statement stmtDestExists = null;
		Statement stmtGetMaxPolnno = null;
		Statement stmtSrcExists = null;
		try {
			connSRCHdr = DbBean.getODBCConnection4BPCS_MASTERS();
			connSRCDet = DbBean.getODBCConnection4BPCS_MASTERS();
			connDESTHdr = DbBean.getConnection();
			connDESTDet = DbBean.getConnection();
			connDESDetUpdate = DbBean.getConnection();
			connDESDetExists = DbBean.getConnection();
			connDESSrcExists = DbBean.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String selectSrcHdr = "SELECT OEORDH.ORDNUMBER, OEORDH.TYPE, OEORDH.ORDDATE, OEORDH.EXPDATE,"
				+ "OEORDH.CUSTOMER, OEORDH.BILNAME,OEORDH.SHPCOUNTRY FROM OEORDH OEORDH "
				+ "WHERE (OEORDH.ORDNUMBER='" + sDono + "' AND TYPE = 2";

		String selectSrcDet = "SELECT OEORDD.LINENUM, OEORDD.ITEM, OEORDD.ITEMDESC, OEORDD.QTYORDERED, OEORDD.ORDUNIT, "
				+ " OEORDD.UNITPRICE, OEORDD.PICKSEQ FROM OEORDD OEORDD WHERE (OEORDD.ORDUNIQ='"
				+ sDono + "')";

		
		try {
			stmtSrcHdr = connSRCHdr.createStatement();
			stmtSrcDet = connSRCDet.createStatement();
			stmtDestHdr = connDESTHdr.createStatement();
			stmtDestDet = connDESTDet.createStatement();
			stmtDestUpdate = connDESDetUpdate.createStatement();
			stmtDestExists = connDESDetExists.createStatement();
			stmtSrcExists = connDESSrcExists.createStatement();

			rsSRCHdr = stmtSrcHdr.executeQuery(selectSrcHdr);
			rsSRCDet = stmtSrcDet.executeQuery(selectSrcDet);
			int i = 0;
			/*
			 * if(!(rsSRCHdr.next())) {
			 * System.out.println("No records found in source"); return false; }
			 * else {
			 */
			int cnt = 0;
			while (rsSRCHdr.next()) {
				String ORDNUMBER = rsSRCHdr.getString(1);
				String TYPE = rsSRCHdr.getString(2);
				String ORDDATE = rsSRCHdr.getString(3);
				String EXPDATE = rsSRCHdr.getString(4);
				String CUSTOMER = rsSRCHdr.getString(5);
				String BILNAME = rsSRCHdr.getString(6);
				String AREA = rsSRCHdr.getString(7);
				rsSrcExist = stmtSrcExists.executeQuery("SELECT * FROM DOHDR WHERE PLANT = '"
								+ plant + "' AND DONO = '" + ORDNUMBER + "'");
				if (!(rsSrcExist.next())) {
				
					String stmtInsertHdr = "INSERT INTO DOHDR (PLANT,DONO,DOTYPE,CUSTNO,ORDDATE,DELDATE,USERFLD1,USERFLD2) VALUES ('"
							+ plant
							+ "','"
							+ ORDNUMBER
							+ "','"
							+ TYPE
							+ "','"
							+ CUSTOMER
							+ "','"
							+ ORDDATE
							+ "','"
							+ EXPDATE
							+ "','" + BILNAME + "','" + AREA + "')";
					// System.out.println("stmtInsertHdr: " + stmtInsertHdr);
					stmtDestHdr.execute(stmtInsertHdr);
				}
				while (rsSRCDet.next()) {
					String LINENUM = rsSRCDet.getString(1);
					String ITEM = rsSRCDet.getString(2);
					String DESC = rsSRCDet.getString(3);
					String QTYORDERED = rsSRCDet.getString(4);
					String ORDUNIT = rsSRCDet.getString(5);
					String UNITPRICE = rsSRCDet.getString(6);
					String PICKSEQ = rsSRCDet.getString(7);

					rsDestExist = stmtDestExists
							.executeQuery("SELECT * FROM DODET WHERE DONO='"
									+ ORDNUMBER + "' AND DOLNNO ='" + LINENUM
									+ "'");
					if (!(rsDestExist.next())) {
						System.out.println("Inserting record in DODET");
						String stmtInsertDet = "INSERT INTO DODET(PLANT,DONO,DOLNNO,LNSTAT,ITEM,QTYOR,QTYIS,QTYAC,QTYRJ,UNITMO,USERFLD1,USERFLD2) VALUES "
								+ "('"
								+ plant
								+ "','"
								+ ORDNUMBER
								+ "',"
								+ LINENUM
								+ ",'N','"
								+ ITEM
								+ "',"
								+ new Float(QTYORDERED).floatValue()
								+ ",0.0,0.0,new Float(UNITPRICE).floatValue(),'"
								+ DESC + "','" + PICKSEQ + "')";
						// System.out.println("stmtInsertDet: " +
						// stmtInsertDet);
						stmtDestDet.execute(stmtInsertDet);
					} else {

						String updateDet = "UPDATE PODET SET QTYOR="
								+ new Float(QTYORDERED).floatValue()
								+ " WHERE DONO = '" + ORDNUMBER
								+ "' AND DOLNNO = '" + LINENUM + "'";
						// System.out.println("updateDet: " + updateDet);
						stmtDestDet.execute(updateDet);
					}
					cnt = cnt + 1;
				}
			}

			flg = true;
			if (i == 0) {
				throw new Exception("DO not found on BPCS System");
			}
		} catch (Exception e) {
			flg = false;
			System.out.println("SyncDO :: Exception : " + e.toString());
			throw new Exception("Could not able to synchronize DO ");
		} finally {
			try {
				connSRCHdr.close();
				connSRCDet.close();
				connDESTHdr.close();
				connDESTDet.close();
				connDESDetUpdate.close();
				connDESDetExists.close();
				connDESSrcExists.close();

			} catch (Exception e) {
				System.out.println("error in OUTER loop : " + e.toString());
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
