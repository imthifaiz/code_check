package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsPosReceiveTran implements WmsTran, IMLogger {

	private boolean printLog = MLoggerConstant.WmsMiscIssueMaterial_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.WmsMiscIssueMaterial_PRINTPLANTMASTERINFO;
	StrUtils su = new StrUtils();
	DateUtils dateUtils = null;
	String lineqty = "", batch = "", balqty = "";
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

	public WmsPosReceiveTran() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = true;

		if (flag) {

			flag = processInvMst(m);

		}

		
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		String extCond = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();

			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.BATCH, map.get(IConstants.BATCH));
		
			//No need to check now if batch is provided. Check only for NOBATCH.
			if ("NOBATCH".equals(map.get(IConstants.BATCH))) {
				flag = _InvMstDAO.isExisit(htInvMst, "");				
			}
			double invumqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
			if (flag) {
				String INVID="";				
				ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, extCond);
				if (!alStock.isEmpty()) {
					Iterator iterStock = alStock.iterator();
					Map mapIterStock = (Map)iterStock.next();
					INVID = (String) mapIterStock.get(IDBConstants.INVID);
				}
				
				StringBuffer sql = new StringBuffer(" SET ");
				sql.append(IDBConstants.QTY + " = QTY +'"
						+ invumqty + "'");
				sql.append("," + IDBConstants.USERFLD3 + " = '"
						+ map.get("USERFLD3") + "'");
				sql.append("," + IDBConstants.USERFLD4 + " = '"
						+ map.get(IConstants.BATCH) + "'");
				sql.append("," + IDBConstants.UPDATED_AT + " = '"
						+ dateUtils.getDateTime() + "'");
				sql.append("," + IConstants.EXPIREDATE + " = '"
						+ map.get("EXPIREDATE") + "'");
				if(INVID!="")
					htInvMst.put(IDBConstants.INVID, INVID);
				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
				

			} else if (!flag) {
	
				
				htInvMst.put(IDBConstants.QTY, String.valueOf(invumqty));
				htInvMst.put(IDBConstants.USERFLD3, map.get("USERFLD3"));
				//htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htInvMst.put(IDBConstants.CREATED_AT, (map.get(IConstants.RECIEVEDATE) != null ? map.get(IConstants.RECIEVEDATE).toString().replaceAll("-", "").replaceAll("/", "") : map.get(IConstants.TRAN_DATE).toString().replaceAll("-", "").replaceAll("/", "")) + "000000");
				htInvMst.put(IDBConstants.STATUS, "");
				htInvMst.put(IConstants.EXPIREDATE,map.get("EXPIREDATE"));
				flag = _InvMstDAO.insertInvMst(htInvMst);
			}

			if (flag == true)
			{
				flag = processMovHis_IN(map);
			    flag = processRecvDet(map);
			}

			else
			{
				flag=false;
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", map.get("DIRTYPE"));
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));

			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put("QTY", map.get(IConstants.QTY));
			if((String)map.get(IConstants.UOM)!=null)
			htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UOM));    
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.REMARKS, (map.get(IConstants.REMARKS)+","+map.get(IConstants.REASONCODE)+","+map.get(IConstants.EMPNO)+","+map.get(IConstants.RECVDATE)));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.MOVHIS_ORDNUM));	


			flag = movHisDao.insertIntoMovHis(htRecvHis);
			
			
			
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	
	public boolean processRecvDet(Map map) throws Exception {
		boolean flag = false;
		RecvDetDAO recvdetDao = new RecvDetDAO();
		recvdetDao.setmLogger(mLogger);
		StrUtils su = new StrUtils();
		DateUtils dateUtils = new DateUtils();
		try {
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			//htRecvDet.put(IDBConstants.PODET_PONUM, map.get("TranId"));
			htRecvDet.put(IDBConstants.PODET_PONUM, "");
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, "");
			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
			htRecvDet.put("BATCH", map.get(IConstants.BATCH));
			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvDet.put("RECQTY", map.get(IConstants.QTY));
			htRecvDet.put("ORDQTY", map.get(IConstants.QTY));
			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
            htRecvDet.put("REMARK", map.get(IConstants.REMARKS));
		    htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "GR");
		    htRecvDet.put(IDBConstants.RECVDATE,  map.get(IConstants.RECVDATE));
		    htRecvDet.put(IDBConstants.TRAN_TYPE,  map.get(IConstants.TRAN_TYPE));
		    htRecvDet.put(IDBConstants.REFNO,  map.get(IConstants.REFNO));
		    htRecvDet.put(IDBConstants.CNAME,  map.get(IConstants.CNAME));
		    htRecvDet.put(IDBConstants.RSNCODE, map.get(IConstants.REASONCODE));
		    htRecvDet.put(IDBConstants.EMPNO, map.get(IConstants.EMPNO));
		    htRecvDet.put("GRNO", map.get("TranId"));
		    htRecvDet.put(IConstants.LNNO,map.get(IConstants.LNNO));
			flag = recvdetDao.insertRecvDet(htRecvDet);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}


}
