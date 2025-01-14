package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.track.constants.MLoggerConstant;
import com.track.db.util.BillUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ReturnOrderDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.ReturnOrderDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ReturnOrderDAO_PRINTPLANTMASTERLOG;
	
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
	
	/**
	 * Method to get Purchase Order Details with GRNO and BILL NO list using Prepared Statement
	 * ReturnType: List
	 * Parameter : Hashtable
	 * Author : Abhilash
	 * Date : 26 February 2020
	 */
	
	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "FINPORETURN" + "]");
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
	
	public List<Map<String, String>> getPurchaseOrderReturnDetails(String pono, String grno, String bill, String plant) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = null;
		BillUtil billUtil = new BillUtil();
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		try {
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			
			if(pono.length()>0){
				condition += " AND a.pono = ? ";
				args.add(pono);
			}
			if(grno.length()>0){
				condition += " AND b.GRNO = ? ";
				args.add(grno);
			}
			if(bill.length()>0){
				condition += " AND c.BILL = ? ";
				args.add(bill);
			}  
			String qtype="";
			if(pono.length()>0){
				qtype = "PURCHASE";
			}else {
				if(grno.length()>0){
					qtype = billUtil.getbygrno(plant, grno);
				}else if(bill.length()>0){
					qtype = billUtil.getbybillno(plant, bill);
				}
			}
			
			/*
			 * condition += " AND c.BILL_STATUS != ? "; args.add("CANCELLED");
			 */
			if(qtype.equalsIgnoreCase("PURCHASE")){
			sQry = "SELECT a.pono,a.polnno,a.lnstat,ISNULL(c.BILL_STATUS,'') BILL_STATUS,ISNULL(b.grno,'') grno,ISNULL(c.bill,'') bill,ISNULL(c.CREDITNOTESSTATUS,'') CREDITNOTESSTATUS,a.item,a.itemdesc,isnull(max(a.qtyor),0) as qtyor,"
					+ "isnull(sum(b.recQty),0) as qtyrc,isnull(sum(b.REVERSEQTY),0) as qtyrd,isnull(a.userfld2,'') as ref,isnull(a.userfld3,'') as sname,"
					+ "b.loc,b.batch,isnull(b.expirydat,'') expirydate,isnull(a.UNITMO,'') as uom,"
					+ "ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM=a.UNITMO),1) UOMQTY  from "
					+ "["+plant+"_PODET]a left join ["+plant+"_RECVDET] b on a.polnno=b.lnno "
					+ "left join ["+plant+"_FINBILLHDR] c on b.GRNO = c.GRNO"
					+ " where a.plant <> '' "+ condition
					+ "and a.pono=b.pono and a.item=b.item "
					+ " group  by a.pono,b.batch,a.polnno,b.GRNO,c.BILL,c.CREDITNOTESSTATUS,a.item,a.itemdesc,b.loc,b.batch,a.userfld2,"
					+ "a.userfld3,a.lnstat,b.expirydat,a.UNITMO,c.BILL_STATUS order by a.polnno";
			}else {
				sQry = "SELECT b.pono,b.LNNO as polnno,'C' as lnstat,ISNULL(c.BILL_STATUS,'') BILL_STATUS,ISNULL(b.grno,'') grno,ISNULL(c.bill,'') bill,"
						+ "ISNULL(c.CREDITNOTESSTATUS,'') CREDITNOTESSTATUS,b.item,b.itemdesc,isnull(max(b.ORDQTY),0) as qtyor,isnull(sum(b.recQty),0) as qtyrc,isnull(sum(b.REVERSEQTY),0) as qtyrd,'' as ref,"
						+ "ISNULL((SELECT v.VNAME FROM " + plant +"_VENDMST v WHERE v.VENDNO=c.VENDNO),'') as sname,b.loc,b.batch,isnull(b.expirydat,'') expirydate,ISNULL(d.UOM,'') as uom,ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM=d.UOM),1) UOMQTY from ["+plant+"_RECVDET] b "
						+ "left join ["+plant+"_FINBILLHDR] c on b.GRNO = c.GRNO left join ["+plant+"_FINBILLDET] d on d.BILLHDRID = c.ID and d.LNNO = b.LNNO and d.ITEM = b.ITEM where b.plant <> '' "+ condition
						+ "group  by b.pono,b.batch,b.LNNO,b.GRNO,c.BILL,c.VENDNO,d.UOM,c.CREDITNOTESSTATUS,b.item,b.itemdesc,b.loc,b.batch,b.CNAME,b.expirydat,c.BILL_STATUS order by b.LNNO";
			}
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			   returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}
	
	/**
	 * Method for inserting the PO Return details using Prepared Statement
	 * ReturnType: boolean
	 * Parameter : Hashtable
	 * Author : Abhilash
	 * Date : 29 February 2020
	 */
	public boolean insertPoReturnDetails(Hashtable ht, String plant)throws Exception{
		boolean insertCount = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "";
	  try{
		  /*Instantiate the list*/
		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}
			query = "INSERT INTO ["+ plant +"_FINPORETURN] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
		  /*Create the PreparedStatement object*/
		  if(connection != null){
			  ps = connection.prepareStatement(query);
			  this.mLogger.query(this.printQuery, query);
			/*Execute the Query*/
		     insertCount = execute_NonSelectQuery(ps, args);
		  }		  
	  }catch (NamingException e) {
			MLogger.log(0, "" + e.getMessage()+" With values "+args);
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			MLogger.log(0, "" + e.getMessage()+" With values "+args);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage()+" With values "+args);
			e.printStackTrace();
			throw e;
		} finally {
			if (ps != null && connection != null) {
				DbBean.closeConnection(connection, ps);
			}
		}
		return insertCount;
	}
	
	
	/**
	 * Method to get Purchase Order Details for PO Return Summary using Prepared Statement
	 * ReturnType: List
	 * Parameter : String, String,String, String, String, String
	 * Author : Abhilash
	 * Date : 29 February 2020
	 */
	public List<Map<String, String>> getPurchaseOrderReturnByGrouping(String pono, String grno, 
			String bill, String vendno, String frmDate, String toDate, String plant) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		try{
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			args.add(plant);
			if(pono.length()>0){
				condition += " AND PONO = ? ";
				args.add(pono);
			}
			
			if(bill.length()>0){
				condition += " AND BILL = ? ";
				args.add(bill);
			}
			
			if(grno.length()>0){
				condition += " AND GRNO = ? ";
				args.add(grno);
			}
			
			if(vendno.length()>0){
				condition += " AND VENDNO = ? ";
				args.add(vendno);
			}
			
			if(frmDate.length()>0){
				condition += "AND CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) >= ?";
				args.add(frmDate);
			}  
			if(toDate.length()>0){
				condition += "AND CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) <= ?";
				args.add(toDate);
			}			
			
			sQry = "SELECT PONO,LNNO,GRNO,BILL,(SELECT VNAME FROM ["+plant+"_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME,RETURN_DATE,STATUS,SUM(RETURN_QTY) AS RETURN_QTY FROM "
					+ "["+plant+"_FINPORETURN] A WHERE PLANT = ? "+condition+" GROUP BY PONO,LNNO,GRNO,BILL,VENDNO,RETURN_DATE,STATUS";
			sQry=sQry+ " ORDER BY CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) DESC ";
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			   returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}
	
	public List<Map<String, String>> getPurchaseOrderReturnByGroupingforsummary(String pono, String grno, 
			String bill, String vendno, String frmDate, String toDate, String plant) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		try{
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			args.add(plant);
			if(pono.length()>0){
				condition += " AND PONO = ? ";
				args.add(pono);
			}
			
			if(bill.length()>0){
				condition += " AND BILL = ? ";
				args.add(bill);
			}
			
			if(grno.length()>0){
				condition += " AND GRNO = ? ";
				args.add(grno);
			}
			
			if(vendno.length()>0){
				condition += " AND VENDNO = ? ";
				args.add(vendno);
			}
			
			if(frmDate.length()>0){
				condition += " AND CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) >= ?";
				args.add(frmDate);
			}  
			if(toDate.length()>0){
				condition += " AND CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) <= ?";
				args.add(toDate);
			}			
			
			sQry = "SELECT PONO,PORETURN,GRNO,BILL,(SELECT VNAME FROM ["+plant+"_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME,RETURN_DATE,STATUS,SUM(RETURN_QTY) AS RETURN_QTY FROM "
					+ "["+plant+"_FINPORETURN] A WHERE PLANT = ? "+condition+" GROUP BY PORETURN,PONO,GRNO,BILL,VENDNO,RETURN_DATE,STATUS";
			sQry=sQry+ " ORDER BY CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) DESC,PORETURN DESC";
			//sQry=sQry+ " ORDER BY PORETURN DESC ";
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			   returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}
	
	/**
	 * Method to get Purchase Order Details for PO Return Summary using Prepared Statement
	 * ReturnType: List
	 * Parameter : String, String,String, String, String
	 * Author : Abhilash
	 * Date : 29 February 2020
	 */
	public List<Map<String, String>> getPOReturnDetails(String pono, String grno, 
			String bill, String vendno, String returnDate, String plant) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		try{
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			args.add(plant);
			if(pono.length()>0){
				condition += " AND PONO = ? ";
				args.add(pono);
			}
			
			if(bill.length()>0){
				condition += " AND BILL = ? ";
				args.add(bill);
			}
			
			if(grno.length()>0){
				condition += " AND GRNO = ? ";
				args.add(grno);
			}
			
			if(vendno.length()>0){
				condition += " AND VENDNO = ? ";
				args.add(vendno);
			}
			
			if(returnDate.length()>0){
				condition += " AND RETURN_DATE = ?";
				args.add(returnDate);
			}  			
			
			sQry = "SELECT PONO,LNNO,GRNO,BILL,(SELECT VNAME FROM ["+plant+"_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME,RETURN_DATE,STATUS,RETURN_QTY FROM "
					+ "["+plant+"_FINPORETURN] A WHERE PLANT = ? "+condition+"";
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			   returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}
	
	/**
	 * Method to get Sales Order Details with GRNO and BILL NO list using Prepared Statement
	 * ReturnType: List
	 * Parameter : String, String, String
	 * Author : Abhilash
	 * Date : 01 March 2020
	 */
	
	public List<Map<String, String>> getSalesOrderReturnDetails(String dono, String invoiceno,String gino, String plant) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		ShipHisDAO shipHisDAO = new ShipHisDAO();
		InvoiceDAO invoiceDAO = new InvoiceDAO();
		try {
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			
			if(dono.length()>0){
				condition += " and a.dono = ? ";
				args.add(dono);
			}
			if(invoiceno.length()>0){
				condition += " and c.invoice = ? ";
				args.add(invoiceno);
			}  
			
			if(gino.length()>0){
				condition += " and a.INVOICENO = ? ";
				args.add(gino);
			}  
			condition += " and a.plant = ? ";
			args.add(plant);
			
			
			/*sQry = "SELECT b.dono dono,dolno,isnull(c.invoice,'') as invoice,isnull(c.CREDITNOTESSTATUS,'') as CREDITNOTESSTATUS,b.cname, loc1 loc,b.item,b.itemdesc,"
					+ "isnull(ordqty,0) as qtyor,isnull(sum(Pickqty),0) as qtyissue,batch,UNITMO,"
					+ "isnull((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM=UNITMO),1) UOMQTY  from "
					+ "["+plant+"_DODET] a left join ["+plant+"_FININVOICEHDR] c on a.dono = c.dono "
					+ "LEFT JOIN ["+plant+"_SHIPHIS] b ON c.GINO=b.INVOICENO"
					+ "where a.plant <> '' "
					+ "and a.DOLNNO=b.DOLNO and STATUS='C' and a.ITEM=b.ITEM  "+ condition
					+ "group  by b.dono,dolno,c.invoice,c.CREDITNOTESSTATUS,b.cname,loc1,b.item,b.itemdesc,ordqty,batch,"
					+ "UNITMO having isnull(sum(Pickqty),0)>0 order by cast(dolno as int)";*/
			
			/*sQry = "SELECT a.dono dono,dolno,isnull(a.INVOICENO,'') as invoice,isnull(c.CREDITNOTESSTATUS,'') as CREDITNOTESSTATUS,a.cname, a.loc1 loc,a.item,a.itemdesc,"
					+ "isnull(ordqty,0) as qtyor,isnull(sum(Pickqty),0) as qtyissue,batch,UNITMO,"
					+ "isnull((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM=UNITMO),1) UOMQTY  from "
					+ "["+plant+"_SHIPHIS] a LEFT JOIN ["+plant+"_DODET] b ON a.DONO=b.DONO  "
					+ "left join ["+plant+"_FININVOICEHDR] c on c.DONO=a.DONO "
					+ "where b.plant <> '' "
					+ "and b.DOLNNO=a.DOLNO and STATUS='C' and b.ITEM=a.ITEM  "+ condition
					+ "group  by a.dono,dolno,a.INVOICENO,c.CREDITNOTESSTATUS,a.cname,loc1,a.item,a.itemdesc,ordqty,batch,"
					+ "UNITMO having isnull(sum(Pickqty),0)>0 order by cast(dolno as int)";*/
			
			String invoicetype = "";
			if(dono.length()>0) {
				invoicetype = "SALES";
			}else {
				if(invoiceno.length()>0 && gino.length()>0) {
					Hashtable ht = new Hashtable();
					ht.put("INVOICENO", gino);
					ht.put("PLANT", plant);
					String query = "ISNULL(DONO,'') AS DONO ";
					ArrayList SHIPHISTLIST = shipHisDAO.selectShipHis(query, ht);
					Map shiphis=(Map)SHIPHISTLIST.get(0);
					
					if(shiphis.get("DONO").toString().equalsIgnoreCase("")) {
						invoicetype = "INVENTORY";
					}else {
						invoicetype = "SALES";
					}
				}else if(gino.length()>0) {
					Hashtable ht = new Hashtable();
					ht.put("INVOICENO", gino);
					ht.put("PLANT", plant);
					String query = "ISNULL(DONO,'') AS DONO ";
					ArrayList SHIPHISTLIST = shipHisDAO.selectShipHis(query, ht);
					Map shiphis=(Map)SHIPHISTLIST.get(0);
					
					if(shiphis.get("DONO").toString().equalsIgnoreCase("")) {
						invoicetype = "INVENTORY";
					}else {
						invoicetype = "SALES";
					}
				}else if(invoiceno.length()>0) {
					Hashtable ht = new Hashtable();
					ht.put("INVOICE", invoiceno);
					String query = "SELECT ISNULL(DONO,'') AS DONO FROM "+plant+"_FININVOICEHDR WHERE PLANT ='"+plant+"' ";
					List invoiceHdrList =  invoiceDAO.selectForReport(query, ht, "");
					Map invoiceHdr=(Map)invoiceHdrList.get(0);
					
					if(invoiceHdr.get("DONO").toString().equalsIgnoreCase("")) {
						invoicetype = "INVENTORY";
					}else {
						invoicetype = "SALES";
					}
				}
			}
			
			
			if(invoicetype.equalsIgnoreCase("SALES")) {
				sQry = "SELECT a.dono dono,dolno,isnull(a.INVOICENO,'') as gino,a.cname, a.loc1 loc,a.item,a.itemdesc,ISNULL(c.BILL_STATUS,'') AS BILL_STATUS,"
						+ "(SELECT CREDITNOTESSTATUS FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = a.INVOICENO) as CREDITNOTESSTATUS,"
						+ "(SELECT ISNULL(CustCode,'') FROM ["+plant+"_DOHDR] WHERE DONO = b.DONO) as CUSTCODE,"
//						+ "isnull((select ISNULL(CNAME,'') from ["+plant+"_CUSTMST] where CUSTNO=c.CUSTNO),1) CustName,"
						+ "isnull(ordqty,0) as qtyor,isnull(REVERSEQTY,0) as qtyrd,isnull(Pickqty,0) as qtyissue,batch,UNITMO,"
						+ "isnull((select ISNULL(INVOICE,'') from ["+plant+"_FININVOICEHDR] where GINO=a.INVOICENO),'') invoice,"
						+ "isnull((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM=UNITMO),1) UOMQTY  from "
						+ "["+plant+"_SHIPHIS] a LEFT JOIN ["+plant+"_DODET] b ON a.DONO=b.DONO  "
						+ "left join ["+plant+"_FININVOICEHDR] c on c.GINO=a.INVOICENO "
						+ "where b.plant <> '' "
						+ "and b.DOLNNO=a.DOLNO and STATUS='C' and b.ITEM=a.ITEM  "+ condition
						+ "group  by a.dono,b.dono,dolno,a.INVOICENO,a.Pickqty,a.cname,loc1,a.item,a.itemdesc,ordqty,batch,c.BILL_STATUS,"
						+ "UNITMO,isnull(REVERSEQTY,0) having isnull(sum(Pickqty),0)>0 order by cast(dolno as int)";
			}else {
//				sQry = "SELECT a.dono dono,dolno,isnull(a.INVOICENO,'') as gino,a.cname, a.loc1 loc,a.item,a.itemdesc,ISNULL(c.BILL_STATUS,'') AS BILL_STATUS,c.CUSTNO AS CUSTCODE,"
						sQry = "SELECT a.dono dono,dolno,isnull(a.INVOICENO,'') as gino,a.cname, a.loc1 loc,a.item,isnull((select ISNULL(ITEMDESC,'') from ["+plant+"_ITEMMST] where ITEM=a.ITEM),'') itemdesc,ISNULL(c.BILL_STATUS,'') AS BILL_STATUS,c.CUSTNO AS CUSTCODE,"
						+ "(SELECT CREDITNOTESSTATUS FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = a.INVOICENO) as CREDITNOTESSTATUS,"/*+ "(SELECT ISNULL(CUSTNO,'') FROM ["+plant+"_DOHDR] WHERE ID = b.INVOICEHDRID) as CUSTCODE,"*/
						+ "isnull(ordqty,0) as qtyor,isnull(REVERSEQTY,0) as qtyrd,isnull(Pickqty,0) as qtyissue,b.batch,ISNULL(b.UOM,'') UNITMO,"
						+ "isnull((select ISNULL(CNAME,'') from ["+plant+"_CUSTMST] where CUSTNO=c.CUSTNO),1) CustName,"
						+ "isnull((select ISNULL(INVOICE,'') from ["+plant+"_FININVOICEHDR] where GINO=a.INVOICENO),'') invoice,"
						+ "isnull((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM=b.UOM),1) UOMQTY  from "
						+ "["+plant+"_SHIPHIS] a left join ["+plant+"_FININVOICEHDR] c on c.GINO=a.INVOICENO LEFT JOIN ["+plant+"_FININVOICEDET] b ON b.INVOICEHDRID=c.ID and b.LNNO=a.DOLNO "
						+ "where b.plant <> '' "
						+ "and b.LNNO=a.DOLNO and STATUS='C' and b.ITEM=a.ITEM  "+ condition
						+ "group  by a.dono,dolno,a.INVOICENO,a.Pickqty,a.cname,c.CUSTNO,loc1,a.item,a.itemdesc,ordqty,b.batch,c.BILL_STATUS,"
						+ "UOM,isnull(REVERSEQTY,0) having isnull(sum(Pickqty),0)>0 order by cast(dolno as int)";
			}
			
			
			
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			   returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}
	
	/**
	 * Method for inserting the PO Return details using Prepared Statement
	 * ReturnType: boolean
	 * Parameter : Hashtable
	 * Author : Abhilash
	 * Date : 29 February 2020
	 */
	public boolean insertSOReturnDetails(Hashtable ht, String plant)throws Exception{
		boolean insertCount = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "";
	  try{
		  /*Instantiate the list*/
		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}
			query = "INSERT INTO ["+ plant +"_FINSORETURN] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
		  /*Create the PreparedStatement object*/
		  if(connection != null){
			  ps = connection.prepareStatement(query);
			  this.mLogger.query(this.printQuery, query);
			/*Execute the Query*/
		     insertCount = execute_NonSelectQuery(ps, args);
		  }		  
	  }catch (NamingException e) {
			MLogger.log(0, "" + e.getMessage()+" With values "+args);
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			MLogger.log(0, "" + e.getMessage()+" With values "+args);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage()+" With values "+args);
			e.printStackTrace();
			throw e;
		} finally {
			if (ps != null && connection != null) {
				DbBean.closeConnection(connection, ps);
			}
		}
		return insertCount;
	}
	
	
	/**
	 * Method to get Purchase Order Details for PO Return Summary using Prepared Statement
	 * ReturnType: List
	 * Parameter : String, String,String, String, String, String
	 * Author : Abhilash
	 * Date : 29 February 2020
	 */
	public List<Map<String, String>> getSalesOrderReturnByGrouping(String dono, String gino, String invoice, 
			String custno, String frmDate, String toDate, String plant) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		try{
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			args.add(plant);
			if(dono.length()>0){
				condition += " AND DONO = ? ";
				args.add(dono);
			}
			
			if(invoice.length()>0){
				condition += " AND INVOICE = ? ";
				args.add(invoice);
			}
			
			if(gino.length()>0){
				condition += " AND GINO = ? ";
				args.add(gino);
			}
			
			if(custno.length()>0){
				condition += " AND CUSTNO = ? ";
				args.add(custno);
			}
			
			if(frmDate.length()>0){
				condition += "AND CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) >= ?";
				args.add(frmDate);
			}  
			if(toDate.length()>0){
				condition += "AND CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) <= ?";
				args.add(toDate);
			}			
			
			sQry = "SELECT DONO,SORETURN,INVOICE,ISNULL(GINO,'') as GINO,(SELECT CNAME FROM ["+plant+"_CUSTMST] WHERE CUSTNO = A.CUSTNO ) AS CNAME,RETURN_DATE,STATUS,SUM(RETURN_QTY) AS RETURN_QTY FROM "
					+ "["+plant+"_FINSORETURN] A WHERE PLANT = ? "+condition+" GROUP BY SORETURN,DONO,INVOICE,GINO,CUSTNO,RETURN_DATE,STATUS";
			sQry=sQry+ " ORDER BY CAST((SUBSTRING(RETURN_DATE,7,4) + '-' + SUBSTRING(RETURN_DATE,4,2) + '-' + SUBSTRING(RETURN_DATE,1,2)) AS date) DESC,SORETURN DESC";
			//sQry=sQry+ " ORDER BY SORETURN DESC ";
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			   returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}
	
	/**
	 * Method to get Sales Order Details for PO Return Summary using Prepared Statement
	 * ReturnType: List
	 * Parameter : String, String,String, String, String
	 * Author : Abhilash
	 * Date : 29 February 2020
	 */
	public List<Map<String, String>> getSOReturnDetails(String dono, String invoice, 
			String custno, String returnDate, String plant) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		try{
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			args.add(plant);
			if(dono.length()>0){
				condition += " AND DONO = ? ";
				args.add(dono);
			}
			
			if(invoice.length()>0){
				condition += " AND INVOICE = ? ";
				args.add(invoice);
			}
			
			if(custno.length()>0){
				condition += " AND CUSTNO = ? ";
				args.add(custno);
			}
			
			if(returnDate.length()>0){
				condition += " AND RETURN_DATE = ?";
				args.add(returnDate);
			}  			
			
			sQry = "SELECT DONO,LNNO,INVOICE,(SELECT CNAME FROM ["+plant+"_CUSTMST] WHERE CUSTNO = A.CUSTNO ) AS CNAME,RETURN_DATE,STATUS,RETURN_QTY,ITEM FROM "
					+ "["+plant+"_FINSORETURN] A WHERE PLANT = ? "+condition+"";
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			   returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}
	
	public List<Map<String, String>> getPOReturnDetailsbyprno(String prno,String pono, String grno, String plant) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		try{
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			args.add(plant);
			if(prno.length()>0){
				condition += " AND PORETURN = ? ";
				args.add(prno);
			}
			
			if(pono.length()>0){
				condition += " AND PONO = ? ";
				args.add(pono);
			}
			
			if(grno.length()>0){
				condition += " AND GRNO = ? ";
				args.add(grno);
			}
			
			sQry = "SELECT PONO,LNNO,PORETURN,GRNO,BILL,ITEM,(SELECT VNAME FROM ["+plant+"_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME,RETURN_DATE,STATUS,RETURN_QTY,ISNULL(CREDITED_QTY,'0') AS CREDITED_QTY FROM "
					+ "["+plant+"_FINPORETURN] A WHERE PLANT = ? "+condition+"";
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			   returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}


public List<Map<String, String>> getPOReturnDetailsbyprnoandcost(String prno,String pono,String grno,String plant) throws Exception {
	Connection connection = null;
	List<Map<String, String>> returnOrderList = null;
	PreparedStatement ps = null;
	List<String> args = null;
	String sQry = "";
	String condition = "";
	try{
		/*Instantiate the list*/
		args = new ArrayList<String>();			
		connection = DbBean.getConnection();
		args.add(plant);
		if(prno.length()>0){
			condition += " AND PORETURN = ? ";
			args.add(prno);
		}
		
		if(pono.length()>0){
			condition += " AND PONO = ? ";
			args.add(pono);
		}
		
		if(grno.length()>0){
			condition += " AND GRNO = ? ";
			args.add(grno);
		}
		
		if(pono.length()>0){
		
		sQry = "SELECT PONO,VENDNO,NOTE,LNNO,PORETURN,GRNO,ITEM,BILL,(SELECT VNAME FROM ["+plant+"_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME,RETURN_DATE,STATUS,(ISNULL(RETURN_QTY,'0') - ISNULL(CREDITED_QTY,'0')) AS RETURN_QTY,"
				+"ISNULL((SELECT CATLOGPATH FROM "+plant+"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as CATLOGPATH,"
				+" ISNULL((SELECT C.COST FROM [" + plant + "_ITEMMST] C WHERE A.ITEM = C.ITEM),'') BASECOST,"
				+ "ISNULL((SELECT ITEMDESC FROM "+ plant +"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as ITEMDESC,"
				+"ISNULL((SELECT ORDER_DISCOUNT FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ORDERDISCOUNT,"
				+"ISNULL((SELECT ORDERDISCOUNTTYPE FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ORDERDISCOUNTTYPE,"
				+"ISNULL((SELECT ITEM_RATES FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ISTAXINCLUSIVE,"
				+"ISNULL((SELECT ISDISCOUNTTAX FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ISDISCOUNTTAXINC,"
				+"ISNULL((SELECT ISORDERDISCOUNTTAX FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ISODISCOUNTTAXINC,"
				+"ISNULL((SELECT INBOUND_GST FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS GST,"
				+"ISNULL((SELECT DISCOUNT FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS BILLDISCOUNT,"
				+"ISNULL((SELECT DISCOUNT_TYPE FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS BILLDISCOUNTTYPE,"
				+"ISNULL((SELECT SUB_TOTAL FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS BILLSUB_TOTAL,"
				+"ISNULL((SELECT PROJECTID FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS PROJECTID,"
				+"ISNULL((SELECT SHIPPINGID FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS SHIPPINGID,"
				
				+"ISNULL((SELECT EMPNO FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'') AS EMPNO,"
				+"ISNULL((SELECT TAXTREATMENT FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'') AS TAXTREATMENT," //RESVI
				
				+ "ISNULL((SELECT top 1 FNAME FROM " +plant +"_EMP_MST E join ["+plant+"_FINBILLHDR] H on E.EMPNO=H.EMPNO  WHERE BILL = A.BILL),'') as EMP_NAME,"
				+"ISNULL((SELECT PAYMENT_TERMS FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'') AS PAYMENT_TERMS,"
				+"ISNULL((SELECT SHIPPINGCUSTOMER FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'') AS SHIPPINGCUSTOMER,"
				+"ISNULL(( select top 1 CURRENCYID from "+plant+"_FINBILLHDR p where p.BILL=A.BILL),(select CURRENCYID from "+plant+"_POHDR p where p.PONO=A.PONO)) as CURRENCYID,"
				+"ISNULL(( select top 1 d.CURRENCYUSEQT from "+plant+"_FINBILLHDR p join "+plant+"_FINBILLDET d on p.ID=d.BILLHDRID where p.BILL=A.BILL),(ISNULL((select top 1 CURRENCYUSEQT from "+plant+"_PODET p where p.PONO=A.PONO),1))) as CURRENCYUSEQT,"
				+"ISNULL((SELECT SUM(C.DISCOUNT) FROM ["+plant+"_FINBILLHDR] AS B left join ["+plant+"_FINBILLDET] C on B.ID = C.BILLHDRID WHERE B.BILL = A.BILL ),'0') AS BILLLINEDISCOUNT,"
				+"(SELECT UNITCOST FROM ["+plant+"_PODET] WHERE PONO = A.PONO AND POLNNO = A.LNNO AND ITEM = A.ITEM) AS UNITCOST FROM "
				+ "["+plant+"_FINPORETURN] A WHERE PLANT = ? "+condition+"";
		}else {
			sQry = "SELECT PONO,VENDNO,NOTE,LNNO,PORETURN,GRNO,ITEM,BILL,(SELECT VNAME FROM ["+plant+"_VENDMST] WHERE VENDNO = A.VENDNO ) AS VNAME,RETURN_DATE,STATUS,(ISNULL(RETURN_QTY,'0') - ISNULL(CREDITED_QTY,'0')) AS RETURN_QTY,"
					+"ISNULL((SELECT CATLOGPATH FROM "+plant+"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as CATLOGPATH,"
					+" ISNULL((SELECT C.COST FROM [" + plant + "_ITEMMST] C WHERE A.ITEM = C.ITEM),'') BASECOST,"
					+"ISNULL((SELECT ORDER_DISCOUNT FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ORDERDISCOUNT,"
					+"ISNULL((SELECT ORDERDISCOUNTTYPE FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ORDERDISCOUNTTYPE,"
					+"ISNULL((SELECT ITEM_RATES FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ISTAXINCLUSIVE,"
					+"ISNULL((SELECT ISDISCOUNTTAX FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ISDISCOUNTTAXINC,"
					+"ISNULL((SELECT ISORDERDISCOUNTTAX FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS ISODISCOUNTTAXINC,"
					+"ISNULL((SELECT INBOUND_GST FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS GST,"
					+"ISNULL((SELECT DISCOUNT FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS BILLDISCOUNT,"
					+"ISNULL((SELECT DISCOUNT_TYPE FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS BILLDISCOUNTTYPE,"
					+"ISNULL((SELECT SUB_TOTAL FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS BILLSUB_TOTAL,"
					+"ISNULL((SELECT PROJECTID FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS PROJECTID,"
					+"ISNULL((SELECT SHIPPINGID FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS SHIPPINGID,"
					
					+"ISNULL((SELECT EMPNO FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS EMPNO,"
						+"ISNULL((SELECT TAXTREATMENT FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'') AS TAXTREATMENT," //RESVI
					
					
					+ "ISNULL((SELECT top 1 FNAME FROM " +plant +"_EMP_MST E join ["+plant+"_FINBILLHDR] H on E.EMPNO=H.EMPNO  WHERE BILL = A.BILL),'') as EMP_NAME,"
					
					+"ISNULL((SELECT PAYMENT_TERMS FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'0') AS PAYMENT_TERMS,"
					+"ISNULL((SELECT SHIPPINGCUSTOMER FROM ["+plant+"_FINBILLHDR] WHERE BILL = A.BILL),'') AS SHIPPINGCUSTOMER,"
					+"ISNULL(( select top 1 CURRENCYID from "+plant+"_FINBILLHDR p where p.BILL=A.BILL),(select CURRENCYID from "+plant+"_POHDR p where p.PONO=A.PONO)) as CURRENCYID,"
					+"ISNULL(( select top 1 d.CURRENCYUSEQT from "+plant+"_FINBILLHDR p join "+plant+"_FINBILLDET d on p.ID=d.BILLHDRID where p.BILL=A.BILL),(ISNULL((select CURRENCYUSEQT from "+plant+"_FINBILLHDR p where p.GRNO=A.GRNO),1))) as CURRENCYUSEQT,"
					+"ISNULL((SELECT SUM(C.DISCOUNT) FROM ["+plant+"_FINBILLHDR] AS B left join ["+plant+"_FINBILLDET] C on B.ID = C.BILLHDRID WHERE B.BILL = A.BILL ),'0') AS BILLLINEDISCOUNT,"
					+"(SELECT COST FROM ["+plant+"_FINBILLDET] as det left join ["+plant+"_FINBILLHDR] as hdr on hdr.ID = det.BILLHDRID WHERE hdr.GRNO = A.GRNO AND det.LNNO = A.LNNO AND det.ITEM = A.ITEM) AS UNITCOST FROM "
					+ "["+plant+"_FINPORETURN] A WHERE PLANT = ? "+condition+"";
		}
		if(connection != null){
		   /*Create  PreparedStatement object*/
		   ps = connection.prepareStatement(sQry);
		    
		   this.mLogger.query(this.printQuery, sQry);
		   returnOrderList = selectData(ps, args);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return returnOrderList;
}


public boolean updatePoReturnDetails(String setquery,String condition, String plant)throws Exception{
	boolean insertCount = false;
	Connection connection = null;
	PreparedStatement ps = null;
	List<String> args = null;
    String query = "";
  try{
	  /*Instantiate the list*/
	    args = new ArrayList<String>();		    
		connection = DbBean.getConnection();			
		
		query = "UPDATE ["+ plant +"_FINPORETURN] SET "+setquery+" WHERE "+condition;
	  /*Create the PreparedStatement object*/
	  if(connection != null){
		  ps = connection.prepareStatement(query);
		  this.mLogger.query(this.printQuery, query);
		/*Execute the Query*/
	     insertCount = execute_NonSelectQuery(ps, args);
	  }		  
  }catch (NamingException e) {
		MLogger.log(0, "" + e.getMessage()+" With values "+args);
		e.printStackTrace();
		throw e;
	} catch (SQLException e) {
		MLogger.log(0, "" + e.getMessage()+" With values "+args);
		e.printStackTrace();
		throw e;
	} catch (Exception e) {
		MLogger.log(0, "" + e.getMessage()+" With values "+args);
		e.printStackTrace();
		throw e;
	} finally {
		if (ps != null && connection != null) {
			DbBean.closeConnection(connection, ps);
		}
	}
	return insertCount;
}

public List<Map<String, String>> getSOReturnDetailsBYso(String sorno,String dono,String gino, String invoice, String plant) throws Exception {
	Connection connection = null;
	List<Map<String, String>> returnOrderList = null;
	PreparedStatement ps = null;
	List<String> args = null;
	String sQry = "";
	String condition = "";
	try{
		/*Instantiate the list*/
		args = new ArrayList<String>();			
		connection = DbBean.getConnection();
		args.add(plant);
		if(sorno.length()>0){
			condition += " AND SORETURN = ? ";
			args.add(sorno);
		}
		
		if(dono.length()>0){
			condition += " AND DONO = ? ";
			args.add(dono);
		}
		
		if(gino.length()>0){
			condition += " AND GINO = ? ";
			args.add(gino);
		}
		
		if(invoice.length()>0){
			condition += " AND INVOICE = ? ";
			args.add(invoice);
		}
		
		if(dono.length()>0){
			sQry = "SELECT DONO,SORETURN,ISNULL(GINO,'') AS GINO,ISNULL(CREDITNOTE_QTY,'') AS CREDITNOTE_QTY,LNNO,CUSTNO,NOTE,ITEM,INVOICE,(SELECT CNAME FROM ["+plant+"_CUSTMST] WHERE CUSTNO = A.CUSTNO ) AS CNAME,RETURN_DATE,STATUS,RETURN_QTY, "
					+"ISNULL((SELECT UNITPRICE FROM "+plant+"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as BASECOST,"
					+"ISNULL((SELECT ITEMDESC FROM "+ plant +"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as ITEMDESC,"
					+"ISNULL((SELECT CATLOGPATH FROM "+plant+"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as CATLOGPATH,"
					+"ISNULL((SELECT ORDERDISCOUNT FROM ["+plant+"_DOHDR] WHERE DONO = A.DONO),'0') AS ORDERDISCOUNT,"
					+"ISNULL((SELECT top 1 SUB_TOTAL FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS INVOICESUB_TOTAL,"
					+"ISNULL(( select top 1 CURRENCYID from "+plant+"_FININVOICEHDR p where p.INVOICE=A.INVOICE),(select CURRENCYID from "+plant+"_DOHDR p where p.DONO=A.DONO)) as CURRENCYID,"
					+"ISNULL(( select top 1 p.CURRENCYUSEQT from "+plant+"_FININVOICEHDR p join "+plant+"_FININVOICEDET d on p.ID=d.INVOICEHDRID where p.INVOICE=A.INVOICE),(ISNULL((select top 1 p.CURRENCYUSEQT from "+plant+"_DODET p where p.DONO=A.DONO),1))) as CURRENCYUSEQT,"
					+"ISNULL((SELECT top 1 DISCOUNT FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS INVOICEDISCOUNT,"
					+"ISNULL((SELECT top 1 DISCOUNT_TYPE FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS INVOICEDISCOUNTTYPE,"
					+"ISNULL((SELECT top 1 PROJECTID FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS PROJECTID,"
					+"ISNULL((SELECT top 1 SHIPPINGID FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS SHIPPINGID,"
					+"ISNULL((SELECT top 1 SHIPPINGCUSTOMER FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'') AS SHIPPINGCUSTOMER,"
					+"ISNULL((SELECT top 1 PAYMENT_TERMS FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'') AS PAYMENT_TERMS,"
					+"ISNULL((SELECT top 1  TAXID FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS TAXID,"
					+"ISNULL((SELECT ORDERDISCOUNTTYPE FROM ["+plant+"_DOHDR] WHERE DONO = A.DONO ),'%') AS ORDERDISCOUNTTYPE,"
					+"ISNULL((SELECT top 1 ISDISCOUNTTAX FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS ISDISCOUNTTAX,"
					+"ISNULL((SELECT top 1 ISORDERDISCOUNTTAX FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS ISORDERDISCOUNTTAX,"
					+"ISNULL((SELECT OUTBOUND_GST FROM ["+plant+"_DOHDR] WHERE DONO = A.DONO ),'0') AS OUTBOUD_GST,"
					+"ISNULL((SELECT top 1 SALES_LOCATION FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS SALES_LOCATION,"
					+"ISNULL((SELECT SUM(C.DISCOUNT) FROM ["+plant+"_FININVOICEHDR] AS B left join ["+plant+"_FININVOICEDET] C on B.ID = C.INVOICEHDRID WHERE B.INVOICE = A.INVOICE ),'0') AS INVOICELINEDISCOUNT,"
					+"(SELECT UNITPRICE FROM ["+plant+"_DODET] WHERE DONO = A.DONO AND DOLNNO = A.LNNO AND ITEM = A.ITEM) AS UNITPRICE FROM "
					+ "["+plant+"_FINSORETURN] A WHERE PLANT = ? "+condition+"";
		}else {
			sQry = "SELECT DONO,SORETURN,ISNULL(GINO,'') AS GINO,ISNULL(CREDITNOTE_QTY,'') AS CREDITNOTE_QTY,LNNO,CUSTNO,NOTE,ITEM,INVOICE,(SELECT CNAME FROM ["+plant+"_CUSTMST] WHERE CUSTNO = A.CUSTNO ) AS CNAME,RETURN_DATE,STATUS,RETURN_QTY, "
					+"ISNULL((SELECT C.INVOICEHDRID FROM ["+plant+"_FININVOICEHDR] AS B left join ["+plant+"_FININVOICEDET] C on B.ID = C.INVOICEHDRID WHERE B.INVOICE = A.INVOICE AND C.LNNO = A.LNNO AND C.ITEM = A.ITEM ),'0') AS INVOICEHDRID,"
					+"ISNULL((SELECT UNITPRICE FROM "+plant+"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as BASECOST,"
					+"ISNULL((SELECT ITEMDESC FROM "+ plant +"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as ITEMDESC,"
					+"ISNULL((SELECT CATLOGPATH FROM "+plant+"_ITEMMST C WHERE A.ITEM = C.ITEM),'') as CATLOGPATH,"
					+"ISNULL((SELECT ORDERDISCOUNT FROM ["+plant+"_DOHDR] WHERE DONO = A.DONO),'0') AS ORDERDISCOUNT,"
					+"ISNULL((SELECT SUB_TOTAL FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS INVOICESUB_TOTAL,"
					+"ISNULL(( select CURRENCYID from "+plant+"_FININVOICEHDR p where p.INVOICE=A.INVOICE),(select CURRENCYID from "+plant+"_DOHDR p where p.DONO=A.DONO)) as CURRENCYID,"
					+"ISNULL(( select top 1 p.CURRENCYUSEQT from "+plant+"_FININVOICEHDR p join "+plant+"_FININVOICEDET d on p.ID=d.INVOICEHDRID where p.INVOICE=A.INVOICE),(ISNULL((select top 1 p.CURRENCYUSEQT from "+plant+"_DODET p where p.DONO=A.DONO),1))) as CURRENCYUSEQT,"
					+"ISNULL((SELECT DISCOUNT FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS INVOICEDISCOUNT,"
					+"ISNULL((SELECT DISCOUNT_TYPE FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS INVOICEDISCOUNTTYPE,"
					+"ISNULL((SELECT PROJECTID FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS PROJECTID,"
					+"ISNULL((SELECT SHIPPINGID FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS SHIPPINGID,"
					+"ISNULL((SELECT SHIPPINGCUSTOMER FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'') AS SHIPPINGCUSTOMER,"
					+"ISNULL((SELECT PAYMENT_TERMS FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'') AS PAYMENT_TERMS,"
					+"ISNULL((SELECT TAXID FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS TAXID,"
					+"ISNULL((SELECT ORDERDISCOUNTTYPE FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'%') AS ORDERDISCOUNTTYPE,"
					+"ISNULL((SELECT ISDISCOUNTTAX FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS ISDISCOUNTTAX,"
					+"ISNULL((SELECT ISORDERDISCOUNTTAX FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS ISORDERDISCOUNTTAX,"
					+"ISNULL((SELECT OUTBOUD_GST FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS OUTBOUD_GST,"
					+"ISNULL((SELECT SALES_LOCATION FROM ["+plant+"_FININVOICEHDR] WHERE INVOICE = A.INVOICE ),'0') AS SALES_LOCATION,"
					+"ISNULL((SELECT SUM(C.DISCOUNT) FROM ["+plant+"_FININVOICEHDR] AS B left join ["+plant+"_FININVOICEDET] C on B.ID = C.INVOICEHDRID WHERE B.INVOICE = A.INVOICE ),'0') AS INVOICELINEDISCOUNT,"
					+"ISNULL((SELECT C.UNITPRICE FROM ["+plant+"_FININVOICEHDR] AS B left join ["+plant+"_FININVOICEDET] C on B.ID = C.INVOICEHDRID WHERE B.INVOICE = A.INVOICE AND C.LNNO = A.LNNO AND C.ITEM = A.ITEM ),'0') AS UNITPRICE FROM "
					+ "["+plant+"_FINSORETURN] A WHERE PLANT = ? "+condition+"";
		}
		if(connection != null){
		   /*Create  PreparedStatement object*/
		   ps = connection.prepareStatement(sQry);
		    
		   this.mLogger.query(this.printQuery, sQry);
		   returnOrderList = selectData(ps, args);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return returnOrderList;
}

public boolean updateSoReturnDetails(String setquery,String condition, String plant)throws Exception{
	boolean insertCount = false;
	Connection connection = null;
	PreparedStatement ps = null;
	List<String> args = null;
    String query = "";
  try{
	  /*Instantiate the list*/
	    args = new ArrayList<String>();		    
		connection = DbBean.getConnection();			
		
		query = "UPDATE ["+ plant +"_FINSORETURN] SET "+setquery+" WHERE "+condition;
	  /*Create the PreparedStatement object*/
	  if(connection != null){
		  ps = connection.prepareStatement(query);
		  this.mLogger.query(this.printQuery, query);
		/*Execute the Query*/
	     insertCount = execute_NonSelectQuery(ps, args);
	  }		  
  }catch (NamingException e) {
		MLogger.log(0, "" + e.getMessage()+" With values "+args);
		e.printStackTrace();
		throw e;
	} catch (SQLException e) {
		MLogger.log(0, "" + e.getMessage()+" With values "+args);
		e.printStackTrace();
		throw e;
	} catch (Exception e) {
		MLogger.log(0, "" + e.getMessage()+" With values "+args);
		e.printStackTrace();
		throw e;
	} finally {
		if (ps != null && connection != null) {
			DbBean.closeConnection(connection, ps);
		}
	}
	return insertCount;
}


public List<Map<String, String>> getPOReturnDetailsbyVendor(String plant,String query,String extquery) throws Exception {
	Connection connection = null;
	List<Map<String, String>> returnOrderList = null;
	PreparedStatement ps = null;
	List<String> args = null;
	String sQry = "";
	
	try{
		/*Instantiate the list*/
		args = new ArrayList<String>();			
		connection = DbBean.getConnection();
		sQry = "SELECT "+query+" FROM ["+plant+"_FINPORETURN] A WHERE "+extquery+"";
		
		if(connection != null){
		   /*Create  PreparedStatement object*/
		   ps = connection.prepareStatement(sQry);
		    
		   this.mLogger.query(this.printQuery, sQry);
		   returnOrderList = selectData(ps, args);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return returnOrderList;
}

public List<Map<String, String>> getSOReturnDetailsbycustomer(String plant,String query,String extquery) throws Exception {
	Connection connection = null;
	List<Map<String, String>> returnOrderList = null;
	PreparedStatement ps = null;
	List<String> args = null;
	String sQry = "";
	
	try{
		/*Instantiate the list*/
		args = new ArrayList<String>();			
		connection = DbBean.getConnection();
		sQry = "SELECT "+query+" FROM ["+plant+"_FINSORETURN] A WHERE "+extquery+"";
		
		if(connection != null){
		   /*Create  PreparedStatement object*/
		   ps = connection.prepareStatement(sQry);
		    
		   this.mLogger.query(this.printQuery, sQry);
		   returnOrderList = selectData(ps, args);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return returnOrderList;
}

}

