package com.track.session.report;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

@Deprecated
public interface ReportSesRemote extends EJBObject {
	public List reportOnCycleCnt2Inv(int i) throws RemoteException, Exception;

	public List reportOnPhysicalStockTake2Inv(int i, String fromLoc,
			String toLoc) throws RemoteException, Exception;

	public boolean updateInventoryWithStockTake(boolean isCycleCount)
			throws RemoteException, Exception;

	public boolean updateInventoryWithStockTake4Loc(String fromLoc, String toLoc)
			throws RemoteException, Exception;

	public boolean canUpdateCCInvenoty(boolean isCycleCount)
			throws RemoteException, Exception;
}
