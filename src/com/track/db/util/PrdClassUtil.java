package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.dao.PrdClassDAO;
import com.track.dao.PrdDeptDAO;
import com.track.util.MLogger;

public class PrdClassUtil {
	public PrdClassUtil() {

	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertPrdClsMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {
			PrdClassDAO itemDao = new PrdClassDAO();
			itemDao.setmLogger(mLogger);
			inserted = itemDao.insertPrdClsMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsItemType(Hashtable htLoc) throws Exception {

		boolean isExists = false;
		PrdClassDAO dao = new PrdClassDAO();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(htLoc);

		} catch (Exception e) {

			throw e;
		}

		return isExists;
	}
	
	
    public ArrayList getPrdClassDetails(String prdclass, String plant) {
        ArrayList arrList = new ArrayList();
        try {
        	PrdClassDAO dao = new PrdClassDAO();
        		dao.setmLogger(mLogger);
                arrList = dao.getprdclassDetail(prdclass, plant);
        } catch (Exception e) {
        }
        return arrList;
}

	public boolean updatePrdtypeId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			PrdClassDAO dao = new PrdClassDAO();
			dao.setmLogger(mLogger);
			update = dao.updatePrdClsMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deletePrdTypeId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			PrdClassDAO dao = new PrdClassDAO();
			dao.setmLogger(mLogger);
			deleted = dao.deletePrdClsId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	 public boolean isExistPRDCAT(String prd_cls_id,String prd_cls_desc, String plant) {
			boolean exists = false;
			try {
				PrdClassDAO dao = new PrdClassDAO();
				dao.setmLogger(mLogger);
				exists = dao.isExistPRDCAT(prd_cls_id,prd_cls_desc,plant);

			} catch (Exception e) {
			}
			return exists;
		}

	public ArrayList getPrdTypeList(String aItemId, String plant, String Cond) {

		ArrayList al = null;
		PrdClassDAO dao = new PrdClassDAO();
		dao.setmLogger(mLogger);
		try {
			al = dao.getPrdClsDetails(aItemId, plant, Cond);

		} catch (Exception e) {

		}

		return al;
	}

}
