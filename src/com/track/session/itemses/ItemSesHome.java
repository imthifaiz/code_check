package com.track.session.itemses;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface ItemSesHome extends EJBHome {
	public ItemSesRemote create() throws RemoteException, CreateException;
}