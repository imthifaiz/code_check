package com.track.tran;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.MasterDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;


public class WmsPaymentTermMaster implements WmsTran, IMLogger{
	
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

	public boolean processWmsTran(Map map) throws Exception {
		return process(map);
	}
	@SuppressWarnings("unchecked")
	private boolean process(Map mapObject) throws Exception {
		try {
			
			MovHisDAO movHisDao = new MovHisDAO();
			MasterDAO masterDAO = new MasterDAO();
			Boolean result = Boolean.TRUE;
		    Boolean movhisResult = Boolean.TRUE;
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, mapObject.get(IDBConstants.PLANT));
			ht.put(IDBConstants.PAYMENT_TERMS, mapObject.get(IDBConstants.PAYMENT_TERMS));
			ht.put("NO_DAYS", mapObject.get("NO_DAYS"));
			ht.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
			ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			
			result = result && masterDAO.InsertPaymentTerm(ht);
			if(result){
				Hashtable htRecvHis = new Hashtable();
				htRecvHis.clear();
				htRecvHis.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
				htRecvHis.put("DIRTYPE",TransactionConstants.ADD_PAYMENTTERMS);
				htRecvHis.put("ORDNUM","");
			//	htRecvHis.put("NO_DAYS","");
				htRecvHis.put(IDBConstants.ITEM, "");
				htRecvHis.put("BATNO", "");
				htRecvHis.put(IDBConstants.LOC, "");
				htRecvHis.put("REMARKS",mapObject.get(IDBConstants.PAYMENT_TERMS));
				htRecvHis.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
				htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				result = movHisDao.insertIntoMovHis(htRecvHis);
			}
		   return result;
		} catch (Exception e) {
			throw e;
		}

	}

}
