package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class ProductionBomDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.ProductionBomDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ProductionBomDAO_PRINTPLANTMASTERLOG;

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

	public ProductionBomDAO() {

	}

	public boolean insertIntoProdBomMst(Hashtable ht) throws Exception {
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
					+ "PROD_BOM_MST" + "]" + "("
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

	public boolean isExists(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PROD_BOM_MST"
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

	public boolean updateProdBomMst(Hashtable htUpdate, Hashtable htCondition)
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
					+ "PROD_BOM_MST" + "]" + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}
	
	public boolean updatePrdBOM(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		String TABLE = "PROD_BOM_MST";
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + TABLE + "]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");

			String conditon = formCondition(htCondition);

			sql.append(conditon);

			if (extCond.length() != 0) {
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());

			flag = updateData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;
	}

	public boolean deleteProdBomMst(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deletePrdId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PROD_BOM_MST"
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

	public ArrayList getProdBomDetails(String pitem, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct PITEM,CITEM,QTY,ISNULL(SEQNUM,'')SEQNUM,ISNULL(REMARK1,'')REMARK1,ISNULL(REMARK2,'')REMARK2,"
					//+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CUOM,"
					+"isnull(CHILDUOM,'') CUOM,isnull(PARENTUOM,'') PUOM, "
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.PITEM ) PDESC,"
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CDESC,CRAT from"
					+ "["
					+ plant
					+ "_"
					+ "PROD_BOM_MST]PB where PLANT<>'' AND PITEM='"
					+ pitem
					+"'   " + extraCon
					+ " order by CRAT ";
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
	
	public ArrayList getCopyProdBomDetails(String pitem, String plant,
			String extraCon) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
			boolean flag = false;
			String sQry = "select distinct PITEM,CITEM,QTY,ISNULL(SEQNUM,'')SEQNUM,ISNULL(REMARK1,'')REMARK1,ISNULL(REMARK2,'')REMARK2,"
					+"isnull(CHILDUOM,'') CUOM,isnull(PARENTUOM,'') PUOM, "
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.PITEM ) PDESC,"
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CDESC,CRAT from"
					+ "["
					+ plant
					+ "_"
					+ "PROD_BOM_MST]PB where PLANT<>'' AND PITEM='"
					+ pitem
					+"'   " + extraCon
					+ " order by CRAT ";
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
	
	public ArrayList getProcessingProdBomDetails(String pitem, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			if(extraCon.equals("SEMIKITBOM"))
				extraCon=" AND BOMTYPE='KIT' AND PITEM IN (SELECT ITEM from ["+ plant + "_ITEMMST] WHERE ISCOMPRO=2 )";
			else
				extraCon = extraCon + " AND PITEM IN (SELECT ITEM from ["+ plant + "_INVMST] WHERE LOC LIKE '%PROCESSING%') ";
			boolean flag = false;
			String sQry = "select distinct PITEM,CITEM,QTY,ISNULL(SEQNUM,'')SEQNUM,ISNULL(REMARK1,'')REMARK1,ISNULL(REMARK2,'')REMARK2,"
					//+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CUOM,"
					+"isnull(CHILDUOM,'') CUOM,isnull(PARENTUOM,'') PUOM, "
					+"(select QPUOM from ["+plant+"_UOM] where UOM = PB.CHILDUOM ) CQPUOM,"
					+"(select QPUOM from ["+plant+"_UOM] where UOM = PB.PARENTUOM ) PQPUOM,"
					+"ISNULL((select SUM(QTY) from ["+plant+"_INVMST] I where I.ITEM = PB.CITEM AND I.USERFLD4='NOBATCH' AND I.LOC=(SELECT TOP 1 PRD_DEPT_ID from ["+plant+"_ITEMMST] T WHERE T.ITEM = PB.CITEM ) ),0) INVQTY,"
					+"ISNULL((select SUM(QTY) from ["+plant+"_INVMST] I where I.ITEM = PB.CITEM AND I.USERFLD4='NOBATCH' AND I.LOC='Kitchen' ),0) KitchenQTY,"
					+"(select PURCHASEUOM from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CPURCHASEUOM,"
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CDESC,CRAT from"
					+ "["
					+ plant
					+ "_"
					+ "PROD_BOM_MST]PB where PLANT<>'' AND PITEM='"
					+ pitem
					+"' " + extraCon
					+ " order by CRAT ";
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
	
	public ArrayList getProdBomDetailsforSummary(Hashtable ht,String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		MovHisDAO movHisDAO = new MovHisDAO();
		movHisDAO.setmLogger(mLogger);
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct PITEM,CITEM,QTY,ISNULL(SEQNUM,'')SEQNUM,ISNULL(REMARK1,'')REMARK1,ISNULL(REMARK2,'')REMARK2,"
					+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) PUOM,"
					+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CUOM,"
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) PDESC,"
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CDESC from"
					+ "["
					+ plant
					+ "_"
					+ "PROD_BOM_MST]PB where PLANT<>''"
					+ extraCon;
					//+ " ORDER BY LOC_TYPE_ID ";
			this.mLogger.query(this.printQuery, sQry);
			
			al = movHisDAO.selectForReport(sQry, ht);
			
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
	
	public ArrayList getProcessingProdBompitemlist(String pitem, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			String Loc ="PROCESSING";
if(extraCon.equals("SEMIKITBOM")) {
	extraCon=" AND BOMTYPE='KIT' AND PITEM IN (SELECT ITEM from ["+ plant + "_ITEMMST] WHERE ISCOMPRO=2 )";
	Loc ="FREEZER2";
}
else
	extraCon = extraCon + " AND PITEM IN (SELECT ITEM from ["+ plant + "_INVMST] WHERE LOC LIKE '%PROCESSING%') ";
			boolean flag = false;
			String sQry = "select distinct PITEM,"
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) PDESC, "
					+"(select isnull(Remark1,'') from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) PDETDESC, "
					+"(select isnull(PRD_DEPT_ID,'') from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) PRD_DEPT_ID, "
					+"(select isnull(COST,0) from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) COST, "
					+"(select isnull(CPPI,'') from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) CPPI, "
					+"(select isnull(INCPRICE,0) from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) INCPRICE, "
					+"ISNULL((select SUM(ISNULL(INVM.QTY,0)) from ["+ plant + "_INVMST] INVM where PB.pitem = INVM.ITEM AND INVM.LOC LIKE '%"+Loc+"%'),0) AS INVQTY,"
					+"(select QPUOM from ["+plant+"_UOM] where UOM = PB.PARENTUOM ) PQPUOM,"
					+"isnull(PARENTUOM,'') UOM from"
					+ "["
					+ plant
					+ "_"
					+ "PROD_BOM_MST]PB where PLANT<>'' AND PITEM like'%"
					+ pitem
					+"%' " + extraCon;
					//+ " ORDER BY LOC_TYPE_ID ";
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
	
	public ArrayList getProdBompitemlist(String pitem, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct PITEM,"
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) PDESC, "
					+"(select isnull(Remark1,'') from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) PDETDESC, "
					+"ISNULL((select SUM(ISNULL(INVM.QTY,0)) from ["+ plant + "_INVMST] INVM where PB.pitem = INVM.ITEM),0) AS INVQTY,"
					//+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = PB.pitem ) UOM from"
					+"isnull(PARENTUOM,'') UOM from"
					+ "["
					+ plant
					+ "_"
					+ "PROD_BOM_MST]PB where PLANT<>'' AND PITEM like'%"
					+ pitem
					+"%'  " + extraCon;
					//+ " ORDER BY LOC_TYPE_ID ";
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
	
	public ArrayList getProdBomchilditemlist(String pitem,String citem, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct CITEM,"
					+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CDESC, "
					+"(select isnull(Remark1,'') from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CDETDESC, "
					+"(select isnull(NONSTKFLAG,'N') from ["+plant+"_ITEMMST] where ITEM = PB.citem ) NONSTKFLAG, "
					+"ISNULL((select SUM(ISNULL(INVM.QTY,0)) from ["+ plant + "_INVMST] INVM where PB.citem = INVM.ITEM),0) AS INVQTY,"
					//+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = PB.citem ) UOM from"
					+"isnull(CHILDUOM,'') UOM from"
					+ "["
					+ plant
					+ "_"
					+ "PROD_BOM_MST]PB where PLANT<>'' AND PITEM like'%"
					+ pitem
					+"%' AND CITEM like'%"+citem+"%'"  + extraCon;
					//+ " ORDER BY LOC_TYPE_ID ";
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
	
	
	public ArrayList getChildItemsFromBOM(String Plant,String pitem,String reportQty,String opseq) 
			throws Exception{
	      MLogger.log(1, this.getClass() + " getChildItemsFromWip()");
	      ArrayList arrList = new ArrayList();
	      java.sql.Connection con = null;
	      try {
	    	  con = com.track.gates.DbBean.getConnection();
	        Hashtable ht = new Hashtable();
	       
	        
	         //StringBuffer aQuery=new StringBuffer();
	    
	         String sQry =" select distinct PITEM,CITEM,"+reportQty+" *QTY as QTYREQ from "
	        		 + "["
	        		 + Plant
	        		 + "_"
	        		 +"PROD_BOM_MST] "
	        		 +"  where  PLANT='" +Plant
	        		 +"' AND PITEM='"
					 + pitem
					  +"' AND SEQNUM='"
					 + opseq+"'";
					 
					 
	         this.mLogger.query(this.printQuery, sQry);
	         arrList = selectData(con, sQry);
	     
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
		}  
	      return arrList;
	    }
	public boolean insertIntoEquivalentItem(Hashtable ht) throws Exception {
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
					+ "EQUIVALENT_ITEM_MAPPING" + "]" + "("
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

	public boolean isExistsEquivalentitem(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "EQUIVALENT_ITEM_MAPPING"
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
		
	public String getEquivalentitem(String Plant, String Item,String childItem) throws Exception {
		String equiitem = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", Plant);
		ht.put("ITEM", Item);
		ht.put("CHILDITEM", childItem);

		String query = " EQIITEM as equivalentitem ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			equiitem = (String) m.get("equivalentitem");
		} else {
			equiitem = "";
		}
		if (equiitem.equalsIgnoreCase(null) || equiitem.length() == 0) {
			equiitem = "";
		}
		return equiitem;
	}
	public String getchilditemfromequivalent(String Plant, String Item,String equiItem) throws Exception {
		String childitem = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", Plant);
		ht.put("ITEM", Item);
		ht.put("EQIITEM", equiItem);

		String query = " CHILDITEM as childitem ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			childitem = (String) m.get("childitem");
		} else {
			childitem = "";
		}
		if (childitem.equalsIgnoreCase(null) || childitem.length() == 0) {
			childitem = "";
		}
		return childitem;
	}
	public Map selectRow(String query, Hashtable ht, String extCondi)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + (String) ht.get("PLANT") + "_EQUIVALENT_ITEM_MAPPING]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCondi.length() > 0)
				sql.append(" and " + extCondi);
			this.mLogger.query(this.printQuery, sql.toString());
			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return map;
	}
	
public boolean deleteEquivalentitem(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deleteequivalent()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "EQUIVALENT_ITEM_MAPPING"
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
	
public ArrayList getProdBomSummaryDetails(String pitem, String plant,String pdesc,String pdetaildesc,String citem,String cdesc,String cdetaildesc,String eitem,String edesc,String edetaildesc
		,String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		
		if (pitem.length() > 0) {
			extraCon = extraCon + " AND PITEM  = '" + pitem
					+ "'  ";
		}
		
		if (pdesc.length() > 0) {
			extraCon = extraCon + " AND PITEM IN (select ITEM from  ["+plant+"_ITEMMST] where ITEMDESC = '" + pdesc + "' and ITEM=PB.PITEM)";
		}
		
		if (pdetaildesc.length() > 0) {
			extraCon = extraCon + " AND PITEM IN (select ITEM from  ["+plant+"_ITEMMST] where Remark1 = '" +pdetaildesc + "' and ITEM=PB.PITEM)";
		}
				
		if (citem.length() > 0) {
			extraCon = extraCon + " AND CITEM  = '" + citem
					+ "'  ";
		}
		
		if (cdesc.length() > 0) {
			extraCon = extraCon + " AND CITEM IN (select ITEM from  ["+plant+"_ITEMMST] where ITEMDESC = '" + cdesc + "' and ITEM=PB.CITEM)";
		}
		
		if (cdetaildesc.length() > 0) {
			extraCon = extraCon + " AND CITEM IN (select ITEM from  ["+plant+"_ITEMMST] where Remark1 = '" +cdetaildesc + "' and ITEM=PB.CITEM)";
		}
		
	
		boolean flag = false;
		String sQry = "select distinct PITEM,CITEM,QTY,ISNULL(SEQNUM,'')SEQNUM,ISNULL(REMARK1,'')REMARK1,ISNULL(REMARK2,'')REMARK2,"
				+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = PB.citem ) CUOM,CRAT from"
				+ "["
				+ plant
				+ "_"
				+ "PROD_BOM_MST]PB where PLANT <>'' "
				+" " + extraCon
				+ " order by PITEM,CITEM ";
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

public ArrayList getParentBomSummaryDetails(String pitem, String plant) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		
		boolean flag = false;
		String sQry = "select distinct PARENTUOM,PITEM,(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = PB.PITEM ) ITEMDESC,(select REMARK1 from ["+plant+"_ITEMMST] where ITEM = PB.PITEM ) ITEMDETAILDESC,(select STKUOM from ["+plant+"_ITEMMST] where ITEM = PB.PITEM ) CUOM from["+plant+"_PROD_BOM_MST]PB where PLANT <>'' AND BOMTYPE='KIT' AND PITEM like '%"+pitem+"%' group by PITEM,PARENTUOM";
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

public ArrayList getkittingDetails(String pitem,String loc,String batch, String plant,
		String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

		boolean flag = false;
		String sQry = "select distinct PARENT_PRODUCT,PARENT_PRODUCT_BATCH,CHILD_PRODUCT,PARENT_PRODUCT_LOC,CHILD_PRODUCT_LOC,CHILD_PRODUCT_BATCH,QTY,isnull(CHKQTY,0)CHKQTY,isnull(CHKSTATUS,'N')STATUS,isnull(CHKBATCH,'')CHKBATCH,isnull(SCANITEM,'')SCANITEM,"
				+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = B.CHILD_PRODUCT ) CDESC,PARENT_PRODUCT_QTY,ISNULL(PARENTUOM,'') PARENTUOM,"
				+"(select LOC_TYPE_ID2 from ["+plant+"_locmst] where loc = B.PARENT_PRODUCT_LOC ) LOC_TYPE_ID2,"
				+"(select FNAME from ["+plant+"_EMP_MST] where EMPNO = B.EMPNO ) EMP_NAME,EMPNO,LOC_TYPE_ID,RSNCODE,REMARKS,ISNULL(ORDDATE,'') ORDDATE,"
				+"(select isnull(Remark1,'') from ["+plant+"_ITEMMST] where ITEM = B.CHILD_PRODUCT ) CDETDESC,CREATE_AT from"
				+ "["
				+ plant
				+ "_"
				+ "BOM]B where PLANT<>'' AND PARENT_PRODUCT='"
				+ pitem
				+"' AND PARENT_PRODUCT_BATCH like '%"+batch+"%'" + extraCon
				+ " order by CREATE_AT ";
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
public String getkittingparentbatch(String Plant, String Item,String childItem) throws Exception {
	String pbatch = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("CHILD_PRODUCT", childItem);

	String query = " PARENT_PRODUCT_BATCH as PBATCH ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		pbatch = (String) m.get("PBATCH");
	} else {
		pbatch = "";
	}
	if (pbatch.equalsIgnoreCase(null) || pbatch.length() == 0) {
		pbatch = "";
	}
	return pbatch;
}
public String getkittingparentbatchwithkono(String Plant, String Item,String childItem,String kono) throws Exception {
	String pbatch = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("CHILD_PRODUCT", childItem);
	ht.put("KONO", kono);

	String query = " PARENT_PRODUCT_BATCH as PBATCH ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		pbatch = (String) m.get("PBATCH");
	} else {
		pbatch = "";
	}
	if (pbatch.equalsIgnoreCase(null) || pbatch.length() == 0) {
		pbatch = "";
	}
	return pbatch;
}
/*
public String getkittingchildbatch(String Plant, String Item,String childItem) throws Exception {
	String cbatch = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("CHILD_PRODUCT", childItem);

	String query = " CHILD_PRODUCT_BATCH as CBATCH ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		for (int iCnt =0; iCnt<m.size(); iCnt++){
			Map lineArr = (Map) m.get(iCnt);
			cbatch = (String) lineArr.get("CBATCH");
			if(cbatch.length()>0){
				cbatch = cbatch +","+(String) lineArr.get("CBATCH");
			}
		}
		
	} else {
		cbatch = "";
	}
	if (cbatch.equalsIgnoreCase(null) || cbatch.length() == 0) {
		cbatch = "";
	}
	return cbatch;
}*/
public ArrayList getkitchildbatch(String Plant, String Item,String pbatch,String childItem,
		String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

		boolean flag = false;
		String sQry = "SELECT ISNULL(STUFF((SELECT ','+ CHILD_PRODUCT_BATCH+'('+CONVERT(VARCHAR(20),SUM(QTY))+ ')' AS [text()]"
                +" HAVING SUM(QTY)>0 FOR XML PATH('')),1,1,' '),'') AS CBATCH from "
				+"["
				+ Plant
				+ "_"
				+ "BOM] where PLANT<>'' AND PLANT='"+Plant+"' AND PARENT_PRODUCT='"
				+ Item
				+"' AND PARENT_PRODUCT_BATCH='"
				+pbatch
				+ "' AND  CHILD_PRODUCT='"
				+childItem+"'" + extraCon
				+"   GROUP BY CHILD_PRODUCT_BATCH" ;
				
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

public String getkittingchildqty(String Plant, String Item,String pbatch,String childItem) throws Exception {
	String cqty = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", pbatch);
	ht.put("CHILD_PRODUCT", childItem);

	String query = " isnull(sum(QTY),0) as qty ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		cqty = (String) m.get("qty");
	} else {
		cqty = "0";
	}
	/*if (cqty.equalsIgnoreCase(null) || cqty.length() == 0) {
		cqty = "0";
	}*/
	return cqty;
}
public String getkittingchildloc(String Plant, String Item,String pbatch,String childItem) throws Exception {
	String cloc = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", pbatch);
	ht.put("CHILD_PRODUCT", childItem);

	String query = " CHILD_PRODUCT_LOC as loc ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		cloc = (String) m.get("loc");
	} else {
		cloc = "";
	}
	if (cloc.equalsIgnoreCase(null) || cloc.length() == 0) {
		cloc = "";
	}
	return cloc;
}
public String getkittingscanitem(String Plant, String Item,String pbatch,String childItem) throws Exception {
	String scanitem = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", pbatch);
	ht.put("CHILD_PRODUCT", childItem);

	String query = " top 1 isnull(SCANITEM,'') as scanitem ";

	Map m = selectkitRow(query, ht, " plant='"+Plant+"' order by CREATE_AT desc");
	if (m.size() > 0) {
		scanitem = (String) m.get("scanitem");
	} else {
		scanitem = "";
	}
	if (scanitem.equalsIgnoreCase(null) || scanitem.length() == 0) {
		scanitem = "";
	}
	return scanitem;
}
public String getkittingchildqtywithkono(String Plant, String Item,String pbatch,String childItem,String kono) throws Exception {
	String cqty = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", pbatch);
	ht.put("CHILD_PRODUCT", childItem);
	ht.put("KONO", kono);

	String query = " isnull(sum(QTY),0) as qty ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		cqty = (String) m.get("qty");
	} else {
		cqty = "0";
	}
	/*if (cqty.equalsIgnoreCase(null) || cqty.length() == 0) {
		cqty = "0";
	}*/
	return cqty;
}
public String getkittingchildlocwithkono(String Plant, String Item,String pbatch,String childItem,String kono) throws Exception {
	String cloc = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", pbatch);
	ht.put("CHILD_PRODUCT", childItem);
	ht.put("KONO", kono);

	String query = " CHILD_PRODUCT_LOC as loc ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		cloc = (String) m.get("loc");
	} else {
		cloc = "";
	}
	if (cloc.equalsIgnoreCase(null) || cloc.length() == 0) {
		cloc = "";
	}
	return cloc;
}
public String getkittingscanitemwithkono(String Plant, String Item,String pbatch,String childItem,String kono) throws Exception {
	String scanitem = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", pbatch);
	ht.put("CHILD_PRODUCT", childItem);
	ht.put("KONO", kono);

	String query = " top 1 isnull(SCANITEM,'') as scanitem ";

	Map m = selectkitRow(query, ht, " plant='"+Plant+"' order by CREATE_AT desc");
	if (m.size() > 0) {
		scanitem = (String) m.get("scanitem");
	} else {
		scanitem = "";
	}
	if (scanitem.equalsIgnoreCase(null) || scanitem.length() == 0) {
		scanitem = "";
	}
	return scanitem;
}

public Map selectkitRow(String query, Hashtable ht, String extCondi)
		throws Exception {
	Map map = new HashMap();
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ "[" + (String) ht.get("PLANT") + "_BOM]");
		sql.append(" WHERE ");
		String conditon = formCondition(ht);
		sql.append(conditon);
		if (extCondi.length() > 0)
			sql.append(" and " + extCondi);
		this.mLogger.query(this.printQuery, sql.toString());
		map = getRowOfData(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return map;
}
public ArrayList selectBomHdr(String query, Hashtable ht, String extCond)
		throws Exception {
	boolean flag = false;
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
			+ ht.get("PLANT") + "_" + "BOM" + "]");

	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();

		if (ht.size() > 0) {

			sql.append(" WHERE ");

			conditon = formCondition(ht);

			sql.append(conditon);

		}
		if (extCond.length() > 0)
			sql.append(" " + extCond);
		this.mLogger.query(this.printQuery, sql.toString());

		alData = selectData(con, sql.toString());

	} catch (Exception e) {
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return alData;
}
public boolean isExistskitting(Hashtable ht) throws Exception {

	boolean flag = false;
	java.sql.Connection con = null;

	try {
		con = com.track.gates.DbBean.getConnection();

		// query
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" 1 ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "BOM"
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
public boolean updateBOM(String query, Hashtable htCondition, String extCond)
		throws Exception {
	boolean flag = false;
	String TABLE = "BOM";
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" UPDATE " + "["
				+ htCondition.get("PLANT") + "_" + TABLE + "]");
		sql.append(" ");
		sql.append(query);
		sql.append(" WHERE ");

		String conditon = formCondition(htCondition);

		sql.append(conditon);

		if (extCond.length() != 0) {
			sql.append(extCond);
		}
		this.mLogger.query(this.printQuery, sql.toString());

		flag = updateData(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con);
	}
	return flag;
}
public ArrayList getKittingDeKittinglist(String pitem, String kono, String afrmDate, String atoDate, String plant,
		String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		String dtCondStr =" AND ISNULL(ORDDATE,'')<>'' AND CAST((SUBSTRING(ORDDATE, 7, 4) + '-' + SUBSTRING(ORDDATE, 4, 2) + '-' + SUBSTRING(ORDDATE, 1, 2)) AS date)";

		if (afrmDate.length() > 0) {
			extraCon +=  dtCondStr + "  >= '" + afrmDate + "' ";
				if (atoDate.length() > 0) {
					extraCon += dtCondStr+ " <= '" + atoDate + "' ";
				}
		} else {
				if (atoDate.length() > 0) {
					extraCon += dtCondStr+ " <= '" + atoDate + "' ";
				}
		}
		boolean flag = false;
		String sQry = "WITH S AS (select distinct PARENT_PRODUCT,ISNULL(ORDDATE,'') ORDDATE,PARENT_PRODUCT_LOC,PARENT_PRODUCT_BATCH,"
				+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = B.PARENT_PRODUCT ) PDESC, "
				+"(select isnull(Remark1,'') from ["+plant+"_ITEMMST] where ITEM = B.PARENT_PRODUCT ) PDETDESC, "
				+"isnull(PARENTUOM,'') UOM,isnull(KONO,'') KONO from"
				+ "["
				+ plant
				+ "_"
				+ "BOM]B where PLANT<>'' AND PLANT='"+plant+"') "
				+ "SELECT * FROM S WHERE PARENT_PRODUCT like'%"
				+ pitem
				+"%'  AND KONO like'%" 
				+ kono  
				+"%'  " + extraCon		
				+ " ORDER BY KONO DESC, CAST((SUBSTRING(ORDDATE,7,4) + '-' + SUBSTRING(ORDDATE,4,2) + '-' + SUBSTRING(ORDDATE,1,2))AS DATE) DESC ";
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

public ArrayList getKittingpitemlist(String pitem, String plant,
		String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

		boolean flag = false;
		String sQry = "select distinct PARENT_PRODUCT,"
				+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = B.PARENT_PRODUCT ) PDESC, "
				+"(select isnull(Remark1,'') from ["+plant+"_ITEMMST] where ITEM = B.PARENT_PRODUCT ) PDETDESC, "
				+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = B.PARENT_PRODUCT ) UOM from"
				+ "["
				+ plant
				+ "_"
				+ "BOM]B where PLANT<>'' AND PARENT_PRODUCT like'%"
				+ pitem
				+"%'  " + extraCon;
				//+ " ORDER BY LOC_TYPE_ID ";
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
public ArrayList getKittingchilditemlist(String pitem,String citem, String plant,
		String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

		boolean flag = false;
		String sQry = "select distinct CHILD_PRODUCT,"
				+"(select ITEMDESC from ["+plant+"_ITEMMST] where ITEM = B.CHILD_PRODUCT ) CDESC, "
				+"(select isnull(Remark1,'') from ["+plant+"_ITEMMST] where ITEM = B.CHILD_PRODUCT ) CDETDESC, "
				+"(select STKUOM from ["+plant+"_ITEMMST] where ITEM = B.CHILD_PRODUCT ) UOM from"
				+ "["
				+ plant
				+ "_"
				+ "BOM]B where PLANT<>'' AND PARENT_PRODUCT like'%"
				+ pitem
				+"%' AND CHILD_PRODUCT like'%"+citem+"%'"  + extraCon;
				//+ " ORDER BY LOC_TYPE_ID ";
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
public ArrayList getKittingBatch(String plant,String pitem,String pbatch,String citem,String batch) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		if(batch.equalsIgnoreCase("NOBATCH"))
		{
			batch = "";
		}
		String sQry = "select isnull(CHILD_PRODUCT_BATCH,'') as batch,isnull(Qty,0) as qty  from "
				+ "["
				+ plant
				+ "_"
				+ "BOM] where plant='"
				+ plant
				+ "'  and PARENT_PRODUCT = '"
				+ pitem
				//+ "'   and Qty > 0  and CHILD_PRODUCT = '"+citem+"'"
				+ "' AND PARENT_PRODUCT_BATCH='"+pbatch+"'  and Qty > 0 AND SCANITEM='"+citem+"' and CHILD_PRODUCT_BATCH like '" + batch + "%' ORDER BY CHILD_PRODUCT_BATCH ";
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
public String getkittingparentqty(String Plant, String Item,String batch) throws Exception {
	String pqty = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", batch);

	String query = " isnull(PARENT_PRODUCT_QTY,0) as PQTY ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		pqty = (String) m.get("PQTY");
	} else {
		pqty = "0";
	}
	if (pqty.equalsIgnoreCase(null) || pqty.length() == 0) {
		pqty = "0";
	}
	return pqty;
}
public String getkittingparentqtywithkono(String Plant, String Item,String batch,String kono) throws Exception {
	String pqty = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", batch);
	ht.put("KONO", kono);

	String query = " isnull(PARENT_PRODUCT_QTY,0) as PQTY ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		pqty = (String) m.get("PQTY");
	} else {
		pqty = "0";
	}
	if (pqty.equalsIgnoreCase(null) || pqty.length() == 0) {
		pqty = "0";
	}
	return pqty;
}
public String getkittingparentinvqtywithkono(String Plant, String Item,String batch,String kono) throws Exception {
	String pqty = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", batch);
	ht.put("KONO", kono);

	String query = " distinct isnull(INVQTY,0) as INVQTY ";

	Map m = selectkitRow(query, ht, "");
	if (m.size() > 0) {
		pqty = (String) m.get("INVQTY");
	} else {
		pqty = "0";
	}
	if (pqty.equalsIgnoreCase(null) || pqty.length() == 0) {
		pqty = "0";
	}
	return pqty;
}
public String getchildbomqty(String Plant, String Item,String citem,String type) throws Exception {
	String qty = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PITEM", Item);
	ht.put("CITEM", citem);
	ht.put("BOMTYPE", type);

	String query = " isnull(QTY,0) as QTY ";

	Map m = selectprodbomRow(query, ht, "");
	if (m.size() > 0) {
		qty = (String) m.get("QTY");
	} else {
		qty = "0";
	}
	if (qty.equalsIgnoreCase(null) || qty.length() == 0) {
		qty = "0";
	}
	return qty;
}
public Map selectprodbomRow(String query, Hashtable ht, String extCondi)
		throws Exception {
	Map map = new HashMap();
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ "[" + (String) ht.get("PLANT") + "_PROD_BOM_MST]");
		sql.append(" WHERE ");
		String conditon = formCondition(ht);
		sql.append(conditon);
		if (extCondi.length() > 0)
			sql.append(" and " + extCondi);
		this.mLogger.query(this.printQuery, sql.toString());
		map = getRowOfData(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return map;
}
public int getCount(Hashtable ht) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int iCount = 0;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM ["+ ht.get("PLANT") + "_PROD_BOM_MST]  where "+ formCondition(ht);
	    this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			iCount = rs.getInt(1);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return iCount;

}
public String getkittinglocation(String Plant, String Item,String batch) throws Exception {
	String loc = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("PARENT_PRODUCT", Item);
	ht.put("PARENT_PRODUCT_BATCH", batch);

	String query = " isnull(PARENT_PRODUCT_LOC,'') as PLOC ";

	Map m = selectkitRow(query, ht, " PLANT<>'' Order by CREATE_AT DESC");
	if (m.size() > 0) {
		loc = (String) m.get("PLOC");
	} else {
		loc = "";
	}
	if (loc.equalsIgnoreCase(null) || loc.length() == 0) {
		loc = "";
	}
	return loc;
}

public ArrayList getProductionBOMDetial(String aPitem,String plant, String cond) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	ArrayList arrList = new ArrayList();
	Connection con = null;
	try {
		con = DbBean.getConnection();

		String sQry = "SELECT ISNULL(PITEM,'') PITEM,ISNULL(CITEM,'') CITEM,ISNULL(QTY,0) QTY,ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2 FROM "
				+ "["
				+ plant
				+ "_"
				+ "PROD_BOM_MST"
				+ "]"
				+ " WHERE PITEM = '" + aPitem + "'" + cond;
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		
		while (rs.next()) {
			ArrayList arrLine = new ArrayList();
			arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // pitem
			arrLine.add(1, StrUtils.fString((String) rs.getString(2))); // citem
			arrLine.add(2, StrUtils.fString((String) rs.getString(3))); // qty
			//arrLine.add(3, StrUtils.fString((String) rs.getString(4))); // seqnum
			arrLine.add(3, StrUtils.fString((String) rs.getString(4))); // remark1
			arrLine.add(4, StrUtils.fString((String) rs.getString(5))); // remark2
			arrList.add(arrLine);
			
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return arrList;

}




}
