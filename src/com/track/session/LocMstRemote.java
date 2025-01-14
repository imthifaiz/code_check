package com.track.session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.EJBObject;

@Deprecated
public interface LocMstRemote extends EJBObject {
	public String sayHello() throws RemoteException, Exception;

	public boolean updateLocMst(Hashtable ht1, Hashtable ht2, String s)
			throws RemoteException, Exception;

	public boolean updateLocMst(String query, Hashtable htCondition,
			String extCondition) throws Exception, RemoteException;

	public boolean insertIntoLocMst(Hashtable ht) throws RemoteException,
			Exception;

	public boolean deleteLoc(String aCustno) throws Exception, RemoteException;

	public boolean deleteLoc(String aCustno, String plant) throws Exception,
			RemoteException;

	public ArrayList getLocDetails(String selectList, Hashtable ht,
			String extCond) throws RemoteException, Exception;

	public ArrayList getAllLocDetails(String selectList, Hashtable ht,
			String extCond) throws RemoteException, Exception;

	public ArrayList getLocList(String selectList, Hashtable ht, String loc)
			throws RemoteException, Exception;

}