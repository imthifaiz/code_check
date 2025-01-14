package com.track.session.itemmap;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBObject;

@Deprecated
public interface ItemMapRemote extends EJBObject {

	public boolean insertIntoItemMap(Hashtable ht) throws RemoteException,
			Exception;

	public boolean deleteItemMap(Hashtable ht) throws RemoteException,
			Exception;

	public boolean isExistsMapItem(String aMapItem) throws RemoteException,
			Exception;

	public List getMapItems4Key(String aKeyItem) throws RemoteException,
			Exception;

	public ArrayList getKeyItem4MapItem(String aMapItem)
			throws RemoteException, Exception;
}