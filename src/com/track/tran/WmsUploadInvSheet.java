package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.db.util.ItemUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsUploadInvSheet implements WmsTran,IMLogger {
	
	DateUtils dateUtils = null;
	InvMstDAO _InvMstDAO = null;
	RecvDetDAO _RecvDetDAO = null;
	ItemUtil itemdao = null;
	LocMstDAO locdao = null;
	public WmsUploadInvSheet() {
		_InvMstDAO = new InvMstDAO();
		dateUtils = new DateUtils();
		itemdao = new ItemUtil();
		locdao = new LocMstDAO();
		_RecvDetDAO = new RecvDetDAO();
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
		_InvMstDAO.setmLogger(mLogger);
		Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
		htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
		//
		
		//check for existence of item
		itemdao.setmLogger(mLogger);
		locdao.setmLogger(mLogger);
		boolean result=itemdao.isExistsItemMst((String)map.get(IConstants.ITEM), (String)map.get("PLANT"));
		
		//check for valid location
		Hashtable htcdn = new Hashtable();
		htcdn.put(IConstants.PLANT, map.get(IConstants.PLANT));
		htcdn.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
		boolean  result1=locdao.isExisit(htcdn, "");	
		 if(result==true&&result1==true){
		flag = _InvMstDAO.isExisit(htInvMst, "");
		
		if (flag) {
			StringBuffer sql1 = new StringBuffer(" SET ");
		    sql1.append(IDBConstants.QTY + " = '"
		                    + map.get(IConstants.QTY) + "', ");
			/* Commented by samatha on 24/10/2011 to override the Qty instead of accumlation
                         * sql1.append(IDBConstants.QTY + " = QTY +'"
					+ map.get(IConstants.QTY) + "', ");*/
                                        
			sql1.append(IDBConstants.EXPIREDATE + " = '"
					+ map.get(IConstants.EXPIREDATE) + "', ");
			sql1.append(IDBConstants.UPDATED_AT + " = '"
					+ DateUtils.getDateTime() + "', ");
			sql1.append(IDBConstants.UPDATED_BY + " = '"
					+ map.get("LOGIN_USER") + "'");
			flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
			if(flag) {				
				StringBuffer sql2 = new StringBuffer(" SET ");
				sql2.append(" ORDQTY = '" + map.get(IConstants.QTY) + "', ");
				sql2.append(" RECQTY = '" + map.get(IConstants.QTY) + "', ");
				sql2.append(IDBConstants.UPDATED_AT + " = '" + DateUtils.getDateTime() + "', ");
				sql2.append(IDBConstants.UPDATED_BY + " = '" + map.get("LOGIN_USER") + "'");
				
				Hashtable htRecvDet = new Hashtable();
				htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htRecvDet.put("PONO","");
				htRecvDet.put(IDBConstants.ITEM, map.get(IDBConstants.ITEM));
				htRecvDet.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
				htRecvDet.put("BATCH", map.get(IDBConstants.BATCH));
				htRecvDet.put("UNITCOST", map.get("AVERAGEUNITCOST"));				
				htRecvDet.put("TRAN_TYPE", "INVENTORYUPLOAD");
				boolean isExists = _RecvDetDAO.isExisit(htRecvDet, (String)map.get(IConstants.PLANT));
				if(isExists) {
					flag = _RecvDetDAO.update(sql2.toString(), htRecvDet, "",
							(String)map.get(IConstants.PLANT));
				}
				
			}

		} else {
		        htInvMst.put(IConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
			htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));

			htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htInvMst.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			flag = _InvMstDAO.insertInvMst(htInvMst);
			if(flag) {
				Hashtable htRecvDet = new Hashtable();
				htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htRecvDet.put("PONO","");
				htRecvDet.put(IDBConstants.ITEM, map.get(IDBConstants.ITEM));
				htRecvDet.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
				htRecvDet.put("BATCH", map.get(IDBConstants.BATCH));
				htRecvDet.put("ORDQTY", map.get(IConstants.QTY));
				htRecvDet.put("RECQTY", map.get(IConstants.QTY));
				htRecvDet.put("UNITCOST", map.get("AVERAGEUNITCOST"));				
				htRecvDet.put("TRAN_TYPE", "INVENTORYUPLOAD");
				htRecvDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htRecvDet.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
				flag = _RecvDetDAO.insertRecvDet(htRecvDet);
			}
		}
		 }else
		 {
			 throw new Exception("One of the ITEM or Loc is not created yet.");
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
			htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_INV_UPLOAD);
			htmov.put(IDBConstants.LOC, map.get("LOC"));
			htmov.put("Qty", map.get("IMPORTQTY"));
			htmov.put(IDBConstants.ITEM, map.get("ITEM"));
			htmov.put("BATNO", map.get(IDBConstants.BATCH));
			if((String)map.get(IConstants.UOM)!=null)
				htmov.put(IDBConstants.UOM, map.get(IConstants.UOM));
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
