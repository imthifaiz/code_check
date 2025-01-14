package com.track.session.movhis;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBObject;

@Deprecated
public interface MovHisRemote extends EJBObject {
	public boolean insertIntoMovHis(Hashtable ht) throws RemoteException,
			Exception;

	public List qryMovHis(String aDirType, String aMovtType)
			throws RemoteException, Exception;

	public ArrayList queryMovhis(ArrayList listQryFields, Hashtable ht,
			String afrmDate, String atoDate) throws RemoteException, Exception;
}