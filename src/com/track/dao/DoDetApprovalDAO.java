package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.track.constants.MLoggerConstant;
import com.track.db.object.DoDetApproval;
import com.track.db.object.DodetApprovalRemarks;
import com.track.gates.DbBean;
import com.track.util.MLogger;

public class DoDetApprovalDAO extends BaseDAO {

	public static String TABLE_NAME = "DODETAPPROVAL";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.DODETAPPROVALDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.DODETAPPROVALDAO_PRINTPLANTMASTERLOG;

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

	public DoDetApprovalDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "DODETAPPROVAL" + "]";
	}

	public DoDetApprovalDAO() {

	}
	
	public boolean addDoDet(DoDetApproval doDet) throws Exception {
		boolean insertFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
				query = "INSERT INTO ["+doDet.getPLANT()+"_DODETAPPROVAL]" + 
						"           ([PLANT]" + 
						"           ,[DONO]" + 
						"           ,[DOLNNO]" + 
						"           ,[PickStatus]" + 
						"           ,[LNSTAT]" + 
						"           ,[ITEM]" + 
						"           ,[ItemDesc]" + 
						"           ,[TRANDATE]" + 
						"           ,[UNITPRICE]" + 
						"           ,[QTYOR]" + 
						"           ,[QTYIS]" + 
						"           ,[QtyPick]" + 
						"           ,[UNITMO]" +  
						"           ,[CRAT]" + 
						"           ,[CRBY]" + 
						"           ,[USERFLD1]" + 
						"           ,[USERFLD2]" + 
						"           ,[USERFLD3]" + 
						"           ,[CURRENCYUSEQT]" + 
						"           ,[ESTNO]" + 
						"           ,[ESTLNNO]" + 
						"           ,[PRODGST]" + 
						"           ,[PRODUCTDELIVERYDATE]" + 
						"           ,[TAX_TYPE]" + 
						"           ,[ACCOUNT_NAME]" +
						"           ,[DISCOUNT]" +
						"           ,[DISCOUNT_TYPE]" +
						"           ,[UNITCOST]" +
						"           ,[ADDONAMOUNT]" +
						"           ,[ADDONTYPE]" +
						"           ,[UKEY])" +
						"     VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, doDet.getPLANT());
				   ps.setString(2, doDet.getDONO());
				   ps.setLong(3, doDet.getDOLNNO());
				   ps.setString(4, doDet.getPickStatus());
				   ps.setString(5, doDet.getLNSTAT());
				   ps.setString(6, doDet.getITEM());
				   ps.setString(7, doDet.getItemDesc());
				   ps.setString(8, doDet.getTRANDATE());
				   ps.setDouble(9, doDet.getUNITPRICE());
				   ps.setBigDecimal(10, doDet.getQTYOR());
				   ps.setBigDecimal(11, doDet.getQTYIS());
				   ps.setBigDecimal(12, doDet.getQtyPick());
				   ps.setString(13, doDet.getUNITMO());
				   ps.setString(14, doDet.getCRAT());
				   ps.setString(15, doDet.getCRBY());
				   ps.setString(16, doDet.getUSERFLD1());
				   ps.setString(17, doDet.getUSERFLD2());
				   ps.setString(18, doDet.getUSERFLD3());
				   ps.setDouble(19, doDet.getCURRENCYUSEQT());
				   ps.setString(20, doDet.getESTNO());
				   ps.setInt(21, doDet.getESTLNNO());
				   ps.setDouble(22, doDet.getPRODGST());
				   ps.setString(23, doDet.getPRODUCTDELIVERYDATE());
				   ps.setString(24, doDet.getTAX_TYPE());
				   ps.setString(25, doDet.getACCOUNT_NAME());
				   ps.setDouble(26, doDet.getDISCOUNT());
				   ps.setString(27, doDet.getDISCOUNT_TYPE());
				   ps.setDouble(28, doDet.getUNITCOST());
				   ps.setDouble(29, doDet.getADDONAMOUNT());
				   ps.setString(30, doDet.getADDONTYPE());
				   ps.setString(31, doDet.getUKEY());
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   insertFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Creating Sales Order failed, no rows affected.");
				   }
				   this.mLogger.query(this.printQuery, query);		
				}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Item already added to out going order");
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return insertFlag;
	}

	public boolean addDoDetApprovalRemarks(DodetApprovalRemarks dodetApprovalRemarks) throws Exception {
 		boolean insertFlag = false;
 		boolean flag = false;
 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+dodetApprovalRemarks.getPLANT()+"_DODETAPPROVAL_REMARKS]" + 
 					"           ([PLANT]" + 
 					"           ,[DONO]" + 
 					"           ,[DOLNNO]" + 
 					"           ,[ITEM]" + 
 					"           ,[REMARKS]" + 
 					"           ,[CRAT]" +
 					"           ,[CRBY],[UKEY],[ID_REMARKS]) VALUES (?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, dodetApprovalRemarks.getPLANT());
	 			ps.setString(2, dodetApprovalRemarks.getDONO());
	 			ps.setInt(3, dodetApprovalRemarks.getDOLNNO());
	 			ps.setString(4, dodetApprovalRemarks.getITEM());
	 			ps.setString(5, dodetApprovalRemarks.getREMARKS());
	 			ps.setString(6, dodetApprovalRemarks.getCRAT());
	 			ps.setString(7, dodetApprovalRemarks.getCRBY());
	 			ps.setString(8, dodetApprovalRemarks.getUKEY());
	 			ps.setInt(9, dodetApprovalRemarks.getID_REMARKS());
	 			
 			   int count=ps.executeUpdate();
 			   if(count>0)
 			   {
 				   insertFlag = true;
 			   }
 			   else
 			   {
 				   throw new SQLException("Creating Sales Order remarks failed, no rows affected.");
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
