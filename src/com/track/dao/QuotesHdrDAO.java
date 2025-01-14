package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.QuotesHDR;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class QuotesHdrDAO extends BaseDAO {


	public static String TABLE_HEADER = "INVOICEQUOTESHDR";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.QuotesHdrDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.QuotesHdrDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addQuotesHDR(QuotesHDR quotesHDR)  throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ quotesHDR.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[CUSTNO]" + 
					"           ,[QUOTESNO]" +
					"           ,[DONO]" + 
					"           ,[QUOTES_DATE]" +
					"           ,[DUE_DATE]" + 
					"           ,[PAYMENT_TERMS]" +
					"           ,[EMPNO]" + 
					"           ,[ITEM_RATES]" +
					"           ,[DISCOUNT]" + 
					"           ,[DISCOUNT_TYPE]" +
					"           ,[DISCOUNT_ACCOUNT]" + 
					"           ,[SHIPPINGCOST]" +
					"           ,[ADJUSTMENT]" + 
					"           ,[SUB_TOTAL]" +
					"           ,[TOTAL_AMOUNT]" + 
					"           ,[QUOTES_STATUS]" +
					"           ,[NOTE]" + 
					"           ,[TERMSCONDITIONS]" +
					"           ,[ADVANCEFROM]" + 
					"           ,[ISEXPENSE]" +
					"           ,[SALES_LOCATION]" + 
					"           ,[TAXTREATMENT]" +
					"           ,[CREDITNOTESSTATUS]" + 
					"           ,[TAXAMOUNT]" +
					"           ,[TAX_STATUS]" + 
					"           ,[GINO]" +
					"           ,[ORDER_DISCOUNT]" + 
					"           ,[CURRENCYID]" +
					"           ,[CURRENCYTOBASE]" + 
					"           ,[INCOTERMS]" +
					"           ,[SHIPPINGID]" + 
					"           ,[SHIPPINGCUSTOMER]" +
					"           ,[ORIGIN]" + 
					"           ,[DEDUCT_INV]" +
					"           ,[PROJECTID]" + 
					"           ,[CURRENCYUSEQT]" +
					"           ,[ORDERDISCOUNTTYPE]" + 
					"           ,[TAXID]" +
					"           ,[ISDISCOUNTTAX]" + 
					"           ,[ISORDERDISCOUNTTAX]" +
					"           ,[ISSHIPPINGTAX]" + 
					"           ,[OUTBOUD_GST]" +
					"           ,[JobNum]" + 
					"           ,[TRANSPORTID]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, quotesHDR.getPLANT());
				   ps.setString(2, quotesHDR.getCUSTNO());
				   ps.setString(3, quotesHDR.getQUOTESNO());
				   ps.setString(4, quotesHDR.getDONO());
				   ps.setString(5, quotesHDR.getQUOTES_DATE());
				   ps.setString(6, quotesHDR.getDUE_DATE());
				   ps.setString(7, quotesHDR.getPAYMENT_TERMS());
				   ps.setString(8, quotesHDR.getEMPNO());
				   ps.setShort(9, quotesHDR.getITEM_RATES());
				   ps.setDouble(10, quotesHDR.getDISCOUNT());
				   ps.setString(11, quotesHDR.getDISCOUNT_TYPE());
				   ps.setString(12, quotesHDR.getDISCOUNT_ACCOUNT());
				   ps.setDouble(13, quotesHDR.getSHIPPINGCOST());
				   ps.setDouble(14, quotesHDR.getADJUSTMENT());
				   ps.setDouble(15, quotesHDR.getSUB_TOTAL());
				   ps.setDouble(16, quotesHDR.getTOTAL_AMOUNT());
				   ps.setString(17, quotesHDR.getQUOTES_STATUS());
				   ps.setString(18, quotesHDR.getNOTE());
				   ps.setString(19, quotesHDR.getTERMSCONDITIONS());
				   ps.setString(20, quotesHDR.getADVANCEFROM());
				   ps.setShort(21, quotesHDR.getISEXPENSE());
				   ps.setString(22, quotesHDR.getSALES_LOCATION());
				   ps.setString(23, quotesHDR.getTAXTREATMENT());
				   ps.setShort(24, quotesHDR.getCREDITNOTESSTATUS());
				   ps.setDouble(25, quotesHDR.getTAXAMOUNT());
				   ps.setString(26, quotesHDR.getTAX_STATUS());
				   ps.setString(27, quotesHDR.getGINO());
				   ps.setDouble(28, quotesHDR.getORDER_DISCOUNT());
				   ps.setString(29, quotesHDR.getCURRENCYID());
				   ps.setDouble(30, quotesHDR.getCURRENCYTOBASE());
				   ps.setString(31, quotesHDR.getINCOTERMS());
				   ps.setString(32, quotesHDR.getSHIPPINGID());
				   ps.setString(33, quotesHDR.getSHIPPINGCUSTOMER());
				   ps.setString(34, quotesHDR.getORIGIN());
				   ps.setShort(35, quotesHDR.getDEDUCT_INV());
				   ps.setInt(36, quotesHDR.getPROJECTID());
				   ps.setDouble(37, quotesHDR.getCURRENCYUSEQT());
				   ps.setString(38, quotesHDR.getORDERDISCOUNTTYPE());
				   ps.setInt(39, quotesHDR.getTAXID());
				   ps.setShort(40, quotesHDR.getISDISCOUNTTAX());
				   ps.setShort(41, quotesHDR.getISORDERDISCOUNTTAX());
				   ps.setShort(42, quotesHDR.getISSHIPPINGTAX());
				   ps.setString(43, quotesHDR.getOUTBOUD_GST());
				   ps.setDouble(44, quotesHDR.getJobNum());
				   ps.setInt(45, quotesHDR.getTRANSPORTID());
				   ps.setString(46, DateUtils.getDateTime());
				   ps.setString(47, quotesHDR.getCRBY());
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	HdrId = rs.getInt(1);
		                    
		                }
				   }
				   else
				   {
					   throw new SQLException("Creating Quotes failed, no rows affected.");
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
		return HdrId;
	}
	
	public List<QuotesHDR> getAllQuotesHDR(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<QuotesHDR> quotesHDRList=new ArrayList<QuotesHDR>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   QuotesHDR quotesHDR=new QuotesHDR();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, quotesHDR);
	                   quotesHDRList.add(quotesHDR);
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
		return quotesHDRList;
	}
	
	
	public QuotesHDR getQuotesHDRById(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    QuotesHDR quotesHDR=new QuotesHDR();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, quotesHDR);
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
		return quotesHDR;
	}
	
	public int updateQuotesHDR(QuotesHDR quotesHDR, String user)  throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ quotesHDR.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+quotesHDR.getPLANT()+"',"
					+ "CUSTNO='"+quotesHDR.getCUSTNO()+"',"
					+ "QUOTESNO='"+quotesHDR.getQUOTESNO()+"',"
					+ "DONO='"+quotesHDR.getDONO()+"',"
					+ "QUOTES_DATE='"+quotesHDR.getQUOTES_DATE()+"',"
					+ "DUE_DATE='"+quotesHDR.getDUE_DATE()+"',"
					+ "PAYMENT_TERMS='"+quotesHDR.getPAYMENT_TERMS()+"',"
					+ "EMPNO='"+quotesHDR.getEMPNO()+"',"
					+ "ITEM_RATES='"+quotesHDR.getITEM_RATES()+"',"
					+ "DISCOUNT='"+quotesHDR.getDISCOUNT()+"',"
					+ "DISCOUNT_TYPE='"+quotesHDR.getDISCOUNT_TYPE()+"',"
					+ "DISCOUNT_ACCOUNT='"+quotesHDR.getDISCOUNT_ACCOUNT()+"',"
					+ "SHIPPINGCOST='"+quotesHDR.getSHIPPINGCOST()+"',"
					+ "ADJUSTMENT='"+quotesHDR.getADJUSTMENT()+"',"
					+ "SUB_TOTAL='"+quotesHDR.getSUB_TOTAL()+"',"
					+ "TOTAL_AMOUNT='"+quotesHDR.getTOTAL_AMOUNT()+"',"
					+ "QUOTES_STATUS='"+quotesHDR.getQUOTES_STATUS()+"',"
					+ "NOTE='"+quotesHDR.getNOTE()+"',"
					+ "TERMSCONDITIONS='"+quotesHDR.getTERMSCONDITIONS()+"',"
					+ "ADVANCEFROM='"+quotesHDR.getADVANCEFROM()+"',"
					+ "ISEXPENSE='"+quotesHDR.getISEXPENSE()+"',"
					+ "SALES_LOCATION='"+quotesHDR.getSALES_LOCATION()+"',"
					+ "TAXTREATMENT='"+quotesHDR.getTAXTREATMENT()+"',"
					+ "CREDITNOTESSTATUS='"+quotesHDR.getCREDITNOTESSTATUS()+"',"
					+ "TAXAMOUNT='"+quotesHDR.getTAXAMOUNT()+"',"
					+ "TAX_STATUS='"+quotesHDR.getTAX_STATUS()+"',"
					+ "GINO='"+quotesHDR.getGINO()+"',"
					+ "ORDER_DISCOUNT='"+quotesHDR.getORDER_DISCOUNT()+"',"
					+ "CURRENCYID='"+quotesHDR.getCURRENCYID()+"',"
					+ "CURRENCYTOBASE='"+quotesHDR.getCURRENCYTOBASE()+"',"
					+ "INCOTERMS='"+quotesHDR.getINCOTERMS()+"',"
					+ "SHIPPINGID='"+quotesHDR.getSHIPPINGID()+"',"
					+ "SHIPPINGCUSTOMER='"+quotesHDR.getSHIPPINGCUSTOMER()+"',"
					+ "ORIGIN='"+quotesHDR.getORIGIN()+"',"
					+ "DEDUCT_INV='"+quotesHDR.getDEDUCT_INV()+"',"
					+ "PROJECTID='"+quotesHDR.getPROJECTID()+"',"
					+ "CURRENCYUSEQT='"+quotesHDR.getCURRENCYUSEQT()+"',"
					+ "ORDERDISCOUNTTYPE='"+quotesHDR.getORDERDISCOUNTTYPE()+"',"
					+ "TAXID='"+quotesHDR.getTAXID()+"',"
					+ "ISDISCOUNTTAX='"+quotesHDR.getISDISCOUNTTAX()+"',"
					+ "ISORDERDISCOUNTTAX='"+quotesHDR.getISORDERDISCOUNTTAX()+"',"
					+ "ISSHIPPINGTAX='"+quotesHDR.getISSHIPPINGTAX()+"',"
					+ "OUTBOUD_GST='"+quotesHDR.getOUTBOUD_GST()+"',"
					+ "JobNum='"+quotesHDR.getJobNum()+"',"
					+ "='"+quotesHDR.getTRANSPORTID()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',"
					+ "UPBY='"+user+"' WHERE ID="+quotesHDR.getID();
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	HdrId = rs.getInt(1);   
		                }  
				   }
				   else
				   {
					   throw new SQLException("Updating quotes failed, no rows affected.");
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
		return HdrId;
	}
	
	public boolean DeleteQuotesHDR(String plant, int id)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_HEADER+"]"
			                        + " WHERE ID ='"+id+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0) {
			        	deletestatus = true;
			        }
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deletestatus;
 	}
	

	public boolean addInvoiceQuotesAttachments(List<Hashtable<String, String>>  invoiceAttachmentList, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> invoiceDetInfo : invoiceAttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = invoiceDetInfo.keys();
				for (int i = 0; i < invoiceDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) invoiceDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_INVOICEQUOTESATTACHMENTS]  ("
				+ FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
				
				if(connection != null){
					/*Create  PreparedStatement object*/
					ps = connection.prepareStatement(query);
					this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if(flag){
						args.clear();
					}
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return flag;
	}

}
