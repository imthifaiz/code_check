package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.track.constants.MLoggerConstant;
import com.track.db.object.PoDetApproval;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PodetApprovalRemarks;
import com.track.gates.DbBean;
import com.track.util.MLogger;

public class PoDetApprovalDAO  extends BaseDAO {

	public static String TABLE_NAME = "PODETAPPROVAL";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.PODETAPPROVALDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PODETAPPROVALDAO_PRINTPLANTMASTERLOG;

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

	public PoDetApprovalDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "PODETAPPROVAL" + "]";
	}

	public PoDetApprovalDAO() {

	}
	
	public boolean addPoDet(PoDetApproval PoDet) throws Exception {
 		boolean insertFlag = false;
 		boolean flag = false;
 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+PoDet.getPLANT()+"_PODETAPPROVAL]" + 
 					"           ([PLANT]" + 
 					"           ,[PONO]" + 
 					"           ,[POLNNO]" + 
 					"           ,[LNSTAT]" + 
 					"           ,[ITEM]" + 
 					"           ,[ItemDesc]" + 
 					"           ,[TRANDATE]" + 
 					"           ,[UNITCOST]" + 
 					"           ,[QTYOR]" + 
 					"           ,[QTYRC]" + 
 					"           ,[UNITMO]" + 
 					"           ,[USERFLD1]" + 
 					"           ,[USERFLD2]" + 
 					"           ,[USERFLD3]" + 
 					"           ,[USERFLD4]" + 
 					"           ,[CURRENCYUSEQT]" + 
 					"           ,[PRODGST]" + 
 					"           ,[PRODUCTDELIVERYDATE]" + 
 					"           ,[COMMENT1]" + 
 					"           ,[CRAT]" +
 					"           ,[DISCOUNT]" + 
 					"           ,[DISCOUNT_TYPE]" + 
 					"           ,[ACCOUNT_NAME]" + 
 					"           ,[TAX_TYPE]" + 
 					"           ,[UNITCOST_AOD]" + 
 					"           ,[CRBY],[POESTLNNO],[POESTNO],[UKEY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, PoDet.getPLANT());
	 			ps.setString(2, PoDet.getPONO());
	 			ps.setInt(3, PoDet.getPOLNNO());
	 			ps.setString(4, PoDet.getLNSTAT());
	 			ps.setString(5, PoDet.getITEM());
	 			ps.setString(6, PoDet.getItemDesc());
	 			ps.setString(7, PoDet.getTRANDATE());
	 			ps.setDouble(8, PoDet.getUNITCOST());
	 			ps.setBigDecimal(9, PoDet.getQTYOR());
	 			ps.setBigDecimal(10, PoDet.getQTYRC());
	 			ps.setString(11, PoDet.getUNITMO());
	 			ps.setString(12, PoDet.getUSERFLD1());
	 			ps.setString(13, PoDet.getUSERFLD2());
	 			ps.setString(14, PoDet.getUSERFLD3());
	 			ps.setString(15, PoDet.getUSERFLD4());
	 			ps.setDouble(16, PoDet.getCURRENCYUSEQT());
	 			ps.setDouble(17, PoDet.getPRODGST());
	 			ps.setString(18, PoDet.getPRODUCTDELIVERYDATE());
	 			ps.setString(19, PoDet.getCOMMENT1());
	 			ps.setString(20, PoDet.getCRAT());
	 			ps.setDouble(21, PoDet.getDISCOUNT());
	 			ps.setString(22, PoDet.getDISCOUNT_TYPE());
	 			ps.setString(23, PoDet.getACCOUNT_NAME());
	 			ps.setString(24, PoDet.getTAX_TYPE());
	 			ps.setDouble(25, PoDet.getUNITCOST_AOD());
	 			ps.setString(26, PoDet.getCRBY());
	 			ps.setInt(27, PoDet.getPOESTLNNO());
	 			ps.setString(28, PoDet.getPOESTNO());
	 			ps.setString(29, PoDet.getUKEY());


 			   int count=ps.executeUpdate();
 			   if(count>0)
 			   {
 				   insertFlag = true;
 			   }
 			   else
 			   {
 				   throw new SQLException("Creating Purchase Order detail failed, no rows affected.");
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
 		return insertFlag;
 	}
	
	public boolean addPoDetApprovalRemarks(PodetApprovalRemarks podetApprovalRemarks) throws Exception {
 		boolean insertFlag = false;
 		boolean flag = false;
 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+podetApprovalRemarks.getPLANT()+"_PODETAPPROVAL_REMARKS]" + 
 					"           ([PLANT]" + 
 					"           ,[PONO]" + 
 					"           ,[POLNNO]" + 
 					"           ,[ITEM]" + 
 					"           ,[REMARKS]" + 
 					"           ,[CRAT]" +
 					"           ,[CRBY],[UKEY],[ID_REMARKS]) VALUES (?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, podetApprovalRemarks.getPLANT());
	 			ps.setString(2, podetApprovalRemarks.getPONO());
	 			ps.setInt(3, podetApprovalRemarks.getPOLNNO());
	 			ps.setString(4, podetApprovalRemarks.getITEM());
	 			ps.setString(5, podetApprovalRemarks.getREMARKS());
	 			ps.setString(6, podetApprovalRemarks.getCRAT());
	 			ps.setString(7, podetApprovalRemarks.getCRBY());
	 			ps.setString(8, podetApprovalRemarks.getUKEY());
	 			ps.setInt(9, podetApprovalRemarks.getID_REMARKS());
	 			
 			   int count=ps.executeUpdate();
 			   if(count>0)
 			   {
 				   insertFlag = true;
 			   }
 			   else
 			   {
 				   throw new SQLException("Creating Purchase Order remarks failed, no rows affected.");
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
 		return insertFlag;
 	}
}
