package com.track.session.po;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBObject;

@Deprecated
public interface PORemote extends EJBObject {
	public int getCountOpenPO4Item(String aItem) throws RemoteException,
			Exception;

	public ArrayList getItemDetails4PONO(String aPONO) throws RemoteException,
			Exception;

	public int getCountOpenItems4PO(String aPONO) throws RemoteException,
			Exception;

	public int getCountOpenItems4Vendor(String aVendNo) throws RemoteException,
			Exception;

	public int getCountOpenPO4Vendor(String aVendNo) throws RemoteException,
			Exception;

	public ArrayList getOpenItemDetails4PO(String aPONO)
			throws RemoteException, Exception;

	public String getPONO4Vendor(String aVendNo) throws RemoteException,
			Exception;

	public ArrayList queryPODET_Col(List listQryFields, Hashtable ht,
			String aExtraCondition) throws RemoteException, Exception;

	public boolean updatePodet(Hashtable htUpdate, Hashtable htCondition)
			throws RemoteException, Exception;

	public boolean updatePODET4Recving(String aPono, String aItem,
			String aLnstatus, float aRecvQty, String aUsrid)
			throws RemoteException, Exception;

	public ArrayList getPOList() throws RemoteException, Exception;

	// public ArrayList getPOItemDetails(String aPONO) throws
	// RemoteException,Exception;
}