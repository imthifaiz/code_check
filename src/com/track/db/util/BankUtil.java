package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.dao.PrdBrandDAO;
import com.track.dao.BankDAO;
import com.track.util.MLogger;

public class BankUtil {

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertBankMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			BankDAO BankDAO = new BankDAO();
			BankDAO.setmLogger(mLogger);
			inserted = BankDAO.insertIntoBankMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsItemType(Hashtable<String, String> ht) throws Exception {

		boolean isExists = false;
		BankDAO dao = new BankDAO();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(ht);

		} catch (Exception e) {
			throw e;
		}

		return isExists;
	}

	public boolean updateBank(Hashtable<String, String> htUpdate, Hashtable<String, String> htCondition)
			throws Exception {
		boolean update = false;
		try {
			BankDAO dao = new BankDAO();
			dao.setmLogger(mLogger);
			update = dao.updateBankMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}
	
	public ArrayList getToBank(String aCustName,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			BankDAO bankDAO = new BankDAO();
			bankDAO.setmLogger(mLogger);
			arrList = bankDAO.getToBankDetails(aCustName,
					plant);
		} catch (Exception e) {
		}
		return arrList;
	}

	public ArrayList getToBankSummary(String plant,String BANK_BRANCH,String BANK_BRANCH_CODE,String BANK_NAME) 
			throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			BankDAO bankDAO = new BankDAO();
			bankDAO.setmLogger(mLogger);
			arrList = bankDAO.getToBankDetailsForSummary(plant,BANK_BRANCH,BANK_BRANCH_CODE,BANK_NAME);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList<Map<String, String>> getBankList(String bank_branch_code, String plant, String cond,String bank_name,String bank_branch) {

		ArrayList<Map<String, String>> al = null;
		BankDAO bankDAO = new BankDAO();
		bankDAO.setmLogger(mLogger);
		try {
			al = bankDAO.getBankDetails(bank_branch_code, plant, cond, bank_name, bank_branch);

		} catch (Exception e) {
		}

		return al;
	}
	//start code by Bruhan to delete prod brand id on 22/jan/2014
	public boolean deleteBank(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			BankDAO bankDAO = new BankDAO();
			bankDAO.setmLogger(mLogger);
			deleted = bankDAO.deleteBankMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	
	public ArrayList getPoHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			BankDAO bankDAO = new BankDAO();
			bankDAO.setmLogger(mLogger);
			al = bankDAO.selectPoHdr(query, ht, extCond);
		} catch (Exception e) {
			//this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	
/*	public ArrayList getBrandList(String BANK_BRANCH_CODE, String plant, String cond) {

		ArrayList al = null;
		BankDAO bankDAO = new BankDAO();
		bankDAO.setmLogger(mLogger);
		try {
			al = bankDAO.getBankDetails(BANK_BRANCH_CODE, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}*/
}
