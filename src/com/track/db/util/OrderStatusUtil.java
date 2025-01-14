package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.dao.OrderStatusDAO;
import com.track.dao.PrdTypeDAO;
import com.track.util.MLogger;

public class OrderStatusUtil {

	public OrderStatusUtil() {

	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertOrdStatusMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			OrderStatusDAO OrdstatusDao = new OrderStatusDAO();
			OrdstatusDao.setmLogger(mLogger);
			inserted = OrdstatusDao.insertIntoOrdStatusMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsOrdStatus(Hashtable htOrdStatus) throws Exception {

		boolean isExists = false;
		OrderStatusDAO OrdstatusDao = new OrderStatusDAO();
		OrdstatusDao.setmLogger(mLogger);
		try {
			isExists = OrdstatusDao.isExists(htOrdStatus);

		} catch (Exception e) {
			throw e;
		}

		return isExists;
	}

	public boolean updateOrdStatusId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			OrderStatusDAO OrdstatusDao = new OrderStatusDAO();
			OrdstatusDao.setmLogger(mLogger);
			update = OrdstatusDao.updateOrdStatusMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deleteOrdStatusId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			OrderStatusDAO OrdstatusDao = new OrderStatusDAO();
			OrdstatusDao.setmLogger(mLogger);
			deleted = OrdstatusDao.deleteOrdStatusId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}

	public ArrayList getOrdStatusList(String aOrdstatusId, String plant, String cond) {

		ArrayList al = null;
		OrderStatusDAO OrdstatusDao = new OrderStatusDAO();
		OrdstatusDao.setmLogger(mLogger);
		try {
			al = OrdstatusDao.getOrdStatusDetails(aOrdstatusId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
}
