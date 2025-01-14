package com.track.session.invses;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.EJBObject;

@Deprecated
public interface InvSesRemote extends EJBObject {

	public boolean insertIntoInvsmt(Hashtable ht) throws RemoteException,
			Exception;

	public boolean updateInvsmt(Hashtable htUpdate, Hashtable htCondition)
			throws RemoteException, Exception;

	public boolean deleteInvsmt(Hashtable ht) throws RemoteException, Exception;

	public ArrayList queryInvsmt(ArrayList listQryFields, Hashtable ht,
			String extracond) throws RemoteException, Exception;

	public int getCountInvmst(Hashtable ht) throws RemoteException, Exception;

	public boolean insertIntoInvsmt(String aItem, String aLoc, String userId)
			throws RemoteException, Exception;

	public boolean isExistsInvMst(String aItem, String aLoc)
			throws RemoteException, Exception;

	public boolean updateQty4PK(String aItem, String aLoc, String aQty,
			String userId, boolean aAddQty) throws RemoteException, Exception;

	public ArrayList getInvDetails(String aItem, String aLoc)
			throws RemoteException, Exception;

	public ArrayList getLocList4Item(String aItem) throws RemoteException,
			Exception;

	public ArrayList getLocList4ItemQty(String aItem) throws RemoteException,
			Exception;
}
