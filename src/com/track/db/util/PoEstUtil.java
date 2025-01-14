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

import com.track.dao.PoEstHdrDAO;
import com.track.dao.PoEstDetDAO;
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

public class PoEstUtil {
	PoEstHdrDAO _PoEstHdrDAO = null;
	PoEstDetDAO _PoEstDetDAO = null;
	private POBeanDAO poBeanDAO = new POBeanDAO();
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.POUtil_PRINTPLANTMASTERLOG;
	private boolean printQuery = MLoggerConstant.POUtil_PRINTPLANTMASTERQUERY;
	
	StrUtils strUtils = new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public PoEstUtil() {
		_PoEstDetDAO = new PoEstDetDAO();
		_PoEstHdrDAO = new PoEstHdrDAO();

	}

	public Boolean removeRow(String plant, String POESTNO, String loginUser) {
		UserTransaction ut = null;
		Boolean flag = Boolean.valueOf(false);
		try {
			MovHisDAO movHisDao = new MovHisDAO();
			Hashtable ht= new Hashtable();
			ht.put("PLANT", plant);
			ht.put("POESTNO", POESTNO);
			movHisDao.setmLogger(mLogger);
			_PoEstHdrDAO.setmLogger(mLogger);
			_PoEstDetDAO.setmLogger(mLogger);
			ut = DbBean.getUserTranaction();
			ut.begin();
			Boolean removeHeader = this._PoEstHdrDAO.removeOrder(plant, POESTNO);
			Boolean removeDetails = this._PoEstDetDAO.removeOrderDetails(plant,POESTNO);
 		//delete & insert podet multi remarks 
            if(removeDetails){
               
                Hashtable htPoRemarksDel = new Hashtable();
                htPoRemarksDel.put(IDBConstants.PLANT,plant);
                htPoRemarksDel.put(IDBConstants.PODET_POESTNUM, POESTNO);
				if(this._PoEstDetDAO.isExisitPoMultiRemarks(htPoRemarksDel))
                {
                   flag = this._PoEstDetDAO.deletePoMultiRemarks(htPoRemarksDel);
                }
            }
           //delete podet multi remarks end
			 
			
			Hashtable htMovHis = createMoveHisHashtable(plant, POESTNO, loginUser);
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
	
	
	 public Map getPOReceiptHdrDetails(String aplant) throws Exception {
         Map m =  new HashMap();
         PoEstHdrDAO   dao = new  PoEstHdrDAO ();
         m=dao.getPOReciptHeaderDetails(aplant);
         return m;
   }

	private Hashtable createMoveHisHashtable(String plant, String POESTNO,
			String loginUser) {
		Hashtable<String, String> htMovHis = new Hashtable<String, String>();
		htMovHis.put(IDBConstants.PLANT, plant);
		htMovHis.put("DIRTYPE", "DELETE_PURCHASE_ESTIMATE_ORDER");
		htMovHis.put(IDBConstants.ITEM, " ");
		htMovHis.put("BATNO", " ");
		htMovHis.put("ORDNUM", POESTNO);
		htMovHis.put("MOVTID", " ");
		htMovHis.put("RECID", " ");
		htMovHis.put(IDBConstants.LOC, " ");
		htMovHis.put(IDBConstants.CREATED_BY, loginUser);
		htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		return htMovHis;
	}

	

	

	public boolean savePoHdrDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {
			_PoEstHdrDAO.setmLogger(mLogger);
			flag = _PoEstHdrDAO.insertPoHdr(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			//throw e;
       throw new Exception("incoming order created already");
		}

		return flag;
	}

	
	public ArrayList getPoDetDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoEstDetDAO.setmLogger(mLogger);
			al = _PoEstDetDAO.selectPoDet(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	

	public ArrayList getPoEstHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoEstHdrDAO.setmLogger(mLogger);
			al = _PoEstHdrDAO.selectPoHdr(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	
	public ArrayList getPoEstHdrDetails(String query, Hashtable ht)
			throws Exception {
		
		ArrayList al = new ArrayList();
		
		try {
			_PoEstHdrDAO.setmLogger(mLogger);
			al = _PoEstHdrDAO.selectPoHdr(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		
		return al;
	}

	
	public boolean savePoDetDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {

		
			_PoEstDetDAO.setmLogger(mLogger);
			flag = _PoEstDetDAO.insertPoDet(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	 public Map getPOReceiptInvoiceHdrDetails(String aplant) throws Exception {
	        Map m =  new HashMap();
	        PoEstHdrDAO   dao = new  PoEstHdrDAO ();
	        m=dao.getPOReciptInvoiceHeaderDetails(aplant);
	         return m;
	 }
	 
	 public boolean savePoMultiRemarks(Hashtable ht) throws Exception {
			boolean flag = false;
			try {
				_PoEstDetDAO.setmLogger(mLogger);
				flag = _PoEstDetDAO.insertPoMultiRemarks(ht);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}

			return flag;
		}
	 
	 
	 
	 public String getIBDiscountSelectedItemVNO(String aPlant,String vendno,String aItem) throws Exception {
		    String ConvertedUnitCost="";
		      try{
		           ConvertedUnitCost= _PoEstHdrDAO.getIBDiscountSelectedItemVNO(aPlant, vendno,aItem);
		      }catch(Exception e){
		          throw e;
		      }
		    return ConvertedUnitCost;
		}
	
	public List getPreviousOrderDetails(Hashtable ht, String rows) throws Exception {
		List ordersList = new ArrayList();
		try {
			_PoEstHdrDAO.setmLogger(mLogger);
			ordersList = _PoEstHdrDAO.getPreviousOrderDetails(ht, rows);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return ordersList;
	}
	
	
	
	


}
