package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.dao.PrdDeptDAO;

import com.track.util.MLogger;

public class PrdDeptUtil {
	public PrdDeptUtil() {

	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertPrdDepMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {
			PrdDeptDAO itemDao = new PrdDeptDAO();
			itemDao.setmLogger(mLogger);
			inserted = itemDao.insertPrdDepMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsItemType(Hashtable htLoc) throws Exception {

		boolean isExists = false;
		PrdDeptDAO dao = new PrdDeptDAO();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(htLoc);

		} catch (Exception e) {

			throw e;
		}

		return isExists;
	}
	
	
    public ArrayList getPrdDeptDetails(String prdDept, String plant) {
        ArrayList arrList = new ArrayList();
        try {
        	PrdDeptDAO dao = new PrdDeptDAO();
        		dao.setmLogger(mLogger);
                arrList = dao.getprdDeptDetail(prdDept, plant);
        } catch (Exception e) {
        }
        return arrList;
}

	public boolean updatePrdtypeId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			PrdDeptDAO dao = new PrdDeptDAO();
			dao.setmLogger(mLogger);
			update = dao.updatePrdDepMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deletePrdTypeId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			PrdDeptDAO dao = new PrdDeptDAO();
			dao.setmLogger(mLogger);
			deleted = dao.deletePrdDepId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	
	 public boolean isExistPRDDEPT(String prd_dept_id,String prd_dept_desc, String plant) {
			boolean exists = false;
			try {
				PrdDeptDAO dao = new PrdDeptDAO();
				dao.setmLogger(mLogger);
				exists = dao.isExistPRDDEPT(prd_dept_id,prd_dept_desc,plant);

			} catch (Exception e) {
			}
			return exists;
		}

	public ArrayList getPrdTypeList(String aItemId, String plant, String Cond) {

		ArrayList al = null;
		PrdDeptDAO dao = new PrdDeptDAO();
		dao.setmLogger(mLogger);
		try {
			al = dao.getPrdDepDetails(aItemId, plant, Cond);

		} catch (Exception e) {

		}

		return al;
	}

}
