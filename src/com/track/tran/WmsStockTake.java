package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.StockTakeDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsStockTake implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsStockTake_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsStockTake_PRINTPLANTMASTERINFO;
	StockTakeDAO _StockTakeDAO = null;
	DateUtils dateUtils = null;
        StrUtils su = new StrUtils();
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

	public WmsStockTake() {
		_StockTakeDAO = new StockTakeDAO();
		_StockTakeDAO.setmLogger(mLogger);
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		flag = processStockTake(m);
		if (flag) {
			//flag = processMovHis(m);

		}

		return flag;
	}
	/* ************Modification History*********************************
	   Sep 23 2014, Description: To include CRBY,CRAT 
	   April 29 2015 Bruhan, Description: To include REMARKS
	*/
	/*public boolean processStockTake(Map<String, String> map) throws Exception {

		boolean flag = false;

		try {

			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put("UOM", map.get("UOM"));
			htInvMst.put("BATCH", map.get(IConstants.BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			flag = _StockTakeDAO.isExisit(htInvMst, "");
			if (flag) {
				StringBuffer sql = new StringBuffer(" SET ");

				sql.append("" + IDBConstants.QTY + " = " + IDBConstants.QTY
						+ " + '" + map.get(IConstants.QTY) + "',REMARKS='" + map.get(IConstants.REMARKS) + "'");

				flag = _StockTakeDAO.update(sql.toString(), htInvMst, "");

			} else {
				htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put("UOM", map.get("UOM"));
				htInvMst.put(IDBConstants.STATUS, "C");
				htInvMst.put("CR_DATE",  DateUtils.getDate());
				htInvMst.put(IDBConstants.USERFLD1,su.InsertQuotes( new ItemMstDAO()
						.getItemDesc((String) map.get(IConstants.PLANT),
								(String) map.get(IConstants.ITEM))));
				htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htInvMst.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htInvMst.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));

				flag = _StockTakeDAO.insertStkTake(htInvMst);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}*/
	public boolean processStockTake(Map<String, String> map) throws Exception {

		boolean flag = false;

		try {

			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			//htInvMst.put("UOM", map.get("UOM"));
			htInvMst.put("BATCH", map.get(IConstants.BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put("STATUS", "N");
			flag = _StockTakeDAO.isExisit(htInvMst, "");
			if (flag) {
				StringBuffer sql = new StringBuffer(" SET ");

//				sql.append("" + IDBConstants.QTY + " = QTY + " + IDBConstants.QTY+ " + '" + map.get(IConstants.QTY) + "' ,DIFFQTY= DIFFQTY+ '" + Double.parseDouble(map.get("DIFFQTY")) + "',INVFLAG='" + map.get("INVFLAG") +  "',REMARKS='" + map.get(IConstants.REMARKS) + "' ,UPAT='" + DateUtils.getDateTime() + "' ,UPBY='" + map.get(IConstants.LOGIN_USER) +"'");
				sql.append("" + IDBConstants.QTY + " =  " + IDBConstants.QTY+ " + '" + map.get(IConstants.QTY) + "' ,DIFFQTY= '" + Double.parseDouble(map.get("DIFFQTY")) + "',INVFLAG='" + map.get("INVFLAG") +  "',REMARKS='" + map.get(IConstants.REMARKS) + "' ,UPAT='" + DateUtils.getDateTime() + "' ,UPBY='" + map.get(IConstants.LOGIN_USER) +"'");

				flag = _StockTakeDAO.update(sql.toString(), htInvMst, "");

			} else {
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.BATCH, map.get(IConstants.BATCH));
				flag = new InvMstDAO().isExisit(htInvMst, "");
				if(!flag) {
					
					htInvMst.put(IDBConstants.EXPIREDATE, "");
					htInvMst.put(IDBConstants.QTY,"0");
					htInvMst.put(IDBConstants.USERFLD3, "");
					htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDate().toString().replaceAll("/", "") + "000000");
					htInvMst.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
					htInvMst.put(IDBConstants.STATUS, "");
		
					flag = new InvMstDAO().insertInvMst(htInvMst);
					
				}
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put("BATCH", map.get(IConstants.BATCH));
				htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));
				htInvMst.put("DIFFQTY", map.get("DIFFQTY"));
				htInvMst.put("INVFLAG", map.get("INVFLAG"));
				htInvMst.put("UOM", map.get("UOM"));
				htInvMst.put("STATUS", "N");
				//htInvMst.put(IDBConstants.STATUS, "C");
				htInvMst.put("CR_DATE",  DateUtils.getDate());
				htInvMst.put(IDBConstants.USERFLD1,su.InsertQuotes( new ItemMstDAO()
						.getItemDesc((String) map.get(IConstants.PLANT),
								(String) map.get(IConstants.ITEM))));
				htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htInvMst.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htInvMst.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));

				flag = _StockTakeDAO.insertStkTake(htInvMst);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

    /* * ************Modification History*********************************
	   April 29 2015 Bruhan, Description: To include REMARKS
	*/
	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.STOCK_TAKE);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htRecvHis.put(IDBConstants.CREATED_BY, map
					.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
					.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

}