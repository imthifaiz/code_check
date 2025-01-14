package com.track.session.invses;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface InvSesHome extends EJBHome {
	public InvSesRemote create() throws RemoteException, CreateException;
}