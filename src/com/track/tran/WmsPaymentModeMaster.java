package com.track.tran;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.MasterDAO;
import com.track.dao.CoaDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.CoaUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;


public class WmsPaymentModeMaster implements WmsTran, IMLogger{
	
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
			ht.put(IDBConstants.PAYMENTMODE, mapObject.get(IDBConstants.PAYMENTMODE));
			ht.put("PAYMENTMODESERIAL", mapObject.get("PAYMENTMODESERIAL"));
			ht.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
			ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			
			result = result && masterDAO.InsertPaymentMode(ht);
			
			if(result){
				CoaUtil coaUtil = new CoaUtil();
				CoaDAO coaDAO = new CoaDAO();
				String paymode = (String)mapObject.get(IDBConstants.PAYMENTMODE);
				String plant = (String)mapObject.get(IDBConstants.PLANT);
				String accname = paymode.toUpperCase()+" IN TRANSIT";
				boolean isexistacc = coaDAO.isExistAccount(accname, plant);
				if(!isexistacc) {		
					Hashtable<String, String> accountHt = new Hashtable<>();
					accountHt.put("PLANT", plant);
					accountHt.put("ACCOUNTTYPE", "3");
					accountHt.put("ACCOUNTDETAILTYPE", "7");
					accountHt.put("ACCOUNT_NAME", accname);
					accountHt.put("DESCRIPTION", "");
					accountHt.put("ISSUBACCOUNT", "0");
					accountHt.put("SUBACCOUNTNAME", "");
					accountHt.put("OPENINGBALANCE", "");
					accountHt.put("OPENINGBALANCEDATE", "");
					accountHt.put("LANDEDCOSTCAL", "0");
					accountHt.put("CRAT", new DateUtils().getDateTime());
					accountHt.put("CRBY", (String)mapObject.get(IDBConstants.LOGIN_USER));
					accountHt.put("UPAT", new DateUtils().getDateTime());
	
					String gcode = coaDAO.GetGCodeById("3", plant);
					String dcode = coaDAO.GetDCodeById("7", plant);
					List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, "3", "7");
					String maxid = "";
					String atcode = "";
					if(listQry.size() > 0) {
						for (int i = 0; i < listQry.size(); i++) {
							Map<String, String> m = listQry.get(i);
							maxid = m.get("CODE");
						}
					
						int count = Integer.valueOf(maxid);
						atcode = String.valueOf(count+1);
						if(atcode.length() == 1) {
							atcode = "0"+atcode;
						}
					}else {
						atcode = "01";
					}
					String accountCode = gcode+"-"+dcode+atcode;
					accountHt.put("ACCOUNT_CODE", accountCode);
					accountHt.put("CODE", atcode);
					coaUtil.addAccount(accountHt, plant);
				
				}			
			}
			
			if(result){
				Hashtable htRecvHis = new Hashtable();
				htRecvHis.clear();
				htRecvHis.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
				htRecvHis.put("DIRTYPE",TransactionConstants.ADD_PAYMENTMODE);
				htRecvHis.put("ORDNUM","");
				htRecvHis.put(IDBConstants.ITEM, "");
				htRecvHis.put("BATNO", "");
				htRecvHis.put(IDBConstants.LOC, "");
				htRecvHis.put("REMARKS",mapObject.get(IDBConstants.PAYMENTMODE));
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
