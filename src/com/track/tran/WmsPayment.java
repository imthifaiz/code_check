package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderPaymentDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.db.util.POUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsPayment implements WmsTran,  IMLogger{
	
	private boolean printLog = MLoggerConstant.WmsReceiveMaterialRandom_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsReceiveMaterialRandom_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
        StrUtils su = new StrUtils();
        POUtil poutil = new POUtil();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}
	
	public WmsPayment() {
		dateUtils = new DateUtils();
	}
	
	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			String OrderSelected = (String)m.get("ORDERNAME");
			if(OrderSelected.equalsIgnoreCase("Outbound")){
				flag=this.processDoHdr(m);
				if(flag){
					flag=this.processOrderPaymentDetails(m);
				}
				if(flag){
					flag=this.processMovHis(m);
				}
			}else if(OrderSelected.equalsIgnoreCase("Inbound")){
				flag=this.processPoHdr(m);
				if(flag){
					flag=this.processOrderPaymentDetails(m);
				}
				if(flag){
					flag=this.processMovHis(m);
				}
			}else if(OrderSelected.equalsIgnoreCase("Others")){
				
				flag=this.processOrderPaymentDetails(m);
				
				if(flag){
					flag=this.processMovHis(m);
				}
			}else{
				throw new Exception("Please select the order type for the Payment");
			}
		
		   
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processMovHis(Map<String,String> map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable<String,String> htRecvHis = new Hashtable<String,String>();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.ADD_PAYMENT);
			htRecvHis.put(IDBConstants.ITEM, "");
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.CUSTOMER_CODE, "");
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get("PAYMENT_REFNO"));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get("ORDNO"));
			htRecvHis.put(IDBConstants.LOC,map.get("PAYMENT_MODE"));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put("BATNO", "");
			htRecvHis.put("QTY", map.get("AMOUNT_PAID"));
			htRecvHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htRecvHis.put("REMARKS", su.InsertQuotes(map.get("PAYMENT_REMARKS")));

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processPoHdr(Map<String,String> map) throws Exception {

		boolean flag = false;
		try {
			String queryPoHdr = "";
	
			PoHdrDAO _PoHdrDAO = new PoHdrDAO();
			_PoHdrDAO.setmLogger(mLogger);
			Hashtable<String,String> htCondiPoHdr = new Hashtable<String,String>();
			htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
			htCondiPoHdr.put("pono", map.get("ORDNO"));

				flag = _PoHdrDAO.isExisit(htCondiPoHdr,"");

				if (flag)
				
					queryPoHdr = "set  PAYMENT_STATUS = '"+(String)map.get("PAYMENT_STATUS")+"',STATUS_ID ='"+(String)map.get("STATUS_ID")+"'";
				
				else
					throw new Exception ("Order No. "+map.get("ORDNO")+" not found to process the paymnet");

				flag = _PoHdrDAO.updatePO(queryPoHdr, htCondiPoHdr, "");

			
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}
	public boolean processDoHdr(Map<String,String> map) throws Exception {

		boolean flag = false;
		
			try {
				String queryDoHdr = "";
		
				DoHdrDAO _DoHdrDAO = new DoHdrDAO();
				_DoHdrDAO.setmLogger(mLogger);
				Hashtable<String,String> htCondiDoHdr = new Hashtable<String,String>();
				htCondiDoHdr.put("PLANT", map.get(IConstants.PLANT));
				htCondiDoHdr.put("Dono", map.get("ORDNO"));

					flag = _DoHdrDAO.isExisit(htCondiDoHdr,"");

					if (flag)
						queryDoHdr = "set  PAYMENT_STATUS = '"+(String)map.get("PAYMENT_STATUS")+"',STATUS_ID ='"+(String)map.get("STATUS_ID")+"'";
					else
						throw new Exception ("Order No. "+map.get("ORDNO")+" not found to process the paymnet");

					flag = _DoHdrDAO.update(queryDoHdr, htCondiDoHdr, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processOrderPaymentDetails(Map<String,String> map) throws Exception {
		boolean flag = false;
		OrderPaymentDAO orderPaymentDAo = new OrderPaymentDAO();
		orderPaymentDAo.setmLogger(mLogger);
		try {
			Hashtable<String,String> htOrdPayment = new Hashtable<String,String>();
			htOrdPayment.clear();
			htOrdPayment.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htOrdPayment.put("ORDNO", map.get("ORDNO"));
			htOrdPayment.put("ORDERNAME", map.get("ORDERNAME"));
			htOrdPayment.put("ORDERTYPE", map.get("ORDERTYPE"));
			htOrdPayment.put("AMOUNT_PAID", map.get("AMOUNT_PAID"));
			htOrdPayment.put("PAYMENT_DT", map.get("PAYMENT_DT"));
			htOrdPayment.put("PAYMENT_MODE", map.get("PAYMENT_MODE"));
			htOrdPayment.put("PAYMENT_REFNO", map.get("PAYMENT_REFNO"));
			htOrdPayment.put("PAYMENT_TYPE", map.get("PAYMENT_TYPE"));
			htOrdPayment.put("PAYMENT_ID", map.get("PAYMENT_ID"));
			htOrdPayment.put("PAYMENT_REMARKS",map.get("PAYMENT_REMARKS"));
			htOrdPayment.put("COMMENT1", map.get("COMMENT1"));
			htOrdPayment.put("COMMENT2", map.get("COMMENT2"));
			htOrdPayment.put("CRAT", DateUtils.getDateTime());
			htOrdPayment.put("CRBY",  map.get(IConstants.LOGIN_USER));
			htOrdPayment.put("CustCode",  map.get("CustCode"));
			
			flag = orderPaymentDAo.insertIntoOrderPaymentDet(htOrdPayment);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}


}
