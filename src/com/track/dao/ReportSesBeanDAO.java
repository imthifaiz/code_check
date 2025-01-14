package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpDepartmentMst;
import com.track.db.object.ItemQtyBigDecimalPojo;
import com.track.db.object.ItemQtyPojo;
import com.track.db.object.OpenCloseReportPojo;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class ReportSesBeanDAO extends BaseDAO {

	private boolean printQuery = MLoggerConstant.REPORTSESBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.REPORTSESBEANDAO_PRINTPLANTMASTERLOG;
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

	public List reportOnCycleCnt2Inv(int i) throws Exception {
		Date dt = new Date();
		SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
		String today = dfVisualDate.format(dt);
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

			if (i == 1)
				extraFilter = " and INVQTY <> CYCLECOUNTQTY";
			String stmt = "SELECT A.ITEM,B.ITEMDESC,A.LOC,A.INVQTY,A.CYCLECOUNTQTY,QTYDIFF FROM CompInvMstNCycCnt a, ITEMMST b WHERE  a.item = b.item  and a.item "
					+ "in(select distinct(item) from cyclecnt where '"
					+ today
					+ "'"
					+ "between cyclesdate and cycleedate) "
					+ extraFilter
					+ " ORDER BY A.ITEM";
			this.mLogger.query(this.printQuery, stmt);
			statement = con.prepareStatement(stmt);

			rs = statement.executeQuery();
			while (rs.next()) {
				Vector vec = new Vector();
				vec.add(0, StrUtils.fString((String) rs.getString(1)));
				vec.add(1, StrUtils.fString((String) rs.getString(2)));
				vec.add(2, StrUtils.fString((String) rs.getString(3)));
				vec.add(3, StrUtils.fString((String) rs.getString(4)));
				vec.add(4, StrUtils.fString((String) rs.getString(5)));
				vec.add(5, StrUtils.fString((String) rs.getString(6)));
				listReport.add(vec);
			}
		} catch (Exception ee) {
			this.mLogger.exception(this.printLog, "", ee);
			throw new Exception(ee.getLocalizedMessage());
		} finally {
			DbBean.closeConnection(con, statement);
		}
		return listReport;
	}

	public List reportOnPhysicalStockTake2Inv(int i, String fromLoc,
			String toLoc) throws Exception {

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

			if (fromLoc.length() > 0 && toLoc.length() > 0)
				betweenStmt = " and LOC between " + fromLoc + " and " + toLoc
						+ " ";
			String stmt = "SELECT A.ITEM,B.ITEMDESC,A.LOC,A.INVQTY,A.CYCLECOUNTQTY,QTYDIFF FROM CompInvMstNCycCnt a, ITEMMST b WHERE  a.item = b.item  "
					+ extraFilter + betweenStmt + " ORDER BY A.ITEM";
			this.mLogger.query(this.printQuery, stmt);
			statement = con.prepareStatement(stmt);
			rs = statement.executeQuery();
			while (rs.next()) {
				Vector vec = new Vector();
				vec.add(0, StrUtils.fString((String) rs.getString(1)));
				vec.add(1, StrUtils.fString((String) rs.getString(2)));
				vec.add(2, StrUtils.fString((String) rs.getString(3)));
				vec.add(3, StrUtils.fString((String) rs.getString(4)));
				vec.add(4, StrUtils.fString((String) rs.getString(5)));
				vec.add(5, StrUtils.fString((String) rs.getString(6)));
				listReport.add(vec);
			}
		} catch (Exception ee) {
			this.mLogger.exception(this.printLog, "", ee);
			throw new Exception(ee.getLocalizedMessage());
		} finally {
			DbBean.closeConnection(con, statement);
		}
		return listReport;
	}

	public boolean updateInventoryWithStockTake(boolean isCycleCount)
			throws Exception {
		boolean result = false;
		String con_4_cycleCount = "";
		PreparedStatement ps_delete_inv = null;
		PreparedStatement ps_insert_inv = null;
		PreparedStatement ps_update_cc = null;

		Connection con_delete_inv = null;
		Connection con_insert_inv = null;
		Connection con_update_cc = null;
		Date dt = new Date();
		SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
		String today = dfVisualDate.format(dt);
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
			this.mLogger.query(this.printQuery, delete_invmst);
			ps_delete_inv = con_delete_inv.prepareStatement(delete_invmst);
			iCnt = ps_delete_inv.executeUpdate();
			if (iCnt < 0) {

				return false;
			}
			this.mLogger.query(this.printQuery, insert_invmst);
			ps_insert_inv = con_insert_inv.prepareStatement(insert_invmst);
			iCnt = ps_insert_inv.executeUpdate();
			if (iCnt <= 0) {

				return false;
			}
			this.mLogger.query(this.printQuery, update_cc);
			ps_update_cc = con_update_cc.prepareStatement(update_cc);
			iCnt = ps_update_cc.executeUpdate();
			if (iCnt < 0) {
				return false;
			}
			result = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con_delete_inv, ps_delete_inv);
			DbBean.closeConnection(con_insert_inv, ps_insert_inv);
			DbBean.closeConnection(con_update_cc, ps_update_cc);
		}
		return result;

	}

	public boolean updateInventoryWithStockTake4Loc(String fromLoc, String toLoc)
			throws Exception {
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
			this.mLogger.query(this.printQuery, delete_invmst);
			ps_delete_inv = con_delete_inv.prepareStatement(delete_invmst);
			iCnt = ps_delete_inv.executeUpdate();
			if (iCnt < 0) {

				return false;
			}
			this.mLogger.query(this.printQuery, insert_invmst);
			ps_insert_inv = con_insert_inv.prepareStatement(insert_invmst);
			iCnt = ps_insert_inv.executeUpdate();
			if (iCnt <= 0) {
				return false;
			}
			this.mLogger.query(this.printQuery, update_cc);
			ps_update_cc = con_update_cc.prepareStatement(update_cc);
			iCnt = ps_update_cc.executeUpdate();
			if (iCnt < 0) {

				return false;
			}
			result = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con_delete_inv, ps_delete_inv);
			DbBean.closeConnection(con_insert_inv, ps_insert_inv);
			DbBean.closeConnection(con_update_cc, ps_update_cc);
		}
		return result;

	}

	public boolean canUpdateCCInvenoty(boolean isCycleCount) throws Exception {
		boolean flg = false;
		String con_4_cycleCount = "";
		Connection con = null;
		Date dt = new Date();
		SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
		String today = dfVisualDate.format(dt);
		if (isCycleCount == true) {
			con_4_cycleCount = " and  item in(select distinct(item) from cyclecnt "
					+ " where '"
					+ today
					+ "' between cyclesdate and cycleedate)";
		}
		Statement stmt = null;
		try {
			con = DbBean.getConnection();
			stmt = con.createStatement();
			String q = "SELECT DISTINCT(ITEM) FROM CYCLECOUNT where  (ISNULL(USERFLG1,'') = '' OR ISNULL(USERFLG1,'') != 'C') "
					+ con_4_cycleCount;
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				flg = true;
			}
		} catch (Exception e) {
		} finally {
			DbBean.closeConnection(con, stmt);
		}
		return flg;
	}
	
	
	public List<ItemQtyBigDecimalPojo> findByItemQtyS(String plant, String fromdate, String todate)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ItemQtyBigDecimalPojo> ItemQtyPojoList=new ArrayList<ItemQtyBigDecimalPojo>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT ITEM AS item,SUM(PICKQTY) AS qty FROM " +
					plant+"_SHIPHIS WHERE CONVERT(DATETIME, ISSUEDATE, 104) between  CONVERT(DATETIME,'"+fromdate+"', 104) " +
					"and  CONVERT(DATETIME, '"+todate+"', 104) GROUP BY ITEM";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ItemQtyBigDecimalPojo itemQtyPojo=new ItemQtyBigDecimalPojo();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, itemQtyPojo);
	                   ItemQtyPojoList.add(itemQtyPojo);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			//throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return ItemQtyPojoList;
	}
	
	public List<ItemQtyBigDecimalPojo> findByItemQtyToDateS(String plant, String date)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ItemQtyBigDecimalPojo> ItemQtyPojoList=new ArrayList<ItemQtyBigDecimalPojo>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT ITEM AS item,SUM(PICKQTY) AS qty FROM " +
					plant+"_SHIPHIS WHERE CONVERT(DATETIME, ISSUEDATE, 104) < CONVERT(DATETIME,'"+date+"', 104) " +
					"GROUP BY ITEM";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ItemQtyBigDecimalPojo itemQtyPojo=new ItemQtyBigDecimalPojo();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, itemQtyPojo);
	                   ItemQtyPojoList.add(itemQtyPojo);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			//throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return ItemQtyPojoList;
	}
	
	public List<ItemQtyBigDecimalPojo> findByItemQtyFromDateS(String plant, String date)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ItemQtyBigDecimalPojo> ItemQtyPojoList=new ArrayList<ItemQtyBigDecimalPojo>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT ITEM AS item,SUM(PICKQTY) AS qty FROM " +
		    		plant+"_SHIPHIS WHERE CONVERT(DATETIME, ISSUEDATE, 104) > CONVERT(DATETIME,'"+date+"', 104) " +
					"GROUP BY ITEM";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ItemQtyBigDecimalPojo itemQtyPojo=new ItemQtyBigDecimalPojo();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, itemQtyPojo);
	                   ItemQtyPojoList.add(itemQtyPojo);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			//throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return ItemQtyPojoList;
	}
	
	public List<ItemQtyBigDecimalPojo> findByItemQtyR(String plant, String fromdate, String todate)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ItemQtyBigDecimalPojo> ItemQtyPojoList=new ArrayList<ItemQtyBigDecimalPojo>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT ITEM AS item,SUM(RECQTY) AS qty FROM " +
					plant+"_RECVDET WHERE CONVERT(DATETIME, RECVDATE, 104) between CONVERT(DATETIME,'"+fromdate+"', 104) " +
		            "and  CONVERT(DATETIME,'"+todate+"', 104) GROUP BY ITEM";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ItemQtyBigDecimalPojo itemQtyPojo=new ItemQtyBigDecimalPojo();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, itemQtyPojo);
	                   ItemQtyPojoList.add(itemQtyPojo);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			//throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return ItemQtyPojoList;
	}
	
	public List<ItemQtyBigDecimalPojo> findByItemQtyToDateR(String plant, String date)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ItemQtyBigDecimalPojo> ItemQtyPojoList=new ArrayList<ItemQtyBigDecimalPojo>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT ITEM AS item,SUM(RECQTY) AS qty FROM " +
			    		plant+"_RECVDET WHERE CONVERT(DATETIME, RECVDATE, 104) < CONVERT(DATETIME,'"+date+"', 104) " +
			            "GROUP BY ITEM";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ItemQtyBigDecimalPojo itemQtyPojo=new ItemQtyBigDecimalPojo();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, itemQtyPojo);
	                   ItemQtyPojoList.add(itemQtyPojo);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			//throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return ItemQtyPojoList;
	}
	
	public List<ItemQtyBigDecimalPojo> findByItemQtyFromDateR(String plant, String date)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ItemQtyBigDecimalPojo> ItemQtyPojoList=new ArrayList<ItemQtyBigDecimalPojo>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT ITEM AS item,SUM(RECQTY) AS qty FROM " +
					plant+"_RECVDET WHERE CONVERT(DATETIME, RECVDATE, 104) > CONVERT(DATETIME,'"+date+"', 104) " +
		            "GROUP BY ITEM";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ItemQtyBigDecimalPojo itemQtyPojo=new ItemQtyBigDecimalPojo();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, itemQtyPojo);
	                   ItemQtyPojoList.add(itemQtyPojo);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			//throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return ItemQtyPojoList;
	}
	
	public String getavgcost(String plant, String item, String uom) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			String currency=new PlantMstDAO().getBaseCurrency(plant);
			conn = DbBean.getConnection();
			
			String query = "SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM " + plant + "_RECVDET R WHERE ITEM='" + item + "' AND "
					+ "CURRENCYID IS NOT NULL AND tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD') )>0 THEN "
					+ "(Select ISNULL(CAST(ISNULL(SUM(CASE WHEN A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from  "
					+ "(select TRAN_TYPE,RECQTY VC,CASE WHEN TRAN_TYPE = 'INVENTORYUPLOAD' THEN (isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM " + plant + "_CURRENCYMST WHERE "
					+ "CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')),0)*R.RECQTY) ELSE CAST( (CAST(ISNULL(ISNULL((select ISNULL(QPUOM,1) from " + plant + "_UOM where UOM=''),1) * "
					+ "( ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM " + plant + "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')),0)+ ISNULL((SELECT "
					+ "(SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM " + plant + "_RECVDET c left join " + plant + "_FINBILLHDR d on c.PONO = d.PONO "
					+ "and c.GRNO = d.GRNO left join " + plant + "_FINBILLDET e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = '" + item + "'" 
					+ "OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = '" + item + "'),0) +  (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM " + plant + "_CURRENCYMST WHERE " 
					+ "CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+ CASE WHEN (SELECT SUM(LANDED_COST) FROM " + plant + "_FINBILLHDR c join "
					+ plant + "_FINBILLDET d  ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select " 
					+ "SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from " + plant + "_podet s where s.pono=R.pono),0)),0))/100))/ (SELECT CURRENCYUSEQT  FROM " + plant + "_CURRENCYMST WHERE " 
					+ "CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"'))),0) / (ISNULL((select ISNULL(QPUOM,1) from " + plant + "_UOM where UOM='" + uom+ "'),1))) ,0) "
					+ "*(SELECT CURRENCYUSEQT FROM " + plant + "_CURRENCYMST    WHERE  CURRENCYID='"+currency+"')*(SELECT CURRENCYUSEQT  FROM " + plant + "_CURRENCYMST "    
					+ "WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')) AS DECIMAL(20,5)) )  * CAST((SELECT CURRENCYUSEQT "  
					+ "FROM " + plant + "_CURRENCYMST WHERE  CURRENCYID='"+currency+"')AS DECIMAL(20,5))  / CAST((SELECT CURRENCYUSEQT " 
					+ "FROM " + plant + "_CURRENCYMST WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')) AS DECIMAL(20,5)) "
					+ "* RECQTY AS DECIMAL(20,5)) END AS UNITCOST from " + plant + "_RECVDET R LEFT JOIN " + plant + "_POHDR P ON R.PONO = P.PONO " 
					+ "where item ='" + item + "' AND ISNULL(R.UNITCOST,0) != 0 AND tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD')    ) A) "   
					+ " ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM " + plant + "_RECVDET WHERE ITEM='" + item + "' AND tran_type IN('INVENTORYUPLOAD') )>0 "
					+ " THEN (SELECT SUM(UNITCOST) FROM " + plant + "_RECVDET C where item = '" + item + "' "
					+ " AND ISNULL(C.UNITCOST,0) != 0 AND tran_type IN('INVENTORYUPLOAD')) ELSE "
					+ "CAST(((SELECT M.COST FROM " + plant + "_ITEMMST M WHERE M.ITEM = '" + item + "')*(SELECT CURRENCYUSEQT  FROM "
					+ plant + "_CURRENCYMST WHERE  CURRENCYID='"+currency+"')) AS DECIMAL(20,5))   END) END  AS AVERAGE_COST"; 

			
			this.mLogger.query(this.printQuery, query);
			Map m = getRowOfData(conn, query);

			return (String) m.get("AVERAGE_COST");
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			return null;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

	}
	
	
	
	public ArrayList IncomingoutgoingqtyReport(String plant, String item, String dept, String brand, String cat,
			String sybcat, String extraCon, String fromdate, String todate, String tdate) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			String currency=new PlantMstDAO().getBaseCurrency(plant);
			con = com.track.gates.DbBean.getConnection();

			if (item.length() > 0) {
				extraCon = extraCon + " AND IT.ITEM  = '" + item + "'  ";
			}

			if (dept.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_DEPT_ID  = '" + item + "'  ";
			}

			if (brand.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_BRAND_ID  = '" + brand + "'  ";
			}

			if (cat.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_CLS_ID  = '" + cat + "'  ";
			}

			if (sybcat.length() > 0) {
				extraCon = extraCon + " AND IT.ITEMTYPE  = '" + sybcat + "'  ";
			}

			boolean flag = false;
			String sQry = "SELECT OP.PLANT AS PLANT, OP.ITEM AS ITEM, OP.ITEMDESC AS ITEMDESC, OP.PRD_CLS_ID AS PRDCLSID, OP.ITEMTYPE AS ITEMTYPE, OP.PRD_BRAND_ID AS PRD_BRAND_ID, OP.PRD_DEPT_ID AS PRD_DEPT_ID, OP.rqty AS TOTALRECEIVEDQTY, OP.iqty AS TOTALISSUEDQTY, OP.STOCKONHAND AS STOCKONHAND FROM (SELECT IT.PLANT, IT.ITEM, IT.ITEMDESC, IT.ITEMTYPE, IT.PRD_CLS_ID, IT.PRD_BRAND_ID, IT.PRD_DEPT_ID, IT.INCPRICEUNIT, IT.INCPRICE, "
					+ "((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RVT.RECVDATE, 104) BETWEEN CONVERT(DATETIME,'"
					+ fromdate + "', 104) AND CONVERT(DATETIME,'" + todate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS rqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) BETWEEN CONVERT(DATETIME,'"
					+ fromdate + "', 104) AND CONVERT(DATETIME, '" + todate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS iqty, (ISNULL((SELECT ISNULL(SUM(QTY),0) FROM "
					+ plant + "_INVMST AS IM WHERE IM.ITEM = IT.ITEM),0)/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS STOCKONHAND FROM " + plant
					+ "_ITEMMST AS IT WHERE IT.NONSTKFLAG <> 'Y' " + extraCon + ") as op";
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

	public ArrayList IncomingoutgoingqtyDetailReport(String plant, String item, String fromdate, String todate) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			String sCondition = "";
			String fdate="",tdate="";
			if (fromdate.length()>5)
		        fdate    = fromdate.substring(6)+ fromdate.substring(3,5)+fromdate.substring(0,2);

		       if(todate==null) todate=""; else todate = todate.trim();
		       if (todate.length()>5)
		       tdate    = todate.substring(6)+ todate.substring(3,5)+todate.substring(0,2);
			if (fromdate.length() > 0) {

				sCondition = sCondition + " AND  SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) >= '" 
						+ fdate + "'  ";
				if (todate.length() > 0) {
					sCondition = sCondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" 
							+ tdate + "'  ";
				}
			} else {
				if (todate.length() > 0) {
					sCondition = sCondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" 
							+ todate + "'  ";
				}
			}
			boolean flag = false;
			  
			String sQry = "SELECT * FROM ("  
			  +"SELECT 'RECEIVE' TYPE,ISNULL(TRAN_TYPE,'BILL') TRAN_TYPE,CASE WHEN TRAN_TYPE IS NULL THEN ISNULL((SELECT BILL FROM " + plant + "_FINBILLHDR B WHERE B.GRNO=RVT.GRNO),'') ELSE RVT.PONO END AS ORDER_NO,isnull(CNAME,'') CNAME,ITEM,ISNULL(ITEMDESC, isnull((select TOP 1 itemdesc from " + plant + "_ITEMMST C where RVT.item=C.item ),'') ) ITEMDESC,LOC,BATCH,RECQTY QTY,RECVDATE TRANDATE,isnull(REMARK,'') REMARK,isnull(CRBY,'') as users, RVT.RECVDATE as issuedate FROM " + plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RVT.RECVDATE, 104) BETWEEN CONVERT(DATETIME,'" + fromdate+ "', 104) AND CONVERT(DATETIME,'" + todate+ "', 104) AND RVT.ITEM ='" + item + "' UNION "
			  +"SELECT 'ISSUE' TYPE,'Sales' TRAN_TYPE,a.dono ORDER_NO,CNAME,a.item AS ITEM,isnull(a.itemdesc,'') ITEMDESC,isnull(upper(a.loc),0) LOC,isnull(a.batch,'') BATCH,case a.status when 'C' then isnull(a.pickqty,0) else '0' end as QTY ,ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) TRANDATE,isnull(a.remark,'') REMARK,isnull(a.CRBY,'') as users,a.issuedate  from [" + plant + "_shiphis] a, [" + plant + "_dodet] b, [" + plant + "_dohdr] d  where a.dono<>''  and a.dono=b.dono AND a.dolno=b.dolnno AND d.DONO = b.DONO AND a.ITEM=b.ITEM AND ISNULL(A.TRAN_TYPE,'') NOT IN ('GOODSISSUEWITHBATCH','GOODSISSUE','TAXINVOICE','KITTING','DE-KITTING') "+sCondition+" AND a.ITEM ='" + item + "'  UNION "
			  +"SELECT 'ISSUE' TYPE,a.TRAN_TYPE,a.dono ORDER_NO,CNAME,a.item AS ITEM,isnull(a.itemdesc,'') ITEMDESC,isnull(upper(a.loc),0) LOC,isnull(a.batch,'') BATCH,isnull(a.pickqty,0) as QTY, ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) TRANDATE,isnull(a.remark,'') REMARK,isnull(a.CRBY,'') as users,a.issuedate  from [" + plant + "_shiphis] a where (A.TRAN_TYPE='GOODSISSUEWITHBATCH' OR a.TRAN_TYPE='GOODSISSUE' OR a.TRAN_TYPE='TAXINVOICE' OR a.TRAN_TYPE='KITTING' OR a.TRAN_TYPE='DE-KITTING')  "+sCondition+"  AND a.ITEM ='" + item + "'   UNION " 
			  +"SELECT 'ISSUE' TYPE,'Invoice' TRAN_TYPE,B.INVOICE AS ORDER_NO,(SELECT CNAME FROM [" + plant + "_CUSTMST] WHERE CUSTNO=B.CUSTNO) AS CNAME,C.ITEM,isnull((select itemdesc from " + plant + "_ITEMMST where item=C.item ),'') as ITEMDESC, isnull(upper(a.loc),0) LOC,isnull(a.batch,'') batch,case a.status when 'C' then isnull(a.pickqty,0) else '0' end as QTY ,ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) TRANDATE,isnull(a.remark,'')remark, isnull(a.CRBY,'') as users,a.issuedate  FROM [" + plant + "_SHIPHIS] A JOIN [" + plant + "_FININVOICEHDR] B ON A.INVOICENO = B.GINO JOIN [" + plant + "_FININVOICEDET] C ON B.ID = C.INVOICEHDRID AND A.ITEM=C.ITEM AND A.DOLNO=C.LNNO WHERE B.DONO=''  "+sCondition+"   AND a.ITEM ='" + item + "' " 
			  +") a order by a.TYPE desc, CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date) desc";
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

	public ArrayList OpenAnclosingReport(String plant, String item, String dept, String brand, String cat,
			String sybcat, String extraCon, String fromdate, String todate, String tdate) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			String currency=new PlantMstDAO().getBaseCurrency(plant);
			con = com.track.gates.DbBean.getConnection();
			
			if (item.length() > 0) {
				extraCon = extraCon + " AND IT.ITEM  = '" + item + "'  ";
			}
			
			if (dept.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_DEPT_ID  = '" + item + "'  ";
			}
			
			if (brand.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_BRAND_ID  = '" + brand + "'  ";
			}
			
			if (cat.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_CLS_ID  = '" + cat + "'  ";
			}
			
			if (sybcat.length() > 0) {
				extraCon = extraCon + " AND IT.ITEMTYPE  = '" + sybcat + "'  ";
			}
			
			boolean flag = false;
			String sQry = "SELECT OP.PLANT AS PLANT, OP.ITEM AS ITEM, OP.ITEMDESC AS ITEMDESC, OP.PRD_CLS_ID AS PRDCLSID, OP.ITEMTYPE AS ITEMTYPE, OP.PRD_BRAND_ID AS PRD_BRAND_ID, OP.PRD_DEPT_ID AS PRD_DEPT_ID, ISNULL(OP.rqty,0) AS TOTALRECEIVEDQTY, ISNULL(OP.iqty,0) AS TOTALISSUEDQTY, (ISNULL(OP.orqty,0) - ISNULL(OP.oiqty,0)) AS OPENINGSTOCKQTY, (ISNULL(OP.crqty,0) - ISNULL(OP.ciqty,0)) AS CLOSINGSTOCKQTY,  ISNULL(OP.lastrqty,0) AS LASTRECEIVEDQTY, ISNULL(OP.lastiqty,0) AS LASTISSUEDQTY, ISNULL(OP.STOCKONHAND,0) AS STOCKONHAND FROM (SELECT IT.PLANT, IT.ITEM, IT.ITEMDESC, IT.ITEMTYPE, IT.PRD_CLS_ID, IT.PRD_BRAND_ID, IT.PRD_DEPT_ID, IT.INCPRICEUNIT, IT.INCPRICE, "
					+ "((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RVT.RECVDATE, 104) BETWEEN CONVERT(DATETIME,'"
					+ fromdate + "', 104) AND CONVERT(DATETIME,'" + todate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS rqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) BETWEEN CONVERT(DATETIME,'"
					+ fromdate + "', 104) AND CONVERT(DATETIME, '" + todate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS iqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RVT.RECVDATE, 104) > CONVERT(DATETIME,'" + todate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS lastrqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RECVDATE, 104) < CONVERT(DATETIME,'" + fromdate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS orqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) > CONVERT(DATETIME,'" + todate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS lastiqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) < CONVERT(DATETIME,'"
					+ fromdate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS oiqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RECVDATE, 104) < CONVERT(DATETIME,'" + tdate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS crqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) < CONVERT(DATETIME,'" + tdate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS ciqty, (ISNULL((SELECT ISNULL(SUM(QTY),0) FROM "
					+ plant + "_INVMST AS IM WHERE IM.ITEM = IT.ITEM),0)/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS STOCKONHAND FROM " + plant
					+ "_ITEMMST AS IT WHERE IT.NONSTKFLAG <> 'Y' " + extraCon + ") as op";
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
	
	public ArrayList OpenAnclosingAvgCostReport(String plant, String item, String dept, String brand, String cat,
			String sybcat, String extraCon, String fromdate, String todate, String tdate) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			String currency=new PlantMstDAO().getBaseCurrency(plant);
			con = com.track.gates.DbBean.getConnection();
			
			if (item.length() > 0) {
				extraCon = extraCon + " AND IT.ITEM  = '" + item + "'  ";
			}
			
			if (dept.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_DEPT_ID  = '" + item + "'  ";
			}
			
			if (brand.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_BRAND_ID  = '" + brand + "'  ";
			}
			
			if (cat.length() > 0) {
				extraCon = extraCon + " AND IT.PRD_CLS_ID  = '" + cat + "'  ";
			}
			
			if (sybcat.length() > 0) {
				extraCon = extraCon + " AND IT.ITEMTYPE  = '" + sybcat + "'  ";
			}
			
			boolean flag = false;
			String sQry = "SELECT OP.PLANT AS PLANT, OP.ITEM AS ITEM, OP.ITEMDESC AS ITEMDESC, OP.PRD_CLS_ID AS PRDCLSID, OP.ITEMTYPE AS ITEMTYPE, OP.PRD_BRAND_ID AS PRD_BRAND_ID, OP.PRD_DEPT_ID AS PRD_DEPT_ID, OP.rqty AS TOTALRECEIVEDQTY, OP.iqty AS TOTALISSUEDQTY, (OP.orqty - OP.oiqty) AS OPENINGSTOCKQTY, (OP.crqty - OP.ciqty) AS CLOSINGSTOCKQTY, OP.AVERAGECOST AS AVERAGECOST, (OP.AVERAGECOST * OP.rqty) AS TOTALCOST, (CASE WHEN OP.INCPRICEUNIT = '%' THEN (OP.AVERAGECOST + ((OP.AVERAGECOST/100)*OP.INCPRICE)) ELSE (OP.AVERAGECOST+ OP.INCPRICE) END) AS PRICE, ((CASE WHEN OP.INCPRICEUNIT = '%' THEN (OP.AVERAGECOST + ((OP.AVERAGECOST/100)*OP.INCPRICE)) ELSE (OP.AVERAGECOST+ OP.INCPRICE) END)*(OP.iqty)) AS TOTALPRICE, OP.lastrqty AS LASTRECEIVEDQTY, OP.lastiqty AS LASTISSUEDQTY, OP.STOCKONHAND AS STOCKONHAND FROM (SELECT IT.PLANT, IT.ITEM, IT.ITEMDESC, IT.ITEMTYPE, IT.PRD_CLS_ID, IT.PRD_BRAND_ID, IT.PRD_DEPT_ID, IT.INCPRICEUNIT, IT.INCPRICE, (SELECT AVG.AVERAGE_COST AS AVGE FROM ( SELECT ( ( ( SELECT CASE WHEN ( SELECT COUNT(CURRENCYID) FROM "
					+ plant
					+ "_RECVDET R WHERE ITEM = IT.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN( 'IB', 'GOODSRECEIPTWITHBATCH', 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) )> 0 THEN ( Select ISNULL( CAST( ISNULL( SUM( CASE WHEN A.TRAN_TYPE = 'GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END ), 0 )/ SUM(VC) AS DECIMAL(20, 5) ), 0 ) AS AVERGAGE_COST from ( select TRAN_TYPE, RECQTY VC, CASE WHEN TRAN_TYPE IN ( 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) THEN ( isnull( R.unitcost *( SELECT CURRENCYUSEQT FROM "
					+ plant
					+ "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, '"+currency+"') ), 0 )* R.RECQTY ) ELSE CAST( ( CAST( ISNULL( ISNULL( ( select ISNULL(QPUOM, 1) from "
					+ plant
					+ "_UOM where UOM = '' ), 1 ) * ( ISNULL( ( ( isnull( R.unitcost *( SELECT CURRENCYUSEQT FROM "
					+ plant
					+ "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, '"+currency+"') ), 0 )+ ISNULL( ( SELECT ( SUM(E.QTY * LANDED_COST)/ SUM(E.QTY) ) FROM "
					+ plant + "_RECVDET c left join " + plant
					+ "_FINBILLHDR d on c.PONO = d.PONO and c.GRNO = d.GRNO left join " + plant
					+ "_FINBILLDET e on d.ID = e.BILLHDRID where c.pono = R.pono and c.LNNO = R.LNNO and e.ITEM = IT.ITEM OR c.TRAN_TYPE = 'GOODSRECEIPTWITHBATCH' AND c.item = IT.ITEM ), 0 ) + ( ISNULL( R.unitcost *( SELECT CURRENCYUSEQT FROM "
					+ plant
					+ "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, '"+currency+"') ), 0 ) * ( ( ( ISNULL(P.LOCALEXPENSES, 0)+ CASE WHEN ( SELECT SUM(LANDED_COST) FROM "
					+ plant + "_FINBILLHDR c join " + plant
					+ "_FINBILLDET d ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO ) is null THEN P.SHIPPINGCOST ELSE 0 END )* 100 )/ NULLIF( ( ISNULL( ( select SUM( s.qtyor * s.UNITCOST * s.CURRENCYUSEQT ) from "
					+ plant + "_podet s where s.pono = R.pono ), 0 ) ), 0 ) )/ 100 ) )/ ( SELECT CURRENCYUSEQT FROM "
					+ plant
					+ "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, '"+currency+"') ) ), 0 ) ), 0 ) *( SELECT CURRENCYUSEQT FROM "
					+ plant + "_CURRENCYMST WHERE CURRENCYID = '"+currency+"' )*( SELECT CURRENCYUSEQT FROM " + plant
					+ "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, '"+currency+"') ) AS DECIMAL(20, 5) ) ) * CAST( ( SELECT CURRENCYUSEQT FROM "
					+ plant
					+ "_CURRENCYMST WHERE CURRENCYID = '"+currency+"' ) AS DECIMAL(20, 5) ) / CAST( ( SELECT CURRENCYUSEQT FROM "
					+ plant
					+ "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, '"+currency+"') ) AS DECIMAL(20, 5) ) * RECQTY AS DECIMAL(20, 5) ) END AS UNITCOST from "
					+ plant + "_RECVDET R LEFT JOIN " + plant
					+ "_POHDR P ON R.PONO = P.PONO where item = IT.ITEM AND ISNULL(R.UNITCOST, 0) != 0 AND tran_type IN( 'IB', 'GOODSRECEIPTWITHBATCH', 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) ) A ) ELSE ( SELECT CASE WHEN ( SELECT COUNT(*) FROM "
					+ plant
					+ "_RECVDET WHERE ITEM = IT.ITEM AND tran_type IN( 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) )> 0 THEN ( SELECT ISNULL( SUM(UNITCOST), 0 ) FROM "
					+ plant
					+ "_RECVDET C where item = IT.ITEM AND ISNULL(C.UNITCOST, 0) != 0 AND tran_type IN( 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) ) ELSE CAST( ( ( SELECT M.COST FROM "
					+ plant + "_ITEMMST M WHERE M.ITEM = IT.ITEM )*( SELECT CURRENCYUSEQT FROM " + plant
					+ "_CURRENCYMST WHERE CURRENCYID = '"+currency+"' ) ) AS DECIMAL(20, 5) ) END ) END AS AVERAGE_COST )/ ( SELECT ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_ITEMMST AS I LEFT JOIN " + plant
					+ "_UOM AS U ON I.PURCHASEUOM = U.UOM WHERE I.ITEM = IT.ITEM ) ) * ( SELECT ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_ITEMMST AS I LEFT JOIN " + plant
					+ "_UOM AS U ON I.INVENTORYUOM = U.UOM WHERE I.ITEM = IT.ITEM ) ) AS AVERAGE_COST ) AS AVG) AVERAGECOST, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RVT.RECVDATE, 104) BETWEEN CONVERT(DATETIME,'"
					+ fromdate + "', 104) AND CONVERT(DATETIME,'" + todate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS rqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) BETWEEN CONVERT(DATETIME,'"
					+ fromdate + "', 104) AND CONVERT(DATETIME, '" + todate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS iqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RVT.RECVDATE, 104) > CONVERT(DATETIME,'" + todate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS lastrqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RECVDATE, 104) < CONVERT(DATETIME,'" + fromdate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS orqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) > CONVERT(DATETIME,'" + todate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS lastiqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) < CONVERT(DATETIME,'"
					+ fromdate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS oiqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					+ plant + "_RECVDET AS RVT WHERE CONVERT(DATETIME, RECVDATE, 104) < CONVERT(DATETIME,'" + tdate
					+ "', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS crqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					+ plant + "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) < CONVERT(DATETIME,'" + tdate
					+ "', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM " + plant
					+ "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS ciqty, (ISNULL((SELECT ISNULL(SUM(QTY),0) FROM "
					+ plant + "_INVMST AS IM WHERE IM.ITEM = IT.ITEM),0)/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					+ plant + "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS STOCKONHAND FROM " + plant
					+ "_ITEMMST AS IT WHERE IT.NONSTKFLAG <> 'Y' " + extraCon + ") as op";
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
	 
	 
	
	
	public List<OpenCloseReportPojo> OpenAnclosingReportbak(String plant,String item,String dept,String brand,String cat,String sybcat,String extraCon,String fromdate,String todate,String tdate)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<OpenCloseReportPojo> openClosePojoList=new ArrayList<OpenCloseReportPojo>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query ="SELECT OP.PLANT AS PLANT, OP.ITEM AS ITEM, OP.ITEMDESC AS ITEMDESC, OP.PRD_CLS_ID AS PRDCLSID, OP.ITEMTYPE AS ITEMTYPE, OP.PRD_BRAND_ID AS PRD_BRAND_ID, OP.PRD_DEPT_ID AS PRD_DEPT_ID, OP.rqty AS TOTALRECEIVEDQTY, OP.iqty AS TOTALISSUEDQTY, (OP.orqty - OP.oiqty) AS OPENINGSTOCKQTY, (OP.crqty - OP.ciqty) AS CLOSINGSTOCKQTY, OP.AVERAGECOST AS AVERAGECOST, (OP.AVERAGECOST * OP.rqty) AS TOTALCOST, (CASE WHEN OP.INCPRICEUNIT = '%' THEN (OP.AVERAGECOST + ((OP.AVERAGECOST/100)*OP.INCPRICE)) ELSE (OP.AVERAGECOST+ OP.INCPRICE) END) AS PRICE, ((CASE WHEN OP.INCPRICEUNIT = '%' THEN (OP.AVERAGECOST + ((OP.AVERAGECOST/100)*OP.INCPRICE)) ELSE (OP.AVERAGECOST+ OP.INCPRICE) END)*(OP.iqty)) AS TOTALPRICE, OP.lastrqty AS LASTRECEIVEDQTY, OP.lastiqty AS LASTISSUEDQTY, OP.STOCKONHAND AS STOCKONHAND FROM (SELECT IT.PLANT, IT.ITEM, IT.ITEMDESC, IT.ITEMTYPE, IT.PRD_CLS_ID, IT.PRD_BRAND_ID, IT.PRD_DEPT_ID, IT.INCPRICEUNIT, IT.INCPRICE, (SELECT AVG.AVERAGE_COST AS AVGE FROM ( SELECT ( ( ( SELECT CASE WHEN ( SELECT COUNT(CURRENCYID) FROM "
					  + plant +
					  "_RECVDET R WHERE ITEM = IT.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN( 'IB', 'GOODSRECEIPTWITHBATCH', 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) )> 0 THEN ( Select ISNULL( CAST( ISNULL( SUM( CASE WHEN A.TRAN_TYPE = 'GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END ), 0 )/ SUM(VC) AS DECIMAL(20, 5) ), 0 ) AS AVERGAGE_COST from ( select TRAN_TYPE, RECQTY VC, CASE WHEN TRAN_TYPE IN ( 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) THEN ( isnull( R.unitcost *( SELECT CURRENCYUSEQT FROM "
					  + plant +
					  "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, 'SGD') ), 0 )* R.RECQTY ) ELSE CAST( ( CAST( ISNULL( ISNULL( ( select ISNULL(QPUOM, 1) from "
					  + plant +
					  "_UOM where UOM = '' ), 1 ) * ( ISNULL( ( ( isnull( R.unitcost *( SELECT CURRENCYUSEQT FROM "
					  + plant +
					  "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, 'SGD') ), 0 )+ ISNULL( ( SELECT ( SUM(E.QTY * LANDED_COST)/ SUM(E.QTY) ) FROM "
					  + plant + "_RECVDET c left join " + plant +
					  "_FINBILLHDR d on c.PONO = d.PONO and c.GRNO = d.GRNO left join " + plant +
					  "_FINBILLDET e on d.ID = e.BILLHDRID where c.pono = R.pono and c.LNNO = R.LNNO and e.ITEM = IT.ITEM OR c.TRAN_TYPE = 'GOODSRECEIPTWITHBATCH' AND c.item = IT.ITEM ), 0 ) + ( ISNULL( R.unitcost *( SELECT CURRENCYUSEQT FROM "
					  + plant +
					  "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, 'SGD') ), 0 ) * ( ( ( ISNULL(P.LOCALEXPENSES, 0)+ CASE WHEN ( SELECT SUM(LANDED_COST) FROM "
					  + plant + "_FINBILLHDR c join " + plant +
					  "_FINBILLDET d ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO ) is null THEN P.SHIPPINGCOST ELSE 0 END )* 100 )/ NULLIF( ( ISNULL( ( select SUM( s.qtyor * s.UNITCOST * s.CURRENCYUSEQT ) from "
					  + plant +
					  "_podet s where s.pono = R.pono ), 0 ) ), 0 ) )/ 100 ) )/ ( SELECT CURRENCYUSEQT FROM "
					  + plant +
					  "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, 'SGD') ) ), 0 ) ), 0 ) *( SELECT CURRENCYUSEQT FROM "
					  + plant +
					  "_CURRENCYMST WHERE CURRENCYID = 'SGD' )*( SELECT CURRENCYUSEQT FROM " +
					  plant +
					  "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, 'SGD') ) AS DECIMAL(20, 5) ) ) * CAST( ( SELECT CURRENCYUSEQT FROM "
					  + plant +
					  "_CURRENCYMST WHERE CURRENCYID = 'SGD' ) AS DECIMAL(20, 5) ) / CAST( ( SELECT CURRENCYUSEQT FROM "
					  + plant +
					  "_CURRENCYMST WHERE CURRENCYID = ISNULL(P.CURRENCYID, 'SGD') ) AS DECIMAL(20, 5) ) * RECQTY AS DECIMAL(20, 5) ) END AS UNITCOST from "
					  + plant + "_RECVDET R LEFT JOIN " + plant +
					  "_POHDR P ON R.PONO = P.PONO where item = IT.ITEM AND ISNULL(R.UNITCOST, 0) != 0 AND tran_type IN( 'IB', 'GOODSRECEIPTWITHBATCH', 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) ) A ) ELSE ( SELECT CASE WHEN ( SELECT COUNT(*) FROM "
					  + plant +
					  "_RECVDET WHERE ITEM = IT.ITEM AND tran_type IN( 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) )> 0 THEN ( SELECT ISNULL( SUM(UNITCOST), 0 ) FROM "
					  + plant +
					  "_RECVDET C where item = IT.ITEM AND ISNULL(C.UNITCOST, 0) != 0 AND tran_type IN( 'INVENTORYUPLOAD', 'DE-KITTING', 'KITTING' ) ) ELSE CAST( ( ( SELECT M.COST FROM "
					  + plant + "_ITEMMST M WHERE M.ITEM = IT.ITEM )*( SELECT CURRENCYUSEQT FROM "
					  + plant +
					  "_CURRENCYMST WHERE CURRENCYID = 'SGD' ) ) AS DECIMAL(20, 5) ) END ) END AS AVERAGE_COST )/ ( SELECT ISNULL(U.QPUOM, '1') FROM "
					  + plant + "_ITEMMST AS I LEFT JOIN " + plant +
					  "_UOM AS U ON I.PURCHASEUOM = U.UOM WHERE I.ITEM = IT.ITEM ) ) * ( SELECT ISNULL(U.QPUOM, '1') FROM "
					  + plant + "_ITEMMST AS I LEFT JOIN " + plant +
					  "_UOM AS U ON I.INVENTORYUOM = U.UOM WHERE I.ITEM = IT.ITEM ) ) AS AVERAGE_COST ) AS AVG) AVERAGECOST, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					  + plant +
					  "_RECVDET AS RVT WHERE CONVERT(DATETIME, RVT.RECVDATE, 104) BETWEEN CONVERT(DATETIME,'"
					  +fromdate+"', 104) AND CONVERT(DATETIME,'"
					  +todate+"', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS rqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					  + plant +
					  "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) BETWEEN CONVERT(DATETIME,'"
					  +fromdate+"', 104) AND CONVERT(DATETIME, '"
					  +todate+"', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS iqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					  + plant +
					  "_RECVDET AS RVT WHERE CONVERT(DATETIME, RVT.RECVDATE, 104) > CONVERT(DATETIME,'"
					  +todate+"', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS lastrqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					  + plant +
					  "_RECVDET AS RVT WHERE CONVERT(DATETIME, RECVDATE, 104) < CONVERT(DATETIME,'"
					  +fromdate+"', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS orqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					  + plant +
					  "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) > CONVERT(DATETIME,'"
					  +todate+"', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS lastiqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					  + plant +
					  "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) < CONVERT(DATETIME,'"
					  +fromdate+"', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS oiqty, ((ISNULL((SELECT SUM(RVT.RECQTY) FROM "
					  + plant +
					  "_RECVDET AS RVT WHERE CONVERT(DATETIME, RECVDATE, 104) < CONVERT(DATETIME,'"
					  +tdate+"', 104) AND RVT.ITEM = IT.ITEM GROUP BY RVT.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.PURCHASEUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS crqty, ((ISNULL((SELECT SUM(SH.PICKQTY) FROM "
					  + plant +
					  "_SHIPHIS AS SH WHERE CONVERT(DATETIME, SH.ISSUEDATE, 104) < CONVERT(DATETIME,'"
					  +tdate+"', 104) AND SH.ITEM = IT.ITEM GROUP BY SH.ITEM),0)*(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.SALESUOM))/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant +
					  "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS ciqty, (ISNULL((SELECT ISNULL(SUM(QTY),0) FROM "
					  + plant +
					  "_INVMST AS IM WHERE IM.ITEM = IT.ITEM),0)/(SELECT TOP 1 ISNULL(U.QPUOM, '1') FROM "
					  + plant + "_UOM AS U WHERE U.UOM = IT.INVENTORYUOM)) AS STOCKONHAND FROM " +
					  plant + "_ITEMMST AS IT WHERE IT.NONSTKFLAG <> 'Y' "+extraCon+") as op";
					  this.mLogger.query(this.printQuery, query);

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   OpenCloseReportPojo openclosePojo=new OpenCloseReportPojo();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, openclosePojo);
	                   openClosePojoList.add(openclosePojo);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			DbBean.closeConnection(connection);			
			//throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return openClosePojoList;
	}
	
}