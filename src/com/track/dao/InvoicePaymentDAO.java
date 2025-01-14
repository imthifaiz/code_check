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
import com.track.db.object.InvPaymentAttachment;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.InvPaymentHeader;
import com.track.db.util.ExceptionUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;


public class InvoicePaymentDAO extends BaseDAO{
	
	public static String TABLE_HEADER = "FINRECEIVEHDR";
	public static String TABLE_DETAIL = "FINRECEIVEDET";
	public static String TABLE_ATTACHMENT = "FINRECEIVEATTACHMENTS";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.InvoicePaymentDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.InvoicePaymentDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addInvoicePaymentHdr(InvPaymentHeader invPaymentHeader, String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ plant +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[CUSTNO]" + 
					"           ,[TRANSACTIONID]" + 
					"           ,[AMOUNTRECEIVED]" + 
					"           ,[AMOUNTUFP]" + 
					"           ,[AMOUNTREFUNDED]" + 
					"           ,[RECEIVE_DATE]" + 
					"           ,[RECEIVE_MODE]" + 
					"           ,[DEPOSIT_TO]" + 
					"           ,[BANK_BRANCH]" + 
					"           ,[BANK_CHARGE]" + 
					"           ,[CHECQUE_NO]" + 
					"           ,[REFERENCE]" + 
					"           ,[NOTE]" + 
					"           ,[CRAT]" + 
					"           ,[CREDITAPPLYKEY]" + 
					"           ,[ACCOUNT_NAME]" + 
					"           ,[CHEQUE_DATE]" + 
					"           ,[ISPDCPROCESS]" +
					"           ,[PROJECTID]" +
					"           ,[CRBY],[CURRENCYID]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, invPaymentHeader.getPLANT());
				   ps.setString(2, invPaymentHeader.getCUSTNO());
				   ps.setString(3, invPaymentHeader.getTRANSACTIONID());
				   ps.setDouble(4, invPaymentHeader.getAMOUNTRECEIVED());
				   ps.setDouble(5, invPaymentHeader.getAMOUNTUFP());
				   ps.setDouble(6, invPaymentHeader.getAMOUNTREFUNDED());
				   ps.setString(7, invPaymentHeader.getRECEIVE_DATE());
				   ps.setString(8, invPaymentHeader.getRECEIVE_MODE());
				   ps.setString(9, invPaymentHeader.getDEPOSIT_TO());
				   ps.setString(10, invPaymentHeader.getBANK_BRANCH());
				   ps.setDouble(11, invPaymentHeader.getBANK_CHARGE());
				   ps.setString(12, invPaymentHeader.getCHECQUE_NO());
				   ps.setString(13, invPaymentHeader.getREFERENCE());
				   ps.setString(14, invPaymentHeader.getNOTE());
				   ps.setString(15, dateutils.getDateTime());
				   ps.setString(16, invPaymentHeader.getCREDITAPPLYKEY());
				   ps.setString(17, invPaymentHeader.getACCOUNT_NAME());
				   ps.setString(18, invPaymentHeader.getCHEQUE_DATE());
				   ps.setInt(19, invPaymentHeader.getISPDCPROCESS());
				   ps.setInt(20, invPaymentHeader.getPROJECTID());
				   ps.setString(21, user);
				   ps.setString(22, invPaymentHeader.getCURRENCYID());
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	invoiceHdrId = rs.getInt(1);
		                    
		                }
				   }
				   else
				   {
					   throw new SQLException("Creating payment record failed, no rows affected.");
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
	public int updateInvoicePaymentHdr(InvPaymentHeader invPaymentHeader, String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "UPDATE ["+ plant +"_"+TABLE_HEADER+"] SET [PLANT]=?" + 
					"           ,[CUSTNO]=?" + 
					"           ,[AMOUNTRECEIVED]=?" + 
					"           ,[AMOUNTUFP]=?" + 
					"           ,[AMOUNTREFUNDED]=?" + 
					"           ,[RECEIVE_DATE]=?" + 
					"           ,[RECEIVE_MODE]=?" + 
					"           ,[DEPOSIT_TO]=?" + 
					"           ,[BANK_BRANCH]=?" + 
					"           ,[BANK_CHARGE]=?" + 
					"           ,[CHECQUE_NO]=?" + 
					"           ,[REFERENCE]=?" + 
					"           ,[NOTE]=?" + 
					"           ,[UPAT]=?" + 
					"           ,[ACCOUNT_NAME]=?" + 
					"           ,[CHEQUE_DATE]=?" + 
					"           ,[ISPDCPROCESS]=?" +
					"           ,[PROJECTID]=?" +
					"           ,[UPBY]=? WHERE ID=?";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, invPaymentHeader.getPLANT());
				   ps.setString(2, invPaymentHeader.getCUSTNO());
				   ps.setDouble(3, invPaymentHeader.getAMOUNTRECEIVED());
				   ps.setDouble(4, invPaymentHeader.getAMOUNTUFP());
				   ps.setDouble(5, invPaymentHeader.getAMOUNTREFUNDED());
				   ps.setString(6, invPaymentHeader.getRECEIVE_DATE());
				   ps.setString(7, invPaymentHeader.getRECEIVE_MODE());
				   ps.setString(8, invPaymentHeader.getDEPOSIT_TO());
				   ps.setString(9, invPaymentHeader.getBANK_BRANCH());
				   ps.setDouble(10, invPaymentHeader.getBANK_CHARGE());
				   ps.setString(11, invPaymentHeader.getCHECQUE_NO());
				   ps.setString(12, invPaymentHeader.getREFERENCE());
				   ps.setString(13, invPaymentHeader.getNOTE());
				   ps.setString(14, dateutils.getDateTime());
				   ps.setString(15, invPaymentHeader.getACCOUNT_NAME());
				   ps.setString(16, invPaymentHeader.getCHEQUE_DATE());
				   ps.setInt(17, invPaymentHeader.getISPDCPROCESS());
				   ps.setInt(18, invPaymentHeader.getPROJECTID());
				   ps.setString(19, user);
				   ps.setInt(20, invPaymentHeader.getID());
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	invoiceHdrId = rs.getInt(1);
		                    
		                }
				   }
				   else
				   {
					   throw new SQLException("Creating payment record failed, no rows affected.");
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
	public int addInvoicePaymentDet(InvPaymentDetail invPaymentDetail, String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ plant +"_"+TABLE_DETAIL+"]([PLANT]" + 
					"           ,[LNNO]" + 
					"           ,[RECEIVEHDRID]" + 
					"           ,[INVOICEHDRID]" + 
					"           ,[AMOUNT]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" +
					"           ,[DONO]" +
					"           ,[TYPE]" +
					"           ,[BALANCE]" +
					"           ,[ADVANCEFROM]" +
					"           ,[CREDITAPPLYKEY]" +
					"           ,[CREDITNOTEHDRID],[CURRENCYUSEQT],[TOTAL_PAYING]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, invPaymentDetail.getPLANT());
				   ps.setInt(2, invPaymentDetail.getLNNO());
				   ps.setInt(3, invPaymentDetail.getRECEIVEHDRID());
				   ps.setInt(4, invPaymentDetail.getINVOICEHDRID());
				   ps.setDouble(5, invPaymentDetail.getAMOUNT());
				   ps.setString(6, dateutils.getDateTime());
				   ps.setString(7, user);
				   ps.setString(8, invPaymentDetail.getDONO());
				   ps.setString(9, invPaymentDetail.getTYPE());
				   ps.setDouble(10, invPaymentDetail.getBALANCE());
				   ps.setString(11, invPaymentDetail.getADVANCEFROM());
				   ps.setString(12, invPaymentDetail.getCREDITAPPLYKEY());
				   ps.setInt(13, invPaymentDetail.getCREDITNOTEHDRID());
				   ps.setDouble(14, invPaymentDetail.getCURRENCYUSEQT());
				   ps.setDouble(15, invPaymentDetail.getTOTAL_PAYING());
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   invoiceHdrId=count;
				   }
				   else
				   {
					   throw new SQLException("Creating payment record failed, no rows affected.");
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
	public int updateInvoicePaymentDet(InvPaymentDetail invPaymentDetail, String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "UPDATE ["+ plant +"_"+TABLE_DETAIL+"] SET [PLANT]=?" + 
					"           ,[BALANCE]=?" + 
					"           ,[AMOUNT]=?" + 
					"           ,[UPAT]=?" + 
					"           ,[UPBY]=? WHERE [LNNO]=? AND [RECEIVEHDRID]=? AND [INVOICEHDRID]=?";
			

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, invPaymentDetail.getPLANT());
				   ps.setDouble(2, invPaymentDetail.getBALANCE());
				   ps.setDouble(3, invPaymentDetail.getAMOUNT());
				   ps.setString(4, dateutils.getDateTime());
				   ps.setString(5, user);
				   ps.setInt(6, invPaymentDetail.getLNNO());
				   ps.setInt(7, invPaymentDetail.getRECEIVEHDRID());
				   ps.setInt(8, invPaymentDetail.getINVOICEHDRID());
				   
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   invoiceHdrId=count;
				   }
				   else
				   {
					   throw new SQLException("Creating payment record failed, no rows affected.");
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
	public int addInvoicePaymentAttachment(InvPaymentAttachment invPaymentAttachment, String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ plant +"_"+TABLE_ATTACHMENT+"]([PLANT]" + 
					"           ,[RECEIVEHDRID]" + 
					"           ,[FileType]" + 
					"           ,[FileName]" + 
					"           ,[FileSize]" + 
					"           ,[FilePath]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, invPaymentAttachment.getPLANT());
				   ps.setInt(2, invPaymentAttachment.getRECEIVEHDRID());
				   ps.setString(3, invPaymentAttachment.getFileType());
				   ps.setString(4, invPaymentAttachment.getFileName());
				   ps.setInt(5, invPaymentAttachment.getFileSize());
				   ps.setString(6, invPaymentAttachment.getFilePath());
				   ps.setString(7, dateutils.getDateTime());
				   ps.setString(8, user);
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   invoiceHdrId=count;
				   }
				   else
				   {
					   throw new SQLException("Creating payment record failed, no rows affected.");
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
	public int deleteInvoicePaymentAttachmentById(int ID, String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "DELETE FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE [ID]=?";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setInt(1, ID);
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   invoiceHdrId=count;
				   }
				   else
				   {
					   throw new SQLException("Creating payment record failed, no rows affected.");
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
	
	public int deleteInvoicePaymentDetById(int ID, String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "DELETE FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE [ID]=?";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setInt(1, ID);
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   invoiceHdrId=count;
				   }
				   else
				   {
					   throw new SQLException("Creating payment record failed, no rows affected.");
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
	public List<InvPaymentHeader> getAllInvoicePayment(String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<InvPaymentHeader> invoicePayments=new ArrayList<InvPaymentHeader>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    InvPaymentHeader invoicePaymentHeader=new InvPaymentHeader();
	                    loadResultSetIntoObject(rst, invoicePaymentHeader);
	                    invoicePayments.add(invoicePaymentHeader);
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
		return invoicePayments;
	}
	public InvPaymentHeader getInvoicePaymentById(int ID,String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = ID;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    InvPaymentHeader invoicePaymentHeader=new InvPaymentHeader();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,CUSTNO,ISNULL(ISPDCPROCESS,0) ISPDCPROCESS,ISNULL(A.PROJECTID,0) PROJECTID,ISNULL(A.ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(A.CHEQUE_DATE,'') AS CHEQUE_DATE,ISNULL(A.CREDITAPPLYKEY,'0') AS CREDITAPPLYKEY,TRANSACTIONID,AMOUNTRECEIVED,AMOUNTREFUNDED,RECEIVE_DATE,RECEIVE_MODE,DEPOSIT_TO,BANK_BRANCH,BANK_CHARGE,CHECQUE_NO,REFERENCE,NOTE,CRAT,CRBY,UPAT,UPBY,ISNULL(CURRENCYID,'') CURRENCYID,"
					+ "ISNULL((SELECT Top 1 R.CURRENCYUSEQT FROM "+plant+"_FINRECEIVEDET R WHERE R.RECEIVEHDRID=A.ID),1) CURRENCYUSEQT,(CAST((AMOUNTRECEIVED * (ISNULL((SELECT Top 1 R.CURRENCYUSEQT FROM "+plant+"_FINRECEIVEDET R WHERE R.RECEIVEHDRID=A.ID),1))) as varchar)) CONV_AMOUNTRECEIVED,"
					+ "(AMOUNTRECEIVED-(ISNULL((SELECT SUM(B.AMOUNT) FROM "+plant+"_FINRECEIVEDET B WHERE B.RECEIVEHDRID=A.ID AND B.LNNO != 0),0))) AS AMOUNTUFP "
					+ "FROM ["+ plant +"_"+TABLE_HEADER+"] A WHERE ID="+invoiceHdrId+" ORDER BY ID DESC";
			this.mLogger.query(this.printQuery, query);
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    loadResultSetIntoObject(rst, invoicePaymentHeader);
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
		return invoicePaymentHeader;
	}
	public List<InvPaymentDetail> getInvoicePaymentDetails(int ID,String plant,String user)throws Exception {
		boolean flag = false;
		int receiveHdrId = ID;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<InvPaymentDetail> invoicePaymentDetails=new ArrayList<InvPaymentDetail>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE RECEIVEHDRID="+receiveHdrId+" ORDER BY LNNO ASC";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    InvPaymentDetail invoicePaymentDetail=new InvPaymentDetail();
	                    loadResultSetIntoObject(rst, invoicePaymentDetail);
	                    invoicePaymentDetails.add(invoicePaymentDetail);
	                }  
				   System.out.println(invoicePaymentDetails.toString());
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
		return invoicePaymentDetails;
	}
	
	public List<InvPaymentDetail> getInvoicePaymentDetailsbylineno(int ID,String lnno,String plant)throws Exception {
		boolean flag = false;
		int receiveHdrId = ID;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<InvPaymentDetail> invoicePaymentDetails=new ArrayList<InvPaymentDetail>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE RECEIVEHDRID="+receiveHdrId+"AND LNNO="+lnno;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    InvPaymentDetail invoicePaymentDetail=new InvPaymentDetail();
	                    loadResultSetIntoObject(rst, invoicePaymentDetail);
	                    invoicePaymentDetails.add(invoicePaymentDetail);
	                }  
				   System.out.println(invoicePaymentDetails.toString());
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
		return invoicePaymentDetails;
	}
	
	public List<InvPaymentDetail> getInvoicePaymentDetailsbyid(int ID,String plant,String user)throws Exception {
		boolean flag = false;
		int detId = ID;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<InvPaymentDetail> invoicePaymentDetails=new ArrayList<InvPaymentDetail>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE ID="+detId;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    InvPaymentDetail invoicePaymentDetail=new InvPaymentDetail();
	                    loadResultSetIntoObject(rst, invoicePaymentDetail);
	                    invoicePaymentDetails.add(invoicePaymentDetail);
	                }  
				   System.out.println(invoicePaymentDetails.toString());
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
		return invoicePaymentDetails;
	}
	
	public List<InvPaymentDetail> getInvoicePaymentDetailsbydono(String dono,String plant,String user)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<InvPaymentDetail> invoicePaymentDetails=new ArrayList<InvPaymentDetail>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE DONO='"+dono+"' AND TYPE='ADVANCE' AND LNNO='0'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    InvPaymentDetail invoicePaymentDetail=new InvPaymentDetail();
	                    loadResultSetIntoObject(rst, invoicePaymentDetail);
	                    invoicePaymentDetails.add(invoicePaymentDetail);
	                }  
				   System.out.println(invoicePaymentDetails.toString());
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
		return invoicePaymentDetails;
	}
	
	public List getInvoicePaymentDetails(String plant, String type, String custno, String dono)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<Map<String, String>> invoicePaymentDetails=new ArrayList<>();
	   		try {	    
			connection = DbBean.getConnection();
			query = "SELECT b.NOTE,b.RECEIVE_DATE,a.AMOUNT,a.BALANCE,a.RECEIVEHDRID,a.ID,b.REFERENCE,a.CREDITNOTEHDRID FROM"
					+"["+ plant +"_"+TABLE_DETAIL+"] a JOIN "+ plant +"_FINRECEIVEHDR b ON a.RECEIVEHDRID = b.ID WHERE "
					+"a.TYPE='"+type+"' AND b.CUSTNO='"+custno+"' AND a.DONO ='"+dono+"' AND a.BALANCE != 0";

			if(connection != null){
				this.mLogger.query(this.printQuery, query.toString());
				invoicePaymentDetails = selectData(connection, query.toString());			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return invoicePaymentDetails;
	}
	
	public int getmaxlineno(int ID,String plant ) throws Exception {
		boolean flag = false;
		int lineno = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT MAX(LNNO) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE [RECEIVEHDRID]="+ID;
			System.out.println("--------------------------"+query);
			if(connection != null){
				   ps = connection.prepareStatement(query);
				   ResultSet rs = ps.executeQuery();
				   if (rs.next()) {
					   lineno = rs.getInt(1);
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
		return lineno;
	}
	
	public Double getcreditamoutusingorderno(String plant, String DONO) throws Exception {
		boolean flag = false;
		Double balamount = 0.0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT SUM(BALANCE * ISNULL(CURRENCYUSEQT,0)) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE DONO='"+DONO+"' and TYPE='ADVANCE'";
			
			if(connection != null){
				   ps = connection.prepareStatement(query);
				   ResultSet rs = ps.executeQuery();
				   if (rs.next()) {
					   balamount = rs.getDouble(1);
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
		return balamount;
	}
	
	
	public Double getpaidamoutusingorderno(String plant, String DONO) throws Exception {
		boolean flag = false;
		Double balamount = 0.0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT SUM(AMOUNT * ISNULL(CURRENCYUSEQT,0)) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE DONO='"+DONO+"' and TYPE='REGULAR'";
			
			if(connection != null){
				   ps = connection.prepareStatement(query);
				   ResultSet rs = ps.executeQuery();
				   if (rs.next()) {
					   balamount = rs.getDouble(1);
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
		return balamount;
	}
	
	public Double getcreditamout(String plant, String custno) throws Exception {
		boolean flag = false;
		Double balamount = 0.0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT SUM(BALANCE) FROM ["+ plant +"_"+TABLE_DETAIL+"] a JOIN "+ plant +"_FINRECEIVEHDR b ON a.RECEIVEHDRID = b.ID WHERE " 
					+"a.TYPE='ADVANCE' AND b.CUSTNO='"+custno+"' AND a.DONO ='0'"; 
			
			if(connection != null){
				   ps = connection.prepareStatement(query);
				   ResultSet rs = ps.executeQuery();
				   if (rs.next()) {
					   balamount = rs.getDouble(1);
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
		return balamount;
	}
	
	
	public Double getbalacedue(String plant, String invid) throws Exception {
		boolean flag = false;
		Double balamount = 0.0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT SUM(AMOUNT) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE INVOICEHDRID="+invid+" AND TYPE='REGULAR'";
			
			if(connection != null){
				   ps = connection.prepareStatement(query);
				   ResultSet rs = ps.executeQuery();
				   if (rs.next()) {
					   balamount = rs.getDouble(1);
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
		return balamount;
	}
	
	public Double getbalacedueusingrechdr(String plant, String invid) throws Exception {
		boolean flag = false;
		Double balamount = 0.0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT SUM(AMOUNT) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE RECEIVEHDRID="+invid+" AND TYPE='REGULAR'";
			
			if(connection != null){
				   ps = connection.prepareStatement(query);
				   ResultSet rs = ps.executeQuery();
				   if (rs.next()) {
					   balamount = rs.getDouble(1);
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
		return balamount;
	}
	
	
	public InvPaymentDetail getInvoicePaymentDetailsbyid(int ID,String plant)throws Exception {
		boolean flag = false;
		int destId = ID;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    InvPaymentDetail invoicePaymentDetails=new InvPaymentDetail();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE ID="+destId;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   System.out.println(rst.toString());
	                    loadResultSetIntoObject(rst, invoicePaymentDetails);
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
		return invoicePaymentDetails;
	}
	
	public InvPaymentDetail getInvoicePaymentDetailsbycreditid(int ID,String plant)throws Exception {
		boolean flag = false;
		int crid = ID;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    InvPaymentDetail invoicePaymentDetails=new InvPaymentDetail();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE CREDITNOTEHDRID ="+crid;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   System.out.println(rst.toString());
	                    loadResultSetIntoObject(rst, invoicePaymentDetails);
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
		return invoicePaymentDetails;
	}

	public List<InvPaymentAttachment> getInvoiceAttachmentDetails(int ID,String plant,String user)throws Exception {
		boolean flag = false;
		int receiveHdrId = ID;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<InvPaymentAttachment> InvPaymentAttachmentList=new ArrayList<InvPaymentAttachment>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE RECEIVEHDRID="+receiveHdrId+" ORDER BY ID ASC";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					    InvPaymentAttachment invoiceAttachment=new InvPaymentAttachment();
	                    loadResultSetIntoObject(rst, invoiceAttachment);
	                    InvPaymentAttachmentList.add(invoiceAttachment);
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
		return InvPaymentAttachmentList;
	}
	public List<InvPaymentAttachment> getInvoiceAttachmentDetailsByID(int ID,String plant,String user)throws Exception {
		boolean flag = false;
		int Id = ID;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<InvPaymentAttachment> InvPaymentAttachmentList=new ArrayList<InvPaymentAttachment>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE ID="+Id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					    InvPaymentAttachment invoiceAttachment=new InvPaymentAttachment();
	                    loadResultSetIntoObject(rst, invoiceAttachment);
	                    InvPaymentAttachmentList.add(invoiceAttachment);
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
		return InvPaymentAttachmentList;
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
	
public List<InvPaymentHeader> getAllInvoicePaymentByFilter(String whereQuery,String plant,String user)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<InvPaymentHeader> invoicePayments=new ArrayList<InvPaymentHeader>();
	    PlantMstDAO plantMstDAO = new PlantMstDAO();
	    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		try {	    
			connection = DbBean.getConnection();
			/*query = "SELECT PLANT,ID,CUSTNO,ISNULL(ISPDCPROCESS,0) ISPDCPROCESS,ISNULL(A.ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(A.CHEQUE_DATE,'') AS CHEQUE_DATE,ISNULL(A.CREDITAPPLYKEY,'0') AS CREDITAPPLYKEY,TRANSACTIONID,AMOUNTRECEIVED,AMOUNTREFUNDED,RECEIVE_DATE,RECEIVE_MODE,DEPOSIT_TO,BANK_BRANCH,BANK_CHARGE,CHECQUE_NO,REFERENCE,NOTE,CRAT,CRBY,UPAT,UPBY,"
					+"ISNULL((SELECT SUM(R.BALANCE) FROM "+plant+"_FINRECEIVEDET R WHERE R.RECEIVEHDRID=A.ID AND R.LNNO != 0),0) UNUSEDAMOUNT,"
					+ "(AMOUNTRECEIVED-(ISNULL((SELECT SUM(B.AMOUNT) FROM "+plant+"_FINRECEIVEDET B WHERE B.RECEIVEHDRID=A.ID AND B.LNNO != 0),0))) AS AMOUNTUFP FROM ["+ plant +"_"+TABLE_HEADER+"] AS A "+whereQuery+" ORDER BY ID DESC, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + SUBSTRING(RECEIVE_DATE, 4, 2) + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) DESC ";*/
			
			query = "SELECT PLANT,ID,CUSTNO,ISNULL(ISPDCPROCESS,0) ISPDCPROCESS,ISNULL(A.PROJECTID,0) PROJECTID,ISNULL(A.ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(A.CHEQUE_DATE,'') AS CHEQUE_DATE,ISNULL(A.CREDITAPPLYKEY,'0') AS CREDITAPPLYKEY,TRANSACTIONID,AMOUNTRECEIVED,AMOUNTREFUNDED,RECEIVE_DATE,RECEIVE_MODE,DEPOSIT_TO,BANK_BRANCH,BANK_CHARGE,CHECQUE_NO,REFERENCE,NOTE,CRAT,CRBY,UPAT,UPBY,ISNULL(CURRENCYID,'')CURRENCYID,"
					+ "ISNULL((SELECT Top 1 R.CURRENCYUSEQT FROM "+plant+"_FINRECEIVEDET R WHERE R.RECEIVEHDRID=A.ID),1) CURRENCYUSEQT,ISNULL(A.CURRENCYID,'') + (CAST((  CAST((AMOUNTRECEIVED * (ISNULL((SELECT Top 1 R.CURRENCYUSEQT FROM "+plant+"_FINRECEIVEDET R WHERE R.RECEIVEHDRID=A.ID),1))) as decimal(18,"+numberOfDecimal+")))as varchar)) CONV_AMOUNTRECEIVED,"
					+ "ISNULL((SELECT SUM(R.BALANCE) FROM "+plant+"_FINRECEIVEDET R WHERE R.RECEIVEHDRID=A.ID AND R.LNNO = 0),0) AS AMOUNTUFP FROM ["+ plant +"_"+TABLE_HEADER+"] AS A "+whereQuery+" ORDER BY ID DESC, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + SUBSTRING(RECEIVE_DATE, 4, 2) + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) DESC ";
			this.mLogger.query(this.printQuery, query);
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    InvPaymentHeader invoicePaymentHeader=new InvPaymentHeader();
	                    loadResultSetIntoObject(rst, invoicePaymentHeader);
	                    invoicePayments.add(invoicePaymentHeader);
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
		return invoicePayments;
	}	


public List getPaymentReceivedDetails(String whereQuery,String plant,String user)throws Exception {
	boolean flag = false;
	int invoiceHdrId = 0;
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
    List invoicePayments=new ArrayList();
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT * "
				+" ,ISNULL((STUFF((SELECT ',' + CONVERT(VARCHAR,ISNULL((SELECT INVOICE FROM ["+plant+"_FININVOICEHDR] WHERE ID = INVOICEHDRID),''))" + 
				" FROM ["+plant+"_FINRECEIVEDET] " + 
				" WHERE (RECEIVEHDRID = A.ID) AND INVOICEHDRID <> 0" + 
				" FOR XML PATH ('')), 1, 1, '')),'') AS INVOICE FROM ["+ plant +"_"+TABLE_HEADER+"] A "+whereQuery+" ORDER BY ID DESC, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + SUBSTRING(RECEIVE_DATE, 4, 2) + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) DESC ";
		this.mLogger.query(this.printQuery, query);
		if(connection != null){
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   ResultSet rst = ps.executeQuery();
			   while (rst.next()) {
                    InvPaymentHeader invoicePaymentHeader=new InvPaymentHeader();
                    loadResultSetIntoObject(rst, invoicePaymentHeader);
                    invoicePayments.add(invoicePaymentHeader);
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
	return invoicePayments;
}	

public ArrayList getPaymentsMadeSummaryView(String whereQuery,String plant,String user) {
	ArrayList arrList = new ArrayList();
	String sCondition = "",dtCondStr="",extraCon="";
	String query ="";
	Connection connection = null;
	 Hashtable htData = new Hashtable();
	try {
		connection = DbBean.getConnection();
		query = "SELECT * "
				+" ,ISNULL((STUFF((SELECT ',' + CONVERT(VARCHAR,ISNULL((SELECT INVOICE FROM ["+plant+"_FININVOICEHDR] WHERE ID = INVOICEHDRID),''))" + 
				" FROM ["+plant+"_FINRECEIVEDET] " + 
				" WHERE (RECEIVEHDRID = A.ID) AND INVOICEHDRID <> 0" + 
				" FOR XML PATH ('')), 1, 1, '')),'') AS INVOICE FROM ["+ plant +"_"+TABLE_HEADER+"] A "+whereQuery+" ORDER BY ID DESC, CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + SUBSTRING(RECEIVE_DATE, 4, 2) + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) DESC ";
		arrList = selectData(connection, query.toString());
	 }catch (Exception e) {
		this.mLogger.exception(this.printLog,"Exception :repportUtil :: getBillPaymentSummary:", e);
	}
	return arrList;
}

public String getOutStdAmt(String query) throws Exception {
	Map map = new HashMap();
	java.sql.Connection con = null;
	String outstdamt="";
	try {    
		con = DbBean.getConnection();
		this.mLogger.query(this.printQuery, query.toString());
		map = getRowOfData(con, query.toString());
		Map m = new BaseDAO().getRowOfData(con, query);
		outstdamt = (String) m.get("OUTSTDAMT");
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return outstdamt;	
}


public List<InvPaymentDetail> getInvoicePaymentDetailsbyuuid(String uuid,String plant,String user)throws Exception {
	boolean flag = false;
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
    List<InvPaymentDetail> invoicePaymentDetails=new ArrayList<InvPaymentDetail>();
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE CREDITAPPLYKEY='"+uuid+"'";
		this.mLogger.query(this.printQuery, query);
		if(connection != null){
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   ResultSet rst = ps.executeQuery();
			   while (rst.next()) {
                    InvPaymentDetail invoicePaymentDetail=new InvPaymentDetail();
                    loadResultSetIntoObject(rst, invoicePaymentDetail);
                    invoicePaymentDetails.add(invoicePaymentDetail);
                }  
			   System.out.println(invoicePaymentDetails.toString());
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
	return invoicePaymentDetails;
}





public void addPaymentpdc(Hashtable ht, String plant)throws Exception {
	boolean flag = false;
	int billHdrId = 0;
	Connection connection = null;
	PreparedStatement ps = null;
	List<String> args = null;
    String query = "";
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
		}
		query = "INSERT INTO ["+ plant +"_FINRECEIVEPDC] ("
				+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";

		if(connection != null){
			  /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   
		this.mLogger.query(this.printQuery, query);
		billHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}

}

public boolean addMultiplePaymentpdc(List<Hashtable<String, String>> billpdcInfoList, String plant) 
		throws Exception {
	boolean flag = false;
	Connection connection = null;
	PreparedStatement ps = null;
	List<String> args = null;
	String query = "";
	try {		
		/*Instantiate the list*/
		args = new ArrayList<String>();
		
		connection = DbBean.getConnection();
		
		for (Hashtable<String, String> billDetInfo : billpdcInfoList) {
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = billDetInfo.keys();
			for (int i = 0; i < billDetInfo.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) billDetInfo.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}		
			query = "INSERT INTO ["+ plant +"_FINRECEIVEPDC]  ("
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

public List getpdcbipayid(Hashtable ht) throws Exception {
	java.sql.Connection con = null;
	List<Map<String, String>> billPaymentDetList = new ArrayList<>();
	Map<String, String> map = null;
	String query="";
	List<String> args = null;
	try {			
		/*Instantiate the list*/
	    args = new ArrayList<String>();
	    
		con = com.track.gates.DbBean.getConnection();
		query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINRECEIVEPDC] "
				+ "WHERE RECEIVEID = ? AND PLANT = ?";
		PreparedStatement ps = con.prepareStatement(query);  
		
		/*Storing all the query param argument in list squentially*/
		args.add((String) ht.get("PAYMENTID"));
		args.add((String) ht.get("PLANT"));			
		
		this.mLogger.query(this.printQuery, query);			
		billPaymentDetList = selectData(ps, args);			
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return billPaymentDetList;
}

public int deletepdcbypayid(String plant,String id) throws Exception {
	java.sql.Connection con = null;
	Map<String, String> map = null;
	String query="";
	int count=0;
	try {			
	    
		con = com.track.gates.DbBean.getConnection();
		query="DELETE FROM " + "[" + plant + "_FINRECEIVEPDC] "
				+ "WHERE RECEIVEID = "+id+" AND PLANT = '"+ plant +"' AND STATUS != 'PROCESSED'";
		PreparedStatement ps = con.prepareStatement(query);  
		this.mLogger.query(this.printQuery, query);	
		count=ps.executeUpdate();
		
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return count;
}

public int updatePaymentpdc(String paymentpdcId,String SETQUERY, String plant)throws Exception {
	boolean flag = false;
	int billHdrId = 2;
	Connection connection = null;
	PreparedStatement ps = null;
	List<String> args = null;
    String query = "";
	try {
		 /*Instantiate the list*/
	    args = new ArrayList<String>();		    
		connection = DbBean.getConnection();			
		query = "UPDATE ["+ plant +"_FINRECEIVEPDC] SET "+SETQUERY+" WHERE ID="+paymentpdcId;

		if(connection != null){
			  /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   
		this.mLogger.query(this.printQuery, query);
		billHdrId = ps.executeUpdate();
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return billHdrId;
}

public List getPaymentRecvPdcForDashboard(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
	java.sql.Connection con = null;
	List<Map<String, String>> billPaymentDetList = new ArrayList<>();
	Map<String, String> map = null;
	String query="";
	String sCondition = "",dtCondStr="",extraCon="";
	List<String> args = null;
	try {
		args = new ArrayList<String>();
		con = com.track.gates.DbBean.getConnection();
		query = "SELECT RECEIVEID,RECEIVE_DATE, "
				+"ISNULL((SELECT CNAME FROM ["+plant+"_CUSTMST] C WHERE C.CUSTNO = A.CUSTNO),'') CUSTOMER, "
				+"BANK_BRANCH,CHECQUE_NO,CHEQUE_DATE,ISNULL(CHEQUE_REVERSAL_DATE,'') CHEQUE_REVERSAL_DATE,CAST(CHEQUE_AMOUNT AS DECIMAL(18,"+numberOfDecimal+")) CHEQUE_AMOUNT "
				+ " FROM ["+plant+"_FINRECEIVEPDC] A WHERE STATUS = 'NOT PROCESSED'  AND PLANT = ? "
				+" AND CONVERT(DATETIME, CHEQUE_DATE, 103) between '" + fromDate + "' and '" + toDate + "' "
				+" OR CONVERT(DATETIME, CHEQUE_REVERSAL_DATE, 103) between '" + fromDate + "' and '" + toDate + "' ";
		PreparedStatement ps = con.prepareStatement(query);  
		
		/*Storing all the query param argument in list squentially*/			
		args.add(plant);			
		
		this.mLogger.query(this.printQuery, query);			
		billPaymentDetList = selectData(ps, args);			
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return billPaymentDetList;
}

public String getPdcAmountReceivableForDashboard(String plant, String fromDate, String toDate, 
		String numberOfDecimal) throws Exception {
      java.sql.Connection con = null;
      String pdcAmount = "";
      String SqlQuery="";
      try {
	      con = DbBean.getConnection();
	      SqlQuery = "SELECT CAST(ISNULL(SUM(CHEQUE_AMOUNT),0) AS DECIMAL(18,"+numberOfDecimal+")) PDC_AMOUNT FROM ["+plant+"_FINRECEIVEPDC] " 
	        		+ " WHERE STATUS = 'NOT PROCESSED' "
					+ " AND CONVERT(DATETIME, CHEQUE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
	        		+ " OR CONVERT(DATETIME, CHEQUE_REVERSAL_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' ";
	      System.out.println(SqlQuery.toString());
	      Map m = this.getRowOfData(con, SqlQuery);
	      pdcAmount = (String) m.get("PDC_AMOUNT");
      } catch (Exception e) {
              this.mLogger.exception(this.printLog, "", e);
              throw e;
      } finally {
              if (con != null) {
                      DbBean.closeConnection(con);
              }
      }
      return pdcAmount;
  }

  public List getpdcpayment(Hashtable ht, String afrmDate,
		String atoDate,String CUSTNO,String BANK,
		String CHECQUE_NO,String TYPE,String STATUS) throws Exception {
	java.sql.Connection con = null;
	List<Map<String, String>> invoicePaymentDetList = new ArrayList<>();
	Map<String, String> map = null;
	String query="";
	String sCondition = "",dtCondStr="",extraCon="",dtCondStrrev="";
	List<String> args = null;
	try {			
		/*Instantiate the list*/
	    args = new ArrayList<String>();
	    
	    dtCondStr ="and (ISNULL(CHEQUE_DATE,'')<>'' AND CAST((SUBSTRING(CHEQUE_DATE, 7, 4) + '-' + SUBSTRING(CHEQUE_DATE, 4, 2) + '-' + SUBSTRING(CHEQUE_DATE, 1, 2)) AS date)";
	    dtCondStrrev ="or ISNULL(CHEQUE_REVERSAL_DATE,'')<>'' AND CAST((SUBSTRING(CHEQUE_REVERSAL_DATE, 7, 4) + '-' + SUBSTRING(CHEQUE_REVERSAL_DATE, 4, 2) + '-' + SUBSTRING(CHEQUE_REVERSAL_DATE, 1, 2)) AS date)";
	    //extraCon= " order by CAST((SUBSTRING(CHEQUE_DATE, 7, 4) + SUBSTRING(CHEQUE_DATE, 4, 2) + SUBSTRING(CHEQUE_DATE, 1, 2)) AS date) desc";
	    extraCon= " order by RECEIVEID desc";
	    if (afrmDate.length() > 0) {
          	sCondition = sCondition + dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  ";
          	sCondition = sCondition + dtCondStrrev + "  >= '" 
						+ afrmDate
						+ "' ) ";
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					sCondition = sCondition +dtCondStrrev+ " <= '" 
		  					+ atoDate
		  					+ "' ) ";
				}
			  } else {
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					sCondition = sCondition +dtCondStrrev+ " <= '" 
		  					+ atoDate
		  					+ "' ) ";
				}
		     	}
	    
	    if(TYPE.equalsIgnoreCase("PROCESS"))
	    	sCondition = sCondition +" AND STATUS='NOT PROCESSED' ";
	    else
	    {
	    	if(STATUS.equalsIgnoreCase("PROCESSED"))
	    		sCondition = sCondition +" AND STATUS='PROCESSED' ";
	    	else if(STATUS.equalsIgnoreCase("NOT PROCESSED"))
	    		sCondition = sCondition +" AND STATUS='NOT PROCESSED' ";
	    	else	
	    	sCondition = sCondition +" AND STATUS IN ('NOT PROCESSED','PROCESSED') ";
	    }
		con = com.track.gates.DbBean.getConnection();
		query="SELECT *,CASE WHEN CUSTNO='N/A' THEN ISNULL(ACCOUNT_NAME,'Loan from Financial Institution') else '' END ACCOUNT,ISNULL((SELECT CNAME FROM " + "[" + ht.get("PLANT") +"_CUSTMST] V WHERE V.CUSTNO=P.CUSTNO),'') CUSTOMER FROM " + "[" + ht.get("PLANT") + "_FINRECEIVEPDC] P "
				+ "WHERE CUSTNO like '%"+CUSTNO+"%' AND BANK_BRANCH like '%"+BANK+"%' AND CHECQUE_NO  like '%"+CHECQUE_NO+"%' AND PLANT = ? " + sCondition + extraCon;
		PreparedStatement ps = con.prepareStatement(query);  
		
		/*Storing all the query param argument in list squentially*/
		
		args.add((String) ht.get("PLANT"));			
		
		this.mLogger.query(this.printQuery, query);			
		invoicePaymentDetList = selectData(ps, args);			
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return invoicePaymentDetList;
}

public int updatePaymentPDC(String paymentId,Hashtable ht, String plant)throws Exception {
	boolean flag = false;
	int billHdrId = 2;
	Connection connection = null;
	PreparedStatement ps = null;
	List<String> args = null;
    String query = "";
	try {
		 /*Instantiate the list*/
	    args = new ArrayList<String>();		    
		connection = DbBean.getConnection();			
		String FIELDS = "", VALUES = "",SETQUERY=" ";
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
		query = "UPDATE ["+ plant +"_FINRECEIVEPDC] SET "+SETQUERY+" WHERE ID="+paymentId;

		if(connection != null){
			  /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   
		this.mLogger.query(this.printQuery, query);
		billHdrId = ps.executeUpdate();
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return billHdrId;
}

public List getpdcpaymentById(Hashtable ht) throws Exception {
	java.sql.Connection con = null;
	List<Map<String, String>> billPaymentDetList = new ArrayList<>();
	Map<String, String> map = null;
	String query="";		
	List<String> args = null;
	try {			
		/*Instantiate the list*/
	    args = new ArrayList<String>();
	    
		con = com.track.gates.DbBean.getConnection();
		query="SELECT *,CASE WHEN CUSTNO='N/A' THEN ISNULL(ACCOUNT_NAME,'Loan from Financial Institution') else '' END ACCOUNT,ISNULL((SELECT CNAME FROM " + "[" + ht.get("PLANT") +"_CUSTMST] V WHERE V.CUSTNO=P.CUSTNO),'') CUSTOMER FROM " + "[" + ht.get("PLANT") + "_FINRECEIVEPDC] P "
				+ "WHERE ID = ? AND PLANT = ? ";
		PreparedStatement ps = con.prepareStatement(query);  
		
		/*Storing all the query param argument in list squentially*/
		
		args.add((String) ht.get("ID"));
		args.add((String) ht.get("PLANT"));			
		
		
		this.mLogger.query(this.printQuery, query);			
		billPaymentDetList = selectData(ps, args);			
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return billPaymentDetList;
}

public boolean deleteInvoicepaymentall(String plant,String TranId)
        throws Exception {
		boolean dstatus = false;
		PreparedStatement ps = null;
		PreparedStatement pshdr = null;
		PreparedStatement psatt = null;
		PreparedStatement pspdc = null;
		Connection con = null;
		try {
		        con = DbBean.getConnection();
		        
		        
		        String sQry = "DELETE FROM " + "[" + plant + "_FINRECEIVEHDR" + "]"
		                        + " WHERE ID ='"+TranId+"'";
		        this.mLogger.query(this.printQuery, sQry);
		        ps = con.prepareStatement(sQry);
		        int iCnt = ps.executeUpdate();
		        if (iCnt > 0)
		        	dstatus = true;
		        if(dstatus) {
		        sQry = "DELETE FROM " + "[" + plant + "_FINRECEIVEDET" + "]"
                        + " WHERE RECEIVEHDRID ='"+TranId+"'";
		        this.mLogger.query(this.printQuery, sQry);
		        pshdr = con.prepareStatement(sQry);
		        int iCnthdr = pshdr.executeUpdate();
		       
		         sQry = "DELETE FROM " + "[" + plant + "_FINRECEIVEATTACHMENTS" + "]"
	                    + " WHERE RECEIVEHDRID ='"+TranId+"'";
			    this.mLogger.query(this.printQuery, sQry);
			    psatt = con.prepareStatement(sQry);
			    int iCntatt = psatt.executeUpdate();
			    
			    sQry = "DELETE FROM " + "[" + plant + "_FINRECEIVEPDC" + "]"
	                    + " WHERE RECEIVEID ='"+TranId+"'";
			    this.mLogger.query(this.printQuery, sQry);
			    pspdc = con.prepareStatement(sQry);
			    int iCntpdc = pspdc.executeUpdate();
			    
		        }
		        
		} catch (Exception e) {
			dstatus = false;
		    this.mLogger.exception(this.printLog, "", e);
		} finally {
		        DbBean.closeConnection(con, ps);
		}
		
		return dstatus;
	}

}
