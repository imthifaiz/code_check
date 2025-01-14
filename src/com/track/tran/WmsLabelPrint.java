package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.LabelPrintDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsLabelPrint  implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsStockTake_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsStockTake_PRINTPLANTMASTERINFO;
	LabelPrintDAO _LabelPrintDAO = null;
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

	public WmsLabelPrint() {
		_LabelPrintDAO = new LabelPrintDAO();
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		flag = processTemptableLabelPrint(m);
		if (flag) {
			//flag = processMovHis(m);
		}

		return flag;
	}

	public boolean processTemptableLabelPrint(Map<String, String> map) throws Exception {
		boolean flag = false;
		try {
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.clear();
			ht.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			ht.put("REFNO", map.get(IConstants.REFNO.toString()));
			ht.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.ORDERNO));
			ht.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			ht.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
			ht.put("BATCH", map.get(IConstants.BATCH));
			ht.put(IDBConstants.QTY, map.get(IConstants.QTY));
			ht.put(IDBConstants.LOC, map.get(IConstants.LOC));
			ht.put("UOM", map.get("UOM"));
			ht.put("REMARKS", map.get("REMARKS"));
			ht.put("STATUS","N");
			ht.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			ht.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			ht.put("ORDERSTATUS",map.get("ORDERSTATUS"));
			ht.put("CNAME",map.get("CNAME"));
			flag = _LabelPrintDAO.insertLabelType(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", "Label Print " +map.get(IConstants.LABEL_PRINT_TYPE));
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}
}