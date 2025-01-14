package com.track.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.object.DBTable;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.InvPaymentHeader;
import com.track.db.object.TaxReturnFillAdjust;
import com.track.db.object.TaxReturnFillDet;
import com.track.db.object.TaxReturnFillHdr;
import com.track.db.object.TaxReturnPaymentDet;
import com.track.db.object.TaxReturnPaymentHdr;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;


public class TaxReturnFilingDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TaxSettingDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TaxSettingDAO_PRINTPLANTMASTERLOG;
	
	private String TaxReturnHeader="FINTAXRETURNFILLHDR";
	private String TaxReturnDet="FINTAXRETURNFILLDET";
	private String TaxReturnAdjust="FINTAXFILEADJUSTMENT";
	private String TaxReturnPayment="FINTAXRETURNFILLHDR";
	private String TaxReturnPaymentDet="FINTAXRETURNPAYMENT";
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
	
	public int addTaxSetting(Hashtable ht, String plant,boolean isEdit,String taxid) throws Exception
	{
		boolean flag = false;
		int addTaxsettings = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "",SETQUERY=" ";
		try {
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
				if(i<ht.size()-1)
				{
					SETQUERY+=key+"='"+value+"',";
				}
				else
				{
					SETQUERY+=key+"='"+value+"'";
				}
			}
			if(isEdit)
			{
				query = "UPDATE ["+ plant +"_FINTAXSETTINGS] SET "+SETQUERY+" WHERE ID="+taxid;

				if(connection != null){
					  /*Create  PreparedStatement object*/
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   
				this.mLogger.query(this.printQuery, query);
				addTaxsettings = ps.executeUpdate();
				}
			}
			else
			{
				query = "INSERT INTO ["+ plant +"_FINTAXSETTINGS] ("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";

				if(connection != null){
					  /*Create  PreparedStatement object*/
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   
				this.mLogger.query(this.printQuery, query);
				addTaxsettings = execute_NonSelectQueryGetLastInsert(ps, args);
				}
			
			}
			query = "UPDATE [PLNTMST] SET RCBNO='"+ht.get("TAX")+"',TAXBYLABEL='"+ht.get("TAXBYLABEL")+"' WHERE PLANT='"+plant+"'";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			addTaxsettings = ps.executeUpdate();
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return addTaxsettings;
	}
	
	public List<Map<String, String>> getTaxSalesFiling(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINTAXRETURNFILLCONFIG] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND MODULE='SALES'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	
	public List<Map<String, String>> getTaxFilingForSG(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINTAXRETURNFILLCONFIG] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND MODULE='GST' AND COUNTRY_CODE='"+ht.get("COUNTRY_CODE")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	public List<Map<String, String>> getTaxExpenseFiling(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINTAXRETURNFILLCONFIG] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND MODULE='EXPENSE'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	public List<Map<String, String>> getTaxDueFiling(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINTAXRETURNFILLCONFIG] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND MODULE='DUE'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	public Map<String,String> getTaxSetting(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		Map<String, String> map = null;
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM ["+ht.get("PLANT")+"_FINTAXSETTINGS] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			map = getRowOfData(con, query);		
			
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
	public Map<String,String> getPlantMst(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		Map<String, String> map = null;
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM [PLNTMST] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			map = getRowOfData(con, query);		
			
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
	
	
	public List<Map<String, String>> getSalesInvoice(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FININVOICEHDR] a left join " + "[" + ht.get("PLANT")+"_FININVOICEDET] b on b.INVOICEHDRID=a.ID "
					+ "WHERE a.PLANT ='"+ht.get("PLANT")+"' AND CAST((SUBSTRING(a.INVOICE_DATE, 7, 4) + SUBSTRING(a.INVOICE_DATE, 4, 2) + SUBSTRING(a.INVOICE_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	
	public List<Map<String, String>> getSalesInvoiceForTaxFill(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT A.INVOICE_DATE,B.TAXBOX,A.TAXAMOUNT,A.INVOICE,A.SUB_TOTAL,A.ORDER_DISCOUNT,A.ORDERDISCOUNTTYPE,A.ISORDERDISCOUNTTAX,A.DISCOUNT,A.DISCOUNT_TYPE,"
					+ "A.ISDISCOUNTTAX,A.SHIPPINGCOST,A.ISSHIPPINGTAX FROM "+ht.get("PLANT")+"_FININVOICEHDR AS A LEFT JOIN FINCOUNTRYTAXTYPE AS B ON B.ID = A.TAXID "
					+ "WHERE A.PLANT ='"+ht.get("PLANT")+"' AND CAST((SUBSTRING(A.INVOICE_DATE, 7, 4) + SUBSTRING(A.INVOICE_DATE, 4, 2) + SUBSTRING(A.INVOICE_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	
	public List<Map<String, String>> getPurchaseBillForTaxFill(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT A.BILL_DATE,B.TAXBOX,A.TAXAMOUNT,A.BILL,A.SUB_TOTAL,A.ORDER_DISCOUNT,A.ORDERDISCOUNTTYPE,A.ISORDERDISCOUNTTAX,A.DISCOUNT,A.DISCOUNT_TYPE,"
					+ "A.ISDISCOUNTTAX,A.SHIPPINGCOST,A.ISSHIPPINGTAXABLE FROM "+ht.get("PLANT")+"_FINBILLHDR AS A LEFT JOIN FINCOUNTRYTAXTYPE AS B ON B.ID = A.TAXID "
					+ "WHERE A.PLANT ='"+ht.get("PLANT")+"' AND B.TAXBOX = '5' AND CAST((SUBSTRING(A.BILL_DATE, 7, 4) + SUBSTRING(A.BILL_DATE, 4, 2) + SUBSTRING(A.BILL_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	
	public List<Map<String, String>> getExpenseForTaxFill(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT A.EXPENSES_DATE,B.TAXBOX,A.TAXAMOUNT,A.ID,A.SUB_TOTAL FROM "+ht.get("PLANT")+"_FINEXPENSESHDR AS A LEFT JOIN FINCOUNTRYTAXTYPE AS B ON B.ID = A.TAXID "
					+ "WHERE A.PLANT ='"+ht.get("PLANT")+"' AND B.TAXBOX = '5' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + SUBSTRING(A.EXPENSES_DATE, 4, 2) + SUBSTRING(A.EXPENSES_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	
	
	
	public List<Map<String, String>> getSalesInvoiceNotTaxGen(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FININVOICEHDR] a left join " + "[" + ht.get("PLANT") +"_FININVOICEDET] b on b.INVOICEHDRID=a.ID "
					+ "WHERE a.PLANT ='"+ht.get("PLANT")+"' AND ISNULL(a.TAX_STATUS,'') NOT IN('Tax Generated') AND CAST((SUBSTRING(a.INVOICE_DATE, 7, 4) + SUBSTRING(a.INVOICE_DATE, 4, 2) + SUBSTRING(a.INVOICE_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	public List<Map<String, String>> getBillBYTaxTreamentType(Hashtable ht,String cond,String extracond) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINBILLHDR] a "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND CAST((SUBSTRING(a.BILL_DATE, 7, 4) + SUBSTRING(a.BILL_DATE, 4, 2) + SUBSTRING(a.BILL_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			if(!cond.isEmpty())
			{
				query=query+" AND "+cond;
			}
			if(!extracond.isEmpty())
			{
				query=query+" AND "+extracond;
			}
			
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	public List<Map<String, String>> getBillBYTaxTreamentTypeNotTaxGen(Hashtable ht,String cond,String extracond) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINBILLHDR] a "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND ISNULL(a.TAX_STATUS,'') NOT IN('Tax Generated') AND CAST((SUBSTRING(a.BILL_DATE, 7, 4) + SUBSTRING(a.BILL_DATE, 4, 2) + SUBSTRING(a.BILL_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			if(!cond.isEmpty())
			{
				query=query+" AND "+cond;
			}
			if(!extracond.isEmpty())
			{
				query=query+" AND "+extracond;
			}
			
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	public List<Map<String, String>> getExpenseBYTaxDateFilter(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINEXPENSESHDR] a "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND CAST((SUBSTRING(a.EXPENSES_DATE, 7, 4) + SUBSTRING(a.EXPENSES_DATE, 4, 2) + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	public List<Map<String, String>> getExpenseBYTaxDateFilterNotTaxGen(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINEXPENSESHDR] a "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND ISNULL(a.TAX_STATUS,'') NOT IN('Tax Generated') AND CAST((SUBSTRING(a.EXPENSES_DATE, 7, 4) + SUBSTRING(a.EXPENSES_DATE, 4, 2) + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date) "
					+"BETWEEN '"+ht.get("FROM")+"' AND '"+ht.get("TO")+"'";
			
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	public Map<String,String> getPo(Hashtable ht,String pono) throws Exception {
		java.sql.Connection con = null;
		Map<String, String> poRow;
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_POHDR] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND PONO='"+pono+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			//map = getRowOfData(con, query);	
			poRow = getRowOfData(con, query);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return poRow;
	}
	public List<Map<String,String>> IsTaxRegistered(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ISTAXREGISTRED FROM [PLNTMST] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			//map = getRowOfData(con, query);	
			taxReturnFilingList = selectData(con, query.toString());
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	
	public int addTaxReturnHdr(TaxReturnFillHdr taxReturnHdr)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ taxReturnHdr.getPLANT() +"_"+TaxReturnHeader+"]([PLANT]" + 
					"           ,[COUNTRY_CODE]" + 
					"           ,[STATUS]" + 
					"           ,[FROM_DATE]" + 
					"           ,[TO_DATE]" + 
					"           ,[REPORTING_PERIOD]" + 
					"           ,[FILED_ON]" +
					"           ,[PAYMENTDUE_ON]" +
					"           ,[TAX_BASIS]" +
					"           ,[TOTAL_TAXPAYABLE]" +
					"           ,[TOTAL_TAXRECLAIMABLE]" +
					"           ,[BALANCEDUE]" +
					"           ,[TAXPREVIOUSINCLUDE]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";


			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, taxReturnHdr.getPLANT());
				   ps.setString(2, taxReturnHdr.getCOUNTRY_CODE());
				   ps.setString(3, taxReturnHdr.getSTATUS());
				   ps.setString(4, taxReturnHdr.getFROM_DATE());
				   ps.setString(5, taxReturnHdr.getTO_DATE());
				   ps.setString(6, taxReturnHdr.getREPORTING_PERIOD());
				   ps.setString(7, taxReturnHdr.getFILED_ON());
				   ps.setString(8, taxReturnHdr.getPAYMENTDUE_ON());
				   ps.setString(9, taxReturnHdr.getTAX_BASIS());
				   ps.setDouble(10, taxReturnHdr.getTOTAL_TAXPAYABLE());
				   ps.setDouble(11, taxReturnHdr.getTOTAL_TAXRECLAIMABLE());
				   ps.setDouble(12, taxReturnHdr.getBALANCEDUE());
				   ps.setBoolean(13, taxReturnHdr.isTAXPREVIOUSINCLUDED());
				  
				   ps.executeUpdate();
				   ResultSet rs = ps.getGeneratedKeys();
				   if(rs.next())
	                {
					   invoiceHdrId = rs.getInt(1);
	                }
				   else
				   {
					   throw new SQLException("Unable to generate tax return.Please contact admin");
				   }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return invoiceHdrId;
	}
	
	public int addTaxReturnDet(TaxReturnFillDet taxReturnDet)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ taxReturnDet.getPLANT() +"_"+TaxReturnDet+"]([PLANT]" + 
					"           ,[COUNTRY_CODE]" + 
					"           ,[DATE]" + 
					"           ,[TRANSACTION_TYPE]" + 
					"           ,[TAXHDR_ID]" + 
					"           ,[TRANSACTION_ID]" + 
					"           ,[BOX]" + 
					"           ,[TAXABLE_AMOUNT]" + 
					"           ,[TAX_AMOUNT]" +
					"           ,[ADJUSTMENTS]" +
					"           ,[ISTAXPREVIOUS]) VALUES (?,?,?,?,?,?,?,?,?,?,?)";


			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, taxReturnDet.getPLANT());
				   ps.setString(2, taxReturnDet.getCOUNTRY_CODE());
				   ps.setString(3, taxReturnDet.getDATE());
				   ps.setString(4, taxReturnDet.getTRANSACTION_TYPE());
				   ps.setInt(5, taxReturnDet.getTAXHDR_ID());
				   ps.setString(6, taxReturnDet.getTRANSACTION_ID());
				   ps.setString(7, taxReturnDet.getBOX());
				   ps.setDouble(8, taxReturnDet.getTAXABLE_AMOUNT());
				   ps.setDouble(9, taxReturnDet.getTAX_AMOUNT());
				   ps.setDouble(10, taxReturnDet.getADJUSTMENTS());
				   ps.setBoolean(11, taxReturnDet.isISTAXPREVIOUS());
				   ps.executeUpdate();
				   ResultSet rs = ps.getGeneratedKeys();
				   if(rs.next())
	                {
					   invoiceHdrId = rs.getInt(1);
	                }
				   else
				   {
					   throw new SQLException("Unable to generate tax return.Please contact admin");
				   }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return invoiceHdrId;
	}
	public List<TaxReturnFillHdr> getAllTaxReturnHdr(String plant)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillHdr> taxReturnHdrList=new ArrayList<TaxReturnFillHdr>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnHeader+"] ORDER BY ID DESC";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnFillHdr taxReturnHeader=new TaxReturnFillHdr();
	                    loadResultSetIntoObject(rst, taxReturnHeader);
	                    taxReturnHdrList.add(taxReturnHeader);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnHdrList;
	}
	public TaxReturnFillHdr getTaxReturnHdrById(String plant,String headerid)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillHdr> taxReturnHdrList=new ArrayList<TaxReturnFillHdr>();
	    TaxReturnFillHdr taxReturnHeader=new TaxReturnFillHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnHeader+"] WHERE ID='"+headerid+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   
	                    loadResultSetIntoObject(rst, taxReturnHeader);
	                    //taxReturnHdrList.add(taxReturnHeader);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnHeader;
	}
	public List<TaxReturnFillDet> getAllTaxReturnDet(String plant,String headerid)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillDet> taxReturnDetList=new ArrayList<TaxReturnFillDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnDet+"] WHERE PLANT='"+plant+"' AND TAXHDR_ID='"+headerid+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnFillDet taxReturnDet=new TaxReturnFillDet();
	                    loadResultSetIntoObject(rst, taxReturnDet);
	                    taxReturnDetList.add(taxReturnDet);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnDetList;
	}
	
	public List<TaxReturnFillDet> getAllTaxReturnDetSummary(String plant,String fromdate,String todate)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillDet> taxReturnDetList=new ArrayList<TaxReturnFillDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnDet+"] WHERE PLANT='"+plant+"' AND CAST((SUBSTRING(DATE, 7, 4) + SUBSTRING(DATE, 4, 2) + SUBSTRING(DATE, 1, 2)) AS date) BETWEEN '"+fromdate+"' AND '"+todate+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnFillDet taxReturnDet=new TaxReturnFillDet();
	                    loadResultSetIntoObject(rst, taxReturnDet);
	                    taxReturnDetList.add(taxReturnDet);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnDetList;
	}
	
	public List<TaxReturnFillDet> getAllTaxReturnDetByBox(String plant,String headerid,String box)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillDet> taxReturnDetList=new ArrayList<TaxReturnFillDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnDet+"] WHERE PLANT='"+plant+"' AND TAXHDR_ID='"+headerid+"' AND BOX='"+box+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnFillDet taxReturnDet=new TaxReturnFillDet();
	                    loadResultSetIntoObject(rst, taxReturnDet);
	                    taxReturnDetList.add(taxReturnDet);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnDetList;
	}
	public List<TaxReturnFillDet> getAllTaxReturnDetByBoxes(String plant,String headerid,String boxesquery)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillDet> taxReturnDetList=new ArrayList<TaxReturnFillDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnDet+"] WHERE PLANT='"+plant+"' AND TAXHDR_ID='"+headerid+"'";
			if(!boxesquery.isEmpty())
			{
				query+=" AND "+boxesquery;
			}

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnFillDet taxReturnDet=new TaxReturnFillDet();
	                    loadResultSetIntoObject(rst, taxReturnDet);
	                    taxReturnDetList.add(taxReturnDet);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnDetList;
	}
	public List<TaxReturnFillDet> getAllPreviousTaxReturnDet(String plant,String headerid)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillDet> taxReturnDetList=new ArrayList<TaxReturnFillDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnDet+"] WHERE PLANT='"+plant+"' AND ISTAXPREVIOUS=1 AND TAXHDR_ID='"+headerid+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnFillDet taxReturnDet=new TaxReturnFillDet();
	                    loadResultSetIntoObject(rst, taxReturnDet);
	                    taxReturnDetList.add(taxReturnDet);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnDetList;
	}
	public List<TaxReturnFillAdjust> getAllTaxReturnDetAdjust(String plant,String headerid)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillAdjust> taxReturnDetAdjustList=new ArrayList<TaxReturnFillAdjust>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnAdjust+"] WHERE PLANT='"+plant+"' AND TAXHDR_ID='"+headerid+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnFillAdjust taxReturnDetAdj=new TaxReturnFillAdjust();
	                    loadResultSetIntoObject(rst, taxReturnDetAdj);
	                    taxReturnDetAdjustList.add(taxReturnDetAdj);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnDetAdjustList;
	}
	public List<TaxReturnFillHdr> getAllTaxReturnPaymentsHdr(String plant)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnFillHdr> taxReturnPaymentList=new ArrayList<TaxReturnFillHdr>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnPayment+"] WHERE PLANT='"+plant+"' AND STATUS='Filed' AND BALANCEDUE>0";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnFillHdr taxReturnPayment=new TaxReturnFillHdr();
	                    loadResultSetIntoObject(rst, taxReturnPayment);
	                    taxReturnPaymentList.add(taxReturnPayment);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnPaymentList;
	}
	public List<TaxReturnPaymentDet> getAllTaxReturnPaymentsDet(String plant)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<TaxReturnPaymentDet> taxReturnPaymentDetList=new ArrayList<TaxReturnPaymentDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TaxReturnPaymentDet+"] WHERE PLANT='"+plant+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   TaxReturnPaymentDet taxReturnPaymentDet=new TaxReturnPaymentDet();
	                    loadResultSetIntoObject(rst, taxReturnPaymentDet);
	                    taxReturnPaymentDetList.add(taxReturnPaymentDet);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return taxReturnPaymentDetList;
	}
	public int addTaxReturnPaymentDet(TaxReturnPaymentDet taxReturnDet)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ taxReturnDet.getPLANT() +"_"+TaxReturnPaymentDet+"]([PLANT]" + 
					"           ,[PAYMENTHDR_ID]" + 
					"           ,[TAX_RETURN]" + 
					"           ,[DATE]" + 
					"           ,[PAID_THROUGH]" + 
					"           ,[AMOUNT_PAID]" + 
					"           ,[AMOUNT_RECLAIMED]" + 
					"           ,[REFERENCE]" + 
					"           ,[DESCRIPTION]" +
					"          ) VALUES (?,?,?,?,?,?,?,?,?)";


			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, taxReturnDet.getPLANT());
				   ps.setInt(2, taxReturnDet.getPAYMENTHDR_ID());
				   ps.setString(3, taxReturnDet.getTAX_RETURN());
				   ps.setString(4, taxReturnDet.getDATE());
				   ps.setString(5, taxReturnDet.getPAID_THROUGH());
				   ps.setDouble(6, taxReturnDet.getAMOUNT_PAID());
				   ps.setDouble(7, taxReturnDet.getAMOUNT_RECLAIMED());
				   ps.setString(8, taxReturnDet.getREFERENCE());
				   ps.setString(9, taxReturnDet.getDESCRIPTION());
				  
				   ps.executeUpdate();
				   ResultSet rs = ps.getGeneratedKeys();
				   if(rs.next())
	                {
					   invoiceHdrId = rs.getInt(1);
	                }
				   else
				   {
					   throw new SQLException("Unable to pay tax return.Please contact admin");
				   }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return invoiceHdrId;
	}
	
	public int getMaxId(String plant) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		int maxid=0;
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query = "SELECT MAX(ID) FROM ["+ plant +"_"+TaxReturnHeader+"]";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			//map = getRowOfData(con, query);	
			if(con != null){
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   maxid=rst.getInt(1);
				   }
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return maxid;
	}
	public String getTODate(String plant,int id) throws Exception {
		java.sql.Connection con = null;
		String todate = null;
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query = "SELECT TO_DATE FROM ["+ plant +"_"+TaxReturnHeader+"] WHERE PLANT='"+plant+"' AND ID="+id;
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			//map = getRowOfData(con, query);	
			if(con != null){
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   todate=rst.getString(1);
				   }
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return todate;
	}
	public int updateTaxBalanceDue(String plant,String headerid,String balancedue) throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					query = "UPDATE ["+ plant +"_FINTAXRETURNFILLHDR] SET BALANCEDUE='"+balancedue+"' WHERE ID="+headerid;

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					updatecount = ps.executeUpdate();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}
	public int updateTaxHeaderStatus(String plant,String headerid,String status) throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					query = "UPDATE ["+ plant +"_FINTAXRETURNFILLHDR] SET STATUS='"+status+"' WHERE ID="+headerid;

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					updatecount = ps.executeUpdate();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}
	public int updateTaxHeader(String plant,String headerid,String updateitems) throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					query = "UPDATE ["+ plant +"_FINTAXRETURNFILLHDR]";
					
					if(!updateitems.isEmpty())
						query+=" SET "+updateitems;
					
					query+=" WHERE ID="+headerid;

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					updatecount = ps.executeUpdate();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}
	public int deletePaymentById(String plant,String headerid) throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					query = "DELETE FROM ["+plant+"_"+TaxReturnPaymentDet+"] WHERE ID="+headerid;

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					updatecount = ps.executeUpdate();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}
	public int deleteTaxDetById(String plant,String taxheaderid) throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					query = "DELETE FROM ["+plant+"_"+TaxReturnDet+"] WHERE TAXHDR_ID="+taxheaderid;

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					updatecount = ps.executeUpdate();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}
	public int deletePreviousTaxDetById(String plant,String taxheaderid) throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					query = "DELETE FROM ["+plant+"_"+TaxReturnDet+"] WHERE ISTAXPREVIOUS=1 AND TAXHDR_ID="+taxheaderid;

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					updatecount = ps.executeUpdate();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}
	public int updateTaxItemStatus(String plant,String taxtype,String headerid,String status) throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					if(taxtype.equalsIgnoreCase("Invoice"))
					{
						query = "UPDATE ["+ plant +"_FININVOICEHDR] SET TAX_STATUS='"+status+"' WHERE INVOICE='"+headerid+"'";
					}
					else if(taxtype.equalsIgnoreCase("Bill"))
					{
						query = "UPDATE ["+ plant +"_FINBILLHDR] SET TAX_STATUS='"+status+"' WHERE BILL='"+headerid+"'";
					}
					else if(taxtype.equalsIgnoreCase("Expense"))
					{
						query = "UPDATE ["+ plant +"_FINEXPENSESHDR] SET TAX_STATUS='"+status+"' WHERE ID='"+headerid+"'";
					}
					

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					updatecount = ps.executeUpdate();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}
	
	
	public List<Map<String, String>> getDetailTaxFilingReportForSG(String plant,String fromdate,String todate) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnFilingList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT A.BOX,A.DATE,A.TRANSACTION_TYPE,A.TRANSACTION_ID,A.TAXABLE_AMOUNT,A.TAX_AMOUNT,C.DESC_HEADING,\r\n" + 
					"CASE    \r\n" + 
					"WHEN A.TRANSACTION_TYPE = 'Invoice' THEN ISNULL((SELECT IVC.CNAME FROM "+plant+"_FININVOICEHDR AS IV LEFT JOIN "+plant+"_CUSTMST AS IVC ON IVC.CUSTNO = IV.CUSTNO WHERE IV.INVOICE = A.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN A.TRANSACTION_TYPE = 'Bill' THEN ISNULL((SELECT VM.VNAME FROM "+plant+"_FINBILLHDR AS BI LEFT JOIN "+plant+"_VENDMST AS VM ON BI.VENDNO = VM.VENDNO WHERE BI.BILL = A.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN A.TRANSACTION_TYPE = 'Expense' THEN ''\r\n" + 
					"WHEN A.TRANSACTION_TYPE = 'GST Adjustments' THEN ''\r\n" + 
					"ELSE ''END AS NAME,\r\n" + 
					"CASE    \r\n" + 
					"WHEN A.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT IV.TAXID FROM "+plant+"_FININVOICEHDR AS IV  WHERE INVOICE = A.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN A.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT BI.TAXID FROM "+plant+"_FINBILLHDR AS BI WHERE BI.BILL = A.TRANSACTION_ID),'-')\r\n" + 
					"WHEN A.TRANSACTION_TYPE = 'EXPENSE' THEN ISNULL((SELECT ISNULL(EX.TAXID,'') FROM "+plant+"_FINEXPENSESHDR AS EX WHERE EX.ID = A.TRANSACTION_ID),'-')       \r\n" + 
					"WHEN A.TRANSACTION_TYPE = 'GST Adjustments' THEN ISNULL((SELECT ISNULL(AD.TAXID,'') FROM "+plant+"_FINTAXFILEADJUSTMENT AS AD WHERE AD.ID = A.TRANSACTION_ID),'-')       \r\n" + 
					"ELSE ''END AS TAXID\r\n" + 
					"FROM ["+plant+"_FINTAXRETURNFILLDET] as A JOIN "+plant+"_FINTAXRETURNFILLHDR AS B ON B.ID=A.TAXHDR_ID LEFT JOIN "+plant+"_FINTAXRETURNFILLCONFIG AS C ON C.BOX = A.BOX \r\n" + 
					"AND C.COUNTRY_CODE = B.COUNTRY_CODE AND C.MODULE='GST' WHERE A.PLANT='TEST' \r\n" + 
					"AND CAST((SUBSTRING(A.DATE, 7, 4) + SUBSTRING(A.DATE, 4, 2) + SUBSTRING(A.DATE, 1, 2)) AS date) BETWEEN '"+fromdate+"' \r\n" + 
					"AND '"+todate+"' AND A.BOX IN ('1','2','3','5','6','7') ORDER BY A.BOX ASC";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnFilingList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnFilingList;
	}
	
	
	
	
	public static void loadResultSetIntoObject(ResultSet rst, Object object)
	        throws IllegalArgumentException, IllegalAccessException, SQLException {
	    Class<?> zclass = object.getClass();
	    for (Field field : zclass.getDeclaredFields()) {
	        field.setAccessible(true);
	        DBTable column = field.getAnnotation(DBTable.class);
	        Object value = rst.getObject(column.columnName());
	        Class<?> type = field.getType();
	        if (isPrimitive(type)) {//check primitive type(Point 5)
	            Class<?> boxed = boxPrimitiveClass(type);//box if primitive(Point 6)
	            value = boxed.cast(value);
	        }
	        field.set(object, value);
	    }
	}

	public static boolean isPrimitive(Class<?> type) {
	    return (type == int.class || type == long.class || type == double.class || type == float.class
	            || type == boolean.class || type == byte.class || type == char.class || type == short.class);
	}

	public static Class<?> boxPrimitiveClass(Class<?> type) {
	    if (type == int.class) {
	        return Integer.class;
	    } else if (type == long.class) {
	        return Long.class;
	    } else if (type == double.class) {
	        return Double.class;
	    } else if (type == float.class) {
	        return Float.class;
	    } else if (type == boolean.class) {
	        return Boolean.class;
	    } else if (type == byte.class) {
	        return Byte.class;
	    } else if (type == char.class) {
	        return Character.class;
	    } else if (type == short.class) {
	        return Short.class;
	    } else {
	        String string = "class '" + type.getName() + "' is not a primitive";
	        throw new IllegalArgumentException(string);
	    }
	}
}