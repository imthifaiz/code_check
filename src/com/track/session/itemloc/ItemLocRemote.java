package com.track.session.itemloc;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBObject;

@Deprecated
public interface ItemLocRemote extends EJBObject {
	public boolean insertIntoItemLoc(Hashtable ht) throws RemoteException,
			Exception;

	public boolean deleteItemLoc(Hashtable ht) throws RemoteException,
			Exception;

	public List queryItemLoc(String aItem) throws RemoteException, Exception;

	public List getLocations4Item(String aKeyItem) throws RemoteException,
			Exception;

	public boolean isExistItemAndLoc(String aItem, String aLoc)
			throws RemoteException, Exception;

	public ArrayList getItemLocationList(String aItem, String aLoc)
			throws RemoteException, Exception;
}