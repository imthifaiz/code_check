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

public class SyncPO {
	public SyncPO() {
	}

	private static final boolean DEBUG = true;
	DateUtils dateUtils = new DateUtils();
	MLogger logger = new MLogger();
	StrUtils strUtil = new StrUtils();
	DateUtils dateUtil = new DateUtils();
	String CRAT = dateUtils.getDate(new java.text.SimpleDateFormat(
			"yyyyMMddhhmmss"));
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public boolean syncPOProcess() throws Exception {
		String upat = dateUtil.getDateTime();
		boolean flg = false;
		List poList = new ArrayList();
		String plant = IConstants.PLANT_VAL;
		Date dt = new Date();
		logger.log("Sync::syncPO(" + plant + ") Starting @@@@@ .... at"
				+ formatter.format(dt), DEBUG);
		Connection connSRCHdr = null;
		Connection connSRCDet = null;
		Connection connDESTHdr = null;
		Connection connDESTDet = null;
		Connection connDESDetUpdate = null;
		Connection connDESDetExists = null;
		Connection connDESSrcExists = null;
		Connection connClosePo = null;
		Connection connTimeStamp = null;

		ResultSet rsSRCHdr = null;
		ResultSet rsSRCDet = null;
		ResultSet rsDestExist = null;
		ResultSet rsSrcExist = null;
		ResultSet rsTime = null;

		Statement stmtSrcHdr = null;
		Statement stmtSrcDet = null;
		Statement stmtDestHdr = null;
		Statement stmtDestDet = null;
		Statement stmtDestUpdate = null;
		Statement stmtDestExists = null;
		Statement stmtGetMaxPolnno = null;
		Statement stmtSrcExists = null;
		Statement stmtClosePo = null;
		Statement stmtTimeStamp = null;

		String CRAT = dateUtils.getDate(new java.text.SimpleDateFormat(
				"yyyyMMddhhmmss"));
		try {
			connSRCHdr = DbBean.getODBCConnection4BPCS_MASTERS();
			connSRCDet = DbBean.getODBCConnection4BPCS_MASTERS();
			connDESTHdr = DbBean.getConnection();
			connDESTDet = DbBean.getConnection();
			connDESDetUpdate = DbBean.getConnection();
			connDESDetExists = DbBean.getConnection();
			connDESSrcExists = DbBean.getConnection();
			connClosePo = DbBean.getConnection();
			connTimeStamp = DbBean.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String selectSrcDet = "SELECT POPORH1.PORTYPE, POPORH1.DATE, POPORH1.PONUMBER, "
				+ "POPORH1.VDCODE, POPORH1.VDNAME, POPORL.ITEMNO, POPORL.ITEMDESC,"
				+ " POPORL.OQORDERED, POPORL.UNITCOST, POPORL.PORLSEQ, "
				+ " POPORH1.EXPARRIVAL "
				+ " FROM POPORH1 POPORH1, POPORL POPORL "
				+ " WHERE POPORH1.PORHSEQ = POPORL.PORHSEQ AND ((POPORH1.PORTYPE=3))";

		try {
			stmtSrcHdr = connSRCHdr.createStatement();

			stmtSrcDet = connSRCDet.createStatement();

			stmtDestHdr = connDESTHdr.createStatement();
			stmtDestDet = connDESTDet.createStatement();
			stmtDestUpdate = connDESDetUpdate.createStatement();
			stmtDestExists = connDESDetExists.createStatement();
			stmtSrcExists = connDESSrcExists.createStatement();
			stmtClosePo = connClosePo.createStatement();
			stmtTimeStamp = connTimeStamp.createStatement();

			rsSRCDet = stmtSrcDet.executeQuery(selectSrcDet);
			int i = 0;
			int cnt = 0;
			String sUpat = "";
			while (rsSRCDet.next()) {
				String PORTYPE = strUtil.fString(rsSRCDet.getString(1)); // POLTYPE
				String PODATE = strUtil.fString(rsSRCDet.getString(2)); // DELDATE
				String PONUMBER = strUtil.fString(rsSRCDet.getString(3)); // PONO
				String VDCODE = strUtil.fString(rsSRCDet.getString(4)); // USERFLD2
				String VDNAME = strUtil.fString(rsSRCDet.getString(5)); // USERFLD3
				VDNAME = strUtil.InsertQuotes(VDNAME);
				String ITEMNO = strUtil.fString(rsSRCDet.getString(6)); // ITEM
				String ITEMDESC = strUtil.fString(rsSRCDet.getString(7)); // USERFLD1
				ITEMDESC = strUtil.InsertQuotes(ITEMDESC);

				String OQORDERED = strUtil.fString(rsSRCDet.getString(8)); // QTYOR
				String UNITCOST = strUtil.fString(rsSRCDet.getString(9)); // UNITMO
				String PORLSEQ = strUtil.fString(rsSRCDet.getString(10)); // USERFLD4
				String EXPARRIVAL = strUtil.fString(rsSRCDet.getString(11)); // USERTIME1
				float fOrdQty = new Float(OQORDERED).floatValue(); // QTYOR

				String selectTimeStamp = "SELECT TOP 1 UPAT  FROM PODET WHERE PONO='"
						+ PONUMBER + "' AND ITEM='" + ITEMNO + "'";

				rsDestExist = stmtDestExists
						.executeQuery("SELECT * FROM PODET WHERE PONO='"
								+ PONUMBER + "' AND ITEM ='" + ITEMNO + "'");

				rsTime = stmtTimeStamp.executeQuery(selectTimeStamp);
				while (rsTime.next()) {
					sUpat = rsTime.getString("UPAT");
				}

				if (!(rsDestExist.next())) {
					int nextlno = getNextPolnno(plant, PONUMBER);

					String stmtInsertDet = "INSERT INTO PODET(PLANT,PONO,POLNNO,LNSTAT,ITEM,QTYOR,QTYRC,DELDATE,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,USERTIME1,POLTYPE,CRAT,CRBY,UPAT) VALUES "
							+ "('"
							+ plant
							+ "','"
							+ PONUMBER
							+ "',"
							+ nextlno
							+ ",'N','"
							+ ITEMNO
							+ "',"
							+ fOrdQty
							+ ",0.0,'"
							+ PODATE
							+ "','"
							+ UNITCOST
							+ "','"
							+ ITEMDESC
							+ "','"
							+ VDCODE
							+ "','"
							+ VDNAME
							+ "','"
							+ PORLSEQ
							+ "','"
							+ EXPARRIVAL
							+ "','"
							+ PORTYPE
							+ "','"
							+ CRAT + "','','" + upat + "')";
					stmtDestDet.execute(stmtInsertDet);
				} else {

					if (strUtil.fString(sUpat).equalsIgnoreCase(upat)) {

						String updateDet = "UPDATE PODET SET QTYOR=ISNULL(QTYOR,0) + "
								+ fOrdQty
								+ " WHERE PONO = '"
								+ PONUMBER
								+ "' AND ITEM = '" + ITEMNO + "' ";

						stmtDestDet.execute(updateDet);
					} else {

						String updateDet = "UPDATE PODET SET QTYOR=" + fOrdQty
								+ ",UPAT = '" + upat + "' WHERE PONO = '"
								+ PONUMBER + "' AND ITEM = '" + ITEMNO + "' ";

						stmtDestDet.execute(updateDet);
					}
				}
				poList.add(PONUMBER);
				cnt = cnt + 1;
			}

			if (poList.size() > 0) {
				String sql_ponos = strUtil
						.getStringSeparatedByQuotsFromList(poList);
				int iCntClose = stmtClosePo
						.executeUpdate("UPDATE PODET SET LNSTAT = 'C' WHERE PONO NOT IN ("
								+ sql_ponos
								+ ") AND PONO NOT IN (SELECT PONO FROM PODET WHERE  LNSTAT ='N') ");

			} else {
				int iCntClose = stmtClosePo
						.executeUpdate("UPDATE PODET SET LNSTAT = 'C' WHERE PONO NOT IN ('') AND PONO NOT IN (SELECT PONO FROM PODET WHERE  LNSTAT ='N') ");

			}
			flg = true;
		} catch (Exception e) {
			flg = false;

			throw new Exception("Could not able to synchronize PO");
		} finally {
			try {
				DbBean.closeConnection(connSRCHdr, stmtSrcHdr);
				DbBean.closeConnection(connSRCDet, stmtSrcDet);
				DbBean.closeConnection(connDESTHdr, stmtDestHdr);
				DbBean.closeConnection(connDESTDet, stmtDestDet);
				DbBean.closeConnection(connDESDetUpdate, stmtDestUpdate);
				DbBean.closeConnection(connDESDetExists, stmtDestExists);
				DbBean.closeConnection(connDESSrcExists, stmtSrcExists);
				DbBean.closeConnection(connClosePo, stmtClosePo);
				DbBean.closeConnection(connTimeStamp, stmtTimeStamp);
			} catch (Exception e) {

				throw new Exception("Could not able to synchronize Items");
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
					.executeQuery("select isnull(max(cast((polnno) as integer)),0)+1 as nextPolnno from podet where plant = '"
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

	public String getTimeStamp(String aPono, String aItem) {
		Connection conn = null;
		Statement stmtTimestamp = null;
		ResultSet rs = null;
		String upAt = "";
		try {
			conn = DbBean.getConnection();
			stmtTimestamp = conn.createStatement();
			rs = stmtTimestamp
					.executeQuery("SELECT TOP 1 UPAT  FROM PODET WHERE PONO='"
							+ aPono + "' AND ITEM='" + aItem + "' ");
			while (rs.next()) {
				upAt = rs.getString("UPAT");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			DbBean.closeConnection(conn, stmtTimestamp);
		}
		return upAt;
	}
}