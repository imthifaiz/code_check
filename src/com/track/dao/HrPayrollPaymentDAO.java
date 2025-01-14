package com.track.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.DBTable;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.HrPayrollPaymentAttachment;
import com.track.db.object.HrPayrollPaymentDet;
import com.track.db.object.HrPayrollPaymentHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrPayrollPaymentDAO  extends BaseDAO {
	
	public static String TABLE_HEADER = "HRPAYROLLPAYMENTHDR";
	public static String TABLE_DETAILS = "HRPAYROLLPAYMENTDET";
	public static String TABLE_ATTACHMENTS = "HRPAYROLLPAYMENTATTACHMENTS";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrPayrollPaymentDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrPayrollPaymentDAO_PRINTPLANTMASTERLOG;
	
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
	
	
	/*-------------- HEADER -------------- */
	
	public int addHDR(HrPayrollPaymentHdr hrPayrollPaymentHdr) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ hrPayrollPaymentHdr.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					",[AMOUNTPAID]" + 
					",[PAYMENT_DATE]" + 
					",[PAYMENT_MODE]" + 
					",[PAID_THROUGH]" + 
					",[REFERENCE]" + 
					",[BANK_BRANCH]" + 
					",[AMOUNTUFP]" + 
					",[AMOUNTREFUNDED]" + 
					",[BANK_CHARGE]" + 
					",[CHEQUE_NO]" + 
					",[PAYMENT_TYPE]" + 
					",[CHEQUE_AMOUNT]" + 
					",[ACCOUNT_NAME]" + 
					",[CHEQUE_DATE]" + 
					",[ISPDCPROCESS]" + 
					",[CURRENCYID]" + 
					",[NOTE]" + 
					",[CRAT]" + 
					",[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, hrPayrollPaymentHdr.getPLANT());
				   ps.setDouble(2, hrPayrollPaymentHdr.getAMOUNTPAID());
				   ps.setString(3, hrPayrollPaymentHdr.getPAYMENT_DATE());
				   ps.setString(4, hrPayrollPaymentHdr.getPAYMENT_MODE());
				   ps.setString(5, hrPayrollPaymentHdr.getPAID_THROUGH());
				   ps.setString(6, hrPayrollPaymentHdr.getREFERENCE());
				   ps.setString(7, hrPayrollPaymentHdr.getBANK_BRANCH());
				   ps.setDouble(8, hrPayrollPaymentHdr.getAMOUNTUFP());
				   ps.setDouble(9, hrPayrollPaymentHdr.getAMOUNTREFUNDED());
				   ps.setDouble(10, hrPayrollPaymentHdr.getBANK_CHARGE());
				   ps.setString(11, hrPayrollPaymentHdr.getCHEQUE_NO());
				   ps.setString(12, hrPayrollPaymentHdr.getPAYMENT_TYPE());
				   ps.setDouble(13, hrPayrollPaymentHdr.getCHEQUE_AMOUNT());
				   ps.setString(14, hrPayrollPaymentHdr.getACCOUNT_NAME());
				   ps.setString(15, hrPayrollPaymentHdr.getCHEQUE_DATE());
				   ps.setShort(16, hrPayrollPaymentHdr.getISPDCPROCESS());
				   ps.setString(17, hrPayrollPaymentHdr.getCURRENCYID());
				   ps.setString(18, hrPayrollPaymentHdr.getNOTE());
				   ps.setString(19, DateUtils.getDateTime());
				   ps.setString(20, hrPayrollPaymentHdr.getCRBY());
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
					   throw new SQLException("Creating payroll payment header failed, no rows affected.");
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
	
	
	public int updateHDR(HrPayrollPaymentHdr hrPayrollPaymentHdr, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ hrPayrollPaymentHdr.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+hrPayrollPaymentHdr.getPLANT()+"',"
					+ "AMOUNTPAID='"+hrPayrollPaymentHdr.getAMOUNTPAID()+"',"
					+ "PAYMENT_DATE='"+hrPayrollPaymentHdr.getPAYMENT_DATE()+"',"
					+ "PAYMENT_MODE='"+hrPayrollPaymentHdr.getPAYMENT_MODE()+"',"
					+ "PAID_THROUGH='"+hrPayrollPaymentHdr.getPAID_THROUGH()+"',"
					+ "REFERENCE='"+hrPayrollPaymentHdr.getREFERENCE()+"',"
					+ "BANK_BRANCH='"+hrPayrollPaymentHdr.getBANK_BRANCH()+"',"
					+ "AMOUNTUFP='"+hrPayrollPaymentHdr.getAMOUNTUFP()+"',"
					+ "AMOUNTREFUNDED='"+hrPayrollPaymentHdr.getAMOUNTREFUNDED()+"',"
					+ "BANK_CHARGE='"+hrPayrollPaymentHdr.getBANK_CHARGE()+"',"
					+ "CHEQUE_NO='"+hrPayrollPaymentHdr.getCHEQUE_NO()+"',"
					+ "PAYMENT_TYPE='"+hrPayrollPaymentHdr.getPAYMENT_TYPE()+"',"
					+ "CHEQUE_AMOUNT='"+hrPayrollPaymentHdr.getCHEQUE_AMOUNT()+"',"
					+ "ACCOUNT_NAME='"+hrPayrollPaymentHdr.getACCOUNT_NAME()+"',"
					+ "CHEQUE_DATE='"+hrPayrollPaymentHdr.getCHEQUE_DATE()+"',"
					+ "ISPDCPROCESS='"+hrPayrollPaymentHdr.getISPDCPROCESS()+"',"
					+ "CURRENCYID='"+hrPayrollPaymentHdr.getCURRENCYID()+"',"
					+ "NOTE='"+hrPayrollPaymentHdr.getNOTE()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',"
					+ "UPBY='"+user+"' WHERE ID="+hrPayrollPaymentHdr.getID();
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
					   throw new SQLException("Updating payroll payment header failed, no rows affected.");
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
	

	public boolean DeleteHDR(String plant, int id)
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
	
	public List<HrPayrollPaymentHdr> getAllHrPayrollPaymentHdr(String plant,String type) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollPaymentHdr> HrPayrollPaymentHdrlist=new ArrayList<HrPayrollPaymentHdr>();
	    
		try {	    
			connection = DbBean.getConnection();
			ArrayList al = new ArrayList();
			query = "SELECT PLANT,ID,AMOUNTPAID,PAYMENT_DATE,PAYMENT_MODE,PAID_THROUGH," + 
					"REFERENCE,BANK_BRANCH,AMOUNTUFP,AMOUNTREFUNDED,BANK_CHARGE," + 
					"CHEQUE_NO,PAYMENT_TYPE,CHEQUE_AMOUNT,ACCOUNT_NAME,CHEQUE_DATE," + 
					"ISPDCPROCESS,CURRENCYID,NOTE,CRAT,CRBY,UPAT,UPBY FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PAYMENT_TYPE = '"+type+"' ORDER BY ID DESC";
			/*
			 * al = selectData(connection, query); System.out.println(al.toString());
			 */
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollPaymentHdr hrPayrollPaymentHdr=new HrPayrollPaymentHdr();
					   loadResultSetIntoObject(rst, hrPayrollPaymentHdr);
					   System.out.println(hrPayrollPaymentHdr);
	                   HrPayrollPaymentHdrlist.add(hrPayrollPaymentHdr);
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
		return HrPayrollPaymentHdrlist;
	}
	
	public HrPayrollPaymentHdr getHrPayrollPaymentHdrId(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrPayrollPaymentHdr hrPayrollPaymentHdr=new HrPayrollPaymentHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollPaymentHdr);
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
		return hrPayrollPaymentHdr;
	}


	/*-------------- DETAILS -------------- */
	


	public int addDET(HrPayrollPaymentDet hrPayrollPaymentDet) throws Exception
	{
		boolean flag = false;
		int detId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ hrPayrollPaymentDet.getPLANT() +"_"+TABLE_DETAILS+"]([PLANT]" + 
					"           ,[LNNO]" + 
					"           ,[PAYHDRID]" + 
					"           ,[PAYID]" + 
					"           ,[AMOUNT]" + 
					"           ,[TYPE]" + 
					"           ,[CURRENCYTOBASE]" + 
					"           ,[BASETOORDERCURRENCY]" + 
					"           ,[CURRENCYUSEQT]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, hrPayrollPaymentDet.getPLANT());
				   ps.setInt(2, hrPayrollPaymentDet.getLNNO());
				   ps.setInt(3, hrPayrollPaymentDet.getPAYHDRID());
				   ps.setInt(4, hrPayrollPaymentDet.getPAYID());
				   ps.setDouble(5, hrPayrollPaymentDet.getAMOUNT());
				   ps.setString(6, hrPayrollPaymentDet.getTYPE());
				   ps.setDouble(7, hrPayrollPaymentDet.getCURRENCYTOBASE());
				   ps.setDouble(8, hrPayrollPaymentDet.getBASETOORDERCURRENCY());
				   ps.setDouble(9, hrPayrollPaymentDet.getCURRENCYUSEQT());
				   ps.setString(10, DateUtils.getDateTime());
				   ps.setString(11, hrPayrollPaymentDet.getCRBY());
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	detId = rs.getInt(1);
		                    
		                }
				   }
				   else
				   {
					   throw new SQLException("Creating payroll payment details failed, no rows affected.");
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
		return detId;
	}
	
	
	public int updateDET(HrPayrollPaymentDet hrPayrollPaymentDet, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ hrPayrollPaymentDet.getPLANT() +"_"+TABLE_DETAILS+"] SET PLANT='"+hrPayrollPaymentDet.getPLANT()+"',"
					+ "LNNO='"+hrPayrollPaymentDet.getLNNO()+"',"
					+ "PAYHDRID='"+hrPayrollPaymentDet.getPAYHDRID()+"',"
					+ "PAYID='"+hrPayrollPaymentDet.getPAYID()+"',"
					+ "AMOUNT='"+hrPayrollPaymentDet.getAMOUNT()+"',"
					+ "TYPE='"+hrPayrollPaymentDet.getTYPE()+"',"
					+ "CURRENCYTOBASE='"+hrPayrollPaymentDet.getCURRENCYTOBASE()+"',"
					+ "BASETOORDERCURRENCY='"+hrPayrollPaymentDet.getBASETOORDERCURRENCY()+"',"
					+ "CURRENCYUSEQT='"+hrPayrollPaymentDet.getCURRENCYUSEQT()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',"
					+ "UPBY='"+user+"' WHERE ID="+hrPayrollPaymentDet.getID();
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
					   throw new SQLException("Updating payroll payment details failed, no rows affected.");
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
	
	public boolean DeleteDET(String plant, int id)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_DETAILS+"]"
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
	

	public List<HrPayrollPaymentDet> getdetbyhdrid(String plant,int id) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollPaymentDet> HrPayrollPaymentHDetlist=new ArrayList<HrPayrollPaymentDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT ID,PLANT,LNNO,PAYHDRID,PAYID,AMOUNT,ISNULL(TYPE,'') AS TYPE,CURRENCYTOBASE," + 
					"BASETOORDERCURRENCY,CURRENCYUSEQT,CRAT,CRBY,ISNULL(UPAT,'') AS UPAT,ISNULL(UPBY,'') AS UPBY FROM ["+ plant +"_"+TABLE_DETAILS+"] WHERE PAYHDRID = '"+id+"' ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollPaymentDet hrPayrollPaymentDet=new HrPayrollPaymentDet();
					   loadResultSetIntoObject(rst, hrPayrollPaymentDet);
	                   HrPayrollPaymentHDetlist.add(hrPayrollPaymentDet);
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
		return HrPayrollPaymentHDetlist;
	}
	
	public int getHrPayrollPaymendetbypayId(String plant, int payid)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    int hdrid =0;
	    HrPayrollPaymentDet hrPayrollPaymentDet=new HrPayrollPaymentDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAILS+"] WHERE PAYID="+payid+" AND TYPE='PAYROLL'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollPaymentDet);
	                    hdrid = hrPayrollPaymentDet.getPAYHDRID();
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
		return hdrid;
	}
	
	/*-------------- ATTACHMENTS -------------- */
	
	public int addAttachment(HrPayrollPaymentAttachment hrPayrollPaymentAttachment, String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ plant +"_"+TABLE_ATTACHMENTS+"]([PLANT]" + 
					"           ,[PAYHDRID]" + 
					"           ,[FileType]" + 
					"           ,[FileName]" + 
					"           ,[FileSize]" + 
					"           ,[FilePath]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, hrPayrollPaymentAttachment.getPLANT());
				   ps.setInt(2, hrPayrollPaymentAttachment.getPAYHDRID());
				   ps.setString(3, hrPayrollPaymentAttachment.getFileType());
				   ps.setString(4, hrPayrollPaymentAttachment.getFileName());
				   ps.setInt(5, hrPayrollPaymentAttachment.getFileSize());
				   ps.setString(6, hrPayrollPaymentAttachment.getFilePath());
				   ps.setString(7, dateutils.getDateTime());
				   ps.setString(8, user);
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   invoiceHdrId=count;
				   }
				   else
				   {
					   throw new SQLException("Creating payroll payment attachments failed, no rows affected.");
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
	
	public int updateAttachment(HrPayrollPaymentAttachment hrPayrollPaymentAttachment, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ hrPayrollPaymentAttachment.getPLANT() +"_"+TABLE_ATTACHMENTS+"] SET PLANT='"+hrPayrollPaymentAttachment.getPLANT()+"',"
					+ "PAYHDRID='"+hrPayrollPaymentAttachment.getPAYHDRID()+"',"
					+ "FileType='"+hrPayrollPaymentAttachment.getFileType()+"',"
					+ "FileName='"+hrPayrollPaymentAttachment.getFileName()+"',"
					+ "FileSize='"+hrPayrollPaymentAttachment.getFileSize()+"',"
					+ "FilePath='"+hrPayrollPaymentAttachment.getFilePath()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',"
					+ "UPBY='"+user+"' WHERE ID="+hrPayrollPaymentAttachment.getID();
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
					   throw new SQLException("Updating payroll payment attachments failed, no rows affected.");
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
	
	public boolean DeleteAttachment(String plant, int id)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_ATTACHMENTS+"]"
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
	
	public List<HrPayrollPaymentAttachment> getdattbyhdrid(String plant,int id) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollPaymentAttachment> HrPayrollPaymentAttachmentlist=new ArrayList<HrPayrollPaymentAttachment>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENTS+"] WHERE PAYHDRID = '"+id+"' ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollPaymentAttachment hrPayrollPaymentAttachment=new HrPayrollPaymentAttachment();
					   loadResultSetIntoObject(rst, hrPayrollPaymentAttachment);
					   HrPayrollPaymentAttachmentlist.add(hrPayrollPaymentAttachment);
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
		return HrPayrollPaymentAttachmentlist;
	}
	
	public HrPayrollPaymentAttachment getHrPayrollPaymentAttachmentId(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrPayrollPaymentAttachment hrPayrollPaymentAttachment=new HrPayrollPaymentAttachment();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENTS+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollPaymentAttachment);
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
		return hrPayrollPaymentAttachment;
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
