package com.track.db.util;

import java.util.ArrayList;
import java.util.List;

import com.track.dao.ReportSesBeanDAO;
import com.track.util.MLogger;

public class ReportUtil {
	private static final boolean DEBUG = true;
	private ReportSesBeanDAO reportSesBeanDAO = new ReportSesBeanDAO();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public ReportUtil() {
	}

	public List reportOnPhysicalStockTake2Inv(int i, String fromLoc,
			String toLoc) {
		List listReport = new ArrayList();

		try {
			reportSesBeanDAO.setmLogger(mLogger);
			listReport = reportSesBeanDAO.reportOnPhysicalStockTake2Inv(i,
					fromLoc, toLoc);
		} catch (Exception e) {
		}
		return listReport;
	}

	public List reportOnCycleCnt2Inv(int i) {
		List listReport = new ArrayList();
		try {
			reportSesBeanDAO.setmLogger(mLogger);
			listReport = reportSesBeanDAO.reportOnCycleCnt2Inv(i);

		} catch (Exception e) {
		}
		return listReport;
	}

	public boolean CanUpdateCycleCount(boolean isCycleCount) {
		boolean flg = false;
		try {
			reportSesBeanDAO.setmLogger(mLogger);
			flg = reportSesBeanDAO.canUpdateCCInvenoty(isCycleCount);
		} catch (Exception e) {
			flg = false;
		}
		return flg;

	}

	public boolean updateInventoryWithStockTake(boolean isCycleCount)
			throws Exception {
		boolean result = false;
		try {
			reportSesBeanDAO.setmLogger(mLogger);
			result = reportSesBeanDAO
					.updateInventoryWithStockTake(isCycleCount);
		} catch (Exception e) {
			result = false;
			throw new Exception("Could not able to update inventory");
		}
		return result;
	}

	public boolean updateInventoryWithStockTake4Loc(String fromLoc, String toLoc)
			throws Exception {
		boolean result = false;
		try {
			reportSesBeanDAO.setmLogger(mLogger);
			result = reportSesBeanDAO.updateInventoryWithStockTake4Loc(fromLoc,
					toLoc);
		} catch (Exception e) {
			result = false;

			throw new Exception("Could not able to update inventory");
		}
		return result;
	}

}