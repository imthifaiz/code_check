package com.track.session;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface LocMstHome extends EJBHome {
	LocMstRemote create() throws RemoteException, CreateException;
}