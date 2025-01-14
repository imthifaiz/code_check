
package com.track.dao;	
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.Journal;
import com.track.db.object.JournalAttachment;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.LedgerDetails;
import com.track.db.object.LedgerDetailsRec;
import com.track.db.object.LedgerDetailsWithId;
import com.track.db.object.ReconciliationPojo;
import com.track.db.object.TrialBalanceDetails;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;	
@SuppressWarnings({"rawtypes", "unchecked"})
public class JournalDAO extends BaseDAO {	
	public static String TABLE_HEADER = "FINJOURNALHDR";	
	public static String TABLE_DETAIL = "FINJOURNALDET";	
	public static String TABLE_ATTACHMENT = "FINJOURNALATTACHMENTS";	
	public String plant = "";	
	private MLogger mLogger = new MLogger();	
	private boolean printQuery = MLoggerConstant.JournalDAO_PRINTPLANTMASTERQUERY;	
	private boolean printLog = MLoggerConstant.JournalDAO_PRINTPLANTMASTERLOG;	
		
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
		
	public int addJournal(Journal journal,String user) throws Exception	
	{	
		JournalHeader journalHead=journal.getJournalHeader();	
//		boolean flag = false;	
		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
		try {	    	
			connection = DbBean.getConnection();	
			query = "INSERT INTO ["+ journalHead.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 	
					"           ,[JOURNAL_DATE]" + 	
					"           ,[TRANSACTION_ID]" + 	
					"           ,[TRANSACTION_TYPE]" + 	
					"           ,[REFERENCE]" + 	
					"           ,[NOTE]" + 	
					"           ,[CURRENCYID]" + 	
					"           ,[JOURNAL_TYPE]" + 	
					"           ,[JOURNAL_STATUS]" + 	
					"           ,[SUB_TOTAL]" + 	
					"           ,[TOTAL_AMOUNT]" + 	
					"           ,[CRAT]" + 	
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ps.setString(1, journalHead.getPLANT());	
				   ps.setString(2, journalHead.getJOURNAL_DATE());	
				   ps.setString(3, journalHead.getTRANSACTION_ID());	
				   ps.setString(4, journalHead.getTRANSACTION_TYPE());	
				   ps.setString(5, journalHead.getREFERENCE());	
				   ps.setString(6, journalHead.getNOTE());	
				   ps.setString(7, journalHead.getCURRENCYID());	
				   ps.setString(8, journalHead.getJOURNAL_TYPE());	
				   ps.setString(9, journalHead.getJOURNAL_STATUS());	
				   ps.setDouble(10, journalHead.getSUB_TOTAL());	
				   ps.setDouble(11, journalHead.getTOTAL_AMOUNT());	
				   ps.setString(12, DateUtils.getDateTime());	
				   ps.setString(13, user);	
				   int count=ps.executeUpdate();	
				   if(count>0)	
				   {	
					   ResultSet rs = ps.getGeneratedKeys();	
		                if(rs.next())	
		                {	
		                	journalHdrId = rs.getInt(1);	
		                    	
		                }	
		                int i=0;	
		                for(JournalDetail journdet:journal.getJournalDetails())	
		                {	
		                	i++;	
		                	journdet.setJOURNALHDRID(journalHdrId);	
		                	journdet.setLNNO(i);	
		                	addJournalDet(journdet,user); 	
		                }	
		                if(journal.getJournalAttachment()!=null)	
		                {	
		                	for(JournalAttachment journalAttach:journal.getJournalAttachment())	
			                {	
			                	journalAttach.setJOURNALHDRID(journalHdrId);	
			                	addJournalAttachment(journalAttach,user);	
			                }	
		                }	
		                	
		                	
				   }	
				   else	
				   {	
					   throw new SQLException("Creating journal record failed, no rows affected.");	
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
		return journalHdrId;	
	}	
	private int addJournalDet(JournalDetail journalDet,String user) throws Exception	
	{	
			
//		boolean flag = false;	
		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
		try {	    	
			connection = DbBean.getConnection();	
			query = "INSERT INTO ["+ journalDet.getPLANT() +"_"+TABLE_DETAIL+"]([PLANT]" + 	
					"           ,[LNNO]" + 	
					"           ,[JOURNALHDRID]" + 	
					"           ,[ACCOUNT_ID]" +	
					"           ,[ACCOUNT_NAME]" + 	
					"           ,[DESCRIPTION]" +	
					"           ,[DEBITS]" + 	
					"           ,[CREDITS]" + 	
					"           ,[OPENINGBALANCEDEBITS]" + 	
					"           ,[OPENINGBALANCECREDITS]" + 	
					"           ,[CLOSINGBALANCEDEBITS]" + 	
					"           ,[CLOSINGBALANCECREDITS]" + 	
					"           ,[CRAT]" + 	
					"           ,[RECONCILIATION]" + 	
					"           ,[BANKDATE]" + 	
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ps.setString(1, journalDet.getPLANT());	
				   ps.setInt(2, journalDet.getLNNO());	
				   ps.setInt(3, journalDet.getJOURNALHDRID());	
				   ps.setInt(4, journalDet.getACCOUNT_ID());	
				   ps.setString(5, journalDet.getACCOUNT_NAME());	
				   ps.setString(6, journalDet.getDESCRIPTION());	
				   ps.setDouble(7, journalDet.getDEBITS());	
				   ps.setDouble(8, journalDet.getCREDITS());	
				   ps.setDouble(9, journalDet.getDEBITS());	
				   ps.setDouble(10, journalDet.getDEBITS());	
				   ps.setDouble(11, journalDet.getDEBITS());	
				   ps.setDouble(12, journalDet.getDEBITS());	
				   ps.setString(13, DateUtils.getDateTime());	
				   if(journalDet.getRECONCILIATION() == null || journalDet.getRECONCILIATION().equals(null)) {
					   ps.setShort(14, Short.parseShort("0"));
				   }else {
					   ps.setShort(14, journalDet.getRECONCILIATION());
				   }
				   ps.setString(15, journalDet.getBANKDATE());
				   ps.setString(16, user);
				   int count=ps.executeUpdate();	
				   if(count>0)	
				   {	
					   ResultSet rs = ps.getGeneratedKeys();	
		                if(rs.next())	
		                {	
		                	journalHdrId = rs.getInt(1);	
		                    	
		                }	
				   }	
				   else	
				   {	
					   throw new SQLException("Creating journal Detail record failed, no rows affected.");	
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
		return journalHdrId;	
	}	
		
	private int addJournalAttachment(JournalAttachment journalAttach,String user) throws Exception	
	{	
			
//		boolean flag = false;	
		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
		try {	    	
			connection = DbBean.getConnection();	
			query = "INSERT INTO ["+ journalAttach.getPLANT() +"_"+TABLE_ATTACHMENT+"]([PLANT]" + 	
					"           ,[JOURNALHDRID]" + 	
					"           ,[FileType]" + 	
					"           ,[FileName]" + 	
					"           ,[FileSize]" + 	
					"           ,[FilePath]" + 	
					"           ,[CRAT]" + 	
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?)";	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ps.setString(1, journalAttach.getPLANT());	
				   ps.setInt(2, journalAttach.getJOURNALHDRID());	
				   ps.setString(3, journalAttach.getFileType());	
				   ps.setString(4, journalAttach.getFileName());	
				   ps.setInt(5, journalAttach.getFileSize());	
				   ps.setString(6, journalAttach.getFilePath());	
				   ps.setString(7, DateUtils.getDateTime());	
				   ps.setString(8, user);	
				   int count=ps.executeUpdate();	
				   if(count>0)	
				   {	
					   ResultSet rs = ps.getGeneratedKeys();	
		                if(rs.next())	
		                {	
		                	journalHdrId = rs.getInt(1);	
		                    	
		                }	
				   }	
				   else	
				   {	
					   throw new SQLException("Creating journal Detail record failed, no rows affected.");	
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
		return journalHdrId;	
	}	
		
	public List<Journal> getAllJournals(String plant,String user,String type)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    List<Journal> journalList=new ArrayList<>();	
	    List<JournalHeader> journalHeaderList=new ArrayList<JournalHeader>();	
	    	
		try {	    	
			connection = DbBean.getConnection();	
			if(type.equalsIgnoreCase("ALL"))	
			{	
				query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";	
			}	
			else	
			{	
				if(type.equalsIgnoreCase("JOURNAL"))	
					query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_TYPE !='CONTRA' ORDER BY ID DESC";	
				else	
					query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_TYPE ='CONTRA' ORDER BY ID DESC";	
			}	
				
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalHeader journalHeader=new JournalHeader();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);	
	                    journalHeaderList.add(journalHeader);	
	                }   	
			this.mLogger.query(this.printQuery, query);	
				
			}	
			for(JournalHeader journalHead:journalHeaderList)	
			{	
				List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
				query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID="+journalHead.getID();	
				this.mLogger.query(this.printQuery, query);	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalDetail journalDetail=new JournalDetail();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                    journalDetailList.add(journalDetail);	
		                }	
				}	
				 List<JournalAttachment> journalAttachmentList=new ArrayList<JournalAttachment>();	
				query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE JOURNALHDRID="+journalHead.getID();	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalAttachment journalAttach=new JournalAttachment();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalAttach);	
		                    journalAttachmentList.add(journalAttach);	
		                }	
				}	
				Journal journal=new Journal();	
				journal.setJournalHeader(journalHead);	
				journal.setJournalDetails(journalDetailList);	
				journal.setJournalAttachment(journalAttachmentList);	
				journalList.add(journal);	
			}	
		} catch (Exception e) {	
			this.mLogger.exception(this.printLog, "", e);	
				
			throw e;	
		} finally {	
			if (connection != null) {	
				DbBean.closeConnection(connection);	
			}	
		}	
		return journalList;	
	}	
	public Journal getJournalById(String plant,int id)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    Journal journal=new Journal();	
	    JournalHeader journalHeader=new JournalHeader();	
	    List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
	    List<JournalAttachment> journalAttachmentList=new ArrayList<JournalAttachment>();	
		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);	
	                }   	
			this.mLogger.query(this.printQuery, query);	
				
			}	
				
				query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID="+id;	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalDetail journalDetail=new JournalDetail();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                    journalDetailList.add(journalDetail);	
		                }	
				}	
				query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE JOURNALHDRID="+id;	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalAttachment journalAttach=new JournalAttachment();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalAttach);	
		                    journalAttachmentList.add(journalAttach);	
		                }	
				}	
				journal.setJournalHeader(journalHeader);	
				journal.setJournalDetails(journalDetailList);	
				journal.setJournalAttachment(journalAttachmentList);	
		} catch (Exception e) {	
			this.mLogger.exception(this.printLog, "", e);	
				
			throw e;	
		} finally {	
			if (connection != null) {	
				DbBean.closeConnection(connection);	
			}	
		}	
		return journal;	
	}	
	public JournalHeader getJournalHeaderById(String plant,int id)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
//	    Journal journal=new Journal();	
	    JournalHeader journalHeader=new JournalHeader();	
//	    List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
//	    List<JournalAttachment> journalAttachmentList=new ArrayList<JournalAttachment>();	
		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);	
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
		return journalHeader;	
	}	
	public Journal getJournalByTransactionId(String plant,String transid,String transtype)throws Exception {
		return getJournalByTransactionId(plant, transid, transtype, false);
	}
	public Journal getJournalByTransactionId(String plant,String transid,String transtype, boolean isTransactionTypeLike)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    Journal journal=new Journal();	
	    JournalHeader journalHeader=new JournalHeader();	
	    List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
	    List<JournalAttachment> journalAttachmentList=new ArrayList<JournalAttachment>();	
		try {	    	
			connection = DbBean.getConnection();	
			if (isTransactionTypeLike) {
				query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_ID='"+transid+"' AND TRANSACTION_TYPE LIKE '%"+transtype+"%'";	
			}else {
				query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_ID='"+transid+"' AND TRANSACTION_TYPE='"+transtype+"'";	
			}
			//query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_ID='"+transid+"'";
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);	
	                }   	
			this.mLogger.query(this.printQuery, query);	
				
			}	
				
				query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID="+journalHeader.getID();	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalDetail journalDetail=new JournalDetail();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                    journalDetailList.add(journalDetail);	
		                }	
				}	
				query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE JOURNALHDRID="+journalHeader.getID();	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalAttachment journalAttach=new JournalAttachment();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalAttach);	
		                    journalAttachmentList.add(journalAttach);	
		                }	
				}	
				journal.setJournalHeader(journalHeader);	
				journal.setJournalDetails(journalDetailList);	
				journal.setJournalAttachment(journalAttachmentList);	
		} catch (Exception e) {	
			this.mLogger.exception(this.printLog, "", e);	
				
			throw e;	
		} finally {	
			if (connection != null) {	
				DbBean.closeConnection(connection);	
			}	
		}	
		return journal;	
	}	
	public List<Journal> getJournalsByDateWithoutAttach(String plant,String fromDate,String toDate)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    List<Journal> journals=new ArrayList<Journal>();	
	    	
	    List<JournalHeader> journalHeaderList=new ArrayList<JournalHeader>();	
	    	
	    //List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
	   // List<JournalAttachment> journalAttachmentList=new ArrayList<JournalAttachment>();	
		try {	    	
			connection = DbBean.getConnection();
			if(fromDate.length() > 0 && toDate.length() > 0){
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			}else if(fromDate.length() > 0 && toDate.length() == 0) {
				query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date) >= '"+fromDate+"'";
			}else if(fromDate.length() == 0 && toDate.length() > 0){
				query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date) <= '"+toDate+"'";
			}else {
				query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"]";
			}
				
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalHeader journalHeader=new JournalHeader();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);	
	                    journalHeaderList.add(journalHeader);	
	                }   	
			this.mLogger.query(this.printQuery, query);	
				
			}	
			for(JournalHeader journHead:journalHeaderList)	
			{	
				List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
				query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID="+journHead.getID();	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalDetail journalDetail=new JournalDetail();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                    journalDetailList.add(journalDetail);	
		                }	
				}	
				Journal journal=new Journal();	
				journal.setJournalHeader(journHead);	
				journal.setJournalDetails(journalDetailList);	
				journals.add(journal);	
			}	
					
					
		} catch (Exception e) {	
			this.mLogger.exception(this.printLog, "", e);	
				
			throw e;	
		} finally {	
			if (connection != null) {	
				DbBean.closeConnection(connection);	
			}	
		}	
		return journals;	
	}	
		
	public List<JournalDetail> getJournalDetailsByAccountTypeAndDATE(String plant,String fromDate,String toDate,String accounttypes) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+"  order by ca.ACCOUNTTYPE desc";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}	
	
	public List<ReconciliationPojo> getJournalDetailsByAccount(String plant,String fromDate,String toDate,String account) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<ReconciliationPojo> ReconciliationPojoList=new ArrayList<ReconciliationPojo>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "SELECT D.ID,D.PLANT,H.JOURNAL_DATE AS DATE,D.DESCRIPTION AS ACCOUNT,H.JOURNAL_TYPE AS VOUCHERTYPE,'' AS FILTERDATE, "
					+ "ISNULL(D.BANKDATE,'') AS INSTRUMENTDATE,D.DEBITS AS DEBIT,D.CREDITS AS CREDIT,ISNULL(D.BANKDATE,'') AS "
					+ "BANKDATE FROM "+plant+"_FINJOURNALDET AS D JOIN "+plant+"_FINJOURNALHDR AS H  ON H.ID = D.JOURNALHDRID "
					+ "WHERE ISNULL(D.RECONCILIATION,0) = 0 where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(ISNULL(D.BANKDATE,H.JOURNAL_DATE), 4, 2) + "
					+ "SUBSTRING(ISNULL(D.BANKDATE,H.JOURNAL_DATE), 1, 2)) AS date) BETWEEN '"+fromDate+"' AND '"+toDate+"' AND D.ACCOUNT_ID = '"+account+"' AND "
					+ "ISNULL(D.RECONCILIATION,0) = '0'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   ReconciliationPojo reconciliationPojo=new ReconciliationPojo();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationPojo);	
	                    ReconciliationPojoList.add(reconciliationPojo);	
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
		return ReconciliationPojoList;	
	}
	
	public List<ReconciliationPojo> getJournalDetailsByAccountTodate(String plant,String account) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<ReconciliationPojo> ReconciliationPojoList=new ArrayList<ReconciliationPojo>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "SELECT D.ID,D.PLANT,ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS DATE,D.DESCRIPTION AS ACCOUNT,H.JOURNAL_TYPE AS VOUCHERTYPE,'' AS FILTERDATE,"
					+ "ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS INSTRUMENTDATE,D.DEBITS AS DEBIT,D.CREDITS AS CREDIT,ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS "
					+ "BANKDATE FROM "+plant+"_FINJOURNALDET AS D JOIN "+plant+"_FINJOURNALHDR AS H  ON H.ID = D.JOURNALHDRID "
					+ "WHERE ISNULL(D.RECONCILIATION,0) = 0 AND D.ACCOUNT_ID = '"+account+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   ReconciliationPojo reconciliationPojo=new ReconciliationPojo();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationPojo);	
	                    ReconciliationPojoList.add(reconciliationPojo);	
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
		return ReconciliationPojoList;	
	}
	
	public List<ReconciliationPojo> getJournalDetailsByAccountTodateRid(String plant,String account,String rid) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<ReconciliationPojo> ReconciliationPojoList=new ArrayList<ReconciliationPojo>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "SELECT D.ID,D.PLANT,ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS DATE,D.DESCRIPTION AS ACCOUNT,H.JOURNAL_TYPE AS VOUCHERTYPE,'' AS FILTERDATE, "
					+ "ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS INSTRUMENTDATE,D.DEBITS AS DEBIT,D.CREDITS AS CREDIT,ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS "
					+ "BANKDATE FROM "+plant+"_FINJOURNALDET AS D JOIN "+plant+"_FINJOURNALHDR AS H  ON H.ID = D.JOURNALHDRID "
					+ "WHERE ISNULL(D.RID,0) = '"+rid+"' AND D.ACCOUNT_ID = '"+account+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   ReconciliationPojo reconciliationPojo=new ReconciliationPojo();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationPojo);	
	                    ReconciliationPojoList.add(reconciliationPojo);	
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
		return ReconciliationPojoList;	
	}
	
	
	public List<TrialBalanceDetails> getTrialBalanceDetailsByAccount(String plant,String account,String fromDate,String toDate)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<TrialBalanceDetails> trialBalanceDetails=new ArrayList<TrialBalanceDetails>();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,JOURNAL_DATE,ACCOUNT_NAME,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   TrialBalanceDetails trialBalanceDetail=new TrialBalanceDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, trialBalanceDetail);	
	                    trialBalanceDetails.add(trialBalanceDetail);	
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
		return trialBalanceDetails;	
	}	
	public List<LedgerDetails> getLedgerDetailsByAccount(String plant,String account,String fromDate,String toDate)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,ISNULL(det.BANKDATE,hdr.JOURNAL_DATE) AS JOURNAL_DATE,ACCOUNT_NAME,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}
	
	public List<Journal> getProjectJournalsByDateWithoutAttach(String plant,String fromDate,String toDate,String projId)throws Exception {
		//boolean flag = false;
		//int journalHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<Journal> journals=new ArrayList<Journal>();
	    
	    List<JournalHeader> journalHeaderList=new ArrayList<JournalHeader>();
	    
	    //List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();
	   // List<JournalAttachment> journalAttachmentList=new ArrayList<JournalAttachment>();
		try {	    
			connection = DbBean.getConnection();
			if(projId.isEmpty())
			{
				
				query = "SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_finbillhdr] B ON A.TRANSACTION_ID=B.BILL WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND (TRANSACTION_TYPE LIKE '%BILL%' OR TRANSACTION_TYPE LIKE '%BILL_REVERSAL%')"+ 
						" AND B.PROJECTID = '0' UNION ALL "+
						 
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_finexpenseshdr] B ON A.TRANSACTION_ID=B.ID WHERE"+
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND (TRANSACTION_TYPE LIKE '%EXPENSE%' OR TRANSACTION_TYPE LIKE '%EXPENSE FOR PO%')"+
						" AND B.PROJECTID = '0' UNION ALL "+
						
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_FINVENDORCREDITNOTEHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND TRANSACTION_TYPE LIKE '%SUPPLIERCREDITNOTES%'"+
						" AND B.PROJECTID = '0' UNION ALL "+
					
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_FINPAYMENTHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND TRANSACTION_TYPE LIKE '%PURCHASEPAYMENT%'"+
						" AND B.PROJECTID = '0' UNION ALL "+
						
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_fininvoicehdr] B ON A.TRANSACTION_ID=B.invoice WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND (TRANSACTION_TYPE LIKE '%INVOICE%' OR TRANSACTION_TYPE LIKE '%INOVICE_REVERSAL%' OR TRANSACTION_TYPE LIKE '%COSTOFGOODSOLD%')"+
						" AND B.PROJECTID = '0' UNION ALL "+
						
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_FINCUSTOMERCREDITNOTEHDR] B ON A.TRANSACTION_ID=B.creditnote WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND TRANSACTION_TYPE LIKE '%CUSTOMERCREDITNOTES%'"+ 
						" AND B.PROJECTID = '0' UNION ALL "+
						
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_FINRECEIVEHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND TRANSACTION_TYPE LIKE '%SALESPAYMENT%'"+ 
						" AND B.PROJECTID = '0'";
			
						
			}
			else 
			{
				query = "SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_finbillhdr] B ON A.TRANSACTION_ID=B.BILL WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND (TRANSACTION_TYPE LIKE '%BILL%' OR TRANSACTION_TYPE LIKE '%BILL_REVERSAL%')"+ 
						" AND B.PROJECTID='"+projId+"' UNION ALL "+
						 
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_finexpenseshdr] B ON A.TRANSACTION_ID=B.ID WHERE"+
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND (TRANSACTION_TYPE LIKE '%EXPENSE%' OR TRANSACTION_TYPE LIKE '%EXPENSE FOR PO%')"+
						" AND B.PROJECTID='"+projId+"' UNION ALL "+
						
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_FINVENDORCREDITNOTEHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND TRANSACTION_TYPE LIKE '%SUPPLIERCREDITNOTES%'"+
						" AND B.PROJECTID='"+projId+"' UNION ALL "+
					
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_FINPAYMENTHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND TRANSACTION_TYPE LIKE '%PURCHASEPAYMENT%'"+
						" AND B.PROJECTID='"+projId+"' UNION ALL "+
						
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_fininvoicehdr] B ON A.TRANSACTION_ID=B.invoice WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND (TRANSACTION_TYPE LIKE '%INVOICE%' OR TRANSACTION_TYPE LIKE '%INOVICE_REVERSAL%' OR TRANSACTION_TYPE LIKE '%COSTOFGOODSOLD%')"+
						" AND B.PROJECTID='"+projId+"' UNION ALL "+
						
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_FINCUSTOMERCREDITNOTEHDR] B ON A.TRANSACTION_ID=B.creditnote WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND TRANSACTION_TYPE LIKE '%CUSTOMERCREDITNOTES%'"+ 
						" AND B.PROJECTID='"+projId+"' UNION ALL "+
						
						"SELECT A.PLANT,A.ID,A.JOURNAL_DATE,A.REFERENCE,A.NOTE,A.CURRENCYID,A.JOURNAL_TYPE,A.JOURNAL_STATUS,A.TRANSACTION_ID,"+
						"A.TRANSACTION_TYPE,A.SUB_TOTAL,A.TOTAL_AMOUNT,A.CRAT,A.CRBY,A.UPAT,A.UPBY FROM ["+ plant +"_FINJOURNALHDR] A JOIN  ["+ plant +"_FINRECEIVEHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND TRANSACTION_TYPE LIKE '%SALESPAYMENT%'"+ 
						" AND B.PROJECTID='"+projId+"'";
				
			}
			
			System.out.println(query);
			
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   JournalHeader journalHeader=new JournalHeader();
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);
	                    journalHeaderList.add(journalHeader);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
			for(JournalHeader journHead:journalHeaderList)
			{
				List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();
				query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID="+journHead.getID();
				if(connection != null){
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   JournalDetail journalDetail=new JournalDetail();
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);
		                    journalDetailList.add(journalDetail);
		                }
				}
				Journal journal=new Journal();
				journal.setJournalHeader(journHead);
				journal.setJournalDetails(journalDetailList);
				journals.add(journal);
			}
				
				
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return journals;
	}
	
	
	public List<LedgerDetails> getLedgerDetailsByDate(String plant,String fromDate,String toDate)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
//		String lmt=" ";	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
		try {	    	
				
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,JOURNAL_DATE,ACCOUNT_NAME,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' order by JOURNALHDRID desc";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}	
	public List<LedgerDetails> getJournalLedgerDetailsByDate(String plant,String fromDate,String toDate,String limit)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		String lmt=" ";	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
		try {	    	
			if(!limit.equalsIgnoreCase("All") &&!"".equals(limit) ) {	
				lmt= "TOP "+limit;	
			}	
				
			connection = DbBean.getConnection();	
			query= "select  JOURNALHDRID,JOURNAL_DATE,ACCOUNT_NAME,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS "	
					+ "from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "	
							+ " where hdr.id in(select "+lmt+" journalhdrid FROM ["+ plant +"_"+TABLE_DETAIL+"] ) AND "	
							+ "CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}	
	public List<LedgerDetails> getGeneralLedgerDetailsByDate(String plant,String fromDate,String toDate,String Search)throws Exception {	
		String strSearch=" ";	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
		try {	    	
			if(Search!=null &&!"".equals(Search) ) {	
				strSearch= " and det.account_name='"+Search+"'";	
			}	
				
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,ISNULL(BANKDATE,JOURNAL_DATE) AS JOURNAL_DATE,ACCOUNT_NAME,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS "	
					+ "from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "	
							+ "where CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' "+strSearch+" ";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}	
	
	public List<LedgerDetailsWithId> getGeneralLedgerIdDetailsByDate(String plant,String fromDate,String toDate,String Search)throws Exception {	
		String strSearch=" ";	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetailsWithId> ledgerDetailsArr=new ArrayList<LedgerDetailsWithId>();	
	   	
		try {	    	
			if(Search!=null &&!"".equals(Search) ) {	
				strSearch= " and det.account_name='"+Search+"'";	
			}	
				
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,ISNULL(BANKDATE,JOURNAL_DATE) AS JOURNAL_DATE,ISNULL(RECONCILIATION,0) AS RECONCILIATION,ACCOUNT_NAME,ACCOUNT_ID,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,'0' AS BALANCEAMT,DEBITS,CREDITS "	
					+ "from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "	
							+ "where CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' "+strSearch+" ";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetailsWithId ledgerDetail=new LedgerDetailsWithId();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}	
	public int getmaxcount(String plant ) throws Exception {	
//		boolean flag = false;	
		int lineno = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT ISNULL(max(ID),0) FROM ["+ plant +"_"+TABLE_HEADER+"]";	
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
	
	
//  RESVI START JOURNAL
  public int Journalcount(String plant, String afrmDate, String atoDate)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Journalcount = 0;
		String sCondition = "";
		String dtCondStr =    " AND (SUBSTRING(JOURNAL_DATE, 7, 4) + '-' + SUBSTRING(JOURNAL_DATE, 4, 2) + '-' + SUBSTRING(JOURNAL_DATE, 1, 2))";
		if (afrmDate.length() > 0) {
        	sCondition = " " + dtCondStr +"  >= '" + afrmDate
        	+ "'  ";
        	if (atoDate.length() > 0) {
        		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
        		+ "'  ";
        	}
        }
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "FINJOURNALHDR" + "]" + " WHERE TRANSACTION_TYPE ='JOURNAL' and " + IConstants.PLANT
					+ " = '" + plant.toUpperCase() + "'"+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Journalcount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Journalcount;
	}

//RESVI ENDS
  
  
//RESVI START CONTRA
public int Contracount(String plant, String afrmDate, String atoDate)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Contracount = 0;
		String sCondition = "";
		String dtCondStr =    " AND (SUBSTRING(JOURNAL_DATE, 7, 4) + '-' + SUBSTRING(JOURNAL_DATE, 4, 2) + '-' + SUBSTRING(JOURNAL_DATE, 1, 2))";
		if (afrmDate.length() > 0) {
      	sCondition = " " + dtCondStr +"  >= '" + afrmDate
      	+ "'  ";
      	if (atoDate.length() > 0) {
      		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
      		+ "'  ";
      	}
      }
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "FINJOURNALHDR" + "]" + " WHERE TRANSACTION_TYPE ='CONTRA' and " + IConstants.PLANT
					+ " = '" + plant.toUpperCase() + "'"+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Contracount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Contracount;
	}

//RESVI ENDS
	public int updateJournal(Journal journal,String user) throws Exception	
	{	
		JournalHeader journalHead=journal.getJournalHeader();	
//		boolean flag = false;	
		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
		try {	    	
			connection = DbBean.getConnection();	
			query="UPDATE ["+ journalHead.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+journalHead.getPLANT()+"',JOURNAL_DATE='"+journalHead.getJOURNAL_DATE()+"',TRANSACTION_ID='"+journalHead.getTRANSACTION_ID()+"',TRANSACTION_TYPE='"+journalHead.getTRANSACTION_TYPE()+"',REFERENCE='"+journalHead.getREFERENCE()+"',NOTE='"+journalHead.getNOTE()+"',CURRENCYID='"+journalHead.getCURRENCYID()+"',JOURNAL_TYPE='"+journalHead.getJOURNAL_TYPE()+"',JOURNAL_STATUS='"+journalHead.getJOURNAL_STATUS()+"',SUB_TOTAL='"+journalHead.getSUB_TOTAL()+"',TOTAL_AMOUNT='"+journalHead.getTOTAL_AMOUNT()+"',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+journalHead.getID();	
			this.mLogger.query(this.printQuery, query);		
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   int count=ps.executeUpdate();	
				   if(count>0)	
				   {	
					   ResultSet rs = ps.getGeneratedKeys();	
		                if(rs.next())	
		                {	
		                	journalHdrId = rs.getInt(1);	
		                    	
		                }	
		                query="DELETE FROM ["+ journalHead.getPLANT() +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID="+journalHead.getID();	
		                ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
						ps.executeUpdate();	
		                int i=0;	
		                for(JournalDetail journdet:journal.getJournalDetails())	
		                {	
		                	i++;	
		                	journdet.setJOURNALHDRID(journalHead.getID());	
		                	journdet.setLNNO(i);	
		                	addJournalDet(journdet,user); 	
		                }	
		                if(journal.getJournalAttachment()!=null)	
		                {	
			                for(JournalAttachment journalAttach:journal.getJournalAttachment())	
			                {	
			                	journalAttach.setJOURNALHDRID(journalHead.getID());	
			                	addJournalAttachment(journalAttach,user);	
			                }	
		                }   	
		                	
				   }	
				   else	
				   {	
					   throw new SQLException("Creating journal record failed, no rows affected.");	
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
		return journalHdrId;	
	}	
	public List<JournalAttachment> getJournalAttachmentDetailsByID(int ID,String plant,String user)throws Exception {	
//		boolean flag = false;	
		int Id = ID;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    List<JournalAttachment> JournalAttachmentList=new ArrayList<JournalAttachment>();	
		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE ID="+Id;	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalAttachment journalAttachment=new JournalAttachment();	
					   ResultSetToObjectMap.loadResultSetIntoObject(rst, journalAttachment);	
	                    JournalAttachmentList.add(journalAttachment);	
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
		return JournalAttachmentList;	
	}	
	public int deleteJournalAttachmentById(int ID, String plant,String user)throws Exception {	
//		boolean flag = false;	
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
					   throw new SQLException("Delete journal failed, no rows affected.");	
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
		
		
	public boolean deleteJournal(String plant,int TranId)	
	        throws Exception {	
			boolean deleteprdForTranId = false;	
//			boolean deleteprdForTranIdhdr = false;	
//			boolean deleteprdForTranIdatt = false;	
			PreparedStatement ps = null;	
			PreparedStatement pshdr = null;	
			PreparedStatement psatt = null;	
			Connection con = null;	
			try {	
			        con = DbBean.getConnection();	
			        	
			        	
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_HEADER+"]"	
			                        + " WHERE ID ='"+TranId+"'";	
			        this.mLogger.query(this.printQuery, sQry);	
			        ps = con.prepareStatement(sQry);	
			        int iCnt = ps.executeUpdate();	
			        if (iCnt > 0)	
			                deleteprdForTranId = true;	
			        if(deleteprdForTranId) {	
			        sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_DETAIL+"]"	
	                        + " WHERE JOURNALHDRID ='"+TranId+"'";	
			        this.mLogger.query(this.printQuery, sQry);	
			        pshdr = con.prepareStatement(sQry);	
//			        int iCnthdr = 
			        		pshdr.executeUpdate();	
			     	
			         sQry = "DELETE FROM " + "["+ plant +"_"+TABLE_ATTACHMENT+"]"	
		                    + " WHERE JOURNALHDRID ='"+TranId+"'";	
				    this.mLogger.query(this.printQuery, sQry);	
				    psatt = con.prepareStatement(sQry);	
//				    int iCntatt = 
				    		psatt.executeUpdate();	
			        }	
			        	
			} catch (Exception e) {	
			        this.mLogger.exception(this.printLog, "", e);	
			} finally {	
			        DbBean.closeConnection(con, ps);	
			}	
				
			return deleteprdForTranId;	
 	}	
	public boolean DeleteJournal(String plant,String TranId)	
	        throws Exception {	
			boolean deleteprdForTranId = false;	
			PreparedStatement pshdr = null;	
			PreparedStatement ps = null;	
			Connection con = null;	
			try {	
			        con = DbBean.getConnection();	
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_HEADER+"]"	
			                        + " WHERE ID ='"+TranId+"'";	
			        this.mLogger.query(this.printQuery, sQry);	
			        ps = con.prepareStatement(sQry);	
			        int iCnt = ps.executeUpdate();	
			        if (iCnt > 0) 	
			                deleteprdForTranId = true;	
	                if(deleteprdForTranId) {	
				        sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_DETAIL+"]"	
		                        + " WHERE JOURNALHDRID ='"+TranId+"'";	
				        this.mLogger.query(this.printQuery, sQry);	
				        pshdr = con.prepareStatement(sQry);	
				     int iCnthdr = pshdr.executeUpdate();	
				     if(iCnthdr>0)	
				    	 deleteprdForTranId = true;	
				        }	
			        	
			} catch (Exception e) {	
			        this.mLogger.exception(this.printLog, "", e);	
			} finally {	
			        DbBean.closeConnection(con, ps);	
			}	
				
			return deleteprdForTranId;	
 	}		
	
	public Integer getJournalTridandTrtype(String plant,String TranId,String Trantype)throws Exception {	

		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    Journal journal=new Journal();	
	    JournalHeader journalHeader=new JournalHeader();	
		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_ID ='"+TranId+"' AND TRANSACTION_TYPE ='"+Trantype+"'";
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);	
	                    journalHdrId = journalHeader.getID();
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
		return journalHdrId;	
	}
	
	public List getTrialBalanceQueryType(String plant,String account,String fromDate,String toDate)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List trialBalanceDetails=new ArrayList();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "SELECT CASE WHEN (DEBITS = (SELECT ISNULL(SUM(CREDITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID)  - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "WHEN (CREDITS = (SELECT ISNULL(SUM(DEBITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID) - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "ELSE  'SUB' END AS TRANSACTION_TYPE "	
					+ "FROM ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID where det1.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(hdr1.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr1.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr1.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' GROUP BY DEBITS,JOURNALHDRID,CREDITS";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
	                    trialBalanceDetails.add(rst.getString(1));	
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
		return trialBalanceDetails;	
	}	
		
	public List<TrialBalanceDetails> getTrialBalanceDetailsByAccountByMainType(String plant,String account,String fromDate,String toDate)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    String subquery = "";	
	  	
	    List<TrialBalanceDetails> trialBalanceDetails=new ArrayList<TrialBalanceDetails>();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			subquery= "select JOURNALHDRID from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
				
			query= "select JOURNALHDRID,JOURNAL_DATE,ACCOUNT_NAME,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS as CREDITS,CREDITS as DEBITS from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID<>'"+account+"' AND JOURNALHDRID IN ("+subquery+") AND CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   TrialBalanceDetails trialBalanceDetail=new TrialBalanceDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, trialBalanceDetail);	
	                    trialBalanceDetails.add(trialBalanceDetail);	
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
		return trialBalanceDetails;	
	}	
		
	public List<TrialBalanceDetails> getTrialBalanceDetailsByAccountBySubType(String plant,String account,String fromDate,String toDate)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<TrialBalanceDetails> trialBalanceDetails=new ArrayList<TrialBalanceDetails>();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS, "	
					+ "ISNULL((select (SELECT  ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] A WHERE A.JOURNALHDRID = HDR1.ID" 	
					+ " AND ((A.CREDITS = (SELECT SUM(DEBITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID)  - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' ) ) OR" 	
					+ " (A.DEBITS = (SELECT SUM(CREDITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID) - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )))"	
					+ " ) ACCOUNT_NAME"	
					+ " from ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID" 	
					+ " where det1.ACCOUNT_ID='"+account+"' AND "	
					+ " CAST((SUBSTRING(hdr1.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr1.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr1.JOURNAL_DATE, 1, 2)) AS date)" 	
					+ " BETWEEN '"+fromDate+"' AND '"+toDate+"' and det1.JOURNALHDRID = det.JOURNALHDRID"	
					+ "),'') as ACCOUNT_NAME "	
					+ "from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   TrialBalanceDetails trialBalanceDetail=new TrialBalanceDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, trialBalanceDetail);	
	                    trialBalanceDetails.add(trialBalanceDetail);	
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
		return trialBalanceDetails;	
	}	
		
	public List<TrialBalanceDetails> getTrialBalanceDetailsByAccountByType(String plant,String account,String fromDate,String toDate)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    String subquery = "", typequery = "";	
	  	
	    List<TrialBalanceDetails> trialBalanceDetails=new ArrayList<TrialBalanceDetails>();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			typequery= "SELECT CASE WHEN (DEBITS = (SELECT ISNULL(SUM(CREDITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID)  - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "WHEN (CREDITS = (SELECT ISNULL(SUM(DEBITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID) - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "ELSE  'SUB' END AS TRANSACTION_TYPE, JOURNALHDRID "	
					+ "FROM ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID where det1.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(hdr1.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr1.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr1.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' GROUP BY DEBITS,JOURNALHDRID,CREDITS";	
				
			subquery= "select det.JOURNALHDRID from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
				
			query= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,CREDITS as DEBITS,DEBITS as CREDITS,ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "	
					+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID WHERE det.ACCOUNT_ID<>'"+account+"' AND det.JOURNALHDRID IN ("+subquery+") AND D.TRANSACTION_TYPE='MAIN' AND CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
				
			query+= " UNION ALL ";	
				
			query+= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS, "	
					+ "ISNULL((select (SELECT  ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] A WHERE A.JOURNALHDRID = HDR1.ID" 	
					+ " AND ((A.CREDITS = (SELECT SUM(DEBITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID)  - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' ) ) OR" 	
					+ " (A.DEBITS = (SELECT SUM(CREDITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID) - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )))"	
					+ " ) ACCOUNT_NAME"	
					+ " from ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID" 	
					+ " where det1.ACCOUNT_ID='"+account+"' AND "	
					+ " CAST((SUBSTRING(hdr1.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr1.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr1.JOURNAL_DATE, 1, 2)) AS date)" 	
					+ " BETWEEN '"+fromDate+"' AND '"+toDate+"' and det1.JOURNALHDRID = det.JOURNALHDRID"	
					+ "),'') as ACCOUNT_NAME "	
					+ "from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "					
					+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND D.TRANSACTION_TYPE='SUB' AND CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
				
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   TrialBalanceDetails trialBalanceDetail=new TrialBalanceDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, trialBalanceDetail);	
	                    trialBalanceDetails.add(trialBalanceDetail);	
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
		return trialBalanceDetails;	
	}	
		
		
	public List<JournalDetail> getJournalDetailsByChartOfAccountIdAndDATE(String plant,String fromDate,String toDate,String ChartAccountId) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' AND  jd.ACCOUNT_ID ='"+ChartAccountId+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}	
		
	public List<JournalDetail> getJournalDetailsByAccountDetailTypeAndDATE(String plant,String fromDate,String toDate,String accountdetailtypes) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTDETAILTYPE in "+accountdetailtypes;	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}	
		
	
	public List<LedgerDetails> getLedgerDetailsByAccountAll(String plant,String account)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select A.JOURNALHDRID,B.JOURNAL_DATE,A.ACCOUNT_NAME,A.DESCRIPTION,B.TRANSACTION_TYPE,B.TRANSACTION_ID,B.REFERENCE,A.DEBITS AS CREDITS,A.CREDITS AS DEBITS "			
					+ "FROM ["+ plant +"_FINJOURNALDET] AS A inner join ["+ plant +"_FINJOURNALHDR] B "
					+ "on B.ID=A.JOURNALHDRID where " 
					+ "A.JOURNALHDRID IN (select hdr.ID from ["+ plant +"_FINJOURNALDET] jd inner join ["+ plant +"_FINJOURNALHDR] hdr "
					+ "on hdr.ID=jd.JOURNALHDRID where jd.ACCOUNT_ID in ("+account+") AND ISNULL(jd.RECONCILIATION,'0') != '1') AND A.ACCOUNT_ID!='"+account+"'"
					+ "order by CAST((SUBSTRING(B.JOURNAL_DATE, 7, 4) + SUBSTRING(B.JOURNAL_DATE, 4, 2) + SUBSTRING(B.JOURNAL_DATE, 1, 2)) AS date) desc ";

					/*+ "from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"'";	*/
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}
	
	public List<LedgerDetails> getLedgerDetailsByAccountAllReconcile(String plant,String account)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select A.ID AS JOURNALHDRID,B.JOURNAL_DATE,A.ACCOUNT_NAME,A.DESCRIPTION,B.TRANSACTION_TYPE,B.TRANSACTION_ID,B.REFERENCE,A.DEBITS AS CREDITS,A.CREDITS AS DEBITS "			
					+ "FROM ["+ plant +"_FINJOURNALDET] AS A inner join ["+ plant +"_FINJOURNALHDR] B "
					+ "on B.ID=A.JOURNALHDRID where " 
					+ "A.JOURNALHDRID IN (select hdr.ID from ["+ plant +"_FINJOURNALDET] jd inner join ["+ plant +"_FINJOURNALHDR] hdr "
					+ "on hdr.ID=jd.JOURNALHDRID where jd.ACCOUNT_ID in ("+account+") AND ISNULL(jd.RECONCILIATION,'0') != '1') AND A.ACCOUNT_ID!='"+account+"' AND ISNULL(A.RECONCILIATION,'0') != '1' "
					+ "order by CAST((SUBSTRING(B.JOURNAL_DATE, 7, 4) + SUBSTRING(B.JOURNAL_DATE, 4, 2) + SUBSTRING(B.JOURNAL_DATE, 1, 2)) AS date) desc ";

					/*+ "from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"'";	*/
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}
	
	public List<LedgerDetails> getLedgerDetailsByAccountAllReconcileByRId(String plant,String account,String rid)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select A.ID AS JOURNALHDRID,B.JOURNAL_DATE,A.ACCOUNT_NAME,A.DESCRIPTION,B.TRANSACTION_TYPE,B.TRANSACTION_ID,B.REFERENCE,A.DEBITS AS CREDITS,A.CREDITS AS DEBITS "			
					+ "FROM ["+ plant +"_FINJOURNALDET] AS A inner join ["+ plant +"_FINJOURNALHDR] B "
					+ "on B.ID=A.JOURNALHDRID where " 
					+ "A.JOURNALHDRID IN (select hdr.ID from ["+ plant +"_FINJOURNALDET] jd inner join ["+ plant +"_FINJOURNALHDR] hdr "
					+ "on hdr.ID=jd.JOURNALHDRID where ISNULL(jd.RID,'0') = '"+rid+"') AND A.ACCOUNT_ID!='"+account+"' "
					+ "order by CAST((SUBSTRING(B.JOURNAL_DATE, 7, 4) + SUBSTRING(B.JOURNAL_DATE, 4, 2) + SUBSTRING(B.JOURNAL_DATE, 1, 2)) AS date) desc ";

					/*+ "from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"'";	*/
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}
	
	public int updatejournldet(String plant,String bankdate, String accountid, String hdrid, String user) throws Exception
	{
//		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			//query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET BANKDATE='"+bankdate+"',RECONCILIATION='1',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE JOURNALHDRID='"+hdrid+"' AND ACCOUNT_ID ='"+accountid+"'";
			query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET BANKDATE='"+bankdate+"',RECONCILIATION='1',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+user+"' WHERE JOURNALHDRID='"+hdrid+"'";
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
					   throw new SQLException("Updating journal detail failed, no rows affected.");
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
	
	public int updatejdetbyidforreconcile(String plant,String bankdate, String accountid, String hdrid, int rid,String user) throws Exception
	{
//		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			//query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET BANKDATE='"+bankdate+"',RECONCILIATION='1',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE JOURNALHDRID='"+hdrid+"' AND ACCOUNT_ID ='"+accountid+"'";
			query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET BANKDATE='"+bankdate+"',RID='"+rid+"',RECONCILIATION='1',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+user+"' WHERE ID='"+hdrid+"'";
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
					   throw new SQLException("Updating journal detail failed, no rows affected.");
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
	
	public int updatejdetbyidforreconcilewithoutdate(String plant, String accountid, String hdrid, int rid,String user) throws Exception
	{
//		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			//query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET BANKDATE='"+bankdate+"',RECONCILIATION='1',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE JOURNALHDRID='"+hdrid+"' AND ACCOUNT_ID ='"+accountid+"'";
			query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET RID='"+rid+"',RECONCILIATION='1',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+user+"' WHERE ID='"+hdrid+"'";
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
					   throw new SQLException("Updating journal detail failed, no rows affected.");
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
	
	public int updatereconciliationstatus(String plant,int rid,String user,int rstatus) throws Exception
	{
//		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			//query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET BANKDATE='"+bankdate+"',RECONCILIATION='1',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE JOURNALHDRID='"+hdrid+"' AND ACCOUNT_ID ='"+accountid+"'";
			query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET RID='0',RECONCILIATION='"+rstatus+"',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+user+"' WHERE RID='"+rid+"'";
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
					   throw new SQLException("Updating journal detail failed, no rows affected.");
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
	
	
	public List<LedgerDetailsRec> getLedgerDetailsByAccountAllRec(String plant,String account)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetailsRec> ledgerDetailsArr=new ArrayList<LedgerDetailsRec>();	

		try {	    	
			connection = DbBean.getConnection();	
			query= "select A.JOURNALHDRID,B.JOURNAL_DATE,A.ACCOUNT_NAME,A.DESCRIPTION,ISNULL(A.BANKDATE,'') AS BANKDATE,B.TRANSACTION_TYPE,B.TRANSACTION_ID,B.REFERENCE,A.DEBITS AS CREDITS,A.CREDITS AS DEBITS "			
					+ "FROM ["+ plant +"_FINJOURNALDET] AS A inner join ["+ plant +"_FINJOURNALHDR] B "
					+ "on B.ID=A.JOURNALHDRID where " 
					+ "A.JOURNALHDRID IN (select hdr.ID from ["+ plant +"_FINJOURNALDET] jd inner join ["+ plant +"_FINJOURNALHDR] hdr "
					+ "on hdr.ID=jd.JOURNALHDRID where jd.ACCOUNT_ID in ("+account+") AND ISNULL(jd.RECONCILIATION,'0') = '1') AND A.ACCOUNT_ID!='"+account+"'"
					+ "order by CAST((SUBSTRING(B.JOURNAL_DATE, 7, 4) + SUBSTRING(B.JOURNAL_DATE, 4, 2) + SUBSTRING(B.JOURNAL_DATE, 1, 2)) AS date) desc ";
			/*query= "select A.JOURNALHDRID,B.JOURNAL_DATE,A.ACCOUNT_NAME,A.DESCRIPTION,ISNULL(A.BANKDATE,'') AS BANKDATE,B.TRANSACTION_TYPE,B.TRANSACTION_ID,B.REFERENCE,A.DEBITS AS CREDITS,A.CREDITS AS DEBITS "			
					+ "FROM ["+ plant +"_FINJOURNALDET] AS A inner join ["+ plant +"_FINJOURNALHDR] B "
					+ "on B.ID=A.JOURNALHDRID where " 
					+ "A.JOURNALHDRID IN (select hdr.ID from ["+ plant +"_FINJOURNALDET] jd inner join ["+ plant +"_FINJOURNALHDR] hdr "
					+ "on hdr.ID=jd.JOURNALHDRID where jd.ACCOUNT_ID in ("+account+")) AND A.ACCOUNT_ID!='"+account+"'"
					+ "order by CAST((SUBSTRING(B.JOURNAL_DATE, 7, 4) + SUBSTRING(B.JOURNAL_DATE, 4, 2) + SUBSTRING(B.JOURNAL_DATE, 1, 2)) AS date) desc ";*/
					/*+ "from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"'";	*/
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetailsRec ledgerDetail=new LedgerDetailsRec();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}
	
	
	
	public double getbankbalancecbookbal(String plant,String accountid)throws Exception {	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    double output=0.0;
	    double credit=0.0;
	    double debit=0.0;

		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE ACCOUNT_ID='"+accountid+"'";	
			
			if(connection != null){	
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
		                ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                credit = credit+journalDetail.getCREDITS();
		                debit = debit+journalDetail.getDEBITS();
		            }	
				   
				   output = debit - credit;
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
		
		return output;
	}
	
	public double getbankbalancecbookbalasondate(String plant,String accountid,String todate)throws Exception {	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    double output=0.0;
	    double credit=0.0;
	    double debit=0.0;

		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] AS A JOIN ["+ plant +"_"+TABLE_HEADER+"]  AS B ON A.JOURNALHDRID = B.ID WHERE A.ACCOUNT_ID='"+accountid+"' AND CONVERT(DATETIME, ISNULL(A.BANKDATE,B.JOURNAL_DATE), 103)  <= CONVERT(DATETIME,'"+todate+"', 103)";	
			
			if(connection != null){	
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
		                ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                credit = credit+journalDetail.getCREDITS();
		                debit = debit+journalDetail.getDEBITS();
		            }	
				   
				   output = debit - credit;
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
		
		return output;
	}
	
	public double getpendingnotreflectedinbank(String plant,String accountid)throws Exception {	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    double output=0.0;
	    double credit=0.0;
	    double debit=0.0;

		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE ACCOUNT_ID='"+accountid+"' AND ISNULL(RECONCILIATION,'0') != '1'";
			
			if(connection != null){	
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
		                ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                credit = credit+journalDetail.getCREDITS();
		                debit = debit+journalDetail.getDEBITS();
		            }	
				   
				   output = debit - credit;
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
		
		return output;
	}
	
	public double getbalanceasperbank(String plant,String accountid)throws Exception {	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    double output=0.0;
	    double credit=0.0;
	    double debit=0.0;

		try {	    	
			connection = DbBean.getConnection();	
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE ACCOUNT_ID='"+accountid+"' AND ISNULL(RECONCILIATION,'0') = '1'";
			
			if(connection != null){	
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
		                ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                credit = credit+journalDetail.getCREDITS();
		                debit = debit+journalDetail.getDEBITS();
		            }	
				   
				   output = debit - credit;
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
		
		return output;
	}
	
	
	public boolean isAccountExisit(String plant,String account) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    boolean status = false;
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select * from ["+ plant +"_"+TABLE_DETAIL+"] WHERE ACCOUNT_NAME = '"+account+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
	                }   	
			this.mLogger.query(this.printQuery, query);	
				
			}	
					
			if(journalsDetails.size() > 0) {
				status = true;
			}
					
		} catch (Exception e) {	
			this.mLogger.exception(this.printLog, "", e);	
				
			throw e;	
		} finally {	
			if (connection != null) {	
				DbBean.closeConnection(connection);	
			}	
		}	
		return status;	
	}	
	
	public List<JournalDetail> getJournalDetailsByChartOfAccountIdAndAll(String plant,String ChartAccountId) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where jd.ACCOUNT_ID ='"+ChartAccountId+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}	
		
	public List<JournalDetail> getJournalDetailsByAccountDetailTypeAndAll(String plant,String accountdetailtypes) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTDETAILTYPE in "+accountdetailtypes;	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}	
	
	
	public List<JournalDetail> getJournalDetailsBySalesRecipt(String plant,int sid) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "SELECT JD.* FROM "+ plant +"_FINJOURNALHDR AS JH JOIN "+ plant +"_FINJOURNALDET AS JD ON JH.ID=JD.JOURNALHDRID JOIN "+ plant +"_POSAMOUNTBYPAYMODE AS PM ON JH.TRANSACTION_ID = PM.ID AND JH.TRANSACTION_TYPE = 'POS_RECEIVE' WHERE PM.HDRID='"+sid+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}	
	
	
	public List<JournalDetail> getJournalDetailsByHdrId(String plant,int id) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "SELECT JD.* FROM "+ plant +"_FINJOURNALDET AS JD WHERE JD.JOURNALHDRID='"+id+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}
	
	public JournalDetail getJournalDetailsById(String plant,int id) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    JournalDetail journalDetail=new JournalDetail();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "SELECT JD.* FROM "+ plant +"_FINJOURNALDET AS JD WHERE JD.ID='"+id+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
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
		return journalDetail;	
	}
	
	public List<JournalDetail> getJournalDetailsBySalesBankin(String plant,int sid) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query="SELECT JD.* FROM "+ plant +"_FINJOURNALHDR AS JH JOIN "+ plant +"_FINJOURNALDET AS JD ON JH.ID=JD.JOURNALHDRID JOIN "+ plant +"_POSBANKINBYPAYMODE AS PM ON JH.TRANSACTION_ID = PM.ID AND JH.TRANSACTION_TYPE = 'POS_BANK_IN' WHERE PM.HDRID='"+sid+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}	
		
	public Journal getJournalByTransactionId(String plant,String transid)throws Exception {	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    Journal journal=new Journal();	
	    JournalHeader journalHeader=new JournalHeader();	
	    List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
	    List<JournalAttachment> journalAttachmentList=new ArrayList<JournalAttachment>();	
		try {	    	
			connection = DbBean.getConnection();		
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_ID='"+transid+"'";
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);	
	                }   	
			this.mLogger.query(this.printQuery, query);	
				
			}	
				
				query = "SELECT * FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID="+journalHeader.getID();	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalDetail journalDetail=new JournalDetail();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
		                    journalDetailList.add(journalDetail);	
		                }	
				}	
				query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE JOURNALHDRID="+journalHeader.getID();	
				if(connection != null){	
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
					   ResultSet rst = ps.executeQuery();	
					   while (rst.next()) {	
						   JournalAttachment journalAttach=new JournalAttachment();	
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalAttach);	
		                    journalAttachmentList.add(journalAttach);	
		                }	
				}	
				journal.setJournalHeader(journalHeader);	
				journal.setJournalDetails(journalDetailList);	
				journal.setJournalAttachment(journalAttachmentList);	
		} catch (Exception e) {	
			this.mLogger.exception(this.printLog, "", e);	
				
			throw e;	
		} finally {	
			if (connection != null) {	
				DbBean.closeConnection(connection);	
			}	
		}	
		return journal;	
	}
	
	public List<JournalDetail> getJournalDetailsByActTypeNoActDetAndDATE(String plant,String fromDate,String toDate,String accounttypes,String accountdetailtypes) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND ca.ACCOUNTDETAILTYPE NOT IN "+accountdetailtypes;	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}
	
	public List<LedgerDetails> getDetailsByActTypeNoActDetAndDATELedger(String plant,String fromDate,String toDate,String accounttypes,String accountdetailtypes) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();		
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,JOURNAL_DATE,jd.ACCOUNT_NAME,jd.DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND ca.ACCOUNTDETAILTYPE NOT IN "+accountdetailtypes;	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}	
	
	
	public List<LedgerDetails> getDetailsByActDetAndDATELedger(String plant,String fromDate,String toDate,String accountdetailtypes) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();		
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,JOURNAL_DATE,jd.ACCOUNT_NAME,jd.DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTDETAILTYPE IN "+accountdetailtypes;	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}

	
	
	public List<JournalDetail> getJournalDetailsByAccountTypeAndDATEproid(String plant,String fromDate,String toDate,String accounttypes,String projId) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			/*query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where CAST((SUBSTRING(hdr.JOURNAL_DATE, 7, 4) + SUBSTRING(hdr.JOURNAL_DATE, 4, 2) + SUBSTRING(hdr.JOURNAL_DATE, 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes;	
			this.mLogger.query(this.printQuery, query);	*/
			
			if(projId.isEmpty())
			{
				
				query = "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "
						+ "inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_finbillhdr] B ON A.TRANSACTION_ID=B.BILL WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND (TRANSACTION_TYPE LIKE '%BILL%' OR TRANSACTION_TYPE LIKE '%BILL_REVERSAL%')"+ 
						" AND B.PROJECTID = '0' UNION ALL "+
						 
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_finexpenseshdr] B ON A.TRANSACTION_ID=B.ID WHERE"+
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND (TRANSACTION_TYPE LIKE '%EXPENSE%' OR TRANSACTION_TYPE LIKE '%EXPENSE FOR PO%')"+
						" AND B.PROJECTID = '0' UNION ALL "+
						
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_FINVENDORCREDITNOTEHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+"  AND TRANSACTION_TYPE LIKE '%SUPPLIERCREDITNOTES%'"+
						" AND B.PROJECTID = '0' UNION ALL "+
					
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_FINPAYMENTHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND TRANSACTION_TYPE LIKE '%PURCHASEPAYMENT%'"+
						" AND B.PROJECTID = '0' UNION ALL "+
						
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_fininvoicehdr] B ON A.TRANSACTION_ID=B.invoice WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND (TRANSACTION_TYPE LIKE '%INVOICE%' OR TRANSACTION_TYPE LIKE '%INOVICE_REVERSAL%' OR TRANSACTION_TYPE LIKE '%COSTOFGOODSOLD%')"+
						" AND B.PROJECTID = '0' UNION ALL "+
						
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_FINCUSTOMERCREDITNOTEHDR] B ON A.TRANSACTION_ID=B.creditnote WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND TRANSACTION_TYPE LIKE '%CUSTOMERCREDITNOTES%'"+ 
						" AND B.PROJECTID = '0' UNION ALL "+
						
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_FINRECEIVEHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND TRANSACTION_TYPE LIKE '%SALESPAYMENT%'"+ 
						" AND B.PROJECTID = '0'";
			
						
			}
			else 
			{

						 
				query = "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "
						+ "inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_finbillhdr] B ON A.TRANSACTION_ID=B.BILL WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND (TRANSACTION_TYPE LIKE '%BILL%' OR TRANSACTION_TYPE LIKE '%BILL_REVERSAL%')"+ 
						" AND B.PROJECTID = '"+projId+"' UNION ALL "+
						 
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_finexpenseshdr] B ON A.TRANSACTION_ID=B.ID WHERE"+
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND (TRANSACTION_TYPE LIKE '%EXPENSE%' OR TRANSACTION_TYPE LIKE '%EXPENSE FOR PO%')"+
						" AND B.PROJECTID = '"+projId+"' UNION ALL "+
						
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_FINVENDORCREDITNOTEHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+"  AND TRANSACTION_TYPE LIKE '%SUPPLIERCREDITNOTES%'"+
						" AND B.PROJECTID = '"+projId+"' UNION ALL "+
					
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_FINPAYMENTHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND TRANSACTION_TYPE LIKE '%PURCHASEPAYMENT%'"+
						" AND B.PROJECTID = '"+projId+"' UNION ALL "+
						
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_fininvoicehdr] B ON A.TRANSACTION_ID=B.invoice WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND (TRANSACTION_TYPE LIKE '%INVOICE%' OR TRANSACTION_TYPE LIKE '%INOVICE_REVERSAL%' OR TRANSACTION_TYPE LIKE '%COSTOFGOODSOLD%')"+
						" AND B.PROJECTID = '"+projId+"' UNION ALL "+
						
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_FINCUSTOMERCREDITNOTEHDR] B ON A.TRANSACTION_ID=B.creditnote WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND TRANSACTION_TYPE LIKE '%CUSTOMERCREDITNOTES%'"+ 
						" AND B.PROJECTID = '"+projId+"' UNION ALL "+
						
						"select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID "+ 
						"inner join ["+ plant +"_FINJOURNALHDR] A on A.ID=jd.JOURNALHDRID JOIN  ["+ plant +"_FINRECEIVEHDR] B ON A.TRANSACTION_ID=B.ID WHERE"+ 
						" CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)"+ 
						" BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ca.ACCOUNTTYPE in "+accounttypes+" AND TRANSACTION_TYPE LIKE '%SALESPAYMENT%'"+ 
						" AND B.PROJECTID = '"+projId+"'";
				
			}
			
			System.out.println(query);
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
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
		return journalsDetails;	
	}	
	
	public List<LedgerDetails> getLedgerDetailsByTransactionId(String plant,String billNo)throws Exception {	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
		try {	    	
				
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,JOURNAL_DATE,ACCOUNT_NAME,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where hdr.TRANSACTION_ID = '"	
					+billNo+"' order by JOURNALHDRID desc";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}
	
	public List<LedgerDetails> getLedgerDetailsByJournalId(String plant,String journalId)throws Exception {	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	
	    List<LedgerDetails> ledgerDetailsArr=new ArrayList<LedgerDetails>();	
	   	
		try {	    	
				
			connection = DbBean.getConnection();	
			query= "select JOURNALHDRID,JOURNAL_DATE,ACCOUNT_NAME,DESCRIPTION,TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where hdr.ID = '"	
					+journalId+"' order by JOURNALHDRID desc";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   LedgerDetails ledgerDetail=new LedgerDetails();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, ledgerDetail);	
	                    ledgerDetailsArr.add(ledgerDetail);	
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
		return ledgerDetailsArr;	
	}
	
	public Double getcurretbalByChartOfAccountId(String plant,String ChartAccountId) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	  	Double opbal=0.0;
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where jd.ACCOUNT_ID ='"+ChartAccountId+"'";	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
	                }   	
			this.mLogger.query(this.printQuery, query);	
				
			}	
			Double debit=0.00;
			Double credit=0.00;
			for(JournalDetail jDet:journalsDetails)
			{
				debit+=jDet.getDEBITS();
				credit+=jDet.getCREDITS();
			}		
			opbal = debit - credit;	
		} catch (Exception e) {	
			this.mLogger.exception(this.printLog, "", e);	
				
			throw e;	
		} finally {	
			if (connection != null) {	
				DbBean.closeConnection(connection);	
			}	
		}	
		return opbal;	
	}	
	
	public double getJournalDetailsForCash(String plant,String ChartAccountId) throws Exception{	
//		boolean flag = false;	
//		int journalHdrId = 0;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
	    Double totalCashInHand = 0.0;
	    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
	   	
		try {	    	
			connection = DbBean.getConnection();	
			query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where jd.ACCOUNT_ID IN "+ChartAccountId;	
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ResultSet rst = ps.executeQuery();	
				   while (rst.next()) {	
					   JournalDetail journalDetail=new JournalDetail();	
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
	                    journalsDetails.add(journalDetail);	
	                }   	
				   Double debit=0.00;
					Double credit=0.00;
					for(JournalDetail journalDet:journalsDetails)
					{
						debit+=journalDet.getDEBITS();
						credit+=journalDet.getCREDITS();
					}
					totalCashInHand=debit-credit;
					//return totalCashInHand;
				
			}	
					
					
		} catch (Exception e) {	
			this.mLogger.exception(this.printLog, "", e);	
				
			throw e;	
		} finally {	
			if (connection != null) {	
				DbBean.closeConnection(connection);	
			}	
		}	
		return totalCashInHand;
	}	
		



public Double getJournalDetailsBybank(String plant,String accountdetailtypes,String notaccdetail) throws Exception{	
//	boolean flag = false;	
//	int journalHdrId = 0;	
	Connection connection = null;	
	PreparedStatement ps = null;	
	Double totalCashAtBank=0.0;
    String query = "";	
  	
    List<JournalDetail> journalsDetails=new ArrayList<JournalDetail>();	
   	
	try {	    	
		connection = DbBean.getConnection();	
		query= "select jd.* from ["+ plant +"_"+TABLE_DETAIL+"] jd inner join ["+ plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTDETAILTYPE in "+accountdetailtypes+" AND ca.ID NOT IN"+notaccdetail;	
		this.mLogger.query(this.printQuery, query);	
		if(connection != null){	
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
			   ResultSet rst = ps.executeQuery();	
			   while (rst.next()) {	
				   JournalDetail journalDetail=new JournalDetail();	
                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
                    journalsDetails.add(journalDetail);	
                }   	
		this.mLogger.query(this.printQuery, query);	
		
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalsDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		totalCashAtBank=debit-credit;
		
			
		}	
				
				
	} catch (Exception e) {	
		this.mLogger.exception(this.printLog, "", e);	
			
		throw e;	
	} finally {	
		if (connection != null) {	
			DbBean.closeConnection(connection);	
		}	
	}	
	return totalCashAtBank;
}
public boolean isExisitJournalDet(Hashtable ht) throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append("COUNT(*) ");
		sql.append(" ");
		sql.append(" FROM " + "["+ht.get("PLANT")+"_"+TABLE_DETAIL+"]");
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

public boolean updateJournalDet(String query, Hashtable htCondition, String extCond)
		throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" UPDATE " + "["
				+ htCondition.get("PLANT") + "_" + TABLE_DETAIL + "]");
		sql.append(" ");
		sql.append(query);
		sql.append(" WHERE ");
		String conditon = formCondition(htCondition);
		sql.append(conditon);

		if (extCond.length() != 0) {
			sql.append(extCond);
		}

		flag = updateData(con, sql.toString());
		this.mLogger.query(this.printQuery, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con);
	}
	return flag;
}

public boolean getisexistTridandTrtype(String plant,int TranId,String Trantype)throws Exception {	

	boolean result = false;	
	Connection connection = null;	
	PreparedStatement ps = null;	
    String query = "";	
    List<JournalHeader> JournalHeaderList = new ArrayList<JournalHeader>();
  ;	
	try {	    	
		connection = DbBean.getConnection();	
		query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_ID ='"+TranId+"' AND TRANSACTION_TYPE ='"+Trantype+"'";
		if(connection != null){	
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
			   ResultSet rst = ps.executeQuery();	
			   while (rst.next()) {	
				   JournalHeader journalHeader=new JournalHeader();
                   ResultSetToObjectMap.loadResultSetIntoObject(rst, journalHeader);	
				   JournalHeaderList.add(journalHeader);
                }   	
		this.mLogger.query(this.printQuery, query);	
		
		if(JournalHeaderList.size() > 0)
			result = true;
		}	
				
	} catch (Exception e) {	
		this.mLogger.exception(this.printLog, "", e);	
		throw e;	
	} finally {	
		if (connection != null) {	
			DbBean.closeConnection(connection);	
		}	
	}	
	return result;	
}


public int journalreconciliationdelete(String plant,int rid,String user) throws Exception
{
//	boolean flag = false;
	int HdrId = 0;
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
	try {	    
		connection = DbBean.getConnection();
		query="UPDATE ["+ plant+"_"+TABLE_DETAIL+"] SET RID='0',RECONCILIATION='0',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+user+"' WHERE  RID='"+rid+"'";
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
				   throw new SQLException("Updating journal detail failed, no rows affected.");
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


public List<Journal> getJournalsByBankDateWithoutAttach(String plant,String fromDate,String toDate)throws Exception {	
//	boolean flag = false;	
//	int journalHdrId = 0;	
	Connection connection = null;	
	PreparedStatement ps = null;	
    String query = "";	
    List<Journal> journals=new ArrayList<Journal>();	
    	
    List<JournalHeader> journalHeaderList=new ArrayList<JournalHeader>();	
    	
    //List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
   // List<JournalAttachment> journalAttachmentList=new ArrayList<JournalAttachment>();	
	try {	    	
		connection = DbBean.getConnection();
		if(fromDate.length() > 0 && toDate.length() > 0){
		query = "SELECT A.* FROM ["+ plant +"_"+TABLE_DETAIL+"] AS A JOIN ["+ plant +"_"+TABLE_HEADER+"] AS B ON A.JOURNALHDRID = B.ID WHERE CAST((SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 1, 2)) AS date) "	
				+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}else if(fromDate.length() > 0 && toDate.length() == 0) {
			query = "SELECT A.* FROM ["+ plant +"_"+TABLE_DETAIL+"] AS A JOIN ["+ plant +"_"+TABLE_HEADER+"] AS B ON A.JOURNALHDRID = B.ID WHERE CAST((SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 1, 2)) AS date) >= '"+fromDate+"'";
		}else if(fromDate.length() == 0 && toDate.length() > 0){
			query = "SELECT A.* FROM ["+ plant +"_"+TABLE_DETAIL+"] AS A JOIN ["+ plant +"_"+TABLE_HEADER+"] AS B ON A.JOURNALHDRID = B.ID WHERE CAST((SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(A.BANKDATE,B.JOURNAL_DATE), 1, 2)) AS date) <= '"+toDate+"'";
		}else {
			query = "SELECT A.* FROM ["+ plant +"_"+TABLE_DETAIL+"]";
		}
		
		List<JournalDetail> journalDetailList=new ArrayList<JournalDetail>();	
			
		if(connection != null){	
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
			   ResultSet rst = ps.executeQuery();	
			   while (rst.next()) {	
				   JournalDetail journalDetail=new JournalDetail();	
                   ResultSetToObjectMap.loadResultSetIntoObject(rst, journalDetail);	
                   journalDetailList.add(journalDetail);	
                }   	
		this.mLogger.query(this.printQuery, query);	
			
		}	
		
		Journal journal=new Journal();	
		journal.setJournalDetails(journalDetailList);	
		journals.add(journal);	

				
				
	} catch (Exception e) {	
		this.mLogger.exception(this.printLog, "", e);	
			
		throw e;	
	} finally {	
		if (connection != null) {	
			DbBean.closeConnection(connection);	
		}	
	}	
	return journals;	
}

public List<TrialBalanceDetails> getTrialBalanceDetBankDateByAccountByType(String plant,String account,String fromDate,String toDate)throws Exception {	
//	boolean flag = false;	
//	int journalHdrId = 0;	
	Connection connection = null;	
	PreparedStatement ps = null;	
    String query = "";	
    String subquery = "", typequery = "";	
  	
    List<TrialBalanceDetails> trialBalanceDetails=new ArrayList<TrialBalanceDetails>();	
   	
   	
	try {	    	
		connection = DbBean.getConnection();	
		
		
		if(fromDate.length() > 0 && toDate.length() == 0) {
			typequery= "SELECT CASE WHEN (DEBITS = (SELECT ISNULL(SUM(CREDITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID)  - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "WHEN (CREDITS = (SELECT ISNULL(SUM(DEBITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID) - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "ELSE  'SUB' END AS TRANSACTION_TYPE, JOURNALHDRID "	
					+ "FROM ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID where det1.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 1, 2)) AS date) "	
					+">= '"+fromDate+"' GROUP BY DEBITS,JOURNALHDRID,CREDITS";	
				
			subquery= "select det.JOURNALHDRID from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+">= '"+fromDate+"'";	
				
			query= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,CREDITS as DEBITS,DEBITS as CREDITS,ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "	
					+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID WHERE det.ACCOUNT_ID<>'"+account+"' AND det.JOURNALHDRID IN ("+subquery+") AND D.TRANSACTION_TYPE='MAIN' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+">= '"+fromDate+"'";	
				
			query+= " UNION ALL ";	
				
			query+= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS, "	
					+ "ISNULL((select (SELECT  ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] A WHERE A.JOURNALHDRID = HDR1.ID" 	
					+ " AND ((A.CREDITS = (SELECT SUM(DEBITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID)  - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' ) ) OR" 	
					+ " (A.DEBITS = (SELECT SUM(CREDITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID) - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )))"	
					+ " ) ACCOUNT_NAME"	
					+ " from ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID" 	
					+ " where det1.ACCOUNT_ID='"+account+"' AND "	
					+ " CAST((SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 1, 2)) AS date)" 	
					+ " >= '"+fromDate+"' and det1.JOURNALHDRID = det.JOURNALHDRID"	
					+ "),'') as ACCOUNT_NAME "	
					+ "from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "					
					+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND D.TRANSACTION_TYPE='SUB' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+">= '"+fromDate+"'";	
			
		}else if(fromDate.length() == 0 && toDate.length() > 0) {
			typequery= "SELECT CASE WHEN (DEBITS = (SELECT ISNULL(SUM(CREDITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID)  - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "WHEN (CREDITS = (SELECT ISNULL(SUM(DEBITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID) - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "ELSE  'SUB' END AS TRANSACTION_TYPE, JOURNALHDRID "	
					+ "FROM ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID where det1.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 1, 2)) AS date) "	
					+"<= '"+toDate+"' GROUP BY DEBITS,JOURNALHDRID,CREDITS";	
				
			subquery= "select det.JOURNALHDRID from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"<= '"+toDate+"'";	
				
			query= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,CREDITS as DEBITS,DEBITS as CREDITS,ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "	
					+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID WHERE det.ACCOUNT_ID<>'"+account+"' AND det.JOURNALHDRID IN ("+subquery+") AND D.TRANSACTION_TYPE='MAIN' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"<= '"+toDate+"'";	
				
			query+= " UNION ALL ";	
				
			query+= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS, "	
					+ "ISNULL((select (SELECT  ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] A WHERE A.JOURNALHDRID = HDR1.ID" 	
					+ " AND ((A.CREDITS = (SELECT SUM(DEBITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID)  - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' ) ) OR" 	
					+ " (A.DEBITS = (SELECT SUM(CREDITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID) - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )))"	
					+ " ) ACCOUNT_NAME"	
					+ " from ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID" 	
					+ " where det1.ACCOUNT_ID='"+account+"' AND "	
					+ " CAST((SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 1, 2)) AS date)" 	
					+ " <= '"+toDate+"' and det1.JOURNALHDRID = det.JOURNALHDRID"	
					+ "),'') as ACCOUNT_NAME "	
					+ "from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "					
					+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND D.TRANSACTION_TYPE='SUB' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"<= '"+toDate+"'";	
			
		}else {
			typequery= "SELECT CASE WHEN (DEBITS = (SELECT ISNULL(SUM(CREDITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID)  - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "WHEN (CREDITS = (SELECT ISNULL(SUM(DEBITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID) - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
					+ "ELSE  'SUB' END AS TRANSACTION_TYPE, JOURNALHDRID "	
					+ "FROM ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID where det1.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"' GROUP BY DEBITS,JOURNALHDRID,CREDITS";	
				
			subquery= "select det.JOURNALHDRID from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
				
			query= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,CREDITS as DEBITS,DEBITS as CREDITS,ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "	
					+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID WHERE det.ACCOUNT_ID<>'"+account+"' AND det.JOURNALHDRID IN ("+subquery+") AND D.TRANSACTION_TYPE='MAIN' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
				
			query+= " UNION ALL ";	
				
			query+= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS, "	
					+ "ISNULL((select (SELECT  ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] A WHERE A.JOURNALHDRID = HDR1.ID" 	
					+ " AND ((A.CREDITS = (SELECT SUM(DEBITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID)  - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' ) ) OR" 	
					+ " (A.DEBITS = (SELECT SUM(CREDITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID) - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )))"	
					+ " ) ACCOUNT_NAME"	
					+ " from ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID" 	
					+ " where det1.ACCOUNT_ID='"+account+"' AND "	
					+ " CAST((SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 1, 2)) AS date)" 	
					+ " BETWEEN '"+fromDate+"' AND '"+toDate+"' and det1.JOURNALHDRID = det.JOURNALHDRID"	
					+ "),'') as ACCOUNT_NAME "	
					+ "from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "					
					+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND D.TRANSACTION_TYPE='SUB' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
					+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
		}
		
		
		/*typequery= "SELECT CASE WHEN (DEBITS = (SELECT ISNULL(SUM(CREDITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID)  - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
				+ "WHEN (CREDITS = (SELECT ISNULL(SUM(DEBITS),0) FROM ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID) - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )) THEN 'MAIN' "	
				+ "ELSE  'SUB' END AS TRANSACTION_TYPE, JOURNALHDRID "	
				+ "FROM ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID where det1.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 1, 2)) AS date) "	
				+"BETWEEN '"+fromDate+"' AND '"+toDate+"' GROUP BY DEBITS,JOURNALHDRID,CREDITS";	
			
		subquery= "select det.JOURNALHDRID from ["+ plant +"_"+TABLE_DETAIL+"] det inner join ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
				+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
			
		query= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,CREDITS as DEBITS,DEBITS as CREDITS,ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "	
				+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID WHERE det.ACCOUNT_ID<>'"+account+"' AND det.JOURNALHDRID IN ("+subquery+") AND D.TRANSACTION_TYPE='MAIN' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
				+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	
			
		query+= " UNION ALL ";	
			
		query+= "select det.JOURNALHDRID,JOURNAL_DATE,DESCRIPTION,hdr.TRANSACTION_TYPE,TRANSACTION_ID,REFERENCE,DEBITS,CREDITS, "	
				+ "ISNULL((select (SELECT  ACCOUNT_NAME from ["+ plant +"_"+TABLE_DETAIL+"] A WHERE A.JOURNALHDRID = HDR1.ID" 	
				+ " AND ((A.CREDITS = (SELECT SUM(DEBITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID)  - (SELECT ISNULL(SUM(CREDITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' ) ) OR" 	
				+ " (A.DEBITS = (SELECT SUM(CREDITS) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = A.JOURNALHDRID) - (SELECT ISNULL(SUM(DEBITS),0) from ["+ plant +"_"+TABLE_DETAIL+"] WHERE JOURNALHDRID = det1.JOURNALHDRID AND ACCOUNT_ID <> '"+account+"' AND ACCOUNT_NAME LIKE '%Discounts%' )))"	
				+ " ) ACCOUNT_NAME"	
				+ " from ["+ plant +"_"+TABLE_DETAIL+"] det1 inner join ["+ plant +"_"+TABLE_HEADER+"] hdr1 on hdr1.ID=det1.JOURNALHDRID" 	
				+ " where det1.ACCOUNT_ID='"+account+"' AND "	
				+ " CAST((SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det1.BANKDATE,hdr1.JOURNAL_DATE), 1, 2)) AS date)" 	
				+ " BETWEEN '"+fromDate+"' AND '"+toDate+"' and det1.JOURNALHDRID = det.JOURNALHDRID"	
				+ "),'') as ACCOUNT_NAME "	
				+ "from ["+ plant +"_"+TABLE_DETAIL+"] det JOIN ["+ plant +"_"+TABLE_HEADER+"] hdr on hdr.ID=det.JOURNALHDRID "					
				+ "JOIN  ("+typequery+")D ON det.JOURNALHDRID = D.JOURNALHDRID where det.ACCOUNT_ID='"+account+"' AND D.TRANSACTION_TYPE='SUB' AND CAST((SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 7, 4) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 4, 2) + SUBSTRING(ISNULL(det.BANKDATE,hdr.JOURNAL_DATE), 1, 2)) AS date) "	
				+"BETWEEN '"+fromDate+"' AND '"+toDate+"'";	*/
			
		this.mLogger.query(this.printQuery, query);	
		if(connection != null){	
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
			   ResultSet rst = ps.executeQuery();	
			   while (rst.next()) {	
				   TrialBalanceDetails trialBalanceDetail=new TrialBalanceDetails();	
                    ResultSetToObjectMap.loadResultSetIntoObject(rst, trialBalanceDetail);	
                    trialBalanceDetails.add(trialBalanceDetail);	
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
	return trialBalanceDetails;	
}

public boolean isjournalExisit(String plant,String trantype,String tranid) throws Exception{	
//	boolean flag = false;	
//	int journalHdrId = 0;	
	Connection connection = null;	
	PreparedStatement ps = null;	
    String query = "";	
    boolean status = false;
    List<JournalHeader> journalshdrlist=new ArrayList<JournalHeader>();	
   	
	try {	    	
		connection = DbBean.getConnection();	
		query= "select * from ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_TYPE = '"+trantype+"' AND TRANSACTION_ID = '"+tranid+"'";	
		this.mLogger.query(this.printQuery, query);	
		if(connection != null){	
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
			   ResultSet rst = ps.executeQuery();	
			   while (rst.next()) {	
				   JournalHeader journalshdr=new JournalHeader();	
                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalshdr);	
                    journalshdrlist.add(journalshdr);	
                }   	
		this.mLogger.query(this.printQuery, query);	
			
		}	
				
		if(journalshdrlist.size() > 0) {
			status = true;
		}
				
	} catch (Exception e) {	
		this.mLogger.exception(this.printLog, "", e);	
			
		throw e;	
	} finally {	
		if (connection != null) {	
			DbBean.closeConnection(connection);	
		}	
	}	
	return status;	
}	

public int getjid(String plant,String trantype,String tranid) throws Exception {	
//	boolean flag = false;	
	int jid = 0;	
	Connection connection = null;	
	PreparedStatement ps = null;	
    String query = "";	
	try {	    	
		connection = DbBean.getConnection();	
		query= "select * from ["+ plant +"_"+TABLE_HEADER+"] WHERE TRANSACTION_TYPE = '"+trantype+"' AND TRANSACTION_ID = '"+tranid+"'";	
		this.mLogger.query(this.printQuery, query);	
		if(connection != null){	
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
			   ResultSet rst = ps.executeQuery();	
			   while (rst.next()) {	
				   JournalHeader journalshdr=new JournalHeader();	
                    ResultSetToObjectMap.loadResultSetIntoObject(rst, journalshdr);	
                    jid = journalshdr.getID();
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
	return jid;	
}

public ArrayList getJournalSummaryView(Hashtable ht, String afrmDate,
		String atoDate, String plant) {
	ArrayList arrList = new ArrayList();
	String sCondition = "",dtCondStr="",extraCon="";
	StringBuffer sql;
	 Hashtable htData = new Hashtable();
	try {

        		dtCondStr ="and ISNULL(JOURNAL_DATE,'')<>'' AND CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date)";
        		extraCon= "order by CAST((SUBSTRING(JOURNAL_DATE, 7, 4) + SUBSTRING(JOURNAL_DATE, 4, 2) + SUBSTRING(JOURNAL_DATE, 1, 2)) AS date) DESC,ID DESC ";
		       
				   if (afrmDate.length() > 0) {
	              	sCondition = sCondition + dtCondStr + "  >= '" 
	  						+ afrmDate
	  						+ "'  ";
	  				if (atoDate.length() > 0) {
	  					sCondition = sCondition +dtCondStr+ " <= '" 
	  					+ atoDate
	  					+ "'  ";
	  				}
	  			  } else {
	  				if (atoDate.length() > 0) {
	  					sCondition = sCondition +dtCondStr+ " <= '" 
	  					+ atoDate
	  					+ "'  ";
	  				}
	  		     	}   
                 
		           sql = new StringBuffer("select D.JOURNAL_DATE,D.ID,REFERENCE,D.JOURNAL_STATUS,D.NOTE,D.CRBY,D.TOTAL_AMOUNT");				           
		           sql.append(" from " + plant +"_FINJOURNALHDR D WHERE D.PLANT='"+ plant+"'" + sCondition);
		           
		           if (ht.get("REFERENCE") != null) {
	  					  sql.append(" AND REFERENCE = '" + ht.get("REFERENCE") + "'");
	  				    }
				   if (ht.get("TRANSACTION_TYPE") != null) {
	  					  sql.append(" AND TRANSACTION_TYPE = '" + ht.get("TRANSACTION_TYPE") + "'");
	  				    }
		           
				  arrList = this.selectForReport(sql.toString(), htData, extraCon);
   

	 }catch (Exception e) {
		this.mLogger.exception(this.printLog,
				"Exception :repportUtil :: getJournalSummaryView:", e);
	}
	return arrList;
}

public ArrayList selectForReport(String query, Hashtable ht, String extCond)
		throws Exception {
//	boolean flag = false;
	ArrayList al = new ArrayList();
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(query);
		String conditon = "";
		if (ht.size() > 0) {
			sql.append(" AND ");
			conditon = formCondition(ht);
			sql.append(" " + conditon);
		}

		if (extCond.length() > 0) {
			sql.append("  ");

			sql.append(" " + extCond);
		}
		this.mLogger.query(this.printQuery, sql.toString());

		al = selectData(con, sql.toString());
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

public List<ReconciliationPojo> getJournalDetailsByAccountTodateDeposits(String plant,String account,String rid, String afrmDate,
		String atoDate) throws Exception{	
//	boolean flag = false;	
//	int journalHdrId = 0;	
	Connection connection = null;	
	PreparedStatement ps = null;	
	String sCondition = "",dtCondStr="", query = "";	
  	
    List<ReconciliationPojo> ReconciliationPojoList=new ArrayList<ReconciliationPojo>();	
   	
	try {	    	
		connection = DbBean.getConnection();
		
		dtCondStr =" AND CAST((SUBSTRING(ISNULL(D.BANKDATE,H.JOURNAL_DATE), 7, 4) + '-' + SUBSTRING(ISNULL(D.BANKDATE,H.JOURNAL_DATE), 4, 2) + '-' + SUBSTRING(ISNULL(D.BANKDATE,H.JOURNAL_DATE), 1, 2)) AS date)";
		
		if (afrmDate.length() > 0) {
          	sCondition = sCondition + dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
				}
			  } else {
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
				}
		     	}
		
		query= "SELECT D.ID,D.PLANT,ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS DATE,D.DESCRIPTION AS ACCOUNT,H.JOURNAL_TYPE AS VOUCHERTYPE,'' AS FILTERDATE,"
				+ "ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS INSTRUMENTDATE,D.DEBITS AS DEBIT,D.CREDITS AS CREDIT,ISNULL(D.BANKDATE,H.JOURNAL_DATE) AS "
				+ "BANKDATE FROM "+plant+"_FINJOURNALDET AS D JOIN "+plant+"_FINJOURNALHDR AS H  ON H.ID = D.JOURNALHDRID "
				+ "WHERE D.ACCOUNT_ID = '"+account+"' "+ sCondition +" AND ISNULL(RID,0) != "+rid;	
		this.mLogger.query(this.printQuery, query);	
		if(connection != null){	
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
			   ResultSet rst = ps.executeQuery();	
			   while (rst.next()) {	
				   ReconciliationPojo reconciliationPojo=new ReconciliationPojo();	
                    ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationPojo);	
                    ReconciliationPojoList.add(reconciliationPojo);	
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
	return ReconciliationPojoList;	
}


}
