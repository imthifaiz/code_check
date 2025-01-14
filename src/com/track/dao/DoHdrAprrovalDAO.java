package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.track.constants.MLoggerConstant;
import com.track.db.object.DoHdr;
import com.track.db.object.DoHdrApproval;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class DoHdrAprrovalDAO  extends BaseDAO {

	public static String plant = "";
	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.DOHDRAPPROVALDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.DOHDRAPPROVALDAO_PRINTPLANTMASTERLOG;

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

	public DoHdrAprrovalDAO() {
		_StrUtils = new StrUtils();
	}
	
	public DoHdrAprrovalDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_DOHDRAPPROVAL" + "]";
	}

	public static String TABLE_NAME = "DOHDRAPPROVAL";
	
	public boolean addDoHdr(DoHdrApproval doHdr) throws Exception {
		boolean insertFlag = false;
//		boolean flag = false;
//		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+doHdr.getPLANT()+"_"+TABLE_NAME+"]" + 
					"           ([PLANT]" + 
					"           ,[DONO]" + 
					"           ,[ORDERTYPE]" + 
					"           ,[DELDATE]" + 
					"           ,[STATUS]" + 
					"           ,[PickStaus]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
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
					"           ,[CURRENCYID]" + 
					"           ,[DELIVERYDATE]" + 
					"           ,[TIMESLOTS]" + 
					"           ,[OUTBOUND_GST]" + 
					"           ,[STATUS_ID]" + 
					"           ,[EMPNO]" + 
					"           ,[ESTNO]" + 
					"           ,[Remark3]" + 
					"           ,[ORDERDISCOUNT]" + 
					"           ,[SHIPPINGCOST]" + 
					"           ,[INCOTERMS]" + 
					"           ,[PAYMENTTYPE]" + 
					"           ,[DELIVERYDATEFORMAT]" + 
					"           ,[APPROVESTATUS]" + 
					"           ,[SALES_LOCATION]" + 
					"           ,[ORDER_STATUS]" +
					"           ,[DISCOUNT]" +
					"           ,[DISCOUNT_TYPE]" +
					"           ,[ADJUSTMENT]" +
					"           ,[ITEM_RATES]" +
					"           ,[CURRENCYUSEQT]" +
					"           ,[ORDERDISCOUNTTYPE]" +
					"           ,[TAXID]" +
					"           ,[ISDISCOUNTTAX]" +
					"           ,[ISORDERDISCOUNTTAX]" +
					"           ,[ISSHIPPINGTAX]" +
					"           ,[PROJECTID]" +
					"           ,[TRANSPORTID]" +
					"           ,[PAYMENT_TERMS]" +
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
					"           ,[APP_CUST_ORDER_STATUS]" +
					"           ,[UKEY]" +
					"           ,[REASON]" +
					"           ,[ID]" +
					"           ,[TAXTREATMENT]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, doHdr.getPLANT());
			   ps.setString(2, doHdr.getDONO());
			   ps.setString(3, doHdr.getORDERTYPE());
			   ps.setString(4, doHdr.getDELDATE());
			   ps.setString(5, doHdr.getSTATUS());
			   ps.setString(6, doHdr.getPickStaus());
			   ps.setString(7, doHdr.getCRAT());
			   ps.setString(8, doHdr.getCRBY());
			   ps.setString(9, doHdr.getCustCode());
			   ps.setString(10, doHdr.getCustName());
			   ps.setString(11, doHdr.getJobNum());
			   ps.setString(12, doHdr.getPersonInCharge());
			   ps.setString(13, doHdr.getContactNum());
			   ps.setString(14, doHdr.getAddress());
			   ps.setString(15, doHdr.getAddress2());
			   ps.setString(16, doHdr.getAddress3());
			   ps.setString(17, doHdr.getCollectionDate());
			   ps.setString(18, doHdr.getCollectionTime());
			   ps.setString(19, doHdr.getRemark1());
			   ps.setString(20, doHdr.getRemark2());
			   ps.setString(21, doHdr.getSHIPPINGID());
			   ps.setString(22, doHdr.getSHIPPINGCUSTOMER());
			   ps.setString(23, doHdr.getCURRENCYID());
			   ps.setString(24, doHdr.getDELIVERYDATE());
			   ps.setString(25, doHdr.getTIMESLOTS());
			   ps.setString(26, Double.toString(doHdr.getOUTBOUND_GST()));
			   ps.setString(27, doHdr.getSTATUS_ID());
			   ps.setString(28, doHdr.getEMPNO());
			   ps.setString(29, doHdr.getESTNO());
			   ps.setString(30, doHdr.getRemark3());
			   ps.setString(31, Double.toString(doHdr.getORDERDISCOUNT()));
			   ps.setString(32, Double.toString(doHdr.getSHIPPINGCOST()));
			   ps.setString(33, doHdr.getINCOTERMS());
			   ps.setString(34, doHdr.getPAYMENTTYPE());
			   ps.setString(35, Short.toString(doHdr.getDELIVERYDATEFORMAT()));
			   ps.setString(36, doHdr.getAPPROVESTATUS());
			   ps.setString(37, doHdr.getSALES_LOCATION());
			   ps.setString(38, doHdr.getORDER_STATUS());
			   ps.setString(39, Double.toString(doHdr.getDISCOUNT()));
			   ps.setString(40, doHdr.getDISCOUNT_TYPE());
			   ps.setString(41, Double.toString(doHdr.getADJUSTMENT()));
			   ps.setString(42, Short.toString(doHdr.getITEM_RATES()));
			   ps.setDouble(43, doHdr.getCURRENCYUSEQT());
			   ps.setString(44, doHdr.getORDERDISCOUNTTYPE());
			   ps.setInt(45, doHdr.getTAXID());
			   ps.setShort(46, doHdr.getISDISCOUNTTAX());
			   ps.setShort(47, doHdr.getISORDERDISCOUNTTAX());
			   ps.setShort(48, doHdr.getISSHIPPINGTAX());
			   ps.setInt(49, doHdr.getPROJECTID());
			   ps.setInt(50, doHdr.getTRANSPORTID());
			   ps.setString(51, doHdr.getPAYMENT_TERMS());
			   ps.setString(52, doHdr.getSHIPCONTACTNAME());
				ps.setString(53, doHdr.getSHIPDESGINATION());
				ps.setString(54, doHdr.getSHIPWORKPHONE());
				ps.setString(55, doHdr.getSHIPHPNO());
				ps.setString(56, doHdr.getSHIPEMAIL());
				ps.setString(57, doHdr.getSHIPCOUNTRY());
				ps.setString(58, doHdr.getSHIPADDR1());
				ps.setString(59, doHdr.getSHIPADDR2());
				ps.setString(60, doHdr.getSHIPADDR3());
				ps.setString(61, doHdr.getSHIPADDR4());
				ps.setString(62, doHdr.getSHIPSTATE());
				ps.setString(63, doHdr.getSHIPZIP());
				ps.setString(64, doHdr.getAPP_CUST_ORDER_STATUS());
				ps.setString(65, doHdr.getUKEY());
				ps.setString(66, doHdr.getREASON());
				ps.setInt(67, doHdr.getID());
			   ps.setString(68, doHdr.getTAXTREATMENT());
			  
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
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return insertFlag;
	}
}
