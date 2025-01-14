package com.track.session.so;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBObject;

@Deprecated
public interface SORemote extends EJBObject {
	public ArrayList getOpenSO_Col(String aUserId) throws RemoteException,
			Exception;

	public ArrayList getItemDet4SO_Col(String aSono) throws RemoteException,
			Exception;

	public ArrayList getOpenItemDet4SO_Col(String aSono)
			throws RemoteException, Exception;

	public ArrayList getItemDetails4Item(String aSono, String aDolno)
			throws RemoteException, Exception;

	public ArrayList getItemDetails4NoStckItem(String aSono, String aDolno)
			throws RemoteException, Exception;

	public boolean updateQtyIssue(String aSono, String aItem, String aDolno,
			String aLoc, String aPickQty, String aStatus, String aRemarks,
			String aUsrId) throws RemoteException, Exception;

	public boolean isExistItem4SO(String aSono, String aItem)
			throws RemoteException, Exception;

	public boolean isAllItemsPicked4SO(String aSONO) throws RemoteException,
			Exception;

	public boolean updateSO(Hashtable htUpdate, Hashtable htCondition,
			String aExtraCondition) throws RemoteException, Exception;

	public String loadOpenItems4SO(String aSONO) throws RemoteException,
			Exception;

	public ArrayList getSOSummary() throws RemoteException, Exception;

	public ArrayList queryDODET_Col(List listQryFields, Hashtable ht,
			String aExtraCondition) throws RemoteException, Exception;

	public float getAvailableQty(String aItem, String aLoc)
			throws RemoteException, Exception;

	public ArrayList getOpenItemDet4SOSortByLoc_Col(String aSono)
			throws RemoteException, Exception;

	public boolean updateQtyIssue4Reverse(String aSono, String aItem,
			String aLoc, String aPickQty, String aStatus, String aRemarks,
			String aUsrId) throws RemoteException, Exception;

	public boolean updateBlkOrCloseStatus(String aSono, String aStatus,
			String aUserid) throws RemoteException, Exception;

	public String CheckUserAlreadyPicking(String aSONO) throws RemoteException,
			Exception;

	public String loadDolnnoForItem(String aSONO, String aItem)
			throws RemoteException, Exception;

	public String loadItemForDolnno(String aSONO, String aDolno)
			throws RemoteException, Exception;

	public ArrayList getAllItemDetails_Col_PDA(String sSONO)
			throws RemoteException, Exception;
}