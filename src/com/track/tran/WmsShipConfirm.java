package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ShipHisDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsShipConfirm implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsShipConfirm_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsShipConfirm_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
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

	public WmsShipConfirm() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = true;

		if (flag) {

			flag = processDodetForIssue(m);

		}

		if (flag) {

			flag = processInvMst(m);

		}

		if (flag) {

			flag = processShipHis(m);

		}

		if (flag) {
			flag = processMovHis_OUT(m);
		}

		return flag;
	}

	public boolean processDodetForIssue(Map map) throws Exception {

		boolean flag = false;
		// update receive qty in podet
		DoDetDAO _DoDetDAO = new DoDetDAO();
		DoHdrDAO _DoHdrDAO = new DoHdrDAO();
		_DoDetDAO.setmLogger(mLogger);
		_DoHdrDAO.setmLogger(mLogger);

		Hashtable htCondiPoDet = new Hashtable();
		StringBuffer query = new StringBuffer("");

		htCondiPoDet.put("PLANT", map.get(IConstants.PLANT));
		htCondiPoDet.put("item", map.get(IConstants.ITEM));
		htCondiPoDet.put("dono", map.get(IConstants.DODET_DONUM));
		htCondiPoDet.put("dolnno", map.get(IConstants.DODET_DOLNNO));

		query.append("isnull(QtyOr,0) as QtyOr");
		query.append(",isnull(QtyIs,0) as QtyIs");

		Map mQty = _DoDetDAO.selectRow(query.toString(), htCondiPoDet);

		double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
		double isQty = Double.parseDouble((String) mQty.get("QtyIs"));

		double tranQty = Double.parseDouble((String) map.get(IConstants.QTY));

		String queryPoDet = "";
		String queryPoHdr = "";

		if (ordQty == isQty + tranQty) {
			queryPoDet = "set qtyis= isNull(qtyis,0) + "
					+ map.get(IConstants.QTY) + " , LNSTAT='C' ";

		} else {
			queryPoDet = "set qtyis= isNull(qtyis,0) + "
					+ map.get(IConstants.QTY) + " , LNSTAT='O' ";

		}

		flag = _DoDetDAO.update(queryPoDet, htCondiPoDet, "");

		if (flag) {
			Hashtable htCondiPoHdr = new Hashtable();
			htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
			htCondiPoHdr.put("dono", map.get(IConstants.DODET_DONUM));

			flag = _DoDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");

			if (flag)
				queryPoHdr = "set STATUS='O' ";
			else
				queryPoHdr = "set STATUS='C' ";

			flag = _DoHdrDAO.update(queryPoHdr, htCondiPoHdr, "");

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
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));

			StringBuffer sql = new StringBuffer(" SET ");
			sql.append("QTY" + " = QTY - " + map.get(IConstants.QTY) + " ");

			extCond = "AND QTY >='" + map.get(IConstants.QTY) + "' ";

			flag = _InvMstDAO.update(sql.toString(), htInvMst, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htMovhis = new Hashtable();
			htMovhis.clear();
			htMovhis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovhis.put("DIRTYPE", TransactionConstants.ORD_ISSUE);
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("MOVTID", "OUT");
			htMovhis.put("RECID", "");
			MLogger.log(0, "Stage 1");
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htMovhis.put("BATNO", map.get(IConstants.BATCH));
			htMovhis.put("QTY", map.get(IConstants.QTY));
			htMovhis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovhis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

			flag = movHisDao.insertIntoMovHis(htMovhis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processShipHis(Map map) throws Exception {
		boolean flag = false;
		String extCond = "";
		ShipHisDAO shiDao = new ShipHisDAO();
		shiDao.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.DODET_DONUM, map
					.get(IConstants.DODET_DONUM));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put("BATCH", map.get(IConstants.BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));

			StringBuffer sql = new StringBuffer(" SET ");
			sql.append(" STATUS ='C' ");

			flag = shiDao.update(sql.toString(), htInvMst, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

}