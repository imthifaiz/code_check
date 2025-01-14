package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.dao.PrdClassDAO;
import com.track.dao.PrdTypeDAO;
import com.track.util.MLogger;

public class PrdTypeUtil {

	public PrdTypeUtil() {

	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertPrdTypeMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			PrdTypeDAO itemDao = new PrdTypeDAO();
			itemDao.setmLogger(mLogger);
			inserted = itemDao.insertIntoRsnMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsItemType(Hashtable htLoc) throws Exception {

		boolean isExists = false;
		PrdTypeDAO dao = new PrdTypeDAO();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(htLoc);

		} catch (Exception e) {
			throw e;
		}

		return isExists;
	}
	
	
    public ArrayList getPrdTypeDetails(String prdtype, String plant) {
        ArrayList arrList = new ArrayList();
        try {
        	PrdTypeDAO dao = new PrdTypeDAO();
        		dao.setmLogger(mLogger);
                arrList = dao.getprdtypeDetail(prdtype, plant);
        } catch (Exception e) {
        }
        return arrList;
}

	public boolean updatePrdtypeId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			PrdTypeDAO dao = new PrdTypeDAO();
			dao.setmLogger(mLogger);
			update = dao.updatePrdTypeMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deletePrdTypeId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			PrdTypeDAO dao = new PrdTypeDAO();
			dao.setmLogger(mLogger);
			deleted = dao.deletePrdId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	
	public boolean isExistPrdSubCat(String prd_type_id,String prd_type_desc, String plant) {
		boolean exists = false;
		try {
			PrdTypeDAO dao = new PrdTypeDAO();
			dao.setmLogger(mLogger);
			exists = dao.isExistPrdSubCat(prd_type_id,prd_type_desc,plant);

		} catch (Exception e) {
		}
		return exists;
	}

	public ArrayList getPrdTypeList(String aItemId, String plant, String cond) {

		ArrayList al = null;
		PrdTypeDAO dao = new PrdTypeDAO();
		dao.setmLogger(mLogger);
		try {
			al = dao.getPrdTypeDetails(aItemId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
}
