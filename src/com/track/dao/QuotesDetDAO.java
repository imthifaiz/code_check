package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.QuotesDET;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class QuotesDetDAO {


	public static String TABLE_HEADER = "INVOICEQUOTESDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.QuotesDetDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.QuotesDetDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addQuotesDET(QuotesDET quotesDET) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ quotesDET.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[LNNO]" +
					"           ,[QUOTESHDRID]" + 
					"           ,[ITEM]" +
					"           ,[ACCOUNT_NAME]" + 
					"           ,[QTY]" +
					"           ,[UNITPRICE]" + 
					"           ,[DISCOUNT]" +
					"           ,[TAX_TYPE]" + 
					"           ,[AMOUNT]" +
					"           ,[DISCOUNT_TYPE]" + 
					"           ,[NOTE]" +
					"           ,[BASETOORDERCURRENCY]" + 
					"           ,[LOC]" +
					"           ,[UOM]" + 
					"           ,[BATCH]" +
					"           ,[PACKINGLIST]" + 
					"           ,[DELIVERYNOTE]" +
					"           ,[CURRENCYUSEQT]" + 
					"           ,[IS_COGS_SET]" +
					"           ,[QTYIS]" + 
					"           ,[STATUS]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, quotesDET.getPLANT());
				   ps.setInt(2, quotesDET.getLNNO());
				   ps.setInt(3, quotesDET.getQUOTESHDRID());
				   ps.setString(4, quotesDET.getITEM());
				   ps.setString(5, quotesDET.getACCOUNT_NAME());
				   ps.setDouble(6, quotesDET.getQTY());
				   ps.setDouble(7, quotesDET.getUNITPRICE());
				   ps.setDouble(8, quotesDET.getDISCOUNT());
				   ps.setString(9, quotesDET.getTAX_TYPE());
				   ps.setDouble(10, quotesDET.getAMOUNT());
				   ps.setString(11, quotesDET.getDISCOUNT_TYPE());
				   ps.setString(12, quotesDET.getNOTE());
				   ps.setDouble(13, quotesDET.getBASETOORDERCURRENCY());
				   ps.setString(14, quotesDET.getLOC());
				   ps.setString(15, quotesDET.getUOM());
				   ps.setString(16, quotesDET.getBATCH());
				   ps.setString(17, quotesDET.getPACKINGLIST());
				   ps.setString(18, quotesDET.getDELIVERYNOTE());
				   ps.setDouble(19, quotesDET.getCURRENCYUSEQT());
				   ps.setString(20, quotesDET.getIS_COGS_SET());
				   ps.setDouble(21, quotesDET.getQTYIS());
				   ps.setString(22, quotesDET.getSTATUS());
				   ps.setString(23, DateUtils.getDateTime());
				   ps.setString(24, quotesDET.getCRBY());
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
	
	public List<QuotesDET> getAllQuotesDET(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<QuotesDET> quotesDETList=new ArrayList<QuotesDET>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   QuotesDET quotesDET=new QuotesDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, quotesDET);
	                   quotesDETList.add(quotesDET);
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
		return quotesDETList;
	}
	
	
	public QuotesDET getQuotesDETById(String plant, int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    QuotesDET quotesDET=new QuotesDET();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, quotesDET);
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
		return quotesDET;
	}
	
	public int updateQuotesDET(QuotesDET quotesDET, String user)  throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ quotesDET.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+quotesDET.getPLANT()+"',"
					+ "LNNO='"+quotesDET.getLNNO()+"',"
					+ "QUOTESHDRID='"+quotesDET.getQUOTESHDRID()+"',"
					+ "ITEM='"+quotesDET.getITEM()+"',"
					+ "ACCOUNT_NAME='"+quotesDET.getACCOUNT_NAME()+"',"
					+ "QTY='"+quotesDET.getQTY()+"',"
					+ "UNITPRICE='"+quotesDET.getUNITPRICE()+"',"
					+ "DISCOUNT='"+quotesDET.getDISCOUNT()+"',"
					+ "TAX_TYPE='"+quotesDET.getTAX_TYPE()+"',"
					+ "AMOUNT='"+quotesDET.getAMOUNT()+"',"
					+ "DISCOUNT_TYPE='"+quotesDET.getDISCOUNT_TYPE()+"',"
					+ "NOTE='"+quotesDET.getNOTE()+"',"
					+ "BASETOORDERCURRENCY='"+quotesDET.getBASETOORDERCURRENCY()+"',"
					+ "LOC='"+quotesDET.getLOC()+"',"
					+ "UOM='"+quotesDET.getUOM()+"',"
					+ "BATCH='"+quotesDET.getBATCH()+"',"
					+ "PACKINGLIST='"+quotesDET.getPACKINGLIST()+"',"
					+ "DELIVERYNOTE='"+quotesDET.getDELIVERYNOTE()+"',"
					+ "CURRENCYUSEQT='"+quotesDET.getCURRENCYUSEQT()+"',"
					+ "IS_COGS_SET='"+quotesDET.getIS_COGS_SET()+"',"
					+ "QTYIS='"+quotesDET.getQTYIS()+"',"
					+ "STATUS='"+quotesDET.getSTATUS()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',"
					+ "UPBY='"+user+"' WHERE ID="+quotesDET.getID();
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
	
	public boolean DeleteQuotesDET(String plant, int id)
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
	

	public List<QuotesDET> getAllQuotesDETByQID(String plant, int QUOTESHDRID) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<QuotesDET> quotesDETList=new ArrayList<QuotesDET>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"]  WHERE QUOTESHDRID='"+QUOTESHDRID+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   QuotesDET quotesDET=new QuotesDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, quotesDET);
	                   quotesDETList.add(quotesDET);
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
		return quotesDETList;
	}

}
