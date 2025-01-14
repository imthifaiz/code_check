package com.track.session.itemmap;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface ItemMapHome extends EJBHome {
	public ItemMapRemote create() throws RemoteException, CreateException;
}