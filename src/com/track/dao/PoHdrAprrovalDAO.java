package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.track.constants.MLoggerConstant;
import com.track.db.object.PoHdr;
import com.track.db.object.PoHdrApproval;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class PoHdrAprrovalDAO extends BaseDAO {

	public static String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POHDRAPPROVALDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POHDRAPPROVALDAO_PRINTPLANTMASTERLOG;

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

	public PoHdrAprrovalDAO() {
		_StrUtils = new StrUtils();
	}

	public PoHdrAprrovalDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_POHDRAPPROVAL" + "]";
	}

	public static String TABLE_NAME = "POHDRAPPROVAL";
	
	public boolean addPoHdr(PoHdrApproval poHdr) throws Exception {
 		boolean insertFlag = false;
// 		boolean flag = false;
// 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+poHdr.getPLANT()+"_POHDRAPPROVAL]" + 
 					"           ([PLANT]" + 
 					"           ,[PONO]" +
 					"           ,[STATUS]" +
 					"           ,[ORDERTYPE]" +
 					"           ,[DELDATE]" +
 					"           ,[CRAT]" +
 					"           ,[CRBY]" +
 					"           ,[UPAT]" +
 					"           ,[UPBY]" +
 					"           ,[CustCode]" +
 					"           ,[CustName]" +
 					"           ,[JobNum]" +
 					"           ,[PersonInCharge]" +
 					"           ,[contactNum]" +
 					"           ,[Address]" +
 					"           ,[Address2]" +
 					"           ,[Address3]" +
 					"           ,[CollectionDate]" +
 					"           ,[CollectionTime]" +
 					"           ,[Remark1]" +
 					"           ,[Remark2]" +
 					"           ,[SHIPPINGID]" +
 					"           ,[SHIPPINGCUSTOMER]" +
 					"           ,[INBOUND_GST]" +
 					"           ,[CURRENCYID]" +
 					"           ,[STATUS_ID]" +
 					"           ,[REMARK3]" +
 					"           ,[ORDERDISCOUNT]" +
 					"           ,[SHIPPINGCOST]" +
 					"           ,[INCOTERMS]" +
 					"           ,[ADJUSTMENT]" +
 					"           ,[PAYMENTTYPE]" +
 					"           ,[DELIVERYDATEFORMAT]" +
 					"           ,[PURCHASE_LOCATION]" +
 					"           ,[TAXTREATMENT]" +
 					"           ,[REVERSECHARGE]" +
 					"           ,[ORDER_STATUS]" +
 					"           ,[LOCALEXPENSES]" +
 					"           ,[ISDISCOUNTTAX]" +
 					"           ,[ISSHIPPINGTAX]" +
 					"           ,[TAXID]" +
 					"           ,[ISTAXINCLUSIVE]" +
 					"           ,[ORDERDISCOUNTTYPE]" +
 					"           ,[CURRENCYUSEQT]" +
 					"           ,[PROJECTID]" +
 					"           ,[TRANSPORTID]" +
 					"           ,[PAYMENT_TERMS]" +
 					"           ,[EMPNO]" +
 					"           ,[SHIPCONTACTNAME]" +
					"           ,[SHIPDESGINATION]" +
					"           ,[SHIPWORKPHONE]" +
					"           ,[SHIPHPNO]" +
					"           ,[SHIPEMAIL]" +
					"           ,[SHIPCOUNTRY]" +
					"           ,[SHIPADDR1]" +
					"           ,[SHIPADDR2]" +
					"           ,[SHIPADDR3]" +
					"           ,[SHIPADDR4]" +
					"           ,[SHIPSTATE]" +
					"           ,[SHIPZIP]" +
 					"           ,[GOODSIMPORT],[POESTNO],[UKEY],[ID],[APPROVAL_STATUS],[REASON]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, poHdr.getPLANT());
	 			ps.setString(2, poHdr.getPONO());
	 			ps.setString(3, poHdr.getSTATUS());
	 			ps.setString(4, poHdr.getORDERTYPE());
	 			ps.setString(5, poHdr.getDELDATE());
	 			ps.setString(6, poHdr.getCRAT());
	 			ps.setString(7, poHdr.getCRBY());
	 			ps.setString(8, poHdr.getUPAT());
	 			ps.setString(9, poHdr.getUPBY());
	 			ps.setString(10, poHdr.getCustCode());
	 			ps.setString(11, poHdr.getCustName());
	 			ps.setString(12, poHdr.getJobNum());
	 			ps.setString(13, poHdr.getPersonInCharge());
	 			ps.setString(14, poHdr.getContactNum());
	 			ps.setString(15, poHdr.getAddress());
	 			ps.setString(16, poHdr.getAddress2());
	 			ps.setString(17, poHdr.getAddress3());
	 			ps.setString(18, poHdr.getCollectionDate());
	 			ps.setString(19, poHdr.getCollectionTime());
	 			ps.setString(20, poHdr.getRemark1());
	 			ps.setString(21, poHdr.getRemark2());
	 			ps.setString(22, poHdr.getSHIPPINGID());
	 			ps.setString(23, poHdr.getSHIPPINGCUSTOMER());
	 			ps.setDouble(24, poHdr.getINBOUND_GST());
	 			ps.setString(25, poHdr.getCURRENCYID());
	 			ps.setString(26, poHdr.getSTATUS_ID());
	 			ps.setString(27, poHdr.getREMARK3());
	 			ps.setDouble(28, poHdr.getORDERDISCOUNT());
	 			ps.setDouble(29, poHdr.getSHIPPINGCOST());
	 			ps.setString(30, poHdr.getINCOTERMS());
	 			ps.setDouble(31, poHdr.getADJUSTMENT());
	 			ps.setString(32, poHdr.getPAYMENTTYPE());
	 			ps.setDouble(33, poHdr.getDELIVERYDATEFORMAT());
	 			ps.setString(34, poHdr.getPURCHASE_LOCATION());
	 			ps.setString(35, poHdr.getTAXTREATMENT());
	 			ps.setDouble(36, poHdr.getREVERSECHARGE());
	 			ps.setString(37, poHdr.getORDER_STATUS());
	 			ps.setDouble(38, poHdr.getLOCALEXPENSES());
	 			ps.setDouble(39, poHdr.getISDISCOUNTTAX());
	 			ps.setDouble(40, poHdr.getISSHIPPINGTAX());
	 			ps.setInt(41, poHdr.getTAXID());
	 			ps.setDouble(42, poHdr.getISTAXINCLUSIVE());
	 			ps.setString(43, poHdr.getORDERDISCOUNTTYPE());
	 			ps.setDouble(44, poHdr.getCURRENCYUSEQT());
	 			ps.setDouble(45, poHdr.getPROJECTID());
	 			ps.setDouble(46, poHdr.getTRANSPORTID());
	 			ps.setString(47, poHdr.getPAYMENT_TERMS());
	 			ps.setString(48, poHdr.getEMPNO());
	 			ps.setString(49, poHdr.getSHIPCONTACTNAME());
				ps.setString(50, poHdr.getSHIPDESGINATION());
				ps.setString(51, poHdr.getSHIPWORKPHONE());
				ps.setString(52, poHdr.getSHIPHPNO());
				ps.setString(53, poHdr.getSHIPEMAIL());
				ps.setString(54, poHdr.getSHIPCOUNTRY());
				ps.setString(55, poHdr.getSHIPADDR1());
				ps.setString(56, poHdr.getSHIPADDR2());
				ps.setString(57, poHdr.getSHIPADDR3());
				ps.setString(58, poHdr.getSHIPADDR4());
				ps.setString(59, poHdr.getSHIPSTATE());
				ps.setString(60, poHdr.getSHIPZIP());
	 			ps.setDouble(61, poHdr.getGOODSIMPORT());
	 			ps.setString(62, poHdr.getPOESTNO());
	 			ps.setString(63, poHdr.getUKEY());
	 			ps.setInt(64, poHdr.getID());
	 			ps.setString(65, poHdr.getAPPROVAL_STATUS());
	 			ps.setString(66, poHdr.getREASON());

 			   int count=ps.executeUpdate();
 			   if(count>0)
 			   {
 				   insertFlag = true;
 			   }
 			   else
 			   {
 				   throw new SQLException("Creating Purchase Order failed, no rows affected.");
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
