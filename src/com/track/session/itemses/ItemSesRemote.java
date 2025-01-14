package com.track.session.itemses;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBObject;

@Deprecated
public interface ItemSesRemote extends EJBObject {
	public boolean insertIntoItemMst(Hashtable ht) throws RemoteException,
			Exception;

	public boolean updateItemMst(Hashtable htUpdate, Hashtable htCondition)
			throws RemoteException, Exception;

	public boolean deleteItemMst(Hashtable ht) throws RemoteException,
			Exception;

	public List queryItemMst(List listQryFields, Hashtable ht)
			throws RemoteException, Exception;

	public int getCountItemMst(Hashtable ht) throws RemoteException, Exception;

	public List queryItemMst(String aItem) throws RemoteException, Exception;

	public List queryItemMst(String aItem, String plant, String cond)
			throws RemoteException, Exception;

	public ArrayList getItemDetails(String aItem) throws RemoteException,
			Exception;

	public ArrayList getStockReorderItemList() throws RemoteException,
			Exception;
}