package com.track.session.so;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface SOHome extends EJBHome {
	public SORemote create() throws RemoteException, CreateException;
}