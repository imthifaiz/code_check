package com.track.session.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.transaction.UserTransaction;

import com.track.db.util.InvUtil;
import com.track.gates.DbBean;
import com.track.tables.CYCLECNT;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@Deprecated
public class ReportSesBean implements SessionBean {
	java.util.Date dt = new java.util.Date();
	SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
	String today = dfVisualDate.format(dt);
	MLogger logger = new MLogger();
	StrUtils strUtils = new StrUtils();
	UserTransaction ut;
	InvUtil invUtil = new InvUtil();
	CYCLECNT cyclecntTbl = new CYCLECNT();
	public static final boolean DEBUG = true;
	SessionContext sessionContext;

	public void ejbCreate() {
	}

	public void ejbRemove() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void setSessionContext(SessionContext sessionContext) {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		this.sessionContext = sessionContext;
	}

	public List reportOnCycleCnt2Inv(int i) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		List listReport = new ArrayList();
		PreparedStatement statement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
		} catch (Exception e) {
			throw new Exception(e.getLocalizedMessage());
		}
		try {

			String extraFilter = "";
			// 15.12.2004 comment by ariff due to wrong implementation on view.
			if (i == 1)
				extraFilter = " and INVQTY <> CYCLECOUNTQTY";
			statement = con
					.prepareStatement("SELECT A.ITEM,B.ITEMDESC,A.LOC,A.INVQTY,A.CYCLECOUNTQTY,QTYDIFF FROM CompInvMstNCycCnt a, ITEMMST b WHERE  a.item = b.item  and a.item "
							+ "in(select distinct(item) from cyclecnt where '"
							+ today
							+ "'"
							+ "between cyclesdate and cycleedate) "
							+ extraFilter + " ORDER BY A.ITEM");
			// if(i==1) extraFilter = " and INVQTY <> BACKQTY";
			// statement =
			// con.prepareStatement("SELECT A.PLANT, A.ITEM,B.ITEMDESC, A.USRKEY1, A.USRKEY2, A.INVQTY,A.BACKQTY FROM CompBckStkNInvMst a, ITEMMST b WHERE  a.item = b.item  and a.item in(select distinct(item) from cyclecnt) "+extraFilter+" ORDER BY A.ITEM");
			rs = statement.executeQuery();
			while (rs.next()) {
				Vector vec = new Vector();
				vec.add(0, strUtils.fString((String) rs.getString(1)));
				vec.add(1, strUtils.fString((String) rs.getString(2)));
				vec.add(2, strUtils.fString((String) rs.getString(3)));
				vec.add(3, strUtils.fString((String) rs.getString(4)));
				vec.add(4, strUtils.fString((String) rs.getString(5)));
				vec.add(5, strUtils.fString((String) rs.getString(6)));

				listReport.add(vec);

			}
		} catch (Exception ee) {
			logger.log("reportOnStockTake2Inv :: Exception : " + ee.toString(),
					DEBUG);
			throw new Exception(ee.getLocalizedMessage());
		} finally {
			DbBean.closeConnection(con, statement);
		}
		return listReport;
	}

	public List reportOnPhysicalStockTake2Inv(int i, String fromLoc,
			String toLoc) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");

		List listReport = new ArrayList();
		PreparedStatement statement = null;
		ResultSet rs = null;
		Connection con = null;
		String betweenStmt = "";
		try {
			con = DbBean.getConnection();
		} catch (Exception e) {
			throw new Exception(e.getLocalizedMessage());
		}
		try {
			String extraFilter = "";
			if (i == 1)
				extraFilter = " and INVQTY <> CYCLECOUNTQTY";
			// statement =
			// con.prepareStatement("SELECT A.ITEM,B.ITEMDESC,A.LOC,A.INVQTY,A.CYCLECOUNTQTY,QTYDIFF FROM CompInvMstNCycCnt a, ITEMMST b WHERE  a.item = b.item "+extraFilter+" ORDER BY A.ITEM");
			if (fromLoc.length() > 0 && toLoc.length() > 0)
				betweenStmt = " and LOC between " + fromLoc + " and " + toLoc
						+ " ";
			String stmt = "SELECT A.ITEM,B.ITEMDESC,A.LOC,A.INVQTY,A.CYCLECOUNTQTY,QTYDIFF FROM CompInvMstNCycCnt a, ITEMMST b WHERE  a.item = b.item  "
					+ extraFilter + betweenStmt + " ORDER BY A.ITEM";
			MLogger.log(0, "[SQL]" + stmt);
			statement = con.prepareStatement(stmt);
			rs = statement.executeQuery();
			while (rs.next()) {
				Vector vec = new Vector();
				vec.add(0, strUtils.fString((String) rs.getString(1)));
				vec.add(1, strUtils.fString((String) rs.getString(2)));
				vec.add(2, strUtils.fString((String) rs.getString(3)));
				vec.add(3, strUtils.fString((String) rs.getString(4)));
				vec.add(4, strUtils.fString((String) rs.getString(5)));
				vec.add(5, strUtils.fString((String) rs.getString(6)));

				listReport.add(vec);
			}
		} catch (Exception ee) {
			logger.log("reportOnStockTake2Inv :: Exception : " + ee.toString(),
					DEBUG);
			throw new Exception(ee.getLocalizedMessage());
		} finally {
			DbBean.closeConnection(con, statement);
		}
		return listReport;
	}

	public boolean updateInventoryWithStockTake(boolean isCycleCount)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean result = false;
		String con_4_cycleCount = "";
		PreparedStatement ps_delete_inv = null;
		PreparedStatement ps_insert_inv = null;
		PreparedStatement ps_update_cc = null;

		Connection con_delete_inv = null;
		Connection con_insert_inv = null;
		Connection con_update_cc = null;

		if (isCycleCount == true) {
			con_4_cycleCount = "where item in(select distinct(item) from cyclecnt "
					+ "where '"
					+ today
					+ "' between cyclesdate and cycleedate)";
		}
		try {
			String insert_invmst = "INSERT INTO invmst SELECT * from cyclecount "
					+ con_4_cycleCount;
			String delete_invmst = "DELETE FROM INVMST " + con_4_cycleCount;
			String update_cc = "UPDATE CYCLECOUNT SET USERFLG1 = 'C' "
					+ con_4_cycleCount;

			con_delete_inv = DbBean.getConnection();
			con_insert_inv = DbBean.getConnection();
			con_update_cc = DbBean.getConnection();
			int iCnt = 0;

			ps_delete_inv = con_delete_inv.prepareStatement(delete_invmst);
			iCnt = ps_delete_inv.executeUpdate();
			if (iCnt < 0) {

				return false;
			}

			ps_insert_inv = con_insert_inv.prepareStatement(insert_invmst);
			iCnt = ps_insert_inv.executeUpdate();
			if (iCnt <= 0) {

				return false;
			}

			ps_update_cc = con_update_cc.prepareStatement(update_cc);
			iCnt = ps_update_cc.executeUpdate();
			if (iCnt < 0) {

				return false;
			}
			result = true;

		} catch (Exception e) {
			System.out.println("Inventory Insert : " + e.toString());
		} finally {
			DbBean.closeConnection(con_delete_inv, ps_delete_inv);
			DbBean.closeConnection(con_insert_inv, ps_insert_inv);
			DbBean.closeConnection(con_update_cc, ps_update_cc);
		}
		return result;

	}

	public boolean updateInventoryWithStockTake4Loc(String fromLoc, String toLoc)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean result = false;

		String betweenStmt = "";
		PreparedStatement ps_delete_inv = null;
		PreparedStatement ps_insert_inv = null;
		PreparedStatement ps_update_cc = null;

		Connection con_delete_inv = null;
		Connection con_insert_inv = null;
		Connection con_update_cc = null;

		if (fromLoc.length() > 0 && toLoc.length() > 0)
			betweenStmt = " Where  loc between " + fromLoc + " and " + toLoc
					+ " ";
		try {
			String insert_invmst = "INSERT INTO invmst SELECT * from cyclecount "
					+ betweenStmt;
			String delete_invmst = "DELETE FROM INVMST " + betweenStmt;
			String update_cc = "DELETE FROM CYCLECOUNT " + betweenStmt;

			con_delete_inv = DbBean.getConnection();
			con_insert_inv = DbBean.getConnection();
			con_update_cc = DbBean.getConnection();
			int iCnt = 0;

			ps_delete_inv = con_delete_inv.prepareStatement(delete_invmst);
			iCnt = ps_delete_inv.executeUpdate();
			if (iCnt < 0) {
				System.out.println("delete_invmst" + delete_invmst);
				System.out.println("Couldn't delete item from invmst");
				return false;
			}

			ps_insert_inv = con_insert_inv.prepareStatement(insert_invmst);
			iCnt = ps_insert_inv.executeUpdate();
			if (iCnt <= 0) {
				System.out.println("insert_invmst" + insert_invmst);
				System.out.println("Couldn't insert item from invmst");
				return false;
			}

			ps_update_cc = con_update_cc.prepareStatement(update_cc);
			iCnt = ps_update_cc.executeUpdate();
			if (iCnt < 0) {

				System.out.println("Couldn't update the status in CycleCount");
				return false;
			}
			result = true;

		} catch (Exception e) {
			System.out.println("Inventory Insert : " + e.toString());
		} finally {
			DbBean.closeConnection(con_delete_inv, ps_delete_inv);
			DbBean.closeConnection(con_insert_inv, ps_insert_inv);
			DbBean.closeConnection(con_update_cc, ps_update_cc);
		}
		return result;

	}

	public boolean canUpdateCCInvenoty(boolean isCycleCount) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean flg = false;
		String con_4_cycleCount = "";
		Connection con = null;
		con = DbBean.getConnection();
		if (isCycleCount == true) {
			con_4_cycleCount = " and  item in(select distinct(item) from cyclecnt "
					+ " where '"
					+ today
					+ "' between cyclesdate and cycleedate)";
		}

		try {
			Statement stmt = con.createStatement();
			java.util.Date dt = new java.util.Date();
			SimpleDateFormat dfVisualDate = new SimpleDateFormat("MM/dd/yyyy");
			String today = dfVisualDate.format(dt);
			String q = "SELECT DISTINCT(ITEM) FROM CYCLECOUNT where  (ISNULL(USERFLG1,'') = '' OR ISNULL(USERFLG1,'') != 'C') "
					+ con_4_cycleCount;

			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				flg = true;
			}
		} catch (Exception e) {
		}
		return flg;
	}
}