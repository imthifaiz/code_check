package com.track.session.po;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface POHome extends EJBHome {
	public PORemote create() throws RemoteException, CreateException;
}