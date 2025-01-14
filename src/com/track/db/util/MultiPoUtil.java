package com.track.db.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POBeanDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.multiPoEstDetDAO;
import com.track.dao.MultiPoEstHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
//---Added by Bruhan on March 28 2014, Description:To WmsIBRECEIVEReverse
import com.track.tran.WmsIBRECEIVEReversal;
//---End Added by Bruhan on March 28 2014, Description:To WmsIBRECEIVEReverse
import com.track.tran.WmsReceiveMaterial;
import com.track.tran.WmsReceiveMaterialRandom;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class MultiPoUtil {
	MultiPoEstHdrDAO _MultiPoEstHdrDAO = null;
	multiPoEstDetDAO _multiPoEstDetDAO= null;
	private POBeanDAO poBeanDAO = new POBeanDAO();
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.MultiPoUtil_PRINTPLANTMASTERLOG;
	private boolean printQuery = MLoggerConstant.MultiPoUtil_PRINTPLANTMASTERQUERY;
	
	StrUtils strUtils = new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public MultiPoUtil() {
		_multiPoEstDetDAO = new multiPoEstDetDAO();
		_MultiPoEstHdrDAO = new MultiPoEstHdrDAO();
		_multiPoEstDetDAO.setmLogger(mLogger);
		_MultiPoEstHdrDAO.setmLogger(mLogger);
	}

	public ArrayList getMultiPoEstHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_MultiPoEstHdrDAO.setmLogger(mLogger);
			al = _MultiPoEstHdrDAO.selectMultiPoEstHdr(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	
	
	public List getPreviousOrderDetails(Hashtable ht, String rows) throws Exception {
		List ordersList = new ArrayList();
		try {
			_MultiPoEstHdrDAO.setmLogger(mLogger);
			ordersList = _MultiPoEstHdrDAO.getPreviousOrderDetails(ht, rows);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return ordersList;
	}
    
	
	   public Map getPOReceiptInvoiceHdrDetails(String aplant) throws Exception {
	        Map m =  new HashMap();
	        PoHdrDAO   dao = new  PoHdrDAO();
	        m=dao.getPurchaseEstimateHdrDetails(aplant);
	         return m;
	 }
	   
	   
	   public Boolean removeRow(String plant, String POMULTIESTNO, String loginUser) {
			UserTransaction ut = null;
			Boolean flag = Boolean.valueOf(false);
			try {
				MovHisDAO movHisDao = new MovHisDAO();
				Hashtable ht= new Hashtable();
				ht.put("PLANT", plant);
				ht.put("POMULTIESTNO", POMULTIESTNO);
				movHisDao.setmLogger(mLogger);
				_MultiPoEstHdrDAO.setmLogger(mLogger);
				_multiPoEstDetDAO.setmLogger(mLogger);
				ut = DbBean.getUserTranaction();
				ut.begin();
				Boolean removeHeader = this._MultiPoEstHdrDAO.removeOrder(plant, POMULTIESTNO);
				Boolean removeDetails = this._multiPoEstDetDAO.removeOrderDetails(plant,POMULTIESTNO);
	 		//delete & insert podet multi remarks 
	            if(removeDetails){
	               
	                Hashtable htPoRemarksDel = new Hashtable();
	                htPoRemarksDel.put(IDBConstants.PLANT,plant);
	                htPoRemarksDel.put(IDBConstants.PODET_POMULTIESTNUM, POMULTIESTNO);
					if(this._multiPoEstDetDAO.isExisitPoMultiRemarks(htPoRemarksDel))
	                {
	                   flag = this._multiPoEstDetDAO.deletePoMultiRemarks(htPoRemarksDel);
	                }
	            }
	           //delete podet multi remarks end
				 				
				Hashtable htMovHis = createMoveHisHashtable(plant, POMULTIESTNO, loginUser);
				Boolean movementHiss = movHisDao.insertIntoMovHis(htMovHis);
				if (removeHeader & removeDetails & movementHiss) {
					DbBean.CommitTran(ut);
					flag = Boolean.valueOf(true);
				} else {
					DbBean.RollbackTran(ut);
					flag = Boolean.valueOf(false);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				flag = Boolean.valueOf(false);
			}
			return flag;

		}
	   
	   
	   private Hashtable createMoveHisHashtable(String plant, String POMULTIESTNO,
				String loginUser) {
			Hashtable<String, String> htMovHis = new Hashtable<String, String>();
			htMovHis.put(IDBConstants.PLANT, plant);
			htMovHis.put("DIRTYPE", "DELETE_MULTI_PURCHASE_ESTIMATE_ORDER");
			htMovHis.put(IDBConstants.ITEM, " ");
			htMovHis.put("BATNO", " ");
			htMovHis.put("ORDNUM", POMULTIESTNO);
			htMovHis.put("MOVTID", " ");
			htMovHis.put("RECID", " ");
			htMovHis.put(IDBConstants.LOC, " ");
			htMovHis.put(IDBConstants.CREATED_BY, loginUser);
			htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
			htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			return htMovHis;
		}
	    
	public boolean savePoMultiRemarks(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			_multiPoEstDetDAO.setmLogger(mLogger);
			flag = _multiPoEstDetDAO.insertPoMultiRemarks(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}
	}
