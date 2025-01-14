package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.dao.PrdBrandDAO;
import com.track.dao.PrdTypeDAO;
import com.track.util.MLogger;

public class PrdBrandUtil {

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertPrdBrandMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			PrdBrandDAO prdBrandDAO = new PrdBrandDAO();
			prdBrandDAO.setmLogger(mLogger);
			inserted = prdBrandDAO.insertIntoMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsItemType(Hashtable<String, String> ht) throws Exception {

		boolean isExists = false;
		PrdBrandDAO dao = new PrdBrandDAO();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(ht);

		} catch (Exception e) {
			throw e;
		}

		return isExists;
	}
	
    public ArrayList getPrdBrandDetails(String prdbrand, String plant) {
        ArrayList arrList = new ArrayList();
        try {
        		PrdBrandDAO dao = new PrdBrandDAO();
        		dao.setmLogger(mLogger);
                arrList = dao.getprdbrandDetail(prdbrand, plant);
        } catch (Exception e) {
        }
        return arrList;
}

	public boolean updatePrdBrand(Hashtable<String, String> htUpdate, Hashtable<String, String> htCondition)
			throws Exception {
		boolean update = false;
		try {
			PrdBrandDAO dao = new PrdBrandDAO();
			dao.setmLogger(mLogger);
			update = dao.updatePrdBrandMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public ArrayList<Map<String, String>> getPrdBrandList(String brandID, String plant, String cond) {

		ArrayList<Map<String, String>> al = null;
		PrdBrandDAO prdBrandDAO = new PrdBrandDAO();
		prdBrandDAO.setmLogger(mLogger);
		try {
			al = prdBrandDAO.getPrdBrandDetails(brandID, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	//start code by Bruhan to delete prod brand id on 22/jan/2014
	public boolean deletePrdBrandId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			PrdBrandDAO prdBrandDAO = new PrdBrandDAO();
			prdBrandDAO.setmLogger(mLogger);
			deleted = prdBrandDAO.deletePrdBrandMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	//ENd code by Bruhan to delete prod brand id on 22/jan/2014
	
	//start code by Thanzith 20/jan/2022

	public boolean isExistPrdBrand(String prd_brand_id,String prd_brand_desc, String plant) {
		boolean exists = false;
		try {
			PrdBrandDAO dao = new PrdBrandDAO();
			dao.setmLogger(mLogger);
			exists = dao.isExistPrdBrand(prd_brand_id,prd_brand_desc,plant);

		} catch (Exception e) {
		}
		return exists;
	}
	// End
	public ArrayList getBrandList(String brandID, String plant, String cond) {

		ArrayList al = null;
		PrdBrandDAO prdBrandDAO = new PrdBrandDAO();
		prdBrandDAO.setmLogger(mLogger);
		try {
			al = prdBrandDAO.getPrdBrandDetails(brandID, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
}
