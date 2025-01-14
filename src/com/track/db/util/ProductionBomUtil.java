package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.transaction.UserTransaction;

import com.track.constants.MLoggerConstant;
import com.track.dao.ItemMstDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.PrdTypeDAO;
import com.track.gates.DbBean;
import com.track.tran.WmsDeKittingFromPC;
import com.track.tran.WmsKittingFromPC;
import com.track.tran.WmsPalletization;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class ProductionBomUtil {
	ItemMstDAO _ItemMstDAO = null;
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.ProductionBomUtil_PRINTPLANTMASTERLOG;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
		
	}

	public ProductionBomUtil() {
		_ItemMstDAO = new ItemMstDAO();

	}
	
	public boolean insertProdBomMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			ProductionBomDAO ProdBomDao = new ProductionBomDAO();
			ProdBomDao.setmLogger(mLogger);
			inserted = ProdBomDao.insertIntoProdBomMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsProdBom(Hashtable htLoc) throws Exception {

		boolean isExists = false;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			isExists = ProdBomDao.isExists(htLoc);

		} catch (Exception e) {
			throw e;
		}

		return isExists;
	}

	public boolean updateProdBom(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			ProductionBomDAO ProdBomDao = new ProductionBomDAO();
			ProdBomDao.setmLogger(mLogger);
			update = ProdBomDao.updateProdBomMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deleteProdBom(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			ProductionBomDAO ProdBomDao = new ProductionBomDAO();
			ProdBomDao.setmLogger(mLogger);
			deleted = ProdBomDao.deleteProdBomMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	
	public ArrayList getProcessingProdBomList(String apitem,String plant, String cond) {

		ArrayList al = null;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			al = ProdBomDao.getProcessingProdBomDetails(apitem, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}

	public ArrayList getProdBomList(String apitem,String plant, String cond) {

		ArrayList al = null;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			al = ProdBomDao.getProdBomDetails(apitem, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getProdBomSummaryList(Hashtable ht,String plant, String cond) {

		ArrayList al = null;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			al = ProdBomDao.getProdBomDetailsforSummary(ht, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getProcessingProdBompitemList(String apitem,String plant, String cond) {

		ArrayList al = null;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			al = ProdBomDao.getProcessingProdBompitemlist(apitem, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getProdBompitemList(String apitem,String plant, String cond) {

		ArrayList al = null;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			al = ProdBomDao.getProdBompitemlist(apitem, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getProdBomchilditemList(String apitem,String acitem,String plant, String cond) {

		ArrayList al = null;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			al = ProdBomDao.getProdBomchilditemlist(apitem,acitem, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getProdBomSummaryList(String apitem,String plant,String pdesc,String pdetaildesc,String citem,String cdesc,String cdetaildesc,String eitem,String edesc,String edetaildesc,String cond) {

		ArrayList al = null;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			al = ProdBomDao.getProdBomSummaryDetails(apitem, plant, pdesc, pdetaildesc, citem, cdesc, cdetaildesc, eitem, edesc, edetaildesc,cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getParentBomSummaryList(String apitem,String plant) {

		ArrayList al = null;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ProdBomDao.setmLogger(mLogger);
		try {
			al = ProdBomDao.getParentBomSummaryDetails(apitem,plant);

		} catch (Exception e) {
		}

		return al;
	}
	
public Boolean Dokitting(Hashtable htkit)
			throws Exception {
		Boolean result = Boolean.valueOf(false);
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			WmsTran tran = new WmsKittingFromPC();
			((IMLogger) tran).setMapDataToLogger(this.mLogger
					.getLoggerConstans());
			result = tran.processWmsTran(htkit);
			if (result == true) {
				DbBean.CommitTran(ut);
				result = true;
			} else {
				DbBean.RollbackTran(ut);
				result = false;
			}

		} catch (Exception e) {
			result = false;
			DbBean.RollbackTran(ut);
			throw e;
		}
		return result;
	}
public ArrayList getkittingList(String apitem,String ploc,String pbatch,String plant, String cond) {

	ArrayList al = null;
	ProductionBomDAO ProdBomDao = new ProductionBomDAO();
	ProdBomDao.setmLogger(mLogger);
	try {
		al = ProdBomDao.getkittingDetails(apitem,ploc,pbatch,plant, cond);

	} catch (Exception e) {
	}

	return al;
}
public Boolean Dekitting(Hashtable htdekit)
		throws Exception {
	Boolean result = Boolean.valueOf(false);
	UserTransaction ut = null;
	try {
		ut = DbBean.getUserTranaction();
		ut.begin();
		WmsTran tran = new WmsDeKittingFromPC();
		((IMLogger) tran).setMapDataToLogger(this.mLogger
				.getLoggerConstans());
		result = tran.processWmsTran(htdekit);
		if (result == true) {
			DbBean.CommitTran(ut);
			result = true;
		} else {
			DbBean.RollbackTran(ut);
			result = false;
		}

	} catch (Exception e) {
		result = false;
		DbBean.RollbackTran(ut);
		throw e;
	}
	return result;
}
public ArrayList getKitpitemList(String apitem,String plant, String cond) {

	ArrayList al = null;
	ProductionBomDAO ProdBomDao = new ProductionBomDAO();
	ProdBomDao.setmLogger(mLogger);
	try {
		al = ProdBomDao.getKittingpitemlist(apitem, plant, cond);

	} catch (Exception e) {
	}

	return al;
}
public ArrayList getKitchilditemList(String apitem,String acitem,String plant, String cond) {

	ArrayList al = null;
	ProductionBomDAO ProdBomDao = new ProductionBomDAO();
	ProdBomDao.setmLogger(mLogger);
	try {
		al = ProdBomDao.getKittingchilditemlist(apitem,acitem, plant, cond);

	} catch (Exception e) {
	}

	return al;
}
public Boolean DokittingBulk(Hashtable htkit)
		throws Exception {
	Boolean result = Boolean.valueOf(false);
	//UserTransaction ut = null;
	try {
		//ut = DbBean.getUserTranaction();
		//ut.begin();
		WmsTran tran = new WmsKittingFromPC();
		((IMLogger) tran).setMapDataToLogger(this.mLogger
				.getLoggerConstans());
		result = tran.processWmsTran(htkit);
		if (result == true) {
			//DbBean.CommitTran(ut);
			result = true;
		} else {
			//DbBean.RollbackTran(ut);
			result = false;
		}

	} catch (Exception e) {
		result = false;
		//DbBean.RollbackTran(ut);
		throw e;
	}
	return result;
}

public ArrayList getProductionBOMDetail(String aPitem,	String plant, String cond) throws Exception {
	 ArrayList arrList = new ArrayList();
	 try
	  {
		 ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		 arrList = ProdBomDao.getProductionBOMDetial(aPitem,plant, cond);
	  } catch (Exception e) {
	}
	  return arrList;
}
}
