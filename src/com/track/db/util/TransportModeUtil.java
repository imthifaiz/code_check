package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.dao.TransportModeDAO;
import com.track.util.MLogger;
@SuppressWarnings({"rawtypes", "unchecked"})
public class TransportModeUtil {

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertTransportModeMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			TransportModeDAO TranModeDAO = new TransportModeDAO();
			TranModeDAO.setmLogger(mLogger);
			inserted = TranModeDAO.insertIntoMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	
	public int insertTransportModeModalMst(Hashtable ht) throws Exception {
		int inserted = 0;
		try {

			TransportModeDAO TranModeDAO = new TransportModeDAO();
			TranModeDAO.setmLogger(mLogger);
			inserted = TranModeDAO.insertIntoMstModal(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}
	
	public boolean isExistsItemType(Hashtable<String, String> ht) throws Exception {

		boolean isExists = false;
		TransportModeDAO dao = new TransportModeDAO();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(ht);

		} catch (Exception e) {
			throw e;
		}

		return isExists;
	}
	

	public boolean isExistTransport(String trans, String plant) {
		boolean exists = false;
		TransportModeDAO dao = new TransportModeDAO();
		try {
			dao.setmLogger(mLogger);
			exists = dao.isExistTransport(trans, plant);

		} catch (Exception e) {
		}
		return exists;
	}

	public boolean updateTranModelMst(Hashtable<String, String> htUpdate, Hashtable<String, String> htCondition)
			throws Exception {
		boolean update = false;
		try {
			TransportModeDAO dao = new TransportModeDAO();
			dao.setmLogger(mLogger);
			update = dao.updateTranModelMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public ArrayList<Map<String, String>> getTranModelDetails(String brandID, String plant, String cond) {

		ArrayList<Map<String, String>> al = null;
		TransportModeDAO TranModeDAO = new TransportModeDAO();
		TranModeDAO.setmLogger(mLogger);
		try {
			al = TranModeDAO.getTranModelDetails(brandID, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	//start code by Bruhan to delete prod brand id on 22/jan/2014
	public boolean deleteTranModelMst(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			TransportModeDAO TranModeDAO = new TransportModeDAO();
			TranModeDAO.setmLogger(mLogger);
			deleted = TranModeDAO.deleteTranModelMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	//ENd code by Bruhan to delete prod brand id on 22/jan/2014
	
	
	public ArrayList getTranModeList(String transportModeID, String plant, String cond) throws Exception{

		ArrayList al = null;
		TransportModeDAO TranModeDAO = new TransportModeDAO();
		TranModeDAO.setmLogger(mLogger);
		try {
			al = TranModeDAO.getTranModelDetails(transportModeID, plant, cond);

		} catch (Exception e) {
			throw e;
		}

		return al;
	}
}
