package com.track.session.itemloc;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface ItemLocHome extends EJBHome {
	public ItemLocRemote create() throws RemoteException, CreateException;
}