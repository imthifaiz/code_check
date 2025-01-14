package com.track.session.tblControl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

@Deprecated
public interface TblControlHome extends EJBHome {
	public TblControlRemote create() throws RemoteException, CreateException;
}