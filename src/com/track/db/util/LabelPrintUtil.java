package com.track.db.util;

import java.math.BigDecimal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import java.util.Map;
import javax.transaction.UserTransaction;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.CustomerReturnDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POBeanDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.LabelPrintDAO;
import com.track.dao.ToHdrDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.tables.DOHDR;
import com.track.tran.WmsLabelPrint;
import com.track.tran.WmsOBISSUEReversal;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class LabelPrintUtil {
	
	DoHdrDAO _DoHdrDAO = null;
	DoDetDAO _DoDetDAO = null;
	LabelPrintDAO _LabelPrintDAO=null;
	StrUtils strUtils = new StrUtils();
	private boolean printLog = MLoggerConstant.LabelPrintUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private POBeanDAO poBeanDAO = new POBeanDAO();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public LabelPrintUtil () {
		_DoDetDAO = new DoDetDAO();
		_DoHdrDAO = new DoHdrDAO();
		_LabelPrintDAO=new LabelPrintDAO();

		_DoDetDAO.setmLogger(mLogger);
		_DoHdrDAO.setmLogger(mLogger);
		_LabelPrintDAO.setmLogger(mLogger);
	
	}
	
	public boolean process_LabelPrint(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;

		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			
			flag = process_Wms_LabelPrint(obj);
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = false;			
			throw e;
		}

		return flag;
	}
	
	public ArrayList getLabelSetting(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_LabelPrintDAO.setmLogger(mLogger);
			al = _LabelPrintDAO. selectLabelSetting(query, ht, extCond);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}
	
	public ArrayList getLabelDetail(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_LabelPrintDAO.setmLogger(mLogger);
			al = _LabelPrintDAO. selectLabelDetail(query, ht, extCond);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}



	public boolean process_Wms_LabelPrint(Map map) throws Exception {
		boolean flag = false;
		WmsTran tran = new WmsLabelPrint();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		
		return flag;
	}


}
