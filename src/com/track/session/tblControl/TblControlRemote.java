package com.track.session.tblControl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.EJBObject;
@Deprecated
public interface TblControlRemote extends EJBObject {
	public boolean insertIntoTblControl(Hashtable ht) throws RemoteException,
			Exception;

	public String getNextSeqNo(String aFunc) throws RemoteException, Exception;

	public ArrayList getTblControlList(String aFunc) throws RemoteException,
			Exception;

	public ArrayList getTblControlDetails(String aFunc) throws RemoteException,
			Exception;

	public boolean updateTblControl(Hashtable htUpdate, Hashtable htCondition)
			throws RemoteException, Exception;

	public boolean deleteTblControl(String aFunc) throws RemoteException,
			Exception;

	public boolean isExistInTblControl(String aFunc) throws RemoteException,
			Exception;
}