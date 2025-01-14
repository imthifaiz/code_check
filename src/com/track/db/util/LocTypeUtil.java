package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.dao.LocTypeDAO;
import com.track.dao.PrdTypeDAO;
import com.track.util.MLogger;

public class LocTypeUtil {

	public LocTypeUtil() {

	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertLocTypeMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			LocTypeDAO LocDao = new LocTypeDAO();
			LocDao.setmLogger(mLogger);
			inserted = LocDao.insertIntoLocTypeMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsLocType(Hashtable htLoc) throws Exception {

		boolean isExists = false;
		LocTypeDAO LocDao = new LocTypeDAO();
		LocDao.setmLogger(mLogger);
		try {
			isExists = LocDao.isExists(htLoc);

		} catch (Exception e) {
			throw e;
		}

		return isExists;
	}

	public boolean updateLoctypeId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			LocTypeDAO LocDao = new LocTypeDAO();
			LocDao.setmLogger(mLogger);
			update = LocDao.updateLocTypeMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deleteLocTypeId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			LocTypeDAO LocDao = new LocTypeDAO();
			LocDao.setmLogger(mLogger);
			deleted = LocDao.deleteLocTypeId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}

	public ArrayList getLocTypeList(String aLocId, String plant, String cond) {

		ArrayList al = null;
		LocTypeDAO LocDao = new LocTypeDAO();
		LocDao.setmLogger(mLogger);
		try {
			al = LocDao.getLocTypeDetails(aLocId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getLocTypeListtwo(String aLocId, String plant, String cond) {

		ArrayList al = null;
		LocTypeDAO LocDao = new LocTypeDAO();
		LocDao.setmLogger(mLogger);
		try {
			al = LocDao.getLocTypeDetailsTwo(aLocId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getLocTypeListthree(String aLocId, String plant, String cond) {
		
		ArrayList al = null;
		LocTypeDAO LocDao = new LocTypeDAO();
		LocDao.setmLogger(mLogger);
		try {
			al = LocDao.getLocTypeDetailsThree(aLocId, plant, cond);
			
		} catch (Exception e) {
		}
		
		return al;
	}
}
