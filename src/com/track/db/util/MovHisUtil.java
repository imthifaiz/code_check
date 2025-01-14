package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.MovHisBeanDAO;
import com.track.util.MLogger;

public class MovHisUtil {
	private MovHisBeanDAO movHisBeanDAO = new MovHisBeanDAO();
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.MovHisUtil_PRINTPLANTMASTERLOG;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public MovHisUtil() {
	}

	/**
	 * method : insertIntoRecvHis(Hashtable ht) description : insert new record
	 * into RECVHIS
	 * 
	 * @param ht
	 * @return
	 */
	public boolean insertIntoMovHis(Hashtable ht) {
		boolean inserted = false;
		try {
			movHisBeanDAO.setmLogger(mLogger);
			inserted = movHisBeanDAO.insertIntoMovHis(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}

	/**
	 * @method : getMovList(String aDirType, String aMovtType)
	 * @description : get the item movement details based on the given direction
	 *              type (inbound / outbound) and logs type (stock,
	 *              misc,transfer, sample, consignmnet etc)
	 * @param aDirType
	 * @param aMovtType
	 * @return
	 */
	public List getMovList(String aDirType, String aMovtType) {
		List listMovHis = new ArrayList();
		try {
			movHisBeanDAO.setmLogger(mLogger);
			listMovHis = movHisBeanDAO.qryMovHis(aDirType, aMovtType);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listMovHis;
	}

	/**
	 * 
	 * @param aFDate
	 * @param aTDate
	 * @return
	 * @throws Exception
	 */
	public ArrayList getMovHisList(Hashtable ht, String afrmDate, String atoDate)
			throws Exception {
		ArrayList arrMovList = new ArrayList();
		try {
			movHisBeanDAO.setmLogger(mLogger);

			ArrayList fieldsList = new ArrayList();
			fieldsList.add(IConstants.CREATED_AT);
			fieldsList.add(IConstants.DIRTYPE);
			fieldsList.add(IConstants.MOVTID);
			fieldsList.add(IConstants.PONO);
			fieldsList.add(IConstants.ITEM);
			fieldsList.add(IConstants.LOC);
			fieldsList.add(IConstants.QTY);
			fieldsList.add(IConstants.CREATED_BY);
			arrMovList = movHisBeanDAO.queryMovhis(fieldsList, ht, afrmDate,
					atoDate);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrMovList;
	}

}