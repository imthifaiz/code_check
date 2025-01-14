package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.ItemUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsUploadAppDisplay implements WmsTran,IMLogger {
	
	DateUtils dateUtils = null;
	ItemUtil itemdao = null;
	CustMstDAO custMstDAO = null;
	CustUtil custUtil = null;
	public WmsUploadAppDisplay() {
		dateUtils = new DateUtils();
		itemdao = new ItemUtil();
		custMstDAO = new CustMstDAO();
		custUtil = new CustUtil();
	}
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {

		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}
	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
	

		MLogger.log(0,"2.insert LocMSt - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 1");
		flag = processCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		if(flag== true)
			flag=processMovHis(m);

		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean processCountSheet(Map map) throws Exception {
		boolean flag = false;

		this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
		Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		//
		
		boolean result=custUtil.isExistAppCustomer((String)map.get("CUSTNO"),(String)map.get("ITEM"), (String)map.get("PLANT"));
		
		if(result==true){
		flag = result;
		
		if (flag) {
			StringBuffer sql1 = new StringBuffer(" SET ");
			sql1.append("ORDER_QTY"+ " = '" + map.get("ORDER_QTY") + "', ");
			sql1.append("MAX_ORDER_QTY"+ " = '" + map.get("MAX_ORDER_QTY") + "', ");
			sql1.append(IDBConstants.UPDATED_AT + " = '" + DateUtils.getDateTime() + "', ");
			sql1.append(IDBConstants.UPDATED_BY + " = '" + map.get("LOGIN_USER") + "'");
			flag = custMstDAO.updateAppQty(sql1.toString(), htInvMst, "");
		} 
		}else{
			 htInvMst.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			 htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			 htInvMst.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
			 htInvMst.put("ORDER_QTY", (String) map.get("ORDER_QTY"));
			 htInvMst.put("MAX_ORDER_QTY", map.get("MAX_ORDER_QTY"));		
			 htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			 htInvMst.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			 flag = custMstDAO.insertAppQty(htInvMst);
		 }
		return flag;
	}
	@SuppressWarnings("unchecked")
	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htmov = new Hashtable();
			htmov.clear();
			htmov.put(IDBConstants.PLANT, map.get("PLANT"));
			htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_ORD_DISPLAY_UPLOAD);
			htmov.put(IDBConstants.CUSTOMER_CODE, map.get("CUSTNO"));
			htmov.put("Qty", map.get("ORDER_QTY"));
			htmov.put(IDBConstants.ITEM, map.get("ITEM"));
			htmov.put("BATNO", "");
			htmov.put(IDBConstants.MOVTID, "IN");
			htmov.put(IDBConstants.RECID, "");
			htmov.put(IDBConstants.PONO, "");
			htmov.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			htmov.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
		    htmov .put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		  

			flag = movHisDao.insertIntoMovHis(htmov);

		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
}
